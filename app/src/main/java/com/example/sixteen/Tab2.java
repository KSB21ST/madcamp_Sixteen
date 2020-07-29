package com.example.sixteen;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixteen.Retrofit.Data;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Tab2  extends Fragment {
    private RecyclerAdapter adapter;
    RecyclerView recyclerView;
    IMyService iMyService;
    JSONArray jsonArray;
    ArrayList<Data> listData;
    ImageButton btn_search;
    EditText bar_search;
    String input_mbti;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(getContext());
        recyclerView.setAdapter(adapter);

        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        bar_search = (EditText) view.findViewById(R.id.bar_search);
        //final EditText input_mbti = (EditText)view.findViewById(R.id.search_bar);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
        iMyService.UserInfo().enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                    btn_search.setOnClickListener( new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {

                            try {
                                adapter.clearItem();
                                Log.d("input_search", bar_search.getText().toString()+ "");
                                input_mbti = bar_search.getText().toString().toUpperCase();

                                jsonArray = new JSONArray(response.body());
                                listData = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    String email = jsonArray.getJSONObject(i).getString("email");
                                    String phoneNumber = jsonArray.getJSONObject(i).getString("phoneNumber");
                                    Log.d("phonenumber",phoneNumber+"");
                                    String mbti = jsonArray.getJSONObject(i).getString("mbti");
                                    Log.d("mbti",mbti+"");
                                    Data data = new Data(name, email, phoneNumber, mbti);
                                    if(mbti.equals(input_mbti)){
                                        adapter.addItem(data);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

               //}

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        adapter.notifyDataSetChanged();

        return view;
    }
}