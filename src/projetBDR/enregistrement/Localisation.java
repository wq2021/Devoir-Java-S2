package projetBDR.enregistrement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe qui présente les informations dans la table de la Localisation
 * @author Jianying Liu, Qi Wang
 *
 */

public class Localisation extends Enregistrement{
    private final String PRIMARY_KEY = "identifiantLieu";
    private String identifiantLieu;
    private String titre;
    private String locationScene;
    private int codePostal;
    private String dateDebut;
    private String dateFin;
    private double coordoX;
    private double coordoY;

    /**
     * Constructeur de la classe Localisation
     * @param identifiantLieu clé primaire de localisation dans la table, unique
     * @param titre titre de la production, clé étrangère
     * @param locationScene adresse de lieu de tournage
     * @param codePostal   5 chiffres
     * @param dateDebut  format date aaaa-mm-jj, commencement de tournage à cet endroit
     * @param dateFin format date aaaa-mm-jj, fin de tournage à cet endroit
     * @param coordoX  type double, coordonnées géographiques
     * @param coordoY  type double, coordonnées géographiques
     *
     */
    public Localisation(String identifiantLieu, String titre, String locationScene, int codePostal, String dateDebut, String dateFin, double coordoX, double coordoY) {
        setIdentifiantLieu(identifiantLieu);
        this.titre = titre;
        this.locationScene = locationScene;
        setCodePostal(codePostal);
        setDateDebut(dateDebut);
        setDateFin(dateFin);
        this.coordoX = coordoX;
        this.coordoY = coordoY;
    }

    /**
     * Obtenir et renvoyer un objet contenant les informations de tous les champs
     * redéfinition de getAllFieldInfo() dans classe parent Enregistrement
     * @return champsInfo ordonnées selon l'ordre dans la BD
     */
    @Override
    public Object[] getAllFieldInfo() {
        Object[] champsInfo = {identifiantLieu,titre,locationScene,codePostal,dateDebut,dateFin,coordoX,coordoY};
        return champsInfo;
    }

    /**
     * renvoie le nom de la clé primaire de localisation
     * @return type String, fixé pour Localisation
     */
    @Override
    public String getPrimaryKey() {
        return PRIMARY_KEY;
    }

    /**
     * Méthode pour vérifier si une chaîne de caractères correspond au format date (yyyy-mm-dd).
     * @param date type String
     * @return valeur booléan : True/False
     */
    private boolean validateDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            format.parse(date);
        } catch (ParseException e){
            return false;
        }
        return true;
    }

    /**
     * Méthode setter pour vérifier si une chaîne de caractères
     * correspond au format identifiant lieu (regex).
     * @param identifiantLieu String
     */
    private void setIdentifiantLieu(String identifiantLieu) {
        Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]+");
        Matcher matcher = pattern.matcher(identifiantLieu);
        if (!matcher.matches()){
            throw new IllegalArgumentException("identifiant du lieu illégal (format demandé: annéeTournage-numéro)");
        }
        this.identifiantLieu = identifiantLieu;
    }

    /**
     * Méthode setter pour vérifier si le param
     * correspond au format code postal (5 chiffres).
     * @param codePostal int
     */
    private void setCodePostal(int codePostal) {
        if ( String.valueOf(codePostal).length()!= 5){
            throw new IllegalArgumentException("Code Postal doit être une séquence de 5 chiffres");
        }
        this.codePostal = codePostal;
    }


    /**
     * Méthode setter pour vérifier si dateDebut
     * correspond au format date.
     * @param dateDebut String
     */
    private void setDateDebut(String dateDebut) {
        if (!validateDate(dateDebut)){
            throw new IllegalArgumentException("Format de date incorrecte ! Format demandé: aaaa-mm-jj");
        }
        this.dateDebut = dateDebut;
    }

    /**
     * Méthode setter pour vérifier si dateFin
     * correspond au format date.
     * @param dateFin String
     */
    private void setDateFin(String dateFin) {
        if (!validateDate(dateFin)){
            throw new IllegalArgumentException("Format de date incorrecte ! Format demandé: aaaa-mm-jj");
        }
        this.dateFin = dateFin;
    }

    public String getIdentifiantLieu() {
        return identifiantLieu;
    }

    public String getTitre() {
        return titre;
    }

    public String getLocationScene() {
        return locationScene;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public double getCoordoX() {
        return coordoX;
    }

    public double getCoordoY() {
        return coordoY;
    }
}
