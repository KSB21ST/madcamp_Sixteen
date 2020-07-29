package com.example.sixteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditInfo extends AppCompatActivity {
    private Context mContext;
    private SharedPreferences sp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String filePath = "";
    private String edit_name, edit_email, edit_pnb, edit_mbti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        mContext = this;

        //TAB3에서 전달된 값들을 받아온다
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        edit_name = extras.getString("user_name");
        edit_email = extras.getString("user_email");
        edit_pnb = extras.getString("user_pnb");
        edit_mbti = extras.getString("user_mbti");


        //바꿀 수 없는 것: email, name
        TextView edt_editinfo_email = (TextView) findViewById(R.id.editinfo_email);
        edt_editinfo_email.setText(edit_email);
        TextView edt_editinfo_name = (TextView) findViewById(R.id.editinfo_name);
        edt_editinfo_name.setText(edit_name);


        //mbti 선택하는 칸
        Spinner mbti_edt_Spinner = (Spinner) findViewById(R.id.editinfo_mbti);
        ArrayAdapter<String> mbti_edt_Adapter = new ArrayAdapter<String>(EditInfo.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.MBTI));
        mbti_edt_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mbti_edt_Spinner.setAdapter(mbti_edt_Adapter);


        //retrofit2 불러오기
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);


        //register위에 x 버튼
        Button btn_close_editinfo = (Button) findViewById(R.id.btn_close_editinfo);
        Button btn_photo_editinfo = (Button) findViewById(R.id.editinfo_photo);
        Button btn_next_editinfo = (Button) findViewById(R.id.editinfo_next);

        //사진 다시 지정
        btn_photo_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, galleryview.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        //종료 및 디비에 정보 전달
        btn_next_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("*******editInfo*****", "**^^^^^^^^^^^^^^**");
                //선택한 사진을 가져와서 db에 올려준다 --profile_img는 image_Adapter의 calimagebyview에서 저장된 이미지의 경로
                sp = getSharedPreferences("DB", MODE_PRIVATE);
                String profile_img_edit = sp.getString("serverpath", "");

                MaterialEditText edt_editinfo_phoneNumber = (MaterialEditText) findViewById(R.id.editinfo_phoneNumber);
                Spinner mbtiSpinner = (Spinner) findViewById(R.id.editinfo_mbti);

                //*****************************************************************************************************************************************************
                EditUser(edit_name,
                        edt_editinfo_phoneNumber.getText().toString(),
                        mbtiSpinner.getSelectedItem().toString(),
                        profile_img_edit
                );
            }
        });

        //종료버튼
        btn_close_editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void EditUser(String name, String phoneNumber, String mbti, String img) {
        Log.d("*******imguri*****", "**^&&&&&&&&&&&&&&&**");
        compositeDisposable.add(iMyService.EditUser(name, phoneNumber, mbti, img)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(EditInfo.this, "" + response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

}