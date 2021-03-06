/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.invoice;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.event.EventController;
import rentfur.subject.SubjectController;
import rentfur.util.DateLabelFormatter;
import rentfur.util.NumberToLetterConverter;

/**
 *
 * @author FDuarte
 */

public class InvoiceShow extends JInternalFrame{
    private final InvoiceController invoiceController;
    private final JPanel invoiceCreatePanel;
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
    private final JButton cancelInvoice;
    private JDesktopPane eventPane;
    private final JTable invoiceDetailTable;
    private final DefaultTableModel invoiceDetailDefaultTableModel;
    private final JScrollPane invoiceDetailTableJScrollPane;
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
    private final HashMap invoiceMap;
    
    public InvoiceShow(InvoiceController invoiceController, int eventId, int invoiceId){
        this.invoiceController = invoiceController;
        
        invoiceCreatePanel = new JPanel();
        invoiceCreatePanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        invoiceMap = InvoiceController.getInvoiceById(invoiceId);
        
        titleLabel = new JLabel("<HTML><U>Factura</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        invoiceCreatePanel.add(titleLabel);
        
        invoiceDateLabel = new JLabel("Fecha:");
        invoiceDateLabel.setBounds(30, 60, 130, 25);
        invoiceCreatePanel.add(invoiceDateLabel);
        
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
        invoiceCreatePanel.add(datePicker);
        
        invoiceNumberLabel = new JLabel("Numero:");
        invoiceNumberLabel.setBounds(370, 60, 130, 25);
        invoiceCreatePanel.add(invoiceNumberLabel);
        
        branchTextField = new JTextField(invoiceMap.get("invoice_branch").toString());
        branchTextField.setEditable(false);
        branchTextField.setHorizontalAlignment(JLabel.CENTER);
        branchTextField.setBounds(470, 60, 40, 25);
        invoiceCreatePanel.add(branchTextField);
        
        JLabel firstSeparator = new JLabel("-");
        firstSeparator.setBounds(517, 60, 40, 25);
        invoiceCreatePanel.add(firstSeparator);
        
        printerTextField = new JTextField(invoiceMap.get("invoice_printer").toString());
        printerTextField.setEditable(false);
        printerTextField.setHorizontalAlignment(JLabel.CENTER);
        printerTextField.setBounds(530, 60, 40, 25);
        invoiceCreatePanel.add(printerTextField);
        
        JLabel secondSeparator = new JLabel("-");
        secondSeparator.setBounds(577, 60, 40, 25);
        invoiceCreatePanel.add(secondSeparator);
        
        numberTextField = new JTextField(invoiceMap.get("invoice_number").toString());
        numberTextField.setEditable(false);
        numberTextField.setHorizontalAlignment(JLabel.RIGHT);
        numberTextField.setBounds(590, 60, 60, 25);
        invoiceCreatePanel.add(numberTextField);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        invoiceCreatePanel.add(subjectLabel);
        
        fisaclStampNumberLabel = new JLabel("Timbrado:");
        fisaclStampNumberLabel.setBounds(700, 60, 130, 25);
        invoiceCreatePanel.add(fisaclStampNumberLabel);
        
        fiscalStampNumberTextField = new JTextField(invoiceMap.get("fiscal_stamp_number").toString());
        fiscalStampNumberTextField.setEditable(false);
        fiscalStampNumberTextField.setHorizontalAlignment(JLabel.RIGHT);
        fiscalStampNumberTextField.setBounds(790, 60, 100, 25);
        invoiceCreatePanel.add(fiscalStampNumberTextField);
        
        observationLabel = new JLabel("Observaciones:");
        observationLabel.setBounds(710, 150, 130, 25);
        invoiceCreatePanel.add(observationLabel);
        
        observationTextArea = new JTextArea(0,0);
        observationTextArea.setLineWrap(true);
        observationTextArea.setWrapStyleWord(true);
        observationTextArea.setText(invoiceMap.get("observation").toString());
        observationTextArea.setEditable(false);
        JScrollPane observationScrollPane = new JScrollPane();
        observationScrollPane.setBounds(830, 150, 230, 110);
        observationScrollPane.setViewportView(observationTextArea);
        invoiceCreatePanel.add(observationScrollPane);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        invoiceCreatePanel.add(subjectCodeLabel);

        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        invoiceCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        invoiceCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        invoiceCreatePanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        invoiceCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        invoiceCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        invoiceCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        invoiceCreatePanel.add(subjectTelephoneTextField);
        
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
        invoiceCreatePanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        invoiceCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        invoiceCreatePanel.add(subjectFiscalNumberLabel);
        
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
        invoiceCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        invoiceCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        invoiceCreatePanel.add(subjectCityTextField);
        
        invoiceDetailDefaultTableModel = new InvoiceDetailDefaultTableModel();
        invoiceDetailTable = new JTable(invoiceDetailDefaultTableModel);
        
        invoiceDetailDefaultTableModel.addColumn("Item");
        invoiceDetailDefaultTableModel.addColumn("Codigo");
        invoiceDetailDefaultTableModel.addColumn("Descripcion");
        invoiceDetailDefaultTableModel.addColumn("Tasa de Impuesto");
        invoiceDetailDefaultTableModel.addColumn("Cantidad");
        invoiceDetailDefaultTableModel.addColumn("Precio Unitario");
        invoiceDetailDefaultTableModel.addColumn("Exenta");
        invoiceDetailDefaultTableModel.addColumn("5%");
        invoiceDetailDefaultTableModel.addColumn("10%");
        invoiceDetailDefaultTableModel.addColumn("SubTotal");
        invoiceDetailDefaultTableModel.addColumn("Monto Iva");
        invoiceDetailDefaultTableModel.addColumn("ID DETALLE EVENTO");
        
        invoiceDetailTable.setRowHeight(22);
        
        invoiceDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setMinWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setMaxWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setPreferredWidth(0);
        
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT_COLUMN).setMinWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT_COLUMN).setMaxWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT_COLUMN).setPreferredWidth(0);

        invoiceDetailTable.getColumnModel().getColumn(EVENT_DETAIL_ID_COLUMN).setMinWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(EVENT_DETAIL_ID_COLUMN).setMaxWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(EVENT_DETAIL_ID_COLUMN).setPreferredWidth(0);
        
        //Alineacion a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        invoiceDetailTableJScrollPane = new JScrollPane(invoiceDetailTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        invoiceDetailTableJScrollPane.setBounds(30, 350, 1180, 290);
        add(invoiceDetailTableJScrollPane);
        
        invoiceDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setMinWidth(50);
        invoiceDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setMaxWidth(50);
        invoiceDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setPreferredWidth(50);
        invoiceDetailTable.getColumnModel().getColumn(ITEM_COLUMN).setCellRenderer(centerRenderer);
        
        invoiceDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMinWidth(80);
        invoiceDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(80);
        invoiceDetailTable.getColumnModel().getColumn(CODE_COLUMN).setPreferredWidth(80);
        invoiceDetailTable.getColumnModel().getColumn(CODE_COLUMN).setCellRenderer(centerRenderer);
     
        invoiceDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setMaxWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(EXEMPT_COLUMN).setCellRenderer(rightRenderer);

        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setMaxWidth(130);
        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setPreferredWidth(130);
        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setPreferredWidth(130);
        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellRenderer(centerRenderer);
        
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setMaxWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(rightRenderer);
        
        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setMaxWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE_COLUMN).setCellRenderer(rightRenderer);
        
        invoiceDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setMaxWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(TAX_5_COLUMN).setCellRenderer(rightRenderer);
        
        invoiceDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setMaxWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setPreferredWidth(100);
        invoiceDetailTable.getColumnModel().getColumn(TAX_10_COLUMN).setCellRenderer(rightRenderer);
        
         //Sub Totales
        subTotalLabel = new JLabel("Sub Totales: ");
        subTotalLabel.setBounds(30, 660, 80, 25);
        invoiceCreatePanel.add(subTotalLabel);
        
        total5Label = new JLabel("5%: ");
        total5Label.setBounds(160, 660, 30, 25);
        invoiceCreatePanel.add(total5Label);
        
        total5TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("tax05total").toString())));
        total5TextField.setBounds(200, 660, 170, 25);
        total5TextField.setEditable(false);
        total5TextField.setHorizontalAlignment(JLabel.RIGHT);
        invoiceCreatePanel.add(total5TextField);
        
        total10Label = new JLabel("10%: ");
        total10Label.setBounds(390, 660, 35, 25);
        invoiceCreatePanel.add(total10Label);
        
        total10TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("taxted10total").toString())));
        total10TextField.setEditable(false);
        total10TextField.setHorizontalAlignment(JLabel.RIGHT);
        total10TextField.setBounds(440, 660, 170, 25);
        invoiceCreatePanel.add(total10TextField);
        
        exemptTotalLabel = new JLabel("Exentas: ");
        exemptTotalLabel.setBounds(640, 660, 100, 25);
        invoiceCreatePanel.add(exemptTotalLabel);
        
        exemptTotalTextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("exempt_total").toString())));
        exemptTotalTextField.setEditable(false);
        exemptTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        exemptTotalTextField.setBounds(730, 660, 170, 25);
        invoiceCreatePanel.add(exemptTotalTextField);
        
        //Liquidacion del IVA
        taxSettlementLabel = new JLabel("Liquidacion del IVA: ");
        taxSettlementLabel.setBounds(30, 690, 130, 25);
        invoiceCreatePanel.add(taxSettlementLabel);
        
        tax5Label = new JLabel("5%: ");
        tax5Label.setBounds(160, 690, 30, 25);
        invoiceCreatePanel.add(tax5Label);
        
        tax5TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("tax05total").toString())));
        tax5TextField.setBounds(200, 690, 170, 25);
        tax5TextField.setEditable(false);
        tax5TextField.setHorizontalAlignment(JLabel.RIGHT);
        invoiceCreatePanel.add(tax5TextField);
        
        tax10Label = new JLabel("10%: ");
        tax10Label.setBounds(390, 690, 35, 25);
        invoiceCreatePanel.add(tax10Label);
        
        tax10TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("tax10total").toString())));
        tax10TextField.setEditable(false);
        tax10TextField.setHorizontalAlignment(JLabel.RIGHT);
        tax10TextField.setBounds(440, 690, 170, 25);
        invoiceCreatePanel.add(tax10TextField);
        
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(640, 690, 100, 25);
        invoiceCreatePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("tax_total").toString())));
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(730, 690, 170, 25);
        invoiceCreatePanel.add(totalTaxTextField);
        
        //Total
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(30, 720, 80, 25);
        invoiceCreatePanel.add(totalLabel);

        totalTextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("net_total").toString())));
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(200, 720, 170, 25);
        invoiceCreatePanel.add(totalTextField);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelInvoice = new JButton();
        cancelInvoice.setText(" Cerrar");
        cancelInvoice.setIcon(cancelImageIcon);
        cancelInvoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelInvoice.setBounds(1000, 770, 170, 32);
        invoiceCreatePanel.add(cancelInvoice);
        
        
        HashMap invoiceDetailMap;
        ArrayList invoiceDetailList = (ArrayList) invoiceMap.get("invoiceDetail");
        Object[] row = new Object[invoiceDetailTable.getColumnCount()];
        
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
            row[EVENT_DETAIL_ID_COLUMN] = invoiceDetailMap.get("id").toString();
        
            invoiceDetailDefaultTableModel.addRow(row);
        }
        
        add(invoiceCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Factura");
        setBounds(250,30,1250,860);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        invoiceController.invoiceShowClosed();
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
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
