package ikbal.com.cookpadphotogallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import ikbal.com.cookpadphotogallery.model.Photo;

/**
 * Created by ikbal on 11/08/2017.
 */

public class GalleryPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private AppCompatActivity activity;
    private List<Photo> photos;

    public GalleryPagerAdapter(AppCompatActivity activity, List<Photo> photos) {
        this.activity = activity;
        this.photos = photos;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = layoutInflater.inflate(R.layout.layout_gallery, container,
                false);

        final ImageView originalImageView = (ImageView) viewLayout.findViewById(R.id.originalImageView);

        //first put small image and replace when the original imag downloaded
        Picasso.with(activity)
                .load(photos.get(position).smallSizedUrl())
                .into(originalImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        //load original
                        Picasso.with(activity)
                                .load(photos.get(position).originalUrl())
                                .into(originalImageView);
                    }

                    @Override
                    public void onError() {
//do nothing
                    }
                });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }
}
