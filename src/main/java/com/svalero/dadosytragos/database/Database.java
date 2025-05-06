package com.svalero.dadosytragos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    // Conectar bd
    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        connection = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/dados_y_tragos?useUnicode=true&characterEncoding=UTF-8", // local
               //   "jdbc:mariadb://localhost:3307/dados_y_tragos?useUnicode=true&characterEncoding=UTF-8", // docker
                "root",  // usuario
                "nerea"   // contraseña
        );
        //System.out.println("Conexion establecida con exito");
    }

    // Cerrar conexion
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            //System.out.println("Conexion cerrada");
        }
    }

    // Getter de la conexion
    public Connection getConnection() {
        return connection;
    }
}
