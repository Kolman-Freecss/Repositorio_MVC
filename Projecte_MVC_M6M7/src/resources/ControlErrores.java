package resources;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControlErrores {

	public static void mostrarError(String title, String sms) {
		mostrarAlert("Error", title, sms, AlertType.ERROR, "error-panel");
	}

	public static void mostrarInfo(String title, String sms) {
		mostrarAlert("Informació", title, sms, AlertType.INFORMATION, "info-panel");
	}

	private static void mostrarAlert(String title, String header, String sms, AlertType tipus, String paneId) {

		Alert alert = new Alert(tipus);
		alert.setTitle(title);
		alert.getDialogPane().setId(paneId);
		alert.setHeaderText(header);
		alert.setContentText(sms);
		alert.showAndWait();
	}

}
