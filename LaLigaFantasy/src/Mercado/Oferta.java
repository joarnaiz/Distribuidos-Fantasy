package Mercado;

import java.io.Serializable;

import Equipo.Equipo;
import Jugador.Jugador;

public class Oferta implements Serializable{
	private Equipo comprador;
	private Equipo vendedor;
	private Jugador fichaje;
	private double cantidad;
	
	public Oferta(Equipo equipoCompra, Equipo equipoVenta, Jugador jugadorFichaje, double cantidadTraspaso) {
		this.comprador = equipoCompra;
		this.vendedor = equipoVenta;
		this.fichaje = jugadorFichaje;
		this.cantidad = cantidadTraspaso;
	}
	
	public Equipo getComprador() {
		return this.comprador;
	}
	
	public Equipo getVendedor() {
		return this.vendedor;
	}
	
	public Jugador getJugadorAFichar() {
		return this.fichaje;
	}
	
	public double getCantidadTraspaso() {
		return this.cantidad;
	}
	
	public String toString() {
		return "Oferta de " + comprador.getNombre() + " por " + fichaje.getNombre() + " con valor de " + this.cantidad + " €";
	}
}