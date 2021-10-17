import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * @author Simone Schiavone		MAT:582418
 */
public class LettoreConti extends Thread {
	ArrayList<ContoCorrente> listaconti;
	ThreadPoolExecutor tpe;
	public LettoreConti(ArrayList<ContoCorrente> a, ThreadPoolExecutor b) {
		this.listaconti=a;
		this.tpe=b;
	}
	
	public void run() {
		System.out.println("---Inizio Lettura Conti Correnti---");
		for(ContoCorrente i : listaconti) {
			tpe.execute(i);
		}
		tpe.shutdown(); 
		try {
			tpe.awaitTermination(5000L,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---Conteggio Terminato---");
	}
}
