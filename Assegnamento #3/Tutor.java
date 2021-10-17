//VERSIONE LOCK
import java.util.concurrent.locks.*;
public class Tutor {
	private int[] computer; //1 se occupato, 0 se libero
	private Lock[] computerlocks;
	private Condition[] computerconds;
	
	private int numpc; //variabile che memorizza in un preciso istante quanti sono i pc liberi
		
	private int ProfInAttesa;
	private Lock proflock;
	private Condition profcond;
	
	private Lock studlock;
	private Condition studcond;
	
	public Tutor() {
		this.computer=new int[20];
		this.computerlocks=new Lock[20];
		this.computerconds=new Condition[20];
		for(int i=0;i<20;i++) {
			this.computer[i]=0;
			this.computerlocks[i]=new ReentrantLock();
			this.computerconds[i]=this.computerlocks[i].newCondition();
		}
		
		this.ProfInAttesa=0;
		this.proflock=new ReentrantLock();
		this.profcond=proflock.newCondition();
		
		this.numpc=20;
		
		this.studlock=new ReentrantLock();
		this.studcond=studlock.newCondition();
		
	}
	
	public void RichiestaTesista(int index) {
		computerlocks[index].lock();
		try {
			while(computer[index]==1 || ProfInAttesa>0) { 
				System.out.printf("Tesista: %s si mette in attesa del pc %d\n",Thread.currentThread().getName(),index);
				computerconds[index].await();
			}
			computer[index]=1;
			numpc--;
		}catch(InterruptedException e) {
			e.printStackTrace();
		}finally {
			computerlocks[index].unlock();
		}
	}
	
	public void UscitaTesista(int index) {
		 computerlocks[index].lock();
		 try {
			computer[index]=0;
			numpc++;
			if(ProfInAttesa!=0) {
				proflock.lock();
				profcond.signalAll();
				proflock.unlock();
			}
			else {
				computerconds[index].signalAll();
				studlock.lock();
				studcond.signalAll();
				studlock.unlock();
			}
		 }finally {
			computerlocks[index].unlock();
		 }
	 }
	
	public void RichiestaProfessore() {
		proflock.lock();
		try {
			this.ProfInAttesa++;
			while(this.numpc!=20) {
				System.out.printf("Professore: %s si mette in attesa dei computer\n",Thread.currentThread().getName());
				profcond.await();
			}
			for(int i=0;i<20;i++) {
				System.out.printf("Professore: %s acquisisce il pc %d\n",Thread.currentThread().getName(),i);
				computer[i]=1;
				computerlocks[i].lock(); //riesco sicuramente ad acquisire tutte le lock 
				this.numpc--;
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally {
			proflock.unlock();
		}
	}
	
	public void UscitaProfessore() {
		proflock.lock();
		try {
			this.ProfInAttesa--;
			for(int i=0;i<20;i++) { //rilasciamo le lock di tutti i computer
				computer[i]=0;
				this.numpc++;
				if(ProfInAttesa==0) { 
					//se non ci sono professori in attesa, attiva gli utenti che stanno aspettando quel pc
					computerconds[i].signalAll();
				}
				computerlocks[i].unlock(); 
			}
			System.out.printf("Professore: ha rilasciato tutti i pc\n",Thread.currentThread().getName());
			//se c'è qualche professore che sta aspettando notificalo
			if(ProfInAttesa==0) {
				studlock.lock();
				studcond.signalAll();
				studlock.unlock();
			}
			else {
				profcond.signalAll();
				System.out.printf("Si notificano i professori in attesa \n");
			}
		}finally {
			proflock.unlock();
		}
	}

	public int RichiestaStudente() {
		int pcassegnato=-1;
		studlock.lock();
		try {
			while(numpc==0 || ProfInAttesa>0) {
				//se non ci sono pc liberi oppure c'è un professore che sta aspettando
				System.out.printf("Studente: %s si mette in attesa di un pc libero\n",Thread.currentThread().getName());
				studcond.await();
			}
			this.numpc--; //decremento il nr di pc liberi
			
			//se sono stato svegliato significa che ci sarà almeno un pc libero 	
			for(int i=0;i<20;i++) {
				if(computerlocks[i].tryLock()) {
					//se il computer è "libero" con la tryLock acquisisco la sua lock
					computer[i]=1;
					pcassegnato=i;
					break;
				}
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally {
			studlock.unlock();
		}
		return pcassegnato;
	}

	public void UscitaStudente(int pcindex) {
		studlock.lock();
		try {
			computer[pcindex]=0;
			this.numpc++;
			//sveglio tesisti
			computerconds[pcindex].signalAll(); 
			computerlocks[pcindex].unlock();
			//sveglio i professori
			if(ProfInAttesa>0) {
				proflock.lock();
				profcond.signalAll();
				proflock.unlock();
			}
		}finally {
			studlock.unlock();
		}
		return;		
	}
}
