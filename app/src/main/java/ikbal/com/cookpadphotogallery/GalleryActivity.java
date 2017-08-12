package ikbal.com.cookpadphotogallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.model.Photo;

/**
 * Created by ikbal on 12/08/2017.
 */

public class GalleryActivity extends AppCompatActivity {
    public static final String EXTRA_PHOTOS = "EXTRA_PHOTOS";
    public static final String EXTRA_SELECTED_INDEX = "EXTRA_SELECTED_INDEX";
    @BindView(R.id.gallery_pager)
    ViewPager galleryPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);
       // supportPostponeEnterTransition();

        ButterKnife.bind(this);
        String photosJson = getIntent().getStringExtra(EXTRA_PHOTOS);
        if (photosJson != null){
            Gson gson = new Gson();
            List<Photo> photos = gson.fromJson(photosJson, new TypeToken<List<Photo>>(){}.getType());
            int selectedIndex = getIntent().getIntExtra(EXTRA_SELECTED_INDEX,0);
            GalleryPagerAdapter pagerAdapter = new GalleryPagerAdapter(this,photos);
            galleryPager.setAdapter(pagerAdapter);
            galleryPager.setCurrentItem(selectedIndex);
        }
    }
}
