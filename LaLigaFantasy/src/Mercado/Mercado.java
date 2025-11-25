package Mercado;

import java.util.ArrayList;

import Jugador.Jugador;

public class Mercado {

	private List<Jugador> jugadoresDisponibles;
	private List<Jugador> poolJugadores;
	
	public Mercado(List<Jugador> poolJugadores) {
		this.poolJugadores = new ArrayList<>(poolJugadores);
		this.jugadoresDisponibles = new ArrayList<Jugador>();
	}
	
	public void actualizarJugadoresDisponibles() {
		
	}
	
	public void actualizarMercado() {
		
	}
	
	public void mostrarMercado() {
		System.out.println("Mercado:");
		for(Jugador j : this.jugadoresDisponibles) {
			j.toString();
		}
	}
	
}
