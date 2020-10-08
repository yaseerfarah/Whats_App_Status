package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageResponse {
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private List<ImageData> data;
    @SerializedName("Next Page")
    private String next_Page;
    @SerializedName("Prev Page")
    private String prev_Page;
    @SerializedName("message")
    private String message;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setData(List<ImageData> data){
        this.data = data;
    }
    public List<ImageData> getData(){
        return this.data;
    }
    public void setNext_Page(String next_Page){
        this.next_Page=next_Page;
    }
    public String getNext_Page(){
        return this.next_Page;
    }
    public void setPrev_Page(String prev_Page){
        this.prev_Page=prev_Page;
    }
    public String getPrev_Page(){
        return this.prev_Page;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

}
