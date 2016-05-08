package com.hytc.nhytc.manager;

import android.app.Activity;
import android.content.Context;

import com.hytc.nhytc.dbDAO.FriendDBDao;
import com.hytc.nhytc.domain.RYTokenUser;
import com.hytc.nhytc.tool.RYConnectService;

/**
 * Created by Administrator on 2016/2/21.
 */
public class RYfriendManager {
    public static boolean isexistfriend(Context context, String userid) {
        FriendDBDao dao = new FriendDBDao(context);
        return dao.isExist(userid);
    }

    public static void update(Context context, String token, String name, String head) {
        FriendDBDao dao = new FriendDBDao(context);
        dao.updatedata(token, name, head);
    }

    /**
     * 增加数据到数据库
     *
     * @param context 上下文
     * @param userid   好友的tokenid
     * @param name     好友的昵称
     * @param head     好友的头像路径
     */
    public static void putdata(Context context, String userid, String name, String head) {
        FriendDBDao dao = new FriendDBDao(context);
        dao.adddata(userid, name, head);
    }

    /**
     * 从数据库获取数据给信息提供者
     *
     * @param context 上下文
     * @param userid   好友的tokenid
     * @return 用户信息的实体类
     */
    public static RYTokenUser getFriendinfo(Context context, String userid) {
        RYTokenUser user;
        FriendDBDao dao = new FriendDBDao(context);
        user = dao.getUserInfo(userid);
        return user;
    }
}
