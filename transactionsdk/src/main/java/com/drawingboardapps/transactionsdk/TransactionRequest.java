package com.drawingboardapps.transactionsdk;

import java.util.LinkedHashMap;

/**
 * Created by Zach on 4/23/2017.
 */

public class TransactionRequest {

    private String localTransId;
    private String subtotal;
    private String tax;
    private String total;
    //<item_key, price (ex 2@10.00)>
    private LinkedHashMap<String, String> lineItems;

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

    public LinkedHashMap<String, String> getLineItems() {
        return lineItems;
    }

    public void setLineItems(LinkedHashMap<String, String> lineItems) {
        this.lineItems = lineItems;
    }

}
