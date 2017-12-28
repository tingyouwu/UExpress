package com.wty.app.uexpress.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author wty
 * Fragment 基类
 **/
public class BaseFragment extends Fragment {

	protected BaseActivity activity;
	protected View rootView;
	private boolean init = false;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		this.init = true;
		Log.d("性能调试", this.getClass().getSimpleName() + " onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("性能调试", this.getClass().getSimpleName() + " onActivityCreated");
		this.init = true;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("性能调试", this.getClass().getSimpleName() + " onDestroyView");
		if(rootView != null){
			((ViewGroup)(rootView.getParent())).removeView(rootView);
		}
	}

	public void setActivity(BaseActivity activity){
		this.activity = activity;
	}

	public boolean isInitView(){
		return init;
	}

}
