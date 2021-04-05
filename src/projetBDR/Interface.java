package projetBDR;

import projetBDR.enregistrement.Localisation;
import projetBDR.enregistrement.Production;

import java.util.Scanner;

/**
 * Classe qui propose une interface pour faire interagir entre l'utilisateur et la BD. 
 * @author Jianying Liu, Qi Wang
 */
public class Interface {
    private final Scanner QUESTION = new Scanner(System.in);

    /**
     * Méthode qui dirige vers les opérations différentes 
     * en fonction de la réponse du client :
     * S pour chercher (et l'option de supprimer une information dans ce bloc)
     * A pour ajouter, M pour modifier.
     */
    public void managerOperation(){
        String answer = welcomePart();
        ManagerDB managerDB = new ManagerDB();
        while (!answer.equals("quit")){
            switch (answer){
                case "S":
                    // param1 : titre
                    String titre = scanAnswer("Quel titre voulez-vous chercher ?");
                    String boolCode = scanAnswer("Vous voulez chercher avec un code postal parisien ? Taper Y pour oui, N pour non.");
                    // param2 : code_postal est vide
                    if (boolCode.equals("N")) {
                        boolean result = managerDB.searchInfo(titre);
                        if (result){
                            deleteLocalOrProduction(managerDB);
                        }
                    }
                    // param2 : code_postal n'est pas vide
                    else if(boolCode.equals("Y")){
                        String codePostal = scanAnswer("Insérez un code postal, s'il vous plaît:");
                        boolean result = managerDB.searchInfo(titre, codePostal);
                        if (result){
                            deleteLocalOrProduction(managerDB);
                        }
                    }
                    break;

                case "A":
                    String choixAjouter = scanAnswer("Désirez-vous ajouter une localisation(Tapez 1) ou une production(Tapez 2) ?");
                    if(choixAjouter.equals("1")){
                        addNewLocation(managerDB);
                    }
                    else if (choixAjouter.equals("2")) {
                        addNewProduction(managerDB);
                    }
                    break;

                case "M":
                    String filmTitre = scanAnswer("Entrez le titre de la production: ");
                    String choixModifier = scanAnswer("Quelle information désirez-vous modifier ? Taper 1 pour une localisation, Taper 2 pour la production.");
                    boolean flag = managerDB.searchInfo(filmTitre);
                    if(choixModifier.equals("1") && flag){
                        System.out.println("Maintenant vous allez modifier une information pour la localisation.");
                        String idLieu = scanAnswer("Entrez l'identifiant du lieu à modifier: ");

                        /// Modification1 : localisation de la scène
                        String newLocalisation = scanAnswer("Entrez la nouvelle localisation de la scène pour cet identifiant: ");
                        managerDB.modifierOneTupleInTable("LieuxTournage", idLieu, newLocalisation);
                    }
                    else if (choixModifier.equals("2") && flag) {
                        System.out.println("Maintenant vous allez modifier une information pour la production.");

                        // Modification 2 : réalisateur de la production
                        String newRealisateur = scanAnswer("Entrez le nouveau réalisateur pour cette production :");
                        managerDB.modifierOneTupleInTable("Cinema", filmTitre, newRealisateur);
                    } else {
                        System.out.println("Production non trouvée, modification impossible.");
                    }
                    break;

                default: System.out.println("Bien essayé ! Mais on ne propose rien pour vous.");
            }
            answer = scanAnswer("Tapez 'S' pour chercher, 'A' pour ajouter une information, 'M' pour modifier une information, 'quit' pour quitter le système.");
        }
    }

    /**
     * Méthode qui permet de supprimer une information selon le choix de l'utilisateur.
     * On propose deux choix : 1 pour supprimer un lieu de tournage, 2 pour supprimer un film et tous ses lieux de tournage.
     * @param managerDB objet ManagerDB instancié dans managerOperation()
     */
    private void deleteLocalOrProduction(ManagerDB managerDB){
        String boolSupprimer = scanAnswer("Vous voulez supprimer certaines informations parmi ces résultats ? Taper Y pour oui, N pour non.");
        if (boolSupprimer.equals("Y")){
            String choixSupprimer = scanAnswer("Taper 1 pour supprimer un lieu de tournage, Taper 2 pour supprimer un film et tous ses lieux de tournage.");
            if(choixSupprimer.equals("1")){
                System.out.println("Maintenant vous allez supprimer l'information pour un lieu de tournage.");
                String idLieu = scanAnswer("Entrez l'identifiant du lieu à supprimer: ");
                managerDB.deleteOneTupleByPrimaryKey("identifiantLieu", idLieu, "LieuxTournage");
            }
            else if (choixSupprimer.equals("2")) {
                System.out.println("Maintenant vous allez supprimer une production et ses lieux de tournage.");
                String titreSupprimer = scanAnswer("Entrez le titre du film à supprimer: ");
                managerDB.deleteOneTupleByPrimaryKey("titre", titreSupprimer, "Cinema");
            }
        }
        else if (boolSupprimer.equals("N")) {
            System.out.println("ok!");
        }
    }

