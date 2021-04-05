package projetBDR;

import projetBDR.enregistrement.Enregistrement;
import projetBDR.enregistrement.Localisation;
import projetBDR.enregistrement.Production;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Cette classe permet de lire le fichier csv et
 * extraire, stocker les informations séparement pour
 * la table de localisation et la table de production.
 *
 * @author Jianying Liu, Qi Wang
 */

public class CSVReader {
    private static ArrayList<Enregistrement> localisations = new ArrayList<>() ;
    private static ArrayList<Enregistrement> productions;

    /**
     * Méthode pour lire le fichier csv en traitant ";" comme le séparateur,
     * et stocker les informations dont on a besoin dans la ArrayList/Hashset
     * pour la table Localisation et la table Production.
     *
     * @param filePath chemin vers le fichier csv comportant les données
     *
     */
    public static void loadCSVFile(String filePath) {
        String line;
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            HashSet<Production> productionsDistinct = new HashSet<>();

            while ((line = br.readLine()) != null) {

                String[] info = line.split(cvsSplitBy);

                String id = info[0];
                String titre = info[3];
                String locationScene = info[6];
                int code = Integer.parseInt(info[7]);
                String debut = info[8];
                String fin = info[9];
                double cox = Double.parseDouble(info[10]);
                double coy = Double.parseDouble(info[11]);

                String an = info[1];
                int annee = Integer.parseInt(an);
                String type = info[2];
                String real = info[4];
                String prod = info[5];


                Localisation tableLoc = new Localisation(id, titre, locationScene, code, debut, fin, cox, coy);
                Production tableProd = new Production(titre, annee, type, real, prod);

                localisations.add(tableLoc);
                productionsDistinct.add(tableProd);
            }
            productions = new ArrayList<>(productionsDistinct);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * retourner la liste de la localisation.
     * @return localisations  objet du type ArrayList;
     */
    public static ArrayList<Enregistrement> getLocalisations() {
        return localisations;
    }

    /**
     * retourner la hashset de la productiion.
     * @return pruductions  objet du type ArrayList;
     */
    public static ArrayList<Enregistrement> getProductions() {
        return productions;
    }

}
