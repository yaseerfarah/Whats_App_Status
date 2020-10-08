package yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO;

import java.util.List;

public class CategoryImage {

    private int id;

    private String title;

    private int parent_id;

    private boolean hasNext;
    private List<String> Images;


    public CategoryImage(int id, String title, int parent_id, List<String> images, boolean hasNext) {
        this.id = id;
        this.title = title;
        this.parent_id = parent_id;
        this.Images = images;
        this.hasNext=hasNext;
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

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public void addImages(List<String> images) {
        Images.addAll(images);
    }
}
