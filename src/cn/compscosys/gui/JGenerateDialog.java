package cn.compscosys.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.compscosys.config.LoggerConfigureReader;
import cn.compscosys.objects.TempletValue;
import cn.compscosys.web.SqlConnecter;

import javax.swing.JProgressBar;

public class JGenerateDialog extends JDialog implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private int succeededNumber = 0;
	private int failedNumber = 0;
	
	private String folderPath;
	private String passwordString;
	private Thread analysisThread;
	
	private Image linkIcon = null;
	private String linkText = null;
	private ArrayList<String[]> studentInformation = null;

	private final JPanel contentPanel = new JPanel();
	private final JProgressBar progressBar = new JProgressBar();
	private final JButton cancelButton = new JButton("Cancel");
	
	private SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
	private TempletValue[] templetValues = new TempletValue[10];

	/**
	 * Create the dialog.
	 */
	public JGenerateDialog(JFrame parentFrame, String folderPath, String passwordString) {
		super(parentFrame, "", true);
		this.folderPath = folderPath + File.separator + df.format(new Date(System.currentTimeMillis()));
		this.passwordString = passwordString;
		
		this.setSize(450, 118);
		this.setResizable(false);
		
		layoutInit();
		actionInit();
		
		this.setLocationRelativeTo(parentFrame);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	
	private void layoutInit() {
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 10, 414, 25);
		contentPanel.add(progressBar);

		cancelButton.setBounds(179, 44, 69, 25);
		contentPanel.add(cancelButton);
		cancelButton.setActionCommand("Cancel");
	}
	
	private void actionInit() {
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				onCancel();
			}
		});
	}
	private void onCancel() {
		this.analysisThread.interrupt();
		this.dispose();
	}
	
	private boolean generate() {
		this.analysisThread = new Thread(this);
		this.analysisThread.start();
		return true;
	}
	
	/**
	 * 根据学生的学号计算出学生使用的模板编号
	 * @param studentID 学生学号
	 * @return 模板编号
	 */
	private int getTempletNumber(String studentID) {
		int templetNumber = 0;
		for(int i = 0; i < studentID.length(); i++) {
			try {
				templetNumber += Integer.valueOf(studentID.charAt(i));
			} catch(Exception e) {
				continue;
			}
		}
		return templetNumber - templetNumber / 10 * 10;
	}
	
	/**
	 * 根据模板编号（0-9）加载并返回相应的模板数据
	 * @param templateIndex 模板编号（0-9）
	 * @return 模板数据（若传入非法参数则返回null）
	 */
	private TempletValue loadTemplate(int templateIndex) {
		if(templateIndex > 9 || templateIndex < 0) { return null; }
		if(templetValues[templateIndex] == null) {
			String titleString = this.getTitle();
			this.setTitle("正在加载模板" + templateIndex);
			templetValues[templateIndex] = TempletValue.creatTempleteValue(templateIndex);

			/**
			 * 若加载模板失败，则找到一个已加载的模板并返回，同时将该模板赋值给当前模板
			 */
			if(templetValues[templateIndex] == null) {
				for(int i = 0; i < 10; i++) {
					if(templetValues[i] != null) {
						templetValues[templateIndex] = templetValues[i];
						break;
					}
				}
			}

			this.setTitle(titleString);
		}
		return templetValues[templateIndex];
	}

	/**
	 * 根据所给图像和模板数据生成朋友圈背景图
	 * 说明：生成的图像在高度上会超出设定大小1~10像素， 而宽度与设定大小相同
	 * @param image 从数据库服务器中获取的（用户上传的）原始背景图像
	 * @param targetWidth 背景图像的目标宽度
	 * @param targetHeight 背景图像的目标高度
	 * @return 生成的背景图
	 */
	private BufferedImage backgroundCrop(BufferedImage image, int targetWidth, int targetHeight) {
		targetHeight = targetHeight / 10 * 10 + 10;
		
		int cacheWidth = 0;
		int cacheHeight = 0;
		if(((double)targetWidth / targetHeight) < ((double)image.getWidth() / image.getHeight())) {
			cacheHeight = targetHeight;
			cacheWidth = image.getWidth() * cacheHeight / image.getHeight();
		}
		else {
			cacheWidth = targetWidth;
			cacheHeight = image.getHeight() * cacheWidth / image.getWidth();
		}
		
		BufferedImage cacheImage = new BufferedImage(cacheWidth, cacheHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = (Graphics2D) cacheImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, 0, 0, cacheWidth, cacheHeight, null);
		
		return cacheImage.getSubimage((cacheWidth - targetWidth) / 2, (cacheHeight - targetHeight) / 2, targetWidth, targetHeight);
	}
	
	/**
	 * 根据所给图像生成带有圆角的正方形用户头像
	 * @param image 从数据库服务器中获取的（用户上传的）原始用户头像
	 * @return 生成的用户头像
	 */
	private BufferedImage portraitCrop(BufferedImage image) {
		int targetSize = Math.min(image.getWidth(), image.getHeight());
		BufferedImage squareImage = image.getSubimage((image.getWidth() - targetSize) / 2, (image.getHeight() - targetSize) / 2, targetSize, targetSize);
		BufferedImage outputImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, targetSize, targetSize, targetSize / 13f, targetSize / 13f));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(squareImage, 0, 0, null);
        g2.dispose();
        return outputImage;
	}
	
	/**
	 * 根据画笔设置和字符串计算出文字所占的宽度（单位：像素）
	 * @param g 画笔
	 * @param text 文字
	 * @return 文字所占的宽度（单位：像素）
	 */
	private int getStringWidth(Graphics2D g, String text) {
		FontMetrics fm = g.getFontMetrics();
		return fm.stringWidth(text);
	}

	/**
	 * 绘制分行文本
	 * @param g 画笔
	 * @param text 文本
	 * @param lineHeight 行高
	 * @param maxWidth 行宽
	 * @param maxLine 最大行数
	 * @param left 左边距
	 * @param top 上边距
	 */
	private void drawString(Graphics2D g, String text, int lineHeight, int maxWidth, int maxLine, int left, int top) {
		if (text == null || text.length() == 0) return;
		text = text.replaceAll("\\n+", "\n").trim();
		FontMetrics fm = g.getFontMetrics();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			sb.append(c);
			int stringWidth = fm.stringWidth(sb.toString());
			if (c == '\n' || stringWidth > maxWidth) {
				if(c == '\n') { i += 1; }
				if (maxLine > 1) {
					g.drawString(text.substring(0, i), left, top);
					// System.out.println("双行第一行");
					drawString(g, text.substring(i), lineHeight, maxWidth, maxLine - 1, left, top + lineHeight);
				} else {
					g.drawString(text.substring(0, i - 1) + "…", left, top);
					// System.out.println("双行第二行");
				}
				return;
			}
		}
		g.drawString(text, left, top + lineHeight / 2);
		// System.out.println("单行第一行");
	}
	
	/**
	 * 将屏幕截图保存为PNG图像文件
	 * @param bufferedImage 屏幕截图
	 * @param studentName 学生姓名，作为文件名称
	 */
	private void writeImageFile(BufferedImage bufferedImage, String studentName) throws Exception {
        File outputfile = new File(this.folderPath + File.separator + studentName + ".png");
		ImageIO.write(bufferedImage, "png", outputfile);
    }
	
	/**
	 * 根据学生信息和模板数据创建屏幕截图
	 * @param currentStudent 学生信息
	 * @param tamplate 模板数据
	 * @param backgroundAndPortrait 原始素材图像
	 * @return 屏幕截图
	 */
	public BufferedImage createImageByStudent(String[] currentStudent, TempletValue tamplate, BufferedImage[] backgroundAndPortrait) {
		int stateBarHeight = tamplate.statusBarHeight;
		SimpleDateFormat topTimeDf = new SimpleDateFormat("HH:mm");
		
		BufferedImage currentImage = new BufferedImage(tamplate.mainWidth, tamplate.mainHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = (Graphics2D) currentImage.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2d.drawImage(backgroundCrop(backgroundAndPortrait[0], tamplate.mainWidth, tamplate.backgroundHeight), 0, 0, null);
		
		g2d.setColor(new Color(255, 255, 255));
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, tamplate.topTime.height));
		g2d.drawString(topTimeDf.format(new Date(System.currentTimeMillis() + (long) (1 + Math.random() * 14400000))),
				tamplate.topTime.x, tamplate.topTime.y);
		g2d.fillRect(0, tamplate.backgroundHeight + 1, tamplate.mainWidth, tamplate.mainHeight - tamplate.backgroundHeight);
		g2d.setColor(new Color(247, 247, 247));
		g2d.fillRect(tamplate.grayBackground.x, tamplate.grayBackground.y, tamplate.grayBackground.width, tamplate.grayBackground.height);
		g2d.setColor(new Color(217, 217, 217));
		g2d.drawLine(0, tamplate.bottomLine, tamplate.mainWidth, tamplate.bottomLine);
		
		g2d.drawImage(portraitCrop(backgroundAndPortrait[1]), tamplate.rightPortrait.x, tamplate.rightPortrait.y,
				tamplate.rightPortrait.width, tamplate.rightPortrait.height, null);
		g2d.drawImage(portraitCrop(backgroundAndPortrait[1]), tamplate.leftPortrait.x, tamplate.leftPortrait.y,
				tamplate.leftPortrait.width, tamplate.leftPortrait.height, null);
		g2d.drawImage(tamplate.getStatusBar(), 0, 0, null);
		
		g2d.setColor(new Color(0, 0, 0, 40));
		g2d.fillRect(0, 0, tamplate.mainWidth, stateBarHeight);
		g2d.setPaint(new GradientPaint(0, stateBarHeight, new Color(0, 0, 0, 40), 0, stateBarHeight * 4, new Color(0, 0, 0, 0), false));
		g2d.fillRect(0, stateBarHeight, tamplate.mainWidth, stateBarHeight * 3);
		
		g2d.drawImage(tamplate.getTopReturn(), tamplate.topReturn.x, tamplate.topReturn.y, tamplate.topReturn.width, tamplate.topReturn.height, null);
		g2d.drawImage(tamplate.getTopCamera(), tamplate.topCamera.x, tamplate.topCamera.y, tamplate.topCamera.width, tamplate.topCamera.height, null);
		g2d.drawImage(tamplate.getBottomFunction(), tamplate.bottomFunction.x, tamplate.bottomFunction.y,
				tamplate.bottomFunction.width, tamplate.bottomFunction.height, null);
		g2d.drawImage(this.linkIcon, tamplate.linkIcon.x, tamplate.linkIcon.y, tamplate.linkIcon.width, tamplate.linkIcon.height, null);

		g2d.setColor(new Color(255, 255, 255));
		g2d.setFont(new Font("微软雅黑", Font.BOLD, tamplate.rightNickname.height));
		g2d.drawString(currentStudent[3], tamplate.rightNickname.x - getStringWidth(g2d, currentStudent[3]), tamplate.rightNickname.y);
		
		g2d.setColor(new Color(54, 54, 54));
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, tamplate.linkText.height));
		drawString(g2d, linkText, tamplate.linkText.height - 5,
				tamplate.grayBackground.width - 2 * (tamplate.linkText.x - tamplate.grayBackground.x) + tamplate.linkIcon.width, 2,
				tamplate.linkText.x, tamplate.linkText.singleLine);
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, tamplate.studentInfo.height));
		g2d.drawString(currentStudent[2] + " " + currentStudent[1] + " ", tamplate.studentInfo.x, tamplate.studentInfo.y);
		
		g2d.setColor(new Color(128, 128, 128));
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, tamplate.bottomTime.height));
		g2d.drawString((int) (1 + Math.random() * 9) + "分钟前   ", tamplate.bottomTime.x, tamplate.bottomTime.y);
		
		g2d.setColor(new Color(82, 111, 151));
		g2d.setFont(new Font("微软雅黑", Font.BOLD, tamplate.leftNickname.height));
		g2d.drawString(currentStudent[3], tamplate.leftNickname.x, tamplate.leftNickname.y);
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, tamplate.studentInfo.height));
		g2d.drawString(currentStudent[0], tamplate.studentInfo.x + getStringWidth(g2d, currentStudent[2] + " " + currentStudent[1] + " "),
				tamplate.studentInfo.y);
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, tamplate.bottomTime.height));
		g2d.drawString("删除", tamplate.bottomTime.x + getStringWidth(g2d, (int) (1 + Math.random() * 9) + "分钟前   "), tamplate.bottomTime.y);

		g2d.dispose();
		return currentImage;
	}
	
	public void setInformation(Image linkIcon, String linkText, ArrayList<String[]> studentInformation) {
		this.linkIcon = linkIcon;
		this.linkText = linkText;
		this.studentInformation = studentInformation;
		this.progressBar.setMaximum(studentInformation.size());
	}
	
	public void setVisible(boolean visible) {
		if(linkIcon == null || linkText == null || studentInformation == null) { return; }
		if(visible) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() { public void run() { generate(); timer.cancel(); } }, 50);
		}
		super.setVisible(visible);
	}
	
	public int succeededNumber() {
		return succeededNumber;
	}
	
	public int failedNumber() {
		return failedNumber;
	}

	/**
	 * 在该文件夹中新建项目文件，并从服务器下载用户数据，然后拼接生成截图
	 * 拼接前通过取学号各位之和的最后一位，得出相应的模板序号（0-9），然后再生成，保证对相同的人每次生成的结果相同
	 * 截图时间为当前系统时间（HH:mm）随机地向前或后推移随机的时间长度
	 **/
	public void run() {
		this.setTitle("正在连接远程服务器");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			LoggerConfigureReader.logger.warn("Exception", e);
		}
		new File(folderPath).mkdirs();
		
		this.setTitle("正在从服务器下载资源并生成");
		int currentNumber = 0;
		SqlConnecter sqlConnecter = new SqlConnecter();
		if(!sqlConnecter.isLinked()) {
    		JOptionPane.showMessageDialog(null, "连接数据库时发生了未知错误，请检查网络连接或配置文件并稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
    		sqlConnecter.close();
    		this.dispose();
    		return;
    	}
		
		String[] currentStudent = null;
		Iterator<String[]> studentSelecter = studentInformation.iterator();
		
		BufferedImage currentImage = null;
		BufferedImage[] backgroundAndPortrait = null;
		TempletValue tamplate = null;
		
		Queue<String[]> untemplatedQueue = new LinkedList<>();
		boolean isQueueRendering = false;
		
		while(isQueueRendering ? (untemplatedQueue.size() != 0) : (studentSelecter.hasNext() && !Thread.currentThread().isInterrupted())) {
			currentStudent = isQueueRendering ? untemplatedQueue.remove() : studentSelecter.next();

			tamplate = loadTemplate(getTempletNumber(currentStudent[0]));
			// 若当前学生的模板加载失败，则将当前学生加入未渲染队列，待全部学生都渲染完毕再渲染
			// 若再次渲染时依旧找不到模板，则将当前学生标记为“生成失败”，并跳过当前学生向下执行
			if(tamplate == null) {
				if(isQueueRendering) {
					failedNumber++;
					progressBar.setValue(++currentNumber);
				}
				else {
					LoggerConfigureReader.logger.warn("Template 1 does not exist. Trying to use other templates instead");
					untemplatedQueue.add(currentStudent.clone());
					if(!studentSelecter.hasNext()) {
						isQueueRendering = true;
					}
				}
				continue;
			}

			backgroundAndPortrait = sqlConnecter.getPictures(currentStudent[2], passwordString, currentStudent[0]);
			// 若此变量为空则证明在读取数据库时出现异常，直接将当前学生标记为“生成失败”，并跳过当前学生向下执行
			if(backgroundAndPortrait == null) {
				failedNumber++;
				progressBar.setValue(++currentNumber);
				
				if(!(isQueueRendering || studentSelecter.hasNext())) {
					isQueueRendering = true;
				}
				continue;
			}
			
			currentImage = createImageByStudent(currentStudent, tamplate, backgroundAndPortrait);
			// 若文件写入出现错误，则直接将当前学生标记为“生成失败”，并跳过当前学生向下执行
			try {
				writeImageFile(currentImage, currentStudent[0] + currentStudent[1]);
			} catch (Exception e) {
				failedNumber++;
				LoggerConfigureReader.logger.error("Exception", e);
				progressBar.setValue(++currentNumber);
				
				if(!(isQueueRendering || studentSelecter.hasNext())) {
					isQueueRendering = true;
				}
				continue;
			}
			
			System.gc();
			progressBar.setValue(++currentNumber);
			succeededNumber++;
			
			if(!(isQueueRendering || studentSelecter.hasNext())) {
				isQueueRendering = true;
			}
		}
		
		sqlConnecter.close();
		this.dispose();
	}
}
