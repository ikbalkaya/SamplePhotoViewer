package ikbal.com.cookpadphotogallery.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.R;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.presenters.PhotoDetailPresenter;
import ikbal.com.cookpadphotogallery.presenters.PhotoDetailPresenterImpl;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;
import ikbal.com.cookpadphotogallery.view.PhotoDetailView;

/**
 * Created by ikbal on 14/08/2017.
 */

public class PhotoDetailFragment extends Fragment  implements PhotoDetailView{
    private Photo photo;
    private int position;
    private Target picassoTarget;

    @BindView(R.id.originalImageView)
    ImageView originalImageView;
    @BindView(R.id.pager_progress_bar)
    ProgressBar progressBar;

    private PhotoDetailPresenter presenter;

    public static final String PHOTO_JSON = "photoJson";

    public static PhotoDetailFragment newInstance(String photoJson) {
        PhotoDetailFragment detailFragment = new PhotoDetailFragment();

        Bundle args = new Bundle();
        args.putString(PHOTO_JSON, photoJson);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PhotoDetailPresenterImpl(this);

        if (getArguments() != null && getArguments().getString(PHOTO_JSON) != null) {
            photo = PhotoSerializableUtils.photoFromJson(getArguments().getString(PHOTO_JSON));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_pager, container, false);
        ButterKnife.bind(this, view);
        ViewCompat.setTransitionName(originalImageView,photo.getId());
        //load only if this is the current position
        presenter.startLoadingPhoto();

        return view;
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        getActivity().supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @Override
    public void loadThumbPhoto() {
       // progressBar.setVisibility(View.VISIBLE);
        //thumb is already downloaded
         Picasso.with(getActivity())
                 .load(photo.thumbUrl())
                 .into(originalImageView, new Callback() {
                     @Override
                     public void onSuccess() {
                         presenter.startLoadingBigSizePhoto();
                         scheduleStartPostponedTransition(originalImageView);
                     }

                     @Override
                     public void onError() {
                         originalImageView.setImageResource(R.drawable.no_image_available);
                         scheduleStartPostponedTransition(originalImageView);
                     }
                 });
    }

    @Override
    public void loadBigPhoto() {
        progressBar.setVisibility(View.VISIBLE);
        picassoTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                progressBar.setVisibility(View.GONE);
                originalImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.with(getActivity())
                .load(photo.bigImageUrl())
                .into(picassoTarget);

    }
}
