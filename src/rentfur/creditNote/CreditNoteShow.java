package rentfur.creditNote;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.event.EventController;
import rentfur.invoice.InvoiceController;
import rentfur.subject.SubjectController;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */
public class CreditNoteShow extends JInternalFrame{
    private final CreditNoteController creditNoteController;
    private final JPanel creditNoteShowPanel;
    private final JLabel subjectCodeLabel;
    private final JLabel titleLabel;
    private final JLabel subjectLabel;
    private final JLabel fiscalStampNumberLabel;
    private final JLabel subjectAddressLabel;
    private final JLabel subjectNameLabel;
    private final JLabel subjectTelephoneLabel;
    private final JLabel subjectTradenameLabel;
    private final JLabel subjectFiscalNumberLabel;
    private final JLabel subjectCityLabel;
    private final JLabel creditNoteDateLabel;
    private final JLabel creditNoteNumberLabel;
    private final JLabel creditNoteInvoiceNumberLabel;
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
    private final JLabel cancelledLabel;
    private final JLabel cancelledDateLabel;
    private final JLabel cancelledReasonLabel;
    private final JTextField cancelledTextField;
    private final JTextField cancelledDateTextField;
    private final JTextArea cancelledReasonTextArea;
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
    private final JTextField invoiceNumberTextField;
    private final JTextField fiscalStampNumberTextField;
    private final JTextArea observationTextArea;
    private final JButton cancelCreditNote;
    private JDesktopPane eventPane;
    private final JTable creditNoteDetailTable;
    private final DefaultTableModel creditNoteDetailDefaultTableModel;
    private final JScrollPane creditNoteDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final int ITEM_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int TAX_RATE_COLUMN = 3;
    private final int QUANTITY_COLUMN = 4;
    private final int UNIT_PRICE_COLUMN = 5;
    private final int EXEMPT_COLUMN = 6;
    private final int TAX_5_COLUMN = 7;
    private final int TAX_10_COLUMN = 8;
    private final int SUB_TOTAL_COLUMN = 9;
    private final int TAX_AMOUNT_COLUMN = 10;
    private final int EVENT_DETAIL_ID_COLUMN = 11;
    
    private final HashMap eventMap;
    private final HashMap subjectMap;
    private final HashMap creditNoteMap;
    private final HashMap invoiceMap;
    
    private static final double TAX5 = 21;
    private static final double TAX10 = 11;
    
