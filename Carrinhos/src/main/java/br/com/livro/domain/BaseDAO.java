package br.com.livro.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {

	protected Connection getConnection() throws SQLException {
		// URL de conexão com o banco de dados
		String url = "jdbc:mysql://localhost/livro";

		// Conecta utilizando a URL, usuario e senha.
		Connection conn = DriverManager.getConnection(url, "livro", "livro123");
		return conn;
	}
	
	public static void main(String[] args) throws SQLException {
		BaseDAO db = new BaseDAO();
		//testa a conexao
		Connection conn = db.getConnection();
		System.out.println(conn);
	}
}