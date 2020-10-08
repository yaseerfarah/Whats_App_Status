package yaseerfarah22.com.whatsappsticker_2.ViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import yaseerfarah22.com.whatsappsticker_2.Data.WhatsappStickerApi;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryResponse;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageAndVideo;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageResponse;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategories;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategoryImage;

public class CategoryViewModel extends ViewModel {




    private MediatorLiveData<List<CategoryData>> categoryDataLiveData;
    private MediatorLiveData<List<SubCategoryImage>> subCategoryDataLiveData;


    private Context context;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private WhatsappStickerApi whatsappStickerApi;
    private List<CategoryData> categoryDataList;
    private List<SubCategoryImage> subCategoryDataList;
    public List<ImageAndVideo>clipImage=new ArrayList<>();




    @Inject
    public CategoryViewModel(Context context, WhatsappStickerApi whatsappStickerApi) {
        this.context = context;
        this.whatsappStickerApi = whatsappStickerApi;


        this.categoryDataLiveData=new MediatorLiveData<>();
        this.categoryDataList=new ArrayList<>();

        this.subCategoryDataLiveData=new MediatorLiveData<>();
        this.subCategoryDataList=new ArrayList<>();


        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("RXjava Plugin Error",throwable.getMessage());
            }
        });


    }



    public void clearClipImage(){
        clipImage.clear();
    }

    public LiveData<List<CategoryData>> getCategoryLiveData(){
        return categoryDataLiveData;
    }

    public LiveData<List<SubCategoryImage>> getSubCategoryLiveData(){
        return subCategoryDataLiveData;
    }



    public void nextSubCategoryPage(int id){

        SubCategoryImage[] subCategoryImage=new SubCategoryImage[1];
        for (SubCategoryImage subCategoryImage1:subCategoryDataList){
            if (subCategoryImage1.getId()==id){
                subCategoryImage[0]=subCategoryImage1;
            }
        }

        if (subCategoryImage[0].isHasNext()){

            final int page=subCategoryImage[0].getPage()+1;

           // subCategoryImage[0].setPage(page++);
            disposables.add(whatsappStickerApi.getCategoryImages(String.valueOf(id),String.valueOf(page))
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(imageResponse -> {
                        if (imageResponse.getNext_Page()!=null&&imageResponse.getNext_Page().trim().length()>0){
                            subCategoryImage[0].setHasNext(true);
                        }else {
                            subCategoryImage[0].setHasNext(false);
                        }

                        List<ImageAndVideo> im=new ArrayList<>();

                        for (ImageData imageData1 : imageResponse.getData()) {
                            im.add(new ImageAndVideo(imageData1.getImage(),imageData1.getVideo()));
                        }

                        subCategoryImage[0].addImagesAndVideos(im);
                       // subCategoryImage[0].setPage(page);

                        return subCategoryImage[0];

                    })
                    .subscribe(this::onNextSubCategoryPage,this::onError)


            );

        }
    }



    public void nextCategoryPage(int id){

        CategoryData[] categoryDataH=new CategoryData[1];
        for (CategoryData categoryData:categoryDataList){
            if (categoryData.getId()==id){
                categoryDataH[0]=categoryData;
            }
        }

        if (categoryDataH[0].isHasNext()){

            int page=categoryDataH[0].getPage();
            categoryDataH[0].setPage(page++);
            disposables.add(whatsappStickerApi.getCategoryImages(String.valueOf(id),String.valueOf(page))
                    .subscribeOn(Schedulers.io())
                    .map(retrofit2.Response::body)
                    .map(imageResponse -> {
                        if (imageResponse.getNext_Page()!=null&&imageResponse.getNext_Page().trim().length()>0){
                            categoryDataH[0].setHasNext(true);
                        }else {
                            categoryDataH[0].setHasNext(false);
                        }

                        List<String> im=new ArrayList<>();

                        for (ImageData imageData1 : imageResponse.getData()) {
                            im.add(imageData1.getImage());
                        }

                        categoryDataH[0].addImages(im);

                        return categoryDataH[0];

                    })
                    .subscribe(this::onNextCategoryPage,this::onError)


            );

        }
    }


    public void reGetSubCategoryImage(){
        subCategoryDataLiveData.postValue(subCategoryDataList);
    }

    public void getCategoryData() {



        if (categoryDataList.size()==0) {

            disposables.add(whatsappStickerApi.getCategory().subscribeOn(Schedulers.io())

                    .doOnError(throwable -> {
                        Log.e("Error RXjava",throwable.getMessage());
                        try {
                            throw throwable;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    })
                    .map(retrofit2.Response::body)
                    .map(CategoryResponse::getData)
                    .flatMap(Observable::fromIterable)
                    .flatMap(categoryData -> {
                        categoryData.setPage(1);
                        categoryDataList.add(categoryData);

                        return getSubDataObservable(categoryData);
                    })
                    .subscribe(this::onNextVenue, this::onError, this::onCompleteVenues));


        }else {
            categoryDataLiveData.postValue(categoryDataList);
        }

    }





    private Observable<HashMap<SubCategories,List<ImageData>>> getSubCatPhoto(SubCategories subCategories) {

        List<String> images=new ArrayList<>();
        Observable<HashMap<SubCategories,List<ImageData>>> oImages=whatsappStickerApi.getCategoryImages(String.valueOf(subCategories.getId()),"1").
                subscribeOn(Schedulers.io()).
                map(retrofit2.Response::body)
                .map(imageResponse -> {
                    if (imageResponse.getNext_Page()!=null&&imageResponse.getNext_Page().trim().length()>0){
                        subCategories.setHasNext(true);
                    }else {
                        subCategories.setHasNext(false);
                    }
                    HashMap<SubCategories,List<ImageData>> listHashMap=new HashMap<>();
                    listHashMap.put(subCategories,imageResponse.getData());
                    return listHashMap;
                })

                ;

        return  oImages;
    }




    private Observable<SubCategoryImage> getCatPhoto(CategoryData categoryData) {

        Observable<SubCategoryImage> oImages=whatsappStickerApi.getCategoryImages(String.valueOf(categoryData.getId()),"1").
                subscribeOn(Schedulers.io()).
                map(retrofit2.Response::body)
                .map(imageResponse -> {
                    if (imageResponse.getNext_Page()!=null&&imageResponse.getNext_Page().trim().length()>0){
                        categoryData.setHasNext(true);
                    }else {
                        categoryData.setHasNext(false);
                    }
                    List<ImageAndVideo> imageAndVideo=new ArrayList<>();

                    for (ImageData imageData:imageResponse.getData()){
                        imageAndVideo.add(new ImageAndVideo(imageData.getImage(),imageData.getVideo()));

                    }
                    SubCategoryImage subCategoryImage=new SubCategoryImage(categoryData.getId(),categoryData.getTitle(),0,1,imageAndVideo,categoryData.isHasNext());

                   // categoryData.setImages(imgList);
                    return subCategoryImage;
                })

                ;

        return  oImages;
    }



    private void getCategoryImeges(List<CategoryData> categoryData){

        disposables.add(Observable.fromIterable(categoryData)
                .filter(categoryData1 -> {
                    return (categoryData1.getCountSubCategory()==0);
                })
                .flatMap(categoryData1 -> {

                    return getCatPhoto(categoryData1);

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNextCategoryImage,this::onError,this::onCompleteCategoryImage)



        );


    }





    private Observable<SubCategoryImage> getSubDataObservable(CategoryData categoryData) {

        List<SubCategories> subCategoriesList=new ArrayList<>();
        final SubCategories[] subCategoriesU = new SubCategories[1];

        Observable<SubCategoryImage>  oSubCategoriesObservable =  Observable.fromIterable(categoryData.getSub_categories()).
                subscribeOn(Schedulers.io())
                .flatMap(subCategories ->{

                    return getSubCatPhoto(subCategories);
                }).map(subCategoriesListHashMap -> {
                    List<ImageAndVideo> imageAndVideo=new ArrayList<>();

                    SubCategories subCategories2=null;

                    for (SubCategories subCategories:subCategoriesListHashMap.keySet()) {
                        subCategories2=subCategories;
                        for (ImageData imageData1 : subCategoriesListHashMap.get(subCategories)) {

                            imageAndVideo.add(new ImageAndVideo(imageData1.getImage(),imageData1.getVideo()));


                        }
                    }

                    SubCategoryImage subCategoryImage=new SubCategoryImage(subCategories2.getId(),subCategories2.getTitle(),subCategories2.getParent_id(),1,imageAndVideo,subCategories2.isHasNext());

                    return subCategoryImage;
                })
                ;




        return  oSubCategoriesObservable;
    }





    private void onNextVenue(SubCategoryImage subCategoryImage){
        //categoryDataList.add(categoryData);

        subCategoryDataList.add(subCategoryImage);

    }
    private void onCompleteVenues() {


        getCategoryImeges(categoryDataList);
    }
    private void onError(Throwable throwable) {
       Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();

    }



    private void onNextCategoryImage(SubCategoryImage subCategoryImage){
        //categoryDataList.add(categoryData);
        subCategoryDataList.add(subCategoryImage);

    }
    private void onCompleteCategoryImage() {
        subCategoryDataLiveData.postValue(subCategoryDataList);
        subCategoryDataLiveData.postValue(subCategoryDataList);
        categoryDataLiveData.postValue(categoryDataList);


    }




    private void onNextSubCategoryPage(SubCategoryImage subCategoryImage){
       for (int i=0;i<subCategoryDataList.size();i++){

           if (subCategoryImage.getId()==subCategoryDataList.get(i).getId()){

               subCategoryDataList.remove(i);
               subCategoryImage.setPage(subCategoryImage.getPage()+1);
               subCategoryDataList.add(subCategoryImage);
               subCategoryDataLiveData.postValue(subCategoryDataList);
               break;

           }
       }

    }

    private void onNextCategoryPage(CategoryData categoryData){
        for (int i=0;i<categoryDataList.size();i++){

            if (categoryData.getId()==categoryDataList.get(i).getId()){

                categoryDataList.remove(i);
                categoryDataList.add(categoryData);
                categoryDataLiveData.postValue(categoryDataList);
                break;

            }
        }

    }




    public void onClear(){

        disposables.clear();
    }


    @Override
    protected void onCleared() {
        super.onCleared();


    }
}
