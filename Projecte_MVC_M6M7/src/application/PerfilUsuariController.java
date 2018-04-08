package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.DaoManager;
import dao.UsuarisDao;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Usuaris;
import resources.ControlErrores;

public class PerfilUsuariController implements Initializable{

	@FXML private TextField etUsuari;
	@FXML private TextField etPerfil;
	@FXML private TextField etEspecialitat;
	@FXML private TextField etPassword;
	@FXML private TextField etNom;
	@FXML private TextField etCognoms;
	@FXML private TextField etCorreu;
	@FXML private TextField etNumColegiat;
	@FXML private Button btModificar;

	private UsuarisDao usuDao = DaoManager.getUsuarisDao();
	private String usuariNom;
	private Usuaris usuari;

	/**
	 * Serveix per saber si al final hem acabat per afegir o modificar
	 */
	private static boolean confirmacio = true;

	/**
	 * Aquestes variables son per si el usuari Logat es un administrador
	 */
	private static Usuaris usuariAdminAModificar;
	private SubfinestraAfegirDoctorController controladorAfegir;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		try {

			setIconImages();

			/**
			 * Agafem l'usuari logat
			 */
			usuariNom = LoginController.getUsuariDoctor();
			usuari = usuDao.getUsuari(usuariNom);
			usuariAdminAModificar = usuari;

			/**
			 * Control de usuari Logat
			 */
			if(!"ADMINISTRADOR".equals(usuari.getPerfils().getDescripcio())){
				this.btModificar.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						TextInputDialog dialog = new TextInputDialog("Contrasenya");
						dialog.setTitle("Canvi de contrasenya");
						dialog.setContentText("Introdueix la nova contrasenya:");

						Optional<String> result = dialog.showAndWait();
						if (result.isPresent()){
							try {
								Usuaris updateDoctor = new Usuaris(usuari.getIdUsuari(),
										usuari.getPerfils(),
										result.get(),
										usuari.getNom(),
										usuari.getCognoms(),
										usuari.getCorreu(),
										usuari.getNumcolegiat(),
										usuari.getEspecialitat());

								usuDao.updateUsuari(updateDoctor);

								/**
								 * Fem un petit refresh de la vista
								 */
								usuari = usuDao.getUsuari(usuariNom);
								etPassword.setText(usuari.getPassword());

								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Confirmació");
								alert.setHeaderText(null);
								alert.setContentText("S'ha modificat la teva contrasenya correctament");
								alert.showAndWait();
							} catch (SQLException e1) {
								ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al intentar modificar les dades");
							}
						}
					}
				});
			}else if("ADMINISTRADOR".equals(usuari.getPerfils().getDescripcio())){
				this.btModificar.setOnAction(new EventHandler<ActionEvent>(){
					@Override public void handle(ActionEvent e) {
						try{
							showAfegirDoctor("perfil");

							if(confirmacio){
								Usuaris updateDoctor = new Usuaris(controladorAfegir.getUsuari(),
										controladorAfegir.getPerfil(),
										controladorAfegir.getPassword(),
										controladorAfegir.getNom(),
										controladorAfegir.getCognoms(),
										controladorAfegir.getCorreu(),
										controladorAfegir.getNumColegiat(),
										controladorAfegir.getEspecialitat());

								usuDao.updateUsuari(updateDoctor);

								/**
								 * Fem un petit refresh de la vista
								 */
								usuari = usuDao.getUsuari(usuariNom);
								putDataIfModificate();
							}

						} catch (SQLException e1) {
							ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al intentar modificar les dades");
						}
					}
				});
			}


			putDataIfModificate();

		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al intentar modificar les dades");
		}

	}

	/**
	 * Posem la data recollida del usuari Logat
	 */
	private void putDataIfModificate() {
		etUsuari.setText(usuari.getIdUsuari());
		etPerfil.setText(usuari.getPerfils().getDescripcio());
		etEspecialitat.setText(usuari.getEspecialitat());
		etPassword.setText(usuari.getPassword());
		etNom.setText(usuari.getNom());
		etCognoms.setText(usuari.getCognoms());
		etCorreu.setText(usuari.getCorreu());
		etNumColegiat.setText(String.valueOf(usuari.getNumcolegiat()));
	}

	public void showAfegirDoctor(String funcionalitat) {
		try {

			Stage window = new Stage();
			FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirDoctor.fxml"));
			BorderPane root = new BorderPane();
			root = carregador.load();
			controladorAfegir = carregador.getController();

			controladorAfegir.setFuncionalitatS(funcionalitat);

			if("afegir".equals(funcionalitat)){
				window.setTitle("Afegir Doctor");
			}else if("modificar".equals(funcionalitat)){
				window.setTitle("Modificar Doctor");
			}else if("consultar".equals(funcionalitat)){
				window.setTitle("Consultar Doctor");
			}
			window.setScene(new Scene(root));
			window.setResizable(false);
			window.initModality(Modality.APPLICATION_MODAL);
			window.showAndWait();

		} catch (IOException e) {
			ControlErrores.mostrarError("Error de carga de pantalla", "Hi ha hagut algun error de connexio torna a intentar-ho");
		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	public void setIconImages(){

		URL linkModificar = getClass().getResource("/resources/editar.png");

		Image imageModificar = new Image(linkModificar.toString(),24, 24, false, true);

		btModificar.setGraphic(new ImageView(imageModificar));
		btModificar.setStyle("-fx-base: #b6e7c9;");

	}

	public static void setConfirmacio(boolean confirmacioP) {
		confirmacio = confirmacioP;
	}

	public static Usuaris getUsuariAdminAModificar() {
		return usuariAdminAModificar;
	}

}
