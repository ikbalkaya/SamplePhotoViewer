package ikbal.com.cookpadphotogallery;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import ikbal.com.cookpadphotogallery.api.ApiCreator;
import ikbal.com.cookpadphotogallery.api.FlickrApi;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.model.PhotoListResponse;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This service will get the response from endpoint and followingly start to
 * prefetch all the photos available in the response for a faster load of photos
 */

public class PhotoCacheService extends IntentService{
    private static final String TAG = PhotoCacheService.class.getSimpleName();

    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_PHOTOS = "EXTRA_PHOTOS";
    public static final int PHOTOS_RECEIVED_CODE = 10;
    public static final String EXTRA_FAIL_MESSAGE = "EXTRA_FAIL_MESSAGE";
    public static final int PHOTOS_RECEIVED_FAILED_CODE = 20 ;


    private ResultReceiver resultReceiver;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PhotoCacheService() {
        super("PhotoCacheService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        resultReceiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
       retrieveGalleryPhotos();
    }
    private void retrieveGalleryPhotos(){
        FlickrApi api = ApiCreator.getApiReference();

        final Call<PhotoListResponse> photoListCall = api.fetchRecentPhotos(1000, 1);
        photoListCall.enqueue(new Callback<PhotoListResponse>() {
            @Override
            public void onResponse(Call<PhotoListResponse> call, Response<PhotoListResponse> response) {
                PhotoListResponse photoListResponse = response.body();

                Bundle extras = new Bundle();
                extras.putString(EXTRA_PHOTOS, PhotoSerializableUtils.photoListToJson(photoListResponse.getPhotos()));
                resultReceiver.send(PHOTOS_RECEIVED_CODE, extras);
                for(Photo photo : photoListResponse.getPhotos()){
                    downloadPhoto(photo);
                }
            }

            @Override
            public void onFailure(Call<PhotoListResponse> call, Throwable t) {
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_FAIL_MESSAGE,t.getLocalizedMessage());
                resultReceiver.send(PHOTOS_RECEIVED_FAILED_CODE, bundle);
            }
        });
    }
    private void downloadPhoto(final Photo photo){
        /**
        Picasso.with(this)
                .load(photo.thumbUrl())
                .fetch(new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: "+photo.getId());
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "onError: "+photo.getId());
                    }
                });
         */
        Picasso.with(this)
                .load(photo.thumbUrl())
                .into(new ImageCacheTarget(photo.thumbUrl()));
    }
}
