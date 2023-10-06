package main;

public class Product extends AbstractProduct {
    public Product(int productId, String productName, int productStock, int productTypeId, int productPrice) {
	super(productId, productName, productStock, productTypeId, productPrice);
    }
}
