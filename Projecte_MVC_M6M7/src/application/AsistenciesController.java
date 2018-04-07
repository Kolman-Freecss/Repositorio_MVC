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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Assistencies;
import pojos.Clients;
import pojos.Usuaris;
import resources.ControlErrores;

public class AsistenciesController implements Initializable{

	@FXML private TableView<Assistencies> historialTable;
	@FXML private TableColumn<Assistencies, String> colDia;
	@FXML private TableColumn<Assistencies, String> colConsulta;
	@FXML private TableColumn<Assistencies, String> colAssistent;
	@FXML private TableColumn<Assistencies, String> colComentari;
	@FXML private Button btAfegir;

	private static Clients pacient;
	private AssistenciesDao asistDao = DaoManager.getAssistenciesDao();
	private UsuarisDao usuariDao = DaoManager.getUsuarisDao();
	private SubfinestraAfegirAsistenciaController controladorAfegir;

	private List<Assistencies> llistaAssistencies = new LinkedList<Assistencies>();
	private ObservableList<Assistencies> llistaActual;

	/**
	 * Variables per obtenir el usuari Logat
	 */
	private String usuariDoctor;
	private Usuaris usuariLogat;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		/**
		 * Obtenim el usuari Logat
		 */
		usuariDoctor = LoginController.getUsuariDoctor();
		try {
			usuariLogat = usuariDao.getUsuari(usuariDoctor);
		} catch (SQLException e1) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}
		if(!"SANITARI".equals(usuariLogat.getPerfils().getDescripcio())){
			btAfegir.setVisible(false);
		}

		refreshGrid();

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



			Assistencies newAssistencia = new Assistencies(controladorAfegir.getServei(),
					this.usuariLogat,
					controladorAfegir.getClient(),
					sdf.parse(controladorAfegir.getFecha()),
					controladorAfegir.getObservacions());

			asistDao.addAssistencia(newAssistencia);

			llistaAssistencies.add(newAssistencia);

			refreshGrid();

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void showAfegirAsistencia() throws IOException, HibernateException, NumberFormatException, SQLException{
		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirAsistencia.fxml"));
		BorderPane root = carregador.load();

		controladorAfegir = carregador.getController();
		controladorAfegir.setFuncionalitatS();

		window.setTitle("Consultar Doctor");
		window.setScene(new Scene(root));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();



	}

}



















