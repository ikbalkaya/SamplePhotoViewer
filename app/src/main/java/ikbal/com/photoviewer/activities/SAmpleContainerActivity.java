package ikbal.com.photoviewer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ikbal.com.photoviewer.fragments.PhotoGalleryFragment;
import ikbal.com.photoviewer.R;

public class SAmpleContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_container);
        getSupportFragmentManager().beginTransaction().add(PhotoGalleryFragment.newInstance(),"hello").commit();
    }
}
