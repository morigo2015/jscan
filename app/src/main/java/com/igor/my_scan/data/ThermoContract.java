package com.igor.my_scan.data;

import android.provider.BaseColumns;

public final class ThermoContract {

    private ThermoContract() {
    }

    public static final class MarksEntry implements BaseColumns {
        public final static String TABLE_NAME = "Marks";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_MARK_ID = "mark_id";
        public final static String COLUMN_CODE = "code";
        public final static String COLUMN_EQUIP_ID = "equip_id";
        public final static String COLUMN_DESCRIPTION = "description";
    }
}
