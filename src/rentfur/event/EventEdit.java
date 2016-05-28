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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.swing.JTextArea;
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
import rentfur.util.NumericTextField;
import rentfur.util.SQLUtilService;
import rentfur.util.searches.FurnitureSearch;
import rentfur.util.searches.SearchController;
import rentfur.util.searches.SubjectSearch;

/**
 *
 * @author FDuarte
 */
public class EventEdit extends JInternalFrame{
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
    private final JLabel placeOfDeliveryLabel;
    private final JLabel observationLabel;
    private final JLabel totalLabel;
    private final JLabel totalTaxLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JDatePickerImpl datePicker;
    private final JComboBox statusComboBox;
    private final JTextArea placeOfDeliveryTextArea;
    private final JTextArea observationTextArea;
    private final JTextField totalTextField;
    private final JTextField totalTaxTextField;
    private final JButton selectSubjectButton;
    private final JButton addFurnituresButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    private JDesktopPane eventPane;
    
    private final JTable eventDetailTable;
    private final DefaultTableModel eventDetailDefaultTableModel;
    private final JScrollPane eventDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    
    private final int ID_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int TAX_RATE_COLUMN = 3;
    private final int STOCK_AVAILABLE_COLUMN = 4;
    private final int UNIT_PRICE = 5;
    private final int FINE_AMOUNT_COLUMN = 6;
    private final int QUANTITY_COLUMN = 7;
    private final int SUB_TOTAL_COLUMN = 8;
    private final int TAX_5_COLUMN = 9;
    private final int TAX_10_COLUMN = 10;
    private final int DELETE_BUTTON_COLUMN = 11;
    
    
    private ArrayList taxList = new ArrayList(); //Tasas de IVA
    private HashMap taxRatioMap = new HashMap(); //Cocientes para IVA
    private HashMap taxableRatioMap = new HashMap();//Cocientes para Gravadas
    
    private HashMap eventMap;
    private HashMap subjectMap;
    
