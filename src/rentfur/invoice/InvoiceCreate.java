/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.invoice;

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
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.table.TableCellRenderer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.book.BookController;
import rentfur.event.EventController;
import rentfur.furniture.FurnitureController;
import rentfur.subject.SubjectController;
import rentfur.util.DateLabelFormatter;
import rentfur.util.searches.FurnitureInvoiceSearch;
import rentfur.util.searches.SearchController;

/**
 *
 * @author FDuarte
 */
public class InvoiceCreate extends JInternalFrame{
    private final InvoiceController invoiceController;
    private final SearchController searchController;
    private FurnitureInvoiceSearch furnitureInvoiceSearch;
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
    private final JButton addFurnituresButton;
    private final JButton saveInvoice;
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
    private final int DELETE_COLUMN = 9;
    private final int SUB_TOTAL_COLUMN = 10;
    private final int TAX_AMOUNT_COLUMN = 11;
    private final int EVENT_DETAIL_ID_COLUMN = 12;
    
    private final HashMap eventMap;
    private final HashMap subjectMap;
    private final HashMap invoiceNumMap;
    
    private static final double TAX5 = 21;
    private static final double TAX10 = 11;
    
    private final ArrayList furnitureCodesAdded = new ArrayList();
    
    public InvoiceCreate(InvoiceController invoiceController, int eventId){
        this.invoiceController = invoiceController;
        searchController = new SearchController();
        
        invoiceCreatePanel = new JPanel();
        invoiceCreatePanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        invoiceNumMap = BookController.getInvoicetNum();
        
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
        
        branchTextField = new JTextField(invoiceNumMap.get("branch").toString());
        branchTextField.setEditable(false);
        branchTextField.setHorizontalAlignment(JLabel.CENTER);
        branchTextField.setBounds(470, 60, 40, 25);
        invoiceCreatePanel.add(branchTextField);
        
        JLabel firstSeparator = new JLabel("-");
        firstSeparator.setBounds(517, 60, 40, 25);
        invoiceCreatePanel.add(firstSeparator);
        
        printerTextField = new JTextField(invoiceNumMap.get("printer").toString());
        printerTextField.setEditable(false);
        printerTextField.setHorizontalAlignment(JLabel.CENTER);
        printerTextField.setBounds(530, 60, 40, 25);
        invoiceCreatePanel.add(printerTextField);
        
        JLabel secondSeparator = new JLabel("-");
        secondSeparator.setBounds(577, 60, 40, 25);
        invoiceCreatePanel.add(secondSeparator);
        
        String receiptNum = String.format ("%07d", (Integer) invoiceNumMap.get("number"));
        numberTextField = new JTextField(receiptNum);
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
        
        fiscalStampNumberTextField = new JTextField(invoiceNumMap.get("fiscalStampNumber").toString());
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
        
        ImageIcon addFurnitureImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addFurnituresButton = new JButton();
        addFurnituresButton.setIcon(addFurnitureImageIcon);
        addFurnituresButton.setBounds(30, 290, 180, 32);
        addFurnituresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addDetailsButtonAction();
            }
        });
        addFurnituresButton.setText(" Seleccionar Detalles");
        invoiceCreatePanel.add(addFurnituresButton);
        
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
        invoiceDetailDefaultTableModel.addColumn("");
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
        
        invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        
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
        
        invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setMinWidth(60);
        invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setMaxWidth(60);
        invoiceDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setPreferredWidth(60);
        
         //Sub Totales
        subTotalLabel = new JLabel("Sub Totales: ");
        subTotalLabel.setBounds(30, 660, 80, 25);
        invoiceCreatePanel.add(subTotalLabel);
        
        total5Label = new JLabel("5%: ");
        total5Label.setBounds(160, 660, 30, 25);
        invoiceCreatePanel.add(total5Label);
        
        total5TextField = new JTextField("0");
        total5TextField.setBounds(200, 660, 170, 25);
        total5TextField.setEditable(false);
        total5TextField.setHorizontalAlignment(JLabel.RIGHT);
        invoiceCreatePanel.add(total5TextField);
        
        total10Label = new JLabel("10%: ");
        total10Label.setBounds(390, 660, 35, 25);
        invoiceCreatePanel.add(total10Label);
        
        total10TextField = new JTextField("0");
        total10TextField.setEditable(false);
        total10TextField.setHorizontalAlignment(JLabel.RIGHT);
        total10TextField.setBounds(440, 660, 170, 25);
        invoiceCreatePanel.add(total10TextField);
        
        exemptTotalLabel = new JLabel("Exentas: ");
        exemptTotalLabel.setBounds(640, 660, 100, 25);
        invoiceCreatePanel.add(exemptTotalLabel);
        
        exemptTotalTextField = new JTextField("0");
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
        
        tax5TextField = new JTextField("0");
        tax5TextField.setBounds(200, 690, 170, 25);
        tax5TextField.setEditable(false);
        tax5TextField.setHorizontalAlignment(JLabel.RIGHT);
        invoiceCreatePanel.add(tax5TextField);
        
        tax10Label = new JLabel("10%: ");
        tax10Label.setBounds(390, 690, 35, 25);
        invoiceCreatePanel.add(tax10Label);
        
        tax10TextField = new JTextField("0");
        tax10TextField.setEditable(false);
        tax10TextField.setHorizontalAlignment(JLabel.RIGHT);
        tax10TextField.setBounds(440, 690, 170, 25);
        invoiceCreatePanel.add(tax10TextField);
        
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(640, 690, 100, 25);
        invoiceCreatePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField("0");
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(730, 690, 170, 25);
        invoiceCreatePanel.add(totalTaxTextField);
        
        //Total
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(30, 720, 80, 25);
        invoiceCreatePanel.add(totalLabel);
    
        totalTextField = new JTextField("0");
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(200, 720, 170, 25);
        invoiceCreatePanel.add(totalTextField);
        
        ImageIcon saveReceiptImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveInvoice = new JButton();
        saveInvoice.setText(" Guardar");
        saveInvoice.setIcon(saveReceiptImageIcon);
        saveInvoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveInvoiceButtonAction();
            }
        });
        saveInvoice.setBounds(800, 770, 170, 32);
        invoiceCreatePanel.add(saveInvoice);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelInvoice = new JButton();
        cancelInvoice.setText(" Cancelar");
        cancelInvoice.setIcon(cancelImageIcon);
        cancelInvoice.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelInvoice.setBounds(1000, 770, 170, 32);
        invoiceCreatePanel.add(cancelInvoice);
        
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
    
    private void addDetailsButtonAction(){
        int eventId = Integer.valueOf(eventMap.get("id").toString());
        ArrayList furnitureDetailList = (ArrayList) eventMap.get("detail");
        furnitureDetailList.addAll((ArrayList) eventMap.get("penaltyDetail"));
        furnitureInvoiceSearch = searchController.getInvoiceFurnitureSearch(eventId, furnitureDetailList, furnitureCodesAdded);
        furnitureInvoiceSearch.setVisible(true);
        showSearchDialog(furnitureInvoiceSearch);
        inactivateElements();
        furnitureInvoiceSearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
                if(!searchController.getFurnitureInvoicedCodes().isEmpty()){
                    addFuritureSelectedToDetailTable(searchController.getFurnitureInvoicedCodes());
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
    
    private void saveInvoiceButtonAction(){
        JOptionPane optionPane;
        JDialog dialog;
        if (invoiceDetailTable.isEditing()){
            invoiceDetailTable.getCellEditor().stopCellEditing();
        }
        
        if(invoiceDetailTable.getRowCount()==0){
            optionPane = new JOptionPane("No ha sido agregado ningun detalle (item)", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            Date invoceDate = (Date) datePicker.getModel().getValue();
            HashMap detailMap;
            ArrayList invoiceDetailList = new ArrayList();
            Vector dataVector;
            double netTotal = 0;
            double exemptTotal = 0, tax05total = 0, tax10total = 0, taxTotal = 0, taxted05total = 0, taxted10total = 0;
            String observation = observationTextArea.getText();
            int eventId = Integer.valueOf(eventMap.get("id").toString());
            try {
            
                for (int row = 0; row < invoiceDetailTable.getRowCount(); row++){
                    dataVector = (Vector) invoiceDetailDefaultTableModel.getDataVector().get(row);
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
                    detailMap.put("taxAmount", Double.valueOf(dataVector.get(TAX_AMOUNT_COLUMN).toString()));
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
                        
            } catch (ParseException ex) {
                Logger.getLogger(InvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            HashMap returnMap = invoiceController.createInvoice(subjectMap, invoiceDetailList, invoceDate, eventId, invoiceNumMap, netTotal, exemptTotal, tax05total, tax10total, taxTotal, taxted05total, taxted10total, observation);
            
            if(((Integer)returnMap.get("status"))==InvoiceController.SUCCESFULLY_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
            }else if((Integer)returnMap.get("status") == InvoiceController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void addFuritureSelectedToDetailTable(HashMap furnitureCodes){
        try {
            HashMap furnitureMap;
            HashMap itemValuesMap;
            ArrayList<Object> furnitureCodesList = new ArrayList<>(furnitureCodes.keySet());
            ArrayList furnitureList = FurnitureController.getFurnitureListByCodeList(furnitureCodesList);
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
                furnitureCodesAdded.add(furnitureMap.get("code"));
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
                    totalTax5 = totalTax5 + (new BigDecimal(subTotal / TAX5).setScale(2, RoundingMode.CEILING).doubleValue());
                    totalTax = totalTax + (new BigDecimal(subTotal / TAX5).setScale(2, RoundingMode.CEILING).doubleValue());
                    total5TextField.setText(amountFormat.format(subTotal5));
                    tax5TextField.setText(amountFormat.format(totalTax5));
                    
                    row[TAX_AMOUNT_COLUMN] = new BigDecimal(subTotal / TAX5).setScale(2, RoundingMode.CEILING).doubleValue();
                }else{
                    row[TAX_5_COLUMN] = 0;
                }
                
                if(amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString())).equals("10")){
                    row[TAX_10_COLUMN] = amountFormat.format(subTotal);
                    subTotal10 = subTotal10 + subTotal;
                    totalTax10 = totalTax10 + (new BigDecimal(subTotal / TAX10).setScale(2, RoundingMode.CEILING).doubleValue());
                    
                    totalTax = totalTax + (new BigDecimal(subTotal / TAX10).setScale(2, RoundingMode.CEILING).doubleValue());
                    total10TextField.setText(amountFormat.format(subTotal10));
                    tax10TextField.setText(amountFormat.format(totalTax10));
                    
                    row[TAX_AMOUNT_COLUMN] = new BigDecimal(subTotal / TAX10).setScale(2, RoundingMode.CEILING).doubleValue();
                }else{
                    row[TAX_10_COLUMN] = 0;
                }
                
                if(amountFormat.format(Double.valueOf(itemValuesMap.get("taxRate").toString())).equals("0")){
                    row[TAX_AMOUNT_COLUMN] = 0;
                }
                
                row[DELETE_COLUMN] = "";
                row[SUB_TOTAL_COLUMN] = amountFormat.format(subTotal);
                row[EVENT_DETAIL_ID_COLUMN] = Integer.valueOf(itemValuesMap.get("eventDetailId").toString());
                invoiceDetailDefaultTableModel.addRow(row);
            }
            
            totalTaxTextField.setText(amountFormat.format(totalTax));
            totalTextField.setText(amountFormat.format(total));
            
        } catch (ParseException ex) {
            Logger.getLogger(InvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateDetailTableRowItem(){
        
        for(int i = 1; i <= invoiceDetailDefaultTableModel.getRowCount(); i++){
            invoiceDetailDefaultTableModel.setValueAt(i, (i-1), ITEM_COLUMN);
        }
        
    }
    
    private void removeRow(int row) throws ParseException{
        try {
            double subTotal = amountFormat.parse(invoiceDetailTable.getValueAt(row, SUB_TOTAL_COLUMN).toString()).doubleValue();

            //Item tax
            String furnitureTaxRateString = invoiceDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
            double itemTaxAmount = 0;
            double taxAmount = 0;
            double subTotal10 = 0;
            double subTotal5 = 0 ;
            double exemptTotal = 0;
            double currentSubTotalAmount = 0;
            
            switch (furnitureTaxRateString) {
                case "5":
                    subTotal5 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAX_5_COLUMN).toString()).doubleValue();
                    itemTaxAmount = new BigDecimal(subTotal5/ TAX5).setScale(2, RoundingMode.CEILING).doubleValue();
                    
                    //SUBTOTAL
                    currentSubTotalAmount = amountFormat.parse(total5TextField.getText()).doubleValue();
                    currentSubTotalAmount = currentSubTotalAmount - subTotal5;
                    total5TextField.setText(amountFormat.format(currentSubTotalAmount));
                            
                    //TAX
                    taxAmount = amountFormat.parse(tax5TextField.getText()).doubleValue();
                    taxAmount = taxAmount - itemTaxAmount;
                    tax5TextField.setText(amountFormat.format(taxAmount));
                    break;
                case "10":
                    subTotal10 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAX_10_COLUMN).toString()).doubleValue();
                    
                    //SUBTOTAL
                    currentSubTotalAmount = amountFormat.parse(total10TextField.getText()).doubleValue();
                    currentSubTotalAmount = currentSubTotalAmount - subTotal10;
                    total10TextField.setText(amountFormat.format(currentSubTotalAmount));
                    
                    //TAX
                    itemTaxAmount = new BigDecimal(subTotal10 / TAX10).setScale(2, RoundingMode.CEILING).doubleValue();
                    taxAmount = amountFormat.parse(tax10TextField.getText()).doubleValue();
                    taxAmount = taxAmount - itemTaxAmount;
                    tax10TextField.setText(amountFormat.format(taxAmount));
                    break;
                default:
                    //SUBTOTAL
                    exemptTotal = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, EXEMPT_COLUMN).toString()).doubleValue();
                    currentSubTotalAmount = amountFormat.parse(exemptTotalTextField.getText()).doubleValue();
                    currentSubTotalAmount = currentSubTotalAmount - exemptTotal;
                    exemptTotalTextField.setText(amountFormat.format(currentSubTotalAmount));
                    break;
            }

            //TOTAL
            double total = amountFormat.parse(totalTextField.getText()).doubleValue();
            total = total - subTotal;
            totalTextField.setText(amountFormat.format(total));

            //TOTAL IVA
            double totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
            totalTax = totalTax - itemTaxAmount;
            totalTaxTextField.setText(amountFormat.format(totalTax));

            String furnitureCode = invoiceDetailDefaultTableModel.getValueAt(row, CODE_COLUMN).toString();
            furnitureCodesAdded.remove(furnitureCode);
            invoiceDetailDefaultTableModel.removeRow(row);
            
            updateDetailTableRowItem();
        }catch (Throwable th) {
            Logger.getLogger(InvoiceCreate.class.getName()).log(Level.SEVERE, null, th);
        }
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        invoiceController.invoiceCreateClosed();
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
        addFurnituresButton.setEnabled(false);
//        saveButton.setEnabled(false);
//        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        addFurnituresButton.setEnabled(true);
//        saveButton.setEnabled(true);
//        cancelButton.setEnabled(true);
    }
    
    private class InvoiceDetailDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            switch(column){
                case DELETE_COLUMN:
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
                Logger.getLogger(InvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      }
}
