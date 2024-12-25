package com.example.appmarket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appmarket.fragment.RecommendFragment;
import com.example.appmarket.fragment.SearchFragment;
import com.example.appmarket.fragment.UpdateFragment;
import com.example.appmarket.util.HttpHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static final String URL = "http://124.93.196.45:10001";
    public static final String USERNAME = "18910639713";
    public static final String PASSWORD = "123456";

    private BottomNavigationView bottomNavigationView;

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            login();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        fragments = new ArrayList<>();
        fragments.add(new RecommendFragment());
        fragments.add(new SearchFragment());
        fragments.add(new UpdateFragment());

        showFragment(fragments.get(0));

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.recommend_item){
                    showFragment(fragments.get(0));
                } else if (itemId == R.id.search_item) {
                    showFragment(fragments.get(1));
                } else if (itemId == R.id.update_item) {
                    showFragment(fragments.get(2));
                }
                return true;
            }
        });

    }

    /***
     * 切换fragment
     * @param fragment
     */
    private void showFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 登陆
     */
    public void login() throws JSONException, ExecutionException, InterruptedException {
        JSONObject data = new JSONObject();
        data.put("username", USERNAME);
        data.put("password", PASSWORD);
        CompletableFuture<String> future = HttpHandler.sendJsonPost(URL + "/dev-api/login", new HashMap<>(), data.toString());
        future.thenAccept(s -> {
            JSONObject res;
            try {
                res = new JSONObject(s);
                if (res.getInt("code") != 200){
                    return;
                }

                SharedPreferences preferences = getSharedPreferences("market", MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("token", res.getString("token"));
                edit.apply();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        CompletableFuture.allOf(future).join();
    }
}