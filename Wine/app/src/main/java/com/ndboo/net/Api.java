package com.ndboo.net;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/26.
 */

public interface Api {

    @POST(Urls.URL_LOGIN_BY_PASSWORD)
    Observable<String> loginByPassword(@Query("memberAccount") String name,
                             @Query("memberPassword") String password);

    @POST(Urls.URL_REGISTER)
    Observable<String> register(@Query("human_account") String humanAccount,
                                @Query("human_password") String humanPassword);
}
