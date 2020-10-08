package yaseerfarah22.com.whatsappsticker_2.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StickerPackCollections implements Parcelable {


    @SerializedName("android_play_store_link")
    String android_play_store_link;
    @SerializedName( "ios_app_store_link")
    String ios_app_store_link;
    @SerializedName("sticker_packs")
    List<StickerPack>stickerPacks;


    public StickerPackCollections(String android_play_store_link, String ios_app_store_link, List<StickerPack> stickerPacks) {
        this.android_play_store_link = android_play_store_link;
        this.ios_app_store_link = ios_app_store_link;
        this.stickerPacks = stickerPacks;
    }


    public List<StickerPack> getStickerPacks() {
        return stickerPacks;
    }

    public void setStickerPacks(List<StickerPack> stickerPacks) {
        this.stickerPacks = stickerPacks;
    }

    private StickerPackCollections (Parcel in){
        stickerPacks=in.createTypedArrayList(StickerPack.CREATOR);
        android_play_store_link=in.readString();
        ios_app_store_link=in.readString();
    }


    public static final Creator<StickerPackCollections> CREATOR = new Creator<StickerPackCollections>() {
        @Override
        public StickerPackCollections createFromParcel(Parcel in) {
            return new StickerPackCollections(in);
        }

        @Override
        public StickerPackCollections[] newArray(int size) {
            return new StickerPackCollections[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(android_play_store_link);
        dest.writeString(ios_app_store_link);
        dest.writeTypedList(stickerPacks);

    }
}
