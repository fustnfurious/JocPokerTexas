import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class Taula {
	
	public static void main(String[] args) {
		ArrayList<Carta> pub = new ArrayList<>();
		for(int i=0; i<7; i++) {
			int num = ThreadLocalRandom.current().nextInt(1, 13 + 1);
			int pal = ThreadLocalRandom.current().nextInt(1, 4 + 1);
			pub.add(new Carta(num, pal));
			
		}
		/*
		int pal =2;
		pub.add(new Carta(1, pal));
		pub.add(new Carta(10, pal));
		pub.add(new Carta(10, pal));
		pub.add(new Carta(10, pal));
		pub.add(new Carta(10, pal));
		pub.add(new Carta(11, pal));
		pub.add(new Carta(11, pal));*/
	
		Taula taula = new Taula();
		for(int i=0; i<7; i++) {
			System.out.println(pub.get(i).toString());
		}
		System.out.println(taula.escales(pub).toString());
	}
	
	public final int ESCALA_REIAL = 9;
	public final int ESCALA_COLOR = 8;
	public final int POKER = 7;
	public final int FULL = 6;
	public final int COLOR = 5;
	public final int ESCALA = 4;
	public final int TRIO = 3;
	public final int DOBLE_PARELLA = 2;
	public final int PARELLA = 1;
	public final int CARTA_ALTA = 0;
	
	protected ArrayList<Jugador> jugadors;
	protected ArrayList<Carta> baralla;
	protected ArrayList<Carta> pub;
	
	public int guanyador() {
		int guanyador=0;
		Rank guany = rankMa((Ma) jugadors.get(0).getMa(), pub);
		for(int i=1; i<jugadors.size(); i++) {
			if(guany.compareTo(rankMa((Ma) jugadors.get(i).getMa(), pub))<0) {
				guanyador=i;
			}
		}
		return guanyador;
	}
	
	
	public class Ma {
		protected Carta c1;
		protected Carta c2;
	}
	
	
	public class Rank implements Comparable<Rank>{
		protected int rank;
		protected int numCartaAlta;
		
		public Rank(int rank, int num) {
			this.rank=rank;
			this.numCartaAlta=num;
		}
		
		@Override
		public int compareTo(Rank ra) {
			if(this.rank != ra.rank) {
				return this.rank-ra.rank;
			} else {
				return this.numCartaAlta-ra.numCartaAlta;
			}
		}
		
		public String toString() {
			String ran;
			switch(this.rank) {
			case PARELLA: ran="Parella";
			break;
			case DOBLE_PARELLA: ran="Doble Parella";
			break;
			case TRIO: ran="Trio";
			break;
			case ESCALA: ran="Escala";
			break;
			case COLOR: ran="Color";
			break;
			case FULL: ran="Full";
			break;
			case POKER: ran="Poker";
			break;
			case ESCALA_COLOR: ran="Escala Color";
			break;
			case ESCALA_REIAL: ran="Escala Reial";
			break;
			default: ran="Carta Alta";
			}
			return "Ranking: " + ran + "  Carta Alta = " + this.numCartaAlta + "\n";
		}
	}
	
	
	public Rank rankMa(Ma ma, ArrayList<Carta> pub) {
		ArrayList<Carta> tot = new ArrayList<>();
		tot.add(ma.c1);
		tot.add(ma.c2);
		for(int i=0; i<pub.size(); i++) {
			tot.add(pub.get(i));
		}
		ArrayList<Rank> ranks = new ArrayList<>();
		ranks.add(parellesTriosPokersFulls(tot));
		ranks.add(escales(tot));
		ranks.add(color(tot));
		Collections.sort(ranks);
		return ranks.get(ranks.size()-1);
	}
	
	
	
	public Rank parellesTriosPokersFulls (ArrayList<Carta> tot) {
		
		boolean parella=false;
		boolean dobleParella=false;
		boolean trio=false;
		boolean full=false;
		int iguals=0;
		int numCartaAlta=0;
		
		Collections.sort(tot);
		
		for(int i=0; i<tot.size()-1; i++) {
			for(int j=i+1;j<tot.size();j++) {
				if(tot.get(i).getNum()==tot.get(j).getNum()) {
					iguals++;
				} else {
					break;
				}
			}
			switch(iguals) {
			case 1: {
				if(trio) {
					full=true;
				} else if (parella) {
					dobleParella=true;
					if(numCartaAlta<tot.get(i).getNum()) {
						numCartaAlta=tot.get(i).getNum();
					}
				} else {
					parella=true;
					numCartaAlta=tot.get(i).getNum();
				}
			}
			break;
			case 2: {
				if(trio) {
					if(numCartaAlta<tot.get(i).getNum()) {
						numCartaAlta=tot.get(i).getNum();
					}
				} else {
					trio=true;
					numCartaAlta=tot.get(i).getNum();
				}
				if(parella) {
					full=true;
				}
				
			}
			break;
			case 3: {
				return new Rank(POKER, tot.get(i).getNum());
			}
			}
			i+=iguals;
			iguals=0;
			
		}
		
		if(full) {
			return new Rank(FULL, numCartaAlta);
		}
		if(trio) {
			return new Rank(TRIO, numCartaAlta);
		}
		if(dobleParella) {
			return new Rank(DOBLE_PARELLA, numCartaAlta);
		}
		if(parella) {
			return new Rank(PARELLA, numCartaAlta);
		} else {
			return new Rank(CARTA_ALTA, tot.get(6).getNum());
		}
		
	}

	
	public Rank color(ArrayList<Carta> tot) {
		int pal;
		for(int i=0; i<4; i++) {
			pal=0;
			ArrayList<Carta> aux=tot;
			for(int j=0; j<tot.size(); j++) {
				if(tot.get(j).getPal()==i) {
					pal++;
				}
			}
			if(pal>=5) {
				for(int k=0; k<tot.size(); k++) {
					if(aux.get(k).getPal()!=i) {
						aux.remove(k);
					}
				}
				Collections.sort(aux);
				return new Rank(COLOR, aux.get(aux.size()-1).getNum());
			}
		}
		return new Rank(0,0);
	}
	
	
	public Rank escales(ArrayList<Carta> tot) {
		boolean escala=false;
		int cartaAlta;
		for(int i=0; i<tot.size();i++) { //afegit "1" si hi ha assos (as=14 en la resta de funcions)
			if(tot.get(i).getNum()==14) {
				tot.add(new Carta(1, tot.get(i).getPal()));
			}
		}
		Collections.sort(tot);
		cartaAlta = tot.get(tot.size()-1).getNum();
		ArrayList<Carta> cinc = tot;
		
		for(int i=0; i<cinc.size();i++) { //esborrar numeros repetits
			for(int j=i+1; j<cinc.size(); j++) {
				if(cinc.get(i).getNum() == cinc.get(j).getNum()) {
					cinc.remove(i);
					i--;
				} else {
					break;
				}
			}
			
		}
		//trobar escales
		for(int i=cinc.size()-1; i>cinc.size()-4;i--) { //itera nomes 3 vegades (escala de 5 en 7 cartes)
			if(cinc.get(i).getNum()-4 == cinc.get(i-4).getNum()) {
				escala=true;
				cartaAlta=cinc.get(i).getNum();
				break;
			}
		}
		
		if(!escala) {
			return new Rank(CARTA_ALTA, cartaAlta);
		} else {
			//escala color?
			for(int i=0; i<tot.size(); i++) {
				return new Rank(ESCALA, cartaAlta);
			}
			
			if(cinc.get(4).getNum()==14) {
				return new Rank (ESCALA_REIAL, 14);
			} else return new Rank (ESCALA_COLOR, cinc.get(4).getNum());
		}
		
		
		
		/*
		 * 
		 * System.out.println("\n\n");
		 for(int i=0; i<tot.size(); i++) {
			System.out.println(tot.get(i).toString());
		}*/
		
		//return new Rank(0, 0);
	}
	
	
	
	
	
}
