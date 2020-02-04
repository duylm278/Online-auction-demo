package cloud.auction.adapter;

import android.app.Activity;

import com.squareup.picasso.Picasso;

import java.util.List;

import cloud.auction.model.Image;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class SliderImageAdapter extends SliderAdapter {

    private List<Image> images;
    private Activity activity;

    public SliderImageAdapter(List<Image> images, Activity activity) {
        this.images = images;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return images==null ? 0: images.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        Picasso.get().load(images.get(position).getImage())
//                .resize(200, 250)

//                .centerCrop()
                .into(imageSlideViewHolder.imageView);
    }
}
