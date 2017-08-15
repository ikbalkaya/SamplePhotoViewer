package ikbal.com.photoviewer.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ikbal.com.photoviewer.model.PhotoListResponse;

/**
 * Created by ikbal on 11/08/2017.
 */
 class PhotoListDeserializer implements JsonDeserializer<PhotoListResponse> {

    @Override
    public PhotoListResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject response = json.getAsJsonObject();
        Gson gson = new Gson();
        JsonElement photosElement = response.get("photos");
        PhotoListResponse listResponse = gson.fromJson(photosElement,PhotoListResponse.class);
        return listResponse;

    }
}
