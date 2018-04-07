package application;

import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.control.TextField;
import pojos.Assistencies;
import pojos.Clients;
import resources.ControlErrores;

public class PacientsXDoctorController implements Initializable{

	@FXML private ListView<String> colPacients;

	@FXML private TextField txtNom;
	@FXML private TextField txtCognoms;
	@FXML private TextField txtTelefon;
	@FXML private TextField txtCorreu;

	ClientsDao clientDao = DaoManager.getClientsDao();
	AssistenciesDao assistenciesDao = DaoManager.getAssistenciesDao();
	private List<Clients> temporaryLlistaPacients = new LinkedList<Clients>();
	private List<Assistencies> llistatAssistencies = new LinkedList<Assistencies>();
	private String usuariDoctor;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		List<String> llistaPacients = new LinkedList<String>();

		/**
		 * Agafem el doctor que esta logat en aquest moment
		 */
		usuariDoctor = LoginController.getUsuariDoctor();

		if(!LoginController.getTipusPerfil().equals("ADMINISTRADOR") && !LoginController.getTipusPerfil().equals("GESTIÓ")){
			txtNom.disabledProperty();
			txtCognoms.disabledProperty();
			txtTelefon.disabledProperty();
			txtCorreu.disabledProperty();
		}

		try {
			temporaryLlistaPacients.addAll(clientDao.getClients());
			llistatAssistencies = this.assistenciesDao.getAssistencies();

		} catch (HibernateException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		for (Assistencies a : llistatAssistencies) {
			if(a.getUsuaris().getIdUsuari().equals(usuariDoctor)){
				//el llistat d'assistencies solo tiene el id por algun motivo
				//por eso tengo que obtener el cliente apartir del id
				Clients client;
				try {
					client = clientDao.getClient(a.getClients().getIdClient());
					llistaPacients.add(client.getNom());
				} catch (HibernateException e) {
					ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
				}
			}
		}

		if(this.colPacients != null){

			ObservableList<String> items;
			try {
				items = FXCollections.observableArrayList(llistaPacients);
				colPacients.setItems(items);
			} catch (HibernateException e) {
				e.printStackTrace();
			}

		}

	}


}
