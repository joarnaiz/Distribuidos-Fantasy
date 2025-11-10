package Equipo;

import java.util.ArrayList;

import Jugador.Jugador;

public class Equipo {
	
	private String nombre;
	private int puntosTotales;
	private ArrayList<Jugador> plantilla;
	private ArrayList<Jugador> alineacion;
	private static final int MAX_JUGADORES_PlANTILLA = 22;
	private static final int TITULARES = 11;
	
	public Equipo(String nom) {
		this.nombre = nom;
		this.plantilla = new ArrayList<>();
        this.alineacion = new ArrayList<>();
	}
	
	public int getPuntos() {
		return this.puntosTotales;
	}
	
	public boolean equipoCompleto() {
		return this.plantilla.size() == MAX_JUGADORES_PlANTILLA;
	}
	
	public boolean onceCompleto() {
		return this.alineacion.size() == TITULARES;
	}
	
	public void aniadirJugador(Jugador j) {
		if(!equipoCompleto()) {
			this.plantilla.add(j);
		}else {
			System.out.println("Maximo de jugadores alcanzado");
		}
	}
	
	
	//Alinear el once inicial
	
	public void mostrarPlantilla() {
		System.out.println("Plantilla de " +this.nombre + " :");
		for(Jugador j : this.plantilla) {
			j.toString();
		}
	}
	
	public void mostrarAlineacion() {
		System.out.println("Once inicial de " +this.nombre + " :");
		for(Jugador j : this.alineacion) {
			j.toString();
		}
	}
	
	@Override
	public boolean equals(Object e) {
	    if (this == e) return true;         
	    if (e == null || getClass() != e.getClass()) return false; 
	    Equipo eq = (Equipo) e;
	    return this.nombre == eq.nombre;                     
	} 
	
	public String toString() {
		return this.nombre + " con " + this.puntosTotales;
	}

}
