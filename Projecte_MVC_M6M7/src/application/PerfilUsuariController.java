package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import dao.DaoManager;
import dao.UsuarisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Perfils;
import pojos.Usuaris;

public class PerfilUsuariController implements Initializable{

	@FXML private TextField etUsuari;
	@FXML private TextField etPerfil;
	@FXML private TextField etEspecialitat;
	@FXML private TextField etPassword;
	@FXML private TextField etNom;
	@FXML private TextField etCognoms;
	@FXML private TextField etCorreu;
	@FXML private TextField etNumColegiat;

	private UsuarisDao usuDao = DaoManager.getUsuarisDao();
	private String usuariNom;
	private Usuaris usuari;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		usuariNom = LoginController.getUsuariDoctor();
		try {
			usuari = usuDao.getUsuari(usuariNom);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		etUsuari.setText(usuari.getIdUsuari());
		etPerfil.setText(usuari.getPerfils().getDescripcio());
		etEspecialitat.setText(usuari.getEspecialitat());
		etPassword.setText(usuari.getPassword());
		etNom.setText(usuari.getNom());
		etCognoms.setText(usuari.getCognoms());
		etCorreu.setText(usuari.getCorreu());
		etNumColegiat.setText(String.valueOf(usuari.getNumcolegiat()));


	}

}
