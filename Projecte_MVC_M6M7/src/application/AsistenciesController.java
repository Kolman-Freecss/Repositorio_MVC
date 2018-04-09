package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.AssistenciesDao;
import dao.DaoManager;
import dao.UsuarisDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Assistencies;
import pojos.Usuaris;
import resources.ControlErrores;

public class AsistenciesController implements Initializable{

	@FXML private TableView<Assistencies> historialTable;
	@FXML private TableColumn<Assistencies, String> colDia;
	@FXML private TableColumn<Assistencies, String> colConsulta;
	@FXML private TableColumn<Assistencies, String> colClient;
	@FXML private TableColumn<Assistencies, String> colAssistent;
	@FXML private TableColumn<Assistencies, String> colComentari;
	@FXML private Button btAfegir;

	private AssistenciesDao asistDao = DaoManager.getAssistenciesDao();
	private UsuarisDao usuariDao = DaoManager.getUsuarisDao();
	private SubfinestraAfegirAsistenciaController controladorAfegir;

	private List<Assistencies> llistaAssistencies = new LinkedList<Assistencies>();
	private ObservableList<Assistencies> llistaActual;

	/**
	 * Serveix per saber si al final hem acabat per afegir o modificar
	 */
	private static boolean confirmacio = false;

	/**
	 * Variables per obtenir el usuari Logat
	 */
	private String usuariDoctor;
	private Usuaris usuariLogat;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		try {

			setIconImages();

			/**
			 * Obtenim el usuari Logat
			 */
			usuariDoctor = LoginController.getUsuariDoctor();
			usuariLogat = usuariDao.getUsuari(usuariDoctor);

			if(!"SANITARI".equals(usuariLogat.getPerfils().getDescripcio()) && !"ADMINISTRADOR".equals(usuariLogat.getPerfils().getDescripcio())){
				btAfegir.setVisible(false);
			}

			refreshGrid();

		} catch (SQLException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	private void refreshGrid() {
		try {
			llistaAssistencies = asistDao.getAssistencies();
		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		llistaActual = FXCollections.observableList(llistaAssistencies);

		this.historialTable.setItems(llistaActual);

		colDia.setCellValueFactory(param ->
		new SimpleStringProperty(sdf.format(param.getValue().getData()))
				);

		colConsulta.setCellValueFactory(param ->
		new SimpleStringProperty(param.getValue().getServeis().getDescripcio())
				);

		colClient.setCellValueFactory(param ->
		new SimpleStringProperty(param.getValue().getClients().getNom())
				);

		colAssistent.setCellValueFactory(param ->
		new SimpleStringProperty(param.getValue().getUsuaris().getNom())
				);

		colComentari.setCellValueFactory(param ->
		new SimpleStringProperty(param.getValue().getObservacions())
				);
	}

	@FXML
	public void clickAfegir(ActionEvent event) throws ParseException{

		try {
			showAfegirAsistencia();

			if(confirmacio){
				Assistencies newAssistencia = new Assistencies(controladorAfegir.getServei(),
						this.usuariLogat,
						controladorAfegir.getClient(),
						sdf.parse(controladorAfegir.getFecha()),
						controladorAfegir.getObservacions());

				asistDao.addAssistencia(newAssistencia);

				llistaAssistencies.add(newAssistencia);

				refreshGrid();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Confirmació");
				alert.setHeaderText(null);
				alert.setContentText("S'ha afegit la nova asistencia correctament!");
				alert.showAndWait();
			}

		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	private void showAfegirAsistencia() {
		try {

			Stage window = new Stage();
			FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirAsistencia.fxml"));
			BorderPane root = new BorderPane();
			root = carregador.load();

			controladorAfegir = carregador.getController();
			controladorAfegir.setFuncionalitatS();

			window.setTitle("Consultar Doctor");
			window.setScene(new Scene(root));
			window.setResizable(false);
			window.initModality(Modality.APPLICATION_MODAL);
			window.showAndWait();

		} catch (IOException e) {
			ControlErrores.mostrarError("Error de carga de pantalla", "Hi ha hagut algun error de connexio torna a intentar-ho");
		}
	}

	public void setIconImages(){

		URL linkAfegir = getClass().getResource("/resources/añadir.png");

		Image imageAfegir = new Image(linkAfegir.toString(),24, 24, false, true);

		btAfegir.setGraphic(new ImageView(imageAfegir));
		btAfegir.setStyle("-fx-base: #b6e7c9;");

	}

	public static void setConfirmacio(boolean confirmacioP) {
		confirmacio = confirmacioP;
	}

}



















