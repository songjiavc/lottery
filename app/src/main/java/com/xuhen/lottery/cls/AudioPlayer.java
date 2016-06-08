package com.xuhen.lottery.cls;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xuhen.lottery.common.SystemVar;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class AudioPlayer{
	private static String file = null;
	private static MediaPlayer ap = null;
	private static boolean loop = true;
	
	public static void Init(){
		ap = new MediaPlayer();
	}
	
	public static void SetLoopStatus(boolean b){
		AudioPlayer.loop = true;
	}
	
	public static void Play(String file,boolean b){
		AudioPlayer.loop = b;
		Play(file);
	}
	
	public static void Play(final String file){
		AudioPlayer.file = file;
		if(CheckFile()==false){
			return;
		}
		if(AudioPlayer.ap!=null){
			AudioPlayer.ap.reset();
		}else{
			AudioPlayer.Init();
		}
		try {
			AudioPlayer.ap.setDataSource(AudioPlayer.file);
			AudioPlayer.ap.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AudioPlayer.ap.setVolume(SystemVar.video_volume, SystemVar.video_volume);
		AudioPlayer.ap.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				Play(file);	
			}
		});
		AudioPlayer.ap.start();	
	}
	
	public static void Stop(){
		if(AudioPlayer.ap!=null&&AudioPlayer.ap.isPlaying()==true){
			AudioPlayer.ap.stop();
			AudioPlayer.ap.reset();
		}
	}
	
	private static boolean CheckFile(){
		boolean b = false;
		if(AudioPlayer.file==null){
			return b;
		}
		Pattern p = Pattern.compile(".*mp3$");
        Matcher m = p.matcher(AudioPlayer.file);
		if(m.matches()==false){
			return b;
		}
		File file = new File(AudioPlayer.file);
		if(file.exists()==true){
			if(file.isFile()==true){
				b = true;
			}
		}		
		return b;
	}
}
