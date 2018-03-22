package dao;


public class DaoManager {

	private static UsuarisDao usuDao;
	private static ServeisDao serveisDao;
	private static ClientsDao clientsDao;
	private static AssistenciesDao assistenciesDao;


	public static UsuarisDao getUsuarisDao(){

		if(usuDao == null){
			usuDao = new UsuarisDaoJDBC();
		}

		return usuDao;
	}

	public static ServeisDao getServeisDao(){

		if(serveisDao == null){
			serveisDao = new ServeisDaoHibernate();
		}

		return serveisDao;
	}

	public static ClientsDao getClientsDao(){

		if(clientsDao == null){
			clientsDao = new ClientsDaoHibernate();
		}

		return clientsDao;
	}

	public static AssistenciesDao getAssistenciesDao(){

		if(assistenciesDao == null){
			assistenciesDao = new AssistenciesDaoHibernate();
		}

		return assistenciesDao;
	}

}
