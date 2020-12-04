package PokerModel;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.*;


public class Joc{
	
	protected ArrayList<ClientThread> jugadors_actius = new ArrayList<>();
	private Taula tauler = new Taula();
	private Baralla baralla;
	Scanner scanner = new Scanner(System.in);
	private int index_torn_relatiu;
	
	public Joc(ArrayList<ClientThread> jugadors_inicials){
		this.baralla = new Baralla();
		this.jugadors_actius = new ArrayList<ClientThread>(jugadors_inicials);
	}
	
//	public Joc(ArrayList<Jugador> jugadors_inicials, Servidor ss){
//		this.baralla = new Baralla();
//		this.jugadors_actius = new ArrayList<Client>(jugadors_inicials);
//		this.ss = ss;
//	}
	
	
	public int torn_actiu(ClientThread client) throws Exception {
		int output = -1;
		String st;
		while(output == -1) {
			st = "\nEl teu torn. Selecciona amb el numero.\n(1)Passar\n(2)Apostar\n(3)Retirarse";
			try {
				client.out_client.writeObject(st);
				client.out_client.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("antes");
			output = (Integer) client.in_client.readObject();
			System.out.println("despues");
//			output_int = jugador.p1.updateButton();
			switch(output) {
			case 1:
				st = "Passant torn...";
				client.out_client.writeObject(st);
				client.out_client.flush();
				
				break;
				
			case 2:  ///FALTA TRACTAR L'EXEMPCIÓ DE UNA LECTURA DE TEXT EN COMPTES DE NUMERO
				int output_sencer = 0;
				boolean apostat_correctament = false;
				while(apostat_correctament == false) {
					st = "Quant vols apostar? Tens: "+client.jugador.getDiners();
					//agafo l'output i el printo
//					output = scanner.nextLine();
					client.out_client.writeObject(st);
					client.out_client.flush();
					output = client.in_client.readInt();
//				    output_sencer = Integer.parseInt(output);
				    //Comprova que tingui diners per la aposta
				    if(output_sencer <= client.jugador.getDiners()) {
				    	apostat_correctament = true;
				    }else{
				    	st = "No tens tants diners. Pots apostar fins: "+client.jugador.getDiners();
				    }
				}
				//retiro els diners del jugador i li faig un update del que porta apostat a la taula
				int diners_restants = client.jugador.apostar(output_sencer);
				client.jugador.updateDiners_apostats(output_sencer);
				System.out.println(client.jugador.getNom()+" ha apostat: "+output);
				System.out.println(client.jugador.getNom()+" li queden: "+diners_restants);
				
				//afegeixo els diners sobre la taula i actualitzo la aposta
				tauler.afegir_diners_taula(output_sencer);
				tauler.update_aposta_activa(output_sencer);
				System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats.");

				return 1;//indica que hi ha una nova sequencia de torns
				
			case 3:
				st = "t'has retirat";
				remove_jugador(client.jugador.getNom());
				return 2;
				
			default:
				st = "No has triat cap opció correcta";
				client.out_client.writeObject(st);
				output = -1;
			}
		}
		return 0;//indica que no hi ha una nova sequencia de torns
	}
	
	public int torn_actiu(Jugador jugador, int aposta_actual) {
		String output = "none";
		// comprova que hi hagi un output correcte
		while(output == "none") {
			//separo dos casos, nomes puc fer all-in o puc aumentar la aposta
			if(jugador.getDiners() > aposta_actual - jugador.getDinersApostats()) {
				System.out.println("\nTorn de: "+jugador.getNom()+". Selecciona amb el numero.\n(1)Igualar\n(2)Apostar\n(3)Retirarse");
				output = scanner.nextLine();
				switch(output) {
				//igualar la aposta
				case "1":
					System.out.println("Has igualat la aposta");
					System.out.println("Tenies apostat: "+jugador.getDinersApostats());
					
					//afegir diners a la taula
					tauler.afegir_diners_taula(aposta_actual - jugador.getDinersApostats());
					System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats en total.");
					
					//retirar els diners del jugador i update de la seva aposta
					jugador.apostar(aposta_actual - jugador.getDinersApostats());
					jugador.updateDiners_apostats(aposta_actual);
					
					System.out.println("Et queden: "+jugador.getDiners()+", i ara tens apostats: "+jugador.getDinersApostats());
					
					break;
					
				case "2":  ///FALTA TRACTAR L'EXEMPCIÓ DE UNA LECTURA DE TEXT EN COMPTES DE NUMERO
					int output_sencer = 0;
					boolean apostat_correctament = false;
					while(apostat_correctament == false) {
						System.out.println("Quant vols apostar? Hi ha "+aposta_actual+" apostant-se.\n Ja has apostat: "+jugador.getDinersApostats()+".Tens: "+jugador.getDiners());
						
						//agafant i canviant a int
						output = scanner.nextLine();
					    output_sencer = Integer.parseInt(output);
					    
					    //comprovar que l'aposta arriba a igualar (al menys) i que tens suficients diners
					    if(output_sencer <= jugador.getDiners() && output_sencer >= aposta_actual - jugador.getDinersApostats()) {
					    	apostat_correctament = true;
					    }else{
					    	System.out.println("No tens tants diners. Pots apostar fins: "+jugador.getDiners());
					    }
					}
					// es retiren els diners del jugador i s'actualitza la seva aposta
					System.out.println(jugador.getNom()+" ha apostat: "+(output_sencer + jugador.getDinersApostats()));
					int diners_restants = jugador.apostar(output_sencer);
					jugador.updateDiners_apostats(jugador.getDinersApostats() + output_sencer);
					System.out.println(jugador.getNom()+" li queden: "+diners_restants);
					
					//actualitzar diners a la taula + l'aposta actual
					tauler.afegir_diners_taula(output_sencer);
					tauler.update_aposta_activa(jugador.getDinersApostats());
					System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats en total.");
					
					return 1; //indica que hi ha una nova sequencia de torns, s'ha apostat
					
				case "3":
					System.out.println(jugador.getNom() + " s'ha retirat del joc.");
					remove_jugador(jugador.getNom());
					return 2;
					
				default:
					System.out.println("No has triat cap opció correcta");
					output = "none";
				}
			}else{
				System.out.println("\nTorn de: "+jugador.getNom()+". Selecciona amb el numero.\n(1)All-in\n(2)Retirarse");
				output = scanner.nextLine();
				switch(output) {
				case "1":
					System.out.println(jugador.getNom()+" ha fet all-in.");
					tauler.afegir_diners_taula(jugador.getDiners());
					jugador.apostar(jugador.getDiners());
					break;
					
				case "2":
					System.out.println(jugador.getNom() + " s'ha retirat del joc.");
					remove_jugador(jugador.getNom());
					return 2;
					
				default:
					System.out.println("No has triat cap opció correcta");
					output = "none";
				}
			}
		}
		return 0; //indica que no hi ha una nova sequencia de torns
	}
	
	//retorna false si hi ha nous torns d'aposta - true si es canvia de torn
	public boolean sequencia_torns(int index_torn_actiu, boolean aposta) {
		if(aposta == false) {
			for(int i=index_torn_actiu ; i < index_torn_actiu + jugadors_actius.size() ; i++) {
				int index_torn_actiu_real = i%jugadors_actius.size();
				int moviment;
				try {
					moviment = torn_actiu(jugadors_actius.get(index_torn_actiu_real));
					if(moviment == 1) {
						index_torn_relatiu = index_torn_actiu_real;
						return false;
					}else if(moviment == 2){
						i -= 1;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else if(aposta == true){
			for(int i=index_torn_actiu + 1 ; i < index_torn_actiu + jugadors_actius.size(); i++) {
				int index_torn_actiu_real = i%jugadors_actius.size();
				int moviment = torn_actiu(jugadors_actius.get(index_torn_actiu_real).jugador, tauler.get_aposta_activa());
				if(moviment == 1) {
					index_torn_relatiu = index_torn_actiu_real;
					return false;
				}else if(moviment == 2) {
					i -= 1;
				}
			}
		}
		return true;
	}
	
	public void flush_apostes() {
		for(int i=0 ; i < jugadors_actius.size() ; i++) {
			jugadors_actius.get(i).jugador.updateDiners_apostats(0);
		}
	}
	
	public boolean check_lastone() {
		if(jugadors_actius.size() == 0 || jugadors_actius.size() == 1) {
			return true;
		}else {
			return false;
		}
	}
	
	public void donar_cartes_taula() {
		//Fa un check de quantes cartes hi ha a la taula per a tothom i en funció fa una cosa o una altra
		if(tauler.cartes_sobre_taula.size() == 0) {
			for(int i=0 ; i<3 ; i++) {
				tauler.cartes_sobre_taula.add(baralla.pick_and_remove_Carta());
			}
		}else if(tauler.cartes_sobre_taula.size() == 5){
			System.out.println("\nS'ha acabat el joc.");
		}else {
			tauler.cartes_sobre_taula.add(baralla.pick_and_remove_Carta());
		}
	}

	private void print_cartes_jugadors() {
		for(int i=0 ; i < jugadors_actius.size() ; i++ ) {
			System.out.println("\nJugador "+jugadors_actius.get(i).jugador.getNom());
			jugadors_actius.get(i).jugador.getMa();
		}
	}
	
	private void print_cartes_taula() {
		System.out.println("\n----------------------------------");
		for(int i=0 ; i < tauler.cartes_sobre_taula.size() ; i++ ) {
			System.out.println("Carta "+(i+1)+" -> "+tauler.cartes_sobre_taula.get(i));
		}
		System.out.println("----------------------------------\n");
	}
	
	private void donar_cartes_jugadors() {
		for(int i=0 ; i < jugadors_actius.size() ; i++ ) {
			jugadors_actius.get(i).jugador.nova_Ma(baralla);
		}
	}
	
//	private void add_jugador(String nom, int diners) {
//		this.jugadors_actius.add(new Jugador(nom, diners));
//	}
	
	private void remove_jugador(String nom) {
		String iter_name;
		for (int i=0 ; i < jugadors_actius.size() ; i++) {
			iter_name = jugadors_actius.get(i).jugador.getNom();
			if(iter_name == nom) {
				this.jugadors_actius.remove(i);
				break;
			}
		}
	}
	
	public void generar_joc(int index_torn_absolut) {
		boolean nou_torn, check_guanyador = true;
		int first_time = 1;
		
		donar_cartes_jugadors();
		print_cartes_jugadors();
		
		for(int j=0; j<4 ; j++) {
			do {
				if(first_time == 1) {
					nou_torn = sequencia_torns(index_torn_absolut, false);
					first_time = 0;
				}else{
					nou_torn = sequencia_torns(index_torn_relatiu, true);
				}
			}while(!nou_torn);
			if(check_lastone()) {
				check_guanyador = false;
				if(jugadors_actius.size() == 1) {
					jugadors_actius.get(0).jugador.rebreDiners(tauler.get_diners_taula());
				}
				break;
			}
			if(j==3) {
				check_guanyador = true;
			}
			flush_apostes();
			first_time = 1;
			donar_cartes_taula();
			print_cartes_taula();
		}
//		if(check_guanyador) {
//			int guanyador = tauler.guanyador(jugadors_actius , tauler.cartes_sobre_taula);
//			System.out.println("Ha guanyat: "+jugadors_actius.get(guanyador).jugador.getNom());
//		}
	}
}
