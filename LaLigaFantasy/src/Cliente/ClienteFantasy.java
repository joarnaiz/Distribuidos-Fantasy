package Cliente;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import Equipo.Equipo;
import Liga.Liga;
import Mercado.Mercado;

public class ClienteFantasy {

	public static void main(String[] args) {
		
		try(Socket socket = new Socket("localhost",7777);
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
				ObjectInputStream ois  = new ObjectInputStream(in); 
				ObjectOutputStream oos = new ObjectOutputStream(out)){
			
			
			
			
			System.out.println("Bienvenido! Escribe el nombre de tu Equipo Fantasy");
			Scanner nE = new Scanner(System.in);
			String nombre = nE.nextLine();
			
			oos.writeObject(nombre);
			oos.flush();
			
			//Mandamos al servidor el nombre del equipo, este lo guarda, nos asigna a un equipo, nos mete en la liga...
			//Y ya entramos al menu principal para gestionar nuestro equipo
			
			boolean salir=false;
			int opcion=0;
			Scanner s = new Scanner(System.in);
		
			
			while(!salir) {
				menuPrincipal();
				opcion = s.nextInt();
				s.nextLine();
				
				switch(opcion) {
					case 1:
						menuEquipo(ois,oos);
						break;
					case 2:
						menuLiga(ois,oos);
						break;
					case 3:
						menuMercado(ois,oos);
						break;
					case 4:
						//Enviar al servidor que ha abandonado la liga para que borre todos sus datos.
						salir=true;
						break;
					default:
						System.out.println("Opción incorrecta");
				}
				
			}
			
			s.close();
			nE.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	private static void menuPrincipal() {
		System.out.println("==============================================");
		System.out.println("     DISTRIBUIDOS FANTASY--MENU PRINCIPAL"     );
		System.out.println("==============================================");
		System.out.println();
		System.out.println("---------------------------");
		System.out.println("1. MI EQUIPO");
		System.out.println("2. Ver mi Liga");
		System.out.println("3. Mercado");
		System.out.println("4.Abandonar la liga");
		System.out.println("---------------------------");
		System.out.println();
	}
	
	private static void menuEquipo(ObjectInputStream ois,ObjectOutputStream oos) {  
        boolean salir=false;
        Scanner s = new Scanner(System.in);
        while(!salir) {    
        	System.out.println();
        	System.out.println("------ SECCION Equipo ------");
            System.out.println("1. Ver Plantilla");
            System.out.println("2. Ver once");
            System.out.println("3. Alinear jugadores");
            System.out.println("4. Sustituir jugadores");
            System.out.println("5. Economía del club");
            System.out.println("6. Volver al Menu Principal");
            System.out.println("-----------------------------");
            System.out.println();
            
            int opcion = s.nextInt();
            s.nextLine();
            switch(opcion) {
	            case 1:
	            	try {

	                    String m = "Ver plantilla";
	                    oos.writeObject(m);
	                    oos.flush();

	                    Equipo e = (Equipo) ois.readObject();
	                    e.mostrarPlantilla();
	            	}catch(IOException e) {
	                    e.printStackTrace();
	                }catch (ClassNotFoundException e1) {
	                    e1.printStackTrace();
	                }
	            	
	            	break;
	            case 2:
	            	
	            	try {

	                    String m = "Ver once";
	                    oos.writeObject(m);
	                    oos.flush();

	                    Equipo e = (Equipo)ois.readObject();
	                    e.mostrarAlineacion();
	                    
	            	}catch(IOException e) {
	                    e.printStackTrace();
	                }catch (ClassNotFoundException e1) {
	                    e1.printStackTrace();
	                }
	            	
	            	break;
	            case 3:
	            	try {
	            		String m = "Alinear jugador";
		            	oos.writeObject(m);
		            	oos.flush();
	                    
		            	System.out.println("Elige el id del jugador que quieres añadir");
		            	int jugador = s.nextInt();
		            	
		            	oos.writeObject(jugador);
		            	oos.flush();
		            	
		            	String respuesta = ois.readObject().toString();
		            	System.out.println(respuesta);
		            	
	            	}catch(IOException e) {
	            		e.printStackTrace();
	            	} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	break;
	            case 4:
	            	try {
	            		String m = "Sustituir jugador";
		            	oos.writeObject(m);
		            	oos.flush();
	                    
		            	System.out.println("Elige el id del jugador que quieres cambiar");
		            	int jSale = s.nextInt();
		            	System.out.println("Elige el id del jugador que quieres añadir a tu once");
		            	int jEntra = s.nextInt();
		            	s.nextLine();
		            	
		            	oos.writeObject(jSale);
		            	oos.writeObject(jEntra);
		            	oos.flush();
		            	
		            	String respuesta = ois.readObject().toString();
		            	System.out.println(respuesta);
		            	
	            	}catch(IOException e) {
	            		e.printStackTrace();
	            	} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	break;
	            case 5:
	            	try {
	            		String m = "Eco club";
		            	oos.writeObject(m);
		            	oos.flush();
		            	
		            	String respuesta = ois.readObject().toString();
		            	System.out.println(respuesta);
	            	} catch(IOException e) {
	            		e.printStackTrace();
	            	} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	break;
	            case 6:
	            	salir = true;
	            	break;
	            default:
	            	System.out.println("Numero Incorrecto");
            }
        }        
	}
	
	private static void menuLiga(ObjectInputStream ois,ObjectOutputStream oos) {        
        boolean salir=false;
        Scanner s = new Scanner(System.in);
        while(!salir) { 	
        	System.out.println();
        	System.out.println("--- SECCION LIGA ---");
            System.out.println("1. Ver Clasificacion General");
            System.out.println("2. Ver Puntos de una  Jornada");
            System.out.println("3. Ojear Equipo");
            System.out.println("4. Volver al Menu Principal");
            System.out.println();
            int opcion = s.nextInt();
            s.nextLine();
            switch(opcion) {
	            case 1:
				try {
					String m = "Ver clasificacion";
					oos.writeObject(m);
					oos.flush();
					
					Liga l = (Liga) ois.readObject();
					l.verClasificacion();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            	
	            	break;
	            case 2:
	            	try {
	            		String m = "Ver clasificacion";
						oos.writeObject(m);
						oos.flush();
						System.out.println("Que jornada quieres ver");
						int jornada = s.nextInt();
						s.nextLine();
						
						Liga l = (Liga) ois.readObject();
						l.verClasificacionJornada(jornada);
	            	}catch(IOException e) {
	            		e.printStackTrace();
	            	} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
					
	            	break;
	            case 3:
	            	try {
						String m = "Ojear Equipo";
						oos.writeObject(m);
						oos.flush();
						
						System.out.println("Elige un equipo para ojear (Escribe el nombre)");
						String equipoOjear = s.nextLine();
						oos.writeObject(equipoOjear);
						oos.flush();
						
						Equipo e = (Equipo) ois.readObject();
						
						if (e!=null) {		
							e.mostrarPlantilla();
						} else {
							System.out.println("Ese equipo no se encuentra en la liga");
						}	
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	break;
	            case 4:
	            	salir = true;
	            	break;
	            default:
	            	System.out.println("Numero Incorrecto");
            }
            
            
        }
	}
	
	private static void menuMercado(ObjectInputStream ois,ObjectOutputStream oos) {        
        boolean salir = false;
    	Scanner s = new Scanner(System.in);
        while(!salir) {
        	System.out.println();
        	System.out.println("--- SECCION MERCADO ---");
            System.out.println("1. Ver Jugadores en Venta");
            System.out.println("2. Pujar Jugador");
            System.out.println("3. Poner a la venta un jugador");
            System.out.println("4. Volver al Menu Principal");
            System.out.println();
            int opcion = s.nextInt();
            s.nextLine();
            switch(opcion) {
	            case 1:
	            	try {
	            		String m = "Ver jugadores en Venta";
	            		oos.writeObject(m);
						oos.flush();
						
						Mercado mercado = (Mercado) ois.readObject();
						mercado.mostrarMercado();
	            	} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	            	break;
	            case 2:
	            	try {
	            		String m = "Pujar jugador";
	            		oos.writeObject(m);
	            		oos.flush();
	            		
	            		System.out.println("Introduce el número en la lista del jugador a fichar");
	            		int numJugador = s.nextInt();
	            		s.nextLine();
	            		
	            		System.out.println("Introduce la cantidad de tu puja");
	            		String puja = s.nextLine();
	            		double cantidad = Double.parseDouble(puja);
	            		
	            		oos.writeInt(numJugador);
	            		oos.writeDouble(cantidad);
	            		oos.flush();
	            		
	            		String respuesta = ois.readObject().toString();
	            		System.out.println(respuesta);
	            		
	            	} catch (IOException e) {
	            		e.printStackTrace();
	            	} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	break;
	            case 3: 
	            	break;
	            case 4: 
	            	salir = true;
	            	break;
	            default:
	            	System.out.println("Numero Incorrecto");
            }
        }
	}
}
