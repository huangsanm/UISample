package com.huashengmi.ui.android.ui.image.picasso;

import android.app.ListActivity;
import android.os.Bundle;

import com.huashengmi.ui.android.R;

import java.util.ArrayList;
import java.util.List;

public class PicassoActivity extends ListActivity {

    private List<String> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
        mImageList = new ArrayList<String>();
        mImageList.add("http://imgt9.bdstatic.com/it/u=2,771258473&fm=25&gp=0.jpg");
        mImageList.add("http://s1.dwstatic.com/group1/M00/E8/10/b76f9d3bb275857d8f62f67b1ca7aaf3.gif");
        mImageList.add("http://a.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=5af5baa96f224f4a5799771a3bc7ab31/962bd40735fae6cd938767ee0db30f2442a70f0a.jpg");
        mImageList.add("http://e.hiphotos.baidu.com/image/h%3D1080%3Bcrop%3D0%2C0%2C1920%2C1080/sign=e035a7735cdf8db1a32e78643113e637/d8f9d72a6059252d687fbbef369b033b5ab5b9f5.jpg");
        mImageList.add("http://h.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=9a520fd93b01213fcf334ad566d70db2/0bd162d9f2d3572c069da7dc8813632762d0c3b6.jpg");
        mImageList.add("http://b.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=c8c6268e4dc2d562f208d4e4d521ab8c/f603918fa0ec08fa20c39b495bee3d6d54fbda19.jpg");
        mImageList.add("http://d.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=433d7cce0ef3d7ca0cf63b7fc02f856a/43a7d933c895d1437654dfd371f082025aaf071b.jpg");
        mImageList.add("http://h.hiphotos.baidu.com/image/h%3D1080%3Bcrop%3D0%2C0%2C1920%2C1080/sign=c3b36c51f21f3a2945c8d1cea1158752/6a63f6246b600c33c8a31347184c510fd8f9a163.jpg");
        mImageList.add("http://a.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=d92b13755066d0167e199a21a51bef64/9a504fc2d5628535052d429992ef76c6a6ef63bd.jpg");
        mImageList.add("http://d.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=2c9695c6271f95caa6f596bffb27445d/342ac65c10385343406e17999113b07ecb8088e1.jpg");
        mImageList.add("http://e.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=cf529fd7cdfc1e17fdbf883878a0cd60/d058ccbf6c81800a9523a1cfb33533fa838b4728.jpg");
        mImageList.add("http://e.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=e94603fbd3c8a786be2a4e075539f25e/11385343fbf2b2115f4a264fc88065380cd78e4f.jpg");
        mImageList.add("http://a.hiphotos.baidu.com/image/w%3D1920%3Bcrop%3D0%2C0%2C1920%2C1080/sign=a5dd47f4950a304e5222a4f3e3f89ce5/3b87e950352ac65c48fa5583f9f2b21192138a46.jpg");
        mImageList.add("http://e.hiphotos.baidu.com/image/w%3D230/sign=d325bbf07d3e6709be0042fc0bc69fb8/9f510fb30f2442a7ad90ba72d343ad4bd013024e.jpg");
        mImageList.add("http://g.hiphotos.baidu.com/image/w%3D230/sign=74621b91e9c4b7453494b015fffd1e78/63d9f2d3572c11dfd7db229b612762d0f703c204.jpg");
        mImageList.add("http://d.hiphotos.baidu.com/image/w%3D230/sign=bc16c7fd72cf3bc7e800caefe101babd/43a7d933c895d1437654dfd371f082025aaf071b.jpg");

        setListAdapter(new ImageAdapter(this, mImageList));
    }
}
