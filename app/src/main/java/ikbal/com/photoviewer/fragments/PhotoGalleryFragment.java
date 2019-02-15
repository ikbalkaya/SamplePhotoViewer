package ikbal.com.photoviewer.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.photoviewer.activities.GalleryActivity;
import ikbal.com.photoviewer.activities.GalleryPagerActivity;
import ikbal.com.photoviewer.adapters.GalleryRecyclerViewAdapter;
import ikbal.com.photoviewer.model.Photo;
import ikbal.com.photoviewer.presenters.GalleryPresenter;
import ikbal.com.photoviewer.presenters.GridPresenter;
import ikbal.com.photoviewer.services.PhotoCacheService;
import ikbal.com.photoviewer.utils.DisplayUtils;
import ikbal.com.photoviewer.utils.PhotoSerializableUtils;
import ikbal.com.photoviewer.view.GalleryView;
import ikbal.com.photoviewer.R;


public class PhotoGalleryFragment extends Fragment implements GalleryRecyclerViewAdapter.OnThumbClickListener,
        GalleryView, SwipeRefreshLayout.OnRefreshListener {
    private static final int PAGER_REQUEST_CODE = 20;

    @BindView(R.id.photos_recyclerView)
    RecyclerView photosRecyclerView;

    @BindView(R.id.empty_view)
    LinearLayout emptyView;

    @BindView(R.id.empty_textView)
    TextView emptyTextView;

    @BindView(R.id.images_loading_progressBar)
    ProgressBar loadingProgressBar;

    @BindView(R.id.gallery_swipeRefreshLayout)
    SwipeRefreshLayout gallerySwipeRefreshLayout;

    private GridLayoutManager photosLayoutManager;
    private GalleryRecyclerViewAdapter adapter;
    private List<Photo> photos;

    private GalleryPresenter presenter;


    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View contentView = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        ButterKnife.bind(contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        photosLayoutManager = new GridLayoutManager(getActivity(), DisplayUtils.photoPerRow());
        photosRecyclerView.setLayoutManager(photosLayoutManager);

        presenter = new GridPresenter(this);

        if (savedInstanceState != null) {
            String photosJson = savedInstanceState.getString(PhotoCacheService.EXTRA_PHOTOS);
            photos = PhotoSerializableUtils.photoListFromJson(photosJson);
            updateRecyclerViewAdapter();
        } else {
            presenter.loadPhotoList((AppCompatActivity) getActivity());
        }
        //set refresh listener
        gallerySwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /***
         if (context instanceof OnFragmentInteractionListener) {
         mListener = (OnFragmentInteractionListener) context;
         } else {
         throw new RuntimeException(context.toString()
         + " must implement OnFragmentInteractionListener");
         }

         **/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //   mListener = null;
    }

    public void retry(View view) {
        presenter.loadPhotoList((AppCompatActivity) getActivity());
    }

    @Override
    public void onClickOnThumb(final int photoIndex, final ImageView imageView) {
        Intent intent = new Intent(getActivity(), GalleryPagerActivity.class);
        Gson gson = new Gson();
        intent.putExtra(GalleryPagerActivity.EXTRA_PHOTOS, gson.toJson(photos));
        intent.putExtra(GalleryPagerActivity.EXTRA_SELECTED_INDEX, photoIndex);
        String transitionName = photos.get(photoIndex).getId();
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imageView, transitionName);

        startActivityForResult(intent, PAGER_REQUEST_CODE, options.toBundle());
    }

    private void updateRecyclerViewAdapter() {
        adapter = new GalleryRecyclerViewAdapter(photos, this);
        photosRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showList(List<Photo> photoList) {
        this.photos = photoList;
        updateRecyclerViewAdapter();

        //also cancel refreshing
        gallerySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyView(String errorMessage) {
        emptyView.setVisibility(View.VISIBLE);
        emptyTextView.setText(errorMessage);

        //if it was refreshing cancel that as well
        gallerySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        presenter.refreshPhotoList((AppCompatActivity) getActivity());
    }
}
