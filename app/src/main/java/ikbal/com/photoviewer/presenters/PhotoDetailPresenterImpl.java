package ikbal.com.photoviewer.presenters;

import ikbal.com.photoviewer.view.PhotoDetailView;

/**
 * Created by ikbal on 14/08/2017.
 */

public class PhotoDetailPresenterImpl implements PhotoDetailPresenter {
    private PhotoDetailView photoDetailView;

    public PhotoDetailPresenterImpl(PhotoDetailView photoDetailView) {
        this.photoDetailView = photoDetailView;
    }

    @Override
    public void startLoadingPhoto() {
        if (photoDetailView != null){
            photoDetailView.loadThumbPhoto();
        }
    }

    @Override
    public void startLoadingBigSizePhoto() {
        if (photoDetailView != null){
            photoDetailView.loadBigPhoto();
        }
    }
}
