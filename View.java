

public class View {
	public static final int PASSAR = 1;
	public static final int APOSTAR = 2;
	public static final int RETIRAR = 3;
	public static final int IGUALAR = 4;
	public static final int SENSE_APOSTA = 5;
	public static final int AMB_APOSTA = 6;
	public static final int AMB_ALLIN = 7;
	public static final int MALA_APOSTA = 8;
	public static final int BONA_APOSTA = 9;
	public static final int SENSE_DINERS = 10;
	public static final int INFO_GUANYADORS_INCOMING = 11;
	public static final int ULTIM = 12;
	
	public static final String ST_PASSAR = "Passant torn..."; 
	public static final String ST_APOSTAR = "Quant vols apostar?";
	public static final String ST_RETIRAR = "T'has retirat.";
	public static final String ST_IGUALAR = "Has igualat la aposta";
	public static final String ST_SENSE_APOSTA = "\nEl teu torn. Selecciona amb el numero.\n(1)Passar\n(2)Apostar\n(3)Retirarse";
	public static final String ST_AMB_APOSTA = "El teu torn. Selecciona amb el numero.\n(1)Igualar\n(2)Apostar\n(3)Retirarse";
	public static final String ST_AMB_ALLIN = "El teu torn. Selecciona amb el numero.\n(1)All-in\n(2)Retirar-se";
	public static final String ST_MALA_APOSTA = "No tens tants diners. Redueix la aposta.";
	public static final String ST_MALA_OPCIO = "No has triat cap opcio correcta";
	public static final String ST_SENSE_DINERS = "Ja no pots apostar, es passa el teu torn";
	public static final String ST_FINALITZACIO = "\nS'ha acabat el joc";
	public static final String ST_SEG_RONDA = "Passant a la seguent ronda...";
	
	public View() {
	}
	
	public String printCartes_Jugador(ClientThread client, Taula tauler) {
		String cartes = "\n**********************"
				+"\u001B[32m"
				+client.jugador.ma
				+"\u001B[0m"
				+"\n**********************";
		return cartes;
	}
	
	public String printInfo_Jugador(ClientThread p1, Taula tauler) {
		String info = "***********************"
				+"\u001B[32m"
				+p1.jugador.ma
				+"\u001B[0m"
				+"\n------------------------"
				+"\nTAULA";
		for(int i=0 ; i < tauler.cartes_sobre_taula.size(); i++) {
			info = info + "\nCarta: "+tauler.cartes_sobre_taula.get(i);
		}
		info = info + "\n------------------------";
		if(p1.jugador.getDiners()==0) {
			info+="\u001B[31m"
				+"\n-Diners disponibles: "+ p1.jugador.getDiners()
				+"\u001B[0m";
		} else {
			info+="\n-Diners disponibles: "+ p1.jugador.getDiners();
		}
			info+="\n-Diners apostats: "+ p1.jugador.getDinersApostats()
				+"\n-Ultima aposta: "+ tauler.get_aposta_activa()
				+"\n-Diners sobre la taula: "+tauler.get_diners_taula()
				+"\n***********************\n";
		
		return info;
	}
	
	protected void print_cartes_taula(Taula tauler) {
		System.out.println("\n----------------------------------");
		for(int i=0 ; i < tauler.cartes_sobre_taula.size() ; i++ ) {
			System.out.println("Carta "+(i+1)+" -> "+tauler.cartes_sobre_taula.get(i));
		}
		System.out.println("----------------------------------\n");
	}
}
