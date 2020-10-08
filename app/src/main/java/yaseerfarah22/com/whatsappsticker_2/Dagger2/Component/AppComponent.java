package yaseerfarah22.com.whatsappsticker_2.Dagger2.Component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import yaseerfarah22.com.whatsappsticker_2.Dagger2.ActivityBuilder;
import yaseerfarah22.com.whatsappsticker_2.Dagger2.RepositoryModule;
import yaseerfarah22.com.whatsappsticker_2.Dagger2.ViewModelModule;
import yaseerfarah22.com.whatsappsticker_2.View.AppController;


@Singleton
@Component(modules = {ViewModelModule.class, ActivityBuilder.class, AndroidSupportInjectionModule.class, RepositoryModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder appContext(Context context);

        AppComponent build();

    }



    void inject(AppController appController);

}
