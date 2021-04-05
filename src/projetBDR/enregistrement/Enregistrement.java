package projetBDR.enregistrement;

/**
 * Classe abstraire
 * parent de Production et de Localisation
 * @author Jianying Liu, Qi Wang
 */

public abstract class Enregistrement {

    // déclaration des méthodes abstraires, à redéfinir dans ses classes enfant
    public abstract Object[] getAllFieldInfo();
    public abstract String getPrimaryKey();
}
