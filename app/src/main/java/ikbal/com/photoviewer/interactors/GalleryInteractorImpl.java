package ikbal.com.photoviewer.interactors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ikbal.com.photoviewer.services.PhotoCacheService;
import ikbal.com.photoviewer.model.Photo;
import ikbal.com.photoviewer.utils.PhotoSerializableUtils;

public class GalleryInteractorImpl implements GalleryInteractor {
    @Override
    public void fetchImages(AppCompatActivity activity, GalleryInteractor.GalleryListInteractorListener listener) {
        Intent photoCacheServiceIntent = new Intent(activity, PhotoCacheService.class);
        ThumbCacheResultReceiver resultReceiver = new ThumbCacheResultReceiver(listener);
        photoCacheServiceIntent.putExtra(PhotoCacheService.EXTRA_RECEIVER, resultReceiver);
        activity.startService(photoCacheServiceIntent);
    }

    /**
     * This class has been implemented to act as a bridge between intent service
     * and the activity called it. interaction listener has been delegated to this class
     * as it's responsible for any message coming from intent service
     * */
    private class ThumbCacheResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        GalleryListInteractorListener listener;
        ThumbCacheResultReceiver(GalleryInteractor.GalleryListInteractorListener listener) {
            super(new Handler(Looper.getMainLooper()));
            this.listener = listener;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == PhotoCacheService.PHOTOS_RECEIVED_CODE) {
                String photosJson = resultData.getString(PhotoCacheService.EXTRA_PHOTOS);
                final List<Photo> photos = PhotoSerializableUtils.photoListFromJson(photosJson);
                listener.onPhotosRetrieved(photos);
            } else if (resultCode == PhotoCacheService.PHOTOS_RECEIVED_FAILED_CODE) {
                String error = resultData.getString(PhotoCacheService.EXTRA_FAIL_MESSAGE);
                listener.onErrorRetrievingPhotos(error);
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }

}
