package main;

public class Cart extends AbstractProduct {
    private String userId;
    private String productTypeName;
    private int productQuantity;

    public Cart(int productId, String userId, String productName, String productTypeName, int productQuantity,
	    int productPrice) {
	super(productId, productName, 0, 0, productPrice);
	this.userId = userId;
	this.productTypeName = productTypeName;
	this.productQuantity = productQuantity;
    }

    @Override
    public String getProductName() {
	return super.getProductName();
    }

    public void setProductQuantity(int productQuantity) {
	this.productQuantity = productQuantity;
    }

    public String getProductTypeName() {
	return productTypeName;
    }

    @Override
    public int getProductPrice() {
	return super.getProductPrice();
    }

    @Override
    public int getProductId() {
	return super.getProductId();
    }

    public String getUserId() {
	return userId;
    }

    public int getProductQuantity() {
	return productQuantity;
    }
}
