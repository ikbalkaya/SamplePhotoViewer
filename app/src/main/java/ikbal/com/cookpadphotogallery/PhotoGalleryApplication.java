package ikbal.com.cookpadphotogallery;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by ikbal on 12/08/2017.
 */

public class PhotoGalleryApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //configure Picasso
        //https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
