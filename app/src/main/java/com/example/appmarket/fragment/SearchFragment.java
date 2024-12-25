package com.example.appmarket.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appmarket.R;
import com.example.appmarket.adapters.AppListAdapter;
import com.example.appmarket.entity.AppInfoEntity;
import com.example.appmarket.util.AppHandler;

import java.util.List;

public class SearchFragment extends Fragment {
    private EditText searchEditText;
    private Button searchBtn;
    private ListView searchResultList;

    private List<AppInfoEntity> apps;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchEditText = view.findViewById(R.id.search_src_text);
        searchBtn = view.findViewById(R.id.search_button);
        searchResultList = view.findViewById(R.id.search_listview);

        String token = getActivity().getSharedPreferences("market", Context.MODE_PRIVATE).getString("token", null);
        apps = AppHandler.getRecommendList(token);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().trim();
                List<AppInfoEntity> filteredApps = AppHandler.filterApps(searchText, apps);

                if (filteredApps.isEmpty()){
                    Toast.makeText(getContext(), "暂无结果", Toast.LENGTH_SHORT).show();
                    return;
                }

                searchResultList.setAdapter(new AppListAdapter(filteredApps, getContext()));
            }
        });
    }
}
