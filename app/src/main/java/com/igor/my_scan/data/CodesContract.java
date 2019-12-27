package com.igor.my_scan.data;

import android.provider.BaseColumns;

public final class CodesContract {

    private CodesContract() {
    }

    public static final class CodesEntry implements BaseColumns {
        public final static String TABLE_NAME = "code_info";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_INFO = "info";
    }
}


