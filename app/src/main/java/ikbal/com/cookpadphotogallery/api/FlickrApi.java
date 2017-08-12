package ikbal.com.cookpadphotogallery.api;

import ikbal.com.cookpadphotogallery.model.PhotoListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ikbal on 11/08/2017.
 */

public interface FlickrApi {
    @GET(ApiConstants.BASE_API_URL+"/services/rest/?method=flickr.photos.getRecent&api_key="+
            ApiConstants.API_KEY+"&format=json&nojsoncallback=1")
    Call<PhotoListResponse> fetchRecentPhotos(@Query("per_page") int perPage, @Query("page") int page);
}
