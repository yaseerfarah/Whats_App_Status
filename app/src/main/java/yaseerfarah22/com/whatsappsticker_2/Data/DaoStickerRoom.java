package yaseerfarah22.com.whatsappsticker_2.Data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomSticker;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomStickerPack;



@Dao
public interface DaoStickerRoom {


    @Insert
    Long insertSticker(RoomStickerPack stickerPack);


    @Query("SELECT * FROM RoomStickerPack")
    List<RoomStickerPack> fetchAllStickers();


    @Query("SELECT * FROM RoomStickerPack WHERE identifier =:identifire")
    RoomStickerPack getSticker(int identifire);


    @Update
    void updateSticker(RoomStickerPack stickerPack);


    @Delete
    void deleteSticker(RoomStickerPack stickerPack);

}
