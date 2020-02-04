package cloud.auction.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cloud.auction.R;
import cloud.auction.activity.ProductActivity;
import cloud.auction.model.User;

public class PeopleAuctionRecycleAdapter extends RecyclerView.Adapter<PeopleAuctionRecycleAdapter.PeopleAuctionViewHolder> {
    private List<User> listPeoHis;
    private Activity activity;

    public PeopleAuctionRecycleAdapter(List<User> listPeoHis, Activity activity) {
        this.listPeoHis = listPeoHis;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PeopleAuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_auction_items, parent, false);
        return new PeopleAuctionViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final PeopleAuctionViewHolder holder, int position) {
        User user = listPeoHis.get(position);
        holder.peo_his_name.setText(user.getFullName());
        holder.peo_his_price.setText(user.getUsername());
        holder.peo_his_time.setText(user.getEmail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProductActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPeoHis == null ? 0 : listPeoHis.size();
    }

    public void updateCountText(){

    }

    class PeopleAuctionViewHolder extends RecyclerView.ViewHolder {

        private TextView peo_his_name;
        private TextView peo_his_price;
        private TextView peo_his_time;

        public PeopleAuctionViewHolder(@NonNull View itemView) {
            super(itemView);
            peo_his_name = itemView.findViewById(R.id.peo_his_name);
            peo_his_price = itemView.findViewById(R.id.peo_his_price);
            peo_his_time = itemView.findViewById(R.id.peo_his_time);

        }
    }
}
