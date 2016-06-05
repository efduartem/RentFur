package rentfur.event;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import rentfur.furniture.FurnitureController;
import rentfur.receipt.ReceiptController;
import rentfur.receipt.ReceiptCreate;
import rentfur.receipt.ReceiptShow;
import rentfur.subject.SubjectController;
import rentfur.util.ComboBoxItem;
import rentfur.util.NumericTextField;
import rentfur.util.searches.FurniturePenaltySearch;
import rentfur.util.searches.FurnitureSearch;
import rentfur.util.searches.SearchController;

/**
 *
 * @author FDuarte
 */
public class EventShowAndEdit extends JInternalFrame{
    private final EventController eventController;
    private ReceiptCreate receiptCreate;
    private ReceiptShow receiptShow;
    private ReceiptController receiptController;
    private final SearchController searchController;
    private FurnitureSearch furnitureSearch;
    private FurniturePenaltySearch furniturePenaltySearch;
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
    private final JLabel observationLabel;
    private final JLabel totalLabel;
    private final JLabel balanceTotalLabel;
    private final JLabel invoicedTotalLabel;
    private final JLabel payedTotalLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JComboBox statusComboBox;
    private final JTextField deliveryDateTextField;
    private final JTextArea placeOfDeliveryTextArea;
    private final JTextArea observationTextArea;
    private final JTextField totalTextField;
    private final JTextField balanceTotalTextField;
    private final JTextField invoicedTotalTextField;
    private final JTextField payedTotalTextField;
    private JButton paymentRecordButton;
    private final JButton saveChangesButton;
    private final JButton addFurnitureButton;
    private final JButton addPenaltyButton;
    private final JButton closeButton;
    private final JTabbedPane tabs;
    private JDesktopPane pane;
    private JTable eventDetailTable;
    private DefaultTableModel eventDetailDefaultTableModel;
    private JScrollPane eventDetailTableJScrollPane;
    private DefaultTableModel eventDetailPenaltyDefaultTableModel;
    private JTable eventDetailpenaltyTable;
    private JScrollPane eventDetailPenaltyTableJScrollPane;
    private DefaultTableModel receiptDefaultTableModel;
    private JTable receiptTable;
    private JScrollPane receiptTableJScrollPane;
    private DefaultTableModel paymentMethodDefaultTableModel;
    private JTable paymentMethodTable;
    private JScrollPane paymentMethodTableJScrollPane;
    
    private static final int ID_COLUMN = 0;
    private static final int CODE_COLUMN_CHARGES = 1;
    private static final int DESCRIPTION_COLUMN = 2;
    private static final int TAX_RATE_COLUMN = 3;
    private static final int STOCK_COLUMN = 4;
    private static final int UNIT_PRICE_COLUMN = 5;
    private static final int PENALTY_COLUMN = 6;
    private static final int QUANTITY_COLUMN_CHARGES = 7;
    private static final int SUBTOTAL_COLUMN = 8;
    private static final int DELETE_BUTTON_COLUMN = 9;
    private static final int ANNEXED_COLUMN = 10;
    
    private static final int PENALTY_ID_COLUMN = 0;
    private static final int PENALTY_CODE_COLUMN_CHARGES = 1;
    private static final int PENALTY_DESCRIPTION_COLUMN = 2;
    private static final int PENALTY_TAX_RATE_COLUMN = 3;
    private static final int PENALTY_CONFIRMED_COLUMN = 4;
    private static final int PENALTY_QUANTITY_COLUMN_CHARGES = 5;
    private static final int PENALTY_PENALTY_COLUMN = 6;
    private static final int PENALTY_SUBTOTAL_COLUMN = 7;
    private static final int PENALTY_DELETE_BUTTON_COLUMN = 8;
    
    private static final int RECEIPT_ID_COLUMN = 0;
    private static final int RECEIPT_DATE_COLUMN = 1;
    private static final int RECEIPT_NUMBER_COLUMN = 2;
    private static final int RECEIPT_AMOUNT_COLUMN = 3;
    private static final int RECEIPT_CANCELLED_COLUMN = 4;
    private static final int RECEIPT_CANCELLED_DATE_COLUMN = 5;
    private static final int RECEIPT_CANCELLED_REASON_COLUMN = 6;
    private static final int RECEIPT_CANCELLED_BUTTON_COLUMN = 7;
    private static final int RECEIPT_SHOW_BUTTON_COLUMN = 8;
    
    
    private final int PAYMENT_METHOD_COLUMN = 0;
    private final int DOC_NUMBER_COLUMN = 1;
    private final int DOC_EMITION_DATE_COLUMN = 2;
    private final int DOC_PAYMENT_DATE_COLUMN = 3;
    private final int DOC_DUE_DATE_COLUMN = 4;
    private final int BANK_COLUMN = 5;
    private final int AMOUNT_COLUMN = 6;
    
    private HashMap eventMap;
    private HashMap subjectMap;
    private ArrayList receiptList;
    private int eventId;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    private final ArrayList furnitureCodesPenalized = new ArrayList();
    
