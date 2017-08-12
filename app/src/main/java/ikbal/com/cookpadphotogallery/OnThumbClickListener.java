package ikbal.com.cookpadphotogallery;

import android.widget.ImageView;

import ikbal.com.cookpadphotogallery.model.Photo;

/**
 * Created by ikbal on 12/08/2017.
 */

public interface OnThumbClickListener {
    void onClickOnThumb(int photoIndex, ImageView imageView) ;
}
