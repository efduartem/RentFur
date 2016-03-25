/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author hp
 */
public class UserCreate extends JInternalFrame{
    private final UserController userController;
    private final JPanel userCreatePanel;
    private final JLabel usernameLabel;
    private final JLabel fullnameLabel;
    private final JLabel passwordLabel;
    private final JLabel positionLabel; 
    private final JTextField usernameTextField;
    private final JTextField fullnameTextField;
    private final JPasswordField passwordPasswordField;
    private final JComboBox positionComboBox;
    private final ImageIcon createIconImage;
    private final JButton saveButton;
    
    public UserCreate(UserController userController){
        this.userController = userController;
        
        userCreatePanel = new JPanel();
        userCreatePanel.setLayout(null);

        usernameLabel = new JLabel("Nombre usuario:");
        usernameLabel.setBounds(50,20, 100, 25);
        userCreatePanel.add(usernameLabel);
        
        usernameTextField = new JTextField();
        usernameTextField.setBounds(180, 20, 160, 25);
        userCreatePanel.add(usernameTextField);
        
        fullnameLabel = new JLabel("Nombre completo:");
        fullnameLabel.setBounds(50,50, 100, 25);
        userCreatePanel.add(fullnameLabel);
        
        fullnameTextField = new JTextField();
        fullnameTextField.setBounds(180, 50, 160, 25);
        userCreatePanel.add(fullnameTextField);
        
        passwordLabel = new JLabel("Contraseña");
        passwordLabel.setBounds(50,80, 100, 25);
        userCreatePanel.add(passwordLabel);
        
        passwordPasswordField = new JPasswordField();
        passwordPasswordField.setBounds(180, 80, 160, 25);
        userCreatePanel.add(passwordPasswordField);
        
        positionLabel = new JLabel("Cargo");
        positionLabel.setBounds(50,110, 100, 25);
        userCreatePanel.add(positionLabel);
        
        ComboBoxItem[] positionsComboBox = userController.getPositionForComboBox();
        positionComboBox = new JComboBox(positionsComboBox);
        positionComboBox.setBounds(180, 110, 160, 25);
        userCreatePanel.add(positionComboBox);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(60, 320, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        userCreatePanel.add(saveButton);
    }
    
    private void saveButtonAction(ActionEvent e) {
        String username = usernameTextField.getText();
        String fullname = fullnameTextField.getText();
        char[] password = passwordPasswordField.getPassword();
        ComboBoxItem position = (ComboBoxItem) positionComboBox.getSelectedItem();
        String positionId = "";
        if(position!=null){
            positionId = position.getKey();
        }
        
        
        HashMap mapReturn = userController.saveUser(username, fullname, password, positionId);
        if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cancelButtonAction(ActionEvent e) {
        this.dispose();
        userController.createViewClosed();
    }
}
