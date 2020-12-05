package PokerModel;

public class Carta implements Comparable<Carta> {
	int num;
	int pal;

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
		return num +" - "+tradueix(pal);
	}
	
	public String tradueix(int pal) {
		String nom_pal;
		switch(pal) {
		case 1:
			nom_pal = "pica";
			break;
		case 2:
			nom_pal = "cor";
			break;
		case 3:
			nom_pal = "trebol";
			break;
		case 4:
			nom_pal = "diamant";
			break;
		default:
			nom_pal = "inidentificat";
		}
		return nom_pal;
	}

}
