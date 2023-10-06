package main;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;

public class MainForm {

    Database database = new Database();

    Alerts alerts = new Alerts();

    LoginData loginData = new LoginData();

    private Stage mainStage;

    private MenuBar menuBar;

    private Menu menu;
    private MenuItem logOutMenuItem;

    private Menu transactionMenu;
    private MenuItem buyProductMenuItem;
    private MenuItem viewTransactionMenuItem;

    private Menu manageMenu;
    private MenuItem manageProductMenuItem;
    private MenuItem manageProductTypeMenuItem;

    private Scene scene;

    public MainForm(String userRole) {

	menu = new Menu("User");
	logOutMenuItem = new MenuItem("Log Out");
	logOutMenuItem.setOnAction(event -> logOut());

	menu.getItems().add(logOutMenuItem);

	transactionMenu = new Menu("Transaction");
	buyProductMenuItem = new MenuItem("Buy Product");
	buyProductMenuItem.setOnAction(event -> openProductForm());
	viewTransactionMenuItem = new MenuItem("View Transaction");
	viewTransactionMenuItem.setOnAction(event -> openTransactionHistory());
	transactionMenu.getItems().addAll(buyProductMenuItem, viewTransactionMenuItem);

	manageMenu = new Menu("Manage");
	manageProductMenuItem = new MenuItem("Manage Product");
	manageProductMenuItem.setOnAction(event -> openManageProductForm());
	manageProductTypeMenuItem = new MenuItem("Manage Product Type");
	manageProductTypeMenuItem.setOnAction(event -> openManageProductTypeForm());
	manageMenu.getItems().addAll(manageProductMenuItem, manageProductTypeMenuItem);

	menuBar = new MenuBar();
	if (userRole.equals("User")) {
	    menuBar.getMenus().addAll(menu, transactionMenu);
	} else if (userRole.equals("Admin")) {
	    menuBar.getMenus().addAll(menu, manageMenu);
	}
    }

    void start(Stage mainStage) {

	this.mainStage = mainStage;

	BorderPane borderpane = new BorderPane();
	borderpane.setTop(menuBar);

	scene = new Scene(borderpane, 720, 720);
	mainStage.setTitle("Shoes Station");
	mainStage.setScene(scene);
	mainStage.setResizable(false);
	mainStage.show();
    }

    private void logOut() {

	mainStage.close();

	LoginForm loginForm = new LoginForm();
	loginForm.start(new Stage());
    }

    private void resetScene() {
	mainStage.setScene(scene);
    }

