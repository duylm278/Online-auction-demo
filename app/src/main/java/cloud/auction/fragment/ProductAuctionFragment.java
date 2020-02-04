package cloud.auction.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import cloud.auction.R;
import cloud.auction.activity.Main2Activity;
import cloud.auction.adapter.SliderImageAdapter;
import cloud.auction.model.Bid;
import cloud.auction.model.Bidding;
import cloud.auction.model.ObjectResponse;
import cloud.auction.service.BidService;
import cloud.auction.service.OfferService;
import cloud.auction.service.VolleyCallback;
import cloud.auction.ultils.Constant;
import ss.com.bannerslider.ImageLoadingService;
import ss.com.bannerslider.Slider;

public class ProductAuctionFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private Slider proImage;
    private TextView proName,proTimeExpiredValue,proCurrentPriceValue,proBetsCurrentPriceValue;
    private Button btnGotoAuction;
    private long timeCount = 10000000;
    private Spinner dropdown;
    private ArrayAdapter<String>adapter;
    private ObjectMapper om = new ObjectMapper();
    private String bidID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_auct, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        innitView();
        Intent intent = getActivity().getIntent();
        bidID = intent.getStringExtra("BidID");
        setValueView();

        btnGotoAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_MANAGER, Context.MODE_PRIVATE);
                String tokenName = sharedPreferences.getString(Constant.TOKEN_NAME, "1");
                String tokenType = sharedPreferences.getString(Constant.TOKEN_TYPE, "2");
                OfferService.Offer(getActivity(), bidID, dropdown.getSelectedItem().toString(), tokenType + " " + tokenName, new VolleyCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        ObjectResponse objectResponse = (ObjectResponse)data;
                        if (objectResponse.isSuccess()){
                            Toast.makeText(getActivity(), "Đấu Thành Công !!!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Đấu Thất Bại !!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
    private void innitView(){
        dropdown = getActivity().findViewById(R.id.spinner);
        proImage=getActivity().findViewById(R.id.proImage);
        proName=getActivity().findViewById(R.id.proName);
        proCurrentPriceValue=getActivity().findViewById(R.id.proCurrentPriceValue);
//        proBetsCurrentPriceValue=getActivity().findViewById(R.id.proBetsCurrentPriceValue);
        proTimeExpiredValue=getActivity().findViewById(R.id.proTimeExpiredValue);
        btnGotoAuction=getActivity().findViewById(R.id.btnGotoAuction);
    }
    private void setValueView(){

        BidService.getBidByID(getActivity(), bidID,new VolleyCallback() {

            @Override
            public void onSuccess(Object data) {
                Bid bid = om.convertValue(data,Bid.class);
//                Bid bid =bidding.getBidding();
//                Picasso.get().load(bid.getProd1uct().getImages().get(1).getImage())
//                        .resize(150, 120)
//                        .centerCrop()
//                        .into(proImage);
                Slider.init(new ImageLoadingService() {
                    @Override
                    public void loadImage(String url, ImageView imageView) {
                        imageView.setImageBitmap(BitmapFactory.decodeFile(url));
                    }

                    @Override
                    public void loadImage(int resource, ImageView imageView) {
                        imageView.setImageResource(resource);
                    }

                    @Override
                    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
                    }
                });

                proImage.setAdapter(new SliderImageAdapter(bid.getProduct().getImages(),getActivity()));
                proImage.setSelectedSlide(0);
                proName.setText(bid.getProduct().getName());
                proCurrentPriceValue.setText(String.format("%,d",Long.parseLong(bid.getCurrentPrice()+""))+" VNĐ");
                adapter = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item);
                for (int i = 1; i <= 20 ; i++) {
//                    adapter.add(String.format("%,d",Integer.parseInt(bid.getProduct().getPrice())*i));
                    adapter.add(bid.getCurrentPrice()+i*50000+"");
                }
                dropdown.setAdapter(adapter);
                LocalDateTime end =LocalDateTime.parse(bid.getEndTime());
//                LocalDateTime start =LocalDateTime.parse(bid.getStartTime());
                Long timeEnd = end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                Long timeNow = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                if(timeEnd-timeNow>0){
                    timeCount=timeEnd-timeNow;
                }else {
                    timeCount = 0;
                    proTimeExpiredValue.setText("00:00:00:00");
                }
                new CountDownTimer(timeCount,1000){
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timeCount = millisUntilFinished;
                        long day = (timeCount/(1000*60*60))/24;
                        long hours = (timeCount/(1000*60*60))%24;
                        long minutes = (timeCount/(1000*60))%60;
                        long seconds = (timeCount/1000)%60;
                        String string = String.format(Locale.getDefault(),"%02d:%02d:%02d:%02d",day,hours,minutes,seconds);
                        proTimeExpiredValue.setText(string);
                    }

                    @Override
                    public void onFinish() {
                    }
                }.start();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
