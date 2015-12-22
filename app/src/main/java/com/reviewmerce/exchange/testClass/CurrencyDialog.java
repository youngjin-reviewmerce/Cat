package com.reviewmerce.exchange.testClass;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.reviewmerce.exchange.ListView.CurrencyAdapter;
import com.reviewmerce.exchange.commonData.CurrencyItem;

import java.util.ArrayList;
import java.util.List;


public class CurrencyDialog extends Dialog{

	private IListDialogSelectListener  mListener;

	private List<CurrencyItem> mItems = new ArrayList<CurrencyItem>();
	private Context context;
	CurrencyAdapter mAdapter;
	public interface IListDialogSelectListener{
		public void onSelectListItem(int position);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);
		
		//setContentView(R.layout.currency_dialog);

		createList();
		setLayout();
		setTitle(mTitle);
		setContent(mContent);
		setClickListener(mLeftClickListener , mRightClickListener);
	}
	
	public CurrencyDialog(Context context,CurrencyAdapter adapter) {
		// Dialog
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mAdapter = adapter;
	}
	
	public CurrencyDialog(Context context , String title ,
			View.OnClickListener singleListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mLeftClickListener = singleListener;
	}
	
	public CurrencyDialog(Context context , String title , String content ,
			View.OnClickListener leftListener ,	View.OnClickListener rightListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.mTitle = title;
		this.mContent = content;
		this.mLeftClickListener = leftListener;
		this.mRightClickListener = rightListener;
	}

	private void createList()
	{
		//LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		ListView dlgView = new ListView(getContext());//inflater.inflate(R.layout.currency_dialog, null);
		dlgView.setAdapter(mAdapter);
	}
	private void setTitle(String title){
		mTitleView.setText(title);
	}
	
	private void setContent(String content){
		mContentView.setText(content);
	}
	
	private void setClickListener(View.OnClickListener left , View.OnClickListener right){
		if(left!=null && right!=null){
			mLeftButton.setOnClickListener(left);
			mRightButton.setOnClickListener(right);
		}else if(left!=null && right==null){
			mLeftButton.setOnClickListener(left);
		}else {
			
		}
	}
	
	/*
	 * Layout
	 */
	private TextView mTitleView;
	private TextView mContentView;
	private Button mLeftButton;
	private Button mRightButton;
	private String mTitle;
	private String mContent;
	
	
	private View.OnClickListener mLeftClickListener;
	private View.OnClickListener mRightClickListener;
	
	/*
	 * Layout
	 */
	private void setLayout(){
//		mTitleView = (TextView) findViewById(R.id.tv_title);
//		mContentView = (TextView) findViewById(R.id.tv_content);
//		mLeftButton = (Button) findViewById(R.id.bt_left);
	//	mRightButton = (Button) findViewById(R.id.bt_right);
	}
	
}









