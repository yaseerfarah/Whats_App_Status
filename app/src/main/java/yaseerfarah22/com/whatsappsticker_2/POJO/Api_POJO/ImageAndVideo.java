package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

public class ImageAndVideo {

    private String imageUri;
    private String videoUri;

    public ImageAndVideo(String imageUri, String videoUri) {
        this.imageUri = imageUri;
        this.videoUri = videoUri;
    }


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(String videoUri) {
        this.videoUri = videoUri;
    }
}
