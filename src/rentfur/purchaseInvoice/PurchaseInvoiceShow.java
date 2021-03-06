/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.purchaseInvoice;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.java.balloontip.BalloonTip;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.event.EventController;
import rentfur.provider.ProviderController;
import rentfur.subject.SubjectController;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */

public class PurchaseInvoiceShow extends JInternalFrame{
    private final PurchaseInvoiceController purchaseInvoiceController;
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
    
    private final HashMap providerMap;
    private final HashMap invoiceMap;
    
    private static final double TAX5 = 21;
    private static final double TAX10 = 11;
    
    private final ImageIcon helpIconImage;
    private final JLabel helpLabel;
    private BalloonTip helpBalloonTip;
    public PurchaseInvoiceShow(PurchaseInvoiceController PurchaseInvoiceController, int invoiceId){
        this.purchaseInvoiceController = PurchaseInvoiceController;
        
        invoiceCreatePanel = new JPanel();
        invoiceCreatePanel.setLayout(null);
        
        invoiceMap = PurchaseInvoiceController.getPurchaseInvoiceById(invoiceId);
        providerMap = ProviderController.getProviderByCode(invoiceMap.get("provider_code").toString());
        
        
        titleLabel = new JLabel("<HTML><U>Factura de Compra</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(500, 20, 300, 25);
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
        
        subjectLabel = new JLabel("<HTML><U>Datos del Proveedor</U></HTML>");
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

        subjectCodeTextField = new JTextField(providerMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        invoiceCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        invoiceCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(providerMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        invoiceCreatePanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        invoiceCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(providerMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        invoiceCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        invoiceCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(providerMap.get("telephone").toString());
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
        
        subjectTradenameTextField = new JTextField(providerMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        invoiceCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        invoiceCreatePanel.add(subjectFiscalNumberLabel);
        
        subjectFiscalNumberTextField = new JTextField();
        String fiscalNumber; 
        if(((String)providerMap.get("fiscalNumber")).contains("-")){
            fiscalNumber = amountFormat.format(Double.valueOf(((String)providerMap.get("fiscalNumber")).split("-")[0]));
            subjectFiscalNumberTextField.setText(fiscalNumber+"-"+((String)providerMap.get("fiscalNumber")).split("-")[1]);
        }else{
            fiscalNumber = amountFormat.format(Double.valueOf(((String)providerMap.get("fiscalNumber"))));
            subjectFiscalNumberTextField.setText(fiscalNumber);
        }
        subjectFiscalNumberTextField.setEditable(false);
        subjectFiscalNumberTextField.setBounds(490, 210, 170, 25);
        invoiceCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        invoiceCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(providerMap.get("city").toString());
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
        
        total5TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("total_taxable_5").toString())));
        total5TextField.setBounds(200, 660, 170, 25);
        total5TextField.setEditable(false);
        total5TextField.setHorizontalAlignment(JLabel.RIGHT);
        invoiceCreatePanel.add(total5TextField);
        
        total10Label = new JLabel("10%: ");
        total10Label.setBounds(390, 660, 35, 25);
        invoiceCreatePanel.add(total10Label);
        
        total10TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("total_taxable_10").toString())));
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
        
