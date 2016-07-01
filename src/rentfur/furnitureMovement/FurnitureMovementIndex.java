/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furnitureMovement;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import org.jdatepicker.impl.JDatePickerImpl;
import rentfur.position.PositionController;
import rentfur.util.NumericTextField;
import rentfur.util.UserRoles;
import rentfur.util.ComboBoxItem;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */
public class FurnitureMovementIndex extends JInternalFrame{
    private final FurnitureMovementController furnitureMovementController;
    private FurnitureMovementShow furnitureMovementShow;
    private final JPanel furnitureMovementIndexParamsPanel;
    private final JLabel movementNumberLabel;
    private final JLabel movementTypeLabel;
    private final JLabel conceptCodeLabel;
    private final JLabel movementDateLabel;
//    private final JLabel addressLabel;
//    private final JLabel telephoneLabel;
//    private final JLabel fiscalNumberLabel;
//    private final JLabel cityLabel;
    private final JTextField movementNumberTextField;
    private final JComboBox movementTypeComboBox;
    private final JComboBox conceptCodeComboBox;
    private final JTextField movementDateTextField;
    private final JDatePickerImpl movementDateDatePicker;
//    private final JTextField addressTextField;
//    private final JTextField telephoneTextField;
//    private final NumericTextField fiscalNumberTextField;
//    private final JTextField cityTextField;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
//    private final ImageIcon createIconImage;
//    private final JButton createButton;
    private final JTable furnitureMovementResultTable;
    private final DefaultTableModel furnitureMovementResultDefaultTableModel;
    private final JScrollPane furnitureMovementResultTableJScrollPane;
    private final DecimalFormat amountFormat;
    //private boolean onlyQuery = false;
    private JDesktopPane movementIndexPane;
    
    public FurnitureMovementIndex(FurnitureMovementController furnitureMovementController, UserRoles userRoles){
        
        this.furnitureMovementController = furnitureMovementController;
        
//        if((Boolean)userRoles.getRolesMap().get(PositionController.ROLE_RF_FURNITURE)){
//            onlyQuery = true;
//        }
        
        furnitureMovementIndexParamsPanel = new JPanel();
        furnitureMovementIndexParamsPanel.setLayout(null);
        
        //FILA 1
        movementNumberLabel = new JLabel("Nro de Movimiento:");
        movementNumberLabel.setBounds(30, 50, 130, 25);
        furnitureMovementIndexParamsPanel.add(movementNumberLabel);
        
        movementNumberTextField = new JTextField();
        movementNumberTextField.setBounds(200, 50, 170, 25);
        furnitureMovementIndexParamsPanel.add(movementNumberTextField);
        
//        addressLabel = new JLabel("Dirección:");
//        addressLabel.setBounds(370, 50, 160, 25);
//        furnitureMovementIndexParamsPanel.add(addressLabel);
//        
//        addressTextField = new JTextField();
//        addressTextField.setBounds(440, 50, 170, 25);
//        furnitureMovementIndexParamsPanel.add(addressTextField);
        
        //FILA 2
        movementTypeLabel = new JLabel("Tipo de Movimiento:");
        movementTypeLabel.setBounds(30, 80, 130, 25);
        furnitureMovementIndexParamsPanel.add(movementTypeLabel);
        
        ComboBoxItem[] movementTypes = FurnitureMovementController.getMovementTypesForComboBox(true);
        movementTypeComboBox = new JComboBox(movementTypes);
        movementTypeComboBox.setBounds(200, 80, 170, 25);
        furnitureMovementIndexParamsPanel.add(movementTypeComboBox);
        
//        telephoneLabel = new JLabel("Teléfono:");
//        telephoneLabel.setBounds(370, 80, 100, 25);
//        furnitureMovementIndexParamsPanel.add(telephoneLabel);
//        
//        telephoneTextField = new JTextField();
//        telephoneTextField.setBounds(440, 80, 170, 25);
//        furnitureMovementIndexParamsPanel.add(telephoneTextField);
        
        //FILA 3
        conceptCodeLabel = new JLabel("Concepto:");
        conceptCodeLabel.setBounds(30, 110, 130, 25);
        furnitureMovementIndexParamsPanel.add(conceptCodeLabel);
        
        ComboBoxItem[] conceptCodes = FurnitureMovementController.getConceptCodesForComboBox(true);
        conceptCodeComboBox = new JComboBox(conceptCodes);
        conceptCodeComboBox.setBounds(200, 110, 250, 25);
        furnitureMovementIndexParamsPanel.add(conceptCodeComboBox);
        
//        fiscalNumberLabel = new JLabel("RUC / CI:");
//        fiscalNumberLabel.setBounds(370, 110, 120, 25);
//        furnitureMovementIndexParamsPanel.add(fiscalNumberLabel);
//        
        amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
//        
//        fiscalNumberTextField = new NumericTextField(20, amountFormat);
//        fiscalNumberTextField.setBounds(440, 110, 170, 25);
//        fiscalNumberTextField.addKeyListener(new KeyListener() {
//
//                     @Override
//                     public void keyTyped(KeyEvent e) {
//                     }
//
//                     @Override
//                     public void keyPressed(KeyEvent e) {
//                     }
//
//                     @Override
//                     public void keyReleased(KeyEvent e) {
//                         update(e);
//                     }
//                     
//                     public void update(KeyEvent e){
//                         String texto = fiscalNumberTextField.getText();
//                         texto = texto.replaceAll("\\.", "");
//                         if(e.getKeyChar()!=','){
//                            texto = texto.replaceAll(",", ".");
//                            if(!texto.isEmpty()){
//                                fiscalNumberTextField.setValue(Double.valueOf(texto));
//                            }
//                         }else{
//                             texto = texto.replaceAll(",", ".");
//                         }
//                     }
//                 });
//
//        furnitureMovementIndexParamsPanel.add(fiscalNumberTextField);
        
        //FILA 4
        movementDateLabel = new JLabel("Fecha de Movimiento:");
        movementDateLabel.setBounds(30, 140, 130, 25);
        furnitureMovementIndexParamsPanel.add(movementDateLabel);
        
        movementDateTextField = new JTextField("");
        movementDateTextField.setBounds(200, 140, 170, 25);
        furnitureMovementIndexParamsPanel.add(movementDateTextField);
        
        UtilDateModel model = new UtilDateModel(new Date());
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        movementDateDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        movementDateDatePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setMovementDateValue();
            }
        });
        movementDateDatePicker.setBounds(390, 140, 170, 25);
        furnitureMovementIndexParamsPanel.add(movementDateDatePicker);
        
