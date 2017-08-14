package ikbal.com.cookpadphotogallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ikbal.com.cookpadphotogallery.GalleryRecyclerViewAdapter;
import ikbal.com.cookpadphotogallery.OnThumbClickListener;
import ikbal.com.cookpadphotogallery.R;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.presenters.GalleryPresenter;
import ikbal.com.cookpadphotogallery.presenters.GalleryPresenterImpl;
import ikbal.com.cookpadphotogallery.services.PhotoCacheService;
import ikbal.com.cookpadphotogallery.view.GalleryView;
import ikbal.com.cookpadphotogallery.utils.DisplayUtils;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;

public class GalleryActivity extends AppCompatActivity
        implements OnThumbClickListener, GalleryView {
    private static final int PAGER_REQUEST_CODE = 20;
    @BindView(R.id.photos_recyclerView)
    RecyclerView photosRecyclerView;

    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    @BindView(R.id.images_loading_progressBar)
    ProgressBar loadingProgressBar;

    private GridLayoutManager photosLayoutManager;
    private GalleryRecyclerViewAdapter adapter;
    private List<Photo> photos;

    private GalleryPresenter presenter;
    private MenuItem refreshMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        photosLayoutManager = new GridLayoutManager(this, DisplayUtils.photoPerRow());

        photosRecyclerView.setLayoutManager(photosLayoutManager);

        presenter = new GalleryPresenterImpl(this);

        if (savedInstanceState != null) {
            String photosJson = savedInstanceState.getString(PhotoCacheService.EXTRA_PHOTOS);
            photos = PhotoSerializableUtils.photoListFromJson(photosJson);
            loadGalleryImages();
        } else {
            presenter.loadImages(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (photos != null) {
            outState.putString(PhotoCacheService.EXTRA_PHOTOS, PhotoSerializableUtils.photoListToJson(photos));
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallery, menu);
        refreshMenuItem = menu.getItem(0);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh){
            //reload evertthing
            presenter.loadImages(this);
        }
        return true;
    }

    /**
     * https://youtu.be/4L4fLrWDvAU?t=1964
     * */
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        final int selectedIndex = data.getIntExtra(GalleryPagerActivity.EXTRA_SELECTED_INDEX,0);
        final Photo photo = photos.get(selectedIndex);

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                View view = adapter.viewForSharedElementId(selectedIndex);
                if (view != null){
                    sharedElements.put(photo.getId(),view);
                }

            }
        });
    }

    @OnClick(R.id.retry_button)
    public void retry(View view) {
        presenter.loadImages(this);
    }

    @Override
    public void onClickOnThumb(final int photoIndex, final ImageView imageView, final ProgressBar progressBar) {
        navigateToPager(photoIndex, imageView);
    }

    private void navigateToPager(int photoIndex, ImageView imageView) {
        Intent intent = new Intent(this, GalleryPagerActivity.class);

        Gson gson = new Gson();
        intent.putExtra(GalleryPagerActivity.EXTRA_PHOTOS, gson.toJson(photos));
        intent.putExtra(GalleryPagerActivity.EXTRA_SELECTED_INDEX, photoIndex);
        String transitionName = photos.get(photoIndex).getId();
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);

       // startActivity(intent, options.toBundle());
        //
        startActivityForResult(intent, PAGER_REQUEST_CODE, options.toBundle());
    }


    private void loadGalleryImages() {
        adapter = new GalleryRecyclerViewAdapter(photos, GalleryActivity.this);
        photosRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        if(refreshMenuItem != null){
            refreshMenuItem.setEnabled(false);
        }
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showList(List<Photo> photoList) {
        if(refreshMenuItem != null){
            refreshMenuItem.setEnabled(true);
        }

        this.photos = photoList;
        loadGalleryImages();
    }

    @Override
    public void showNoItem() {
        if(refreshMenuItem != null){
            refreshMenuItem.setEnabled(false);
        }

        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoItem() {
        emptyView.setVisibility(View.GONE);
    }
}

