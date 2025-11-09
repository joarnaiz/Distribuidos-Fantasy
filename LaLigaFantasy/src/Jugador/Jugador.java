package Jugador;


public class Jugador {

	private static int contadorId = 0;
	private int id;
	private String nombre;
	private String equipo;
	private String posicion;
	private double valor;
	private int puntos;
	
	public Jugador(String nomb,String pos,String eq,double val) {
		
		this.id = contadorId++;
		this.nombre = nomb;
		this.posicion = pos;
		this.equipo=eq;
		this.valor = val;
		this.puntos = 0;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	public String getEquipo() {
		return this.equipo;
	}
	public String getPosicion() {
		return this.posicion;
	}
	public int getPuntos() {
		return this.puntos;
	}
	
	public void aniadirPuntos(int p) {
		this.puntos+=p;
	}
	
	public void aniadirValor(double valor) {
		this.valor+=valor;
	}
	
	@Override
	public boolean equals(Object j) {
	    if (this == j) return true;         
	    if (j == null || getClass() != j.getClass()) return false; 
	    Jugador jug = (Jugador) j;
	    return id == jug.id;                     
	}
	

	public String toString() {
		String jugador = "Nombre: " + this.nombre + " Equipo: " + this.equipo + " Posicion: " + this.posicion + " valor: " + this.valor
				+ " puntos " + this.puntos;
		return jugador;
	}

}
