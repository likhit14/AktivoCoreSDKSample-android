package com.test.aktivocoresdksample_android;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Source {

    @POST("graphql/user/{userId}")
//    @GraphQuery("data_api")
    Call<ResponseBody> postAllUserData(
            @Path("userId") String userId,
            @Body RequestBody body
    );
}
