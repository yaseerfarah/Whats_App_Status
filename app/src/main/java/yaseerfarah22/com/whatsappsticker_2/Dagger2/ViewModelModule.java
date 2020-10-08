package yaseerfarah22.com.whatsappsticker_2.Dagger2;

import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import androidx.lifecycle.ViewModel;
import dagger.MapKey;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import yaseerfarah22.com.whatsappsticker_2.Data.StickerData;
import yaseerfarah22.com.whatsappsticker_2.Data.WhatsappStickerApi;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.CategoryViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.StickersViewModel;
import yaseerfarah22.com.whatsappsticker_2.ViewModel.ViewModelFactory;


@Module
public class ViewModelModule {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @MapKey
    @interface ViewModelKey {
        Class<? extends ViewModel> value();
    }



    @Singleton
    @Provides
    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {

        return new ViewModelFactory(providerMap);
    }

    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(CategoryViewModel.class)
    ViewModel categoryViewModel(Context context, WhatsappStickerApi whatsappStickerApi) {
        return new CategoryViewModel(context,whatsappStickerApi);
    }



    @Provides
    @Singleton
    @IntoMap
    @ViewModelKey(StickersViewModel.class)
    ViewModel stickerViewModel(Context context, StickerData stickerData) {
        return new StickersViewModel(context,stickerData);
    }




}
