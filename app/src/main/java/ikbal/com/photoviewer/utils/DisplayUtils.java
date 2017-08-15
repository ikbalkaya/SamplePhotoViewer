package ikbal.com.photoviewer.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by ikbal on 12/08/2017.
 */

public class DisplayUtils {
    public static final int MIN_THUMB_SIZE = 250;

      static int getScreenWidth() {
         final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

         return displayMetrics.widthPixels;
    }

    public static int photoPerRow(){
        int screenWidth = getScreenWidth();
       return screenWidth/ MIN_THUMB_SIZE;
    }
    public static int thumbDimension(){
        int screenWidth = getScreenWidth();
        int numberOfPhotos = photoPerRow();
        //distribute remaining to minwidth
        int remainingWidth = screenWidth - (DisplayUtils.MIN_THUMB_SIZE * numberOfPhotos);
        int calculatedWidth = remainingWidth/numberOfPhotos + DisplayUtils.MIN_THUMB_SIZE;
        return calculatedWidth;
    }
}
