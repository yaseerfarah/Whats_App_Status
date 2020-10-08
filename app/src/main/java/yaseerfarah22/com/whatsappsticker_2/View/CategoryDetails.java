package yaseerfarah22.com.whatsappsticker_2.View;


import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import dagger.android.support.AndroidSupportInjection;
import q.rorbin.badgeview.QBadgeView;
import yaseerfarah22.com.whatsappsticker_2.Adapter.DetailsCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Adapter.ImageCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Interface.BadgeListener;
import yaseerfarah22.com.whatsappsticker_2.Interface.InternetStatus;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageAndVideo;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategoryImage;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropBitmap;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.whatsappsticker_2.Util.NetworkReceiver;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.CategoryViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import android.telecom.Call;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static yaseerfarah22.com.whatsappsticker_2.Adapter.ImageCardViewAdapter.DETAILS;
import static yaseerfarah22.com.whatsappsticker_2.View.MainActivity.isOnline;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryDetails extends Fragment implements BadgeListener ,InternetStatus {



    final String TAG="SCROOOOOLLL";

    public static final String IS_CLIP="IsClip";
    public static final String IS_PARENT="IsParent";
    public static final String CLIP_IMAGE="ClipImage";
    public static final String ID="ID";

    @Inject
    ViewModelFactory viewModelFactory;

    CategoryViewModel categoryViewModel;
    StickersViewModel stickersViewModel;

    private NestedScrollView nestedScrollView;
    private Observer cropBitmapObserver;
    private Observer subCategoryObserver;
    private List<ImageAndVideo> categoryImages=new ArrayList<>();
    private GridLayoutManager gridLayout;

    private int id;
    private boolean isParent;
    private boolean isClip;
    private int firstVisiblePosition;
    private boolean isScrolling=false;
    private boolean isLoading=false;
    private QBadgeView clipBadge;
    private SubCategoryImage subCategoryImage;
    private CategoryData categoryData;
    private RelativeLayout connFailed;
    private NetworkReceiver networkReceiver;
    private int space;
    private int space_land;
    private int clipBoardSize=0;

    Button toCrop;
    RecyclerView recyclerView;
    ImageCardViewAdapter imageCardViewAdapter;
    NavController navController;
    ProgressBar progressBar;




    public CategoryDetails() {
        // Required empty public constructor

        gridLayout=new GridLayoutManager(getContext(),2);


        subCategoryObserver=new Observer<List<SubCategoryImage>>() {
            @Override
            public void onChanged(List<SubCategoryImage> subCategoryImages) {
                categoryImages.clear();
                isLoading=false;
                progressBar.setVisibility(View.INVISIBLE);

                for (SubCategoryImage subCategory:subCategoryImages){

                    if (subCategory.getId()==id){
                        subCategoryImage=subCategory;
                        categoryImages.addAll(subCategoryImage.getImageAndVideos());
                       // Toast.makeText(getContext(),String.valueOf(id)+String.valueOf(categoryImages.size()),Toast.LENGTH_LONG).show();
                    }
                }

                imageCardViewAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(0);

            }
        };



        cropBitmapObserver=new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> cropBitmapList) {
                clipBoardSize=cropBitmapList.size();
               addBadge(cropBitmapList.size());

            }
        };




    }



    @Override
    public void onStart() {
        super.onStart();
        recyclerView.setLayoutManager(gridLayout);
        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);
        if (!isClip) {
            categoryViewModel.getSubCategoryLiveData().observe(this, subCategoryObserver);

        }
        stickersViewModel.getClipBoardLiveData().observe(this,cropBitmapObserver);
       // Toast.makeText(getContext(),String.valueOf(isClip),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isClip) {
            categoryViewModel.getSubCategoryLiveData().removeObservers(this);

            categoryImages.clear();
        }
        stickersViewModel.getClipBoardLiveData().removeObservers(this);
        getActivity().unregisterReceiver(networkReceiver);
        recyclerView.setLayoutManager(null);



    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        AndroidSupportInjection.inject(this);

        networkReceiver=new NetworkReceiver(this);


        //Toast.makeText(getContext(),String.valueOf(id),Toast.LENGTH_LONG).show();

        categoryViewModel= ViewModelProviders.of(this,viewModelFactory).get(CategoryViewModel.class);
        stickersViewModel=ViewModelProviders.of(this,viewModelFactory).get(StickersViewModel.class);


        isClip=getArguments().getBoolean(IS_CLIP);
        if (!isClip) {
            id = getArguments().getInt(ID);
            isParent=getArguments().getBoolean(IS_PARENT);
        }else {
            categoryImages.addAll(categoryViewModel.clipImage);
        }

       //Toast.makeText(getContext(),String.valueOf(isClip),Toast.LENGTH_SHORT).show();

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Toast.makeText(getContext(),"hi",Toast.LENGTH_SHORT).show();
        connFailed=(RelativeLayout)view.findViewById(R.id.failed_conn);
        navController= Navigation.findNavController(view);
        toCrop=(Button) view.findViewById(R.id.to_crop);
        recyclerView=(RecyclerView)view.findViewById(R.id.details_recycler);
        progressBar=(ProgressBar)view.findViewById(R.id.prog);
        nestedScrollView=(NestedScrollView)view.findViewById(R.id.nested_scroll);
        firstVisiblePosition=gridLayout.findFirstVisibleItemPosition();

        clipBadge=(QBadgeView) new QBadgeView(getContext())
                .setGravityOffset(toCrop.getWidth(), toCrop.getHeight(), true)
                .bindTarget(toCrop);








        imageCardViewAdapter=new ImageCardViewAdapter(getContext(),categoryImages,navController,categoryViewModel.clipImage,DETAILS,this,stickersViewModel,getResources());



        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                space=20;
                space_land=60;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                space=5;
                space_land=60;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                space=5;
                space_land=60;
                break;
        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(space_land,getResources()),GridSpacingItemDecoration.HomeLayout,width,(int)getResources().getDimension(R.dimen.image_card_width)));

        } else {
            // In portrait
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(space,getResources()),GridSpacingItemDecoration.HomeLayout,width,(int)getResources().getDimension(R.dimen.image_card_width)));

        }

        recyclerView.setAdapter(imageCardViewAdapter);
        recyclerView.smoothScrollToPosition(0);


        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Log.i(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Log.i(TAG, "TOP SCROLL");
                }

                if ((v.getChildAt(v.getChildCount()-1).getBottom()-(v.getHeight()+v.getScrollY()))==0){
                   // Toast.makeText(getContext(),"bottom",Toast.LENGTH_SHORT).show();
                    onScroll();
                }

            }
        });



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling=true;
                   // onScroll();

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });


        toCrop.setOnClickListener(v -> {

            if (!isClip) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(IS_CLIP, true);
                bundle.putSerializable(CLIP_IMAGE, (ArrayList<ImageAndVideo>) categoryViewModel.clipImage);
                navController.navigate(R.id.action_categoryDetails_self, bundle);

            }else {
                if (clipBoardSize==4){
                   // stickersViewModel.addAllCropBitmap(categoryImages);
                    navController.navigate(R.id.action_categoryDetails_to_addSticker);
                }else {

                   Toast.makeText(getContext(),getResources().getString(R.string.add_4_img),Toast.LENGTH_SHORT).show();
                }

            }
        });


        connFailed.setOnClickListener(v -> {
            checkConnection();
        });


    }






    private void onScroll(){
        int childCount=gridLayout.getChildCount();
        int pastVisibleItem;
        int totalItem=gridLayout.getItemCount();
        int currentVisibleItem;
        currentVisibleItem= gridLayout.findFirstVisibleItemPosition();

        firstVisiblePosition=currentVisibleItem;


        pastVisibleItem=currentVisibleItem;
        Log.e("HERE",String.valueOf((childCount+pastVisibleItem))+"//"+totalItem);
        if (!isClip&&subCategoryImage.isHasNext()&&!isLoading&&isScrolling&&((childCount+pastVisibleItem)>=totalItem)){
            isScrolling=false;
            isLoading=true;
            progressBar.setVisibility(View.VISIBLE);
            categoryViewModel.nextSubCategoryPage(id);

           // Toast.makeText(getContext(),"scroll",Toast.LENGTH_SHORT).show();
        }

    }






    public  void slideToTop(final View view, final int alphaD, final int tranD) {


        view.post(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(),String.valueOf(view.getMeasuredHeightAndState()),Toast.LENGTH_SHORT).show();
                TranslateAnimation animate;
                animate = new TranslateAnimation(0, 0, view.getMeasuredHeightAndState(), 0);
                animate.setDuration(tranD);
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(alphaD);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(animate);
                animationSet.addAnimation(alphaAnimation);
                view.startAnimation(animationSet);
            }
        });

    }



    public  void slideToDown(final View view, final int alphaD, final int tranD) {


        view.post(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                //Toast.makeText(getContext(),String.valueOf(view.getMeasuredHeightAndState()),Toast.LENGTH_SHORT).show();
                TranslateAnimation animate;
                animate = new TranslateAnimation(0, 0, -view.getMeasuredHeightAndState(), 0);
                animate.setDuration(tranD);
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                alphaAnimation.setDuration(alphaD);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(animate);
                animationSet.addAnimation(alphaAnimation);
                view.startAnimation(animationSet);
            }
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

    private void checkConnection(){

        if (isOnline){
            connFailed.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            categoryViewModel.reGetSubCategoryImage();
            progressBar.setVisibility(View.VISIBLE);
        }else {
            connFailed.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void Connect() {
        connFailed.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        if (!isClip&&categoryImages.size()==0) {
            progressBar.setVisibility(View.VISIBLE);
            categoryViewModel.reGetSubCategoryImage();
        }


    }

    @Override
    public void notConnect() {
        connFailed.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
