package com.hytc.nhytc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hytc.nhytc.R;
import com.hytc.nhytc.activity.GetCourseActivity;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/21.
 */
public class NoCourseFragment extends Fragment{

    private View view;
    private Button createCourse;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_no_course,container,false);
        createCourse = (Button) view.findViewById(R.id.bt_get_course);
        ButterKnife.bind(this,view);
        createCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetCourseActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
