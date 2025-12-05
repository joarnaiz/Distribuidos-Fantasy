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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import Equipo.Equipo;
import Jugador.Jugador;
import Liga.Liga;
import Mercado.Mercado;
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
					t.scheduleAtFixedRate(new Jornada(todosJugadores,liga,t) ,10*60*1000,20*60*1000);
					
					
					
					
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
		        		oos.writeObject(ois);
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
		        		
		        }
	        }
	        
	        
			
			
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

class ActualizaMercado extends TimerTask{

	private Mercado mercado;
	
	public ActualizaMercado(Mercado m) {
		this.mercado=m;
	}
	@Override
	public void run() {
		this.mercado.comprobarPujas();
		this.mercado.actualizarMercado();
		
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
		//Actualizamos tanto el precio como los puntos de los jugadores.
		//Actualizamos las clasificaciones totales y de la jornada.
		
	}
	
}