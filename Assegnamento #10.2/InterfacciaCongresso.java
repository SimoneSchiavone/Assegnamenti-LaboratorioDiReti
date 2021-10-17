
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Simone Schiavone 	MAT: 582418
 */

public interface InterfacciaCongresso extends Remote {
	
	//Metodo per la registrazione dello speaker "nome" alla sessione "sessione" nel giorno "giorno"
	//Il metodo restituisce true se l'inserimento avviene correttamente. Viene restituita una eccezione
	//se i parametri inseriti non sono corretti (es nome nullo o valori giorno/sessione fuori dal range 
	//accettato. In caso di problemi con RMI lancia una RemoteException
	boolean registraspeaker(String nome,int giorno, int sessione) throws IllegalArgumentException, 
		NullPointerException, SessionFullException, RemoteException;
	
	//Metodo per la restituzione del programma completo del congresso. Restituisce RemoteException
	//in caso di problemi con RMI
	public ArrayList<HashMap<Integer , ArrayList<String>>> ottieniprogramma() throws RemoteException;
}
