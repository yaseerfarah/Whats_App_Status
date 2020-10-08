package yaseerfarah22.com.whatsappsticker_2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import yaseerfarah22.com.whatsappsticker_2.Data.ReadAndWriteToFile;
import yaseerfarah22.com.whatsappsticker_2.Interface.StickerPackDeleteListener;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;
import yaseerfarah22.com.whatsappsticker_2.R;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;

import static android.os.ParcelFileDescriptor.MODE_READ_ONLY;



public class HomeCardViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private List<StickerPack> stickerPackerList;
    private  final int FIRST=1;
    private  final int OTHER=2;
    private NavController navController;
    private StickersViewModel stickersViewModel;
    private ReadAndWriteToFile readAndWriteToFile;


    public HomeCardViewAdapter(Context context, List<StickerPack> stickerPackers,StickersViewModel stickersViewModel,NavController navController) {
        this.context = context;
        this.stickerPackerList=stickerPackers;
        this.navController=navController;
        this.stickersViewModel=stickersViewModel;
        this.readAndWriteToFile=new ReadAndWriteToFile(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_pack_cardview, parent, false);
            return new HomeCardViewAdapter.Pack_holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof Pack_holder) {

            ((Pack_holder) holder).imageView.setImageBitmap(null);
            ((Pack_holder) holder).delete.setEnabled(true);

          if (stickerPackerList.get(holder.getAdapterPosition()).trayImageFile.trim().length()>0){
            try {
                File imageDir=new File(readAndWriteToFile.getStickerFile(),stickerPackerList.get(holder.getAdapterPosition()).identifier);
                File file=new File(imageDir,stickerPackerList.get(holder.getAdapterPosition()).trayImageFile);

                AssetFileDescriptor assetFileDescriptor= new AssetFileDescriptor(ParcelFileDescriptor.open(file, MODE_READ_ONLY),0,file.length()) ;
                final FileDescriptor fileDescriptor = assetFileDescriptor.getParcelFileDescriptor().getFileDescriptor();
                final Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                ((Pack_holder) holder).imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("ERROR",e.getMessage());
                Toast.makeText(context,e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

            //Glide.with(context).load(stickerPackerList.get(holder.getAdapterPosition()).trayImageFile).into(((Pack_holder) holder).imageView);

            ((Pack_holder) holder).name.setText(stickerPackerList.get(holder.getAdapterPosition()).name);
            ((Pack_holder) holder).author.setText(stickerPackerList.get(holder.getAdapterPosition()).publisher);


            ((Pack_holder) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stickerPackerList.get(holder.getAdapterPosition()).trayImageFile.trim().length()>0){
                        stickersViewModel.setStickerPack(stickerPackerList.get(holder.getAdapterPosition()));
                        navController.navigate(R.id.action_home2_to_addSticker);
                    }else {
                    stickersViewModel.setStickerPack(stickerPackerList.get(holder.getAdapterPosition()));
                    navController.navigate(R.id.action_home2_to_parentCategory);
                    }
                }
            });


            ((Pack_holder) holder).delete.setOnClickListener(v -> {

                stickersViewModel.deleteStickerPacks(stickerPackerList.get(holder.getAdapterPosition()), new StickerPackDeleteListener() {
                    @Override
                    public void onDeleted() {
                        stickersViewModel.getAllStickerPacks();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Error Delete",e.getMessage());
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            });

        }

    }




    @Override
    public int getItemCount() {
        return stickerPackerList.size();
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
    public class Pack_holder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ImageButton delete;
        TextView name,author;
        RelativeLayout relativeLayout;

        public Pack_holder(View itemView) {
            super(itemView);
           imageView =(ImageView) itemView.findViewById(R.id.card_image);
           name=(TextView) itemView.findViewById(R.id.card_title);
           author=(TextView) itemView.findViewById(R.id.card_author);
           relativeLayout=(RelativeLayout)itemView.findViewById(R.id.cardview);
           delete=(ImageButton)itemView.findViewById(R.id.delete);
        }
    }


    //////////////////////////////////////////////////////////
    public class Logo_holder extends RecyclerView.ViewHolder{


        public Logo_holder(View itemView) {
            super(itemView);



        }
    }




}
