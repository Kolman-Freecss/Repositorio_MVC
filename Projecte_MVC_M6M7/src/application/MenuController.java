package application;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MenuController implements Initializable {

	@FXML private BorderPane paneArrel;
	@FXML private AnchorPane paneVista;

	@FXML private Menu menuServeis;

	/**
	 * Per portar quin tipus de perfil te l'usuari logat
	 */
	private LoginController loginController;


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		/*FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaLogin.fxml"));
		try {
			BorderPane root = carregador.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loginController = carregador.getController();*/

		/**
		 * Si l'usuari no es un Administrador no tindrá accés
		 */
		if(!LoginController.getTipusPerfil().equals("ADMINISTRADOR")){
			menuServeis.setVisible(false);;
		}

	}

	public void carregarVista(Pane vista) {
		if (vista == null) return;

		if (checkSiVistaEstaCarregada(vista.getId())) return;

		//Limpia lo del centro del BorderPane o algo asi creo hay que INVESTIGARLO MAS
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
	}


	//Vistes.................................................................................

	@FXML
	public void obrirDoctors() {
		try {
			BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaDoctors.fxml"));

			//txtF1 = (TextField) this.vistaInici.lookup("#txtF1"); opcion valida antes de cargar la vista


			this.carregarVista(vista);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void obrirTotsPacients() {
		try {
			BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaTotsPacients.fxml"));

			//txtF1 = (TextField) this.vistaInici.lookup("#txtF1"); opcion valida antes de cargar la vista


			this.carregarVista(vista);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void obrirAbout() {
		try {
			BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaAbout.fxml"));

			//txtF1 = (TextField) this.vistaInici.lookup("#txtF1"); opcion valida antes de cargar la vista


			this.carregarVista(vista);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void obrirPerfilUsuari() {
		try {
			BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaPerfilUsuari.fxml"));

			//txtF1 = (TextField) this.vistaInici.lookup("#txtF1"); opcion valida antes de cargar la vista


			this.carregarVista(vista);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void obrirServeis() {
		try {
			BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaServeis.fxml"));

			//txtF1 = (TextField) this.vistaInici.lookup("#txtF1"); opcion valida antes de cargar la vista


			this.carregarVista(vista);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void tancarSessio() {
		try {
			BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaLogin.fxml"));

			//txtF1 = (TextField) this.vistaInici.lookup("#txtF1"); opcion valida antes de cargar la vista


			this.carregarVista(vista);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