    public EventEdit(EventController eventController, int eventId){
        this.eventController = eventController;
        searchController = new SearchController();
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventCreatePanel = new JPanel();
        eventCreatePanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        
        titleLabel = new JLabel("<HTML><U>Datos del Evento</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        eventCreatePanel.add(titleLabel);
        
        eventDateLabel = new JLabel("Fecha del Evento:");
        eventDateLabel.setBounds(30, 60, 130, 25);
        eventCreatePanel.add(eventDateLabel);
        
        Date deliveryDate = new Date(((Timestamp) eventMap.get("deliveryDate")).getTime());
        UtilDateModel model = new UtilDateModel(deliveryDate);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        //datePicker.getComponent(1).setEnabled(false);
//        datePicker.setEnabled(false);
        //Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//        datePicker.getModel().setDate(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.MONTH), 30);
        datePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showDayChangeWarning();
            }
        });
        datePicker.setBounds(160, 60, 170, 25);
        eventCreatePanel.add(datePicker);
        
        statusLabel = new JLabel("Estado:");
        statusLabel.setBounds(370, 60, 130, 25);
        eventCreatePanel.add(statusLabel);
        
        ComboBoxItem[] eventStatusAvailableComboBox = EventController.getEventStatusAvailablesForCreateEvent(false);
        
        ComboBoxItem eventStatusComboBoxItem = null;
        for (ComboBoxItem eventStatusComboBoxFor : eventStatusAvailableComboBox) {
            eventStatusComboBoxItem = eventStatusComboBoxFor;
            if(eventStatusComboBoxItem.getKey().equals(eventMap.get("status").toString())){
                break;
            }
        }
        
        statusComboBox = new JComboBox(eventStatusAvailableComboBox);
        statusComboBox.setSelectedItem(eventStatusComboBoxItem);
        //statusComboBox.setEnabled(false);
        statusComboBox.setBounds(490, 60, 170, 25);
        statusComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });
        eventCreatePanel.add(statusComboBox);
        
        placeOfDeliveryLabel = new JLabel("Lugar de Entrega:");
        placeOfDeliveryLabel.setBounds(710, 60, 130, 25);
        eventCreatePanel.add(placeOfDeliveryLabel);
        
        placeOfDeliveryTextArea = new JTextArea(0,0);
        placeOfDeliveryTextArea.setText(eventMap.get("placeOfDelivery").toString());
        placeOfDeliveryTextArea.setLineWrap(true);
        placeOfDeliveryTextArea.setWrapStyleWord(true);
        JScrollPane placeOfDeliveryScrollPane = new JScrollPane();
        placeOfDeliveryScrollPane.setBounds(830, 60, 230, 90);
        placeOfDeliveryScrollPane.setViewportView(placeOfDeliveryTextArea);
        eventCreatePanel.add(placeOfDeliveryScrollPane);
        
        observationLabel = new JLabel("Observaciones:");
        observationLabel.setBounds(710, 160, 130, 25);
        eventCreatePanel.add(observationLabel);
        
        observationTextArea = new JTextArea(0,0);
        observationTextArea.setText(eventMap.get("observation").toString());
        observationTextArea.setLineWrap(true);
        observationTextArea.setWrapStyleWord(true);
        JScrollPane observationScrollPane = new JScrollPane();
        observationScrollPane.setBounds(830, 160, 230, 90);
        observationScrollPane.setViewportView(observationTextArea);
        eventCreatePanel.add(observationScrollPane);
        
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
        
        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        eventCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        eventCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        eventCreatePanel.add(subjectAddressTextField);
        
        //Total Evento
        totalLabel = new JLabel("Total: ");
        totalLabel.setBounds(1100, 150, 80, 25);
        eventCreatePanel.add(totalLabel);
        
        totalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("netTotal").toString())));
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(1200, 150, 170, 25);
        eventCreatePanel.add(totalTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        eventCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        eventCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        eventCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        eventCreatePanel.add(subjectTelephoneTextField);
        
        //Total IVA
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(1100, 180, 80, 25);
        eventCreatePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("totalTax").toString())));
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(1200, 180, 170, 25);
        eventCreatePanel.add(totalTaxTextField);
        
        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 210, 120, 25);
        eventCreatePanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        eventCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        eventCreatePanel.add(subjectFiscalNumberLabel);
        
        subjectFiscalNumberTextField = new JTextField();
        String fiscalNumber; 
        if(((String)subjectMap.get("fiscalNumber")).contains("-")){
            fiscalNumber = amountFormat.format(Double.valueOf(((String)subjectMap.get("fiscalNumber")).split("-")[0]));
            subjectFiscalNumberTextField.setText(fiscalNumber+"-"+((String)subjectMap.get("fiscalNumber")).split("-")[1]);
        }else{
            fiscalNumber = amountFormat.format(Double.valueOf(((String)subjectMap.get("fiscalNumber"))));
            subjectFiscalNumberTextField.setText(fiscalNumber);
        }
        subjectFiscalNumberTextField.setEditable(false);
        subjectFiscalNumberTextField.setBounds(490, 210, 170, 25);
        eventCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        eventCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
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
        
        ImageIcon addFurnitureImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addFurnituresButton = new JButton();
        addFurnituresButton.setIcon(addFurnitureImageIcon);
        addFurnituresButton.setBounds(30, 290, 180, 32);
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
        
        eventDetailDefaultTableModel.addColumn("Id");//0
        eventDetailDefaultTableModel.addColumn("Código");//1
        eventDetailDefaultTableModel.addColumn("Descripción");//2
        eventDetailDefaultTableModel.addColumn("Tasa de Impuesto");//3
        eventDetailDefaultTableModel.addColumn("Disponibilidad");//4
        eventDetailDefaultTableModel.addColumn("Precio Unitario");//5
        eventDetailDefaultTableModel.addColumn("Multa");//6
        eventDetailDefaultTableModel.addColumn("Cantidad");//7
        eventDetailDefaultTableModel.addColumn("Subtotal");//8
        eventDetailDefaultTableModel.addColumn("5 %");//9
        eventDetailDefaultTableModel.addColumn("10 %");//10
        eventDetailDefaultTableModel.addColumn("");//11
        
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
        
            }
        }        
        
        eventDetailTable.setRowHeight(22);
        
        TableCellRenderer rendererFromHeader = eventDetailTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setHeaderRenderer(new SimpleHeaderRenderer());
        eventDetailTable.getColumnModel().getColumn(STOCK_AVAILABLE_COLUMN).setHeaderRenderer(new AvailableSimpleHeaderRenderer());
        
        //ID
        eventDetailTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        eventDetailTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        eventDetailTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        
        //CODE
        eventDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(80);
        eventDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMinWidth(80);
        
        //TAX RATE
        eventDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellRenderer(rightRenderer);
        
        //STOCK
        eventDetailTable.getColumnModel().getColumn(STOCK_AVAILABLE_COLUMN).setCellRenderer(new AvailableCellRenderer());
        
        //UNIT PRICE
        eventDetailTable.getColumnModel().getColumn(UNIT_PRICE).setCellRenderer(rightRenderer);
        
        //FINE AMOUNT
        eventDetailTable.getColumnModel().getColumn(FINE_AMOUNT_COLUMN).setCellRenderer(rightRenderer);
        
        //QUANTITY
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellEditor(new QuantityCellEditor());
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(new QuantityCellRenderer());
        
        //SUB TOTAL
        eventDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setCellRenderer(rightRenderer);
        
        
        eventDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setMaxWidth(90);
        eventDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setMinWidth(90);
        eventDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setCellRenderer(rightRenderer);
                
        eventDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setMaxWidth(90);
        eventDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setMinWidth(90);
        eventDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setCellRenderer(rightRenderer);
        
        //DELETE BUTTON in Table
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMaxWidth(80);
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMinWidth(80);
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setResizable(false);
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        
        eventDetailTableJScrollPane = new JScrollPane();
        eventDetailTableJScrollPane.setBounds(30, 340, 1350, 300);
        eventDetailTableJScrollPane.setViewportView(eventDetailTable);
        
        add(eventDetailTableJScrollPane);
        
        ArrayList furnitureDetailList = (ArrayList) eventMap.get("detail");
        addFuritureDetailToChargesTable(furnitureDetailList);
        
        ImageIcon saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
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
        setTitle("Editar Presupuesto");
        setBounds(150,50,1450,800);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void selectSubjectButtonAction(){
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
    
    private void addFuritureDetailToChargesTable(ArrayList furnitureDetailList){
        HashMap furnitureMap;
        
        Date deliveryDate = new Date(((Timestamp) eventMap.get("deliveryDate")).getTime());
        int stockAvailable = 0;
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureDetailList.size(); i++){
            furnitureMap = (HashMap) furnitureDetailList.get(i);
            row[ID_COLUMN] = furnitureMap.get("id");
            row[CODE_COLUMN] = furnitureMap.get("code");
            row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[TAX_RATE_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxRate"));
            stockAvailable = FurnitureController.getFurnitureStockByCodeAndDay(furnitureMap.get("code").toString(), deliveryDate);
            row[STOCK_AVAILABLE_COLUMN] = stockAvailable;
            row[UNIT_PRICE] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[FINE_AMOUNT_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN] = furnitureMap.get("quantity");
            row[SUB_TOTAL_COLUMN] = amountFormat.format((Double)furnitureMap.get("totalAmount"));
            row[TAX_5_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxAmount5"));
            row[TAX_10_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxAmount10"));
            row[DELETE_BUTTON_COLUMN] = "";
            furnitureCodesAdded.add(furnitureMap.get("code"));
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    private void addFurnituresButtonAction(){
        
        if(datePicker.getModel().getValue() == null){
            JOptionPane optionPane = new JOptionPane("Favor ingresar la fecha del evento de modo a obtener la disponibilidad de mobiliarios", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            furnitureSearch = searchController.getFurnitureSearch(furnitureCodesAdded, (Date)datePicker.getModel().getValue());
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
    
    private class AvailableSimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public AvailableSimpleHeaderRenderer() {
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
                                                        case QUANTITY_COLUMN:    return true;
                                                        case DELETE_BUTTON_COLUMN:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    private void showDayChangeWarning(){
        int respuesta = JOptionPane.showConfirmDialog(this, "Con esta accion se eliminaran los mobiliarios precargados para este presupuesto, debido a que las disponibilidades se veran afectadas. Confirmar accion?","Confirmar cambio de Fecha", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(respuesta == JOptionPane.YES_OPTION){
            //removeA
            try{
                int rowCount = eventDetailDefaultTableModel.getRowCount();
                for(int i = (rowCount-1); i >= 0; i-- ){
                    removeRow(i);
                }
            }catch (ParseException ex) {
                Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            Date deliveryDate = new Date(((Timestamp) eventMap.get("deliveryDate")).getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deliveryDate);
            datePicker.getModel().setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        }
    }
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        selectSubjectButton.setEnabled(false);
        addFurnituresButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        selectSubjectButton.setEnabled(true);
        addFurnituresButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }
    
    private void showSearchDialog(Object dialogView){
        eventPane = this.getDesktopPane();
        eventPane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        eventPane.setVisible(true);
    }
    
    private void saveEventButtonAction(){
        JOptionPane optionPane;
        JDialog dialog;
        if(datePicker.getModel().getValue() == null){
            optionPane = new JOptionPane("Favor ingresar la fecha del evento", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(placeOfDeliveryTextArea.getText().equals("")){
            optionPane = new JOptionPane("Favor ingrese el lugar de Entrega", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(eventDetailTable.getRowCount()==0){
            optionPane = new JOptionPane("No ha sido agregado ningun detalle (item)", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(subjectCodeTextField.getText().equals("")){
            optionPane = new JOptionPane("No ha sido seleccionado ningun Cliente", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(!allQuantityAdded()){
            optionPane = new JOptionPane("Existen detalles con cantidad 0 (Cero). Favor ingrese correctamente todas las cantidades.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            
            HashMap subjectMap = SubjectController.getSubjectByCode(subjectCodeTextField.getText()); 
            Date deliveryDate = (Date) datePicker.getModel().getValue();
            ComboBoxItem statusComboBoxItem = (ComboBoxItem) statusComboBox.getSelectedItem();
            int status = Integer.valueOf(statusComboBoxItem.getKey());
            String placeOfDelivery = placeOfDeliveryTextArea.getText();
            String observation = observationTextArea.getText();
            HashMap furnitureMap;
            ArrayList furnitureList = new ArrayList();
            Vector dataVector;
            double netTotal = 0;
            double totalTax = 0;
            double totalTax5 = 0;
            double totalTax10 = 0;
            double totalTaxable5 = 0;
            double totalTaxable10 = 0;
            double totalTaxable = 0;
            boolean taxable = false;
            
            try {
            
                for (int row = 0; row < eventDetailTable.getRowCount(); row++){
                    taxable = false;
                    dataVector = (Vector) eventDetailDefaultTableModel.getDataVector().get(row);
                    furnitureMap = new HashMap();
                    furnitureMap.put("code",dataVector.get(CODE_COLUMN));
                    furnitureMap.put("quantity",amountFormat.parse(dataVector.get(QUANTITY_COLUMN).toString()).intValue());
                    furnitureMap.put("subTotal", amountFormat.parse(dataVector.get(SUB_TOTAL_COLUMN).toString()).doubleValue());
                    totalTax5 += amountFormat.parse(dataVector.get(TAX_5_COLUMN).toString()).doubleValue();
                    totalTax10 += amountFormat.parse(dataVector.get(TAX_10_COLUMN).toString()).doubleValue();
                    if(amountFormat.parse(dataVector.get(TAX_RATE_COLUMN).toString()).doubleValue()==5){
                        totalTaxable5 += amountFormat.parse(dataVector.get(SUB_TOTAL_COLUMN).toString()).doubleValue();
                        taxable = true;
                    }else if(amountFormat.parse(dataVector.get(TAX_RATE_COLUMN).toString()).doubleValue()==10){
                        totalTaxable10 += amountFormat.parse(dataVector.get(SUB_TOTAL_COLUMN).toString()).doubleValue();
                        taxable = true;
                    }
                    if(taxable){
                        totalTaxable += amountFormat.parse(dataVector.get(SUB_TOTAL_COLUMN).toString()).doubleValue();
                    }
                    furnitureMap.put("tax5", amountFormat.parse(dataVector.get(TAX_5_COLUMN).toString()).doubleValue());
                    furnitureMap.put("tax10", amountFormat.parse(dataVector.get(TAX_10_COLUMN).toString()).doubleValue());
                    furnitureList.add(furnitureMap);
                }

                netTotal = amountFormat.parse(totalTextField.getText()).doubleValue();
                totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
            
            } catch (ParseException ex) {
                Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
            int eventId = (Integer) eventMap.get("id");
            HashMap returnMap = eventController.updateEventBudgeted(subjectMap, deliveryDate, status, placeOfDelivery, furnitureList, netTotal, totalTax, totalTax5, totalTax10, totalTaxable5, totalTaxable10, totalTaxable, observation, eventId);
            
            if(((Integer)returnMap.get("status"))==EventController.SUCCESFULLY_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
            }else if((Integer)returnMap.get("status") == EventController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void cancelButtonAction(ActionEvent e) {
        doDefaultCloseAction();
    }
    

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        eventController.eventEditClosed();
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private boolean allQuantityAdded(){
        Vector dataVector;
        try {
            for (int row = 0; row < eventDetailTable.getRowCount(); row++){
               dataVector = (Vector) eventDetailDefaultTableModel.getDataVector().get(row);
                   if(amountFormat.parse(dataVector.get(QUANTITY_COLUMN).toString()).intValue()==0){
                       return false;
                   }
           }
        } catch (ParseException ex) {
            Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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
    
    private void addFuritureSelectedToDetailTable(ArrayList furnitureCodes){
        
        HashMap furnitureMap;
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeWithDayStock(furnitureCodes, (Date) datePicker.getModel().getValue());
        
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesAdded.add(furnitureMap.get("code"));
            row[0] = furnitureMap.get("id");
            row[1] = furnitureMap.get("code");
            row[2] = furnitureMap.get("description");
            row[TAX_RATE_COLUMN] = furnitureMap.get("taxRate");
            row[STOCK_AVAILABLE_COLUMN] = amountFormat.format(Long.valueOf(furnitureMap.get("stockAvailable").toString()));
            row[UNIT_PRICE] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[6] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN] = 0;
            row[8] = 0;
            
            for(int iTax = SUB_TOTAL_COLUMN; iTax < (SUB_TOTAL_COLUMN + taxList.size()); iTax++){
                 row[iTax + 1] = 0;
            }
            
            row[11] = "";
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    private void updateSubTotal(int newQuantity, int row, int column) throws ParseException{
        //SubTotal
        double oldSubTotal = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, SUB_TOTAL_COLUMN).toString()).doubleValue();
        Number unitPrice = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, UNIT_PRICE).toString());
        double subTotal = newQuantity * unitPrice.doubleValue();
        eventDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, SUB_TOTAL_COLUMN);
        
        //Item tax
        String furnitureTaxRateString = eventDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
        double oldItemTaxAmount =0;
        double furnitureTaxRate = 0;
        double itemTax = 0;
        
        if(furnitureTaxRateString.equals("10")){
            
            oldItemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, TAX_10_COLUMN).toString()).doubleValue();
            
            furnitureTaxRate = Double.valueOf((String)taxRatioMap.get(furnitureTaxRateString));
            itemTax = new BigDecimal(subTotal / furnitureTaxRate).setScale(0, RoundingMode.HALF_UP).doubleValue();
            eventDetailDefaultTableModel.setValueAt(amountFormat.format(itemTax), row, TAX_10_COLUMN);
            
        }else if(furnitureTaxRateString.equals("5")){
            oldItemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, TAX_5_COLUMN).toString()).doubleValue();
            furnitureTaxRate = Double.valueOf((String)taxRatioMap.get(furnitureTaxRateString));
            itemTax = new BigDecimal(subTotal / furnitureTaxRate).setScale(0, RoundingMode.HALF_UP).doubleValue();
            eventDetailDefaultTableModel.setValueAt(amountFormat.format(itemTax), row, TAX_5_COLUMN);
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
    
    private void removeRow(int row) throws ParseException{
        
        double subTotal = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, SUB_TOTAL_COLUMN).toString()).doubleValue();
        
        //Item tax
        String furnitureTaxRateString = eventDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
        double itemTaxAmount =0;
        
        if(furnitureTaxRateString.equals("10")){
            itemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, TAX_10_COLUMN).toString()).doubleValue();
        }else if(furnitureTaxRateString.equals("5")){
            itemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, TAX_5_COLUMN).toString()).doubleValue();
        }

        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = total - subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL IVA
        double totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
        totalTax = totalTax - itemTaxAmount;
        totalTaxTextField.setText(amountFormat.format(totalTax));
        
        String furnitureCode = eventDetailDefaultTableModel.getValueAt(row, CODE_COLUMN).toString();
        furnitureCodesAdded.remove(furnitureCode);
        eventDetailDefaultTableModel.removeRow(row);
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
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });
            
            try {
                ((NumericTextField) this.component).setValue(amountFormat.parse(value.toString()));
            } catch (ParseException ex) {
                Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            JOptionPane optionPane;
            JDialog dialog;
            try {
                int oldQuantity = amountFormat.parse((eventDetailDefaultTableModel.getValueAt(row, QUANTITY_COLUMN).toString())).intValue();
                String newQuantityString = ((JTextField) component).getText();
                if(newQuantityString.equals("")){
                    newQuantityString = "0";
                }
                int newQuantity = amountFormat.parse(newQuantityString).intValue();
                double stockAvailable = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, STOCK_AVAILABLE_COLUMN).toString()).doubleValue();
                if(stockAvailable < newQuantity){
                    optionPane = new JOptionPane("El valor ingresado supera la cantidad disponible de este mobiliario", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    dialog = optionPane.createDialog(null, "Atencion!");
                    dialog.setVisible(true);
                    ((JTextField) component).setText(amountFormat.format(oldQuantity));
                }else{
                    ((JTextField) component).setText(amountFormat.format(newQuantity));
                    updateSubTotal(newQuantity, row, column);
                }
            } catch (Throwable th) {
                Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, th);
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
    
    class AvailableCellRenderer extends JTextField implements TableCellRenderer {

        public AvailableCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
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
            } catch (ParseException ex) {
                Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      }
}
