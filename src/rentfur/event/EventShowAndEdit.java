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
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import rentfur.furniture.FurnitureController;
import rentfur.receipt.ReceiptController;
import rentfur.receipt.ReceiptCreate;
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
    private static final int PENALTY_PENALTY_COLUMN = 4;
    private static final int PENALTY_QUANTITY_COLUMN_CHARGES = 5;
    private static final int PENALTY_SUBTOTAL_COLUMN = 6;
    private static final int PENALTY_DELETE_BUTTON_COLUMN = 7;
    
    private HashMap eventMap;
    private HashMap subjectMap;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    private final ArrayList furnitureCodesPenalized = new ArrayList();
    
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    
    public EventShowAndEdit(EventController eventController, int eventId){
        this.eventController = eventController;
        
        searchController = new SearchController();
        receiptController = new ReceiptController();
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventHeaderPanel = new JPanel();
        eventHeaderPanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        
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
        
        ComboBoxItem[] eventStatusAvailableComboBox = EventController.getEventStatusAvailablesForShowAndEditEvent(false);
        
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
        balanceTotalLabel = new JLabel("Saldo: ");
        balanceTotalLabel.setBounds(1100, 180, 80, 25);
        eventHeaderPanel.add(balanceTotalLabel);
        
        balanceTotalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("balance").toString())));
        balanceTotalTextField.setEditable(false);
        balanceTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        balanceTotalTextField.setBounds(1200, 180, 170, 25);
        eventHeaderPanel.add(balanceTotalTextField);
        
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
            saveChangesButton.setVisible(false);
            addFurnitureButton.setVisible(false);
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
            if(index==1 && ((Integer)eventMap.get("status"))!=EventController.CANCELED){
                paymentRecordButton.setVisible(true);
            }else{
                paymentRecordButton.setVisible(false);
            }
            
            if(index==0 && ((Integer)eventMap.get("status"))!=EventController.CANCELED){
                addFurnitureButton.setVisible(true);
            }else{
                addFurnitureButton.setVisible(false);
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
        
        eventDetailTable.setRowHeight(22);
        eventDetailTable.setBorder(BorderFactory.createEtchedBorder());
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
        if(((Integer)eventMap.get("status"))==EventController.CONFIRMED){
            panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(new Color(79, 135, 155), 1), "Mobiliarios Contratados", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.DIALOG, Font.BOLD, 16)));
        }else{
            panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createLineBorder(new Color(79, 135, 155), 1), "Mobiliarios a Contratar", TitledBorder.CENTER, TitledBorder.TOP, new Font(Font.DIALOG, Font.BOLD, 16)));
        }
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(100,30));
        panel.add(eventDetailTableJScrollPane);
        
        //TEST
        //TABLA DE DETALLES PARA LA PRIMERO PESTAÑA QUE CONTENDRA LOS CARGOS DEL EVENTO
        eventDetailPenaltyDefaultTableModel = new eventDetailAnnexedDefaultTableModel();
        eventDetailpenaltyTable = new JTable(eventDetailPenaltyDefaultTableModel);
        
        eventDetailPenaltyDefaultTableModel.addColumn("Id");
        eventDetailPenaltyDefaultTableModel.addColumn("Código");
        eventDetailPenaltyDefaultTableModel.addColumn("Descripción");
        eventDetailPenaltyDefaultTableModel.addColumn("Tasa de Impuesto");
        eventDetailPenaltyDefaultTableModel.addColumn("Multa");
        eventDetailPenaltyDefaultTableModel.addColumn("Cantidad");
        eventDetailPenaltyDefaultTableModel.addColumn("Subtotal");
        eventDetailPenaltyDefaultTableModel.addColumn("");
        
        eventDetailpenaltyTable.setRowHeight(22);
        eventDetailpenaltyTable.setBorder(BorderFactory.createEtchedBorder());
        
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
        int stock = 0;
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
            row[ANNEXED_COLUMN] = false;
        
            eventDetailDefaultTableModel.addRow(row);
        }
    }
    
    public void saveChangesButtonAction(){
        
        ComboBoxItem statusComboBoxItem = (ComboBoxItem) statusComboBox.getSelectedItem();
        int status = Integer.valueOf(statusComboBoxItem.getKey());
            
        HashMap mapReturn = eventController.updateEventConfirmed(subjectMap, eventMap, status);
        System.out.println("mapReturn: "+mapReturn);
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
        Date deliveryDate = new Date(((Timestamp)eventMap.get("deliveryDate")).getTime());
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeWithDayStock(furnitureCodes, deliveryDate);
        
        Object[] row = new Object[eventDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesPenalized.add(furnitureMap.get("code"));
            
            row[ID_COLUMN] = furnitureMap.get("id");
            row[PENALTY_CODE_COLUMN_CHARGES] = furnitureMap.get("code");
            row[PENALTY_DESCRIPTION_COLUMN] = furnitureMap.get("description");
            row[PENALTY_TAX_RATE_COLUMN] = amountFormat.format((Double)furnitureMap.get("taxRate"));
            row[PENALTY_PENALTY_COLUMN] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[PENALTY_QUANTITY_COLUMN_CHARGES] = 0;
            row[PENALTY_SUBTOTAL_COLUMN] = 0;
            row[PENALTY_DELETE_BUTTON_COLUMN] = "";
        
            eventDetailPenaltyDefaultTableModel.addRow(row);
        }
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
        String furnitureCode = eventDetailPenaltyDefaultTableModel.getValueAt(row, PENALTY_CODE_COLUMN_CHARGES).toString();
        furnitureCodesPenalized.remove(furnitureCode);
        eventDetailPenaltyDefaultTableModel.removeRow(row);
    }
    
     private void removeRow(int row) throws ParseException{
        
        double subTotal = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, SUBTOTAL_COLUMN).toString()).doubleValue();
        
        //Item tax
