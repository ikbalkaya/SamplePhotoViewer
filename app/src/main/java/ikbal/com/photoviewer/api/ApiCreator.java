package ikbal.com.photoviewer.api;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ikbal.com.photoviewer.model.PhotoListResponse;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiCreator {

    public static final int ACTION_RECENT_PHOTOS = 1;

    public static FlickrApi getServiceReference(int action){
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        GsonConverterFactory converterFactory = getConverterFactory(action);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_API_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build();

        return retrofit.create(FlickrApi.class);
    }

    /**
     * This method normally needs to move to another class if multiple
     * response types and deserializers needs to be provided
     * */
    @NonNull
    private static GsonConverterFactory getConverterFactory(int action) {
        if (action == ACTION_RECENT_PHOTOS){
            GsonBuilder gsonBuilder = new GsonBuilder();
            PhotoListDeserializer deserializer = new PhotoListDeserializer();
            gsonBuilder.registerTypeAdapter(PhotoListResponse.class,deserializer);
            Gson gson = gsonBuilder.create();
            return GsonConverterFactory.create(gson);
        }
        return null;
    }


}
