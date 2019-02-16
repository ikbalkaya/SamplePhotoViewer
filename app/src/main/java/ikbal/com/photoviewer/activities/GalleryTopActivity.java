package ikbal.com.photoviewer.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.photoviewer.fragments.PhotoGalleryFragment;
import ikbal.com.photoviewer.R;

public class GalleryTopActivity extends AppCompatActivity {
    @BindView(R.id.selected_image_view)
    ImageView selectedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_container);
        ButterKnife.bind(this);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        final PhotoGalleryFragment photogalleryFragment = PhotoGalleryFragment.newInstance();

        ft.add(R.id.fragment_container, photogalleryFragment).commit();
    }
}
