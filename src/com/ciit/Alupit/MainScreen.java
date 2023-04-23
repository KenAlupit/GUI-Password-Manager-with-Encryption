package com.ciit.Alupit;

import javax.swing.JFrame;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.JPasswordField;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.CipherOutputStream; 
import javax.crypto.CipherInputStream;
import javax.swing.ImageIcon;

//Creates the main screen of the program where users can add, edit, and remove passwords
public class MainScreen extends JFrame{

	private static final long serialVersionUID = 1L;
	private JFrame frmPasswordManager = this;
	private JTextField nameTextField;
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	private static final String SECRET_KEY_ARCHIVE_FILENAME = "SecretKeyArchive.dat";
	private static final String PASSWORD_ARCHIVE_FILENAME = "SerializedPasswordObject.dat";
	private ArrayList<PasswordContainer> passwordArrayList = new ArrayList<PasswordContainer>();
	private DefaultListModel<PasswordContainer> listTitle = new DefaultListModel<>(); 
	private FileOutputStream fileOut;
	private SecretKey myDesKey;
	private Cipher desCipher;
	private JButton saveBtn;
	private JButton editBtn;
	private JButton cancelBtn;
	private JList<PasswordContainer> passwordList;

	public MainScreen(){		
		
		try {
			desCipher = Cipher.getInstance("AES");
			myDesKey = null;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
		File passwordFilePath = new File(PASSWORD_ARCHIVE_FILENAME);
		File secretKeyArchiveFilePath = new File(SECRET_KEY_ARCHIVE_FILENAME);
		
		//Checks whether important files exists if not they will create the missing files
		if(!secretKeyArchiveFilePath.exists()) { 
			try {
				fileOut = new FileOutputStream(SECRET_KEY_ARCHIVE_FILENAME);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		if(!passwordFilePath.exists()) { 
			try {
				fileOut = new FileOutputStream(PASSWORD_ARCHIVE_FILENAME);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}		
		else {
			if (secretKeyArchiveFilePath.length() != 0) {
				passwordArrayList = readEncryptedObjectFromFile(PASSWORD_ARCHIVE_FILENAME);
			}
		    if (passwordArrayList == null) {
		        passwordArrayList = new ArrayList<PasswordContainer>();
		    }
		    for (int i = 0; i < passwordArrayList.size(); i++) {
		    	listTitle.addElement(passwordArrayList.get(i));  
		      }
		}

		InterfaceDesign.frameDesignInitializer(frmPasswordManager);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(34, 34, 34));
		panel.setBounds(272, 0, 487, 483);
		frmPasswordManager.getContentPane().add(panel);
		panel.setLayout(null);
		panel.setVisible(false);
		
		JLabel lblName = new JLabel("Name");
		lblName.setForeground(new Color(206, 206, 206));
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblName.setBounds(10, 58, 369, 44);
		panel.add(lblName);
		
		nameTextField = new JTextField();
		nameTextField.setForeground(Color.WHITE);
		nameTextField.setBackground(new Color(63, 63, 63));
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameTextField.setBounds(10, 103, 457, 44);
		nameTextField.setBorder(InterfaceDesign.getCustomBorderCompound());
		panel.add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(new Color(206, 206, 206));
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsername.setBounds(10, 158, 369, 44);
		panel.add(lblUsername);
		
		usernameTextField = new JTextField();
		usernameTextField.setForeground(Color.WHITE);
		usernameTextField.setBackground(new Color(63, 63, 63));
		usernameTextField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(10, 197, 457, 48);
		usernameTextField.setBorder(InterfaceDesign.getCustomBorderCompound());
		panel.add(usernameTextField);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		passwordField.setForeground(Color.WHITE);
		passwordField.setBackground(new Color(63, 63, 63));
		passwordField.setBounds(10, 299, 347, 45);
		passwordField.setBorder(InterfaceDesign.getCustomBorderCompound());
		panel.add(passwordField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(new Color(206, 206, 206));
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setBounds(10, 256, 369, 44);
		panel.add(lblPassword);
		
		//Momentarily shows the password for a few seconds before hiding the password back
		JButton showPasswordBtn = new JButton("");
		showPasswordBtn.setIcon(new ImageIcon(getClass().getResource("/icons/eye-3-512.png")));
		showPasswordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfaceDesign.showPassword(passwordField);
			}
		});
		showPasswordBtn.setBorderPainted(false);
		showPasswordBtn.setBounds(367, 299, 50, 45);
		showPasswordBtn.setBackground(new Color(34, 34, 34));
		showPasswordBtn.setOpaque(false);
		showPasswordBtn.setFocusPainted(false);
		showPasswordBtn.setIcon(InterfaceDesign.setIconSize(showPasswordBtn.getIcon()));
		panel.add(showPasswordBtn);
		
		//Deletes the currently selected item from the list and saved data
		JButton deleteBtn = new JButton("Delete");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice = CustomOptionPane.showCustomOptionDialog(frmPasswordManager, "Are you sure you want to delete this item?", "Confirmation" );
				switch (choice) {
					case JOptionPane.YES_OPTION:
						passwordArrayList.remove(passwordList.getSelectedIndex());
						listTitle.remove(passwordList.getSelectedIndex());
						writeEncryptedObjectToFile(passwordArrayList);
						passwordList.clearSelection();
						panel.setVisible(false);
					break;
			      }
				
			}
		});
		deleteBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		deleteBtn.setBounds(367, 435, 100, 33);
		deleteBtn.setForeground(new Color(226, 58, 45));
		deleteBtn.setBackground(new Color(54, 54, 54));
		deleteBtn.setBorder(InterfaceDesign.getCustomBorderCompound());
		deleteBtn.setFocusPainted(false);
		panel.add(deleteBtn);
		
		JLabel lblNewLabel_1 = new JLabel("ITEM INFORMATION");
		lblNewLabel_1.setForeground(new Color(206, 206, 206));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(10, 11, 383, 48);
		panel.add(lblNewLabel_1);
		
		//Performs the saving feature into a .dat file
		saveBtn = new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PasswordContainer passwordObj = new PasswordContainer();
				
				//If there are no selected item in the list
				//it will save all of the text fields into the appropriate file  
				if (passwordList.getSelectedIndex() == -1) {
					
					//Checks whether the name is not empty, if it is it will notify the user
					if (nameTextField.getText().length() <= 0) {
						CustomOptionPane.showCustomMessageDialog(frmPasswordManager, "Name is required");
						return;
					}
					else {
						passwordObj.setName(nameTextField.getText());
					}
					
					//Checks whether the password is not empty, if it is it will notify the user
					if (passwordField.getPassword().length <= 0) {
						CustomOptionPane.showCustomMessageDialog(frmPasswordManager, "Password is required");
						return;
					}else {
						passwordObj.setPassword(toBytes(passwordField.getPassword()));
					}
					
					passwordObj.setUsername(usernameTextField.getText());
					passwordArrayList.add(passwordObj);
					writeEncryptedObjectToFile(passwordArrayList);
					passwordArrayList = readEncryptedObjectFromFile(PASSWORD_ARCHIVE_FILENAME);
					
					//Sets the items on the list of passwords
				    for (int i = 0; i < passwordArrayList.size(); i++) {
				    	if (i == passwordArrayList.size()-1) {
				    		listTitle.addElement(passwordArrayList.get(i)); 
				    	}
				      }
				    
				    setTextFieldEditable(false);
					
					passwordList.setSelectedIndex(passwordList.getModel().getSize()-1);
					
					toggleEditSaveButton(true);
					
				}
				//If there is a selected item in the list it will update the index of the selected item instead
				else {
					//Checks whether the name is not empty, if it is it will notify the user
					if (nameTextField.getText().length() <= 0) {
						CustomOptionPane.showCustomMessageDialog(frmPasswordManager, "Name is required");
						return;
					}
					else {
						passwordArrayList.get(passwordList.getSelectedIndex()).setName(nameTextField.getText());
					}
					
					//Checks whether the password is not empty, if it is it will notify the user
					if (passwordField.getPassword().length <= 0) {
						CustomOptionPane.showCustomMessageDialog(frmPasswordManager, "Password is required");
						return;
					}else {
						passwordArrayList.get(passwordList.getSelectedIndex()).setUsername(usernameTextField.getText());
					}
					
					passwordArrayList.get(passwordList.getSelectedIndex()).setPassword(toBytes(passwordField.getPassword()));
					
					writeEncryptedObjectToFile(passwordArrayList);
					
					listTitle.set(passwordList.getSelectedIndex(), passwordArrayList.get(passwordList.getSelectedIndex()));
					
					toggleEditSaveButton(true);
				}
				
				setCancelButtonVisibility(false);
				setTextFieldEditable(false);
			}
		});
		saveBtn.setForeground(Color.WHITE);
		saveBtn.setBackground(new Color(54, 54, 54));
		saveBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		saveBtn.setBounds(10, 435, 100, 33);
		saveBtn.setBorder(InterfaceDesign.getCustomBorderCompound());
		saveBtn.setFocusPainted(false);
		panel.add(saveBtn);
		
		//Enables editing of all the text fields on a selected list item
		editBtn = new JButton("Edit");
		editBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameTextField.setEditable(true);
				usernameTextField.setEditable(true);
				passwordField.setEditable(true);
				
				toggleEditSaveButton(false);
				
				setCancelButtonVisibility(true);
			}
		});
		editBtn.setForeground(Color.WHITE);
		editBtn.setBackground(new Color(54, 54, 54));
		editBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		editBtn.setBounds(10, 435, 100, 33);
		editBtn.setVisible(false);
		editBtn.setBorder(InterfaceDesign.getCustomBorderCompound());
		editBtn.setFocusPainted(false);
		panel.add(editBtn);
		
		//Copies the password directly to the computers clipboard
		JButton copyBtn = new JButton("");
		copyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringSelection selection = new StringSelection(new String(passwordField.getPassword()));
			    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			    
			    clipboard.setContents(selection, null);
			}
		});
		
		copyBtn.setIcon(new ImageIcon(getClass().getResource("/icons/copy-512.png")));
		copyBtn.setIcon(InterfaceDesign.setIconSize(copyBtn.getIcon()));
		copyBtn.setBorderPainted(false);
		copyBtn.setBackground(new Color(34, 34, 34));
		copyBtn.setOpaque(false);
		copyBtn.setBounds(417, 299, 50, 45);
		copyBtn.setFocusPainted(false);
		panel.add(copyBtn);
		
		//Resets the text fields to its previous state and value
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleEditSaveButton(true);
				deleteBtn.setVisible(true);
				
				setTextFieldEditable(false);
				
				nameTextField.setText(passwordArrayList.get(passwordList.getSelectedIndex()).getName());
				usernameTextField.setText(passwordArrayList.get(passwordList.getSelectedIndex()).getUsername());
				passwordField.setText(new String(toChar(passwordArrayList.get(passwordList.getSelectedIndex()).getPassword())));
				
				setCancelButtonVisibility(false);
			}
		});
		cancelBtn.setForeground(Color.WHITE);
		cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cancelBtn.setBackground(new Color(54, 54, 54));
		cancelBtn.setBounds(120, 435, 100, 33);
		panel.add(cancelBtn);
		setCancelButtonVisibility(false);
		cancelBtn.setFocusPainted(false);
		cancelBtn.setBorder(InterfaceDesign.getCustomBorderCompound());
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(61, 61, 61));
		panel_1.setBounds(0, 0, 269, 483);
		frmPasswordManager.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		passwordList = new JList<PasswordContainer>();
		passwordList.setForeground(Color.WHITE);
		passwordList.setBackground(new Color(54, 54, 54));
		passwordList.setBounds(0, 0, 269, 417);
		panel_1.add(passwordList);
		passwordList.setModel(listTitle);
        passwordList.setFixedCellHeight(50);
        passwordList.setCellRenderer(new PasswordListRenderer());
		
        //Opens a new empty panel where users can add a new password
		JButton addPasswordBtn = new JButton("+");
		addPasswordBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.setVisible(true);
				deleteBtn.setVisible(false);
				
				toggleEditSaveButton(false);
				
				setTextFieldEditable(true);
				
				nameTextField.setText(null);
				usernameTextField.setText(null);
				passwordField.setText(null);
				
				passwordList.clearSelection();
			}
		});
		
		//Opens the selected item in the list
		passwordList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (passwordList.getSelectedIndex() != -1) {
					setCancelButtonVisibility(false);
					toggleEditSaveButton(true);
					
					panel.setVisible(true);
					deleteBtn.setVisible(true);
					
					setTextFieldEditable(false);
					
					nameTextField.setText(passwordArrayList.get(passwordList.getSelectedIndex()).getName());
					usernameTextField.setText(passwordArrayList.get(passwordList.getSelectedIndex()).getUsername());
					passwordField.setText(new String(toChar(passwordArrayList.get(passwordList.getSelectedIndex()).getPassword())));
				}
				
			}
		});
		addPasswordBtn.setFocusPainted(false);
		addPasswordBtn.setBackground(new Color(54, 54, 54));
		addPasswordBtn.setForeground(Color.WHITE);
		addPasswordBtn.setBorder(InterfaceDesign.getCustomBorderCompound());
		
		addPasswordBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		addPasswordBtn.setBounds(10, 428, 249, 43);
		panel_1.add(addPasswordBtn);
	}
	
	//Converts char array into a byte array
	private byte[] toBytes(char[] chars) {
		  CharBuffer charBuffer = CharBuffer.wrap(chars);
		  ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		  byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
		            byteBuffer.position(), byteBuffer.limit());
		  Arrays.fill(byteBuffer.array(), (byte) 0);
		  return bytes;
		}
	
	//Converts byte array into a char array
	private char[] toChar(byte[] bytes) {
		 ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		 CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
		 char[] chars = Arrays.copyOfRange(charBuffer.array(),
				 charBuffer.position(), charBuffer.limit());
		 Arrays.fill(charBuffer.array(), (char) 0);
		 return chars;
	}
	
	//Encrypts an ArrayList of PasswordContainer objects then saves it into a .dat file
	private void writeEncryptedObjectToFile(ArrayList<PasswordContainer> object) {
        try {
        	if (myDesKey == null) {
	            KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
	            myDesKey = keygenerator.generateKey();
	            writeSecretKeyToFile(myDesKey);
		 	}
        	desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
            SealedObject sealedObject = new SealedObject( object, desCipher);
            CipherOutputStream cipherOutputStream = new CipherOutputStream( new BufferedOutputStream( new FileOutputStream( PASSWORD_ARCHIVE_FILENAME ) ), desCipher );
            ObjectOutputStream objectOut = new ObjectOutputStream(cipherOutputStream);
            objectOut.writeObject(sealedObject);
            objectOut.close();
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	//Reads the encrypted saved ArrayList of PasswordContainer objects from a file and returns it
    @SuppressWarnings("unchecked")
	private ArrayList<PasswordContainer> readEncryptedObjectFromFile(String filepath) {
        try {
        	myDesKey = readSecretKeyFromFile(SECRET_KEY_ARCHIVE_FILENAME);
        	desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        	
        	CipherInputStream cipherInputStream = new CipherInputStream( new BufferedInputStream( new FileInputStream( PASSWORD_ARCHIVE_FILENAME ) ), desCipher );
            ObjectInputStream objectIn = new ObjectInputStream(cipherInputStream);
            SealedObject sealedObject = (SealedObject) objectIn.readObject();
            ArrayList<PasswordContainer> obj = (ArrayList<PasswordContainer>) sealedObject.getObject(desCipher);
            objectIn.close();
            
            return obj;
 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    //Saves the secret key into a .dat file so that it can be retrieved when the main screen starts
    public void writeSecretKeyToFile(Object serObj) {
        try {
            fileOut = new FileOutputStream(SECRET_KEY_ARCHIVE_FILENAME);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //Reads the saved secret key on a file and returns the secret key object
    public SecretKey readSecretKeyFromFile(String filepath) {    	 
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
 
            SecretKey obj = (SecretKey) objectIn.readObject();
 
            objectIn.close();
            return obj;
 
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    //Sets all of the text fields to either editable or not
    public void setTextFieldEditable(boolean editable) {
		nameTextField.setEditable(editable);
		usernameTextField.setEditable(editable);
		passwordField.setEditable(editable);
    }
    
    //Toggles the edit and save button as each of one of the buttons 
    //can only be visible and enabled one at a time
    public void toggleEditSaveButton(boolean editing) {
		saveBtn.setVisible(!editing);
		saveBtn.setEnabled(!editing);
		editBtn.setVisible(editing);
		editBtn.setEnabled(editing);
    }
    //Sets the cancel button visibility and if it is enabled
    public void setCancelButtonVisibility(boolean visible) {
		cancelBtn.setEnabled(visible);
		cancelBtn.setVisible(visible);
    }
}
