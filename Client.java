

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {
	protected Socket socket;
	protected Jugador jugador_client;
	protected ObjectInputStream in_client;
	protected ObjectOutputStream out_client;
	
	private boolean exit_player, mala_opcio;
	private int opcio;
	private Scanner scanner = new Scanner(System.in);

	public Client(String nom, int diners) throws InterruptedException {
		jugador_client = new Jugador(nom, diners);
		this.exit_player = false;
		try {
			socket = new Socket("localhost", 8888);
			in_client = new ObjectInputStream(socket.getInputStream());
			out_client = new ObjectOutputStream(socket.getOutputStream());
			
			out_client.writeObject(jugador_client);
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
				boolean joc_actiu = true;
				while(joc_actiu) {
					String situacio_ronda = (String) in_client.readObject();
					System.out.println(situacio_ronda);
					Thread.sleep(400);
					ronda();
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
		case View.SENSE_APOSTA:
			while(mala_opcio) {
				System.out.println(View.ST_SENSE_APOSTA);
				opcio = scanner.nextInt();
				Thread.sleep(1000);
				out_client.writeInt(opcio);
				out_client.flush();
				mala_opcio = false;
				switch(opcio) {
				case 1:
					System.out.println(View.ST_PASSAR);
					break;
				case 2:
					do {
						System.out.println(View.ST_APOSTAR);
						opcio = scanner.nextInt();
						Thread.sleep(1000);
						out_client.writeInt(opcio);
						out_client.flush();
					}while(in_client.readInt() == View.MALA_APOSTA);
						
					break;
				case 3:
					System.out.println(View.ST_RETIRAR);
					break;
					
				default:
					System.out.println(View.ST_MALA_OPCIO);
					mala_opcio = true;
				}
			}
			break;
			
		case View.AMB_APOSTA:
			while(mala_opcio) {
				System.out.println(View.ST_AMB_APOSTA);
				opcio = scanner.nextInt();
				Thread.sleep(1000);
				out_client.writeInt(opcio);
				out_client.flush();
				mala_opcio = false;
				switch(opcio) {
				case 1:
					System.out.println(View.ST_IGUALAR);
					break;
				case 2:
					do {
						System.out.println(View.ST_APOSTAR);
						opcio = scanner.nextInt();
						Thread.sleep(1000);
						out_client.writeInt(opcio);
						out_client.flush();
					}while(in_client.readInt() == View.MALA_APOSTA);
						
					break;
				case 3:
					System.out.println(View.ST_RETIRAR);
					break;
					
				default:
					System.out.println(View.ST_MALA_OPCIO);
					mala_opcio = true;
				}
			}
			break;
		case View.AMB_ALLIN:
			while(mala_opcio) {
				System.out.println(View.ST_AMB_ALLIN);
				opcio = scanner.nextInt();
				Thread.sleep(1000);
				out_client.writeInt(opcio);
				out_client.flush();
				mala_opcio = false;
				switch(opcio) {
				case 1:
					System.out.println(View.ST_AMB_ALLIN);
					break;
				case 2:
					System.out.println(View.ST_RETIRAR);
					break;
				default:
					System.out.println(View.ST_MALA_OPCIO);
					mala_opcio = true;
				}
			}
			break;
		case View.INFO_GUANYADORS_INCOMING: 
			String info = (String) in_client.readObject();
			System.out.println(info);
			System.out.println(View.SEG_RONDA);
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
			Client client = new Client(input, diners_inicials);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

	}

}

