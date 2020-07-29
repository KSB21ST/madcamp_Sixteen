package com.example.sixteen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.Share;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixteen.Retrofit.Data;

import java.nio.InvalidMarkException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    private  SharedPreferences sp;
    private ImageView profile_item;

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();
    RecyclerAdapter(Context c){
        mContext = c;
    }
    private Context mContext;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        Log.d("%%%%%%%%%%%%%%", "onbindviewholder");
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    void clearItem() {
        listData.clear();
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView phonenumber;
        private String mbti;
        private String email;
        private ImageView profile_item;
        private ImageView profile_item_me;
        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phonenumber = itemView.findViewById(R.id.phonenumber);
            profile_item = itemView.findViewById(R.id.item_img);
            profile_item_me = itemView.findViewById(R.id.user_img);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, Profile.class);
                    String user_name = name.getText().toString();
                    String user_phonenumber = phonenumber.getText().toString();
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("user_email", email);
                    intent.putExtra("user_pnb", user_phonenumber);
                    intent.putExtra("user_mbti", mbti);
                    mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });

        }

        void onBind(Data data) {
            name.setText(data.getName());
            phonenumber.setText(data.getPhoneNumber());
            mbti = data.getMbti();
            email = data.getEmail();

//            sp = mContext.getSharedPreferences("DB", MODE_PRIVATE);
//            String pemail = sp.getString("pemail", "");
//            Log.d("%%%%%%%%pemail", pemail);

            Glide.with(mContext).load("http://192.249.19.244:1280/download/"+email).into(profile_item);
//            Glide.with(mContext).load("http://192.249.19.244:1280/download/"+pemail).into(profile_item_me);
        }

    }
}