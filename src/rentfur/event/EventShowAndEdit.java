package rentfur.event;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.receipt.ReceiptController;
import rentfur.receipt.ReceiptCreate;
import rentfur.subject.SubjectController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */
public class EventShowAndEdit extends JInternalFrame{
    private final EventController eventController;
    private ReceiptCreate receiptCreate;
    private ReceiptController receiptController;
    private final JPanel eventHeaderPanel;
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
    private final JLabel totalLabel;
    private final JLabel totalTaxLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JComboBox statusComboBox;
    private final JTextField deliveryDateTextField;
    private final JTextField placeOfDeliveryTextField;
    private final JTextField totalTextField;
    private final JTextField totalTaxTextField;
    private JButton paymentRecordButton;
    private final JTabbedPane tabs;
    private JDesktopPane pane;
    private JTable eventDetailTable;
    private DefaultTableModel eventDetailDefaultTableModel;
    private JScrollPane eventDetailTableJScrollPane;
    
    private static final int ID_COLUMN = 0;
    private static final int CODE_COLUMN_CHARGES = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int TAX_RATE_COLUMN = 3;
    private static final int UNIT_PRICE_COLUMN = 4;
    private static final int PENALTY_COLUMN = 5;
    private static final int QUANTITY_COLUMN_CHARGES = 6;
    private static final int SUBTOTAL_COLUMN = 7;
    private HashMap eventMap;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    public EventShowAndEdit(EventController eventController, int eventId){
        this.eventController = eventController;
        
        receiptController = new ReceiptController();
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventHeaderPanel = new JPanel();
        eventHeaderPanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        HashMap subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        
        titleLabel = new JLabel("<HTML><U>Detalles del Evento</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        eventHeaderPanel.add(titleLabel);
        
        eventDateLabel = new JLabel("Fecha del Evento:");
        eventDateLabel.setBounds(30, 60, 130, 25);
        eventHeaderPanel.add(eventDateLabel);
        
        Date deliveryDate = new Date(((Timestamp)eventMap.get("deliveryDate")).getTime());
        deliveryDateTextField = new JTextField(deliveryDate.toString());
        deliveryDateTextField.setBounds(160, 60, 170, 25);
        deliveryDateTextField.setEditable(false);
        eventHeaderPanel.add(deliveryDateTextField);
        
        statusLabel = new JLabel("Estado:");
        statusLabel.setBounds(370, 60, 130, 25);
        eventHeaderPanel.add(statusLabel);
        
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
        statusComboBox.setEnabled(false);
        statusComboBox.setEditable(false);
        statusComboBox.setBounds(490, 60, 170, 25);
        statusComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //filter();
            }
        });
        eventHeaderPanel.add(statusComboBox);
        
        placeOfDeliveryLabel = new JLabel("Lugar de Entrega:");
        placeOfDeliveryLabel.setBounds(710, 60, 130, 25);
        eventHeaderPanel.add(placeOfDeliveryLabel);
        
        placeOfDeliveryTextField = new JTextField(eventMap.get("placeOfDelivery").toString());
        placeOfDeliveryTextField.setEditable(false);
        placeOfDeliveryTextField.setBounds(830, 60, 230, 25);
        eventHeaderPanel.add(placeOfDeliveryTextField);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        eventHeaderPanel.add(subjectLabel);
        
        totalSummaryLabel = new JLabel("<HTML><U>Totales</U></HTML>");
        totalSummaryLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        totalSummaryLabel.setBounds(1130, 110, 200, 25);
        eventHeaderPanel.add(totalSummaryLabel);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        eventHeaderPanel.add(subjectCodeLabel);
        
        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        eventHeaderPanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        eventHeaderPanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        eventHeaderPanel.add(subjectAddressTextField);
        
        //Total Evento
        totalLabel = new JLabel("Total: ");
        totalLabel.setBounds(1100, 150, 80, 25);
        eventHeaderPanel.add(totalLabel);
        
        totalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("netTotal").toString())));
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(1200, 150, 170, 25);
        eventHeaderPanel.add(totalTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        eventHeaderPanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        eventHeaderPanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        eventHeaderPanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        eventHeaderPanel.add(subjectTelephoneTextField);
        
        //Total IVA
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(1100, 180, 80, 25);
        eventHeaderPanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("totalTax").toString())));
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(1200, 180, 170, 25);
        eventHeaderPanel.add(totalTaxTextField);
        
        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 210, 120, 25);
        eventHeaderPanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        eventHeaderPanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        eventHeaderPanel.add(subjectFiscalNumberLabel);
        
        
        
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
        eventHeaderPanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        eventHeaderPanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        eventHeaderPanel.add(subjectCityTextField);
        
        //Boton para registrar pagos
        paymentRecordButton = new JButton("Registrar Pago");
        paymentRecordButton.setBounds(1250, 330, 160, 35);
        paymentRecordButton.setVisible(false);
        paymentRecordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                paymentRecordButtonAction();
            }
        });
        add(paymentRecordButton);
        
        //CREAMOS EL CONJUNTO DE PESTAÑAS
        tabs = new JTabbedPane();
        
        //DEFINIMOS EL TAMAÑO Y UBICACION DEL "PANEL DE PESTAÑAS" (?)
        tabs.setBounds(30, 280, 1200, 500);
        
        //Cargamos el Tab de Cargos del Evento
        addChargesTabComponent(eventMap);
        
        //Cargamos el Tab de Abonos/Pagos del Evento
        addPaymentsTabComponent();
        
        //Cargamos el Tab de Facturas del Evento
        addInvoicesTabComponent();
        
        ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            if(index==1){
                paymentRecordButton.setVisible(true);
            }else{
                paymentRecordButton.setVisible(false);
            }
            System.out.println("Tab changed to: " + index);
          }
        };
        tabs.addChangeListener(changeListener);
        
        //AGREGAMOS AL INTERNAL FRAME EL "PANEL DE PESTAÑAS" (?)
        add(tabs);
        
        add(eventHeaderPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Detalles del Evento");
        setBounds(150,30,1450,900);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        eventController.eventShowAndEditClosed();
        //eventController.setEventShowAndEditInMainWindow(30);
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
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
                                                        case QUANTITY_COLUMN_CHARGES:
                                                            int rows = ((ArrayList) eventMap.get("detail")).size();
                                                            return row >= rows;
                                                            default:    return false;
                                                    }
                                                }
           
    }
    
    private void addFuritureDetailToChargesTable(ArrayList furnitureDetailList){
        
        HashMap furnitureMap;
        
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureDetailList.size(); i++){
            furnitureMap = (HashMap) furnitureDetailList.get(i);
            row[ID_COLUMN] = furnitureMap.get("id");
            row[CODE_COLUMN_CHARGES] = furnitureMap.get("code");
            row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[TAX_RATE_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxRate"));
            row[UNIT_PRICE_COLUMN] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[PENALTY_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN_CHARGES] = furnitureMap.get("quantity");
            row[SUBTOTAL_COLUMN] = amountFormat.format((Double)furnitureMap.get("totalAmount"));
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    private void addChargesTabComponent(HashMap eventMap){
        //TABLA DE DETALLES PARA LA PRIMERO PESTAÑA QUE CONTENDRA LOS CARGOS DEL EVENTO
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
        eventDetailDefaultTableModel.addColumn("Precio Unitario");
        eventDetailDefaultTableModel.addColumn("Multa");
        eventDetailDefaultTableModel.addColumn("Cantidad");
        eventDetailDefaultTableModel.addColumn("Subtotal");
        
        eventDetailTable.setRowHeight(22);
        
        //ID
        eventDetailTable.getColumnModel().getColumn(0).setMaxWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setMinWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        ArrayList furnitureDetailList = (ArrayList) eventMap.get("detail");
        addFuritureDetailToChargesTable(furnitureDetailList);
        eventDetailTableJScrollPane = new JScrollPane(eventDetailTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        eventDetailTableJScrollPane.setBounds(0, 0, 1350, 300);
//        eventDetailTableJScrollPane.setPreferredSize(new Dimension(600, 60));
//        eventDetailTableJScrollPane.setViewportView(eventDetailTable);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(eventDetailTableJScrollPane, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(1190, 200));
        panel.setVisible(true);
        
        JPanel eventItemsPanel=new JPanel();
//        eventItemsPanel.setLayout (null);
//        eventItemsPanel.setAutoscrolls(true);
        eventItemsPanel.add(panel);
        //paymentPanel.setBounds(0, 0, 200, 200);
        //AÑADIMOS EL JPANEL
        tabs.addTab("", eventItemsPanel);
        
        //AÑADIMOS EL JSCROLLPANE CON LA TABLE ADENTRO
        //tabs.addTab("", eventDetailTableJScrollPane);
        
        //AÑADIMOS UN NOMBRE A LA PESTAÑA UNO
        JLabel lab = new JLabel("Cargos");
        lab.setHorizontalAlignment(JLabel.CENTER);
        lab.setPreferredSize(new Dimension(100, 30));
        tabs.setTabComponentAt(0, lab);
    }
    
    private void addPaymentsTabComponent(){
        //PANEL TEMPORAL PARA LA SEGUNDA PESTANHA QUE CONTENDRIA LOS ABONOS
        JPanel paymentPanel=new JPanel();
        paymentPanel.setLayout (null);
        //paymentPanel.setBounds(0, 0, 200, 200);
        //AÑADIMOS EL JPANEL
        tabs.addTab("", paymentPanel);
        
        //AÑADIMOS UN NOMBRE A LA PESTAÑA DOS
        JLabel lab = new JLabel("Abonos");
        lab.setHorizontalAlignment(JLabel.CENTER);
        lab.setPreferredSize(new Dimension(100, 30));
        tabs.setTabComponentAt(1, lab);
        
    }
    
    private void addInvoicesTabComponent(){
        //PANEL TEMPORAL PARA LA TERCERA PESTANHA QUE CONTENDRIA LAS FACTURAS
        JPanel panel3=new JPanel();
        //Componentes del panel3
        JLabel et_p3=new JLabel("Estas en el panel 3");
        panel3.add(et_p3);
 
        //AÑADIMOS EL JPANEL 3
        tabs.addTab("", panel3);
        
        //AÑADIMOS UN NOMBRE A LA PESTAÑA TRES
        JLabel lab = new JLabel("Facturas");
        lab.setHorizontalAlignment(JLabel.CENTER);
        lab.setPreferredSize(new Dimension(100, 30));
        tabs.setTabComponentAt(2, lab);
    }
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        tabs.setEnabled(false);
//        selectSubjectButton.setEnabled(false);
//        addFurnituresButton.setEnabled(false);
//        saveButton.setEnabled(false);
//        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        tabs.setEnabled(true);
//        selectSubjectButton.setEnabled(true);
//        addFurnituresButton.setEnabled(true);
//        saveButton.setEnabled(true);
//        cancelButton.setEnabled(true);
    }
    
    private void paymentRecordButtonAction(){
        
        receiptCreate = receiptController.getReceiptCreate();
        receiptCreate.setVisible(true);
        showSearchDialog(receiptCreate);
        inactivateElements();
        receiptCreate.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
//                if(!searchController.getFurnitureSelectedCode().isEmpty()){
//                    addFuritureSelectedToDetailTable(searchController.getFurnitureSelectedCode());
//                }
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
    
    private void showSearchDialog(Object dialogView){
        pane = getDesktopPane();
        pane.add((JInternalFrame) dialogView, JLayeredPane.MODAL_LAYER);
        pane.setVisible(true);
    }
}
