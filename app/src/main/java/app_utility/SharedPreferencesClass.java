package app_utility;


import android.content.Context;
import android.content.SharedPreferences;

import static app_utility.StaticReferenceClass.DEFAULT_ODOO_ID;


public class SharedPreferencesClass {

    // Context
    SharedPreferences sharedPreferences;
    private Context _context;

    // Shared_pref file name
    //private static final String PREF_NAME = "TagProPref";

    private static final String APP_PREFERENCES = "TagProPreferences";

    private static final int PRIVATE_MODE = 0;



    // 0 = stall;
    // 1 = admin

    // All Shared Preferences Keys
    // private static final String IS_LOGIN = "IsLoggedIn";


    // User name (make variable public to access from outside)
    private static final String IS_USER_FIRST_TIME = "IS_USER_FIRST_TIME";

    private static final String USER_TYPE = "USER_TYPE";

    private static final String IS_LOGGED_IN = "IS_LOGGED_IN";

    private static final String USER_ODOO_ID = "USER_ODOO_ID";

    private static final String USER_NAME = "USER_NAME";

    //private static final String KEY_TAG_ID = "tagID";
    SharedPreferences.Editor editor;

    // Constructor
    public SharedPreferencesClass(Context context) {
        this._context = context;

        sharedPreferences = _context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        //editor.apply();
    }

    /*
     * Create login session
     * */
    /*public void createLoginSession(String name, String tagID){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, false);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_TAG_ID, tagID);

        // commit changes
        editor.commit();
    }

    *//*
     * Get stored session data
     * *//*
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_TAG_ID, sharedPreferences.getString(KEY_TAG_ID, null));

        // return user
        return user;
    }

    *//*
     * Clear session details
     * *//*
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }*/

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    public void setUserLogStatus(boolean bValue, String sValue){
        /*SharedPreferences sharedPreferences = _context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();*/
        editor.putBoolean(IS_LOGGED_IN, bValue);
        editor.putString(USER_NAME, sValue);
        editor.apply();
    }

    public boolean getUserLogStatus(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUserName(){
        return sharedPreferences.getString(USER_NAME, null);
    }

    public void setUserOdooID(int userOdooID){
        editor.putInt(USER_ODOO_ID, userOdooID);
        editor.apply();
    }

    public int getUserOdooID(){
        return sharedPreferences.getInt(USER_ODOO_ID, DEFAULT_ODOO_ID);
    }

}