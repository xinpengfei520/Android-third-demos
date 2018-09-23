package com.auguigu.lijingxin.mobileplayer.Base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

;

/**
 * Created by MSI on 2016/9/6.
 * 各个Fragment的基类
 */
public abstract class BaseFragment extends Fragment {

    public Context mContext;

    @Override
    //当其子类创建时调用此方法(传上下文)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    //当其子类初始化视图时调用此方法
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return initView();
    }

    public abstract View initView();//抽象方法,其子类一定要实现


    @Override
    //当其子类初始化数据时调用此方法
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    public void initData() {


    }
}

