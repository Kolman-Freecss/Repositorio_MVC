package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.AssistenciesDao;
import dao.ClientsDao;
import dao.DaoManager;
import dao.UsuarisDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Assistencies;
import pojos.Clients;
import pojos.Usuaris;
import resources.ControlErrores;

public class DoctorsController implements Initializable{

	@FXML private ListView<String> colDoctors;
	@FXML private ListView<String> colPacients;
	@FXML private Button btConsulta;
	@FXML private Button btAfegir;
	@FXML private Button btEliminar;
	@FXML private Button btModificar;

	private ClientsDao clientDao = DaoManager.getClientsDao();
	private UsuarisDao usuariDao = DaoManager.getUsuarisDao();
	private AssistenciesDao assistenciesDao = DaoManager.getAssistenciesDao();

	private ObservableList<String> items;
	private ObservableList<String> itemsPacients;

	private static List<Usuaris> llistaDoctors = new LinkedList<Usuaris>();
	private SubfinestraAfegirDoctorController controladorAfegir;

	/**
	 * Serveix per saber si al final hem acabat per afegir o modificar
	 */
	private static boolean confirmacio = true;

	/**
	 * Guardem els doctors que volem modificar o consultar per si volem utilitzar alguna d'aquestes funcionalitats
	 */
	private static Usuaris doctorAModificar;
	private static Usuaris doctorAConsultar;

