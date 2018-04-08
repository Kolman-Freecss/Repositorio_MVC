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
import resources.ControlErrores;

public class SubfinestraAfegirServeiController implements Initializable{

	@FXML private Button etAfegir;
	@FXML private Button etVolver;
	@FXML private TextField et1;
	@FXML private TextField et2;

	private static String funcionalitat;

	private int code;
	private String nomServei;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		if("modificar".equals(funcionalitat)){
			et1.disabledProperty();
		}

	}


	@FXML
	public void clickAfegir(ActionEvent event){

		try {
			ServeisController.setConfirmacio(true);

			code = Integer.parseInt(et1.getText());
			nomServei = et2.getText();

			Node source = (Node) event.getSource();
			Stage stage2 = (Stage) source.getScene().getWindow();
			stage2.close();

		} catch (NumberFormatException e) {
			ControlErrores.mostrarWarning("Error en el format de camp", "Es necesita numeros en el camp code");
		}


	}

	@FXML
	public void clickVolver(ActionEvent event){

		ServeisController.setConfirmacio(false);

		Node source = (Node) event.getSource();
		Stage stage2 = (Stage) source.getScene().getWindow();
		stage2.close();

	}


	public int getCode() {
		return code;
	}


	public String getNomServei() {
		return nomServei;
	}

	public void setFuncionalitatS(String funcionalitatP) {
		funcionalitat = funcionalitatP;

		/**
		 * Posem la data
		 */
		if("modificar".equals(funcionalitat)){
			et1.setEditable(false);
			et1.setText(String.valueOf(ServeisController.getServeiAModificar().getCodi()));
			et2.setText(ServeisController.getServeiAModificar().getDescripcio());
		}
	}





}


















