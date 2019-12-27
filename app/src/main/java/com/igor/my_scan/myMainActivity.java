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
import com.igor.my_scan.data.CodesContract;
import com.igor.my_scan.data.CodesDbHelper;


public class myMainActivity extends Activity {

    private Button buttonBarCodeScan;
    private TextView textMsgView;
    private TextView textCodeInfoView;
    private CodesDbHelper mDbHelper;

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

        mDbHelper = new CodesDbHelper(this);
    }

    public void displayMessage(final String msg) {
//        Toast.makeText(getApplicationContext(), "Scan started!!", Toast.LENGTH_LONG).show();
        textMsgView.setText(msg);
    }

    public void showCodeInfo(final String code) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Зададим условие для выборки - список столбцов
        String[] projection = {
                CodesContract.CodesEntry._ID,
                CodesContract.CodesEntry.COLUMN_CODE,
                CodesContract.CodesEntry.COLUMN_INFO
        };

        // Делаем запрос
        /*
        Cursor cursor = db.query(
                CodesContract.CodesEntry.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order
         */

        String query_str = String.format("SELECT _id, code, info FROM %s WHERE %s = %s",
                CodesContract.CodesEntry.TABLE_NAME,
                CodesContract.CodesEntry.COLUMN_CODE,
                code);
        Cursor cursor = db.rawQuery(query_str,null);
        if (cursor.getCount() ==0) {
            textCodeInfoView.setText(String.format("Code %s is unknown!!",code));
        }
        else  if(cursor.getCount() >1) {
            textCodeInfoView.setText(String.format("Code %s is found more then once",code));
        }
        else { // 1 row
//            int id = cursor.getInt(cursor.getColumnIndex(CodesContract.CodesEntry._ID));
            int infoIdx = cursor.getColumnIndex(CodesContract.CodesEntry.COLUMN_INFO);
            cursor.moveToFirst();
            String info = cursor.getString(infoIdx);
            textCodeInfoView.setText(String.format("%s:\n%s",code,info));
        }
        cursor.close();
        /*

//        TextView displayTextView = (TextView) findViewById(R.id.text_view_info);

        try {

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(CodesContract.CodesEntry._ID);
            int codeColumnIndex = cursor.getColumnIndex(CodesContract.CodesEntry.COLUMN_CODE);
            int infoColumnIndex = cursor.getColumnIndex(CodesContract.CodesEntry.COLUMN_INFO);


            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(codeColumnIndex);
                String currentCity = cursor.getString(infoColumnIndex);
                // Выводим значения каждого столбца
                textCodeInfoView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentCity));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }
        */
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


