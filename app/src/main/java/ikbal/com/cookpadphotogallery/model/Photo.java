package ikbal.com.cookpadphotogallery.model;

/**
 * Created by ikbal on 11/08/2017.
 */

public class Photo {
    private String id;
    private String secret;
    private String server;


    /*Max 240 on longest side, to show on gallery list */
    public String smallSizedUrl(){
        int farmId = 1;
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s_m.jpg",farmId,this.server,this.id,this.secret);
    }
    public String originalUrl(){
        int farmId = 1;
        return String.format("https://farm%d.staticflickr.com/%s/%s_%s_o.jpg",farmId,this.server,this.id,this.secret);
    }
    // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg

    //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg

}
