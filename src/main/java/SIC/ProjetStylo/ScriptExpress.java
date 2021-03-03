package SIC.ProjetStylo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ScriptExpress {

	public static void main(String[] args) throws IOException {
		
		PrintWriter pw = null;
		
		try {
			
			File file = new File("styloFAO.exp");
			FileWriter fw = new FileWriter(file,true);
			pw = new PrintWriter(fw);
			pw.println("ISO 10303_21;");
			pw.println("Begin data;");
			pw.println("#1 = Produit('P1', 'Stylo',(#2, #3, #4, #5));");
			pw.println("#2 = Produit('P2', 'Capuchon', $);");
			pw.println("#3 = Produit('P3', 'Bouchon', $);");
			pw.println("#4 = Produit('P4', 'Tube cylindrique', $);");
			pw.println("#5 = Produit('P5', 'Cartouche', (#6, #7));");
			pw.println("#6 = Produit('P6', 'Mine', $) ;");
			pw.println("#7 = Produit('P7', 'Pointe', (#8, #9));");
			pw.println("#8 = Produit('P8', 'Bille', $);");
			pw.println("#9 = Produit('P9', 'Objet conique', $);");
			pw.println("End data;");
			pw.println("End ISO 10303_21;");
		}
		catch (IOException e) {
		     e.printStackTrace();
		  } finally {
		     if (pw != null) {
		        pw.close();
		     }
		  }
	}

}
