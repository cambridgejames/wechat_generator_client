package cn.compscosys.gui;

import cn.compscosys.objects.ValidateCode;
import cn.compscosys.web.SqlConnecter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class JLoginDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	private String className;
	private String code = "";
	private String[][] studentInformation = null;
	
	private final JPanel contentPanel = new JPanel();
	private JLabel label = new JLabel("班级序号");
	private JTextField username = new JTextField();
	private JLabel label_1 = new JLabel("管理员密码");
	private JPasswordField password = new JPasswordField();
	private JPanel buttonPane = new JPanel();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private final JPanel panel = new JPanel();
	private final JLabel label_2 = new JLabel("验证码（不区分大小写）");
	private final JLabel validateCodeLabel = new JLabel("点击刷新验证码");
	private final JTextField validateCodeField = new JTextField();

	/**
	 * Create the dialog.
	 */
	public JLoginDialog(JFrame frame) {
		super(frame, "登录数据库", true);
		this.setSize(315, 253);
		this.setResizable(false);
		
		layoutInit();
		actionInit();
		
		this.setLocationRelativeTo(frame);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
	private void layoutInit() {
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		label.setBounds(5, 0, 289, 20);
		
		contentPanel.add(label);
		username.setBounds(5, 25, 289, 30);
		contentPanel.add(username);
		label_1.setBounds(5, 55, 289, 30);
		contentPanel.add(label_1);
		password.setBounds(5, 85, 289, 30);
		contentPanel.add(password);
		username.setColumns(10);
		label_2.setBounds(5, 115, 289, 30);
		
		contentPanel.add(label_2);
		panel.setBounds(5, 145, 289, 30);
		
		contentPanel.add(panel);
		panel.setLayout(null);

		validateCodeField.setBounds(0, 0, 179, 30);
		validateCodeField.setColumns(10);
		validateCodeLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		validateCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		validateCodeLabel.setBorder(new LineBorder(UIManager.getColor("TextField.shadow")));
		validateCodeLabel.setBounds(189, 0, 100, 30);
		
		panel.add(validateCodeField);
		panel.add(validateCodeLabel);

		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		okButton.setPreferredSize(new Dimension(70, 23));

		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		cancelButton.setPreferredSize(new Dimension(70, 23));

		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);
	}
	
	private void actionInit() {
		validateCodeLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				changeValidateCode(false);
			}
		});
	}
	
	private void changeValidateCode() { changeValidateCode(true); }
	private void changeValidateCode(boolean b) {
		validateCodeLabel.setText("");
		ValidateCode validateCode = new ValidateCode(100, 30, 4, 100);
		this.validateCodeLabel.setIcon(new ImageIcon(validateCode.getBuffImg()));
		this.code = validateCode.getCode();
		if(b) {
			password.setText("");
			validateCodeField.setText("");
		}
	}
	
	private boolean checkCaseInsensiive(String codeInput) { return codeInput.toLowerCase().equals(code.toLowerCase()); }
	
	public String getClassName() {
		return this.className;
	}
	
	public String[][] getStudentInformation() {
		return this.studentInformation;
	}

	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource() == okButton) {
			String usernameString = username.getText();
			String passwordString = String.valueOf(password.getPassword());
			if(usernameString.isEmpty() || passwordString.isEmpty()) {
				JOptionPane.showMessageDialog(this, "用户名或密码不能为空", "警告", JOptionPane.WARNING_MESSAGE);
				changeValidateCode();
				return;
			}
			String checkingString = validateCodeField.getText();
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
			
        	SqlConnecter sqlConnecter = new SqlConnecter();
        	if(!sqlConnecter.isLinked()) {
        		JOptionPane.showMessageDialog(null, "连接数据库时发生了未知错误，请检查网络连接或配置文件并稍后重试", "错误", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	className = sqlConnecter.login(usernameString, passwordString);
            if(className == null) {
	            JOptionPane.showMessageDialog(this, "用户名或密码错误", "警告", JOptionPane.WARNING_MESSAGE);
				changeValidateCode();
	            return;
            }

            studentInformation = sqlConnecter.getStudentInformation(usernameString, className);
            sqlConnecter.close();
	        this.dispose();
		}
		else if(ae.getSource() == cancelButton) {
			this.dispose();
		}
	}
}
