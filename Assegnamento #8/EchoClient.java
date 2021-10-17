import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author Simone Schiavone		MAT: 582418
 */

public class EchoClient {
	private final static int PORT=6789;
	private final static int BUFFERDIM=1024;
	
	public static void main(String[] args) {
		InetAddress ip=null;
		try {
			ip= InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		InetSocketAddress address=new InetSocketAddress(ip,PORT);
		SocketChannel SChannel;
		Scanner scanner=new Scanner(System.in);
		//I SocketChannel sono associati ad un oggetto di tipo Socket e sono utilizzati
		//per la comunicazione tra client e server. Il SocketChannel è implicitamente 
		//creato se si accetta una connessione su un ServerSocketChannel. Lato client va
		//esplicitamente creato quando apriamo una connessione verso un server mediante 
		//l'operazione connect()
		try {
			SChannel = SocketChannel.open();
			try {
				SChannel.connect(address);
			}catch(ConnectException ce) {
				System.out.println("CONNESSIONE RIFIUTATA! Il server è chiuso.");
				scanner.close();
				return;
			}
			SChannel.configureBlocking(true); //Modalità BLOCCANTE settata
			
			while(true) {
				System.out.println("EchoClient: Collegato a "+SChannel.getLocalAddress());
				System.out.println("EchoClient: Scrivere il messaggio da inviare! "
						+ "Un messaggio 'STOP' comporta la chiusura del server e del client");
				//Scanner scanner=new Scanner(System.in);
				String message=scanner.nextLine();
				System.out.println("HAI SCRITTO: "+message);
				
				ByteBuffer bufferout=ByteBuffer.wrap(message.getBytes());
				bufferout.clear(); //Preparo il buffer alla lettura dopo la scrittura
				System.out.println("EchoClient: Inizio la scrittura del messaggio sul canale verso il server");
				while(bufferout.hasRemaining()) {
					System.out.println("EchoClient: Ho scritto "+SChannel.write(bufferout)+" bytes di messaggio");
				}
				
				System.out.println("EchoClient: Scrittura effettuata. Attendo la risposta del server");
				bufferout.flip(); //Preparo il buffer alla scrittura dopo la lettura
				StringBuilder response=new StringBuilder(""); //Stringa vuota
				
				boolean terminato=false;
				ByteBuffer bufferin=ByteBuffer.allocateDirect(BUFFERDIM);
				while(!terminato) {
					int readbytes=SChannel.read(bufferin);
					System.out.println("EchoClient: Ho letto "+readbytes+" bytes dal canale");
					bufferin.flip();
					while(bufferin.hasRemaining()) {
						response.append((char)bufferin.get());				
					}
					if(readbytes<BUFFERDIM) //Significa che ho letto tutti i dati sul canale
						terminato=true;
				}
				System.out.printf("EchoClient: Ho ricevuto la risposta del server-->\n"+response.toString()+"\n");
				
				if(response.toString().equals("STOP...ECHOED BY SERVER")) {
					System.out.println("EchoClient: hai ordinato la chiusura del client e del server.");
					scanner.close();
					break;	//Chiudiamo il client ed il server
				}
				
			}
			System.out.println("---CLIENT TERMINATO---");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		

	}

}
