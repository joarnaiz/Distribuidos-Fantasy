package Mercado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Equipo.Equipo;
import Jugador.Jugador;
import Mercado.Puja;

public class Mercado {

	private List<Jugador> jugadoresDisponibles;
	private List<Jugador> jugadoresTotales;
	private static final int JUGADORES_MERCADO = 20;
	private final Random random = new Random();
	private List<Puja> listaPujas;
	
	public Mercado(List<Jugador> poolJugadores) {
		this.jugadoresTotales = new ArrayList<>(poolJugadores);
		this.jugadoresDisponibles = new ArrayList<Jugador>();
		this.listaPujas = new ArrayList<>();
		actualizarMercado();
	}
	
	public void actualizarMercado() {
		this.jugadoresDisponibles.clear();
		
		List<Jugador> copia = new ArrayList<>(this.jugadoresTotales);
		Collections.shuffle(copia, random);
		
		int limite = Math.min(copia.size(), JUGADORES_MERCADO); // Por si hay menos de 20 futbolistas disponibles
		for (int i = 0; i < limite; i++) {
			this.jugadoresDisponibles.add(copia.get(i));
		}
	}
	
	public void mostrarMercado() {
		System.out.println("--- MERCADO ---");
		if (jugadoresDisponibles.isEmpty()) {
            System.out.println("No hay jugadores en el mercado actualmente.");
            return;
        }
		 for (int i = 1; i <= jugadoresDisponibles.size(); i++) {
			 Jugador j = jugadoresDisponibles.get(i-1);
			 
			 System.out.println(i + ". " + j.toString());
		 }	
	}
	
	public boolean pujarJugador(int opcion, Equipo equipo, double tuPuja) {
		if (opcion < 1 || opcion > jugadoresDisponibles.size() || tuPuja < jugadoresDisponibles.get(opcion-1).getValor()) {
			System.out.println("Error al pujar por el jugador");
			return  false;
		}
		
		Jugador fichaje = jugadoresDisponibles.get(opcion-1);
		
		if (equipo.getSaldo() < tuPuja) {
			System.out.println("El jugador no tiene dinero");
			return false;
		}
		
		equipo.setSaldo(-tuPuja);
		Puja p = new Puja(equipo, fichaje, tuPuja);
		listaPujas.add(p);
		
		// En caso de que la puja sea igual, gana el jugador que primero pujó
		System.out.println("Has pujado por: " + fichaje.getNombre());
		return true;
	}
	
	public void comprobarPujas() {
		System.out.println("Comprobando pujas...");
		if (listaPujas.isEmpty()) {
			System.out.println("No ha habido pujas en este mercado");
			return;
		}
		
		List<Jugador> vendidos = new ArrayList<>();
		
		// Para cada jugador del mercado comprobamos quien ha pujado mas alto
		for (Jugador j : jugadoresDisponibles) {
			Puja mejorPuja = null;
			double cantidadPujaMaxima = 0.0;
			List<Puja> pujasJugador = new ArrayList<>(); // Para guardar todas las pujas hechas por ese jugador
			
			for (Puja p : listaPujas) {
				if (p.getJugadorPuja().equals(j)) {
					pujasJugador.add(p);
				}
				
				if(p.getCantidadPuja() > cantidadPujaMaxima) {
					cantidadPujaMaxima = p.getCantidadPuja();
					mejorPuja = p;
				}
			}
			
			if (mejorPuja != null) {
				Equipo ganadorPuja = mejorPuja.getEquipoPuja();
				ganadorPuja.aniadirJugador(j);
				System.out.println("El jugador " + ganadorPuja.getNombre() + " ha fichado a " + j.getNombre() + "por " + mejorPuja.getCantidadPuja() + " €");
				vendidos.add(j);
				
				for (Puja p : pujasJugador) {
					if (p != mejorPuja) {
						p.getEquipoPuja().setSaldo(p.getCantidadPuja());
					}
				}
			}
		}
		
		jugadoresDisponibles.removeAll(vendidos);
		listaPujas.clear();
	}
}
