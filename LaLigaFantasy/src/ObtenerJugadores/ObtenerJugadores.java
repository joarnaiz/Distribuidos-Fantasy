package ObtenerJugadores;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Jugador.Jugador;

public class ObtenerJugadores {
	
	private List<Jugador> Jugadores;
	public ObtenerJugadores() {
		Jugadores = obtenerJugadores();
	}
	
	public static void main(String[] args) {
		List<String> l = obtenerEquipos();
		System.out.println(l.size());
	}

	public static List<String> obtenerEquipos() {
		List<String> listaEquipos = new ArrayList<>();
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
				System.out.println(nombre);
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
	
	public List<Jugador> obtenerJugadores() {
		
	}

}
