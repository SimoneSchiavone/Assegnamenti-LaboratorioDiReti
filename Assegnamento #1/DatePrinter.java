/*Esercizio 1 – Ciascun programma ha un Thread. Non creerai un Thread poichè la JVM crea sempre un main Thread.
1. Crea la classe DatePrinter con un metodo public static void main(String args[]) a cui
aggiungerai il codice per i passi seguenti.
2. Crea un loop infinito con while(true).
3. Nel corpo del loop devono essere eseguite le seguenti azioni: stampare data e ora correnti
e nome del thread in esecuzione (suggerimento: usa Thread.currentThread()) e
successivamente stare in sleep per 2 secondi. Suggerimento: usa java.util.Calendar
https://docs.oracle.com/javase/8/docs/api/java/util/Calendar.html per recuperare data e
ora correnti.
Al termine dell’esercizio provare ad aggiungere dopo il ciclo while l’operazione di stampa del
nome del thread in esecuzione
 */
import java.util.Calendar;
import java.util.Date;

public class DatePrinter {
	public static void main(String[] args) {
		while(true) {
			Calendar calendar = Calendar.getInstance();
			Date rightnow=calendar.getTime();
			System.out.println(rightnow+" , in esecuzione il thread "+Thread.currentThread().getName());
			try {
				Thread.sleep(2000);
			}catch(InterruptedException x) {
				x.printStackTrace();
			}
		}
		//System.out.println("In esecuzione: "+Thread.currentThread().getName());
		/*Unreachable Code :
		Every statement in any java program must be reachable i.e every statement must be executable at 
		least once in any one of the possible flows. If any code can not be executable in any of the possible
		flows, then it is called unreachable code. Unreachable code in java is a compile time error.*/
	}
}

