

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
	
	public static final int PASSAR = 1;
	public static final int APOSTAR = 2;
	public static final int RETIRAR = 3;
	public static final int IGUALAR = 4;
	public static final int SENSE_APOSTA = 5;
	public static final int AMB_APOSTA = 6;
	public static final int AMB_ALLIN = 7;
	public static final int MALA_APOSTA = 8;
	public static final int BONA_APOSTA = 9;
	public static final int SENSE_DINERS = 10;
	public static final int INFO_GUANYADORS_INCOMING = 11;
	
	public static final String ST_PASSAR = "Passant torn..."; 
	public static final String ST_APOSTAR = "Quant vols apostar?";
	public static final String ST_RETIRAR = "T'has retirat.";
	public static final String ST_IGUALAR = "Has igualat la aposta";
	public static final String ST_SENSE_APOSTA = "\nEl teu torn. Selecciona amb el numero.\n(1)Passar\n(2)Apostar\n(3)Retirarse";
	public static final String ST_AMB_APOSTA = "El teu torn. Selecciona amb el numero.\n(1)Igualar\n(2)Apostar\n(3)Retirarse";
	public static final String ST_AMB_ALLIN = "El teu torn. Selecciona amb el numero.\n(1)All-in\n(2)Retirar-se";
	public static final String ST_MALA_APOSTA = "No tens tants diners. Redueix la aposta.";
	public static final String ST_MALA_OPCIO = "No has triat cap opcio correcta";
	public static final String ST_SENSE_DINERS = "Ja no pots apostar, es passa el teu torn";
	public static final String ST_FINALITZACIO = "\nS'ha acabat el joc";
	public static final String ST_SEG_RONDA = "Passant a la seguent ronda...";
	
	protected Socket socket;
	protected ObjectInputStream in_client;
	protected ObjectOutputStream out_client;
	
	private boolean exit_player, mala_opcio, joc_actiu;
	private int opcio;
	private Scanner scanner = new Scanner(System.in);

	public void crearClient(String nom, int diners, String adr) throws InterruptedException {
		this.exit_player = false;
		try {
			socket = new Socket(adr, 8888);
			in_client = new ObjectInputStream(socket.getInputStream());
			out_client = new ObjectOutputStream(socket.getOutputStream());
			
			out_client.writeObject(nom);
			out_client.flush();
			out_client.writeObject(diners);
			out_client.flush();
			
			String line = (String) in_client.readObject();
			System.out.println(line);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(!exit_player) {
			try {
				String situacio_inicial = (String) in_client.readObject();
				System.out.println(situacio_inicial);
				joc_actiu = true;
				while(joc_actiu) {
					String situacio_ronda = (String) in_client.readObject();
					System.out.println(situacio_ronda);
					Thread.sleep(400);
					ronda();
					if(joc_actiu == false) {
						break;
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	//cada cop que el jugador te un torn
	private void ronda() throws Exception{
		opcio = in_client.readInt();
		mala_opcio = true;
		switch(opcio) {
		case SENSE_APOSTA:
			while(mala_opcio) {
				System.out.println(ST_SENSE_APOSTA);
				opcio = scanner.nextInt();
				Thread.sleep(1000);
				out_client.writeInt(opcio);
				out_client.flush();
				mala_opcio = false;
				switch(opcio) {
				case 1:
					System.out.println(ST_PASSAR);
					break;
				case 2:
					do {
						System.out.println(ST_APOSTAR);
						opcio = scanner.nextInt();
						Thread.sleep(1000);
						out_client.writeInt(opcio);
						out_client.flush();
					}while(in_client.readInt() == MALA_APOSTA);
						
					break;
				case 3:
					System.out.println(ST_RETIRAR);
					break;
					
				default:
					System.out.println(ST_MALA_OPCIO);
					mala_opcio = true;
				}
			}
			break;
			
		case AMB_APOSTA:
			while(mala_opcio) {
				System.out.println(ST_AMB_APOSTA);
				opcio = scanner.nextInt();
				Thread.sleep(1000);
				out_client.writeInt(opcio);
				out_client.flush();
				mala_opcio = false;
				switch(opcio) {
				case 1:
					System.out.println(ST_IGUALAR);
					break;
				case 2:
					do {
						System.out.println(ST_APOSTAR);
						opcio = scanner.nextInt();
						Thread.sleep(1000);
						out_client.writeInt(opcio);
						out_client.flush();
					}while(in_client.readInt() == MALA_APOSTA);
						
					break;
				case 3:
					System.out.println(ST_RETIRAR);
					break;
					
				default:
					System.out.println(ST_MALA_OPCIO);
					mala_opcio = true;
				}
			}
			break;
		case AMB_ALLIN:
			while(mala_opcio) {
				System.out.println(ST_AMB_ALLIN);
				opcio = scanner.nextInt();
				Thread.sleep(1000);
				out_client.writeInt(opcio);
				out_client.flush();
				mala_opcio = false;
				switch(opcio) {
				case 1:
					System.out.println(ST_AMB_ALLIN);
					break;
				case 2:
					System.out.println(ST_RETIRAR);
					break;
				default:
					System.out.println(ST_MALA_OPCIO);
					mala_opcio = true;
				}
			}
			break;
		case SENSE_DINERS:
			System.out.println(ST_SENSE_DINERS);
			break;
			
		case INFO_GUANYADORS_INCOMING: 
			String info = (String) in_client.readObject();
			System.out.println(info);
			System.out.println(ST_SEG_RONDA);
			joc_actiu = false;
			break;
			
		
		}
		
	}
	
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Com et dius?");
		String input = scanner.nextLine();
		System.out.println("Quants diners vols posar?");  
		int diners_inicials = scanner.nextInt();
		
		
		try {
			Client client = new Client();
			client.crearClient(input, diners_inicials, args[0]);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.socket.close()
			client.in_client.close()
			client.out_client.close()
			scanner.close();
		}

	}

}

