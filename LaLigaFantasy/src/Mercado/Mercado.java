package Mercado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Jugador.Jugador;

public class Mercado {

	private List<Jugador> jugadoresDisponibles;
	private List<Jugador> jugadoresTotales;
	private static final int JUGADORES_MERCADO = 20;
	private final Random random = new Random();
	
	public Mercado(List<Jugador> poolJugadores) {
		this.jugadoresTotales = new ArrayList<>(poolJugadores);
		this.jugadoresDisponibles = new ArrayList<Jugador>();
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
		 for (int i = 0; i < jugadoresDisponibles.size(); i++) {
			 System.out.println(jugadoresDisponibles.get(i));
		 }	
	}
	
	public Jugador ficharJugador(String id) {
		return null;
	}
	
}
