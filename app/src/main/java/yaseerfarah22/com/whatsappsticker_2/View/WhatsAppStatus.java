package yaseerfarah22.com.whatsappsticker_2.View;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import yaseerfarah22.com.whatsappsticker_2.BuildConfig;
import yaseerfarah22.com.whatsappsticker_2.Data.ReadAndWriteToFile;
import yaseerfarah22.com.whatsappsticker_2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhatsAppStatus extends Fragment {


    public static final String IMAGE_URI="Image_Uri";
    public static final String TYPE="Type";

    public static final int  IMAGE=1;
    public static final int VIDEO=0;

    ImageView imageView;
    Button download,share;
    ProgressBar progressBar,loading;
    VideoView videoView;
    ProgressDialog progressdialog;


    String path_uri;
    int type;
    Bitmap bitmap;
    File file;
    boolean isDownload,isLoading;




    public WhatsAppStatus() {
        // Required empty public constructor
        isDownload=false;
        isLoading=true;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path_uri=getArguments().getString(IMAGE_URI);
        type=getArguments().getInt(TYPE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_whats_app_status, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=(ImageView) view.findViewById(R.id.image_view);
        download=(Button) view.findViewById(R.id.download);
        share=(Button) view.findViewById(R.id.share) ;
        progressBar=view.findViewById(R.id.prog);
        loading=(ProgressBar) view.findViewById(R.id.loading);
        videoView=(VideoView) view.findViewById(R.id.video_view);

        //Progress dialog for download video
        progressdialog = new ProgressDialog(getContext());

        progressdialog.setIndeterminate(false);

        progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressdialog.setCancelable(false);

        progressdialog.setMax(100);




        switch (type){

            case IMAGE:
                videoView.setVisibility(View.INVISIBLE);
                Glide.with(getContext()).load(path_uri).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        isLoading=false;
                        imageView.setImageDrawable(resource);
                        bitmap=((BitmapDrawable)resource).getBitmap();
                        loading.setVisibility(View.INVISIBLE);

                    }
                });
                break;


            case VIDEO:
                imageView.setVisibility(View.INVISIBLE);
                //setEnable(false);
                try {
                    Uri uri = Uri.parse(path_uri);
                    videoView.setVideoURI(uri);
                    videoView.start();
                    videoView.requestFocus();

                    videoView.setOnPreparedListener(mp -> {
                        isLoading=false;
                        loading.setVisibility(View.INVISIBLE);
                    });

                    videoView.setOnCompletionListener(mp -> {
                        videoView.start();
                        videoView.requestFocus();
                    });
                }catch (Exception e){
                    Log.e("ERROR Video view",e.getMessage());
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }





        }


        download.setOnClickListener(v -> {
            if (!isDownload&&!isLoading) {
                switch (type){
                    case IMAGE:
                        saveSubscribe(saveImage());
                        break;

                    case VIDEO:
                        saveSubscribe(saveVideo());
                }


            }else {
                if (isLoading){
                    Toast.makeText(getContext(),"انتظر حتى يتم التحميل",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getContext(), "تم الحفظ بالفعل", Toast.LENGTH_SHORT).show();

                }
            }

        }

        );

        share.setOnClickListener(v -> {
                    if (!isDownload&&!isLoading) {

                        switch (type){
                            case IMAGE:
                                shareSubscribe(saveImage());
                                break;

                            case VIDEO:
                                shareSubscribe(saveVideo());
                        }


                    }else if(file!=null&&!isLoading) {
                       shareToWhatsApp(file);
                    }
                }
            );









    }



    private Single<String> saveImage(){
        return  Single.create(emitter -> {
          //  String[] uri_arr=uri.split("/");
            //String[] name_arr=Uri.parse(uri).getLastPathSegment().split(".");
           // String name=name_arr[0];
            try {
                String path=ReadAndWriteToFile.writeImageToSpecificFile(getContext(),bitmap,Uri.parse(path_uri).getLastPathSegment());
                if (path!=null){

                    if (!emitter.isDisposed()){

                        emitter.onSuccess(path);
                    }
                }
            }catch (Exception e){
                if (!emitter.isDisposed()){
                    emitter.onError(e);
                }
            }

            ;
        });
    }






    private Single<String> saveVideo(){
        progressdialog.show();
        return  Single.create(emitter -> {

            try {
                String path=ReadAndWriteToFile.downLoadAndSsaveVideo(path_uri,Uri.parse(path_uri).getLastPathSegment(),percentage -> {
                    // Loading Percentage
                    if (percentage<100) {
                        progressdialog.setProgress(percentage);
                    }else {
                        progressdialog.dismiss();
                    }

                });
                if (path!=null){

                    if (!emitter.isDisposed()){

                        emitter.onSuccess(path);
                    }
                }
            }catch (Exception e){
                if (!emitter.isDisposed()){
                    emitter.onError(e);
                }
            }

            ;
        });
    }


    private void shareToWhatsApp(File file){
        String media_type;
        if (type==IMAGE) {
            media_type = "image/*";
        }else {
             media_type = "video/*";
        }

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(media_type);

        // Create the URI from the media
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID+".fileProvider", file);
        share.setPackage("com.whatsapp");
        // Add the URI to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PackageManager packageManager = getActivity().getPackageManager();
        if (share.resolveActivity(packageManager) != null) {
           // startActivity(share);
            startActivity(Intent.createChooser(share, "Share to"));

        } else {
            Toast.makeText(getContext(), "ليس لديك تطبيق WhatsApp", Toast.LENGTH_SHORT).show();
        }


    }


    private void saveSubscribe(Single<String> stringSingle){
        progressBar.setVisibility(View.VISIBLE);
        setEnable(false);
        stringSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        setEnable(true);
                        file = new File(s);
                        isDownload = true;
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "تم الحفظ بنجاح", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        setEnable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("ERROR Save Image", e.getMessage());
                    }
                });


    }


    private void shareSubscribe(Single<String> stringSingle){

        setEnable(false);
        progressBar.setVisibility(View.VISIBLE);
        stringSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s!=null) {
                            setEnable(true);
                            file = new File(s);
                            isDownload = true;
                            progressBar.setVisibility(View.INVISIBLE);
                            shareToWhatsApp(file);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        setEnable(true);
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.e("ERROR Save Image", e.getMessage());
                    }
                });

    }


    private void setEnable(boolean b ){
        download.setEnabled(b);
        share.setEnabled(b);
    }


}
