package ikbal.com.cookpadphotogallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

public class PhotoListActivity extends AppCompatActivity {
    @BindView(R.id.photos_recyclerView)
    RecyclerView photosRecyclerView;

    RecyclerView.LayoutManager layoutManager;
    PhotoRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        ButterKnife.bind(this);
        layoutManager = new GridLayoutManager(this,2);
        photosRecyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoRecyclerViewAdapter();
        photosRecyclerView.setAdapter(adapter);
        FlickrApi api = ApiCreator.getApiReference();

        final Call<PhotoListResponse> photoListCall = api.fetchRecentPhotos(1000, 1);
        photoListCall.enqueue(new Callback<PhotoListResponse>() {
            @Override
            public void onResponse(Call<PhotoListResponse> call, Response<PhotoListResponse> response) {
                PhotoListResponse plr = response.body();
                Log.d("" , "onResponse: ");

                List<Photo> photos = plr.getPhotos();
                adapter.setPhotos(photos);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PhotoListResponse> call, Throwable t) {
                Toast.makeText(PhotoListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

