package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.ClientsDao;
import dao.DaoManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Clients;
import resources.ControlErrores;

public class TotsPacientsController implements Initializable{

	@FXML private ListView<String> colPacients;

	@FXML private TextField txtNom;
	@FXML private TextField txtCognoms;
	@FXML private TextField txtTelefon;
	@FXML private TextField txtCorreu;

	@FXML private Button btAfegir;
	@FXML private Button btModificar;
	@FXML private Button btEliminar;
	@FXML private Button btConsulta;

	ClientsDao clientDao = DaoManager.getClientsDao();

	/**
	 * Serveix per saber si al final hem acabat per afegir o modificar
	 */
	private static boolean confirmacio = false;

	/**
	 * Llista per recollir els objectes per eliminar, afegir...
	 */
	private static List<Clients> llistaPacients = new LinkedList<Clients>();
	private ObservableList<String> items;

	private static Clients pacientAModificar;

	/**
	 * Amb aquest controlador agafarem les dades del controlador en questió per afegir el Pacient
	 */
	private SubfinestraAfegirPacientController controladorAfegirPacients;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		try {
			setIconImages();

			txtNom.setEditable(false);
			txtCognoms.setEditable(false);
			txtTelefon.setEditable(false);
			txtCorreu.setEditable(false);


			llistaPacients.addAll(clientDao.getClients());


			if(this.colPacients != null){

				items = FXCollections.observableArrayList(clientDao.getNomClients());
				colPacients.setItems(items);

				if(colPacients.getItems().size() >= 0){
					colPacients.getSelectionModel().select(0);

					Clients pacient = llistaPacients.get(0);

					txtNom.setText(pacient.getNom());
					txtCognoms.setText(pacient.getCognoms());
					txtTelefon.setText(pacient.getTelefon());
					txtCorreu.setText(pacient.getCorreu());
				}

			}

			this.colPacients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
					Clients pacient = llistaPacients.get(selectedIdx);

					txtNom.setText(pacient.getNom());
					txtCognoms.setText(pacient.getCognoms());
					txtTelefon.setText(pacient.getTelefon());
					txtCorreu.setText(pacient.getCorreu());

				}

			});

		} catch (HibernateException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickAfegir(ActionEvent event){

		try {
			showAfegirPacient("afegir");

			if(confirmacio){
				Clients newClient = new Clients(this.controladorAfegirPacients.getNom(),
						this.controladorAfegirPacients.getCognoms(),
						this.controladorAfegirPacients.getTelefon(),
						this.controladorAfegirPacients.getCorreu());

				this.clientDao.addClient(newClient);

				llistaPacients.add(newClient);
				items.add(newClient.getNom());

				Alert alertI = new Alert(AlertType.INFORMATION);
				alertI.setTitle("Confirmació");
				alertI.setHeaderText(null);
				alertI.setContentText("S'ha afegit el nou pacient correctament!");
				alertI.showAndWait();
			}


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickModificar(ActionEvent event){

		try {

			int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el servicio para mostrar la información en la subfinestra
			 */
			setPacientAModificar(llistaPacients.get(selectedIdx));

			showAfegirPacient("modificar");

			if(confirmacio){
				Clients updatePacient = new Clients(
						pacientAModificar.getIdClient(),
						this.controladorAfegirPacients.getNom(),
						this.controladorAfegirPacients.getCognoms(),
						this.controladorAfegirPacients.getTelefon(),
						this.controladorAfegirPacients.getCorreu());

				clientDao.updateClient(updatePacient);

				llistaPacients.remove(selectedIdx);
				llistaPacients.add(selectedIdx, updatePacient);

				items.remove(selectedIdx);
				items.add(selectedIdx, updatePacient.getNom());

				colPacients.setItems(items);
				colPacients.getSelectionModel().select(selectedIdx);

				Alert alertI = new Alert(AlertType.INFORMATION);
				alertI.setTitle("Confirmació");
				alertI.setHeaderText(null);
				alertI.setContentText("S'ha modificat el pacient correctament");
				alertI.showAndWait();
			}


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}
	}

	@FXML
	private void clickConsulta() throws IOException, HibernateException, NumberFormatException, SQLException{

		final int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
		Clients clientAConsultar = new Clients();
		clientAConsultar = this.clientDao.getClient(llistaPacients.get(selectedIdx).getIdClient());

		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaDetallsPacient.fxml"));
		DetallsPacientController.setPacient(clientAConsultar);
		BorderPane root = carregador.load();

		window.setTitle("Detalls Pacient");
		window.setScene(new Scene(root));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();

	}

	@FXML
	public void clickEliminar(ActionEvent event) throws SQLException{

		try {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmació");
			alert.setContentText("Estas seguro de esto?");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK){

				int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {
					int newSelectedIdx =
							(selectedIdx == colPacients.getItems().size() - 1)
							? selectedIdx - 1
									: selectedIdx;

					clientDao.deleteClient(llistaPacients.get(selectedIdx).getIdClient());

					colPacients.getItems().remove(selectedIdx);
					colPacients.getSelectionModel().select(newSelectedIdx);

					Alert alertI = new Alert(AlertType.INFORMATION);
					alertI.setTitle("Confirmació");
					alertI.setHeaderText(null);
					alertI.setContentText("S'ha eliminat el pacient correctament");
					alertI.showAndWait();
				}

			} else {
				alert.close();
			}


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}
	}

	private void showAfegirPacient(String funcionalitat){
		try {
			Stage window = new Stage();
			FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirPacient.fxml"));
			BorderPane root = new BorderPane();
			root = carregador.load();

			this.controladorAfegirPacients = carregador.getController();
			this.controladorAfegirPacients.setFuncionalitatS(funcionalitat);


			if("afegir".equals(funcionalitat)){
				window.setTitle("Afegir Pacient");
			}else if("modificar".equals(funcionalitat)){
				window.setTitle("Modificar Pacient");
			}
			window.setScene(new Scene(root));
			window.setResizable(false);
			window.initModality(Modality.APPLICATION_MODAL);
			window.showAndWait();

		} catch (IOException e) {
			ControlErrores.mostrarError("Error de carga de pantalla", "Hi ha hagut algun error de connexio torna a intentar-ho");
		}
	}

	public static Clients getPacientAModificar() {
		return pacientAModificar;
	}

	public static void setPacientAModificar(Clients pacientAModificar) {
		TotsPacientsController.pacientAModificar = pacientAModificar;
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
