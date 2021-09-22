package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import displayAdmin.*;
import objects.*;
import util.*;
import myJStuff.MyController;

/**
 * Controller used to manage what the Admin can do when logged in
 * Manages all of the dislpalyAdminPanels and starting the scholarship controller as an admin
 */
public class AdminController extends MyController {
	
	// All of the panels that the controller 
	private AdminPanel ap;
	private AllStudentsPanel asp;
	private ViewStudentPanel vsp;
	private CreateScholarshipPanel csp;
	private AccountPanel acp;
	
	// Current User
	private Admin currentAdmin;
	// List of all of the scholarships
	private HashMap <Integer, Scholarship> scMap;
	
	private List<Student> students;
	
	// Create a JPanel to reference the AdminPanel class
	private JPanel adminPanel;
	private JPanel allStudentsPanel;
	private JPanel viewStudentPanel;
	private JPanel createScholarshipPanel;
	private JPanel accountPanel;
	
	// THis controls all of the dispalyScholarship panels
	private ScholarshipController sController;
	
	
	// Save and write all of the edited data to the csv files
	private Util util;
	
	
	/**
	 * Constructor
	 * @param frame - JFrame
	 * @param globalListener - ActionListener
	 */
	public AdminController(ActionListener globalListener, JFrame frame) {
		super(globalListener, frame);
		
		sController = new ScholarshipController(this,frame);
	}
	
	/**
	 * Method used to switch to this controller
	 * @param currentAdmin - Admin - current user
	 * @param scMap - HashMap<Integer, Scholarship> - list of scholarships
	 */
	public void start(Admin currentAdmin, HashMap<Integer, Scholarship> scMap, List<Student> students, Util util) {
		this.scMap = scMap;
		this.currentAdmin = currentAdmin;
		this.students = students;
		this.util = util;
		ap = new AdminPanel(this, globalListener);
		asp = new AllStudentsPanel(this);
		vsp = new ViewStudentPanel(this);
		csp = new CreateScholarshipPanel(this);
		acp = new AccountPanel(this);
		
		adminPanel = ap.getContentPane();
		allStudentsPanel = asp.getContentPane();
		viewStudentPanel = vsp.getContentPane();
		createScholarshipPanel = csp.getContentPane();
		accountPanel = acp.getContentPane();
		
		// Add all of the students to the AllStudentsPanel
		addAllStudents();
		
		// CHange the current JPanel to the admin panel
		switchToAdminPanel();
	}
	
	/**
	 * Set the correct email and name for the current admin
	 * Switch to AdminPanel
	 */
	private void switchToAdminPanel() {
		ap.setName(currentAdmin.getName());
		ap.setEmail(currentAdmin.getEmail());
		switchPanel(adminPanel);
	}
	
	/**
	 * Switch the current JPanel to the ViewStudentJPanel
	 * Update the panel to display all of the students info
	 * @param ucid - Integer - the seelcted students id number
	 */
	private void switchToViewStudent(int ucid) {
		Student s = findStudentByUcid(ucid);
		String x = scholarshipsAppliedTo(s);
		vsp.setStudent(s,x);
		switchPanel(viewStudentPanel);
	}
	
	/**
	 * Loop through the students and add each one as a row to the AllStudentsPanel
	 * Create a button to view the student and all of the scholarships that student has applied for
	 * Called at the start of the Admin Controller
	 */
	private void addAllStudents() {
		for(Student s: students) {
			asp.addStudent(s);
		}
	}
	/**
	 * Creates a String of all of the current scholarships the student has applied to
	 * @param s - Student
	 * @return - String of all of the scholarships a student has applied to
	 */
	private String scholarshipsAppliedTo(Student s) {
		String scholarships="";
		//Loop through all of the scholarships the student has applied for
		for(int i: s.getScholarshipsAppliedTo()) {
			// Check to make sure the student has applied to the scholarships
			if(scMap.get(i).getStudentsApplied().contains(s.getUCID()))
				// Add the scholarship to the name
				scholarships+=scMap.get(i).getName()+", ";
		}
		if(scholarships.equals(""))
			return "No scholarships Applied To";
		else
			return scholarships.substring(0, scholarships.length()-2);
	}
	/**
	 * Find a Student by UCID
	 * @param ucid 0 Integer
	 * @return Student
	 */
	private Student findStudentByUcid(int ucid) {
		for(Student s: students) {
			if(s.getUCID() == ucid) {
				return s;
			}
		}
		return null;
	}
	/**
	 * Deletes a scholarships and withdraw all students from that scholarships
	 * @param s - Scholarship to delete
	 */
	private void deleteScholarship(Scholarship s) {
		// Delete the scholarships
		util.deleteScholarship(s);
		// Remove the scholarships
		scMap.remove(s.getId());
		// Error handling
		if (s.getStudentsApplied()!= null) {
			// Loop through all of the students that have applied to the scholarship
			for (int i: s.getStudentsApplied()) {
				// Find the student by UCID
				Student x = findStudentByUcid(i);
				// Remove the scholarship from 
				x.removeScholarship(s.getId());
			}
		}
	}
	