//        String furnitureTaxRateString = eventDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
//        double itemTaxAmount =0;
//        int taxIndexIntable = 0;
//        if(!furnitureTaxRateString.equals("0")){
//            
//            taxIndexIntable = SUB_TOTAL_COLUMN + Integer.valueOf(taxList.indexOf(furnitureTaxRateString)) + 1;
//            itemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, taxIndexIntable).toString()).doubleValue();
//            
//        }

        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = total - subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL IVA
//        double totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
//        totalTax = totalTax - itemTaxAmount;
//        totalTaxTextField.setText(amountFormat.format(totalTax));
        
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
        
        //Item tax
//        String furnitureTaxRateString = eventDetailDefaultTableModel.getValueAt(row, ANNEXED_TAX_RATE_COLUMN).toString();
//        double oldItemTaxAmount =0;
//        double furnitureTaxRate = 0;
//        double itemTax = 0;
//        int taxIndexIntable = 0;
//        if(!furnitureTaxRateString.equals("0")){
//            
//            taxIndexIntable = ANNEXED_SUBTOTAL_COLUMN + Integer.valueOf(taxList.indexOf(furnitureTaxRateString)) + 1;
//            oldItemTaxAmount = amountFormat.parse(eventDetailDefaultTableModel.getValueAt(row, taxIndexIntable).toString()).doubleValue();
//            
//            furnitureTaxRate = Double.valueOf((String)taxRatioMap.get(furnitureTaxRateString));
//            itemTax = new BigDecimal(subTotal / furnitureTaxRate).setScale(0, RoundingMode.HALF_UP).doubleValue();
//            eventDetailDefaultTableModel.setValueAt(amountFormat.format(itemTax), row, taxIndexIntable);
//            
//        }
        
        
        
        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = (total - oldSubTotal) + subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL BALANCE
        double balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
        balanceTotal = (balanceTotal - oldSubTotal) + subTotal;
        balanceTotalTextField.setText(amountFormat.format(balanceTotal));
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
                         }else{
                             texto = texto.replaceAll(",", ".");
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
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        tabs.setEnabled(true);
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
    
    private class eventDetailAnnexedDefaultTableModel extends DefaultTableModel{
        
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
                                                        case PENALTY_QUANTITY_COLUMN_CHARGES:
                                                            return true;
                                                        case PENALTY_DELETE_BUTTON_COLUMN:
                                                            return true;
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
}
