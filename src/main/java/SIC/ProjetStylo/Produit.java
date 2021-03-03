package SIC.ProjetStylo;

import java.util.ArrayList;
import java.util.List;

public class Produit {
	
	//attributs
	private String pid;
	private String pnom;
	private List<Produit> composants = new ArrayList<Produit>();
	
	//constructeur du produit qui prend les données extraites de la BD
	public Produit(String pid, String pnom) {
		this.pid=pid;
		this.pnom=pnom;
		
	}
	
	//méthode qui ajoute un nouveau composant à une produit existant. Permet de faire le lien Pmaj, Pmin.
	public void addComposant(Produit p) {
		composants.add(p);
	}
	
	//Méthode qui affiche le produit et ses composants de manière récursive.
	public String toString() {
		
		String foo ="";
		foo+=this.pid+"\n";
		foo+=this.pnom+"\n";
		if(this.composants.size()>0); foo+=composants.toString();
		return foo;
	}
	
	public String getPid() {
		return this.pid;
	}
	
	public String getPnom() {
		return this.pnom;
	}
	public List<Produit> getComposants(){
		return this.composants;
	}
	
	public String getParent(List<Produit> produits) {
		
		for(Produit produit1 : produits) {
			List<Produit> compos = produit1.getComposants();
			for(Produit compo : compos) {
				if(this.pid.equals(compo.getPid())) {
					return compo.getPid();
				}
				
			}
			
		}
		return null;
	}

}
