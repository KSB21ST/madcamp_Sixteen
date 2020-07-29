package com.example.sixteen.gallerypop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Image_Adapter extends BaseAdapter {
    private String imgData;
    private String geoData;
    private ArrayList<String> thumbsDataList;
    private ArrayList<String> thumbsIDList;
    private Context mContext;
    Image_Adapter(Context c){
        mContext = c;
        thumbsDataList = new ArrayList<String>();
        thumbsIDList = new ArrayList<String>();
        getThumbInfo(thumbsIDList, thumbsDataList);
    }


    public boolean deleteSelected(int sIndex){
        return true;
    }

    public int getCount() {
        return thumbsIDList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(340, 350));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        }else{
            imageView = (ImageView) convertView;
        }
        Glide.with(mContext).load(thumbsDataList.get(position)).into(imageView);

        return imageView;
    }

    public final void callImageViewer(int selectedIndex){
        //선택된 것을 bitmap으로 바꿔준다
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        Intent i = new Intent(mContext, ImagePopup.class);
        String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
        Bitmap bm = BitmapFactory.decodeFile(imgPath, bfo);

//        //bitmap을 string으로 바꿔준다
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encoded = Base64.encodeToString(b, Base64.DEFAULT);


        //파일 경로를 sharedpreference에 넣어준다
        SharedPreferences sp = mContext.getSharedPreferences("DB",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("serverpath");
        editor.putString("serverpath", encoded);
        editor.commit();

        //imagepop을 실행시켜준다
        i.putExtra("filename", imgPath);
        mContext.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    private void getThumbInfo(ArrayList thumbsIDs, ArrayList thumbsDatas){
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            String title;
            String thumbsID;
            String thumbsImageID;
            String thumbsData;
            String data;
            String imgSize;

            int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int num = 0;
            do {
                thumbsID = imageCursor.getString(thumbsIDCol);
                thumbsData = imageCursor.getString(thumbsDataCol);
                thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                imgSize = imageCursor.getString(thumbsSizeCol);
                num++;
                if (thumbsImageID != null){
                    thumbsIDs.add(thumbsID);
                    thumbsDatas.add(thumbsData);
                }
            }while (imageCursor.moveToNext());
        }
        imageCursor.close();
        return;
    }

    private String getImageInfo(String ImageData, String Location, String thumbID){
        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj, "_ID='"+ thumbID +"'", null, null);

        if (imageCursor != null && imageCursor.moveToFirst()){
            if (imageCursor.getCount() > 0){
                int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                imageDataPath = imageCursor.getString(imgData);
            }
        }
        imageCursor.close();
        return imageDataPath;
    }
}

