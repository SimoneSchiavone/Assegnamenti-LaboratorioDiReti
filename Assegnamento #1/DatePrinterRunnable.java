import java.util.Calendar;
import java.util.Date;

/*Esercizio 3 – Usa l’interfaccia Runnable
1. Crea una classe DatePrinterRunnable che implementi java.lang.Runnable. Aggiungi un
metodo public static void main(String args[]).
2. L’implementazione del metodo run() nella classe DatePrinterRunnable è lo stesso
dell’esercizio precedente.
3. Nel metodo main(): crea un oggetto DatePrinterRunnable e un oggetto di tipo Thread che
prende come parametro del costruttore un oggetto DatePrinterRunnable. Dopo
l’invocazione del metodo start() stampare il nome del thread in esecuzione.*/

public class DatePrinterRunnable implements Runnable {
	
	public static void main(String[] args) {
		DatePrinterRunnable dpr=new DatePrinterRunnable(); //questa è la classe del "task"
		Thread t=new Thread(dpr);
		t.start();
		System.out.println("in esecuzione il thread "+Thread.currentThread().getName());
	}
	
	public void run(){
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
	}

}
