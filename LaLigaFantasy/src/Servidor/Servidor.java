package Servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Equipo.Equipo;

public class Servidor {
	public static void main(String[] args) {
		ExecutorService usuarios = Executors.newFixedThreadPool(20);
		try(ServerSocket server = new ServerSocket(7777)){
			while(true) {
				try {
					Socket cliente = server.accept();
					
					Usuarios u = new Usuarios(cliente);
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
	
	public Usuarios(Socket s) {
		this.cliente = s;
		
	}
	
	public void run() {
		try(OutputStream out = this.cliente.getOutputStream();
				InputStream in = this.cliente.getInputStream(); 
				ObjectOutputStream oos = new ObjectOutputStream(out);
				ObjectInputStream ois  = new ObjectInputStream(in);){
			
			String nombreEquipo = (String)ois.readObject();
			System.out.println(nombreEquipo);
			
		}catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
