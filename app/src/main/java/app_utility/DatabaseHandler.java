package app_utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.DownloadManager.COLUMN_ID;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BEL_DATA";

    //DataBaseHelper table name
    private static final String TABLE_PERMANENT = "TABLE_PERMANENT";

    //DataBaseHelper table name (2nd table for all tab)
    private static final String TABLE_TEMPORARY = "TABLE_TEMPORARY";

    // BEL_DATA Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_STALL_ID = "stall_id";
    private static final String KEY_EMP_ID = "emp_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TIME = "time";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    /*
    leaving gap between "CREATE TABLE" & TABLE_RECENT gives error watch out!
    Follow the below format
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String CREATE_RECENT_TABLE = "CREATE TABLE " + TABLE_RECENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_TAGID + " TEXT, "
                + KEY_NAME + " TEXT, "
                + KEY_DESIGNATION + " TEXT, "
                + KEY_NUMBER + " TEXT, "
                + KEY_EMAILID + " TEXT, "
                + KEY_DATE + " TEXT, "
                + KEY_TIME + " TEXT, "
                + KEY_LAST_SEEN_TIME + " TEXT, "
                + KEY_MAJOR + " TEXT, "
                + KEY_MINOR + " TEXT, "
                + KEY_UUID + " TEXT, "
                + KEY_RSSI + " TEXT)";*/

        String CREATE_TABLE_PERMANENT = "CREATE TABLE " + TABLE_PERMANENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_STALL_ID + " TEXT, "
                + KEY_EMP_ID + " TEXT, "
                + KEY_AMOUNT + " TEXT, "
                + KEY_TIME + " TEXT)";

        String CREATE_TABLE_TEMPORARY = "CREATE TABLE " + TABLE_PERMANENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_STALL_ID + " TEXT, "
                + KEY_EMP_ID + " TEXT, "
                + KEY_AMOUNT + " TEXT, "
                + KEY_TIME + " TEXT)";

        db.execSQL(CREATE_TABLE_TEMPORARY);
        db.execSQL(CREATE_TABLE_PERMANENT);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERMANENT);

        // Create tables again
        onCreate(db);
    }

    // Adding new data
    public void addData(DataBaseHelper dataBaseHelper) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_ID, dataBaseHelper.getID()); // Contact Name
        values.put(KEY_STALL_ID, dataBaseHelper.get_stall_name());
        values.put(KEY_EMP_ID, dataBaseHelper.get_emp_id()); // Contact Phone
        values.put(KEY_AMOUNT, dataBaseHelper.get_amount());
        values.put(KEY_TIME, dataBaseHelper.get_time());

        // Inserting Row
        //db.insert(TABLE_RECENT, null, values);
        db.insert(TABLE_TEMPORARY, null, values);
        db.insert(TABLE_PERMANENT, null, values);

        db.close(); // Closing database connection
    }

    public int getIdForStringTablePermanent(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERMANENT, new String[]{COLUMN_ID,
                }, KEY_STALL_ID + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }

    public int getIdForStringTableTemporary(String str) {
        int res;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TEMPORARY, new String[]{COLUMN_ID,
                }, KEY_STALL_ID + "=?",
                new String[]{str}, null, null, null, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            res = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        } else {
            res = -1;
        }
        if (cursor != null) {
            cursor.close();
        }
        return res;
    }

    //gets Name of index to check whether to perform update task in recyclerview or not
    public String getNameFromTablePermanent(int ID) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_PERMANENT, new String[]{KEY_EMP_ID,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_EMP_ID));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;

    }

    //gets Name of index to check whether to perform update task in recyclerview or not
    public String getNameFromTableTemporary(int ID) {
        Cursor cursor = null;
        String sName = "";
        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(TABLE_TEMPORARY, new String[]{KEY_EMP_ID,
                }, KEY_ID + "=?",
                new String[]{String.valueOf(ID)}, null, null, null, null);
        //cursor = db.rawQuery("SELECT TABLEALL FROM last_seen WHERE _id" +" = "+ID +" ", new String[] {KEY_ID + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            sName = cursor.getString(cursor.getColumnIndex(KEY_EMP_ID));
        } else {
            sName = "";
        }
        /*if(sName==null){
            return "";
        }*/
        cursor.close();
        return sName;
    }


    // Getting data
    public List<DataBaseHelper> getRowData(int ID) {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMANENT + " WHERE " + " _id " + " = " + ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            //do {
            DataBaseHelper dataBaseHelper = new DataBaseHelper();

            dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
            dataBaseHelper.set_stall_name(cursor.getString(1));
            dataBaseHelper.set_emp_id(cursor.getString(2));
            dataBaseHelper.set_amount(cursor.getString(3));
            dataBaseHelper.set_time(cursor.getString(4));
            // Adding data to list
            dataBaseHelperList.add(dataBaseHelper);
            //} while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    // Getting data
    public List<DataBaseHelper> getAllTemporaryData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TEMPORARY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_stall_name(cursor.getString(1));
                dataBaseHelper.set_emp_id(cursor.getString(2));
                dataBaseHelper.set_amount(cursor.getString(3));
                dataBaseHelper.set_time(cursor.getString(4));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }

    // Getting data
    public List<DataBaseHelper> getAllPermanentData() {
        List<DataBaseHelper> dataBaseHelperList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PERMANENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseHelper dataBaseHelper = new DataBaseHelper();

                dataBaseHelper.set_id(Integer.parseInt(cursor.getString(0)));
                dataBaseHelper.set_stall_name(cursor.getString(1));
                dataBaseHelper.set_emp_id(cursor.getString(2));
                dataBaseHelper.set_amount(cursor.getString(3));
                dataBaseHelper.set_time(cursor.getString(4));
                // Adding data to list
                dataBaseHelperList.add(dataBaseHelper);
            } while (cursor.moveToNext());
        }

        // return recent list
        return dataBaseHelperList;
    }



    // Updating single data
    public int updateMultipleDataList(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";


        ContentValues values = new ContentValues();

        values.put(KEY_STALL_ID, dataBaseHelper.get_stall_name());
        values.put(KEY_EMP_ID, dataBaseHelper.get_emp_id()); // Contact Phone
        values.put(KEY_AMOUNT, dataBaseHelper.get_amount());
        values.put(KEY_TIME, dataBaseHelper.get_time());
        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_TEMPORARY, values, "_id" + " = " + KEY_ID, null);
        /*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*/
    }

    // Updating single data in all tab
    public int updateMultipleDataListAllTab(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        /*values.put(KEY_NAME, dataBaseHelper.getName());
        values.put(KEY_NUMBER, dataBaseHelper.getName());
        values.put(KEY_EMAILID, dataBaseHelper.getName());
        values.put(KEY_DESIGNATION, dataBaseHelper.getName());*/

        values.put(KEY_STALL_ID, dataBaseHelper.get_stall_name());
        values.put(KEY_EMP_ID, dataBaseHelper.get_emp_id()); // Contact Phone
        values.put(KEY_AMOUNT, dataBaseHelper.get_amount());
        values.put(KEY_TIME, dataBaseHelper.get_time());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_PERMANENT, values, "_id" + " = " + KEY_ID, null);
        /*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*/
    }

    // Updating single data
    /*public int updateSingleDataList(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_LAST_SEEN_TIME, dataBaseHelper.getLastSeen());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_TEMPORARY, values, "_id" + " = " + KEY_ID, null);
        *//*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*//*
    }*/

    // Updating single data in all tab
    /*public int updateSingleDataListAllTab(DataBaseHelper dataBaseHelper, int KEY_ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String column = "last_seen";
        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, dataBaseHelper.getName());
        //values.put(KEY_NUMBER, dataBaseHelper.getPhoneNumber());
        values.put(KEY_LAST_SEEN_TIME, dataBaseHelper.getLastSeen());

        // updating row
        //return db.update(TABLE_RECENT, values, column + "last_seen", new String[] {String.valueOf(KEY_ID)});
        return db.update(TABLE_PERMANENT, values, "_id" + " = " + KEY_ID, null);
        *//*ContentValues data=new ContentValues();
        data.put("Field1","bob");
        DB.update(Tablename, data, "_id=" + id, null);*//*
    }*/

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM " + TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    // Deleting single data
    public void deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_RECENT, KEY_ID + " = ?", new String[] { String.valueOf(recent.getID()) });
        db.delete(TABLE_PERMANENT, KEY_ID + " = " + id, null);
        db.close();
    }

    // Getting recent Count
    public int getRecordsCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_PERMANENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);


        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /*public boolean CheckIsDataAlreadyInDBorNot(String TableName,String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_ALL + " where " + dbfield + "="
                + fieldValue;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount<=0){
            return false;
        }
        return true;
    }*/
}
