package com.hytc.nhytc;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.activity.PhotoActivity;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.tool.picture.PhotoInputProvider;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;

/**
 *
 *
 import io.rong.app.database.UserInfos;
import io.rong.app.message.AgreedFriendRequestMessage;
import io.rong.app.message.ContactsProvider;
import io.rong.app.message.provider.RealTimeLocationInputProvider;
import io.rong.app.model.User;
import io.rong.app.ui.activity.MainActivity;
import io.rong.app.ui.activity.NewFriendListActivity;
import io.rong.app.ui.activity.PhotoActivity;
import io.rong.app.ui.activity.RealTimeLocationActivity;
import io.rong.app.ui.activity.SOSOLocationActivity;
import io.rong.app.ui.widget.WinToast;

 */
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.AlterDialogFragment;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.TextInputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.location.RealTimeLocationConstant;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.PublicServiceMultiRichContentMessage;
import io.rong.message.PublicServiceRichContentMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;


/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements RongIMClient.ConnectionStatusListener,
        RongIM.ConversationBehaviorListener,RongIMClient.OnReceiveMessageListener, Handler.Callback {

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    private static RongCloudEvent mRongCloudInstance;

    private Context mContext;
    private Handler mHandler;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
        mHandler = new Handler(this);
    }


    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {

        //RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        //RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        //RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        //RongIM.setConversationListBehaviorListener(this);//会话列表界面操作的监听器
        //RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        //RongIM.setGroupUserInfoProvider(this, true);
//        RongIM.setOnReceivePushMessageListener(this);//自定义 push 通知。
        //消息体内是否有 userinfo 这个属性
        //RongIM.getInstance().setMessageAttachedUserInfo(true);
    }

    /**
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {

        RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);//设置连接状态监听器。

        TextInputProvider textInputProvider = new TextInputProvider(RongContext.getInstance());
        RongIM.setPrimaryInputProvider(textInputProvider);

//        扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new PhotoInputProvider(RongContext.getInstance()),//图片
                //new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                //new RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
        };

        InputProvider.ExtendProvider[] provider1 = {
                new PhotoInputProvider(RongContext.getInstance()),//图片
                //new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                //new RealTimeLocationInputProvider(RongContext.getInstance()),//地理位置
                //new ContactsProvider(RongContext.getInstance()),//通讯录
        };

        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider1);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider1);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, provider1);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, provider1);
    }


    private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) RongContext.getInstance().getApplicationInfo().loadIcon(RongContext.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }











    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(final Context context, Conversation.ConversationType conversationType, UserInfo user) {

        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (user != null) {

            if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE) || conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
                RongIM.getInstance().startPublicServiceProfile(mContext, conversationType, user.getUserId());
            } else {

                BmobQuery<User> query = new BmobQuery<>();
                query.addWhereEqualTo("objectId",user.getUserId());
                query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.setMaxCacheAge(TimeUnit.DAYS.toMillis(3));//此表示缓存3天
                query.findObjects(context, new FindListener<User>() {
                    @Override
                    public void onSuccess(List<User> list) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("person_data", list.get(0));
                        Intent intent = new Intent();
                        intent.setClass(context, PersonDetailDataActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(context,"查看个人详情失败\n请检查网络连接！",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        Log.e(TAG, "----onUserPortraitLongClick");

        return true;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(final Context context, final View view, final Message message) {
        if (message.getContent() instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            Intent intent = new Intent(context, PhotoActivity.class);

            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
            if (imageMessage.getThumUri() != null)
                intent.putExtra("thumbnail", imageMessage.getThumUri());
            context.startActivity(intent);
        }


        return false;
    }


    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {

        Log.e(TAG, "----onMessageLongClick");
        return false;
    }



    @Override
    public boolean handleMessage(android.os.Message msg) {
        return false;
    }


    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    @Override
    public boolean onReceived(Message message, int i) {
        return false;
    }

}
