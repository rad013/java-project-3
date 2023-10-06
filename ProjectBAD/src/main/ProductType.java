package main;

public class ProductType extends AbstractProduct {
    private int productTypeId;
    private String productTypeName;

    public ProductType(int productTypeId, String productTypeName) {
	super(0, productTypeName, 0, 0, 0);
	this.productTypeId = productTypeId;
	this.productTypeName = productTypeName;
    }

    public String getProductTypeName() {
	return productTypeName;
    }

    public int getProductTypeId() {
	return productTypeId;
    }

    public void setProductTypeName(String productTypeName) {
	this.productTypeName = productTypeName;
    }
}