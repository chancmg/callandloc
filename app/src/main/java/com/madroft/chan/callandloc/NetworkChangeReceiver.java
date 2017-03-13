package com.madroft.chan.callandloc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by chan on 3/7/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String PREFERENCE_NAME = "MyPreferenceFileName";

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn =  (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
      //  Log.d(" reciever network info:",networkInfo.toString());
        SharedPreferences settings = context.getSharedPreferences(
                PREFERENCE_NAME, Activity.MODE_PRIVATE);
        boolean needupload=settings.getBoolean("needupload",true);
        Log.d(" reciever needupload:",Boolean.toString(needupload));
        if(networkInfo!=null && networkInfo.isConnected()&& needupload)
        {
                Log.d(Constants.TAG,"net connected");
                context.startService(new Intent(context,uploadservice.class));
        }
    }
}
