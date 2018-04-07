package application;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.AssistenciesDao;
import dao.DaoManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pojos.Assistencies;
import pojos.Clients;
import resources.ControlErrores;

public class DetallsPacientController implements Initializable{

	@FXML private TextField txtNom;
	@FXML private TextField txtCognoms;
	@FXML private TextField txtTelefon;
	@FXML private TextField txtCorreu;
	@FXML private TableView<Assistencies> historialTable;
	@FXML private TableColumn<Assistencies, String> colDia;
	@FXML private TableColumn<Assistencies, String> colConsulta;
	@FXML private TableColumn<Assistencies, String> colAssistent;
	@FXML private TableColumn<Assistencies, String> colComentari;

	private String nom;
	private String cognoms;
	private String telefon;
	private String correu;

	private static Clients pacient;
	private AssistenciesDao asistDao = DaoManager.getAssistenciesDao();

	private List<Assistencies> llistaAssistencies = new LinkedList<Assistencies>();
	private List<Assistencies> llistaAssistenciesPacient = new LinkedList<Assistencies>();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		txtNom.setText(pacient.getNom());
		txtCognoms.setText(pacient.getCognoms());
		txtTelefon.setText(pacient.getTelefon());
		txtCorreu.setText(pacient.getCorreu());

		/**
		 * Obtenim les assistencies del pacient corresponent
		 */
		try {
			llistaAssistencies = asistDao.getAssistencies();
			for (Assistencies a : llistaAssistencies) {
				if(a.getClients().getIdClient() == pacient.getIdClient()){
					llistaAssistenciesPacient.add(a);
				}
			}

		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		ObservableList<Assistencies> llistaActual = FXCollections.observableList(this.llistaAssistenciesPacient);

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

	public String getNom() {
		return nom;
	}


	public String getCognoms() {
		return cognoms;
	}


	public String getTelefon() {
		return telefon;
	}


	public String getCorreu() {
		return correu;
	}

	public static void setPacient(Clients pacientP) {
		pacient = pacientP;



	}



}



















