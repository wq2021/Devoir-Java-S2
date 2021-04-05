package projetBDR;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import projetBDR.enregistrement.Localisation;
import projetBDR.enregistrement.Production;

class ManagerDBTest {
	
	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;
	ManagerDB mDB = new ManagerDB();

	@BeforeEach
	void setUp() throws Exception {
		connection = JDBCUtils.getConnection();
		statement = connection.createStatement();
		String nomCle = "titre";
		String valeurCle = "Daft Punk Unchained";
		String nomTable = "Cinema";
		mDB.deleteOneTupleByPrimaryKey(nomCle, valeurCle, nomTable);
		
		String nomCleB = "identifiantLieu";
		String valeurCleB = "2015-5";
		String nomTableB = "LieuxTournage";
		mDB.deleteOneTupleByPrimaryKey(nomCleB, valeurCleB, nomTableB);
	}

	@AfterEach
	void tearDown(){

		JDBCUtils.close(resultSet, statement);
		JDBCUtils.close(connection);

	}

	@Test
	void testPrepareDBwithCSV() throws SQLException {
		//GIVEN
		// à changer avec votre chemin vers le fichier csv
		String chemin = "./src/projetBDR/resources/lieux-de-tournage-a-paris.csv";
		statement.executeUpdate("drop table LieuxTournage;");
		statement.executeUpdate("drop table Cinema;");
		
		//WHEN
		mDB.prepareDBwithCSV(chemin);
		String requete1 = "SELECT COUNT(titre) FROM Cinema";
		String requete2 = "SELECT COUNT(titre) FROM LieuxTournage";
		
        resultSet = statement.executeQuery(requete1);
        resultSet.next();
        int nbProc = resultSet.getInt(1);
        
        resultSet = statement.executeQuery(requete2);
        resultSet.next();
        int nbLieu = resultSet.getInt(1);
               
		//THEN
		assertEquals(nbProc, 1055);
		assertEquals(nbLieu, 8919);

	}

	@Test
	void testInsertOneTupleInTable() {
		//GIVEN
		Production prod = new Production("Daft Punk Unchained", 2015, "Film" ,"Patrice Gellé, Jean-Louis Blot", "Hervé Martin-Delpierre");
		
		//WHEN
		mDB.insertOneTupleInTable(prod);
		
		//THEN
		assertTrue(mDB.checkFilmExistence("Daft Punk Unchained"));
	}

	@Test
	void testDeleteOneTupleByPrimaryKey(){
		//GIVEN
		Production prod = new Production("Daft Punk Unchained", 2015, "Film" ,"Patrice Gellé, Jean-Louis Blot", "Hervé Martin-Delpierre");
		String nomCle = "titre";
		String valeurCle = "Daft Punk Unchained";
		String nomTable = "Cinema";
		
		//WHEN
		mDB.insertOneTupleInTable(prod);
		mDB.deleteOneTupleByPrimaryKey(nomCle, valeurCle, nomTable);
		
		//THEN
		assertFalse(mDB.checkFilmExistence("Daft Punk Unchained"));
	}

	@Test
	void testModifierOneTupleInTable() throws SQLException {
		//GIVEN
		Production prod = new Production("Daft Punk Unchained", 2015, "Film" ,"Patrice Gellé, Jean-Louis Blot", "Hervé Martin-Delpierre");
		String nomTable = "Cinema";
		String nomCle = "Daft Punk Unchained";
		String nouveauRealisateur = "Thomas Bangalter";
		
		//WHEN
		mDB.insertOneTupleInTable(prod);
		mDB.modifierOneTupleInTable(nomTable, nomCle, nouveauRealisateur);
		
		String requete = "SELECT realisateur FROM Cinema WHERE titre = \"Daft Punk Unchained\"";
        resultSet = statement.executeQuery(requete);
        resultSet.next();
		
		//THEN
		assertEquals(resultSet.getString("realisateur"), nouveauRealisateur);
	}

	@Test
	void testCheckFilmExistence() {
		//GIVEN
		Production prod = new Production("Daft Punk Unchained", 2015, "Film" ,"Patrice Gellé, Jean-Louis Blot", "Hervé Martin-Delpierre");
		mDB.insertOneTupleInTable(prod);
		
		//WHEN
		mDB.checkFilmExistence("Daft Punk Unchained");
		
		//THEN
		assertTrue(mDB.checkFilmExistence("Daft Punk Unchained"));
	}

	@Test
	void testSearchInfoString(){
		// GIVEN

		Production prod = new Production("Daft Punk Unchained", 2015, "Film" ,"Patrice Gellé, Jean-Louis Blot", "Hervé Martin-Delpierre");
		Localisation local = new Localisation("2015-5","Daft Punk Unchained", "rue de Lille, 75005, Paris", 75005 ,"2015-01-02","2015-05-20", 3.3333, 23.4444);
		mDB.insertOneTupleInTable(prod);
		mDB.insertOneTupleInTable(local);
		
		String titre = "Daft Punk Unchained";
		
		// WHEN
		boolean result = mDB.searchInfo(titre);
		
		// THEN
		assertTrue(result);
	}

	@Test
	void testSearchInfo() {
		// GIVEN
		Production prod = new Production("Daft Punk Unchained", 2015, "Film" ,"Patrice Gellé, Jean-Louis Blot", "Hervé Martin-Delpierre");
		Localisation local = new Localisation("2015-5","Daft Punk Unchained", "rue de Lille, 75005, Paris", 75005 ,"2015-01-02","2015-05-20", 3.3333, 23.4444);
		mDB.insertOneTupleInTable(prod);
		mDB.insertOneTupleInTable(local);
		String titre = "Daft Punk Unchained";
		String code = "75005";
		
		// WHEN
		boolean result = mDB.searchInfo(titre, code);
		
		// THEN
		assertTrue(result);
		
	}

}
