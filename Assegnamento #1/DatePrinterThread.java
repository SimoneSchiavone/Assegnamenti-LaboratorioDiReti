import java.util.Calendar;
import java.util.Date;

/*
Esercizio 2 – Estendi la classe Thread
1. Crea una classe DatePrinterThread che estenda java.lang.Thread. Aggiungi un metodo
public static void main(String args[]) per creare e avviare una istanza di DatePrinterThread.
2. Nel metodo public void run() crea un loop infinito con while(true).
3. Nel corpo del loop devono essere eseguite le seguenti azioni: stampare data e ora correnti
e nome del thread in esecuzione e stare in sleep per 2 secondi (2000 millisecondi).
4. Nel metodo main(): crea un’istanza di DatePrinterThread and avviala usando start().
Successivamente stampare di nuovo il nome del thread in esecuzione.
*/
public class DatePrinterThread extends Thread {
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
	
	public static void main(String[] args) {
		DatePrinterThread dpt=new DatePrinterThread();
		dpt.start();
		System.out.println("in esecuzione il thread "+Thread.currentThread().getName());
	}

}
