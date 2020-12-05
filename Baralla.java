
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Baralla {

	protected ArrayList<Integer> numeros = new ArrayList<>(Arrays.asList(2,3,4,5,6,7,8,9,10,11,12,13,14));
	protected ArrayList<Integer> pals = new ArrayList<>(Arrays.asList(1,2,3,4));
	protected ArrayList<Carta> cartes = new ArrayList<>();
	private Random randomGenerator = new Random();
	
	public Baralla(){
		for(int i=0 ; i < numeros.size() ; i++) {
			for(int j=0 ; j < pals.size() ; j++) {
				cartes.add(new Carta(numeros.get(i), pals.get(j)));
			}
		}
	}
	
	public Carta pick_and_remove_Carta() {
//		System.out.println(cartes.size());
		int index_num = randomGenerator.nextInt(cartes.size());
//		System.out.println(index_num);
		return cartes.remove(index_num);
	}
}
