package ikbal.com.cookpadphotogallery.model;

/**
 * Created by ikbal on 11/08/2017.
 */

public class Photo {
    private String id;
    private String secret;
    private String server;


    /*large square 150x150*/
    public String thumbUrl(){
        int farmId = 1;
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s_q.jpg",farmId,this.server,this.id,this.secret);
    }
    public String originalUrl(){
        int farmId = 1;
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s_b.jpg",farmId,this.server,this.id,this.secret);
    }
    // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg

    //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg

}
