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

public class SubfinestraAfegirPacientController implements Initializable{

	@FXML private Button etAfegir;
	@FXML private Button etVolver;
	@FXML private TextField tfNom;
	@FXML private TextField tfCognoms;
	@FXML private TextField tfTelefon;
	@FXML private TextField tfCorreu;

	private String nom;
	private String cognoms;
	private String telefon;
	private String correu;

	private static String funcionalitat;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

	}


	@FXML
	public void clickAfegir(ActionEvent event){

		nom = tfNom.getText();
		cognoms = tfCognoms.getText();
		telefon = tfTelefon.getText();
		correu = tfCorreu.getText();

		Node source = (Node) event.getSource();
		Stage stage2 = (Stage) source.getScene().getWindow();
		stage2.close();


	}

	@FXML
	public void clickVolver(ActionEvent event){



	}


	public String getNom() {
		return nom;
	}


	public String getCognoms() {
		return cognoms;
	}


	public String getTelefon() {
		return telefon;
	}


	public String getCorreu() {
		return correu;
	}

	public void setFuncionalitatS(String funcionalitatP) {
		funcionalitat = funcionalitatP;

		if("modificar".equals(funcionalitat)){
			this.nom = TotsPacientsController.getPacientAModificar().getNom();
			this.cognoms = TotsPacientsController.getPacientAModificar().getCognoms();
			this.telefon = TotsPacientsController.getPacientAModificar().getTelefon();
			this.correu = TotsPacientsController.getPacientAModificar().getCorreu();
			tfNom.setText(TotsPacientsController.getPacientAModificar().getNom());
			tfCognoms.setText(TotsPacientsController.getPacientAModificar().getCognoms());
			tfTelefon.setText(TotsPacientsController.getPacientAModificar().getTelefon());
			tfCorreu.setText(TotsPacientsController.getPacientAModificar().getCorreu());
		}
	}



}


















