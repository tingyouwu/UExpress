package com.wty.app.uexpress.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.wty.app.uexpress.R;

import java.util.List;

/**
 * @Description:右侧的sideBar
 * 点击字母，自动导航到相应拼音的汉字上
 */
public class SideBar extends View {
	private int Max = 27;//最多能放几个字母
	private int start = 0;
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public String[] letters = new String[]{};
	private int choose = -1;// 选中
	private Paint paint = new Paint();
	private TextView mTextDialog;

	public SideBar(Context context) {
		super(context);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		start = (Max - letters.length)/2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / Max;// 获取每一个字母的高度

		for (int i = 0; i < letters.length; i++) {
			paint.setColor(getResources().getColor(R.color.bottom_normal));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(30);
			// 选中的状态
			if ((Max-letters.length)/2+i == choose) {
				paint.setColor(getResources().getColor(R.color.bottom_click));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(letters[i]) / 2;
			float yPos = singleHeight * (start+i) + singleHeight;
			canvas.drawText(letters[i], xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

	}

	
	@SuppressLint("NewApi")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final int c = (int) (y / getHeight() * 27);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundDrawable(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(R.drawable.sidebar_background);
			setAlpha((float) 0.7);
			if (oldChoose != c) {
				if (c >= 0 && c < Max) {
					if(c>=start && c<start+letters.length){
						if (onTouchingLetterChangedListener != null) {
							onTouchingLetterChangedListener.onTouchingLetterChanged(letters[c-start]);
						}
						if (mTextDialog != null) {
							mTextDialog.setText(letters[c-start]);
							mTextDialog.setVisibility(View.VISIBLE);
						}
					}else {
						if (mTextDialog != null) {
							mTextDialog.setVisibility(View.INVISIBLE);
						}
					}
					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public void setLettersList(String[] letters){
		this.letters = letters;
		this.start  = (Max - letters.length)/2;
		invalidate();

	}

	public void setLettersList(List<String> letters){
		setLettersList(letters.toArray(new String[letters.size()]));
	}

	/**
	 * 向外公开的方法
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		void onTouchingLetterChanged(String s);
	}

}