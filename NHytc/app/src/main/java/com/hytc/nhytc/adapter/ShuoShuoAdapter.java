package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.activity.ShuoshuoDetailActivity;
import com.hytc.nhytc.dbDAO.ApproveShuoDBDao;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.User;
import com.hytc.nhytc.manager.RYfriendManager;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.MyRYTokenManager;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/1/28.
 */
public class ShuoShuoAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ShuoShuo> items;
    private BitmapUtils bitmapUtils;
    private ApproveShuoDBDao dbDao;
    private User user;

    public ShuoShuoAdapter(Activity activity,List<ShuoShuo> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        this.bitmapUtils = BitmapHelper.getBitmapUtils(activity.getApplicationContext());
        this.dbDao = new ApproveShuoDBDao(activity);
        this.user = BmobUser.getCurrentUser(activity,User.class);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder  = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.shuoshuoitem,null);
            viewholder = new ViewHolder();
            viewholder.head = (CircleImageView) convertView.findViewById(R.id.iv_shuo_head);
            viewholder.username = (TextView) convertView.findViewById(R.id.tv_username);
            viewholder.time = (TextView) convertView.findViewById(R.id.tv_time_shuo);
            viewholder.myGirdView = (MyGirdView) convertView.findViewById(R.id.gl_picture_shuoshuo_item);


            viewholder.tvname = (TextView) convertView.findViewById(R.id.tv_title_shuoshuo_item);
            viewholder.tvcontent = (TextView) convertView.findViewById(R.id.tv_content_shuoshuo_item);
            viewholder.ivfavorite = (ImageView) convertView.findViewById(R.id.iv_shuoshuo1);
            viewholder.tvfavoritecount = (TextView) convertView.findViewById(R.id.tv_shuoshuo1);
            viewholder.ivcomment = (ImageView) convertView.findViewById(R.id.iv_shuoshuo2);
            viewholder.tvcommentcount = (TextView) convertView.findViewById(R.id.tv_shuoshuo2);
            viewholder.ivchat = (ImageView) convertView.findViewById(R.id.iv_shuoshuo3);

            viewholder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_shuoshuoitem);

            viewholder.relativeLayout1 = (RelativeLayout) convertView.findViewById(R.id.Rl_bo_shuo1);
            viewholder.relativeLayout2 = (RelativeLayout) convertView.findViewById(R.id.Rl_bo_shuo2);
            viewholder.relativeLayout3 = (RelativeLayout) convertView.findViewById(R.id.Rl_bo_shuo3);
            convertView.setTag(viewholder);
        }else {
            viewholder = (ViewHolder) convertView.getTag();
        }


        bitmapUtils.display(viewholder.head, items.get(position).getAuthor().getHeadSculpture());
        viewholder.username.setText(items.get(position).getAuthor().getUsername());
        viewholder.time.setText(ShowTimeTools.getShowTime(items.get(position).getCreatedAt()));
        switch (items.get(position).getTopic()){
            case 0:viewholder.tvname.setText("");break;
            case 1:viewholder.tvname.setText("表情帝");break;
            case 2:viewholder.tvname.setText("考研考证");break;
            case 3:viewholder.tvname.setText("约约约");break;
            case 4:viewholder.tvname.setText("敢不敢再嗅点");break;
            case 5:viewholder.tvname.setText("逗比搞笑");break;
            case 6:viewholder.tvname.setText("我要吐槽");break;
        }
        ArrayList<String> pics = new ArrayList<>();
        if(items.get(position).getPictures() != null) {
            for (Object s : items.get(position).getPictures()) {
                pics.add(String.valueOf(s));
            }
            PicsGirdAdapter adapter = new PicsGirdAdapter(activity,pics,9);
            viewholder.myGirdView.setAdapter(adapter);
        }else {
            PicsGirdAdapter adapter = new PicsGirdAdapter(activity,pics,0);
            viewholder.myGirdView.setAdapter(adapter);
        }
        viewholder.tvcontent.setText(items.get(position).getContent());
        viewholder.tvfavoritecount.setText(items.get(position).getApproveCount() + "");
        viewholder.tvcommentcount.setText(items.get(position).getCommentCount() + "");

        /**
         * 此处关系于点赞的逻辑
         *
         * 本app点赞的逻辑是把本人所有赞的说说都存到本地数据库中
         * 没获取一条说说，就根据该条说说的id看看本人有没有赞过该
         * 条说说，赞过就setIsApprove(true);
         */
        if(dbDao.isExist(items.get(position).getObjectId())){
            items.get(position).setIsApprove(true);
        }

        viewholder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("person_data",items.get(position).getAuthor());
                Intent intent = new Intent();
                intent.setClass(activity, PersonDetailDataActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        /**
         * 处理赞的问题
         */
        if(items.get(position).getIsApprove()){
            viewholder.ivfavorite.setImageResource(R.mipmap.iconfont5);
        }else {
            viewholder.ivfavorite.setImageResource(R.mipmap.iconfont1);
        }


        /**
         * 点赞或取消点赞后的处理
         */
        final ImageView finalimageView = viewholder.ivfavorite;
        final TextView finaltvfavoritecount = viewholder.tvfavoritecount;
        final ViewHolder finalViewholder = viewholder;
        viewholder.relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewholder.relativeLayout1.setClickable(false);
                if(items.get(position).getIsApprove()){
                    approveshuo(false,items.get(position).getObjectId(),position,finalimageView,finaltvfavoritecount,finalViewholder.relativeLayout1);
                }else {
                    approveshuo(true,items.get(position).getObjectId(),position,finalimageView,finaltvfavoritecount,finalViewholder.relativeLayout1);
                }
            }
        });

        viewholder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("shuoinfo", items.get(position));
                Intent intent = new Intent();
                intent.setClass(activity, ShuoshuoDetailActivity.class);
                intent.putExtras(data);
                intent.putExtra("isshowdialog", false);
                intent.putExtra("position", position);
                activity.startActivityForResult(intent, 1);
                //activity.startActivity(intent);
            }
        });


        viewholder.relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("shuoinfo", items.get(position));
                Intent intent1 = new Intent();
                intent1.setClass(activity, ShuoshuoDetailActivity.class);
                intent1.putExtras(data);
                intent1.putExtra("isshowdialog", true);
                intent1.putExtra("position", position);
                activity.startActivityForResult(intent1, 1);
                //activity.startActivity(intent1);
            }
        });


        final ViewHolder finalViewholder1 = viewholder;
        viewholder.relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewholder1.relativeLayout3.setClickable(false);
                boolean ismyself = user.getObjectId().equals(items.get(position).getAuthor().getObjectId());
                boolean isexist = RYfriendManager.isexistfriend(activity, items.get(position).getAuthor().getObjectId());
                if(!ismyself && !isexist){
                    RYfriendManager.putdata(activity, items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername(), items.get(position).getAuthor().getHeadSculpture());
                }
                if(isexist){
                    RYfriendManager.update(activity,items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername(), items.get(position).getAuthor().getHeadSculpture());
                }
                if (ismyself) {
                    Toast.makeText(activity, "亲，这里不能跟自己聊天哦~", Toast.LENGTH_SHORT).show();
                    finalViewholder1.relativeLayout3.setClickable(true);
                }
                else if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(activity, items.get(position).getAuthor().getObjectId(), items.get(position).getAuthor().getUsername());
                    finalViewholder1.relativeLayout3.setClickable(true);
                }
            }
        });

        return convertView;
    }

    /**
     * 处理点赞的方法
     *
     * @param isapprove 本人是否评论过该条说说
     * @param shuoid 点赞或取消点赞的那条说说的id
     * @param position 点赞或取消点赞的那条说说的位置
     * @param imageView 点赞或取消点赞时需要改变的那个图片（大拇指）
     * @param approvecount 该条说说的点赞数目
     */
    private void approveshuo(final Boolean isapprove,final String shuoid, final int position,final ImageView imageView,final TextView approvecount, final RelativeLayout relativeLayout) {
        ShuoShuo shuoShuo = new ShuoShuo();
        shuoShuo.setObjectId(shuoid);
        if(isapprove) {
            shuoShuo.increment("approveCount");
        }else {
            shuoShuo.increment("approveCount",-1);
        }
        shuoShuo.update(activity, new UpdateListener() {
            @Override
            public void onSuccess() {
                if (isapprove){
                    dbDao.adddata(shuoid);
                    items.get(position).setIsApprove(true);
                    items.get(position).setApproveCount(items.get(position).getApproveCount() + 1);
                    approvecount.setText(items.get(position).getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont5);
                }else {
                    dbDao.deletedata(shuoid);
                    items.get(position).setIsApprove(false);
                    items.get(position).setApproveCount(items.get(position).getApproveCount() - 1);
                    approvecount.setText(items.get(position).getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont1);
                }
                relativeLayout.setClickable(true);
            }

            @Override
            public void onFailure(int i, String s) {
                relativeLayout.setClickable(true);
                Toast.makeText(activity, "请检查网络连接！", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private class ViewHolder
    {
        private CircleImageView head;
        private TextView username;
        private TextView time;
        private TextView tvname;
        private TextView tvcontent;
        private ImageView ivfavorite;
        private TextView tvfavoritecount;
        private ImageView ivcomment;
        private TextView tvcommentcount;
        private ImageView ivchat;

        private LinearLayout linearLayout;

        private RelativeLayout relativeLayout1;
        private RelativeLayout relativeLayout2;
        private RelativeLayout relativeLayout3;
        private MyGirdView myGirdView;
    }


    /**
     * 添加数据
     *
     * @param items
     */
    public void additems(List<ShuoShuo> items) {
        this.items.addAll(items);
        notifyDataSetChanged();

    }

    /**
     * 刷新数据
     *
     * @param items
     */
    public void refreshitems(List<ShuoShuo> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void replaceitem(Integer reposition,ShuoShuo reshuoShuo){
        this.items.remove(reposition);
        this.items.add(reposition, reshuoShuo);
        notifyDataSetChanged();
    }


    public void upData(ForResulltMsg msg){
        this.items.get(msg.getPosition()).setCommentCount(Integer.valueOf(msg.getCommentcount()));
        this.items.get(msg.getPosition()).setApproveCount(Integer.valueOf(msg.getApprovecount()));
        if(msg.getStatus()){
            this.items.get(msg.getPosition()).setIsApprove(!this.items.get(msg.getPosition()).getIsApprove());
        }
        notifyDataSetChanged();
    }


}
