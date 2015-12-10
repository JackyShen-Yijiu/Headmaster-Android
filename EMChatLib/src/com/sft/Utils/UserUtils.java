package com.sft.Utils;

import com.sft.emchatlib.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

public class UserUtils {

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar(Context context, String url, ImageView imageView) {
		if (!TextUtils.isEmpty(url)) {
			Picasso.with(context).load(url).placeholder(R.drawable.default_avatar).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.default_avatar).into(imageView);
		}
	}

}
