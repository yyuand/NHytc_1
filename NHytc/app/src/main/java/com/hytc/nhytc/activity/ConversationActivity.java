package com.hytc.nhytc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.hytc.nhytc.domain.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/2/21.
 */
public class ConversationActivity extends FragmentActivity{
    private String mTargetId;
    private String title;
    private TextView name;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        name = (TextView) this.findViewById(R.id.name);
        back = (ImageView) this.findViewById(R.id.iv_back_titlebar);

        mTargetId = getIntent().getData().getQueryParameter("targetId");
        title = getIntent().getData().getQueryParameter("title");
        if(!TextUtils.isEmpty(title)){
            name.setText(title);
        }else {
            getname(mTargetId);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getname(String mTargetId) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId",mTargetId);
        query.findObjects(this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                name.setText(list.get(0).getUsername());
            }

            @Override
            public void onError(int i, String s) {
                name.setText("");
            }
        });
    }
}
