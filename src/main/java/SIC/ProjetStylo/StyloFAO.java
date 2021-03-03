package SIC.ProjetStylo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class StyloFAO {

	/**
	 * Pattern REGEX pour la récupération de l'ID de l'élément express 
	 */ 

	private static final String PATTERN_ID = "(#[0-9]*)"; 

	/** 
	 * Pattern REGEX pour la récupération de l'id d'un produit 
	 */ 

	private static final String PATTERN_ID_PRODUIT = "Produit\\('([a-zA-Z]*[0-9]*)"; 

	/** 
	 * Pattern REGEX pour la récupération du nom du produit 
	 */ 

	private static final String PATTERN_NOM_PRODUIT="Produit\\('[a-zA-Z]*[0-9]*',[\\s]*'([a-zA-Z0-9\\s]*)"; 

	/** 
	 * Pattern REGEX pour la récupération des produits composants 
	 */ 

	private static final String PATTERN_COMPOSANTS = "Produit\\(\\W[a-zA-Z]*[0-9]*\\W,[\\s]*\\W[\\w]*[\\s]*[\\w]*\\W,[\\s]*\\(([\\w|#|,[\\s]*]*)"; 

	/** 
	 * Hashmap<String, Produit> contenant les produits trouvés dans le fichier express 
	 * Où le String représente l'ID du produit vu par express (par exemple #1) 
	 */ 

	private static HashMap<String, Produit> map = new HashMap<String, Produit>(); 

	/** 
	 * Parse le fichier express styloFAO.owl 
	 * et enregistre les produits trouvés dans la liste map 
	 */ 

	static public List<Produit> parseExpressFile(){ 

		ArrayList<Produit> listProduits = new ArrayList<Produit>();

		//System.out.println("\n=========================PARSING FAO EXPRESS FILE=================================\n"); 
		Path file = Paths.get("styloFAO.exp"); 
		HashMap<String, String> mapDependances = new HashMap<String, String>(); 
		try { 
			InputStream in = Files.newInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line = null;

			Boolean isBeginData = false;

			//On parcourt ligne à ligne le fichier express
			while ((line = reader.readLine()) != null) {
				//On repére le début des données
				if(line.contentEquals("Begin data;"))
					isBeginData = true; 

				//Parsing terminé on sort de la boucle 
				else if(line.contentEquals("End data;")) 
					break; 

				//Si on a détecté un produit 
				else if(isBeginData && line.contains("Produit")){ 
					Pattern r = Pattern.compile(PATTERN_ID);
					String identifiantProduit ="";
					String nomProduit ="";
					String id = "";
					Produit p;
					//On récupére l'id express
					Matcher m = r.matcher(line);

					if (m.find( )) {
						//System.out.println("Produit: " + m.group(0) );
						id = m.group(0);
					} 

					//On récupére l'ID de l'objet du produit 
					r = Pattern.compile(PATTERN_ID_PRODUIT); 
					m=r.matcher(line); 

					if(m.find()){ 
						//System.out.println("\t id = " + m.group(0).split("Produit\\('")[1]); 
						identifiantProduit = m.group(0).split("Produit\\('")[1]; 
					} 

					//On récupére le nom du produit 
					r = Pattern.compile(PATTERN_NOM_PRODUIT); 

					m=r.matcher(line); 

					if(m.find()){
						//System.out.println("\t nom = " + m.group(0).split("Produit\\('[a-zA-Z]*[0-9]*',[\\s]*'")[1]);
						nomProduit = m.group(0).split("Produit\\('[a-zA-Z]*[0-9]*',[\\s]*'")[1];
					}

					//On crée un nouveau objet Produit et on l'enregistre dans la liste 
					if(!nomProduit.isEmpty() && !identifiantProduit.isEmpty()){ 
						p = new Produit(identifiantProduit, nomProduit); 
						map.put(id,p);
						listProduits.add(p);
						
					}

					//On récupére la liste des composants 
					r = Pattern.compile(PATTERN_COMPOSANTS); 

					m=r.matcher(line); 

					if(m.find()){

						/*System.out.println("\t dependances = " + m.group(0).split(
								"Produit\\(\\W[a-zA-Z]*[0-9]*\\W,[\\s]*\\W[\\w]*[\\s]*[\\w]*\\W,[\\s]*\\(")[1]);*/
						String dependances =  m.group(0).split(
								"Produit\\(\\W[a-zA-Z]*[0-9]*\\W,[\\s]*\\W[\\w]*[\\s]*[\\w]*\\W,[\\s]*\\(")[1];

						if(!dependances.isEmpty())
							mapDependances.put(id, dependances); 
						
					}
				}
			}

			//On compléte les dépendances en enregistrant la liste des composants dans l'objet produit associé 
			for (Object o : map.entrySet()) {
				Map.Entry<String, Produit> pair = (Map.Entry) o;
				String id =  pair.getKey();
				Produit produit = pair.getValue();

				if(mapDependances.containsKey(id)){
					String dependances = mapDependances.get(id);
					String[] dep = dependances.split(",[\\s]*"); 

					for(String idDep : dep){
						if(map.containsKey(idDep)){
							produit.addComposant(map.get(idDep));
							//						System.out.println("\t  " + map.get(idDep).toString());
							
						} 
					}
				}
			} 
			
			for(Produit p: listProduits) {
				//System.out.println(p.getPid());
			}
		} catch (IOException x) { 
			System.err.println(x); 
		}

		return listProduits;
}
	public static void main(String[] args) throws OWLOntologyCreationException {
		
		
		
		try {
			
			List<Produit> listProduits = StyloFAO.parseExpressFile();
			// Create the manager that we will use to load ontologies. 
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 

			// Create an ontology and name it 
			// "http://www.tiw3.sic/ontologies/nomOntologie" 
			IRI ontologyIRI = IRI.create("http://www.tiw3.sic/ontologies/StyloFAO.owl"); 

			// Create the document IRI for our ontology 
			File fileformated = new File("styloFAO.owl"); 
			IRI documentIRI = IRI.create(fileformated.toURI()); 

			// Set up a mapping, which maps the ontology to the document IRI 
			SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, documentIRI); 
			manager.getIRIMappers().add(mapper); 

			// Now create the ontology we use the ontology IRI (not the physical 
			// URI) 
			OWLOntology ontology; 
			ontology = manager.createOntology(ontologyIRI); 

			// Specify that A is a subclass of B.Add a subclass axiom. 
			OWLDataFactory factory = manager.getOWLDataFactory();  

			List<String> PID = new ArrayList<String>(); 
			List<String> PNOM = new ArrayList<String>(); 
			List<String> PMAJEUR = new ArrayList<String>(); 
			List<String> PMINEUR = new ArrayList<String>(); 

			for(Produit p: listProduits) {
				PID.add(p.getPid());
				PNOM.add(p.getPnom());

				for(Produit pmin: p.getComposants()) {
					PMAJEUR.add(p.getPid());
					PMINEUR.add(pmin.getPid());
				}
			}

			HashMap<String, OWLClass> classes = new HashMap<String, OWLClass>(); 

			// Création des classes 
			for (int i = 0; i < PID.size(); i++) { 
				String pID = PID.get(i); 
				String pNom = PNOM.get(i); 

				// Get hold of references to class 
				OWLClass cls = factory.getOWLClass(IRI.create(ontologyIRI + "#" + 
						Normalizer.normalize(pNom, Normalizer.Form.NFD) 
				.replaceAll("[\u0300-\u036F]", "") 
				.replaceAll(" ", "_"))); 

				classes.put(pID, cls); 
			} 

			// Création de la propriété est composeDe 
			OWLObjectProperty propriete = factory.getOWLObjectProperty(IRI.create(ontologyIRI + "#composeDe")); 

			// Pour chaque classe on associe la propriété 
			HashMap<String, OWLClassExpression> compositions = new HashMap<String, OWLClassExpression>(); 

			for (int j = 0; j < classes.size(); j++) { 
				String pID = PID.get(j); 
				compositions.put(pID,factory.getOWLObjectSomeValuesFrom(propriete,classes.get(pID))); 
			} 

			List<OWLSubClassOfAxiom> liste = new ArrayList<OWLSubClassOfAxiom>(); 

			// System.out.println("taille liste: " + composition.size()); 
			for (int h = 0; h < PMAJEUR.size(); h++) { 
				OWLSubClassOfAxiom ax = factory.getOWLSubClassOfAxiom( 
						classes.get(PMAJEUR.get(h)), 
						compositions.get(PMINEUR.get(h))); 

				liste.add(ax); 
				manager.applyChange(new AddAxiom(ontology, ax)); 
			} 

			// Save the ontology to the location where we loaded it from,in the 
			// default ontology format 
			manager.saveOntology(ontology, 
					new FunctionalSyntaxDocumentFormat(),
					documentIRI); 
			//new OWLDocumentFormat(),

			System.out.println("Fichier OWL Créé avec succés");

		} catch (OWLOntologyCreationException e) { 

			e.printStackTrace(); 
		} catch (OWLOntologyStorageException e) { 

			e.printStackTrace(); 
		} 
	}
		

}
