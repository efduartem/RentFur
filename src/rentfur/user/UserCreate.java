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
    private final JLabel confirmPasswordLabeld;
    private final JTextField usernameTextField;
    private final JTextField fullnameTextField;
    private final JPasswordField passwordPasswordField;
    private final JPasswordField confirmPasswordField;
    private final JComboBox positionComboBox;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton saveButton;
    private final JButton cancelButton;
    
    public UserCreate(UserController userController){
        this.userController = userController;
        
        userCreatePanel = new JPanel();
        userCreatePanel.setLayout(null);

        usernameLabel = new JLabel("Nombre usuario:");
        usernameLabel.setBounds(50, 20, 120, 25);
        userCreatePanel.add(usernameLabel);
        
        usernameTextField = new JTextField();
        usernameTextField.setBounds(200, 20, 160, 25);
        userCreatePanel.add(usernameTextField);
        
        fullnameLabel = new JLabel("Nombre completo:");
        fullnameLabel.setBounds(50,50, 120, 25);
        userCreatePanel.add(fullnameLabel);
        
        fullnameTextField = new JTextField();
        fullnameTextField.setBounds(200, 50, 160, 25);
        userCreatePanel.add(fullnameTextField);
        
        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(50,80, 100, 25);
        userCreatePanel.add(passwordLabel);
        
        passwordPasswordField = new JPasswordField();
        passwordPasswordField.setBounds(200, 80, 160, 25);
        userCreatePanel.add(passwordPasswordField);
        
        confirmPasswordLabeld = new JLabel("Confirmar Contraseña:");
        confirmPasswordLabeld.setBounds(50,110, 140, 25);
        userCreatePanel.add(confirmPasswordLabeld);
        
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(200, 110, 160, 25);
        userCreatePanel.add(confirmPasswordField);
        
        positionLabel = new JLabel("Cargo:");
        positionLabel.setBounds(50,140, 100, 25);
        userCreatePanel.add(positionLabel);
        
        ComboBoxItem[] positionsComboBox = userController.getPositionForComboBox(false);
        positionComboBox = new JComboBox(positionsComboBox);
        positionComboBox.setBounds(200, 140, 160, 25);
        userCreatePanel.add(positionComboBox);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(70, 180, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        
        userCreatePanel.add(saveButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(210, 180, 120, 32);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        userCreatePanel.add(cancelButton);
        
        
        
        add(userCreatePanel);
        
        //pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Crear Usuario");
        setBounds(320,200,450,280);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    private void saveButtonAction(ActionEvent e) {
        String username = usernameTextField.getText();
        String fullname = fullnameTextField.getText();
        String password = String.valueOf(passwordPasswordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        ComboBoxItem position = (ComboBoxItem) positionComboBox.getSelectedItem();
        String positionId = "";
        if(position!=null){
            positionId = position.getKey();
        }
        
        
        HashMap mapReturn = userController.saveUser(username, fullname, password, confirmPassword, positionId);
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
        userController.setEnabledIndexView();
        userController.searchUserButtonAction();
    }
    
    @Override
    public void doDefaultCloseAction() {
        cancelButtonAction(null);
    }
}
