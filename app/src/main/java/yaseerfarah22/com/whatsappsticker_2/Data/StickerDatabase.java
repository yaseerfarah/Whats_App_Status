package yaseerfarah22.com.whatsappsticker_2.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomSticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomStickerPack;
import yaseerfarah22.com.whatsappsticker_2.POJO.StickerPack;

@Database(entities = {RoomStickerPack.class}, version = 1, exportSchema = false)
public abstract class StickerDatabase extends RoomDatabase {

    public abstract DaoStickerRoom daoStickerRoom();

}
