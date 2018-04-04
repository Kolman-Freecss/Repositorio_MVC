package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.ClientsDao;
import dao.DaoManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pojos.Clients;
import pojos.Usuaris;

public class TotsPacientsController implements Initializable{

	@FXML private ListView<String> colPacients;

	@FXML private TextField txtNom;
	@FXML private TextField txtCognoms;
	@FXML private TextField txtTelefon;
	@FXML private TextField txtCorreu;

	ClientsDao clientDao = DaoManager.getClientsDao();
	private static List<Clients> llistaPacients = new LinkedList<Clients>();

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		if(!LoginController.getTipusPerfil().equals("ADMINISTRADOR") && !LoginController.getTipusPerfil().equals("GESTIÓ")){
			txtNom.disabledProperty();
			txtCognoms.disabledProperty();
			txtTelefon.disabledProperty();
			txtCorreu.disabledProperty();
		}

		try {
			llistaPacients.addAll(clientDao.getClients());
		} catch (HibernateException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(this.colPacients != null){

			ObservableList<String> items;
			try {
				items = FXCollections.observableArrayList(clientDao.getNomClients());
				colPacients.setItems(items);
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		this.colPacients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        // Your action here
		    	final int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
		    	Clients pacient = llistaPacients.get(selectedIdx);

		    	txtNom.setText(pacient.getNom());
		    	txtCognoms.setText(pacient.getCognoms());
		    	txtTelefon.setText(pacient.getTelefon());
		    	txtCorreu.setText(pacient.getCorreu());

		    }


		});

	}


}
