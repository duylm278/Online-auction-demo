package cloud.auction.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import cloud.auction.R;
import cloud.auction.adapter.ProductRecycleAdapter;
import cloud.auction.adapter.UserHistoryRecycleAdapter;
import cloud.auction.model.Bid;
import cloud.auction.model.ObjectResponse;
import cloud.auction.service.BidService;
import cloud.auction.service.VolleyCallback;
import cloud.auction.ultils.Constant;

public class UserHistoryFragment extends Fragment {
    private RecyclerView listBid;
    private List<Bid> bids = new ArrayList<>();
    private UserHistoryRecycleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_history, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        getBid();
    }

    private void getBid() {
        listBid = getActivity().findViewById(R.id.list_user_history);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_MANAGER, Context.MODE_PRIVATE);
        String tokenName = sharedPreferences.getString(Constant.TOKEN_NAME, "1");
        String tokenType = sharedPreferences.getString(Constant.TOKEN_TYPE, "2");
        BidService.getFinishedBidByUser(getActivity(), tokenType + " " + tokenName, new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = (ObjectResponse) data;
                if (!objectResponse.getData().equals("empty")) {
                    bids = (List<Bid>) objectResponse.getData();
                    adapter = new UserHistoryRecycleAdapter(bids, getActivity());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    listBid.setLayoutManager(layoutManager);
                    listBid.setAdapter(adapter);
                }

            }
        });
    }


}
