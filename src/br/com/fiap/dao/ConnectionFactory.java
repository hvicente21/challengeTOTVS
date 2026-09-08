package br.com.fiap.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection abrirConexao(){
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
            final String usuario = "rm564116";
            final String senha = "211106";
            con = DriverManager.getConnection(url,usuario,senha);
            System.out.println("Conexão Aberta");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro: A classe de conexão não foi encontrada " + e.getMessage());
        }catch (SQLException e) {
            System.out.println("Erro de SQL " + e.getMessage());
        }
        return con;
    }
    public static void fecharConexao(Connection con){
        if (con == null) {
            return;
        }
        try {
            con.close();
            System.out.println("Conexao fechada");
        } catch (SQLException e) {
            System.out.println("Erro de SQL " + e.getMessage());
        }
    }
}
