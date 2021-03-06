package ikbal.com.photoviewer.adapters;

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
import ikbal.com.photoviewer.R;
import ikbal.com.photoviewer.model.Photo;
import ikbal.com.photoviewer.utils.DisplayUtils;


public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.PhotoViewHolder> {
    private List<Photo> photos;
    private OnThumbClickListener listener;

    private  WeakHashMap<Integer,ImageView> viewMapForSharedElements = new WeakHashMap<>();

    public GalleryRecyclerViewAdapter(List<Photo> photos, OnThumbClickListener listener) {
        this.photos = photos;
        this.listener = listener;
    }
    public interface OnThumbClickListener {
        void onClickOnThumb(int photoIndex, ImageView imageView) ;
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

    public View viewForSharedElementId(int index){
        return viewMapForSharedElements.get(index);
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

        //add imageview to shared element map, helping for exit operation
        viewMapForSharedElements.put(holder.getAdapterPosition(),holder.galleryImageView);
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
                        holder.galleryImageView.setImageResource(R.drawable.no_image_available);
                    }
                });

        //setup shared element transition name
        ViewCompat.setTransitionName(holder.galleryImageView, photo.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickOnThumb(position, holder.galleryImageView);
            }
        });

    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.gallery_imageView)
        ImageView galleryImageView;

        @BindView(R.id.gallery_image_progress)
        ProgressBar galleryImageProgressBar;

         PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
