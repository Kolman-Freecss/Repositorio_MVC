package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.ClientsDao;
import dao.DaoManager;
import dao.UsuarisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class DoctorsController implements Initializable{

	@FXML private ListView<String> colDoctors;
	@FXML private ListView<String> colPacients;
	ClientsDao clientDao = DaoManager.getClientsDao();
	UsuarisDao usuariDao = DaoManager.getUsuarisDao();


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		if(this.colDoctors != null){

			ObservableList<String> items;
			try {
				items = FXCollections.observableArrayList(usuariDao.getNomUsuaris());
				colDoctors.setItems(items);
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}


}
