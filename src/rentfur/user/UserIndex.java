/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.user;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author FDuarte
 */
public class UserIndex extends JInternalFrame{
    private final UserController userController;
    private final JPanel userIndexParamsPanel;
    private final JLabel codeLabel;
    private final JLabel usernameLabel;
    private final JLabel fullnameLabel;
    private final JLabel positionLabel;
    private final JLabel statusLabel;
    private final JTextField codeTextField;
    private final JTextField usernameTextField;
    private final JTextField fullnameTextField;
    private final JComboBox positionComboBox;
    private final JComboBox statusComboBox;
    private final ImageIcon searchIconImage;
    private final JButton searchUserButton;
    private final ImageIcon createIconImage;
    private final JButton createUserButton;
    private final JTable usersResultTable;
    private DefaultTableModel usersResultDefaultTableModel;
    private final JScrollPane usersResultTableJScrollPane;
    
    public UserIndex(UserController userController){
        this.userController = userController;
        
        userIndexParamsPanel = new JPanel();
        userIndexParamsPanel.setLayout(null);
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        userIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(170, 50, 160, 25);
        userIndexParamsPanel.add(codeTextField);

        usernameLabel = new JLabel("Nombre de Usuario:");
        usernameLabel.setBounds(30, 80, 120, 25);
        userIndexParamsPanel.add(usernameLabel);
        
        usernameTextField = new JTextField();
        usernameTextField.setBounds(170, 80, 160, 25);
        userIndexParamsPanel.add(usernameTextField);
        
        fullnameLabel = new JLabel("Nombre Completo:");
        fullnameLabel.setBounds(30, 110, 110, 25);
        userIndexParamsPanel.add(fullnameLabel);
        
        fullnameTextField = new JTextField();
        fullnameTextField.setBounds(170, 110, 160, 25);
        userIndexParamsPanel.add(fullnameTextField);
        
        positionLabel = new JLabel("Cargo:");
        positionLabel.setBounds(30,140, 100, 25);
        userIndexParamsPanel.add(positionLabel);
        
        ComboBoxItem[] positionsComboBox = userController.getPositionForComboBox(true);
        positionComboBox = new JComboBox(positionsComboBox);
        positionComboBox.setBounds(170, 140, 160, 25);
        userIndexParamsPanel.add(positionComboBox);
        
        statusLabel = new JLabel("Estado:");
        statusLabel.setBounds(30, 170, 80, 25);
        userIndexParamsPanel.add(statusLabel);
        
        ComboBoxItem[] userStatus = userController.getUserStatusForComboBox();
        statusComboBox = new JComboBox(userStatus);
        statusComboBox.setBounds(170, 170, 160, 25);
        userIndexParamsPanel.add(statusComboBox);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchUserButton = new JButton("  Buscar", searchIconImage);
        searchUserButton.setBounds(40, 220, 120, 32);
        searchUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchUserButtonAction(e);
            }
        });
        userIndexParamsPanel.add(searchUserButton);
        
        //BOTON PARA CREAR MOBILIARIO
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        createUserButton = new JButton("  Crear Usuario", createIconImage);
        createUserButton.setBounds(180, 220, 180, 32);
        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getUserCreate();
            }
        });
        userIndexParamsPanel.add(createUserButton);
        
        //TABLA DE RESULTADOS
        usersResultDefaultTableModel = new usersIndextResultDefaultTableModel();
        usersResultTable = new JTable(usersResultDefaultTableModel);
        
        //Formato para celda centrada
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        userController.setUserIndexResultsTable(usersResultDefaultTableModel, false, null, null, null, null, null);
        usersResultTable.setRowHeight(22);
        
        //ID
        usersResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        usersResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        usersResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Code
        usersResultTable.getColumnModel().getColumn(1).setMaxWidth(90);
        usersResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        
        //FULLNAME
        usersResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //USERNAME
        usersResultTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        //POSITION
        usersResultTable.getColumnModel().getColumn(4).setMaxWidth(180);
        usersResultTable.getColumnModel().getColumn(4).setMinWidth(180);
        usersResultTable.getColumnModel().getColumn(4).setResizable(false);
       
        //STATUS
        usersResultTable.getColumnModel().getColumn(5).setMaxWidth(90);
        usersResultTable.getColumnModel().getColumn(5).setMinWidth(90);
        usersResultTable.getColumnModel().getColumn(5).setResizable(false);
        usersResultTable.getColumnModel().getColumn(5).setCellRenderer(statusRenderer);
        
        usersResultTable.getColumnModel().getColumn(6).setMaxWidth(90);
        usersResultTable.getColumnModel().getColumn(6).setMinWidth(90);
        usersResultTable.getColumnModel().getColumn(6).setPreferredWidth(90);
        usersResultTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        usersResultTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JTextField()));
        
        usersResultTable.getColumnModel().getColumn(7).setMaxWidth(90);
        usersResultTable.getColumnModel().getColumn(7).setMinWidth(90);
        usersResultTable.getColumnModel().getColumn(7).setPreferredWidth(90);
        usersResultTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        usersResultTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
        
        //Status
        usersResultTable.getColumnModel().getColumn(8).setMaxWidth(0);
        usersResultTable.getColumnModel().getColumn(8).setMinWidth(0);
        usersResultTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        usersResultTable.getColumnModel().getColumn(8).setResizable(false);
        
        usersResultTableJScrollPane = new JScrollPane();
        usersResultTableJScrollPane.setBounds(30, 275, 1000, 300);
        usersResultTableJScrollPane.setViewportView(usersResultTable);
        
        add(usersResultTableJScrollPane); 
        add(userIndexParamsPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Usuarios");
        setBounds(100,50,1100,650);
        setVisible(true);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        closeIndexView(null);
    }
    
    public void searchUserButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String fullname = fullnameTextField.getText();
        String username = usernameTextField.getText();
        ComboBoxItem position = (ComboBoxItem) positionComboBox.getSelectedItem();
        String positionId = "";
        if(position!=null){
            positionId = position.getKey();
        }
        
        ComboBoxItem status = (ComboBoxItem) statusComboBox.getSelectedItem();
        String userStatus = "";
        if(status!=null){
            userStatus = status.getKey();
        }
        userController.setUserIndexResultsTable(usersResultDefaultTableModel, true, code, fullname, username, positionId, userStatus);

    }
    
    private void closeIndexView(ActionEvent e) {
        this.dispose();
        userController.indexViewClosed();
    }
    
    private class usersIndextResultDefaultTableModel extends DefaultTableModel{
        
        /*@Override
        public int getRowCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getColumnCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }*/
        
        @Override
        public boolean isCellEditable(int row, int column) {
                                                    switch(column){
                                                        case 6:     return true;
                                                        case 7:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    public void getUserCreate(){
        userController.getUserCreateView();
    }
    
    public void setDisabledElements(){
        codeTextField.setEditable(false);
        fullnameTextField.setEditable(false);
        usernameTextField.setEditable(false);
        positionComboBox.setEnabled(false);
        createUserButton.setEnabled(false);
        searchUserButton.setEnabled(false);
        this.setClosable(false);
        usersResultTable.setEnabled(false);
        statusComboBox.setEnabled(false);
    }
    
    public void setEnableddElements(){
        codeTextField.setEditable(true);
        fullnameTextField.setEditable(true);
        usernameTextField.setEditable(true);
        positionComboBox.setEnabled(true);
        createUserButton.setEnabled(true);
        searchUserButton.setEnabled(true);
        this.setClosable(true);
        usersResultTable.setEnabled(true);
        statusComboBox.setEnabled(true);
    }
    
    public String updateUserStatus(int row, String label){
        Vector dataVector = (Vector) usersResultDefaultTableModel.getDataVector().get(row);
        int userId = (Integer) dataVector.get(0);
        String code = (String) dataVector.get(1);
        String username = (String) dataVector.get(3);
        boolean active = (boolean) dataVector.get(8);
        String valueToReturn = label;
        int respuesta;
        HashMap mapReturn;
        if(active){
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea inactivar el usuario {0} - {1}?", code, username),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = userController.updateUserStatus(userId, active);
                if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
                    userController.searchUserButtonAction();
                    valueToReturn = "Activar";
                }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea activar el usuario {0} - {1}?", code, username),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = userController.updateUserStatus(userId, active);
                if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
                    userController.searchUserButtonAction();
                    valueToReturn = "Inactivar";
                }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                } 
            }
        }
        
        return valueToReturn;
    }
    
    public void showUserShowAndEditView(int row){
        Vector dataVector = (Vector) usersResultDefaultTableModel.getDataVector().get(row);
        int userId = (Integer) dataVector.get(0);
        userController.getUserShowAndEditView(userId);
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
          if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
          } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
          }
          setText((value == null) ? "" : value.toString());
          return this;
        }
      }

      /**
       * @version 1.0 11/09/98
       */

     class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int row;

        private int column;
        
        public ButtonEditor(JTextField jtf) {
          super(jtf);
          button = new JButton();
          button.setOpaque(true);
          button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
          });
          this.clickCountToStart = 1;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
          label = (value == null) ? "" : value.toString();
          button.setText(label);
          isPushed = true;
          this.row = row;
          this.column = column;
          return button;
        }

        
        @Override
        public Object getCellEditorValue() {
          if (isPushed) {
                if(column==6){
                    label = updateUserStatus(row, label);
                }else if(column==7){
                    showUserShowAndEditView(row);
                }
          }
          isPushed = false;          
          return label;
        }

        @Override
        public boolean stopCellEditing() {
          isPushed = false;
          return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
      }
}