    @SuppressWarnings("unchecked")
    private void openProductForm() {

	resetScene();

	Window window = new Window("Buy Product");

	TableView<Product> productTable = new TableView<>();
	productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	TableColumn<Product, Integer> idColumn = new TableColumn<>("productId");
	idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
	TableColumn<Product, Integer> stockColumn = new TableColumn<>("productStock");
	stockColumn.setCellValueFactory(new PropertyValueFactory<>("productStock"));
	TableColumn<Product, String> nameColumn = new TableColumn<>("productName");
	nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
	TableColumn<Product, Integer> typeColumn = new TableColumn<>("productTypeId");
	typeColumn.setCellValueFactory(new PropertyValueFactory<>("productTypeId"));
	TableColumn<Product, Integer> priceColumn = new TableColumn<>("productPrice");
	priceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

	productTable.getColumns().addAll(idColumn, stockColumn, nameColumn, typeColumn, priceColumn);

	Label idLabel = new Label("Id");
	TextField idField = new TextField();
	idField.setEditable(false);
	Label nameLabel = new Label("Name");
	TextField nameField = new TextField();
	nameField.setEditable(false);
	Label stockLabel = new Label("Stock");
	TextField stockField = new TextField();
	stockField.setEditable(false);
	Label typeLabel = new Label("Type Id");
	TextField typeField = new TextField();
	typeField.setEditable(false);
	Label priceLabel = new Label("Price");
	TextField priceField = new TextField();
	priceField.setEditable(false);

	Label quantityLabel = new Label("Quantity");
	Spinner<Integer> quantitySpinner = new Spinner<>(0, 1000, 0);

	List<Product> products = database.getProductList();

	productTable.setItems(FXCollections.observableArrayList(products));

	productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue != null) {

		idField.setText(Integer.toString(newValue.getProductId()));
		nameField.setText(newValue.getProductName());
		stockField.setText(Integer.toString(newValue.getProductStock()));
		typeField.setText(Integer.toString(newValue.getProductTypeId()));
		priceField.setText(Integer.toString(newValue.getProductPrice()));
	    }
	});

	TableView<Cart> cartTable = new TableView<>();
	cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	TableColumn<Cart, Integer> cartIdColumn = new TableColumn<>("productId");
	cartIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
	TableColumn<Cart, String> cartUserIdColumn = new TableColumn<>("userId");
	cartUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
	TableColumn<Cart, String> cartProductNameColumn = new TableColumn<>("productName");
	cartProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
	TableColumn<Cart, String> cartProductTypeColumn = new TableColumn<>("productType");
	cartProductTypeColumn.setCellValueFactory(new PropertyValueFactory<>("productTypeName"));
	TableColumn<Cart, Integer> cartQuantityColumn = new TableColumn<>("Quantity");
	cartQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("productQuantity"));
	TableColumn<Cart, Integer> cartPriceColumn = new TableColumn<>("productPrice");
	cartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

	cartTable.getColumns().addAll(cartIdColumn, cartUserIdColumn, cartProductNameColumn, cartProductTypeColumn,
		cartQuantityColumn, cartPriceColumn);

	List<Cart> cart = database.getCart(database.getUserId(loginData.getUsername()));

	cartTable.getItems().addAll(cart);

	Button addButton = new Button("Add to Cart");
	addButton.setOnAction(event -> {

	    if (idField.getText().isEmpty()) {
		alerts.showErrorMessage("Must Select a Product First!");
		return;
	    }

	    int quantity = quantitySpinner.getValue();

	    if (quantity == 0) {
		alerts.showErrorMessage("Quantity must be more than 0");
		return;
	    }

	    int stock = database.getProductStock(Integer.parseInt(idField.getText()));

	    if (stock == 0) {
		alerts.showErrorMessage("Stock Empty!");
		return;
	    }

	    if (quantity > stock) {
		alerts.showErrorMessage("Quantity must not be more than stock!");
		return;
	    }

	    Cart item = null;
	    for (Cart cartItem : cart) {
		if (cartItem.getProductId() == Integer.parseInt(idField.getText())) {
		    item = cartItem;
		    break;
		}
	    }

	    if (item != null) {

		if (item.getProductQuantity() + quantity > stock) {
		    alerts.showErrorMessage("Quantity must not be more than stock!");
		    return;
		} else {

		    cart.remove(item);

		    item.setProductQuantity(item.getProductQuantity() + quantity);

		    cart.add(item);

		    cartTable.getItems().setAll(cart);

		    database.updateCart(quantity, item.getProductId(), database.getUserId(loginData.getUsername()));

		    idField.clear();
		    nameField.clear();
		    quantitySpinner.getValueFactory().setValue(0);
		    stockField.clear();
		    typeField.clear();
		    priceField.clear();

		    alerts.showSuccessMessage("Product quantity updated!");
		}
	    } else {

		item = new Cart(Integer.parseInt(idField.getText()), database.getUserId(loginData.getUsername()),
			nameField.getText(), database.getProductTypeName(Integer.valueOf(typeField.getText())),
			quantitySpinner.getValue(), Integer.parseInt(priceField.getText()));

		cart.add(item);

		cartTable.getItems().addAll(item);

		database.insertIntoCart(item.getProductId(), database.getUserId(loginData.getUsername()),
			item.getProductQuantity());

		idField.clear();
		nameField.clear();
		quantitySpinner.getValueFactory().setValue(0);
		stockField.clear();
		typeField.clear();
		priceField.clear();

		alerts.showSuccessMessage("Product added to cart!");
	    }
	});

	Button checkoutButton = new Button("Checkout");
	checkoutButton.setOnAction(event -> {

	    if (cart.isEmpty()) {
		alerts.showErrorMessage("Cart Empty!");
		return;

	    }

	    database.insertTransaction(database.getUserId(loginData.getUsername()), cart);

	    idField.clear();
	    nameField.clear();
	    quantitySpinner.getValueFactory().setValue(0);
	    stockField.clear();
	    typeField.clear();
	    priceField.clear();

	    cart.clear();
	    cartTable.setItems(FXCollections.observableArrayList(cart));

	    database.deleteCart(database.getUserId(loginData.getUsername()));

	    alerts.showSuccessMessage("Check Out success!");

	});
	BorderPane borderPane = new BorderPane();
	GridPane gridPane = new GridPane();

	gridPane.setHgap(20);
	gridPane.setVgap(20);

	gridPane.setAlignment(Pos.TOP_CENTER);

	gridPane.add(idLabel, 0, 0);
	gridPane.add(idField, 1, 0);

	gridPane.add(stockLabel, 2, 0);
	gridPane.add(stockField, 3, 0);

	gridPane.add(nameLabel, 0, 1);
	gridPane.add(nameField, 1, 1);

	gridPane.add(typeLabel, 2, 1);
	gridPane.add(typeField, 3, 1);

	gridPane.add(quantityLabel, 0, 2);
	gridPane.add(quantitySpinner, 1, 2);

	gridPane.add(priceLabel, 2, 2);
	gridPane.add(priceField, 3, 2);

	VBox layout = new VBox(40);
	layout.getChildren().addAll(productTable, gridPane, addButton, cartTable, checkoutButton);
	layout.setAlignment(Pos.TOP_CENTER);
	layout.setMinWidth(720);
	layout.setPrefHeight(720);
	borderPane.setTop(layout);
	window.getRightIcons().add(new CloseIcon(window));
	window.getLeftIcons().add(new MinimizeIcon(window));
	window.setContentPane(borderPane);

	HBox hBoxWindow = new HBox();

	hBoxWindow.getChildren().add(window);

	VBox vboxMenuAndWindow = new VBox(mainStage.getScene().getRoot(), hBoxWindow);

	mainStage.setScene(new Scene(vboxMenuAndWindow));

    }

    @SuppressWarnings("unchecked")
    private void openTransactionHistory() {

	resetScene();

	Window window = new Window("Transaction History");

	TableView<HeaderTransaction> transactionTable = new TableView<>();
	transactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	TableColumn<HeaderTransaction, Integer> headerIdColumn = new TableColumn<>("transactionId");
	headerIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
	TableColumn<HeaderTransaction, String> timeColumn = new TableColumn<>("transactionDate");
	timeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));

	transactionTable.getColumns().addAll(headerIdColumn, timeColumn);

	TableView<DetailTransaction> detailTransactionTable = new TableView<>();
	detailTransactionTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	TableColumn<DetailTransaction, Integer> detailIdColumn = new TableColumn<>("transactionId");
	detailIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
	TableColumn<DetailTransaction, Integer> productColumn = new TableColumn<>("productId");
	productColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
	TableColumn<DetailTransaction, Integer> quantityColumn = new TableColumn<>("Quantity");
	quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

	detailTransactionTable.getColumns().addAll(detailIdColumn, productColumn, quantityColumn);

	List<HeaderTransaction> transaction = database
		.getHeaderTransactionList(database.getUserId(loginData.getUsername()));

	transactionTable.setItems(FXCollections.observableArrayList(transaction));

	Label selectedId = new Label(" ");

	transactionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue != null) {
		selectedId.setText("Current Selected Id : " + Integer.toString(newValue.getTransactionId()));

		List<DetailTransaction> details = database.getDetailTransactionList(newValue.getTransactionId());

		detailTransactionTable.setItems(FXCollections.observableArrayList(details));
	    }
	});

	BorderPane borderPane = new BorderPane();

	VBox layout = new VBox(40);
	layout.getChildren().addAll(transactionTable, selectedId, detailTransactionTable);
	layout.setMinWidth(720);
	layout.setPrefHeight(720);

	borderPane.setTop(layout);

	window.getRightIcons().add(new CloseIcon(window));
	window.getLeftIcons().add(new MinimizeIcon(window));
	window.getContentPane().getChildren().add(borderPane);

	HBox hBoxWindow = new HBox();

	hBoxWindow.getChildren().add(window);

	VBox vboxMenuAndWindow = new VBox(mainStage.getScene().getRoot(), hBoxWindow);

	mainStage.setScene(new Scene(vboxMenuAndWindow));

    }

    @SuppressWarnings("unchecked")
    private void openManageProductForm() {

	resetScene();

	Window window = new Window("Manage Product");

	TableView<Product> productTable = new TableView<>();
	productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	TableColumn<Product, Integer> idColumn = new TableColumn<>("productId");
	idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
	TableColumn<Product, Integer> stockColumn = new TableColumn<>("productStock");
	stockColumn.setCellValueFactory(new PropertyValueFactory<>("productStock"));
	TableColumn<Product, String> nameColumn = new TableColumn<>("productName");
	nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
	TableColumn<Product, String> typeColumn = new TableColumn<>("productTypeName");
	typeColumn.setCellValueFactory(cellData -> {

	    int productTypeId = cellData.getValue().getProductTypeId();

	    String productTypeName = database.getProductTypeName(productTypeId);

	    return new SimpleStringProperty(productTypeName);
	});
	TableColumn<Product, Integer> priceColumn = new TableColumn<>("productPrice");
	priceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));

	productTable.getColumns().addAll(idColumn, stockColumn, nameColumn, typeColumn, priceColumn);

	Label nameLabel = new Label("Name");
	TextField nameField = new TextField();
	Label stockLabel = new Label("Add/New Stock");
	Spinner<Integer> stockSpinner = new Spinner<>(0, 1000, 0);

	Label newTypeLabel = new Label("New Type");
	ComboBox<String> newTypeBox = new ComboBox<>();

	List<ProductType> productTypes = database.getProductTypeList();

	List<Product> products = database.getProductList();

	productTable.setItems(FXCollections.observableArrayList(products));

	for (ProductType type : productTypes) {
	    newTypeBox.getItems().add(type.getProductTypeName());
	}

	Label priceLabel = new Label("Price");
	TextField priceField = new TextField();

	productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

	    if (newValue != null) {
		nameField.setText(newValue.getProductName());
		newTypeBox.getSelectionModel().select(database.getProductTypeName(newValue.getProductTypeId()));
		priceField.setText(String.valueOf(newValue.getProductPrice()));
	    }
	});

	Button removeButton = new Button("Remove");
	Button updateButton = new Button("Update");
	Button insertButton = new Button("Insert");

	removeButton.setOnAction(event -> {

	    Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
	    if (selectedProduct == null) {
		alerts.showErrorMessage("Must Select a Product First!");
		return;
	    }

	    database.deleteProduct(selectedProduct.getProductId());

	    productTable.getItems().remove(selectedProduct);

	    productTable.refresh();

	    nameField.clear();
	    stockSpinner.getValueFactory().setValue(0);
	    newTypeBox.getSelectionModel().clearSelection();
	    priceField.clear();
	    productTable.getSelectionModel().clearSelection();

	    alerts.showSuccessMessage("Delete Success!");

	});

	updateButton.setOnAction(event -> {

	    Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
	    if (selectedProduct == null) {
		alerts.showErrorMessage("Must Select a Product First!");
		return;
	    }
	    if (nameField.getText().isEmpty()) {
		alerts.showErrorMessage("Product name must not be empty!");
		return;
	    }
	    if (newTypeBox.getValue() == null) {
		alerts.showErrorMessage("Must select a product type!");
		return;
	    }
	    int price;
	    try {
		price = Integer.parseInt(priceField.getText());
		if (price <= 0) {
		    alerts.showErrorMessage("Product price must be greater than 0.");
		    return;
		}
	    } catch (NumberFormatException e) {
		alerts.showErrorMessage("Product price must be a number.");
		return;
	    }

	    selectedProduct.setProductName(nameField.getText());
	    selectedProduct.setProductTypeId(database.getProductTypeId(newTypeBox.getValue()));
	    selectedProduct.setProductPrice(price);
	    selectedProduct.setProductStock(selectedProduct.getProductStock() + stockSpinner.getValue());
	    database.updateProduct(selectedProduct);

	    productTable.refresh();

	    nameField.clear();
	    stockSpinner.getValueFactory().setValue(0);
	    newTypeBox.getSelectionModel().clearSelection();
	    priceField.clear();
	    productTable.getSelectionModel().clearSelection();

	    alerts.showSuccessMessage("Product Updated!");
	});

	insertButton.setOnAction(event -> {

	    if (nameField.getText().isEmpty()) {
		alerts.showErrorMessage("Product name must not be empty!");
		return;
	    }

	    if (newTypeBox.getValue() == null) {
		alerts.showErrorMessage("Must select a product type!");
		return;
	    }
	    if (priceField.getText().isEmpty()) {
		alerts.showErrorMessage("Product price must not be empty!");
		return;
	    }
	    if (stockSpinner.getValue() == 0) {
		alerts.showErrorMessage("Stock must not be empty!");
		return;
	    }

	    int price;
	    try {
		price = Integer.parseInt(priceField.getText());
		if (price <= 0) {
		    alerts.showErrorMessage("Product price must be greater than 0!");
		    return;
		}
	    } catch (NumberFormatException e) {
		alerts.showErrorMessage("Product price must be a number!");
		return;
	    }

	    Product newProduct = new Product(0, "", 0, 0, 0);
	    newProduct.setProductName(nameField.getText());
	    newProduct.setProductTypeId(database.getProductTypeId(newTypeBox.getValue()));
	    newProduct.setProductPrice(Integer.parseInt(priceField.getText()));
	    newProduct.setProductStock(stockSpinner.getValue());
	    database.insertProduct(newProduct);

	    productTable.setItems(FXCollections.observableArrayList(database.getProductList()));

	    productTable.refresh();

	    nameField.clear();
	    stockSpinner.getValueFactory().setValue(0);
	    newTypeBox.getSelectionModel().clearSelection();
	    priceField.clear();

	    alerts.showSuccessMessage("Product Insert Success!");

	});

	BorderPane borderPane = new BorderPane();
	GridPane gridPane = new GridPane();

	gridPane.setHgap(20);
	gridPane.setVgap(20);

	gridPane.setAlignment(Pos.TOP_CENTER);

	gridPane.add(nameLabel, 0, 0);
	gridPane.add(nameField, 1, 0);

	gridPane.add(stockLabel, 0, 1);
	gridPane.add(stockSpinner, 1, 1);

	gridPane.add(newTypeLabel, 0, 2);
	gridPane.add(newTypeBox, 1, 2);

	gridPane.add(priceLabel, 0, 3);
	gridPane.add(priceField, 1, 3);

	HBox buttonLayout = new HBox(40);
	buttonLayout.getChildren().addAll(removeButton, updateButton, insertButton);
	buttonLayout.setAlignment(Pos.TOP_CENTER);

	VBox layout = new VBox(40);
	layout.getChildren().addAll(productTable, gridPane, buttonLayout);
	layout.setAlignment(Pos.TOP_CENTER);
	layout.setMinWidth(720);
	layout.setPrefHeight(720);

	borderPane.setTop(layout);
	window.getRightIcons().add(new CloseIcon(window));
	window.getLeftIcons().add(new MinimizeIcon(window));
	window.getContentPane().getChildren().add(borderPane);

	HBox hBoxWindow = new HBox();

	hBoxWindow.getChildren().add(window);

	VBox vboxMenuAndWindow = new VBox(mainStage.getScene().getRoot(), hBoxWindow);

	mainStage.setScene(new Scene(vboxMenuAndWindow));

    }

    @SuppressWarnings("unchecked")
    private void openManageProductTypeForm() {

	resetScene();

	Window window = new Window("Manage Product Type");

	Label nameLabel = new Label("Name");
	TextField nameField = new TextField();

	TableView<ProductType> productTypeTable = new TableView<>();
	productTypeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	TableColumn<ProductType, Integer> idColumn = new TableColumn<>("productTypeId");
	idColumn.setCellValueFactory(new PropertyValueFactory<>("productTypeId"));
	TableColumn<ProductType, String> nameColumn = new TableColumn<>("productTypeName");
	nameColumn.setCellValueFactory(new PropertyValueFactory<>("productTypeName"));

	productTypeTable.getColumns().addAll(idColumn, nameColumn);

	List<ProductType> productTypes = database.getProductTypeList();
	productTypeTable.setItems(FXCollections.observableArrayList(productTypes));

	productTypeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue != null) {
		nameField.setText(newValue.getProductTypeName());
	    }
	});

	Button updateButton = new Button("Update");
	Button removeButton = new Button("Remove");
	Button insertButton = new Button("Insert");

	updateButton.setOnAction(event -> {

	    ProductType selectedType = productTypeTable.getSelectionModel().getSelectedItem();
	    if (selectedType != null && !nameField.getText().isEmpty()) {

		selectedType.setProductTypeName(nameField.getText());
		database.updateProductType(selectedType);

		nameField.clear();

		productTypeTable.refresh();
		productTypeTable.getSelectionModel().clearSelection();

		alerts.showSuccessMessage("Product type updated");
	    } else if (selectedType == null) {
		alerts.showErrorMessage("Must select a product first!");
		return;

	    } else if (nameField.getText().isEmpty()) {
		alerts.showErrorMessage("Name must not be empty!");
		return;

	    }

	});
	removeButton.setOnAction(event -> {

	    ProductType selectedType = productTypeTable.getSelectionModel().getSelectedItem();
	    if (selectedType != null) {

		database.deleteProductType(selectedType.getProductTypeId());

		productTypeTable.setItems(FXCollections.observableArrayList(database.getProductTypeList()));
		productTypeTable.getSelectionModel().clearSelection();

		nameField.clear();

		alerts.showSuccessMessage("Delete Product Type Success!");

	    } else {
		alerts.showErrorMessage("Must select a product first!");
		return;

	    }
	});
	insertButton.setOnAction(event -> {

	    if (!nameField.getText().isEmpty()) {

		ProductType newType = new ProductType(0, nameField.getText());
		database.insertProductType(newType);

		nameField.clear();

		productTypeTable.setItems(FXCollections.observableArrayList(database.getProductTypeList()));
		alerts.showSuccessMessage("Insert Success!");

	    } else {
		alerts.showErrorMessage("Name must not be empty!");
		return;

	    }
	});

	HBox nameBox = new HBox(10);
	nameBox.getChildren().addAll(nameLabel, nameField);
	nameBox.setAlignment(Pos.TOP_CENTER);

	HBox buttonLayout = new HBox(40);
	buttonLayout.getChildren().addAll(updateButton, insertButton, removeButton);
	buttonLayout.setAlignment(Pos.TOP_CENTER);

	VBox layout = new VBox(40);
	layout.getChildren().addAll(productTypeTable, nameBox, buttonLayout);
	layout.setAlignment(Pos.TOP_CENTER);
	layout.setMinWidth(720);
	layout.setPrefHeight(720);

	BorderPane borderPane = new BorderPane();

	borderPane.setTop(layout);
	window.getRightIcons().add(new CloseIcon(window));
	window.getLeftIcons().add(new MinimizeIcon(window));
	window.getContentPane().getChildren().add(borderPane);

	HBox hBoxWindow = new HBox();

	hBoxWindow.getChildren().add(window);

	VBox vboxMenuAndWindow = new VBox(mainStage.getScene().getRoot(), hBoxWindow);

	mainStage.setScene(new Scene(vboxMenuAndWindow));

    }

}