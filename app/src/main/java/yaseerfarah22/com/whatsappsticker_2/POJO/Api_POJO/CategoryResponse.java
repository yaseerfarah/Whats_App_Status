package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse
{
    @SerializedName("status")
    private int status;
    @SerializedName("data")
    private List<CategoryData> data;

    @SerializedName("message")
    private String message;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setData(List<CategoryData> data){
        this.data = data;
    }
    public List<CategoryData> getData(){
        return this.data;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
}
