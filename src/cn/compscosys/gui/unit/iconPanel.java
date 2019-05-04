package cn.compscosys.gui.unit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class iconPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private boolean isSetted = false;
	private Image icon = new ImageIcon().getImage();

	public iconPanel() {
		super();
		this.setPreferredSize(new Dimension(48, 48));
		this.setBackground(Color.WHITE);
	}
	
	public Image getIcon() {
		if(!isSetted) { return null; }
		return this.icon;
	}
	
	public void setIcon(String iconPath) {
		this.icon = new ImageIcon(iconPath).getImage();
		this.isSetted = true;
		this.updateUI();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.drawImage(this.icon, 0, 0, 48, 48, this);
	}
}
