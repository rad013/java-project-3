package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HeaderTransaction extends AbstractTransaction {
    private LocalDateTime time;

    public HeaderTransaction(int transactionId, LocalDateTime time) {
	super(transactionId);
	this.time = time;
    }

    public LocalDateTime getTime() {
	return time;
    }

    public String getFormattedTime() {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	return getTime().format(formatter);
    }
}