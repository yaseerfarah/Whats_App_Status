package yaseerfarah22.com.whatsappsticker_2.Dagger2;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;

import java.net.ConnectException;
import java.nio.channels.NoConnectionPendingException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import yaseerfarah22.com.whatsappsticker_2.Data.StickerData;
import yaseerfarah22.com.whatsappsticker_2.Data.StickerDatabase;
import yaseerfarah22.com.whatsappsticker_2.Data.WhatsappStickerApi;

import static yaseerfarah22.com.whatsappsticker_2.Constants.BASE_URL;
import static yaseerfarah22.com.whatsappsticker_2.Constants.DB_NAME;
import static yaseerfarah22.com.whatsappsticker_2.View.MainActivity.isOnline;


@Module
public class RepositoryModule {



    @Provides
    @Singleton
    public StickerData stickerData(Context context,StickerDatabase stickerDatabase){

        return new StickerData(context,stickerDatabase);

    }



    @Provides
    @Singleton
    public StickerDatabase stickerDatabase(Context context){

        return Room.databaseBuilder(context, StickerDatabase.class, DB_NAME)
                .build();

    }




    @Provides
    @Singleton
    public WhatsappStickerApi whatsappStickerApi(Retrofit retrofit){

        return retrofit.create(WhatsappStickerApi.class);

    }


    @Singleton
    @Provides
    public Retrofit retrofit(){

        OkHttpClient client=new OkHttpClient()
                .newBuilder()
                .addInterceptor(chain -> {
                    if (!isOnline) {
                        throw new NoConnectionPendingException();
                    }
                    try {
                        return chain.proceed(chain.request());
                    }catch (Exception e){
                        e.printStackTrace();
                        throw e;
                    }



                })
                .addNetworkInterceptor(chain -> {
                    if (!isOnline) {
                        throw new NoConnectionPendingException();
                    }
                    try {
                        return chain.proceed(chain.request());
                    }catch (Exception e){
                        e.printStackTrace();
                        throw e;
                    }



                })
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1,TimeUnit.MINUTES)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }







}
