
public class Consumer extends Thread {
	boolean vogliopari;
	Dropbox dropbox;
	
	public Consumer(boolean k, Dropbox d) {
		this.vogliopari=k;
		this.dropbox=d;
	}
	
	public void run() {
		System.out.printf("Il thread %s ha consumato il valore\n",Thread.currentThread().getName(),dropbox.take(this.vogliopari));
	}
}
