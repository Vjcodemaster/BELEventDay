package app_utility;

import android.content.Context;
import android.os.AsyncTask;

import com.bel.antimatter.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static app_utility.OfflineTransferService.onAsyncInterfaceListener;
import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.NETWORK_ERROR_CODE;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.PORT_NO;
import static app_utility.StaticReferenceClass.SERVER_URL;
import static app_utility.StaticReferenceClass.USER_ID;

public class BELAsyncTask extends AsyncTask<String, Void, String> {

    private LinkedHashMap<String, ArrayList<String>> lhmProductsWithID = new LinkedHashMap<>();
    private CircularProgressBar circularProgressBar;
    //private Activity aActivity;
    //private OnAsyncTaskInterface onAsyncTaskInterface;
    private ArrayList<Integer> alPosition = new ArrayList<>();
    private Context context;
    private HashMap<String, Object> hmDataList = new HashMap<>();
    private int nOrderID = 191;
    private int odooID = StaticReferenceClass.DEFAULT_ODOO_ID;

    private int nTemporaryDBID;
    private ArrayList<String> alEmpID = new ArrayList<>();
    private ArrayList<String> alAmount = new ArrayList<>();
    private ArrayList<String> alTime = new ArrayList<>();
    //private ArrayList<String> alEmpID = new ArrayList<>();

    ArrayList<DataBaseHelper> alDBTemporaryData;
    DatabaseHandler db;

    public BELAsyncTask(Context context) {
        this.context = context;
    }

    public BELAsyncTask(Context context, ArrayList<DataBaseHelper> alDBTemporaryData, DatabaseHandler db) {
        this.context = context;
        this.alDBTemporaryData = alDBTemporaryData;
        this.db = db;
    }
    /*public BELAsyncTask(Activity aActivity, OnAsyncTaskInterface onAsyncTaskInterface,
                        HashMap<String, Object> hmDataList) {
        this.aActivity = aActivity;
        this.hmDataList = hmDataList;
        this.onAsyncTaskInterface = onAsyncTaskInterface;
    }*/

