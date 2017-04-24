package com.drawingboardapps.transactionsdk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ServiceCompat;
import android.widget.Toast;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by Zach on 4/23/2017.
 */

public class TransactionService extends Service {
    private Looper looper;
    private ServiceHandler handler;
    BinderImpl binder = new BinderImpl();

    public void startTransaction(TransactionRequest request) {

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                THREAD_PRIORITY_BACKGROUND);
        thread.start();
        looper = thread.getLooper();
        handler = new ServiceHandler(looper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Message msg = handler.obtainMessage();
        msg.arg1 = startId;
        handler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public class BinderImpl extends Binder {
        public TransactionService getService() {
            return TransactionService.this;
        }
    }
}
