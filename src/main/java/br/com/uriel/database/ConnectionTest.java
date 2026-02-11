package br.com.uriel.database;

public class ConnectionTest {
    public static void main (String[] args){
        try {
            ConnectionFactory.getConnection();
            System.out.println("Banco conectado com sucesso!");
        } catch (Exception e){
            System.out.println("Banco não conectado, conexão não estabelecida com sucesso!");
            e.printStackTrace();
        }
    }
}
