package Mercado;

import java.util.ArrayList;

import Jugador.Jugador;

public class Mercado {

	private List<Jugador> jugadoresDisponibles;
	private List<Jugador> poolJugadores;
	private static final int JUGADORES_MERCADO = 20;
	private final Random random = new Random();
	
	public Mercado(List<Jugador> poolJugadores) {
		this.poolJugadores = new ArrayList<>(poolJugadores);
		this.jugadoresDisponibles = new ArrayList<Jugador>();
		actualizarMercado();
	}
	
	public void actualizarJugadoresDisponibles() {
		
	}
	
	public void actualizarMercado() {
		
		
	}
	
	public void mostrarMercado() {
		System.out.println("--- MERCADO ---");
		if (jugadoresDisponibles.isEmpty()) {
            System.out.println("No hay jugadores en el mercado actualmente.");
            return;
        }
		 List<Jugador> copia = new ArrayList<>(jugadoresDisponibles);
	     Collections.shuffle(copia, random);
	     

		for (int i = 0; i < JUGADORES_MERCADO; i++) {
		    Jugador j = copia.get(i);
		    System.out.println(j);
		}
	}
	
}