    private boolean isPresent = false;
    private Boolean isConnected = false;
    private int ERROR_CODE = 0;
    private String sMsgResult;
    private int type;
    private String sStallName;
    private int[] IDS = new int[2];
    String sEmpID;
    String sAmount;
    String sTime;
    String sScannedID;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //setProgressBar();
    }

    @Override
    protected String doInBackground(String... params) {
        type = Integer.parseInt(params[0]);
        switch (type) {
            case 1:
                loginTask();
                break;
            case 2:
                //createEmployeeID(params[1], params[2]);
                for (int i = 0; i < alDBTemporaryData.size(); i++) {
                        /*alEmpID.add(alDBTemporaryData.get(i).get_emp_id());
                        alAmount.add(alDBTemporaryData.get(i).get_amount());
                        alTime.add(alDBTemporaryData.get(i).get_time());*/
                    nTemporaryDBID = alDBTemporaryData.get(i).get_id();
                    sEmpID = alDBTemporaryData.get(i).get_emp_id();
                    sScannedID = alDBTemporaryData.get(i).get_scanned_id();
                        /*alAmount.add(alDBTemporaryData.get(i).get_amount());
                        alTime.add(alDBTemporaryData.get(i).get_time());*/
                    createEmployeeID(nTemporaryDBID, sEmpID, sScannedID);
                }
                //updateTask();
                break;
            case 3:
                placeOrder();
                break;
            case 4:
                readProductAndImageTask();
                //snapRoadTask(params[1]);
                //readProducts();
                break;
            case 5:
                sStallName = params[1];
                validateLogin();
                break;
            case 6:
                int ID = Integer.valueOf(params[1]);
                //if (alDBTemporaryData.size() >= 1) {
                for (int i = 0; i < alDBTemporaryData.size(); i++) {
                        /*alEmpID.add(alDBTemporaryData.get(i).get_emp_id());
                        alAmount.add(alDBTemporaryData.get(i).get_amount());
                        alTime.add(alDBTemporaryData.get(i).get_time());*/
                    nTemporaryDBID = alDBTemporaryData.get(i).get_id();
                    sEmpID = alDBTemporaryData.get(i).get_emp_id();
                    sScannedID = alDBTemporaryData.get(i).get_scanned_id();
                    sAmount = alDBTemporaryData.get(i).get_amount();
                    sTime = alDBTemporaryData.get(i).get_time();
                        /*alAmount.add(alDBTemporaryData.get(i).get_amount());
                        alTime.add(alDBTemporaryData.get(i).get_time());*/
                    createOne2Many(ID, sEmpID, sScannedID, sAmount, sTime, nTemporaryDBID);
                }
                //}

                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (ERROR_CODE != 0) {
            switch (ERROR_CODE) {
                case NETWORK_ERROR_CODE:
                    unableToConnectServer(ERROR_CODE);
                    break;
            }
            ERROR_CODE = 0;
            return;
        }
        switch (type) {
            case 2:
                AdminRegisterService.onAsyncInterfaceListener.onAsyncComplete("ODOO_ID_RETRIEVED", odooID, "");
                break;
            case 4:
                //onAsyncTaskInterface.onAsyncTaskComplete("READ_PRODUCTS", type, lhmProductsWithID, alPosition);
                break;
            /*case 5:

                if(isPresent){
                    LoginActivity.onAsyncInterfaceListener.onAsyncComplete("LOGIN_SUCCESS", type, "");
                    isPresent =false;
                }
                break;*/
            case 5:
                if (isPresent) {
                    onAsyncInterfaceListener.onAsyncComplete("ODOO_ID_RETRIEVED", odooID, "");
                    //LoginActivity.onAsyncInterfaceListener.onAsyncComplete("LOGIN_SUCCESS", type, "");
                    isPresent = false;
                }
                break;
            case 6:
                OfflineTransferService.onAsyncInterfaceListener.onAsyncComplete("ODOO_ID_RETRIEVED", odooID, "");
                break;

        }
        /*if (circularProgressBar != null && circularProgressBar.isShowing()) {
            circularProgressBar.dismiss();
        }*/
    }

    private void loginTask() {
        //if (isConnected) {
        try {
            isConnected = OdooConnect.testConnection(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            if (isConnected) {
                isConnected = true;
                //return true;
            } else {
                isConnected = false;
                sMsgResult = "Connection error";
            }
        } catch (Exception ex) {
            ERROR_CODE = NETWORK_ERROR_CODE;
            // Any other exception
            sMsgResult = "Error: " + ex;
        }
        // }
        //return isConnected;
    }

    private void validateLogin() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Object[] conditions = new Object[1];
            //conditions[0] = new Object[]{"id", "!=", "0099009"};
            conditions[0] = new Object[]{"partner_id", "=", sStallName};
            List<HashMap<String, Object>> stallData = oc.search_read("sale.order", new Object[]{conditions}, "name");
            if (stallData.size() == 1) {
                isPresent = true;
                odooID = Integer.valueOf(stallData.get(0).get("id").toString());
            }
            /*for (int i = 0; i < stallData.size(); ++i) {
                //int id = Integer.valueOf(data.get(i).get("id").toString());
                String sName = String.valueOf(stallData.get(i).get("name").toString());

                *//*if(sStallName.equals(sName)){
                    isPresent = true;

                    return;
                }*//*
                if (sStallName.equals(sName)) {
                    isPresent = true;
                    odooID = Integer.valueOf(stallData.get(i).get("id").toString());
                    return;
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void createOrder() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Integer createCustomer = oc.create("sale.order", new HashMap() {{
                put("partner_id", 562);
                //put("state", ORDER_STATE[0]);
            }});
            IDS[0] = createCustomer;
            //createOne2Many(createCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void createEmployeeID(final int dbID, final String sEmpID, final String sMapNumber) {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Integer createEmployee = oc.create("hr.employee", new HashMap() {{
                put("name", sEmpID);
                put("mobile_phone", sMapNumber);
                //put("state", ORDER_STATE[0]);
            }});
            IDS[0] = createEmployee;
            db.deleteEmployeeData(dbID);
            //createOne2Many(createCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createOne2Many(final int ID, final String sEmpID, final String sScannedID, final String sAmount, final String sTime, int dbID) {

        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
/*
            @SuppressWarnings("unchecked")
            Integer one2Many = oc.create("web.service.child", new HashMap() {{
                put("name", "Autochip");
                put("mobile", "4103246464");
                put("service_id", ID); //one to many
            }});*/

            //if (alOne2ManyModelNames.size() >= 1) {

            if (oc != null) {
                @SuppressWarnings("unchecked")
                //String[] sa = sScannedID.split(";");
                //final String ssScannedID = sa[1];
                Integer one2Many = oc.create("sale.order.line", new HashMap() {{
                    put("product_id", 1); //4
                    put("name", sScannedID + "," + sEmpID + "," + sTime);
                    put("price_unit", sAmount);
                    put("order_id", ID);
                }});
                IDS[1] = one2Many;
                    db.deleteData(dbID);
            }
            //}

        } catch (Exception e) {
            ERROR_CODE = NETWORK_ERROR_CODE;
            e.printStackTrace();
        }
        //return one2Many;
    }

    private void placeOrder() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Boolean idC = oc.write("sale.order", new Object[]{nOrderID}, hmDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readImageTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        Object[] conditions = new Object[2];
        conditions[0] = new Object[]{"res_model", "=", "product.template"};
        conditions[1] = new Object[]{"res_field", "=", "image_medium"};
        List<HashMap<String, Object>> data = oc.search_read("ir.attachment", new Object[]{conditions}, "id", "store_fname", "res_name");

        for (int i = 0; i < data.size(); ++i) {

        }
    }

    /*private void readProducts() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> data = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        for (int i = 0; i < data.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(data.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(data.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(data.get(i).get("list_price").toString());
            lhmProductsWithID.put(sName, alData);
        }
    }*/
    private void readProductAndImageTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> productsData = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        Object[] conditions = new Object[2];
        conditions[0] = new Object[]{"res_model", "=", "product.template"};
        conditions[1] = new Object[]{"res_field", "=", "image_medium"};
        List<HashMap<String, Object>> imageData = oc.search_read("ir.attachment", new Object[]{conditions},
                "id", "store_fname", "res_name");

        for (int i = 0; i < productsData.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(productsData.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(productsData.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(productsData.get(i).get("list_price").toString());
            for (int j = 0; j < imageData.size(); j++) {
                String base64 = imageData.get(j).get("store_fname").toString();
                if (imageData.get(j).get("res_name").toString().equals(sName)) {
                    alData.add(base64);
                    alPosition.add(i);
                    break;
                }
            }
            lhmProductsWithID.put(sName, alData);
        }
    }

    private void unableToConnectServer(int errorCode) {
        OfflineTransferService.onAsyncInterfaceListener.onAsyncComplete("SERVER_ERROR", errorCode, "");
    }

    private void setProgressBar() {
        circularProgressBar = new CircularProgressBar(context);
        circularProgressBar.setCanceledOnTouchOutside(false);
        circularProgressBar.setCancelable(false);
        circularProgressBar.show();
    }

}
