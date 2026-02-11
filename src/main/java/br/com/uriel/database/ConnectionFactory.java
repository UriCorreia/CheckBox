package br.com.uriel.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_tarefas";
    private static final String usuario = "postgres";
    private static final String senha = "uri24";

    // Método de conexão com o banco de dados
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, usuario, senha);
    }
}
