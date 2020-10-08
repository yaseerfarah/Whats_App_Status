package yaseerfarah22.com.whatsappsticker_2.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import androidx.room.TypeConverter;
import yaseerfarah22.com.whatsappsticker_2.POJO.Room_POJO.RoomSticker;

public class StringDataConverter {

    @TypeConverter
    public String fromStickersList(List<String> stringList) {
        if (stringList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(stringList, type);
        return json;
    }

    @TypeConverter
    public List<String> toCountryLangList(String string) {
        if (string == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> stringList = gson.fromJson(string, type);
        return stringList;
    }
}
