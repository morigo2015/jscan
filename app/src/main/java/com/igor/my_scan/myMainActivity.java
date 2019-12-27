package com.igor.my_scan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.igor.my_scan.data.ThermoContract;
import com.igor.my_scan.data.ThermoDbHelper;


public class myMainActivity extends Activity {

    private Button buttonBarCodeScan;
    private TextView textMsgView;
    private TextView textCodeInfoView;
    private ThermoDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        buttonBarCodeScan = findViewById(R.id.button_scan);
        textMsgView = findViewById(R.id.textMsgView);
        textCodeInfoView = findViewById(R.id.textCodeInfoView);

        //Scan Button
        buttonBarCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initiate scan with our custom scan activity
                new IntentIntegrator(myMainActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                displayMessage("Scan pressed!!!");
            }
        });

        mDbHelper = new ThermoDbHelper(this);
    }

    public void displayMessage(final String msg) {
//        Toast.makeText(getApplicationContext(), "Scan started!!", Toast.LENGTH_LONG).show();
        textMsgView.setText(msg);
    }

    public void showCodeInfo(final String code) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String query_str = String.format(
                "SELECT mark_id, code, equip_id, description FROM %s WHERE %s = \"%s\"",
                ThermoContract.MarksEntry.TABLE_NAME,
                ThermoContract.MarksEntry.COLUMN_CODE,
                code);
        Cursor cursor = db.rawQuery(query_str, null);
        if (cursor.getCount() == 0) {
            textCodeInfoView.setText(String.format("Code %s is unknown!!", code));
        } else if (cursor.getCount() > 1) {
            textCodeInfoView.setText(String.format("Code %s is found more then once", code));
        } else { // 1 row
//            int id = cursor.getInt(cursor.getColumnIndex(CodesContract.CodesEntry._ID));
            int descriptionIdx = cursor.getColumnIndex(ThermoContract.MarksEntry.COLUMN_DESCRIPTION);
            int equipIdIdx = cursor.getColumnIndex(ThermoContract.MarksEntry.COLUMN_EQUIP_ID);
            int markIDIdx = cursor.getColumnIndex(ThermoContract.MarksEntry.COLUMN_MARK_ID);
            cursor.moveToFirst();
            String description = cursor.getString(descriptionIdx);
            String equipId = cursor.getString(equipIdIdx);
            String markID = cursor.getString(markIDIdx);
            textCodeInfoView.setText(String.format("%s:\n  equip_id=%s\n  description=%s\n mark_id=%s",
                    code, equipId, description, markID));
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                displayMessage("Scan Cancelled");
            } else {
                String code=result.getContents();
                displayMessage(code);
                showCodeInfo(code);
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            displayMessage("Volume UP!!");
            buttonBarCodeScan.performClick();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}


