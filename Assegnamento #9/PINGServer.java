import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 * @author Simone Schiavone		MAT: 582418
 */
public class PINGServer {
	private static int SERVERPORT; 
	private static long SEED=123456789;
	
	public static void main(String[] args) {
		switch(args.length) {
			case 1:		
						try {
							SERVERPORT=Integer.valueOf(args[0]).intValue();
						}catch(Exception e) {
							System.out.println("PINGServer: ERR -arg 1");
							System.out.println("PINGServer: Formato Parametri--> int SERVERPORT, long SEED (Opzionale)");
							System.out.println("PINGServer: ---Chiusura Server---");
							return;
						}
						break;
			case 2:		
						try {
							SERVERPORT=Integer.valueOf(args[0]).intValue();
						}catch(Exception e) {
							System.out.println("PINGServer: ERR -arg 1");
							System.out.println("PINGServer: Formato Parametri--> int SERVERPORT, long SEED (Opzionale)");
							System.out.println("PINGServer: ---Chiusura Server---");
							return;
						}
						try {
							SEED=Long.valueOf(args[1]).longValue();
						}catch(Exception e) {
							System.out.println("PINGServer: ERR -arg 2");
							System.out.println("PINGServer: Formato Parametri--> int SERVERPORT, long SEED (Opzionale)");
							System.out.println("PINGServer: ---Chiusura Server---");
							return;
						}
						break;
			case 0:		
						System.out.println("PINGServer: Il numero di parametri deve essere 1 o 2.");
						System.out.println("PINGServer: Formato Parametri--> int SERVERPORT, long SEED (Opzionale)");
						System.out.println("PINGServer: ---Chiusura Server---");
						return;
		}
		
		//Dichiarazione della DatagramSocket
		DatagramSocket socket=null;
		try {
			socket=new DatagramSocket(SERVERPORT);
			//Costuisco un datagram socket assegnandolo alla porta SERVERPORT letta da linea di comando
			System.out.println("PINGServer: Server aperto porta "+socket.getLocalPort());
			System.out.println("PINGServer: Dimensione buffer invio "+socket.getSendBufferSize()+" Dimensione buffer ricezione "+socket.getReceiveBufferSize());
		}catch(SocketException e) {
			System.out.println("PINGServer: SocketException");
			System.out.println("PINGServer: ---Chiusura Server---");
			socket.close();
			return;
		}
		
		byte[] buffer=new byte[1024];
		DatagramPacket packet= new DatagramPacket(buffer,buffer.length);
		
		Random random = new Random(SEED);
		
		while(true) {
			try {
				socket.receive(packet);
				System.out.println("PINGServer: Ho ricevuto un pacchetto da "+packet.getAddress()+" "+packet.getPort());
				String message= new String(packet.getData(),0,packet.getLength());
				
				
				//Determiniamo se "perdere" il pacchetto oppure rimandarlo indietro
				int lost=random.nextInt(1000); //genero un numero da 0 a 999
				//System.out.println("RANDOM RESULT: "+lost);
				if (lost % 4==0) { //se il numero generato è un multiplo di 4 allora scarto il pacchetto (Probabilità 25%)
					//System.out.println("PINGServer: Non rimando indietro il pacchetto che avevo ricevuto");
					System.out.print(packet.getAddress()+":"+packet.getPort()+"> "+message+" ACTION: not sent\n");
					continue; //Passo alla prossima iterazione
				}
				
				//Se rimando indietro il pacchetto, genero artificialmente un ritardo
				long artificialdelay=(long) (Math.random()*2000); //Genero un ritardo casuale di max 2 secondi (timer perdita pacchetto);
				Thread.sleep(artificialdelay); //faccio dormire il thread che esegue questo programma per il ritardo generato 
				
				//Inviamo indietro lo stesso messaggio ricevuto
				DatagramPacket response= new DatagramPacket(message.getBytes(),message.getBytes().length,packet.getAddress(),packet.getPort());
				System.out.print(packet.getAddress()+":"+packet.getPort()+"> "+message);
				System.out.println(" ACTION: delayed "+artificialdelay+" ms");
				socket.send(response);
			} catch (IOException e) {
				System.out.println("PINGServer: IOException");
				System.out.println("PINGServer: ---Chiusura Server---");
				socket.close();
				return;
			} catch (InterruptedException e) {
				System.out.println("PINGServer: Interrupted Exception");
				
			}
		}
		
		
		

	}

}
