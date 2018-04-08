package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import dao.DaoManager;
import dao.UsuarisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojos.Perfils;
import resources.ControlErrores;

public class SubfinestraAfegirDoctorController implements Initializable{

	@FXML private Button btAfegir;
	@FXML private Button btVolver;
	@FXML private TextField etUsuari;
	@FXML private PasswordField etPassword;
	@FXML private TextField etNom;
	@FXML private TextField etCognoms;
	@FXML private TextField etCorreu;
	@FXML private TextField etNumColegiat;
	@FXML private ComboBox<String> cbPerfils;
	@FXML private ComboBox<String> cbEspecialitat;

	private UsuarisDao usuDao = DaoManager.getUsuarisDao();
	private List<Perfils> perfilsTemporally = new LinkedList<Perfils>();
	private List<String> perfilsParse = new LinkedList<String>();
	private ObservableList<String> perfilsData;
	private String especialitatsList[] = {"Enfermer", "Asistent"};
	private ObservableList<String> especialitatsData;

	private String usuari;
	private Perfils perfil;
	private String password;
	private String nom;
	private String cognoms;
	private String correu;
	private int numColegiat;
	private String especialitat;

	private static String funcionalitat;

	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

	}


	@FXML
	public void clickAfegir(ActionEvent event){
		int index = 0;
		try{

			DoctorsController.setConfirmacio(false);
			PerfilUsuariController.setConfirmacio(false);

			usuari = etUsuari.getText();
			password = etPassword.getText();
			String perfilPerCreacio = cbPerfils.getValue();
			/**
			 * Obtenim el perfil en base al nom escollit en el ComboBox
			 */
			while(!perfilPerCreacio.equals(perfilsTemporally.get(index).getDescripcio()) && index < perfilsTemporally.size()){
				index++;
			}

			/**
			 * Recollim la data per agafarla desde el DoctorsController
			 */
			perfil = perfilsTemporally.get(index);
			nom = etNom.getText();
			cognoms = etCognoms.getText();
			correu = etCorreu.getText();
			numColegiat = Integer.parseInt(etNumColegiat.getText());
			especialitat = cbEspecialitat.getValue();

			Node source = (Node) event.getSource();
			Stage stage2 = (Stage) source.getScene().getWindow();
			stage2.close();
		}catch(NumberFormatException e){
			ControlErrores.mostrarWarning("Error en el format de camp", "Es necesita numeros");
		}

	}

	@FXML
	public void clickVolver(ActionEvent event){

		DoctorsController.setConfirmacio(false);
		PerfilUsuariController.setConfirmacio(false);

		Node source = (Node) event.getSource();
		Stage stage2 = (Stage) source.getScene().getWindow();
		stage2.close();

	}


	public void setFuncionalitatS(String funcionalitatP) throws SQLException {
		funcionalitat = funcionalitatP;

		/**
		 * Afegim la data al ComboBox de perfils y especialitats
		 */
		perfilsTemporally.addAll(this.usuDao.getPerfils());
		for (Perfils p : perfilsTemporally) {
			perfilsParse.add(p.getDescripcio());
		}
		perfilsData = FXCollections.observableArrayList(perfilsParse);
		cbPerfils.setItems(perfilsData);
		especialitatsData = FXCollections.observableArrayList(especialitatsList);
		cbEspecialitat.setItems(especialitatsData);

		/**
		 * Focus per defecte
		 */
		if(cbEspecialitat.getItems().size() > 0){
			cbEspecialitat.getSelectionModel().select(0);
		}if(cbPerfils.getItems().size() > 0){
			cbPerfils.getSelectionModel().select(0);
		}

		/**
		 * Omplim data
		 */
		if("modificar".equals(funcionalitat)){
			etUsuari.setEditable(false);

			usuari = DoctorsController.getDoctorAModificar().getIdUsuari();
			password = DoctorsController.getDoctorAModificar().getPassword();
			perfil = DoctorsController.getDoctorAModificar().getPerfils();
			nom = DoctorsController.getDoctorAModificar().getNom();
			cognoms = DoctorsController.getDoctorAModificar().getCognoms();
			correu = DoctorsController.getDoctorAModificar().getCorreu();
			numColegiat = DoctorsController.getDoctorAModificar().getNumcolegiat();
			especialitat = DoctorsController.getDoctorAModificar().getEspecialitat();
			etUsuari.setText(usuari);
			etPassword.setText(password);
			cbPerfils.getSelectionModel().select(perfil.getDescripcio());
			etNom.setText(nom);
			etCognoms.setText(cognoms);
			etCorreu.setText(correu);
			etNumColegiat.setText(String.valueOf(numColegiat));
			cbEspecialitat.getSelectionModel().select(especialitat);
		}else if ("consultar".equals(funcionalitat)){
			etUsuari.setEditable(false);
			etPassword.setEditable(false);
			cbPerfils.setEditable(false);
			etNom.setEditable(false);
			etCognoms.setEditable(false);
			etCorreu.setEditable(false);
			etNumColegiat.setEditable(false);
			cbEspecialitat.setEditable(false);
			btAfegir.setVisible(false);

			etUsuari.setText(DoctorsController.getDoctorAConsultar().getIdUsuari());
			etPassword.setText(DoctorsController.getDoctorAConsultar().getPassword());
			cbPerfils.getSelectionModel().select(DoctorsController.getDoctorAConsultar().getPerfils().getDescripcio());
			etNom.setText(DoctorsController.getDoctorAConsultar().getNom());
			etCognoms.setText(DoctorsController.getDoctorAConsultar().getCognoms());
			etCorreu.setText(DoctorsController.getDoctorAConsultar().getCorreu());
			etNumColegiat.setText(String.valueOf(DoctorsController.getDoctorAConsultar().getNumcolegiat()));
			cbEspecialitat.getSelectionModel().select(DoctorsController.getDoctorAConsultar().getEspecialitat());
		}else if("perfil".equals(funcionalitat)){
			etUsuari.setEditable(false);

			usuari = PerfilUsuariController.getUsuariAdminAModificar().getIdUsuari();
			password = PerfilUsuariController.getUsuariAdminAModificar().getPassword();
			perfil = PerfilUsuariController.getUsuariAdminAModificar().getPerfils();
			nom = PerfilUsuariController.getUsuariAdminAModificar().getNom();
			cognoms = PerfilUsuariController.getUsuariAdminAModificar().getCognoms();
			correu = PerfilUsuariController.getUsuariAdminAModificar().getCorreu();
			numColegiat = PerfilUsuariController.getUsuariAdminAModificar().getNumcolegiat();
			especialitat = PerfilUsuariController.getUsuariAdminAModificar().getEspecialitat();
			etUsuari.setText(usuari);
			etPassword.setText(password);
			cbPerfils.getSelectionModel().select(perfil.getDescripcio());
			etNom.setText(nom);
			etCognoms.setText(cognoms);
			etCorreu.setText(correu);
			etNumColegiat.setText(String.valueOf(numColegiat));
			cbEspecialitat.getSelectionModel().select(especialitat);
		}
	}


	public String getUsuari() {
		return usuari;
	}

	public Perfils getPerfil() {
		return perfil;
	}

	public String getPassword() {
		return password;
	}

	public String getNom() {
		return nom;
	}

	public String getCognoms() {
		return cognoms;
	}

	public String getCorreu() {
		return correu;
	}

	public int getNumColegiat() {
		return numColegiat;
	}

	public String getEspecialitat() {
		return especialitat;
	}

}


















