package ikbal.com.cookpadphotogallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import ikbal.com.cookpadphotogallery.utils.DisplayUtils;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;

public class GalleryListActivity extends AppCompatActivity implements OnThumbClickListener {
    @BindView(R.id.photos_recyclerView)
    RecyclerView photosRecyclerView;

    @BindView(R.id.empty_textView)
    TextView emptyTextView;

    GridLayoutManager photosLayoutManager;
    PhotoRecyclerViewAdapter adapter;
    private List<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        ButterKnife.bind(this);
        photosLayoutManager = new GridLayoutManager(this, DisplayUtils.photoPerRow());

        photosRecyclerView.setLayoutManager(photosLayoutManager);

        if (savedInstanceState != null) {
            String photosJson = savedInstanceState.getString(PhotoCacheService.EXTRA_PHOTOS);
            photos = PhotoSerializableUtils.photoListFromJson(photosJson);
            loadDataIntoView();
        } else {
            startPhotoCacheService();
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
    public void onClickOnThumb(final int photoIndex,final  ImageView imageView, final ProgressBar progressBar) {
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
                        Toast.makeText(GalleryListActivity.this, "A problem occured fetching image", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void navigateToGalleryActivity(int photoIndex, ImageView imageView) {
        Intent intent = new Intent(this, GalleryActivity.class);

        Gson gson = new Gson();
        intent.putExtra(GalleryActivity.EXTRA_PHOTOS, gson.toJson(photos));
        intent.putExtra(GalleryActivity.EXTRA_SELECTED_INDEX, photoIndex);
        String transitionName = photos.get(photoIndex).getId();
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageView, transitionName);
        startActivity(intent, options.toBundle());
    }

    private void startPhotoCacheService() {
        Intent photoCacheServiceIntent = new Intent(this, PhotoCacheService.class);
        ThumbCacheResultReceiver resultReceiver = new ThumbCacheResultReceiver(null);
        photoCacheServiceIntent.putExtra(PhotoCacheService.EXTRA_RECEIVER, resultReceiver);
        startService(photoCacheServiceIntent);
    }

    private class ThumbCacheResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        ThumbCacheResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == PhotoCacheService.PHOTOS_RECEIVED_CODE) {
                String photosJson = resultData.getString(PhotoCacheService.EXTRA_PHOTOS);
                photos = PhotoSerializableUtils.photoListFromJson(photosJson);
                emptyTextView.setVisibility(View.GONE);
                loadDataIntoView();
            }else if(resultCode == PhotoCacheService.PHOTOS_RECEIVED_FAILED_CODE){
                String error = resultData.getString(PhotoCacheService.EXTRA_FAIL_MESSAGE);
                Toast.makeText(GalleryListActivity.this, error, Toast.LENGTH_SHORT).show();
                emptyTextView.setVisibility(View.VISIBLE);
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }

    private void loadDataIntoView() {
        adapter = new PhotoRecyclerViewAdapter(photos, GalleryListActivity.this);
        photosRecyclerView.setAdapter(adapter);
    }
}

