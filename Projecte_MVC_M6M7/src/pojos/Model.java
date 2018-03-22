package pojos;

import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

/**
 * Singleton Model
 *
 * El patró Singleton garanteix que només existirà una única instància de la classe
 *
 * Per accedir a la instància Model.getInstance();
 *
 * @author alex
 *
 */
public class Model {
	public static final String[] CICLES = {"DAM","DAW","ASIX"};
	private static Model instance = null;
	private Stage stage;
	private Application app;

	private ObservableList<Clients> clients;

	protected Model() {
		this.clients = FXCollections.observableArrayList();
	}

	public static Model getInstance() {
		if(instance == null) instance = new Model();
		return instance;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Application getApp() {
		return app;
	}

	public void setApp(Application app) {
		this.app = app;
	}

	public ObservableList<Clients> getUnitats() {
		return this.clients;
	}

	public void setUnitats(List<Clients> clients) {
		this.clients = FXCollections.observableArrayList();
		this.clients.addAll(clients);
	}


}
