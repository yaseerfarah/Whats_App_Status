package yaseerfarah22.com.whatsappsticker_2.View;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.whatsappsticker_2.Adapter.HomeCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Interface.PermissionsListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.PermissionsStatus;
import yaseerfarah22.com.whatsappsticker_2.POJO.Sticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static yaseerfarah22.com.whatsappsticker_2.View.MainActivity.isPermissions;


public class Home extends Fragment implements PermissionsListener {



    @Inject
    ViewModelFactory viewModelFactory;

    StickersViewModel stickersViewModel;
    private static final int REQUEST_CODE=500;

    private Observer stickerObserver;
    private List<StickerPack> stickerPackList=new ArrayList<>();


    private Button addPacks;
    private RecyclerView recyclerView;
    private HomeCardViewAdapter homeCardViewAdapter;
    private NavController navController;

    private String[] reqPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE};

    public Home() {
        // Required empty public constructor


        stickerObserver= new Observer<List<StickerPack>>() {
            @Override
            public void onChanged(List<StickerPack> subCategoryImages) {
                stickerPackList.clear();
                // Toast.makeText(getContext(),String.valueOf(subCategoryImages.size()),Toast.LENGTH_LONG).show();
                stickerPackList.addAll(subCategoryImages);
                if (isPermissions){
                    homeCardViewAdapter.notifyDataSetChanged();

                }

            }
        };



    }



    @Override
    public void onStart() {
        super.onStart();
        //stickersViewModel.getAllStickerPacks();
        stickersViewModel.getSteckerPacksLiveData().observe(this,stickerObserver);
        ((PermissionsStatus)getActivity()).setPermissionsListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        stickersViewModel.getSteckerPacksLiveData().removeObservers(this);
        stickerPackList.clear();
        ((PermissionsStatus)getActivity()).setPermissionsListener(null);
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        addPacks=(Button)view.findViewById(R.id.addto);
        recyclerView=(RecyclerView)view.findViewById(R.id.home_recycle);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        List<StickerPack> stickerPacks=new ArrayList<>();

        boolean permissionCheck1 = ContextCompat.checkSelfPermission(getContext(), reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(getContext(), reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;


        ((PermissionsStatus)getActivity()).checkPermissions();



        if (isPermissions){
            isPermissions=true;
            homeCardViewAdapter=new HomeCardViewAdapter(getContext(),stickerPackList,stickersViewModel,navController);

            recyclerView.setAdapter(homeCardViewAdapter);

        }else {
            ((PermissionsStatus)getActivity()).checkPermissions();
        }


        stickerPacks.add(new StickerPack("Sport","Mo","https://www.alyuwm.com/wp-content/uploads/2019/12/qSUvvY8.png"));
        stickerPacks.add(new StickerPack("Cars","Omar","https://ladybirdar.com/wp-content/uploads/2017/12/%D8%A7%D8%AC%D9%85%D9%84-%D8%B5%D9%88%D8%B1-%D8%AF%D8%B9%D8%A7%D8%A1-%D8%A7%D9%84%D8%B3%D9%86%D8%A9-4.jpg"));
        stickerPacks.add(new StickerPack("Memes","Ahmed","https://ahdath-alyom.com/wp-content/uploads/2019/08/%D8%AF%D8%B9%D8%A7%D8%A1-%D8%A7%D9%84%D8%B9%D8%A7%D9%85-%D8%A7%D9%84%D9%87%D8%AC%D8%B1%D9%8A-%D8%A7%D9%84%D8%AC%D8%AF%D9%8A%D8%AF-1441.jpg"));




        //recyclerView.smoothScrollToPosition(0);

        addPacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_dialog();
            }
        });







    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "hi", Toast.LENGTH_LONG).show();
                homeCardViewAdapter = new HomeCardViewAdapter(getContext(), stickerPackList, stickersViewModel, navController);

                recyclerView.setAdapter(homeCardViewAdapter);

                isPermissions = true;


            }
        }

    }





    private void create_dialog(){


        final Dialog dialog=new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);
        final EditText name=(EditText) dialog.findViewById(R.id.name_edit);
        final EditText author=(EditText) dialog.findViewById(R.id.author_edit);
        ImageButton done=(ImageButton) dialog.findViewById(R.id.add);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().length()>0&&author.getText().toString().trim().length()>0){

                    StickerPack stickerPack=new StickerPack(String.valueOf(stickerPackList.size()+1),name.getText().toString(),author.getText().toString(),new ArrayList<Sticker>(),"","","","","","1",false);
                    stickersViewModel.addStickerPack(stickerPack);
                    dialog.dismiss();
                    //Toast.makeText(MainActivity.this,"hi", Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(MainActivity.this,"by", Toast.LENGTH_LONG).show();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();





    }


    @Override
    public void onSuccess() {
       // Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
        homeCardViewAdapter = new HomeCardViewAdapter(getContext(), stickerPackList, stickersViewModel, navController);

        recyclerView.setAdapter(homeCardViewAdapter);
    }

    @Override
    public void onFailed() {
        Toast.makeText(getContext(), "Please allow the Permissions ", Toast.LENGTH_SHORT).show();


    }
}
