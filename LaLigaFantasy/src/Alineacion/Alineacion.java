package Alineacion;

import java.io.Serializable;
import java.util.ArrayList;

import Jugador.Jugador;

public class Alineacion implements Serializable{
	
	private Jugador portero;
	private ArrayList<Jugador> jugadoresDeCampo;
	private static final int Max_Jug = 10;
	
	public Alineacion() {
		this.portero=null;
		this.jugadoresDeCampo = new ArrayList<>();
	}
	
	//añadir jugador, modificar alineacion(cambiar un jugador por otro),eliminar jugador, bool esta completa, mostrar alineacion

}
