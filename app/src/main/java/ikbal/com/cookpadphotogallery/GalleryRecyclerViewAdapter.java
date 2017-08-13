package ikbal.com.cookpadphotogallery;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.model.Photo;
import ikbal.com.cookpadphotogallery.utils.DisplayUtils;

/**
 * Created by ikbal on 11/08/2017.
 */

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.PhotoViewHolder> {
    private List<Photo> photos;
    private OnThumbClickListener listener;

    private  WeakHashMap<String,ImageView> viewMapForSharedElements = new WeakHashMap<>();

    public GalleryRecyclerViewAdapter(List<Photo> photos, OnThumbClickListener listener) {
        this.photos = photos;
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        if (photos != null) {
            return photos.size();
        }
        return 0;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_cell, parent, false);
        PhotoViewHolder viewHolder = new PhotoViewHolder(view);
        updateImageSize(viewHolder.galleryImageView);
        return viewHolder;
    }

    public View viewForSharedElementId(String id){
        return viewMapForSharedElements.get(id);
    }
    /*image size needs to be normalized to adapt the current screen */
    private void updateImageSize(ImageView imageView) {
        int calculatedDimension = DisplayUtils.thumbDimension();

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = calculatedDimension;//same as width
        layoutParams.width = calculatedDimension;

        imageView.setLayoutParams(layoutParams);

    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        final Photo photo = photos.get(position);
        final Context context = holder.itemView.getContext();
        //add imageview to shared element map
        viewMapForSharedElements.put(photo.getId(),holder.galleryImageView);
        Picasso.with(context)
                .load(photo.thumbUrl())
                .into(holder.galleryImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.galleryImageProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        holder.galleryImageProgressBar.setVisibility(View.GONE);
                        holder.galleryImageView.setImageResource(R.drawable.no_picture_available);
                    }
                });

        //setup shared element transition name
        ViewCompat.setTransitionName(holder.galleryImageView, photo.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickOnThumb(position, holder.galleryImageView, holder.galleryImageProgressBar);
            }
        });

    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gallery_imageView)
        ImageView galleryImageView;

        @BindView(R.id.gallery_image_progress)
        ProgressBar galleryImageProgressBar;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
