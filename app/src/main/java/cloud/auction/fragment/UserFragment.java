package cloud.auction.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import cloud.auction.R;
import cloud.auction.adapter.ViewPagerAdapter;

public class UserFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int selectedTab;
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        viewPager = getActivity().findViewById(R.id.accountviewpager);
        setupViewPager(viewPager);

        tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(selectedTab).select();
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new UserBiddingFragment(), "Đang đấu");
        adapter.addFrag(new UserHistoryFragment(), "Lịch sử");
        adapter.addFrag(new UserInfomationFragment(), "Thông tin");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        selectedTab= tabLayout.getSelectedTabPosition();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
