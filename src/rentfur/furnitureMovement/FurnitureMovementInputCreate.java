/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furnitureMovement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.java.balloontip.BalloonTip;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.furniture.FurnitureController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DateLabelFormatter;
import rentfur.util.NumericTextField;
import rentfur.util.searches.FurnitureSearch;
import rentfur.util.searches.SearchController;

/**
 *
 * @author FDuarte
 */
public class FurnitureMovementInputCreate extends JInternalFrame{
    private final FurnitureMovementController furnitureMovementController;
    private final SearchController searchController;
    private FurnitureSearch furnitureSearch;
    private final JPanel inputMovementCreatePanel;
    private final JLabel movementDateLabel;
    private final JLabel conceptCodeLabel;
    private final JLabel titleLabel;
    private final JDatePickerImpl movementDateDatePicker;
    private final JComboBox conceptCodeComboBox;
    private final JButton addFurnituresButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    private JDesktopPane eventPane;
    
    private final JTable inputMovementDetailTable;
    private final DefaultTableModel inputMovementDetailDefaultTableModel;
    private final JScrollPane inputMovementDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    
    private final int ID_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int FAMILY_COLUMN = 3;
    private final int QUANTITY_COLUMN = 4;
    private final int COST_COLUMN = 5;
    private final int AMOUNT_COLUMN = 6;
//    private final int SUB_TOTAL_COLUMN = 8;
//    private final int TAX_10_COLUMN = 9;
//    private final int TAX_5_COLUMN = 10;
    private final int DELETE_BUTTON_COLUMN = 7;
    
