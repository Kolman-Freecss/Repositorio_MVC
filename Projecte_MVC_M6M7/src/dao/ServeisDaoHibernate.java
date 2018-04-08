package dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import pojos.Serveis;

public class ServeisDaoHibernate implements ServeisDao{

	private static SessionFactory factory;



	public ServeisDaoHibernate() {
		super();
		factory = SessionFactoryUtil.getSessionFactory();
	}

	@Override
	public void addServei(Serveis servei) throws HibernateException {
		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(servei);
			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}
	}

	@Override
	public Serveis getServei(int codi) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;
		Serveis servei = new Serveis();

		try {
			tx = session.beginTransaction();

			servei = session.get(Serveis.class, codi);

			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

		return servei;

	}

	@Override
	public void updateServei(Serveis servei) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();

			Serveis serveiInstancia = session.get(Serveis.class, servei.getCodi());

			serveiInstancia.setDescripcio(servei.getDescripcio());

			session.update(serveiInstancia);

			tx.commit();


		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}
	}

	@Override
	public void deleteServei(int codi) throws HibernateException, ConstraintViolationException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();

			Serveis servei = session.get(Serveis.class, codi);
			session.delete(servei);

			tx.commit();


		} catch (ConstraintViolationException a){
			throw a;
		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Serveis> getServeis() throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;
		List<Serveis> list = null;

		try {

			tx = session.beginTransaction();
			list = session.createQuery("FROM Serveis").list();
			tx.commit();


		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}
		return list;
	}

}
