package yaseerfarah22.com.whatsappsticker_2.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import yaseerfarah22.com.whatsappsticker_2.Interface.BadgeListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageAndVideo;
import yaseerfarah22.com.whatsappsticker_2.POJO.CropBitmap;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.View.ImageCategory;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;

import static yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails.ID;
import static yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails.IS_CLIP;
import static yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails.IS_PARENT;
import static yaseerfarah22.com.whatsappsticker_2.View.WhatsAppStatus.IMAGE;
import static yaseerfarah22.com.whatsappsticker_2.View.WhatsAppStatus.IMAGE_URI;
import static yaseerfarah22.com.whatsappsticker_2.View.WhatsAppStatus.TYPE;
import static yaseerfarah22.com.whatsappsticker_2.View.WhatsAppStatus.VIDEO;


public class ImageCardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<ImageAndVideo> imageUri;
    private List<CategoryData> categoryData;
    private List<ImageAndVideo> clipBoard;
    private int type;
    public static final int CAT=1;
    public static final int DETAILS=2;
    public static final int CATEGORY=3;
    private NavController navController;
    private BadgeListener badgeListener;
    HashMap<String, Bitmap> stringBitmapHashMap;
    List<CropBitmap>cropBitmapList;
    private StickersViewModel stickersViewModel;
    private Resources resources;


    public ImageCardViewAdapter(Context context, List<ImageAndVideo> imageUri, NavController navController,List<ImageAndVideo> clipImage, int type,BadgeListener badgeListener, StickersViewModel stickersViewModel,Resources resources) {
        this.context = context;
        this.imageUri=imageUri;
        this.badgeListener=badgeListener;
        this.type=type;
        this.navController=navController;
        this.clipBoard=clipImage;
        stringBitmapHashMap=new HashMap<>();
        this.cropBitmapList=new ArrayList<>();
        this.stickersViewModel=stickersViewModel;
        this.resources=resources;


    }

    public ImageCardViewAdapter(Context context, List<CategoryData> categoryData, NavController navController) {
        this.context = context;
        this.categoryData=categoryData;
        this.navController=navController;
        this.type=CATEGORY;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;

        if (type==CAT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_image_card, parent, false);
        }
        else if (type==DETAILS){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false);
        } else if (type==CATEGORY){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_cat_card, parent, false);

            return new Cat_holder(view);
        }


        return new Pro_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof Pro_holder){


            //addCheck(holder);

            ((Pro_holder) holder).progressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(imageUri.get(holder.getAdapterPosition()).getImageUri())
                    .apply(RequestOptions.timeoutOf(60*1000))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            ((Pro_holder)holder).isLoading=false;
                            ((Pro_holder) holder).progressBar.setVisibility(View.INVISIBLE);
                            ((Pro_holder) holder).imageView.setImageDrawable(resource);
                          //  stringBitmapHashMap.put(imageUri.get(holder.getAdapterPosition()),((BitmapDrawable)resource).getBitmap());
                        }
                    });

           // badgeListener.addBadge(clipBoard.size());
            ((Pro_holder) holder).imageView.setOnClickListener(v -> {

                if (!((Pro_holder) holder).isLoading){
                    Bundle bundle=new Bundle();
                    if (imageUri.get(holder.getAdapterPosition()).getVideoUri()!=null){
                        bundle.putInt(TYPE,VIDEO);
                        bundle.putString(IMAGE_URI,imageUri.get(holder.getAdapterPosition()).getVideoUri());
                    }else {
                        bundle.putInt(TYPE,IMAGE);
                        bundle.putString(IMAGE_URI,imageUri.get(holder.getAdapterPosition()).getImageUri());
                    }


                    navController.navigate(R.id.action_global_whatsAppStatus2,bundle);

                }else {
                    Toast.makeText(context,"انتظر حتى يتم تحميل الصورة",Toast.LENGTH_SHORT).show();
                }

               /* if (!((Pro_holder) holder).isCheck&&!((Pro_holder)holder).isLoading) {
                    if (clipBoard.size()<4) {
                        ((Pro_holder) holder).check.setVisibility(View.VISIBLE);
                        ((Pro_holder) holder).isCheck = true;
                        clipBoard.add(imageUri.get(holder.getAdapterPosition()));
                       // stringBitmapHashMap.put(imageUri.get(holder.getAdapterPosition()),((BitmapDrawable)((Pro_holder) holder).imageView.getDrawable()).getBitmap());
                       // stickersViewModel.addAllCropBitmap(clipBoard,stringBitmapHashMap);
                       // badgeListener.addBadge(clipBoard.size());
                        stickersViewModel.setClipBoardList(clipBoard);
                        checkSize();

                    }else {

                        Toast.makeText(context,resources.getString(R.string.already_pick),Toast.LENGTH_SHORT).show();
                    }
                } else if (((Pro_holder)holder).isLoading){
                    Toast.makeText(context,"انتظر حتى يتم تحميل الصورة",Toast.LENGTH_SHORT).show();
                }
                else {

                    ((Pro_holder) holder).check.setVisibility(View.INVISIBLE);
                    ((Pro_holder) holder).isCheck = false;
                   // stringBitmapHashMap.remove(imageUri.get(holder.getAdapterPosition()));

                    for (int i=0;i<clipBoard.size();i++){

                        if (clipBoard.get(i).trim().matches(imageUri.get(holder.getAdapterPosition()))){
                            clipBoard.remove(i);
                           // notifyDataSetChanged();
                        }

                    }
                    stickersViewModel.setClipBoardList(clipBoard);

                    checkSize();
                   // stickersViewModel.addAllCropBitmap(clipBoard,stringBitmapHashMap);
                   // badgeListener.addBadge(clipBoard.size());
                }*/

            });



        }else {

            ((Cat_holder)holder).textView.setText(categoryData.get(holder.getAdapterPosition()).getTitle());
            ((Cat_holder)holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle=new Bundle();
                    if (categoryData.get(holder.getAdapterPosition()).getCountSubCategory()>0) {
                        bundle.putInt(ID, categoryData.get(holder.getAdapterPosition()).getId());
                        navController.navigate(R.id.action_parentCategory_to_imageCategory, bundle);
                    }else {
                        bundle.putBoolean(IS_CLIP,false);
                        bundle.putBoolean(IS_PARENT, true);
                        bundle.putInt(ID, categoryData.get(holder.getAdapterPosition()).getId());
                        navController.navigate(R.id.action_parentCategory_to_categoryDetails, bundle);
                    }
                }
            });

        }


    }




    @Override
    public int getItemCount() {
        if (type==CATEGORY){
            return categoryData.size();
        }
        return imageUri.size();
    }




    public List<ImageAndVideo> getClipBoard(){
        return clipBoard;
    }

    private void addCheck(RecyclerView.ViewHolder holder){

        checkSize();
        ((Pro_holder)holder).isCheck=false;
        ((Pro_holder)holder).isLoading=true;
        ((Pro_holder)holder).check.setVisibility(View.INVISIBLE);

        for(ImageAndVideo uri:clipBoard){

            if (uri.getImageUri().trim().matches(imageUri.get(holder.getAdapterPosition()).getImageUri())){
                ((Pro_holder)holder).isCheck=true;
                ((Pro_holder)holder).check.setVisibility(View.VISIBLE);
            }

        }

    }


    private void checkSize(){
        //stickersViewModel.setClipBoardList(clipBoard);
        if (clipBoard.size()<4){
            badgeListener.notDone();
        }else {

            stickersViewModel.addAllCropBitmap(clipBoard,stringBitmapHashMap);

            badgeListener.done();
        }

    }



    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageView check;
        boolean isLoading;
        boolean isCheck;
        ProgressBar progressBar;


        public Pro_holder(View itemView) {
            super(itemView);
           imageView =(ImageView) itemView.findViewById(R.id.image_view);
           check=(ImageView) itemView.findViewById(R.id.check);
           progressBar=(ProgressBar)itemView.findViewById(R.id.prog);
           isCheck=false;
           isLoading=true;

        }
    }


    //////////////////////////////////////////////////////////
    public class Cat_holder extends RecyclerView.ViewHolder{
        TextView textView;
        CardView cardView;


        public Cat_holder(View itemView) {
            super(itemView);
            cardView=(CardView) itemView.findViewById(R.id.card_view);
            textView =(TextView) itemView.findViewById(R.id.title);

        }
    }

}
