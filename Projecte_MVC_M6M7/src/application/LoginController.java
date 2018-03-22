package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import dao.DaoManager;
import dao.UsuarisDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pojos.Usuaris;
import resources.ControlErrores;

public class LoginController implements Initializable{

	@FXML private TextField et1;
	@FXML private TextField et2;

	@FXML private Button btEntrar;
	@FXML private Button btRegistrar;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	public void clickEntrar(ActionEvent event){
		try {
			//BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaDoctors.fxml"));
//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaDoctors.fxml"));
			if(this.checkLogin()){
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaAppMenu.fxml"));

				BorderPane root = (BorderPane) fxmlLoader.load();

				MenuController controllerGeneral = fxmlLoader.getController();

				//Creamos una nueva pantalla
				Stage stage = new Stage();
				//Le metemos el BorderPane que viene a ser el menu en la pantalla
				stage.setScene(new Scene(root));

				controllerGeneral.obrirDoctors();

				stage.show();


				/*Parent root1 = (Parent)fxmlLoader.load();

				Stage stage = new Stage();

				stage.setScene(new Scene(root1));

				stage.show();*/

				//((Node)(event.getSource())).getScene().getWindow().hide();
				Node source = (Node) event.getSource();
				Stage stage2 = (Stage) source.getScene().getWindow();
				stage2.close();

			}else{

				ControlErrores.mostrarError(" el usuario no existe", "Usuario o contrase�a incorrectos");

			}

			//this.carregarVista(vista);
		} catch (Exception e) {
			e.printStackTrace();
			//MenuController.mostrarError("No s'ha pogut mostrar la gestió d'Unitats Formatives", e.getMessage());
		}
	}

	@FXML
	public void clickRegistrar(ActionEvent event){

		try {
			//BorderPane vista = (BorderPane)FXMLLoader.load(getClass().getResource("VistaDoctors.fxml"));

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaRegister.fxml"));
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

	private boolean checkLogin(){

		UsuarisDao usuariJDBC = DaoManager.getUsuarisDao();
		Usuaris usuari = null;

		try {

			usuari = usuariJDBC.getUsuari(et1.getText());


		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(usuari == null){
			return false;
		}else{
			if(et2.getText().equals(usuari.getPassword())){
				return true;
			}else{
				return false;
			}
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
