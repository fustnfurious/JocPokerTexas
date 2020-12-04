package PokerModel;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {
	protected Socket socket;
	Jugador jugador_client;
	ObjectInputStream in_client;
	OutputStream out_client;
	ObjectOutputStream out_ob_client;
	boolean exit_player;
	Scanner scanner = new Scanner(System.in);

	public Client(String nom, int diners) throws InterruptedException {
		jugador_client = new Jugador(nom, diners);
		this.exit_player = false;
		try {
			socket = new Socket("localhost", 8888);
			out_client = socket.getOutputStream();
			in_client = new ObjectInputStream(socket.getInputStream());
			Thread.sleep(100);
			out_ob_client = new ObjectOutputStream(out_client);
			out_ob_client.writeObject(jugador_client);
			out_client.flush();
			String line = (String) in_client.readObject();
			System.out.println(line);
			} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(!exit_player) {
			try {
				String st = (String) in_client.readObject();
				System.out.println(st);
				Thread.sleep(1000);
				out_ob_client.writeInt(Integer.parseInt(scanner.nextLine()));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
//	public void addIOServer(ObjectOutputStream out, ObjectInputStream in) {
//		this.out_server = out;
//		this.in_server = in;
//	}
	
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
		}

	}

}

