package ikbal.com.cookpadphotogallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ikbal.com.cookpadphotogallery.model.Photo;

/**
 * Created by ikbal on 11/08/2017.
 */

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoViewHolder> {
    //list photos dependency use dagger
    private List<Photo> photos;

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public int getItemCount() {
        if(photos != null){
            return photos.size();
        }
        return 0;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_cell, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Picasso.with(holder.itemView.getContext())
                .load(photo.smallSizedUrl())
                .into(holder.thumbImageView);
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.thumb_imageView)
        ImageView thumbImageView;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
