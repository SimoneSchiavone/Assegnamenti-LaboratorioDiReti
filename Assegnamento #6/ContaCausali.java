import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * @author Simone Schiavone		Mat: 582418
 * Questa classe provvede alla deserializzazione di una lista di
 * oggetti di tipo ContoCorrente dopo aver effettuato la lettura 
 * di un file .json prodotto dalla classe "Serializzazione" usando
 * la NIO. Successivamente un oggetto della classe LettoreConti (che 
 * estende Thread) legge i conti correnti e li passa ai thread presenti
 * in un threadpool. Tali thread si occuperanno di contare il numero di
 * occorrenze di ogni possibile causale dei movimenti di un conto corrente 
 * aggiornando dei contatori globali.
 */

public class ContaCausali {

	public static void main(String[] args) throws IOException {
		//LETTURA DEL FILE JSON CON NIO
		File filein=new File("conticorrente.json");
		ReadableByteChannel channel=Channels.newChannel(new FileInputStream(filein));
		ByteBuffer buffer=ByteBuffer.allocate((int)filein.length());
		channel.read(buffer);
		//System.out.println(buffer); //stampa il valore delle variabili di stato del buffer
		buffer.flip(); 
		//riporto la variabile position all'inizio del buffer
		//in modo da poter effettuare la lettura
		//System.out.println(buffer);
		
		StringBuilder stringajsonb=new StringBuilder();

		while(buffer.position()<(int)(filein.length())) {
			byte extracted=buffer.get();
			stringajsonb.append((char)extracted);
		}
	
		System.out.printf("STRINGA JSON LETTA DAL FILE\n"+stringajsonb+"\n");
		String stringajson=stringajsonb.toString();
		
		//DESERIALIZZAZIONE
		ArrayList<ContoCorrente> ListaConti=new ArrayList<ContoCorrente>();
		Gson gson=new Gson();
		JsonArray array = JsonParser.parseString(stringajson).getAsJsonArray();
		for(int i=0;i<array.size();i++) {
			ListaConti.add(gson.fromJson(array.get(i), ContoCorrente.class));
		}
	    
	    //GESTIONE CONTEGGIO CAUSALI TRAMITE THREAD PRODUTTORI E CONSUMATORI
	    int[] contatori=new int[5]; //0: Bonifico, 1: Accredito, 2: Bolletino, 3: F24, 4: Pagobancomat
	    Arrays.fill(contatori, 0);
	    for(ContoCorrente i : ListaConti) { 
	    	i.SetContatori(contatori); //Associo alla variabile contatori di ogni conto corrente l'array creato sopra
	    }
	    
	    ArrayBlockingQueue<Runnable> abq=new ArrayBlockingQueue<Runnable>(ListaConti.size());
		ThreadPoolExecutor threadpool=new ThreadPoolExecutor(2, 6, 60L, TimeUnit.MILLISECONDS, abq);
		LettoreConti prod=new LettoreConti(ListaConti,threadpool);
	    threadpool.prestartAllCoreThreads();
	    prod.start();
	    try {
			prod.join(); //Attendiamo il termine dell'operazione di lettura dei conti corrente ed il conteggio delle causali
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    System.out.printf("Risultato Conteggio:\n[Bonifico, Accredito, Bollettino, F24, PagoBancomat]\n");
	    System.out.println(Arrays.toString(contatori));	    
	}
}