    private static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    
    public EventShowAndEdit(EventController eventController, int eventId){
        this.eventController = eventController;
        
        searchController = new SearchController();
        receiptController = new ReceiptController();
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventHeaderPanel = new JPanel();
        eventHeaderPanel.setLayout(null);
        
        this.eventId = eventId;
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        receiptList = ReceiptController.getReceiptsByEventId(eventId);
        
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
        
        ComboBoxItem[] eventStatusAvailableComboBox = EventController.getEventStatusAvailablesForShowAndEditEvent(false, eventMap);
        
        ComboBoxItem eventStatusComboBoxItem = null;
        for (ComboBoxItem eventStatusComboBoxFor : eventStatusAvailableComboBox) {
            eventStatusComboBoxItem = eventStatusComboBoxFor;
            if(eventStatusComboBoxItem.getKey().equals(eventMap.get("status").toString())){
                break;
            }
        }
        
        statusComboBox = new JComboBox(eventStatusAvailableComboBox);
        statusComboBox.setSelectedItem(eventStatusComboBoxItem);
        statusComboBox.setBounds(490, 60, 170, 25);
        eventHeaderPanel.add(statusComboBox);
        
        placeOfDeliveryLabel = new JLabel("Lugar de Entrega:");
        placeOfDeliveryLabel.setBounds(710, 60, 130, 25);
        eventHeaderPanel.add(placeOfDeliveryLabel);
        
        placeOfDeliveryTextArea = new JTextArea(0,0);
        placeOfDeliveryTextArea.setText(eventMap.get("placeOfDelivery").toString());
        placeOfDeliveryTextArea.setLineWrap(true);
        placeOfDeliveryTextArea.setWrapStyleWord(true);
        JScrollPane placeOfDeliveryScrollPane = new JScrollPane();
        placeOfDeliveryScrollPane.setBounds(830, 60, 230, 90);
        placeOfDeliveryScrollPane.setViewportView(placeOfDeliveryTextArea);
        eventHeaderPanel.add(placeOfDeliveryScrollPane);
        
        observationLabel = new JLabel("Observaciones:");
        observationLabel.setBounds(710, 160, 130, 25);
        eventHeaderPanel.add(observationLabel);
        
        observationTextArea = new JTextArea(0,0);
        observationTextArea.setLineWrap(true);
        observationTextArea.setText(eventMap.get("observation").toString());
        observationTextArea.setWrapStyleWord(true);
        JScrollPane observationScrollPane = new JScrollPane();
        observationScrollPane.setBounds(830, 160, 230, 90);
        observationScrollPane.setViewportView(observationTextArea);
        eventHeaderPanel.add(observationScrollPane);
        
        if(((Integer)eventMap.get("status"))==EventController.CANCELED){
            observationTextArea.setEnabled(false);
            placeOfDeliveryTextArea.setEnabled(false);
        }
        
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
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(1100, 150, 80, 25);
        eventHeaderPanel.add(totalLabel);
        
        totalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("netTotal").toString())));
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(1220, 150, 170, 25);
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
        
        payedTotalLabel = new JLabel("Pagado Gs.: ");
        payedTotalLabel.setBounds(1100, 180, 80, 25);
        eventHeaderPanel.add(payedTotalLabel);
        
        payedTotalTextField = new JTextField();
        payedTotalTextField.setEditable(false);
        payedTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        payedTotalTextField.setBounds(1220, 180, 170, 25);
        eventHeaderPanel.add(payedTotalTextField);
        
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
        
         //Total SALDO
        balanceTotalLabel = new JLabel("Saldo Gs.: ");
        balanceTotalLabel.setBounds(1100, 210, 80, 25);
        eventHeaderPanel.add(balanceTotalLabel);
        
        balanceTotalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("balance").toString())));
        balanceTotalTextField.setEditable(false);
        balanceTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        balanceTotalTextField.setBounds(1220, 210, 170, 25);
        eventHeaderPanel.add(balanceTotalTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        eventHeaderPanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        eventHeaderPanel.add(subjectCityTextField);
        
        //FACTURADO
        invoicedTotalLabel = new JLabel("Facturado Gs.: ");
        invoicedTotalLabel.setBounds(1100, 240, 100, 25);
        eventHeaderPanel.add(invoicedTotalLabel);
        
        invoicedTotalTextField = new JTextField("0");
        invoicedTotalTextField.setEditable(false);
        invoicedTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        invoicedTotalTextField.setBounds(1220, 240, 170, 25);
        eventHeaderPanel.add(invoicedTotalTextField);
        
        //Boton para registrar pagos
        ImageIcon paymentRecordImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/money_24x24.png"));
        paymentRecordButton = new JButton(" Registrar Pago");
        paymentRecordButton.setIcon(paymentRecordImageIcon);
        paymentRecordButton.setBounds(1250, 330, 180, 35);
        paymentRecordButton.setVisible(false);
        paymentRecordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                paymentRecordButtonAction();
            }
        });
        add(paymentRecordButton);
        
        //Editar Presupuesto
        ImageIcon editEventImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveChangesButton = new JButton("   Guardar Cambios");
        saveChangesButton.setIcon(editEventImageIcon);
        saveChangesButton.setBounds(1250, 430, 180, 35);
        saveChangesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveChangesButtonAction();
            }
        });
        eventHeaderPanel.add(saveChangesButton);
        
        //Boton para Agregar Mobiliarios Anexos
        ImageIcon addFurnitureImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addFurnitureButton = new JButton(" Agregar Anexos");
        addFurnitureButton.setIcon(addFurnitureImageIcon);
        addFurnitureButton.setBounds(1250, 330, 180, 35);
        addFurnitureButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFurnituresButtonAction();
            }
        });
        add(addFurnitureButton);
        
        addPenaltyButton = new JButton("  Agregar Multa");
        addPenaltyButton.setIcon(addFurnitureImageIcon);
        addPenaltyButton.setBounds(1250, 380, 180, 35);
        addPenaltyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addPenaltyButtonAction();
            }
        });
        add(addPenaltyButton);
        
        ImageIcon closeIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        closeButton = new JButton("   Cerrar              ");
        closeButton.setIcon(closeIconImage);
        closeButton.setBounds(1250, 480, 180, 35);
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        add(closeButton);
        
        if(((Integer)eventMap.get("status"))==EventController.CANCELED){
            saveChangesButton.setEnabled(false);
            addFurnitureButton.setEnabled(false);
            addPenaltyButton.setEnabled(false);
            statusComboBox.setEnabled(false);
        }
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
            switch(index){
                case 0:
                    addFurnitureButton.setVisible(true);
                    addPenaltyButton.setVisible(true);
                    paymentRecordButton.setVisible(false);
                    break;
                case 1:
                    addFurnitureButton.setVisible(false);
                    addPenaltyButton.setVisible(false);
                    paymentRecordButton.setVisible(true);
                break;
                default:
                    addFurnitureButton.setVisible(true);
                    addPenaltyButton.setVisible(true);
                    paymentRecordButton.setVisible(false);
                
            }
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
        setBounds(150,30,1500,900);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void addPaymentsTabComponent(){
        //TABLA DE DETALLES DE PAGOS
        receiptDefaultTableModel = new receiptDefaultTableModel();
        receiptTable = new JTable(receiptDefaultTableModel);
        
        //Alineacion a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Alineacion a la derecha
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        receiptDefaultTableModel.addColumn("Id");
        receiptDefaultTableModel.addColumn("Fecha");
        receiptDefaultTableModel.addColumn("Numero");
        receiptDefaultTableModel.addColumn("Monto");
        receiptDefaultTableModel.addColumn("Anulado");
        receiptDefaultTableModel.addColumn("Fecha de Anulación");
        receiptDefaultTableModel.addColumn("Motivo de Anulación");
        receiptDefaultTableModel.addColumn("");
        receiptDefaultTableModel.addColumn("");
        
        ((DefaultTableCellRenderer)receiptTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        receiptTable.setRowHeight(22);
//        receiptTable.setBorder(BorderFactory.createEtchedBorder());
        receiptTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                    if(receiptTable.getSelectedRow()>0){
                        addPaymentsToReceiptTable((HashMap)receiptList.get(receiptTable.getSelectedRow()));
                    }else{
                        addPaymentsToReceiptTable((HashMap)receiptList.get(0));
                    }
                }
            }
        );
        
        //ID
        receiptTable.getColumnModel().getColumn(RECEIPT_ID_COLUMN).setMaxWidth(0);
        receiptTable.getColumnModel().getColumn(RECEIPT_ID_COLUMN).setMinWidth(0);
        receiptTable.getColumnModel().getColumn(RECEIPT_ID_COLUMN).setPreferredWidth(0);
        
        //NUMBER
        receiptTable.getColumnModel().getColumn(RECEIPT_NUMBER_COLUMN).setMaxWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_NUMBER_COLUMN).setMinWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_NUMBER_COLUMN).setPreferredWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_NUMBER_COLUMN).setCellRenderer(centerRenderer);
        
        //DATE
        receiptTable.getColumnModel().getColumn(RECEIPT_DATE_COLUMN).setMaxWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_DATE_COLUMN).setMinWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_DATE_COLUMN).setPreferredWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_DATE_COLUMN).setCellRenderer(centerRenderer);
        
        //AMOUNT
        receiptTable.getColumnModel().getColumn(RECEIPT_AMOUNT_COLUMN).setMaxWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_AMOUNT_COLUMN).setMinWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_AMOUNT_COLUMN).setPreferredWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_AMOUNT_COLUMN).setCellRenderer(rightRenderer);
        
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_COLUMN).setMaxWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_COLUMN).setMinWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_COLUMN).setPreferredWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_COLUMN).setCellRenderer(centerRenderer);
        
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_DATE_COLUMN).setMaxWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_DATE_COLUMN).setMinWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_DATE_COLUMN).setPreferredWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_DATE_COLUMN).setCellRenderer(centerRenderer);
        
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_REASON_COLUMN).setMaxWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_REASON_COLUMN).setMinWidth(160);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_REASON_COLUMN).setPreferredWidth(160);
        
        
        //CANCELLED BUTTON
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_BUTTON_COLUMN).setMaxWidth(130);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_BUTTON_COLUMN).setMinWidth(130);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_BUTTON_COLUMN).setPreferredWidth(130);
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_BUTTON_COLUMN).setCellRenderer(new ReceiptButtonRenderer());
        receiptTable.getColumnModel().getColumn(RECEIPT_CANCELLED_BUTTON_COLUMN).setCellEditor(new ReceiptButtonEditor(new JTextField()));
        
        //SHOW BUTTON
        receiptTable.getColumnModel().getColumn(RECEIPT_SHOW_BUTTON_COLUMN).setMaxWidth(90);
        receiptTable.getColumnModel().getColumn(RECEIPT_SHOW_BUTTON_COLUMN).setMinWidth(90);
        receiptTable.getColumnModel().getColumn(RECEIPT_SHOW_BUTTON_COLUMN).setPreferredWidth(90);
        receiptTable.getColumnModel().getColumn(RECEIPT_SHOW_BUTTON_COLUMN).setCellRenderer(new ReceiptButtonRenderer());
        receiptTable.getColumnModel().getColumn(RECEIPT_SHOW_BUTTON_COLUMN).setCellEditor(new ReceiptButtonEditor(new JTextField()));
        
        addReceiptsToReceiptTable(receiptList);
        
        receiptTableJScrollPane = new JScrollPane();
        receiptTableJScrollPane.setViewportView(receiptTable);
        
        JPanel headerPanel = new JPanel ();
        headerPanel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(new Color(79, 135, 155), 1), "Recibos", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.DIALOG, Font.BOLD, 16)));
        
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(100,30));
        headerPanel.add(receiptTableJScrollPane);
        
        //TEST
        //TABLA DE DETALLES PARA LA PRIMERO PESTAÑA QUE CONTENDRA LOS CARGOS DEL EVENTO
        paymentMethodDefaultTableModel = new paymentDetailDefaultTableModel();
        paymentMethodTable = new JTable(paymentMethodDefaultTableModel);
        
        paymentMethodDefaultTableModel.addColumn("Medio de Pago");
        paymentMethodDefaultTableModel.addColumn("Numero de Documento");
        paymentMethodDefaultTableModel.addColumn("Fecha de Emision");
        paymentMethodDefaultTableModel.addColumn("Fecha de Cobro");
        paymentMethodDefaultTableModel.addColumn("Vencimiento");
        paymentMethodDefaultTableModel.addColumn("Banco");
        paymentMethodDefaultTableModel.addColumn("Monto");
        paymentMethodDefaultTableModel.addColumn("");
        
        ((DefaultTableCellRenderer)paymentMethodTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        paymentMethodTable.setRowHeight(22);
//        paymentMethodTable.setBorder(BorderFactory.createEtchedBorder());
        
        //paymentMethodTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setCellEditor(new PaymentMethodCellEditor(ReceiptController.getPaymentMethodAvailablesForCreateReceipt(false)));
        //paymentMethodTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setCellRenderer(new PaymentMethodCellRenderer());
        
        paymentMethodTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setCellRenderer(centerRenderer);
        
        paymentMethodTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setCellRenderer(centerRenderer);

        paymentMethodTable.getColumnModel().getColumn(DOC_EMITION_DATE_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_EMITION_DATE_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_EMITION_DATE_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_EMITION_DATE_COLUMN).setCellRenderer(centerRenderer);
        
        paymentMethodTable.getColumnModel().getColumn(DOC_PAYMENT_DATE_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_PAYMENT_DATE_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_PAYMENT_DATE_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_PAYMENT_DATE_COLUMN).setCellRenderer(centerRenderer);
        
        paymentMethodTable.getColumnModel().getColumn(DOC_DUE_DATE_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_DUE_DATE_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_DUE_DATE_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(DOC_DUE_DATE_COLUMN).setCellRenderer(centerRenderer);
        
        paymentMethodTable.getColumnModel().getColumn(BANK_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(BANK_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(BANK_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(BANK_COLUMN).setCellRenderer(centerRenderer);
        
        //AMOUNT
        paymentMethodTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMinWidth(160);
        paymentMethodTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMaxWidth(160);
        paymentMethodTable.getColumnModel().getColumn(AMOUNT_COLUMN).setPreferredWidth(160);
        paymentMethodTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellRenderer(rightRenderer);
        
        if(!receiptList.isEmpty()){
            addPaymentsToReceiptTable((HashMap)receiptList.get(0));
        }
        
        
        paymentMethodTableJScrollPane = new JScrollPane();
        paymentMethodTableJScrollPane.setViewportView(paymentMethodTable);
        
        JPanel panel2 = new JPanel ();
        panel2.setBorder (BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(79, 135, 155), 1), "Detalles" , TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.DIALOG, Font.BOLD, 16)));
        panel2.setPreferredSize(new Dimension(100,30));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(paymentMethodTableJScrollPane);
        
        JPanel eventItemsPanel=new JPanel();
        eventItemsPanel.setLayout(new BoxLayout(eventItemsPanel, BoxLayout.Y_AXIS));        
        eventItemsPanel.add(headerPanel);
        eventItemsPanel.add(panel2);
        
        JScrollPane paymentsDetailJScrollPane = new JScrollPane();
        paymentsDetailJScrollPane.setViewportView(eventItemsPanel);
        tabs.addTab("", paymentsDetailJScrollPane);
        
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
    
    private void addChargesTabComponent(HashMap eventMap){
        //TABLA DE DETALLES PARA LA PRIMERO PESTAÑA QUE CONTENDRA LOS CARGOS DEL EVENTO
        eventDetailDefaultTableModel = new eventDetailDefaultTableModel();
        eventDetailTable = new JTable(eventDetailDefaultTableModel);
        
        TableCellRenderer renderer = new EvenOddRenderer();
        eventDetailTable.setDefaultRenderer(Object.class, renderer);
        
        eventDetailDefaultTableModel.addColumn("Id");
        eventDetailDefaultTableModel.addColumn("Código");
        eventDetailDefaultTableModel.addColumn("Descripción");
        eventDetailDefaultTableModel.addColumn("Tasa de Impuesto");
        eventDetailDefaultTableModel.addColumn("Disponibilidad");
        eventDetailDefaultTableModel.addColumn("Precio Unitario");
        eventDetailDefaultTableModel.addColumn("Multa");
        eventDetailDefaultTableModel.addColumn("Cantidad");
        eventDetailDefaultTableModel.addColumn("Subtotal");
        eventDetailDefaultTableModel.addColumn("");
        eventDetailDefaultTableModel.addColumn("Es Anexo");
        
        ((DefaultTableCellRenderer)eventDetailTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        eventDetailTable.setRowHeight(22);
//        eventDetailTable.setBorder(BorderFactory.createEtchedBorder());
        //ID
        eventDetailTable.getColumnModel().getColumn(0).setMaxWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setMinWidth(0);
        eventDetailTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //CODE
        eventDetailTable.getColumnModel().getColumn(CODE_COLUMN_CHARGES).setMaxWidth(60);
        eventDetailTable.getColumnModel().getColumn(CODE_COLUMN_CHARGES).setMinWidth(60);
        eventDetailTable.getColumnModel().getColumn(CODE_COLUMN_CHARGES).setPreferredWidth(60);
        
        //TAX RATE
        eventDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setMaxWidth(120);
        eventDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setMinWidth(120);
        eventDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setPreferredWidth(120);
        
        //STOCK_COLUMN
        eventDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setMaxWidth(115);
        eventDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setMinWidth(115);
        eventDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setPreferredWidth(115);
        
        //UNIT_PRICE_COLUMN
        eventDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setMaxWidth(115);
        eventDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setMinWidth(115);
        eventDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(115);
        
        //PENALTY_COLUMN
        eventDetailTable.getColumnModel().getColumn(PENALTY_COLUMN).setMaxWidth(115);
        eventDetailTable.getColumnModel().getColumn(PENALTY_COLUMN).setMinWidth(115);
        eventDetailTable.getColumnModel().getColumn(PENALTY_COLUMN).setPreferredWidth(115);
        
        //QUANTITY_COLUMN_CHARGES
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN_CHARGES).setMaxWidth(115);
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN_CHARGES).setMinWidth(115);
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN_CHARGES).setPreferredWidth(115);
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN_CHARGES).setHeaderRenderer(new SimpleQuantityHeaderRenderer());
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN_CHARGES).setCellEditor(new QuantityCellEditor());
        eventDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN_CHARGES).setCellRenderer(new QuantityCellRenderer());
        
        //SUBTOTAL_COLUMN
        eventDetailTable.getColumnModel().getColumn(SUBTOTAL_COLUMN).setMaxWidth(115);
        eventDetailTable.getColumnModel().getColumn(SUBTOTAL_COLUMN).setMinWidth(115);
        eventDetailTable.getColumnModel().getColumn(SUBTOTAL_COLUMN).setPreferredWidth(115);
        
        //DELETE BUTTON in Table
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMaxWidth(80);
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMinWidth(80);
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setResizable(false);
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        eventDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        
        //ANNEXED_COLUMN
        eventDetailTable.getColumnModel().getColumn(ANNEXED_COLUMN).setMaxWidth(0);
        eventDetailTable.getColumnModel().getColumn(ANNEXED_COLUMN).setMinWidth(0);
        eventDetailTable.getColumnModel().getColumn(ANNEXED_COLUMN).setPreferredWidth(0);
        
        ArrayList furnitureDetailList = (ArrayList) eventMap.get("detail");
        addFuritureDetailToChargesTable(furnitureDetailList);
        
        eventDetailTableJScrollPane = new JScrollPane();
        eventDetailTableJScrollPane.setViewportView(eventDetailTable);
        
        JPanel panel = new JPanel ();
        panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(new Color(79, 135, 155), 1), "Mobiliarios Contratados", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.DIALOG, Font.BOLD, 16)));
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(100,30));
        panel.add(eventDetailTableJScrollPane);
        
        //TEST
        //TABLA DE DETALLES PARA LA PRIMERO PESTAÑA QUE CONTENDRA LOS CARGOS DEL EVENTO
        eventDetailPenaltyDefaultTableModel = new eventDetailPenaltyDefaultTableModel();
        eventDetailpenaltyTable = new JTable(eventDetailPenaltyDefaultTableModel);
        
        eventDetailPenaltyDefaultTableModel.addColumn("Id");
        eventDetailPenaltyDefaultTableModel.addColumn("Código");
        eventDetailPenaltyDefaultTableModel.addColumn("Descripción");
        eventDetailPenaltyDefaultTableModel.addColumn("Tasa de Impuesto");
        eventDetailPenaltyDefaultTableModel.addColumn("Contratado");
        eventDetailPenaltyDefaultTableModel.addColumn("Cantidad Multada");
        eventDetailPenaltyDefaultTableModel.addColumn("Multa");
        eventDetailPenaltyDefaultTableModel.addColumn("Subtotal");
        eventDetailPenaltyDefaultTableModel.addColumn("");
        
        ((DefaultTableCellRenderer)eventDetailpenaltyTable.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        eventDetailpenaltyTable.setRowHeight(22);
//        eventDetailpenaltyTable.setBorder(BorderFactory.createEtchedBorder());
        
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_ID_COLUMN).setMaxWidth(0);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_ID_COLUMN).setMinWidth(0);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_ID_COLUMN).setPreferredWidth(0);
        
        //CODE
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_CODE_COLUMN_CHARGES).setMaxWidth(60);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_CODE_COLUMN_CHARGES).setMinWidth(60);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_CODE_COLUMN_CHARGES).setPreferredWidth(60);
        
        //PENALTY_TAX_RATE_COLUMN
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_TAX_RATE_COLUMN).setMaxWidth(120);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_TAX_RATE_COLUMN).setMinWidth(120);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_TAX_RATE_COLUMN).setPreferredWidth(120);
        
        //QUANTITY
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_QUANTITY_COLUMN_CHARGES).setHeaderRenderer(new SimpleQuantityHeaderRenderer());
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_QUANTITY_COLUMN_CHARGES).setCellEditor(new QuantityPenaltyCellEditor());
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_QUANTITY_COLUMN_CHARGES).setCellRenderer(new QuantityCellRenderer());
        
        //DELETE BUTTON in Table
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_DELETE_BUTTON_COLUMN).setMaxWidth(80);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_DELETE_BUTTON_COLUMN).setMinWidth(80);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_DELETE_BUTTON_COLUMN).setResizable(false);
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_DELETE_BUTTON_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        eventDetailpenaltyTable.getColumnModel().getColumn(PENALTY_DELETE_BUTTON_COLUMN).setCellEditor(new DeletePenaltyButtonEditor(new JTextField()));
        
        ArrayList penaltyFurnitureDetailList = (ArrayList) eventMap.get("penaltyDetail");
        addFuritureDetailToPenaltyChargesTable(penaltyFurnitureDetailList);
        
        eventDetailPenaltyTableJScrollPane = new JScrollPane();
        eventDetailPenaltyTableJScrollPane.setViewportView(eventDetailpenaltyTable);
        
        JPanel panel2 = new JPanel ();
        panel2.setBorder (BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(79, 135, 155), 1), "Multas" , TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.DIALOG, Font.BOLD, 16)));
        panel2.setPreferredSize(new Dimension(100,30));
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.add(eventDetailPenaltyTableJScrollPane);
        
        JPanel eventItemsPanel=new JPanel();
        eventItemsPanel.setLayout(new BoxLayout(eventItemsPanel, BoxLayout.Y_AXIS));        
        eventItemsPanel.add(panel);
        eventItemsPanel.add(panel2);
        
        JScrollPane eventDetailTableJScrollPane3 = new JScrollPane();
        eventDetailTableJScrollPane3.setViewportView(eventItemsPanel);
        
        //AÑADIMOS EL JPANEL
        tabs.addTab("", eventDetailTableJScrollPane3);
        
        //AÑADIMOS EL JSCROLLPANE CON LA TABLE ADENTRO
        //tabs.addTab("", eventDetailTableJScrollPane);
        
        //AÑADIMOS UN NOMBRE A LA PESTAÑA UNO
        JLabel lab = new JLabel("Cargos");
        lab.setHorizontalAlignment(JLabel.CENTER);
        lab.setPreferredSize(new Dimension(100, 30));
        tabs.setTabComponentAt(0, lab);
    }
    
    private void addFuritureDetailToChargesTable(ArrayList furnitureDetailList){
        
        HashMap furnitureMap;
        int stock;
        Date deliveryDate = new Date(((Timestamp)eventMap.get("deliveryDate")).getTime());
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureDetailList.size(); i++){
            furnitureMap = (HashMap) furnitureDetailList.get(i);
            row[ID_COLUMN] = furnitureMap.get("id");
            row[CODE_COLUMN_CHARGES] = furnitureMap.get("code");
            row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[TAX_RATE_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxRate"));
            stock = FurnitureController.getFurnitureStockByCodeAndDay(furnitureMap.get("code").toString(), deliveryDate);
            row[STOCK_COLUMN] = stock;
            row[UNIT_PRICE_COLUMN] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[PENALTY_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN_CHARGES] = furnitureMap.get("quantity");
            row[SUBTOTAL_COLUMN] = amountFormat.format((Double)furnitureMap.get("totalAmount"));
            row[DELETE_BUTTON_COLUMN] = "";
            row[ANNEXED_COLUMN] = (Boolean) furnitureMap.get("annexed");
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    private void addFuritureDetailToPenaltyChargesTable(ArrayList penaltyFurnitureDetailList){
        
        HashMap furnitureMap;
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < penaltyFurnitureDetailList.size(); i++){
            furnitureMap = (HashMap) penaltyFurnitureDetailList.get(i);
            furnitureCodesPenalized.add(furnitureMap.get("code"));
            
            row[ID_COLUMN] = furnitureMap.get("id");
            row[PENALTY_CODE_COLUMN_CHARGES] = furnitureMap.get("code");
            row[PENALTY_DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[PENALTY_TAX_RATE_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxRate"));
            row[PENALTY_CONFIRMED_COLUMN] = 10;
            row[PENALTY_QUANTITY_COLUMN_CHARGES] = amountFormat.format((Integer)furnitureMap.get("quantity"));
            row[PENALTY_PENALTY_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[PENALTY_SUBTOTAL_COLUMN] = amountFormat.format((Double)furnitureMap.get("totalAmount"));
            row[PENALTY_DELETE_BUTTON_COLUMN] = "";
        
            eventDetailPenaltyDefaultTableModel.addRow(row);
        }
    }
    
    private void addReceiptsToReceiptTable(ArrayList receiptList){
        try {
            HashMap receiptMap;
            double totalEventPayed = 0;
            int numeroRegistros = receiptDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistros;i++){
                receiptDefaultTableModel.removeRow(0);
            }
            
            Object[] row = new Object[receiptTable.getColumnCount()];
            for(int i = 0; i < receiptList.size(); i++){
                receiptMap = (HashMap) receiptList.get(i);
                
                row[RECEIPT_ID_COLUMN] = (Integer) receiptMap.get("id");
                row[RECEIPT_DATE_COLUMN] = new Date(((Timestamp)receiptMap.get("receiptDate")).getTime());
                row[RECEIPT_NUMBER_COLUMN] = receiptMap.get("receiptBranch") + "-" + receiptMap.get("receiptPrinter") + "-" + receiptMap.get("receiptNumber");
                
                row[RECEIPT_AMOUNT_COLUMN] = amountFormat.format((Double)receiptMap.get("totalPayed"));
                
                if((Boolean)receiptMap.get("cancelled")){
                    row[RECEIPT_CANCELLED_COLUMN] = "SI";
                }else{
                    row[RECEIPT_CANCELLED_COLUMN] = "NO";
                    totalEventPayed += (Double)receiptMap.get("totalPayed");
                }
                
                
                if(receiptMap.get("cancelledDate")!=null){
                    row[RECEIPT_CANCELLED_DATE_COLUMN] =  new Date(((Timestamp)receiptMap.get("cancelledDate")).getTime());
                }else{
                    row[RECEIPT_CANCELLED_DATE_COLUMN] = "";
                }
                
                row[RECEIPT_CANCELLED_REASON_COLUMN] = receiptMap.get("cancelledReason");
                row[RECEIPT_CANCELLED_BUTTON_COLUMN] = " Anular";
                row[RECEIPT_SHOW_BUTTON_COLUMN] = "Ver";
                
                receiptDefaultTableModel.addRow(row);
            }
            
            //TOTAL
            double total = amountFormat.parse(totalTextField.getText()).doubleValue();
            
            payedTotalTextField.setText(amountFormat.format(totalEventPayed));
            
            //TOTAL BALANCE
            double balanceTotal = total - totalEventPayed;
            balanceTotalTextField.setText(amountFormat.format(balanceTotal));
        } catch (ParseException ex) {
            Logger.getLogger(EventShowAndEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addPaymentsToReceiptTable(HashMap receiptMap){
        HashMap paymentMethodMap;
        ArrayList paymentList = (ArrayList) receiptMap.get("paymentMethod");
        Object[] row = new Object[paymentMethodTable.getColumnCount()];
        
        int numeroRegistros = paymentMethodDefaultTableModel.getRowCount();
        for(int i=0;i<numeroRegistros;i++){
            paymentMethodDefaultTableModel.removeRow(0);
        }
        
        for(int i = 0; i < paymentList.size(); i++){
            paymentMethodMap = (HashMap) paymentList.get(i);

            row[PAYMENT_METHOD_COLUMN] = paymentMethodMap.get("paymentType");
            row[DOC_NUMBER_COLUMN] = paymentMethodMap.get("documentNumber");
            if(paymentMethodMap.get("documentEmitionDate")!=null){
                row[DOC_EMITION_DATE_COLUMN] = new Date(((Timestamp)paymentMethodMap.get("documentEmitionDate")).getTime());
            }else{
                row[DOC_EMITION_DATE_COLUMN] = "";
            }
            
            if(paymentMethodMap.get("paymentDate")!=null){
                row[DOC_PAYMENT_DATE_COLUMN] = new Date(((Timestamp)paymentMethodMap.get("paymentDate")).getTime());
            }else{
                row[DOC_PAYMENT_DATE_COLUMN] = "";
            }
            
            if(paymentMethodMap.get("dueDate")!=null){
                row[DOC_DUE_DATE_COLUMN] =  new Date(((Timestamp)paymentMethodMap.get("dueDate")).getTime());
            }else{
                row[DOC_DUE_DATE_COLUMN] = "";
            }
            
            row[BANK_COLUMN] = paymentMethodMap.get("bankName");
            row[AMOUNT_COLUMN] = amountFormat.format((Double)paymentMethodMap.get("totalAmount"));
        
            paymentMethodDefaultTableModel.addRow(row);
        }
    }
    
    
    
    private void saveChangesButtonAction(){
        JOptionPane optionPane;
        JDialog dialog;
        
        int currentEventStatus  = (Integer) eventMap.get("status");
        ComboBoxItem statusComboBoxItem = (ComboBoxItem) statusComboBox.getSelectedItem();
        int status = Integer.valueOf(statusComboBoxItem.getKey());
        
        ArrayList currentFurnitureDetailList = (ArrayList) eventMap.get("detail");
        ArrayList currentPenaltyFurnitureDetailList = (ArrayList) eventMap.get("penaltyDetail");
        
        if(!allQuantityAdded()){
            optionPane = new JOptionPane("Existen detalles con cantidad 0 (Cero). Favor ingrese correctamente todas las cantidades.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(placeOfDeliveryTextArea.getText().equals("")){
            optionPane = new JOptionPane("Favor ingrese el lugar de Entrega", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            
            String placeOfDelivery = placeOfDeliveryTextArea.getText();
            String observation = observationTextArea.getText();
            HashMap furnitureMap;
            ArrayList furnitureList = new ArrayList();
            ArrayList penaltyFurnitureList = new ArrayList();
            Vector dataVector;
            double netTotal = 0;
            double balanceTotal = 0;
            try {
            
                for (int row = 0; row < eventDetailTable.getRowCount(); row++){
                    if(row >= currentFurnitureDetailList.size()){
                        dataVector = (Vector) eventDetailDefaultTableModel.getDataVector().get(row);
                        furnitureMap = new HashMap();
                        furnitureMap.put("code",dataVector.get(CODE_COLUMN_CHARGES));
                        furnitureMap.put("taxRate",amountFormat.parse(dataVector.get(TAX_RATE_COLUMN).toString()).doubleValue());
                        furnitureMap.put("quantity",amountFormat.parse(dataVector.get(QUANTITY_COLUMN_CHARGES).toString()).intValue());
                        furnitureMap.put("subTotal", amountFormat.parse(dataVector.get(SUBTOTAL_COLUMN).toString()).doubleValue());
                        furnitureMap.put("annexed", (Boolean) dataVector.get(ANNEXED_COLUMN));
                        furnitureMap.put("penalty", Boolean.FALSE);
                        furnitureList.add(furnitureMap);
                    }
                }
                
                for (int row = 0; row < eventDetailpenaltyTable.getRowCount(); row++){
                    if(row >= currentPenaltyFurnitureDetailList.size()){
                        dataVector = (Vector) eventDetailPenaltyDefaultTableModel.getDataVector().get(row);
                        furnitureMap = new HashMap();
                        furnitureMap.put("code",dataVector.get(PENALTY_CODE_COLUMN_CHARGES));
                        furnitureMap.put("taxRate",amountFormat.parse(dataVector.get(PENALTY_TAX_RATE_COLUMN).toString()).doubleValue());
                        furnitureMap.put("quantity",amountFormat.parse(dataVector.get(PENALTY_QUANTITY_COLUMN_CHARGES).toString()).intValue());
                        furnitureMap.put("subTotal", amountFormat.parse(dataVector.get(PENALTY_SUBTOTAL_COLUMN).toString()).doubleValue());
                        furnitureMap.put("annexed", Boolean.FALSE);
                        furnitureMap.put("penalty", Boolean.TRUE);
                        penaltyFurnitureList.add(furnitureMap);
                    }
                }

                netTotal = amountFormat.parse(totalTextField.getText()).doubleValue();
                balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
            
            } catch (ParseException ex) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            HashMap returnMap = eventController.updateEventConfirmed(subjectMap, eventMap, status, placeOfDelivery, observation, furnitureList, penaltyFurnitureList, netTotal, balanceTotal);
            if(((Integer)returnMap.get("status"))==EventController.SUCCESFULLY_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
            }else if((Integer)returnMap.get("status") == EventController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void showReceiptView(int row){
        int receiptId = (Integer) receiptDefaultTableModel.getValueAt(row, RECEIPT_ID_COLUMN);
        
        receiptShow = receiptController.getReceiptShow(eventId, receiptId);
        receiptShow.setVisible(true);
        showSearchDialog(receiptShow);
        inactivateElements();
        receiptShow.addInternalFrameListener(new InternalFrameListener() {

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
    
    private boolean allQuantityAdded(){
        Vector dataVector;
        try {
            for (int row = 0; row < eventDetailTable.getRowCount(); row++){
               dataVector = (Vector) eventDetailDefaultTableModel.getDataVector().get(row);
                   if(amountFormat.parse(dataVector.get(QUANTITY_COLUMN_CHARGES).toString()).intValue()==0){
                       return false;
                   }
           }
            
           for (int row = 0; row < eventDetailpenaltyTable.getRowCount(); row++){
               dataVector = (Vector) eventDetailPenaltyDefaultTableModel.getDataVector().get(row);
                   if(amountFormat.parse(dataVector.get(PENALTY_QUANTITY_COLUMN_CHARGES).toString()).intValue()==0){
                       return false;
                   }
           } 
        } catch (ParseException ex) {
            Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private void paymentRecordButtonAction(){
        receiptCreate = receiptController.getReceiptCreate(eventId);
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
                updateReceiptsTable();
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
    
    private void updateReceiptsTable(){
        receiptList = ReceiptController.getReceiptsByEventId(eventId);
        addReceiptsToReceiptTable(receiptList);
        if(!receiptList.isEmpty()){
            addPaymentsToReceiptTable((HashMap)receiptList.get(0));
        }
    }
    
    private void showSearchDialog(Object dialogView){
        pane = getDesktopPane();
        pane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        pane.setVisible(true);
    }
    
    private void addFurnituresButtonAction(){
        Date deliveryDate = new Date(((Timestamp)eventMap.get("deliveryDate")).getTime());
        furnitureSearch = searchController.getFurnitureSearch(furnitureCodesAdded, deliveryDate);
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
    
    private void addPenaltyButtonAction(){
        
        ArrayList furnitureDetailList = (ArrayList) eventMap.get("detail");
        furniturePenaltySearch = searchController.getFurniturePenaltySearch(furnitureCodesPenalized, furnitureDetailList);
        furniturePenaltySearch.setVisible(true);
        showSearchDialog(furniturePenaltySearch);
        inactivateElements();
        furniturePenaltySearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
                if(!searchController.getFurniturePenalizedCodes().isEmpty()){
                    addFuritureSelectedToPenaltyDetailTable(searchController.getFurniturePenalizedCodes());
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
    
    private void addFuritureSelectedToPenaltyDetailTable(ArrayList furnitureCodes){
        
        HashMap furnitureMap;
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeList(furnitureCodes);
        
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesPenalized.add(furnitureMap.get("code"));
            
            row[ID_COLUMN] = furnitureMap.get("id");
            row[PENALTY_CODE_COLUMN_CHARGES] = furnitureMap.get("code");
            row[PENALTY_DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[PENALTY_TAX_RATE_COLUMN] = amountFormat.format((Integer)furnitureMap.get("taxRate"));
            row[PENALTY_CONFIRMED_COLUMN] = getFurtnitureConfirmedQuantity(furnitureMap.get("code").toString());
            row[PENALTY_QUANTITY_COLUMN_CHARGES] = 0;
            row[PENALTY_PENALTY_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[PENALTY_SUBTOTAL_COLUMN] = 0;
            row[PENALTY_DELETE_BUTTON_COLUMN] = "";
        
            eventDetailPenaltyDefaultTableModel.addRow(row);
        }
    }
    
    private int getFurtnitureConfirmedQuantity(String furnitureCode){
        
        String furnitureCodeInTable;
        int valueToReturn = 0;
        try {
            for(int i = 0; i < eventDetailDefaultTableModel.getRowCount(); i++){
                furnitureCodeInTable = eventDetailDefaultTableModel.getValueAt(i, CODE_COLUMN_CHARGES).toString();
                if(furnitureCodeInTable.equals(furnitureCode)){
                        valueToReturn += amountFormat.parse((eventDetailDefaultTableModel.getValueAt(i, QUANTITY_COLUMN_CHARGES).toString())).intValue();
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(EventShowAndEdit.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return valueToReturn;
    }
    
    private void addFuritureSelectedToDetailTable(ArrayList furnitureCodes){
        
        HashMap furnitureMap;
        Date deliveryDate = new Date(((Timestamp)eventMap.get("deliveryDate")).getTime());
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeWithDayStock(furnitureCodes, deliveryDate);
        
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesAdded.add(furnitureMap.get("code"));
            
            row[ID_COLUMN] = furnitureMap.get("id");
            row[CODE_COLUMN_CHARGES] = furnitureMap.get("code");
            row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[TAX_RATE_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxRate"));
            row[STOCK_COLUMN] = amountFormat.format(Long.valueOf(furnitureMap.get("stockAvailable").toString()));
            row[UNIT_PRICE_COLUMN] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[PENALTY_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN_CHARGES] = 0;
            row[SUBTOTAL_COLUMN] = 0;
            row[DELETE_BUTTON_COLUMN] = "";
            row[ANNEXED_COLUMN] = true;
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    private void removePenaltyRow(int row) throws ParseException{
        
        double subTotal = amountFormat.parse(eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_SUBTOTAL_COLUMN).toString()).doubleValue();
        
        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = total - subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL BALANCE
        double balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
        balanceTotal = balanceTotal - subTotal;
        balanceTotalTextField.setText(amountFormat.format(balanceTotal));
        
        String furnitureCode = eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_CODE_COLUMN_CHARGES).toString();
        furnitureCodesPenalized.remove(furnitureCode);
        eventDetailPenaltyDefaultTableModel.removeRow(row);
    }
    
     private void removeRow(int row) throws ParseException{
        
        double subTotal = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, SUBTOTAL_COLUMN).toString()).doubleValue();

        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = total - subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL BALANCE
        double balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
        balanceTotal = balanceTotal - subTotal;
        balanceTotalTextField.setText(amountFormat.format(balanceTotal));
        
        String furnitureCode = eventDetailDefaultTableModel.getValueAt(row, CODE_COLUMN_CHARGES).toString();
        furnitureCodesAdded.remove(furnitureCode);
        eventDetailDefaultTableModel.removeRow(row);
    }
     
     private void updateSubTotal(int newQuantity, int row, int column) throws ParseException{
        //SubTotal
        double oldSubTotal = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, SUBTOTAL_COLUMN).toString()).doubleValue();
        Number unitPrice = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, UNIT_PRICE_COLUMN).toString());
        double subTotal = newQuantity * unitPrice.doubleValue();
        eventDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, SUBTOTAL_COLUMN);
        
        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = (total - oldSubTotal) + subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL BALANCE
        double balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
        balanceTotal = (balanceTotal - oldSubTotal) + subTotal;
        balanceTotalTextField.setText(amountFormat.format(balanceTotal));
    }
     
     private void updatePenaltySubTotal(int newQuantity, int row, int column) throws ParseException{
        //SubTotal
        double oldSubTotal = amountFormat.parse(eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_SUBTOTAL_COLUMN).toString()).doubleValue();
        Number penalty = amountFormat.parse(eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_PENALTY_COLUMN).toString());
        double subTotal = newQuantity * penalty.doubleValue();
        eventDetailPenaltyDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, PENALTY_SUBTOTAL_COLUMN);

        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = (total - oldSubTotal) + subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL BALANCE
        double balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
        balanceTotal = (balanceTotal - oldSubTotal) + subTotal;
        balanceTotalTextField.setText(amountFormat.format(balanceTotal));
    }
     
     public void updateReceiptStatus(int row){
        Vector dataVector = (Vector) receiptDefaultTableModel.getDataVector().get(row);
        int receiptId = (Integer) dataVector.get(RECEIPT_ID_COLUMN);
        String receiptNumber = (String) dataVector.get(RECEIPT_NUMBER_COLUMN);

        int respuesta;
        HashMap mapReturn;
        JTextArea textArea = new JTextArea(6, 50);
        textArea.setEditable(true);
        
        JLabel messagge = new JLabel("Confirma que desea anular el recibo "+receiptNumber+"?");
        JLabel reasonLabel = new JLabel("Favor ingresar un motivo para la anulcación");
        JPanel confirmPanel = new JPanel();
        confirmPanel.setPreferredSize(new Dimension(400,150));

        confirmPanel.add(messagge);
        confirmPanel.add(reasonLabel);
        confirmPanel.add(textArea);
        // display them in a message dialog
        respuesta = JOptionPane.showConfirmDialog(this, confirmPanel,"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(respuesta == JOptionPane.YES_OPTION){
            if(textArea.getText().trim().equals("")){
                JOptionPane.showMessageDialog(null, "Debe ingresar un motivo para confirmar la anulación", "Atencion", JOptionPane.WARNING_MESSAGE);
                updateReceiptStatus(row);
            }else{
                mapReturn = receiptController.cancelReceipt(receiptId, textArea.getText().trim());
                if((Integer) mapReturn.get("status") == ReceiptController.SUCCESFULLY_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                }else if((Integer)mapReturn.get("status") == ReceiptController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                }
                updateReceiptsTable();
            }
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
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            JOptionPane optionPane;
            JDialog dialog;
            try {
                int oldQuantity = amountFormat.parse((eventDetailDefaultTableModel.getValueAt(row, QUANTITY_COLUMN_CHARGES).toString())).intValue();
                String newQuantityString = ((JTextField) component).getText();
                if(newQuantityString.equals("")){
                    newQuantityString = "0";
                }
                int newQuantity = amountFormat.parse(newQuantityString).intValue();
                double stockAvailable = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, STOCK_COLUMN).toString()).doubleValue();
                if(stockAvailable < newQuantity){
                    optionPane = new JOptionPane("El valor ingresado supera la cantidad disponible de este mobiliario", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    dialog = optionPane.createDialog(null, "Atencion!");
                    dialog.setVisible(true);
                    ((JTextField) component).setText(amountFormat.format(oldQuantity));
                }else{
                    ((JTextField) component).setText(amountFormat.format(newQuantity));
                    updateSubTotal(newQuantity, row, column);
                }
            } catch (HeadlessException | ParseException th) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, th);
            }
            return ((JTextField) component).getText();
        }

    }
    
    class QuantityPenaltyCellEditor extends AbstractCellEditor implements TableCellEditor {

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
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            return component;
        }

        @Override
        public Object getCellEditorValue() {
            JOptionPane optionPane;
            JDialog dialog;
            try {
                int oldQuantity = amountFormat.parse((eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_QUANTITY_COLUMN_CHARGES).toString())).intValue();
                String newQuantityString = ((JTextField) component).getText();
                if(newQuantityString.equals("")){
                    newQuantityString = "0";
                }
                int newQuantity = amountFormat.parse(newQuantityString).intValue();
                double confirmedQuantity = amountFormat.parse(eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_CONFIRMED_COLUMN).toString()).doubleValue();
                if(confirmedQuantity < newQuantity){
                    optionPane = new JOptionPane("El valor ingresado supera la cantidad contratada de este mobiliario", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    dialog = optionPane.createDialog(null, "Atencion!");
                    dialog.setVisible(true);
                    ((JTextField) component).setText(amountFormat.format(oldQuantity));
                }else{
                    ((JTextField) component).setText(amountFormat.format(newQuantity));
                    updatePenaltySubTotal(newQuantity, row, column);
                }
            } catch (HeadlessException | ParseException th) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, th);
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
    
    private class SimpleQuantityHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public SimpleQuantityHeaderRenderer() {
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
    
    class EvenOddRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            boolean annexed = Boolean.valueOf(table.getModel().getValueAt(row, ANNEXED_COLUMN).toString());
            Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ((JLabel) renderer).setOpaque(true);
            Color foreground;
            Color background;
            
            if (annexed) {
                foreground = Color.black;
                background = new Color(244, 249, 199);
                renderer.setForeground(foreground);
                renderer.setBackground(background);
            }else{
                foreground = Color.black;
                background = Color.white;
                renderer.setForeground(foreground);
                renderer.setBackground(background);
            }

            if(column == TAX_RATE_COLUMN || column == STOCK_COLUMN || column == UNIT_PRICE_COLUMN || column == PENALTY_COLUMN || column == QUANTITY_COLUMN_CHARGES || column == SUBTOTAL_COLUMN){
                ((JLabel) renderer).setHorizontalAlignment(JLabel.RIGHT);
            }else{
                ((JLabel) renderer).setHorizontalAlignment(JLabel.LEFT);
            }
            
            return renderer;
        }
      }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        eventController.eventShowAndEditClosed();
        //eventController.setEventShowAndEditInMainWindow(30);
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void iconifiedInternalFrame(){
        try {
            this.setIcon(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(EventIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deIconifiedInternalFrame(){
        try {
            this.setIcon(false);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(EventIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        tabs.setEnabled(false);
        paymentRecordButton.setEnabled(false);
        addFurnitureButton.setEnabled(false);
        addPenaltyButton.setEnabled(false);
        saveChangesButton.setEnabled(false);
        closeButton.setEnabled(false);
        eventDetailTable.setEnabled(false);
        eventDetailpenaltyTable.setEnabled(false);
        receiptTable.setEnabled(false);
        paymentMethodTable.setEnabled(false);
        statusComboBox.setEnabled(false);
        placeOfDeliveryTextArea.setEnabled(false);
        observationTextArea.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        tabs.setEnabled(true);
        paymentRecordButton.setEnabled(true);
        addFurnitureButton.setEnabled(true);
        addPenaltyButton.setEnabled(true);
        saveChangesButton.setEnabled(true);
        closeButton.setEnabled(true);
        eventDetailTable.setEnabled(true);
        eventDetailpenaltyTable.setEnabled(true);
        receiptTable.setEnabled(true);
        paymentMethodTable.setEnabled(true);
        statusComboBox.setEnabled(true);
        placeOfDeliveryTextArea.setEnabled(true);
        observationTextArea.setEnabled(true);
    }
    
    private class eventDetailDefaultTableModel extends DefaultTableModel{
         
        @Override
        public boolean isCellEditable(int row, int column) {
            int rows;
            switch(column){
                case QUANTITY_COLUMN_CHARGES:
                     rows = ((ArrayList) eventMap.get("detail")).size();
                    return row >= rows;
                case DELETE_BUTTON_COLUMN:
                    rows = ((ArrayList) eventMap.get("detail")).size();
                    return row >= rows;
                default:    return false;
            }
        }
           
    }
    
    private class eventDetailPenaltyDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
                                                    switch(column){
                                                        case PENALTY_QUANTITY_COLUMN_CHARGES:
                                                            return true;
                                                        case PENALTY_DELETE_BUTTON_COLUMN:
                                                            return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    private class receiptDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            //int rows;
            switch(column){
                case RECEIPT_CANCELLED_BUTTON_COLUMN:
                    boolean cancelled = false;
                    if(receiptDefaultTableModel.getValueAt(row, RECEIPT_CANCELLED_COLUMN).toString().equals("SI")){
                        cancelled = Boolean.TRUE;
                    }
                    return !cancelled;
                case RECEIPT_SHOW_BUTTON_COLUMN:
                    return true;
                default:    return false;
            }
        }
        
    }
    
    private class paymentDetailDefaultTableModel extends DefaultTableModel{
         
        @Override
        public boolean isCellEditable(int row, int column) {
            //int rows;
            switch(column){
                default:    return false;
            }
        }
           
    }
    
    private class DeleteButtonRenderer extends JButton implements TableCellRenderer {

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
    
    private class DeletePenaltyButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int row;

        private int column;
        
        public DeletePenaltyButtonEditor(JTextField jtf) {
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
                removePenaltyRow(row);
            } catch (ParseException ex) {
                Logger.getLogger(EventEdit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      }
    
    class ReceiptButtonRenderer extends JButton implements TableCellRenderer {

        public ReceiptButtonRenderer() {
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
          if(column==RECEIPT_CANCELLED_BUTTON_COLUMN){
              ImageIcon deleteIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/erase_16x16.png"));
              setIcon(deleteIconImage);
              //if(onlyQuery){
//                    String message = "Su usuario solo cuenta con permiso de consultas";
//                    setEnabled(false);
//                    setToolTipText(message);
              //  }
          }
          setText((value == null) ? "" : value.toString());
          return this;
        }
      }

      /**
       * @version 1.0 11/09/98
       */

     class ReceiptButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int row;

        private int column;
        
        public ReceiptButtonEditor(JTextField jtf) {
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
                if(column==RECEIPT_CANCELLED_BUTTON_COLUMN){
//                    if(!onlyQuery){
                       //label = updateFurnitureStatus(row, label);
//                    }
                    updateReceiptStatus(row);
                }else if(column==RECEIPT_SHOW_BUTTON_COLUMN){
                    //showFurnitureShowAndEditView(row);
                    showReceiptView(row);
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