	/**
	 * Saves the edits of a scholarship
	 * @param s - Scholarship to be edited;
	 * @return - true | save success - false | save failed
	 */
	private boolean saveScholarship(Scholarship s) {
		// Validate that all of the edit fields have a value
		boolean invalid = ((sController.getEdits().getName().isEmpty())||
				(sController.getEdits().getGpa()<=0 ||sController.getEdits().getGpa()>4)||
				(sController.getEdits().getDescription().isEmpty())||
				(sController.getEdits().getFaculty().isEmpty())||
				(sController.getEdits().getDepartment().isEmpty())||
				(sController.getEdits().getYearOfStudy())<=0||
				(sController.getEdits().getTypeOfStudy().isEmpty())||
				(sController.getEdits().getNumAllowed())<=0||
				(sController.getEdits().getMoney())<=0);
		// Check if the invalid
		if(!invalid){
			// If not invalid save all of the edits and return true
			s.setName(sController.getEdits().getName());
			s.setGpaRequirement(sController.getEdits().getGpa());
			s.setDescription(sController.getEdits().getDescription());
			s.setFaculty(sController.getEdits().getFaculty());
			s.setDepartment(sController.getEdits().getDepartment());
			s.setYearOfStudy(sController.getEdits().getYearOfStudy());
			s.setTypeOfStudy(sController.getEdits().getTypeOfStudy());
			s.setNumAllowed(sController.getEdits().getNumAllowed());
			s.setMoney(sController.getEdits().getMoney());
			util.saveScholarship(s);
			return true;
		}else{
			//return false
			return false;
		}
		
		
	}
	/**
	 * Create a scholarship and add it to the scMap
	 * @return - true | save success - false | save failed
	 */
	private boolean createScholarship() {
		// Generate an new blank scholarship
		Scholarship s = new Scholarship();
		
		// Check that all of the inputs are valid
		boolean invalid = ((csp.getName().isEmpty())||
				(csp.getGpa()<=0 ||csp.getGpa()>4)||
				(csp.getDescription().isEmpty())||
				(csp.getFaculty().isEmpty())||
				(csp.getDepartment().isEmpty())||
				(csp.getYearOfStudy())<=0||
				(csp.getTypeOfStudy().isEmpty())||
				(csp.getNumAllowed())<=0||
				(csp.getMoney())<=0);
		
		// If they are not invalid save all of the data and add it the scMap
		if(!invalid) {
			s.setName(csp.getName());
			s.setGpaRequirement(csp.getGpa());
			s.setDescription(csp.getDescription());
			s.setFaculty(csp.getFaculty());
			s.setDepartment(csp.getDepartment());
			s.setYearOfStudy(csp.getYearOfStudy());
			s.setTypeOfStudy(csp.getTypeOfStudy());
			s.setNumAllowed(csp.getNumAllowed());
			s.setMoney(csp.getMoney());
			int scholarshipId =0;
			for(Integer val: scMap.keySet()) {
				scholarshipId = val +1;
			}
			// Set the scholarship ID
			s.setId(scholarshipId);
			// put it tin the scMap
			scMap.put(scholarshipId, s);
			// Save it
			util.saveScholarship(s);
			return true;
		}else {
			return false;
		}
	}
	
	private void acceptStudent(Student student, Scholarship scholarship){
		if(scholarship.getNumAllowed()> scholarship.getStudentsAccepted().size()+scholarship.getStudentsWon().size()) {
			student.addToAccept(scholarship.getId());
			scholarship.addStudentToAccepted(student.getUCID());
			util.saveStudent(student);
			util.saveScholarship(scholarship);
			Object[] canApplyOptions = {"OK"};
			int canApplySelectedOption = JOptionPane.showOptionDialog(null, "Students has been accpeted to the scholarship", "SUCCESS",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
					null, canApplyOptions, canApplyOptions[0]);
			if(canApplySelectedOption == JOptionPane.YES_OPTION) {
				
			}
			sController.switchToViewScholarshipPanel(scholarship);
		}else {
			Object[] canApplyOptions = {"OK"};
			int canApplySelectedOption = JOptionPane.showOptionDialog(null, "Max number of students have been accepted", "ERROR",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE,
					null, canApplyOptions, canApplyOptions[0]);
			if(canApplySelectedOption == JOptionPane.YES_OPTION) {
				
			}
		}
	}
	
