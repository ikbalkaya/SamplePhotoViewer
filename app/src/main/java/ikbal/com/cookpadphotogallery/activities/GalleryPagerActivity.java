package ikbal.com.cookpadphotogallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.GalleryPagerAdapter;
import ikbal.com.cookpadphotogallery.R;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.presenters.GalleryPagerPresenter;
import ikbal.com.cookpadphotogallery.presenters.GalleryPagerPresenterImpl;
import ikbal.com.cookpadphotogallery.view.GalleryPagerView;

/**
 * Created by ikbal on 12/08/2017.
 */

public class GalleryPagerActivity extends AppCompatActivity implements GalleryPagerView{
    public static final String EXTRA_PHOTOS = "EXTRA_PHOTOS";
    public static final String EXTRA_SELECTED_INDEX = "EXTRA_SELECTED_INDEX";
    @BindView(R.id.gallery_pager)
    ViewPager galleryPager;

    private GalleryPagerPresenter presenter;
    private List<Photo> photos;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_pager);

        //postpone transition as the image load is asynchronous
        supportPostponeEnterTransition();

        ButterKnife.bind(this);
        presenter = new GalleryPagerPresenterImpl(this);

        String photosJson = getIntent().getStringExtra(EXTRA_PHOTOS);
        if (photosJson != null) {
            Gson gson = new Gson();
            photos = gson.fromJson(photosJson, new TypeToken<List<Photo>>() {
            }.getType());
            int selectedIndex = getIntent().getIntExtra(EXTRA_SELECTED_INDEX, 0);
            presenter.loadPhoto(selectedIndex);
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_INDEX,galleryPager.getCurrentItem());
        setResult(RESULT_OK,intent);
        super.finish();
    }

    @Override
    public void showItem(int position) {
        GalleryPagerAdapter pagerAdapter = new GalleryPagerAdapter(this, photos);
        galleryPager.setAdapter(pagerAdapter);
        galleryPager.setCurrentItem(position);
    }
}
