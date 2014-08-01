package com.huashengmi.ui.android.ui.download;

import com.huashengmi.ui.android.ui.download.Game;

import java.util.List;

/**
 * Created by huangsm on 2014/8/1 0001.
 * Email:huangsanm@foxmail.com
 */
public class DataUtils {

    public static void initData(List<Game> list){
        String name = "放开三国";
        String uri = "http://downum.game.uc.cn/download/wm/527432-2-com.babeltime.fknsango_uc-100004748";
        String iconUri = "http://img.shouyouzhijia.net/c16/16816/ThumbIcon/16816.jpg";
        String pkg = "com.babeltime.fknsango_uc";

        Game game = new Game();
        game.setName(name);
        game.setUri(uri);
        game.setIconUri(iconUri);
        game.setPkg(pkg);
        game.setFileSize("55.5MB");
        list.add(game);

        Game game1 = new Game();
        game1.setName("地铁跑酷");
        game1.setUri("http://cpdown.shouyouzhijia.net/shouyouzhijia/7596h0728.apk");
        game1.setIconUri("http://img.shouyouzhijia.net/c7/7596/ThumbIcon/7596.jpg");
        game1.setPkg("com.kiloo.subwaysurf.full");
        game1.setFileSize("31.9MB");
        list.add(game1);

        Game game2 = new Game();
        game2.setName("我叫MT 标准版");
        game2.setUri("http://downum.game.uc.cn/download/wm/516465-2-com.locojoy.immt_a_chs.uc.standard-100004748");
        game2.setIconUri("http://img.shouyouzhijia.net/c17/17088/ThumbIcon/17088.jpg");
        game2.setPkg("com.locojoy.immt_a_chs.uc.standard");
        game2.setFileSize("75.18MB");
        list.add(game2);

        Game game3 = new Game();
        game3.setName("疯狂消星星");
        game3.setUri("http://cpdown.shouyouzhijia.net/shouyouzhijia/13273.apk");
        game3.setIconUri("http://img.shouyouzhijia.net/c13/13273/ThumbIcon/13273.jpg");
        game3.setPkg("net.lazyer.popstar.sky");
        game3.setFileSize("2.8MB");
        list.add(game3);

        Game game4 = new Game();
        game4.setName("捕鱼达人土豪金");
        game4.setUri("http://cpdown.shouyouzhijia.net/shouyouzhijia/17263S0417.apk");
        game4.setIconUri("http://img.shouyouzhijia.net/c17/17263/ThumbIcon/17263.jpg");
        game4.setPkg("org.cocos2dx.GoldenFishGame");
        game4.setFileSize("8.21MB");
        list.add(game4);

    }

}
