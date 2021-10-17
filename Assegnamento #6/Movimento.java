import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Simone Schiavone 	MAT:582418
 */
public class Movimento {
		private int Anno;
		private int Mese;
		private int Giorno;
		private String Causale;
		private ArrayList<String> ListaCausali= new ArrayList<String>(Arrays.asList("Bonifico","Accredito","Bollettino","F24","PagoBancomat"));
		
		public Movimento() {
			this.Anno=(int)(Math.random()*2)+2019;
			this.Mese=(int)(Math.random()*11)+1;
			this.Giorno=(int)(Math.random()*30)+1;
			this.Causale=ListaCausali.get((int)(Math.random()*5));
		}
		public void StampaInfoMovimento() {
			System.out.printf("\t  DATA: "+this.Giorno+"/"+this.Mese+"/"+this.Anno+"\n");
			System.out.printf("\t  CAUSALE: "+this.Causale+"\n\n");
		}
		
		public String PrendiCausale() {
			return this.Causale;
		}
}
