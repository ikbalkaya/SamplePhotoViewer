package ikbal.com.cookpadphotogallery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.R;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.utils.PhotoSerializableUtils;

/**
 * Created by ikbal on 14/08/2017.
 */

public class PhotoDetailFragment extends Fragment {
    private Photo photo;
    @BindView(R.id.originalImageView)
    ImageView originalImageView;
    @BindView(R.id.pager_progress_bar)
    ProgressBar progressBar;

    public static final String PHOTO_JSON = "photoJson";

    public static PhotoDetailFragment newInstance(String photoJson){
        PhotoDetailFragment detailFragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putString(PHOTO_JSON,photoJson);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().getString(PHOTO_JSON)!=null){
            photo = PhotoSerializableUtils.photoFromJson(getArguments().getString(PHOTO_JSON));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_pager,container,false);
        ButterKnife.bind(this,view);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load(photo.bigImageUrl())
                .into(originalImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                         scheduleStartPostponedTransition(originalImageView);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        scheduleStartPostponedTransition(originalImageView);
                       progressBar.setVisibility(View.GONE);
                        originalImageView.setImageResource(R.drawable.no_picture_available);
                    }
                });
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
}
