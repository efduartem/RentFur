/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author FDuarte
 */
public class SubjectIndex extends JInternalFrame{
    private final SubjectController subjectController;
    private final JPanel subjectIndexParamsPanel;
    //private final JPanel furnitureIndexResultPanel;
    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel statusLabel;
    private final JTextField codeTextField;
    private final JTextField nameTextField;
    private final JComboBox subjectStatusComboBox;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
    private final ImageIcon createIconImage;
    private final JButton createButton;
    private final JTable subjectsResultTable;
    private DefaultTableModel subjectsResultDefaultTableModel;
    private final JScrollPane subjectsResultTableJScrollPane;
    
    
    public SubjectIndex(SubjectController subjectController){
        this.subjectController = subjectController;
        
        subjectIndexParamsPanel = new JPanel();
        subjectIndexParamsPanel.setLayout(null);
        //furnitureIndexResultPanel = new JPanel(new BorderLayout());
        
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        subjectIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(120, 50, 160, 25);
        subjectIndexParamsPanel.add(codeTextField);

        nameLabel = new JLabel("Razón Social:");
        nameLabel.setBounds(30, 80, 80, 25);
        subjectIndexParamsPanel.add(nameLabel);
        
        nameTextField = new JTextField();
        nameTextField.setBounds(120, 80, 160, 25);
        subjectIndexParamsPanel.add(nameTextField);
        
        statusLabel = new JLabel("Activo:");
        statusLabel.setBounds(30, 140, 80, 25);
        subjectIndexParamsPanel.add(statusLabel);
        
        ComboBoxItem[] furnitureStatus = subjectController.getSubjectIsActiveForComboBox();
        subjectStatusComboBox = new JComboBox(furnitureStatus);
        subjectStatusComboBox.setBounds(120, 140, 160, 25);
        subjectIndexParamsPanel.add(subjectStatusComboBox);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Buscar", searchIconImage);
        searchButton.setBounds(30, 190, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //searchFurnitureButtonAction(e);
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
        
        //TABLA DE RESULTADOS
        subjectsResultDefaultTableModel = new subjectsIndextResultDefaultTableModel();
        subjectsResultTable = new JTable(subjectsResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        subjectController.setSubjectIndexResultsTable(subjectsResultDefaultTableModel, false, null, null);
        subjectsResultTable.setRowHeight(22);
        //ID
        subjectsResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        subjectsResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        subjectsResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Code
        subjectsResultTable.getColumnModel().getColumn(1).setMaxWidth(90);
        subjectsResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        
        //Description
        subjectsResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Familia
        /*furnituresResultTable.getColumnModel().getColumn(3).setMaxWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setMinWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setResizable(false);*/
        
        //UnitPrice
        /*furnituresResultTable.getColumnModel().getColumn(4).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(4).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(4).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);*/
        
        //UnitCostPrice
        /*furnituresResultTable.getColumnModel().getColumn(5).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(5).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(5).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);*/
        
        //FineAmount
        /*furnituresResultTable.getColumnModel().getColumn(6).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(6).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(6).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        //Stock Total
        furnituresResultTable.getColumnModel().getColumn(7).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(7).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(7).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        
        furnituresResultTable.getColumnModel().getColumn(8).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(8).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(8).setPreferredWidth(90);
        furnituresResultTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        furnituresResultTable.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JTextField()));*/
        
        subjectsResultTableJScrollPane = new JScrollPane();
        subjectsResultTableJScrollPane.setBounds(30, 240, 850, 300);
        subjectsResultTableJScrollPane.setViewportView(subjectsResultTable);
        
        add(subjectsResultTableJScrollPane);
        add(subjectIndexParamsPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Clientes");
        setBounds(200,50,950,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    
    public void searchFurnitureButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String name = nameTextField.getText();
        /*ComboBoxItem status = (ComboBoxItem) subjectStatusComboBox.getSelectedItem();
        String furnitureStatus = "";
        if(status!=null){
            furnitureStatus = status.getKey();
        }*/
        subjectController.setSubjectIndexResultsTable(subjectsResultDefaultTableModel, true, code, name);
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
                                                        //case 8:     return true;
                                                        //case 4:     return true;
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
        createButton.setEnabled(false);
        searchButton.setEnabled(false);
        this.setClosable(false);
        subjectsResultTable.setEnabled(false);
    }
    
    public void setEnableddElements(){
        codeTextField.setEditable(true);
        nameTextField.setEditable(true);
        searchButton.setEnabled(true);
        createButton.setEnabled(true);
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
    
    public void deleteRowFromResult(int row){
        Vector dataVector = (Vector) subjectsResultDefaultTableModel.getDataVector().get(row);
        int furnitureId = (Integer) dataVector.get(0);
        String code = (String) dataVector.get(1);
        String description = (String) dataVector.get(2);
        int respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea inactivar el mobiliario {0} - {1}?", code, description),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(respuesta == JOptionPane.YES_OPTION){
            //HashMap mapReturn = subjectController.inactivateSubject(furnitureId);
            /*if((Integer) mapReturn.get("status") == subjectController.SUCCESFULLY_SAVED){
                subjectController.searchSubjectButtonAction();
            }else if((Integer)mapReturn.get("status") == subjectController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }*/
        }
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
                //deleteRowFromResult(row);
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
