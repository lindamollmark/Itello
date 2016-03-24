package se.itello.paymentService;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.builders.NullBuilder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import se.itello.paymentModel.Payment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by Linda Möllmark
 * linda.mollmark@gmail.com
 * 0707-47 26 03
 */
public class PaymentServiceHandlerTest {

    private PaymentServiceHandler paymentServiceHandler;
    private BufferedReader readBet;
    private BufferedReader readin;
    List<String> payments;
    private SimpleDateFormat dateform = new SimpleDateFormat("yyyyMMdd");
    private Date date;

    @Before
    public void before() throws IOException, ParseException {
        paymentServiceHandler  = new PaymentServiceHandler();
        readBet = new BufferedReader(new FileReader("Exempelfil_betalningsservice.txt"));
        readin = new BufferedReader(new FileReader("Exempelfil_inbetalningstjansten.txt"));
        date = dateform.parse("20110315");
    }

    @Test
    public void shouldReadBetServFile() throws IOException {
        payments =  paymentServiceHandler.shouldReadFile(readBet);
        assertEquals(5, payments.size());
    }

    @Test
    public void shouldReadInbetFile() throws IOException {
        payments =  paymentServiceHandler.shouldReadFile(readin);
        assertEquals(5, payments.size());
    }

    @Test
    public void shouldSetBetServPayment() throws ParseException, IOException {
        payments =  paymentServiceHandler.shouldReadFile(readBet);
        Payment payment = paymentServiceHandler.setPaymentFromBetalningsservice(payments);

        assertEquals("O", payment.getOpeningType());
        assertEquals("5555 5555555555", payment.getAccountNumber());
        assertEquals("4711.17", payment.getTotalAmount().toString());
        assertEquals(4, payment.getNumberOfPaymentRows());
        assertEquals(date, payment.getPaymentDate());
        assertEquals("SEK", payment.getCurrency());
        assertEquals(4, payment.getPaymentLines().size());
    }
    @Test
    public void shouldSetInbetPayment() throws ParseException, IOException {
        payments =  paymentServiceHandler.shouldReadFile(readin);
        Payment payment = paymentServiceHandler.setPaymentFromInbetalningstjansten(payments);

        assertEquals("00", payment.getOpeningType());
        assertEquals("12341234567897", payment.getAccountNumber());
        assertEquals("1530000", payment.getTotalAmount().toString());
        assertEquals(3, payment.getNumberOfPaymentRows());
        assertEquals(3, payment.getPaymentLines().size());
    }

}