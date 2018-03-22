package resources;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import dao.AssistenciesDao;
import dao.ClientsDao;
import dao.DaoManager;
import dao.ServeisDao;
import dao.UsuarisDao;
import pojos.Assistencies;
import pojos.Clients;
import pojos.Perfils;
import pojos.Serveis;
import pojos.Usuaris;

public class TestMain {

	public static void main(String[] args) throws ParseException, SQLException {

		ServeisDao serveiDao = DaoManager.getServeisDao();
		UsuarisDao usuariDao = DaoManager.getUsuarisDao();
		AssistenciesDao assistenciesDao = DaoManager.getAssistenciesDao();
		ClientsDao clientDao = DaoManager.getClientsDao();


		Perfils perfilParametre = new Perfils(22, "Per pasar com a parametre");
		Usuaris usuariAdd = new Usuaris("23412", perfilParametre, "password", "Sergio", "Martinez",
					"correu@gmail.com", 17, "Programador");
		Clients clientAdd = new Clients("SergioC", "cognom", "5547582");
		Serveis serveiAdd = new Serveis(22, "Descripcio");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Assistencies assistenciaAdd = new Assistencies(serveiAdd, usuariAdd, clientDao.getClient(22), sdf.parse("20/05/2005"));

		//Testing method ADD addUsuari
		/*try {
			usuariDao.addUsuari(usuariAdd);
			System.out.println("Correcto");
		} catch (SQLException e) {
			e.printStackTrace();
		}*/



		//Testing method ADD addAsistencies con autoincrement
		/*try {

			assistenciesDao.addAssistencia(assistenciaAdd);
			System.out.println("chivato");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/



		//Testing method GET getServei
		/*try {
			Serveis servei = serveiDao.getServei(22);
			System.out.println(servei.getCodi());
			System.out.println(servei.getDescripcio());

		} catch (SQLException e) {
			e.printStackTrace();
		}*/



		//Testing method DELETE deleteClient
		//clientDao.deleteClient(22);



		//Testing method ADD addClient con autoincrement
		/*try {

			clientDao.addClient(clientAdd);

		} catch (SQLException e) {
			e.printStackTrace();
		}*/




		//Testing method GETALL getAssistencies que retorna una llista
		/*
		try {

			List<Assistencies> listaAssistencies = assistenciesDao.getAssistencies();
			Iterator it = listaAssistencies.iterator();
			while(it.hasNext()){
				System.out.println(it.next().toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}*/


		//Testing UPDATE method serveis
		/*try {
			serveiDao.updateServei(new Serveis(22, "provaUpda"));
		} catch (SQLException e) {
			e.printStackTrace();
		}*/


		//Testing UPDATE method clients con autoincrement al qual he afegit un constructor per pasar-li el id
		Clients clientUpdate = new Clients(23,"SergioC", "cognom", "5547582", "sergio.com");
		/*try {
			clientDao.updateClient(clientUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}*/

	}

}
