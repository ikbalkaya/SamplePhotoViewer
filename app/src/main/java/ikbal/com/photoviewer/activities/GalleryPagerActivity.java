package ikbal.com.photoviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.photoviewer.fragments.PhotoDetailFragment;
import ikbal.com.photoviewer.R;
import ikbal.com.photoviewer.model.Photo;
import ikbal.com.photoviewer.presenters.GalleryPagerPresenter;
import ikbal.com.photoviewer.presenters.GalleryPagerPresenterImpl;
import ikbal.com.photoviewer.utils.PhotoSerializableUtils;
import ikbal.com.photoviewer.view.GalleryPagerView;


public class GalleryPagerActivity extends AppCompatActivity implements GalleryPagerView {
    public static final String EXTRA_PHOTOS = "EXTRA_PHOTOS";
    public static final String EXTRA_SELECTED_INDEX = "EXTRA_SELECTED_INDEX";
    @BindView(R.id.gallery_pager)
    ViewPager galleryPager;

    private GalleryHorizontalPagerAdapter galleryPagerAdapter;

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
    public void showItem(int position) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.addSharedElement(imageView, ViewCompat.getTransitionName(imageView))

        galleryPagerAdapter = new GalleryHorizontalPagerAdapter(fragmentManager);

        galleryPager.setAdapter(galleryPagerAdapter);
        galleryPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_INDEX,galleryPager.getCurrentItem());
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    private class GalleryHorizontalPagerAdapter extends FragmentStatePagerAdapter {

         GalleryHorizontalPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().addSharedElement()

            return PhotoDetailFragment.newInstance(PhotoSerializableUtils.photoToJson(photos.get(position)));
        }

        @Override
        public int getCount() {
            if(photos != null){
                return photos.size();
            }
            return 0;
        }
    }
}
