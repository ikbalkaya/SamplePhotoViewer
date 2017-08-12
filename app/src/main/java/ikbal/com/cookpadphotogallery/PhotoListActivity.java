package ikbal.com.cookpadphotogallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.utils.DisplayUtils;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;

public class PhotoListActivity extends AppCompatActivity implements OnThumbClickListener {
    @BindView(R.id.photos_recyclerView)
    RecyclerView photosRecyclerView;

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

        if (savedInstanceState != null){
            String photosJson = savedInstanceState.getString(PhotoCacheService.EXTRA_PHOTOS);
            photos = PhotoSerializableUtils.photoListFromJson(photosJson);
            loadDataIntoView();
        }else{
            startPhotoCacheService();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(photos != null){
            outState.putString(PhotoCacheService.EXTRA_PHOTOS, PhotoSerializableUtils.photoListToJson(photos));
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClickOnThumb(int photoIndex, ImageView imageView) {
        Intent intent = new Intent(this, GalleryActivity.class);

        Gson gson = new Gson();

        intent.putExtra(GalleryActivity.EXTRA_PHOTOS, gson.toJson(photos));
        intent.putExtra(GalleryActivity.EXTRA_SELECTED_INDEX, photoIndex);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,imageView,getString(R.string.photo_transition_name));
        startActivity(intent,options.toBundle());
       // startActivity(intent);

        /*
        * ActivityOptionsCompat options = ActivityOptionsCompat.
    makeSceneTransitionAnimation(this, (View)ivProfile, "profile");
startActivity(intent, options.toBundle());
        * */
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
                loadDataIntoView();
            }
            super.onReceiveResult(resultCode, resultData);
        }
    }

    private void loadDataIntoView() {
        adapter = new PhotoRecyclerViewAdapter(photos, PhotoListActivity.this);
        photosRecyclerView.setAdapter(adapter);
    }
}

