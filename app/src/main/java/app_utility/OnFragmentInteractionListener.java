package app_utility;

import java.util.LinkedHashMap;

public interface OnFragmentInteractionListener {
    void onResultReceived(String sMessage, int nCase, String sResult, LinkedHashMap<String, String> lhmFoodAndQuantity);

    //void onResultReceived(String sMessage, int nCase, double dLatitudeStart, double dLongitudeStart, double dLatitudeEnd, double dLongitudeEnd);

    //void onDistanceReceived(String sMessage, int nCase, String sDistance, String sTime, String ID);
}