//        cityLabel = new JLabel("Ciudad:");
//        cityLabel.setBounds(370, 140, 80, 25);
//        furnitureMovementIndexParamsPanel.add(cityLabel);
//        
//        cityTextField = new JTextField();
//        cityTextField.setBounds(440, 140, 170, 25);
//        furnitureMovementIndexParamsPanel.add(cityTextField);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Buscar", searchIconImage);
        searchButton.setBounds(30, 190, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFurnitureMovementButtonAction(e);
            }
        });
        furnitureMovementIndexParamsPanel.add(searchButton);
        
        //BOTON PARA CREAR MOBILIARIO
//        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
//        createButton = new JButton("  Crear Cliente", createIconImage);
//        createButton.setBounds(160, 190, 180, 32);
//        createButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //getSubjectCreate();
//            }
//        });
//        furnitureMovementIndexParamsPanel.add(createButton);
        
//        if(onlyQuery){
//            String message = "Su usuario solo cuenta con permiso de consultas";
//            createButton.setEnabled(false);
//            createButton.setToolTipText(message);
//        }
        
        //TABLA DE RESULTADOS
        furnitureMovementResultDefaultTableModel = new FurnitureMovementIndextResultDefaultTableModel();
        furnitureMovementResultTable = new JTable(furnitureMovementResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        furnitureMovementController.setFurnitureMovementIndexResultsTable(furnitureMovementResultDefaultTableModel, false, null, null, null, null);
        furnitureMovementResultTable.setRowHeight(23);
        //ID
        furnitureMovementResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        furnitureMovementResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        furnitureMovementResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        furnitureMovementResultTable.getColumnModel().getColumn(0).setResizable(false);
        
        //NUMERO
        furnitureMovementResultTable.getColumnModel().getColumn(1).setMaxWidth(110);
        furnitureMovementResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        furnitureMovementResultTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        
        //FECHA
        furnitureMovementResultTable.getColumnModel().getColumn(2).setMaxWidth(180);
        furnitureMovementResultTable.getColumnModel().getColumn(2).setMinWidth(90);
        furnitureMovementResultTable.getColumnModel().getColumn(2).setPreferredWidth(130);
        
        //TIPO
        furnitureMovementResultTable.getColumnModel().getColumn(3).setMaxWidth(180);
        furnitureMovementResultTable.getColumnModel().getColumn(3).setMinWidth(90);
        furnitureMovementResultTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        
        //CONCEPTO
        furnitureMovementResultTable.getColumnModel().getColumn(4).setMaxWidth(280);
        furnitureMovementResultTable.getColumnModel().getColumn(4).setMinWidth(150);
        furnitureMovementResultTable.getColumnModel().getColumn(4).setPreferredWidth(270);
        
        //TIPO DOC
        furnitureMovementResultTable.getColumnModel().getColumn(5).setMaxWidth(200);
        furnitureMovementResultTable.getColumnModel().getColumn(5).setMinWidth(130);
        furnitureMovementResultTable.getColumnModel().getColumn(5).setPreferredWidth(190);
        
        //DOC
        furnitureMovementResultTable.getColumnModel().getColumn(6).setMaxWidth(250);
        furnitureMovementResultTable.getColumnModel().getColumn(6).setMinWidth(150);
        furnitureMovementResultTable.getColumnModel().getColumn(6).setPreferredWidth(220);
        
        //VER
        furnitureMovementResultTable.getColumnModel().getColumn(7).setMaxWidth(110);
        furnitureMovementResultTable.getColumnModel().getColumn(7).setMinWidth(110);
        furnitureMovementResultTable.getColumnModel().getColumn(7).setPreferredWidth(110);
        furnitureMovementResultTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        furnitureMovementResultTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JTextField()));
        
        furnitureMovementResultTableJScrollPane = new JScrollPane();
        furnitureMovementResultTableJScrollPane.setBounds(30, 240, 1130, 300);
        furnitureMovementResultTableJScrollPane.setViewportView(furnitureMovementResultTable);
        
        add(furnitureMovementResultTableJScrollPane);
        add(furnitureMovementIndexParamsPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Movimientos de Mobiliario");
        setBounds(350,100,1230,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        furnitureMovementController.indexViewClosed();
    }
    
    private void showMovementShowView(int row){
        int movementId = Integer.valueOf(furnitureMovementResultDefaultTableModel.getValueAt(row, 0).toString());
        //FurnitureMovementShow
        furnitureMovementShow = furnitureMovementController.getFurnitureMovementShow(movementId);
        furnitureMovementShow.setVisible(true);
        showSearchDialog(furnitureMovementShow);
        inactivateElements();
        furnitureMovementShow.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {}

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {}

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {}

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
        
    }
    
    public void inactivateElements(){
        searchButton.setEnabled(false);
        movementTypeComboBox.setEnabled(false);
        conceptCodeComboBox.setEnabled(false);
        this.setClosable(false);
        this.setIconifiable(false);
        furnitureMovementResultTable.setEnabled(false);
    }
    
    public void activateElements(){
        searchButton.setEnabled(true);
        movementTypeComboBox.setEnabled(true);
        conceptCodeComboBox.setEnabled(true);
        this.setClosable(true);
        this.setIconifiable(true);
        furnitureMovementResultTable.setEnabled(true);
    }
    
    private void showSearchDialog(Object dialogView){
        movementIndexPane = this.getDesktopPane();
        movementIndexPane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        movementIndexPane.setVisible(true);
    }
    
    private void setMovementDateValue(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date movementDate = (Date) movementDateDatePicker.getModel().getValue();
        movementDateTextField.setText(formatter.format(movementDate));
    }
    
    public void searchFurnitureMovementButtonAction(ActionEvent e) {
        String movementNumber = movementNumberTextField.getText();
        ComboBoxItem movementTypeSelected = (ComboBoxItem) movementTypeComboBox.getSelectedItem();
        String movementType = "";
        if(movementTypeSelected!=null){
            movementType = movementTypeSelected.getKey();
        }
        
        ComboBoxItem concetCodeSelected = (ComboBoxItem) conceptCodeComboBox.getSelectedItem();
        String conceptCode = "";
        if(concetCodeSelected!=null){
            conceptCode = concetCodeSelected.getKey();
        }
        
        String movementDate = movementDateTextField.getText();
        
        furnitureMovementController.setFurnitureMovementIndexResultsTable(furnitureMovementResultDefaultTableModel, true, movementNumber, movementType, conceptCode, movementDate);
    }
    
    private class FurnitureMovementIndextResultDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 7:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
          if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
          } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
          }
//          if(column==9){
//              if(onlyQuery){
//                    String message = "Su usuario solo cuenta con permiso de consultas";
//                    setEnabled(false);
//                    setToolTipText(message);
//                }
//          }
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
//                if(column==10){
//                    if(!onlyQuery){
//                        label = updateFurnitureStatus(row, label);
//                    }
//                }else 
                if(column==7){
                    showMovementShowView(row);
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
