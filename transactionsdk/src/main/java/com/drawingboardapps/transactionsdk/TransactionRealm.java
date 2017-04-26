package com.drawingboardapps.transactionsdk;

import java.util.LinkedList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by zach on 4/26/17.
 */

public class TransactionRealm extends RealmObject{
    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public RealmList<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(RealmList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    String subTotal;
    String taxAmt;
    String discountAmt;
    String totalAmt;
    RealmList<LineItem> lineItems = new RealmList<>();

    public TransactionRealm(Transaction transaction) {
        subTotal = transaction.getSubtotal().toString();
        taxAmt = transaction.getTax().toString();
        discountAmt = transaction.getDiscount().toString();
        totalAmt = transaction.getTotal().toString();
        lineItems.addAll(transaction.getLineItems());
    }

    public TransactionRealm(){

    }


}