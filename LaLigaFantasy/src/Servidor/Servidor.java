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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import Equipo.Equipo;
import GeneradorJugadores.Generador;
import Jugador.Jugador;
import Liga.Liga;

public class Servidor {
	
	private static Liga liga= new Liga("Distribuidos-Fantasy");
	
	public static void main(String[] args) {
		ExecutorService usuarios = Executors.newFixedThreadPool(20);
		
		Generador g = new Generador();
		liga.setJugadoresDisponibles(g.generar(100));
		
		Semaphore semaforoAdquirirJugaores = new Semaphore(1);
		
		try(ServerSocket server = new ServerSocket(7777)){	
			while(true) {
				try {		
					Socket cliente = server.accept();
					
					Usuarios u = new Usuarios(cliente, liga,semaforoAdquirirJugaores);
					usuarios.execute(u);
					
					
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
			
			String nombreEquipo = (String)ois.readObject();
			this.equipo = new Equipo(nombreEquipo);
			
			this.liga.aniadirEquipo(this.equipo);
			
			this.semaforo.acquire();
			
			Collections.shuffle(this.liga.getJugadoresDisponibles());
			
			Iterator<Jugador> it = this.liga.getJugadoresDisponibles().iterator();
	        while (it.hasNext()) {
	            Jugador j = it.next();
	            if (j.getPosicion().toString().equalsIgnoreCase("Portero")) {
	                this.equipo.aniadirJugador(j);
	                it.remove(); 
	                break;
	            }
	        }
	        
	        int contador = 0;
	        it = this.liga.getJugadoresDisponibles().iterator(); 
	        while (it.hasNext() && contador < 10) {
	            Jugador j = it.next();
	            if (!j.getPosicion().toString().equalsIgnoreCase("Portero")) {
	                this.equipo.aniadirJugador(j);
	                it.remove();
	                contador++;
	            }
	        }
	        
	        this.semaforo.release();
	        
	        for(Jugador j : this.equipo.getJugadores()) {
    			this.equipo.getAlineacion().aniadirJugador(j);
    		}
	        
	        
	        boolean salir = false;
	        while(!salir) {
	        	String opcion = (String)ois.readObject();        
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
		        		
		        		
			        //----------LIGA---------- 	
		        	case "Ver clasificacion":
		        		oos.writeObject(this.liga);
		        		oos.reset();
		        		oos.flush();
		        		break;	
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
