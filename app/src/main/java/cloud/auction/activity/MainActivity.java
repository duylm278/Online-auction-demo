package cloud.auction.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import cloud.auction.R;
import cloud.auction.adapter.ViewPagerAdapter;
import cloud.auction.fragment.AuctionFragment;
import cloud.auction.fragment.UserFragment;


public class MainActivity extends AppCompatActivity {

    private MenuItem prevMenuItem;
    private BottomNavigationView bottomNavigation;
    private ViewPager viewPager;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        addControls();
        addEvents();

        setBottomNavigationConfig();
        setupViewPager(viewPager);

    }

    private void addControls() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager);
    }

    private void addEvents() {

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setBottomNavigationSelectedItem(item);
                switch (item.getItemId()) {
                    case R.id.nav_auction:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_user:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    drawer = findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                if (prevMenuItem != null)
                    prevMenuItem.setChecked(false);
                else
                    bottomNavigation.getMenu().getItem(0).setChecked(false);

                MenuItem item = bottomNavigation.getMenu().getItem(position);
                setBottomNavigationSelectedItem(item);
                item.setChecked(true);
                prevMenuItem = item;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setBottomNavigationConfig() {
        bottomNavigation.setItemIconTintList(null);
        bottomNavigation.getMenu().getItem(0).setIcon(R.mipmap.nav_auction_selected);
    }

    private void setBottomNavigationSelectedItem(@NonNull MenuItem item) {

        Menu menu = bottomNavigation.getMenu();
        menu.getItem(0).setIcon(R.mipmap.nav_auction);
        menu.getItem(1).setIcon(R.mipmap.nav_user);

        switch (item.getItemId()) {
            case R.id.nav_auction:
                item.setIcon(R.mipmap.nav_auction_selected);
                break;
            case R.id.nav_user:
                item.setIcon(R.mipmap.nav_user_selected);
                break;
        }

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment auctionFragment = new AuctionFragment();
        Fragment userFragment = new UserFragment();

        adapter.addFrag(auctionFragment, "auction");
        adapter.addFrag(userFragment, "user");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Quit")
                .setMessage("Logout")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("no", null)
                .show();
    }
    //    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        }
////        DrawerLayout drawer = findViewById(R.id.drawer_layout);
////        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
