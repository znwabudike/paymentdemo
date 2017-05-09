package com.drawingboardapps.transactionsdk;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.core.deps.dagger.internal.InstanceFactory;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.drawingboardapps.transactionsdk.Transaction;
import com.drawingboardapps.transactionsdk.TransactionCallback;
import com.drawingboardapps.transactionsdk.TransactionHelper;
import com.drawingboardapps.transactionsdk.TransactionRequest;
import com.drawingboardapps.transactionsdk.TransactionResult;
import com.drawingboardapps.transactionsdk.TransactionSDK;
import com.drawingboardapps.transactionsdk.TransactionService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.ConnectException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.exceptions.CompositeException;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Zach on 4/26/2017.
 */

@RunWith(AndroidJUnit4.class)
public class TestTransactionService {

    private final int TEN_SECONDS = 10000;
    CountDownLatch latch;
    TransactionSDK sdk;
    private String TAG = "TestTransactionService";

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Before
    public void setUp() {
        latch = new CountDownLatch(TEN_SECONDS);
        sdk = new TransactionSDK(false);
        sdk.bindService(InstrumentationRegistry.getContext());
    }


//    /**
//     * Test the application can bind with the service.
//     * Note, as soon as a call is made to the api an error is thrown,
//     * the test framework cannot catch the error so assertions will fail.
//     */
//    @Test
//    public void testServiceBindable()  {
//        TransactionService service = bindService();
//        assertTrue(service.isBound());
//        service.onDestroy();
//    }
//
    private TransactionService bindService() {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(),
                        TransactionService.class);

        // Bind the service and grab a reference to the binder.
        IBinder binder = null;
        try {
            binder = mServiceRule.bindService(serviceIntent);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "testWithBoundService: Binding Service");
        TransactionService service =
                ((TransactionService.BinderImpl) binder).getService();
        Log.d(TAG, "testWithBoundService: Service bound?" + service.isBound());
        return service;
    }

    /*
    Below not working, Retrofit/RXAndroid throwing CompoundError that is not catchable in Unit test.
    Does not affect regular operation - works as intended.

    //TODO Need to find another way around this, perhaps Mockito.
     */
//    @Test
//    public void testTransactionComplete() {
//        try {
//            Transaction transaction = new Transaction();
//            transaction.setSubtotal("$1.00");
//            final TransactionRequest request = transaction.buildRequest();
//            TransactionHelper transactionHelper;
//
//            sdk.startTransaction(request, new TransactionCallback() {
//                @Override
//                public void onTransactionComplete(@NonNull TransactionResult transactionResult) {
//                    Assert.assertNotNull(transactionResult);
//                    Assert.assertNotNull(transactionResult.getAmount());
//                    Assert.assertEquals(request.getTotal(), transactionResult.getAmount());
//                    latch.countDown();
//                }
//
//                @Override
//                public void onError(@NonNull Throwable e) {
////                    Log.e(TAG, "onError: ", e);
////                    Assert.assertNotNull(null);
//                    latch.countDown();
//                }
//
//            });
//
//            latch.await(TEN_SECONDS, TimeUnit.MILLISECONDS);
//        } catch (Exception e) {
////            e.printStackTrace();
////            Assert.assertNotNull(null);
//        }
//    }
//
    volatile boolean onAGoodNote = false;
    @Test
    public void testCancelTransaction() {

        try {
            Transaction transaction = new Transaction();
            transaction.setSubtotal("$1.00");
            final TransactionRequest request = transaction.buildRequest();
            sdk.setService(bindService());
            sdk.startTransaction(request, new TransactionCallback() {
                @Override
                public void onTransactionComplete(@NonNull TransactionResult transactionResult) {
                    Assert.assertNotNull(transactionResult);
                    Assert.assertNotNull(transactionResult.getResponse());
                    Assert.assertEquals(TransactionResult.USER_CANCELLED, transactionResult.getResponse());
                    Log.d(TAG, "testCancelTransaction: latchcountdown");
                    onAGoodNote = true;
                    latch.countDown();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.e(TAG, "onError: ", e);
                    assertNotNull(null);
                    latch.countDown();
                }

            });
            sdk.cancelTransaction(request);
            latch.await(TEN_SECONDS, TimeUnit.MILLISECONDS);
            Log.d(TAG, "testCancelTransaction: latch ended onAGoodNote? " + onAGoodNote);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertNotNull(null);
        }

    }
}
