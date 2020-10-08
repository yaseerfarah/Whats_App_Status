package yaseerfarah22.com.whatsappsticker_2.ViewModel;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import yaseerfarah22.com.whatsappsticker_2.Data.ReadAndWriteToFile;
import yaseerfarah22.com.whatsappsticker_2.Data.StickerData;
import yaseerfarah22.com.whatsappsticker_2.Data.WhatsappStickerApi;
import yaseerfarah22.com.whatsappsticker_2.Interface.AddToWhatsAppListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.RoomStickerPackListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.StickerPackDeleteListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryResponse;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageAndVideo;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategories;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategoryImage;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropBitmap;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomSticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomStickerPack;
import yaseerfarah22.com.whatsappsticker_2.POJO.Sticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPackCollections;
import yaseerfarah22.com.whatsappsticker_2.Util.ContentFileParser;


import static yaseerfarah22.com.whatsappsticker_2.Data.ReadAndWriteToFile.FILE_NAME;

public class StickersViewModel extends ViewModel {




    private MediatorLiveData<List<StickerPack>> stickerPacksLiveData;
    private MediatorLiveData<List<CropBitmap>> cropBitmabLiveData;
    private MediatorLiveData<List<String>> clipBoardLiveData;

    private Context context;
    private StickerData stickerData;
    private List<StickerPack> stickerPackList;
    private List<CropBitmap> cropBitmapList;
    private List<String> clipBoardList;
    private StickerPack stickerPack;
    private int num=0;
    private ReadAndWriteToFile readAndWriteToFile;



