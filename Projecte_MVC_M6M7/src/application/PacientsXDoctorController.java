package application;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.AssistenciesDao;
import dao.ClientsDao;
import dao.DaoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import pojos.Assistencies;
import pojos.Clients;
import resources.ControlErrores;

public class PacientsXDoctorController implements Initializable{

	@FXML private ListView<String> colPacients;

	private ClientsDao clientDao = DaoManager.getClientsDao();
	private AssistenciesDao assistenciesDao = DaoManager.getAssistenciesDao();
	private List<Clients> temporaryLlistaPacients = new LinkedList<Clients>();
	private List<Assistencies> llistatAssistencies = new LinkedList<Assistencies>();
	private String usuariDoctor;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		try {
			List<String> llistaPacientsNom = new LinkedList<String>();
			List<Integer> llistaPacients = new LinkedList<Integer>();

			temporaryLlistaPacients.addAll(clientDao.getClients());
			llistatAssistencies = this.assistenciesDao.getAssistencies();

			/**
			 * Agafem el usuari Logat
			 */

			this.usuariDoctor = LoginController.getUsuariDoctor();


			/**
			 * Omplim la llista de pacients guardant el id del usuari perque no es repeteixi
			 */
			for (Assistencies a : llistatAssistencies) {
				if(a.getUsuaris().getIdUsuari().equals(usuariDoctor)){
					Clients client;
					client = clientDao.getClient(a.getClients().getIdClient());
					if(!llistaPacients.contains(client.getIdClient())){
						llistaPacients.add(client.getIdClient());
						llistaPacientsNom.add(client.getNom());
					}
				}
			}

			/**
			 * Omplim la columna amb les dades anteriors
			 */
			if(this.colPacients != null){

				ObservableList<String> items;
				items = FXCollections.observableArrayList(llistaPacientsNom);
				colPacients.setItems(items);

			}

		} catch (HibernateException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}


}
