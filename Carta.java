
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
		return "Num: " + num +" Pal: "+pal;
	}

}
