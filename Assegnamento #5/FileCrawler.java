/*File Crawler
si scriva un programma JAVA che:
riceve in input un filepath che individua una directory D stampa le informazioni
del contenuto di quella directory e, ricorsivamente, tutti i file contenuti nelle
sottodirectory di D. Il programma deve essere strutturato come segue: 
attiva un thread produttore ed un insieme di k thread consumatori. il produttore
comunica con i consumatori mediante una coda. il produttore visita ricorsivamente
la directory data ed, eventualmente tutte le sottodirectory e mette nella coda il
nome di ogni directory individuata. i consumatori prelevano dalla coda i nomi 
delle directories e stampano il loro contenuto. la coda deve essere realizzata con
una LinkedList. Ricordiamo che una Linked List non è una struttura thread-safe.
Dalle API JAVA “Note that the implementation is not synchronized. If multiple 
threads access a linked list concurrently, and at least one of the threads modifies
 the list structurally, it must be synchronized externally”*/
import java.io.File;
import java.util.Scanner;

/**
 * @author Simone Schiavone			MAT:582418
 */
public class FileCrawler {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Digitare il FILEPATH. es: ./Esempio");
		Scanner input=new Scanner(System.in);
		File filepath=new File(input.nextLine());
		//Lista delle varie directory che è riempita dal thread produttore
		ThreadSafeLinkedList<File> list=new ThreadSafeLinkedList<File>();
		//File q=new File("./Esempio");
		Producer p=new Producer(filepath,list);
		//Producer p=new Producer(q,list);
		
		p.start();
		p.join();
		
		int k=3;
		for(int i=0;i<k;i++) {
			Consumer c=new Consumer(list);
			c.start();
		}
		input.close();
	}
}
