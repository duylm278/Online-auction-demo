package cloud.auction.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;

import cloud.auction.R;
import cloud.auction.model.ObjectResponse;
import cloud.auction.model.User;
import cloud.auction.service.UserService;
import cloud.auction.service.VolleyCallback;
import cloud.auction.ultils.Constant;

public class UserInfomationFragment extends Fragment {

    private LinearLayout linearLayout;
    private TextInputLayout textInputLayout;
    private InputMethodManager imm;
    private TextInputLayout email;
    private TextInputLayout phone;
    private TextInputLayout fullname;
    private TextInputLayout address;
    private Button updateUser;
    private ObjectMapper om = new ObjectMapper();
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_infomation, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);

    }

    @Override
    public void onStart() {
        super.onStart();
        textInputLayout=getActivity().findViewById(R.id.address);
        linearLayout=getActivity().findViewById(R.id.out);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), flags);
            }
        });
        email= getActivity().findViewById(R.id.email);
        phone= getActivity().findViewById(R.id.phone);
        fullname= getActivity().findViewById(R.id.fullname);
        address= getActivity().findViewById(R.id.address);
        getUser();
        updateUser = getActivity().findViewById(R.id.btbGoToUpdateUser);
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_MANAGER, Context.MODE_PRIVATE);
                String tokenName =  sharedPreferences.getString(Constant.TOKEN_NAME,"1");
                String tokenType =  sharedPreferences.getString(Constant.TOKEN_TYPE,"2");
                UserService.Update(getActivity(), address.getEditText().getText().toString(),tokenType + " " + tokenName, new VolleyCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        ObjectResponse objectResponse = (ObjectResponse)data;
                        if (objectResponse.isSuccess()){
                            Toast.makeText(getActivity(), "Cập Nhật Thông Tin Thành Công !!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void getUser(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.USER_MANAGER, Context.MODE_PRIVATE);
        String tokenName =  sharedPreferences.getString(Constant.TOKEN_NAME,"1");
        String tokenType =  sharedPreferences.getString(Constant.TOKEN_TYPE,"2");
        UserService.getUserDetail(getActivity(), tokenType + " " + tokenName, new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = (ObjectResponse)data;
                if (!objectResponse.getData().equals("empty")) {
                    User user = om.convertValue(objectResponse.getData(),User.class);
                    email.getEditText().setText(user.getEmail());
                    phone.getEditText().setText(user.getPhone());
                    fullname.getEditText().setText(user.getFullName());
                    address.getEditText().setText(user.getAddress());
                }
            }
        });
    }


}
