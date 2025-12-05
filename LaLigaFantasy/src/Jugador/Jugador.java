package Jugador;

import java.io.Serializable;
import java.util.HashMap;

public class Jugador implements Serializable{

	private static int contadorId = 0;
	private int id;
	private String nombre;
	private String equipo;
	private Posicion posicion;
	private double valor;
	private int puntos;
	private HashMap<Integer,Integer> puntosJornada;
	
	public Jugador(String nomb, Posicion pos, String eq, double val) {
		
		this.id = contadorId++;
		this.nombre = nomb;
		this.equipo = eq;
		this.valor = val;
		this.puntos = 0;
		this.posicion = pos;
		this.puntosJornada = new HashMap<Integer,Integer>();
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getEquipo() {
		return this.equipo;
	}
	
	public Posicion getPosicion() {
		return this.posicion;
	}
	
	public int getPuntos() {
		return this.puntos;
	}
	
	public int getId() {
		return this.id;
	}
	
	public double getValor() {
		return this.valor;
	}
	
	public void setPuntosJornada(int jornada,int puntos) {
		this.puntosJornada.put(jornada, puntos);
		this.puntos+=puntos;
	}
	public int getPuntosJornada(int jornada) {
		return this.puntosJornada.get(jornada);
	}
	
	public void aniadirValor(double valor) {
		this.valor+=valor;
		if(this.valor<500000.0) {
			this.valor=500000.0;
		}
	}
	
	@Override
	public boolean equals(Object j) {
	    if (this == j) return true;         
	    if (j == null || getClass() != j.getClass()) return false; 
	    Jugador jug = (Jugador) j;
	    return this.id == jug.id;                     
	}
	

	public String toString() {
		String stringJugador = "Nombre: " + this.nombre + "  Equipo: " + this.equipo + "  Posicion: " + this.posicion + "  Valor: " + this.valor
				+ "  Puntos: " + this.puntos + "  ID: " + this.id;
		return stringJugador;
	}
	
	public boolean esPortero() {
        return this.posicion == Posicion.PORTERO;
    }

    public boolean esJugadorDeCampo() {
        return this.posicion == Posicion.DECAMPO;
    }

}


