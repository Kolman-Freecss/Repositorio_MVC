package dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import pojos.Assistencies;

public interface AssistenciesDao {

	//CRUD BASICS
	public void addAssistencia(Assistencies assistencia) throws SQLException, HibernateException;
	public Assistencies getAssistencia(Integer idAssistencia) throws SQLException, HibernateException;
	public void updateAssistencia(Assistencies assistencia) throws SQLException, HibernateException;
	public void deleteAssistencia(Integer idAssistencia) throws SQLException, HibernateException;
	public List<Assistencies> getAssistencies() throws SQLException, HibernateException;

}
