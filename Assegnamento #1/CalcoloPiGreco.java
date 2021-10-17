/*Scrivere un programma che attiva un thread T che effettua il calcolo approssimato di PiGreco,
 * Il programma principale riceve in input da linea di comando un parametro che indica il grado
 * di accuratezza (accuracy) per il calcolo di PiGreco ed il tempo massimo di attesa dopo cui il
 * programma principale interompe il thread T. Il thread T effettua un ciclo infinito per il calcolo
 * di PiGreco usando la serie di Gregory-Leibniz ( PiGreco= 4/1 – 4/3 + 4/5 - 4/7 + 4/9 - 4/11 ...).
Il thread esce dal ciclo quando una delle due condizioni seguenti risulta verificata:
	1) il thread è stato interrotto
	2) la differenza tra il valore stimato di PiGreco ed il valore Math.PI (della libreria JAVA) è 
	minore di accuracy
*/

//Creato da SIMONE SCHIAVONE Mat. 582418

import java.util.Scanner;

public class CalcoloPiGreco implements Runnable {
	private float accuracy;
	private float pigreco;
	
	public CalcoloPiGreco(float x) {
		this.accuracy=x;
		this.pigreco=0;
	}
	
	public void run() {  //RUN non deve avere parametri, non posso passarmi l'accuratezza scansionata
		float i=0,d=1;
		while(true) {
			//Controlliamo il flag di interruzione
			if(Thread.currentThread().isInterrupted()) { 
				//thread interrotto --> significa che ha sforato il tempo
				System.out.println("Tempo esaurito. Il valore di PiGreco calcolato è "+this.pigreco);
				break;
			}
			
			if(i%2==0)
				this.pigreco=this.pigreco+(4/d);
			else
				this.pigreco=this.pigreco-(4/d);
			d+=2;
			i++;
			System.out.println("Approssimazione "+this.pigreco+", Esatto "+Math.PI+", differenza "+Math.abs((this.pigreco-Math.PI))+", accuratezza "+this.accuracy);
			if(Math.abs((this.pigreco-Math.PI))<this.accuracy) {
				System.out.println("Il valore di PiGreco calcolato è "+this.pigreco);
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		System.out.println("Inserisci l'accuratezza");
		float accuracy=scanner.nextFloat();
		System.out.println("Inserisci il tempo massimo di attesa in millisecondi");
		int maxwait=scanner.nextInt();
		
		CalcoloPiGreco cpg=new CalcoloPiGreco(accuracy);
		Thread t=new Thread(cpg);
		t.start(); //faccio partire il thread
		try {
			t.join(maxwait); 
			//Il thread del main attende la fine del thread t per un massimo di maxwait millisecondi
			t.interrupt(); //interrupt verso t. Se t è già terminato non succede niente
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		scanner.close();
	}	
}
