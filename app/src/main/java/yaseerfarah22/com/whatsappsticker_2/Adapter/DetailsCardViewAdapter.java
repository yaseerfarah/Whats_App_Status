package yaseerfarah22.com.whatsappsticker_2.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yaseerfarah22.com.whatsappsticker_2.Interface.BadgeListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.Util.GridSpacingItemDecoration;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;

import static yaseerfarah22.com.whatsappsticker_2.Adapter.ImageCardViewAdapter.DETAILS;


public class DetailsCardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<String> imageUri;
    private  final int FIRST=1;
    private  final int OTHER=2;
    private Resources resources;
    private RecyclerView.RecycledViewPool viewPool;
    private NavController navController;
    private BadgeListener badgeListener;
    private List<String>clipImage;
    private StickersViewModel stickersViewModel;


    public DetailsCardViewAdapter(Context context, List<String> imageUri,NavController navController,Resources resources,List<String>clipImage,BadgeListener badgeListener,StickersViewModel stickersViewModel) {
        this.context = context;
        this.imageUri=imageUri;
        this.resources=resources;
        this.navController=navController;
        this.viewPool=new RecyclerView.RecycledViewPool();
        this.badgeListener=badgeListener;
        this.clipImage=clipImage;
        this.stickersViewModel=stickersViewModel;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (viewType==FIRST){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_view, parent, false);
            return new DetailsCardViewAdapter.Logo_holder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_recycler_card, parent, false);
            return new DetailsCardViewAdapter.Image_holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof Image_holder) {


            ((Image_holder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context,2));

            ((Image_holder) holder).recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(5,resources),GridSpacingItemDecoration.HomeLayout,0,0));

            ((Image_holder) holder).recyclerView.setRecycledViewPool(viewPool);

           // ImageCardViewAdapter imageCardViewAdapter= new ImageCardViewAdapter(context,imageUri,navController,clipImage,DETAILS,badgeListener,stickersViewModel,resources);

           // ((Image_holder) holder).recyclerView.setAdapter(imageCardViewAdapter);


        }

    }




    @Override
    public int getItemCount() {
        return 2;
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
    public class Image_holder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;


        public Image_holder(View itemView) {
            super(itemView);
           recyclerView =(RecyclerView) itemView.findViewById(R.id.de_recycler);

        }
    }


    //////////////////////////////////////////////////////////
    public class Logo_holder extends RecyclerView.ViewHolder{


        public Logo_holder(View itemView) {
            super(itemView);



        }
    }




}
