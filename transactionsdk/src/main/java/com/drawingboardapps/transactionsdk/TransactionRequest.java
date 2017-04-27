package com.drawingboardapps.transactionsdk;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created by Zach on 4/23/2017.
 */

public class TransactionRequest implements Serializable{

    private String discount;
    private String localTransId;
    private String subtotal;
    private String tax;
    private String total;
    private String timeMillis;

    //<item_key, price (ex 2@10.00)>
    private LinkedList<LineItem> lineItems;

    public TransactionRequest(){}

    public TransactionRequest(BigDecimal subtotal,
                              BigDecimal tax,
                              BigDecimal total,
                              BigDecimal discount,
                              LinkedList<LineItem> lineitems,
                              String timeMillis) {
        this.total = total.toString();
        this.subtotal = subtotal.toString();
        this.tax = tax.toString();
        this.discount = discount.toString();
        this.lineItems = lineitems;
    }

    public String getLocalTransId() {
        return localTransId;
    }

    public void setLocalTransId(String localTransId) {
        this.localTransId = localTransId;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public LinkedList<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(LinkedList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(String timeMillis) {
        this.timeMillis = timeMillis;
    }
}
