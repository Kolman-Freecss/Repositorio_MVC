package dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pojos.Clients;

public class ClientsDaoHibernate implements ClientsDao{

	private static SessionFactory factory;

	public ClientsDaoHibernate() {
		super();
		factory = SessionFactoryUtil.getSessionFactory();
	}

	@Override
	public void addClient(Clients client) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			session.save(client);
			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

	}

	@Override
	public Clients getClient(int idClient) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;
		Clients client = new Clients();

		try {
			tx = session.beginTransaction();

			client = session.get(Clients.class, idClient);

			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

		return client;
	}

	@Override
	public void updateClient(Clients client) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();

			Clients clientInstancia = session.get(Clients.class, client.getIdClient());
			clientInstancia.setNom(client.getNom());
			clientInstancia.setCognoms(client.getCognoms());
			clientInstancia.setTelefon(client.getTelefon());
			clientInstancia.setCorreu(client.getCorreu());
			session.update(clientInstancia);

			tx.commit();


		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

	}

	@Override
	public void deleteClient(int idClient) throws HibernateException {
		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();

			Clients client = session.get(Clients.class, idClient);
			session.delete(client);

			tx.commit();


		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Clients> getClients() throws HibernateException {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Clients> list = null;

		try {

			tx = session.beginTransaction();
			list = session.createQuery("FROM Clients").list();
			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}
		return list;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNomClients() throws HibernateException {
		Session session = factory.openSession();
		Transaction tx = null;
		List<String> listNoms = new LinkedList<String>();

		try {

			tx = session.beginTransaction();
			listNoms = session.createQuery("SELECT nom FROM Clients").list();
			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}
		return listNoms;

	}

}
