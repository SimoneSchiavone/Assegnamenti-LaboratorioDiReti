import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Simone Schiavone		Mat: 582418
 */
public class ContoCorrente implements Runnable{
	private int ID;
	private String Nome;
	private String Cognome;
	private ArrayList<Movimento> Movimenti;
	private static ArrayList<String> InsiemeDiNomi = new ArrayList<String>(Arrays.asList("Fernando","Italo","Flavio","Eva","Maria","Nadia","Luca","Lorenzo","Giovanni","Giacomo","Gloria","Emma","Laura","Federica","Pietro"));
	private static ArrayList<String> InsiemeDiCognomi = new ArrayList<String>(Arrays.asList("Zito","Lombardi","Milanesi","Romani","Arcuri","Lettiere","Russo","Gallo","Cattaneo","Nucci","Carlotti","Ferretti","Ghelli","Rossi","Bianchi","Colombo"));
	private static int nextid=1;
	private static int[] contatori=null;
	
	public ContoCorrente() {
		this.ID=nextid;
		nextid++;
		this.Nome=InsiemeDiNomi.get((int)(Math.random()*InsiemeDiNomi.size()));
		this.Cognome=InsiemeDiCognomi.get((int)(Math.random()*InsiemeDiCognomi.size()));;
		this.Movimenti=new ArrayList<Movimento>();
		int numeromovimenti=(int)(Math.random()*6)+1; 
		for(int i=0;i<numeromovimenti;i++) {
			this.Movimenti.add(new Movimento());
		}
	}
	
	public void StampaInfoConto() {
		System.out.println("ID: "+this.ID);
		System.out.println("NOME: "+this.Nome);
		System.out.println("COGNOME: "+this.Cognome);
		for(Movimento i : Movimenti)
			i.StampaInfoMovimento();
	}
	
	public void SetContatori(int[] array) {
		contatori=array;
	}
	
	public void run() {
		System.out.println(Thread.currentThread().getName()+" sta elaborando il conto "+this.ID);
		for(Movimento i : Movimenti) {
			switch(i.PrendiCausale()) {
				case "Bonifico":
					contatori[0]++;
					break;
				case "Accredito":
					contatori[1]++;
					break;
				case "Bollettino":
					contatori[2]++;
					break;
				case "F24":
					contatori[3]++;
					break;
				case "PagoBancomat":
					contatori[4]++;
					break;
				default:
					System.out.println("Operazione Sconosciuta");
			}
		}
	}
}
