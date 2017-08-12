package ikbal.com.cookpadphotogallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Gallery;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.api.ApiCreator;
import ikbal.com.cookpadphotogallery.api.FlickrApi;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.model.PhotoListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoListActivity extends AppCompatActivity implements OnThumbClickListener{
    public static final int VERTICAL_SPAN_COUNT = 2;
    public static final int HORIZONTAL_SPAN_COUNT = 1;
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
        photosLayoutManager = new GridLayoutManager(this, VERTICAL_SPAN_COUNT);
        photosRecyclerView.setLayoutManager(photosLayoutManager);

        retrieveGalleryPhotos();
    }

    private void retrieveGalleryPhotos(){
        FlickrApi api = ApiCreator.getApiReference();

        final Call<PhotoListResponse> photoListCall = api.fetchRecentPhotos(1000, 1);
        photoListCall.enqueue(new Callback<PhotoListResponse>() {
            @Override
            public void onResponse(Call<PhotoListResponse> call, Response<PhotoListResponse> response) {
                PhotoListResponse photoListResponse = response.body();
                photos = photoListResponse.getPhotos();
                adapter = new PhotoRecyclerViewAdapter(photos,PhotoListActivity.this);
                photosRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PhotoListResponse> call, Throwable t) {
                Toast.makeText(PhotoListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickOnThumb(int photoIndex) {
        Intent intent = new Intent(this,GalleryActivity.class);

        Gson gson = new Gson();

        intent.putExtra(GalleryActivity.EXTRA_PHOTOS,gson.toJson(photos));
        intent.putExtra(GalleryActivity.EXTRA_SELECTED_INDEX,photoIndex);
        startActivity(intent);
    }
}

