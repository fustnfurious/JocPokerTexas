package PokerModel;

public class Carta implements Comparable<Carta> {
	protected int num;
	protected int pal;

	public Carta(int num, int pal) {
		this.num = num;
		this.pal = pal;
	}
	
	public int getNum() {
		return this.num;
	}
	
	public int getPal() {
		return this.pal;
	}

	@Override
	public int compareTo(Carta car) {
		return this.num-car.num;
	}
	
	public String toString() {
		return numToString() +" de "+palToString();
	}
	
	public String palToString(){
		String nom_pal;
		switch(this.pal) {
		case 1:
			nom_pal = "Piques";
			break;
		case 2:
			nom_pal = "Cors";
			break;
		case 3:
			nom_pal = "Trebols";
			break;
		case 4:
			nom_pal = "Diamants";
			break;
		default:
			nom_pal = "inidentificat";
		}
		return nom_pal;
	}
	
	public String numToString() {
		switch(this.num) {
		case 14: return "As";
		case 13: return "K";
		case 12: return "Q";
		case 11: return "J";
		default: return ""+num;
		}
	}

}
