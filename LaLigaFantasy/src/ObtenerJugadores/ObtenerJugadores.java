package ObtenerJugadores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Jugador.Jugador;
import Jugador.Posicion;

public class ObtenerJugadores {
	
	private ArrayList<Jugador> Jugadores;
	public ObtenerJugadores() {
		Jugadores = obtenerJugadores();
	}
	
	public ArrayList<Jugador> getListaJugadores(){
		return this.Jugadores;
	}

	public static ArrayList<String> obtenerEquipos() {
		ArrayList<String> listaEquipos = new ArrayList<>();
		try {
			URL url = new URL("https://footballpool.dataflex.dev/info.wso");
			HttpURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			con.setDoOutput(true);
			
			String cuerpo = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
					+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
					+ "  <soap:Body>\r\n"
					+ "    <TeamNames xmlns=\"https://footballpool.dataaccess.eu\">\r\n"
					+ "    </TeamNames>\r\n"
					+ "  </soap:Body>\r\n"
					+ "</soap:Envelope>";
			
			try(OutputStream out = con.getOutputStream()){
				out.write(cuerpo.getBytes("utf-8"));
			}
			
			InputStream in = con.getInputStream();
			
			
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(in);
			
			NodeList equipos = d.getElementsByTagName("m:string");
			
			
			for(int i=0;i<equipos.getLength();i++) {
				String nombre = equipos.item(i).getTextContent();
				listaEquipos.add(nombre);
			}

		}catch(IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listaEquipos;
	}
	
	public static ArrayList<Jugador> obtenerJugadores() {
		ExecutorService pool = Executors.newFixedThreadPool(4);
		ArrayList<Jugador> listaJugadores = new ArrayList<>();
		ArrayList<String> paises = obtenerEquipos();
		
		try {
			ArrayList<Future<ArrayList<Jugador>>> listaResultados = new ArrayList<>();
			
			for(String s : paises) {
				JugadorPorPais jpp = new JugadorPorPais(s);
				listaResultados.add(pool.submit(jpp));
			}
			
			for(Future<ArrayList<Jugador>> f : listaResultados) {
				ArrayList<Jugador> jugadorPorSeleccion = f.get();
				listaJugadores.addAll(jugadorPorSeleccion);
			}
			
		}catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool.shutdown();
		
		return listaJugadores;
	}

}

class JugadorPorPais implements Callable<ArrayList<Jugador>>{
	
	private String pais;
	
	public JugadorPorPais(String p) {
		this.pais=p;
	}
	@Override
	public ArrayList<Jugador> call() throws Exception {
		ArrayList<Jugador> lista = new ArrayList<>();
		
		lista.addAll(descargarPorPosicion("G",Posicion.PORTERO));
		lista.addAll(descargarPorPosicion("D",Posicion.DECAMPO));
		lista.addAll(descargarPorPosicion("M",Posicion.DECAMPO));
		lista.addAll(descargarPorPosicion("F",Posicion.DECAMPO));
		
		return lista;
	}
	
	private ArrayList<Jugador> descargarPorPosicion(String posicionAPI,Posicion posicionJuego){
		ArrayList<Jugador> jugadoresPorPosicion = new ArrayList<>();
		try {
			URL url = new URL("https://footballpool.dataflex.dev/info.wso");
			HttpURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			con.setDoOutput(true);
			
			String cuerpo = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
					"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			        "  <soap:Body>" +
			        "    <AllPlayersWithRole xmlns=\"https://footballpool.dataaccess.eu\">" +
			        "      <sTeamName>" + this.pais + "</sTeamName>" + 
			        "      <sRoleCode>" + posicionAPI + "</sRoleCode>" +
			        "    </AllPlayersWithRole>" +
			        "  </soap:Body>" +
			        "</soap:Envelope>";
			
			try(OutputStream out = con.getOutputStream()){
				out.write(cuerpo.getBytes("utf-8"));
			}
			
			InputStream in = con.getInputStream();
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(in);
			
			NodeList jugadores = d.getElementsByTagName("m:sName");
			Random r = new Random();
			
			for(int i=0;i<jugadores.getLength();i++) {
				String nombre = jugadores.item(i).getTextContent();
				double precio = r.nextInt(1, 50)*1000000;
				Jugador j = new Jugador(nombre,posicionJuego,this.pais,precio);
				jugadoresPorPosicion.add(j);
			}
			
		}catch(IOException e){
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jugadoresPorPosicion;
	}
	
}