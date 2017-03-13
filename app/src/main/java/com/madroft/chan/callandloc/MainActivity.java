package com.madroft.chan.callandloc;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static Resources res;
    private Context context;
    private static final int CATEGORY_DETAIL = 1;
    private static final int NO_MEMORY_CARD = 2;
    private static final int TERMS = 3;
    private static final String PREFERENCE_NAME = "MyPreferenceFileName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getBaseContext();

        SharedPreferences settings = this.getSharedPreferences(
                Constants.LISTEN_ENABLED, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", false);
        editor.commit();

        Intent myIntent = new Intent(context, RecordService.class);
        myIntent.putExtra("commandType",Constants.RECORDING_ENABLED);
        myIntent.putExtra("silentMode", false);
        context.startService(myIntent);

        Intent i=new Intent(context,uploadservice.class);
        context.startService(i);

    }
    /**
     * checks if an external memory card is available
     *
     * @return
     */
    public static int updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Constants.MEDIA_MOUNTED;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return Constants.MEDIA_MOUNTED_READ_ONLY;
        } else {
            return Constants.NO_MEDIA;
        }
    }
    private void setSharedPreferences(boolean silentMode) {
        SharedPreferences settings = this.getSharedPreferences(
                Constants.LISTEN_ENABLED, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("silentMode", silentMode);
        editor.commit();

        Intent myIntent = new Intent(context, RecordService.class);
        myIntent.putExtra("commandType",
                silentMode ? Constants.RECORDING_DISABLED
                        : Constants.RECORDING_ENABLED);
        myIntent.putExtra("silentMode", silentMode);
        context.startService(myIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        
    }
}
