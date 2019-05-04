package cn.compscosys.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import cn.compscosys.gui.unit.iconPanel;

import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class JImportFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Image icon;
	private String[][] studentInformation = null;
	
	private final JPanel contentPane = new JPanel();
	private final JPanel settingPanel = new JPanel();
	private final JPanel functionPanel = new JPanel();
	private final JLabel label_icon = new JLabel("图标：用于显示链接左侧的图片");
	private final JButton btnBrowse = new JButton("选择文件...");
	private final iconPanel iconViewer = new iconPanel();
	private final JLabel label_viewIcon = new JLabel("图标预览：");
	private final JLabel label_text = new JLabel("文本：用于显示链接右侧的文字描述");
	private final JScrollPane textScrollPane = new JScrollPane();
	private final JTextArea textArea = new JTextArea("我完成了“青年大学习”网上主题团课第四季第二期的课程，你也来试试吧");
	private final JPanel generatorPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	private final JButton getterButton = new JButton("获取班级成员信息");
	private final JButton generatorButton = new JButton("一键生成截图");
	private final JScrollPane tableScrollPane = new JScrollPane();
	private final JTable studentInfoTable = new JTable();

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
					JImportFrame frame = new JImportFrame();
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
	public JImportFrame() {
		super("“青年大学习”网上主题团课 - 微信朋友圈截图生成器");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 466);

		menuSetup();
		layoutSetup();
		actionSetup();
		
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void menuSetup() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
	}
	
	private void layoutSetup() {
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		settingPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
		settingPanel.setPreferredSize(new Dimension(250, 10));
		contentPane.add(settingPanel, BorderLayout.WEST);
		settingPanel.setLayout(new BorderLayout(0, 0));
		
		functionPanel.setPreferredSize(new Dimension(10, 120));
		functionPanel.setLayout(null);
		settingPanel.add(functionPanel, BorderLayout.NORTH);
		
		label_icon.setBounds(0, 0, 240, 15);
		btnBrowse.setBounds(10, 50, 99, 23);
		iconViewer.setBounds(192, 25, 48, 48);
		label_viewIcon.setBounds(128, 25, 60, 15);
		label_text.setBounds(0, 95, 240, 15);
		
		functionPanel.add(label_icon);
		functionPanel.add(btnBrowse);
		functionPanel.add(iconViewer);
		functionPanel.add(label_viewIcon);
		functionPanel.add(label_text);

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textScrollPane.setViewportView(textArea);
		settingPanel.add(textScrollPane, BorderLayout.CENTER);
		
		contentPane.add(generatorPanel, BorderLayout.CENTER);
		generatorPanel.setLayout(new BorderLayout(0, 0));

		buttonPanel.add(getterButton);
		buttonPanel.add(generatorButton);
		generatorPanel.add(buttonPanel, BorderLayout.NORTH);
		generatorPanel.add(tableScrollPane, BorderLayout.CENTER);
		
		String[] columnNames = new String[] {"学号", "姓名", "班级", "昵称"};
		studentInfoTable.setModel(new DefaultTableModel(new String[][] {}, columnNames) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) { return false; }
	    });
		tableScrollPane.setViewportView(studentInfoTable);
	}
	
	private void actionSetup() {
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter("图像文件 *.bmp *.jpg *.png", "bmp", "jpg", "png");
				JFileChooser jfc = new JFileChooser();
				jfc.setFileFilter(filter);
		        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int value = jfc.showOpenDialog(null);
		        if (value == JFileChooser.APPROVE_OPTION) {
		        	iconViewer.setIcon(jfc.getSelectedFile().getAbsolutePath());
		        	icon = iconViewer.getIcon();
		        }
			}
		});
		getterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JLoginDialog dlg = new JLoginDialog(null);
				dlg.setVisible(true);
				((DefaultTableModel) studentInfoTable.getModel()).setRowCount(0);
				studentInformation = dlg.getStudentInformation();
				if(studentInformation == null) { return; }
				for(String[] information : studentInformation) {
					((DefaultTableModel) studentInfoTable.getModel()).addRow(information);
				}
			}
		});
		generatorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Image icon = iconViewer.getIcon();
				if(icon == null) { JOptionPane.showMessageDialog(null, "链接图标不能为空", "警告", JOptionPane.WARNING_MESSAGE); return; }
				String text = textArea.getText();
				if(text.isEmpty()) { JOptionPane.showMessageDialog(null, "链接描述不能为空", "警告", JOptionPane.WARNING_MESSAGE); return; }
				if(studentInfoTable.getRowCount() == 0) { JOptionPane.showMessageDialog(null, "班级成员不能为空", "警告", JOptionPane.WARNING_MESSAGE); return; }
				
				JValidateDialog dlg = identityVerificationBeforeGeneration();
				if(!dlg.getCheckSolution()) { return; }
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int value = jfc.showOpenDialog(null);
		        if (value == JFileChooser.APPROVE_OPTION) { generate(jfc.getSelectedFile().getAbsolutePath(), dlg.getPasswordString()); }
			}
		});
	}
	
	private JValidateDialog identityVerificationBeforeGeneration() {
		JValidateDialog dlg = new JValidateDialog(this);
		dlg.setVisible(true);
		return dlg;
	}
	
	private void generate(String folderPath, String passwordString) {
		JGenerateDialog dlg = new JGenerateDialog(this, folderPath, passwordString);
		dlg.setInformation(icon, textArea.getText(), new ArrayList<String[]>(Arrays.asList(studentInformation)));
		dlg.setVisible(true);
		int succeededNumber = dlg.succeededNumber();
		int failedNumber = dlg.failedNumber();
		int ungeneratedNumber = studentInformation.length - succeededNumber - failedNumber;
		JOptionPane.showMessageDialog(null, "图片生成：成功" + succeededNumber + "个，失败" + failedNumber + "个，" + ungeneratedNumber
				+ "个任务被忽略", "提示", JOptionPane.WARNING_MESSAGE);
	}
}
