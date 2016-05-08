package com.hytc.nhytc.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.DeleteFileListener;
import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.PersonDetailDataActivity;
import com.hytc.nhytc.activity.ShuoshuoDetailActivity;
import com.hytc.nhytc.dbDAO.ApproveShuoDBDao;
import com.hytc.nhytc.domain.ForResulltMsg;
import com.hytc.nhytc.domain.ShuoShuo;
import com.hytc.nhytc.domain.ShuoShuoComment;
import com.hytc.nhytc.tool.BitmapHelper;
import com.hytc.nhytc.tool.ShowTimeTools;
import com.hytc.nhytc.view.CircleImageView;
import com.hytc.nhytc.view.MyGirdView;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/1/30.
 */
public class ShuoShuoMyAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ShuoShuo> items;
    private BitmapUtils bitmapUtils;
    private ApproveShuoDBDao dbDao;

    public ShuoShuoMyAdapter(Activity activity,List<ShuoShuo> items) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
        this.items = items;
        this.bitmapUtils = BitmapHelper.getBitmapUtils(activity.getApplicationContext());
        this.dbDao = new ApproveShuoDBDao(activity);
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
            convertView = inflater.inflate(R.layout.shuoshuomyitem,null);
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

        viewholder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("person_data", items.get(position).getAuthor());
                Intent intent = new Intent();
                intent.setClass(activity, PersonDetailDataActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });




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
                activity.startActivityForResult(intent, 2);
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
                intent1.putExtra("isshowdialog",true);
                intent1.putExtra("position", position);
                activity.startActivityForResult(intent1,2);
                //activity.startActivity(intent1);
            }
        });


        viewholder.relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 此处写删除我的说说的功能
                 */
                showDeleteDialog(items.get(position).getObjectId(), items.get(position).getPicturesNames(), position);
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
                if (isapprove) {
                    dbDao.adddata(shuoid);
                    items.get(position).setIsApprove(true);
                    items.get(position).setApproveCount(items.get(position).getApproveCount() + 1);
                    approvecount.setText(items.get(position).getApproveCount() + "");
                    imageView.setImageResource(R.mipmap.iconfont5);
                } else {
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
        this.items.addAll(items) ;
        notifyDataSetChanged();
    }

    public void deleteitem(int dposition){
        this.items.remove(dposition);
        notifyDataSetChanged();
    }


    public void deleteData(final String ObJectid, final List<String> picnames,final int nowposition){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("淮说删除中...");
        progressDialog.show();
        ShuoShuo shuoShuo = new ShuoShuo();
        shuoShuo.setObjectId(ObJectid);
        shuoShuo.delete(activity, new DeleteListener() {
            @Override
            public void onSuccess() {
                deleteitem(nowposition);
                progressDialog.dismiss();
                Toast.makeText(activity, "淮说删除成功！", Toast.LENGTH_SHORT).show();
                findCommentid(ObJectid);
                deletepictures(picnames);
            }

            @Override
            public void onFailure(int i, String s) {
                progressDialog.dismiss();
                Toast.makeText(activity, "淮说删除失败\n请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void findCommentid(String ObJectid){
        BmobQuery<ShuoShuoComment> query = new BmobQuery<>();
        query.addWhereEqualTo("shuoshuoid", ObJectid);
        query.findObjects(activity, new FindListener<ShuoShuoComment>() {
            @Override
            public void onSuccess(List<ShuoShuoComment> list) {
                deleteCommentData(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public void deleteCommentData(List<ShuoShuoComment> list){
        List<BmobObject> comments = new ArrayList<>();
        comments.addAll(list);
        new BmobObject().deleteBatch(activity, comments, new DeleteListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }

    public void deletepictures(List<String> picnames){
        if(picnames != null) {
            for (String picname : picnames) {
                BmobProFile.getInstance(activity).deleteFile(picname, new DeleteFileListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }
        }
    }

    public void showDeleteDialog(final String ObJectid, final List<String> picnames,final int nowposition){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("删除");
        builder.setMessage("亲，您确定要删除这条说说么？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(ObJectid,picnames,nowposition);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.create();
        builder.show();
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
