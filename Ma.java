

public class Ma {
	protected Carta carta1;
	protected Carta carta2;
	
	public Ma(Baralla baralla) {
		this.carta1 = baralla.pick_and_remove_Carta();
		this.carta2 = baralla.pick_and_remove_Carta();
	}
	
	public void printMa() {
		System.out.println("Carta: "+carta1.num +" - "+ carta1.pal);
		System.out.println("Carta: "+carta2.num +" - "+ carta2.pal);
	}
	
	public String toString() {
		return "\nCarta: "+carta1 + "\nCarta: "+carta2;
	}
}
