package yaseerfarah22.com.whatsappsticker_2.View;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropBitmap;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropModel;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import android.os.Parcelable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;
import static yaseerfarah22.com.whatsappsticker_2.View.AddSticker.IMAGE_NUM;


/**
 * A simple {@link Fragment} subclass.
 */
public class CropImage extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    StickersViewModel stickersViewModel;


    private CropImageView cropImageView;
    private ImageButton gallery,done,r_Right,r_Left,c_Square,c_Circle,c_Freehand;
    private ProgressBar progressBar;


    private Path clipPath;
    private Bitmap bmp;
    private Bitmap alteredBitmap;
    private Canvas canvas;
    private Paint paint;
    private float downx = 0;
    private float downy = 0;
    private float tdownx = 0;
    private float tdowny = 0;
    private float upx = 0;
    private float upy = 0;
    private long lastTouchDown = 0;
    private Display display;
    private Point size;
    private int screen_width,screen_height;
    private ArrayList<CropModel> cropModelArrayList;
    private float smallx,smally,largex,largey;
    private int lastRotate=0;
    private int lastR=0;
    private int lastRC=0;
    private int diff=0;
    private CropBitmap cropBitmap;
    private boolean is_gallery;
    private boolean is_c_freehand;




    private boolean is_Pick,isCropEnable,isDone,isCanvas,isStart;


    private Bitmap bitmap;
    private Uri imageUri;
    private int imageNum;
    private List<CropBitmap> cropBitmaps;

    private final int PICK_FROM_GALLARY=400;




    public CropImage() {
        // Required empty public constructor
        is_gallery=false;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        AndroidSupportInjection.inject(this);



        stickersViewModel= ViewModelProviders.of(this,viewModelFactory).get(StickersViewModel.class);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crop_image, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        imageNum= getArguments().getInt(IMAGE_NUM);

        gallery=(ImageButton)view.findViewById(R.id.gal);
        done=(ImageButton)view.findViewById(R.id.done);
        r_Left=(ImageButton)view.findViewById(R.id.r_left);
        r_Right=(ImageButton)view.findViewById(R.id.r_right);
        c_Circle=(ImageButton)view.findViewById(R.id.c_cr);
        c_Square=(ImageButton)view.findViewById(R.id.c_sq);
        c_Freehand=(ImageButton)view.findViewById(R.id.c_fr);
        cropImageView=(CropImageView)view.findViewById(R.id.cropImageView);
        progressBar=(ProgressBar)view.findViewById(R.id.prog);






        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery=new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(gallery,PICK_FROM_GALLARY);
            }
        });

        is_Pick=false;
        isCropEnable=true;
        isDone=false;
        isCanvas=false;
        isStart=false;
        cropImageView.setCropEnabled(isCropEnable);
        cropImageView.setCropMode(CropImageView.CropMode.SQUARE);




        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone){
                    cropImageView.crop(null).execute(new CropCallback() {
                        @Override
                        public void onSuccess(Bitmap cropped) {
                            bitmap=cropped;
                            cropImageView.setImageBitmap(bitmap);

                            isCropEnable=false;
                            cropImageView.setCropEnabled(isCropEnable);
                            isDone=true;
                            Toast.makeText(getContext(),getResources().getString(R.string.press_again),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();


                        }
                    });
                }else if (isDone){

                    progressBar.setVisibility(View.VISIBLE);
                    done.setEnabled(false);
                   stickersViewModel.addCropBitmab(new CropBitmap(imageNum,imageUri,bitmap));
                    getActivity().onBackPressed();
                    //Toast.makeText(CropImage.this,"Done",Toast.LENGTH_LONG).show();

                }
            }
        });




        r_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone) {

                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    lastR=lastRotate;
                    lastRotate-=90;
                }else if(is_Pick&&isCanvas&&!isDone) {
                    rotate(alteredBitmap,-90);

                    // startFreehandCrop(rotateBitmap(((BitmapDrawable)cropImageView.getDrawable()).getBitmap(),-90));
                }


            }
        });



        r_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone) {
                    cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    lastR=lastRotate;
                    lastRotate+=90;
                }else if (is_Pick&&isCanvas&&!isDone){
                    rotate(alteredBitmap,90);

                }
            }
        });




        c_Circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone||is_Pick&&isCanvas&&!isDone) {

                    cropImageView.setImageBitmap(bitmap);

                    stopFreehandCrop();
                    isCropEnable=true;
                    cropImageView.setCropEnabled(isCropEnable);
                    cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
                    stopFreehandCrop();
                    countRotate();
                }
            }
        });


        c_Square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_Pick&&isCropEnable&&!isDone||is_Pick&&isCanvas&&!isDone) {


                    cropImageView.setImageBitmap(bitmap);

                    stopFreehandCrop();
                    isCropEnable=true;
                    cropImageView.setCropEnabled(isCropEnable);
                    cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
                    stopFreehandCrop();
                    countRotate();
                }
            }
        });


        c_Freehand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_Pick&&!isDone&&!isCanvas) {
                    isCropEnable = false;
                    cropImageView.setCropEnabled(isCropEnable);
                    startFreehandCrop(((BitmapDrawable)cropImageView.getDrawable()).getBitmap());
                }


            }
        });


        cropBitmap=stickersViewModel.getSpicificBitmap(imageNum);
        if (cropBitmap!=null){
            lastRotate=0;
            stopFreehandCrop();
            is_Pick=true;
            isCropEnable=true;
            isDone=false;
            cropImageView.setCropEnabled(isCropEnable);
            imageUri=cropBitmap.getUri();
            bitmap=cropBitmap.getBitmap();
            cropImageView.setImageBitmap(cropBitmap.getBitmap());
        }else {

            //Open Gallery
            if (!is_gallery) {
                is_gallery=true;
                Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_FROM_GALLARY);

            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK){


            switch (requestCode){


                case PICK_FROM_GALLARY:
                    lastRotate=0;
                    diff=0;
                    lastR=0;
                    lastRC=0;
                    stopFreehandCrop();
                    is_Pick=true;
                    isCropEnable=true;
                    isDone=false;
                    cropImageView.setCropEnabled(isCropEnable);
                    imageUri=data.getData();
                    cropImageView.load(data.getData()).execute(new LoadCallback() {
                        @Override
                        public void onSuccess() {
                            bitmap=cropImageView.getImageBitmap();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });

                    break;

            }

        }

    }

    private byte[] getBitmapByte(Bitmap bmp){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }


    private void startFreehandCrop(Bitmap bitmap){

        isCanvas=true;
        init(bitmap);




        Rect rect;
        int orientation = getResources().getConfiguration().orientation;
            rect = new Rect(0,0,cropImageView.getWidth(), cropImageView.getHeight());




        if (lastR!=lastRotate) {
            //lastR=lastRotate;

            canvas.save();

            if (lastRC!=lastRotate) {
            if (lastRotate>lastRC){
                diff=diff+(lastRotate-lastRC);
            }else {
                diff=diff+(lastRotate+lastRC);
            }


                //diff = lastRotate - lastRC;
                lastRC=lastRotate;
            }
            canvas.rotate(diff, rect.exactCenterX(), rect.exactCenterY());

            if (orientation == Configuration.ORIENTATION_LANDSCAPE){

                Bitmap bitmap1=Bitmap.createScaledBitmap(bmp,cropImageView.getHeight(),cropImageView.getHeight(),false);

                canvas.drawBitmap(bitmap1, (cropImageView.getWidth()-bitmap1.getWidth())/2,(cropImageView.getHeight()-bitmap1.getHeight())/2  , null);


            }else{
                canvas.drawBitmap(bmp, null,rect, null);
            }


            canvas.restore();
            cropImageView.invalidate();
        }else {
            canvas.save();
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){

                Bitmap bitmap1=Bitmap.createScaledBitmap(bmp,cropImageView.getHeight(),cropImageView.getHeight(),false);

                canvas.drawBitmap(bitmap1, (cropImageView.getWidth()-bitmap1.getWidth())/2,(cropImageView.getHeight()-bitmap1.getHeight())/2  , null);


            }else{
                canvas.drawBitmap(bmp, null,rect, null);
            }
            canvas.restore();
        }


        cropImageView.setImageBitmap(alteredBitmap);

        cropImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ontouch(event);
                return true;
            }
        });
    }


    private void stopFreehandCrop(){

        if (canvas!=null){
            isCanvas=false;
            canvas.drawARGB(0, 0, 0, 0);
            canvas=null;
            cropImageView.invalidate();
            cropImageView.setOnTouchListener(null);
        }

    }


    private void init(Bitmap bitmap) {

        cropModelArrayList = new ArrayList<>();

        display = getActivity().getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;


        initcanvas(bitmap);
    }



    private void initcanvas(Bitmap bitmap) {

        Drawable d = cropImageView.getDrawable();
        bmp = ((BitmapDrawable)d).getBitmap();

        alteredBitmap = Bitmap.createBitmap(cropImageView.getWidth(), cropImageView.getHeight(), bitmap.getConfig());
        canvas = new Canvas(alteredBitmap);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);

        paint.setStyle(Paint.Style.STROKE);

    }


    private void ontouch(MotionEvent event){


        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:


                downx = event.getX();
                downy = event.getY();
                clipPath = new Path();
                clipPath.moveTo(downx, downy);
                tdownx = downx;
                tdowny = downy;
                smallx = downx;
                smally = downy;
                largex = downx;
                largey = downy;
                lastTouchDown = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:

                upx = event.getX()>cropImageView.getWidth()?cropImageView.getWidth(): event.getX();
                upy = event.getY()>cropImageView.getHeight()?cropImageView.getHeight(): event.getY();
                cropModelArrayList.add(new CropModel(upx, upy));
                clipPath = new Path();
                clipPath.moveTo(tdownx,tdowny);
                for(int i = 0; i<cropModelArrayList.size();i++){
                    clipPath.lineTo(cropModelArrayList.get(i).getY(),cropModelArrayList.get(i).getX());
                }
                canvas.drawPath(clipPath, paint);
                cropImageView.invalidate();
                downx = upx;
                downy = upy;
                break;

            case MotionEvent.ACTION_UP:

                if (!clipPath.isEmpty()&&cropModelArrayList.size()>10) {
                    if (upx != upy) {
                        upx = event.getX();
                        upy = event.getY();


                        canvas.drawLine(downx, downy, upx, upy, paint);
                        clipPath.lineTo(upx, upy);
                        cropImageView.invalidate();

                        crop();
                    }
                }else {
                    canvas = new Canvas(alteredBitmap);
                }


                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }


    }



    public void crop() {

        clipPath.close();
        clipPath.setFillType(Path.FillType.INVERSE_WINDING);

        for(int i = 0; i<cropModelArrayList.size();i++){
            if(cropModelArrayList.get(i).getY()<smallx&&cropModelArrayList.get(i).getY()>=0){

                smallx=cropModelArrayList.get(i).getY();
            }
            if(cropModelArrayList.get(i).getX()<smally&&cropModelArrayList.get(i).getX()>=0){

                smally=cropModelArrayList.get(i).getX();
            }
            if(cropModelArrayList.get(i).getY()>largex){

                largex=cropModelArrayList.get(i).getY();
            }
            if(cropModelArrayList.get(i).getX()>largey){

                largey=cropModelArrayList.get(i).getX();
            }
        }

        save();

    }


    private void save() {

        if(clipPath != null) {
            final int color = 0xff424242;
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawPath(clipPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            canvas.drawBitmap(alteredBitmap, 0, 0, paint);

            float w = largex - smallx;
            float h = largey - smally;
            bitmap = Bitmap.createBitmap(alteredBitmap, (int) smallx, (int) smally, (int) w, (int) h);



        }else{
            bitmap = bmp;
        }

        cropImageView.setImageBitmap(bitmap);
        stopFreehandCrop();
        isDone=true;



    }




    private void rotate(Bitmap bitmap, int rotationAngleDegree){

        Rect rect;
        Bitmap bitmap1=Bitmap.createScaledBitmap(bmp,cropImageView.getHeight(),cropImageView.getHeight(),false);
        int orientation = getResources().getConfiguration().orientation;
             rect = new Rect(0,0,cropImageView.getWidth(), cropImageView.getHeight());


        canvas.drawColor(Color.WHITE);
        canvas.save();
        diff=diff+rotationAngleDegree;
        canvas.rotate(diff,rect.exactCenterX(),rect.exactCenterY());
        lastR=lastRotate;

        lastRotate=diff;
        lastRC=lastRotate;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape

            canvas.drawBitmap(bitmap1, (cropImageView.getWidth()-bitmap1.getWidth())/2,(cropImageView.getHeight()-bitmap1.getHeight())/2  , null);


        } else {
            // In portrait
            canvas.drawBitmap(bmp, null, new Rect(0,0,cropImageView.getWidth(), cropImageView.getHeight()), null);

        }
        canvas.restore();
        cropImageView.invalidate();
    }




    private void countRotate(){


            boolean reverse;
           // Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();

            if (lastRotate > 0) {
                reverse = false;
            } else {
                reverse = true;
            }


            int rotate = lastRotate / 90;
            while (rotate > 4) {

                rotate = rotate - 4;

            }

            switch (rotate) {
                case 4:
                    break;
                case 3:
                    //Toast.makeText(getContext(),String.valueOf(rotate),Toast.LENGTH_SHORT).show();
                    if (!reverse) {
                        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_270D);
                    } else {
                        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M270D);
                    }
                    break;
                case 2:
                    if (!reverse) {
                        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_180D);
                    } else {
                        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M180D);
                    }
                    break;
                case 1:
                    if (!reverse) {
                        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    } else {
                        cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    }

            }



    }





}
