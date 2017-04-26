package com.drawingboardapps.transactionsdk;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by zach on 4/24/17.
 */

public class LineItem extends RealmObject{

    String id;
    String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