	private void switchToAccountPanel(){
		acp.setName(currentAdmin.getName());
		acp.displayStudent(currentAdmin);
		acp.resetPasswordFileds();
		switchPanel(accountPanel);
	}
	
	@Override
	/**
	 * ActionListener for when a button is pressed that is assigned to the packageController
	 */
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		String name = source.getName();
		
		// Button Name naming convention. Metod/Panel going to_the panel the button is on
		// E.g. TestPanel_HomePanel
		// switch to the TestPanel and the button was pressed on the HomePanel
		switch(name) {
		case "AllStudents_AdminPanel":
			switchPanel(allStudentsPanel);
			break;
		case "AddScholarship_AdminPanel":
			csp.clearAll();
			switchPanel(createScholarshipPanel);
			break;
		case "Create_CreateScholarshipPanel":
			if(createScholarship()) {
				sController.setStudents(students);
				sController.start(true, scMap);
			}
			break;
		case"ViewStudent_AllStudentsPanel":
			// Get the ucid of the student from the button that was pressed
			int ucid = Integer.parseInt(source.getActionCommand());
			// Find the student and switch to the ViewStudentPanel
			switchToViewStudent(ucid);
			break;
		case "Back_AllStudentsPanel":
			switchPanel(adminPanel);
			break;
		// Delete a scholarship
		case "DeleteScholarship_AllScholarshipsPanel":
			Object[] options = { "YES", "NO" };
			int selectedOption = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this scholarship?", "Warning",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]);
			if(selectedOption == JOptionPane.YES_OPTION) {
				Scholarship x = scMap.get(Integer.parseInt(source.getActionCommand()));
				deleteScholarship(x);
				sController.setStudents(students);
				sController.start(true, scMap);
			}
			
			break;
		case "Back_ViewStudentPanel":
			switchPanel(allStudentsPanel);
			break;
		case "Back_CreateScholarshipPanel":
			switchToAdminPanel();
			break;
		case "Back_AccountPanel":
			switchToAdminPanel();
			break;
		case"Back_AllScholarshipsPanel":
			// Get an updated version of all of the scholarship
			// If the current user is a student go to the student controller
			switchToAdminPanel();
			break;
		case "AllScholarships_AdminPanel":
			sController.setStudents(students);
			sController.start(true, scMap);
			break;
		case "SaveEdits_EditScholarshipPanel":
			Scholarship s = scMap.get(Integer.parseInt(source.getActionCommand()));
			if(saveScholarship(s)) {
				sController.start(true, scMap);
			}
			break;
		case "Account_AdminPanel":
			switchToAccountPanel();
			break;
		case "Accept_ViewStudentsAppliedPanel":
			String l = source.getActionCommand();
			String[]split = l.split(":");
			Student r = findStudentByUcid(Integer.parseInt(split[0]));
			Scholarship d = scMap.get(Integer.parseInt(split[1]));
			acceptStudent(r,d);
			break;
		case "UpdatePassword_AccountPanel":
			String p = acp.getNewPassword();
			String cp = acp.getConfirmPassword();
			Object[] okOption = { "OK" };
			if(p.equals("") || cp.equals("")) {
				JOptionPane.showOptionDialog(null, "Password field cannot be empty", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, okOption, okOption[0]);
			}else if(!p.equals(cp)){
				JOptionPane.showOptionDialog(null, "Passwords do not match", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, okOption, okOption[0]);
			}else if(!p.matches("[a-zA-Z0-9]*")){
				JOptionPane.showOptionDialog(null, "Password can only contain letters and numbers", "Warning",
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
						null, okOption, okOption[0]);
			}else {
				JOptionPane.showOptionDialog(null, "Your password has successfully been changed", "Success!",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
						null, okOption, okOption[0]);
				currentAdmin.setPassword(p);
				util.saveAdmin(currentAdmin);
				acp.resetPasswordFileds();
			}
			break;
		default:
			break;
		}
	}
	

}
