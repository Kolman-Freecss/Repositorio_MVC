package dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import pojos.Serveis;

public interface ServeisDao {

	//CRUD BASICS
	public void addServei(Serveis servei) throws HibernateException;
	public Serveis getServei(int codi) throws HibernateException;
	public void updateServei(Serveis servei) throws HibernateException;
	public void deleteServei(int codi) throws HibernateException;
	public List<Serveis> getServeis() throws HibernateException;

}
