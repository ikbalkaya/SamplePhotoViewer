package ikbal.com.cookpadphotogallery.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ikbal on 11/08/2017.
 */

public class PhotoListResponse {
    private int page;
    @SerializedName("photo")
    private List<Photo> photos;

    public int getPage() {
        return page;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}
