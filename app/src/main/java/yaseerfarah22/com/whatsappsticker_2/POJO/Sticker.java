package yaseerfarah22.com.whatsappsticker_2.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sticker implements Parcelable {

    @SerializedName("image_file")
    public String imageFileName;
   // @SerializedName("image_uri")
    //public String uri;
    @SerializedName("emojis")
    public List<String> emojis;

    transient long size;

    public Sticker(String imageFileName, List<String> emojis) {
        this.imageFileName = imageFileName;
        //this.uri=uri;
        this.emojis = emojis;
    }

    protected Sticker(Parcel in) {
        imageFileName = in.readString();
       // uri=in.readString();
        emojis = in.createStringArrayList();
        size = in.readLong();
    }

    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageFileName);
        dest.writeStringList(emojis);
        //dest.writeString(uri);
        dest.writeLong(size);
    }
}
