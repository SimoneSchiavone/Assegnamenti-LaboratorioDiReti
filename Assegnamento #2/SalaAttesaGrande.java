/*Simulare il flusso di clienti in un ufficio postale che ha 4 sportelli. Nell'ufficio esiste:
un'ampia sala d'attesa in cui ogni persona può entrare liberamente. Quando entra, ogni persona prende il numero
dalla numeratrice e aspetta il proprio turno in questa sala. una seconda sala, meno ampia, posta davanti agli 
sportelli, in cui si può entrare solo a gruppi di k persone. Una persona si mette quindi prima in coda nella 
prima sala, poi passa nella seconda sala. Ogni persona impiega un tempo differente per la propria operazione 
allo sportello. Una volta terminata l'operazione, la persona esce dall'ufficio.
Scrivere un programma in cui:
-l'ufficio viene modellato come una classe JAVA, in cui viene attivato un ThreadPool di dimensione uguale al numero degli sportelli
-la coda delle persone presenti nella sala d'attesa è gestita esplicitamente dal programma
-la seconda coda (davanti agli sportelli) è quella gestita implicitamente dal ThreadPool
-ogni persona viene modellata come un task, un task che deve essere assegnato ad uno dei thread associati agli sportelli
-si preveda di far entrare tutte le persone nell'ufficio postale,all'inizio del programma

Facoltativo: prevedere il caso di un flusso continuo di clienti e la possibilità che l'operatore chiuda lo sportello
 stesso dopo che in un certo intervallo di tempo non si presentano clienti al suo sportello.
*/
import java.util.concurrent.*;
import java.util.Scanner;

public class SalaAttesaGrande {

	public static void main(String[] args) {
		SalaAttesaPiccola salasportelli=new SalaAttesaPiccola(4,10); 
		//4 sportelli, 10 posti a sedere nella sala sportelli  
	
		//Riempiamo la coda di un numero di clienti preso da input
		System.out.println("Digitare il numero di clienti che si presentano all'ufficio postale");
		Scanner input=new Scanner(System.in);
		int nrclientidaprocessare=input.nextInt();
		LinkedBlockingQueue<Cliente> codaesterna=new LinkedBlockingQueue<Cliente>(nrclientidaprocessare);
		for(int i=0;i<nrclientidaprocessare;i++) {
			codaesterna.add(new Cliente());
		}
		System.out.println(codaesterna);
		
		salasportelli.aprisportelli();
		
		//Facciamo entrare i clienti in gruppi di max 10 finchè non sono terminati
		while(codaesterna.size()>0) { //ciclo finchè non ho processato tutti i clienti
			
			if(salasportelli.clientiinattesa()==0) { //quando non ho più clienti in attesa in sala piccola
				System.out.println("***INGRESSO GRUPPO***");
				for(int i=0;i<10;i++) { //mando nella sala d'attesa piccola un gruppo di k=10 clienti
					Cliente proxcliente=codaesterna.poll(); //estraggo dalla coda il cliente in testa
					if(proxcliente!=null) { //se ho estratto un elemento lo mando nella sala sportelli
						salasportelli.accettacliente(proxcliente);
					}
				}
			}
		}
		//quando ho caricato tutti i clienti dico agli sportelli di chiudere quanto hanno servito tutti		
		salasportelli.chiudisportelli();
		input.close();
	}

}
