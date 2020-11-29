import java.util.*;

public class Partida {
	
	protected ArrayList<Jugador> jugadors;
	protected Joc joc_actiu;
	private boolean nou_primer_torn = true;
	
	
	public Partida() {
		this.jugadors = new ArrayList<>();
	}
	
	private int canviar_primer_torn() {
//		System.out.println(jugadors_actius.size());
		for(int i=0 ; i < jugadors.size() ; i++) {
//			System.out.println(i);
			if(jugadors.get(i).primerTorn == true) {
				jugadors.get(i).primerTorn = false;
				jugadors.get((i+1)%jugadors.size()).primerTorn = true;
				return i+1;
			}
		}
		if(nou_primer_torn == true) {
			jugadors.get(0).primerTorn = true;
			nou_primer_torn = false;
			return 0;
		}
		return -1;
	}
	
	public static void main(String[] args) {
		Partida partida = new Partida();
		int f = 0;
		boolean exit_partida = true;
		int index_torn_absolut;
		partida.jugadors.add(new Jugador("lluis", 10));
		partida.jugadors.add(new Jugador("isaac", 12));
		partida.jugadors.add(new Jugador("oriol", 14));
		
		while(exit_partida) {
			index_torn_absolut = partida.canviar_primer_torn();
			partida.joc_actiu = new Joc(partida.jugadors);
			partida.joc_actiu.generar_joc(index_torn_absolut);
			if(f == 2z) {
				exit_partida = false;
			}else {
				f++;
			}
			
		}

	}
	
}
