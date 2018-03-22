package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.ClientsDao;
import dao.DaoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class TotsPacientsController implements Initializable{

	@FXML private ListView<String> colPacients;
	ClientsDao clientDao = DaoManager.getClientsDao();

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

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

	}


}
