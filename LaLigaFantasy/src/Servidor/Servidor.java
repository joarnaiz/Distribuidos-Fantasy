package Servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Equipo.Equipo;
import Jugador.Jugador;
import Liga.Liga;

public class Servidor {
	public static void main(String[] args) {
		ExecutorService usuarios = Executors.newFixedThreadPool(20);
		try(ServerSocket server = new ServerSocket(7777)){
			while(true) {
				try {
					Socket cliente = server.accept();
					
					Liga liga = new Liga("Distribuidos-Fantasy");
					Usuarios u = new Usuarios(cliente, liga);
					usuarios.execute(u);
					
					
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Jugador> crearJugadores(){
		ArrayList<Jugador> jugadores = new ArrayList<>();
		
		
		return jugadores;
	}

}

class Usuarios implements Runnable{
	
	private Socket cliente;
	private Liga liga;
	
	public Usuarios(Socket s,Liga l) {
		this.cliente = s;
		this.liga=l;	
	}
	
	public void run() {
		try(OutputStream out = this.cliente.getOutputStream();
				InputStream in = this.cliente.getInputStream(); 
				ObjectOutputStream oos = new ObjectOutputStream(out);
				ObjectInputStream ois  = new ObjectInputStream(in);){
			
			String nombreEquipo = (String)ois.readObject();
			Equipo equipoCliente = new Equipo(nombreEquipo);
			this.liga.aniadirEquipo(equipoCliente);
			
			
			
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
