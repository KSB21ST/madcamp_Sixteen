package com.example.sixteen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sixteen.Retrofit.Data;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Tab1 extends Fragment{
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private IMyService iMyService;
    private JSONArray jsonArray;
    private ArrayList<Data> listData;
    private SharedPreferences sp;
    private Context mContext;
    private  View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        iMyService.UserInfo().enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("retro", 1 + "");
                    try {
                        jsonArray = new JSONArray(response.body());
                        listData = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String name = jsonArray.getJSONObject(i).getString("name");
                            String email = jsonArray.getJSONObject(i).getString("email");
                            String phoneNumber = jsonArray.getJSONObject(i).getString("phoneNumber");
                            String mbti = jsonArray.getJSONObject(i).getString("mbti");
                            Data data = new Data(name, email, phoneNumber, mbti);
                            adapter.addItem(data);
                            adapter.notifyDataSetChanged();
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
        return view;
    }
}
