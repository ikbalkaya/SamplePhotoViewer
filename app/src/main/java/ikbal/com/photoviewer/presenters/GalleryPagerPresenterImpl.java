package ikbal.com.photoviewer.presenters;

import ikbal.com.photoviewer.view.GalleryPagerView;

/**
 * Created by ikbal on 13/08/2017.
 */

public class GalleryPagerPresenterImpl implements GalleryPagerPresenter {
    private GalleryPagerView galleryPagerView;

    public GalleryPagerPresenterImpl(GalleryPagerView galleryPagerView) {
        this.galleryPagerView = galleryPagerView;
    }

    @Override
    public void loadPhoto(int photoIndex) {
          if (galleryPagerView !=null){
              galleryPagerView.showItem(photoIndex);
          }
    }
}