    /**
     * Methode qui permet d'ajouter une nouvelle production
     * selon les informations fournies de l'utilisateur.
     * @param managerDB objet ManagerDB instancié dans managerOperation()
     */
    private void addNewProduction(ManagerDB managerDB){
        try {
            System.out.println("Maintenant vous allez ajouter une info pour le film.");

            String titre = scanAnswer("Titre:");
            String anneeTournage = scanAnswer("Année de tournage (format: aaaa):");
            int annee = Integer.parseInt(anneeTournage);
            String typeTournage = scanAnswer("Type de tournage:");
            String producteur = scanAnswer("Producteur:");
            String realisateur = scanAnswer("Réalisateur:");
            Production production = new Production(titre, annee, typeTournage, producteur, realisateur);
            boolean flag = managerDB.insertOneTupleInTable(production);
            if (flag)
                System.out.println("L'oeuvre \"" + production.getTitre() + "\" est bien ajoutée.");
        } catch (IllegalArgumentException e){
            System.out.println("Entrée illégale:" + e.getMessage());
            System.out.println("Recommencez : ");
            addNewProduction(managerDB);
        }
    }

    /**
     * Methode qui permet d'ajouter une nouvelle localisation.
     * selon les informations fournies de l'utilisateur.
     * @param managerDB objet ManagerDB instancié dans managerOperation()
     */
    private void addNewLocation(ManagerDB managerDB){
        System.out.println("Maintenant vous allez ajouter une info de localisation.");
        String titre = scanAnswer("Titre: ");
        try {
            if (managerDB.checkFilmExistence(titre)){
                String identifiant = scanAnswer("Identifiant du lieu:");
                String locationScene = scanAnswer("Localisation de la scène:");
                String codePostal = scanAnswer("Code postal: ");
                int code = Integer.parseInt(codePostal);
                String dateDebut = scanAnswer("Date de début (format: aaaa-mm-jj):");
                String dateFin = scanAnswer("Date de fin (format: aaaa-mm-jj):");
                String coordoX = scanAnswer("Coordonnée en X:");
                double cox = Double.parseDouble(coordoX);
                String coordoY = scanAnswer("Coordonnée en Y:");
                double coy = Double.parseDouble(coordoY);

                Localisation localisation = new Localisation(identifiant, titre, locationScene, code, dateDebut, dateFin, cox, coy);
                boolean flag = managerDB.insertOneTupleInTable(localisation);
                if (flag)
                    System.out.println("La localisation" + localisation.getTitre() + "est ajouté dans la base");
            } else {
                System.out.println(titre + "n'existe pas dans le système, ajoutez-le d'abord ? (Y/N)");
                if (QUESTION.nextLine().equals("Y")){
                    addNewProduction(managerDB);
                    addNewLocation(managerDB);
                }
            }
        } catch (IllegalArgumentException e){
            System.out.println("Entrée illégale:" + e.getMessage() + " \nRecommencez : ");
            addNewLocation(managerDB);
        }
    }

    /**
     * Méthode pour l'accueil, on fait afficher 
     * quelques lignes pour présenter la BD.
     * @return scanAnswer
     */
    private String welcomePart() {
        System.out.println("Bienvenue dans nos bases de données !");
        System.out.println("La base de données se compose des informations d'un film, notamment le lieu de tournage. \n ");
        System.out.println("Maintenant, voulez-vous procéder à quelle opération ? ");
        return scanAnswer("Tapez 'S' pour chercher, 'A' pour ajouter une information, 'M' pour modifier une information. ");
    }
    
    /**
     * Méthode pour renvoyer la réponse de l'utilisateur
     * étant donné qu'une question donnée.
     * @param question question imprimée dans le terminal pour récupérer l'argument
     * @return QUESTION.nextLine() ligne suivante de la question, soit la réponse de l'utilisateur.
     */
    private String scanAnswer(String question){
        System.out.println(question);
        return QUESTION.nextLine();
    }
}
