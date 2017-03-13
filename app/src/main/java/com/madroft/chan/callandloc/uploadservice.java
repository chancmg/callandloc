package com.madroft.chan.callandloc;

import android.app.Activity;
import android.app.Service;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.adeel.library.easyFTP;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;

import static com.madroft.chan.callandloc.FileHelper.getFilePath;

/**
 * Created by chan on 3/8/2017.
 */

public class uploadservice extends Service {
    private static final String PREFERENCE_NAME = "MyPreferenceFileName";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ConnectivityManager conn =  (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();

        if(networkInfo!=null)
        {
            Toast toast = Toast.makeText(this,
                    "started uploading",
                    Toast.LENGTH_SHORT);
            toast.show();
            new uploadTask().execute();
        }
        else
        {
            Toast toast = Toast.makeText(this,
                    "No network,upload will start once network connected",
                    Toast.LENGTH_SHORT);
            toast.show();
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    class uploadTask extends AsyncTask<String, Void, String> {
        SharedPreferences settings=getSharedPreferences(PREFERENCE_NAME,Activity.MODE_PRIVATE);
        private int uploadcount=settings.getInt("count",1);
        @Override
        protected String doInBackground(String... params) {
            /*try {
                easyFTP ftp = new easyFTP();
                ftp.connect("ftp.madroft.16mb.com", "u345347048.chan", "lkOncDXl0UtF");
                //  boolean status = false;
                 //  status = ftp.setWorkingDirectory("Files/Uploads/calls"); // if User say provided any Destination then Set it , otherwise
                // Upload will be stored on Default /root level on server

                ftp.uploadFile(getFilePath() + "/" + "temp.zip");
                return new String("Upload Successful");
            } catch (Exception e) {
                String t = "Failure : " + e.getLocalizedMessage();
                return t;
            }*/

            FTPClient con = null;
            String status="no success";


            try
            {
                /*con = new FTPClient();
                con.connect(InetAddress.getByName("128.199.179.78"));

                if (con.login("tevatel", "tevatel"))
                {*/
                con = new FTPClient();
                con.connect(InetAddress.getByName("ftp.madroft.16mb.com"));

                if (con.login("u345347048.chan", "lkOncDXl0UtF"))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    String data = getFilePath() + "/" + "temp.zip";
                    Log.d("uploading","true");
                    FileInputStream in = new FileInputStream(new File(data));
                    String filename="/user1record"+Integer.toString(uploadcount)+".zip";
                    Log.d("Filename:",filename);
                    boolean result = con.storeFile(filename, in);
                    in.close();
                    if (result) {
                        Log.v("upload result", "succeeded");
                        status="Upload Successful";
                    }

                    con.logout();
                    con.disconnect();
                }return status;
            }
            catch (Exception e)
            {

                String t = "Failure : " + e.getLocalizedMessage();
                Log.d("upload error",t);
                stopSelf();
                return t;

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contentEquals("Upload Successful")){

                Log.d(Constants.TAG,"upload sucess");
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("needupload", false);
                editor.putInt("count",uploadcount+1);
                editor.apply();
                FileHelper.deleteAllRecords();
                stopSelf();

            }
        }
    }



}
