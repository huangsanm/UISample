package com.huashengmi.ui.android.ui.image.volley;

import android.app.ListActivity;
import android.os.Bundle;

import com.huashengmi.ui.android.R;
import com.huashengmi.ui.android.ui.image.picasso.ImageAdapter;
import com.huashengmi.ui.android.utils.DataUtils;

public class VolleyImageActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
        setListAdapter(new ImageAdapter(this, DataUtils.getImageList(), "volley"));
    }
}
