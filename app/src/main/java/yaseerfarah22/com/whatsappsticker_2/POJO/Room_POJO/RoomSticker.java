package yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import androidx.room.TypeConverters;
import yaseerfarah22.com.whatsappsticker_2.Util.StringDataConverter;

public class RoomSticker implements Serializable {

    public String imageFileName;
    @TypeConverters(StringDataConverter.class)
    public List<String> emojis;



    public RoomSticker(String imageFileName, List<String> emojis) {
        this.imageFileName = imageFileName;
        this.emojis = emojis;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public List<String> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<String> emojis) {
        this.emojis = emojis;
    }
}
