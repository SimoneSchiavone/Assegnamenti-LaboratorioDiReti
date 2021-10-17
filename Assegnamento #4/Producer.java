
public class Producer extends Thread {
	Dropbox dropbox;
	
	public Producer(Dropbox d) {
		this.dropbox=d;
	}
	
	public void run() {
		for(int i=0;i<3;i++)
			dropbox.put((int)(Math.random()*1000));
	}
}
