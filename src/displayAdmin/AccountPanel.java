package displayAdmin;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import myJStuff.*;
import objects.*;

public class AccountPanel extends MyPanel {
	private JButton btnBack;
	private JButton btnPassword;
	private JLabel lblName;
	private JLabel[][] lblAdmin = new JLabel[8][2];
	
	private JLabel lblPassword;
	private JLabel lblConfirmPassword;
	
	private JPasswordField fldPassword;
	private JPasswordField fldConfirmPassword;


	/**
	 * Constructor
	 * @param packageListener
	 */
	public AccountPanel(ActionListener packageListener) {
		this.packageListener = packageListener;
		contentPane.setName("Account Panel");
		
		displayNorth();
		displaySouth();
		displayCenter();
	}
	/**
	 * displayNorth method displays top of the the panel 
	 */
	private void displayNorth() {
		lblName = new MyLabel("Name", Size.defaultLblTitleFontSize);
		north.add(lblName, "cell 0 1,center");
	}
	/**
	 * displayCenter method displays middle of the the panel 
	 */
	private void displayCenter() {
	}
	
	/**
	 * Displays every student in the csv file
	 * @param admin - accessing the csv
	 */
	public void displayStudent(Admin admin) {
		center.removeAll();
		center.repaint();
		lblAdmin[0][0] = new MyLabel("UCID:", Size.defaultLblFontSize);
		lblAdmin[1][0] = new MyLabel("Email:", Size.defaultLblFontSize);
		lblAdmin[0][1] = new MyLabel(""+admin.getUCID(), Size.defaultLblFontSize);
		lblAdmin[1][1] = new MyLabel(admin.getEmail(), Size.defaultLblFontSize);
		for (int i = 0; i<2; i++) {
			center.add(lblAdmin[i][0], String.format("cell %d %d, right",0,i));
			center.add(lblAdmin[i][1], String.format("cell %d %d, left",1,i));
		}
		
		lblPassword = new MyLabel("New Password",Size.defaultLblFontSize);
		center.add(lblPassword,"cell 0 7, right");
		fldPassword = new MyPasswordField("",Size.defaultLblFontSize);
		center.add(fldPassword, "cell 1 7,span");
		lblConfirmPassword = new MyLabel("Confirm Password",Size.defaultLblFontSize);
		center.add(lblConfirmPassword,"cell 0 8,right");
		fldConfirmPassword = new MyPasswordField("",Size.defaultLblFontSize);
		center.add(fldConfirmPassword, "cell 1 8,span");
		
	}
	/**
	 * Deprecated method to get the new Password: in plaintext
	 * @return
	 */
	public String getNewPassword() {
		return fldPassword.getText();
	}
	/**
	 * Deprecated method to get the Password: in plaintext
	 * @return
	 */
	public String getConfirmPassword() {
		return fldConfirmPassword.getText();
	}
	
	public void resetPasswordFileds() {
		fldPassword.setText("");
		fldConfirmPassword.setText("");
	}
	/**
	 * displaySouth method displays bottom of the the panel 
	 */
	private void displaySouth() {
		btnBack = new MyButton("Back", Size.defaultBtnFontSize,Size.defaultBtnEditWidth);
		south.add(btnBack, "cell 0 0");
		btnBack.addActionListener(packageListener);
		btnBack.setName("Back_AccountPanel");
		btnPassword = new MyButton("Update Password", Size.defaultBtnFontSize,Size.defaultBtnEditWidth);
		south.add(btnPassword, "cell 1 0, right");
		btnPassword.addActionListener(packageListener);
		btnPassword.setName("UpdatePassword_AccountPanel");
	}
	
	public void setName(String name) {
		lblName.setText(name);
	}
	
}
