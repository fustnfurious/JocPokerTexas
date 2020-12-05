package PokerModel;

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
	
	public static final String ST_PASSAR = "Passant torn..."; 
	public static final String ST_APOSTAR = "Quant vols apostar?";
	public static final String ST_RETIRAR = "T'has retirat.";
	public static final String ST_IGUALAR = "Has igualat la aposta";
	public static final String ST_SENSE_APOSTA = "\nEl teu torn. Selecciona amb el numero.\n(1)Passar\n(2)Apostar\n(3)Retirarse";
	public static final String ST_AMB_APOSTA = "El teu torn. Selecciona amb el numero.\n(1)Igualar\n(2)Apostar\n(3)Retirarse";
	public static final String ST_AMB_ALLIN = "El teu torn. Selecciona amb el numero.\n(1)All-in\n(2)Retirar-se";
	public static final String ST_MALA_APOSTA = "No tens tants diners. Redueix la aposta.";
	public static final String ST_MALA_OPCIO = "No has triat cap opció correcta";
	
	public View() {
	}
	
	public String printCartes_Jugador(ClientThread client, Taula tauler) {
		String cartes = "**********************"
				+client.jugador.ma
				+"\n**********************";
		return cartes;
	}
	
	public String printInfo_Jugador(ClientThread p1, Taula tauler) {
		String info = "***********************"
				+p1.jugador.ma
				+"\n------------------------"
				+"\nTAULA";
		for(int i=0 ; i < tauler.cartes_sobre_taula.size() ; i++) {
			info = info + "\nCarta: "+tauler.cartes_sobre_taula.get(i);
		}
		info = info + "\n------------------------"
				+ "\n-Diners disponibles: "+ p1.jugador.getDiners()
				+"\n-Diners apostats: "+ p1.jugador.getDinersApostats()
				+"\n-Aposta activa: "+ tauler.get_aposta_activa()
				+"\n-Diners sobre la taula: "+tauler.get_diners_taula()
				+"\n***********************\n";
		
		return info;
	}
}
