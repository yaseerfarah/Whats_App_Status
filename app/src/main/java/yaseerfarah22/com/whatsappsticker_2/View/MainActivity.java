package yaseerfarah22.com.whatsappsticker_2.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import yaseerfarah22.com.whatsappsticker_2.Interface.InternetStatus;
import yaseerfarah22.com.whatsappsticker_2.Interface.PermissionsListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.PermissionsStatus;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.Util.NetworkReceiver;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.CategoryViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, InternetStatus, PermissionsStatus {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelFactory viewModelFactory;

    public static  boolean isPermissions;
    private static final int REQUEST_CODE=500;
    StickersViewModel stickersViewModel;
    CategoryViewModel categoryViewModel;
    NetworkReceiver networkReceiver;
    private PermissionsListener permissionsListener;

    public static boolean isOnline=false;
    private String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        stickersViewModel= ViewModelProviders.of(this,viewModelFactory).get(StickersViewModel.class);
        categoryViewModel= ViewModelProviders.of(this,viewModelFactory).get(CategoryViewModel.class);

        networkReceiver=new NetworkReceiver(this);

        stickersViewModel.getAllStickerPacks();
       // categoryViewModel.getCategoryData();


      // checkPermissions();


    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver,netFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
        categoryViewModel.onClear();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void Connect() {
        isOnline=true;
    }

    @Override
    public void notConnect() {
        isOnline=false;
        //categoryViewModel.onClear();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode==REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionsListener!=null) {
                    permissionsListener.onSuccess();
                }
                isPermissions = true;


            }else {
                if (permissionsListener!=null) {
                    permissionsListener.onFailed();
                }
            }
        }

    }

    @Override
    public void checkPermissions(){
        boolean permissionCheck1 = ContextCompat.checkSelfPermission(this, reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(this, reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;


        if (!(permissionCheck1 && permissionCheck2)){
            isPermissions=false;
            // If permissions are not already granted, request permission from the user.
            ActivityCompat.requestPermissions(this, reqPermissions, REQUEST_CODE);
           // finish();
        }else {
            isPermissions=true;
        }


    }

    public void  setPermissionsListener(PermissionsListener permissionsListener){
        this.permissionsListener=permissionsListener;
    }

}
