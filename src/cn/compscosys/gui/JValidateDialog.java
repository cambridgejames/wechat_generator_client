package cn.compscosys.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.border.LineBorder;

import cn.compscosys.objects.ValidateCode;

import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;

public class JValidateDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private boolean checkSolution = false;
	private String code = "";

	private final JPanel contentPanel = new JPanel();
	private final JLabel label = new JLabel("管理员密码");
	private final JTextField textField = new JTextField();
	private final JLabel validateCodeLabel = new JLabel("点击刷新验证码");
	private final JPanel buttonPane = new JPanel();
	private final JButton okButton = new JButton("OK");
	private final JButton cancelButton = new JButton("Cancel");
	private final JLabel label_1 = new JLabel("验证码（不区分大小写）");
	private final JPasswordField passwordField = new JPasswordField();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			JValidateDialog dialog = new JValidateDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public JValidateDialog(JFrame parentFrame) {
		super(parentFrame, "身份验证", true);
		this.setSize(317, 204);

		layoutInit();
		actionInit();
		this.setLocationRelativeTo(parentFrame);
	}
	
	private void layoutInit() {
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		label.setBounds(5, 5, 300, 15);
		contentPanel.add(label);

		textField.setBounds(5, 93, 180, 30);
		contentPanel.add(textField);
		textField.setPreferredSize(new Dimension(7, 30));
		textField.setColumns(10);

		validateCodeLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		validateCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		validateCodeLabel.setBorder(new LineBorder(UIManager.getColor("TextField.shadow")));
		validateCodeLabel.setBounds(195, 93, 100, 30);
		contentPanel.add(validateCodeLabel);
		validateCodeLabel.setPreferredSize(new Dimension(110, 30));
		label_1.setBounds(5, 70, 300, 15);
		
		contentPanel.add(label_1);
		passwordField.setBounds(5, 30, 290, 30);
		
		contentPanel.add(passwordField);

		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		okButton.setPreferredSize(new Dimension(70, 23));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		cancelButton.setPreferredSize(new Dimension(70, 23));
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}
	
	private void actionInit() {
		validateCodeLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				changeValidateCode(false);
			}
		});
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	private void changeValidateCode() { changeValidateCode(true); }
	private void changeValidateCode(boolean b) {
		validateCodeLabel.setText("");
		ValidateCode validateCode = new ValidateCode(100, 30, 4, 100);
		this.validateCodeLabel.setIcon(new ImageIcon(validateCode.getBuffImg()));
		this.code = validateCode.getCode();
	}
	
	private boolean checkCaseInsensiive(String codeInput) { return codeInput.toLowerCase().equals(code.toLowerCase()); }
	
	public boolean getCheckSolution() {
		return this.checkSolution;
	}
	
	public String getPasswordString() {
		return String.valueOf(passwordField.getPassword());
	}

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if(cmd.equals("OK")) {
			String checkingString = textField.getText();
			if(checkingString.isEmpty()) {
				JOptionPane.showMessageDialog(this, "验证码不能为空", "警告", JOptionPane.WARNING_MESSAGE);
				changeValidateCode();
				return;
			}
			else if(!this.checkCaseInsensiive(checkingString)) {
				JOptionPane.showMessageDialog(this, "验证码错误", "警告", JOptionPane.WARNING_MESSAGE);
				changeValidateCode();
				return;
			}
			this.checkSolution = true;
		}
		this.dispose();
	}
}
