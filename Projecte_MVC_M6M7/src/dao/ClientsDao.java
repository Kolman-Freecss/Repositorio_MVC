package dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import pojos.Clients;

public interface ClientsDao {

	//CRUD BASICS
	public void addClient(Clients client) throws HibernateException;
	public Clients getClient(int idClient) throws HibernateException;
	public void updateClient(Clients client) throws HibernateException;
	public void deleteClient(int idClient) throws HibernateException;
	public List<Clients> getClients() throws HibernateException;

	//Especifics
	public List<String> getNomClients() throws HibernateException;

}
