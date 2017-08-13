package ikbal.com.cookpadphotogallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.presenters.GalleryPresenter;
import ikbal.com.cookpadphotogallery.presenters.GalleryPresenterImpl;
import ikbal.com.cookpadphotogallery.view.GalleryView;
import ikbal.com.cookpadphotogallery.utils.DisplayUtils;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;

public class GalleryActivity extends AppCompatActivity
        implements OnThumbClickListener, GalleryView {
    @BindView(R.id.photos_recyclerView)
    RecyclerView photosRecyclerView;

    @BindView(R.id.empty_textView)
    TextView emptyTextView;

    @BindView(R.id.images_loading_progressBar)
    ProgressBar loadingProgressBar;

    GridLayoutManager photosLayoutManager;
    PhotoRecyclerViewAdapter adapter;
    private List<Photo> photos;

    private GalleryPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        ButterKnife.bind(this);
        photosLayoutManager = new GridLayoutManager(this, DisplayUtils.photoPerRow());

        photosRecyclerView.setLayoutManager(photosLayoutManager);

        presenter = new GalleryPresenterImpl(this);
        presenter.loadImages(this);
        /**
        if (savedInstanceState != null) {
            String photosJson = savedInstanceState.getString(PhotoCacheService.EXTRA_PHOTOS);
            photos = PhotoSerializableUtils.photoListFromJson(photosJson);
            loadDataIntoView();
        } else {
            startPhotoCacheService();
        }
   */
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (photos != null) {
            outState.putString(PhotoCacheService.EXTRA_PHOTOS, PhotoSerializableUtils.photoListToJson(photos));
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClickOnThumb(final int photoIndex, final ImageView imageView, final ProgressBar progressBar) {
        //fetch image before going to next acitivity
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(photos.get(photoIndex).originalUrl())
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        navigateToGalleryActivity(photoIndex, imageView);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(GalleryActivity.this, "A problem occured fetching image", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void navigateToGalleryActivity(int photoIndex, ImageView imageView) {
        Intent intent = new Intent(this, GalleryPagerActivity.class);

        Gson gson = new Gson();
        intent.putExtra(GalleryPagerActivity.EXTRA_PHOTOS, gson.toJson(photos));
        intent.putExtra(GalleryPagerActivity.EXTRA_SELECTED_INDEX, photoIndex);
        String transitionName = photos.get(photoIndex).getId();
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
        startActivity(intent, options.toBundle());
    }


    private void loadDataIntoView() {
        adapter = new PhotoRecyclerViewAdapter(photos, GalleryActivity.this);
        photosRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
         loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showList(List<Photo> photoList) {
        this.photos = photoList;
       loadDataIntoView();
    }

    @Override
    public void showNoItem() {
        emptyTextView.setVisibility(View.VISIBLE);
    }
}

