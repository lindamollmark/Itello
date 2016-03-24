package se.itello.paymentModel;

import java.math.BigDecimal;


/**
 * Created by Linda Möllmark
 * linda.mollmark@gmail.com
 * 0707-47 26 03
 */
public class PaymentLine {
    BigDecimal amount;
    String reference;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
