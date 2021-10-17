import java.io.*;
public class Producer extends Thread {
	private File a;
	private ThreadSafeLinkedList<File> directorylist;
	
	public Producer(File s,ThreadSafeLinkedList<File> l) {
		this.a=s;
		this.directorylist=l;
	}
	
	public void run() {
		ThreadSafeLinkedList<File> aux=new ThreadSafeLinkedList<File>();
		//In aux mi salvo le directory che sono necessarie per effettuare l'esplorazione
		aux.add(a);
		directorylist.add(a);
		
		while(aux.size()>0) { //ci sono file in lista
			File current=aux.remove();
			File[] files=current.listFiles();
			if(files!=null) {
				for(File k : files) {
					if(k.isDirectory()) {
						//Inserisco la nuova directory trovata nella linkedlist condivisa tra consumatori e produttori
						//ed in quella di supporto per l'esplorazione
						System.out.println(Thread.currentThread().getName()+"--> Inserisco nella coda la cartella "+k.getName());
						aux.add(k);
						directorylist.add(k);
					}
				}
			}
		}
	}

}