    private final ImageIcon helpIconImage;
    private final JLabel helpLabel;
    private BalloonTip helpBalloonTip;
    public FurnitureMovementInputCreate(FurnitureMovementController furnitureMovementController){
        this.furnitureMovementController = furnitureMovementController;
        
        searchController = new SearchController();
        
        inputMovementCreatePanel = new JPanel();
        inputMovementCreatePanel.setLayout(null);
        
//        eventMap = EventController.getEventById(eventId);
//        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
//        invoiceNumMap = BookController.getInvoicetNum();
//        detailedInvoice = (Boolean) eventMap.get("detailedInvoice");
        
        titleLabel = new JLabel("<HTML><U>Registro de Entrada</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 23));
        titleLabel.setBounds(200, 20, 300, 25);
        inputMovementCreatePanel.add(titleLabel);
        
        movementDateLabel = new JLabel("Fecha:");
        movementDateLabel.setBounds(30, 60, 130, 25);
        inputMovementCreatePanel.add(movementDateLabel);
        
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
//                if(receiptDetailDefaultTableModel.getRowCount()>0){
//                    //showDayChangeWarning();
//                }else{
//                    //updateCurrentDeliveryDateSelected();
//                }
            }
        });
        movementDateDatePicker.getComponent(1).setEnabled(false);
        movementDateDatePicker.setBounds(160, 60, 230, 25);
        inputMovementCreatePanel.add(movementDateDatePicker);
        
        conceptCodeLabel = new JLabel("Concepto:");
        conceptCodeLabel.setBounds(30, 90, 130, 25);
        inputMovementCreatePanel.add(conceptCodeLabel);
        
        ComboBoxItem[] conceptCodes = FurnitureMovementController.getInputConceptCodesForComboBox(false);
        conceptCodeComboBox = new JComboBox(conceptCodes);
        conceptCodeComboBox.setBounds(160, 90, 250, 25);
        inputMovementCreatePanel.add(conceptCodeComboBox);
        
        ImageIcon addFurnitureImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addFurnituresButton = new JButton();
        addFurnituresButton.setIcon(addFurnitureImageIcon);
        addFurnituresButton.setBounds(30, 140, 200, 32);
        addFurnituresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFurnituresButtonAction();
            }
        });
        addFurnituresButton.setText(" Seleccionar Mobiliarios");
        inputMovementCreatePanel.add(addFurnituresButton);
        
        //TABLA DE DETALLES
        inputMovementDetailDefaultTableModel = new InputMovementDetailDefaultTableModel();
        inputMovementDetailTable = new JTable(inputMovementDetailDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda inactiva
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        inputMovementDetailDefaultTableModel.addColumn("Id");
        inputMovementDetailDefaultTableModel.addColumn("Código");
        inputMovementDetailDefaultTableModel.addColumn("Descripción");
        inputMovementDetailDefaultTableModel.addColumn("Familia");
        inputMovementDetailDefaultTableModel.addColumn("Cantidad");
        inputMovementDetailDefaultTableModel.addColumn("Costo");
        inputMovementDetailDefaultTableModel.addColumn("Importe");
        inputMovementDetailDefaultTableModel.addColumn("");
        
        inputMovementDetailTable.setRowHeight(22);
        
        TableCellRenderer rendererFromHeader = inputMovementDetailTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        inputMovementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setHeaderRenderer(new QuantitySimpleHeaderRenderer());
        inputMovementDetailTable.getColumnModel().getColumn(COST_COLUMN).setHeaderRenderer(new CostSimpleHeaderRenderer());
        
        //ID
        inputMovementDetailTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        inputMovementDetailTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        inputMovementDetailTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        
        //CODE
        inputMovementDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(85);
        inputMovementDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMinWidth(85);
        inputMovementDetailTable.getColumnModel().getColumn(CODE_COLUMN).setPreferredWidth(85);
        
        //DESCRIPTION
        inputMovementDetailTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setMinWidth(250);
        inputMovementDetailTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setMaxWidth(350);
        inputMovementDetailTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setPreferredWidth(300);
        
        //DESCRIPTION
        inputMovementDetailTable.getColumnModel().getColumn(FAMILY_COLUMN).setMinWidth(150);
        inputMovementDetailTable.getColumnModel().getColumn(FAMILY_COLUMN).setMaxWidth(150);
        inputMovementDetailTable.getColumnModel().getColumn(FAMILY_COLUMN).setPreferredWidth(15);
        
        //QUANTITY
        inputMovementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setMinWidth(100);
        inputMovementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setMaxWidth(130);
        inputMovementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setPreferredWidth(120);
        inputMovementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellEditor(new QuantityCellEditor());
        inputMovementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(new QuantityCellRenderer());
        
        //COST
        inputMovementDetailTable.getColumnModel().getColumn(COST_COLUMN).setMinWidth(100);
        inputMovementDetailTable.getColumnModel().getColumn(COST_COLUMN).setMaxWidth(130);
        inputMovementDetailTable.getColumnModel().getColumn(COST_COLUMN).setPreferredWidth(120);
        inputMovementDetailTable.getColumnModel().getColumn(COST_COLUMN).setCellEditor(new CostCellEditor());
        inputMovementDetailTable.getColumnModel().getColumn(COST_COLUMN).setCellRenderer(new CostCellRenderer());
        
        //SUB TOTAL
        inputMovementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMinWidth(100);
        inputMovementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMaxWidth(130);
        inputMovementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setPreferredWidth(120);
        inputMovementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellRenderer(rightRenderer);
        
        //DELETE BUTTON in Table
        inputMovementDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMaxWidth(80);
        inputMovementDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMinWidth(80);
        inputMovementDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setResizable(false);
        inputMovementDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        inputMovementDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        
        inputMovementDetailTableJScrollPane = new JScrollPane();
        inputMovementDetailTableJScrollPane.setBounds(30, 200, 1000, 280);
        inputMovementDetailTableJScrollPane.setViewportView(inputMovementDetailTable);
        
        ImageIcon saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
        saveButton.setBounds(670, 500, 150, 32);
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveMovementButtonAction();
            }
        });
        inputMovementCreatePanel.add(saveButton);
        
        ImageIcon cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(850, 500, 150, 32);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        inputMovementCreatePanel.add(cancelButton);
        
        helpIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/help_24x24.png"));
        helpLabel = new JLabel("AYUDA");
        helpLabel.setIcon(helpIconImage);
        helpLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showHelp();
                    }
        });
        helpLabel.setBounds(1000, 20, 300, 25);
        inputMovementCreatePanel.add(helpLabel);

        helpBalloonTip = new BalloonTip(helpLabel, "<html><head></head><body style='background:#F4EFEF;'><div style='margin:24px 34px;'><h2>Registro de Entradas al Inventario</h2><p>Esta vista permite registrar un nuevo movimiento de entrada para mobiliario completando los siguientes datos.</p><ul><li><p>Fecha</p></li><li><p>Concepto</p></li><li><p>Mobiliarios</p></li></ul><p>Con el boton &nbsp<img src='file:/C:/Users/FDuarte/Documents/NetBeansProjects/RentFur/build/classes/rentfur/button/image/util/create_24x24.png'>&nbsp<strong>Seleccionar Mobiliario</strong>, muestra una ventana que permite buscar y seleccionar uno o varios mobiliarios</p><p>Para registrar se debe presionar el boton &nbsp<img src='file:/C:/Users/FDuarte/Documents/NetBeansProjects/RentFur/build/classes/rentfur/button/image/util/save_24x24.png'>&nbsp<strong>Guardar</strong> en caso contrario presionar &nbsp<img src='file:/C:/Users/FDuarte/Documents/NetBeansProjects/RentFur/build/classes/rentfur/button/image/util/cancel_24x24.png'>&nbsp<strong>Cancelar</strong></p></div></body></html>");
        helpBalloonTip.setVisible(false);
        helpBalloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false);
        
        add(inputMovementDetailTableJScrollPane);
        add(inputMovementCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Entradas al Inventario");
        setBounds(350,80,1100,600);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        furnitureMovementController.setFurnitureMovementInputCreateClosed();
    }
    
    private void addFurnituresButtonAction(){
        ComboBoxItem conceptCodeComboBoxItem = (ComboBoxItem) conceptCodeComboBox.getSelectedItem();
        int conceptCode = Integer.valueOf(conceptCodeComboBoxItem.getKey());
        if(conceptCode == FurnitureMovementController.MOVEMENT_CONCEPT_INITIAL_INVENTORY){
            furnitureSearch = searchController.getFurnitureSearch(furnitureCodesAdded, new Date(), false, true);
        }else{
            furnitureSearch = searchController.getFurnitureSearch(furnitureCodesAdded, new Date(), false, false);
        }
        furnitureSearch.setVisible(true);
        furnitureSearch.setBounds(400,160,1000,460);
        showSearchDialog(furnitureSearch);
        inactivateElements();
        furnitureSearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
                if(!searchController.getFurnitureSelectedCode().isEmpty()){
                    addFuritureSelectedToDetailTable(searchController.getFurnitureSelectedCode());
                }
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
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        //selectSubjectButton.setEnabled(false);
        addFurnituresButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        //selectSubjectButton.setEnabled(true);
        addFurnituresButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }
    
    private void showSearchDialog(Object dialogView){
        eventPane = this.getDesktopPane();
        eventPane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        eventPane.setVisible(true);
    }
    
    private void addFuritureSelectedToDetailTable(ArrayList furnitureCodes){
        
        HashMap furnitureMap;
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeWithDayStock(furnitureCodes, new Date());
        
        Object[] row = new Object[inputMovementDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesAdded.add(furnitureMap.get("code"));
            row[ID_COLUMN] = furnitureMap.get("id");
            row[CODE_COLUMN] = furnitureMap.get("code");
            row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[FAMILY_COLUMN] = furnitureMap.get("family");
            row[QUANTITY_COLUMN] = amountFormat.format(new Double("0"));
            row[COST_COLUMN] = amountFormat.format(Double.valueOf(furnitureMap.get("unitCostPrice").toString()));
            row[AMOUNT_COLUMN] = amountFormat.format(new Double("0"));
            row[DELETE_BUTTON_COLUMN] = "";
        
            inputMovementDetailDefaultTableModel.addRow(row);
        }
        
        conceptCodeComboBox.setEnabled(false);
    }
    
    private void updateValues(int row, int quantity, double cost) throws ParseException{
        //Costo
        if(cost == 0){
            cost = amountFormat.parse(inputMovementDetailDefaultTableModel.getValueAt(row, COST_COLUMN).toString()).doubleValue();
        }
        
        //Cantidad
        if(quantity == 0){
            quantity = amountFormat.parse(inputMovementDetailDefaultTableModel.getValueAt(row, QUANTITY_COLUMN).toString()).intValue();
        }
        
        double subTotal = quantity * cost;
        inputMovementDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, AMOUNT_COLUMN);
    }
    
    private void removeRow(int row) throws ParseException{
        String furnitureCode = inputMovementDetailDefaultTableModel.getValueAt(row, CODE_COLUMN).toString();
        furnitureCodesAdded.remove(furnitureCode);
        inputMovementDetailDefaultTableModel.removeRow(row);
        
        if(inputMovementDetailDefaultTableModel.getRowCount()==0){
            conceptCodeComboBox.setEnabled(true);
        }
    }
    
    private boolean allQuantityAdded(){
        Vector dataVector;
        try {
            for (int row = 0; row < inputMovementDetailDefaultTableModel.getRowCount(); row++){
               dataVector = (Vector) inputMovementDetailDefaultTableModel.getDataVector().get(row);
                   if(amountFormat.parse(dataVector.get(QUANTITY_COLUMN).toString()).intValue()==0){
                       return false;
                   }
           }
        } catch (ParseException ex) {
            Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private void saveMovementButtonAction(){
        JOptionPane optionPane;
        JDialog dialog;
        if(!allQuantityAdded()){
            optionPane = new JOptionPane("Existen detalles con cantidad 0 (Cero). Favor ingrese correctamente todas las cantidades.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            
            ComboBoxItem conceptCodeComboBoxItem = (ComboBoxItem) conceptCodeComboBox.getSelectedItem();
            int conceptCode = Integer.valueOf(conceptCodeComboBoxItem.getKey());
            
            Date movementDate = (Date) movementDateDatePicker.getModel().getValue();
            
            HashMap movementDetailMap;
            ArrayList movementDetailList = new ArrayList();
            Vector dataVector;
            
            try {
            
                for (int row = 0; row < inputMovementDetailTable.getRowCount(); row++){
                    dataVector = (Vector) inputMovementDetailDefaultTableModel.getDataVector().get(row);
                    movementDetailMap = new HashMap();
                    movementDetailMap.put("furnitureId",dataVector.get(ID_COLUMN));
                    movementDetailMap.put("code",dataVector.get(CODE_COLUMN));
                    movementDetailMap.put("description",dataVector.get(DESCRIPTION_COLUMN));
                    movementDetailMap.put("quantity",amountFormat.parse(dataVector.get(QUANTITY_COLUMN).toString()).intValue());
                    movementDetailMap.put("cost",amountFormat.parse(dataVector.get(COST_COLUMN).toString()).intValue());
                    movementDetailMap.put("import",amountFormat.parse(dataVector.get(AMOUNT_COLUMN).toString()).intValue());
                    movementDetailList.add(movementDetailMap);
                }
            
            } catch (ParseException ex) {
                Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            HashMap returnMap = furnitureMovementController.inputMovementRecord(movementDetailList, conceptCode, movementDate);
            
            if(((Integer)returnMap.get("status"))==FurnitureMovementController.SUCCESFULLY_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
            }else if((Integer)returnMap.get("status") == FurnitureMovementController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private class InputMovementDetailDefaultTableModel extends DefaultTableModel{
        
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
                                                        case QUANTITY_COLUMN:    return true;
                                                        case COST_COLUMN:    return true;
                                                        case DELETE_BUTTON_COLUMN:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    private class QuantitySimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public QuantitySimpleHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(new Color(80, 175, 145));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

    }
    
    private class CostSimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public CostSimpleHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(new Color(141, 170, 201));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

    }
    
    class QuantityCellEditor extends AbstractCellEditor implements TableCellEditor {

        JComponent component = new NumericTextField(20, amountFormat);
        int row;
        int column;        
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int rowIndex, int vColIndex) {
            
            this.row = rowIndex;
            this.column = vColIndex;
            ((NumericTextField) this.component).addKeyListener(new KeyListener() {

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
                         String texto = ((NumericTextField) component).getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                ((NumericTextField) component).setValue(Double.valueOf(texto));
                            }
                         }
                     }
                 });
            
            try {
                ((NumericTextField) this.component).setValue(amountFormat.parse(value.toString()));
            } catch (ParseException ex) {
                Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            try {
                String newQuantityString = ((JTextField) component).getText();
                if(newQuantityString.equals("")){
                    newQuantityString = "0";
                }
                int newQuantity = amountFormat.parse(newQuantityString).intValue();
                ((JTextField) component).setText(amountFormat.format(newQuantity));
                updateValues(row, newQuantity, 0);
            } catch (ParseException | HeadlessException th) {
                Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, th);
            }
            return ((JTextField) component).getText();
        }

    }
    
    class QuantityCellRenderer extends JTextField implements TableCellRenderer {

        public QuantityCellRenderer() {
            setOpaque(true);
            setBackground(new Color(237, 247, 243));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Cantidad");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    private class CostCellEditor extends AbstractCellEditor implements TableCellEditor {

        JComponent component = new NumericTextField(20, amountFormat);
        int row;
        int column;        
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int rowIndex, int vColIndex) {
            
            this.row = rowIndex;
            this.column = vColIndex;
            ((NumericTextField) this.component).addKeyListener(new KeyListener() {

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
                         String texto = ((NumericTextField) component).getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                ((NumericTextField) component).setValue(Double.valueOf(texto));
                            }
                         }
                     }
                 });
            
            try {
                ((NumericTextField) this.component).setValue(amountFormat.parse(value.toString()));
            } catch (ParseException ex) {
                Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            try {

                String newCostString = ((JTextField) component).getText();
                if(newCostString.equals("")){
                    newCostString = "0";
                }
                double newCost = amountFormat.parse(newCostString).doubleValue();
                ((JTextField) component).setText(amountFormat.format(newCost));
                updateValues(row, 0, newCost);
            } catch (HeadlessException | ParseException th) {
                Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, th);
            }
            return ((JTextField) component).getText();
        }

    }
    
    private class CostCellRenderer extends JTextField implements TableCellRenderer {

        public CostCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Costo");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class DeleteButtonRenderer extends JButton implements TableCellRenderer {

        public DeleteButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            ImageIcon deleteIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/delete_16x16.png"));
            setIcon(deleteIconImage);
            setToolTipText("Remover");
            setText((value == null) ? "" : value.toString());
            return this;
        }
      }

      /**
       * @version 1.0 11/09/98
       */

     class DeleteButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int row;

        private int column;
        
        public DeleteButtonEditor(JTextField jtf) {
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
//          if (isPushed) {
//                removeRow(row);
//          }
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
            try {
                removeRow(row);
            } catch (ParseException th) {
                Logger.getLogger(FurnitureMovementInputCreate.class.getName()).log(Level.SEVERE, null, th);
            }
        }
      }
    public void showHelp(){
        if(!helpBalloonTip.isVisible()){
            helpBalloonTip.setVisible(true);
        }
    }
}
