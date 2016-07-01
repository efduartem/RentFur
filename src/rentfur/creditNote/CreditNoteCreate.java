package rentfur.creditNote;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
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
import javax.swing.table.TableCellRenderer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.book.BookController;
import rentfur.event.EventController;
import rentfur.furniture.FurnitureController;
import rentfur.invoice.InvoiceController;
import rentfur.subject.SubjectController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DateLabelFormatter;
import rentfur.util.searches.EventInvoiceSearch;
import rentfur.util.searches.SearchController;

/**
 *
 * @author FDuarte
 */
public class CreditNoteCreate extends JInternalFrame{
    
    private final CreditNoteController creditNoteController;
    private final SearchController searchController;
    private EventInvoiceSearch eventInvoiceSearch;
    private final JPanel creditNoteCreatePanel;
    private final JLabel subjectCodeLabel;
    private final JLabel titleLabel;
    private final JLabel subjectLabel;
    private final JLabel fisaclStampNumberLabel;
    private final JLabel subjectAddressLabel;
    private final JLabel subjectNameLabel;
    private final JLabel subjectTelephoneLabel;
    private final JLabel subjectTradenameLabel;
    private final JLabel subjectFiscalNumberLabel;
    private final JLabel subjectCityLabel;
    private final JLabel invoiceDateLabel;
    private final JLabel invoiceNumberLabel;
    private final JLabel totalLabel;
    private final JLabel subTotalLabel;
    private final JLabel total5Label;
    private final JLabel total10Label;
    private final JLabel exemptTotalLabel;
    private final JLabel taxSettlementLabel;
    private final JLabel tax5Label;
    private final JLabel tax10Label;
    private final JLabel totalTaxLabel;
    private final JLabel observationLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JDatePickerImpl datePicker;
    private final JTextField totalTextField;
    private final JTextField total5TextField;
    private final JTextField total10TextField;
    private final JTextField exemptTotalTextField;
    private final JTextField tax5TextField;
    private final JTextField tax10TextField;
    private final JTextField totalTaxTextField;
    private final JTextField branchTextField;
    private final JTextField printerTextField;
    private final JTextField numberTextField;
    private final JTextField fiscalStampNumberTextField;
    private final JTextArea observationTextArea;
    private final JButton selectInvoiceButton;
    private final JButton saveCreditNote;
    private final JButton cancelCreditNote;
    private JDesktopPane eventPane;
    private final JTable creditNoteDetailTable;
    private final DefaultTableModel creditNoteDetailDefaultTableModel;
    private final JScrollPane creditNoteDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    private final DecimalFormat amountFormatTax = new DecimalFormat("#,###.##");
    
    private final int ITEM_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int TAX_RATE_COLUMN = 3;
    private final int QUANTITY_COLUMN = 4;
    private final int UNIT_PRICE_COLUMN = 5;
    private final int EXEMPT_COLUMN = 6;
    private final int TAX_5_COLUMN = 7;
    private final int TAX_10_COLUMN = 8;
    private final int DELETE_COLUMN = 9;
    private final int SUB_TOTAL_COLUMN = 10;
    private final int TAX_AMOUNT_COLUMN = 11;
    private final int EVENT_DETAIL_ID_COLUMN = 12;
    
    private final HashMap eventMap;
    private final HashMap subjectMap;
    private final HashMap creditNoteNumMap;
    private int invoiceSelectedId = 0;
    
    private static final double TAX5 = 21;
    private static final double TAX10 = 11;
    
