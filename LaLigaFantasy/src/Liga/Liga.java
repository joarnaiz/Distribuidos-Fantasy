package Liga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import Equipo.Equipo;
import Jugador.Jugador;
import Mercado.Mercado;

public class Liga implements Serializable{
	
	private String nombre;
	private static final int MAX_EQUIPOS = 20;
	private ArrayList<Equipo> clasificacion;
	private ArrayList<Equipo> clasificacionJornada;
	private HashMap<Integer,ArrayList<Equipo>> jornadas;
	private ArrayList<Jugador> jugadoresLibres;
	private Mercado mercado;
	
	
	public Liga(String nomb) {
		this.nombre=nomb;
		this.clasificacion = new ArrayList<Equipo>();
		this.jugadoresLibres = new ArrayList<Jugador>();
		this.clasificacionJornada = new ArrayList<Equipo>();
		this.jornadas = new HashMap<Integer,ArrayList<Equipo>>;
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
	
	public void actualizarClasificacionJornada(int jornada) {
		this.clasificacionJornada.sort(Comparator.comparingInt((Equipo e)-> e.getPuntosJornada(jornada)).reversed());
		this.jornadas.put(jornada, this.clasificacionJornada);
	}
	
	public void verClasificacion() {
		System.out.println("Clasificacion de la liga " + this.nombre + " :");
		int posicion = 1;
		for(Equipo e : this.clasificacion) {
			System.out.print(posicion + ". " + e);
			posicion++;
		}
	}
	
	public void verClasificacionJornada(int jornada) {
		if(jornada>0 && jornada<=this.jornadas.size()) {
			System.out.println("Clasificacion de la liga " + this.nombre +" en la jornada " + jornada +" :");
			int posicion = 1;
			for(Equipo e : this.jornadas.get(jornada)) {
				System.out.print(posicion + ". " + e);
				posicion++;
			}
		}else {
			System.out.println("No hay clasificacion para esa jornada");
		}		
	}
	
	public ArrayList<Equipo> getClasificacion(){
		return this.clasificacion;
	}
	
	public ArrayList<Equipo> getClasificacionJornada(){
		return this.clasificacionJornada;
	}
	
	public Mercado getMercado() {
		return this.mercado;
	}
	
	public Equipo getEquipoPorNombre(String nombre) {
		for(Equipo eq: this.clasificacion) {
			if(eq.getNombre().equalsIgnoreCase(nombre)) {
				return eq;
			}
		}
		return null;
	}
}
