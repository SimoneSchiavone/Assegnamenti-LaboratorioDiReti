import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Simone Schiavone		MAT: 582418
 */

public class EchoServer {
	private final static int PORT=6789;
	private final static int BUFFERDIM=1024;
	
	public static void main(String[] args) throws IOException {
		System.out.println("EchoServer ATTIVO: In attesa sulla porta "+ PORT);
		InetAddress ip = null;
	    try {
	    	ip = InetAddress.getByName("localhost");
	        //Localhost (127.0.0.1) indirizzo di loopback. Permette di eseguire client e server sullo stesso host. 
	    }catch(UnknownHostException uhe) {
	    	uhe.printStackTrace();
	    }
	    InetSocketAddress isa = new InetSocketAddress(ip, PORT);
	    //SocketAddress è composto dalla coppia <Indirizzo IP, #Porta>
	    //Il non-blocking IO è possibile se associamo dei channels ai socket
	    //Ogni SocketChannel è associato ad un oggetto Socket. Il socket associato
	    //può essere reperito tramite il metodo .socket()
	    ServerSocketChannel SSChannel = ServerSocketChannel.open();
	    //Il SocketChannel è creato implicitamente quando si apre una connessione su un ServerSocket
	    ServerSocket SS = SSChannel.socket();
	    //Binds the ServerSocket to a specific address(IP address and port number). 
	    //If the address is null, then the system will pick up an ephemeral port and a valid local address to bind the socket.
	    SS.bind(isa);
	    //Configuriamo il ServerSocketChannel in modalità non bloccante 
	    SSChannel.configureBlocking(false);
	         
	    //Selector: componente che permette di selezionare un SelectableChannel che è pronto per operazioni di rete 
	    Selector selector=Selector.open();
	    //I canali devono essere registrati su un selettore. Tale operazione restituisce una chiave 
	    //(Uno stesso canale può essere registrato con più selettori)
	    SSChannel.register(selector,SelectionKey.OP_ACCEPT); 
	    /*L'oggetto selectionkey è il risultato della registrazione di un canale su un selettore. Tale oggetto
	     * memorizza 
	     * 	-il canale ed il selettore a cui si riferisce
	     *	- definisce le operazioni, del canale associato, su cui si deve fare il controllo di “readiness”, 
	     *	la prossima volta che il metodo select verrà invocato per monitorare i canali del selettore
	     * 	-il ready set, che dopo l'invocazione della select contiene gli eventi che sono pronti su quel canale
	     * 	-un allegato (cioè spazio di memorizzazione associato a quel canale). 
	     * L'interest set è rappresentato come una bitmask che codifica le operazioni per 
	     * cui si registra un interesse su quel canale. Sono CONNECT, ACCEPT, READ e WRITE.*/
	     while(true) {
	    	 //selector.select() è un metodo che seleziona tra i canali registrati sul selettore selector
	    	 //quelli pronti per almeno una delle operazioni di I/O dell'interest set. Restituisce il 
	    	 //numero di canali pronti e costruisce un insieme contenente le chiavi dei canali pronti
	    	 int num=selector.select();
	    	 if (num==0) //non ci sono canali pronti, vado alla prossima iterazione
	    		 continue;
	    	 Set <SelectionKey> set=selector.selectedKeys();
	    	 Iterator <SelectionKey> iterator = set.iterator();

	    	 while(iterator.hasNext()) {
	    		 SelectionKey extractedkey=iterator.next();
	    		 iterator.remove();
	    		 //Rimuove dal selected set l'ultimo elemento restituito (non dal registered)
	    		 if(extractedkey.isAcceptable()) { //testa se la chiave di questo canale è pronta a ricevere nuove socket connection
	    			 System.out.println("EchoServer: una chiave è pronta per l'accettazione");
	    			 ServerSocketChannel server= (ServerSocketChannel) extractedkey.channel();
	    			 SocketChannel client = server.accept();
	    			 System.out.println("EchoServer: connessione accettata verso "+client);
	    			 client.configureBlocking(false);
	    			
	    			 client.register(selector, SelectionKey.OP_READ);
	    		}
	    		if(extractedkey.isReadable()) {
	    			System.out.println("EchoServer: una chiave è pronta per la lettura");
	    			SocketChannel client= (SocketChannel) extractedkey.channel(); //Cast necessario perchè restituisce un SelectableChannel
	    			ByteBuffer input = ByteBuffer.allocateDirect(BUFFERDIM);
	    			client.configureBlocking(false);
	    			
	    			StringBuilder message=new StringBuilder();
	    			String a=(String) extractedkey.attachment();
	        		if (a!=null) {
	        			message.append(a);
	        		}
	        			
	        		//legge una sequenza di byte dal canale nel buffer "input"
	        		int readbytes=client.read(input);
	        		//prepariamo il buffer alla lettura dopo la scrittura
	        		input.flip();     	
		        	while (input.hasRemaining()) {
		        		message.append((char)input.get());
		        	}
		        	extractedkey.attach(message.toString());
		        	
		        	if(readbytes<BUFFERDIM) { 
		        		//Ho letto tutto il messaggio. Setto l'interesse della chiave in WRITE
		        		System.out.println("EchoServer: ricevuto un messaggio dal client "+client.getLocalAddress()+" -> "+message);
		        		extractedkey.interestOps(SelectionKey.OP_WRITE);
		        	}
		        	if(readbytes==BUFFERDIM) { 
		        		//Ci sono ancora dati sul canale. Ritorno al leggere alla prossima iterazione
	        			 System.out.println("EchoServer: leggo "+readbytes+" bytes dal canale");
	        		}
		        	  	
	        	}if(extractedkey.isWritable()) {
	        		System.out.println("EchoServer: una chiave è pronta per la scrittura");
	        		SocketChannel client= (SocketChannel) extractedkey.channel(); //Cast necessario perchè restituisce un SelectableChannel
	        		 
	        		StringBuilder message=new StringBuilder();
	        		String extractedstring=(String) extractedkey.attachment();
	        		message.append(extractedstring);
	        		message.append("...ECHOED BY SERVER");
	        		
	        		//Racchiude un array in un buffer. Il buffer viene restituito dal metodo statico wrap della classe bytebuffer
	        		ByteBuffer output= ByteBuffer.wrap(message.toString().getBytes());
	        		output.clear();
	        		client.write(output);
	        		        		
	        		//Ho letto tutto il messaggio. Setto l'interesse della chiave in READ
	        		System.out.println("EchoServer: invio un messaggio di risposta al client "+message.toString());
	        		extractedkey.interestOps(SelectionKey.OP_READ);
	        		extractedkey.attach(null); //Rimuovo il messaggio scritto in precedenza dal client
	        		
	        		if(message.toString().equals("STOP...ECHOED BY SERVER")) {
			        	System.out.println("EchoServer: è stata ordinata la chiusura del server da parte di "+client.getLocalAddress());
			        	client.close();
			        	System.out.println("---SERVER TERMINATO---");
		        		return;
		        	}	 
	        	}
	        }
	     }
	}
}
