package dao;

import java.sql.SQLException;
import java.util.List;

import pojos.Perfils;
import pojos.Usuaris;

public interface UsuarisDao {

	//CRUD BASICS
	public void addUsuari(Usuaris usuari) throws SQLException;
	public Usuaris getUsuari(String userid) throws SQLException;
	public void updateUsuari(Usuaris usuari) throws SQLException;
	public void deleteUsuari(Usuaris usuari) throws SQLException;
	public List<Usuaris> getUsuaris() throws SQLException;

	//Metodes Perfils
	public Perfils getPerfil(int codi) throws SQLException;
	public List<String> getNomUsuaris() throws SQLException;

}
