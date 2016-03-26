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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author hp
 */
public class UserShowAndEdit extends JInternalFrame{
    private final UserController userController;
    private final JPanel userShowAndEditPanel;
    private final JLabel codeLabel;
    private final JLabel usernameLabel;
    private final JLabel fullnameLabel;
    private final JLabel passwordLabel;
    private final JLabel positionLabel;
    private final JLabel confirmPasswordLabel;
    private final JLabel newPasswordLabel;
    private final JLabel statusLabel;
    private final JTextField userTextField;
    private final JTextField usernameTextField;
    private final JTextField fullnameTextField;
    private final JPasswordField passwordPasswordField;
    private final JPasswordField confirmPasswordField;
    private final JComboBox positionComboBox;
    private final JCheckBox statusCheckBox;
    private final ImageIcon editImageIcon;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton editButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    public UserShowAndEdit(UserController userController, final int userId){
        this.userController = userController;
        
        userShowAndEditPanel = new JPanel();
        userShowAndEditPanel.setLayout(null);
        
        HashMap usersMap = userController.getUserById(userId);        
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(50,20, 100, 25);
        userShowAndEditPanel.add(codeLabel);
        
        userTextField = new JTextField((String) usersMap.get("code"));
        userTextField.setBounds(180, 20, 160, 25);
        userTextField.setEditable(false);
        userShowAndEditPanel.add(userTextField);
        
        usernameLabel = new JLabel("Nombre usuario:");
        usernameLabel.setBounds(50,50, 100, 25);
        userShowAndEditPanel.add(usernameLabel);
        
        usernameTextField = new JTextField((String) usersMap.get("username"));
        usernameTextField.setBounds(180, 50, 160, 25);
        usernameTextField.setEditable(false);
        userShowAndEditPanel.add(usernameTextField);
        
        fullnameLabel = new JLabel("Nombre completo:");
        fullnameLabel.setBounds(50,80, 100, 25);
        userShowAndEditPanel.add(fullnameLabel);
        
        fullnameTextField = new JTextField((String) usersMap.get("fullname"));
        fullnameTextField.setBounds(180, 80, 160, 25);
        fullnameTextField.setEditable(false);
        userShowAndEditPanel.add(fullnameTextField);
        
        positionLabel = new JLabel("Cargo:");
        positionLabel.setBounds(50,110, 100, 25);
        userShowAndEditPanel.add(positionLabel);
        
        ComboBoxItem[] positionsComboBox = userController.getPositionForComboBox();
        ComboBoxItem positionComboBoxItem = null;
        for (ComboBoxItem positionsComboBoxFor : positionsComboBox) {
            positionComboBoxItem = positionsComboBoxFor;
            if(positionComboBoxItem.getKey().equals(usersMap.get("positionId").toString())){
                break;
            }
        }
        positionComboBox = new JComboBox(positionsComboBox);
        positionComboBox.setSelectedItem(positionComboBoxItem);
        positionComboBox.setBounds(180, 110, 160, 25);
        positionComboBox.setEnabled(false);
        userShowAndEditPanel.add(positionComboBox);
        
        statusLabel = new JLabel("Estado");
        statusLabel.setBounds(50,140, 100, 25);
        userShowAndEditPanel.add(statusLabel);
        
        boolean status = Boolean.valueOf(usersMap.get("active").toString());
        
        statusCheckBox = new JCheckBox("", status);
        statusCheckBox.setBounds(180, 140, 160, 25);
        statusCheckBox.setEnabled(false);
        userShowAndEditPanel.add(statusCheckBox);
                
        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(50,170, 100, 25);
        userShowAndEditPanel.add(passwordLabel);
        
        newPasswordLabel = new JLabel("Nueva Contraseña:");
        newPasswordLabel.setBounds(50,170, 100, 25);
        newPasswordLabel.setVisible(false);
        userShowAndEditPanel.add(newPasswordLabel);
        
        passwordPasswordField = new JPasswordField((String) usersMap.get("password"));
        passwordPasswordField.setBounds(180, 170, 160, 25);
        passwordPasswordField.setEditable(false);
        userShowAndEditPanel.add(passwordPasswordField);
        
        confirmPasswordLabel = new JLabel("Confirmar contraseña:");
        confirmPasswordLabel.setBounds(50,200, 110, 25);
        confirmPasswordLabel.setVisible(false);
        userShowAndEditPanel.add(confirmPasswordLabel);
        
        confirmPasswordField = new JPasswordField((String) usersMap.get("password"));
        confirmPasswordField.setBounds(180, 200, 160, 25);
        confirmPasswordField.setVisible(false);
        userShowAndEditPanel.add(confirmPasswordField);
        
        
        
        editImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        editButton = new JButton(" Editar", editImageIcon);
        editButton.setBounds(60, 230, 120, 32);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonAction(e);
            }
        });
        userShowAndEditPanel.add(editButton);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(60, 240, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e, userId);
            }
        });
        saveButton.setVisible(false);
        userShowAndEditPanel.add(saveButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(200, 240, 120, 32);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        cancelButton.setVisible(false);
        userShowAndEditPanel.add(cancelButton);
        
        add(userShowAndEditPanel);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Usuario "+usersMap.get("fullname"));
        setBounds(200,50,450,350);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    public void editButtonAction(ActionEvent e){
        usernameTextField.setEditable(true);
        fullnameTextField.setEditable(true);
        passwordPasswordField.setEditable(true);
        positionComboBox.setEnabled(true);
        statusCheckBox.setEnabled(true);
        
        newPasswordLabel.setVisible(true);
        passwordLabel.setVisible(false);
        confirmPasswordLabel.setVisible(true);
        confirmPasswordField.setVisible(true);
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
    }
    
     private void saveButtonAction(ActionEvent e, int userId) {
        String username = usernameTextField.getText();
        String fullname = fullnameTextField.getText();
        String password = String.valueOf(passwordPasswordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        boolean status = statusCheckBox.isSelected();
        ComboBoxItem position = (ComboBoxItem) positionComboBox.getSelectedItem();
        String positionId = "";
        if(position!=null){
            positionId = position.getKey();
        }
        
        
        HashMap mapReturn = userController.updateUser(userId, username, fullname, password, confirmPassword, positionId, status);
        if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
     
    private void cancelButtonAction(ActionEvent e) {
        usernameTextField.setEditable(false);
        fullnameTextField.setEditable(false);
        passwordPasswordField.setEditable(false);
        positionComboBox.setEnabled(false);
        statusCheckBox.setEnabled(false);
        
        newPasswordLabel.setVisible(false);
        passwordLabel.setVisible(true);
        confirmPasswordLabel.setVisible(false);
        confirmPasswordField.setVisible(false);
        editButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
    }
}
