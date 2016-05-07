/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.event;

import java.awt.Component;
import rentfur.util.DateLabelFormatter;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.furniture.FurnitureController;
import rentfur.subject.SubjectController;
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
    private final JLabel subjectAddressLabel;
    private final JLabel subjectNameLabel;
    private final JLabel subjectTelephoneLabel;
    private final JLabel subjectTradenameLabel;
    private final JLabel subjectFiscalNumberLabel;
    private final JLabel subjectCityLabel;
    private final JLabel eventDateLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JButton selectSubjectButton;
    private final JButton addFurnituresButton;
    private JDesktopPane pane;
    
    private final JTable eventDetailTable;
    private final DefaultTableModel eventDetailDefaultTableModel;
    private final JScrollPane eventDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    
    public EventCreate(EventController eventController){
        this.eventController = eventController;
        searchController = new SearchController();
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventCreatePanel = new JPanel();
        eventCreatePanel.setLayout(null);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 20, 80, 25);
        eventCreatePanel.add(subjectCodeLabel);
        
        subjectCodeTextField = new JTextField();
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 20, 170, 25);
        eventCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 20, 160, 25);
        eventCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField();
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 20, 170, 25);
        eventCreatePanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 50, 100, 25);
        eventCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField();
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 50, 170, 25);
        eventCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 50, 100, 25);
        eventCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField();
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 50, 170, 25);
        eventCreatePanel.add(subjectTelephoneTextField);
        
        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 80, 120, 25);
        eventCreatePanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField();
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 80, 170, 25);
        eventCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 80, 120, 25);
        eventCreatePanel.add(subjectFiscalNumberLabel);
        
        subjectFiscalNumberTextField = new JTextField();
        subjectFiscalNumberTextField.setEditable(false);
        subjectFiscalNumberTextField.setBounds(490, 80, 170, 25);
        eventCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 110, 80, 25);
        eventCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField();
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 110, 170, 25);
        eventCreatePanel.add(subjectCityTextField);
        
        eventDateLabel = new JLabel("Fecha del Evento:");
        eventDateLabel.setBounds(370, 110, 130, 25);
        eventCreatePanel.add(eventDateLabel);
        
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        datePicker.setBounds(490, 110, 170, 25);
        eventCreatePanel.add(datePicker);
        
        selectSubjectButton = new JButton();
        selectSubjectButton.setBounds(30, 160, 150, 25);
        selectSubjectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectSubjectButtonAction();
            }
        });
        selectSubjectButton.setText("Seleccionar Cliente");
        eventCreatePanel.add(selectSubjectButton);
        
        addFurnituresButton = new JButton();
        addFurnituresButton.setBounds(230, 160, 150, 25);
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
        //statusInactiveRenderer.setBackground(new Color(16751001));
        
        //Formato para celda activa
        //DefaultTableCellRenderer statusActiveRenderer = new DefaultTableCellRenderer();
        //statusActiveRenderer.setHorizontalAlignment(JLabel.CENTER);
        //statusActiveRenderer.setBackground(new Color(13434828));
        
        eventDetailDefaultTableModel.addColumn("Id");
        eventDetailDefaultTableModel.addColumn("Código");
        eventDetailDefaultTableModel.addColumn("Descripción");
        eventDetailDefaultTableModel.addColumn("Disponibilidad");
        eventDetailDefaultTableModel.addColumn("Precio Unitario");
        eventDetailDefaultTableModel.addColumn("Multa");
        eventDetailDefaultTableModel.addColumn("Cantidad");
        eventDetailDefaultTableModel.addColumn("Subtotal");
        
        eventDetailTable.setRowHeight(22);
        
        //ID
        eventDetailTable.getColumnModel().getColumn(0).setMaxWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setMinWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //STOCK
        eventDetailTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        //UNIT PRICE
        eventDetailTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        //FINE AMOUNT
        eventDetailTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        //QUANTITY
        //eventDetailTable.getColumnModel().getColumn(6).setCellEditor(new QuantityEditor(new JTextField()));
        eventDetailTable.getColumnModel().getColumn(6).setCellEditor(new QuantityCellEditor());
        eventDetailTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        //SUB TOTAL
        eventDetailTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        
        eventDetailTableJScrollPane = new JScrollPane();
        eventDetailTableJScrollPane.setBounds(30, 200, 1100, 300);
        eventDetailTableJScrollPane.setViewportView(eventDetailTable);
        
        add(eventDetailTableJScrollPane);
        
        add(eventCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Crear Evento");
        setBounds(30,30,1400,800);
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
                                                        case 6:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    public void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        selectSubjectButton.setEnabled(false);
        addFurnituresButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    public void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        selectSubjectButton.setEnabled(true);
        addFurnituresButton.setEnabled(true);
    }
    
    public void showSearchDialog(Object dialogView){
        pane = getDesktopPane();
        pane.add((JInternalFrame) dialogView, JLayeredPane.MODAL_LAYER);
        pane.setVisible(true);
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
            row[3] = 0;
            row[4] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[5] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[6] = 0;
            row[7] = 0;
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    public void updateSubTotal(int newQuantity, int row, int column) throws ParseException{
        Number unitPrice = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, 4).toString());
        double subTotal = newQuantity * unitPrice.doubleValue();
        eventDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, 7);
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
