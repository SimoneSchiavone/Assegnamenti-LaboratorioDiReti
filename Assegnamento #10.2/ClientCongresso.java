import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Simone Schiavone 	MAT: 582418
 */

public class ClientCongresso {
	private static int porta;
	public static void main(String[] args) {
		//parsing della porta dalla linea di comando
		if(args.length!=1) {
			System.out.println("Inserire come parametro il numero di porta!");
			return;
		}
		try {
			porta=Integer.valueOf(args[0]).intValue();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Errore nel parsing della porta da linea di comando! CHIUSURA SERVER");
			return;
		}
		
		//scansione da stdin del nome utente
		String nomeutente;
		Scanner input=new Scanner(System.in);
		System.out.println("CLIENTCongresso: Inserire il nome utente.");
		nomeutente=input.nextLine();
		
		try {
			Registry registry=LocateRegistry.getRegistry(porta);
			Remote remoteobject= registry.lookup("GESTIONECONGRESSO");
			InterfacciaCongresso gestore= (InterfacciaCongresso) remoteobject;
			
			while(true) {
				System.out.println("---GESTIONE CONGRESSO---");
				System.out.println("Menù operazioni:");
				System.out.println("0 -> registrarti come speaker di una sessione.");
				System.out.println("1 -> stampa intero programma.");
				System.out.println("2 -> uscita dal programma");
				int opzione=input.nextInt();
				
				switch(opzione) {
					//chiusura server
					case 2:	System.out.println("---CHIUSURA CLIENT---");
							return;
					
					//registrazione speaker
					case 0: System.out.println("Inserire il giorno per cui registrarsi (1-2-3)");
							int giorno=input.nextInt();
							System.out.println("Inserire la sessione del giorno"+giorno+" per cui registrarsi (1...12)");
							int sessione=input.nextInt();
							try {
								//invocazione metodo remoto
								if(gestore.registraspeaker(nomeutente, giorno, sessione)) {
									System.out.println("Registrazione avvenuta correttamente per il giorno "+giorno+" sessione "+sessione);
								}
							}catch(NullPointerException npe) {
								System.out.println(npe.getMessage());
							}catch(IllegalArgumentException iae) {
								System.out.println(iae.getMessage());
							}catch(SessionFullException sfe) {
								System.out.println(sfe.getMessage());
							}
							break;
					
					//stampa piano congresso
					case 1: ArrayList<HashMap<Integer , ArrayList<String>>> tabella=gestore.ottieniprogramma();
							for(int i=1;i<=3;i++) {
								System.out.println("---Giorno "+i+" ---");
								for(int j=1;j<=12;j++) {
									System.out.printf("\tSessione "+j+"\n");
									for(int k=0;k<tabella.get(i-1).get(j).size();k++) {
										System.out.println("\t\t"+tabella.get(i-1).get(j).get(k));
									}
								}
							}
							System.out.println();
							break;
					
					default: System.out.println("Operazione inserita non riconosciuta. Leggere il menù operazioni");
							continue;
				}
			}
		}catch(RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		input.close();
	}

}
