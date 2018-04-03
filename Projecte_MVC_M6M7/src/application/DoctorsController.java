package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import antlr.collections.impl.LList;
import dao.AssistenciesDao;
import dao.ClientsDao;
import dao.DaoManager;
import dao.UsuarisDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import pojos.Assistencies;
import pojos.Clients;
import pojos.Usuaris;

public class DoctorsController implements Initializable{

	@FXML private ListView<String> colDoctors;
	@FXML private ListView<String> colPacients;
	ClientsDao clientDao = DaoManager.getClientsDao();
	UsuarisDao usuariDao = DaoManager.getUsuarisDao();

	AssistenciesDao assistenciesDao = DaoManager.getAssistenciesDao();

	//llista amb els usuaris OK ESTO NOS SERVIRA PARA EL REMOVE, YA QUE AQUI NO SIRVE PORQUE EL NOMBRE DEL USUARIO(MEDICO) ES PRIMARY KEY
	//private List<Usuaris> listUsuarisAuxiliar;
	private ObservableList<String> items;
	private ObservableList<String> itemsPacients;

	private static List<Usuaris> llistaDoctors = new LinkedList<Usuaris>();



	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		try {
			llistaDoctors.addAll(usuariDao.getUsuaris());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(this.colDoctors != null){


			try {
				//listUsuarisAuxiliar = usuariDao.getUsuaris();
				items = FXCollections.observableArrayList(usuariDao.getNomUsuaris());//usuariDao.getNomUsuaris()
				colDoctors.setItems(items);
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if(this.colPacients != null){

			//ObservableList<String> items;
			try {
				//Cojes totes les assistencies..
				List<Assistencies> llistatAssistencies = this.assistenciesDao.getAssistencies();


				colDoctors.getSelectionModel().select(0);

				chargePacientXDoctor(llistatAssistencies);
				//Agafem el index del doctor del listview que sera igual al index on es troba el usuari en la llista auxiliar que hem fet
				/*final int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();

				Usuaris doctor = this.llistaDoctors.get(selectedIdx);
				for (Assistencies a : llistatAssistencies) {
					if(a.getUsuaris().getIdUsuari().equals(doctor.getIdUsuari())){
						//el llistat d'assistencies solo tiene el id por algun motivo
						//por eso tengo que obtener el cliente apartir del id
						Clients client = clientDao.getClient(a.getClients().getIdClient());
						llistaPacients.add(client.getNom());

					}

				}*/


		        //if (selectedIdx != -1) {


				/**
				 * Al escollir un altre doctor carga automaticament el ListView dels pacients als que ha fet una assistencia
				 */
				colDoctors.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				    @Override
				    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				        // Your action here

				    	chargePacientXDoctor(llistatAssistencies);
				    }


				});

		          String itemToRemove = colPacients.getSelectionModel().getSelectedItem();

		          /*final int newSelectedIdx =
		            (selectedIdx == colPacients.getItems().size() - 1) ? selectedIdx - 1 : selectedIdx;

		          //serveisDao.deleteServei(codi);
		          colPacients.getItems().remove(selectedIdx);
		          System.out.println(itemToRemove);
		          colPacients.getSelectionModel().select(newSelectedIdx);
		        }*/




			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	private void chargePacientXDoctor(List<Assistencies> llistatAssistencies) {
		final int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();
    	List<String> llistaPacients = new LinkedList<String>();
    	//List<Usuaris> llistaDoctors = new LinkedList<Usuaris>();
		Usuaris doctor = llistaDoctors.get(selectedIdx);
		for (Assistencies a : llistatAssistencies) {
			if(a.getUsuaris().getIdUsuari().equals(doctor.getIdUsuari())){
				//el llistat d'assistencies solo tiene el id por algun motivo
				//por eso tengo que obtener el cliente apartir del id
				Clients client;
				try {
					client = clientDao.getClient(a.getClients().getIdClient());
					llistaPacients.add(client.getNom());

					itemsPacients = FXCollections.observableArrayList(llistaPacients);
					colPacients.setItems(itemsPacients);
				} catch (HibernateException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


}
