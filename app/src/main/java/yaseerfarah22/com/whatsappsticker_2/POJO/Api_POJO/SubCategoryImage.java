package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubCategoryImage {

    private int id;

    private String title;

    private int parent_id;

    private boolean hasNext;
    private List<ImageAndVideo> imageAndVideos;
    private int page;


    public SubCategoryImage(int id, String title, int parent_id,int page, List<ImageAndVideo> imageAndVideos,boolean hasNext) {
        this.id = id;
        this.title = title;
        this.parent_id = parent_id;
        this.imageAndVideos = imageAndVideos;
        this.hasNext=hasNext;
        this.page=page;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }



    public List<ImageAndVideo> getImageAndVideos() {
        return imageAndVideos;
    }

    public void setImageAndVideos(List<ImageAndVideo> imageAndVideos) {
        this.imageAndVideos = imageAndVideos;
    }

    public void addImagesAndVideos(List<ImageAndVideo> imageAndVideos) {
        this.imageAndVideos.addAll(imageAndVideos);
    }
}
