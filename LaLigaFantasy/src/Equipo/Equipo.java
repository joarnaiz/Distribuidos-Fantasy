package Equipo;

import java.io.Serializable;
import java.util.ArrayList;

import Jugador.Jugador;

public class Equipo implements Serializable{
	
	private String nombre;
	private int puntosTotales;
	private ArrayList<Jugador> plantilla;
	private ArrayList<Jugador> alineacion;
	private static final int MAX_JUGADORES_PlANTILLA = 22;
	private static final int TITULARES = 11;
	public double Saldo;
	
	public Equipo(String nom) {
		this.nombre = nom;
		this.plantilla = new ArrayList<>();
        this.alineacion = new ArrayList<>();
        this.Saldo=100000000;
	}
	public String getNombre() {
		return this.nombre;
	}
	
	public int getPuntos() {
		return this.puntosTotales;
	}
	
	public double getSaldo() {
		return this.Saldo;
	}
	public void setSaldo(double d) {
		this.Saldo = d;
	}
	
	public boolean equipoCompleto() {
		return this.plantilla.size() == MAX_JUGADORES_PlANTILLA;
	}
	
	public ArrayList<Jugador> getJugadores(){
		return this.plantilla;
	}
	
	public void setJugadores(ArrayList<Jugador> plantillaInicial) {
		this.plantilla=plantillaInicial;
	}
	
	
	public boolean onceCompleto() {
		return this.alineacion.size() == TITULARES;
	}
	
	public String aniadirJugador(Jugador j) {
		if(!equipoCompleto()) {
			this.plantilla.add(j);
			return "Jugador añadido";
		}else {
			return "Maximo de jugadores alcanzado";
		}
	}
	
	
	
	//Alinear el once inicial
	
	public void mostrarPlantilla() {
		System.out.println("Plantilla de " +this.nombre + " :");
		for(Jugador j : this.plantilla) {
			System.out.println(j);
		}
	}
	
	public void mostrarAlineacion() {
		System.out.println("Once inicial de " +this.nombre + " :");
		for(Jugador j : this.alineacion) {
			System.out.println(j);
		}
	}
	
	@Override
	public boolean equals(Object e) {
	    if (this == e) return true;         
	    if (e == null || getClass() != e.getClass()) return false; 
	    Equipo eq = (Equipo) e;
	    return this.nombre.equals(eq.nombre);                     
	} 
	
	public String toString() {
		return this.nombre + " con " + this.puntosTotales;
	}

}
