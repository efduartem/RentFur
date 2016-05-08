/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.event;

import java.awt.Color;
import java.awt.Component;
import rentfur.util.DateLabelFormatter;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
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
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.furniture.FurnitureController;
import rentfur.subject.SubjectController;
import rentfur.util.ComboBoxItem;
import rentfur.util.SQLUtilService;
import rentfur.util.searches.FurnitureSearch;
import rentfur.util.searches.SearchController;
import rentfur.util.searches.SubjectSearch;

/**
 *
 * @author FDuarte
 */
public class EventCreate extends JInternalFrame{
    private final EventController eventController;
    private final SearchController searchController;
    private SubjectSearch subjectSearch;
    private FurnitureSearch furnitureSearch;
    private final JPanel eventCreatePanel;
    private final JLabel subjectCodeLabel;
    private final JLabel titleLabel;
    private final JLabel subjectLabel;
    private final JLabel totalSummaryLabel;
    private final JLabel subjectAddressLabel;
    private final JLabel subjectNameLabel;
    private final JLabel subjectTelephoneLabel;
    private final JLabel subjectTradenameLabel;
    private final JLabel subjectFiscalNumberLabel;
    private final JLabel subjectCityLabel;
    private final JLabel eventDateLabel;
    private final JLabel statusLabel;
    private final JLabel totalLabel;
    private final JLabel totalTaxLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private JDatePickerImpl datePicker;
    private final JComboBox statusComboBox;
    private final JTextField totalTextField;
    private final JTextField totalTaxTextField;
    private final JButton selectSubjectButton;
    private final JButton addFurnituresButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    private JDesktopPane pane;
    
    private final JTable eventDetailTable;
    private final DefaultTableModel eventDetailDefaultTableModel;
    private final JScrollPane eventDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    
    private final int TAX_RATE_COLUMN = 3;
    private final int UNIT_PRICE = 5;
    private final int QUANTITY_COLUMN = 7;
    private final int SUB_TOTAL_COLUMN = 8;
    
