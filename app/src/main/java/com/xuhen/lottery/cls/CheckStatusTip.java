package com.xuhen.lottery.cls;

public class CheckStatusTip {
	public OneCheckStatusTip weight_tip = null;
	public OneCheckStatusTip ecg_tip = null;
	public OneCheckStatusTip blood_pressure_tip = null;
	public OneCheckStatusTip color_blind_tip = null;
	public OneCheckStatusTip temperature_tip = null;
	public OneCheckStatusTip oxygen_tip = null;
	public OneCheckStatusTip brain_waves_tip = null;
	public OneCheckStatusTip body_component_tip = null;
	
	public class OneCheckStatusTip {
		public String check_init_tip = "请点击“开始检测”按钮,然后开始检测";
		public String check_init_other_tip = "请点击“开始检测”按钮,然后开始检测";
		public String check_signal_tip = "正在检测信号，请稍候…";
		public String check_doing_tip = "正在检测中，请稍候…";
		public String check_success_tip = "本项检测成功，您可以在屏幕下方选择更多检测项";
		public String check_fail_tip = "您可能需要重新检测，请点击“重新检测”";
		public String check_all_finish_tip = "您已经检测完所有项目，请点击屏幕右下角“检测结果”";
		
		public OneCheckStatusTip(){
			check_init_tip = "请点击“开始检测”按钮,然后开始检测";
			check_init_other_tip = "请点击“开始检测”按钮,然后开始检测";
			check_signal_tip = "正在检测信号，请稍候…";
			check_doing_tip = "正在检测中，请稍候…";
			check_success_tip = "本项检测成功，您可以在屏幕下方选择更多检测项";
			check_fail_tip = "您可能需要重新检测，请点击“重新检测”";
			check_all_finish_tip = "您已经检测完所有项目，请点击屏幕右下角“检测结果”";
		}
	}
	
	public CheckStatusTip() {
		this.weight_tip = new OneCheckStatusTip();
		this.ecg_tip = new OneCheckStatusTip();
		this.blood_pressure_tip = new OneCheckStatusTip();
		this.color_blind_tip = new OneCheckStatusTip();
		this.temperature_tip = new OneCheckStatusTip();
		this.oxygen_tip = new OneCheckStatusTip();
		this.brain_waves_tip = new OneCheckStatusTip();
		this.body_component_tip = new OneCheckStatusTip();
		this.weight_tip.check_init_tip = "请端坐在凳子上 ，双脚放在脚印图形区， 然后点击“开始检测”";
		this.ecg_tip.check_init_tip = "请先点击“开始检测”，然后双手握住机器两侧的手柄。注意：孕妇不宜检测心电";
		this.ecg_tip.check_doing_tip = "正在检测中，请保持安静，检测时间大约1分钟，请稍候";
		this.blood_pressure_tip.check_init_tip = "请把左手放入血压臂筒，肘部放在肘置台上, 掌心朝上，手臂放松，然后点击“开始检测”";
		this.blood_pressure_tip.check_init_other_tip = "为保证测量的准确性，两次血压检测相隔的时间至少应为5分钟";
		this.blood_pressure_tip.check_signal_tip = "正在充气，请稍候…";
		this.blood_pressure_tip.check_doing_tip = "正在检测中, 请保持安静…";
		this.color_blind_tip.check_init_tip = "请在键盘上点击你看到的屏幕左侧的数字";
		this.color_blind_tip.check_doing_tip = "选择完毕";
		this.temperature_tip.check_init_tip = "请先点击“开始检测”，然后额头紧贴屏幕上方的体温感应头";
		this.oxygen_tip.check_init_tip = "请确认手指清洁，然后将食指放入血氧检测仪，再点击“开始检测”";
		this.oxygen_tip.check_init_other_tip = "您可能需要重新检测，请确认血氧检测仪手指插入处清洁，再点击“重新检测”";
		this.oxygen_tip.check_doing_tip = "正在检测中,请稍候…";
		this.brain_waves_tip.check_init_tip = "请在抽屉中拿出脑电波检测仪，跟随视频操作戴好检测仪，然后点击“开始检测”";
		this.brain_waves_tip.check_success_tip = "本项检测成功，请把脑电波返回抽屉，然后您可以在屏幕下方选择更多项检测";
		this.body_component_tip.check_init_tip = "请先点击“开始检测”，然后双手握住机器两侧的手柄。注意：孕妇不宜检测人体成分";
		this.body_component_tip.check_doing_tip = "正在检测中,请稍候…";
	}

}
