package cloud.auction.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import cloud.auction.R;
import cloud.auction.activity.ProductActivity;
import cloud.auction.model.Bid;
import cloud.auction.model.Image;

public class ProductRecycleAdapter extends RecyclerView.Adapter<ProductRecycleAdapter.ProductViewHolder> {
    private List<Bid> listBids;
    private Activity activity;
    private long timeCount;
    private ObjectMapper om = new ObjectMapper();
    public ProductRecycleAdapter(List<Bid> listBids, Activity activity) {
        this.listBids = listBids;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items, parent, false);
        return new ProductViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        final Bid bid = om.convertValue(listBids.get(position),Bid.class);
        List<Image> images = bid.getProduct().getImages();
                Picasso.get().load(images.get(0).getImage())
                .into(holder.imageView);
        holder.proName.setText(bid.getProduct().getName());
        holder.proCurrentPrice.setText("Giá hiện tại: "+String.format("%,d",Long.parseLong(bid.getCurrentPrice()+""))+ " VNĐ");
        LocalDateTime end =LocalDateTime.parse(bid.getEndTime());
//        LocalDateTime start =LocalDateTime.parse(bid.getStartTime());
        Long timeEnd = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long timeNow = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        if(timeEnd-timeNow>0){
            timeCount=timeEnd-timeNow;
        }else {
            timeCount = 0;
            holder.proTimeExpired.setText("Thời gian còn: 00:00:00:00");
        }
//        Toast.makeText(activity, timeCount+"", Toast.LENGTH_SHORT).show();
        new CountDownTimer(timeCount,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timeCount = millisUntilFinished;
                long day = (timeCount/(1000*60*60))/24;
                long hours = (timeCount/(1000*60*60))%24;
                long minutes = (timeCount/(1000*60))%60;
                long seconds = (timeCount/1000)%60;
                String string = String.format(Locale.getDefault(),"%02d:%02d:%02d:%02d",day,hours,minutes,seconds);

                holder.proTimeExpired.setText("Thời gian còn: "+ string);
            }

            @Override
            public void onFinish() {

            }
        }.start();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProductActivity.class);
                intent.putExtra("BidID", bid.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBids == null ? 0 : listBids.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView proName;
        private TextView proTimeExpired;
        private TextView proCurrentPrice;
        private TextView proBetsCurrent;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.proImage);
            proName = itemView.findViewById(R.id.proName);
            proTimeExpired = itemView.findViewById(R.id.proTimeExpired);
            proCurrentPrice = itemView.findViewById(R.id.proCurrentPrice);
//            proBetsCurrent = itemView.findViewById(R.id.proBetsCurrent);
        }
    }
}
