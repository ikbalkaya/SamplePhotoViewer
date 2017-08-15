package ikbal.com.photoviewer.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import ikbal.com.photoviewer.model.Photo;

/**
 * Created by ikbal on 12/08/2017.
 */

public class PhotoSerializableUtils {
    public static List<Photo> photoListFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Photo>>(){}.getType());
    }

    public static String photoListToJson(List<Photo> photos){
        Gson gson = new Gson();
        return gson.toJson(photos);
    }
    public static Photo photoFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Photo.class);
    }
    public static String photoToJson(Photo photo){
        Gson gson = new Gson();
        return gson.toJson(photo);
    }
}
