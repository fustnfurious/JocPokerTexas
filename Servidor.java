
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	protected static final int port = 8888;
	protected static final String s = "localhost";
	protected static ServerSocket ss =  null;
	protected static ObjectOutputStream out;
	protected static ObjectInputStream in;
	
	
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
					String nomClient = (String) ct.in_server.readObject();
					int dinersClient = (Integer) ct.in_server.readObject();
					//Jugador jugador = (Jugador) ct.in_server.readObject();
					Jugador jugadorClient = new Jugador(nomClient, dinersClient);
					ct.jugador = jugadorClient;
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
		} catch (Exception e) {
			try {
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return opcio;
	}
	
	public Jugador getJugador() {
		return this.jugador;
	}

	public ObjectOutputStream getOutput() {
		return this.out_server;
	}
	
	public boolean comprovar_si_them_de_xutar() {
		if(this.jugador.diners == 0) {
			return true;
		}else {
			return false;
		}
	}
}
