package cloud.auction.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import cloud.auction.R;
import cloud.auction.adapter.ProductRecycleAdapter;
import cloud.auction.model.Bid;
import cloud.auction.model.Category;
import cloud.auction.model.ObjectResponse;
import cloud.auction.service.BidService;
import cloud.auction.service.CategoryService;
import cloud.auction.service.VolleyCallback;

public class AuctionFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView listBid;
    private List<Bid> bids = new ArrayList<>();
    private List<Category> cates;
    private ProductRecycleAdapter adapter;
    private NavigationView navigationView;
    private Menu menu;
    private MenuItem menuItem;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private ObjectMapper om = new ObjectMapper();
    private LinearLayout processBar;

    public AuctionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_drawer, container, false);

//        Toolbar toolbar = (Toolbar) v.findViewById(R.id.my_app_bar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        innitView();
//        getBid();
//        getCategory();
        processBar.setVisibility(View.GONE);

    }

    private void getCategory() {
        menu = navigationView.getMenu();
        menuItem = menu.add(1, 0, 0, "Tất cả");
        menuItem.setIcon(R.mipmap.ic_select_all);
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        CategoryService.getAll(getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                menu = navigationView.getMenu();
                ObjectResponse objectResponse = (ObjectResponse) data;
                if (!objectResponse.getData().equals("empty")) {
                    cates = (List<Category>) objectResponse.getData();
                    for (int i = 0; i < cates.size(); i++) {
                        Category category = om.convertValue(cates.get(i), Category.class);
                        menuItem = menu.add(1, i + 1, i + 1, category.getName());
                        menuItem.setIcon(R.mipmap.ic_transfer);
                        menuItem.setCheckable(true);
                    }
                }

            }
        });
    }

    private void innitView() {
        listBid = getActivity().findViewById(R.id.list_product);
        navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        processBar = getActivity().findViewById(R.id.progress_bar);
        drawer = getActivity().findViewById(R.id.drawer_layout);
        toolbar = getActivity().findViewById(R.id.toolbar);
//        toolbar.setVerticalScrollbarPosition();
        toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void getBid() {
        BidService.getAll(getActivity(), new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = (ObjectResponse) data;
                if (!objectResponse.getData().equals("empty")) {
                    bids = (List<Bid>) objectResponse.getData();
                    adapter = new ProductRecycleAdapter(bids, getActivity());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    listBid.setLayoutManager(layoutManager);
                    listBid.setAdapter(adapter);
                }

            }
        });
    }

    private void getBidByCate(int cateID) {
        BidService.getBidByCate(getActivity(), cateID, new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = (ObjectResponse) data;
                if (!objectResponse.getData().equals("empty")) {

                    bids = (List<Bid>) objectResponse.getData();
                    adapter = new ProductRecycleAdapter(bids, getActivity());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                    listBid.setLayoutManager(layoutManager);
                    listBid.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cates == null || cates.isEmpty()) {
            getCategory();
        }
        if (bids == null || bids.isEmpty()) {
            getBid();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 0) {
            getBid();
            toolbar.setTitle("Sản phẩm");
//            Toast.makeText(getActivity(), "0", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i <= cates.size(); i++) {
                if (id == i) {
                    Category category = om.convertValue(cates.get(i - 1), Category.class);
                    toolbar.setTitle(category.getName());
////                    Toast.makeText(getActivity(), id + ":" +cates.size()+":" + cates.get(i-1).getName() , Toast.LENGTH_SHORT).show();
                    getBidByCate(category.getId());
                }
            }
        }
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
