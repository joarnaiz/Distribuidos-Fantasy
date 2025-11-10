package Mercado;

import java.util.ArrayList;

import Jugador.Jugador;

public class Mercado {

	private ArrayList<Jugador> jugadoresDisponibles;
	
	public Mercado() {
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
