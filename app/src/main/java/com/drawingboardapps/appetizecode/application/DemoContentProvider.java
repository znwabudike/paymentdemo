package com.drawingboardapps.appetizecode.application;

import com.drawingboardapps.transactionsdk.TransactionEngine;
import com.drawingboardapps.transactionsdk.TransactionRequest;

import java.util.ArrayList;


/**
 * Created by Zach on 4/23/2017.
 */

class DemoContentProvider {

    static DemoContentProvider instance;
    TransactionEngine engine;

    private DemoContentProvider(){
        engine = new TransactionEngine();
    }

    public static DemoContentProvider init(){
        if (instance != null){
            return instance;
        }else{
            instance = new DemoContentProvider();
            return instance;
        }
    }
    static class TransactionDriver{

        public static void startTransaction(){
            engine.startTransaction();
        }
    }

    static class RealmDriver{
        public static void saveTransactionOffline(TransactionRequest request){
            return;
        }

        public ArrayList<TransactionRequest> retrieveOfflineTransactionRequests(){
            return null;
        }
    }
}
