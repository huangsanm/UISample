package com.huashengmi.ui.android.ui.view.androidannotation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.huashengmi.ui.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_aasub)
public class AASubActivity extends Activity {

    @ViewById(R.id.aa_sb)
    TextView mAATextView;

    @Extra(value="name") String mParamsValues;

    @AfterViews void buildComponent(){
        mAATextView.setText("Hello World ！ " + mParamsValues);
    }

}
