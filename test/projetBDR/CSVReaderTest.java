package projetBDR;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import projetBDR.enregistrement.Enregistrement;

public class CSVReaderTest {


    @Test
    public void testLoadCSVFile() {
        //GIVEN
        String chemin = "./src/projetBDR/resources/lieux-de-tournage-a-paris.csv";

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