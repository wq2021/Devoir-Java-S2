package projetBDR;

import projetBDR.enregistrement.Enregistrement;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe abstraite définit des méthodes d'interaction avec MySQL
 * Méthodes à utiliser dans sa classe enfant : ManagerDB
 *
 * @author Jianying Liu, Qi Wang
 *
 */

public abstract class AbstractManagerDB {

    /**
     * Méthode pour modifier/ajouter/supprimer un tuple dans la base de données
     * @param sql type String, phrase sql avec args à remplir pour PreparedStatement
     * @param args Object tableau regroupe tous les args à remplir dans sql
     * @return Indicateur du succès/échec de l'opération, boolean type
     */
    public boolean updateOnePS(String sql, Object[] args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for(int i=0; i < args.length; i++){
                preparedStatement.setObject(i+1, args[i]);
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("La mise à jour a échouée ! Exception: ");
            System.out.println(e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(preparedStatement);
            JDBCUtils.close(connection);
        }
    }

    /**
     * Méthode pour ajouter une liste de productions/localisations dans la BD
     * @param sql  type String, phrase sql avec args à remplir pour PreparedStatement
     * @param enregists   type ArrayList, liste de Production/Localisation instances
     * @return Nombre d'insertions réussies à la BD, int type
     */
    public int updateMultiByPS(String sql, ArrayList<Enregistrement> enregists) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int counterTuple = 0;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            for (Enregistrement element : enregists){
                Object[] args = element.getAllFieldInfo();
                for(int i=0; i < args.length; i++)
                    preparedStatement.setObject(i + 1, args[i]);
                preparedStatement.executeUpdate();
                counterTuple++;
            }
        } catch (SQLException e) {
            System.out.println("La mise à jour a rencontrée un problème: ");
            System.out.println(e.getMessage());
        } finally {
            JDBCUtils.close(preparedStatement);
            JDBCUtils.close(connection);
        }
        return counterTuple;
    }

    /**
     * Méthode qui exécute un/plusieurs sql, pour ajouter/supprimer/modifier la BD
     * @param sqls  un tableau de String, recueille une liste des sql à exécuter
     *              chaine de caractères sql fixée, non paramètre à insérer
     * @return Indicateur du succès/échec de l'opération, boolean type
     */
    public boolean updateByStatement(String[] sqls){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            for (String sql : sqls) {
                statement.executeUpdate(sql);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("La mise à jour a échouée ! Exception: ");
            System.out.println(e.getMessage());
            return false;
        } finally {
            JDBCUtils.close(statement);
            JDBCUtils.close(connection);
        }
    }

    /**
     * Méthode pour exécuter une requête sql
     * @param sql  type String, requête sql fixée, non paramètre à insérer
     * @return liste des résultats, sous forme d'une List de Map instance
     */
    public List<Map<String, Object>> queryByStatement(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int nbColonne = metaData.getColumnCount();
            while (resultSet.next()){
                Map<String,Object> row = new HashMap<>();
                for (int i=1; i <= nbColonne; i++){
                    row.put(metaData.getColumnLabel(i), resultSet.getObject(i));
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            System.out.println("La requête a échouée ! Exception: ");
            System.out.println(e.getMessage());
        } finally {
            JDBCUtils.close(resultSet,statement);
            JDBCUtils.close(connection);
        }
        return resultList;
    }

}
