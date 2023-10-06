package main;

import javafx.scene.control.Alert;

public class Alerts {
    public void showSuccessMessage(String message) {
	Alert alert = new Alert(Alert.AlertType.INFORMATION);
	alert.setTitle("Message");
	alert.setContentText(message);
	alert.showAndWait();
    }

    public void showErrorMessage(String message) {
	Alert alert = new Alert(Alert.AlertType.ERROR);
	alert.setTitle("Error");
	alert.setContentText(message);
	alert.showAndWait();
    }
}
