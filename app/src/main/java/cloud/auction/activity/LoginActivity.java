package cloud.auction.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputLayout;

import cloud.auction.R;
import cloud.auction.model.ObjectResponse;
import cloud.auction.model.Token;
import cloud.auction.service.UserService;
import cloud.auction.service.VolleyCallback;
import cloud.auction.ultils.Constant;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout username;
    private TextInputLayout password;
    private ObjectMapper om = new ObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
    }


    public void toLogin(View view) {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.show();
        String inputUsername = username.getEditText().getText().toString();
        String inputPassword = password.getEditText().getText().toString();

        if (inputUsername.trim().isEmpty()){
            username.setError("Nhập sai");
            progressdialog.dismiss();
            return;
        }
        username.setError("");

        if (inputPassword.trim().isEmpty()){
            password.setError("Nhập sai");
            progressdialog.dismiss();
            return;
        }
        password.setError("");

        UserService.Login(getApplicationContext(), inputUsername  , inputPassword , new VolleyCallback() {
            @Override
            public void onSuccess(Object data) {
                ObjectResponse objectResponse = om.convertValue(data,ObjectResponse.class);


                if(objectResponse.isSuccess()&& objectResponse.isSuccess()){
                    Token token = om.convertValue(objectResponse.getData(),Token.class);
                    progressdialog.dismiss();
                    SharedPreferences sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(Constant.USER_MANAGER, Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(Constant.TOKEN_NAME,token.getAccessToken());
                    edit.putString(Constant.TOKEN_TYPE,token.getTokenType());
                    edit.apply();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
                }else{
                    progressdialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Đăng Nhập Thất Bại", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void toRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right,R.anim.left_out);
    }
}