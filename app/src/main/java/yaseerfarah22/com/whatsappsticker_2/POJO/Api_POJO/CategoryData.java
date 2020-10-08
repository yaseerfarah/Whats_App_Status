package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CategoryData
{
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("parent_id")
    private String parent_id;
    @SerializedName("sub_categories")
    private List<SubCategories> sub_categories;
    @SerializedName("count_sub_category")
    private int countSubCategory;


    private List<SubCategories> sub_categories2=new ArrayList<>();
    private List<String> Images;
    private boolean hasNext;
    private int page;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCountSubCategory() {
        return countSubCategory;
    }

    public void setCountSubCategory(int countSubCategory) {
        this.countSubCategory = countSubCategory;
    }


    public void addImages(List<String> images) {
        Images.addAll(images);
    }
    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public List<SubCategories> getSub_categories2() {
        return sub_categories2;
    }

    public void addSub_categories2(SubCategories sub_categories2) {
        this.sub_categories2.add(sub_categories2);
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setParent_id(String parent_id){
        this.parent_id = parent_id;
    }
    public String getParent_id(){
        return this.parent_id;
    }
    public void setSub_categories(List<SubCategories> sub_categories){
        this.sub_categories = sub_categories;
    }
    public List<SubCategories> getSub_categories(){
        return this.sub_categories;
    }
}
