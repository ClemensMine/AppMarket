package com.example.appmarket.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.appmarket.R;
import com.example.appmarket.entity.AppInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends BaseAdapter {
    private List<AppInfoEntity> apps;
    private Context context;
    private LayoutInflater inflater;

    public AppListAdapter(List<AppInfoEntity> apps, Context context) {
        this.apps = apps;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.recommend_app_listview_layout, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title_text);
        TextView desc = convertView.findViewById(R.id.desc_text);
        TextView downloadNum = convertView.findViewById(R.id.download_num_text);
        Button download = convertView.findViewById(R.id.download_btn);
//        ImageView imageView = convertView.findViewById(R.id.logoPic);

        AppInfoEntity info = apps.get(position);

        title.setText(info.getName());
        desc.setText(info.getDesc());
        downloadNum.setText("下载次数：" + info.getDownNum());
        download.setOnClickListener(v -> onDownloadBtnClick(context, info));

//        Glide.with(convertView)
//                .load(info.getLogoUrl())
//                .into(imageView);

        return convertView;
    }

    private void onDownloadBtnClick(Context context,AppInfoEntity entity){
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(MainActivity.URL + entity.getDownloadLink()));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.9.28.10155_537147618_64.apk"));

        request.setTitle("正在下载：" + entity.getName());
        request.setDescription("请等待");

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,entity.getName());
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        DownloadManager systemService = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (systemService != null){
            systemService.enqueue(request);
        }
    }
}
