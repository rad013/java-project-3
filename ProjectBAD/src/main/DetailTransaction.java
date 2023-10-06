package main;

public class DetailTransaction extends AbstractTransaction {
    private int productId;
    private int quantity;

    public DetailTransaction(int transactionId, int productId, int quantity) {
	super(transactionId);
	this.productId = productId;
	this.quantity = quantity;
    }

    public int getProductId() {
	return productId;
    }

    public int getQuantity() {
	return quantity;
    }
}
