package com.example.appmarket.util;

import androidx.annotation.NonNull;

import com.example.appmarket.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHandler {
    public static final OkHttpClient client = new OkHttpClient();

    /***
     * 进行JSON的post
     * @param url 目标地址
     * @param headers 头信息
     * @param data json数据
     * @return 服务端返回数据
     */
    public static CompletableFuture<String> sendJsonPost(String url, Map<String, String> headers, String data){
        if (data == null){
            data = "{}";
        }

        RequestBody requestBody = RequestBody.create(data, MediaType.parse("application/json; charset=utf-8"));
        Request build = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(requestBody)
                .build();

        CompletableFuture<String> future = new CompletableFuture<>();
        client.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()){
                    future.completeExceptionally(new IOException(response.toString()));
                }else {
                    future.complete(response.body().string());
                }
            }
        });
        return future;
    }

    public static CompletableFuture<String> get(String url, Map<String, String> headers){
        Request build = new Request.Builder()
                .url(url)
                .get()
                .headers(Headers.of(headers))
                .build();

        CompletableFuture<String> future = new CompletableFuture<>();
        client.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                future.complete(response.body().string());
            }
        });

        return future;
    }
}