    private ArrayList taxList = new ArrayList(); //Tasas de IVA
    private HashMap taxRatioMap = new HashMap(); //Cocientes para IVA
    private HashMap taxableRatioMap = new HashMap();//Cocientes para Gravadas
    
    
    public EventCreate(EventController eventController){
        this.eventController = eventController;
        searchController = new SearchController();
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventCreatePanel = new JPanel();
        eventCreatePanel.setLayout(null);
        
        titleLabel = new JLabel("<HTML><U>Datos del Evento</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        eventCreatePanel.add(titleLabel);
        
        eventDateLabel = new JLabel("Fecha del Evento:");
        eventDateLabel.setBounds(30, 60, 130, 25);
        eventCreatePanel.add(eventDateLabel);
        
        statusLabel = new JLabel("Estado:");
        statusLabel.setBounds(370, 60, 130, 25);
        eventCreatePanel.add(statusLabel);
        
        ComboBoxItem[] eventStatusAvailableComboBox = EventController.getEventStatusAvailablesForComboBox(false);
        statusComboBox = new JComboBox(eventStatusAvailableComboBox);
        statusComboBox.setEnabled(false);
        statusComboBox.setBounds(490, 60, 170, 25);
        statusComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //filter();
            }
        });
        eventCreatePanel.add(statusComboBox);
                
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        datePicker.setBounds(160, 60, 170, 25);
        eventCreatePanel.add(datePicker);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        eventCreatePanel.add(subjectLabel);
        
        totalSummaryLabel = new JLabel("<HTML><U>Totales</U></HTML>");
        totalSummaryLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        totalSummaryLabel.setBounds(1130, 110, 200, 25);
        eventCreatePanel.add(totalSummaryLabel);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        eventCreatePanel.add(subjectCodeLabel);
        
        subjectCodeTextField = new JTextField();
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        eventCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        eventCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField();
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        eventCreatePanel.add(subjectAddressTextField);
        
        //Total Evento
        totalLabel = new JLabel("Total: ");
        totalLabel.setBounds(1100, 150, 80, 25);
        eventCreatePanel.add(totalLabel);
        
        totalTextField = new JTextField("0");
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(1200, 150, 170, 25);
        eventCreatePanel.add(totalTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        eventCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField();
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        eventCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        eventCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField();
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        eventCreatePanel.add(subjectTelephoneTextField);
        
        //Total IVA
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(1100, 180, 80, 25);
        eventCreatePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField("0");
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(1200, 180, 170, 25);
        eventCreatePanel.add(totalTaxTextField);
        
        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 210, 120, 25);
        eventCreatePanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField();
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        eventCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        eventCreatePanel.add(subjectFiscalNumberLabel);
        
        subjectFiscalNumberTextField = new JTextField();
        subjectFiscalNumberTextField.setEditable(false);
        subjectFiscalNumberTextField.setBounds(490, 210, 170, 25);
        eventCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        eventCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField();
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        eventCreatePanel.add(subjectCityTextField);
        
        selectSubjectButton = new JButton();
        ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/subject_24x24.png"));
        selectSubjectButton.setIcon(subjectIconImage);
        selectSubjectButton.setBounds(250, 105, 200, 32);
        selectSubjectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectSubjectButtonAction();
            }
        });
        selectSubjectButton.setText(" Seleccionar Cliente");
        eventCreatePanel.add(selectSubjectButton);
        
        addFurnituresButton = new JButton();
        addFurnituresButton.setBounds(30, 290, 150, 32);
        addFurnituresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFurnituresButtonAction();
            }
        });
        addFurnituresButton.setText("Agregar Mobiliario");
        eventCreatePanel.add(addFurnituresButton);
        
        //TABLA DE DETALLES
        eventDetailDefaultTableModel = new eventDetailDefaultTableModel();
        eventDetailTable = new JTable(eventDetailDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda inactiva
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        eventDetailDefaultTableModel.addColumn("Id");
        eventDetailDefaultTableModel.addColumn("Código");
        eventDetailDefaultTableModel.addColumn("Descripción");
        eventDetailDefaultTableModel.addColumn("Tasa de Impuesto");
        eventDetailDefaultTableModel.addColumn("Disponibilidad");
        eventDetailDefaultTableModel.addColumn("Precio Unitario");
        eventDetailDefaultTableModel.addColumn("Multa");
        eventDetailDefaultTableModel.addColumn("Cantidad");
        eventDetailDefaultTableModel.addColumn("Subtotal");
        
        String taxRateColumns = SQLUtilService.getSystemConfigurationValue("furniture.tax.rates");
         
        for(int i = 0; i < taxRateColumns.split(";").length; i++){
            if(!taxRateColumns.split(";")[i].split(",")[0].trim().equals("0")){
                taxList.add(taxRateColumns.split(";")[i].split(",")[0].trim());

                //Configuration Value viene en formato "10,11,1.1" --> Ejemplo para tasa al 10%
                //En el mapa de cocientes para impuestos se guaradan entonces asi "10":"11"
                taxRatioMap.put(taxRateColumns.split(";")[i].split(",")[0], taxRateColumns.split(";")[i].split(",")[1]);

                //Configuration Value viene en formato "10,11,1.1" --> Ejemplo para tasa al 10%
                //En el mapa de cocientes para gravadas de impuestos se guaradan entonces asi "10":"1.1"
                taxableRatioMap.put(taxRateColumns.split(";")[i].split(",")[0], taxRateColumns.split(";")[i].split(",")[2]);

                eventDetailDefaultTableModel.addColumn(taxRateColumns.split(";")[i].split(",")[0]+" %");
            }
        }        
        
        eventDetailTable.setRowHeight(22);
        
        TableCellRenderer rendererFromHeader = eventDetailTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setHeaderRenderer(new SimpleHeaderRenderer());
        
        //ID
        eventDetailTable.getColumnModel().getColumn(0).setMaxWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setMinWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //TAX RATE
        eventDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellRenderer(rightRenderer);
        
        //STOCK
        eventDetailTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        //UNIT PRICE
        eventDetailTable.getColumnModel().getColumn(UNIT_PRICE).setCellRenderer(rightRenderer);
        
        //FINE AMOUNT
        eventDetailTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        //QUANTITY
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellEditor(new QuantityCellEditor());
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(rightRenderer);
        
        //SUB TOTAL
        eventDetailTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        
        for(int i = 0; i < taxRateColumns.split(";").length; i++){
            if(!taxRateColumns.split(";")[i].split(",")[0].trim().equals("0")){
                eventDetailTable.getColumnModel().getColumn(9+i).setMaxWidth(90);
                eventDetailTable.getColumnModel().getColumn(9+i).setMinWidth(90);
                eventDetailTable.getColumnModel().getColumn(9+i).setCellRenderer(rightRenderer);
            }
        }
        
        eventDetailTableJScrollPane = new JScrollPane();
        eventDetailTableJScrollPane.setBounds(30, 340, 1350, 300);
        eventDetailTableJScrollPane.setViewportView(eventDetailTable);
        
        add(eventDetailTableJScrollPane);
        
        
        ImageIcon saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Crear Evento", saveIconImage);
        saveButton.setBounds(1050, 690, 150, 32);
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveEventButtonAction();
            }
        });
        eventCreatePanel.add(saveButton);
        
        ImageIcon cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(1230, 690, 150, 32);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        eventCreatePanel.add(cancelButton);
        
        add(eventCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Crear Evento");
        setBounds(150,50,1450,800);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void selectSubjectButtonAction(){
        subjectSearch = searchController.getSubjectSearch();
        subjectSearch.setVisible(true);
        showSearchDialog(subjectSearch);
        inactivateElements();
        subjectSearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) { 
                activateElements();
                if(!searchController.getSubjetcSelectedCode().equals("")){
                    updateSubjectSelected(searchController.getSubjetcSelectedCode());
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
    };
    
    public void addFurnituresButtonAction(){
        
        if(datePicker.getModel().getValue() == null){
            JOptionPane optionPane = new JOptionPane("Favor ingresar la fecha del evento de modo a obtener la disponibilidad de mobiliarios", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            furnitureSearch = searchController.getFurnitureSearch(furnitureCodesAdded);
            furnitureSearch.setVisible(true);
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
        
    }
    
    private class SimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public SimpleHeaderRenderer() {
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
    
    private class eventDetailDefaultTableModel extends DefaultTableModel{
        
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
                                                        case QUANTITY_COLUMN:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    public void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        selectSubjectButton.setEnabled(false);
        addFurnituresButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    public void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        selectSubjectButton.setEnabled(true);
        addFurnituresButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }
    
    public void showSearchDialog(Object dialogView){
        pane = getDesktopPane();
        pane.add((JInternalFrame) dialogView, JLayeredPane.MODAL_LAYER);
        pane.setVisible(true);
    }
    
    private void saveEventButtonAction(){
        System.out.println("GUARDAR..");
    }
    
    private void cancelButtonAction(ActionEvent e) {
        doDefaultCloseAction();
    }

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        eventController.eventCreateClosed();
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void updateSubjectSelected(String subjectCode){
        HashMap subjectMap = SubjectController.getSubjectByCode(subjectCode);
        subjectCodeTextField.setText((String)subjectMap.get("code"));
        subjectAddressTextField.setText((String)subjectMap.get("address"));
        subjectNameTextField.setText((String)subjectMap.get("name"));
        subjectTelephoneTextField.setText((String)subjectMap.get("telephone"));
        subjectTradenameTextField.setText((String)subjectMap.get("tradename"));
        subjectCityTextField.setText((String)subjectMap.get("city"));
        
        String fiscalNumber; 
        
        if(((String)subjectMap.get("fiscalNumber")).contains("-")){
            fiscalNumber = amountFormat.format(Double.valueOf(((String)subjectMap.get("fiscalNumber")).split("-")[0]));
            subjectFiscalNumberTextField.setText(fiscalNumber+"-"+((String)subjectMap.get("fiscalNumber")).split("-")[1]);
        }else{
            fiscalNumber = amountFormat.format(Double.valueOf(((String)subjectMap.get("fiscalNumber"))));
            subjectFiscalNumberTextField.setText(fiscalNumber);
        }
    }
    
    public void addFuritureSelectedToDetailTable(ArrayList furnitureCodes){
        
        HashMap furnitureMap;
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeList(furnitureCodes);
        
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesAdded.add(furnitureMap.get("code"));
            row[0] = furnitureMap.get("id");
            row[1] = furnitureMap.get("code");
            row[2] = furnitureMap.get("description");
            row[TAX_RATE_COLUMN] = furnitureMap.get("taxRate");
            row[4] = 0;
            row[UNIT_PRICE] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[6] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN] = 0;
            row[8] = 0;
            
            for(int iTax = SUB_TOTAL_COLUMN; iTax < (SUB_TOTAL_COLUMN + taxList.size()); iTax++){
                 row[iTax + 1] = 0;
            }
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    public void updateSubTotal(int newQuantity, int row, int column) throws ParseException{
        //SubTotal
        double oldSubTotal = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, SUB_TOTAL_COLUMN).toString()).doubleValue();
        Number unitPrice = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, UNIT_PRICE).toString());
        double subTotal = newQuantity * unitPrice.doubleValue();
        eventDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, 8);
        
        //Item tax
        String furnitureTaxRateString = eventDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
        double oldItemTaxAmount =0;
        double furnitureTaxRate = 0;
        double itemTax = 0;
        int taxIndexIntable = 0;
        if(!furnitureTaxRateString.equals("0")){
            
            taxIndexIntable = SUB_TOTAL_COLUMN + Integer.valueOf(taxList.indexOf(furnitureTaxRateString)) + 1;
            oldItemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, taxIndexIntable).toString()).doubleValue();
            
            furnitureTaxRate = Double.valueOf((String)taxRatioMap.get(furnitureTaxRateString));
            itemTax = new BigDecimal(subTotal / furnitureTaxRate).setScale(0, RoundingMode.HALF_UP).doubleValue();
            eventDetailDefaultTableModel.setValueAt(amountFormat.format(itemTax), row, taxIndexIntable);
            
        }
        
        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = (total - oldSubTotal) + subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL IVA
        double totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
        totalTax = (totalTax - oldItemTaxAmount) + itemTax;
        totalTaxTextField.setText(amountFormat.format(totalTax));
    }
    
    /*class QuantityEditor extends DefaultCellEditor {
        
        public QuantityEditor(JTextField jtf) {
          super(jtf);
          this.clickCountToStart = 1;
        }
        
        @Override
        public Object getCellEditorValue() {
          System.out.println("OK");
          return label;
        }

        @Override
        public boolean stopCellEditing() {
          System.out.println("stopCellEditing");
          return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
          System.out.println("fireEditingStopped");
          super.fireEditingStopped();
        }
      }*/
    
    class QuantityCellEditor extends AbstractCellEditor implements TableCellEditor {

        JComponent component = new JTextField();
        int row;
        int column;
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int rowIndex, int vColIndex) {
            this.row = rowIndex;
            this.column = vColIndex;
            ((JTextField) component).setText(value.toString());
            return component;
        }

        @Override
        public Object getCellEditorValue() {
          String newQuantityString = ((JTextField) component).getText();
          int newQuantity = Integer.valueOf(newQuantityString);
            try {
                updateSubTotal(newQuantity, row, column);
            } catch (ParseException ex) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
          return ((JTextField) component).getText();
        }

    }
}