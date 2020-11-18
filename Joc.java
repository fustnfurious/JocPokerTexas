import java.util.*;

public class Joc {
	
	private ArrayList<Jugador> jugadors_actius = new ArrayList<>();
	private Taula tauler = new Taula();
	private Baralla baralla;
	private boolean nou_primer_torn = true;
	Scanner scanner = new Scanner(System.in);
	private int index_total_torn;
	
	
	public Joc(){
		this.baralla = new Baralla();
	}
	
	
	public int torn_actiu(Jugador jugador) {
		String output = "none";
		while(output == "none") {
			System.out.println("\nTorn de: "+jugador.getNom()+". Selecciona amb el numero.\n(1)Passar\n(2)Apostar\n(3)Retirarse");
			output = scanner.nextLine();
			System.out.println(output);
			switch(output) {
			case "1":
				System.out.println("Passant torn...");
				break;
				
			case "2":  ///FALTA TRACTAR L'EXEMPCIÓ DE UNA LECTURA DE TEXT EN COMPTES DE NUMERO
				int output_sencer = 0;
				boolean apostat_correctament = false;
				while(apostat_correctament == false) {
					System.out.println("Quant vols apostar? Tens: "+jugador.getDiners());
					//agafo l'output i el printo
					output = scanner.nextLine();
				    output_sencer = Integer.parseInt(output);
				    //Comprova que tingui diners per la aposta
				    if(output_sencer <= jugador.getDiners()) {
				    	apostat_correctament = true;
				    }else{
				    	System.out.println("No tens tants diners. Pots apostar fins: "+jugador.getDiners());
				    }
				}
				//retiro els diners del jugador i li faig un update del que porta apostat a la taula
				int diners_restants = jugador.apostar(output_sencer);
				jugador.updateDiners_apostats(output_sencer);
				System.out.println(jugador.getNom()+" ha apostat: "+output);
				System.out.println(jugador.getNom()+" li queden: "+diners_restants);
				
				//afegeixo els diners sobre la taula i actualitzo la aposta
				tauler.afegir_diners_taula(output_sencer);
				tauler.update_aposta_activa(output_sencer);
				System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats.");

				return 1;//indica que hi ha una nova sequencia de torns
				
			case "3":
				System.out.println(jugador.getNom() + " s'ha retirat del joc.");
				remove_jugador(jugador.getNom());
				return 2;
				
			default:
				System.out.println("No has triat cap opció correcta");
				output = "none";
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
				int moviment = torn_actiu(jugadors_actius.get(index_torn_actiu_real));
				if(moviment == 1) {
					index_total_torn = index_torn_actiu_real;
					return false;
				}else if(moviment == 2){
					i -= 1;
				}
			}
		}else if(aposta == true){
			for(int i=index_torn_actiu + 1 ; i < index_torn_actiu + jugadors_actius.size(); i++) {
				int index_torn_actiu_real = i%jugadors_actius.size();
				int moviment = torn_actiu(jugadors_actius.get(index_torn_actiu_real), tauler.get_aposta_activa());
				if(moviment == 1) {
					index_total_torn = index_torn_actiu_real;
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
			jugadors_actius.get(i).updateDiners_apostats(0);
		}
	}
	
	public boolean check_lastone() {
		if(jugadors_actius.size() == 1) {
			return true;
		}else {
			return false;
		}
	}
	
	private void print_cartes() {
		for(int i=0 ; i < jugadors_actius.size() ; i++ ) {
			System.out.println("\nJugador "+jugadors_actius.get(i).getNom());
			jugadors_actius.get(i).getMa();
		}
	}
	
	private void donar_cartes_jugadors() {
		for(int i=0 ; i < jugadors_actius.size() ; i++ ) {
			jugadors_actius.get(i).nova_Ma(baralla);
		}
	}
	
	private void add_jugador(String nom, int diners) {
		this.jugadors_actius.add(new Jugador(nom, diners));
	}
	
	private void remove_jugador(String nom) {
		String iter_name;
		for (int i=0 ; i < jugadors_actius.size() ; i++) {
			iter_name = jugadors_actius.get(i).getNom();
			if(iter_name == nom) {
				this.jugadors_actius.remove(i);
				break;
			}
		}
	}
	
	private void generar_joc() {
		index_total_torn = canviar_primer_torn();
		boolean nou_torn;
		int first_time = 1;
		
		donar_cartes_jugadors();
		print_cartes();
		
		for(int j=0; j<4 ; j++) {
			do {
				if(first_time == 1) {
					nou_torn = sequencia_torns(index_total_torn, false);
				}else{
					nou_torn = sequencia_torns(index_total_torn, true);
				}
			}while(!nou_torn);
			if(check_lastone()) {
				break;
			}
			flush_apostes();
			first_time = 1;
		}
	}

	

	//auqest metode no hauria d'estar a joc sino a partida
	private int canviar_primer_torn() {
//		System.out.println(jugadors_actius.size());
		for(int i=0 ; i < jugadors_actius.size() ; i++) {
//			System.out.println(i);
			if(jugadors_actius.get(i).primerTorn == true) {
				jugadors_actius.get(i).primerTorn = false;
				jugadors_actius.get((i+1)%jugadors_actius.size()).primerTorn = true;
				return i;
			}
		}
		if(nou_primer_torn == true) {
			jugadors_actius.get(0).primerTorn = true;
			nou_primer_torn = false;
			return 0;
		}
		return -1;
	}
	
	
	public static void main(String[] args) {
		Joc joc = new Joc();
//		System.out.println("hola");
		joc.add_jugador("isaac", 10);
		joc.add_jugador("lluis", 12);
		joc.add_jugador("oriol", 14);
//		joc.add_jugador("paula", 18);
//		joc.add_jugador("maria", 5);
//		System.out.println(joc.jugadors_actius.get(0).carta1);
		joc.generar_joc();

	}

}
