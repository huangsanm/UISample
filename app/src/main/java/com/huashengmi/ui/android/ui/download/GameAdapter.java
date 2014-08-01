package com.huashengmi.ui.android.ui.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.download.common.DownloadHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by huangsm on 2014/8/1 0001.
 * Email:huangsanm@foxmail.com
 */
public class GameAdapter extends BaseAdapter {

    private Context mContext;
    private List<Game> mGameList;
    private LayoutInflater mInflater;
    public GameAdapter(Context context, List<Game> list){
        mGameList = list;
        mContext = context;
        mInflater= LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mGameList.size();
    }

    @Override
    public Game getItem(int position) {
        return mGameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.adapter_multi_download, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.game_icon);
        /*view.findViewById(R.id.btn_start).setOnClickListener(this);
        view.findViewById(R.id.btn_pause).setOnClickListener(this);
        view.findViewById(R.id.btn_resume).setOnClickListener(this);
        view.findViewById(R.id.btn_delete).setOnClickListener(this);

        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.download_progress);*/

        final Game game = mGameList.get(position);
        Picasso.with(mContext).load(game.getIconUri()).placeholder(R.drawable.logo).resize(120, 120).into(imageView);
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadHelper.getInstance(mContext).startDownload(game);
            }
        });
        return view;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                startDownload();
                break;
            case R.id.btn_pause:
                DownloadHelper.getInstance(mContext).pauseDownload(1);
                break;
            case R.id.btn_resume:
                DownloadHelper.getInstance(mContext).resumeDownload(1);
                break;
            case R.id.btn_delete:
                DownloadHelper.getInstance(mContext).deleteDownload(1);
                break;
        }
    }*/

}
