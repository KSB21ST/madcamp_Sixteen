package com.example.sixteen.Retrofit;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMyService {

    @GET("print")
    Call<String> UserInfo();

    @GET("download")
    Call<String> imgUrl(@Query("userEmail") String email);

    @GET("favoritep")
    Call<String> FavorInfo(@Query("user_name") String user_name);


    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("phoneNumber") String phoneNumber,
                                    @Field("password") String password,
                                    @Field("mbti") String mbti,
                                    @Field("img") String img
                                    );

    @POST("edit")
    @FormUrlEncoded
    Observable<String> EditUser(@Field("name") String name,
                                    @Field("phoneNumber") String phoneNumber,
                                    @Field("mbti") String mbti,
                                    @Field("img") String img
    );

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                    @Field("password") String password);

//    @Multipart
//    @POST("upload")
//    Call<ResponseBody> uploadImage(@Part MultipartBody.Part part,
//                                     @Part("somedata") RequestBody requestBody);

    @POST("favorite")
    @FormUrlEncoded
    Observable<String> favoriteUser(@Field("username") String username,
                                    @Field("favname") String favname);
}
