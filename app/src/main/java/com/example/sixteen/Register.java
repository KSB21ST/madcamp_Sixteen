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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sixteen.Retrofit.Data;
import com.example.sixteen.Retrofit.IMyService;
import com.example.sixteen.Retrofit.RetrofitClient;
import com.example.sixteen.gallerypop.galleryview;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

public class Register extends AppCompatActivity {
    private Context mContext;
    private SharedPreferences sp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    String filePath = "";
    private List<Data> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout_new);
        mContext = this;

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        Spinner mbtiSpinner = (Spinner) findViewById(R.id.register_mbti);
        ArrayAdapter<String> mbtiAdapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.MBTI));
        mbtiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mbtiSpinner.setAdapter(mbtiAdapter);


        //register위에 x 버튼
        Button btn_login = (Button) findViewById(R.id.btn_close_register);
        Button btn_photo = (Button) findViewById(R.id.btn_photo);
        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, galleryview.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("*******imguri*****", "**^^^^^^^^^^^^^^**");
                //선택한 사진을 가져와서 db에 올려준다 --profile_img는 image_Adapter의 calimagebyview에서 저장된 이미지의 경로
                sp = getSharedPreferences("DB", MODE_PRIVATE);
                String profile_img = sp.getString("serverpath", "");

//                //create file and pass it
                File file = new File(profile_img);
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//                // add another part within the multipart request
                RequestBody fullName =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), "foo");

                MaterialEditText edt_register_email = (MaterialEditText) findViewById(R.id.register_email);
                MaterialEditText edt_register_name = (MaterialEditText) findViewById(R.id.register_name);
                MaterialEditText edt_register_phoneNumber = (MaterialEditText) findViewById(R.id.register_phoneNumber);
                MaterialEditText edt_register_password = (MaterialEditText) findViewById(R.id.register_password);
                Spinner mbtiSpinner = (Spinner) findViewById(R.id.register_mbti);

                if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                    Toast.makeText(Register.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
                    Toast.makeText(Register.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                    Toast.makeText(Register.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(edt_register_email.getText().toString(),
                        edt_register_name.getText().toString(),
                        edt_register_phoneNumber.getText().toString(),
                        edt_register_password.getText().toString(),
                        mbtiSpinner.getSelectedItem().toString(),
                        profile_img

                );

                RequestBody requestEmail =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), edt_register_email.getText().toString());

//                uploadImage();
//                String s = imguri(edt_register_email.getText().toString());
//                Log.d("*******register*****", s);


            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerUser(String email, String name, String phoneNumber, String password, String mbti, String img) {
        Log.d("*******imguri*****", "**^&&&&&&&&&&&&&&&**");
        compositeDisposable.add(iMyService.registerUser(email, name, phoneNumber, password, mbti, img)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(Register.this, "" + response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

//    private void uploadImage() {
//        File file = new File(filePath);
//
//        Retrofit retrofit = RetrofitClient.getInstance();
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);
//
//        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");
//        Call call = iMyService.uploadImage(parts, someData);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
//    }

//    private String imguri(String email){
//        Log.d("*******imguri*****", "*********************");
//        final String[] uri = {""};
//        Call call = iMyService.imgUrl(email);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                uri[0] = response.body().toString();
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
//        return uri[0];
//    }
}