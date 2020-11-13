
public class Jugador {
	
	protected Carta carta1;
	protected Carta carta2;
	protected int diners;
	
	public Object getMa() {
		return new Object() {
			Carta c1 = carta1;
			Carta c2 = carta2;
		};
	}
	
	
}
