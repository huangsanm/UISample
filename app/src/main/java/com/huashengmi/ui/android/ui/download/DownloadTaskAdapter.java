package com.huashengmi.ui.android.ui.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.app.UiSampleApp;
import com.huashengmi.ui.android.ui.download.common.DownloadHelper;
import com.huashengmi.ui.android.ui.download.common.DownloadItem;
import com.huashengmi.ui.android.ui.download.common.DownloadStatus;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by huangsm on 2014/8/4 0004.
 * Email:huangsanm@foxmail.com
 */
public class DownloadTaskAdapter extends BaseAdapter {

    private Context mContext;
    private List<DownloadItem> mItems;
    private LayoutInflater mInflater;

    public DownloadTaskAdapter(Context context, List<DownloadItem> list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = list;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public DownloadItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DownloadHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_download_task, null);
            holder = new DownloadHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.download_icon);
            holder.mNameTextView = (TextView) convertView.findViewById(R.id.download_name);
            holder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.download_progress);
            holder.mPauseButton = (Button) convertView.findViewById(R.id.download_pause);
            holder.mDeleteButton = (Button) convertView.findViewById(R.id.download_delete);
            convertView.setTag(holder);
        } else {
            holder = (DownloadHolder) convertView.getTag();
        }

        final DownloadItem d = getItem(position);
        Picasso.with(mContext).load(d.getIconUri()).placeholder(R.drawable.logo).resize(120, 120).into(holder.mImageView);
        holder.mNameTextView.setText(d.getName());
        if (d != null) {
            final DownloadItem item = UiSampleApp.mDownloadItem.get(d.getId());
            holder.mProgressBar.setMax((int) item.getTotalByte());
            holder.mProgressBar.setProgress((int) item.getCurrentByte());
            holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadHelper.getInstance(mContext).deleteDownload(item.getId());
                }
            });
            holder.mPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int downloadID = item.getId();
                    Button b = (Button) v;
                    if (item.getStatus() == DownloadStatus.STATUS_PAUSED) {
                        b.setText("已暂停");
                        DownloadHelper.getInstance(mContext).resumeDownload(downloadID);
                    }

                    if (item.getStatus() == DownloadStatus.STATUS_RUNNING) {
                        DownloadHelper.getInstance(mContext).pauseDownload(downloadID);
                        b.setText("下载中");
                    }
                }
            });
        }
        return convertView;
    }

    class DownloadHolder {

        private ImageView mImageView;
        private TextView mNameTextView;
        private ProgressBar mProgressBar;
        private Button mPauseButton;
        private Button mDeleteButton;

    }
}
