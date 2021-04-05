package projetBDR;


import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 * Classe d'outils pour AbstractManagerDB et ManagerDB
 * utilisé pour connecter au MySQL ou libérer les ressources après usage
 *
 * @author Jianying Liu, Qi Wang
 */

public class JDBCUtils {
    private static String DB_URL;
    private static String USER;
    private static String PASS;


    //静态代码块指 Java 类中的 static{ } 代码块，主要用于初始化类，为类的静态变量赋初始值，提升程序性能。
    /*
      lire le fichier properties et affecter les paramètres par rapport à la base de données
     */
    static {
        try {
            //obtenir le chemin vers fichier db.properties
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();
            URL res = classLoader.getResource("projetBDR/resources/db.properties");
            String path = null;
            if (res != null) {
                path = res.getPath();
            }

            // instancier objet properties et importer le contenu du fichier
            Properties pro = new Properties();
            if (path != null) {
                pro.load(new FileReader(path));
            }

            // obtenir les valeurs
            String JDBC_DRIVER = pro.getProperty("JDBC_DRIVER");
            DB_URL = pro.getProperty("DB_URL");
            USER = pro.getProperty("USER");
            PASS = pro.getProperty("PASS");

            // inscrire le driver
            Class.forName(JDBC_DRIVER);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * connecter à la base de données
     * @return objet Connection
     * @throws SQLException problème de connection
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }



    /**
     * cloture des ressources après modification de la base
     * @param statement Instance de Statement ou Preparedstatement à clôturer
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * cloture des ressources après consultation de la base de données
     * @param resultSet Instance de ResoultSet à clôturer
     * @param statement Instance de Statement à clôturer
     */
    public static void close(ResultSet resultSet, Statement statement) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Cloture de la connection après l'utilisation
     * @param connection connection to BD
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
