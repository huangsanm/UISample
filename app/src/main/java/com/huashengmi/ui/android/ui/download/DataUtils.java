package com.huashengmi.ui.android.ui.download;

import java.util.List;

/**
 * Created by huangsm on 2014/8/1 0001.
 * Email:huangsanm@foxmail.com
 */
public class DataUtils {

    public static void initData(List<Game> list){
        String name = "猿题库";
        String uri = "http://gdown.baidu.com/data/wisegame/141d640d1e6187ea/yuantiku_3000099.apk";
        String iconUri = "http://e.hiphotos.bdimg.com/wisegame/pic/item/cbd3fd1f4134970a9ccb810096cad1c8a7865d61.jpg";
        String pkg = "com.babeltime.fknsango_uc";

        Game game = new Game();
        game.setName(name);
        game.setUri(uri);
        game.setIconUri(iconUri);
        game.setPkg(pkg);
        game.setFileSize("55.5MB");
        list.add(game);

        Game game1 = new Game();
        game1.setName("柚柚育儿怀孕母婴");
        game1.setUri("http://gdown.baidu.com/data/wisegame/95bcd288e6100e10/youyouyuerhuaiyunmuying_43.apk");
        game1.setIconUri("http://b.hiphotos.bdimg.com/wisegame/pic/item/e982b9014a90f603ba9d7e613a12b31bb051edec.jpg");
        game1.setPkg("com.kiloo.subwaysurf.full");
        game1.setFileSize("31.9MB");
        list.add(game1);

        Game game2 = new Game();
        game2.setName("段子手");
        game2.setUri("http://gdown.baidu.com/data/wisegame/228e4becc5cdad82/duanzishou_132.apk");
        game2.setIconUri("http://f.hiphotos.bdimg.com/wisegame/pic/item/8a50352ac65c1038d61c8cfcb1119313b07e89aa.jpg");
        game2.setPkg("com.locojoy.immt_a_chs.uc.standard");
        game2.setFileSize("75.18MB");
        list.add(game2);
    }

}
