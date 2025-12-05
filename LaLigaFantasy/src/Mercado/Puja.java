package Mercado;

import java.io.Serializable;

import Equipo.Equipo;
import Jugador.Jugador;

public class Puja implements Serializable{
	private Equipo equipoPuja;
	private Jugador jugadorPuja;
	private double cantidadPuja;
	
	public Puja(Equipo equipo, Jugador jugador, double cantidadPuja) {
		this.equipoPuja = equipo;
		this.jugadorPuja = jugador;
		this.cantidadPuja = cantidadPuja;
	}
	
	public Equipo getEquipoPuja() {
		return this.equipoPuja;
	}
	
	public Jugador getJugadorPuja(){
		return this.jugadorPuja;
	}
	
	public double getCantidadPuja() {
		return this.cantidadPuja;
	}
}
