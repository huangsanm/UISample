package com.huashengmi.ui.android.ui.view.androidannotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huashengmi.ui.android.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_android_annotation)
public class AndroidAnnotationActivity extends Activity implements View.OnClickListener{

    @ViewById(R.id.aa_et)
    EditText mInputEditText;

    @ViewById(R.id.aa_et_src)
    EditText mSrcInputEditText;

    private Context mContext;

    @AfterViews
    void buildComponent(){
        findViewById(R.id.btn_click_src).setOnClickListener(this);
        mContext = this;
    }

    @Click
    void btnOnClick () {
        String textStr = mInputEditText.getEditableText().toString();
        Toast.makeText(this, textStr + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_click_src:
                String textStr = mSrcInputEditText.getEditableText().toString();
                Intent intent_params = new Intent(mContext, AASubActivity_.class);
                intent_params.putExtra("name", textStr);
                startActivity(intent_params);
                break;
        }
    }
}
