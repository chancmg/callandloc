package com.madroft.chan.callandloc;

/**
 * Created by chan on 3/3/2017.
 */

import java.io.File;
import java.util.Date;
import android.app.Activity;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

public class FileHelper {
    /**
     * returns absolute file directory
     *
     * @return
     * @throws Exception
     */
    public static String getFilename(String phoneNumber) throws Exception {
        String filepath = null;
        String myDate = null;
        File file = null;
        if (phoneNumber == null)
            throw new Exception("Phone number can't be empty");
        try {
            filepath = getFilePath();

            file = new File(filepath, Constants.FILE_DIRECTORY);

            if (!file.exists()) {
                file.mkdirs();
            }

            myDate = (String) DateFormat.format("yyyyMMddkkmmss", new Date());


            // Clean characters in file name
            phoneNumber = phoneNumber.replaceAll("[\\*\\+-]", "");
            if (phoneNumber.length() > 10) {
                phoneNumber.substring(phoneNumber.length() - 10,
                        phoneNumber.length());
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception " + phoneNumber);
            e.printStackTrace();
        }

        return (file.getAbsolutePath() + "/d" + myDate + "p" + phoneNumber + ".3gp");
    }

    public static String getFilePath() {
        // TODO: Change to user selected directory
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    // delete all records

    public static void deleteAllRecords(Activity caller) {
        String filepath = getFilePath() + "/" + Constants.FILE_DIRECTORY;
        File file = new File(filepath);

        String listOfFileNames[] = file.list();

        for (int i = 0; i < listOfFileNames.length; i++) {
            File file2 = new File(filepath, listOfFileNames[i]);
            if (file2.exists()) {
                file2.delete();
            }
        }

        filepath = caller.getFilesDir().getAbsolutePath() + "/"
                + Constants.FILE_DIRECTORY;
        file = new File(filepath);

        String listOfFileNames2[] = file.list();

        for (int i = 0; i < listOfFileNames2.length; i++) {
            File file2 = new File(filepath, listOfFileNames2[i]);
            if (file2.exists()) {
                file2.delete();
            }
        }
    }

    // delete single record
    public static void deleteFile(String fileName) {
        if (fileName == null)
            return;
        Log.d(Constants.TAG, "FileHelper deleteFile " + fileName);
        try {
            File file = new File(fileName);

            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, "Exception");
            e.printStackTrace();
        }
    }
}

