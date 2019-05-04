package cn.compscosys.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class TemplateConfigureReader {
	private static String configPath = "." + File.separator + "resources" + File.separator + "template.xml";
	
	public static String font;
	
	public static int mainWidth;
	public static int mainHeight;
	public static int statusBarHeight;
	public static int backgroundHeight;
	public static int bottomLine;
	
	public static int topTime_x;
	public static int rightNickname_x;
	public static int leftNickname_x;
	public static int studentInfo_x;
	public static int bottomTime_x;
	
	public static int topTime_y;
	public static int rightNickname_y;
	public static int leftNickname_y;
	public static int studentInfo_y;
	public static int bottomTime_y;
	
	public static int topTime_height;
	public static int rightNickname_height;
	public static int leftNickname_height;
	public static int studentInfo_height;
	public static int bottomTime_height;
	
	public static int linkText_x;
	public static int linkText_singleLine;
	public static int linkText_doubleLine;
	public static int linkText_height;
	
	public static int topReturn_x;
	public static int topCamera_x;
	public static int rightPortrait_x;
	public static int leftPortrait_x;
	public static int linkIcon_x;
	public static int bottomFunction_x;
	public static int grayBackground_x;
	
	public static int topReturn_y;
	public static int topCamera_y;
	public static int rightPortrait_y;
	public static int leftPortrait_y;
	public static int linkIcon_y;
	public static int bottomFunction_y;
	public static int grayBackground_y;
	
	public static int topReturn_width;
	public static int topCamera_width;
	public static int rightPortrait_width;
	public static int leftPortrait_width;
	public static int linkIcon_width;
	public static int bottomFunction_width;
	public static int grayBackground_width;
	
	public static int topReturn_height;
	public static int topCamera_height;
	public static int rightPortrait_height;
	public static int leftPortrait_height;
	public static int linkIcon_height;
	public static int bottomFunction_height;
	public static int grayBackground_height;
	
	public static ArrayList<int[]> screenshotArrayList;
	
	public static final String setConfigPath(String configPath) {
		TemplateConfigureReader.configPath = configPath;
		return TemplateConfigureReader.configPath;
	}
	
	public static final boolean updateSettings(int templateIndex) {
		try {
			SAXReader xmlReader = new SAXReader();
			Document xmlDocument = xmlReader.read(new File(TemplateConfigureReader.configPath));
			Element rootElement = xmlDocument.getRootElement();
			
			/**
			 * 读取字体设置
			 */
			Element fontElement = rootElement.element("fonts");
			Node fontNode = fontElement.selectSingleNode("template[@id='" + templateIndex + "']");
			font = fontNode.getText();
			
			/**
			 * 读取模板数据
			 */
			Element screenElement = rootElement.element("screens");
			Node templateNode = screenElement.selectSingleNode("template[@id='" + templateIndex + "']");
			
			Node mainNode = templateNode.selectSingleNode("main");
			mainWidth = Integer.parseInt(mainNode.selectSingleNode("width").getText());
			mainHeight = Integer.parseInt(mainNode.selectSingleNode("height").getText());
			statusBarHeight = Integer.parseInt(mainNode.selectSingleNode("statusBarHeight").getText());
			backgroundHeight = Integer.parseInt(mainNode.selectSingleNode("backgroundHeight").getText());
			bottomLine = Integer.parseInt(mainNode.selectSingleNode("bottomLine").getText());
			
			Node textNode = templateNode.selectSingleNode("text");
			topTime_x = Integer.parseInt(textNode.selectSingleNode("topTime").selectSingleNode("x").getText());
			topTime_y = Integer.parseInt(textNode.selectSingleNode("topTime").selectSingleNode("y").getText());
			topTime_height = Integer.parseInt(textNode.selectSingleNode("topTime").selectSingleNode("height").getText());
			
			rightNickname_x = Integer.parseInt(textNode.selectSingleNode("rightNickname").selectSingleNode("x").getText());
			rightNickname_y = Integer.parseInt(textNode.selectSingleNode("rightNickname").selectSingleNode("y").getText());
			rightNickname_height = Integer.parseInt(textNode.selectSingleNode("rightNickname").selectSingleNode("height").getText());
			
			leftNickname_x = Integer.parseInt(textNode.selectSingleNode("leftNickname").selectSingleNode("x").getText());
			leftNickname_y = Integer.parseInt(textNode.selectSingleNode("leftNickname").selectSingleNode("y").getText());
			leftNickname_height = Integer.parseInt(textNode.selectSingleNode("leftNickname").selectSingleNode("height").getText());
			
			studentInfo_x = Integer.parseInt(textNode.selectSingleNode("studentInfo").selectSingleNode("x").getText());
			studentInfo_y = Integer.parseInt(textNode.selectSingleNode("studentInfo").selectSingleNode("y").getText());
			studentInfo_height = Integer.parseInt(textNode.selectSingleNode("studentInfo").selectSingleNode("height").getText());
			
			bottomTime_x = Integer.parseInt(textNode.selectSingleNode("bottomTime").selectSingleNode("x").getText());
			bottomTime_y = Integer.parseInt(textNode.selectSingleNode("bottomTime").selectSingleNode("y").getText());
			bottomTime_height = Integer.parseInt(textNode.selectSingleNode("bottomTime").selectSingleNode("height").getText());

			linkText_x = Integer.parseInt(textNode.selectSingleNode("linkText").selectSingleNode("x").getText());
			linkText_singleLine = Integer.parseInt(textNode.selectSingleNode("linkText").selectSingleNode("singleLine").getText());
			linkText_doubleLine = Integer.parseInt(textNode.selectSingleNode("linkText").selectSingleNode("doubleLine").getText());
			linkText_height = Integer.parseInt(textNode.selectSingleNode("linkText").selectSingleNode("height").getText());
			
			Node blockNode = templateNode.selectSingleNode("block");
			topReturn_x = Integer.parseInt(blockNode.selectSingleNode("topReturn").selectSingleNode("x").getText());
			topReturn_y = Integer.parseInt(blockNode.selectSingleNode("topReturn").selectSingleNode("y").getText());
			topReturn_width = Integer.parseInt(blockNode.selectSingleNode("topReturn").selectSingleNode("width").getText());
			topReturn_height = Integer.parseInt(blockNode.selectSingleNode("topReturn").selectSingleNode("height").getText());
			
			topCamera_x = Integer.parseInt(blockNode.selectSingleNode("topCamera").selectSingleNode("x").getText());
			topCamera_y = Integer.parseInt(blockNode.selectSingleNode("topCamera").selectSingleNode("y").getText());
			topCamera_width = Integer.parseInt(blockNode.selectSingleNode("topCamera").selectSingleNode("width").getText());
			topCamera_height = Integer.parseInt(blockNode.selectSingleNode("topCamera").selectSingleNode("height").getText());
			
			rightPortrait_x = Integer.parseInt(blockNode.selectSingleNode("rightPortrait").selectSingleNode("x").getText());
			rightPortrait_y = Integer.parseInt(blockNode.selectSingleNode("rightPortrait").selectSingleNode("y").getText());
			rightPortrait_width = Integer.parseInt(blockNode.selectSingleNode("rightPortrait").selectSingleNode("width").getText());
			rightPortrait_height = Integer.parseInt(blockNode.selectSingleNode("rightPortrait").selectSingleNode("height").getText());
			
			leftPortrait_x = Integer.parseInt(blockNode.selectSingleNode("leftPortrait").selectSingleNode("x").getText());
			leftPortrait_y = Integer.parseInt(blockNode.selectSingleNode("leftPortrait").selectSingleNode("y").getText());
			leftPortrait_width = Integer.parseInt(blockNode.selectSingleNode("leftPortrait").selectSingleNode("width").getText());
			leftPortrait_height = Integer.parseInt(blockNode.selectSingleNode("leftPortrait").selectSingleNode("height").getText());
			
			linkIcon_x = Integer.parseInt(blockNode.selectSingleNode("linkIcon").selectSingleNode("x").getText());
			linkIcon_y = Integer.parseInt(blockNode.selectSingleNode("linkIcon").selectSingleNode("y").getText());
			linkIcon_width = Integer.parseInt(blockNode.selectSingleNode("linkIcon").selectSingleNode("width").getText());
			linkIcon_height = Integer.parseInt(blockNode.selectSingleNode("linkIcon").selectSingleNode("height").getText());
			
			bottomFunction_x = Integer.parseInt(blockNode.selectSingleNode("bottomFunction").selectSingleNode("x").getText());
			bottomFunction_y = Integer.parseInt(blockNode.selectSingleNode("bottomFunction").selectSingleNode("y").getText());
			bottomFunction_width = Integer.parseInt(blockNode.selectSingleNode("bottomFunction").selectSingleNode("width").getText());
			bottomFunction_height = Integer.parseInt(blockNode.selectSingleNode("bottomFunction").selectSingleNode("height").getText());
			
			grayBackground_x = Integer.parseInt(blockNode.selectSingleNode("grayBackground").selectSingleNode("x").getText());
			grayBackground_y = Integer.parseInt(blockNode.selectSingleNode("grayBackground").selectSingleNode("y").getText());
			grayBackground_width = Integer.parseInt(blockNode.selectSingleNode("grayBackground").selectSingleNode("width").getText());
			grayBackground_height = Integer.parseInt(blockNode.selectSingleNode("grayBackground").selectSingleNode("height").getText());
			
			screenshotArrayList = new ArrayList<>();
			List<Node> screenshotNodes = templateNode.selectSingleNode("screenshot").selectNodes("range");
			for(Node screenshotNode : screenshotNodes) {
				screenshotArrayList.add(new int[] {Integer.parseInt(screenshotNode.selectSingleNode("top").getText()),
						Integer.parseInt(screenshotNode.selectSingleNode("bottom").getText())});
			}

			LoggerConfigureReader.logger.info("Loaded template " + templateIndex + " configure \"" + configPath + "\"");
			return true;
		} catch(DocumentException de) {
			LoggerConfigureReader.logger.warn("Failed to load configure \"" + configPath + "\"");
			LoggerConfigureReader.logger.error("Exception", de);
			return false;
		} catch(Exception e) {
			LoggerConfigureReader.logger.error("Failed while loading configure \"" + configPath + "\"");
			LoggerConfigureReader.logger.error("Exception", e);
			return false;
		}
	}
}
