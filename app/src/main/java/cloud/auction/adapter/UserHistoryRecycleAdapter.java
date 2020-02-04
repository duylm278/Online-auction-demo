package cloud.auction.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import cloud.auction.R;
import cloud.auction.activity.ProductActivity;
import cloud.auction.model.Bid;
import cloud.auction.model.User;

public class UserHistoryRecycleAdapter extends RecyclerView.Adapter<UserHistoryRecycleAdapter.UserHistoryViewHolder> {
    private List<Bid> listUserHis;
    private Activity activity;
    private ObjectMapper om = new ObjectMapper();

    public UserHistoryRecycleAdapter(List<Bid> listUserHis, Activity activity) {
        this.listUserHis = listUserHis;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_history_items, parent, false);
        return new UserHistoryViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHistoryViewHolder holder, int position) {
        Bid bid = om.convertValue(listUserHis.get(position),Bid.class);
        holder.user_his_name.setText(bid.getProduct().getName());
        holder.user_his_price.setText(bid.getProduct().getPrice());
        LocalDateTime end =LocalDateTime.parse(bid.getEndTime());
        holder.user_his_time.setText(end.toLocalDate()+"");
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
        return listUserHis == null ? 0 : listUserHis.size();
    }

    public void updateCountText(){

    }

    class UserHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView user_his_name;
        private TextView user_his_price;
        private TextView user_his_time;

        public UserHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            user_his_name = itemView.findViewById(R.id.user_his_name);
            user_his_price = itemView.findViewById(R.id.user_his_price);
            user_his_time = itemView.findViewById(R.id.user_his_time);

        }
    }
}
