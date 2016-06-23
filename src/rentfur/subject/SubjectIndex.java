/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
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
import rentfur.position.PositionController;
import rentfur.util.ComboBoxItem;
import rentfur.util.NumericTextField;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class SubjectIndex extends JInternalFrame{
    private final SubjectController subjectController;
    private final JPanel subjectIndexParamsPanel;
    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel tradenameLabel;
    private final JLabel statusLabel;
    private final JLabel addressLabel;
    private final JLabel telephoneLabel;
    private final JLabel fiscalNumberLabel;
    private final JLabel cityLabel;
    private final JTextField codeTextField;
    private final JTextField nameTextField;
    private final JTextField tradenameTextField;
    private final JComboBox subjectStatusComboBox;
    private final JTextField addressTextField;
    private final JTextField telephoneTextField;
    private final NumericTextField fiscalNumberTextField;
    private final JTextField cityTextField;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
    private final ImageIcon createIconImage;
    private final JButton createButton;
    private final JTable subjectsResultTable;
    private final DefaultTableModel subjectsResultDefaultTableModel;
    private final JScrollPane subjectsResultTableJScrollPane;
    private final DecimalFormat amountFormat;
    private boolean onlyQuery = false;
    
    
    public SubjectIndex(SubjectController subjectController, UserRoles userRoles){
        this.subjectController = subjectController;
        
        if((Boolean)userRoles.getRolesMap().get(PositionController.ROLE_RF_SUBJECT)){
            onlyQuery = true;
        }
        
        subjectIndexParamsPanel = new JPanel();
        subjectIndexParamsPanel.setLayout(null);
        
        //FILA 1
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        subjectIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(160, 50, 170, 25);
        subjectIndexParamsPanel.add(codeTextField);
        
        addressLabel = new JLabel("Dirección:");
        addressLabel.setBounds(370, 50, 160, 25);
        subjectIndexParamsPanel.add(addressLabel);
        
        addressTextField = new JTextField();
        addressTextField.setBounds(440, 50, 170, 25);
        subjectIndexParamsPanel.add(addressTextField);
        
        //FILA 2
        nameLabel = new JLabel("Razón Social:");
        nameLabel.setBounds(30, 80, 100, 25);
        subjectIndexParamsPanel.add(nameLabel);
        
        nameTextField = new JTextField();
        nameTextField.setBounds(160, 80, 170, 25);
        subjectIndexParamsPanel.add(nameTextField);
        
        telephoneLabel = new JLabel("Teléfono:");
        telephoneLabel.setBounds(370, 80, 100, 25);
        subjectIndexParamsPanel.add(telephoneLabel);
        
        telephoneTextField = new JTextField();
        telephoneTextField.setBounds(440, 80, 170, 25);
        subjectIndexParamsPanel.add(telephoneTextField);
        
        //FILA 3
        tradenameLabel = new JLabel("Nombre Comercial:");
        tradenameLabel.setBounds(30, 110, 120, 25);
        subjectIndexParamsPanel.add(tradenameLabel);
        
        tradenameTextField = new JTextField();
        tradenameTextField.setBounds(160, 110, 170, 25);
        subjectIndexParamsPanel.add(tradenameTextField);
        
        fiscalNumberLabel = new JLabel("RUC / CI:");
        fiscalNumberLabel.setBounds(370, 110, 120, 25);
        subjectIndexParamsPanel.add(fiscalNumberLabel);
        
        amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        fiscalNumberTextField = new NumericTextField(20, amountFormat);
        fiscalNumberTextField.setBounds(440, 110, 170, 25);
        fiscalNumberTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                     }
                     
                     public void update(KeyEvent e){
                         String texto = fiscalNumberTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                fiscalNumberTextField.setValue(Double.valueOf(texto));
                            }
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });

        subjectIndexParamsPanel.add(fiscalNumberTextField);
        
        //FILA 4
        statusLabel = new JLabel("Activo:");
        statusLabel.setBounds(30, 140, 80, 25);
        subjectIndexParamsPanel.add(statusLabel);
        
        ComboBoxItem[] furnitureStatus = subjectController.getSubjectIsActiveForComboBox();
        subjectStatusComboBox = new JComboBox(furnitureStatus);
        subjectStatusComboBox.setBounds(160, 140, 170, 25);
        subjectIndexParamsPanel.add(subjectStatusComboBox);
        
        cityLabel = new JLabel("Ciudad:");
        cityLabel.setBounds(370, 140, 80, 25);
        subjectIndexParamsPanel.add(cityLabel);
        
        cityTextField = new JTextField();
        cityTextField.setBounds(440, 140, 170, 25);
        subjectIndexParamsPanel.add(cityTextField);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Buscar", searchIconImage);
        searchButton.setBounds(30, 190, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSubjectButtonAction(e);
            }
        });
        subjectIndexParamsPanel.add(searchButton);
        
        //BOTON PARA CREAR MOBILIARIO
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        createButton = new JButton("  Crear Cliente", createIconImage);
        createButton.setBounds(160, 190, 180, 32);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSubjectCreate();
            }
        });
        subjectIndexParamsPanel.add(createButton);
        
        if(onlyQuery){
            String message = "Su usuario solo cuenta con permiso de consultas";
            createButton.setEnabled(false);
            createButton.setToolTipText(message);
        }
        
        //TABLA DE RESULTADOS
        subjectsResultDefaultTableModel = new subjectsIndextResultDefaultTableModel();
        subjectsResultTable = new JTable(subjectsResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        subjectController.setSubjectIndexResultsTable(subjectsResultDefaultTableModel, false, null, null, null, null, null, null, null, null);
        subjectsResultTable.setRowHeight(22);
        //ID
        subjectsResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        subjectsResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        subjectsResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Code
        subjectsResultTable.getColumnModel().getColumn(1).setMaxWidth(90);
        subjectsResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        
        //Name
        subjectsResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Tradename
        subjectsResultTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        //Address
        subjectsResultTable.getColumnModel().getColumn(4).setPreferredWidth(210);
        
        //City
        subjectsResultTable.getColumnModel().getColumn(5).setMaxWidth(100);
        subjectsResultTable.getColumnModel().getColumn(5).setMinWidth(100);
        subjectsResultTable.getColumnModel().getColumn(5).setResizable(false);
        
        //Telephone
        subjectsResultTable.getColumnModel().getColumn(6).setMaxWidth(100);
        subjectsResultTable.getColumnModel().getColumn(6).setMinWidth(100);
        subjectsResultTable.getColumnModel().getColumn(6).setResizable(false);
        
        //Fiscal Number
        subjectsResultTable.getColumnModel().getColumn(7).setMaxWidth(100);
        subjectsResultTable.getColumnModel().getColumn(7).setMinWidth(100);
        subjectsResultTable.getColumnModel().getColumn(7).setResizable(false);
        subjectsResultTable.getColumnModel().getColumn(7).setCellRenderer(statusRenderer);
        
        //Activo
        subjectsResultTable.getColumnModel().getColumn(8).setMaxWidth(50);
        subjectsResultTable.getColumnModel().getColumn(8).setMinWidth(50);
        subjectsResultTable.getColumnModel().getColumn(8).setResizable(false);
        subjectsResultTable.getColumnModel().getColumn(8).setCellRenderer(statusRenderer);
        
        //BOTON Activar/Desactivar
        subjectsResultTable.getColumnModel().getColumn(9).setMaxWidth(90);
        subjectsResultTable.getColumnModel().getColumn(9).setMinWidth(90);
        subjectsResultTable.getColumnModel().getColumn(9).setPreferredWidth(90);
        subjectsResultTable.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        subjectsResultTable.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JTextField()));
        
        //BOTON Ver
        subjectsResultTable.getColumnModel().getColumn(10).setMaxWidth(60);
        subjectsResultTable.getColumnModel().getColumn(10).setMinWidth(60);
        subjectsResultTable.getColumnModel().getColumn(10).setPreferredWidth(60);
        subjectsResultTable.getColumnModel().getColumn(10).setCellRenderer(new ButtonRenderer());
        subjectsResultTable.getColumnModel().getColumn(10).setCellEditor(new ButtonEditor(new JTextField()));
        
        //Active
        subjectsResultTable.getColumnModel().getColumn(11).setMaxWidth(0);
        subjectsResultTable.getColumnModel().getColumn(11).setMinWidth(0);
        subjectsResultTable.getColumnModel().getColumn(11).setPreferredWidth(0);
        subjectsResultTable.getColumnModel().getColumn(11).setResizable(false);
        
        subjectsResultTableJScrollPane = new JScrollPane();
        subjectsResultTableJScrollPane.setBounds(30, 240, 1200, 300);
        subjectsResultTableJScrollPane.setViewportView(subjectsResultTable);
        
        add(subjectsResultTableJScrollPane);
        add(subjectIndexParamsPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Clientes");
        setBounds(100,50,1300,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    
    public void searchSubjectButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String name = nameTextField.getText();
        String tradename = tradenameTextField.getText();
        String address = addressTextField.getText();
        String city = cityTextField.getText();
        String telephone = telephoneTextField.getText();
        String fiscalNumber = fiscalNumberTextField.getText();
        fiscalNumber = fiscalNumber.replaceAll("\\.", "");
        ComboBoxItem active = (ComboBoxItem) subjectStatusComboBox.getSelectedItem();
        String subjectActive = "";
        if(active!=null){
            subjectActive = active.getKey();
        }
        subjectController.setSubjectIndexResultsTable(subjectsResultDefaultTableModel, true, code, name, tradename, address, city, telephone, subjectActive, fiscalNumber);
    }
    
    private class subjectsIndextResultDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 9:     return true;
                                                        case 10:    return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    public void getSubjectCreate(){
        subjectController.getSubjectCreateView();
    }
    
    public void setDisabledElements(){
        codeTextField.setEditable(false);
        nameTextField.setEditable(false);
        tradenameTextField.setEditable(false);
        addressTextField.setEditable(false);
        telephoneTextField.setEditable(false);
        cityTextField.setEditable(false);
        fiscalNumberTextField.setEditable(false);
        subjectStatusComboBox.setEnabled(false);
        createButton.setEnabled(false);
        searchButton.setEnabled(false);
        this.setClosable(false);
        subjectsResultTable.setEnabled(false);
    }
    
    public void setEnableddElements(){
        if(!onlyQuery){
            createButton.setEnabled(true);
        }
        codeTextField.setEditable(true);
        nameTextField.setEditable(true);
        tradenameTextField.setEditable(true);
        addressTextField.setEditable(true);
        telephoneTextField.setEditable(true);
        cityTextField.setEditable(true);
        fiscalNumberTextField.setEditable(true);
        subjectStatusComboBox.setEnabled(true);
        searchButton.setEnabled(true);
        this.setClosable(true);
        subjectsResultTable.setEnabled(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        closeIndexView(null);
    }

    
    private void closeIndexView(ActionEvent e) {
        this.dispose();
        subjectController.indexViewClosed();
    }
    
    public String updateSubjectStatus(int row, String label){
        Vector dataVector = (Vector) subjectsResultDefaultTableModel.getDataVector().get(row);
        int subjectId = (Integer) dataVector.get(0);
        String code = (String) dataVector.get(1);
        String name = (String) dataVector.get(2);
        boolean active = (boolean) dataVector.get(11);
        String valueToReturn = label;
        int respuesta;
        HashMap mapReturn;
        if(active){
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea inactivar el cliente {0} - {1}?", code, name),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = subjectController.updateSubjectStatus(subjectId, active);
                if((Integer) mapReturn.get("status") == subjectController.SUCCESFULLY_SAVED){
                    subjectController.searchSubjectButtonAction();
                    valueToReturn = "Activar";
                }else if((Integer)mapReturn.get("status") == subjectController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea activar el cliente {0} - {1}?", code, name),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = subjectController.updateSubjectStatus(subjectId, active);
                if((Integer) mapReturn.get("status") == subjectController.SUCCESFULLY_SAVED){
                    subjectController.searchSubjectButtonAction();
                    valueToReturn = "Inactivar";
                }else if((Integer)mapReturn.get("status") == subjectController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                } 
            }
        }
        
        return valueToReturn;
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
          if(column==9){
              if(onlyQuery){
                    String message = "Su usuario solo cuenta con permiso de consultas";
                    setEnabled(false);
                    setToolTipText(message);
                }
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
            if(column==9){
                if(!onlyQuery){
                    label = updateSubjectStatus(row, label);
                }
            }else if(column==10){
                showSubjectShowAndEditView(row);
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

        private void showSubjectShowAndEditView(int row) {
            Vector dataVector = (Vector) subjectsResultDefaultTableModel.getDataVector().get(row);
            int subjectId = (Integer) dataVector.get(0);
            subjectController.getSubjectShowAndEditView(subjectId);
        }
      }
}
