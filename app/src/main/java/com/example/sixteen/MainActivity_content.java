package com.example.sixteen;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.sixteen.Retrofit.Data;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity_content extends AppCompatActivity {

    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1,tab2,tab3,tab4;
    public PageAdapter pageradapter;
    private SharedPreferences sp;
    private ArrayList<Data> listData;
    JSONArray jsonArray;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        sp = getSharedPreferences("DB", MODE_PRIVATE);
        final String email_id = sp.getString("email_id", "");
        final SharedPreferences.Editor editor = sp .edit();
//        mContext = this;
        //action bar에 이름 표시
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        iMyService.UserInfo().enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("retro", 1 + "");
                    try {
                        editor.clear();
                        jsonArray = new JSONArray(response.body());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String email = jsonArray.getJSONObject(i).getString("email");

                            //이메일이 일치하는 사람 ==  로그인한 사람
                            if(email.equals(email_id)){
                                Log.d("equal?", email+"");
                                String name = jsonArray.getJSONObject(i).getString("name");
                                String phoneNumber = jsonArray.getJSONObject(i).getString("phoneNumber");
                                String mbti = jsonArray.getJSONObject(i).getString("mbti");

                                //SharedPreference에 userinfo를 넣어준다
                                editor.putString("pemail", email);
                                editor.putString("pname", name);
                                editor.putString("pphoneNumber", phoneNumber);
                                editor.putString("pmbti", mbti);
                                editor.commit();

                                //Tab1의 고정된 윗줄 설정해준다
                                TextView txView = (TextView) findViewById(R.id.user_name_hello);
                                txView.setText(name);

                                TextView user_name = (TextView)findViewById(R.id.user_name);
                                TextView user_phonenumber = (TextView)findViewById(R.id.user_phonenumber);
                                user_name.setText(name);
                                user_phonenumber.setText(phoneNumber);

                                break;
                            }
//                                        listData.add(new Data(name, email, phoneNumber));
                            //                            String imgpath = jsonArray.getJSONObject(i).getString("imgpath");
//                            Log.d("imgpath%%%%%%%%%%%",imgpath+"");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
//        String pname = sp.getString("pname", "");
//        TextView txView = (TextView) findViewById(R.id.user_name);
//        txView.setText(pname);

        tabLayout =(TabLayout) findViewById(R.id.tablayout);
        tab1=(TabItem) findViewById(R.id.Tab1);
        tab2=(TabItem) findViewById(R.id.Tab2);
        tab3=(TabItem) findViewById(R.id.Tab3);
        tab4=(TabItem) findViewById(R.id.Tab4);
        viewPager=findViewById(R.id.viewpager);
        pageradapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageradapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        final int[] ICONS = new int[] {
                R.drawable.ic_contact,
                R.drawable.ic_search,
                R.drawable.ic_love,
                R.drawable.ic_list
        };

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(ICONS[0]);
        tabLayout.getTabAt(1).setIcon(ICONS[1]);
        tabLayout.getTabAt(2).setIcon(ICONS[2]);
        tabLayout.getTabAt(3).setIcon(ICONS[3]);
    }
}
