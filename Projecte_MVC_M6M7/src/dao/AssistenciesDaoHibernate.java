package dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pojos.Assistencies;

public class AssistenciesDaoHibernate implements AssistenciesDao{

	private static SessionFactory factory;

	public AssistenciesDaoHibernate() {
		super();
		factory = SessionFactoryUtil.getSessionFactory();
	}

	@Override
	public void addAssistencia(Assistencies assistencia) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.save(assistencia);
			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

	}

	@Override
	public Assistencies getAssistencia(Integer idAssistencia) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;
		Assistencies assistencia = new Assistencies();

		try {
			tx = session.beginTransaction();

			assistencia = session.get(Assistencies.class, idAssistencia);

			tx.commit();

		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

		return assistencia;

	}

	@Override
	public void updateAssistencia(Assistencies assistencia) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();

			Assistencies assistenciaInstancia = session.get(Assistencies.class, assistencia.getCodiAssistencia());
			assistenciaInstancia.setServeis(assistencia.getServeis());
			assistenciaInstancia.setUsuaris(assistencia.getUsuaris());
			assistenciaInstancia.setClients(assistencia.getClients());
			assistenciaInstancia.setData(assistencia.getData());
			assistenciaInstancia.setObservacions(assistencia.getObservacions());
			session.update(assistenciaInstancia);

			tx.commit();


		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

	}

	@Override
	public void deleteAssistencia(Integer idAssistencia) throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();

			Assistencies assistencia = session.get(Assistencies.class, idAssistencia);
			session.delete(assistencia);

			tx.commit();


		} catch (HibernateException e) {
			if(tx!=null) tx.rollback();
			throw e;
		}finally {
			session.close();
		}

	}

	@Override
	public List<Assistencies> getAssistencies() throws HibernateException {

		Session session = factory.openSession();
		Transaction tx = null;
		List<Assistencies> list = null;

		try {

			tx = session.beginTransaction();

			list = session.createQuery("FROM Assistencies").list();

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
