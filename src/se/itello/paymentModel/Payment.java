package se.itello.paymentModel;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by Linda Möllmark
 * linda.mollmark@gmail.com
 * 0707-47 26 03
 */
public class Payment {
    String openingType;
    String accountNumber;
    BigDecimal totalAmount;
    int numberOfPaymentRows;
    Date paymentDate;
    String currency;
    List<PaymentLine> paymentLines;

    public String getOpeningType() {
        return openingType;
    }

    public void setOpeningType(String openingType) {
        this.openingType = openingType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getNumberOfPaymentRows() {
        return numberOfPaymentRows;
    }

    public void setNumberOfPaymentRows(int numberOfPaymentRows) {
        this.numberOfPaymentRows = numberOfPaymentRows;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<PaymentLine> getPaymentLines() {
        return paymentLines;
    }

    public void setPaymentLines(List<PaymentLine> paymentLines) {
        this.paymentLines = paymentLines;
    }
}
