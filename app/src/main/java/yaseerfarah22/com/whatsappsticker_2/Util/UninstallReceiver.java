package yaseerfarah22.com.whatsappsticker_2.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


import java.io.File;

import yaseerfarah22.com.whatsappsticker_2.Interface.InternetStatus;


public class UninstallReceiver extends BroadcastReceiver {





    @Override
    public void onReceive(Context context, Intent intent) {

        /* if(intent.getAction().trim().matches("android.intent.action.PACKAGE_REMOVED")) {

             Toast.makeText(context,"hi2",Toast.LENGTH_SHORT).show();
             if (FILE.exists()) {

                 File[] files = FILE.listFiles();
                 if (files.length>0){
                     for (File file:files){
                         if (file != null && file.isDirectory()) {
                             File[] file_files = file.listFiles();
                             if (file_files != null) {
                                 for (File f : files) {
                                     f.delete();
                                 }
                             }
                             file.delete();
                         }
                             file.delete();

                     }

                 }


             }
         }

*/
    }
}
