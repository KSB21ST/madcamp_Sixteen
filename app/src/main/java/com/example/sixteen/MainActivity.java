package com.example.sixteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;
import com.example.sixteen.gallerypop.galleryview;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.LoginResult;
import com.facebook.FacebookException;
import com.facebook.FacebookCallback;


public class MainActivity extends AppCompatActivity {

    TextView txt_create_account;
    MaterialEditText edt_login_email,edt_login_password;
    Button btn_login;
    PackageInfo info;
    CallbackManager callbackManager;
    private Boolean isPermission = false;
    private LoginButton btn_fb_login;
    private Context mContext = null;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        mContext = this;


//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //Init View
        edt_login_email = (MaterialEditText) findViewById(R.id.login_email);
        edt_login_password = (MaterialEditText) findViewById(R.id.login_password);


        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인한 사용자의 email_id를 sharedpreference에 저장
                String email_id = edt_login_email.getText().toString();
                SharedPreferences sp = mContext.getSharedPreferences("DB",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email_id", email_id);
                editor.commit();
                loginUser(edt_login_email.getText().toString(),
                        edt_login_password.getText().toString());
            }
        });

        CallbackManager callbackManager = CallbackManager.Factory.create();

        btn_fb_login = findViewById(R.id.btn_fb_login);
        btn_fb_login.setReadPermissions("email");

        btn_fb_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            };
        });


        txt_create_account = (TextView) findViewById(R.id.txt_register);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Register.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }



    private void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        System.out.println("response : "+response);
                        if(response.equals("\"Login success\"")){
                            isPermission = true;
                        }
                        else {
                        }
                        goToActivity(MainActivity_content.class);
                        isPermission = false;
                    }
                }));
    }

    private void goToActivity(Class activityClass) {
        if(isPermission == true) {
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Wrong Email or Password", Toast.LENGTH_LONG).show();
        }
    }
}