package com.darren.okhttp_source;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private OkHttpClient mHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(new Cache()).build();
//
//        Request request = new Request.Builder().cacheControl(new CacheControl.Builder().noCache())
        File file = new File(Environment.getExternalStorageDirectory(),"cache");
        Cache cache = new Cache(file,100*1024*1024);
        mHttpClient = new OkHttpClient.Builder()
                .cache(cache)
//                // 加载最前 过期时间缓存多少秒
//                .addInterceptor(new CacheRequestInterceptor(this))
//                // 加载最后,数据缓存 过期时间 30s
//                .addNetworkInterceptor(new CacheResponseInterceptor())
                .build();

        String url = "https://api.saiwuquan.com/api/appv2/sceneModel";
        // 构建一个请求
        final Request request = new Request.Builder()
                .url(url).build();
        // new RealCall 发起请求
        Call call = mHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG",response.body().string());
                // 都是有 第一把，第二把没有网络的了只有缓存的 (30s 以内)，过了 30s 之后又会有网络的了（会再请求更新）
                Log.e("TAG", response.cacheResponse()+" ; "+response.networkResponse());
            }
        });
    }

}
