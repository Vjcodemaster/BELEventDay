package app_utility;



public class DataBaseHelper {

    //private variables
    private int _id;
    //private String _phone_number;
    private String _emp_id;
    private String _stall_name;
    private String _amount;
    private String _time;
    private String _scanned_id;

    // Empty constructor
    public DataBaseHelper(){

    }
    public DataBaseHelper(String _emp_id, String _scanned_id, String _stall_name, String _amount, String _time){
        this._emp_id = _emp_id;
        this._scanned_id = _scanned_id;
        this._stall_name = _stall_name;
        this._amount = _amount;
        this._time = _time;
    }

    public DataBaseHelper(String _emp_id, String _scanned_id){
        this._emp_id = _emp_id;
        this._scanned_id = _scanned_id;
    }

    // constructor
    /*public DataBaseHelper(String name, String _phone_number, String _email_id, String _designation){
        this._name = name;
        this._phone_number = _phone_number;
        this._email_id = _email_id;
        this._designation = _designation;
    }*/

    // constructor
    /*public DataBaseHelper(String _tag_id, String _major, String _minor, String _uuid, String _rssi, String _time,
                          String _date){
        this._tag_id = _tag_id;
        this._major = _major;
        this._minor = _minor;
        this._uuid = _uuid;
        this._rssi = _rssi;
        this._time = _time;
        this._date = _date;
    }

    // constructor
    public DataBaseHelper(String _last_seen_time, int _id){
        this._last_seen_time = _last_seen_time;
        this._id = _id;
    }*/

    // getting ID
    public int get_id(){
        return this._id;
    }

    // setting id
    public void set_id(int id){
        this._id = id;
    }

    // getting tagID
    public String get_emp_id(){
        return this._emp_id;
    }

    // setting tagID
    public void set_emp_id(String _emp_id){
        this._emp_id = _emp_id;
    }

    // getting name
    public String get_stall_name(){
        return this._stall_name;
    }

    // setting name
    public void set_stall_name(String _stall_name){
        this._stall_name = _stall_name;
    }

    // getting phone number
    public String get_amount(){
        return this._amount;
    }

    // setting phone number
    public void set_amount(String _amount){
        this._amount = _amount;
    }

    // getting emailID
    public String get_time(){
        return this._time;
    }

    // setting emailID
    public void set_time(String _time){
        this._time = _time;
    }


    public String get_scanned_id(){
        return this._scanned_id;
    }

    // setting emailID
    public void set_scanned_id(String _scanned_id){
        this._scanned_id = _scanned_id;
    }
}
