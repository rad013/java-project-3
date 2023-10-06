package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterForm {

    void start(Stage registerStage) {
	registerStage.setTitle("Shoes Station");

	Database database = new Database();

	Alerts alerts = new Alerts();

	BorderPane borderPane = new BorderPane();

	GridPane grid = new GridPane();
	grid.setAlignment(Pos.CENTER);
	grid.setHgap(10);
	grid.setVgap(10);
	grid.setPadding(new Insets(25, 25, 25, 25));

	Label titleLabel = new Label("Shoes Station");

	Label usernameLabel = new Label("Username");
	grid.add(usernameLabel, 0, 1);

	TextField usernameTextField = new TextField();
	grid.add(usernameTextField, 1, 1);

	Label emailLabel = new Label("Email");
	grid.add(emailLabel, 0, 2);

	TextField emailTextField = new TextField();
	grid.add(emailTextField, 1, 2);

	Label passwordLabel = new Label("Password");
	grid.add(passwordLabel, 0, 3);

	PasswordField passwordField = new PasswordField();
	grid.add(passwordField, 1, 3);

	Label confirmPasswordLabel = new Label("Confirm Password");
	grid.add(confirmPasswordLabel, 0, 4);

	TextField confirmTextField = new TextField();
	grid.add(confirmTextField, 1, 4);

	Label genderLabel = new Label("Gender");
	grid.add(genderLabel, 0, 5);

	ToggleGroup genderGroup = new ToggleGroup();

	RadioButton maleRadioButton = new RadioButton("Male");
	maleRadioButton.setToggleGroup(genderGroup);

	RadioButton femaleRadioButton = new RadioButton("Female");
	femaleRadioButton.setToggleGroup(genderGroup);

	HBox genderBox = new HBox(20);
	genderBox.getChildren().addAll(femaleRadioButton, maleRadioButton);

	grid.add(genderBox, 1, 5);

	Label ageLabel = new Label("Age");
	grid.add(ageLabel, 0, 6);

	Spinner<Integer> ageSpinner = new Spinner<>(0, 122, 0);
	grid.add(ageSpinner, 1, 6);

	Button registerButton = new Button("Register");

	registerButton.setPadding(new Insets(15, 30, 15, 30));

	Hyperlink loginLink = new Hyperlink("Back to Login");
	loginLink.setOnAction(event -> {

	    registerStage.close();

	    LoginForm loginForm = new LoginForm();
	    loginForm.start(new Stage());

	});

	registerButton.setOnAction(event -> {

	    try {

		String username = usernameTextField.getText();
		String email = emailTextField.getText();
		String password = passwordField.getText();
		String confirmPassword = confirmTextField.getText();
		String userType = "User";

		if (username.length() < 5 || username.length() > 20) {
		    throw new Exception("Username must be between 5-20 characters");
		}
		if (password.length() < 5 || password.length() > 15) {
		    throw new Exception("Password must be between 5-15 characters");
		}
		if (!email.matches("^[^@.]+@[^@.]+\\.[^@.]+$")) {
		    throw new Exception("Email must be an email format: [email]@[provider].[domain]");
		}
		if (!password.equals(confirmPassword)) {
		    throw new Exception("Password and confirm password must be the same");
		}
		if (genderGroup.getSelectedToggle() == null) {
		    throw new Exception("Gender must be selected");
		}
		if (ageSpinner.getValue() < 11 || ageSpinner.getValue() > 100) {
		    throw new Exception("Age must be between 11 and 100");
		}

		String gender = "Male";
		if (femaleRadioButton.isSelected()) {
		    gender = "Female";
		}

		int age = ageSpinner.getValue();

		String userID = database.generateId();

		database.insertUser(userID, username, age, email, gender, password, userType);

		alerts.showSuccessMessage("User Successfully Inserted");

		registerStage.close();

		LoginForm loginForm = new LoginForm();
		loginForm.start(new Stage());
	    } catch (Exception e) {
		alerts.showErrorMessage(e.getMessage());
		return;

	    }

	});

	VBox btns = new VBox(40);
	btns.getChildren().addAll(registerButton, loginLink);
	btns.setPadding(new Insets(20, 0, 20, 0));
	btns.setAlignment(Pos.TOP_CENTER);

	VBox layout = new VBox();
	layout.getChildren().addAll(grid, btns);
	layout.setAlignment(Pos.TOP_CENTER);

	VBox title = new VBox(titleLabel);
	title.setAlignment(Pos.CENTER);
	title.setPadding(new Insets(20, 0, 0, 0));

	borderPane.setTop(title);
	borderPane.setCenter(layout);

	Scene scene = new Scene(borderPane, 500, 500);

	registerStage.setScene(scene);
	registerStage.setResizable(false);
	registerStage.show();
    }

}
