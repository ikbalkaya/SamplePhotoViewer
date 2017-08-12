package ikbal.com.cookpadphotogallery.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by ikbal on 12/08/2017.
 */

public class DisplayUtils {
    static final int THUMB_SIZE_PX = 150+16;//plus padding

     static int getScreenWidth() {
         final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

         return displayMetrics.widthPixels;
    }

    public static int photoPerRow(){
        int screenWidth = getScreenWidth();
       return screenWidth/THUMB_SIZE_PX;
    }
}
