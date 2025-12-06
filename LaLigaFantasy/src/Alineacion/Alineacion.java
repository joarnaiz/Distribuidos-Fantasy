package Alineacion;

import java.io.Serializable;
import java.util.ArrayList;

import Jugador.Jugador;

public class Alineacion implements Serializable{
	
	private Jugador portero;
	private ArrayList<Jugador> jugadoresDeCampo;
	private static final int MAX_JUG = 10;
	
	public Alineacion() {
		this.portero=null;
		this.jugadoresDeCampo = new ArrayList<>();
	}
	
	public String aniadirJugador(Jugador j) {
		if (alineado(j)) {
			return "Jugador ya alineado";
		}
		
		if (j.esPortero()) {
			if (this.portero == null) {
				this.portero = j;
				return "Portero " + j.getNombre() + " añadido correctamente";
			} else {
				return "Ya hay un portero en la alineación";
			}
		}
		
		else {
			if (this.jugadoresDeCampo.size() < MAX_JUG) {
				this.jugadoresDeCampo.add(j);
				return "Jugador de campo " + j.getNombre() + " añadido correctamente";
			} else {
				return "No hay hueco para más jugadores de campo";
			}
		}	
	}
	
	public boolean eliminarJugador(Jugador j) {
		if (this.portero != null && this.portero.equals(j)) {
			this.portero = null;
			System.out.println("Portero " + j.getNombre() + " eliminado de la alineación");
			return true;
		}
		
		if (this.jugadoresDeCampo.remove(j)) {
			System.out.println("Jugador de campo " + j.getNombre() + " eliminado de la alineación");
			return true;
		}
		
		System.out.println("El jugador no estaba en la alineación");
		return false;
	}
	
	public void mostrarAlineacion() {
		System.out.println("--- ALINEACIÓN ---");
			
		System.out.println("Portero:");
		if (this.portero == null) {
			System.out.println("Estás sin portero");
		} else {
			System.out.println("  " + this.portero);
		}
			
		System.out.println("Jugadores de campo:");
		if (this.jugadoresDeCampo.isEmpty()) {
			System.out.println("No tienes jugadores de campo alineados");
		} else {
			for (Jugador j : this.jugadoresDeCampo) {
				System.out.println("   " + j);
			}
		}
	}
	
	/* Lo he hecho en equipo para comprobar que ambos estan en el equipo
	 * 
	public boolean cambiarJugador(Jugador fuera, Jugador dentro) {
		if (!alineado(fuera)) {
			System.out.println("El jugador que quieres sacar no está en la alineación");
			return false;
		}
		
		if(alineado(dentro)) {
			System.out.println("El jugador que entra ya está alineado");
			return false;
		}
		
		boolean eliminado = eliminarJugador(fuera);
		if (!eliminado) {
			return false;
		}
		
		boolean aniadido = aniadirJugador(dentro);
		if (!aniadido) {
			aniadirJugador(fuera); // Si falla, volvemos a meter al jugador que se ha ido para que quede igual
			System.out.println("No se pudo añadir al jugador");
			return false;
		}
		System.out.println("El jugador " + fuera.getNombre() + " ha sido sustituido por " + dentro.getNombre());
		return true;
	}
	*/
	
	public void limpiarAlineacion() {
	    this.portero = null;
	    this.jugadoresDeCampo.clear();
	    
	    System.out.println("Alineación vaciada");
	}
	
	public boolean estaCompleta() {
		return this.portero != null && this.jugadoresDeCampo.size() == MAX_JUG;
	}
	
	public boolean alineado(Jugador j) {
		if (this.portero != null && this.portero.equals(j)) {
			return true;
		}
		
		return this.jugadoresDeCampo.contains(j);
	}
	
	public Jugador getPortero() {
		return portero;
	}

	public ArrayList<Jugador> getJugadoresDeCampo() {
		return jugadoresDeCampo;
	}

}
