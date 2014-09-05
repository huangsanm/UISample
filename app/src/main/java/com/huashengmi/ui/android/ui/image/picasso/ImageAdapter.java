package com.huashengmi.ui.android.ui.image.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.net.volley.VolleyManager;
import com.huashengmi.ui.android.utils.Globals;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by huangsm on 2014/9/2 0002.
 * Email:huangsanm@foxmail.com
 */
public class ImageAdapter extends BaseAdapter {


    private Context mContext;
    private List<String> mList;
    private String mFlag;
    private int mWidth;
    private int mHeight;
    private DisplayImageOptions mOptions;
    public ImageAdapter(Context context, List<String> list, String flag){
        mContext = context;
        mList = list;
        mFlag = flag;
        mWidth = Globals.px2dip(mContext, 450);
        mHeight = Globals.px2dip(mContext, 450);

        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo)
                .showImageOnFail(R.drawable.logo)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .build();
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
        if("picasso".equals(mFlag)){
            ImageView view = new ImageView(mContext);
            view.setPadding(0, 10, 0, 10);
            Picasso.with(mContext).load(mList.get(position)).resize(mWidth, mHeight).placeholder(R.drawable.logo).into(view);
            return view;
        }else if ("volley".equals(mFlag)){
            NetworkImageView view = new NetworkImageView(mContext);
            view.setPadding(0, 10, 0, 10);
            view.setDefaultImageResId(R.drawable.logo);
            view.setLayoutParams(new AbsListView.LayoutParams(mWidth, mHeight));
            view.setImageUrl(mList.get(position), VolleyManager.getInstance(mContext).getImageLoader());
            return view;
        }else if ("uil".equals(mFlag)){
            final ImageView image = new ImageView(mContext);
            image.setPadding(0, 10, 0, 10);
            ImageLoader.getInstance().loadImage(mList.get(position), new ImageSize(mWidth, mHeight), mOptions, new SimpleImageLoadingListener(){
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    image.setImageBitmap(loadedImage);
                }
            });
            return image;
        }else if("helper".equals(mFlag)){
            ImageView image = new ImageView(mContext);
            image.setPadding(0, 10, 0, 10);
            UrlImageViewHelper.setUrlDrawable(image, mList.get(position), R.drawable.logo);
            return image;
        }
        return convertView;
    }

}
