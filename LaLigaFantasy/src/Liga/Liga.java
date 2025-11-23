package Liga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import Equipo.Equipo;
import Jugador.Jugador;

public class Liga implements Serializable{
	
	private String nombre;
	private static final int MAX_EQUIPOS = 20;
	private ArrayList<Equipo> clasificacion;
	private ArrayList<Jugador> jugadoresDisponibles;
	
	public Liga(String nomb) {
		this.nombre=nomb;
		this.clasificacion = new ArrayList<Equipo>();
		this.jugadoresDisponibles = new ArrayList<Jugador>();
	}
	
	public ArrayList<Jugador> getJugadoresDisponibles(){
		return this.jugadoresDisponibles;
	}
	public void setJugadoresDisponibles(ArrayList<Jugador> libres) {
		this.jugadoresDisponibles=libres;
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
			
		}else {
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
		for(Equipo e : this.clasificacion) {
			System.out.print(e.toString());
		}
	}
}
