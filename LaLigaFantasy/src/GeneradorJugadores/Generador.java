package GeneradorJugadores;

import java.util.ArrayList;
import java.util.Random;

import Jugador.Jugador;

public class Generador {
	
	private static final String[] NOMBRES = {
	        "Vinicius", "Bellingham", "Lewandowski", "Griezmann", "Pedri", 
	        "Courtois", "Oblak", "Ter Stegen", "Araujo", "Kubo", 
	        "Aspas", "Gerard Moreno", "Fekir", "Nico Williams", "Lamine Yamal",
	        "Carvajal", "Gavi", "Rodrygo", "Sorloth", "Parejo", "Isco", "Ramos",
	        "Modric", "Kroos", "De Jong", "Gundogan", "Sancet", "Guruzeta"
	    };
	private static final String[] EQUIPOS = {
	        "Real Madrid", "FC Barcelona", "Atlético", "Real Sociedad", 
	        "Betis", "Sevilla", "Villarreal", "Athletic Club", "Valencia", "Osasuna"
	    };
	
	public static ArrayList<Jugador> generar(int cantidad) {
        ArrayList<Jugador> lista = new ArrayList<>();
        Random rand = new Random();

        System.out.println(">>> [MOCK] GENERANDO " + cantidad + " JUGADORES CON PRECIO FIJO (20M)...");

        for (int i = 1; i <= cantidad; i++) {
            
            // 1. Nombre único
            String nombreFinal = NOMBRES[rand.nextInt(NOMBRES.length)] + " " + i;
            
            // 2. Equipo
            String equipo = EQUIPOS[rand.nextInt(EQUIPOS.length)];

            // 3. Posición (10% Portero)
            String textoPosicion;
            if (rand.nextInt(10) == 0) {
                textoPosicion = "Portero";
            } else {
                textoPosicion = "Jugador de campo";
            }

            // 4. VALOR FIJO (20 Millones)
            // En Java puedes usar guiones bajos para leer mejor los números largos
            double valor = 20_000_000.0; 

            // 5. Crear Jugador
            Jugador nuevo = new Jugador(nombreFinal, textoPosicion, equipo, valor);
            
            lista.add(nuevo);
        }

        return lista;
    }
	
	public static void main(String[] args) {
		ArrayList<Jugador> jugadores = generar(200);
		for(Jugador j : jugadores) {
			System.out.println(j);
		}
		
	}
}


