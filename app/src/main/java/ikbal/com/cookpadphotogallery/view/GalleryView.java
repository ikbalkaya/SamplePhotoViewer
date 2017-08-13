package ikbal.com.cookpadphotogallery.view;

import java.util.List;

import ikbal.com.cookpadphotogallery.model.Photo;

/**
 * Created by ikbal on 13/08/2017.
 */

public interface GalleryView {
    void showProgress();
    void hideProgress();
    void showList(List<Photo> photoList);
    void showNoItem();
}
