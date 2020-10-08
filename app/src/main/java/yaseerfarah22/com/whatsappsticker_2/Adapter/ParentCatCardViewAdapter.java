package yaseerfarah22.com.whatsappsticker_2.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryData;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.Util.GridSpacingItemDecoration;

import static yaseerfarah22.com.whatsappsticker_2.Adapter.ImageCardViewAdapter.CATEGORY;
import static yaseerfarah22.com.whatsappsticker_2.Adapter.ImageCardViewAdapter.DETAILS;


public class ParentCatCardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<CategoryData> category;
    private  final int FIRST=1;
    private  final int OTHER=2;
    private Resources resources;
    private NavController navController;


    public ParentCatCardViewAdapter(Context context, List<CategoryData> category, NavController navController, Resources resources) {
        this.context = context;
        this.category=category;
        this.resources=resources;
        this.navController=navController;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if (viewType==FIRST){

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_view, parent, false);
            return new ParentCatCardViewAdapter.Logo_holder(view);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_cat_recycler_card, parent, false);
            return new ParentCatCardViewAdapter.ParentCat_holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ParentCat_holder) {


            ((ParentCat_holder) holder).recyclerView.setLayoutManager(new GridLayoutManager(context,2));

            ((ParentCat_holder) holder).recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,GridSpacingItemDecoration.dpToPx(10,resources),GridSpacingItemDecoration.Category,0,0));

            ImageCardViewAdapter imageCardViewAdapter= new ImageCardViewAdapter(context,category,navController);

            ((ParentCat_holder) holder).recyclerView.setAdapter(imageCardViewAdapter);


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
    public class ParentCat_holder extends RecyclerView.ViewHolder{
        RecyclerView recyclerView;


        public ParentCat_holder(View itemView) {
            super(itemView);
           recyclerView =(RecyclerView) itemView.findViewById(R.id.ct_recycler);

        }
    }


    //////////////////////////////////////////////////////////
    public class Logo_holder extends RecyclerView.ViewHolder{


        public Logo_holder(View itemView) {
            super(itemView);



        }
    }




}
