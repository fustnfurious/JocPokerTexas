
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static final int port = 8888;
	public static final String s = "localhost";
	public static ServerSocket ss =  null;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	
	
	public static void main(String[] args) {
		Partida partida;
		Socket socket;
		try {
			ss = new ServerSocket(Servidor.port);
			partida = new Partida();
			partida.start();
			
			while(true) {
				socket = ss.accept();
				ClientThread ct = new ClientThread(socket);
				ct.start();
				try {
					Thread.sleep(2000);
					Jugador jugador = (Jugador) ct.in_server.readObject();
					ct.jugador = jugador;
					partida.jugadors.add(ct);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("INFO: "+ct.jugador.getNom() + " - "+ct.jugador.getDiners());
					
			}
			
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
	ObjectInputStream in_server;
	ObjectOutputStream out_server;
	
	public ClientThread(Socket s) {
		this.s = s;
	}
	
	public Jugador getJugador() {
		return this.jugador;
	}
	
	public ObjectOutputStream getOutput() {
		return this.out_server;
	}
	
	@Override
	public void run() {
		try {			
			this.out_server = new ObjectOutputStream(s.getOutputStream());
			this.in_server = new ObjectInputStream(s.getInputStream());

			out_server.writeObject(new String("espera a que comenci el joc.."));
			
			while(true) {}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public int getData() {
		int opcio = -2;
		try {
			opcio = in_server.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return opcio;
	}
}
