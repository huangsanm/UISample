package com.huashengmi.ui.android.ui.image.imagehelper;

import android.app.ListActivity;
import android.os.Bundle;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.image.picasso.ImageAdapter;
import com.huashengmi.ui.android.utils.DataUtils;

/**
 * Created by huangsm on 2014/9/3 0003.
 * Email:huangsanm@foxmail.com
 */
public class ImageHelperActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);

        setListAdapter(new ImageAdapter(this, DataUtils.getImageList(), "helper"));
    }
}
