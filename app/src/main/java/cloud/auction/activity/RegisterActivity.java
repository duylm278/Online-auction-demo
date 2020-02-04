package cloud.auction.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;

import cloud.auction.R;
import cloud.auction.model.ObjectResponse;
import cloud.auction.service.UserService;
import cloud.auction.service.VolleyCallback;

import static java.security.AccessController.getContext;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout email;
    private TextInputLayout fullname;
    private TextInputLayout phone;
    private TextInputLayout password;
    private ObjectMapper om = new ObjectMapper();
    LinearLayout linearLayout;
    TextInputLayout textInputLayout;
    InputMethodManager imm;
    final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        innitView();

        hideKeyboard(this);

    }
    public void hideKeyboard(Activity activity) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        linearLayout= activity.findViewById(R.id.outRegister);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), flags);
            }
        });
    }

    private void innitView(){
        email= findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_to_left,R.anim.right_out);
    }


    public void toRegister(View view) {

        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        String inputEmail = email.getEditText().getText().toString();
        String inputFullname = fullname.getEditText().getText().toString();
        String inputPhone = phone.getEditText().getText().toString();
        String inputPassword = password.getEditText().getText().toString();

        if (inputEmail.trim().isEmpty()){
            email.setError("Nhập sai");
            progressdialog.dismiss();
            return;
        }
        email.setError("");

        if (inputFullname.trim().isEmpty()){
            fullname.setError("Nhập sai");
            progressdialog.dismiss();
            return;
        }
        fullname.setError("");

        if (inputPhone.trim().isEmpty()){
            phone.setError("Nhập sai");
            progressdialog.dismiss();
            return;
        }
        phone.setError("");


        if (inputPassword.trim().isEmpty()){
            password.setError("Nhập sai");
            progressdialog.dismiss();
            return;
        }
        password.setError("");

        UserService.Register(this, inputEmail, inputFullname, inputPhone, inputPassword, new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = om.convertValue(data,ObjectResponse.class);
                progressdialog.dismiss();
                if (objectResponse.isSuccess()){

                    Toast.makeText(RegisterActivity.this, "Tạo Tài Khoản Thành Công !!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_to_left,R.anim.right_out);

                }else{
                    Toast.makeText(RegisterActivity.this, "Tài Khoản Đã Tồn Tại Hoặc Lỗi Đã Phát Sinh !!!", Toast.LENGTH_SHORT).show();
                }
                return;

            }
        });


    }
}