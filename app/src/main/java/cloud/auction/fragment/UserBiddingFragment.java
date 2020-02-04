package cloud.auction.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cloud.auction.R;
import cloud.auction.adapter.ProductRecycleAdapter;
import cloud.auction.adapter.UserBiddingRecycleAdapter;
import cloud.auction.model.Bid;
import cloud.auction.model.Category;
import cloud.auction.model.ObjectResponse;
import cloud.auction.service.BidService;
import cloud.auction.service.VolleyCallback;
import cloud.auction.ultils.Constant;

public class UserBiddingFragment extends Fragment {
    private RecyclerView listBid;
    private List<Bid> bids = new ArrayList<>();
    private UserBiddingRecycleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_bidding, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listBid = getActivity().findViewById(R.id.list_product_current_bidding);
        getBid();

    }
    private void getBid() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_MANAGER, Context.MODE_PRIVATE);
        String tokenName =  sharedPreferences.getString(Constant.TOKEN_NAME,"1");
        String tokenType =  sharedPreferences.getString(Constant.TOKEN_TYPE,"2");
        BidService.getCurrentBidByUser(getActivity(),tokenType+" "+tokenName, new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = (ObjectResponse)data;
                if (!objectResponse.getData().equals("empty")) {
                    bids = (List<Bid>) objectResponse.getData();
                    adapter = new UserBiddingRecycleAdapter(bids, getActivity());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    listBid.setLayoutManager(layoutManager);
                    listBid.setAdapter(adapter);
                }
            }
        });
    }
}
