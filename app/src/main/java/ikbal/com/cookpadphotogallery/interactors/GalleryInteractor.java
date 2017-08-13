package ikbal.com.cookpadphotogallery.interactors;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ikbal.com.cookpadphotogallery.model.Photo;

/**
 * Created by ikbal on 13/08/2017.
 */

public interface GalleryInteractor {
    void fetchImages(AppCompatActivity activity, GalleryInteractor.GalleryListInteractorListener listener);

    interface GalleryListInteractorListener{
        void onPhotosRetrieved(List<Photo> photoList);
        void onErrorRetrievingPhotos(String errorString);
    }
}
