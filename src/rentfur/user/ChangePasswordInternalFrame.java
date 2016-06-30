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
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class ChangePasswordInternalFrame extends JInternalFrame{
    private final UserController userController;
    private final JPanel userCreatePanel;
    private final JLabel usernameLabel;
    private final JLabel currentPasswordLabel;
    private final JLabel passwordLabel;
    private final JLabel confirmPasswordLabeld;
    private final JTextField usernameTextField;
    private final JPasswordField currentPasswordPasswordTextField;
    private final JPasswordField passwordPasswordField;
    private final JPasswordField confirmPasswordField;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton saveButton;
    private final JButton cancelButton;
    private UserRoles userRoles;
    
    public ChangePasswordInternalFrame(UserController userController){
        this.userController = userController;
        
        userRoles = new UserRoles();
        
        userCreatePanel = new JPanel();
        userCreatePanel.setLayout(null);

        usernameLabel = new JLabel("Nombre usuario:");
        usernameLabel.setBounds(50, 20, 120, 25);
        userCreatePanel.add(usernameLabel);
        
        usernameTextField = new JTextField(userRoles.getUser().getFullName());
        usernameTextField.setBounds(200, 20, 160, 25);
        usernameTextField.setEnabled(false);
        userCreatePanel.add(usernameTextField);
        
        currentPasswordLabel = new JLabel("Contraseña Actual:");
        currentPasswordLabel.setBounds(50,50, 120, 25);
        userCreatePanel.add(currentPasswordLabel);
        
        currentPasswordPasswordTextField = new JPasswordField();
        currentPasswordPasswordTextField.setBounds(200, 50, 160, 25);
        userCreatePanel.add(currentPasswordPasswordTextField);
        
        passwordLabel = new JLabel("Nueva Contraseña:");
        passwordLabel.setBounds(50,80, 160, 25);
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

        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", createIconImage);
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
                doDefaultCloseAction();
            }
        });
        userCreatePanel.add(cancelButton);

        add(userCreatePanel);
        
        //pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(true);
        setClosable(true);
        setTitle("Cambiar contraseña");
        setBounds(650,280,450,280);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        userController.changeUserPasswordViewClosed();
    }
    
    private void saveButtonAction(ActionEvent e) {
        String currentPassword = String.valueOf(currentPasswordPasswordTextField.getPassword());
        String newPassword = String.valueOf(passwordPasswordField.getPassword());
        String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
        if(!currentPassword.equals(userRoles.getUser().getPassWord())){
            JOptionPane.showMessageDialog(null, "La contraseña actual no es la correcta", "Atencion", JOptionPane.WARNING_MESSAGE);
            currentPasswordPasswordTextField.setText("");
        }else if(!newPassword.equals(confirmPassword)){
            JOptionPane.showMessageDialog(null, "Las contraseñas ingresadas no coinciden", "Atencion", JOptionPane.WARNING_MESSAGE);
            passwordPasswordField.setText("");
            confirmPasswordField.setText("");
        }else{
            HashMap mapReturn = userController.changePassword(userRoles.getUser().getId(), currentPassword, newPassword, confirmPassword);
            if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
            }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
