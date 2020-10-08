package yaseerfarah22.com.whatsappsticker_2.View;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import yaseerfarah22.com.whatsappsticker_2.Dagger2.Component.DaggerAppComponent;


public class AppController extends Application implements HasActivityInjector
{

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent.builder()
                .application(this)
                .appContext(getApplicationContext())
                .build()
                .inject(this);






    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
