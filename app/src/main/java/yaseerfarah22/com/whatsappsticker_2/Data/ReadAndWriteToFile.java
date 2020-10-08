package yaseerfarah22.com.whatsappsticker_2.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import yaseerfarah22.com.whatsappsticker_2.Interface.ProgressListener;

public class ReadAndWriteToFile {


    public static final String FILE_NAME="contents.txt";
   public static final File IMAGE_FILE=new File(Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES),"WhatsAppStatus");

   private File stickerFile;
   private Context context;

    public ReadAndWriteToFile(Context context) {
        this.context = context;
        this.stickerFile=new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"WhatsAppSticker");
    }


    public File getStickerFile() {
        return stickerFile;
    }

    public  boolean writeToFile(Context context, String data){

        if (!stickerFile.exists()){
            stickerFile.mkdir();
        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream( new File(stickerFile, FILE_NAME)));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }


    public  boolean appendToFile(Context context, String data){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream( new File(stickerFile, FILE_NAME),true));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }





    public  String writeImageToFile(Context context,String id, Bitmap bitmap,String name){

        File imageDir = new File(stickerFile, id);
        if (!imageDir.exists()){
            imageDir.mkdirs();
        }


        File imageFile = new File(imageDir, name + ".webp");

        bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 80, os);
            os.flush();
            os.close();
           // Toast.makeText(context,"SAVED",Toast.LENGTH_LONG).show();
            return imageFile.toString();
        } catch (Exception e) {
            Log.e("ERROR stickerFile",e.getMessage());
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }



    }








    public  String writeTrayImageToFile(Context context,String id, Bitmap bitmap,String name){

        File imageDir = new File(stickerFile, id);
        if (!imageDir.exists()){
            imageDir.mkdirs();
        }


        File imageFile = new File(imageDir, name + ".png");

        bitmap = Bitmap.createScaledBitmap(bitmap, 96, 96, true);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            return imageFile.getPath();
        } catch (Exception e) {
            return null;
        }



    }



    public  String readFromFile(Context context){

        String ret = "";

        try {
            InputStream inputStream = new FileInputStream( new File(stickerFile, FILE_NAME));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return null;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return null;
        }

        return ret;


    }



    public  boolean deleteFile(Context context,String name){

        File file = new File(stickerFile, name);
        if (file.exists()){
            return file.delete();

        }else {
            return false;
        }


    }



    public    void deleteDir(String path) {


        File file = new File(stickerFile, path);
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null) {
                for(File f : files) {
                    f.delete();
                }
            }
            file.delete();
        }

    }

    public static   String writeImageToSpecificFile(Context context, Bitmap bitmap,String name){


        if (!IMAGE_FILE.exists()){
            IMAGE_FILE.mkdirs();
        }


        File imageFile = new File(IMAGE_FILE, name + ".webp");

        // bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, true);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.WEBP, 100, os);
            os.flush();
            os.close();
            // Toast.makeText(context,"SAVED",Toast.LENGTH_LONG).show();
            return imageFile.toString();
        } catch (Exception e) {
            Log.e("ERROR stickerFile",e.getMessage());
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }



    }



    public static String downLoadAndSsaveVideo(String video_url, String name, ProgressListener progressListener){

        int count;

        try {
            URL url = new URL(video_url);
            URLConnection conexion = url.openConnection();
            conexion.connect();
            int lenghtOfFile = conexion.getContentLength();
            if (!IMAGE_FILE.exists()){
                IMAGE_FILE.mkdirs();
            }
            File myFile;

            myFile = new File(IMAGE_FILE, name);

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(myFile);
            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                progressListener.onPercentage((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return myFile.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR Download video", e.getMessage());
            return null;
        }


    }





}