    public CreditNoteShow(CreditNoteController creditNoteController, int eventId, int creditNoteId){
        this.creditNoteController = creditNoteController;
        
        creditNoteShowPanel = new JPanel();
        creditNoteShowPanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        creditNoteMap = CreditNoteController.getCreditNoteById(creditNoteId);
        invoiceMap = InvoiceController.getInvoiceById(((Long)creditNoteMap.get("invoice_id")).intValue());
        
        titleLabel = new JLabel("<HTML><U>Nota de Credito</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        creditNoteShowPanel.add(titleLabel);
        
        creditNoteDateLabel = new JLabel("Fecha:");
        creditNoteDateLabel.setBounds(30, 60, 130, 25);
        creditNoteShowPanel.add(creditNoteDateLabel);
        
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
        creditNoteShowPanel.add(datePicker);
        
        creditNoteNumberLabel = new JLabel("Numero:");
        creditNoteNumberLabel.setBounds(370, 60, 130, 25);
        creditNoteShowPanel.add(creditNoteNumberLabel);
        
        branchTextField = new JTextField(creditNoteMap.get("credit_note_branch").toString());
        branchTextField.setEditable(false);
        branchTextField.setHorizontalAlignment(JLabel.CENTER);
        branchTextField.setBounds(470, 60, 40, 25);
        creditNoteShowPanel.add(branchTextField);
        
        JLabel firstSeparator = new JLabel("-");
        firstSeparator.setBounds(517, 60, 40, 25);
        creditNoteShowPanel.add(firstSeparator);
        
        printerTextField = new JTextField(creditNoteMap.get("credit_note_printer").toString());
        printerTextField.setEditable(false);
        printerTextField.setHorizontalAlignment(JLabel.CENTER);
        printerTextField.setBounds(530, 60, 40, 25);
        creditNoteShowPanel.add(printerTextField);
        
        JLabel secondSeparator = new JLabel("-");
        secondSeparator.setBounds(577, 60, 40, 25);
        creditNoteShowPanel.add(secondSeparator);
        
        numberTextField = new JTextField(creditNoteMap.get("credit_note_number").toString());
        numberTextField.setEditable(false);
        numberTextField.setHorizontalAlignment(JLabel.RIGHT);
        numberTextField.setBounds(590, 60, 60, 25);
        creditNoteShowPanel.add(numberTextField);
        
        creditNoteInvoiceNumberLabel = new JLabel("Factura:");
        creditNoteInvoiceNumberLabel.setBounds(370, 100, 130, 25);
        creditNoteShowPanel.add(creditNoteInvoiceNumberLabel);
        
        String invoiceNum = invoiceMap.get("invoice_branch") + "-" + invoiceMap.get("invoice_printer") + "-" + invoiceMap.get("invoice_number");
        invoiceNumberTextField = new JTextField(invoiceNum);
        invoiceNumberTextField.setEditable(false);
        invoiceNumberTextField.setHorizontalAlignment(JLabel.CENTER);
        invoiceNumberTextField.setBounds(470, 100, 180, 25);
        creditNoteShowPanel.add(invoiceNumberTextField);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 130, 200, 25);
        creditNoteShowPanel.add(subjectLabel);
        
        fiscalStampNumberLabel = new JLabel("Timbrado:");
        fiscalStampNumberLabel.setBounds(700, 60, 130, 25);
        creditNoteShowPanel.add(fiscalStampNumberLabel);
        
        fiscalStampNumberTextField = new JTextField(creditNoteMap.get("fiscal_stamp_number").toString());
        fiscalStampNumberTextField.setEditable(false);
        fiscalStampNumberTextField.setHorizontalAlignment(JLabel.RIGHT);
        fiscalStampNumberTextField.setBounds(790, 60, 100, 25);
        creditNoteShowPanel.add(fiscalStampNumberTextField);
        
        observationLabel = new JLabel("Observaciones:");
        observationLabel.setBounds(710, 150, 130, 25);
        creditNoteShowPanel.add(observationLabel);
        
        observationTextArea = new JTextArea(0,0);
        observationTextArea.setLineWrap(true);
        observationTextArea.setWrapStyleWord(true);
        observationTextArea.setText(creditNoteMap.get("observation").toString());
        observationTextArea.setEditable(false);
        JScrollPane observationScrollPane = new JScrollPane();
        observationScrollPane.setBounds(830, 150, 230, 110);
        observationScrollPane.setViewportView(observationTextArea);
        creditNoteShowPanel.add(observationScrollPane);
        
        cancelledLabel = new JLabel("Anulado: ");
        cancelledLabel.setBounds(1100, 100, 200, 25);
        creditNoteShowPanel.add(cancelledLabel);
        
        cancelledTextField = new JTextField();
        if((Boolean)creditNoteMap.get("cancelled")){
            cancelledTextField.setText("SI");
            cancelledTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED));
        }else{
            cancelledTextField.setText("NO");
            cancelledTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GREEN.darker(), Color.GREEN.brighter()));
        }
        cancelledTextField.setEditable(false);
        cancelledTextField.setEnabled(false);
        cancelledTextField.setBounds(1250, 100, 230, 25);
        creditNoteShowPanel.add(cancelledTextField);
        
        cancelledReasonLabel = new JLabel("Motivo: ");
        cancelledReasonLabel.setBounds(1100, 130, 200, 25);
        creditNoteShowPanel.add(cancelledReasonLabel);
        
        cancelledReasonTextArea = new JTextArea(0,0);
        if(creditNoteMap.get("cancelled_reason")!=null){
            cancelledReasonTextArea.setText(creditNoteMap.get("cancelled_reason").toString());
        }else{
            cancelledReasonTextArea.setText("");
        }
        
        cancelledReasonTextArea.setLineWrap(true);
        cancelledReasonTextArea.setWrapStyleWord(true);
        cancelledReasonTextArea.setEditable(false);
        cancelledReasonTextArea.setEnabled(false);
        JScrollPane cancelledReasonScrollPane = new JScrollPane();
        cancelledReasonScrollPane.setBounds(1250, 130, 230, 90);
        cancelledReasonScrollPane.setViewportView(cancelledReasonTextArea);
        creditNoteShowPanel.add(cancelledReasonScrollPane);
        
        cancelledDateLabel = new JLabel("Fecha de Anulacion: ");
        cancelledDateLabel.setBounds(1100, 240, 200, 25);
        creditNoteShowPanel.add(cancelledDateLabel);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cancelledDateTextField = new JTextField();
        if(creditNoteMap.get("cancelled_date")!=null){
            formatter.format(new Date(((Timestamp)creditNoteMap.get("cancelled_date")).getTime()));
            cancelledDateTextField.setText(formatter.format(new Date(((Timestamp)creditNoteMap.get("cancelled_date")).getTime())));
        }else{
            cancelledDateTextField.setText("");
        }
        cancelledDateTextField.setHorizontalAlignment(JLabel.CENTER);
        cancelledDateTextField.setEditable(false);
        cancelledDateTextField.setEnabled(false);
        cancelledDateTextField.setBounds(1250, 240, 230, 25);
        creditNoteShowPanel.add(cancelledDateTextField);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 170, 80, 25);
        creditNoteShowPanel.add(subjectCodeLabel);

        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 170, 170, 25);
        creditNoteShowPanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 170, 160, 25);
        creditNoteShowPanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 170, 170, 25);
        creditNoteShowPanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 200, 100, 25);
        creditNoteShowPanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 200, 170, 25);
        creditNoteShowPanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 200, 100, 25);
        creditNoteShowPanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 200, 170, 25);
        creditNoteShowPanel.add(subjectTelephoneTextField);
        
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
        subjectTradenameLabel.setBounds(30, 230, 120, 25);
        creditNoteShowPanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 230, 170, 25);
        creditNoteShowPanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 230, 120, 25);
        creditNoteShowPanel.add(subjectFiscalNumberLabel);
        
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
        subjectFiscalNumberTextField.setBounds(490, 230, 170, 25);
        creditNoteShowPanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 260, 80, 25);
        creditNoteShowPanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 260, 170, 25);
        creditNoteShowPanel.add(subjectCityTextField);
        
        creditNoteDetailDefaultTableModel = new InvoiceDetailDefaultTableModel();
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
        creditNoteDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(rightRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setCellRenderer(rightRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setCellRenderer(rightRenderer);
        
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setMaxWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setPreferredWidth(100);
        creditNoteDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setCellRenderer(rightRenderer);
        
         //Sub Totales
        subTotalLabel = new JLabel("Sub Totales: ");
        subTotalLabel.setBounds(30, 660, 80, 25);
        creditNoteShowPanel.add(subTotalLabel);
        
        total5Label = new JLabel("5%: ");
        total5Label.setBounds(160, 660, 30, 25);
        creditNoteShowPanel.add(total5Label);
        
        total5TextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("tax05total").toString())));
        total5TextField.setBounds(200, 660, 170, 25);
        total5TextField.setEditable(false);
        total5TextField.setHorizontalAlignment(JLabel.RIGHT);
        creditNoteShowPanel.add(total5TextField);
        
        total10Label = new JLabel("10%: ");
        total10Label.setBounds(390, 660, 35, 25);
        creditNoteShowPanel.add(total10Label);
        
        total10TextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("taxted10total").toString())));
        total10TextField.setEditable(false);
        total10TextField.setHorizontalAlignment(JLabel.RIGHT);
        total10TextField.setBounds(440, 660, 170, 25);
        creditNoteShowPanel.add(total10TextField);
        
        exemptTotalLabel = new JLabel("Exentas: ");
        exemptTotalLabel.setBounds(640, 660, 100, 25);
        creditNoteShowPanel.add(exemptTotalLabel);
        
        exemptTotalTextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("exempt_total").toString())));
        exemptTotalTextField.setEditable(false);
        exemptTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        exemptTotalTextField.setBounds(730, 660, 170, 25);
        creditNoteShowPanel.add(exemptTotalTextField);
        
        //Liquidacion del IVA
        taxSettlementLabel = new JLabel("Liquidacion del IVA: ");
        taxSettlementLabel.setBounds(30, 690, 130, 25);
        creditNoteShowPanel.add(taxSettlementLabel);
        
        tax5Label = new JLabel("5%: ");
        tax5Label.setBounds(160, 690, 30, 25);
        creditNoteShowPanel.add(tax5Label);
        
        tax5TextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("tax05total").toString())));
        tax5TextField.setBounds(200, 690, 170, 25);
        tax5TextField.setEditable(false);
        tax5TextField.setHorizontalAlignment(JLabel.RIGHT);
        creditNoteShowPanel.add(tax5TextField);
        
        tax10Label = new JLabel("10%: ");
        tax10Label.setBounds(390, 690, 35, 25);
        creditNoteShowPanel.add(tax10Label);
        
        tax10TextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("tax10total").toString())));
        tax10TextField.setEditable(false);
        tax10TextField.setHorizontalAlignment(JLabel.RIGHT);
        tax10TextField.setBounds(440, 690, 170, 25);
        creditNoteShowPanel.add(tax10TextField);
        
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(640, 690, 100, 25);
        creditNoteShowPanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("tax_total").toString())));
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(730, 690, 170, 25);
        creditNoteShowPanel.add(totalTaxTextField);
        
        //Total
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(30, 720, 80, 25);
        creditNoteShowPanel.add(totalLabel);
        
        totalTextField = new JTextField(amountFormat.format(Double.valueOf(creditNoteMap.get("net_total").toString())));
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(200, 720, 170, 25);
        creditNoteShowPanel.add(totalTextField);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelCreditNote = new JButton();
        cancelCreditNote.setText(" Cerrar");
        cancelCreditNote.setIcon(cancelImageIcon);
        cancelCreditNote.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelCreditNote.setBounds(1300, 770, 170, 32);
        creditNoteShowPanel.add(cancelCreditNote);
        
        
        HashMap creditNoteDetailMap;
        ArrayList creditNoteDetailList = (ArrayList) creditNoteMap.get("creditNoteDetail");
        Object[] row = new Object[creditNoteDetailTable.getColumnCount()];
        
        for(int i = 0; i < creditNoteDetailList.size(); i++){
            creditNoteDetailMap = (HashMap) creditNoteDetailList.get(i);

            row[ITEM_COLUMN] = creditNoteDetailMap.get("row_number");
            row[CODE_COLUMN] = creditNoteDetailMap.get("furniture_code");
            row[DESCRIPTION_COLUMN] = creditNoteDetailMap.get("description");
            row[TAX_RATE_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("tax_rate"));
            row[QUANTITY_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("quantity"));
            row[UNIT_PRICE_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("unit_price"));
            row[EXEMPT_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("exempt_amount"));
            row[TAX_5_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("taxted05amount"));
            row[TAX_10_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("taxted10amount"));
            row[SUB_TOTAL_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("net_amount"));
            row[TAX_AMOUNT_COLUMN] = amountFormat.format((Double)creditNoteDetailMap.get("tax_amount"));
            row[EVENT_DETAIL_ID_COLUMN] = creditNoteDetailMap.get("id").toString();
        
            creditNoteDetailDefaultTableModel.addRow(row);
        }
        
        add(creditNoteShowPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Nota de Credito");
        setBounds(120,40,1550,860);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        creditNoteController.creditNoteShowClosed();
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void showSearchDialog(Object dialogView){
        eventPane = this.getDesktopPane();
        eventPane.add((JInternalFrame) dialogView, new Integer(1000));
        eventPane.setVisible(true);
    }
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
//        saveButton.setEnabled(false);
//        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
//        saveButton.setEnabled(true);
//        cancelButton.setEnabled(true);
    }
    
    private class InvoiceDetailDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            switch(column){
                default:    return false;
            }
        }
           
    }
}
