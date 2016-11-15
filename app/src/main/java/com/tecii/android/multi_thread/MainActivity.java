package com.tecii.android.multi_thread;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.karel.tecii.multi_thread.R;
import java.util.Random;

public class MainActivity extends Activity {

    ProgressBar bar1;
    ProgressBar bar2;
    TextView msgWorking;
    TextView msgReturned;
    ScrollView myScrollView;
    protected boolean isRunning = false;
    protected final int MAX_SEC = 30;
    protected int globalIntTest = 0;

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String returnedValue = (String) msg.obj;
            msgReturned.append("\n returned value: " + returnedValue);
            myScrollView.fullScroll(View.FOCUS_DOWN);
            bar1.incrementProgressBy(1);

            if (bar1.getProgress() == MAX_SEC) {
                msgReturned.append(" \nDone \n back thread has been stopped");
                isRunning = false;
            }

            if (bar1.getProgress() == bar1.getMax()) {
                msgWorking.setText("Done");
                bar1.setVisibility(View.INVISIBLE);
                bar2.setVisibility(View.INVISIBLE);
            } else {
                msgWorking.setText("Working..." + bar1.getProgress());
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);

        bar1 = (ProgressBar) findViewById(R.id.progress1);
        bar1.setProgress(0);
        bar1.setMax(MAX_SEC);
        bar2 = (ProgressBar) findViewById(R.id.progress2);
        msgWorking = (TextView) findViewById(R.id.txtWorkProgress);
        msgReturned = (TextView) findViewById(R.id.txtReturnedValues);
        myScrollView = (ScrollView) findViewById(R.id.myscroLLer);
        globalIntTest = 1;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < MAX_SEC && isRunning; i++) {
                        Thread.sleep(1000);
                        Random rnd = new Random();
                        int localData = (int) rnd.nextInt(101);
                        String data = "Data-" + getGlobalIntTest() + "" + localData;
                        IncreaseGlobalIntTest(1);
                        Message msg = handler.obtainMessage(1, (String) data);
                        if (isRunning) {
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Throwable t) {
                    isRunning = false;
                }
            }
        });
        isRunning = true;
        background.start();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        isRunning = false;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    public synchronized int getGlobalIntTest() {
        return globalIntTest;
    }

    public synchronized int IncreaseGlobalIntTest(int inc) {
        return globalIntTest += inc;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
