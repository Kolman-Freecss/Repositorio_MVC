package application;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import dao.DaoManager;
import dao.ServeisDao;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Serveis;
import resources.ControlErrores;

public class ServeisController implements Initializable{

	@FXML private ListView<String> colServeis;
	private List<String> listNomServeis = new LinkedList<String>();
	private ServeisDao serveisDao = DaoManager.getServeisDao();

	@FXML private Button btAfegir;
	@FXML private Button btModificar;
	@FXML private Button btEliminar;
	private SubfinestraAfegirServeiController controladorAfegir;

	/**
	 * Serveix per saber si al final hem acabat per afegir o modificar
	 */
	private static boolean confirmacio = false;

	/**
	 * Llista amb els noms dels Serveis
	 */
	private ObservableList<String> items;
	/**
	 * Llista per recollir els objectes per eliminar, afegir...
	 */
	private List<Serveis> listServeis;

	private static Serveis serveiAModificar;


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		setIconImages();



		/**
		 * Si l'usuari no es un administrador no tindrá accés a l'inserció, modificació o eliminació d'un servei
		 */
		if(!LoginController.getTipusPerfil().equals("ADMINISTRADOR")){
			btAfegir.setVisible(false);
			btModificar.setVisible(false);
			btEliminar.setVisible(false);
		}

		if(this.colServeis != null){
			try {

				listServeis = serveisDao.getServeis();
				for(Serveis s : listServeis){
					listNomServeis.add(s.getDescripcio());
				}
				items = FXCollections.observableArrayList(listNomServeis);

				colServeis.setItems(items);

				if(colServeis.getItems().size() >= 0){
					colServeis.getSelectionModel().select(0);
				}

			} catch (HibernateException e) {
				ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
			}

		}

	}

	@FXML
	public void clickEliminar(ActionEvent event) throws HibernateException{

		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmació");
			alert.setContentText("Estas seguro de esto?");

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK){

				int selectedIdx = colServeis.getSelectionModel().getSelectedIndex();
				if (selectedIdx != -1) {
					int newSelectedIdx =
							(selectedIdx == colServeis.getItems().size() - 1)
							? selectedIdx - 1
									: selectedIdx;

					serveisDao.deleteServei(this.listServeis.get(selectedIdx).getCodi());

					colServeis.getItems().remove(selectedIdx);
					colServeis.getSelectionModel().select(newSelectedIdx);

					Alert alertI = new Alert(AlertType.INFORMATION);
					alertI.setTitle("Confirmació");
					alertI.setHeaderText(null);
					alertI.setContentText("S'ha completat la eliminació");
					alertI.showAndWait();
				}

			} else {
				alert.close();
			}


		} catch (ConstraintViolationException e) {
			ControlErrores.mostrarError("Error al eliminar el servei", "Hi ha alguna asistencia relacionada amb aquest servei");
		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}


	@FXML
	public void clickModificar(ActionEvent event){

		try {
			int selectedIdx = colServeis.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el servicio para mostrar la información en la subfinestra
			 */
			setServeiAModificar(this.listServeis.get(selectedIdx));

			showAfegirServei("modificar");

			if(confirmacio){

				Serveis updateServei = new Serveis(controladorAfegir.getCode(), controladorAfegir.getNomServei());

				this.serveisDao.updateServei(updateServei);

				this.listServeis.remove(selectedIdx);
				this.listServeis.add(selectedIdx, updateServei);

				/**
				 * Refresh ListView
				 */
				this.items.remove(selectedIdx);
				this.items.add(selectedIdx, updateServei.getDescripcio());
				this.colServeis.setItems(items);
				colServeis.getSelectionModel().select(selectedIdx);

				Alert alertI = new Alert(AlertType.INFORMATION);
				alertI.setTitle("Confirmació");
				alertI.setHeaderText(null);
				alertI.setContentText("S'ha modificat el servei correctament");
				alertI.showAndWait();

			}

		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}

	@FXML
	public void clickAfegir(ActionEvent event){

		try {

			showAfegirServei("afegir");

			if(confirmacio){
				Serveis newServei = new Serveis(controladorAfegir.getCode(), controladorAfegir.getNomServei());

				serveisDao.addServei(newServei);

				listServeis.add(newServei);
				items.add(newServei.getDescripcio());

				Alert alertI = new Alert(AlertType.INFORMATION);
				alertI.setTitle("Confirmació");
				alertI.setHeaderText(null);
				alertI.setContentText("S'ha afegit el servei correctament");
				alertI.showAndWait();
			}


		} catch (HibernateException e) {
			ControlErrores.mostrarError("Error de carga de dades", "Hi ha hagut algun al cargar les dades");
		}

	}



	private void showAfegirServei(String funcionalitat){
		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirServei.fxml"));
		BorderPane root = new BorderPane();
		try {
			root = carregador.load();
		} catch (IOException e) {
			ControlErrores.mostrarError("Error de carga de pantalla", "Hi ha hagut algun error de connexio torna a intentar-ho");
		}

		controladorAfegir = carregador.getController();
		controladorAfegir.setFuncionalitatS(funcionalitat);

		if("afegir".equals(funcionalitat)){
			window.setTitle("Afegir Servei");
		}else if("modificar".equals(funcionalitat)){
			window.setTitle("Modificar Servei");
		}
		window.setScene(new Scene(root));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();



	}

	public static void setServeiAModificar(Serveis serveiAModificarP) {
		serveiAModificar = serveiAModificarP;
	}

	public static Serveis getServeiAModificar() {
		return serveiAModificar;
	}

	public void setIconImages(){

		URL linkAfegir = getClass().getResource("/resources/añadir.png");
		URL linkModificar = getClass().getResource("/resources/editar.png");
		URL linkEliminar = getClass().getResource("/resources/eliminar.png");

		Image imageAfegir = new Image(linkAfegir.toString(),24, 24, false, true);
		Image imageModificar = new Image(linkModificar.toString(),24, 24, false, true);
		Image imageEliminar= new Image(linkEliminar.toString(),24, 24, false, true);

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


















