

import java.io.Serializable;

public class Jugador implements Serializable{
	protected Ma ma;
	protected int diners;
	protected int diners_apostats_enjoc;
	protected boolean primerTorn;
	protected String nom;
	protected boolean is_playing;

	public Jugador(String nom) {
		this.nom = nom;
		this.primerTorn = false;
	}
	
	public Jugador(String nom, int diners) {
		this.nom = nom;
		this.primerTorn = false;
		this.diners = diners;
		this.diners_apostats_enjoc = 0;
	}
	
	public void nova_Ma(Baralla baralla) {
		this.ma = new Ma(baralla);
	}
	
	public void printMa() {
		this.ma.printMa();
	}

	public Ma getMa() {
		return this.ma;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public int apostar(int aposta) {
		this.diners -= aposta;
		return this.diners;
	}
	
	public void fold() {
		this.is_playing = false;
	}

	public int getDiners() {
		return this.diners;
	}
	
	public void rebreDiners(int dinersGuanyats) {
		this.diners += dinersGuanyats;
	}
	
	public void updateDiners_apostats(int diners) {
		this.diners_apostats_enjoc = diners;
	}

	public int getDinersApostats() {
		return this.diners_apostats_enjoc;
	}
	
}
