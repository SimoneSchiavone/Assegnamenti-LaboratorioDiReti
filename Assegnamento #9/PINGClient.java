import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
/**
 * @author Simone Schiavone		MAT: 582418
 */
public class PINGClient {
	private static String SERVERNAME;
	private static int SERVERPORT;
	
	public static void main(String[] args) {
		//Imposto le variabili necessarie ad effettuare le statistiche
		long maxdelay=Long.MIN_VALUE;
		long mindelay=Long.MAX_VALUE;
		int receivedpackets=0;
		int sentpackets=0;
		float avgdelay=0;
		
		//Prendo gli argomenti dalla linea di comando e ne verifico la correttezza
		if(args.length==2) { 
			try{
				SERVERPORT = Integer.valueOf(args[0]).intValue();
				System.out.println("PINGClient: Porta server letta -->"+SERVERPORT);
			}catch(Exception e){
				System.out.println("PINGClient: ERR -arg 1");
				System.out.println("PINGClient: Formato Parametri--> int SERVERPORT, String SERVERNAME");
				System.out.println("PINGClient: ---Chiusura Client---");
				return;
			}
			try {
				SERVERNAME = args[1];
				System.out.println("PINGClient: Nome del server letto-->"+SERVERNAME);
			}catch(Exception e) {
				System.out.println("PINGClient: ERR -arg 2");
				System.out.println("PINGClient: Formato Parametri--> int SERVERPORT, String SERVERNAME");
				System.out.println("PINGClient: ---Chiusura Client---");
				return;
			}
		}else {
				System.out.println("PINGClient: Il numero di parametri deve essere 2.");
				System.out.println("PINGClient: Formato Parametri--> int SERVERPORT, String SERVERNAME");
				System.out.println("PINGClient: ---Chiusura Client---");
				return;	
		}
		
		//Dichiarazione della DatagramSocket
		DatagramSocket socket=null;
		try {
			socket=new DatagramSocket(); //Crea una datagram socket settando il bind su una porta disponibile del localhost
			System.out.println("PINGClient: Ho scelto la porta "+socket.getLocalPort());
			System.out.println("PINGClient: Dimensione buffer invio "+socket.getSendBufferSize()+" Dimensione buffer ricezione "+socket.getReceiveBufferSize());
		}catch (SocketException e) {
			System.out.println("PINGClient: SocketException");
			System.out.println("PINGClient: ---Chiusura Client---");
			socket.close();
			return;
		}
		
		try {
			for(int i=0;i<10;i++) {
			long sendingtime=System.currentTimeMillis(); //Tempo al momento 
			String message="PING "+i+" "+sendingtime;
			
			byte[] buffer=message.getBytes(); //trasformo la stringa in un array di bytes
			DatagramPacket packet=new DatagramPacket(buffer,buffer.length,InetAddress.getByName(SERVERNAME),SERVERPORT);
			socket.send(packet);
			sentpackets++;
			
			//Setto il timeout per la ricezione del ping dal server (2 secondi=2000 millisecondi)
			socket.setSoTimeout(2000);
			
			try {
				socket.receive(packet);
			}catch(SocketTimeoutException ste) { //è scaduto il timeout e non ho ricevuto risposta dal server
				System.out.println("PING "+i+" RTT: *");
				continue; //Si passa direttamente alla prossima iterazione
			}
			
			//Ho ricevuto il messaggio di risposta dal server prima dello scadere del timer
			receivedpackets++;
			byte[] received=packet.getData();
			String response=new String(received);
			
			//Calcolo RTT ed aggiorno i dati per le statistiche finali
			long delay=System.currentTimeMillis()-sendingtime; 
			mindelay=delay < mindelay ? delay : mindelay;
			maxdelay=delay > maxdelay ? delay : maxdelay;
			avgdelay=+delay;
			System.out.println(response+" RTT: "+delay);
			}
			
		}catch(IOException e) {
			System.out.println("PINGClient: IOException");
			System.out.println("PINGClient: ---Chiusura Client---");
			socket.close();
			return;
		}
		//Calcolo le statistiche finali
		System.out.println("---- PING Statistics ----");
		if(receivedpackets!=0) {
			avgdelay=avgdelay/receivedpackets;
			//Formato della risposta:
			//10 packets transmitted, 7 packets received, 30% packet loss
			//round-trip (ms) min/avg/max = 63/190.29/290
			
			//inviati : 100 = persi : x --> inviati : 100 = (inviati - ricevuti) : x 
			// x= ((inviati - ricevuti)*100)/inviati
			System.out.printf("%d packets trasmitted, %d packet received, %d %% packet loss\n",sentpackets,receivedpackets,(int)((sentpackets-receivedpackets)*100)/sentpackets);
			System.out.printf("round-trip (ms) min/avg/max = %d/%.2f/%d\n",(int)mindelay,avgdelay,(int)maxdelay);
		}else {
			System.out.printf("%d packets trasmitted, %d packet received, %d %% packet loss\n",sentpackets,0,100);
			System.out.printf("round-trip (ms) min/avg/max = --/--/--\n");
		}
		socket.close();
	}
}
