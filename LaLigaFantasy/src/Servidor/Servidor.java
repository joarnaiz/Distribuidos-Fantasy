package Servidor;

import java.io.IOException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import Equipo.Equipo;
import Jugador.Jugador;
import Liga.Liga;
import Mercado.Mercado;
import Mercado.Oferta;
import ObtenerJugadores.ObtenerJugadores;

public class Servidor {
	
	private static Liga liga= new Liga("Distribuidos-Fantasy");
	private static ArrayList<Jugador> todosJugadores=new ArrayList<>();
	
	public static void main(String[] args) {
		ExecutorService usuarios = Executors.newFixedThreadPool(20);
		
		ObtenerJugadores oj = new ObtenerJugadores();
		todosJugadores = oj.getListaJugadores();
		liga.setJugadoresLibres(todosJugadores);
		
		Semaphore semaforoAdquirirJugaores = new Semaphore(1);
		
		try(ServerSocket server = new ServerSocket(7777)){	
			while(true) {
				try {		
					Socket cliente = server.accept();
					
					Usuarios u = new Usuarios(cliente, liga,semaforoAdquirirJugaores);
					usuarios.execute(u);
					
					//Hilo que controla la actualizacion del mercado.
					Timer t = new Timer();
					t.scheduleAtFixedRate(new ActualizaMercado(liga.getMercado()) ,0,10*60*1000);
					
					//Hilo que controla las jornadas
					t.scheduleAtFixedRate(new Jornada(todosJugadores,liga,t),10*60*1000,30*60*1000);
						
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

class Usuarios implements Runnable{
	
	private Socket cliente;
	private Liga liga;
	private Equipo equipo;
	private Semaphore semaforo;
	
	
	public Usuarios(Socket s,Liga l,Semaphore sem) {
		this.cliente = s;
		this.liga=l;	
		this.semaforo=sem;
	}
	
	public void run() {
		try(OutputStream out = this.cliente.getOutputStream();
				InputStream in = this.cliente.getInputStream(); 
				ObjectOutputStream oos = new ObjectOutputStream(out);
				ObjectInputStream ois  = new ObjectInputStream(in);){
			
			String nombreEquipo = ois.readObject().toString();
			this.equipo = new Equipo(nombreEquipo);
			this.liga.aniadirEquipo(this.equipo);
			
			try {
				this.semaforo.acquire();
				
				Collections.shuffle(this.liga.getJugadoresLibres());
				
				Iterator<Jugador> it = this.liga.getJugadoresLibres().iterator();
		        while (it.hasNext()) {
		        	
		            Jugador j = it.next();
		            if (j.getPosicion().toString().equalsIgnoreCase("Portero")) {
		                this.equipo.aniadirJugador(j);
		                it.remove(); 
		                break;
		            }
		        }
		        
		        int contador = 0;
		        it = this.liga.getJugadoresLibres().iterator(); 
		        while (it.hasNext() && contador < 10) {
		        	
		            Jugador j = it.next();
		            if (!j.getPosicion().toString().equalsIgnoreCase("Portero")) {
		                this.equipo.aniadirJugador(j);
		                it.remove();
		                contador++;
		            }
		        }
		        
		        
			}finally {
				this.semaforo.release();
			}
	        
	        for(Jugador j : this.equipo.getJugadores()) {
    			this.equipo.getAlineacion().aniadirJugador(j);
    		}
	        
	        boolean salir = false;
	        while(!salir) {
	        	String opcion = ois.readObject().toString();        
		        switch(opcion) {
		        
		        	case "DIMITO":
		        		this.liga.dimision(this.equipo);
		        		System.out.println(this.equipo + " ha abandonado la liga");
		        		salir=true;
		        		break;
		        
		        		
		        	//---------EQUIPO-----------
		        	case "Ver plantilla":
		        		oos.writeObject(this.equipo);
		        		oos.reset();
			        	oos.flush();
			        	break;
			        	
		        	case "Ver once":
		        		oos.writeObject(this.equipo);
		        		oos.reset();
			        	oos.flush();
			        	break;
			        	
		        	case "Alinear jugador":
		        		int idJugador =(Integer)ois.readObject();
		        		String aniadido=this.equipo.alinearJugador(idJugador);
		        		
		        		oos.writeObject(aniadido);
			        	oos.flush();
		        		break;
		        		
		        	case "Sustituir jugador":
		        		int idSale = (Integer)ois.readObject();
		        		int idEntra = (Integer)ois.readObject();
			        	String resultado=this.equipo.sustituirJugador(idSale, idEntra);
			        	
			        	oos.writeObject(resultado);
			        	oos.flush();
		        		break;
		        		
		        	case "Eco club":
		        		double valorEq = 0.0;
		        		for (Jugador j : this.equipo.getJugadores()) {
		        			valorEq += j.getValor();
		        		}
		        		String saldoEquipo = String.format("Tu saldo es %.2f €", this.equipo.getSaldo());
		        		String valorEquipo = String.format("y tu equipo tiene un valor de %.2f €", valorEq);
		        		String eco = saldoEquipo + " " + valorEquipo;
		        		
		        		oos.writeObject(eco);
		        		oos.flush();
		        		break;
		        		
		        		
			        //----------LIGA---------- 	
		        	case "Ver clasificacion":
		        		oos.writeObject(this.liga);
		        		oos.reset();
		        		oos.flush();
		        		break;	
		        	
		        	case "Ojear Equipo":
		        		
		        		String nombreEq = ois.readObject().toString();
		        		Equipo equipo = this.liga.getEquipoPorNombre(nombreEq);
		        		
		        		oos.writeObject(equipo);
		        		oos.reset();
		        		oos.flush();
		        		break;
		        		
		        		
		        	//----------MERCADO----------
		        	case "Ver jugadores en Venta":
		        		oos.writeObject(this.liga.getMercado());
		        		oos.reset();
		        		oos.flush();
		        		break;
		        		
		        	case "Pujar jugador":
		        		try {
		        			int numJugador = ois.readInt();
		        			double cantidad = ois.readDouble();
		        			
		        			String respuesta = this.liga.getMercado().pujarJugador(numJugador, this.equipo, cantidad);
		        			oos.writeObject(respuesta);
		        			oos.flush();
		        		}catch (IOException e) {
		        			e.printStackTrace();
		        		}
		        		break;
		        	
		        	case "Vender jugador":
		        		try {
		        			int idJugadorVenta = (Integer) ois.readObject();
		        			String venta = this.liga.getMercado().venderJugador(idJugadorVenta, this.equipo);
		        			
		        			oos.writeObject(venta);
		        			oos.flush();
		        			
		        		}catch (Exception e) {
		        	        e.printStackTrace();
		        	    }
		        		break;
		        		
		        	case "Hacer oferta":
		        		try {
		        			String nombEquipo = ois.readObject().toString();
			        		int idJug = Integer.parseInt(ois.readObject().toString());
			        		double cant = Double.parseDouble(ois.readObject().toString());
			        		
			        		String msg = "";
			        		
			        		if(cant > this.equipo.getSaldo()) {
			        			msg = "Saldo insuficiente";
			        		}else {
			        			Equipo rival = this.liga.getEquipoPorNombre(nombEquipo);
				        		
				        		if(rival!=null) {
				        			if (rival.getNombre().equals(this.equipo.getNombre())) {
				        				msg = "No puedes hacerte ofertas a ti mismo");
				        			}else {
				        				Jugador jugadorOferta =null;
					        			for(Jugador j : rival.getJugadores()) {
					        				if(j.getId()==idJug){
					        					jugadorOferta = j;
					        					break;
					        				}
					        			}
					        			
					        			if(jugadorOferta!=null) {
					        				Oferta o = new Oferta(this.equipo,rival,jugadorOferta,cant);
					        				rival.recibirOferta(o);
					        				msg="Oferta enviada";
					        				
					        			}else {
					        				msg="Este equipo no tiene a este jugador";
					        			}
				        			}
				        		}else {
				        			msg="Este equipo no esta en la liga";
				        		}
			        		}
	
			        		oos.writeObject(msg);
			        		oos.flush();
			        		
		        		}catch(IOException e) {
		        			e.printStackTrace();
		        			
		        		}
		        		break;
		        	
		        	case "Buzon de ofertas":
		        		oos.writeObject(this.equipo);
		        		oos.reset();
		        		oos.flush();
		        		break;
		        		
		        	case "Aceptar oferta":
		        		Oferta o = (Oferta) ois.readObject();
		        		
		        		String respuesta = this.liga.getMercado().aceptarOferta(o);
		        		oos.writeObject(respuesta);
		        		oos.flush();
		        		break;
		        		
		        	case "Rechazar oferta":
		        		Oferta of = (Oferta) ois.readObject();
		        		
		        		String res = this.liga.getMercado().rechazarOferta(of);
		        		oos.writeObject(res);
		        		oos.flush();
		        		break;
		        }
	        }
	        
		}catch(IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			
		}finally {
			try {
				this.cliente.close();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}
		}
	}
}


class ActualizaMercado extends TimerTask{

	private Mercado mercado;
	
	public ActualizaMercado(Mercado m) {
		this.mercado=m;
	}
	
	@Override
	public void run() { //Para que otros hilos no puedan hacer operaciones de mercado mientras este cambia
		synchronized(this.mercado) {
			this.mercado.comprobarPujas();
			this.mercado.actualizarMercado();
		}		
	}
	
}


class Jornada extends TimerTask{
	
	private ArrayList<Jugador> Jugadores;
	private Liga liga;
	private Timer timer;
	private final int NUMERO_JORNADAS = 32;
	private int jornadaActual;
	
	public Jornada(ArrayList<Jugador> j,Liga l ,Timer t) {
		this.Jugadores=j;
		this.liga=l;
		this.timer=t;
		this.jornadaActual=1;
	}
	
	public void run() {
		Random r = new Random();
		
		if(this.jornadaActual>NUMERO_JORNADAS) {
			this.timer.cancel();
			return;
		}
		
		synchronized(this.liga) { //Para que mientras se actualizan las clasificaciones y asi no ver otras versiones	
			for(Jugador j : this.liga.getJugadoresLibres()) {
				int n = r.nextInt(-5, 15);
				j.setPuntosJornada(jornadaActual,n);
				j.aniadirValor(n*100000.0);
			}
			
			
			for(Equipo e : this.liga.getClasificacion()) {
				int puntosE=0;
				for(Jugador j : e.getAlineacion().getJugadoresDeCampo()) {
					int n = r.nextInt(-5, 15);
					j.setPuntosJornada(jornadaActual,n);
					j.aniadirValor(n*100000.0);
					
					puntosE+=n;
				}
				e.setPuntosJornada(jornadaActual,puntosE);
			}
			
			this.liga.actualizarClasificacion();
			this.liga.actualizarClasificacionJornada(jornadaActual);
		}
		
		this.jornadaActual++;
	}
}