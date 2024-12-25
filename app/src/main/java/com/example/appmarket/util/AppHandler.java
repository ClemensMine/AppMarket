package com.example.appmarket.util;

import com.example.appmarket.MainActivity;
import com.example.appmarket.entity.AppInfoEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AppHandler {
    /***
     * 获得所有apps
     * @param token 登陆令牌
     * @return app信息
     */
    public static List<AppInfoEntity> getRecommendList(String token){
        Map<String,String> h = new HashMap<>();
        h.put("Authorization", "Bearer " + token);

        List<AppInfoEntity> appList = new ArrayList<>();

        CompletableFuture<String> future = HttpHandler.get(MainActivity.URL + "/dev-api/bs-app-store/app/list", h);
        future.thenAcceptAsync(res->{
            try {
                JSONObject data = new JSONObject(res);
                JSONArray apps = data.getJSONArray("data");

                for (int i = 0; i < apps.length(); i++) {
                    JSONObject appInfo = apps.getJSONObject(i);
                    AppInfoEntity info = new AppInfoEntity();
                    info.setName(appInfo.getString("apkName"));
                    info.setDesc(appInfo.getString("introduce"));
                    info.setDownloadLink(appInfo.getString("downloadPath"));
                    info.setDownNum(appInfo.getInt("apkDownNum"));
                    info.setRecommend(appInfo.getInt("recommend") == 1);
                    info.setLogoUrl(MainActivity.URL + appInfo.getString("logoPic"));
                    info.setBigPicUrl(MainActivity.URL + appInfo.getString("bgPic"));
                    appList.add(info);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.allOf(future).join();

        return appList;
    }

    /***
     * 搜索应用
     * @param name 传入名字
     * @param originalList 原始数据
     * @return 模糊搜索后的数据
     */
    public static List<AppInfoEntity> filterApps(String name, List<AppInfoEntity> originalList){
        if (name.isEmpty()){
            return originalList;
        }

        List<AppInfoEntity> filteredData = new ArrayList<>();

        for (AppInfoEntity app : originalList) {
            if (app.getName().contains(name)){
                filteredData.add(app);
            }
        }

        return filteredData;
    }
}
