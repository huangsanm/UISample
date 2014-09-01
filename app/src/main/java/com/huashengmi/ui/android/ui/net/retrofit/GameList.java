package com.huashengmi.ui.android.ui.net.retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsm on 2014/9/1 0001.
 * Email:huangsanm@foxmail.com
 */
public class GameList {

    private List<Game> List = new ArrayList<Game>();
    private int TotalCount;

    public List<Game> getList() {
        return List;
    }

    public void setList(List<Game> list) {
        List = list;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }
}
