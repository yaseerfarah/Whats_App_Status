package yaseerfarah22.com.whatsappsticker_2.Dagger2;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import yaseerfarah22.com.whatsappsticker_2.View.AddSticker;
import yaseerfarah22.com.whatsappsticker_2.View.CategoryDetails;
import yaseerfarah22.com.whatsappsticker_2.View.CropImage;
import yaseerfarah22.com.whatsappsticker_2.View.Home;
import yaseerfarah22.com.whatsappsticker_2.View.ImageCategory;
import yaseerfarah22.com.whatsappsticker_2.View.ParentCategory;
import yaseerfarah22.com.whatsappsticker_2.View.WhatsAppStatus;


@Module
public abstract class MainActivityFragments {


    @ContributesAndroidInjector()
    abstract AddSticker contributeAddStickerFragment();

    @ContributesAndroidInjector()
    abstract CategoryDetails contributeCategoryDetailsFragment();

    @ContributesAndroidInjector()
    abstract CropImage contributeCropImageFragment();

    @ContributesAndroidInjector()
    abstract Home contributeHomeFragment();

    @ContributesAndroidInjector()
    abstract ImageCategory contributeImageCategoryFragment();

    @ContributesAndroidInjector()
    abstract ParentCategory contributeParentCategoryFragment();

    @ContributesAndroidInjector()
    abstract WhatsAppStatus contributeWhatsAppFragment();






}
