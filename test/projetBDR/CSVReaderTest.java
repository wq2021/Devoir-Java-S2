package projetBDR;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import projetBDR.enregistrement.Enregistrement;

public class CSVReaderTest {


    @Test
    public void testLoadCSVFile() {
        //GIVEN
    	// à changer le chemin absolu selon votre propre environnement
        String chemin = "C:\\Users\\liujianying\\Desktop\\java\\lieux-de-tournage-a-paris.csv";

        //WHEN
        CSVReader.loadCSVFile(chemin);
        ArrayList<Enregistrement> local = CSVReader.getLocalisations();
        ArrayList<Enregistrement> prod = CSVReader.getProductions();

        //THEN
        assertNotNull(local);
        assertNotNull(prod);
        assertEquals(local.size(),8919);
        assertEquals(prod.size(),1055);
    }

}