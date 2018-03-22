package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import dao.DaoManager;
import dao.ServeisDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
	SubfinestraAfegirServeiController controladorAfegir;

	//Llista de que te els noms del serveis
	private ObservableList<String> items;
	private List<Serveis> listServeis;


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
	public void clickAfegir(ActionEvent event){

		try {
			showAfegirServei();

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

	private void showAfegirServei() throws IOException, HibernateException, NumberFormatException, SQLException{
		Stage window = new Stage();
		FXMLLoader carregador = new FXMLLoader(getClass().getResource("VistaSubfinestraAfegirServei.fxml"));
		BorderPane root = carregador.load();

		controladorAfegir = carregador.getController();

		ServeisDao serveisDao = DaoManager.getServeisDao();



		window.setTitle("Afegir Servei");
		window.setScene(new Scene(root));
		window.setResizable(false);
		window.initModality(Modality.APPLICATION_MODAL);
		window.showAndWait();



	}

}


















