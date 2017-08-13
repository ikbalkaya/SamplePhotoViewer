package ikbal.com.cookpadphotogallery.model;



/**
 * Created by ikbal on 11/08/2017.
 */

public class Photo {
    private String id;
    private String secret;
    private String server;

    public String getId() {
        return id;
    }

    /**
     * Photo size specs has been explained in the following link
     * https://www.flickr.com/services/api/misc.urls.html
     */

    //320 for longest side
    public String thumbUrl() {
        int farmId = 1;
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s_n.jpg", farmId, this.server, this.id, this.secret);
    }
//big image url
    public String originalUrl() {
        int farmId = 1;
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s_b.jpg", farmId, this.server, this.id, this.secret);
    }
}
