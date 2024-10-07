package com.ob.rfi;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class StorageUtils {


     public static String getTempStorageLocationPath(Context context,String fileName){
       String  internalStoragePath = null;
         try {
             File path= new File(context.getFilesDir(), "RFI" + File.separator + "temp");
             if(!path.exists()){
                 path.mkdirs();
             }
             File outFile = new File(path, fileName);
             internalStoragePath=outFile.getAbsolutePath();
             //now we can create FileOutputStream and write something to file
         } catch (Exception e) {
             Log.e("TAG", "Saving received message failed with", e);
         }
         return internalStoragePath;
     }


    public static File getTempStorageLocationFile(Context context,String fileName){
        File outFile = null;
        try {

            File root = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                root = new File(/*Environment.getExternalStorageDirectory()*/context.getExternalFilesDirs(null)[0]
                        + File.separator + "RFI" + File.separator + "temp");
            }else {
                root = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "RFI" + File.separator + "temp");
            }



            if(!root.exists()){
                root.mkdirs();
            }
             outFile = new File(root, fileName);
        } catch (Exception e) {
            Log.e("TAG", "Saving received message failed with", e);
        }
        return outFile;
    }

}
