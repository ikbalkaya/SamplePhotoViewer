package ikbal.com.cookpadphotogallery.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.picasso.Picasso;

import ikbal.com.cookpadphotogallery.R;
import ikbal.com.cookpadphotogallery.api.ApiCreator;
import ikbal.com.cookpadphotogallery.api.FlickrApi;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.model.PhotoListResponse;
import ikbal.com.cookpadphotogallery.utils.ConnectivityUtil;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This intent service has been created to get photo response from endpoint and
 * prefetch all the photos available in the response for a faster load of photos later
 * Picasso will then handle loading cached photos
 */

public class PhotoCacheService extends IntentService{
    private static final String TAG = PhotoCacheService.class.getSimpleName();

    public static final String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static final String EXTRA_PHOTOS = "EXTRA_PHOTOS";
    public static final int PHOTOS_RECEIVED_CODE = 10;
    public static final String EXTRA_FAIL_MESSAGE = "EXTRA_FAIL_MESSAGE";
    public static final int PHOTOS_RECEIVED_FAILED_CODE = 20 ;


    private ResultReceiver resultReceiver;


    public PhotoCacheService() {
        super("PhotoCacheService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            resultReceiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ConnectivityUtil.isNetworkAvailable(this)){
            retrieveGalleryPhotos();
        }else {
            sendFailureMessage(getString(R.string.network_not_connected_message));
        }

    }
    private void retrieveGalleryPhotos(){
        FlickrApi api = ApiCreator.getServiceReference(ApiCreator.ACTION_RECENT_PHOTOS);

        final Call<PhotoListResponse> photoListCall = api.fetchRecentPhotos();
        photoListCall.enqueue(new Callback<PhotoListResponse>() {
            @Override
            public void onResponse(Call<PhotoListResponse> call, Response<PhotoListResponse> response) {
                PhotoListResponse photoListResponse = response.body();

                Bundle receiverExtras = new Bundle();
                receiverExtras.putString(EXTRA_PHOTOS, PhotoSerializableUtils.photoListToJson(photoListResponse.getPhotos()));
               //let the receiver know about list of photos before they start to being cached
                if (resultReceiver != null){
                    resultReceiver.send(PHOTOS_RECEIVED_CODE, receiverExtras);
                }

                for(Photo photo : photoListResponse.getPhotos()){
                    downloadPhoto(photo);
                }
            }

            @Override
            public void onFailure(Call<PhotoListResponse> call, Throwable t) {
                sendFailureMessage(t.getLocalizedMessage());
            }
        });
    }
    private void sendFailureMessage(String failure){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FAIL_MESSAGE,failure);
        if (resultReceiver != null){
            resultReceiver.send(PHOTOS_RECEIVED_FAILED_CODE, bundle);
        }

    }
    private void downloadPhoto(final Photo photo){
        Picasso.with(this)
                .load(photo.thumbUrl())
                .fetch(new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: "+photo.thumbUrl());
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "onError:"+photo.thumbUrl());
                    }
                });

    }
}
