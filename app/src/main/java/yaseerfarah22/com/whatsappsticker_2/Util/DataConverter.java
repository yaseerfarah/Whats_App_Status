package yaseerfarah22.com.whatsappsticker_2.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomSticker;

public class DataConverter {

    @TypeConverter
    public String fromStickersList(List<RoomSticker> roomStickers) {
        if (roomStickers == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RoomSticker>>() {}.getType();
        String json = gson.toJson(roomStickers, type);
        return json;
    }

    @TypeConverter
    public List<RoomSticker> toCountryLangList(String stickerString) {
        if (stickerString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RoomSticker>>() {}.getType();
        List<RoomSticker> roomStickers = gson.fromJson(stickerString, type);
        return roomStickers;
    }
}
