package application;

import java.net.URL;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class RegisterController implements Initializable{

	@FXML private TextField et1;
	@FXML private TextField et2;

	@FXML private Button btConfirmar;
	@FXML private Button btVolver;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void clickConfirmar(ActionEvent event){
		try {
			//BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaDoctors.fxml"));
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Look, a Confirmation Dialog");
			alert.setContentText("Are you ok with this?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){


				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaAppMenu.fxml"));

				BorderPane root = (BorderPane) fxmlLoader.load();

				MenuController controllerGeneral = fxmlLoader.getController();

				//Creamos una nueva pantalla
				Stage stage = new Stage();
				//Le metemos el BorderPane que viene a ser el menu en la pantalla
				stage.setScene(new Scene(root));

				controllerGeneral.obrirDoctors();

				stage.show();

				((Node)(event.getSource())).getScene().getWindow().hide();

			} else {
			    // ... user chose CANCEL or closed the dialog
				alert.close();
			}


			//this.carregarVista(vista);
		} catch (Exception e) {
			e.printStackTrace();
			//MenuController.mostrarError("No s'ha pogut mostrar la gestió d'Unitats Formatives", e.getMessage());
		}
	}

	@FXML
	public void clickVolver(ActionEvent event){

		try {
			//BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaDoctors.fxml"));

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaLogin.fxml"));
			Parent root1 = (Parent)fxmlLoader.load();

			Stage stage = new Stage();

			stage.setScene(new Scene(root1));

			stage.show();

			((Node)(event.getSource())).getScene().getWindow().hide();

			//this.carregarVista(vista);
		} catch (Exception e) {
			e.printStackTrace();
			//MenuController.mostrarError("No s'ha pogut mostrar la gestió d'Unitats Formatives", e.getMessage());
		}
	}




	/*public void carregarVista(Pane vista) {
		if (vista == null) return;

		if (checkSiVistaEstaCarregada(vista.getId())) return;

		this.paneVista.getChildren().clear();

		//paneArrel.setPrefHeight(paneArrel.getHeight() - 80.0);

		this.paneVista.getChildren().add(vista);

		AnchorPane.setTopAnchor(vista,0.0);
		AnchorPane.setBottomAnchor(vista,0.0);
		AnchorPane.setLeftAnchor(vista, 0.0);
		AnchorPane.setRightAnchor(vista, 0.0);
		//this.paneVista.setVisible(true);
	}

	private boolean checkSiVistaEstaCarregada(String id) {
		if (id == null || this.paneVista != null || this.paneVista.getChildren() != null) return false;

		Iterator<Node> fills = this.paneVista.getChildren().iterator();

		while (fills.hasNext()) {
			Node aux = fills.next();
			if (id.equals(aux.getId())) return true;
		}

		return false;
	}*/


















}
