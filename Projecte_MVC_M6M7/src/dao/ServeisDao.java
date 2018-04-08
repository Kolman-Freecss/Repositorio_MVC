package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import pojos.Serveis;

public interface ServeisDao {

	//CRUD BASICS
	public void addServei(Serveis servei) throws HibernateException;
	public Serveis getServei(int codi) throws HibernateException;
	public void updateServei(Serveis servei) throws HibernateException;
	public void deleteServei(int codi) throws HibernateException, ConstraintViolationException;
	public List<Serveis> getServeis() throws HibernateException;

}
