package displayStudent;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import myJStuff.*;
import objects.Scholarship;
/**
 * This is the students applied to panel: you can change the priority here
 * @author Steve Khanna, Pierce de Jong
 *
 */
public class AcceptedToPanel extends MyScrollPanel{
private JButton btnBack;
	/**
	 * instance variables
	 */
	private JLabel lblSuccess;
	private int y=1;
	private JLabel lblName;

	/**
	 * class constructor
	 * @param packageListener listener for the panel
	 */
	public AcceptedToPanel(ActionListener packageListener) {
		this.packageListener = packageListener;
		contentPane.setName("Accepted To Panel");
		
		displayNorth();
		displaySouth();
		displayCenter();
	}
	
	/**
	 * displays heading of the panel
	 */
	private void displayNorth() {
		lblSuccess = new MyLabel("Scholarships you have been accepted to", Size.defaultLblSubTitleFontSize);
		north.add(lblSuccess, "cell 0 0,center");
	}
	
	/**
	 * displays attribute names
	 */
	private void displayCenter() {
		lblName = new MyLabel("Name", Colors.grey, Size.defaultLblFontSize);
		center.add(lblName,"cell 0 0");
		lblName = new MyLabel("Amount", Colors.grey, Size.defaultLblFontSize);
		center.add(lblName,"cell 1 0");
		lblName = new MyLabel("Faculty", Colors.grey, Size.defaultLblFontSize);
		center.add(lblName,"cell 2 0");
		lblName = new MyLabel("Department", Colors.grey, Size.defaultLblFontSize);
		center.add(lblName,"cell 3 0");
		
		//addScrollToCenter();
		
	}
	
	/**
	 * displays the back button
	 */
	private void displaySouth() {
		btnBack = new MyButton("Back", Size.defaultBtnFontSize,Size.defaultBtnEditWidth);
		south.add(btnBack, "cell 0 0");
		btnBack.addActionListener(packageListener);
		btnBack.setName("Back_AppliedToPanel");
	}
	
	/**
	 * remove all scholarships from the panel
	 */
	public void resetScholarships() {
		center.removeAll();
		center.revalidate();
		center.repaint();
		displayCenter();
		y = 1;
	}

	/**
	 * add scholarship successfully applied to by the student to center by priority
	 * with an option to withdraw from said scholarship
	 * @param scholarship scholarship to be added
	 * @param priority priority of said scholarship
	 */
	public void addScholarship(Scholarship scholarship, int priority) {
		
		ArrayList<String> labelName = new ArrayList<String>();
		labelName.add(scholarship.getName());
		labelName.add(scholarship.getMoney()+"");
		labelName.add(scholarship.getFaculty());
		if(scholarship.getDepartment().equals("NA")) {
			labelName.add("");
		}else {
			labelName.add(scholarship.getDepartment());			
		}
		JLabel lbl;
		int i = 0;
		for(String lblNm: labelName) {
			lbl = new MyLabel(lblNm,Colors.defaultTextColor,Size.defaultLblFontSize);
			String cellNum = Integer.toString(i);
			center.add(lbl, String.format("cell "+cellNum+" %d, left",y));
			i++;
		}
		JButton btnAccept = new MyButton("Accept", Size.defaultLblFontSize, Size.defaultBtnEditWidth+25);
		JButton btnDecline = new MyButton("Decline", Size.defaultLblFontSize, Size.defaultBtnEditWidth+25);
		center.add(btnAccept,String.format("cell 6 %d, left",y));
		center.add(btnDecline,String.format("cell 7 %d, left",y));
		btnDecline.setName("Decline_AcceptedToPanel");
		btnDecline.addActionListener(packageListener);
		btnDecline.setActionCommand(scholarship.getId()+"");
		btnAccept.setName("Accept_AcceptedToPanel");
		btnAccept.addActionListener(packageListener);
		btnAccept.setActionCommand(scholarship.getId()+"");
		y++;
	}
}
