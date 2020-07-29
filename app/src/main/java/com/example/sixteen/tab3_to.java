package com.example.sixteen;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.sixteen.Retrofit.Data;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class tab3_to extends AppCompatActivity {
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private IMyService iMyService;
    private JSONArray jsonArray;
    private SharedPreferences sp;
    private Context mContext;
    private String login_name, login_email, login_pnb, login_mbti;
    private List<String> favorite;
    private List<Data> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3_to);
        mContext = this;
        Log.d("***************%%", "oncreate");

        //TAB3에서 전달된 값들을 받아온다
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        login_name = extras.getString("user_name");
        login_email = extras.getString("user_email");
        login_pnb = extras.getString("user_pnb");
        login_mbti = extras.getString("user_mbti");

        //recyclerView 불러오기
        recyclerView = findViewById(R.id.recyclerView_to);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(tab3_to.this);
        recyclerView.setAdapter(adapter);

        //레트로핏으로 favorite 들 정보 불러오기
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        //favorite user 정보 가져오기
        iMyService.UserInfo().enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("retro", 1 + "");
                    Log.d("***************%%", "favorinfo");
                    try {
                        jsonArray = new JSONArray(response.body());
                        favorite = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray f = jsonArray.getJSONObject(i).getJSONArray("favorite");
                            for(int i2 = 0; i2 < f.length(); i2++){
                                favorite.add(f.getString(i));
                            }
                        }
                        Log.d("***************%%", "after favorinfo");
                        Log.d("**********2*****%%", favorite.toString());

                        //favor 정보인 애들 데이터 넣어주기
                        iMyService.UserInfo().enqueue(new Callback<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Log.d("***************%%", "after userinfo");
                                    Log.d("retro", 1 + "");
                                    try {
                                        jsonArray = new JSONArray(response.body());
                                        listData = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String name = jsonArray.getJSONObject(i).getString("name");
                                            Log.d("***************%%", name);
                                            for(int j = 0; j < favorite.size(); j++){
                                                if(name.equals(favorite.get(j))){
                                                    Log.d("%%%%%%%%%%%%%%", name);
                                                    String email = jsonArray.getJSONObject(i).getString("email");
                                                    Log.d("%%%%%&&&&&%%%", email);
                                                    String phoneNumber = jsonArray.getJSONObject(i).getString("phoneNumber");
                                                    Log.d("%%@@@@@@@@@@%", email);
                                                    String mbti = jsonArray.getJSONObject(i).getString("mbti");
                                                    Log.d("%%kkkkkkkkkkk%", email);
                                                    JSONArray favorite_json = jsonArray.getJSONObject(i).getJSONArray("favorite");
                                                    Log.d("***************", favorite_json.toString());
                                                    Data data = new Data(name, email, phoneNumber, mbti);
                                                    adapter.addItem(data);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }

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
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        adapter.notifyDataSetChanged();
    }
}