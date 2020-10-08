package yaseerfarah22.com.whatsappsticker_2.Interface;

import java.util.List;

import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomStickerPack;

public interface RoomStickerPackListener {

    void onSuccess(List<RoomStickerPack> roomStickerPacks);
    void onError(Throwable e);

}
