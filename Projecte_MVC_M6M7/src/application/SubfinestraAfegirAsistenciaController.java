package application;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.ClientsDao;
import dao.DaoManager;
import dao.ServeisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Clients;
import pojos.Serveis;
import resources.ControlErrores;

public class SubfinestraAfegirAsistenciaController implements Initializable{

	@FXML private ComboBox<String> cbServeis;
	@FXML private ComboBox<String> cbClients;
	@FXML private Button etAfegir;
	@FXML private Button etVolver;
	@FXML private TextField etFecha;
	@FXML private TextField etObservacions;

	private Serveis servei;
	private Clients client;
	private String fecha;
	private String observacions;

	private ServeisDao serveisDao = DaoManager.getServeisDao();
	private ClientsDao clientsDao = DaoManager.getClientsDao();

	private List<String> llistaForPutData = new LinkedList<String>();
	private List<Serveis> llistaTemporallyServeis = new LinkedList<Serveis>();
	private List<Clients> llistaTemporallyClients = new LinkedList<Clients>();
	private ObservableList<String> observableListDataServeis;
	private ObservableList<String> observableListDataClients;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

	}


	@FXML
	public void clickAfegir(ActionEvent event){

		servei = llistaTemporallyServeis.get(cbServeis.getSelectionModel().getSelectedIndex());
		client = llistaTemporallyClients.get(cbServeis.getSelectionModel().getSelectedIndex());
		fecha = etFecha.getText();
		observacions = etObservacions.getText();

		Node source = (Node) event.getSource();
		Stage stage2 = (Stage) source.getScene().getWindow();
		stage2.close();


	}

	@FXML
	public void clickVolver(ActionEvent event){

		Node source = (Node) event.getSource();
		Stage stage2 = (Stage) source.getScene().getWindow();
		stage2.close();

	}

	public void setFuncionalitatS() {

		/**
		 * Agafem els serveis
		 */
		try {
			llistaTemporallyServeis = serveisDao.getServeis();
		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		for (Serveis s : llistaTemporallyServeis) {
			llistaForPutData.add(s.getCodi() + " - " + s.getDescripcio());
		}
		observableListDataServeis = FXCollections.observableArrayList(llistaForPutData);
		cbServeis.setItems(observableListDataServeis);
		llistaForPutData.clear();


		/**
		 * Agafem els clients
		 */
		try {
			llistaTemporallyClients = clientsDao.getClients();
		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

		for (Clients c : llistaTemporallyClients) {
			llistaForPutData.add(c.getNom());
		}
		observableListDataClients = FXCollections.observableArrayList(llistaForPutData);
		cbClients.setItems(observableListDataClients);
		llistaForPutData.clear();

		/**
		 * Valor per defecte
		 */
		if(!cbServeis.getSelectionModel().isEmpty()){
			cbServeis.getSelectionModel().select(0);
		}else if(!cbClients.getSelectionModel().isEmpty()){
			cbClients.getSelectionModel().select(0);
		}

		/**
		 * Fem el parse del Date
		 */
		Date utilDate = new Date();
		long lnMilisegundos = utilDate.getTime();
		Date sqlDate = new java.sql.Date(lnMilisegundos);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.etFecha.setText(sdf.format(sqlDate));

	}


	public Serveis getServei() {
		return servei;
	}

	public Clients getClient() {
		return client;
	}


	public String getFecha() {
		return fecha;
	}


	public String getObservacions() {
		return observacions;
	}

}


















