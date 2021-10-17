//VERSIONE MONITOR
public class Tutor {
	private int[] computer; //0 libero 1 occupato
	private int ProfInAttesa; //numero di professori in attesa
	private int numpc; //numero di pc liberi
	private int[] TesistiInAttesa;
	
	public Tutor() {
		this.computer=new int[20];
		this.TesistiInAttesa=new int[20];
		for(int i=0;i<20;i++) {
			//computer inizialmente liberi
			computer[i]=0; 
			this.TesistiInAttesa[i]=0;
		}
			
		this.ProfInAttesa=0;
		this.numpc=20;
	}
	
	public synchronized void RichiestaTesista(int pcindex){	
		this.TesistiInAttesa[pcindex]++;
		//se c'è un professore che sta aspettando oppure il pc è occupato 
		//metti il tesista in attesa
		while(this.ProfInAttesa>0 || this.computer[pcindex]==1) {
			System.out.printf("Tesista: %s si mette in attesa del PC %d\n",Thread.currentThread().getName(),pcindex);
			try {
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.computer[pcindex]=1;
		this.numpc--;
	}
	
	public synchronized void UscitaTesista(int pcindex) {
		this.computer[pcindex]=0;
		this.TesistiInAttesa[pcindex]--;
		this.numpc++;
		System.out.println("Si notifica tutti i thread dell'uscita del tesista");
		notifyAll();
	}
	
	public synchronized void RichiestaProfessore(){
		this.ProfInAttesa++;
		//se il laboratorio non è libero il professore si blocca
		while(this.numpc!=20) {
			System.out.printf("Professore: %s si mette in attesa dei PC\n",Thread.currentThread().getName());
			try {
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(int i=0;i<20;i++) {
			System.out.printf("Professore: %s acquisisce il pc %d\n",Thread.currentThread().getName(),i);
			this.computer[i]=1;
			this.numpc--;
		}
	}
	
	public synchronized void UscitaProfessore() {
		this.ProfInAttesa--;
		//libero tutti i computer
		for(int i=0;i<20;i++) {
			System.out.printf("Professore: %s libera il pc %d\n",Thread.currentThread().getName(),i);			
			this.computer[i]=0;
			this.numpc++;
		}
		System.out.println("Si notifica tutti i thread dell'uscita del professore");
		notifyAll();
	}
	
	public synchronized int RichiestaStudente(){
		int pcassegnato=-1;
		while(numpc==0 || ProfInAttesa>0) {
			//se non ci sono pc liberi oppure c'è un professore che sta aspettando
			System.out.printf("Studente: %s si mette in attesa di un pc libero\n",Thread.currentThread().getName());
			try {
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		//vado nel primo computer libero che trovo ciclando su tutti i pc
		while(pcassegnato==-1) {
			for(int i=0;i<20;i++) {
				if(this.computer[i]==0) {
					pcassegnato=i;
					this.computer[i]=1;
					this.numpc--;
					break;
				}
			}
		}
		return pcassegnato;
	}
	
	public synchronized void UscitaStudente(int pcindex) {
		this.computer[pcindex]=0;
		this.numpc++;
		notifyAll();
	}
}
