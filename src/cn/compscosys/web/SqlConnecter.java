package cn.compscosys.web;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import cn.compscosys.config.DatabaseConfigureReader;
import cn.compscosys.config.LoggerConfigureReader;

public class SqlConnecter {
	private boolean isLinked = false;
	private Connection conn = null;
	private Statement stmt = null;
	
	public SqlConnecter() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConfigureReader.updateSettings();
			String[] databaseConfigure = DatabaseConfigureReader.getConfigure();
			conn = DriverManager.getConnection(databaseConfigure[0], databaseConfigure[1], databaseConfigure[2]);
			stmt = conn.createStatement();
			this.isLinked = true;
		} catch(Exception e) {
			LoggerConfigureReader.logger.error("Exception", e);
		}
	}
	
	public String login(String usernameString, String passwordString) {
		String sql = "SELECT className FROM classinfo WHERE classID=? AND adminPassword=?";
		try {
	        PreparedStatement preState = conn.prepareStatement(sql);
	        preState.setString(1, usernameString);
	        preState.setString(2, passwordString);
	        ResultSet rs = preState.executeQuery();
	        rs.last();
	        if(rs.getRow() != 1) {
	            return null;
	        }
	        String className = rs.getString("className");
			return className;
		} catch(SQLException se) {
        	LoggerConfigureReader.logger.error("Exception", se);
			return null;
		}
	}
	
	public String[][] getStudentInformation(String usernameString, String className) {
		String sql = "SELECT studentID, studentName, nickname FROM studentInfo WHERE classID=? "
        		+ "AND nickname is not null AND backgroundPath is not null AND portraitPath is not null";
		String[][] studentInformation = null;
		try {
			PreparedStatement preState = conn.prepareStatement(sql);
	        preState.setString(1, usernameString);
	        ResultSet rs = preState.executeQuery();
	        rs.last();
	        studentInformation = new String[rs.getRow()][4];
	        rs.first();
	        int index = 0;
	        do {
	        	studentInformation[index][0] = rs.getString("studentID");
	        	studentInformation[index][1] = rs.getString("studentName");
	        	studentInformation[index][2] = className;
	        	studentInformation[index][3] = rs.getString("nickname");
	        	index++;
	        } while(rs.next());
		} catch(SQLException se) {
        	LoggerConfigureReader.logger.error("Exception", se);
        	return null;
		}
        return studentInformation;
	}
	
	public BufferedImage[] getPictures(String usernameString, String passwordString, String studentIDString) {
		String sql = "SELECT backgroundPath, portraitPath From studentInfo WHERE classID IN "
				+ "(SELECT classID FROM classInfo WHERE className=? AND adminPassword=?) "
				+ "AND studentID=? AND nickname is not null AND backgroundPath is not null AND portraitPath is not null";
        PreparedStatement preState = null;
        ResultSet rs = null;
		try {
			preState = conn.prepareStatement(sql);
	        preState.setString(1, usernameString);
	        preState.setString(2, passwordString);
	        preState.setString(3, studentIDString);
	        rs = preState.executeQuery();
	        rs.last();
	        if(rs.getRow() != 1) {
	        	LoggerConfigureReader.logger.warn("User information is not unique.");
	        	return null;
	        }
	        rs.first();
	        
	        Blob backkgroundBlob = (Blob) rs.getBlob("backgroundPath");
	        Blob portraitBlob = (Blob) rs.getBlob("portraitPath");
	        BufferedInputStream inputImage;
	        
	        inputImage = new BufferedInputStream(backkgroundBlob.getBinaryStream());
	        BufferedImage backgroundImage = ImageIO.read(inputImage);
	        inputImage.close();
	        inputImage = new BufferedInputStream(portraitBlob.getBinaryStream());
	        BufferedImage portraitImage = ImageIO.read(inputImage);
	        inputImage.close();

	        BufferedImage background = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	        background.getGraphics().drawImage(backgroundImage, 0, 0, null);
	        BufferedImage portrait = new BufferedImage(portraitImage.getWidth(), portraitImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	        portrait.getGraphics().drawImage(portraitImage, 0, 0, null);
	        
	        return new BufferedImage[] {background, portrait};
		} catch (SQLException sqle) {
			LoggerConfigureReader.logger.error("Exception", sqle);
			return null;
		} catch (IOException ioe) {
			LoggerConfigureReader.logger.error("Exception", ioe);
			return null;
		} catch (Exception e) {
			LoggerConfigureReader.logger.error("Exception", e);
			return null;
		}
	}
	
	public boolean isLinked() {
		return this.isLinked;
	}
	
	public void close() {
		try {
			if(stmt != null) { stmt.close(); }
            if(conn != null) { conn.close(); }
		} catch (SQLException e) {
			LoggerConfigureReader.logger.error("Exception", e);
		}
		this.isLinked = false;
	}
}
