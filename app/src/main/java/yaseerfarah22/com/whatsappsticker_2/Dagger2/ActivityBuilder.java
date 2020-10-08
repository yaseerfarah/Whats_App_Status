package yaseerfarah22.com.whatsappsticker_2.Dagger2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import yaseerfarah22.com.whatsappsticker_2.View.MainActivity;


@Module
public abstract class ActivityBuilder {


    @ContributesAndroidInjector(modules = MainActivityFragments.class)
    abstract MainActivity contributeMainActivity();






}
