package yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import yaseerfarah22.com.whatsappsticker_2.POJO.Sticker;
import yaseerfarah22.com.whatsappsticker_2.Util.DataConverter;

@Entity
public class RoomStickerPack implements Serializable {
    @NonNull
    @PrimaryKey
   public final String identifier;

   public final String name;

   public final String publisher;

    public  String trayImageFile;

    public final String publisherEmail;

    public final String publisherWebsite;

    public final String privacyPolicyWebsite;

    public final String licenseAgreementWebsite;

    public final String imageDataVersion;

    public final boolean avoidCache;

    @TypeConverters(DataConverter.class)
    private List<RoomSticker> stickers;


   public RoomStickerPack(String identifier, String name, String publisher, List<RoomSticker>stickers, String trayImageFile, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite, String imageDataVersion, boolean avoidCache) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.stickers=stickers;
        this.trayImageFile = trayImageFile;
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
       this.imageDataVersion = imageDataVersion;
       this.avoidCache = avoidCache;

    }


    @NonNull
    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTrayImageFile() {
        return trayImageFile;
    }

    public void setTrayImageFile(String trayImageFile) {
        this.trayImageFile = trayImageFile;
    }

    public String getPublisherEmail() {
        return publisherEmail;
    }

    public String getPublisherWebsite() {
        return publisherWebsite;
    }

    public String getPrivacyPolicyWebsite() {
        return privacyPolicyWebsite;
    }

    public String getLicenseAgreementWebsite() {
        return licenseAgreementWebsite;
    }

    public String getImageDataVersion() {
        return imageDataVersion;
    }

    public boolean isAvoidCache() {
        return avoidCache;
    }

    public List<RoomSticker> getStickers() {
        return stickers;
    }

    public void setStickers(List<RoomSticker> stickers) {
        this.stickers = stickers;
    }
}