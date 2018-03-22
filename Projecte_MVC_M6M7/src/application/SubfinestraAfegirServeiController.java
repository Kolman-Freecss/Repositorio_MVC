package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.DaoManager;
import dao.ServeisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pojos.Serveis;

public class SubfinestraAfegirServeiController implements Initializable{

	@FXML private Button etAfegir;
	@FXML private Button etVolver;
	@FXML private TextField et1;
	@FXML private TextField et2;


	private String code;
	private String nomServei;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {



	}


	@FXML
	public void clickAfegir(ActionEvent event){

		code = et1.getText();
		nomServei = et2.getText();

		Node source = (Node) event.getSource();
		Stage stage2 = (Stage) source.getScene().getWindow();
		stage2.close();


	}

	@FXML
	public void clickVolver(ActionEvent event){



	}


	public String getCode() {
		return code;
	}


	public String getNomServei() {
		return nomServei;
	}



}


















