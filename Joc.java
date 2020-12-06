import java.io.IOException;
import java.util.*;


public class Joc{
	
	protected ArrayList<ClientThread> jugadors_actius = new ArrayList<>();
	protected ArrayList<ClientThread> jugadors_inici = new ArrayList<>();
	private Taula tauler = new Taula();
	private Baralla baralla;
	private int index_torn_relatiu;
	protected View view;
	
	public Joc(ArrayList<ClientThread> jugadors_inicials){
		this.baralla = new Baralla();
		this.jugadors_actius = new ArrayList<ClientThread>(jugadors_inicials);
		this.jugadors_inici = new ArrayList<ClientThread>(jugadors_inicials);
		this.view = new View();
	}
	
	
	public int torn_actiu(ClientThread client) throws Exception{
		int output = -1;
		client.out_server.writeObject(view.printInfo_Jugador(client, tauler));
		client.out_server.flush();
		Thread.sleep(200);
		if(client.jugador.getDiners() != 0) {
			client.out_server.writeInt(View.SENSE_APOSTA);
			client.out_server.flush();
		}else {
			client.out_server.writeInt(View.SENSE_DINERS);
			client.out_server.flush();
		}
		
		if(client.jugador.getDiners() != 0) {
			while(output == -1) {
				Thread.sleep(100);
				output = client.getData();
				
				switch(output) {
				case 1:
					System.out.println(View.ST_PASSAR);
					break;
					
				case 2: 
					boolean apostat_correctament = false;
					while(apostat_correctament == false) {
						output = client.getData();
						
					    //Comprova que tingui diners per la aposta
					    if(output <= client.jugador.getDiners()) {
					    	apostat_correctament = true;
					    	try {
								client.out_server.writeInt(View.BONA_APOSTA);
								client.out_server.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
					    }else{
					    	System.out.println( "No ha apostat be. Pots apostar fins: "+client.jugador.getDiners());
					    	try {
								client.out_server.writeInt(View.MALA_APOSTA);
								client.out_server.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
					    }
					}
					//retiro els diners del jugador i li faig un update del que porta apostat a la taula
					int diners_restants = client.jugador.apostar(output);
					client.jugador.updateDiners_apostats(output);
					System.out.println(client.jugador.getNom()+" ha apostat: "+output);
					System.out.println(client.jugador.getNom()+" li queden: "+diners_restants);
					
					//afegeixo els diners sobre la taula i actualitzo la aposta
					tauler.afegir_diners_taula(output);
					tauler.update_aposta_activa(output);
					System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats.");
	
					return 1;//indica que hi ha una nova sequencia de torns
					
				case 3:
					System.out.println(View.ST_RETIRAR);
					remove_jugador_actiu(client.jugador.getNom());
					return 2;
					
				default:
					output = -1;
				}
			}
		} else {
			System.out.println("Passant torn");
		}
		return 0;//indica que no hi ha una nova sequencia de torns
	}
	
	public int torn_actiu(ClientThread client, int aposta_actual) throws Exception{
		int output = -1;
		while(output == -1) {
			//separo dos casos, nomes puc fer all-in o puc aumentar la aposta
			if(client.jugador.getDiners() == 0) {
				client.out_server.writeObject(view.printInfo_Jugador(client, tauler));
				client.out_server.flush();
				Thread.sleep(200);
				client.out_server.writeInt(View.SENSE_DINERS);
				client.out_server.flush();
				Thread.sleep(100);
				return 101;
				
			}else if(client.jugador.getDiners() > aposta_actual - client.jugador.getDinersApostats()) {
				
				client.out_server.writeObject(view.printInfo_Jugador(client, tauler));
				client.out_server.flush();
				Thread.sleep(200);
				client.out_server.writeInt(View.AMB_APOSTA);
				client.out_server.flush();
				Thread.sleep(100);
				
				output = client.getData();
				
				switch(output) {
				//igualar la aposta
				case 1:
					System.out.println("Has igualat la aposta");
					System.out.println("Tenies apostat: "+client.jugador.getDinersApostats());
					
					//afegir diners a la taula
					tauler.afegir_diners_taula(aposta_actual - client.jugador.getDinersApostats());
					System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats en total.");
					
					//retirar els diners del jugador i update de la seva aposta
					client.jugador.apostar(aposta_actual - client.jugador.getDinersApostats());
					client.jugador.updateDiners_apostats(aposta_actual);
					
					System.out.println("Et queden: "+client.jugador.getDiners()+", i ara tens apostats: "+client.jugador.getDinersApostats());
					
					break;
					
				case 2:  
					output = 0;
					boolean apostat_correctament = false;
					while(apostat_correctament == false) {
						output = client.getData();
					    
					    //comprovar que l'aposta arriba a igualar (al menys) i que tens suficients diners
					    if(output <= client.jugador.getDiners() && output >= aposta_actual - client.jugador.getDinersApostats()) {
					    	apostat_correctament = true;
					    	try {
								client.out_server.writeInt(View.BONA_APOSTA);
								client.out_server.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
					    }else{
					    	System.out.println( "No ha apostat be. Pots apostar fins: "+client.jugador.getDiners());
					    	try {
								client.out_server.writeInt(View.MALA_APOSTA);
								client.out_server.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
					    }
					}
					// es retiren els diners del jugador i s'actualitza la seva aposta
					System.out.println(client.jugador.getNom()+" ha apostat: "+(output + client.jugador.getDinersApostats()));
					int diners_restants = client.jugador.apostar(output);
					client.jugador.updateDiners_apostats(client.jugador.getDinersApostats() + output);
					System.out.println(client.jugador.getNom()+" li queden: "+diners_restants);
					
					//actualitzar diners a la taula + l'aposta actual
					tauler.afegir_diners_taula(output);
					tauler.update_aposta_activa(client.jugador.getDinersApostats());
					System.out.println("Hi ha "+tauler.get_diners_taula()+" diners apostats en total.");
					
					return 1; //indica que hi ha una nova sequencia de torns, s'ha apostat
					
				case 3:
					System.out.println(View.ST_RETIRAR);
					remove_jugador_actiu(client.jugador.getNom());
					return 2;
					
				default:
					output = -1;
				}
			}else {
				client.out_server.writeObject(view.printInfo_Jugador(client, tauler));
				client.out_server.flush();
				Thread.sleep(200);
				client.out_server.writeInt(View.AMB_ALLIN);
				client.out_server.flush();
				Thread.sleep(100);

				output = client.getData();
				switch(output) {
				case 1:
					System.out.println(client.jugador.getNom()+" ha fet all-in.");
					tauler.afegir_diners_taula(client.jugador.getDiners());
					client.jugador.apostar(client.jugador.getDiners());
					break;
					
				case 2:
					System.out.println(View.ST_RETIRAR);
					remove_jugador_actiu(client.jugador.getNom());
					return 2;
					
				default:
					output = -1;
				}
			}
		}
		return 0; //indica que no hi ha una nova sequencia de torns
	}
	
	//retorna false si hi ha nous torns d'aposta - true si es canvia de torn
	public boolean sequencia_torns(int index_torn_actiu, boolean aposta) throws Exception {
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
				} catch (IOException e) {
					jugadors_actius.get(index_torn_actiu_real).s.close();
					jugadors_inici.remove(index_torn_actiu_real);
					jugadors_actius.remove(index_torn_actiu_real);
				}
			}
		}else if(aposta == true){
			for(int i=index_torn_actiu + 1 ; i < index_torn_actiu + jugadors_actius.size(); i++) {
				int index_torn_actiu_real = i%jugadors_actius.size();
				int moviment = torn_actiu(jugadors_actius.get(index_torn_actiu_real), tauler.get_aposta_activa());
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
		//Fa un check de quantes cartes hi ha a la taula per a tothom i en funcio fa una cosa o una altra
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
	
	private void donar_cartes_jugadors() {
		for(int i=0 ; i < jugadors_actius.size() ; i++ ) {
			jugadors_actius.get(i).jugador.nova_Ma(baralla);
		}
	}
	
	private void remove_jugador_actiu(String nom) {
		String iter_name;
		for (int i=0 ; i < jugadors_actius.size() ; i++) {
			iter_name = jugadors_actius.get(i).jugador.getNom();
			if(iter_name == nom) {
				this.jugadors_actius.remove(i);
				break;
			}
		}
	}
	
	
	
	public void generar_joc(int index_torn_absolut) throws Exception {
		boolean nou_torn, check_guanyador = true, aposta = false;
		
		donar_cartes_jugadors();
		
		for(int i=0 ; i < jugadors_actius.size() ; i++) {
			jugadors_actius.get(i).out_server.writeObject(view.printCartes_Jugador(jugadors_actius.get(i), tauler));
			jugadors_actius.get(i).out_server.flush();
			Thread.sleep(300);
		}
		
		for(int j=0; j<4 ; j++) {
			do {
				if(aposta == false) {
					nou_torn = sequencia_torns(index_torn_absolut, aposta);
					aposta = true;
				}else{
					nou_torn = sequencia_torns(index_torn_relatiu, aposta);
				}
			}while(!nou_torn);
			if(check_lastone()) {
				check_guanyador = false;
				if(jugadors_actius.size() == 0) {
					jugadors_actius.get(0).jugador.rebreDiners(tauler.get_diners_taula());
					break;
				}
				
			}
			if(j==3) {
				check_guanyador = true;
			}
			flush_apostes();
			aposta = false;
			donar_cartes_taula();
			tauler.update_aposta_activa(0);
			view.print_cartes_taula(tauler);
		}
		if(check_guanyador) {
			ArrayList<ClientThread> guanyadors = tauler.guanyador(jugadors_actius , tauler.cartes_sobre_taula);
			int diners_guanyador = tauler.get_diners_taula()/guanyadors.size();
			String info_guanyadors = "\nGuanyador(s):\n";
			for(int i=0; i<guanyadors.size(); i++) {
				guanyadors.get(i).getJugador().rebreDiners(diners_guanyador); //repartir diners
				info_guanyadors+= "- "+guanyadors.get(i).getJugador().getNom()+"\n";
			}
			info_guanyadors+= tauler.rankMa(guanyadors.get(0).getJugador().getMa(), tauler.getCartesTaula()).toString()+"\n";
			for(int i=0; i<jugadors_inici.size(); i++) {
				jugadors_actius.get(i).getOutput().writeObject(View.ST_FINALITZACIO);
				jugadors_actius.get(i).getOutput().flush();
				jugadors_actius.get(i).getOutput().writeInt(View.INFO_GUANYADORS_INCOMING);
				jugadors_actius.get(i).getOutput().flush();
				jugadors_actius.get(i).getOutput().writeObject(info_guanyadors);
				jugadors_actius.get(i).getOutput().flush();
			}
			tauler.reset_diners_taula();
		}
	}
}
