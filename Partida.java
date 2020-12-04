package PokerModel;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.*;


public class Partida extends Thread{
	
	public ArrayList<ClientThread> jugadors;
	private Joc joc_actiu;
	private boolean nou_primer_torn = true;	
	boolean exit_partida = true;
	private Servidor ss;
	int index_torn_absolut;
	int f = 0;
	
	
	public Partida() {
		this.jugadors = new ArrayList<>();
	}
	
	public Partida(Servidor ss) {
		this.jugadors = new ArrayList<>();
		this.ss = ss;
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
			if(jugadors.size() >= 1) {
				index_torn_absolut = canviar_primer_torn();
				joc_actiu = new Joc(jugadors);
				joc_actiu.generar_joc(index_torn_absolut);
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
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
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		boolean exit_partida = true;
		int index_torn_absolut;
		int f = 0;
		Partida partida = new Partida();
//		partida.jugadors.add(new Jugador("lluis", 10));
//		partida.jugadors.add(new Jugador("isaac", 12));
//		partida.jugadors.add(new Jugador("oriol", 14));
//		partida.jugadors.add(new Jugador("helena", 14));
//		partida.jugadors.add(new Jugador("julia", 14));
//		partida.jugadors.add(new Jugador("sandra", 14));
//		PlayerInterface p1 = new PlayerInterface(partida.jugadors.get(0));
		
	}
}
