package com.ciit.Alupit;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

//Contains methods for designing the program
public class InterfaceDesign {
	private static Timer timer;
	
	//Returns a custom border compound
	public static Border getCustomBorderCompound() {
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		
		return compound;
	}
	
	//Resizes an icon and returns an image icon instead
    public static ImageIcon setIconSize(Icon originalIcon) {
		ImageIcon newIcon = null;
		if (originalIcon != null) {
		    Image originalImage = ((ImageIcon) originalIcon).getImage();
		    int newWidth = 35;
		    int newHeight = 35;
		    Image newImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
		    newIcon = new ImageIcon(newImage);
		}
		return newIcon;
    }
    
    //Initializes the design of a frame
    public static void frameDesignInitializer(JFrame frame) {
    	frame.setTitle("Password Manager");
    	frame.getContentPane().setBackground(new Color(34, 34, 34));
    	frame.setBackground(new Color(34, 34, 34));
    	frame.setResizable(false);
    	frame.setBounds(100, 100, 769, 515);
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	frame.getContentPane().setLayout(null);
    }
    
    //Momentarily shows the password for a few seconds before hiding the password back
    public static void showPassword(JPasswordField passwordField) {
		if (passwordField.getEchoChar() == (char)0) {
			passwordField.setEchoChar('•');
			if (timer != null && timer.isRunning()) {
                timer.stop();
            }
		}
		else {
			passwordField.setEchoChar((char)0);
			 if (timer == null || !timer.isRunning()) {
	                timer = new Timer(2000, new ActionListener() {
	                    public void actionPerformed(ActionEvent evt) {
	                        passwordField.setEchoChar('•');
	                        timer.stop();
	                    }
	                });
	                timer.setRepeats(false);
	                timer.start();
			 }
		}
    }
}
