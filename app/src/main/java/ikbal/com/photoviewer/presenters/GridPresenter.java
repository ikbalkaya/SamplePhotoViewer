package ikbal.com.photoviewer.presenters;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import ikbal.com.photoviewer.interactors.GalleryInteractor;
import ikbal.com.photoviewer.interactors.GalleryInteractorImpl;
import ikbal.com.photoviewer.model.Photo;
import ikbal.com.photoviewer.view.GalleryView;

/**
 * Created by ikbal on 13/08/2017.
 */

public class GridPresenter implements GalleryPresenter,
        GalleryInteractor.GalleryListInteractorListener {
    private GalleryView galleryView;
    private AppCompatActivity activity;
    private GalleryInteractor galleryInteractor;

    public GridPresenter(GalleryView galleryView) {
        this.galleryView = galleryView;
        this.galleryInteractor = new GalleryInteractorImpl();
    }

    @Override
    public void loadPhotoList(AppCompatActivity activity) {
      if (galleryView != null){
          galleryView.hideEmptyView();
          galleryView.showProgress();
      }
      this.galleryInteractor.fetchImages(activity,this);
    }

    /** Progress should not be shown here as there is a refreshing progress*/
    @Override
    public void refreshPhotoList(AppCompatActivity activity) {
        if (galleryView != null){
            galleryView.hideEmptyView();
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
