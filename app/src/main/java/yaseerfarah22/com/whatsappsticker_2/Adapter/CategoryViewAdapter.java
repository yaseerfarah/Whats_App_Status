package yaseerfarah22.com.whatsappsticker_2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import yaseerfarah22.com.whatsappsticker_2.Interface.BadgeListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageAndVideo;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.SubCategoryImage;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;

import static yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails.IS_CLIP;


public class CategoryViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private RecyclerView.RecycledViewPool viewPool;
    private List<SubCategoryImage> subCategoryList;
    //private HashMap<String ,List<String>> recycler_data;
    private  final int FIRST=1;
    private  final int OTHER=2;
    private NavController navController;
    private BadgeListener badgeListener;
    private List<ImageAndVideo>clipImage;
    private StickersViewModel stickersViewModel;
    private Resources resources;




    public CategoryViewAdapter(Context context, List<SubCategoryImage> subCategoryList,NavController navController,List<ImageAndVideo>clipImage,BadgeListener badgeListener,StickersViewModel stickersViewModel,Resources resources) {
        this.context = context;
        this.subCategoryList=subCategoryList;
        //this.recycler_data=recycler_data;
        this.navController=navController;
        this.viewPool=new RecyclerView.RecycledViewPool();
        this.badgeListener=badgeListener;
        this.clipImage=clipImage;
        this.stickersViewModel=stickersViewModel;
        this.resources=resources;




    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType==FIRST){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_view, parent, false);
            return new Logo_holder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_recycler_card, parent, false);
            return new Cat_holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        if (holder instanceof Cat_holder){

            ((Cat_holder) holder).recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

            ((Cat_holder) holder).recyclerView.setRecycledViewPool(viewPool);

            ((Cat_holder) holder).title.setText(subCategoryList.get(holder.getAdapterPosition()-1).getTitle()+" :");
            ImageCardViewAdapter imageCardViewAdapter=new ImageCardViewAdapter(context,subCategoryList.get(holder.getAdapterPosition()-1).getImageAndVideos(),navController,clipImage,ImageCardViewAdapter.CAT,badgeListener,stickersViewModel,resources);


            ((Cat_holder) holder).recyclerView.setAdapter(imageCardViewAdapter);



             ((Cat_holder) holder).more.setOnClickListener(v -> {

                 Bundle bundle=new Bundle();
                 bundle.putBoolean(IS_CLIP,false);
                 bundle.putSerializable("ClipImage", (ArrayList<ImageAndVideo>) clipImage);
                 bundle.putInt("ID",subCategoryList.get(holder.getAdapterPosition()-1).getId());
                 navController.navigate(R.id.action_imageCategory_to_categoryDetails,bundle);

             });



        }




    }






    @Override
    public int getItemCount() {
        return subCategoryList.size()+1;
    }



    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return FIRST;
        }
        else {
            return OTHER;
        }
    }




    //////////////////////////////////////////////////////////
    public class Cat_holder extends RecyclerView.ViewHolder{
        TextView title,more;
        RecyclerView recyclerView;

        public Cat_holder(View itemView) {
            super(itemView);
           recyclerView=(RecyclerView) itemView.findViewById(R.id.cat_recycle1);
           title=(TextView) itemView.findViewById(R.id.cat_title1);
           more=(TextView)itemView.findViewById(R.id.cat_more);


        }
    }



    //////////////////////////////////////////////////////////
    public class Logo_holder extends RecyclerView.ViewHolder{


        public Logo_holder(View itemView) {
            super(itemView);



        }
    }

}
