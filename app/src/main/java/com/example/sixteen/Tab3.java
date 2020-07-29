package com.example.sixteen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.sixteen.Retrofit.IMyService;
import com.facebook.CallbackManager;

import io.reactivex.disposables.CompositeDisposable;

import static android.content.Context.MODE_PRIVATE;

public class Tab3  extends Fragment implements View.OnClickListener{

    private Context mContext = null;
    private View view;
    private SharedPreferences sp;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    CallbackManager callbackManager = CallbackManager.Factory.create();

    //btn_randcall, 전화걸 사람 번호 넣기
    String callNumber;
    private String pname, pemail, pmbti, pphoneNumber;
    private TextView tab3_name, tab3_phonenumber, tab3_email, tab3_mbti;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab3, container, false);

        //현재 로그인한 사용자
        sp = getActivity().getSharedPreferences("DB", MODE_PRIVATE);
        pname = sp.getString("pname", "");
        pemail = sp.getString("pemail", "");
        pmbti = sp.getString("pmbti", "");
        pphoneNumber = sp.getString("pphoneNumber", "");

        //현재 로그인 유저의 프로필이 뜨도록
        ImageView image = (ImageView) view.findViewById(R.id.tab3_img);
        Glide.with(getActivity()).load("http://192.249.19.244:1280/download/"+pemail).into(image);
        tab3_name = (TextView) view.findViewById(R.id.name_tab3);
        tab3_name.setText(pname);
        tab3_email = (TextView) view.findViewById(R.id.email_tab3);
        tab3_email.setText(pemail);
        tab3_mbti = (TextView) view.findViewById(R.id.mbti_tab3);
        tab3_mbti.setText(pmbti);
        tab3_phonenumber = (TextView) view.findViewById(R.id.phonenumber_tab3);
        tab3_phonenumber.setText(pphoneNumber);



        //버튼들!
        Button btn_edit = (Button) view.findViewById(R.id.btn_edit);
        Button btn_to = (Button) view.findViewById(R.id.btn_to);
        Button btn_likeme = (Button) view.findViewById(R.id.btn_likeme);
        Button btn_topics = (Button) view.findViewById(R.id.btn_topics);
        Button btn_randcall = (Button) view.findViewById(R.id.btn_randcall);

        btn_edit.setOnClickListener(this);
        btn_to.setOnClickListener(this);
        btn_likeme.setOnClickListener(this);
        btn_topics.setOnClickListener(this);
        btn_randcall.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_edit:
                //에딧 버튼 누르면 레지스터 같은 것이 뜨게
                //                //백엔드 정보를 수정해줘야 할 것 같음 db.user.update 정도?
                Intent i2 = new Intent(getActivity(), EditInfo.class);
                i2.putExtra("user_name", pname);
                i2.putExtra("user_email", pemail);
                i2.putExtra("user_pnb", pphoneNumber);
                i2.putExtra("user_mbti", pmbti);
                startActivity(i2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.btn_to:
                Intent intent = new Intent(getActivity(), tab3_to.class);
                intent.putExtra("user_name", pname);
                intent.putExtra("user_email", pemail);
                intent.putExtra("user_pnb", pphoneNumber);
                intent.putExtra("user_mbti", pmbti);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case R.id.btn_likeme:
                break;
            case R.id.btn_topics:
                //우성 오빠가 리스트 만들어놓음
                break;
            case R.id.btn_randcall:
                //랜덤으로 전화를 걸어야 한다 callNumber에 번호 설정
                //내가 좋아하는 사람을 대상으로 이차 솔팅-그 사람 리스트에 내가 있는지
                String phone = callNumber;
                String s = "tel:"+phone;
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse(s));
                startActivity(i);

        }

    }
}