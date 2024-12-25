package com.example.appmarket.fragment;

import static com.example.appmarket.util.AppHandler.getRecommendList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appmarket.R;
import com.example.appmarket.adapters.AppListAdapter;
import com.example.appmarket.entity.AppInfoEntity;

import java.util.List;
import java.util.stream.Collectors;

public class RecommendFragment extends Fragment {
    private List<AppInfoEntity> appList;
    private ListView appListview;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommend_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appListview = view.findViewById(R.id.recommend_listview);

        String string = getActivity().getSharedPreferences("market", Context.MODE_PRIVATE).getString("token", null);
        if (string == null){
            return;
        }
        appList = getRecommendList(string);
        showInfo(getActivity());
    }

    private void showInfo(Activity activity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (appList.isEmpty()){
                }
                appList = appList.stream().filter(AppInfoEntity::getRecommend).collect(Collectors.toList());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        appListview.setAdapter(new AppListAdapter(appList, getContext()));

                    }
                });
            }
        }).start();
    }
}
