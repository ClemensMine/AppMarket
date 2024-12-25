package com.example.appmarket.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.appmarket.R;
import com.example.appmarket.adapters.AppListAdapter;
import com.example.appmarket.entity.AppInfoEntity;
import com.example.appmarket.util.AppHandler;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UpdateFragment extends Fragment {
    private ListView updateAppList;
    private List<AppInfoEntity> apps;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateAppList = view.findViewById(R.id.update_list);
        String token = getActivity().getSharedPreferences("market", Context.MODE_PRIVATE).getString("token", null);
        apps = AppHandler.getRecommendList(token);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (apps.isEmpty()){
                }
                apps = apps
                        .stream()
                                .filter(v -> new SecureRandom().nextBoolean())
                                        .collect(Collectors.toList());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateAppList.setAdapter(new AppListAdapter(apps, getContext()));
                    }
                });
            }
        }).start();
    }
}