package com.mmuhamadamirzaidi.sellynapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mmuhamadamirzaidi.sellynapp.Model.OrderRequest;
import com.mmuhamadamirzaidi.sellynapp.Model.User;

public class Common {

    public static User currentUser;
    public static OrderRequest currentRequest;

    public static String cart_sub_total_global = "";
    public static String cart_delivery_charge_global = "";
    public static String cart_others_charge_global = "";
    public static String cart_grand_total_global = "";
    public static String cart_discount_global = "";

    public static final String USER_PHONE_KEY = "User";
    public static final String USER_PASSWORD_KEY = "Password";

    public static final String DELETE_CART = "Delete";

    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i=0; i<info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

}
