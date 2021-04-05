package projetBDR.enregistrement;

/**
 * Classe qui présente les informations dans la table de la Production.
 * @author Jianying Liu, Qi Wang
 *
 */
public class Production extends Enregistrement{

    private final String PRIMARY_KEY = "titre";
    private String titre;
    private int anneeTournage;
    private String typeTournage;
    private String producteur;
    private String realisateur;

    /**
     * Constructeur de la classe Production
     * @param titre String
     * @param anneeTournage int
     * @param typeTournage String
     * @param producteur String
     * @param realisateur String
     */
    public Production(String titre, int anneeTournage, String typeTournage, String producteur, String realisateur) {
        this.titre = titre;
        setAnneeTournage(anneeTournage);
        this.typeTournage = typeTournage;
        this.producteur = producteur;
        this.realisateur = realisateur;
    }

    // redéfinir equals() et hashCode() de Object pour éliminer plus tard les doublons avec HashSet
    /**
     * L'utilisation de redéfinition pour réecrire la méthode equals
     * pour comparer le hashcode des deux titres.
     * @param productionIdentique instance de Production pour comparer avec
     * @return boolean
     */
    @Override
    public boolean equals(Object productionIdentique) {
        if (this == productionIdentique){
            return true;
        }
        if (productionIdentique instanceof Production) {                            //before downcasting, make sure the productionIdentique is a Production instance, to avoid possible ClassCastException
            return titre.equals( ((Production) productionIdentique).getTitre());   //downcasting productionIdentique from Object to Production
        }
        return false;
    }

    /**
     * Redéfinition de la méthode hashCode
     * @return hashCode de titre de Production instance
     */
    @Override
    public int hashCode() {
        return titre.hashCode();  //générer le hashcode selon le contenu : Objects.hash(title, anneeTournage...);
    }

    /**
     * Méthode renvoie un tableau de valeurs de tous les champs
     * Pratique de polymorphisme
     * @return Object tableau, valeurs des champs, ordonnées selon l'ordre dans la base de données
     */
    @Override
    public Object[] getAllFieldInfo() {
        return new Object[]{titre, anneeTournage, typeTournage, realisateur, producteur};
    }

    /**
     * Méthode renvoie nom de clé primaire de production dans la BD
     * Pratique de polymorphisme
     * @return String
     */
    @Override
    public String getPrimaryKey() {
        return PRIMARY_KEY;
    }

    /**
     * Méthode setter pour vérifier si anneeTournage
     * est dans le bon cadre (entre 1895 et 2050)
     * @param anneeTournage  type int
     */
    private void setAnneeTournage(int anneeTournage) {
        if ( anneeTournage > 2050 || anneeTournage < 1895 ){
            throw new IllegalArgumentException("L'année de tournage illégale !");
        }
        this.anneeTournage = anneeTournage;
    }

    public String getTitre() {
        return titre;
    }

    public int getAnneeTournage() {
        return anneeTournage;
    }

    public String getTypeTournage() {
        return typeTournage;
    }

    public String getProducteur() {
        return producteur;
    }

    public String getRealisateur() { return realisateur; }
}
