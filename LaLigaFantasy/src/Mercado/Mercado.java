package Mercado;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Equipo.Equipo;
import Jugador.Jugador;
import Mercado.Puja;

public class Mercado implements Serializable{

	private List<Jugador> jugadoresDisponibles;
	private List<Jugador> jugadoresLibres;
	private static final int JUGADORES_MERCADO = 20;
	private final Random random = new Random();
	private List<Puja> listaPujas;
	private long ultimaActualizacionMercado;
	private final long INTERVALO_ACTUALIZACION=10*60*1000;
	
	public Mercado(List<Jugador> poolJugadores) {
		this.jugadoresLibres = new ArrayList<>(poolJugadores);
		this.jugadoresDisponibles = new ArrayList<Jugador>();
		this.listaPujas = new ArrayList<>();
		this.ultimaActualizacionMercado=System.currentTimeMillis();
		actualizarMercado();
	}
	
	public void actualizarMercado() {
		this.jugadoresDisponibles.clear();
		this.ultimaActualizacionMercado=System.currentTimeMillis();
		
		List<Jugador> copia = new ArrayList<>(this.jugadoresLibres);
		Collections.shuffle(copia, random);
		
		int limite = Math.min(copia.size(), JUGADORES_MERCADO); // Por si hay menos de 20 futbolistas disponibles
		for (int i = 0; i < limite; i++) {
			this.jugadoresDisponibles.add(copia.get(i));
		}
	}
	
	public synchronized void mostrarMercado() {
		
		
		System.out.println("--- MERCADO ---");
		if (jugadoresDisponibles.isEmpty()) {
            System.out.println("No hay jugadores en el mercado actualmente.");
            return;
        }
		for (int i = 1; i <= jugadoresDisponibles.size(); i++) {
			Jugador j = jugadoresDisponibles.get(i-1);	 
			System.out.println(i + ". " + j.toString());
		}	
		 
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		long tiempoEnMercado = System.currentTimeMillis()-this.ultimaActualizacionMercado;
		long tiempoRestante = this.INTERVALO_ACTUALIZACION-tiempoEnMercado;
		String crono = sdf.format(tiempoRestante);
		 
		System.out.println();
		System.out.println("Tiempo antes de que actualice: " + crono);
	}
	
	public String pujarJugador(int opcion, Equipo equipo, double tuPuja) {
		if (opcion < 1 || opcion > jugadoresDisponibles.size() || tuPuja < jugadoresDisponibles.get(opcion-1).getValor()) {
			return "Error al pujar por el jugador";
		}
		
		Jugador fichaje = jugadoresDisponibles.get(opcion-1);
		
		if (equipo.getSaldo() < tuPuja) {
			return "El jugador no tiene dinero";
		}
		
		equipo.setSaldo(-tuPuja);
		Puja p = new Puja(equipo, fichaje, tuPuja);
		listaPujas.add(p);
		
		// En caso de que la puja sea igual, gana el jugador que primero pujó
		return "Has pujado " + tuPuja + "€ por " + fichaje.getNombre() + ". ¡Mucha suerte!";
	}
	
	public void comprobarPujas() {
		System.out.println("Comprobando pujas...");
		if (listaPujas.isEmpty()) {
			System.out.println("No ha habido pujas en este mercado");
			return;
		}
		
		List<Jugador> vendidos = new ArrayList<>();
		
		// Para cada jugador del mercado comprobamos quien ha pujado mas alto
		for (Jugador j : jugadoresDisponibles) {
			Puja mejorPuja = null;
			double cantidadPujaMaxima = 0.0;
			List<Puja> pujasJugador = new ArrayList<>(); // Para guardar todas las pujas hechas por ese jugador
			
			for (Puja p : listaPujas) {
				if (p.getJugadorPuja().equals(j)) {
					pujasJugador.add(p);
					if(p.getCantidadPuja() > cantidadPujaMaxima) {
						cantidadPujaMaxima = p.getCantidadPuja();
						mejorPuja = p;
					}
				}
				
			}
			
			if (mejorPuja != null) {
				Equipo ganadorPuja = mejorPuja.getEquipoPuja();
				ganadorPuja.aniadirJugador(j);
				System.out.println("El jugador " + ganadorPuja.getNombre() + " ha fichado a " + j.getNombre() + "por " + mejorPuja.getCantidadPuja() + " €");
				vendidos.add(j);
				
				for (Puja p : pujasJugador) {
					if (p != mejorPuja) {
						p.getEquipoPuja().setSaldo(p.getCantidadPuja());
					}
				}
			}
		}
		
		jugadoresDisponibles.removeAll(vendidos);
		jugadoresLibres.removeAll(vendidos);
		listaPujas.clear();
	}
	
	
	public String venderJugador(int idJugador, Equipo equipoVendedor) {
		Jugador jugadorVenta = equipoVendedor.jugadorEnEquipo(idJugador);
		if (jugadorVenta == null) {
			return "Error, jugador no encontrado";
		}
		
		equipoVendedor.setSaldo(jugadorVenta.getValor());
		equipoVendedor.eliminarJugadorPlantilla(jugadorVenta);
		
		this.jugadoresLibres.add(jugadorVenta);
		
		return "Le has dicho a " + jugadorVenta.getNombre() + " que haga las maletas";
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
		
		comprador.aniadirJugador(jugador);
		vendedor.eliminarJugadorPlantilla(jugador);
		
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
}
