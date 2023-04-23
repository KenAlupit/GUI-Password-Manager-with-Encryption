package com.ciit.Alupit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

//Creates a custom alternative to a JOption pane  
public class CustomOptionPane {
	private static int selection;
	
	//Creates a custom option dialog pane
	public static int showCustomOptionDialog(Component parentComponent, String message, String title) {
		selection = JOptionPane.NO_OPTION;
		JPanel panel  = setOptionPanelDesign(message, "OptionPane.warningIcon");
        
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");
        
        setButtonDesign(yesButton);
        setButtonDesign(noButton);
        
        yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.getRootFrame().dispose();
				selection = JOptionPane.YES_OPTION;
			}
        });
        
        noButton.addActionListener(e -> {
				JOptionPane.getRootFrame().dispose();
				selection = JOptionPane.NO_OPTION;
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        setButtonPanelDesign(buttonPanel, panel);
        
        setCustomJDialog(parentComponent, message, title, panel);
        
        return selection;
    }
	
	//Creates a custom message dialog pane
	public static void showCustomMessageDialog(Component parentComponent, String message) {
		JPanel panel  = setOptionPanelDesign(message, "OptionPane.informationIcon");
        
        JButton okButton = new JButton("Ok");
        
        setButtonDesign(okButton);
        
        okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.getRootFrame().dispose();
			}
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        setButtonPanelDesign(buttonPanel, panel);
        
        setCustomJDialog(parentComponent, message, "Message", panel);
	}
	
	//Sets the design of the buttons
	private static void setButtonDesign(JButton button) {
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(54, 54, 54));
		button.setFont(new Font("Tahoma", Font.PLAIN, 15));
		button.setBorder(InterfaceDesign.getCustomBorderCompound());
		button.setFocusPainted(false);
	}
	
	//Sets the design of a JPanel
	private static JPanel setOptionPanelDesign(String message, String icon) {
		JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(34, 34, 34));
        
        JLabel lblMessage = new JLabel(message);
        Icon warningIcon =  UIManager.getIcon(icon);
        lblMessage.setForeground(new Color(206, 206, 206));
        lblMessage.setIcon(warningIcon);
        
        panel.add(lblMessage, BorderLayout.CENTER);
        
        return panel;
	}
	
	//Creates a custom JDialog
	private static void setCustomJDialog(Component parentComponent, String message, String title, JPanel panel) {
        JDialog dialog = new JDialog(JOptionPane.getRootFrame(), title, true);
        dialog.setContentPane(panel);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);
	}
	
	//Sets the design of the button panel
	private static void setButtonPanelDesign(JPanel buttonPanel, JPanel panel) {
        buttonPanel.setBackground(new Color(34, 34, 34));
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        panel.add(buttonPanel, BorderLayout.SOUTH);
	}
}
