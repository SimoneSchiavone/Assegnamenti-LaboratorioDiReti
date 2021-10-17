import java.io.File;

public class Consumer extends Thread {
	private ThreadSafeLinkedList<File> list;

	public Consumer(ThreadSafeLinkedList<File> a) {
		this.list=a;
	}
	
	public void run(){
		while(list.size()>0) {
			File dir=list.remove();
			if(!dir.isDirectory()) {
				System.out.println("Il file estratto non è una directory!");
				break;
			}else {
				File[] files=dir.listFiles();
				for(File w : files)
					if(w.isFile())
						System.out.println(Thread.currentThread().getName()+"--> DirectoryPadre: "+dir.getName()+" File: "+w.getName());
					else
						System.out.println(Thread.currentThread().getName()+"--> DirectoryPadre: "+dir.getName()+" Directory: "+w.getName());
				
			}
		}
	}

}
