package com.wty.app.uexpress.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author wty
 * Fragment 基类
 **/
public abstract class BaseFragment extends Fragment {

	protected BaseActivity activity;
	protected View rootView;
	private boolean isInitView = false;
	private boolean isInitData = false;
	private Unbinder unbinder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (activity == null) {
			activity =  (BaseActivity) getActivity();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d("性能调试", this.getClass().getSimpleName() + " onCreateView");
		if (rootView == null) {
			rootView = inflater.inflate(getLayoutResource(), container, false);
			unbinder = ButterKnife.bind(this, rootView);
		}
		this.isInitView = true;
		return rootView;
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d("性能调试", this.getClass().getSimpleName() + " onActivityCreated");
		this.isInitView = true;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser && isInitView){
			//setUserVisibleHint在onCreateView之前调用的，
			//在视图未初始化的时候就使用的话，会有空指针异常
			onInitData();
			doWorkOnResume();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(getUserVisibleHint()){
			onInitData();
			doWorkOnResume();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("性能调试", this.getClass().getSimpleName() + " onDestroyView");
		if(rootView != null){
			((ViewGroup)(rootView.getParent())).removeView(rootView);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
	}

	/**
	 * 功能描述：初始化view以及数据
	 **/
	private void onInitData(){
		if(!isInitData){
			isInitData = true;
			onInitView();
		}
	}

	public void setActivity(BaseActivity activity){
		this.activity = activity;
	}

	public void handleActionBar(){}

	/**
	 * 当fragment处于可见并且已经初始之后，做一些操作
	 **/
	abstract public void doWorkOnResume();
	/**
	 * 获取布局文件
	 **/
	abstract protected int getLayoutResource();
	/**
	 * View初始化工作,包括listview的adapter绑定，view显示效果
	 **/
	abstract protected void onInitView();

	/***
	 * 用于切换tab时修改一些值
	 */
	public void handleOnShow(){

	}
}
