package com.drawingboardapps.transactionsdk;

import android.content.ClipData;
import android.support.annotation.IntRange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import io.realm.RealmObject;

/**
 * Created by zach on 4/24/17.
 */

public class Transaction implements Serializable {

    BigDecimal subtotal = new BigDecimal(0.0);
    BigDecimal tax = new BigDecimal(0.0);
    BigDecimal discount = new BigDecimal(0.0);
    BigDecimal total = new BigDecimal(0.0);
    LinkedList<LineItem> lineitems = new LinkedList<>();
    private LinkedList<LineItem> lineItems;

    public TransactionRequest buildRequest() {
        calculateTotal();
        return new TransactionRequest(
                subtotal,
                tax,
                total,
                discount,
                lineitems,
                System.currentTimeMillis() + "");
    }

    public void addLineItem(LineItem item){
        subtotal = subtotal.add(new BigDecimal(item.price));
        lineitems.add(item);
    }

    private String getDisplayTotal(){
        BigDecimal tempTotal = subtotal.subtract(discount);
        tempTotal = tempTotal.add(tempTotal.multiply(tax));
        return tempTotal.setScale(2, RoundingMode.HALF_UP)+"";
    }

    private void calculateTotal() {
        subtotal = subtotal.subtract(discount);
        total = subtotal.add(subtotal.multiply(tax));
        total = total.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) throws Exception {
        subtotal = subtotal.replace("$","");
        if (subtotal == null || subtotal.length() == 0){
            throw new Exception("Invalid Amount");
        }
        this.subtotal = new BigDecimal(subtotal);
    }

    public TransactionRealm getRealmObject(){
        return new TransactionRealm();
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LinkedList<LineItem> getLineItems() {
        return lineItems;
    }

}
