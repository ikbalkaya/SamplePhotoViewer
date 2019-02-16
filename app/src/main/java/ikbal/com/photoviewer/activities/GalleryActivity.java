package ikbal.com.photoviewer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ikbal.com.photoviewer.adapters.GalleryRecyclerViewAdapter;
import ikbal.com.photoviewer.R;
import ikbal.com.photoviewer.fragments.PhotoGalleryFragment;
import ikbal.com.photoviewer.model.Photo;
import ikbal.com.photoviewer.presenters.GalleryPresenter;
import ikbal.com.photoviewer.presenters.GridPresenter;
import ikbal.com.photoviewer.services.PhotoCacheService;
import ikbal.com.photoviewer.view.GalleryView;
import ikbal.com.photoviewer.utils.DisplayUtils;
import ikbal.com.photoviewer.utils.PhotoSerializableUtils;

public class GalleryActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, PhotoGalleryFragment.newInstance()).commit();
    }

    /***
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (photos != null) {
            outState.putString(PhotoCacheService.EXTRA_PHOTOS, PhotoSerializableUtils.photoListToJson(photos));
        }
        super.onSaveInstanceState(outState);
    }


    /**
     * https://youtu.be/4L4fLrWDvAU?t=1964
     */
    /**
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        final int selectedIndex = data.getIntExtra(GalleryPagerActivity.EXTRA_SELECTED_INDEX, 0);
        final Photo photo = photos.get(selectedIndex);

        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                View view = adapter.viewForSharedElementId(selectedIndex);
                if (view != null) {
                    sharedElements.put(photo.getId(), view);
                }

            }
        });
    }

    **/
}

