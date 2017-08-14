package ikbal.com.cookpadphotogallery.presenters;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by ikbal on 13/08/2017.
 */

public interface GalleryPresenter {
    void loadPhotoList(AppCompatActivity activity);//activity is given to start intent service
    void refreshPhotoList(AppCompatActivity activity);
}
