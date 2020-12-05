package PokerModel;
import java.util.*;


public class Partida extends Thread{
	
	public ArrayList<ClientThread> jugadors;
	private Joc joc_actiu;
	private boolean nou_primer_torn = true, game_active;	
	boolean exit_partida = true;
	int index_torn_absolut;
	int f = 0;
	
	
	public Partida() {
		this.jugadors = new ArrayList<>();
	}
		
	private int canviar_primer_torn() {
//		System.out.println(jugadors_actius.size());
		for(int i=0 ; i < jugadors.size() ; i++) {
//			System.out.println(i);
			if(jugadors.get(i).jugador.primerTorn == true) {
				jugadors.get(i).jugador.primerTorn = false;
				jugadors.get((i+1)%jugadors.size()).jugador.primerTorn = true;
				return i+1;
			}
		}
		if(nou_primer_torn == true) {
			jugadors.get(0).jugador.primerTorn = true;
			nou_primer_torn = false;
			return 0;
		}
		return -1;
	}
	
	public void run() {
		while(exit_partida) {
			if(jugadors.size() >= 2 & game_active == false) {
				game_active = true;
				index_torn_absolut = canviar_primer_torn();
				joc_actiu = new Joc(jugadors);
				try {
					joc_actiu.generar_joc(index_torn_absolut);
					Thread.sleep(15000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			} else {
				System.out.println("esperant jugadors...");
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
