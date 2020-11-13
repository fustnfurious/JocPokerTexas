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
		pub.add(new Carta(6, pal));
		pub.add(new Carta(9, pal));
		pub.add(new Carta(3, pal));
		pub.add(new Carta(9, pal));
		pub.add(new Carta(9, pal));
		pub.add(new Carta(13, pal));
		pub.add(new Carta(12, pal));*/
		Taula taula = new Taula();
		for(int i=0; i<7; i++) {
			System.out.println(pub.get(i).toString());
		}
		System.out.println(taula.parellesTriosPokersFulls(pub).toString());
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
		for(int i=0; i<jugadors.size(); i++) {
			
		}
		return 0;
	}
	
	public class Ma {
		protected Carta c1;
		protected Carta c2;
	}
	
	public class Rank {
		protected int rank;
		protected int numCartaAlta;
		
		public Rank(int rank, int num) {
			this.rank=rank;
			this.numCartaAlta=num;
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
		
		return new Rank(1,1);
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
	
	
	
	
	/*
	 * 
	 * for(int i=0; i<tot.size(); i++) {
			for(int j=i+1; j<tot.size()-1; j++) {
				if(tot.get(i).getNum()==tot.get(j).getNum()) {
					iguals++;
					switch(iguals) {
					case 1: {
						if(parella) {
							dobleParella=true;
							if(!trio && tot.get(i).getNum()>numCartaAlta) {
								numCartaAlta=tot.get(i).getNum();
							}
							
						} else {
							parella=true;
							if(trio) {
								full=true;
							}
							if(!dobleParella && !trio && tot.get(i).getNum()>numCartaAlta) {
								numCartaAlta=tot.get(i).getNum();
							}
						}
					}
					break;
					case 2: {
						if(parella) {
							full=true;
						}
						if(trio) {
							if(tot.get(i).getNum()>numCartaAlta) {
								numCartaAlta=tot.get(i).getNum();
							}
						} else {
							trio=true;
							numCartaAlta=tot.get(i).getNum();
						}
						
					}
					break;
					case 3: {
						return new Rank(POKER, tot.get(i).getNum());
					}
					}
					
				} else {
					i=iguals;
					iguals=0;
					break;
				}
			}
			
		}
	 */
	
	
	
	
}
