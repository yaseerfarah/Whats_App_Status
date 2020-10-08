package yaseerfarah22.com.whatsappsticker_2.View;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import dagger.android.support.AndroidSupportInjection;
import q.rorbin.badgeview.QBadgeView;
import yaseerfarah22.com.whatsappsticker_2.Adapter.CategoryViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Interface.BadgeListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageAndVideo;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategories;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategoryImage;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropBitmap;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.CategoryViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails.CLIP_IMAGE;
import static yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails.IS_CLIP;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageCategory extends Fragment implements BadgeListener {



    @Inject
    ViewModelFactory viewModelFactory;

    CategoryViewModel categoryViewModel;
    StickersViewModel stickersViewModel;

    private Observer parentCategoryObserver;
    private List<SubCategoryImage> categoryName=new ArrayList<>();

    private RecyclerView recyclerView;
    private CategoryViewAdapter categoryViewAdapter;
    private Button toCrop;
    private NavController navController;
    private int id;
    private  HashMap<String,List<String>> stringListHashMap;
    private  List<String> title;
    private  List<String> image;
    private QBadgeView clipBadge;
    private Observer cropBitmapObserver;


    public ImageCategory() {
        // Required empty public constructor

         title=new ArrayList<>();
         image=new ArrayList<>();




        image.add("https://www.alyuwm.com/wp-content/uploads/2019/12/qSUvvY8.png");
        image.add("https://ladybirdar.com/wp-content/uploads/2017/12/%D8%A7%D8%AC%D9%85%D9%84-%D8%B5%D9%88%D8%B1-%D8%AF%D8%B9%D8%A7%D8%A1-%D8%A7%D9%84%D8%B3%D9%86%D8%A9-4.jpg");
        image.add("https://ahdath-alyom.com/wp-content/uploads/2019/08/%D8%AF%D8%B9%D8%A7%D8%A1-%D8%A7%D9%84%D8%B9%D8%A7%D9%85-%D8%A7%D9%84%D9%87%D8%AC%D8%B1%D9%8A-%D8%A7%D9%84%D8%AC%D8%AF%D9%8A%D8%AF-1441.jpg");



        stringListHashMap=new HashMap<>();

        parentCategoryObserver=new Observer<List<SubCategoryImage>>() {
            @Override
            public void onChanged(List<SubCategoryImage> subCategoryImages) {
                categoryName.clear();

                for (SubCategoryImage subCategoryImage:subCategoryImages){

                    if (subCategoryImage.getParent_id()==id&&subCategoryImage.getImageAndVideos().size()>0){
                        categoryName.add(subCategoryImage);
                    }
                }


                categoryViewAdapter.notifyDataSetChanged();
               // recyclerView.smoothScrollToPosition(0);

            }
        };



        cropBitmapObserver=new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> cropBitmapList) {
                addBadge(cropBitmapList.size());

            }
        };


    }



    @Override
    public void onStart() {
        super.onStart();
        categoryViewModel.getSubCategoryLiveData().observe(this,parentCategoryObserver);
        stickersViewModel.getClipBoardLiveData().observe(this,cropBitmapObserver);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

    }

    @Override
    public void onStop() {
        super.onStop();
        categoryViewModel.getSubCategoryLiveData().removeObservers(this);
        stickersViewModel.getClipBoardLiveData().removeObservers(this);
        categoryName.clear();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        AndroidSupportInjection.inject(this);

       id= getArguments().getInt("ID");
       //Toast.makeText(getContext(),String.valueOf(id),Toast.LENGTH_LONG).show();

        categoryViewModel= ViewModelProviders.of(this,viewModelFactory).get(CategoryViewModel.class);

        stickersViewModel= ViewModelProviders.of(this,viewModelFactory).get(StickersViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_category, container, false);




    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView=(RecyclerView)view.findViewById(R.id.cat_recycle);
        toCrop=(Button)view.findViewById(R.id.to_crop);


        navController= Navigation.findNavController(view);


        clipBadge=(QBadgeView) new QBadgeView(getContext())
                .setGravityOffset(toCrop.getWidth(), toCrop.getHeight(), true)
                .bindTarget(toCrop);

        categoryViewAdapter=new CategoryViewAdapter(getContext(),categoryName,navController,categoryViewModel.clipImage,this,stickersViewModel,getResources());

        recyclerView.setAdapter(categoryViewAdapter);



        toCrop.setOnClickListener(v -> {

            Bundle bundle=new Bundle();
            bundle.putBoolean(IS_CLIP,true);
            bundle.putSerializable(CLIP_IMAGE, (ArrayList<ImageAndVideo>) categoryViewModel.clipImage);
            navController.navigate(R.id.action_imageCategory_to_categoryDetails,bundle);

        });


    }





    @Override
    public void addBadge(int number) {
        // add badge

        clipBadge.setVisibility(View.VISIBLE);
        clipBadge.setBadgeNumber(number);

    }

    @Override
    public void done() {
        toCrop.setText(getResources().getString(R.string.confirm_img));
    }

    @Override
    public void notDone() {
        toCrop.setText(getResources().getString(R.string.add_4_img));
    }
}
