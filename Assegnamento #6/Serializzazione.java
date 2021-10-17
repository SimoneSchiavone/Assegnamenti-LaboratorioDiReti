import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Simone Schiavone		Mat: 582418
 * Questa classe provvede alla Serializzazione di un 
 * certo numero di conti correnti memorizzati in una lista.
 * Per la serializzazione ho utilizzato la libreria Gson. 
 */

public class Serializzazione {

	public static void main(String[] args) throws IOException {
		int numclienti=(int)(Math.random()*10)+1; //1-10 clienti
		//Inizializziamo una lista di conti correnti
		ArrayList<ContoCorrente> ListaConti=new ArrayList<ContoCorrente>();
		for(int i=0;i<numclienti;i++) {
			ListaConti.add(new ContoCorrente());
		}
		
		//Gson gson= new Gson();
		Gson gson= new GsonBuilder().setPrettyPrinting().create();
		File fileout=new File("conticorrente.json");
		WritableByteChannel channel=Channels.newChannel(new FileOutputStream(fileout));
		//Metodo del pacchetto Gson per la serializzazione
		String stringajson=gson.toJson(ListaConti);
		//System.out.println(json);
		ByteBuffer buffer=ByteBuffer.allocate(stringajson.getBytes().length);
		buffer.put(stringajson.getBytes());
		buffer.flip();
		channel.write(buffer);
		for(int i=0;i<ListaConti.size();i++) {
			ListaConti.get(i).StampaInfoConto();
		}
		System.out.printf("SERIALIZZAZIONE AVVENUTA. \n Nella cartella del progetto si trova il file 'conticorrente.json'");
	}

}
