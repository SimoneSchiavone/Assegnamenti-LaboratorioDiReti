import java.io.*;
import java.util.ArrayList;
/*Esercizio 1
Scrivere un programma Java che, a partire dal percorso di una directory (es.
"/path/to/dir/"), recupera il contenuto della directory e delle eventuali sottodirectory.
Il programma scrive in un file di nome “directories” il nome delle directory che incontra
e nel file “files” il nome dei file.*/

public class EsercizioPreparatorio {
	public static void main(String[] args) throws IOException{
		//Percorso iniziale. Come parametro del costruttore file mettiamo il suo percorso sottoforma di stringa
		File partenza= new File("/path/to/dir/");
		if(!partenza.isDirectory()) {
			System.out.println("Questo file non è una cartella!");
			return;
		}
		
		//uso una struttura di appoggio per poter memorizzare le varie directory
		//in modo da poterle visitare tutte
		ArrayList<File>directories=new ArrayList<File>();
		directories.add(partenza);
		
		//FormattedDataStream con bufferizzazione
		DataOutputStream dir=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("directories"))));
		DataOutputStream file=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File("files"))));
		//finchè non ho visitato tutte le directories, estraggo quella in testa,
		//ne estraggo tutti i file e "appendo" le directories che individuo
		//nell'arraylist creata
		
		while(directories.size()>0) { //ci sono file in lista
			File corrente=directories.remove(0);
			File[] files=corrente.listFiles();
			//visitiamo tutti i file
			for(File k : files) {
				String nome=k.getName(); 
				//si potrebbe usare il toString per avere il riferimento alla stringa corrente ed agganciare direttamente il \n
				nome= nome + "n"; 
				if(k.isDirectory()) {
					directories.add(k);
					dir.write(nome.getBytes());
					/*Da Java API:
					byte[]	getBytes()
					Encodes this String into a sequence of bytes using the platform's default charset, 
					storing the result into a new byte array.*/					
				}else
					file.write(nome.getBytes());
			}
		}
		
		dir.flush();
		file.flush();
		dir.close();
		file.close();
	}
}
