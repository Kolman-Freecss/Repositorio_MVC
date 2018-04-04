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

	@FXML private TextField et1; //usuari
	@FXML private TextField et2; //password

	@FXML private Button btEntrar;
	@FXML private Button btRegistrar;

	private static String tipusPerfil;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

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


	/**
	 * Checkea si el usuari existeix y quin tipus de perfil es
	 * @return Si existeix = true / Si no existeix = false
	 */
	private boolean checkLogin(){

		UsuarisDao usuariJDBC = DaoManager.getUsuarisDao();
		Usuaris usuari = null;

		try {

			usuari = usuariJDBC.getUsuari(et1.getText());

			tipusPerfil = usuari.getPerfils().getDescripcio();


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

	public static String getTipusPerfil() {
		return tipusPerfil;
	}
}
