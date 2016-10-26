package com.jzjf.headmaster;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jzjf.headmaster.R;
import com.jzjf.headmaster.api.ApiHttpClient;
import com.jzjf.headmaster.global.HeadmasterApplication;

/**
 * 反馈详情
 * @author pengdonghua
 *
 */
public class FeedBackDetailAct extends BaseActivity{
	
	private ImageView img,imgResponse;
	
	private TextView tvName,tvTime,tvContent,tvResponseName,tvResponseContent,tvResponseTime;
	
	private EditText et;

	@Override
	protected void initView() {
		setTitle("教练反馈");
		View view = View.inflate(this, R.layout.act_feedback_detail, null);
		content.addView(view);
		tvName = (TextView) view.findViewById(R.id.act_feedback_name);
		tvTime = (TextView) view.findViewById(R.id.act_feedback_time);
		tvContent = (TextView) view.findViewById(R.id.act_feedback_content);
		tvResponseName = (TextView) view.findViewById(R.id.act_feedback_response_name);
		tvResponseContent = (TextView) view.findViewById(R.id.act_feedback_response_content);
		tvResponseTime = (TextView) view.findViewById(R.id.act_feedback_response_time);
		et = (EditText) view.findViewById(R.id.act_feedback_et);
	}

	@Override
	protected void initData() {
		
		
	}
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.act_feedback_btn://不能为空
			if(TextUtils.isEmpty(et.getText().toString())){
				Toast.makeText(this, "回复不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			loadSubmit(et.getText().toString());
			break;
		}
	}
	
	/**
	 * 提交回复
	 */
	private void loadSubmit(String content){
		
		String userId = HeadmasterApplication.app.userInfo.userid;
		String schoolId = HeadmasterApplication.app.userInfo.driveschool.schoolid;
		String backId = "0";
		ApiHttpClient.get("statistics/replycoachfeedback?userid=" + userId
				+ "&replycontent=" + content+ "&feedbackid=" + backId + "&schoolid=" + schoolId,
				handler);
	}

	@Override
	public void processSuccess(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processFailure() {
		
	}

}
