package com.huashengmi.ui.android.ui;


import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.huashengmi.ui.android.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends ListFragment {

    public final static String PARAM_ACTION = "PARAMS_ACTION";

    private String mAction;
    private MainActivity mActivity;
    private PackageManager mPackageManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAction = getArguments().getString(PARAM_ACTION);
        mActivity = (MainActivity) getActivity();
        mPackageManager = mActivity.getPackageManager();

        SimpleAdapter adapter = new SimpleAdapter(mActivity, getListData(),
                android.R.layout.simple_list_item_1, new String[]{"title"},
                new int[]{android.R.id.text1});
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }

    private List<Map<String, Object>> getListData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(mAction);
        List<ResolveInfo> list = mPackageManager.queryIntentActivities(mainIntent, 0);
        final int N = list.size();
        for (int i = 0; i < N; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(mPackageManager);
            String label = TextUtils.isEmpty(labelSeq) ? info.activityInfo.name
                    : labelSeq.toString();
            addItem(data, activityIntent(info.activityInfo.applicationInfo.packageName, info.activityInfo.name), label);
        }
        return data;
    }

    public Intent activityIntent(String pkg, String componentName) {
        Intent intent = new Intent();
        intent.setClassName(pkg, componentName);
        return intent;
    }

    public void addItem(List<Map<String, Object>> data, Intent intent,
                        String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", name);
        map.put("intent", intent);
        data.add(map);
    }


}
