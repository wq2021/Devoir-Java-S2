package projetBDR.enregistrement;

/**
 * Exception personalisée
 *
 * @author Jianying Liu, Qi Wang
 */

public class IllegalEntryException extends Exception{

	private static final long serialVersionUID = 1L;

	IllegalEntryException(String msg){
        super(msg);
    }
}
