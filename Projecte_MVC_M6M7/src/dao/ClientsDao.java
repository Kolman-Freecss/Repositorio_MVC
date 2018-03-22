package dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import pojos.Clients;

public interface ClientsDao {

	//CRUD BASICS
	public void addClient(Clients client) throws SQLException, HibernateException;
	public Clients getClient(int idClient) throws SQLException, HibernateException;
	public void updateClient(Clients client) throws SQLException, HibernateException;
	public void deleteClient(int idClient) throws SQLException, HibernateException;
	public List<Clients> getClients() throws SQLException, HibernateException;

	//Especifics
	public List<String> getNomClients() throws SQLException, HibernateException;

}
