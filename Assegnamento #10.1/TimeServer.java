import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Simone Schiavone		MAT: 582418
 */

public class TimeServer {
	private static int port=6789;
	private static String host;
	
	public static void main(String[] args) {
		//Prendiamo dalla linea di comando l'indirizzo IP di dategroup
		if(args.length==1) {
			host=args[0];
		}else {
			System.out.println("TIMEServer: Deve essere inserito un indirizzo IP");
			return;
		}
		
		InetAddress ip=null;
		try {
			ip= InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("TIMEServer: Unknown Host Exception!");
		} 
		
		//Per spedire i messaggi dobbiamo creare una DatagramSocket su una porta anonima
		//Non è necessario collegare il socket ad un gruppo multicast (solo per inviare)
		//Creare un pacchetto inserendo nell'intestazione l'indirizzo del gruppo multicast
		//a cui si vuol inviare un pacchetto. Spedire il paccheto tramite il socket creato.
		
		try(DatagramSocket socket=new DatagramSocket()) {
			//prima di inviare il pacchetto controlliamo che l'indirizzo sia un multicast address
			if(ip.isMulticastAddress()) {
				System.out.println("TIMEServer: L'indirizzo "+host+" è un multicast address");
			}else {
				System.out.println("TIMEServer: L'indirizzo inserito NON è un multicast address. Deve essere del tipo 224-239.x.x.x; Chiudo il server");
				socket.close(); 
				return;
			}
			
			DatagramPacket packet=null;
			SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss z",Locale.ITALY);
			
			while(true) {
				Date now=new Date(); //viene incapsulata la data e l'ora di allocazione dell'oggetto
				String timestamp=sdf.format(now);
				
				packet=new DatagramPacket(timestamp.getBytes(),timestamp.length(),ip,port);
				try {
					socket.send(packet);
					System.out.println("TIMESERVER: pacchetto inviato");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				long sleepingtime=(long)(Math.random()*5000); //tempo max di attesa 5 sec
				
				try {
					Thread.sleep(sleepingtime);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		
	}

}
