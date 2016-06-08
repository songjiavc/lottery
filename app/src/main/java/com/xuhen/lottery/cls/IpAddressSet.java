package com.xuhen.lottery.cls;

public class IpAddressSet {
	public final static int ETH0 = 1;
	public final static int WLAN0 = 2;
	public static native int GetResult();
	public static native String SetIpAddress(int net,String ip,String net_mask);
	public static native String SetGateWay(int net,String gate_way);
	public static native String SetDns(String dns);
	public static native int Version();
	public static native String GetIpAddress(int net);
	public static native String GetNetMask(int net);
	public static native String GetGateWay(int net);
	public static native String GetDns();
	public static native String SetAutoDhcp(int net);
	public static native String GetHttp(String url);
	public static native String SetLayoutType(int layout_type);
	public static native int GetLayoutType();
	public static native String Reboot();
	
	static {
		System.loadLibrary("ipaddressset");
	}
	
	public IpAddressSet(){
		
	}
}
