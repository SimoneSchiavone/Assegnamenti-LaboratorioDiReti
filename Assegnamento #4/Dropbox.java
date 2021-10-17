
public class Dropbox {
	private int buffer;
	
	synchronized int take(boolean x) {
		int res=0;
		if((this.buffer%2==0 && x) || (this.buffer%2==1 && !x))
			res=buffer;
		else {
			try {
				System.out.printf("Thread %s è in attesa \n",Thread.currentThread().getName());
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.buffer=0;
		System.out.printf("Thread %s ha consumato il valore %d\n",Thread.currentThread().getName(),res);
		return res;
	}
	
	synchronized void put(int n) {
		this.buffer=n;
		notify();
	}
	
	public static void main(String[] args) {
		Dropbox drop=new Dropbox();
		Consumer c1=new Consumer(true, drop);
		Consumer c2=new Consumer(true, drop);
		Consumer c3=new Consumer(false, drop);
		Producer p=new Producer(drop);
		p.start();
		c1.start();
		c2.start();
		c3.start();
		
	}
}
