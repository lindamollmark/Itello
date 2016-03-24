package se.itello.paymentService;

import se.itello.paymentModel.Payment;
import se.itello.paymentModel.PaymentLine;
import se.itello.paymentReceiver.PaymentReceiver;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Linda Möllmark
 * linda.mollmark@gmail.com
 * 0707-47 26 03
 */

/**
 * Handles .txt-files with paymentrows.
 * Read, parse and finally send them to a paymentReceiver.
 */
public class PaymentServiceHandler {

    private PaymentReceiver paymentReceiver;
    private SimpleDateFormat dateform = new SimpleDateFormat("yyyyMMdd");
    private static final String POST_TYPE_O = "O";
    private static final String POST_TYPE_B = "B";
    private static final String POST_TYPE_ZERO = "0";
    private static final String POST_TYPE_THREE = "3";
    private static final String POST_TYPE_NINE = "9";
    private static final String FILE_FROM_INBET = "inbetalningstjansten";
    private static final String FILE_FROM_BETTJANST = "betalningsservice";

    /**
     * Called when a new file for payment is received
     * @param fileName The received file
     * @throws IOException throwed if there is something wrong with the file
     * @throws ParseException throwed if you cant parse the read rows from the file.
     */
    public void newFile(String fileName) throws IOException, ParseException {
        Payment payments = new Payment();
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        List<String> paymentRows =  shouldReadFile(file);
        if(fileName.contains(FILE_FROM_INBET)){
            payments = setPaymentFromInbetalningstjansten(paymentRows);
        }
        else if(fileName.contains(FILE_FROM_BETTJANST)){
            payments = setPaymentFromBetalningsservice(paymentRows);
        }
        runPayment(payments);
    }

    /**
     * Called when you will start a new payment bundle
     * @param payments The whole payment to run.
     */
    void runPayment(Payment payments) {
        paymentReceiver.startPaymentBundle(payments.getAccountNumber(), payments.getPaymentDate(), payments.getCurrency());
        for (PaymentLine paymentLine: payments.getPaymentLines()) {
            paymentReceiver.payment(paymentLine.getAmount(), paymentLine.getReference());
        }
        paymentReceiver.endPaymentBundle();
    }

    /**
     * When you received a file and need to read it line by line
     * @param file The file to read
     * @return An array with the parsed line
     * @throws IOException Throwed if there is something wrong with the file
     */
    List<String> shouldReadFile(BufferedReader file) throws IOException {
        List<String> payments = new ArrayList<>();
        while (true){
            String paymentLine = file.readLine();
            if(paymentLine == null){
                break;
            }
            payments.add(paymentLine);
        }
        file.close();
        return payments;
    }

    /**
     * Adds the paymentrows to a object.
     * @param paymentrows A list with all the paymentrows
     * @return A complete payment
     * @throws ParseException Thrown if its not possible to parse the rows
     */
    Payment setPaymentFromBetalningsservice(List<String> paymentrows) throws ParseException {
        Payment payment = new Payment();
        List<PaymentLine> paymentLines = new ArrayList<>();
        for (String line: paymentrows) {
            String firstLetter = String.valueOf(line.charAt(0));
            if(firstLetter.equals(POST_TYPE_O)){
                payment.setOpeningType(firstLetter);
                payment.setAccountNumber(line.substring(1,16));
                String kd = line.substring(16,30).trim().replaceAll(",",".");
                BigDecimal bd = new BigDecimal(kd);
                payment.setTotalAmount(bd);
                payment.setNumberOfPaymentRows(Integer.valueOf(line.substring(30,40).trim()));
                Date date = dateform.parse(line.substring(40,48));
                payment.setPaymentDate(date);
                payment.setCurrency(line.substring(48,51));
            }else if(firstLetter.equals(POST_TYPE_B)){
                PaymentLine paymentLine = new PaymentLine();
                String kd = line.substring(1,15).trim().replaceAll(",",".");
                BigDecimal bd = new BigDecimal(kd);
                paymentLine.setAmount(bd);
                paymentLine.setReference(line.substring(15,50).trim());
                paymentLines.add(paymentLine);
            }
        }
        payment.setPaymentLines(paymentLines);
        return payment;
    }

    /**
     * Adds the paymentrows to a object.
     * @param paymentrows A list with all the paymentrows
     * @return A complete payment
     * @throws ParseException Thrown if its not possible to parse the rows
     */
    Payment setPaymentFromInbetalningstjansten(List<String> paymentrows) throws ParseException {
        Payment payment = new Payment();
        List<PaymentLine> paymentLines = new ArrayList<>();
        for (String line: paymentrows) {
            String firstLetter = String.valueOf(line.charAt(0));
            if(firstLetter.equals(POST_TYPE_ZERO)){
                payment.setOpeningType(firstLetter+0);
                payment.setAccountNumber(line.substring(10,24));
            }
            else if(firstLetter.equals(POST_TYPE_THREE)){
                PaymentLine paymentLine = new PaymentLine();
                String kd = line.substring(2,22).trim().replaceAll(",",".");
                BigDecimal bd = new BigDecimal(kd);
                paymentLine.setAmount(bd);
                paymentLine.setReference(line.substring(40,65).trim());
                paymentLines.add(paymentLine);
            }
            else if(firstLetter.equals(POST_TYPE_NINE)){
                String amount = line.substring(2,22).trim().replaceAll(",",".");
                BigDecimal bd = new BigDecimal(amount);
                payment.setTotalAmount(bd);
                payment.setNumberOfPaymentRows(Integer.valueOf(line.substring(30,38).trim()));
            }
        }
        payment.setPaymentLines(paymentLines);
        return payment;
    }
}
