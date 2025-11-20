package Cliente;

import java.util.Scanner;

public class ClienteFantasy {

	public static void main(String[] args) {
		
		boolean salir=false;
		int opcion=0;
		Scanner s = new Scanner(System.in);
		
		while(!salir) {
			menuPrincipal();
			opcion = s.nextInt();
			
			switch(opcion) {
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				default:
					System.out.println("Opción incorrecta");
			}
			
		}
		
	}
	
	
	public static void menuPrincipal() {
		System.out.println("==============================================");
		System.out.println("     DISTRIBUIDOS FANTASY--MENU PRINCIPAL"     );
		System.out.println("==============================================");
		System.out.println();
		System.out.println("---------------------------");
		System.out.println("1. MI EQUIPO");
		System.out.println("2. Ver mi Liga");
		System.out.println("3. Mercado");
		System.out.println("4.Abandonar la liga");
		System.out.println("---------------------------");
		System.out.println();
	}

}
