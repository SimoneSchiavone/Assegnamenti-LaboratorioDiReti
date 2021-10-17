
public class Cliente implements Runnable {
	private static int prossimonumero=1;
	private int numero; 
	
	public Cliente() {
		this.numero=prossimonumero;
		prossimonumero++;
	}
	public void run() {
		System.out.printf("Cliente %d: Inizio operazione presso sportello %s\n",this.numero,Thread.currentThread().getName());
		//Genero un numero casuale nell'intervallo [0,5000] ms
		int r=(int)(Math.random()*5000);
		//Mettiamo in attesa questo thread per r millisecondi
		try {
			Thread.sleep(r);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Cliente %d: Operazione terminata\n",this.numero);
	}
	
	public int getid() {
		return this.numero;
	}
}
