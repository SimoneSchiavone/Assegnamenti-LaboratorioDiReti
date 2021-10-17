//VERSIONE MONITOR
public class Tesista implements Runnable {
	int k;
	private int pcindex;
	Tutor laboratorio;
	
	public Tesista(Tutor x) {
		//il tesista vuole usare sempre lo stesso computer (scelgo casualmente)
		this.pcindex=(int)(Math.random()*19);
		this.k=(int) (Math.random()*3+1); //un utente accede un nr di volte tra 1 e 4
		this.laboratorio=x;
	}
	
	public void run() {	
		System.out.printf("Tesista: %s deve accedere %d volte al laboratorio\n",Thread.currentThread().getName(),this.k);
		for(int i=0;i<this.k;i++) {
			this.laboratorio.RichiestaTesista(pcindex);
			
			int tempoutilizzo=(int)(Math.random()*3000);
			System.out.printf("Tesista: %s utilizza il computer %d per %d millisecondi\n",Thread.currentThread().getName(),pcindex,tempoutilizzo);
			try {
				Thread.sleep(tempoutilizzo); //utilizza il computer per un tempo max di 3 secondi
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			this.laboratorio.UscitaTesista(pcindex);

			//simuliamo un tempo in cui il tesista sta fuori dal laboratorio (max 4 sec)
			try {
				int tempouscita=(int)(Math.random()*4000);
				Thread.sleep(tempouscita);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}	
		System.out.printf("***Tesista %s HA TERMINATO LA SUA ATTIVITA'***\n",Thread.currentThread().getName());
	}

}
