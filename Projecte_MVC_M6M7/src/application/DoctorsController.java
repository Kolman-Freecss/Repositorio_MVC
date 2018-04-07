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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
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

		/**
		 * Control de usuari Logat
		 */
		usuariLogat = LoginController.getTipusPerfil();
		if(!"ADMINISTRADOR".equals(usuariLogat)){
			this.btAfegir.setVisible(false);
			this.btEliminar.setVisible(false);
			this.btModificar.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	            	TextInputDialog dialog = new TextInputDialog("Contrasenya");
	            	dialog.setTitle("Canvi de contrasenya");
	            	dialog.setHeaderText("Look, a Text Input Dialog");
	            	dialog.setContentText("Introdueix la nova contrasenya:");

	            	Optional<String> result = dialog.showAndWait();
	            	if (result.isPresent()){
	            		int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();
	        			/**
	        			 * Cojemos el doctor para mostrar la información en la subfinestra
	        			 */
	        			setDoctorAModificar(llistaDoctors.get(selectedIdx));

	            		Usuaris updateDoctor = new Usuaris(doctorAModificar.getIdUsuari(),
	            				doctorAModificar.getPerfils(),
	            				result.get(),
	            				doctorAModificar.getNom(),
	            				doctorAModificar.getCognoms(),
	            				doctorAModificar.getCorreu(),
	            				doctorAModificar.getNumcolegiat(),
	            				doctorAModificar.getEspecialitat());

	            		try {
							usuariDao.updateUsuari(updateDoctor);
						} catch (SQLException e1) {
							ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
						}
	            	    System.out.println("Your name: " + result.get());
	            	}
	            }
	        });
		}

		try {
			llistaDoctors.addAll(usuariDao.getUsuaris());
		} catch (SQLException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		if(this.colDoctors != null){

			try {
				items = FXCollections.observableArrayList(usuariDao.getNomUsuaris());
				colDoctors.setItems(items);
			}catch (SQLException e) {
				ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
			}

		}

		if(this.colPacients != null){

			try {
				List<Assistencies> llistatAssistencies = this.assistenciesDao.getAssistencies();

				colDoctors.getSelectionModel().select(0);

				chargePacientXDoctor(llistatAssistencies);

				/**
				 * Al escollir un altre doctor carga automaticament el ListView dels pacients als que ha fet una assistencia
				 */
				colDoctors.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				    @Override
				    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				    	chargePacientXDoctor(llistatAssistencies);
				    }
				});

			} catch (HibernateException e) {
				ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
			}

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


		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickModificar(ActionEvent event){

		try {
			int selectedIdx = colDoctors.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el servicio para mostrar la información en la subfinestra
			 */
			setDoctorAModificar(llistaDoctors.get(selectedIdx));

			showAfegirDoctor("modificar");

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

			          try {
						usuariDao.deleteUsuari(usuariAEliminar);
					} catch (SQLException e) {
						ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
					}

			          colDoctors.getItems().remove(selectedIdx);
			          colDoctors.getSelectionModel().select(newSelectedIdx);
			        }

			} else {
				alert.close();
			}


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
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
	private void showAfegirDoctor(String funcionalitat) {
		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirDoctor.fxml"));
		BorderPane root = new BorderPane();

		try {
			root = carregador.load();
		} catch (IOException e) {
			ControlErrores.mostrarError("Error de carga de pantalla", "Hi ha hagut algun error de connexio torna a intentar-ho");
		}

		controladorAfegir = carregador.getController();
		try {
			controladorAfegir.setFuncionalitatS(funcionalitat);
		} catch (SQLException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

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

}
