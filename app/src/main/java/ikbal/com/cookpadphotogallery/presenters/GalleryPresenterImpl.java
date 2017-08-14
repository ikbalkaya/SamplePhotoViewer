package ikbal.com.cookpadphotogallery.presenters;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ikbal.com.cookpadphotogallery.interactors.GalleryInteractor;
import ikbal.com.cookpadphotogallery.interactors.GalleryInteractorImpl;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.view.GalleryView;

/**
 * Created by ikbal on 13/08/2017.
 */

public class GalleryPresenterImpl implements GalleryPresenter,
        GalleryInteractor.GalleryListInteractorListener {
    private GalleryView galleryView;
    private GalleryInteractor galleryInteractor;

    public GalleryPresenterImpl(GalleryView galleryView) {
        this.galleryView = galleryView;
        this.galleryInteractor = new GalleryInteractorImpl();
    }

    @Override
    public void loadImages(AppCompatActivity activity) {
      if (galleryView != null){
          galleryView.hideEmptyView();
          galleryView.showProgress();
      }
      this.galleryInteractor.fetchImages(activity,this);
    }


    @Override
    public void onPhotosRetrieved(List<Photo> photoList) {
        if(galleryView != null){
            galleryView.hideEmptyView();
            galleryView.hideProgress();
            galleryView.showList(photoList);
        }
    }

    @Override
    public void onErrorRetrievingPhotos(String errorString) {
        if(galleryView != null){
            galleryView.hideProgress();
            galleryView.showEmptyView(errorString);
        }
    }
}