	/**
	 * Tipus d'usuari Logat
	 */
	private String usuariLogat;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		try {

			setIconImages();

			/**
			 * Control de usuari Logat
			 */
			usuariLogat = LoginController.getTipusPerfil();
			if(!"ADMINISTRADOR".equals(usuariLogat)){
				this.btAfegir.setVisible(false);
				this.btEliminar.setVisible(false);
				this.btModificar.setVisible(false);
			}

			llistaDoctors.addAll(usuariDao.getUsuaris());


			if(this.colDoctors != null){

				items = FXCollections.observableArrayList(usuariDao.getNomUsuaris());
				colDoctors.setItems(items);

				if(colDoctors.getItems().size() >= 0){
					colDoctors.getSelectionModel().select(0);
				}

			}

			if(this.colPacients != null){

				List<Assistencies> llistatAssistencies = this.assistenciesDao.getAssistencies();

				chargePacientXDoctor(llistatAssistencies);

				/**
				 * Listener per al ListView
				 * Al escollir un altre doctor carga automaticament el ListView dels pacients als que ha fet una assistencia
				 */
				colDoctors.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						chargePacientXDoctor(llistatAssistencies);
					}
				});

			}

		} catch (SQLException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	private void chargePacientXDoctor(List<Assistencies> llistatAssistencies) {
		final int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();
		List<String> llistaPacients = new LinkedList<String>();
		Usuaris doctor = llistaDoctors.get(selectedIdx);
		for (Assistencies a : llistatAssistencies) {
			if(a.getUsuaris().getIdUsuari().equals(doctor.getIdUsuari())){
				Clients client;
				try {
					client = clientDao.getClient(a.getClients().getIdClient());
					llistaPacients.add(client.getNom());

					itemsPacients = FXCollections.observableArrayList(llistaPacients);
					colPacients.setItems(itemsPacients);
				} catch (HibernateException e) {
					ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
				}
			}
		}
	}

	@FXML
	public void clickAfegir(ActionEvent event){

		try {
			showAfegirDoctor("afegir");

			if(confirmacio){
				Usuaris newDoctor = new Usuaris(controladorAfegir.getUsuari(),
						controladorAfegir.getPerfil(),
						controladorAfegir.getPassword(),
						controladorAfegir.getNom(),
						controladorAfegir.getCognoms(),
						controladorAfegir.getCorreu(),
						controladorAfegir.getNumColegiat(),
						controladorAfegir.getEspecialitat());

				usuariDao.addUsuari(newDoctor);

				llistaDoctors.add(newDoctor);
				items.add(newDoctor.getNom());

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Confirmació");
				alert.setHeaderText(null);
				alert.setContentText("S'ha afegit el nou doctor correctament!");
				alert.showAndWait();
			}


		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickModificar(ActionEvent event){

		try {
			int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el doctor para mostrar la información en la subfinestra
			 */
			setDoctorAModificar(llistaDoctors.get(selectedIdx));

			showAfegirDoctor("modificar");

			if(confirmacio){
				Usuaris updateDoctor = new Usuaris(controladorAfegir.getUsuari(),
						controladorAfegir.getPerfil(),
						controladorAfegir.getPassword(),
						controladorAfegir.getNom(),
						controladorAfegir.getCognoms(),
						controladorAfegir.getCorreu(),
						controladorAfegir.getNumColegiat(),
						controladorAfegir.getEspecialitat());

				usuariDao.updateUsuari(updateDoctor);

				llistaDoctors.remove(selectedIdx);
				llistaDoctors.add(selectedIdx, updateDoctor);

				items.remove(selectedIdx);
				items.add(selectedIdx, updateDoctor.getNom());

				colDoctors.setItems(items);
				colDoctors.getSelectionModel().select(selectedIdx);


				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Confirmació");
				alert.setHeaderText(null);
				alert.setContentText("S'ha completat la teva modificació!");
				alert.showAndWait();
			}

		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickEliminar(ActionEvent event){

		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmació");
			alert.setContentText("Estas seguro de esto?");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK){
				int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {

					final int newSelectedIdx =
							(selectedIdx == colDoctors.getItems().size() - 1)
							? selectedIdx - 1
									: selectedIdx;


					Usuaris usuariAEliminar = llistaDoctors.get(selectedIdx);


					usuariDao.deleteUsuari(usuariAEliminar);


					colDoctors.getItems().remove(selectedIdx);
					colDoctors.getSelectionModel().select(newSelectedIdx);

					Alert alertI = new Alert(AlertType.INFORMATION);
					alertI.setTitle("Confirmació");
					alertI.setHeaderText(null);
					alertI.setContentText("S'ha completat la eliminació");
					alertI.showAndWait();
				}

			} else {
				alert.close();
			}


		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun problema al intentar eliminar al usuari");
		}

	}

	@FXML
	public void clickConsulta(ActionEvent event){

		try {
			int selectedIdx = this.colDoctors.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el doctor para mostrar la información en la subfinestra
			 */
			setDoctorAConsultar(llistaDoctors.get(selectedIdx));
			showAfegirDoctor("consultar");


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	/**
	 * Obrim el modal
	 * @param funcionalitat
	 */
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

	public static Usuaris getDoctorAModificar() {
		return doctorAModificar;
	}

	public static Usuaris getDoctorAConsultar() {
		return doctorAConsultar;
	}

	public static void setDoctorAModificar(Usuaris doctorAModificar) {
		DoctorsController.doctorAModificar = doctorAModificar;
	}

	public static void setDoctorAConsultar(Usuaris doctorAConsultar) {
		DoctorsController.doctorAConsultar = doctorAConsultar;
	}

	public void setIconImages(){

		URL linkNew = getClass().getResource("/resources/informacion.png");
		URL linkAfegir = getClass().getResource("/resources/añadir.png");
		URL linkModificar = getClass().getResource("/resources/editar.png");
		URL linkEliminar = getClass().getResource("/resources/eliminar.png");

		Image imageNew = new Image(linkNew.toString(),24, 24, false, true);
		Image imageAfegir = new Image(linkAfegir.toString(),24, 24, false, true);
		Image imageModificar = new Image(linkModificar.toString(),24, 24, false, true);
		Image imageEliminar= new Image(linkEliminar.toString(),24, 24, false, true);

		btConsulta.setGraphic(new ImageView(imageNew));
		btConsulta.setStyle("-fx-base: #b6e7c9;");
		btAfegir.setGraphic(new ImageView(imageAfegir));
		btAfegir.setStyle("-fx-base: #b6e7c9;");
		btModificar.setGraphic(new ImageView(imageModificar));
		btModificar.setStyle("-fx-base: #b6e7c9;");
		btEliminar.setGraphic(new ImageView(imageEliminar));
		btEliminar.setStyle("-fx-base: #b6e7c9;");

	}

	public static void setConfirmacio(boolean confirmacioP) {
		confirmacio = confirmacioP;
	}

}
