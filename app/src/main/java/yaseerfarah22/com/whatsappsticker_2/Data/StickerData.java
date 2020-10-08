package yaseerfarah22.com.whatsappsticker_2.Data;

import android.content.Context;
import android.widget.Toast;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import androidx.room.Room;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yaseerfarah22.com.whatsappsticker_2.Interface.RoomStickerPackListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.StickerPackDeleteListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomSticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomStickerPack;
import yaseerfarah22.com.whatsappsticker_2.POJO.Sticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;

public class StickerData {

    private Context context;
    private StickerDatabase stickerDatabase;


    @Inject
    public StickerData(Context context, StickerDatabase stickerDatabase) {
        this.context = context;
        this.stickerDatabase = stickerDatabase;
    }


    public void insertSticker(String identifier, String name, String publisher, List<RoomSticker> stickers, String trayImageFile, String trayUri, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite, String imageDataVersion, boolean avoidCache){

        RoomStickerPack stickerPack=new RoomStickerPack(identifier,name,publisher,stickers,trayImageFile,publisherEmail,publisherWebsite,privacyPolicyWebsite,licenseAgreementWebsite,imageDataVersion,avoidCache);


        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {

                    stickerDatabase.daoStickerRoom().insertSticker(stickerPack);
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });



    }



    public void insertSticker(RoomStickerPack roomSticker){

        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {

                   long i= stickerDatabase.daoStickerRoom().insertSticker(roomSticker);
                   // Toast.makeText(context,i+"/",Toast.LENGTH_LONG).show();

                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }



    public void getPackStickers(RoomStickerPackListener roomStickerPackListener){
        //Toast.makeText(context,"hi",Toast.LENGTH_LONG).show();


        Single.create(new SingleOnSubscribe() {
            @Override
            public void subscribe(SingleEmitter emitter) throws Exception {

                try {

                    List<RoomStickerPack> roomStickerPacks= stickerDatabase.daoStickerRoom().fetchAllStickers();
                    if (!emitter.isDisposed()) {
                        emitter.onSuccess(roomStickerPacks);
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }


            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<RoomStickerPack>>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<RoomStickerPack> roomStickerPacks) {
                roomStickerPackListener.onSuccess(roomStickerPacks);
            }

            @Override
            public void onError(Throwable e) {
                roomStickerPackListener.onError(e);

            }
        });

    }


    public void updateStickerPack(RoomStickerPack roomStickerPack){


        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {

                    stickerDatabase.daoStickerRoom().updateSticker(roomStickerPack);
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }


    public void deleteStickerPack(RoomStickerPack roomStickerPack, StickerPackDeleteListener stickerPackDeleteListener){

        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                try {

                    stickerDatabase.daoStickerRoom().deleteSticker(roomStickerPack);
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }

                }catch (Exception e){
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        stickerPackDeleteListener.onDeleted();

                    }

                    @Override
                    public void onError(Throwable e) {
                        stickerPackDeleteListener.onError(e);
                    }
                });

    }



}
