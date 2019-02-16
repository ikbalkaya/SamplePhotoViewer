package ikbal.com.photoviewer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ikbal.com.photoviewer.R;

public class GallerySelectorActivity extends AppCompatActivity {
    private final static String[] galleryTypes = new String[]{"Detail","Top"};
    private final static Class[] activities =
            new Class[]{GalleryActivity.class,GalleryTopActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Class<GalleryActivity> galleryActivityClass = GalleryActivity.class;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_selector);
        final ListView selectorListView = findViewById(R.id.selector_listView);
        selectorListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, galleryTypes));
        selectorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(GallerySelectorActivity.this,activities[i]));
            }
        });
    }
}
