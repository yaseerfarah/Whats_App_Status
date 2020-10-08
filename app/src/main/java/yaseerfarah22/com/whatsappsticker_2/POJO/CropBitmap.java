package yaseerfarah22.com.whatsappsticker_2.POJO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class CropBitmap  {

    private Uri uri;
    private int imgNumper;
    private Bitmap bitmap;


    public CropBitmap(int imgNumper,Uri uri, Bitmap bitmap) {
        this.imgNumper=imgNumper;
        this.uri = uri;
        this.bitmap = bitmap;

    }




    public Uri getUri() {
        return uri;
    }

    public Bitmap getBitmap() {

        return bitmap;
    }

    public int getImgNumper() {
        return imgNumper;
    }


}
