package main;

public abstract class AbstractProduct {
    private int productId;
    private String productName;
    private int productStock;
    private int productTypeId;
    private int productPrice;

    public AbstractProduct(int productId, String productName, int productStock, int productTypeId, int productPrice) {
	this.productId = productId;
	this.productName = productName;
	this.productStock = productStock;
	this.productTypeId = productTypeId;
	this.productPrice = productPrice;
    }

    public int getProductId() {
	return productId;
    }

    public void setProductId(int productId) {
	this.productId = productId;
    }

    public void setProductName(String productName) {
	this.productName = productName;
    }

    public void setProductStock(int productStock) {
	this.productStock = productStock;
    }

    public void setProductTypeId(int productTypeId) {
	this.productTypeId = productTypeId;
    }

    public void setProductPrice(int productPrice) {
	this.productPrice = productPrice;
    }

    public String getProductName() {
	return productName;
    }

    public int getProductStock() {
	return productStock;
    }

    public int getProductTypeId() {
	return productTypeId;
    }

    public int getProductPrice() {
	return productPrice;
    }
}