    @Inject
    public StickersViewModel(Context context, StickerData stickerData) {
        this.context = context;
        this.stickerData=stickerData;
        this.stickerPacksLiveData=new MediatorLiveData<>();
        this.cropBitmabLiveData=new MediatorLiveData<>();
        this.clipBoardLiveData=new MediatorLiveData<>();
        this.stickerPackList=new ArrayList<>();
        this.cropBitmapList=new ArrayList<>();
        this.clipBoardList=new ArrayList<>();
        this.readAndWriteToFile=new ReadAndWriteToFile(context);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("RXjava Plugin Error",throwable.getMessage());
            }
        });


    }


    public StickerPack getStickerPack() {

        return stickerPack;
    }

    public void setStickerPack(StickerPack stickerPack) {
        this.stickerPack = stickerPack;
        cropBitmapList.clear();

        cropBitmabLiveData.postValue(cropBitmapList);
    }



    public LiveData<List<StickerPack>> getSteckerPacksLiveData(){
        return stickerPacksLiveData;
    }
    public LiveData<List<CropBitmap>> getCropBitmapLiveData(){
        return cropBitmabLiveData;
    }
    public LiveData<List<String>> getClipBoardLiveData(){
        return clipBoardLiveData;
    }


    public void setClipBoardList(List<String> clipBoards){
        this.clipBoardList.clear();
        this.clipBoardList.addAll(clipBoards);
        this.clipBoardLiveData.postValue(clipBoardList);
    }

    public void clearCropBitmap(){
        cropBitmapList.clear();
    }

    public void addCropBitmab(CropBitmap cropBitmap){


        for (int i=0;i<cropBitmapList.size();i++){

            if (cropBitmapList.get(i).getImgNumper()==cropBitmap.getImgNumper()){

                cropBitmapList.remove(i);
                cropBitmapList.add(cropBitmap);
                cropBitmabLiveData.postValue(cropBitmapList);
                return;

            }

        }

        cropBitmapList.add(cropBitmap);
        cropBitmabLiveData.postValue(cropBitmapList);


    }





    public void addAllCropBitmap(List<ImageAndVideo> clipBoard, HashMap<String, Bitmap> stringBitmapHashMap){

       // Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
        cropBitmapList.clear();

        for (int i=0;i<clipBoard.size();i++){
          //  addCropBitmab(new CropBitmap(i+1,Uri.parse(clipBoard.get(i)),stringBitmapHashMap.get(clipBoard.get(i))));
           final int num=i+1;
            Glide.with(context).load(clipBoard.get(i)).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    addCropBitmab(new CropBitmap(num,Uri.parse(clipBoard.get(num-1).getImageUri()),((BitmapDrawable)resource).getBitmap()));
                }
            });


        }



    }

    public void addStickerPack(StickerPack stickerPack){



        RoomStickerPack roomStickerPack=new RoomStickerPack(stickerPack.identifier,stickerPack.name,stickerPack.publisher,convertToRoom(stickerPack.getStickers()),stickerPack.trayImageFile,stickerPack.publisherEmail,stickerPack.publisherWebsite,stickerPack.privacyPolicyWebsite,stickerPack.licenseAgreementWebsite,stickerPack.imageDataVersion,stickerPack.avoidCache);

        stickerData.insertSticker(roomStickerPack);
        stickerPackList.add(stickerPack);
        stickerPacksLiveData.postValue(stickerPackList);



    }


    public void deleteStickerPacks(StickerPack stickerPack,StickerPackDeleteListener stickerPackDeleteListener){

        readAndWriteToFile.deleteDir(stickerPack.identifier);


        checkIfExistsAndDelete(stickerPack);


        RoomStickerPack roomStickerPack=new RoomStickerPack(stickerPack.identifier,stickerPack.name,stickerPack.publisher,convertToRoom(stickerPack.getStickers()),stickerPack.trayImageFile,stickerPack.publisherEmail,stickerPack.publisherWebsite,stickerPack.privacyPolicyWebsite,stickerPack.licenseAgreementWebsite,stickerPack.imageDataVersion,stickerPack.avoidCache);

        stickerData.deleteStickerPack(roomStickerPack,stickerPackDeleteListener);




    }






    public void getAllStickerPacks(){


        stickerData.getPackStickers(new RoomStickerPackListener() {
            @Override
            public void onSuccess(List<RoomStickerPack> roomStickerPacks) {
                stickerPackList.clear();
                stickerPackList.addAll(convertToStickerPack(roomStickerPacks));
                stickerPacksLiveData.postValue(stickerPackList);
                //Toast.makeText(context,"hi",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }


    public CropBitmap getSpicificBitmap(int imgNum){
        for (int i=0;i<cropBitmapList.size();i++){

            if (cropBitmapList.get(i).getImgNumper()==imgNum){

                return cropBitmapList.get(i);

            }

        }

        return null;

    }


    public void addToWhatsApp(AddToWhatsAppListener addToWhatsAppListener){

        StickerPackCollections stickerPackCollections;
        List<Sticker> stickers=new ArrayList<>();
        List<CropBitmap> cropBitmaps=new ArrayList<>();

        for (int i=0;i<cropBitmapList.size();i++){
            if (i==0){
                continue;
            }else {
                cropBitmaps.add(cropBitmapList.get(i));
            }
        }



        Observable.fromIterable(cropBitmaps)
                .doOnError(throwable -> {
                    Log.e("Add to File Error",throwable.getMessage());
                    Toast.makeText(context,throwable.getMessage(),Toast.LENGTH_LONG).show();
                })
                .map(cropBitmap -> {
                    readAndWriteToFile.writeImageToFile(context,stickerPack.identifier,cropBitmap.getBitmap(),"s_"+cropBitmap.getUri().getLastPathSegment());
                    return cropBitmap;
                })
                .map(cropBitmap -> {
                    return new Sticker("s_"+cropBitmap.getUri().getLastPathSegment()+".webp",new ArrayList<String>());
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Sticker>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Sticker sticker) {
                        stickers.add(sticker);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Add to File Error",e.getMessage());
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        addToWhatsAppListener.onFailed();
                    }

                    @Override
                    public void onComplete() {

                        stickerPack.setStickers(stickers);

                        if (stickerPack.getTrayImageFile().trim().length()==0){
                            stickerPack.setTrayImageFile("s_"+cropBitmapList.get(0).getUri().getLastPathSegment()+".png");
                            readAndWriteToFile.writeTrayImageToFile(context,stickerPack.identifier,cropBitmapList.get(0).getBitmap(),"s_"+cropBitmapList.get(0).getUri().getLastPathSegment());
                        }
                        checkIfExistsAndWrite(stickerPack);
                        updateStickerPack(stickerPack);
                        addToWhatsAppListener.onSuccess();

                    }
                });

        //stickerPack=new StickerPack("2","Sport","Yasser",stickers,"tray_Cuppy.png",ReadAndWriteToFile.writeTrayImageToFile(AddSticker.this,cat_imgB,"tray_Cuppy"),"","","","","1",false);











    }





    private boolean checkIfExistsAndWrite(StickerPack stickerPack1) {

        List<StickerPack> stickerPackList=new ArrayList<>();
        Gson gson=new Gson();
        boolean isWrite=false;
        StickerPackCollections stickerPackCollections;

        File file = new File(readAndWriteToFile.getStickerFile(), FILE_NAME);
        if (file.exists()) {


            try (InputStream contentsInputStream = new FileInputStream(file)) {

                stickerPackList.addAll( ContentFileParser.parseStickerPacks(contentsInputStream));



            } catch (IOException | IllegalStateException e) {
                throw new RuntimeException("ERROR" + " file has some issues: " + e.getMessage(), e);
            }


        } else {

            stickerPackList.add(stickerPack1);
            stickerPackCollections=new StickerPackCollections("","",stickerPackList);
            String data=gson.toJson(stickerPackCollections);
            isWrite=readAndWriteToFile.writeToFile(context,data);
            return isWrite;
        }


        if (stickerPackList.size()>0){

            for (int i=0;i<stickerPackList.size();i++) {

                if (stickerPack1.identifier.trim().matches(stickerPackList.get(i).identifier.trim())) {

                    stickerPackList.remove(i);
                    stickerPackList.add(stickerPack1);
                    stickerPackCollections=new StickerPackCollections("","",stickerPackList);
                    String data=gson.toJson(stickerPackCollections);
                    isWrite=readAndWriteToFile.writeToFile(context,data);
                    //Toast.makeText(context,"h",Toast.LENGTH_LONG).show();
                    return isWrite;
                }

            }


            stickerPackList.add(stickerPack1);
            stickerPackCollections=new StickerPackCollections("","",stickerPackList);
            String data=gson.toJson(stickerPackCollections);
            isWrite=readAndWriteToFile.writeToFile(context,data);
            return isWrite;


        }


        return isWrite;

    }

    private boolean checkIfExistsAndDelete(StickerPack stickerPack1) {

        List<StickerPack> stickerPackList=new ArrayList<>();
        Gson gson=new Gson();
        boolean isdeleted=false;
        StickerPackCollections stickerPackCollections;

        File file = new File(readAndWriteToFile.getStickerFile(), FILE_NAME);
        if (file.exists()) {


            try (InputStream contentsInputStream = new FileInputStream(file)) {

                stickerPackList.addAll( ContentFileParser.parseStickerPacks(contentsInputStream));



            } catch (IOException | IllegalStateException e) {
                throw new RuntimeException("ERROR" + " file has some issues: " + e.getMessage(), e);
            }


        } else {

           return false;
        }


        if (stickerPackList.size()>0){

            for (int i=0;i<stickerPackList.size();i++) {

                if (stickerPack1.identifier.trim().matches(stickerPackList.get(i).identifier.trim())) {

                    stickerPackList.remove(i);

                    if (stickerPackList.size()>0) {

                        stickerPackCollections = new StickerPackCollections("", "", stickerPackList);
                        String data = gson.toJson(stickerPackCollections);
                        isdeleted = readAndWriteToFile.writeToFile(context, data);
                        //Toast.makeText(context,"h",Toast.LENGTH_LONG).show();
                    }else {
                        isdeleted=readAndWriteToFile.deleteFile(context,FILE_NAME);
                    }
                    return isdeleted;
                }

            }

        }


        return isdeleted;

    }




    private void updateStickerPack(StickerPack stickerPack){
        RoomStickerPack roomStickerPack=new RoomStickerPack(stickerPack.identifier,stickerPack.name,stickerPack.publisher,convertToRoom(stickerPack.getStickers()),stickerPack.trayImageFile,stickerPack.publisherEmail,stickerPack.publisherWebsite,stickerPack.privacyPolicyWebsite,stickerPack.licenseAgreementWebsite,stickerPack.imageDataVersion,stickerPack.avoidCache);

        stickerData.updateStickerPack(roomStickerPack);
        setStickerPack(stickerPack);



    }





    private List<RoomSticker> convertToRoom(List<Sticker> stickers){

        List<RoomSticker> roomStickers=new ArrayList<>();

        for (Sticker sticker:stickers){
            roomStickers.add(new RoomSticker(sticker.imageFileName,sticker.emojis));

        }

        return roomStickers;

    }

    private List<Sticker> convertToSticker(List<RoomSticker> roomStickers){

        List<Sticker> stickers=new ArrayList<>();

        for (RoomSticker roomSticker:roomStickers){
            stickers.add(new Sticker(roomSticker.imageFileName,roomSticker.emojis));

        }

        return stickers;

    }


    private List<StickerPack> convertToStickerPack(List<RoomStickerPack> roomStickerPacks){

        List<StickerPack> stickerPacks=new ArrayList<>();
       for (RoomStickerPack roomStickerPack:roomStickerPacks){

           stickerPacks.add(new StickerPack(roomStickerPack.identifier,roomStickerPack.name,roomStickerPack.publisher,convertToSticker(roomStickerPack.getStickers()),roomStickerPack.trayImageFile,roomStickerPack.publisherEmail,roomStickerPack.publisherWebsite,roomStickerPack.privacyPolicyWebsite,roomStickerPack.licenseAgreementWebsite,roomStickerPack.imageDataVersion,roomStickerPack.avoidCache));
       }

       return stickerPacks;
    }


    private byte[] getBitmapByte(Bitmap bmp){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    @Override
    protected void onCleared() {
        super.onCleared();


    }
}
