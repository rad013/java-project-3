package main;

public abstract class AbstractTransaction {
    private int transactionId;

    public AbstractTransaction(int transactionId) {
	this.transactionId = transactionId;
    }

    public int getTransactionId() {
	return transactionId;
    }
}
