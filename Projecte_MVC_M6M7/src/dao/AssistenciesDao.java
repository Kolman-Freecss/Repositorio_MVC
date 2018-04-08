package dao;

import java.util.List;

import org.hibernate.HibernateException;

import pojos.Assistencies;

public interface AssistenciesDao {

	//CRUD BASICS
	public void addAssistencia(Assistencies assistencia) throws HibernateException;
	public Assistencies getAssistencia(Integer idAssistencia) throws HibernateException;
	public void updateAssistencia(Assistencies assistencia) throws HibernateException;
	public void deleteAssistencia(Integer idAssistencia) throws HibernateException;
	public List<Assistencies> getAssistencies() throws HibernateException;

}
