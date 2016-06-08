package com.xuhen.lottery.cls;

import org.json.JSONObject;

import com.xuhen.lottery.common.MyClass;

public class Http {
	public static String HttpPost(String url,JSONObject json){
		String str = MyClass.GetPostContentByUrl(url, json.toString());
		return str;
	}
}
