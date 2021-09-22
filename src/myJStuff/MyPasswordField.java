package myJStuff;

import java.awt.Dimension;

import javax.swing.JPasswordField;
/**
 * Custom password field class to ensure password is plain text and not a hash
 * @author Pierce
 *
 */
public class MyPasswordField extends JPasswordField{
	
	private static final long serialVersionUID = 1L;
	
	public MyPasswordField(int size) {
		setText("");
		setFont(new MyFont(size));
		setPreferredSize(new Dimension(Size.defaultBtnWidth,Size.defaultBtnHeight));
	}
	
	public MyPasswordField(String password, int fontSize) {
		setText(password);
		setFont(new MyFont(fontSize));
		setPreferredSize(new Dimension(Size.defaultBtnWidth,Size.defaultBtnHeight));
	}
	
	public MyPasswordField(String password, int size, int length) {
		setText(password);
		setFont(new MyFont(size));
		setPreferredSize(new Dimension(Size.defaultBtnWidth,Size.defaultBtnHeight));
	}

}