    public CreditNoteCreate(CreditNoteController creditNoteController, int eventId){
        this.creditNoteController = creditNoteController;
        searchController = new SearchController();
        
        creditNoteCreatePanel = new JPanel();
        creditNoteCreatePanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        creditNoteNumMap = BookController.getCreditNoteNum();
        
        titleLabel = new JLabel("<HTML><U>Nota de Credito</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        creditNoteCreatePanel.add(titleLabel);
        
        invoiceDateLabel = new JLabel("Fecha:");
        invoiceDateLabel.setBounds(30, 60, 130, 25);
        creditNoteCreatePanel.add(invoiceDateLabel);
        
        UtilDateModel model = new UtilDateModel(new Date());
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        datePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                if(receiptDetailDefaultTableModel.getRowCount()>0){
//                    //showDayChangeWarning();
//                }else{
//                    //updateCurrentDeliveryDateSelected();
//                }
            }
        });
        datePicker.getComponent(1).setEnabled(false);
        datePicker.setBounds(160, 60, 170, 25);
        creditNoteCreatePanel.add(datePicker);
        
        invoiceNumberLabel = new JLabel("Numero:");
        invoiceNumberLabel.setBounds(370, 60, 130, 25);
        creditNoteCreatePanel.add(invoiceNumberLabel);
        
        branchTextField = new JTextField(creditNoteNumMap.get("branch").toString());
        branchTextField.setEditable(false);
        branchTextField.setHorizontalAlignment(JLabel.CENTER);
        branchTextField.setBounds(470, 60, 40, 25);
        creditNoteCreatePanel.add(branchTextField);
        
        JLabel firstSeparator = new JLabel("-");
        firstSeparator.setBounds(517, 60, 40, 25);
        creditNoteCreatePanel.add(firstSeparator);
        
        printerTextField = new JTextField(creditNoteNumMap.get("printer").toString());
        printerTextField.setEditable(false);
        printerTextField.setHorizontalAlignment(JLabel.CENTER);
        printerTextField.setBounds(530, 60, 40, 25);
        creditNoteCreatePanel.add(printerTextField);
        
        JLabel secondSeparator = new JLabel("-");
        secondSeparator.setBounds(577, 60, 40, 25);
        creditNoteCreatePanel.add(secondSeparator);
        
        String receiptNum = String.format ("%07d", (Integer) creditNoteNumMap.get("number"));
        numberTextField = new JTextField(receiptNum);
        numberTextField.setEditable(false);
        numberTextField.setHorizontalAlignment(JLabel.RIGHT);
        numberTextField.setBounds(590, 60, 60, 25);
        creditNoteCreatePanel.add(numberTextField);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        creditNoteCreatePanel.add(subjectLabel);
        
        fisaclStampNumberLabel = new JLabel("Timbrado:");
        fisaclStampNumberLabel.setBounds(700, 60, 130, 25);
        creditNoteCreatePanel.add(fisaclStampNumberLabel);
        
        fiscalStampNumberTextField = new JTextField(creditNoteNumMap.get("fiscalStampNumber").toString());
        fiscalStampNumberTextField.setEditable(false);
        fiscalStampNumberTextField.setHorizontalAlignment(JLabel.RIGHT);
        fiscalStampNumberTextField.setBounds(790, 60, 100, 25);
        creditNoteCreatePanel.add(fiscalStampNumberTextField);
        
        observationLabel = new JLabel("Observaciones:");
        observationLabel.setBounds(710, 150, 130, 25);
        creditNoteCreatePanel.add(observationLabel);
        
        observationTextArea = new JTextArea(0,0);
        observationTextArea.setLineWrap(true);
        observationTextArea.setWrapStyleWord(true);
        JScrollPane observationScrollPane = new JScrollPane();
        observationScrollPane.setBounds(830, 150, 230, 110);
        observationScrollPane.setViewportView(observationTextArea);
        creditNoteCreatePanel.add(observationScrollPane);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        creditNoteCreatePanel.add(subjectCodeLabel);

        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        creditNoteCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        creditNoteCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        creditNoteCreatePanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        creditNoteCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        creditNoteCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        creditNoteCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        creditNoteCreatePanel.add(subjectTelephoneTextField);
        
