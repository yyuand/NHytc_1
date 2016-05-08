package com.hytc.nhytc.tool;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.hytc.nhytc.RongCloudEvent;
import com.hytc.nhytc.dbDAO.FriendDBDao;
import com.hytc.nhytc.domain.RYTokenUser;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2016/2/21.
 */
public class RYConnectService extends Service implements RongIM.UserInfoProvider {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String token = MyRYTokenManager.getRYToken(this);
        if("".equals(token)){
            getRYToken();
        }else {
            connectRY(token);
        }
        infoProvider();
        return super.onStartCommand(intent, flags, startId);
    }

    private void infoProvider() {
        RongIM.setUserInfoProvider(RYConnectService.this, true);
    }

    private void connectRY(String RYToken) {
        RongIM.connect(RYToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                getRYToken();
            }

            @Override
            public void onSuccess(String s) {
                RongCloudEvent.getInstance().setOtherListener();
                Log.e("RYConnectService", "onSuccess" + s);
            }


            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("RYConnectService", "onError" + String.valueOf(errorCode));
            }
        });
    }


    public void getRYToken() {
        User user = BmobUser.getCurrentUser(this, User.class);
        RequestParams params = new RequestParams();
        params.addBodyParameter("userId", user.getObjectId());
        params.addBodyParameter("name", user.getUsername());
        params.addBodyParameter("portraitUri", user.getHeadSculpture());
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://115.29.51.191:8080/get_token", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject data = new JSONObject(responseInfo.result);
                    if ("SUCCESS".equals(data.getString("status"))) {
                        String rytoken = data.getString("data");
                        MyRYTokenManager.saveToken(RYConnectService.this, rytoken);
                        connectRY(rytoken);
                    }
                    Log.e("123321ww", "onSuccess" + data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RYConnectService.this, "获取RY失败！", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(HttpException e, String s) {
                Log.e("123321ww", "onFailure" + e + s);
                Toast.makeText(RYConnectService.this, "获取RY失败！", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public UserInfo getUserInfo(String s) {
        if(!RYfriendManager.isexistfriend(this, s)){
            datatodb(s);
            return null;
        }else {
            RYTokenUser user = RYfriendManager.getFriendinfo(RYConnectService.this, s);
            Uri uri = Uri.parse(user.getHead());
            return new UserInfo(s,user.getName(),uri);
        }
    }


    public void datatodb(final String userId){
            final FriendDBDao dao = new FriendDBDao(this);
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId",userId);
            query.findObjects(this, new FindListener<User>() {
                @Override
                public void onSuccess(List<User> list) {
                    User user = list.get(0);
                    dao.adddata(user.getObjectId(), user.getUsername(), user.getHeadSculpture());
                }

                @Override
                public void onError(int i, String s) {

                }
            });
    }

}
