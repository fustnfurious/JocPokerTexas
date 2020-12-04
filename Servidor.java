package PokerModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
	public static final int port = 8888;
	public static final String s = "localhost";
	public static ServerSocket ss =  null;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	
	
	public static void main(String[] args) {
//		Servidor servidor = new Servidor();
		Partida partida;
		Socket socket;
//		ArrayList<Client> s_list = new ArrayList<>();
		try {
			ss = new ServerSocket(Servidor.port);
			partida = new Partida();
			partida.start();
			
			while(true) {
				socket = ss.accept();
	//			in = new ObjectInputStream(socket.getInputStream());
				ClientThread ct = new ClientThread(socket);
				ct.start();
				try {
					Thread.sleep(2000);
					Jugador jugador = (Jugador) ct.in_client.readObject();
					ct.jugador = jugador;
					partida.jugadors.add(ct);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("INFO: "+ct.jugador.getNom() + " - "+ct.jugador.getDiners());

				
//				if(s_list.size() < 3) {
//					socket = ss.accept();
//					out = new ObjectOutputStream(socket.getOutputStream());
//					in = new ObjectInputStream(socket.getInputStream());
//					try {
//						Client client_a_server = (Client) in.readObject();
//						client_a_server.addIOServer(out, in);
//						partida.jugadors.add(client_a_server);
//						s_list.add(client_a_server);
					
						
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					
				}
//			}
			
		} catch (IOException e) {
			System.out.println("No s'ha pogut inicialitzar -> "+e);
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}

class ClientThread extends Thread {
	Jugador jugador;
	Socket s;
	ObjectInputStream in_client;
	ObjectOutputStream out_client;
	
	public ClientThread(Socket s) {
		this.s = s;
	}
	
	@Override
	public void run() {
		try {
			out_client = new ObjectOutputStream(s.getOutputStream());
			in_client = new ObjectInputStream(s.getInputStream());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			out_client.writeObject(new String("espera a que comenci el joc.."));
			while(true) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
