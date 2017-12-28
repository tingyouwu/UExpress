package com.wty.app.uexpress.widget.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wty.app.uexpress.R;
import com.wty.app.uexpress.widget.xrecyclerview.progressindicator.AVLoadingIndicatorView;

public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {

	private LinearLayout mContainer;
    private LinearLayout last_refresh_layout;
	private ImageView mArrowImageView;
	private SimpleViewSwitcher mProgressBar;
	private TextView mStatusTextView;
	private int mState = STATE_NORMAL;

	private TextView mHeaderTimeView;
    private TextView mHeaderTimeViewLabel;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
    private String status_normal;
    private String status_release_to_refresh;
    private String status_refreshing;
    private String status_done;
	private static final int ROTATE_ANIM_DURATION = 250;

	public int mMeasuredHeight;

	public ArrowRefreshHeader(Context context) {
		super(context);
		initView(context);
        status_normal = context.getResources().getString(R.string.listview_header_hint_normal);
        status_release_to_refresh = context.getResources().getString(R.string.listview_header_hint_release);
        status_refreshing = context.getResources().getString(R.string.refreshing);
        status_done = context.getResources().getString(R.string.refresh_done);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ArrowRefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
        status_normal = context.getResources().getString(R.string.listview_header_hint_normal);
        status_release_to_refresh = context.getResources().getString(R.string.listview_header_hint_release);
        status_refreshing = context.getResources().getString(R.string.refreshing);
        status_done = context.getResources().getString(R.string.refresh_done);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.listview_header, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
		this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

		addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
		mStatusTextView = (TextView)findViewById(R.id.refresh_status_textview);
        last_refresh_layout = (LinearLayout) findViewById(R.id.last_refresh_layout);

        //init the progress view
		mProgressBar = (SimpleViewSwitcher)findViewById(R.id.listview_header_progressbar);
        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(getContext().getApplicationContext());
        mProgressBar.setView(progressView);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);

		mHeaderTimeView = (TextView)findViewById(R.id.last_refresh_time);
        mHeaderTimeViewLabel = (TextView)findViewById(R.id.last_refresh_label);
		measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		mMeasuredHeight = getMeasuredHeight();
	}

    public void setStatusNorml(String normal){
        status_normal = normal;
        mStatusTextView.setText(status_normal);
    }

    public void setStatusRelease(String release){
        status_release_to_refresh = release;
    }

    public void setStatusRefreshing(String refreshing){
        status_refreshing = refreshing;
    }

    public void setDimissLastRefreshLayout(boolean dismiss){
        if(dismiss){
            last_refresh_layout.setVisibility(View.GONE);
        }else{
            last_refresh_layout.setVisibility(View.VISIBLE);
        }
    }

    public void setStatusDone(String done){
        status_done = done;
    }

    public void setProgressStyle(int style) {
        if(style == ProgressStyle.SysProgress){
            mProgressBar.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        }else{
            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
            mProgressBar.setView(progressView);
        }
    }

    public void setArrowImageView(int resid){
        mArrowImageView.setImageResource(resid);
    }

	public void setState(int state) {
        if (state == mState) {
            return ;
        }

		if (state == STATE_REFRESHING) {	// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
            smoothScrollTo(mMeasuredHeight);
		} else if(state == STATE_DONE) {
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {	// 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mStatusTextView.setText(status_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mStatusTextView.setText(status_release_to_refresh);
                }
                break;
            case STATE_REFRESHING:
                mStatusTextView.setText(status_refreshing);
                break;
            case STATE_DONE:
                mStatusTextView.setText(status_done);
                break;
            default:
		}
		
		mState = state;
	}

    public int getState() {
        return mState;
    }

    @Override
	public void refreshComplete(String date){
        if (date.isEmpty()) {
            date = "";
        }
        mHeaderTimeView.setText(date);
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                reset();
            }
        }, 200);
	}

	public void setVisibleHeight(int height) {
		if (height < 0) {
            height = 0;
        }
		LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
		return lp.height;
	}

    @Override
    public void onMove(float delta) {
        if(getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            // 未处于刷新状态，更新箭头
            if (mState <= STATE_RELEASE_TO_REFRESH) {
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                }else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) {
            // not visible.
            isOnRefresh = false;
        }

        if(getVisibleHeight() > mMeasuredHeight &&  mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <=  mMeasuredHeight) {
            //return;
        }
        if (mState != STATE_REFRESHING) {
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESHING) {
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 200);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(200).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}