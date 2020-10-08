package yaseerfarah22.com.whatsappsticker_2.POJO;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


public class StickerPack implements Parcelable {

    @SerializedName("identifier")
   public final String identifier;
    @SerializedName("name")
   public final String name;
    @SerializedName("publisher")
   public final String publisher;
    @SerializedName("tray_image_file")
    public  String trayImageFile;
    @SerializedName("publisher_email")
    public final String publisherEmail;
    @SerializedName("publisher_website")
    public final String publisherWebsite;
    @SerializedName("privacy_policy_website")
    public final String privacyPolicyWebsite;
    @SerializedName("license_agreement_website")
    public final String licenseAgreementWebsite;
    @SerializedName("image_data_version")
    public final String imageDataVersion;
    @SerializedName("avoid_cache")
    public final boolean avoidCache;
    //@SerializedName("tray_uri")
    //public String trayUri;


    public transient  String iosAppStoreLink;
    @SerializedName("stickers")
    private List<Sticker> stickers;

    private transient  long totalSize;

    public transient  String androidPlayStoreLink;

    private transient  boolean isWhitelisted;


   public StickerPack(String identifier, String name, String publisher,List<Sticker>stickers, String trayImageFile, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite, String imageDataVersion, boolean avoidCache) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.stickers=stickers;
        this.trayImageFile = trayImageFile;
       // this.trayUri=trayUri;
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
       this.imageDataVersion = imageDataVersion;
       this.avoidCache = avoidCache;

    }

    public StickerPack(String name, String publisher, String trayImageFile) {
        this.name = name;
        this.publisher = publisher;
        this.trayImageFile = trayImageFile;
        this.identifier = "";
        this.stickers=new ArrayList<>();
        this.trayImageFile = trayImageFile;
        // this.trayUri=trayUri;
        this.publisherEmail = "";
        this.publisherWebsite = "";
        this.privacyPolicyWebsite = "";
        this.licenseAgreementWebsite = "";
        this.imageDataVersion = "";
        this.avoidCache = false;
    }

    public String getTrayImageFile() {
        return trayImageFile;
    }

    public void setTrayImageFile(String trayImageFile) {
        this.trayImageFile=trayImageFile;
    }

    public void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    boolean getIsWhitelisted() {
        return isWhitelisted;
    }

    private StickerPack(Parcel in) {
        identifier = in.readString();
        name = in.readString();
        publisher = in.readString();
        trayImageFile = in.readString();
       // trayUri=in.readString();
        publisherEmail = in.readString();
        publisherWebsite = in.readString();
        privacyPolicyWebsite = in.readString();
        licenseAgreementWebsite = in.readString();
        iosAppStoreLink = in.readString();
        stickers = in.createTypedArrayList(Sticker.CREATOR);
        totalSize = in.readLong();
        androidPlayStoreLink = in.readString();
        isWhitelisted = in.readByte() != 0;
        imageDataVersion = in.readString();
        avoidCache = in.readByte() != 0;

    }




    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        @Override
        public StickerPack createFromParcel(Parcel in) {
            return new StickerPack(in);
        }

        @Override
        public StickerPack[] newArray(int size) {
            return new StickerPack[size];
        }
    };

    public void setStickers(List<Sticker> stickers) {
        this.stickers = stickers;
        totalSize = 0;
        for (Sticker sticker : stickers) {
            totalSize += sticker.size;
        }
    }

    public void setAndroidPlayStoreLink(String androidPlayStoreLink) {
        this.androidPlayStoreLink = androidPlayStoreLink;
    }

    public void setIosAppStoreLink(String iosAppStoreLink) {
        this.iosAppStoreLink = iosAppStoreLink;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public long getTotalSize() {
        return totalSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeString(trayImageFile);
       // dest.writeString(trayUri);
        dest.writeString(publisherEmail);
        dest.writeString(publisherWebsite);
        dest.writeString(privacyPolicyWebsite);
        dest.writeString(licenseAgreementWebsite);
        dest.writeString(iosAppStoreLink);
        dest.writeTypedList(stickers);
        dest.writeLong(totalSize);
        dest.writeString(androidPlayStoreLink);
        dest.writeByte((byte) (isWhitelisted ? 1 : 0));
        dest.writeString(imageDataVersion);
        dest.writeByte((byte) (avoidCache ? 1 : 0));
    }
}