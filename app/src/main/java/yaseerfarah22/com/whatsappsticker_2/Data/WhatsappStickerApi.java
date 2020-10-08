package yaseerfarah22.com.whatsappsticker_2.Data;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.CategoryResponse;
import yaseerfarah22.com.whatsappsticker_2.POJO.Api_POJO.ImageResponse;

public interface WhatsappStickerApi {


    @GET("categorys")
    Observable<Response<CategoryResponse>> getCategory();
    @GET("categorys/{id}")
    Observable<Response<ImageResponse>> getCategoryImages(@Path("id") String id, @Query("page") String page);



}
