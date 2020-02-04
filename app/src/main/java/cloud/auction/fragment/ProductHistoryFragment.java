package cloud.auction.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cloud.auction.R;
import cloud.auction.adapter.PeopleAuctionRecycleAdapter;
import cloud.auction.model.Bidding;
import cloud.auction.model.User;

public class ProductHistoryFragment extends Fragment {
    private RecyclerView listPeoHis;
    private List<User> users = new ArrayList<>();
    private PeopleAuctionRecycleAdapter adapter;
    private ObjectMapper om = new ObjectMapper();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_history, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listPeoHis = getActivity().findViewById(R.id.list_people_auction);
        users = new ArrayList<>();

        Intent intent = getActivity().getIntent();
        String bidID = intent.getStringExtra("BidID");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("offers")
                .whereEqualTo("biddingId", bidID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        users.clear();
                        List<String> cities = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Map<String, Object> data = doc.getData();
                            User user = new User();
                            user.setFullName((String) data.get("accountName"));
                            user.setUsername( data.get("money").toString());
                            user.setEmail(data.get("time").toString());
                            users.add(user);
                        }

                        adapter = new PeopleAuctionRecycleAdapter(users, getActivity());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        listPeoHis.setLayoutManager(layoutManager);
                        listPeoHis.setAdapter(adapter);
                    }
                });

    }
}



