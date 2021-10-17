import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * @author Simone Schiavone		MAT: 582418
 */

public class TimeClient {
	private static int port=6789;
	private static String host;
	
	public static void main(String[] args) {
		//Prendiamo dalla linea di comando l'indirizzo IP di dategroup
		if(args.length==1) {
			host=args[0];
		}else {
			System.out.println("TIMEClient: Deve essere inserito un indirizzo IP");
			return;
		}
		
		InetAddress ip=null;
		try {
			ip= InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("TIMEClient: Unknown Host Exception!");
		} 
		
		//Creiamo la MulticastSocket ed uniamoci al gruppo
		try(MulticastSocket multicastsocket=new MulticastSocket(port)){
			//prima di fare la join controlliamo che l'indirizzo inserito sia un MulticastAddress
			if(ip.isMulticastAddress()) {
				System.out.println("TIMEClient: L'indirizzo "+host+" è un multicast address");
			}else {
				System.out.println("TIMEClient: L'indirizzo inserito NON è un multicast address. Deve essere del tipo 224-239.x.x.x; Chiudo il client");
				multicastsocket.close(); 
				return;
			}
			multicastsocket.joinGroup(ip);
			
			byte[] buffer=new byte[1024];
			DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
			
			for(int i=0;i<10;i++) {
				multicastsocket.receive(packet);
				System.out.println("TIMEClient: Ricezione Pacchetto nr->"+(i+1));
				String messagereceived=new String(packet.getData(),0,packet.getLength());
				System.out.println(messagereceived);
			}
			
			System.out.println("TIMEClient: Ho ricevuto 10 pacchetti. Chiudo il programma");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
