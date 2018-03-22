package dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import pojos.Serveis;

public interface ServeisDao {

	//CRUD BASICS
	public void addServei(Serveis servei) throws SQLException, HibernateException;
	public Serveis getServei(int codi) throws SQLException, HibernateException;
	public void updateServei(Serveis servei) throws SQLException, HibernateException;
	public void deleteServei(int codi) throws SQLException, HibernateException;
	public List<Serveis> getServeis() throws SQLException, HibernateException;

}
