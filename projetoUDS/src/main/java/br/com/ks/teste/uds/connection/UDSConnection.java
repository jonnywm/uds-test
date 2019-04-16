package br.com.ks.teste.uds.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonny
 */
public class UDSConnection {

    private static Connection connection = null;
    private static final UDSConnection SELF = null;
    private static final String URL_CONNECTION = "jdbc:postgresql://localhost/projetouds";
    private static final Properties PROPS = new Properties();

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL_CONNECTION, PROPS);
            } catch (SQLException ex) {
                Logger.getLogger(UDSConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }

    public static synchronized UDSConnection getInstance() {
        if (SELF == null) {
            return new UDSConnection();
        } else {
            return SELF;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (SELF != null) {
            disconnect();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.getLogger(UDSConnection.class.getName()).log(Level.SEVERE, "Não foi possível desconectar do banco de dados", e);
        }
    }

    private UDSConnection() {
        PROPS.setProperty("user", "postgres");
        PROPS.setProperty("password", "postgres");
    }
}
