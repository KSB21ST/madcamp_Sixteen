package com.example.sixteen.Retrofit;

import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

public class Data {

    public Data(String name, String email, String PhoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = PhoneNumber;
    }

    public Data(String name, String email, String PhoneNumber, String mbti){
        this.name = name;
        this.email = email;
        this.phoneNumber = PhoneNumber;
        this.mbti = mbti;
    }

    public Data(String name, String email, String PhoneNumber, String mbti, String imgpath){
        this.name = name;
        this.email = email;
        this.phoneNumber = PhoneNumber;
        this.mbti = mbti;
        this.img = imgpath;
    }

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("salt")
    @Expose
    private String salt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("mbti")
    @Expose
    private String mbti;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("img")
    @Expose
    private List<Data> favorite = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String phoneNumber) {
        this.mbti = mbti;
    }

    public String getImgPath() {return img;}

    public void setImgPath(String phoneNumber) {
        this.img = img;
    }

    public List<Data> getFavorite() { return favorite;}

    public void setFavorite(List<Data> favorite) {
        this.favorite = favorite;
    }
}