package com.xuhen.lottery.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.xuhen.lottery.activity.AppActivity;
import com.xuhen.lottery.activity.MainActivity;
import com.xuhen.lottery.activity.SettingActivity;
import com.xuhen.lottery.activity.UserActivity;
import com.xuhen.lottery.cls.Http;
import com.xuhen.lottery.view.CustomDialog;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.method.KeyListener;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyClass {
	public static void PrintErrorLog(String tag,String message){
		Log.e(tag, message);
	}
	public static void PrintInfoLog(String tag,String message){
		Log.i(tag, message);
	}
	public static void PrintLog(String tag,String message){
		Log.e(tag, message);
	}
	public static void PrintErrorLog(String message){
		Log.e("lottery", message);
	}
	public static void PrintInfoLog(String message){
		Log.i("lottery", message);
	}
	public static void PrintLog(String message){
		Log.e("lottery", message);
	}
	public static void SendMessage(Handler handler,int msg_id){
		Message msg = new Message();
		msg.what = msg_id;
		if(handler!=null)
		{
			handler.sendMessage(msg);
		}
	}
	public static void SendMessageDelay(Handler handler,int msg_id,long ms){
		Message msg = new Message();
		msg.what = msg_id;
		if(handler!=null)
		{
			handler.sendMessageDelayed(msg, ms);
		}
	}
	
	public static void SendMessageDelay(Handler handler,int msg_id,int arg1,long ms){
		Message msg = new Message();
		msg.what = msg_id;
		msg.arg1 = arg1;
		if(handler!=null)
		{
			handler.sendMessageDelayed(msg, ms);
		}
	}
	public static void SendMessageDelay(Handler handler,int msg_id,int arg1,int arg2,Object obj,long ms){
		Message msg = new Message();
		msg.what = msg_id;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		if(handler!=null)
		{
			handler.sendMessageDelayed(msg, ms);
		}
	}
	public static void SendMessageDelay(Handler handler,int msg_id,int arg1,int arg2,long ms){
		Message msg = new Message();
		msg.what = msg_id;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		if(handler!=null)
		{
			handler.sendMessageDelayed(msg, ms);
		}
	}
	
	public static void RemoveMessage(Handler handler,int msg_id){
		handler.removeMessages(msg_id);
	}
	public static void SendMessage(Handler handler,int msg_id, int arg){
		Message msg = new Message();
		msg.what = msg_id;
		msg.arg1 = arg;
		if(handler!=null)
		{
			handler.sendMessage(msg);
		}
	}
	public static void SendMessage(Handler handler,int msg_id, int arg1, int arg2){
		Message msg = new Message();
		msg.what = msg_id;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		if(handler!=null)
		{
			handler.sendMessage(msg);
		}
	}
	public static void SendMessage(Handler handler,int msg_id, Object obj){
		Message msg = new Message();
		msg.what = msg_id;
		msg.obj = obj;
		if(handler!=null)
		{
			handler.sendMessage(msg);
		}
	}
	public static void SendMessage(Handler handler,int msg_id, Object obj,int ms){
		Message msg = new Message();
		msg.what = msg_id;
		msg.obj = obj;
		if(handler!=null)
		{
			handler.sendMessageDelayed(msg, ms);
		}
	}
	public static void MySleep(long ms){
		SystemClock.sleep(ms);
	}
	public static JSONObject JsonPutString(JSONObject json, String key, String value) {
		try {
			json.put(key, value);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return json;
	}
	public static JSONObject JsonPutJson(JSONObject json, String key, JSONObject value) {
		try {
			json.put(key, value);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return json;
	}
	public static JSONObject JsonPutLong(JSONObject json, String key, Long value) {
		try {
			json.put(key, value);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return json;
	}
	public static JSONObject JsonPutInt(JSONObject json, String key, int value) {
		try {
			json.put(key, value);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return json;
	}
	public static JSONArray JsonArrayPutInt(JSONArray arr, int value) {
		try {
			arr.put(value);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return arr;
	}
	public static JSONArray JsonArrayPutJson(JSONArray arr, JSONObject value) {
		try {
			arr.put(value);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return arr;
	}
	public static JSONObject JsonPutArray(JSONObject json, String key, JSONArray value) {
		try {
			json.put(key, value);
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		return json;
	}
	public static JSONArray JsonArrayInit() {
		JSONArray json_arr = new JSONArray();
		return json_arr;
	}
	public static JSONArray JsonArrayInit(String str_json_array) {
		JSONArray json_arr = null;
		try {
			json_arr = new JSONArray(str_json_array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json_arr;
	}
	
	public static JSONObject JsonInit() {
		JSONObject json = new JSONObject();
		return json;
	}
	public static JSONObject JsonInit(String str_json){
		JSONObject json = null;
		try{
			json= new JSONObject(str_json);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	public static JSONArray GetJsonArray(JSONObject json, String key){
		JSONArray array = null;
		try {
			array = json.getJSONArray(key);
		} catch (JSONException e) {
			e.printStackTrace();			
		}
		return array;
	}
	public static JSONObject GetJsonObject(JSONObject json, String key){
		JSONObject obj = null;
		try {
			obj = json.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();			
		}
		return obj;
	}
	
	public static int GetJsonInt(JSONObject json, String key){
		int ret = -1;
		try {
			ret= json.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();			
		}
		return ret;
	}
	public static Long GetJsonLong(JSONObject json, String key){
		Long ret = -1L;
		try {
			ret= json.getLong(key);
		} catch (JSONException e) {
			e.printStackTrace();			
		}
		return ret;
	}
	public static String GetJsonString(JSONObject json, String key){
		String str = null;
		if(json.has(key)==false){
			return str;
		}
		try {
			str= json.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();			
		}
		return str;
	}
	public static Float GetJsonFloat(JSONObject json, String key){
		Float f = 0.0f;
		String str = GetJsonString(json,key);
		try{
			f = Float.parseFloat(str);
		}catch(Exception e){
			e.printStackTrace();
		}
		return f;
	}
	public static boolean GetJsonbool(JSONObject json, String key){
		boolean b = false;
		if(json.has(key)==false){//不存在，就返回
			return b;
		}
		try {
			b= json.getBoolean(key);
		} catch (JSONException e) {
			e.printStackTrace();		
		}
		return b;
	}
	public static String GetStringbyJsonArray(JSONArray arr, int index){
		String str = null;
		try {
			str = arr.getString(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static JSONObject GetJsonbyJsonArray(JSONArray arr, int index){
		JSONObject json = null;
		try {
			json = arr.getJSONObject(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	/**
	 * 生成硬件唯一的UUID
	 */
	public static String UUID(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	    String tmDevice, tmSerial, androidId;
	    
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());	   
	    String deviceId = deviceUuid.toString().toLowerCase();	  
	    deviceId = deviceId.substring(9, 28)+"-"+deviceId.substring(28,32)+"-"+deviceId.substring(32);
	    return deviceId;
	}
	public static String getMAC(Context my_context){
		Boolean b = false;
		WifiManager wifi = (WifiManager)my_context.getSystemService(Context.WIFI_SERVICE);  
		WifiInfo info = wifi.getConnectionInfo();
		String mac = info.getMacAddress();
		if(mac==null){
			mac = "11:22:33:44:55:66";
		}
		return mac.replaceAll(":", "");
	}
	/*
	public static String GetBtMac(Context my_context){
		String mac=null;
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter!=null){
			if (adapter.isEnabled()==true)  
			{  
				mac = adapter.getAddress().replaceAll(":", "");
			}else{
				mac = "closed";
			}			
		}
		return mac;
	}
	*/
	public static String Json2Sql(String str_json){
		return str_json.replaceAll("\"", "&quot;");
	}
	public static String Sql2Json(String str_sql){
		return str_sql.replaceAll("&quot;", "\"");
	}
	public static void EmptyDir(String dir){
		File file = new File(dir);
		File[] childFiles = file.listFiles();
		if (childFiles != null && childFiles.length != 0) {
			for (int i = 0; i < childFiles.length; i++) {
				if(childFiles[i].isFile()==true){
					childFiles[i].delete();
				}
			} 
		}
	}
	public static boolean RenameDir(String src_dir,String dest_dir){
		boolean b = false;
		File file = new File(src_dir);
		if(file.isDirectory()==true&&file.canWrite()==true){
			File dest_file = new File(dest_dir);
			try{
				b = file.renameTo(dest_file);
			}catch(Exception e){
				e.printStackTrace();
				b = false;
			}
		}
		return b;
	}
	public static boolean RenameFile(String src_file,String dest_file){
		boolean b = false;
		File file = new File(src_file);
		if(file.isFile()==true&&file.canWrite()==true){
			File file1 = new File(dest_file);
			try{
				b = file.renameTo(file1);
			}catch(Exception e){
				e.printStackTrace();
				b = false;
			}
		}
		return b;
	}
	public static Date ConverToDate(String strDate){
		Date dt = new Date();		
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dt = df.parse(strDate);
		} catch (Exception e) {
		}
		return dt;
	}
	public static String ConverToString(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		return df.format(date);
	}
	public static Boolean WriteFile(String content, String file){
		Boolean b=false;
		OutputStream outstream = null;
		try{
			outstream = new FileOutputStream(file);
			outstream.write(content.getBytes());
			outstream.flush();
			outstream.close();
			b=true;
		}catch (Exception e){
			MyClass.PrintLog("media_ad","write file fail");
			if(outstream!=null){
				try {
					outstream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MyClass.DelFile(file);
			}
		}
		return b;
	}
	public static boolean CreateDir(String dir_path){
		boolean b = false;
		File file = new File(dir_path);
		if(file.exists()==false){
			file.mkdirs();
			b = true;
		}
		return b;
	}
	
	public static String HexString(byte[] b) {
		char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

	public static byte[] StringHex(String hexString){
		if(hexString == null){
			return null;
		}
		if(hexString.length() % 2 ==1){
			return null;
		}
		//格式校验证
		if(MyClass.CheckHexString(hexString)==false){
			return null;
		}
		byte[] ret = new byte[hexString.length()/2];
		for (int i = 0; i < hexString.length(); i+=2) {
			ret[i/2] = Integer.decode("0x"+hexString.substring(i,i+2)).byteValue();
		}
		return ret;
	}
	
	public static int StringInt(String str){
		int ret = -65535;
		if(str == null){
			return ret;
		}
		try{
			ret = Integer.parseInt(str);
		}catch(Exception e){
			
		}
		return ret;
	}
	
	public static boolean StringBool(String str){
		boolean b = false;
		if(str == null){
			return b;
		}
		try{
			b = Boolean.parseBoolean(str);
		}catch(Exception e){
			
		}
		return b;
	}
	
	public static long StringLong(String str){
		long ret = -1;
		if(str == null){
			return ret;
		}
		try{
			ret = Long.parseLong(str);
		}catch(Exception e){
			
		}
		return ret;
	}
	
	public static Float StringFloat(String str){
		float ret = 0.0f;
		if(str == null){
			return ret;
		}
		try{
			ret = Float.parseFloat(str);
		}catch(Exception e){
			
		}
		return ret;
	}

    public static String MD5(byte[] buf) {
    	MessageDigest digester;
    	String str_ret = null;
        try{
        	digester = MessageDigest.getInstance("MD5");
        	digester.update(buf);
            str_ret = HexString(digester.digest());   
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return str_ret;
    }
    
    //对于大文件的MD5计算，内存不足，需要部分读取计算
    public static String GetFileMD5(String full_file){    	
    	MessageDigest digester;
    	String str_ret = null;
    	File file = new File(full_file);
    	if(file.exists()==false||file.canRead()==false){
    		return str_ret;
    	}
        try{
			byte[] buf = new byte[65536];
			InputStream fosfrom = new FileInputStream(full_file);
			int i = 0;
			int c=0;
			digester = MessageDigest.getInstance("MD5");
			while ((c = fosfrom.read(buf, 0, 65536)) > 0) 
			{
				digester.update(buf, 0, c);
			}
			fosfrom.close();      	
            str_ret = HexString(digester.digest());   
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return str_ret;
    }
  //比较字符的前Length个字
    public static int IsEquals(String str_src, String str_dest,int length) {
		int ret = -2;
		if(str_src==null||str_dest==null){
			return -2;
		}
		byte[] src_bytes = str_src.getBytes();
		byte[] dest_bytes = str_dest.getBytes();
		if(src_bytes.length<length||dest_bytes.length<length){
			return -2;
		}
		for(int i=0;i<length;i++){
			if(MyClass.uint(src_bytes[i])>MyClass.uint(dest_bytes[i])){
				ret = 1;
				break;
			}else if(MyClass.uint(src_bytes[i])<MyClass.uint(dest_bytes[i])){				
				ret = -1;
				break;
			}
		}
		//没有判定结果
		if(ret == -2){
			ret = 0;
		}
		return ret;
	}
    //byte转int
  	public static int uint(byte b){
  		int ret = 0;
  		ret = (int)b;
  		if(b<0){
  			ret += 256;
  		}
  		return ret;		
  	}	
	public static int IsEquals(String str_src, String str_dest) {
		int ret = -2;
		if(str_src==null||str_dest==null){
			return -2;
		}
		byte[] src_bytes = str_src.getBytes();
		byte[] dest_bytes = str_dest.getBytes();
		if(src_bytes.length>dest_bytes.length){
			return 1;
		}
		if(src_bytes.length<dest_bytes.length){
			return -1;
		}
		for(int i=0;i<src_bytes.length&&i<dest_bytes.length;i++){
			if(MyClass.uint(src_bytes[i])>MyClass.uint(dest_bytes[i])){
				ret = 1;
				break;
			}else if(MyClass.uint(src_bytes[i])<MyClass.uint(dest_bytes[i])){				
				ret = -1;
				break;
			}
		}
		//MyClass.PrintLog("isequals", "str_ret is "+Integer.toString(ret));
		if(ret == -2){
			ret = 0;
		}
		//MyClass.PrintLog("isequals", "str_ret is "+Integer.toString(ret));
		return ret;
	}
	public static Boolean DelFile(String string) {
		Boolean b = false;
		File file = new File(string);
		if(file.exists() == true){
			b = file.delete();
		}
		return b;
	}
	
	public static void DelDir(String dir){
		File file = new File(dir);
		if (file.isFile()) {  
			file.delete();  
			return;  
		}  
        if(file.isDirectory()){  
            File[] childFiles = file.listFiles();  
            if (childFiles == null || childFiles.length == 0) {  
                file.delete();  
                return;  
            }     
            for (int i = 0; i < childFiles.length; i++) {  
            	DelDir(childFiles[i].getPath()+"/"+childFiles[i].getName());  
            }  
            file.delete();  
        }  
	}
	
	public static void ShowWindowText(Context context,String str){
		try{
			Toast toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String GetMoreError(Exception e){
		StackTraceElement[] stack = e.getStackTrace();
		String str = e.getLocalizedMessage();
		for(StackTraceElement ste : stack){
			str += ste.toString()+"\r\n";
		}
		return str;
	}
	public static int GetRandInt(int max){
		int ret = 0;
		Random r = new Random();
		ret = r.nextInt(max);
		return ret;
	}
	//用户退出前必须调用,函数中会上传测量结果
	/*public static void UserExit(){
		if(MyVar.check_record!=null){
			if(UserData.user_login=true&&"5".equals(UserData.user_login_type)==false&&UserData.idcard_number.equals("")!=true&&UserData.idcard_number.equals("0")!=true){
				MyVar.check_record.Commit();
			}else{
				MyVar.check_record.GuestCommit();
			}
		}
	}*/
	//取得当前时间字符串
	public static String GetNowDateString(){
		Date dt = new Date();
		return MyClass.ConverToString(dt);
	}
	//取得当前时间,时间戳
	public static int GetNowTimestamp(){
		Date dt = new Date();
		return MyClass.GetTimestamp(dt);
	}
	//取得当前时间,毫秒
	public static long GetNowMs(){
		Date dt = new Date();
		long ms = dt.getTime();
		return ms;
	}
	//取得时间的时间戳
	public static int GetTimestamp(Date dt){
		long ms = dt.getTime();
		int time = (int)(ms/1000);
		return time;
	}
	//通过时间戳取得date
	public static Date GetDateByTimestamp(int time){
		Date dt = new Date();
		dt.setTime(time*1000L);
		return dt;
	}
	//检查TEL格式
	public static boolean CheckTelFormat(String str){
		Pattern p = Pattern.compile("^1[358][0-9]{9}$");
        Matcher m = p.matcher(str);
		return m.matches();
	}
	//检查是否整数
	public static boolean CheckNumber(String str){
		Pattern p = Pattern.compile("^[0-9]+$");  
        Matcher m = p.matcher(str);
		return m.matches();
	}
	//检查是否16进制字符串
	public static boolean CheckHexString(String str){
		Pattern p = Pattern.compile("^[0-9A-Fa-f]+$");  
        Matcher m = p.matcher(str);
		return m.matches();
	}
	//通过函数名,运行函数,只支持类CallBack中的函数,不支持网络回调方法,只能是简单本地方法
	public static boolean RunFunByName(String function_name,String idcard_number,String str){
		boolean b = false;
		Class<?> threadClazz;
		try {
			threadClazz = Class.forName("com.xuhen.lottery.common.CallBack");
			Method method = threadClazz.getMethod(function_name,String.class,String.class);  
	        b = (Boolean)method.invoke(null,idcard_number,str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return b;
	}
	//体重协议双字节合并
	public static int GetIntByTwoByte(byte high_byte,byte low_byte){
		int ret = 0;
		ret = MyClass.uint(high_byte)*256+MyClass.uint(low_byte);
		return ret;
	}
	//格式化浮点数
	public static String FormatDouble(double input, String style){
		DecimalFormat df = new DecimalFormat();
		df.applyPattern(style);
		return df.format(input);
	}
	
	//将短字符串转化成时间
	public static Date StrToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	//将日期时间转化成星期
	public static int DateToWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > 7) {
			return 1;
		}
		return dayIndex;
	}
	//将字符串时间转化为星期
	public static String StrToWeek(String date){
		String[]WEEK = {
			"周日","周一","周二","周三","周四","周五","周六"
		};
		int pos = DateToWeek(StrToDate(date));
		return WEEK[pos-1];		
	}
	//从资源文件中复制文件
	public static int CopyAssetsFile(AssetManager asset_manager,String asset_file_name, String dest_file_name) {
		InputStream fosfrom = null;
		try 
		{
			fosfrom = asset_manager.open(asset_file_name);
			OutputStream fosto = new FileOutputStream(dest_file_name);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) 
			{
				fosto.write(bt, 0, c);				
			}			
			fosto.flush();
			fosto.close();
			fosfrom.close();
			return 0;	
		} catch (Exception ex){
			ex.printStackTrace();
			if(fosfrom!=null){
				try {
					fosfrom.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
			return -1;
		}
		
	}
	/*
	 * 
	 * 
	 * 校验规则是：
（1）十七位数字本体码加权求和公式 
S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和 
Ai:表示第i位置上的身份证号码数字值 
Wi:表示第i位置上的加权因子 
Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
（2）计算模 
Y = mod(S, 11) 
（3）通过模得到对应的校验码 
Y: 0 1 2 3 4 5 6 7 8 9 10 
校验码: 1 0 X 9 8 7 6 5 4 3 2
	 */
	//校验身份证号
	public static boolean CheckIdcardFormat(String str_idcard_number){
		boolean b = false;
		int[] power = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
		String[] mod = {"1","0","X","9","8","7","6","5","4","3","2"};
		
		if(str_idcard_number==null||str_idcard_number.length()!=18){
			return b;
		}
		if(MyClass.CheckNumber(str_idcard_number.substring(0,17))==false){
			return b;
		}
		int checksum = 0;
		for(int i=0;i<17;i++){
			checksum += power[i]* Integer.parseInt(str_idcard_number.substring(i, i+1));
		}
		checksum = checksum%11;
		String str_checksum = mod[checksum];
		if(str_idcard_number.substring(17).equals(str_checksum)==true){
			b=true;
		}
		return b;
	}
	public static float GetTextSize(float text_size){
		float new_text_size = text_size*MyVar.GetScreenWidth()/MyVar.GetDesignWidth()/MyVar.GetScaledDensity();
		return new_text_size;
	}
	//布局定义
	public static LayoutParams GetLayoutParams(int view_x,int view_y,int view_width,int view_height){
		int screen_width = MyVar.GetScreenWidth();
		int screen_height = MyVar.GetScreenHeight();
		int view_new_x = view_x*screen_width/MyVar.GetDesignHeight();
		int view_new_y = view_y*screen_height/MyVar.GetDesignWidth();
		int view_new_width = view_width*screen_width/MyVar.GetDesignHeight();
		int view_new_height = view_height*screen_height/MyVar.GetDesignWidth();
		@SuppressWarnings("deprecation")
		LayoutParams layout = new LayoutParams(view_new_width,view_new_height,view_new_x,view_new_y);
		return layout;
	}
	public static String GetPostContentByUrl(String str_url, String data){
		String str_ret = null;
		String str_port,str_host,str_file;
		String respond_status = "000";
		int pos = str_url.indexOf("http://");
		if(pos == -1){
			return str_ret;
		}
		int pos1 = str_url.indexOf('/', 7);
		String str_temp = str_url.substring(7,pos1);
		
		str_file = str_url.substring(pos1);
		pos = str_temp.indexOf(':');
		if(pos == -1){
			str_host = str_temp;
			str_port = "80";
		}else{
			str_host = str_temp.substring(0,pos);
			str_port = str_temp.substring(pos+1);
		}
		int port = Integer.parseInt(str_port);
		Socket s = new Socket();    
		SocketAddress remoteAddress = new InetSocketAddress(str_host, port);
		try {
			s.setSoTimeout(5000);
			s.connect(remoteAddress, 5000);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("PAD_beat", "Remote Server IO error");
			if(s!=null&&s.isConnected()==true){
				try {
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return str_ret;
		}
		OutputStream os=null;
		InputStream is=null;
		try {
			os = s.getOutputStream();
			int length = data.getBytes().length;//text/html   application/json
			String send_str = "POST "+str_file+" HTTP/1.0\r\nHOST: "+str_temp+"\r\nContent-Type: application/x-www-form-urlencoded\r\nPragma: no-cache\r\nCache-Control: no-cache, no-store, max-age=0\r\nConnection: close\r\nContent-Length: "+Integer.toString(length)+"\r\n\r\n"+data;			
			os.write(send_str.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
			str_ret = MyClass.GetJsonError("url is "+str_url+", exception message>>>"+MyClass.GetMoreError(e));
			Log.e("PAD_beat", "post data error");
			try {
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			return str_ret;
		}
		try {
			is = s.getInputStream();
			byte[] rev_buf = new byte[1024];
			//if(MyVar.SERVER_HEATH_HISTORY.equals(str_url)==true){
				MyClass.MySleep(100);
			//}
			int ret = is.read(rev_buf,0,1024);
			int len = ret;
			String str_rev_data = new String(rev_buf);
			while(str_rev_data.indexOf("\r\n\r\n")==-1){
				try{
					ret = is.read(rev_buf, len, 1024-len);
				}catch(Exception e){
					e.printStackTrace();
					str_ret = MyClass.GetJsonError("server read exception, message>>>"+MyClass.GetMoreError(e));
					is.close();
					if(s!=null&&s.isConnected()==true){
						s.close();
					}
					return str_ret;
				}
				len += ret;
				str_rev_data = new String(rev_buf,0,len);
				//MyClass.PrintLog("", str_rev_data);
			}
			//取得应答状态
			pos = str_rev_data.indexOf(' ');
			if(pos!=-1){
				pos1 = str_rev_data.indexOf(' ',pos+1);
				if(pos1!=-1&&pos1-pos==4){
					respond_status = str_rev_data.substring(pos+1,pos1);
				}
			}			
			pos = str_rev_data.toLowerCase().indexOf("content-length:");
			if(pos==-1){
				if(len >=1024){
					//继续接收剩余部分
					pos1 =str_rev_data.indexOf("\r\n\r\n")+4;
					len = 1024-pos1;
					byte[] msg = new byte[100000];
					System.arraycopy(rev_buf, pos1, msg, 0, len);
					do{
						try{
							ret = is.read(msg, len, 100000-len);
						}catch(Exception e1){
							e1.printStackTrace();
							break;
						}						
						if(ret==-1){//接收流断了
							break;
						}
						len += ret;
					}while(true);
					str_rev_data = new String(msg,0,len);
					pos = str_rev_data.lastIndexOf("\r\n0\r\n\r\n");
					pos1 = str_rev_data.indexOf("\r\n");
					if(pos!=-1&&pos1!=-1){
						str_rev_data = str_rev_data.substring(pos1+2, pos);						
					}
				}else{
					pos1 =str_rev_data.indexOf("\r\n\r\n")+4;
					int content_length = len-pos1;
					byte[] msg = new byte[content_length];
					System.arraycopy(rev_buf, pos1, msg, 0, content_length);
					str_rev_data = new String(msg,0,content_length);
					pos = str_rev_data.lastIndexOf("\r\n0\r\n\r\n");
					pos1 = str_rev_data.indexOf("\r\n");
					if(pos!=-1&&pos1!=-1){
						str_rev_data = str_rev_data.substring(pos1+2, pos);						
					}
				}
			}else{
				pos1 = str_rev_data.indexOf("\r\n", pos+15);
				String str_length = str_rev_data.substring(pos+15, pos1).trim();
				int content_length = Integer.parseInt(str_length);
				pos1 =str_rev_data.indexOf("\r\n\r\n")+4;
				len = len-pos1;
				byte[] msg = new byte[content_length];
				if(len<content_length){//未接收完成
					System.arraycopy(rev_buf, pos1, msg, 0, len);
					do{
						ret = is.read(msg, len, content_length-len);
						if(ret==-1){//接收流断了
							s.close();
							return null;
						}
						len += ret;
					}while(len<content_length);
				}else{
					//len有可能长于content-length,这样就会有异常
					System.arraycopy(rev_buf, pos1, msg, 0, content_length);
				}
				str_rev_data = new String(msg,0,content_length);
			}
			str_ret = str_rev_data;
			s.close();
		}catch(Exception e){
			e.printStackTrace();
			str_ret = MyClass.GetJsonError("url is "+str_url+",status is "+respond_status+", exception message>>>"+MyClass.GetMoreError(e));
			return str_ret;
		}
		if(respond_status.equalsIgnoreCase("200")==false){
			str_ret = MyClass.GetJsonError("url is "+str_url+",status is "+respond_status+", message>>>"+str_ret);
		}
		//MyClass.PrintInfoLog("receive data is "+str_ret);
		return str_ret;
	}
	public static String GetJsonError(String str){
		JSONObject json = JsonInit();
		json = MyClass.JsonPutString(json, "status", "fail");
		json = MyClass.JsonPutString(json, "errno", str);
		return json.toString();
	}
	
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {  
	    int w = bitmap.getWidth();
	    int h = bitmap.getHeight();
	    Matrix matrix = new Matrix();
	    float scaleWidth = ((float) width / w);
	    float scaleHeight = ((float) height / h);
	    matrix.postScale(scaleWidth, scaleHeight);
	    Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	    return newbmp;  
	}
	//检查IP地址存在
	public static boolean CheckIpExist(String ip){
		String ping = "ping -c 1 -w 2.5 ";
    	boolean b = false;
    	ping += ip;
    	Process proc;
		try {
			proc = Runtime.getRuntime().exec(ping);
			int result = proc.waitFor();
			if(result==0){
	        	b= true;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}       
        return b;
	}
	//检查ip地址有效与否
	public static boolean CheckIpVaild(String ip){
		boolean b = false;
		if(ip==null||ip.length()<7||ip.length()>15){
			return b;
		}
		if(ip.contains(".")==false){
			return b;
		}
		String[] arr = ip.split("\\.");
		if(arr==null||arr.length!=4){
			return b;
		}
		for(int i=0;i<4;i++){
			try{
				int value = Integer.parseInt(arr[i]);
				if(value<0||value>255){
					return b;
				}
			}catch(Exception e){
				return b;
			}
		}
		return true;
	}
	//检查PORT存在
	public static boolean CheckPortExist(String ip, int port){
		String cmd = "nc -z -w 2 -t ";
    	boolean b = false;
    	cmd += ip+" "+Integer.toString(port);
    	Process proc;
		try {
			proc = Runtime.getRuntime().exec(cmd);
			int result = proc.waitFor();
			if(result==0){
	        	b= true;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}       
        return b;
	}
	//健康记录时间格式转换
	public static String HeathDateFormat(String str_date){
		String str_ret = "2000-01-01T00:00:00.000+0800";
		if(str_date==null||str_date.length()!=19){
			return str_ret;
		}
		str_date += ".000+0800";
		str_date = str_date.replace(" ", "T");
		return str_date;
	}
	//获取当前APK版本
	public static int GetCurrentApkVersion(Context context){
		int ret = 0;
		String str_package_name = context.getPackageName();
		PackageManager pm = context.getPackageManager();
		try{
			PackageInfo info = pm.getPackageInfo(str_package_name, 0);
			if(info!=null){
				ret = info.versionCode;
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
		return ret;
	}
	//获取APK文件的版本号
	public static int GetApkFileVersion(Context context,String apk_file){
		int ret = 0;
		PackageManager pm = context.getPackageManager();  
	    PackageInfo info = pm.getPackageArchiveInfo(apk_file, PackageManager.GET_ACTIVITIES);  
	    if(info != null){
	        ret = info.versionCode;
	    }
	    return ret;
	}
	public static String getContentByFile(String str_file){
		String str_ret = null;
		InputStream fosfrom = null;
		try 
		{
			File file = new File(str_file);
			if(file.exists()==false){
				return str_ret;
			}
			int max_len = (int)file.length();
			fosfrom = new FileInputStream(str_file);
			byte bt[] = new byte[max_len];
			int c=0;
			int len = 0;
			while ((c = fosfrom.read(bt, len, max_len-len)) > 0) 
			{
				len+=c;
			}
			fosfrom.close();
			str_ret = new String(bt);
		} catch (Exception ex){
			ex.printStackTrace();
			if(fosfrom!=null){
				try {
					fosfrom.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return str_ret;
	}
	public static int GetUserAge(int birth){
		Calendar calendar = Calendar.getInstance();
	    int now_year = calendar.get(Calendar.YEAR);
	    if(now_year>2099){
    		now_year = 2000;
    	}
	    int now_month = calendar.get(Calendar.MONTH)+1;
	    int now_day = calendar.get(Calendar.DAY_OF_MONTH)+1;
	    int now_ymd = now_year*10000+now_month*100+now_day;
	    if(now_ymd<MyVar.build_date+20000000||now_ymd<SystemVar.last_one_run_data+20000000){
	    	now_ymd = SystemVar.last_one_run_data+20000000;
	    }
	    int age = (int)((now_ymd - birth)/10000);
	    return age;
	}
	/*public static void InitUserLoginData() {		
		//用户登录,写入新的登录记录
		MySqlLite.Insert("insert into login (idcard_number,login_time) values ('"+UserData.idcard_number+"','"+MyClass.GetNowDateString()+"')");
		//获取用户登录次数
		UserData.user_login_cnt = (Integer)MySqlLite.SimpleQuery("select count(*) as num from login where idcard_number='"+UserData.idcard_number+"'");
		
		//用户语言切换
		Pattern p = Pattern.compile("^44[015]{1}[0-9]{14}[0-9X]{1}$");
        Matcher m = p.matcher(UserData.idcard_number);
		if(m.matches()==true){
			MyVar.AUDIO_PATH = MyVar.APK_ROOT_PATH+"cantonese/";
		}else{
			MyVar.AUDIO_PATH = MyVar.APK_ROOT_PATH+"audio/";
		}
		MyVar.check_record.dt_login = MyClass.GetNowDateString();//记录登录时间
		MyVar.check_record.idcard_number = UserData.idcard_number;
		MyVar.check_record.idcard_sex = UserData.idcard_sex;
		MyVar.check_record.idcard_name = UserData.idcard_name;
		MyVar.check_record.user_age = UserData.user_age;
		MyVar.check_record.user_tel = UserData.user_tel;
		UserData.user_login = true;//用户登录			
	}*/
	//输入框过滤函数(只能输入X与数字)
	public static KeyListener GetTextKeyListener(){
		KeyListener listener = new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, false){
			@Override
			public boolean onKeyDown(View view, Editable content, int keyCode,
					KeyEvent event) {
				MyClass.PrintInfoLog("key="+keyCode);
				//67为删除
				//52为X
				//7-16为0-9
				if(keyCode==52||keyCode==67||(keyCode>=7&&keyCode<=16)){
					return super.onKeyDown(view, content, keyCode, event);
				}else{
					return true;
				}				
			}

			@Override
			public int getInputType() {
				//其它种类可能出现无光标现象
				return android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
			}
			
		};
		return listener;
	}
	//输入框过滤函数(只能输入数字)
	public static KeyListener GetNumberKeyListener(){
		KeyListener listener = new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, false){
			@Override
			public boolean onKeyDown(View view, Editable content, int keyCode,
					KeyEvent event) {
				//MyClass.PrintErrorLog("key="+keyCode);
				//67为删除
				//52为X
				//7-16为0-9
				if(keyCode==67||(keyCode>=7&&keyCode<=16)){
					return super.onKeyDown(view, content, keyCode, event);
				}else{
					return true;
				}				
			}
			@Override
			public int getInputType() {
				//其它种类可能出现无光标现象
				return android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;
			}
			
		};
		return listener;
	}
	
	public static void ShowDlgWindow(Activity activity,String title,String error_info){
		int width = 720;
		int height = width*5/8;
		CustomDialog dlg = new CustomDialog(activity);
		dlg.SetSize(width, height);
		dlg.show();
		dlg.SetTitle(title);
		dlg.SetButtonStatus(dlg.btn_ok, View.INVISIBLE);
		dlg.SetMessage(error_info);
		dlg.SetMessageGravity(Gravity.CENTER);
		dlg.btn_cancel.setTag(dlg);
		dlg.btn_cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				CustomDialog dlg = (CustomDialog)arg0.getTag();	
				dlg.dismiss();
			}
		});
	}
	
	public static void ShowOkDlgWindow(Activity activity,String title,String error_info){
		int width = 720;
		int height = width*5/8;
		CustomDialog dlg = new CustomDialog(activity);
		dlg.SetSize(width, height);
		dlg.show();
		dlg.SetTitle(title);
		dlg.SetButtonStatus(dlg.btn_cancel, View.INVISIBLE);
		dlg.SetMessage(error_info);
		dlg.SetMessageGravity(Gravity.CENTER);
		dlg.btn_ok.setTag(dlg);
		dlg.btn_ok.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				CustomDialog dlg = (CustomDialog)arg0.getTag();	
				dlg.dismiss();
			}
		});
	}
	
	public static InetAddress IntToInetAddress(int hostAddress) {
        InetAddress inetAddress;
        byte[] addressBytes = { (byte)(0xff & hostAddress),
                                (byte)(0xff & (hostAddress >> 8)),
                                (byte)(0xff & (hostAddress >> 16)),
                                (byte)(0xff & (hostAddress >> 24)) };

        try {
           inetAddress = InetAddress.getByAddress(addressBytes);
        } catch(UnknownHostException e) {
           return null;
        }

        return inetAddress;
    }
	
	//检查WIFI可以连网不
	public static Boolean CheckWifiOpen(Context my_context){
		Boolean b = false;
		WifiManager manager = (WifiManager)my_context.getSystemService(Context.WIFI_SERVICE);
		if(manager.isWifiEnabled()==true){
			b = true;
		}
		return b;		
	}
	//检查WIFI可以连网不
	public static Boolean CheckWifiStatus(Context my_context){
		Boolean b = false;
		ConnectivityManager manager = (ConnectivityManager)my_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info!=null){
			b = info.isConnected();
		}
		return b;		
	}
	//检查可以连网不
	public static Boolean CheckNetworkStatus(Context my_context){
		Boolean b = false;
		ConnectivityManager manager = (ConnectivityManager)my_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo	info = manager.getActiveNetworkInfo();
		if(info!=null){
			b=info.isConnected();
		}
		return b;		
	}
	//检查3G可以连网不
	public static Boolean Check3GStatus(Context my_context){
		Boolean b = false;
		ConnectivityManager manager = (ConnectivityManager)my_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		b = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
		return b;		
	}
	//检查3G可以连网不
	public static Boolean Set3GStatus(Context my_context,Boolean status){
		Boolean b = false;
		ConnectivityManager manager = (ConnectivityManager)my_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//State mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();		
		//if(mobile == State.CONNECTED||mobile==State.CONNECTING){
			Method setMobileDataEnable;
			try {
				setMobileDataEnable = manager.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
				setMobileDataEnable.invoke(manager, status);
				b = true;
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		//}
		return b;		
	}
	
	//获取一个随机端口,用于TCP视频聊天的本地端口
	public static int GetRandTcpVideoLocalPort(){
		int port = 12000;
		Random rand = new Random();
		port += rand.nextInt(99);
		return port;
	}
	//打开用户信息页面
	public static void ShowUserActivity(int cur_activity_id){
		if(MyVar.USER_PAGE_ID!=cur_activity_id){
			MyVar.CURRENT_PAGE_ID = MyVar.USER_PAGE_ID;
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			intent.setClass(GlobalApplication.getInstance().getApplicationContext(), UserActivity.class);
			GlobalApplication.getInstance().startActivity(intent);
		}
	}
	//打开APP列表页面
	public static void ShowAppActivity(int cur_activity_id){
		if(MyVar.APP_PAGE_ID!=cur_activity_id){
			MyVar.CURRENT_PAGE_ID = MyVar.APP_PAGE_ID;
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
			intent.setClass(GlobalApplication.getInstance().getApplicationContext(), AppActivity.class);
			GlobalApplication.getInstance().startActivity(intent);
		}
	}
	//打开设置页面
	public static void ShowSettingActivity(int cur_activity_id){
		if(MyVar.SETTING_PAGE_ID!=cur_activity_id){		
			MyVar.CURRENT_PAGE_ID = MyVar.SETTING_PAGE_ID;
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			intent.setClass(GlobalApplication.getInstance().getApplicationContext(), SettingActivity.class);
			GlobalApplication.getInstance().startActivity(intent);
		}
	}
	//打开设置页面
	public static void ShowWifiSettingActivity(int cur_activity_id){
		if(MyVar.CURRENT_PAGE_ID==cur_activity_id){			
			String pkg = "com.xuhen.mywifi";
	        //应用的主activity类
	        String cls = "com.xuhen.mywifi.WifiActivity";            
	        ComponentName componet = new ComponentName(pkg, cls);            
	        Intent i = new Intent();
	        i.setComponent(componet);
	        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
	        GlobalApplication.getInstance().startActivity(i);
		}
	}
	//网络协议请求
	public static void SendHttpRequest(final String url,final Handler handler,final int msg_id){
		if(handler==null){
			return;
		}
		//防止上次才请求网络，用户再次点击
		if(handler.hasMessages(msg_id)==true){
			MyClass.ShowWindowText(GlobalApplication.getInstance(), "正在网络请求，请稍候再试...");
			return;
		}
		Thread th = new Thread(){
			@Override
			public void run() {
				MyClass.SendMessageDelay(handler, msg_id, 10000);
				String str_ret = getContentByUrl(url);
				MyClass.RemoveMessage(handler, msg_id);
				if(str_ret==null||"ERROR".equals(str_ret)==true){
					MyClass.SendMessage(handler, msg_id, "ERROR");
				}else{
					MyClass.SendMessage(handler, msg_id, str_ret);
				}
				super.run();
			}			
		};
		th.start();
	}
	public static String getContentByUrl(String str_url) {
		String str_ret = "ERROR";
		HttpGet hg = null;
		try {
			hg = new HttpGet(str_url.trim());
		} catch(Exception e) {
			e.printStackTrace();
			return str_ret;
		}
		DefaultHttpClient client = new DefaultHttpClient();
		//client.getParams().setParameter("http.connection.timeout", Integer.valueOf(0xfa0));
		//client.getParams().setParameter("http.socket.timeout", Integer.valueOf(0xfa0));
		try {
			HttpResponse response = client.execute(hg);
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch(ClientProtocolException e) {
			PrintLog(" is a http url");
		} catch(IOException e) {
			PrintLog(" http io error");
		}
		return str_ret;
	}
	public static String GetStationId(){
		if(MyVar.station_dto==null){
			return null;
		}
		String station_id = MyClass.GetJsonString(MyVar.station_dto,"id");
		if(station_id==null){
			return null;
		}
		return station_id;
	}
	
	public static void UserLogout(){
		SystemVar.username = "";
		SystemVar.password = "";
		SystemVar.SaveCfg();
		MyVar.login_flag = false;
		MyVar.announcement = null;
		MyVar.company_notice = null;
		MyVar.station_dto = null;
		MyVar.use_app_dtos = null;
		MyVar.server_apps_list = null;
		MyVar.read_annuonce_ids = "";
	}
	
	public static String GetCpuName(){
		String key = "Hardware";
		String str_ret = "PK3188";
		try{
			FileReader reader = new FileReader("/proc/cpuinfo");
			char bt[] = new char[10240];
			int c=0;
			int len = 0;
			while ((c = reader.read(bt, len, 1024)) > 0) 
			{
				len+=c;
			}
			if(len<=10){
				return str_ret;
			}
			String str_content = new String(bt);
			if(str_content!=null&&str_content.contains(key)==true){
				int s_pos = str_content.indexOf(key);
				s_pos = str_content.indexOf(":",s_pos);
				int e_pos = str_content.indexOf("\n", s_pos);
				if(s_pos!=-1&&e_pos!=-1&&e_pos-s_pos>1){
					String str_tmp = str_content.substring(s_pos+1, e_pos);
					str_ret = str_tmp.trim(); 
				}
			}
		}catch (Exception e){
		     e.printStackTrace();
		}
		return str_ret;
	}
	
	public static long GetRamSize(){
		String key = "MemTotal";
		long ret = 1*1024*1024*1024;
		try{
			FileReader reader = new FileReader("/proc/meminfo");
			char bt[] = new char[10240];
			int c=0;
			int len = 0;
			while ((c = reader.read(bt, len, 1024)) > 0) 
			{
				len+=c;
			}
			if(len<=10){
				return ret;
			}
			String str_content = new String(bt);
			if(str_content!=null&&str_content.contains(key)==true){
				int s_pos = str_content.indexOf(key);
				s_pos = str_content.indexOf(":",s_pos);
				int e_pos = str_content.indexOf("kB", s_pos);
				if(s_pos!=-1&&e_pos!=-1&&e_pos-s_pos>1){
					String str_tmp = str_content.substring(s_pos+1, e_pos);
					ret = Long.parseLong(str_tmp.trim())*1024; 
				}
			}
		}catch (Exception e){
		     e.printStackTrace();
		}
		return ret;
	}  

	public static boolean SetKeyUp(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_SETTINGS==keyCode){
			MyClass.ShowSettingActivity(MyVar.CURRENT_PAGE_ID);
			return true;
		}
		if(keyCode==0x04){
			return true;
		}
		/*if(KeyEvent.==keyCode){
			AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}*/
		return false;
	}
	
	public static boolean SetKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_SETTINGS==keyCode){
			return true;
		}
		if(keyCode==0x04){
			return true;
		}
		return false;
	}
	
	public static void CheckError(){
		boolean b = true;
		MyClass.PrintInfoLog("screen:width="+MyVar.GetScreenWidth()+",height="+MyVar.GetScreenHeight());
		int width = MyVar.GetScreenWidth();
		int height = MyVar.GetScreenHeight();
		if(MyVar.SYSTEM_LAYOUT_TYPE==1&&width>=height){
			MyClass.PrintErrorLog("screen info error,reboot launcher");
			GlobalApplication.getInstance().ExitApp();
			return;
		}
		if(MyVar.SYSTEM_LAYOUT_TYPE==0&&width<=height){
			MyClass.PrintErrorLog("screen info error,reboot launcher");
			GlobalApplication.getInstance().ExitApp();
			return;
		}
	}
}
