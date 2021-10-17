import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SalaAttesaPiccola {
	private int nrsportelli;
	private int capienzasala;
	private ArrayBlockingQueue<Runnable> abq; 
	private ThreadPoolExecutor executor;
	
	public SalaAttesaPiccola(int a,int b) {
		this.nrsportelli=a;
		this.capienzasala=b;
		this.abq=new ArrayBlockingQueue<Runnable>(capienzasala);
		this.executor=new ThreadPoolExecutor(this.nrsportelli,this.nrsportelli,60L,TimeUnit.MILLISECONDS,abq);
	}
	
	public void accettacliente(Cliente x) {
		System.out.println("Cliente "+x.getid()+": Ha fatto ingresso nella sala sportelli.");
		executor.execute(x);
	}
	
	public int clientiinattesa() {
		return abq.size();
	}
	
	public void chiudisportelli() {
		executor.shutdown();
		System.out.println("***SALA SPORTELLI CHIUSA. I clienti già entrati verranno serviti.***");
	}
	
	public void aprisportelli() {
		executor.prestartAllCoreThreads();
	}
}
