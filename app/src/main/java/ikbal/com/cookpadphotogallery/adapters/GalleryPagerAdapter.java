package ikbal.com.cookpadphotogallery.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import ikbal.com.cookpadphotogallery.R;
import ikbal.com.cookpadphotogallery.model.Photo;


public class GalleryPagerAdapter extends PagerAdapter {
    private AppCompatActivity activity;
    private List<Photo> photos;

    public GalleryPagerAdapter(AppCompatActivity activity, List<Photo> photos) {
        this.activity = activity;
        this.photos = photos;
    }


    @Override
    public int getCount() {
        if (photos != null) {
            return photos.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout = layoutInflater.inflate(R.layout.layout_gallery_pager, container,
                false);

        final ImageView originalImageView = (ImageView) viewLayout.findViewById(R.id.originalImageView);
        final ProgressBar progressBar = (ProgressBar) viewLayout.findViewById(R.id.pager_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        ViewCompat.setTransitionName(originalImageView, photos.get(position).getId());
        Log.d("photoUrl", "instantiateItem: "+photos.get(position).originalUrl());
        Picasso.with(activity)
                .load(photos.get(position).originalUrl())
                .into(originalImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        scheduleStartPostponedTransition(originalImageView);
                    }

                    @Override
                    public void onError() {
                        originalImageView.setImageResource(R.drawable.no_image_available);
                        progressBar.setVisibility(View.GONE);
                        scheduleStartPostponedTransition(originalImageView);
                    }
                });

        container.addView(viewLayout);

        return viewLayout;
    }


    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        activity.supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }
}