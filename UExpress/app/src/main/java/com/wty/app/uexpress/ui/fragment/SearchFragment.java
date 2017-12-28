package com.wty.app.uexpress.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseFragment;

/**
 * @author wty
 * 查快递
 */
public class SearchFragment extends BaseFragment {

    public static final String TAG = "SearchFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanState) {
        super.onCreateView(inflater,container,savedInstanState);
        if(rootView == null){
            rootView = inflater.inflate(R.layout.activity_main, container,false);
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isInitView()){
            if(isVisibleToUser){
                refreshView();
            }
        }
    }

    private void refreshView(){

    }

}
