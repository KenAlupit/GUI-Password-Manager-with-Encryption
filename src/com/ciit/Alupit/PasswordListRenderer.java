package com.ciit.Alupit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

//Creates the custom list cell renderer for the JList
public class PasswordListRenderer extends JPanel implements ListCellRenderer<PasswordContainer>{
	private static final long serialVersionUID = 1L;
	private JLabel lbName = new JLabel();
	private JLabel lbUsername = new JLabel();
	
	public PasswordListRenderer() {
		setLayout(new BorderLayout(5, 5));
		
        JPanel panelText = new JPanel(new GridLayout(0, 1));
        panelText.add(lbName);
        panelText.add(lbUsername);
        add(panelText, BorderLayout.CENTER);
	}
	
	//Initializes the design of the list cell renderer and returns itself
	@Override
	public Component getListCellRendererComponent(JList<? extends PasswordContainer> list, PasswordContainer passwordContainer, int index,
            boolean isSelected, boolean cellHasFocus) {
		
		lbName.setText(passwordContainer.getName());
		lbUsername.setText(passwordContainer.getUsername());
		
		lbName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lbUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		lbName.setForeground(Color.WHITE);
		lbUsername.setForeground(new Color(206, 206, 206));
		
		lbName.setBorder(new EmptyBorder(5, 10, 0, 0));
		lbUsername.setBorder(new EmptyBorder(0, 10, 0, 0));
		
		lbName.setOpaque(true);
		lbUsername.setOpaque(true);
		
		if(isSelected) {
			lbName.setBackground(new Color(60, 60, 60));
			lbUsername.setBackground(new Color(60, 60, 60));
		}else {
	        lbName.setBackground(new Color(54, 54, 54));
	        lbUsername.setBackground(new Color(54, 54, 54));;
	    }
		
        return this;
	}

}
