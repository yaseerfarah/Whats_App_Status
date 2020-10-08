package yaseerfarah22.com.whatsappsticker_2.View;


import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import dagger.android.support.AndroidSupportInjection;
import yaseerfarah22.com.whatsappsticker_2.Adapter.DetailsCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Adapter.ImageCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Adapter.ParentCatCardViewAdapter;
import yaseerfarah22.com.whatsappsticker_2.Interface.InternetStatus;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.whatsappsticker_2.Util.NetworkReceiver;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.CategoryViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;

import static yaseerfarah22.com.whatsappsticker_2.View.MainActivity.isOnline;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentCategory extends Fragment implements InternetStatus
{

    @Inject
    ViewModelFactory viewModelFactory;

    CategoryViewModel categoryViewModel;

    private Observer parentCategoryObserver;
    private List<CategoryData> categoryName=new ArrayList<>();
    private ProgressBar progressBar;
    private RelativeLayout connFailed;
    private NetworkReceiver networkReceiver;
    private int space;
    private int space_land;


    Button toCrop;
    RecyclerView recyclerView;
    ImageCardViewAdapter parentCatCardViewAdapter;
    NavController navController;


    public ParentCategory() {
        // Required empty public constructor

        parentCategoryObserver=new Observer<List<CategoryData>>() {
            @Override
            public void onChanged(List<CategoryData> categoryData) {
                if (categoryData.size()>0) {
                    progressBar.setVisibility(View.INVISIBLE);
                    categoryName.clear();
                    categoryName.addAll(categoryData);
                    parentCatCardViewAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter netFilter=new IntentFilter();
        netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(networkReceiver,netFilter);
       

        categoryViewModel.getCategoryLiveData().observe(this,parentCategoryObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(networkReceiver);
        categoryViewModel.getCategoryLiveData().removeObservers(this);
        categoryName.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        AndroidSupportInjection.inject(this);
        networkReceiver=new NetworkReceiver(this);

        categoryViewModel= ViewModelProviders.of(this,viewModelFactory).get(CategoryViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_category, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connFailed=(RelativeLayout)view.findViewById(R.id.failed_conn);
        navController= Navigation.findNavController(view);
        toCrop=(Button) view.findViewById(R.id.to_crop);
        progressBar=(ProgressBar)view.findViewById(R.id.prog);
        recyclerView=(RecyclerView)view.findViewById(R.id.parent_cat_recycler);

       // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));



        parentCatCardViewAdapter= new ImageCardViewAdapter(getContext(),categoryName,navController);
        recyclerView.setAdapter(parentCatCardViewAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;


        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                space=60;
                space_land=140;
                Toast.makeText(getContext(),"Large",Toast.LENGTH_SHORT).show();

                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                space=10;
                space_land=60;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
               space=10;
               space_land=60;
                break;


        }


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(space_land,getResources()),GridSpacingItemDecoration.Category,width,(int)getResources().getDimension(R.dimen.parent_cat_width)));

        } else {
            // In portrait
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(space,getResources()),GridSpacingItemDecoration.Category,width,(int)getResources().getDimension(R.dimen.parent_cat_width)));

        }


        recyclerView.smoothScrollToPosition(0);


        toCrop.setOnClickListener(v -> {

            navController.navigate(R.id.action_parentCategory_to_addSticker);

        });



        connFailed.setOnClickListener(v -> {
            checkConnection();

        });







    }



    private void checkConnection(){

        if (isOnline){
            connFailed.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            categoryViewModel.getCategoryData();
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
        categoryViewModel.getCategoryData();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void notConnect() {
        connFailed.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
