package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Database {

    public Connection getConnection() {
	Connection connection = null;
	try {
	    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shoestation", "root", "");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return connection;
    }

    public String getUserType(String username) {
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery("SELECT userType FROM user WHERE userName='" + username + "'");

	    if (resultSet.next()) {

		return resultSet.getString("userType");

	    } else {
		return null;
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public boolean checkUsernamePassword(String username, String password) {
	boolean matchFound = false;
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery(
		    "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'");

	    matchFound = resultSet.next();

	    resultSet.close();
	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return matchFound;
    }

    public String generateId() {
	String id = null;
	try {
	    Random random = new Random();

	    id = "CU" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
	    Connection connection = getConnection();

	    Statement statement = getConnection().createStatement();

	    ResultSet resultSet = statement.executeQuery("SELECT userId FROM user");

	    List<String> existingIds = new ArrayList<>();
	    while (resultSet.next()) {
		existingIds.add(resultSet.getString("userId"));
	    }

	    while (existingIds.contains(id)) {
		id = "CU" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();

	    return id;

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return id;

    }

    public void insertUser(String userId, String username, int age, String email, String gender, String password,
	    String userType) {

	try {
	    Connection connection = getConnection();

	    Statement statement = getConnection().createStatement();

	    statement.executeUpdate(
		    "INSERT INTO user (userId, username, age, email, gender, password, userType) VALUES ('" + userId
			    + "', '" + username + "', '" + age + "', '" + email + "', '" + gender + "', '" + password
			    + "', '" + userType + "')");

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public List<Product> getProductList() {
	List<Product> products = new ArrayList<>();
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery("SELECT * FROM product");

	    while (resultSet.next()) {
		Product product = new Product(resultSet.getInt("productId"), resultSet.getString("productName"),
			resultSet.getInt("productStock"), resultSet.getInt("productTypeId"),
			resultSet.getInt("productPrice"));
		products.add(product);
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return products;
    }

    public String getUserId(String currentUsername) {
	String userId = null;

	try {
	    Connection connection = getConnection();
	    PreparedStatement statement = connection.prepareStatement("SELECT userId FROM user WHERE username = ?");
	    statement.setString(1, currentUsername);
	    ResultSet resultSet = statement.executeQuery();
	    if (resultSet.next()) {
		userId = resultSet.getString("userId");
	    }
	    resultSet.close();
	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return userId;
    }

    public int getProductStock(int productId) {
	int stock = 0;
	try {
	    Connection connection = getConnection();
	    PreparedStatement statement = connection
		    .prepareStatement("SELECT productStock FROM product WHERE productId = ?");
	    statement.setInt(1, productId);
	    ResultSet resultSet = statement.executeQuery();
	    stock = 0;
	    if (resultSet.next()) {
		stock = resultSet.getInt("productStock");
	    }
	    statement.close();
	    connection.close();
	    return stock;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return stock;
    }

    public int getLastTransactionId() {
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement
		    .executeQuery("SELECT transactionId FROM headertransaction ORDER BY transactionId DESC LIMIT 1");

	    if (resultSet.next()) {
		return resultSet.getInt("transactionId");
	    } else {
		return 0;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    public void insertTransaction(String userId, List<Cart> cart) {
	try {
	    Connection connection = getConnection();

	    int lastTransactionId = getLastTransactionId();
	    int transactionId = lastTransactionId + 1;

	    PreparedStatement headerStatement = connection.prepareStatement(
		    "INSERT INTO headertransaction (transactionId, userId, Time) VALUES (?, ?, NOW())");
	    headerStatement.setInt(1, transactionId);
	    headerStatement.setString(2, userId);
	    headerStatement.executeUpdate();

	    PreparedStatement detailStatement = connection.prepareStatement(
		    "INSERT INTO detailtransaction (transactionId, productId, Quantity) VALUES (?, ?, ?)");
	    for (Cart item : cart) {
		detailStatement.setInt(1, transactionId);
		detailStatement.setInt(2, item.getProductId());
		detailStatement.setInt(3, item.getProductQuantity());
		detailStatement.executeUpdate();
	    }

	    headerStatement.close();
	    detailStatement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public List<HeaderTransaction> getHeaderTransactionList(String userId) {
	List<HeaderTransaction> headerTransactions = new ArrayList<>();
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("SELECT transactionId, Time FROM headertransaction WHERE userId = ?");
	    statement.setString(1, userId);
	    ResultSet resultSet = statement.executeQuery();

	    while (resultSet.next()) {
		HeaderTransaction headerTransaction = new HeaderTransaction(resultSet.getInt("transactionId"),
			resultSet.getTimestamp("Time").toLocalDateTime());
		headerTransactions.add(headerTransaction);
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return headerTransactions;
    }

    public List<DetailTransaction> getDetailTransactionList(int transactionId) {
	List<DetailTransaction> detailTransactions = new ArrayList<>();
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("SELECT * FROM detailtransaction WHERE transactionId = ?");
	    statement.setInt(1, transactionId);
	    ResultSet resultSet = statement.executeQuery();

	    while (resultSet.next()) {
		DetailTransaction detailTransaction = new DetailTransaction(resultSet.getInt("transactionId"),
			resultSet.getInt("productId"), resultSet.getInt("Quantity"));
		detailTransactions.add(detailTransaction);
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return detailTransactions;
    }

    public List<ProductType> getProductTypeList() {
	List<ProductType> productTypes = new ArrayList<>();
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement.executeQuery("SELECT * FROM productType");

	    while (resultSet.next()) {
		ProductType productType = new ProductType(resultSet.getInt("productTypeId"),
			resultSet.getString("productTypeName"));
		productTypes.add(productType);
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return productTypes;
    }

    public void updateProduct(Product product) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection.prepareStatement(
		    "UPDATE product SET productStock = ?, productTypeId = ?, productPrice = ? WHERE productId = ?");
	    statement.setInt(1, product.getProductStock());
	    statement.setInt(2, product.getProductTypeId());
	    statement.setInt(3, product.getProductPrice());
	    statement.setInt(4, product.getProductId());
	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void deleteProduct(int productId) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection.prepareStatement("DELETE FROM product WHERE productId = ?");
	    statement.setInt(1, productId);
	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public int getLastProductId() {
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement
		    .executeQuery("SELECT productId FROM product ORDER BY productId DESC LIMIT 1");

	    if (resultSet.next()) {
		return resultSet.getInt("productId");
	    } else {
		return 0;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    public void insertProduct(Product newProduct) {
	try {
	    Connection connection = getConnection();
	    int productId = getLastProductId();

	    int newId = productId + 1;
	    PreparedStatement statement = connection.prepareStatement(
		    "INSERT INTO product (productId, productName, productStock, productTypeId, productPrice ) VALUES (?, ?, ?, ?, ?)");
	    statement.setInt(1, newId);
	    statement.setString(2, newProduct.getProductName());
	    statement.setInt(3, newProduct.getProductStock());
	    statement.setInt(4, newProduct.getProductTypeId());
	    statement.setInt(5, newProduct.getProductPrice());
	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public String getProductTypeName(int productTypeId) {
	String productTypeName = "";
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("SELECT productTypeName FROM producttype WHERE productTypeId = ?");
	    statement.setInt(1, productTypeId);
	    ResultSet resultSet = statement.executeQuery();

	    if (resultSet.next()) {
		productTypeName = resultSet.getString("productTypeName");
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return productTypeName;

    }

    public int getProductTypeId(String typeName) {
	int typeId = -1;

	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("SELECT productTypeId FROM producttype WHERE productTypeName = ?");
	    statement.setString(1, typeName);
	    ResultSet resultSet = statement.executeQuery();

	    if (resultSet.next()) {
		typeId = resultSet.getInt("productTypeId");
	    }

	    resultSet.close();
	    statement.close();
	    connection.close();

	    return typeId;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return typeId;

    }

    public void updateProductType(ProductType productType) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("UPDATE productType SET productTypeName = ? WHERE productTypeId = ?");
	    statement.setString(1, productType.getProductTypeName());
	    statement.setInt(2, productType.getProductTypeId());
	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void deleteProductType(int productTypeId) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("DELETE FROM producttype WHERE productTypeId = ?");
	    statement.setInt(1, productTypeId);

	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public int getLastProductTypeId() {
	try {
	    Connection connection = getConnection();

	    Statement statement = connection.createStatement();
	    ResultSet resultSet = statement
		    .executeQuery("SELECT productTypeId FROM productType ORDER BY productTypeId DESC LIMIT 1");

	    if (resultSet.next()) {
		return resultSet.getInt("productTypeId");
	    } else {
		return 0;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    public void insertProductType(ProductType newType) {
	try {
	    Connection connection = getConnection();

	    int productTypeId = getLastProductTypeId();

	    int newId = productTypeId + 1;

	    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO productType VALUES (?, ?)");
	    insertStatement.setInt(1, newId);
	    insertStatement.setString(2, newType.getProductTypeName());
	    insertStatement.executeUpdate();

	    insertStatement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void insertIntoCart(int productId, String userId, int quantity) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement insertStatement = connection
		    .prepareStatement("INSERT INTO cart (productId, userId, quantity) VALUES (?, ?, ?)");

	    insertStatement.setInt(1, productId);
	    insertStatement.setString(2, userId);
	    insertStatement.setInt(3, quantity);
	    insertStatement.executeUpdate();

	    insertStatement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public List<Cart> getCart(String userId) {
	List<Cart> cart = new ArrayList<>();

	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection.prepareStatement(
		    "SELECT c.productId, c.userId, c.quantity, p.productName, p.productTypeId, p.productPrice FROM cart c INNER JOIN product p ON c.productId = p.productId WHERE c.userId = ?");
	    statement.setString(1, userId);

	    ResultSet resultSet = statement.executeQuery();

	    while (resultSet.next()) {
		int productId = resultSet.getInt("productId");
		userId = resultSet.getString("userId");
		int quantity = resultSet.getInt("quantity");
		String productName = resultSet.getString("productName");
		int productTypeId = resultSet.getInt("productTypeId");
		int productPrice = resultSet.getInt("productPrice");

		String productType = getProductTypeName(productTypeId);

		Cart item = new Cart(productId, userId, productName, productType, quantity, productPrice);
		cart.add(item);
	    }

	    return cart;

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return cart;
    }

    public void updateCart(int quantity, int productId, String userId) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection
		    .prepareStatement("UPDATE cart SET quantity = quantity + ? WHERE productId = ? AND UserId = ?");
	    statement.setInt(1, quantity);
	    statement.setInt(2, productId);
	    statement.setString(3, userId);
	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void deleteCart(String userId) {
	try {
	    Connection connection = getConnection();

	    PreparedStatement statement = connection.prepareStatement("DELETE FROM cart WHERE userId = ?");
	    statement.setString(1, userId);

	    statement.executeUpdate();

	    statement.close();
	    connection.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

}
