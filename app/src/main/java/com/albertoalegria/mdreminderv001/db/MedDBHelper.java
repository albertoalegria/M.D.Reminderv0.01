package com.albertoalegria.mdreminderv001.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.albertoalegria.mdreminderv001.model.Med;

import java.util.ArrayList;
import java.util.Arrays;

import static com.albertoalegria.mdreminderv001.utils.Constants.Db.*;

/**
 * Created by alberto on 03/03/17.
 */

public class MedDBHelper extends SQLiteOpenHelper implements DAO<Med> {

    private static final String TAG = "MedDAO";
    private final String query = "CREATE TABLE " + TB_NAME +
            "(" + MED_ID + " INTEGER PRIMARY KEY, " +
            MED_NAME     + " TEXT, " +
            MED_TYPE     + " INTEGER, " +
            MED_QUANTITY + " REAL, " +
            MED_HOURS    + " TEXT, " +
            MED_IMGPATH + " TEXT)";

    public MedDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP DATABASE IF EXISTS " + DB_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void create(Med med) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MED_NAME, med.getName());
        values.put(MED_TYPE, med.getType());
        values.put(MED_QUANTITY, med.getQuantity());
        values.put(MED_IMGPATH, med.getFirstImagePath());
        values.put(MED_HOURS, med.getHoursAsString());

        database.insert(TB_NAME, null, values);

    }

    @Override
    public Med retrieve(String name) {
        Med med = null;
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(TB_NAME, COLS, MED_NAME + "= ?", new String[]{name}, null, null, null);

        if (cursor.moveToFirst()) {
            med = convert(cursor);
        }

        cursor.close();
        return med;
    }

    @Override
    public void update(Med med) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MED_NAME, med.getName());
        values.put(MED_TYPE, med.getType());
        values.put(MED_QUANTITY, med.getQuantity());
        values.put(MED_IMGPATH, med.getFirstImagePath());
        values.put(MED_HOURS, med.getHoursAsString());
        database.update(TB_NAME, values, MED_NAME + "=?", new String[]{med.getName()});
    }

    @Override
    public void delete(String name) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TB_NAME, MED_NAME + "=?", new String[]{name});
    }

    @Override
    public ArrayList<Med> retrieveAll() {
        ArrayList<Med> allMeds = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TB_NAME, null);

        //DatabaseUtils.dumpCursor(cursor);

        while (cursor.moveToNext()) {
            //Log.d(TAG, "retrieveAll: retrieving y'all "+ cursor.getString(1));
            Med med = retrieve(cursor.getString(1));
            allMeds.add(med);
            //Log.d(TAG, "retrieveAll: Added " + med.getName());
        }

        cursor.close();

        return allMeds;
    }

    public int getMedId(Med med) {
        int id = -1;

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor =  database.rawQuery("SELECT id FROM " + TB_NAME + " WHERE " + MED_NAME + "='" + med.getName() + "'", null);

        while (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }

        cursor.close();
        return id;
    }

    private Med convert(Cursor cursor) {
        return new Med.Builder()
                .setName(cursor.getString(0))
                .setType(cursor.getInt(1))
                .setQuantity(cursor.getDouble(2))
                .setHours(stringToArray(cursor.getString(3)))
                .setFirstImagePath(cursor.getString(4))
                .Build();
    }

    private ArrayList<String> stringToArray(String hours) {
        return new ArrayList<>(Arrays.asList(hours.split(",")));
    }


    public void printCols() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Log.d(TAG, "printCols: " + query);
        Cursor cursor = sqLiteDatabase.query(TB_NAME, null, null, null, null, null, null);

        for (String string : cursor.getColumnNames()) {
            Log.d(TAG, "printCols: " + string);
        }

        cursor.close();
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}
