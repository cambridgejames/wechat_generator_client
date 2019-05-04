package cn.compscosys.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.compscosys.objects.TempletValue;

import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cn.compscosys.gui.JGenerateDialog;
import java.awt.Component;

public class JPictureViewerFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JGenerateDialog generateDialog = new JGenerateDialog(this, null, "123456");

	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	private final JComboBox comboBox = new JComboBox();
	private final JButton button = new JButton("生成");
	private final JScrollPane scrollPane = new JScrollPane();
	private final JLabel label = new JLabel("暂无预览", JLabel.CENTER);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JPictureViewerFrame frame = new JPictureViewerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JPictureViewerFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		this.controlInit();
		this.actionInit();
		this.generatorInit();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void controlInit() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		comboBox.setMaximumRowCount(20);
		comboBox.setModel(new DefaultComboBoxModel(new String[] {
				"模板 - 0", "模板 - 1", "模板 - 2", "模板 - 3", "模板 - 4", "模板 - 5", "模板 - 6", "模板 - 7", "模板 - 8", "模板 - 9"
			}));
		contentPane.add(comboBox, BorderLayout.NORTH);
		contentPane.add(button, BorderLayout.SOUTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		scrollPane.setViewportView(label);
	}
	
	private void actionInit() {
		this.button.addActionListener(this);
	}
	
	private void generatorInit() {
		this.generateDialog.setInformation(new ImageIcon("." + File.separator + "debug" + File.separator + "linkIcon.jpg").getImage(),
				"我完成了“青年大学习”网上主题团课第四季第二期的课程，你也来试试吧", new ArrayList<>());
	}
	
	/**
	 * 根据模板编号（0-9）加载并返回相应的模板数据
	 * @param templateIndex 模板编号（0-9）
	 * @return 模板数据（若传入非法参数则返回null）
	 */
	private TempletValue loadTemplate(int templateIndex) {
		if(templateIndex > 9 || templateIndex < 0) { return null; }
		TempletValue template = TempletValue.creatTempleteValue(templateIndex);
		return template;
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		BufferedImage viewImage = null;
		try {
			viewImage = generateDialog.createImageByStudent(new String[] {"20160000", "吴彦祖", "信科1601班", "吴彦祖的微信"},
					loadTemplate(this.comboBox.getSelectedIndex()),
					new BufferedImage[] {
							ImageIO.read(new File("." + File.separator + "debug" + File.separator + "background.jpg")),
							ImageIO.read(new File("." + File.separator + "debug" + File.separator + "portrait.jpg"))
						});
			
			int targetWidth = scrollPane.getWidth() - 10;
			int targetHeight = scrollPane.getHeight() - 10;
			int cacheWidth = 0;
			int cacheHeight = 0;
			if(((double)targetWidth / targetHeight) < ((double)viewImage.getWidth() / viewImage.getHeight())) {
				cacheWidth = targetWidth;
				cacheHeight = viewImage.getHeight() * cacheWidth / viewImage.getWidth();
			}
			else {
				cacheHeight = targetHeight;
				cacheWidth = viewImage.getWidth() * cacheHeight / viewImage.getHeight();
			}
			
			BufferedImage cacheImage = new BufferedImage(cacheWidth, cacheHeight, BufferedImage.TYPE_3BYTE_BGR); 
			Graphics2D g2d = (Graphics2D) cacheImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(viewImage.getScaledInstance(cacheWidth, cacheHeight, Image.SCALE_SMOOTH), 0, 0, cacheWidth, cacheHeight, null);
			g2d.dispose();
			
			label.setText("");
			label.setIcon(new ImageIcon(cacheImage));
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(this, "图片生成失败\r\nTemplate-ID: [" + this.comboBox.getSelectedIndex() + "]", "错误", JOptionPane.YES_NO_OPTION);
		}
	}
}
