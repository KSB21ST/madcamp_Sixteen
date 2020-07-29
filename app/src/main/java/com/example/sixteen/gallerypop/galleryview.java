package com.example.sixteen.gallerypop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.sixteen.R;

public class galleryview extends AppCompatActivity implements View.OnClickListener{

    private GridView mgridView;
    private SharedPreferences sp;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleryview);
        mContext = this;
        mgridView = (GridView) findViewById(R.id.grid_view);
        final Image_Adapter ia = new Image_Adapter(this);
        mgridView.setAdapter(ia);
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView parent, View v, int position, long id){
                ia.callImageViewer(position);
            }
        });
        Button btn_gallery = (Button) findViewById(R.id.btn_back_galleryview);
        btn_gallery.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_galleryview:
                this.finish();
                break;
        }
    }
}