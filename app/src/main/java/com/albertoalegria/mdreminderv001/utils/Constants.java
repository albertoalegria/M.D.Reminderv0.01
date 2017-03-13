package com.albertoalegria.mdreminderv001.utils;

/**
 * Created by alberto on 04/03/17.
 */

public class Constants {
    public static class Db {
        public static final String DB_NAME = "db_meds.db";
        public static final String TB_NAME = "meds";
        public static final String MED_ID = "id";
        public static final String MED_NAME = "name";
        public static final String MED_TYPE = "type";
        public static final String MED_QUANTITY = "quantity";
        public static final String MED_HOURS = "hours";
        public static final String MED_IMGPATH = "imgpath";
        //public static final String MED_IMG2PATH = "imgpath2";
        //public static final String[] COLS = {MED_NAME, MED_TYPE, MED_QUANTITY, MED_HOURS, MED_IMG1PATH, MED_IMG2PATH};
        public static final String[] COLS = {MED_NAME, MED_TYPE, MED_QUANTITY, MED_HOURS, MED_IMGPATH};
    }

    public static class Types {
        public static final int PILL = 0x00;
        public static final int SYRUP = 0x01;
        public static final int INJECTION = 0x02;
    }

}
