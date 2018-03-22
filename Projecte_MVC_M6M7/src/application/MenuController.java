package application;

import java.io.IOException;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MenuController {

	@FXML private BorderPane paneArrel;
	@FXML private AnchorPane paneVista;




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

}
