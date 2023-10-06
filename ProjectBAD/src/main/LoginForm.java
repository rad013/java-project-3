package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginForm {

    void start(Stage loginStage) {
	loginStage.setTitle("Shoes Station");

	LoginData loginData = new LoginData();

	Database database = new Database();

	Alerts alerts = new Alerts();

	BorderPane borderPane = new BorderPane();

	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(25, 25, 25, 25));

	Label titleLabel = new Label("Shoes Station");

	Label userNameLabel = new Label("Username");
	grid.add(userNameLabel, 0, 1);

	TextField userNameTextField = new TextField();
	grid.add(userNameTextField, 1, 1);

	Label passwordLabel = new Label("Password");
	grid.add(passwordLabel, 0, 2);

	PasswordField passwordField = new PasswordField();
	grid.add(passwordField, 1, 2);

	Button loginButton = new Button("Login");
	loginButton.setPadding(new Insets(15, 30, 15, 30));

	Hyperlink registerLink = new Hyperlink("Register Here");

	registerLink.setOnAction(event -> {

	    loginStage.close();

	    RegisterForm registerForm = new RegisterForm();
	    registerForm.start(new Stage());

	});

	loginButton.setOnAction(event -> {
	    try {

		String username = userNameTextField.getText();
		String password = passwordField.getText();

		if (username.isEmpty()) {
		    throw new Exception("Username field is empty");
		}
		if (password.isEmpty()) {
		    throw new Exception("Password field is empty");
		}

		boolean loginSuccess = database.checkUsernamePassword(username, password);

		if (loginSuccess) {

		    loginStage.close();
		    alerts.showSuccessMessage("Login Success!");

		    loginData.setUsername(username);

		    MainForm mainForm = new MainForm(database.getUserType(username));
		    mainForm.start(new Stage());

		} else {
		    alerts.showErrorMessage("Incorrect Username / Password");
		    return;
		}
	    } catch (Exception e) {

		alerts.showErrorMessage(e.getMessage());
		return;

	    }
	});
	VBox btns = new VBox(40);
	btns.getChildren().addAll(loginButton, registerLink);
	btns.setPadding(new Insets(20, 0, 20, 0));
	btns.setAlignment(Pos.CENTER);

	VBox layout = new VBox();
	layout.getChildren().addAll(grid, btns);
	layout.setAlignment(Pos.CENTER);

	
	VBox title = new VBox(titleLabel);
	title.setAlignment(Pos.CENTER);
	title.setPadding(new Insets(20, 0, 0, 0));

	borderPane.setTop(title);
	
	borderPane.setCenter(layout);

	Scene scene = new Scene(borderPane, 500, 500);
	loginStage.setScene(scene);
	loginStage.setResizable(false);
	loginStage.show();
    }

}
