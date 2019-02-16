package ikbal.com.photoviewer;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by ikbal on 12/08/2017.
 */

public class PhotoGalleryApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,100000000));
        Picasso picasso = builder.build();
        Picasso.setSingletonInstance(picasso);
    }
}
