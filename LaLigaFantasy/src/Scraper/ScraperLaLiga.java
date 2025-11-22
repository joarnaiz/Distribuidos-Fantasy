package Scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ScraperLaLiga {
	public static void main(String[] args) {
		try {
			URL url = new URL("https://www.transfermarkt.es/laliga/startseite/wettbewerb/ES1");
			URLConnection con = url.openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			InputStream in = con.getInputStream();
			
			
			try(BufferedReader bf = new BufferedReader(new InputStreamReader(in))){
				String linea;
				ArrayList<String> equipos = new ArrayList<>();
				
				String partes[];
				String enlace;

				while((linea=bf.readLine())!=null){
					if(linea.contains("class=\"zentriert no-border-rechts\"")) {
						partes = linea.split("href=");
						enlace = partes[1].split("\"")[1];
						
						if(enlace.contains("startseite")) {
							equipos.add(enlace);
						}
						
						
					}
				}
				System.out.println(equipos.size());
				for(String s : equipos) {
					System.out.println(s);
				}
				
			}
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
