package yaseerfarah22.com.whatsappsticker_2.ViewModel;


import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;



@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<?extends ViewModel>,Provider<ViewModel>> classProviderMap;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> classProviderMap) {
        this.classProviderMap = classProviderMap;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {


        return (T)classProviderMap.get(modelClass).get();
    }
}
