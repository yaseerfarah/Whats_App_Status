package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

import com.google.gson.annotations.SerializedName;

public class ImageData {
    @SerializedName("id")
    private int id;
    @SerializedName("Image")
    private String Image;
    @SerializedName("video_path")
    private String video;
    @SerializedName("category_id")
    private int category_id;


    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setImage(String Image){
        this.Image = Image;
    }
    public String getImage(){
        return this.Image;
    }
    public void setCategory_id(int category_id){
        this.category_id = category_id;
    }
    public int getCategory_id(){
        return this.category_id;
    }

}
