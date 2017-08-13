package ikbal.com.cookpadphotogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ikbal on 12/08/2017.
 */

public class ImageCacheTarget implements Target {
    private String url;

    public ImageCacheTarget(String url) {
        this.url = url;
    }


    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                File file = new File(Environment.getDataDirectory()+ "/imagecache/" + url.hashCode()+".jpg");
                try {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.flush();
                    ostream.close();
                } catch (IOException e) {
                    Log.e("IOException", e.getLocalizedMessage());
                }
            }
        }).start();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        Log.d("", "onBitmapFailed: ");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        Log.d("", "onPrepareLoad: ");
    }
}
