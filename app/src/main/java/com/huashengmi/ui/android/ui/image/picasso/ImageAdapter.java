package com.huashengmi.ui.android.ui.image.picasso;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.utils.Globals;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by huangsm on 2014/9/2 0002.
 * Email:huangsanm@foxmail.com
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    public ImageAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = new ImageView(mContext);
        view.setPadding(0, 10, 0, 10);
        Picasso.with(mContext).load(mList.get(position)).resize(Globals.px2dip(mContext, 450), Globals.px2dip(mContext, 450)).placeholder(R.drawable.logo).into(view);
        return view;
    }

}
