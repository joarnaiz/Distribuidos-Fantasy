package Equipo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Alineacion.Alineacion;
import Jugador.Jugador;
import Mercado.Oferta;

public class Equipo implements Serializable{
	
	//POner el atributo de serializable(en alguna de las practicas esta, ya lo mirare)
	
	private String nombre;
	private int puntosTotales;
	private HashMap<Integer,Integer> puntosJornada;
	private ArrayList<Jugador> plantilla;
	private Alineacion alineacion;
	private static final int MAX_JUGADORES_PlANTILLA = 22;
	private static final int TITULARES = 11;
	public double saldo;
	private ArrayList<Oferta> ofertas;
	
	public Equipo(String nom) {
		this.nombre = nom;
		this.plantilla = new ArrayList<>();
        this.saldo = 100000000; // 100 millones
        this.alineacion = new Alineacion();
        this.puntosJornada = new HashMap<Integer,Integer>();
        this.puntosTotales=0;
        this.ofertas = new ArrayList<>();
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public int getPuntos() {
		return this.puntosTotales;
	}
	
	public void setPuntosJornada(int jornada,int puntos) {
		this.puntosJornada.put(jornada, puntos);
		this.puntosTotales+=puntos;
	}
	
	public int getPuntosJornada(int jornada) {
		return this.puntosJornada.get(jornada);
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public void setSaldo(double d) {
		this.saldo += d;
	}
	
	public void eliminarJugadorPlantilla(Jugador j) {
		this.alineacion.eliminarJugador(j);
		this.plantilla.remove(j);
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
		return this.alineacion.estaCompleta();
	}
	
	public String aniadirJugador(Jugador j) {
		if(!equipoCompleto()) {
			this.plantilla.add(j);
			return "Jugador añadido";
		}else {
			return "Maximo de jugadores alcanzado";
		}
	}
	
	public Alineacion getAlineacion() {
		return this.alineacion;
	}
		
	public Jugador jugadorEnEquipo(int id) { //Si el jugador esta en el equipo devuelve el jugador, si no devuelve null
		Jugador jug = null;
		for(Jugador j:this.plantilla) {
			if(j.getId()==id) {
				jug = j;
			}
		}
		return jug;
	}
	
	public String alinearJugador(int idJugador) {
		Jugador j = jugadorEnEquipo(idJugador);
		String resultado = "El jugador no existe en la plantilla";
		
		if(j!=null) {
			resultado = this.alineacion.aniadirJugador(j);
		}
		return resultado;
	}
	
	public String sustituirJugador(int sale,int entra) { //Los sustituye en la alineacion
		
		Jugador jEntra = jugadorEnEquipo(entra);
		Jugador jSale = jugadorEnEquipo(sale);
		String resultado="Los jugadores se han sustituido correctamente";
		
		if(jEntra!=null && jSale!=null) {			
			if(!this.alineacion.eliminarJugador(jSale)) {
				resultado = "El jugador que quieres cambiar no está alineado";
			}else {
				resultado = this.alineacion.aniadirJugador(jEntra);
				
				//Si falla el añadirJugador añadimos el anterior para que no se quede incompleta la alineacion
				
				if(!resultado.contains("correctamente")) {
					this.alineacion.aniadirJugador(jSale);
				}
			}
			
		}else {
			resultado = "Alguno de los jugadores no esta en tu plantilla";
		}
		
		return resultado;
	}
	
	public void mostrarPlantilla() {
		System.out.println("Plantilla de " +this.nombre + " :");
		for(Jugador j : this.plantilla) {
			System.out.println(j);
		}
	}
	
	public void recibirOferta(Oferta oferta) {
		this.ofertas.add(oferta);
	}
	
	public ArrayList<Oferta> getOfertas(){
		return this.ofertas;
	}
	
	public void eliminarOferta(Oferta oferta) {
		this.ofertas.remove(oferta);
	}
	
	
	public String aceptarOferta(Oferta oferta) {
		Equipo comprador = oferta.getComprador(); 
		Equipo vendedor = oferta.getVendedor();
		Jugador jugador = oferta.getJugadorAFichar();
		double precio = oferta.getCantidadTraspaso();
		
		if (comprador.getSaldo() < precio) {
	        vendedor.eliminarOferta(oferta);
	        return "No se ha podido realizar el traspaso porque el comprador se ha quedado sin saldo.";
	    }
		
		if (vendedor.jugadorEnEquipo(jugador.getId()) == null) {
	        vendedor.eliminarOferta(oferta);
	        return "Error, este jugador ya no está en tu plantilla.";
	    }
		
		comprador.setSaldo(-precio);
		vendedor.setSaldo(precio);
		
		vendedor.eliminarJugadorPlantilla(jugador);
		comprador.aniadirJugador(jugador);
		
		
		// Eliminamos TODAS las ofertas que ha recibido el vendedor por ese jugador
		List<Oferta> ofertasBasura = new ArrayList<>();
		
		for (Oferta o : vendedor.getOfertas()) {
			if (o.getJugadorAFichar().getId() == jugador.getId()) {
				ofertasBasura.add(o);
			}
		}
		
		vendedor.getOfertas().removeAll(ofertasBasura);
		
		return "Has vendido a " + jugador.getNombre() + " a tu rival" + comprador.getNombre() + " por " + precio + "€";
	}
	
	public String rechazarOferta(Oferta oferta) {
		Equipo vendedor = oferta.getVendedor();
		vendedor.eliminarOferta(oferta);
		
		return "Oferta rechazada, el jugador " + oferta.getJugadorAFichar() + " se queda en tu club";
	}
	
	public void mostrarAlineacion() {
		System.out.println("Once inicial de " +this.nombre + " :");
		this.alineacion.mostrarAlineacion();
	}
	
	@Override
	public boolean equals(Object e) {
	    if (this == e) return true;         
	    if (e == null || getClass() != e.getClass()) return false; 
	    Equipo eq = (Equipo) e;
	    return this.nombre.equals(eq.nombre);                     
	} 
	
	public String toString() {
		return this.nombre + " con " + this.puntosTotales +"\n";
	}

}
