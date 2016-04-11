/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.provider;

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
import rentfur.util.ComboBoxItem;
import rentfur.util.NumericTextField;


/**
 *
 * @author hp
 */
public class ProviderIndex extends JInternalFrame{
    private final ProviderController providerController;
    private final JPanel providerIndexParamsPanel;
    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel tradenameLabel;
    private final JLabel fiscalNumberLabel;
    private final JLabel addressLabel;
    private final JLabel telephoneLabel;
    private final JLabel cityLabel;
    private final JLabel statusLabel;
    private final JTextField codeTextField;
    private final JTextField nameTextField;
    private final JTextField tradenameTextField;
    private final NumericTextField fiscalNumberTextField;
    private final JComboBox providerStatusComboBox;
    private final JTextField addressTextField;
    private final JTextField telephoneTextField;
    private final JTextField cityTextField;
    private final DefaultTableModel providerResultDefaultTableModel;
    private final JTable providerResultTable;
    private final JScrollPane providerResultTableJScrollPane;
    private final DecimalFormat amountFormat;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
    private final ImageIcon createIconImage;
    private final JButton createButton;
    
    
    public ProviderIndex(ProviderController providerController){
        this.providerController = providerController;
        
        providerIndexParamsPanel = new JPanel();
        providerIndexParamsPanel.setLayout(null);
        
        //FILA 1
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        providerIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(160, 50, 170, 25);
        providerIndexParamsPanel.add(codeTextField);
        
        addressLabel = new JLabel("Dirección:");
        addressLabel.setBounds(370, 50, 160, 25);
        providerIndexParamsPanel.add(addressLabel);
        
        addressTextField = new JTextField();
        addressTextField.setBounds(440, 50, 170, 25);
        providerIndexParamsPanel.add(addressTextField);
        
        //FILA 2
        nameLabel = new JLabel("Razón Social:");
        nameLabel.setBounds(30, 80, 100, 25);
        providerIndexParamsPanel.add(nameLabel);
        
        nameTextField = new JTextField();
        nameTextField.setBounds(160, 80, 170, 25);
        providerIndexParamsPanel.add(nameTextField);
        
        telephoneLabel = new JLabel("Teléfono:");
        telephoneLabel.setBounds(370, 80, 100, 25);
        providerIndexParamsPanel.add(telephoneLabel);
        
        telephoneTextField = new JTextField();
        telephoneTextField.setBounds(440, 80, 170, 25);
        providerIndexParamsPanel.add(telephoneTextField);
        
        //FILA 3
        tradenameLabel = new JLabel("Nombre Comercial:");
        tradenameLabel.setBounds(30, 110, 120, 25);
        providerIndexParamsPanel.add(tradenameLabel);
        
        tradenameTextField = new JTextField();
        tradenameTextField.setBounds(160, 110, 170, 25);
        providerIndexParamsPanel.add(tradenameTextField);
        
        fiscalNumberLabel = new JLabel("RUC / CI:");
        fiscalNumberLabel.setBounds(370, 110, 120, 25);
        providerIndexParamsPanel.add(fiscalNumberLabel);
        
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

        providerIndexParamsPanel.add(fiscalNumberTextField);
        
        //FILA 4
        statusLabel = new JLabel("Activo:");
        statusLabel.setBounds(30, 140, 80, 25);
        providerIndexParamsPanel.add(statusLabel);
        
        ComboBoxItem[] furnitureStatus = providerController.getProviderIsActiveForComboBox();
        providerStatusComboBox = new JComboBox(furnitureStatus);
        providerStatusComboBox.setBounds(160, 140, 170, 25);
        providerIndexParamsPanel.add(providerStatusComboBox);
        
        cityLabel = new JLabel("Ciudad:");
        cityLabel.setBounds(370, 140, 80, 25);
        providerIndexParamsPanel.add(cityLabel);
        
        cityTextField = new JTextField();
        cityTextField.setBounds(440, 140, 170, 25);
        providerIndexParamsPanel.add(cityTextField);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Buscar", searchIconImage);
        searchButton.setBounds(30, 190, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProviderButtonAction(e);
            }
        });
        providerIndexParamsPanel.add(searchButton);
        
        //BOTON PARA CREAR PROVEEDOR
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        createButton = new JButton("  Crear Proveedor", createIconImage);
        createButton.setBounds(160, 190, 180, 32);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getProviderCreate();
            }
        });
        providerIndexParamsPanel.add(createButton);
        
        //TABLA DE RESULTADOS
        providerResultDefaultTableModel = new providerIndextResultDefaultTableModel();
        providerResultTable = new JTable(providerResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        providerController.setProviderIndexResultsTable(providerResultDefaultTableModel, false, null, null, null, null, null, null, null, null);
        providerResultTable.setRowHeight(22);
        
        //ID
        providerResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        providerResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        providerResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Code
        providerResultTable.getColumnModel().getColumn(1).setMaxWidth(90);
        providerResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        
        //Name
        providerResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Tradename
        providerResultTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        //Address
        providerResultTable.getColumnModel().getColumn(4).setPreferredWidth(180);
        
        //City
        providerResultTable.getColumnModel().getColumn(5).setMaxWidth(100);
        providerResultTable.getColumnModel().getColumn(5).setMinWidth(100);
        providerResultTable.getColumnModel().getColumn(5).setResizable(false);
        
        //Telephone
        providerResultTable.getColumnModel().getColumn(6).setMaxWidth(100);
        providerResultTable.getColumnModel().getColumn(6).setMinWidth(100);
        providerResultTable.getColumnModel().getColumn(6).setResizable(false);
        
        //Fiscal Number
        providerResultTable.getColumnModel().getColumn(7).setMaxWidth(100);
        providerResultTable.getColumnModel().getColumn(7).setMinWidth(100);
        providerResultTable.getColumnModel().getColumn(7).setResizable(false);
        providerResultTable.getColumnModel().getColumn(7).setCellRenderer(statusRenderer);
        
        //Activo
        providerResultTable.getColumnModel().getColumn(8).setMaxWidth(50);
        providerResultTable.getColumnModel().getColumn(8).setMinWidth(50);
        providerResultTable.getColumnModel().getColumn(8).setResizable(false);
        providerResultTable.getColumnModel().getColumn(8).setCellRenderer(statusRenderer);

         //BOTON Activar/Desactivar
        providerResultTable.getColumnModel().getColumn(9).setMaxWidth(90);
        providerResultTable.getColumnModel().getColumn(9).setMinWidth(90);
        providerResultTable.getColumnModel().getColumn(9).setPreferredWidth(90);
        providerResultTable.getColumnModel().getColumn(9).setCellRenderer(new ProviderIndex.ButtonRenderer());
        providerResultTable.getColumnModel().getColumn(9).setCellEditor(new ProviderIndex.ButtonEditor(new JTextField()));
        
        //BOTON Ver
        providerResultTable.getColumnModel().getColumn(10).setMaxWidth(60);
        providerResultTable.getColumnModel().getColumn(10).setMinWidth(60);
        providerResultTable.getColumnModel().getColumn(10).setPreferredWidth(60);
        providerResultTable.getColumnModel().getColumn(10).setCellRenderer(new ProviderIndex.ButtonRenderer());
        providerResultTable.getColumnModel().getColumn(10).setCellEditor(new ProviderIndex.ButtonEditor(new JTextField()));
        
        //Active
        providerResultTable.getColumnModel().getColumn(11).setMaxWidth(0);
        providerResultTable.getColumnModel().getColumn(11).setMinWidth(0);
        providerResultTable.getColumnModel().getColumn(11).setPreferredWidth(0);
        providerResultTable.getColumnModel().getColumn(11).setResizable(false);
        
        providerResultTableJScrollPane = new JScrollPane();
        providerResultTableJScrollPane.setBounds(30, 240, 1100, 300);
        providerResultTableJScrollPane.setViewportView(providerResultTable);
        
        add(providerResultTableJScrollPane);        
        add(providerIndexParamsPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Administrar Proveedor");
        setBounds(100,50,1200,700);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void searchProviderButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String name = nameTextField.getText();
        String tradename = tradenameTextField.getText();
        String address = addressTextField.getText();
        String city = cityTextField.getText();
        String telephone = telephoneTextField.getText();
        String fiscalNumber = fiscalNumberTextField.getText();
        fiscalNumber = fiscalNumber.replaceAll("\\.", "");
        ComboBoxItem active = (ComboBoxItem) providerStatusComboBox.getSelectedItem();
        String providerActive = "";
        if(active!=null){
            providerActive = active.getKey();
        }
       providerController.setProviderIndexResultsTable(providerResultDefaultTableModel, true, code, name, tradename, address, city, telephone, providerActive, fiscalNumber);
    }
    
     private class providerIndextResultDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 10:     return true;
                                                        default:    return false;
                                                    }
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
                label = updateProviderStatus(row, label);
            }else if(column==10){
                showProviderShowAndEditView(row);
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
        
        private void showProviderShowAndEditView(int row){
            Vector dataVector = (Vector) providerResultDefaultTableModel.getDataVector().get(row);
            int providerId = (Integer) dataVector.get(0);
            providerController.getProviderShowAndEditView(providerId);
        }
      }
    
    public String updateProviderStatus(int row, String label){
        Vector dataVector = (Vector) providerResultDefaultTableModel.getDataVector().get(row);
        int providerId = (Integer) dataVector.get(0);
        String code = (String) dataVector.get(1);
        String name = (String) dataVector.get(2);
        boolean active = (boolean) dataVector.get(11);
        String valueToReturn = label;
        int respuesta;
        HashMap mapReturn;
        if(active){
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea inactivar el proveedor {0} - {1}?", code, name),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = providerController.updateProviderStatus(providerId, active);
                if((Integer) mapReturn.get("status") == providerController.SUCCESFULLY_SAVED){
                    providerController.searchProviderButtonAction();
                    valueToReturn = "Activar";
                }else if((Integer)mapReturn.get("status") == providerController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea activar el proveedor {0} - {1}?", code, name),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = providerController.updateProviderStatus(providerId, active);
                if((Integer) mapReturn.get("status") == providerController.SUCCESFULLY_SAVED){
                    providerController.searchProviderButtonAction();
                    valueToReturn = "Inactivar";
                }else if((Integer)mapReturn.get("status") == providerController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                } 
            }
        }
        
        return valueToReturn;
    }
    
    public void getProviderCreate(){
        providerController.getProviderCreateView();
    }
    
    public void setDisabledElements(){
        codeTextField.setEditable(false);
        nameTextField.setEditable(false);
        tradenameTextField.setEditable(false);
        addressTextField.setEditable(false);
        telephoneTextField.setEditable(false);
        cityTextField.setEditable(false);
        fiscalNumberTextField.setEditable(false);
        providerStatusComboBox.setEnabled(false);
        createButton.setEnabled(false);
        searchButton.setEnabled(false);
        this.setClosable(false);
        providerResultTable.setEnabled(false);
    }
    
    public void setEnabledElements(){
        codeTextField.setEditable(true);
        nameTextField.setEditable(true);
        tradenameTextField.setEditable(true);
        addressTextField.setEditable(true);
        telephoneTextField.setEditable(true);
        cityTextField.setEditable(true);
        fiscalNumberTextField.setEditable(true);
        providerStatusComboBox.setEnabled(true);
        searchButton.setEnabled(true);
        createButton.setEnabled(true);
        this.setClosable(true);
        providerResultTable.setEnabled(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        closeIndexView(null);
    }
    
    private void closeIndexView(ActionEvent e) {
        this.dispose();
        providerController.indexViewClosed();
    }
    
    public void showProviderShowAndEditView(int row){
        Vector dataVector = (Vector) providerResultDefaultTableModel.getDataVector().get(row);
        int providerId = (Integer) dataVector.get(0);
        providerController.getProviderShowAndEditView(providerId);
    }
}
