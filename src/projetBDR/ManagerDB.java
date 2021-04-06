package projetBDR;

import projetBDR.enregistrement.Enregistrement;

import java.util.*;

/**
 * Classe génère des sql selon args reçu et effectue des interactions avec la BD
 * Classe enfant de AbstractManagerDB
 *
 * @author Jianying Liu, Qi Wang
 */

public class ManagerDB extends AbstractManagerDB{

    /**
     * méthodes crée deux tables Cinema et LieuxTournage dans une base de données vide
     * imprimer un méssage de réussite si succès
     * utilisée par méthode prepareDBwithCSV()
     *
     * Info sur les tables :
     * Cinema : titre (clé primaire)      5 colonnes
     * LieuxTournage : identifiantLieu (clé primaire) titre (c'est le titre de production, clé étrangère)   8 colonnes
     * contreinte de clé étrangère: CASCADE
     */
    private void createTables(){
        String sql1 = "create table Cinema ( titre VARCHAR(100) not null, anneeTournage YEAR, type VARCHAR(30), " +
                "realisateur VARCHAR(60), producteur VARCHAR(100), primary key (titre));";
        String sql2 = "create table LieuxTournage ( identifiantLieu VARCHAR(10), titre VARCHAR(100) not null, localisationScene VARCHAR(200) not null, " +
                "codePostal CHAR(5), dateDebut DATE, dateFin DATE, coordonneeX DOUBLE not null, coordonneeY DOUBLE not null, primary key (identifiantLieu), " +
                "constraint film_constrainte foreign key (titre) references Cinema (titre) on delete cascade on update cascade);";
        String[] sqls = {sql1, sql2};
        boolean flag = updateByStatement(sqls);
        if (flag)
            System.out.println("Les tables LieuxTournage et Cinema sont bien créés");
    }

    /**
     * ajouter une liste de productions/localisations dans la BD
     * imprimer le nombre d'ajouts réussis en précisant le type (Production/Localisation)
     * utilisé par prepareDBwithCSV()
     *
     * @param enregistrements un ArrayList de Production/Localisation instances
     * @param tablename  type String, nom de table dans la BD, "Cinema" ou "LieuxTournage"
     */
    private void insertListOfEnregistrementToTable(ArrayList<Enregistrement> enregistrements, String tablename){
        String sql;
        String enregistType;
        if (tablename.equals("Cinema")){
            sql = "INSERT INTO cinema ( titre, anneeTournage, type, realisateur, producteur) VALUES (?, ?, ?, ?, ?)";
            enregistType = " production(s) ";
        } else {
            sql = "INSERT INTO LieuxTournage ( identifiantLieu, titre, localisationScene, codePostal, dateDebut, dateFin, coordonneeX, coordonneeY) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            enregistType = " localisation(s) ";
        }
        int counterSucces = updateMultiByPS(sql, enregistrements);
        System.out.println(counterSucces + enregistType + "sont sauvegardées dans le système maintenant !");
    }

    /**
     * Méthode qui initialise une base de données vide (création des tables) et remplit ces tables avec des infos dans un fichier csv
     * imprimer un message de réussite dans terminal si succès
     *
     * @param filePath type String, le chemain vers le fichier csv
     */
    public void prepareDBwithCSV(String filePath) {
        CSVReader.loadCSVFile(filePath);
        ArrayList<Enregistrement> listProductions = CSVReader.getProductions();
        ArrayList<Enregistrement> listLocalisations = CSVReader.getLocalisations();

        createTables();
        insertListOfEnregistrementToTable(listProductions,"Cinema");
        insertListOfEnregistrementToTable(listLocalisations,"LieuxTournage");
        System.out.println("Toutes les infos dans le fichier csv sont chargés dans la base de données!");
    }

    /**
     * Ajouter une production/localisation dans sa table correspondante
     * @param enregistrement Production/Localisation instance
     * @return Indicateur de succès/échec, type boolean
     */
    public boolean insertOneTupleInTable(Enregistrement enregistrement){
        String key = enregistrement.getPrimaryKey();
        String sql;
        if (key.equals("identifiantLieu")){
            sql = "INSERT INTO LieuxTournage ( identifiantLieu, titre, localisationScene, codePostal, dateDebut, dateFin, coordonneeX, coordonneeY) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO Cinema ( titre, anneeTournage, type, realisateur, producteur) VALUES (?, ?, ?, ?, ?)";
        }
        Object[] args = enregistrement.getAllFieldInfo();
        return updateOnePS(sql, args);
    }