        tax5TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("total_tax_5").toString())));
        tax5TextField.setBounds(200, 690, 170, 25);
        tax5TextField.setEditable(false);
        tax5TextField.setHorizontalAlignment(JLabel.RIGHT);
        invoiceCreatePanel.add(tax5TextField);
        
        tax10Label = new JLabel("10%: ");
        tax10Label.setBounds(390, 690, 35, 25);
        invoiceCreatePanel.add(tax10Label);
        
        tax10TextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("total_tax_10").toString())));
        tax10TextField.setEditable(false);
        tax10TextField.setHorizontalAlignment(JLabel.RIGHT);
        tax10TextField.setBounds(440, 690, 170, 25);
        invoiceCreatePanel.add(tax10TextField);
        
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(640, 690, 100, 25);
        invoiceCreatePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField(amountFormat.format(Double.valueOf(invoiceMap.get("total_tax").toString())));
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

            row[ITEM_COLUMN] = i+1;
            row[CODE_COLUMN] = invoiceDetailMap.get("code");
            row[DESCRIPTION_COLUMN] = invoiceDetailMap.get("description");
            row[TAX_RATE_COLUMN] = amountFormat.format((Integer)invoiceDetailMap.get("tax_rate"));
            row[QUANTITY_COLUMN] = amountFormat.format((Integer)invoiceDetailMap.get("quantity"));
            row[UNIT_PRICE_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("unit_price"));
            //row[EXEMPT_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("exempt_amount"));
            row[EXEMPT_COLUMN] = 0;
            row[TAX_5_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("taxable_amount_5"));
            row[TAX_10_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("taxable_amount_10"));
            row[SUB_TOTAL_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("total_amount"));
            row[TAX_AMOUNT_COLUMN] = amountFormat.format((Double)invoiceDetailMap.get("tax_amount"));
            row[EVENT_DETAIL_ID_COLUMN] = 0;
        
            invoiceDetailDefaultTableModel.addRow(row);
        }
        
        helpIconImage  = new ImageIcon(getClass().getResource("/rentfur/button/image/util/help_24x24.png"));
        helpLabel = new JLabel("AYUDA");
        helpLabel.setIcon(helpIconImage);
        helpLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showHelp();
                    }
        });
        helpLabel.setBounds(1150, 20, 80, 25);
        invoiceCreatePanel.add(helpLabel);

        helpBalloonTip = new BalloonTip(helpLabel, "<html><head></head><body style='background:#F4EFEF;'><div style='margin:24px 34px;'><h2>Detalles de la Compra</h2><p>En esta vista se muestran los datos de la compra seleccionada. Primeramente se muestran los datos de la cabecera como:</p><ul><li><p>Fecha de factura</p></li><li><p>Numero</p></li><li><p>Timbrado</p></li><li><p>Datos del Proveedor</p></li></ul><p>En el sector de abajo se muestra una tabla con los detalles de la factura compra.</p><ul><li><p>Numero Item</p></li><li><p>Codigo</p></li><li><p>Descripcion</p></li><li><p>Tasa de Impuesto</p></li><li><p>Cantidad</p></li><li><p>Precio unitario</p></li><li><p>Exenta</p></li><li><p>Gravada 5%</p></li><li><p>Gravada 10%</p></li></ul><p>Y por ultimo, en la parte inferior de la vista se muestra los totales de la factura compra.</p><ul><li><p>Subtotales 5%, 10% y exenta</p></li><li><p>Liquidacion del Iva</p></li><li><p>Total Iva</p></li><li><p>Total Gs</p></li></ul></div></body></html>");
        helpBalloonTip.setVisible(false);
        helpBalloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false);
        
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
    
    private void addItemsToDetailTable(HashMap furnitureCodes){
        try {
            HashMap furnitureMap;
            HashMap itemValuesMap;
            ArrayList<Object> furnitureCodesList = new ArrayList<>(furnitureCodes.keySet());
            ArrayList furnitureList = null;//FurnitureController.getFurnitureListByCodeList(furnitureCodesList);
            int rowNum = invoiceDetailDefaultTableModel.getRowCount()+1;
            double price, quantity, subTotal;
            
            double subTotal5 = amountFormat.parse(total5TextField.getText()).doubleValue();
            double subTotal10 = amountFormat.parse(total10TextField.getText()).doubleValue();
            double subTotalExempt = amountFormat.parse(exemptTotalTextField.getText()).doubleValue();
            
            double totalTax5 = amountFormat.parse(tax5TextField.getText()).doubleValue();
            double totalTax10 = amountFormat.parse(tax10TextField.getText()).doubleValue();
            double totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
            
            double total = amountFormat.parse(totalTextField.getText()).doubleValue();
            
            Object[] row = new Object[invoiceDetailTable.getColumnCount()];
            for(int i = 0; i < furnitureList.size(); i++){
                furnitureMap = (HashMap) furnitureList.get(i);
                itemValuesMap = (HashMap) furnitureCodes.get(furnitureMap.get("code").toString());
                //furnitureCodes.add(furnitureMap.get("code"));
                price = Double.valueOf(itemValuesMap.get("price").toString());
                quantity = Double.valueOf(itemValuesMap.get("quantity").toString());
                
                row[ITEM_COLUMN] = rowNum++;
                row[CODE_COLUMN] = furnitureMap.get("code");
                row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
                row[TAX_RATE_COLUMN] = amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString()));
                row[QUANTITY_COLUMN] = amountFormat.format(Double.valueOf(itemValuesMap.get("quantity").toString()));
                row[UNIT_PRICE_COLUMN] = amountFormat.format(Double.valueOf(itemValuesMap.get("price").toString()));
                
                subTotal = price * quantity;
                total = total + subTotal;
                if(amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString())).equals("0")){
                    row[EXEMPT_COLUMN] = amountFormat.format(subTotal);
                    subTotalExempt = subTotalExempt + subTotal;
                    exemptTotalTextField.setText(amountFormat.format(subTotalExempt));
                }else{
                    row[EXEMPT_COLUMN] = 0;
                }
                
                if(amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString())).equals("5")){
                    row[TAX_5_COLUMN] = amountFormat.format(subTotal);
                    subTotal5 = subTotal5 + subTotal;
                    totalTax5 = totalTax5 + (new BigDecimal(subTotal / TAX5).setScale(0, RoundingMode.HALF_UP).doubleValue());
                    totalTax = totalTax + (new BigDecimal(subTotal / TAX5).setScale(0, RoundingMode.HALF_UP).doubleValue());
                    total5TextField.setText(amountFormat.format(subTotal5));
                    tax5TextField.setText(amountFormat.format(totalTax5));
                    
                    row[TAX_AMOUNT_COLUMN] = new BigDecimal(subTotal / TAX5).setScale(0, RoundingMode.HALF_UP).doubleValue();
                }else{
                    row[TAX_5_COLUMN] = 0;
                }
                
                if(amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString())).equals("10")){
                    row[TAX_10_COLUMN] = amountFormat.format(subTotal);
                    subTotal10 = subTotal10 + subTotal;
                    totalTax10 = totalTax10 + (new BigDecimal(subTotal / TAX10).setScale(0, RoundingMode.HALF_UP).doubleValue());
                    totalTax = totalTax + (new BigDecimal(subTotal / TAX10).setScale(0, RoundingMode.HALF_UP).doubleValue());
                    total10TextField.setText(amountFormat.format(subTotal10));
                    tax10TextField.setText(amountFormat.format(totalTax10));
                    
                    row[TAX_AMOUNT_COLUMN] = new BigDecimal(subTotal / TAX10).setScale(0, RoundingMode.HALF_UP).doubleValue();
                }else{
                    row[TAX_10_COLUMN] = 0;
                }
                
                if(amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString())).equals("0")){
                    row[TAX_AMOUNT_COLUMN] = 0;
                }
                
                row[SUB_TOTAL_COLUMN] = amountFormat.format(subTotal);
                row[EVENT_DETAIL_ID_COLUMN] = Integer.valueOf(itemValuesMap.get("eventDetailId").toString());
                invoiceDetailDefaultTableModel.addRow(row);
            }
            
            totalTaxTextField.setText(amountFormat.format(totalTax));
            totalTextField.setText(amountFormat.format(total));
            
        } catch (ParseException ex) {
            Logger.getLogger(PurchaseInvoiceShow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateDetailTableRowItem(){
        
        for(int i = 1; i <= invoiceDetailDefaultTableModel.getRowCount(); i++){
            invoiceDetailDefaultTableModel.setValueAt(i, (i-1), ITEM_COLUMN);
        }
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        purchaseInvoiceController.purchaseInvoiceShowClosed();
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
    
    public void showHelp(){
        if(!helpBalloonTip.isVisible()){
            helpBalloonTip.setVisible(true);
        }
    }
}
