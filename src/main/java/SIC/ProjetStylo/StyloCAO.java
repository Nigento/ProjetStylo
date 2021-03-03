package SIC.ProjetStylo;

import java.io.File;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class StyloCAO {

	public static void main(String[] args) throws SQLException {
		
		try {
			
			// Create the manager that we will use to load ontologies. 
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 

			// Create an ontology and name it 
			// "http://www.tiw3.sic/ontologies/nomOntologie" 
			IRI ontologyIRI = IRI.create("http://www.tiw3.sic/ontologies/StyloCAO.owl"); 

			// Create the document IRI for our ontology 
			File fileformated = new File("styloCAO.owl"); 
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
			Connexion_BD connexion = new Connexion_BD(); 

			List<String> PID = connexion.getPID(); 
			List<String> PNOM = connexion.getPNom(); 
			List<String> PMAJEUR = connexion.getPMajeur(); 
			List<String> PMINEUR = connexion.getPMineur(); 

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
			manager.saveOntology(ontology, new FunctionalSyntaxDocumentFormat(),documentIRI); 

			} catch (OWLOntologyCreationException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
			} catch (OWLOntologyStorageException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
			}
	}
}
