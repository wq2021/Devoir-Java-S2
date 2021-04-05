package projetBDR;

/**
 * exécuter avec l'étape initialisation:
 * java Main path/to/csv
 * Entrer directement dans l'étage manager
 * java Main
 */
public class Main {
    public static void main(String[] args){
        ManagerDB managerDB = new ManagerDB();
        if (args.length==1){
            String filePath = args[0];
            managerDB.prepareDBwithCSV(filePath);
        }
        Interface interf = new Interface();
        interf.managerOperation();
    }
}
