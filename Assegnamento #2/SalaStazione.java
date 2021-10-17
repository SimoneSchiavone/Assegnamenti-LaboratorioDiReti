/*Esercizio 1
Nella sala biglietteria di una stazione sono presenti 5 emettitrici automatiche dei biglietti. Nella sala non
possono essere presenti più di 10 persone in attesa di usare le emettitrici.
Scrivere un programma che simula la situazione sopra descritta.
• La sala della stazione viene modellata come una classe JAVA. Uno dopo l’altro arrivano 50
viaggiatori (simulare un intervallo di 50 ms con Thread.sleep).
• ogni viaggiatore viene simulato da un task, la prima operazione consiste nello stampare
“Viaggiatore {id}: sto acquistando un biglietto”, aspettare per un intervallo di tempo random tra 0 e
1000 ms e poi stampa “Viaggiatore {id}: ho acquistato il biglietto”.
• I task vengono assegnati a un numero di thread pari al numero delle emettitrici
• Il rispetto della capienza massima della sala viene garantita dalla coda gestita dal thread. I
viaggiatori che non possono entrare in un certo istante perché la capienza massima è stata
raggiunta abbandonano la stazione (il programma main stampa quindi “Traveler no. {i}: sala
esaurita”.
• Suggerimento: usare un oggetto ThreadPoolExecutor in cui il numero di thread è pari al numero
degli sportelli
*/
import java.util.concurrent.*;
public class SalaStazione {

	public static void main(String[] args) {
		int nrmacchine=5;
		int capacitasala=10;
		ArrayBlockingQueue<Runnable> abq= new ArrayBlockingQueue<Runnable>(capacitasala);
		ThreadPoolExecutor executor=new ThreadPoolExecutor(nrmacchine,nrmacchine,60L,TimeUnit.MILLISECONDS,abq);
		int nrviaggiatori=0;
		//Accendiamo le macchine della sala
		executor.prestartAllCoreThreads(); //ho 5 thread che aspettano che arrivi qualche task da eseguire
		while(nrviaggiatori<50) {
			
			try {
				Thread.sleep(50);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			Viaggiatore tmp=new Viaggiatore();
			
			try {
				executor.execute(tmp);
			}catch(RejectedExecutionException e) {
				System.out.printf("Viaggiatore %d: sala esaurita\n",tmp.getid());
			}
			
			nrviaggiatori++;
		}
		//Chiudo la sala facendo comunque effettuare l'operazione a chi è dentro
		executor.shutdown(); //graceful termination
	}
}
