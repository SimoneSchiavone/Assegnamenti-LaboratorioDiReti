import java.util.Scanner;
//VERSIONE LOCK
public class Test {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		int nrprof,nrstud,nrtes;
		System.out.println("Inserisci il nr di studenti");
		nrstud=input.nextInt();
		System.out.println("Inserisci il nr di professori");
		nrprof=input.nextInt();
		System.out.println("Inserisci il nr di tesisti");
		nrtes=input.nextInt();
		input.close();
		
		Tutor laboratorio=new Tutor();
		for(int i=0;i<nrstud;i++) {
			new Thread(new Studente(laboratorio)).start();
		}
		for(int i=0;i<nrprof;i++) {
			new Thread(new Professore(laboratorio)).start();
		}
		for(int i=0;i<nrtes;i++) {
			new Thread(new Tesista(laboratorio)).start();
		}
	}

}