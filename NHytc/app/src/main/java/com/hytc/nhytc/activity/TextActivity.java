package com.hytc.nhytc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hytc.nhytc.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/4/19.
 */
public class TextActivity extends Activity implements View.OnClickListener {
    private Button button;
    private TextView textView;
    private Object codeUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        button = (Button) findViewById(R.id.bt_text);
        textView = (TextView) findViewById(R.id.tv_text);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_text:
                getCodeUrl();
                break;
        }
    }

    public void getCodeUrl() {
        RequestParams params = new RequestParams();
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, "http://115.29.51.191:9001/get_code", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONObject data = new JSONObject(responseInfo.result);
                    if("SUCCESS".equals(data.get("status"))){
                        StringBuilder builder = new StringBuilder();
                        builder.append(data.get("code_name")).append("\n").append(data.getString("cookie"));
                        textView.setText(builder);
                    }else {
                        textView.setText("status == failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.setText("failed==>" + e.toString());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
