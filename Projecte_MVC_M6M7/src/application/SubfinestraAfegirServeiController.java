package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SubfinestraAfegirServeiController implements Initializable{

	@FXML private Button etAfegir;
	@FXML private Button etVolver;
	@FXML private TextField et1;
	@FXML private TextField et2;

	private static String funcionalitat;

	private String code;
	private String nomServei;

	private static ServeisController controladorServeis;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		if("modificar".equals(funcionalitat)){
			et1.disabledProperty();
		}

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

	public void setFuncionalitatS(String funcionalitatP) {
		funcionalitat = funcionalitatP;

		if("modificar".equals(funcionalitat)){
			et1.setText(String.valueOf(ServeisController.getServeiAModificar().getCodi()));
			et2.setText(ServeisController.getServeiAModificar().getDescripcio());
		}
	}





}


















