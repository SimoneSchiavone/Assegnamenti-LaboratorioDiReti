import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
public class ThreadSafeLinkedList<E> {
	private LinkedList<E> list;
	private ReentrantLock listlock;
	
	public ThreadSafeLinkedList() {
		this.list=new LinkedList<E>();
		this.listlock=new ReentrantLock();
	}
	
	public void add(E element) {
		listlock.lock();
		list.add(element);
		listlock.unlock();
	}
	
	public E remove() {
		listlock.lock();
		E aux=list.remove();
		listlock.unlock();
		return aux;
	}
	
	public int size() {
		return list.size();
	}
}
