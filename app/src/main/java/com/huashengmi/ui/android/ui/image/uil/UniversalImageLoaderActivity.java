package com.huashengmi.ui.android.ui.image.uil;

import android.app.ListActivity;
import android.os.Bundle;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.image.picasso.ImageAdapter;
import com.huashengmi.ui.android.utils.DataUtils;

public class UniversalImageLoaderActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
        setListAdapter(new ImageAdapter(this, DataUtils.getImageList(), "uil"));
    }
}