    /**
     * Méthode supprime un enregistrement dans la table désignée, selon titre/identifiant de lieu fourni
     * imprime un message de réussite dans terminal si succès
     *
     * @param keyName type String, "titre"/"identifiantLieu"
     * @param primaryKey type String, le titre/identifiantLieu de l'enregistrement à supprimer
     * @param tableName type String, désigner le nom de table à modifier
     */
    public void deleteOneTupleByPrimaryKey(String keyName, String primaryKey, String tableName){
        String sql = "DELETE FROM " + tableName + " WHERE " + keyName + "=\"" + primaryKey + "\";";
        String[] sqls = {sql};
        boolean flag = updateByStatement(sqls);
        if (flag)
            System.out.println(primaryKey + " a bien été supprimé du système ! ");
    }

    /**
     * Méthode pour modifier un enregistrement
     * imprime un message de réussite dans terminal si succès
     *
     * @param tablename type String, désigner le nom de table à modifier
     * @param primaryKey type String, le titre/identifiantLieu de l'enregistrement à modifier
     * @param newValue type String, le nouveau réalisateur (pour une production) /localisation (pour une localisation) de scène
     */
    public void modifierOneTupleInTable(String tablename, String primaryKey, String newValue){
        String sql;
        if (tablename.equals("Cinema")){
            sql = "UPDATE Cinema SET realisateur = \"" + newValue + "\" WHERE titre= \"" + primaryKey + "\";";
        } else {
            sql = "UPDATE LieuxTournage SET localisationScene = \"" + newValue + "\" WHERE identifiantLieu= \"" + primaryKey + "\";";
        }
        String[] sqls = {sql};
        updateByStatement(sqls);
        System.out.println("L'info de " + primaryKey + " a été changée avec succès.");
    }


    /**
     * Vérifier l'existence d'une production
     * @param titre  titre de la production à vérifier, type String
     * @return Indicateur de son existence, type boolean
     */
    public boolean checkFilmExistence(String titre){
        String sql = "SELECT * FROM Cinema WHERE Cinema.titre =  \"" + titre + "\";";
        List<Map<String, Object>> resultList = queryByStatement(sql);
        return resultList.size() != 0;
    }

    //surcharge

    /**
     * chercher les enregistrements de lieux de tournage d'une certaine production, imprimer le résultat dans terminal
     * @param titre titre de production à requêter
     * @return type boolean, indiquer l'existance de ces localisations dans la base, renvoie false si aucun enregistrement est trouvé
     */
    public boolean searchInfo(String titre){
        String sql = "SELECT * FROM Cinema, LieuxTournage WHERE Cinema.titre = LieuxTournage.titre AND LieuxTournage.titre = \"" + titre + "\";";
        return searchBySQL(sql, titre);
    }

    /**
     * chercher les enregistrements de lieux de tournage d'une certaine production dans un certain quartier, imprimer le résultat dans terminal
     * @param titre titre de production à requêter
     * @param codePostal   type String, limite le quartier à chercher avec ce code postal
     * @return type boolean, indiquer l'existance de ces localisations dans la base, renvoie false si aucun enregistrement est trouvé
     */
    public boolean searchInfo(String titre, String codePostal){
        String sql = "SELECT * FROM Cinema, LieuxTournage WHERE cinema.titre = LieuxTournage.titre AND LieuxTournage.titre = \"" + titre + "\" AND codePostal =" + codePostal + ";";
        return searchBySQL(sql, titre);
    }

    /**
     * exécuter une requête sql, afficher tous les résultats trouvés dans le terminal
     * utilisé par searchInfo()
     *
     * @param sql type String, requête sql fixée pour Statement, sans args à insérer
     * @param titre titre de production à requêter
     * @return  type boolean, indiquer l'existance de ces localisations dans la base, renvoie false si aucun enregistrement est trouvé
     */
    private boolean searchBySQL(String sql, String titre){
        List<Map<String, Object>> resultList = queryByStatement(sql);

        //Pour avoir une sortie ordonnée de colonnes
        String[] colonnes = {"identifiantLieu", "localisationScene", "dateDebut", "dateFin", "anneeTournage", "type", "realisateur", "producteur"};

        int resultCount = resultList.size();
        System.out.println("#######################################");
        System.out.println("Résultats de recherche: " + resultCount + " lieu(x) de tournage trouvé(s) pour " + titre);
        System.out.println("#######################################");

        for (Map<String, Object> row : resultList){
            System.out.println("----------------------");
            for (String col : colonnes){
                System.out.println(col + " : " + row.get(col));
            }
        }
        return resultCount != 0;
    }

}

