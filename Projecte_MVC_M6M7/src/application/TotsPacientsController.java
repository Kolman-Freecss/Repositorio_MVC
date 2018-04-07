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

		txtNom.setEditable(false);
		//txtNom.setMouseTransparent(true); esto no sino no podriamos copiar el nombre arrastrando el raton
		txtCognoms.setEditable(false);
		txtTelefon.setEditable(false);
		txtCorreu.setEditable(false);

		try {
			llistaPacients.addAll(clientDao.getClients());
		} catch (HibernateException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		if(this.colPacients != null){


			try {
				items = FXCollections.observableArrayList(clientDao.getNomClients());
				colPacients.setItems(items);
			} catch (HibernateException e) {
				ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
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

	}

	@FXML
	public void clickAfegir(ActionEvent event){

		try {
			showAfegirPacient("afegir");

			Clients newClient = new Clients(this.controladorAfegirPacients.getNom(),
					this.controladorAfegirPacients.getCognoms(),
					this.controladorAfegirPacients.getTelefon(),
					this.controladorAfegirPacients.getCorreu());

			this.clientDao.addClient(newClient);

			this.llistaPacients.add(newClient);
			items.add(newClient.getNom());


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickModificar(ActionEvent event){

		try {
			final int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el servicio para mostrar la información en la subfinestra
			 */
			this.setPacientAModificar(this.llistaPacients.get(selectedIdx));

			showAfegirPacient("modificar");

			Clients updatePacient = new Clients(
					pacientAModificar.getIdClient(),
					this.controladorAfegirPacients.getNom(),
					this.controladorAfegirPacients.getCognoms(),
					this.controladorAfegirPacients.getTelefon(),
					this.controladorAfegirPacients.getCorreu());

			//para updatear no podemos modificar el code
			clientDao.updateClient(updatePacient);

			llistaPacients.remove(selectedIdx);//-----------
			llistaPacients.add(selectedIdx, updatePacient);

			//colPacients.getItems().remove(selectedIdx);
			items.remove(selectedIdx);
			items.add(selectedIdx, updatePacient.getNom());
			colPacients.setItems(items);
			colPacients.getSelectionModel().select(selectedIdx);


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}
	}

	private void showAfegirPacient(String funcionalitat){
		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirPacient.fxml"));
		BorderPane root = new BorderPane();
		try {
			root = carregador.load();
		} catch (IOException e) {
			ControlErrores.mostrarError("Error de carga de pantalla", "Hi ha hagut algun error de connexio torna a intentar-ho");
		}

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
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Look, a Confirmation Dialog");
			alert.setContentText("Are you ok with this?");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK){

			        final int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();
			        if (selectedIdx != -1) {
			        	//esto es solo para mostrar
			          String itemToRemove = colPacients.getSelectionModel().getSelectedItem();

			          final int newSelectedIdx =
			            (selectedIdx == colPacients.getItems().size() - 1)
			               ? selectedIdx - 1
			               : selectedIdx;



			          clientDao.deleteClient(this.llistaPacients.get(selectedIdx).getIdClient());

			          colPacients.getItems().remove(selectedIdx);
			          System.out.println(itemToRemove);
			          colPacients.getSelectionModel().select(newSelectedIdx);
			        }

			} else {
				alert.close();
			}


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	public static Clients getPacientAModificar() {
		return pacientAModificar;
	}

	public static void setPacientAModificar(Clients pacientAModificar) {
		TotsPacientsController.pacientAModificar = pacientAModificar;
	}


}
