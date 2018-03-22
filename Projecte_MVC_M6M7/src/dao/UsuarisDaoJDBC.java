package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import dao.GestionConnect;
import pojos.Perfils;
import pojos.Usuaris;

public class UsuarisDaoJDBC implements UsuarisDao{

	private Connection c;

	public UsuarisDaoJDBC() {
		c = GestionConnect.obtenirConnexio();
	}

	@Override
	public void addUsuari(Usuaris usuari) throws SQLException{
		c = GestionConnect.obtenirConnexio();

		String sql = "INSERT INTO usuaris(idUsuari, perfil, password, nom, cognoms, correu, numcolegiat, especialitat) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		PreparedStatement sentenciaPrep = c.prepareStatement(sql);

		sentenciaPrep.setString(1, usuari.getIdUsuari());
		sentenciaPrep.setInt(2, usuari.getPerfils().getCodi());
		sentenciaPrep.setString(3, usuari.getPassword());
		sentenciaPrep.setString(4, usuari.getNom());
		sentenciaPrep.setString(5, usuari.getCognoms());
		sentenciaPrep.setString(6, usuari.getCorreu());
		sentenciaPrep.setInt(7, usuari.getNumcolegiat());
		sentenciaPrep.setString(8, usuari.getEspecialitat());

		sentenciaPrep.executeUpdate();

	}

	@Override
	public Usuaris getUsuari(String userid) throws SQLException{
		c = GestionConnect.obtenirConnexio();
		Usuaris usuari = null;

		String sql = "SELECT * FROM usuaris WHERE idUsuari = ?";
		PreparedStatement sentenciaPrep = c.prepareStatement(sql);

		sentenciaPrep.setString(1, userid);
		ResultSet resultat = sentenciaPrep.executeQuery();

		while(resultat.next()){
			usuari = new Usuaris(resultat.getString(1), resultat.getString(2), resultat.getString(3),
					resultat.getString(4), resultat.getString(5), this.getPerfil(resultat.getInt(6)), resultat.getInt(7), resultat.getString(8));
		}

		return usuari;
	}

	/**
	 * Modificació d'usuari sense cambiar el Id
	 */
	@Override
	public void updateUsuari(Usuaris usuari) throws SQLException{
		c = GestionConnect.obtenirConnexio();

		String sql = "UPDATE usuaris SET perfils=?, password=?, nom=?, cognoms=?, correu=?, numcolegiat=?, especialitat=? WHERE idUsuari = ?";
		PreparedStatement sentenciaPrep = c.prepareStatement(sql);

		sentenciaPrep.setInt(1, usuari.getPerfils().getCodi());
		sentenciaPrep.setString(2, usuari.getPassword());
		sentenciaPrep.setString(3, usuari.getNom());
		sentenciaPrep.setString(4, usuari.getCognoms());
		sentenciaPrep.setString(5, usuari.getCorreu());
		sentenciaPrep.setInt(6, usuari.getNumcolegiat());
		sentenciaPrep.setString(7, usuari.getEspecialitat());
		sentenciaPrep.setString(8, usuari.getIdUsuari());

		sentenciaPrep.executeUpdate();

	}

	@Override
	public void deleteUsuari(Usuaris usuari) throws SQLException{
		c = GestionConnect.obtenirConnexio();

		String sql = "DELETE FROM usuaris WHERE idUsuari = ?";
		PreparedStatement sentenciaPrep = c.prepareStatement(sql);

		sentenciaPrep.setString(1, usuari.getIdUsuari());

		sentenciaPrep.executeUpdate();


	}

	@Override
	public List<Usuaris> getUsuaris() throws SQLException{
		c = GestionConnect.obtenirConnexio();

		Vector<Usuaris> vectorReturn = new Vector<Usuaris>();

		Statement sentencia = c.createStatement();

		ResultSet resultat = sentencia.executeQuery("SELECT * "
				+ "FROM usuaris");

		while(resultat.next()){
			vectorReturn.add(this.getUsuari(resultat.getString(1)));
		}

		return vectorReturn;
	}


	public Perfils getPerfil(int codi) throws SQLException{

		c = GestionConnect.obtenirConnexio();
		Perfils perfil = null;

		String sql = "SELECT * FROM perfils WHERE codi = ?";
		PreparedStatement sentenciaPrep = c.prepareStatement(sql);

		sentenciaPrep.setInt(1, codi);
		ResultSet resultat = sentenciaPrep.executeQuery();


		while(resultat.next()){
			perfil = new Perfils(resultat.getInt(1), resultat.getString(2));
		}

		return perfil;

	}

	/**
	 * Retorna el nom de tots els Doctors
	 */
	@Override
	public List<String> getNomUsuaris() throws SQLException{
		c = GestionConnect.obtenirConnexio();

		Vector<String> vectorNomsReturn = new Vector<String>();

		Statement sentencia = c.createStatement();

		ResultSet resultat = sentencia.executeQuery("SELECT nom "
				+ "FROM usuaris");

		while(resultat.next()){
			vectorNomsReturn.add(resultat.getString(1));
		}

		return vectorNomsReturn;
	}

}








