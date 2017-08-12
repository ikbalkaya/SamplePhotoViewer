package ikbal.com.cookpadphotogallery.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ikbal.com.cookpadphotogallery.model.PhotoListResponse;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ikbal on 11/08/2017.
 */

public class ApiCreator {

    public static FlickrApi getApiReference(){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_API_URL)
                .client(client)
                .addConverterFactory(getPhotoListConverterFactory())
                .build();

        return retrofit.create(FlickrApi.class);
    }

    @NonNull
    private static GsonConverterFactory getPhotoListConverterFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        PhotoListDeserializer deserializer = new PhotoListDeserializer();
        gsonBuilder.registerTypeAdapter(PhotoListResponse.class,deserializer);
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }


}
