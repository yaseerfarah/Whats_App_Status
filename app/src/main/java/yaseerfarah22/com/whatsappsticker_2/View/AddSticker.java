package yaseerfarah22.com.whatsappsticker_2.View;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.whatsappsticker_2.BuildConfig;
import yaseerfarah22.com.whatsappsticker_2.Data.ReadAndWriteToFile;
import yaseerfarah22.com.whatsappsticker_2.Data.StickerContentProvider;
import yaseerfarah22.com.whatsappsticker_2.Interface.AddToWhatsAppListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.PermissionsListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.PermissionsStatus;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropBitmap;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.CategoryViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.os.ParcelFileDescriptor.MODE_READ_ONLY;

import static yaseerfarah22.com.whatsappsticker_2.View.MainActivity.isPermissions;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddSticker extends Fragment implements PermissionsListener {

    public static final String IMAGE_NUM="Image_Num";
    private static final int REQUEST_CODE=500;

    @Inject
    ViewModelFactory viewModelFactory;

    StickersViewModel stickersViewModel;
    CategoryViewModel categoryViewModel;

    private Observer stickerObserver;
    private List<CropBitmap> cropBitmapList=new ArrayList<>();
    private ReadAndWriteToFile readAndWriteToFile;


    private CardView cat_card,card1,card2,card3;

    private ImageView cat_img,img1,img2,img3,cat_icon, icon1,icon2,icon3;
    private Button toWhatsApp;
    private ProgressBar progressBar;
    private NavController navController;
    private boolean isSaved=false;
    private String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};


    public AddSticker() {
        // Required empty public constructor

        stickerObserver= new Observer<List<CropBitmap>>() {
            @Override
            public void onChanged(List<CropBitmap> cropBitmaps) {
                cropBitmapList.clear();
                cropBitmapList.addAll(cropBitmaps);
               // Toast.makeText(getContext(),String.valueOf(cropBitmaps.size()),Toast.LENGTH_SHORT).show();


                setImage();
                isButtonEnable();


            }
        };



    }



    @Override
    public void onStart() {
        super.onStart();
        //stickersViewModel.getAllStickerPacks();
        stickersViewModel.getCropBitmapLiveData().observe(this,stickerObserver);
        ((PermissionsStatus)getActivity()).setPermissionsListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        stickersViewModel.getCropBitmapLiveData().removeObservers(this);
        cropBitmapList.clear();
        ((PermissionsStatus)getActivity()).setPermissionsListener(null);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        AndroidSupportInjection.inject(this);
        readAndWriteToFile=new ReadAndWriteToFile(getContext());

        stickersViewModel= ViewModelProviders.of(this,viewModelFactory).get(StickersViewModel.class);
        categoryViewModel=ViewModelProviders.of(this,viewModelFactory).get(CategoryViewModel.class);




    }


    @Override
    public void onResume() {
        super.onResume();
        if (stickersViewModel.getStickerPack().getStickers().size()>0){
            isSaved=true;
        }else {
            isSaved=false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_sticker, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        cat_img=(ImageView)view.findViewById(R.id.tray_icon_image);
        img1=(ImageView)view.findViewById(R.id.st_1_image);
        img2=(ImageView)view.findViewById(R.id.st_2_image);
        img3=(ImageView)view.findViewById(R.id.st_3_image);
        cat_icon=(ImageView) view.findViewById(R.id.tray_icon);
        icon1=(ImageView)view.findViewById(R.id.st_1_icon);
        icon2=(ImageView)view.findViewById(R.id.st_2_icon);
        icon3=(ImageView)view.findViewById(R.id.st_3_icon);

        cat_card=(CardView)view.findViewById(R.id.tray_card);
        card1=(CardView)view.findViewById(R.id.st_1);
        card2=(CardView)view.findViewById(R.id.st_2);
        card3=(CardView)view.findViewById(R.id.st_3);
        progressBar=(ProgressBar)view.findViewById(R.id.prog);

        if (stickersViewModel.getStickerPack().getStickers().size()>0){
            isSaved=true;
        }else {
            isSaved=false;
        }
        toWhatsApp=(Button)view.findViewById(R.id.to_whatsapp);
        isButtonEnable();

        if (isSaved){
            ((PermissionsStatus)getActivity()).checkPermissions();
            reLoadImage(stickersViewModel.getStickerPack().getTrayImageFile(),cat_img,cat_icon);
            reLoadImage(stickersViewModel.getStickerPack().getStickers().get(0).imageFileName,img1,icon1);
            reLoadImage(stickersViewModel.getStickerPack().getStickers().get(1).imageFileName,img2,icon2);
            reLoadImage(stickersViewModel.getStickerPack().getStickers().get(2).imageFileName,img3,icon3);

        }

        navController= Navigation.findNavController(view);

        cat_card.setOnClickListener(v -> {
            if(!isSaved) {
                Bundle bundle = new Bundle();
                bundle.putInt(IMAGE_NUM, 1);
                navController.navigate(R.id.action_addSticker_to_cropImage, bundle);
            }

        });
        card1.setOnClickListener(v -> {
            if(!isSaved) {
                Bundle bundle = new Bundle();
                bundle.putInt(IMAGE_NUM, 2);
                navController.navigate(R.id.action_addSticker_to_cropImage, bundle);
            }

        });

        card2.setOnClickListener(v -> {
            if(!isSaved) {
                Bundle bundle = new Bundle();
                bundle.putInt(IMAGE_NUM, 3);
                navController.navigate(R.id.action_addSticker_to_cropImage, bundle);
            }

        });
        card3.setOnClickListener(v -> {

            if(!isSaved) {
                Bundle bundle = new Bundle();
                bundle.putInt(IMAGE_NUM, 4);
                navController.navigate(R.id.action_addSticker_to_cropImage, bundle);
            }

        });


        toWhatsApp.setOnClickListener(v -> {

            ((PermissionsStatus)getActivity()).checkPermissions();




            if (isPermissions){
                progressBar.setVisibility(View.VISIBLE);
                addToWhatsApp();

            }else {
                ((PermissionsStatus)getActivity()).checkPermissions();
            }




        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "hi", Toast.LENGTH_LONG).show();

            addToWhatsApp();


        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.INVISIBLE);

        if(requestCode==500){

            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                final String validationError = data.getStringExtra("validation_error");
                if (validationError != null) {
                    if (Boolean.parseBoolean("true")) {
                        //error should be shown to developer only, not users.
                    }
                    Log.e("ERROR WHATSAPP", "Validation failed:" + validationError);

                    Toast.makeText(getContext(), validationError, Toast.LENGTH_LONG).show();
                }
            }else if (resultCode==Activity.RESULT_OK){


                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent);
            }

        }


    }

    private void setImage(){

        if (stickersViewModel.getStickerPack().getTrayImageFile().trim().length()==0) {
            for (int i = 0; i < cropBitmapList.size(); i++) {

                switch (cropBitmapList.get(i).getImgNumper()) {
                    case 1:
                        cat_icon.setVisibility(View.INVISIBLE);
                        cat_img.setImageBitmap(cropBitmapList.get(i).getBitmap());
                        break;
                    case 2:
                        icon1.setVisibility(View.INVISIBLE);
                        img1.setImageBitmap(cropBitmapList.get(i).getBitmap());
                        break;
                    case 3:
                        icon2.setVisibility(View.INVISIBLE);
                        img2.setImageBitmap(cropBitmapList.get(i).getBitmap());
                        break;
                    case 4:
                        icon3.setVisibility(View.INVISIBLE);
                        img3.setImageBitmap(cropBitmapList.get(i).getBitmap());
                        break;

                }

            }
        }

    }



    private void reLoadImage(String name,ImageView imageView,ImageView icon){

        if (isPermissions) {

            try {
                File imageDir = new File(readAndWriteToFile.getStickerFile(), stickersViewModel.getStickerPack().identifier);
                File file = new File(imageDir, name);

                AssetFileDescriptor assetFileDescriptor = new AssetFileDescriptor(ParcelFileDescriptor.open(file, MODE_READ_ONLY), 0, file.length());
                final FileDescriptor fileDescriptor = assetFileDescriptor.getParcelFileDescriptor().getFileDescriptor();
                final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                imageView.setImageBitmap(bitmap);
                icon.setVisibility(View.INVISIBLE);
            } catch (FileNotFoundException e) {
                Log.e("ERROR", e.getMessage());
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }

    }


    private void isButtonEnable(){

        if (cropBitmapList.size()==4&&stickersViewModel.getStickerPack().getTrayImageFile().trim().length()==0){
            toWhatsApp.setEnabled(true);
        }else {
            toWhatsApp.setEnabled(false);
        }

    }


    private void addToWhatsApp() {

        stickersViewModel.addToWhatsApp(new AddToWhatsAppListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(getContext(), "hi", Toast.LENGTH_LONG).show();

                String identifier=stickersViewModel.getStickerPack().identifier;
                String authority= BuildConfig.CONTENT_PROVIDER_AUTHORITY;
                String stickerPackName=stickersViewModel.getStickerPack().name;

                getActivity().getContentResolver().call(StickerContentProvider.CONTENT_URI,"on_create",null,null);

                Intent intent = new Intent();
                intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
                intent.putExtra("sticker_pack_id", identifier); //identifier is the pack's identifier in contents.json file
                intent.putExtra("sticker_pack_authority", authority); //authority is the ContentProvider's authority. In the case of the sample app it is BuildConfig.CONTENT_PROVIDER_AUTHORITY.
                intent.putExtra("sticker_pack_name", stickerPackName); //stickerPackName is the name of the sticker pack.
                try {

                    startActivityForResult(intent, 500);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                categoryViewModel.clearClipImage();
                stickersViewModel.clearCropBitmap();
                stickersViewModel.setClipBoardList(new ArrayList<>());
                requireActivity().getOnBackPressedDispatcher().addCallback(AddSticker.this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        navController.popBackStack(R.id.home2,false);
                    }
                });


            }

            @Override
            public void onFailed() {
                Toast.makeText(getContext(), "Some Thing Wrong", Toast.LENGTH_LONG).show();
            }
        });



    }


    @Override
    public void onSuccess() {
       // Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();

        if (isSaved){
            reLoadImage(stickersViewModel.getStickerPack().getTrayImageFile(),cat_img,cat_icon);
            reLoadImage(stickersViewModel.getStickerPack().getStickers().get(0).imageFileName,img1,icon1);
            reLoadImage(stickersViewModel.getStickerPack().getStickers().get(1).imageFileName,img2,icon2);
            reLoadImage(stickersViewModel.getStickerPack().getStickers().get(2).imageFileName,img3,icon3);

        }else {
            addToWhatsApp();

        }
       //
    }

    @Override
    public void onFailed() {
        Toast.makeText(getContext(), "Please allow the Permissions ", Toast.LENGTH_SHORT).show();

    }
}
