package cn.compscosys.objects;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

import cn.compscosys.config.LoggerConfigureReader;
import cn.compscosys.config.TemplateConfigureReader;
import cn.compscosys.objects.geometry.Paragraph;
import cn.compscosys.objects.geometry.Rectangle;
import cn.compscosys.objects.geometry.Screenshot;
import cn.compscosys.objects.geometry.Text;

public class TempletValue {
	private final int templateIndex;
	public final String font;
	
	public final int mainWidth;
	public final int mainHeight;
	public final int statusBarHeight;
	public final int backgroundHeight;
	public final int bottomLine;
	
	public final Text topTime;
	public final Text rightNickname;
	public final Text leftNickname;
	public final Text studentInfo;
	public final Text bottomTime;
	
	public final Paragraph linkText;
	
	public final Rectangle topReturn;
	public final Rectangle topCamera;
	public final Rectangle rightPortrait;
	public final Rectangle leftPortrait;
	public final Rectangle linkIcon;
	public final Rectangle bottomFunction;
	public final Rectangle grayBackground;
	
	public final Screenshot[] screenshots;

	public static TempletValue creatTempleteValue(int templateIndex) {
		if(templateIndex < 0 || templateIndex > 9) { return null; }
		boolean isExist = TemplateConfigureReader.updateSettings(templateIndex);
		if(isExist) {
			return new TempletValue(templateIndex);
		}
		else {
			return null;
		}
	}
	
	private TempletValue(int templateIndex) {
		this.templateIndex = templateIndex;
		font = TemplateConfigureReader.font;

		mainWidth = TemplateConfigureReader.mainWidth;
		mainHeight = TemplateConfigureReader.mainHeight;
		statusBarHeight = TemplateConfigureReader.statusBarHeight;
		backgroundHeight = TemplateConfigureReader.backgroundHeight;
		bottomLine = TemplateConfigureReader.bottomLine;
		
		topTime = new Text(TemplateConfigureReader.topTime_x, TemplateConfigureReader.topTime_y, TemplateConfigureReader.topTime_height);
		rightNickname = new Text(TemplateConfigureReader.rightNickname_x, TemplateConfigureReader.rightNickname_y, TemplateConfigureReader.rightNickname_height);
		leftNickname = new Text(TemplateConfigureReader.leftNickname_x, TemplateConfigureReader.leftNickname_y, TemplateConfigureReader.leftNickname_height);
		studentInfo = new Text(TemplateConfigureReader.studentInfo_x, TemplateConfigureReader.studentInfo_y, TemplateConfigureReader.studentInfo_height);
		bottomTime = new Text(TemplateConfigureReader.bottomTime_x, TemplateConfigureReader.bottomTime_y, TemplateConfigureReader.bottomTime_height);
		
		linkText = new Paragraph(TemplateConfigureReader.linkText_x, TemplateConfigureReader.linkText_singleLine,
				TemplateConfigureReader.linkText_doubleLine, TemplateConfigureReader.linkText_height);
		
		topReturn = new Rectangle(TemplateConfigureReader.topReturn_x, TemplateConfigureReader.topReturn_y,
				TemplateConfigureReader.topReturn_width, TemplateConfigureReader.topReturn_height);
		topCamera = new Rectangle(TemplateConfigureReader.topCamera_x, TemplateConfigureReader.topCamera_y,
				TemplateConfigureReader.topCamera_width, TemplateConfigureReader.topCamera_height);
		rightPortrait = new Rectangle(TemplateConfigureReader.rightPortrait_x, TemplateConfigureReader.rightPortrait_y,
				TemplateConfigureReader.rightPortrait_width, TemplateConfigureReader.rightPortrait_height);
		leftPortrait = new Rectangle(TemplateConfigureReader.leftPortrait_x, TemplateConfigureReader.leftPortrait_y,
				TemplateConfigureReader.leftPortrait_width, TemplateConfigureReader.leftPortrait_height);
		linkIcon = new Rectangle(TemplateConfigureReader.linkIcon_x, TemplateConfigureReader.linkIcon_y,
				TemplateConfigureReader.linkIcon_width, TemplateConfigureReader.linkIcon_height);
		bottomFunction = new Rectangle(TemplateConfigureReader.bottomFunction_x, TemplateConfigureReader.bottomFunction_y,
				TemplateConfigureReader.bottomFunction_width, TemplateConfigureReader.bottomFunction_height);
		grayBackground = new Rectangle(TemplateConfigureReader.grayBackground_x, TemplateConfigureReader.grayBackground_y,
				TemplateConfigureReader.grayBackground_width, TemplateConfigureReader.grayBackground_height);
		
		screenshots = new Screenshot[TemplateConfigureReader.screenshotArrayList.size()];
		int index = 0;
		for(int[] screenshotInts : TemplateConfigureReader.screenshotArrayList) {
			screenshots[index] = new Screenshot(screenshotInts[0], screenshotInts[1]);
			index++;
		}
	}
	
	private Image getImageByName(String picutreNameString) {
		String filePath = "." + File.separator + "templets" + File.separator + picutreNameString;
		try {
			Image statusBarImage = new ImageIcon(filePath).getImage();
			return statusBarImage;
		} catch (Exception e) {
			LoggerConfigureReader.logger.warn("Filed to load file: " + filePath);
			LoggerConfigureReader.logger.error("Exception", e);
			return null;
		}
	}
	
	public Image getStatusBar() {
		String filePath = Integer.toString(this.templateIndex) + "_status_bar.png";
		Image statusBarImage = this.getImageByName(filePath);
		return statusBarImage;
	}
	
	public Image getTopCamera() {
		Image topCameraImage = this.getImageByName("top_camera.png");
		//Image changedTopCameraImage = topCameraImage.getScaledInstance(this.topCamera.width, this.topCamera.height, Image.SCALE_SMOOTH);
		return topCameraImage;
	}
	
	public Image getTopReturn() {
		Image topReturnImage = this.getImageByName("top_return.png");
		//Image changedTopReturnImage = topReturnImage.getScaledInstance(this.topReturn.width, this.topReturn.height, Image.SCALE_SMOOTH);
		return topReturnImage;
	}
	
	public Image getBottomFunction() {
		Image bottomFunctionImage = this.getImageByName("bottom_function.png");
		//Image changedBottomFunctionImage = bottomFunctionImage.getScaledInstance(this.bottomFunction.width, this.bottomFunction.height, Image.SCALE_SMOOTH);
		return bottomFunctionImage;
	}
	
	@Override
	public String toString() {
		return "[Template " + templateIndex + ": width=" + mainWidth + ", height=" + mainHeight + "]";
	}
}
