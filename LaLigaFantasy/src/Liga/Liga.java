package Liga;

import java.util.ArrayList;
import java.util.Comparator;

import Equipo.Equipo;

public class Liga {
	
	private String nombre;
	private static final int MAX_EQUIPOS = 20;
	private ArrayList<Equipo> clasificacion;
	
	public Liga(String nomb) {
		this.nombre=nomb;
		this.clasificacion = new ArrayList<Equipo>();
	}
	
	public boolean ligaCompleta() {
		return this.clasificacion.size()==MAX_EQUIPOS;
	}
	
	public void aniadirEquipo(Equipo e) {
		if(!ligaCompleta()) {
			this.clasificacion.add(e);
		}else {
			System.out.println("Liga completa");
		}
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
