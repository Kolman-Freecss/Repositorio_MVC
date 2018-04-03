package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.DaoManager;
import dao.ServeisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojos.Serveis;

public class ServeisController implements Initializable{

	@FXML private ListView<String> colServeis;
	private List<String> listNomServeis = new LinkedList<String>();
	ServeisDao serveisDao = DaoManager.getServeisDao();

	@FXML private Button btAfegir;
	@FXML private Button btModificar;
	@FXML private Button btEliminar;
	SubfinestraAfegirServeiController controladorAfegir;

	//Llista de que te els noms del serveis
	private ObservableList<String> items;
	private List<Serveis> listServeis; //Guardamos toda la lista de objetos de la cual podemos recojer para eliminar, añadir...


	private static Serveis serveiAModificar;


	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {

		if(this.colServeis != null){


			try {

				//Es per fer-ho de mes formes pero el mes optim es portar les dades necesaries del Back directament
				listServeis = serveisDao.getServeis();
				for(Serveis s : listServeis){
					listNomServeis.add(s.getDescripcio());
				}

				items = FXCollections.observableArrayList(listNomServeis);

				colServeis.setItems(items);
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

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



			        final int selectedIdx = colServeis.getSelectionModel().getSelectedIndex();
			        if (selectedIdx != -1) {
			        	//esto es solo para mostrar
			          String itemToRemove = colServeis.getSelectionModel().getSelectedItem();

			          final int newSelectedIdx =
			            (selectedIdx == colServeis.getItems().size() - 1)
			               ? selectedIdx - 1
			               : selectedIdx;



			          serveisDao.deleteServei(this.listServeis.get(selectedIdx).getCodi());

			          colServeis.getItems().remove(selectedIdx);
			          System.out.println(itemToRemove);
			          //creo que hay que eliminar este.. o nu...
			          colServeis.getSelectionModel().select(newSelectedIdx);
			        }



			} else {
			    // ... user chose CANCEL or closed the dialog
				alert.close();
			}


		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@FXML
	public void clickModificar(ActionEvent event){

		try {
			final int selectedIdx = colServeis.getSelectionModel().getSelectedIndex();
			/**
			 * Cojemos el servicio para mostrar la información en la subfinestra
			 */
			this.setServeiAModificar(this.listServeis.get(selectedIdx));

			showAfegirServei("modificar");

			Serveis updateServei = new Serveis(Integer.parseInt(controladorAfegir.getCode()), controladorAfegir.getNomServei());

			//para updatear no podemos modificar el code
			serveisDao.updateServei(updateServei);

			listServeis.add(selectedIdx, updateServei);

			colServeis.getItems().remove(selectedIdx);
			items.add(selectedIdx, updateServei.getDescripcio());


		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	public void clickAfegir(ActionEvent event){

		try {
			showAfegirServei("afegir");

			Serveis newServei = new Serveis(Integer.parseInt(controladorAfegir.getCode()), controladorAfegir.getNomServei());

			serveisDao.addServei(newServei);

			listServeis.add(newServei);
			items.add(newServei.getDescripcio());


		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	private void showAfegirServei(String funcionalitat) throws IOException, HibernateException, NumberFormatException, SQLException{
		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirServei.fxml"));
		BorderPane root = carregador.load();

		//recoje el controlador que esta asociado a la variable "carregador" en este caso es el controlador de la vista "VistaSubfinestraAfegirServei.fxml"
		controladorAfegir = carregador.getController();

		controladorAfegir.setFuncionalitat(funcionalitat);

		//ServeisDao serveisDao = DaoManager.getServeisDao();


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





}


















