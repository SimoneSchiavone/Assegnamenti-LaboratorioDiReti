import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Simone Schiavone		 MAT 582418
 */

public class ServerMain {
		
	public static void main(String[] args) throws IOException {
		final int default_port = 6789;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss z",Locale.ITALY);
		
		//Stampiamo sul terminale i file contenuti nella cartella del progetto
		File currentdir=new File(".");
		File[] dirfiles=currentdir.listFiles();
		System.out.printf("__________\nFILE DELLA CARTELLA\n");
		for(File f : dirfiles) {
			if(!f.isDirectory())
				System.out.println(f.getName());
		}
		System.out.println("__________");
		System.out.println("Per chiudere il server richiedere il file shutdown");
		System.out.println("Formato delle richieste da scrivere nel browser: localhost:6789/filename");
		
		//TryWithResources: supporto per la chiusura sistematica delle risorse e/o
		//connessioni aperte da un programma. Gli argomenti tra parentesi sono le
		//risorse che JAVA garantisce di chiudere al termine del blocco. 
		try(ServerSocket ListenSocket=new ServerSocket (default_port)) {
			ListenSocket.setSoTimeout(30000); //Impostiamo il timer affinchè dopo 30s di inattività il server venga arrestato 			
			//Socket di ascolto del server
		
			while(true) {
				//Socket di comunicazione
				System.out.printf ("-------------------- \nWeb server in attesa di richieste sulla porta " + default_port+"\n");
				try(Socket ConnectionSocket=ListenSocket.accept();
						BufferedReader in = new BufferedReader (new InputStreamReader(ConnectionSocket.getInputStream()));
							DataOutputStream out = new DataOutputStream(ConnectionSocket.getOutputStream());){
					Date now=new Date(); //viene incapsulata la data di allocazione dell'oggetto
					System.out.println(sdf.format(now)+" ---> Il webserver ha ricevuto una richiesta");
					System.out.println("Connessione da "+ConnectionSocket);				
					
					
					
					//Leggiamo la richiesta HTTP
					char[] buf=new char[1000];
					in.read(buf);
					String request=String.copyValueOf(buf);
					System.out.println(request); 
					
					//Ignoro le richieste favicon.ico
					if(request.startsWith("GET /favicon.ico")) {
						in.close();
						out.close();
						continue; //si va all'iterazione successiva
					}
					
					//Se viene richiesto il file shoutdown si procede alla chiusura del server
					if(request.startsWith("GET /shutdown")) {
						try {
							out.writeBytes("HTTP/1.1 200 OK\r\n\r\n");
							File closedgif=new File("./closed.gif");
							//Copies all bytes from a file to an output stream.
							Files.copy(closedgif.toPath(), out);
							out.flush();
						}catch(SocketException se){
							se.printStackTrace();
						}
						System.out.println("Shutdown command --- Chiusura Server");
						break; //esco dal ciclo while(true)
					}
					//Il server deve gestire solamente richieste "GET"
					if(!request.startsWith("GET")) {
						System.out.println("RICHIESTA NON GESTIBILE");
						System.out.println(request);
						//Se la richiesta non è gestibile restituire un exit code 501
						String response="HTTP/1.1 501 Not Implemented\r\n"; //Status Line
						response= response +"Date: "+sdf.format(new Date())+"\r\n";
						response= response + "Server: ServerMain\r\n";
						System.out.printf(response);
						//Scrittura della risposta HTTP
						try {
							response=response+"\r\n";
							out.writeBytes(response);
							out.flush();
						}catch(SocketException se) {
							se.printStackTrace();
						}
					}else {
						System.out.println("RICHIESTA GESTIBILE");
						//Ricaviamo il nome del file desiderato dalla stringa della richiesta http
						//Vogliamo suddividere la stringa della richiesta in parole in modo da poter
						//prendere la seconda parola che rappresenta il nome del file.
						
						//https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html
						// Request-Line = Method SP Request-URI SP HTTP-Version CRLF
						// " " --> Spazio (SP) \r --> Ritorno Carrello (CR) \n --> New Line/Line Feed (LF)
						
						//Utilizziamo il metodo split della classe String che permette di suddividere una
						//stringa in base ad una espressione regolare
						String [] words=request.split(" ");
						
						/* Test split
						int counter=0;
						for(String s : words) {
							System.out.println("Stringa "+counter+" "+s);
							counter++;
						}*/
						
						//Recuperiamo il nome del file
						String filename=words[1];
						System.out.println("Il file richiesto è:"+filename);
						//Dobbiamo ricostruire il path
						String pathstring=System.getProperty("user.dir") + words[1];
						System.out.println(pathstring);
						File file=new File(pathstring);
						Path path=Paths.get(pathstring);
						
						//Status-Line=HTTP-Version SP Status-Code SP Reason-Phrase CRLF
						if(file.isFile()) {
							//Gestione della risposta positiva
							System.out.println("File trovato");
							String response="HTTP/1.1 200 OK\r\n"; //Status Line
							response= response +"Date: "+sdf.format(new Date())+"\r\n";
							response= response + "Server: ServerMain\r\n";
							//Files.probeContentType returns the content type of the file or null if the content type cannot be determinated
							response= response + "Content-Type: "+Files.probeContentType(path)+"\r\n";
							response= response + "Content-Length: "+file.length()+"\r\n";
							response= response + "Last-Modified: "+sdf.format(file.lastModified())+"\r\n";
							
							System.out.printf(response);
						
							//Scrittura della risposta HTTP
							try {
								response=response+"\r\n";
								out.writeBytes(response);
								Files.copy(file.toPath(), out);
								out.flush();
							}catch(SocketException se) {
								se.printStackTrace();
							}
						}else {
							System.out.println("File non trovato");
							String response="HTTP/1.1 404 Not Found\r\n"; //Status Line
							response= response +"Date: "+sdf.format(new Date())+"\r\n";
							response= response + "Server: ServerMain\r\n";
							
							System.out.println(response);
							try {
								response=response+"\r\n";
								out.writeBytes(response);
								File failgif=new File("./404.gif");
								Files.copy(failgif.toPath(), out);
								out.flush();
							}catch(SocketException se) {
								se.printStackTrace();
							}
							
							
						}
					}					
				}catch(SocketTimeoutException ste) {
					System.out.println("SocketTimeout --- Chiusura Server");
					break;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}	
}
