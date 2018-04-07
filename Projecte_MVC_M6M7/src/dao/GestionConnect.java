package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GestionConnect {

	private static String url = "jdbc:mysql://192.168.1.109/bdclinica";
	private static String user = "root";
	private static String pwd = "root";
	private static Connection connexio = null;

	private static int connectar(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connexio = DriverManager.getConnection(url,user,pwd);
			return 0;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static Connection obtenirConnexio(){
		try {
			if (connexio == null || connexio.isClosed()){
				connectar();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connexio;
	}

	public static void tancarConnexio(){
		try {
			connexio.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}




}


