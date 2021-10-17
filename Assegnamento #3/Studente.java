//VERSIONE LOCK
public class Studente implements Runnable{
	int k;
	Tutor laboratorio;
	
	public Studente(Tutor x) {
		this.k=(int) (Math.random()*3+1); //un utente accede un nr di volte tra 1 e 4
		this.laboratorio=x;
	}
	
	public void run() {
		System.out.printf("Studente: %s deve accedere %d volte al laboratorio\n",Thread.currentThread().getName(),this.k);
		for(int i=0;i<this.k;i++) {
			int pcassegnato=this.laboratorio.RichiestaStudente();
			
			int tempoutilizzo=(int)(Math.random()*3000);
			System.out.printf("Studente: %s utilizza il computer %d per %d millisecondi\n",Thread.currentThread().getName(),pcassegnato,tempoutilizzo);
			try {
				Thread.sleep(tempoutilizzo); //utilizza il computer per un tempo max di 3 secondi
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			this.laboratorio.UscitaStudente(pcassegnato);

			//simuliamo un tempo in cui il professore sta fuori dal laboratorio (max 4 sec)
			try {
				int tempouscita=(int)(Math.random()*4000);
				Thread.sleep(tempouscita);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}	
		System.out.printf("***Studente %s HA TERMINATO LA SUA ATTIVITA'***\n",Thread.currentThread().getName());
	}
}
