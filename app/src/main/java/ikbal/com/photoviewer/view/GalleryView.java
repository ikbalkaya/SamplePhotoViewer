package ikbal.com.photoviewer.view;

import java.util.List;

import ikbal.com.photoviewer.model.Photo;

/**
 * Created by ikbal on 13/08/2017.
 */

public interface GalleryView {
    void showProgress();
    void hideProgress();
    void showList(List<Photo> photoList);
    void showEmptyView(String errorMessage);
    void hideEmptyView();
}