        //Total IVA
//        totalTaxLabel = new JLabel("Total IVA: ");
//        totalTaxLabel.setBounds(1100, 180, 80, 25);
//        receiptCreatePanel.add(totalTaxLabel);
        
//        totalTaxTextField = new JTextField("0");
//        totalTaxTextField.setEditable(false);
//        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
//        totalTaxTextField.setBounds(1200, 180, 170, 25);
//        receiptCreatePanel.add(totalTaxTextField);
        
        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 210, 120, 25);
        creditNoteCreatePanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        creditNoteCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        creditNoteCreatePanel.add(subjectFiscalNumberLabel);
        
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
        creditNoteCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        creditNoteCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        creditNoteCreatePanel.add(subjectCityTextField);
        
        ImageIcon addFurnitureImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        selectInvoiceButton = new JButton();
        selectInvoiceButton.setIcon(addFurnitureImageIcon);
        selectInvoiceButton.setBounds(30, 290, 180, 32);
        selectInvoiceButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addDetailsButtonAction();
            }
        });
        selectInvoiceButton.setText(" Seleccionar Factura");
        
        creditNoteCreatePanel.add(selectInvoiceButton);
        
        creditNoteDetailDefaultTableModel = new CreditNoteDetailDefaultTableModel();
        creditNoteDetailTable = new JTable(creditNoteDetailDefaultTableModel);
        
        creditNoteDetailDefaultTableModel.addColumn("Item");
        creditNoteDetailDefaultTableModel.addColumn("Codigo");
        creditNoteDetailDefaultTableModel.addColumn("Descripcion");
        creditNoteDetailDefaultTableModel.addColumn("Tasa de Impuesto");
        creditNoteDetailDefaultTableModel.addColumn("Cantidad");
        creditNoteDetailDefaultTableModel.addColumn("Precio Unitario");
        creditNoteDetailDefaultTableModel.addColumn("Exenta");
        creditNoteDetailDefaultTableModel.addColumn("5%");
        creditNoteDetailDefaultTableModel.addColumn("10%");
        creditNoteDetailDefaultTableModel.addColumn("");
        creditNoteDetailDefaultTableModel.addColumn("SubTotal");
        creditNoteDetailDefaultTableModel.addColumn("Monto Iva");
        creditNoteDetailDefaultTableModel.addColumn("ID DETALLE EVENTO");
        
        creditNoteDetailTable.setRowHeight(22);
        
        creditNoteDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setMinWidth(0);
        creditNoteDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setMaxWidth(0);
        creditNoteDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setPreferredWidth(0);
        
        creditNoteDetailTable.getColumnModel().getColumn(TAX_AMOUNT_COLUMN).setMinWidth(0);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_AMOUNT_COLUMN).setMaxWidth(0);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_AMOUNT_COLUMN).setPreferredWidth(0);

        creditNoteDetailTable.getColumnModel().getColumn(EVENT_DETAIL_ID_COLUMN).setMinWidth(0);
        creditNoteDetailTable.getColumnModel().getColumn(EVENT_DETAIL_ID_COLUMN).setMaxWidth(0);
        creditNoteDetailTable.getColumnModel().getColumn(EVENT_DETAIL_ID_COLUMN).setPreferredWidth(0);
        
        //invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        //invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        
        //Alineacion a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        creditNoteDetailTableJScrollPane = new JScrollPane(creditNoteDetailTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        creditNoteDetailTableJScrollPane.setBounds(30, 350, 1180, 290);
        add(creditNoteDetailTableJScrollPane);
        
        creditNoteDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setMinWidth(50);
        creditNoteDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setMaxWidth(50);
        creditNoteDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setPreferredWidth(50);
        creditNoteDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setCellRenderer(centerRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMinWidth(80);
        creditNoteDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(80);
        creditNoteDetailTable.getColumnModel().getColumn(CODE_COLUMN).setPreferredWidth(80);
        creditNoteDetailTable.getColumnModel().getColumn(CODE_COLUMN).setCellRenderer(centerRenderer);
     
        creditNoteDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setCellRenderer(rightRenderer);

        creditNoteDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setMaxWidth(130);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setPreferredWidth(130);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setPreferredWidth(130);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellRenderer(centerRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setPreferredWidth(100);
//        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellEditor(new QuantityCellEditor());
        creditNoteDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(new QuantityCellRenderer());
        
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(100);
//        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setCellEditor(new UnitPriceCellEditor());
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setCellRenderer(new UnitPriceCellRenderer());
        
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setCellRenderer(rightRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setCellRenderer(rightRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setMinWidth(60);
        creditNoteDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setMaxWidth(60);
        creditNoteDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setPreferredWidth(60);
        
         //Sub Totales
        subTotalLabel = new JLabel("Sub Totales: ");
        subTotalLabel.setBounds(30, 660, 80, 25);
        creditNoteCreatePanel.add(subTotalLabel);
        
        total5Label = new JLabel("5%: ");
        total5Label.setBounds(160, 660, 30, 25);
        creditNoteCreatePanel.add(total5Label);
        
        total5TextField = new JTextField("0");
        total5TextField.setBounds(200, 660, 170, 25);
        total5TextField.setEditable(false);
        total5TextField.setHorizontalAlignment(JLabel.RIGHT);
        creditNoteCreatePanel.add(total5TextField);
        
        total10Label = new JLabel("10%: ");
        total10Label.setBounds(390, 660, 35, 25);
        creditNoteCreatePanel.add(total10Label);
        
        total10TextField = new JTextField("0");
        total10TextField.setEditable(false);
        total10TextField.setHorizontalAlignment(JLabel.RIGHT);
        total10TextField.setBounds(440, 660, 170, 25);
        creditNoteCreatePanel.add(total10TextField);
        
        exemptTotalLabel = new JLabel("Exentas: ");
        exemptTotalLabel.setBounds(640, 660, 100, 25);
        creditNoteCreatePanel.add(exemptTotalLabel);
        
        exemptTotalTextField = new JTextField("0");
        exemptTotalTextField.setEditable(false);
        exemptTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        exemptTotalTextField.setBounds(730, 660, 170, 25);
        creditNoteCreatePanel.add(exemptTotalTextField);
        
        //Liquidacion del IVA
        taxSettlementLabel = new JLabel("Liquidacion del IVA: ");
        taxSettlementLabel.setBounds(30, 690, 130, 25);
        creditNoteCreatePanel.add(taxSettlementLabel);
        
        tax5Label = new JLabel("5%: ");
        tax5Label.setBounds(160, 690, 30, 25);
        creditNoteCreatePanel.add(tax5Label);
        
        tax5TextField = new JTextField("0");
        tax5TextField.setBounds(200, 690, 170, 25);
        tax5TextField.setEditable(false);
        tax5TextField.setHorizontalAlignment(JLabel.RIGHT);
        creditNoteCreatePanel.add(tax5TextField);
        
        tax10Label = new JLabel("10%: ");
        tax10Label.setBounds(390, 690, 35, 25);
        creditNoteCreatePanel.add(tax10Label);
        
        tax10TextField = new JTextField("0");
        tax10TextField.setEditable(false);
        tax10TextField.setHorizontalAlignment(JLabel.RIGHT);
        tax10TextField.setBounds(440, 690, 170, 25);
        creditNoteCreatePanel.add(tax10TextField);
        
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(640, 690, 100, 25);
        creditNoteCreatePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField("0");
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(730, 690, 170, 25);
        creditNoteCreatePanel.add(totalTaxTextField);
        
        //Total
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(30, 720, 80, 25);
        creditNoteCreatePanel.add(totalLabel);
    
        totalTextField = new JTextField("0");
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(200, 720, 170, 25);
        creditNoteCreatePanel.add(totalTextField);
        
        ImageIcon saveReceiptImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveCreditNote = new JButton();
        saveCreditNote.setText(" Guardar");
        saveCreditNote.setIcon(saveReceiptImageIcon);
        saveCreditNote.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveCreditNoteButtonAction();
            }
        });
        saveCreditNote.setBounds(800, 770, 170, 32);
        creditNoteCreatePanel.add(saveCreditNote);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelCreditNote = new JButton();
        cancelCreditNote.setText(" Cancelar");
        cancelCreditNote.setIcon(cancelImageIcon);
        cancelCreditNote.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelCreditNote.setBounds(1000, 770, 170, 32);
        creditNoteCreatePanel.add(cancelCreditNote);
        
        add(creditNoteCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Nota de Credito");
        setBounds(250,30,1250,860);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        creditNoteController.creditNoteCreateClosed();
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void addDetailsButtonAction(){
        int eventId = Integer.valueOf(eventMap.get("id").toString());
        ArrayList furnitureDetailList;
        
        furnitureDetailList = (ArrayList) eventMap.get("detail");
        furnitureDetailList.addAll((ArrayList) eventMap.get("penaltyDetail"));
        eventInvoiceSearch = searchController.getEventInvoiceSearch(eventId);
        eventInvoiceSearch.setVisible(true);
        showSearchDialog(eventInvoiceSearch);
        inactivateElements();
        eventInvoiceSearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
                if(searchController.getInvoiceSelectedId()!=0){
                    invoiceSelectedId = searchController.getInvoiceSelectedId();
                    addInvoiceSelectedToDetailTable(searchController.getInvoiceSelectedId());
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
    
    private void addInvoiceSelectedToDetailTable(int invoiceId){
        try {
            HashMap invoiceDetailMap;
            HashMap invoiceMap = InvoiceController.getInvoiceById(invoiceId);
            ArrayList invoiceDetailList = (ArrayList)invoiceMap.get("invoiceDetail");
            double total = amountFormat.parse(totalTextField.getText()).doubleValue();
            
            int rowNumber = 0;
            rowNumber = creditNoteDetailDefaultTableModel.getRowCount();
            for(int i=0;i<rowNumber;i++){
                creditNoteDetailDefaultTableModel.removeRow(0);
            }
            
            Object[] row = new Object[creditNoteDetailTable.getColumnCount()];
            
            for(int i = 0; i < invoiceDetailList.size(); i++){
                invoiceDetailMap = (HashMap) invoiceDetailList.get(i);

                row[ITEM_COLUMN] = invoiceDetailMap.get("row_number");
                row[CODE_COLUMN] = invoiceDetailMap.get("furniture_code");
                row[DESCRIPTION_COLUMN] = invoiceDetailMap.get("description");
                row[TAX_RATE_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("tax_rate"));
                row[QUANTITY_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("quantity"));
                row[UNIT_PRICE_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("unit_price"));
                row[EXEMPT_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("exempt_amount"));
                row[TAX_5_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("taxted05amount"));
                row[TAX_10_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("taxted10amount"));
                row[SUB_TOTAL_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("net_amount"));
                row[TAX_AMOUNT_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("tax_amount"));
                row[EVENT_DETAIL_ID_COLUMN] = invoiceDetailMap.get("event_detail_id").toString();

                creditNoteDetailDefaultTableModel.addRow(row);
            }

            total5TextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("tax05total").toString())));
            total10TextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("taxted10total").toString())));
            exemptTotalTextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("exempt_total").toString())));
        
            tax5TextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("tax05total").toString())));
            tax10TextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("tax10total").toString())));
            totalTaxTextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("tax_total").toString())));
        
            totalTextField.setText(amountFormat.format(Double.valueOf(invoiceMap.get("net_total").toString())));
            
        } catch (ParseException ex) {
            Logger.getLogger(CreditNoteCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        selectInvoiceButton.setEnabled(false);
//        saveButton.setEnabled(false);
//        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        selectInvoiceButton.setEnabled(true);
//        saveButton.setEnabled(true);
//        cancelButton.setEnabled(true);
    }
    
    private void showSearchDialog(Object dialogView){
        eventPane = this.getDesktopPane();
        eventPane.add((JInternalFrame) dialogView, new Integer(1000));
        eventPane.setVisible(true);
    }
    
    private void saveCreditNoteButtonAction(){
        try {
            JOptionPane optionPane;
            JDialog dialog;
            if (creditNoteDetailTable.isEditing()){
                creditNoteDetailTable.getCellEditor().stopCellEditing();
            }
            if(creditNoteDetailTable.getRowCount()==0){
                optionPane = new JOptionPane("No ha seleccionado ninguna factura", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                dialog = optionPane.createDialog(this, "Atencion!");
                dialog.setVisible(true);
            }else{
                Date invoceDate = (Date) datePicker.getModel().getValue();
                HashMap detailMap;
                ArrayList invoiceDetailList = new ArrayList();
                Vector dataVector;
                double netTotal, exemptTotal = 0, tax05total = 0, tax10total = 0, taxTotal = 0, taxted05total = 0, taxted10total = 0;
                String observation = observationTextArea.getText();
                int eventId = Integer.valueOf(eventMap.get("id").toString());

                for (int row = 0; row < creditNoteDetailTable.getRowCount(); row++){
                    dataVector = (Vector) creditNoteDetailDefaultTableModel.getDataVector().get(row);
                    detailMap = new HashMap();
                    detailMap.put("rowNumber", dataVector.get(ITEM_COLUMN));
                    detailMap.put("code", dataVector.get(CODE_COLUMN));
                    detailMap.put("description", dataVector.get(DESCRIPTION_COLUMN));
                    detailMap.put("taxRate", amountFormat.parse(dataVector.get(TAX_RATE_COLUMN).toString()).doubleValue());
                    detailMap.put("quantity", amountFormat.parse(dataVector.get(QUANTITY_COLUMN).toString()).doubleValue());
                    detailMap.put("unitPrice", amountFormat.parse(dataVector.get(UNIT_PRICE_COLUMN).toString()).doubleValue());
                    detailMap.put("exemptAmount", amountFormat.parse(dataVector.get(EXEMPT_COLUMN).toString()).doubleValue());
                    detailMap.put("tax5Amount", amountFormat.parse(dataVector.get(TAX_5_COLUMN).toString()).doubleValue());
                    detailMap.put("tax10Amount", amountFormat.parse(dataVector.get(TAX_10_COLUMN).toString()).doubleValue());
                    detailMap.put("totalAmount", amountFormat.parse(dataVector.get(SUB_TOTAL_COLUMN).toString()).doubleValue());
                    detailMap.put("taxAmount", amountFormatTax.parse(dataVector.get(TAX_AMOUNT_COLUMN).toString()).doubleValue());
                    detailMap.put("eventDetailId", dataVector.get(EVENT_DETAIL_ID_COLUMN).toString());
                    invoiceDetailList.add(detailMap);
                }

                netTotal = amountFormat.parse(totalTextField.getText()).doubleValue();
                exemptTotal = amountFormat.parse(exemptTotalTextField.getText()).doubleValue();
                tax05total = amountFormat.parse(tax5TextField.getText()).doubleValue();
                tax10total = amountFormat.parse(tax10TextField.getText()).doubleValue();
                taxTotal = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
                taxted05total = amountFormat.parse(total5TextField.getText()).doubleValue();
                taxted10total = amountFormat.parse(total10TextField.getText()).doubleValue();
                
                HashMap returnMap = creditNoteController.createCreditNote(subjectMap, invoiceDetailList, invoceDate, eventId, creditNoteNumMap, netTotal, exemptTotal, tax05total, tax10total, taxTotal, taxted05total, taxted10total, observation, invoiceSelectedId);

                if(((Integer)returnMap.get("status"))== CreditNoteController.SUCCESFULLY_SAVED){
                    JOptionPane.showMessageDialog(null, returnMap.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                    doDefaultCloseAction();
                }else if((Integer)returnMap.get("status") == CreditNoteController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, returnMap.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(CreditNoteCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class CreditNoteDetailDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            switch(column){
                default:    return false;
            }
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
    
    class UnitPriceCellRenderer extends JTextField implements TableCellRenderer {

        public UnitPriceCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Precio");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
}
