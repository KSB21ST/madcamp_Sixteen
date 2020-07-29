package com.example.sixteen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;
import com.facebook.CallbackManager;
import com.facebook.share.Share;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Profile extends Activity implements View.OnClickListener {
    private Context mContext;
    private SharedPreferences sp;
    private String encoded;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    String pname;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_layout);
        mContext = this;

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //TAB1에서 전달된 값들을 받아온다
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        user_name = extras.getString("user_name");
        String  user_email = extras.getString("user_email");
        String user_pnb = extras.getString("user_pnb");
        String user_mbti = extras.getString("user_mbti");

        //textview를 설정해준다
        TextView prof_name = (TextView) findViewById(R.id.name_profile) ;
        prof_name.setText(user_name) ;
        TextView prof_email = (TextView) findViewById(R.id.email_profile) ;
        prof_email.setText(user_email) ;
        TextView prof_pnb = (TextView) findViewById(R.id.phonenumber_profile) ;
        prof_pnb.setText(user_pnb) ;
        TextView prof_mbti = (TextView) findViewById(R.id.mbti_profile) ;
        prof_mbti.setText(user_mbti) ;

        //이미지를 넣어준다
        ImageView profimg = (ImageView) findViewById(R.id.profile_img);
        Glide.with(mContext).load("http://192.249.19.244:1280/download/"+user_email).into(profimg);

        //버튼
        Button btn_fav_profile = (Button) findViewById(R.id.btn_fav_profile);
        btn_fav_profile.setOnClickListener(this);


        //현재 로그인한 사용자
        sp = getSharedPreferences("DB", MODE_PRIVATE);
        pname = sp.getString("pname", "");

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_fav_profile:
                //Post 해준다 -> favorite 에 user_name 추가
                compositeDisposable.add(iMyService.favoriteUser(pname, user_name)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                Toast.makeText(Profile.this, ""+response, Toast.LENGTH_SHORT).show();
                            }
                        }));
                break;
        }
    }
}
