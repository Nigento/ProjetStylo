package SIC.ProjetStylo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Connexion_BD{

	
	//Va chercher les deux objets ResultSet liés à la BD pour remplir des listes avec les données
	public List<String> getPID() throws SQLException {
		
		String url = "jdbc:postgresql://bd-pedago.univ-lyon1.fr:5432/p1510353";
		Properties props = new Properties();
		props.setProperty("user","p1510353");
		props.setProperty("password","ohFpcNuP7RZh");
		
		//Lance la connexion
		Connection connection = DriverManager.getConnection(url, props);
		Statement statement = ((Connection) connection).createStatement();
		
		//Recherche les tables et sélectionne toutes les données pour les associer aux variables de la classe
		ResultSet resultSet = statement.executeQuery("SELECT * FROM Produit");
		
		List<String> foo = new ArrayList<String>();

		while (resultSet.next()) {
	        foo.add(resultSet.getString("Pnum"));
	    }
		return foo;
	}
	
	public List<String> getPNom() throws SQLException {
		
		String url = "jdbc:postgresql://bd-pedago.univ-lyon1.fr:5432/p1510353";
		Properties props = new Properties();
		props.setProperty("user","p1510353");
		props.setProperty("password","ohFpcNuP7RZh");
		
		//Lance la connexion
		Connection connection = DriverManager.getConnection(url, props);
		Statement statement = ((Connection) connection).createStatement();
		
		//Recherche les tables et sélectionne toutes les données pour les associer aux variables de la classe
		ResultSet resultSet = statement.executeQuery("SELECT * FROM Produit");
		
		List<String> foo = new ArrayList<String>();

		while (resultSet.next()) {
	        foo.add(resultSet.getString("Pnom"));
	    }
		return foo;
	}
	
	public List<String> getPMajeur() throws SQLException {
		
		String url = "jdbc:postgresql://bd-pedago.univ-lyon1.fr:5432/p1510353";
		Properties props = new Properties();
		props.setProperty("user","p1510353");
		props.setProperty("password","ohFpcNuP7RZh");
		
		//Lance la connexion
		Connection connection = DriverManager.getConnection(url, props);
		Statement statement = ((Connection) connection).createStatement();
		
		//Recherche les tables et sélectionne toutes les données pour les associer aux variables de la classe
		ResultSet resultSet = statement.executeQuery("SELECT * FROM Composition");
		
		List<String> foo = new ArrayList<String>();
		while (resultSet.next()) {
	        foo.add(resultSet.getString("PMajeur"));
	    }
		return foo;
		
	}
	
	public List<String> getPMineur() throws SQLException {
		
		String url = "jdbc:postgresql://bd-pedago.univ-lyon1.fr:5432/p1510353";
		Properties props = new Properties();
		props.setProperty("user","p1510353");
		props.setProperty("password","ohFpcNuP7RZh");
		
		//Lance la connexion
		Connection connection = DriverManager.getConnection(url, props);
		Statement statement = ((Connection) connection).createStatement();
		
		//Recherche les tables et sélectionne toutes les données pour les associer aux variables de la classe
		ResultSet resultSet = statement.executeQuery("SELECT * FROM Composition");
		
		List<String> foo = new ArrayList<String>();
		while (resultSet.next()) {
	        foo.add(resultSet.getString("PMineur"));
	    }
		return foo;
	}
	
	
	
}
