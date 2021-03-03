package SIC.ProjetStylo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

public class App
{
    public static void main( String[] args )
    {
    	try {/*
		    String url = "jdbc:postgresql://bd-pedago.univ-lyon1.fr:5432/p1510353";
			Properties props = new Properties();
			props.setProperty("user","p1510353");
			props.setProperty("password","ohFpcNuP7RZh");
			Connection connection = DriverManager.getConnection(url, props);
		 
		    System.out.println("Connecté à la base de données PostgreSQL!");
		    
		    // Travail sur la base
		    // Ici, on écrira du code pour, par exemple, interroger la base
		    // Exemple :
		    Statement statement = ((Connection) connection).createStatement();
		    System.out.println("Lecture d'une table ...");
		    ResultSet resultSet = statement.executeQuery("SELECT * FROM Produit");
		    while (resultSet.next()) {
		        System.out.printf(resultSet.getString("Pnum"));
		    }
		    
		    // Ne pas oublier de fermer la session quand on a fini de manipuler la base
		    ((Connection) connection).close();
    		*/
    		Connexion_BD connexion = new Connexion_BD();
    		
    		List<String> PID = connexion.getPID();
    		
    		List<String> PNOM = connexion.getPNom(); 
    		
    		
    		for(int i=0;i<PID.size();i++) {
    			System.out.println(PNOM.get(i));
    			System.out.println(PID.get(i));
    		}
    		
		 
		    } catch (SQLException e) {
		        System.out.println("Connection failure.");
		        e.printStackTrace();
		        }
    	
    }
}
