package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.HibernateException;

import antlr.collections.impl.LList;
import dao.AssistenciesDao;
import dao.ClientsDao;
import dao.DaoManager;
import dao.UsuarisDao;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import pojos.Assistencies;
import pojos.Usuaris;

public class DoctorsController implements Initializable{

	@FXML private ListView<String> colDoctors;
	@FXML private ListView<String> colPacients;
	ClientsDao clientDao = DaoManager.getClientsDao();
	UsuarisDao usuariDao = DaoManager.getUsuarisDao();

	AssistenciesDao assistenciesDao = DaoManager.getAssistenciesDao();

	//llista amb els usuaris OK ESTO NOS SERVIRA PARA EL REMOVE, YA QUE AQUI NO SIRVE PORQUE EL NOMBRE DEL USUARIO(MEDICO) ES PRIMARY KEY
	//private List<Usuaris> listUsuarisAuxiliar;
	private ObservableList<String> items;

	//private List<Usuaris> llistaDoctors = new LinkedList<Usuaris>(Arrays.asList(usuariDao.getUsuaris()));



	@Override
	public void initialize(URL url, ResourceBundle rsrcs) {


		if(this.colDoctors != null){


			try {
				//listUsuarisAuxiliar = usuariDao.getUsuaris();
				items = FXCollections.observableArrayList(usuariDao.getNomUsuaris());//usuariDao.getNomUsuaris()
				colDoctors.setItems(items);
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if(this.colPacients != null){

			//ObservableList<String> items;
			try {
				//Cojes totes les assistencies..
				List<Assistencies> llistatAssistencies = this.assistenciesDao.getAssistencies();
				//P
				List<String> llistaPacients = new LinkedList<String>();


				//Agafem el index del doctor del listview que sera igual al index on es troba el usuari en la llista auxiliar que hem fet
				final int selectedIdx = colPacients.getSelectionModel().getSelectedIndex();


		        //if (selectedIdx != -1) {

				colPacients.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				    @Override
				    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				        // Your action here
				        System.out.println("Selected item: " + newValue);

				      //Iterem en el llistat d'assistencies per trobar els pacients que estan relacionats amb el assistent que l'ha fet la consulta
				          for(Assistencies a : llistatAssistencies){
				        	  if(a.getUsuaris().getIdUsuari().equals(newValue)){
				        		  llistaPacients.add(a.getClients().getNom());
				        	  }
				          }


						items = FXCollections.observableArrayList(llistaPacients);
						colPacients.setItems(items);
				    }
				});

		          String itemToRemove = colPacients.getSelectionModel().getSelectedItem();

		          /*final int newSelectedIdx =
		            (selectedIdx == colPacients.getItems().size() - 1) ? selectedIdx - 1 : selectedIdx;

		          //serveisDao.deleteServei(codi);
		          colPacients.getItems().remove(selectedIdx);
		          System.out.println(itemToRemove);
		          colPacients.getSelectionModel().select(newSelectedIdx);
		        }*/




			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}


}
