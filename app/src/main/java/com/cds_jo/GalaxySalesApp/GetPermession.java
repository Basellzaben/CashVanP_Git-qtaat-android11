package com.cds_jo.GalaxySalesApp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

/**
 * Created by Hp on 14/06/2016.
 */
public class GetPermession {
    private Context context;




    public  boolean CheckAction( Context _context , String ScreenCode , int ActionCode ){
        Boolean Result = false;
        this.context = _context;
        SqlHandler  sqlHandler = new SqlHandler(context);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        final String Permession = sharedPreferences.getString("Permession", "0");

        if (Permession.equalsIgnoreCase("0")){
            Result=  true ;
        }else {

            String q = "Select ifnull(Permession,0) as Permession from ManPermession Where SCR_Code ='" + ScreenCode + "'   And SCR_Action ='" + ActionCode + "'";
            Cursor c = sqlHandler.selectQuery(q);

            if (c != null && c.getCount() > 0) {
                if (c.moveToFirst()) {
                    if (c.getString(c.getColumnIndex("Permession")).equalsIgnoreCase("1")) {
                        Result = true;
                    } else

                        Result = false;
                }
                c.close();
            }
        }
        return  Result ;
    }
}
