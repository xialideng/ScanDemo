package com.sf.scandemo.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.regex.Pattern;

public class MainActivity extends Activity {
    String SCANNER_ACTION = "com.android.server.scannerservice.broadcast";
    String SCANNER_DATA = "scannerdata";
    private EditText billNumberEdiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        billNumberEdiText = (EditText) findViewById(R.id.billNumber);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerScannerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(scannerReceiver);
    }

    private BroadcastReceiver scannerReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (SCANNER_ACTION.equals(intent.getAction())) {
                String barcode = removeAllWhiteSpace(intent.getExtras().getString(SCANNER_DATA));
                billNumberEdiText.setText(barcode);
            }
        }
    };

    public static String removeAllWhiteSpace(String text) {
        Pattern whiteSpacePattern = Pattern.compile("\\s+");
        return text == null ? null : whiteSpacePattern.matcher(text).replaceAll("");
    }

    private void registerScannerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCANNER_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(scannerReceiver, filter);
    }
}
