package ikbal.com.photoviewer.model;



/**
 * Created by ikbal on 11/08/2017.
 */

public class Photo {
    private String id;
    private String secret;
    private String server;
    private String farm;

    public String getId() {
        return id;
    }

    /**
     * Photo size specs has been explained in the following link
     * https://www.flickr.com/services/api/misc.urls.html
     */

    //320 for longest side
    public String thumbUrl() {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s_n.jpg", this.farm, this.server, this.id, this.secret);
    }
//big image url
    public String bigImageUrl() {
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s_b.jpg", this.farm, this.server, this.id, this.secret);
    }
}
