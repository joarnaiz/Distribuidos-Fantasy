package Liga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import Equipo.Equipo;
import Jugador.Jugador;
import Mercado.Mercado;

public class Liga implements Serializable{
	
	private String nombre;
	private static final int MAX_EQUIPOS = 20;
	private ArrayList<Equipo> clasificacion;
	private ArrayList<Jugador> jugadoresLibres;
	private Mercado mercado;
	
	public Liga(String nomb) {
		this.nombre=nomb;
		this.clasificacion = new ArrayList<Equipo>();
		this.jugadoresLibres = new ArrayList<Jugador>();
	}
	
	public ArrayList<Jugador> getJugadoresLibres(){
		return this.jugadoresLibres;
	}
	
	public void setJugadoresLibres(ArrayList<Jugador> libres) {
		this.jugadoresLibres=libres;
		
		if (this.mercado == null) {
			this.mercado = new Mercado(libres);
		}
	}
	
	public boolean ligaCompleta() {
		return this.clasificacion.size()==MAX_EQUIPOS;
	}
	
	public synchronized String aniadirEquipo(Equipo e) {
		if(!ligaCompleta()) {
			for(Equipo eq : this.clasificacion) {
				if(eq.getNombre().equalsIgnoreCase(e.getNombre())) {
					return "Equipo existente";
				}
			}
		}
		else {
			return "Liga completa";
		}
		this.clasificacion.add(e);
		return "Se ha unido a la Liga";
	}
	
	
	public void actualizarClasificacion() {
		this.clasificacion.sort(Comparator.comparingInt(Equipo::getPuntos).reversed());
	}
	
	public void verClasificacion() {
		System.out.println("Clasificacion de la liga " + this.nombre + " :");
		int posicion = 1;
		for(Equipo e : this.clasificacion) {
			System.out.print(posicion + ". " + e);
			posicion++;
		}
	}
	
	public ArrayList<Equipo> getClasificacion(){
		return this.clasificacion;
	}
	
	public Mercado getMercado() {
		return this.mercado;
	}
}
