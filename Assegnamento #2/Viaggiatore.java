import java.lang.Math;
public class Viaggiatore implements Runnable {
	private static int idcounter=0;
	private int id;
	
	public Viaggiatore() {
		this.id=idcounter;
		idcounter++;
	}
	
	@Override
	public void run() {
		System.out.printf("Viaggiatore %d: sto acquistando un biglietto\n",this.id);
		//Genero un numero casuale nell'intervallo [0,1000] ms
		int r=(int)(Math.random()*1000);
		//System.out.printf("NUMERO CASUALE GENERATO %d\n",r);
		//Mettiamo in attesa questo thread per r millisecondi
		try {
			Thread.sleep(r);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Viaggiatore %d: ho acquistato il biglietto\n",this.id);
	}
	
	public int getid() {
		return this.id;
	}
}
