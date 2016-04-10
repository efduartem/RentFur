/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.user;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.border.EmptyBorder;
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
    private final ImageIcon saveIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton editButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    String[] tooltips;
    private int positionComboBoxDefaultItemIndex = 0;
            
    public UserShowAndEdit(UserController userController, final int userId){
        this.userController = userController;
        
        userShowAndEditPanel = new JPanel();
        userShowAndEditPanel.setLayout(null);
        
        HashMap usersMap = userController.getUserById(userId);        
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(50,20, 120, 25);
        userShowAndEditPanel.add(codeLabel);
        
        userTextField = new JTextField((String) usersMap.get("code"));
        userTextField.setBounds(200, 20, 160, 25);
        userTextField.setEditable(false);
        userShowAndEditPanel.add(userTextField);
        
        usernameLabel = new JLabel("Nombre usuario:");
        usernameLabel.setBounds(50,50, 120, 25);
        userShowAndEditPanel.add(usernameLabel);
        
        usernameTextField = new JTextField((String) usersMap.get("username"));
        usernameTextField.setBounds(200, 50, 160, 25);
        usernameTextField.setEditable(false);
        userShowAndEditPanel.add(usernameTextField);
        
        fullnameLabel = new JLabel("Nombre completo:");
        fullnameLabel.setBounds(50,80, 140, 25);
        userShowAndEditPanel.add(fullnameLabel);
        
        fullnameTextField = new JTextField((String) usersMap.get("fullname"));
        fullnameTextField.setBounds(200, 80, 160, 25);
        fullnameTextField.setEditable(false);
        userShowAndEditPanel.add(fullnameTextField);
        
        positionLabel = new JLabel("Cargo:");
        positionLabel.setBounds(50,110, 100, 25);
        userShowAndEditPanel.add(positionLabel);
        
        ComboBoxItem[] positionsComboBox = userController.getPositionForComboBox(false);
        ComboBoxItem positionComboBoxItem = null;
        
        tooltips = new String[positionsComboBox.length];//{ "Javanese ", "Japanese ", "Latin " };
        
        for (int i = 0; i < positionsComboBox.length; i++) {
            positionComboBoxItem = positionsComboBox[i];
            if(positionComboBoxItem.isEnable()){
                tooltips[i] = "";
            }else{
                tooltips[i] = "El Cargo no cuenta con permisos asignados";
            }
            
        }
        
        for (int i = 0; i < positionsComboBox.length; i++) {
            positionComboBoxItem = positionsComboBox[i];
            if(positionComboBoxItem.getKey().equals(usersMap.get("positionId").toString())){
                positionComboBoxDefaultItemIndex = i;
                break;
            }
        }
        
        positionComboBox = new JComboBox(positionsComboBox);
        positionComboBox.addActionListener(new ComboListener(positionComboBox));
        positionComboBox.setRenderer(new ComboRenderer());
        positionComboBox.setBounds(200, 110, 160, 25);
        positionComboBox.setEnabled(false);
        userShowAndEditPanel.add(positionComboBox);
        
        statusLabel = new JLabel("Estado");
        statusLabel.setBounds(50,140, 100, 25);
        userShowAndEditPanel.add(statusLabel);
        
        boolean status = Boolean.valueOf(usersMap.get("active").toString());
        
        statusCheckBox = new JCheckBox("", status);
        statusCheckBox.setBounds(200, 140, 160, 25);
        statusCheckBox.setEnabled(false);
        userShowAndEditPanel.add(statusCheckBox);
                
        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(50,170, 100, 25);
        userShowAndEditPanel.add(passwordLabel);
        
        newPasswordLabel = new JLabel("Nueva Contraseña:");
        newPasswordLabel.setBounds(50,170, 140, 25);
        newPasswordLabel.setVisible(false);
        userShowAndEditPanel.add(newPasswordLabel);
        
        passwordPasswordField = new JPasswordField((String) usersMap.get("password"));
        passwordPasswordField.setBounds(200, 170, 160, 25);
        passwordPasswordField.setEditable(false);
        userShowAndEditPanel.add(passwordPasswordField);
        
        confirmPasswordLabel = new JLabel("Confirmar contraseña:");
        confirmPasswordLabel.setBounds(50,200, 140, 25);
        confirmPasswordLabel.setVisible(false);
        userShowAndEditPanel.add(confirmPasswordLabel);
        
        confirmPasswordField = new JPasswordField((String) usersMap.get("password"));
        confirmPasswordField.setBounds(200, 200, 160, 25);
        confirmPasswordField.setVisible(false);
        userShowAndEditPanel.add(confirmPasswordField);
        
        
        
        editImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/edit_24x24.png"));
        editButton = new JButton("     Editar", editImageIcon);
        editButton.setBounds(110, 230, 200, 32);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonAction(e);
            }
        });
        userShowAndEditPanel.add(editButton);
        
        saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
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
        setBounds(350,150,450,350);
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
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        userController.showAndEditViewClosed();
        userController.setEnabledIndexView();
        userController.searchUserButtonAction();
    }
    
    class ComboRenderer extends JLabel implements ListCellRenderer {

        public ComboRenderer() {
          setOpaque(true);
          setBorder(new EmptyBorder(1, 1, 1, 1));
        }

        public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
          if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            if (-1 < index) {
                list.setToolTipText(tooltips[index]);
            }
          } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
          }
          if (!((ComboBoxItem) value).isEnable()) {
            setBackground(list.getBackground());
            setForeground(UIManager.getColor("Label.disabledForeground"));
          }
          setFont(list.getFont());
          setText((value == null) ? "" : value.toString());
          return this;
        }
      }
     
     class ComboListener implements ActionListener {
        JComboBox combo;

        Object currentItem;

        ComboListener(JComboBox combo) {
          this.combo = combo;
          combo.setSelectedIndex(positionComboBoxDefaultItemIndex);
          currentItem = combo.getSelectedItem();
        }

        public void actionPerformed(ActionEvent e) {
          Object tempItem = combo.getSelectedItem();
          if (!((ComboBoxItem)tempItem).isEnable()) {
            combo.setSelectedItem(currentItem);
          } else {
            currentItem = tempItem;
          }
        }
      }
}
