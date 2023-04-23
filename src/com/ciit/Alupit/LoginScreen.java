package com.ciit.Alupit;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class LoginScreen {

	private JFrame frmPasswordManagerLogin;
	private JTextField usernameTextField;
	private JLabel lblUsername;
	private JButton showPasswordBtn;
	private Timer timer;
	private JPasswordField passwordField;
	private JLabel lblTimerCounter;
	private int loginAttempts = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen window = new LoginScreen();
					window.frmPasswordManagerLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginScreen() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPasswordManagerLogin = new JFrame();
		InterfaceDesign.frameDesignInitializer(frmPasswordManagerLogin);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		loginBtn.setBackground(new Color(54, 54, 54));
		loginBtn.setForeground(Color.WHITE);
		loginBtn.setBorder(InterfaceDesign.getCustomBorderCompound());
		loginBtn.setFocusPainted(false);
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//After 5 failed login attempts the login screen will be momentarily be disabled for 15 seconds
				if (loginAttempts >= 5) {
					usernameTextField.setEnabled(false);
					passwordField.setEnabled(false);
					loginBtn.setEnabled(false);
					showPasswordBtn.setEnabled(false);
					
			        if (timer == null || !timer.isRunning()) {
			            timer = new Timer(1000, new ActionListener() {
			                int counter = 15;
			                public void actionPerformed(ActionEvent evt) {
			                    counter--;
			                    lblTimerCounter.setText("Too many failed login attempts please try again in: " + String.valueOf(counter) + " seconds");
			                    if (counter <= 0) {
			                        usernameTextField.setEnabled(true);
			                        passwordField.setEnabled(true);
			                        loginBtn.setEnabled(true);
			    					showPasswordBtn.setEnabled(true);
			                        lblTimerCounter.setText("");
			                        loginAttempts = 1;
			                        timer.stop();
			                    }
			                }
			            });
			            timer.start();		   
					 }
				}
				
				//Checks whether the username is not empty, if it is it will notify the user
				if (usernameTextField.getText().length() <= 0) {
					CustomOptionPane.showCustomMessageDialog(frmPasswordManagerLogin, "Username is required");
					return;
				}
				
				//Checks whether the password is not empty, if it is it will notify the user
				if (passwordField.getPassword().length <= 0) {
					CustomOptionPane.showCustomMessageDialog(frmPasswordManagerLogin, "Password is required");
					return;
				}
				//Checks whether the username and password is correct
				if (usernameTextField.getText().equals("user") && new String(passwordField.getPassword()).equals("admin")) {
					MainScreen mainScreen = new MainScreen();
					frmPasswordManagerLogin.setVisible(false);
					mainScreen.setVisible(true);
				}else {
					CustomOptionPane.showCustomMessageDialog(frmPasswordManagerLogin, "Username or Password is incorrect please try again");
					loginAttempts += 1;
				}
			}
		});
		loginBtn.setBounds(313, 348, 125, 33);
		frmPasswordManagerLogin.getContentPane().add(loginBtn);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setBounds(132, 224, 100, 23);
		frmPasswordManagerLogin.getContentPane().add(lblPassword);
		
		usernameTextField = new JTextField();
		usernameTextField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		usernameTextField.setForeground(Color.WHITE);
		usernameTextField.setBackground(new Color(63, 63, 63));
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(132, 141, 487, 46);
		usernameTextField.setBorder(InterfaceDesign.getCustomBorderCompound());
		frmPasswordManagerLogin.getContentPane().add(usernameTextField);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setBounds(132, 107, 150, 23);
		frmPasswordManagerLogin.getContentPane().add(lblUsername);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		passwordField.setForeground(Color.WHITE);
		passwordField.setEchoChar('•');;
		passwordField.setBackground(new Color(63, 63, 63));
		passwordField.setBounds(132, 262, 426, 46);
		passwordField.setBorder(InterfaceDesign.getCustomBorderCompound());
		frmPasswordManagerLogin.getContentPane().add(passwordField);
		
		//Momentarily shows the password for a few seconds before hiding the password back
		showPasswordBtn = new JButton("");
		showPasswordBtn.setIcon(new ImageIcon(getClass().getResource("/icons/eye-3-512.png")));
		showPasswordBtn.setIcon(InterfaceDesign.setIconSize(showPasswordBtn.getIcon()));
		showPasswordBtn.setFocusPainted(false);
		showPasswordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfaceDesign.showPassword(passwordField);
			}
		});
		showPasswordBtn.setBorderPainted(false);
		showPasswordBtn.setBounds(568, 262, 51, 46);
		showPasswordBtn.setBackground(new Color(34, 34, 34));
		showPasswordBtn.setOpaque(false);
		frmPasswordManagerLogin.getContentPane().add(showPasswordBtn);
		
		lblTimerCounter = new JLabel("");
		lblTimerCounter.setForeground(Color.WHITE);
		lblTimerCounter.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTimerCounter.setBounds(132, 392, 487, 33);
		lblTimerCounter.setHorizontalAlignment(SwingConstants.CENTER);
		frmPasswordManagerLogin.getContentPane().add(lblTimerCounter);
	}
}
