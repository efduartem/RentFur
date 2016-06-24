/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.purchaseInvoice;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.event.EventCreate;
import rentfur.furniture.FurnitureController;
import rentfur.provider.ProviderController;
import rentfur.subject.SubjectController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DateLabelFormatter;
import rentfur.util.NumericTextField;
import rentfur.util.SQLUtilService;
import rentfur.util.searches.FurnitureSearch;
import rentfur.util.searches.ProviderSearch;
import rentfur.util.searches.SearchController;

/**
 *
 * @author hp
 */
public class PurchaseInvoiceCreate extends JInternalFrame{
    private final PurchaseInvoiceController purchaseInvoiceController;
    private  ProviderSearch providerSearch;
    private FurnitureSearch furnitureSearch;
    private final SearchController searchController;
    private final JPanel purchaseInvoicePanel;
    private final JLabel invoicingDateLabel;
    private final JLabel invoiceNumberLabel;
    private final JLabel invoiceFiscalStampNumberLabel;
    private final JLabel providerLabel;
    private final JLabel providerCodeLabel;
    private final JLabel providerNameLabel;
    private final JLabel providerAddressLabel;
    private final JLabel providerTelephoneLabel;
    private final JLabel providerTradenameLabel;
    private final JLabel providerFiscalNumberLabel;
    private final JLabel providerCityLabel;
    private final JLabel invoiceGuionLabel;
    private final JLabel totalLabel;
    private final JLabel totalTaxLabel;
    private final JLabel invoiceGuion2Label;
    private final JLabel subTotalLabel;
    private final JLabel total5Label;
    private final JLabel total10Label;
    private final JLabel exemptTotalLabel;
    private final JLabel taxSettlementLabel;
    private final JLabel tax5Label;
    private final JLabel tax10Label;
    private final JTextField invoiceBranchTextField;
    private final JTextField invoicePrinterTextField;
    private final JTextField invoiceNumberTextField;
    private final JTextField invoiceFiscalStampNumberTextField;
    private final JTextField providerCodeTextField;
    private final JTextField providerNameTextField;
    private final JTextField providerAddressTextField;
    private final JTextField providerTelephoneTextField;
    private final JTextField providerTradenameTextField;
    private final JTextField providerFiscalNumberTextField;
    private final JTextField providerCityTextField;
    private final JTextField total5TextField;
    private final JTextField total10TextField;
    private final JTextField exemptTotalTextField;
    private final JTextField tax5TextField;
    private final JTextField tax10TextField;
    private final JTextField totalTaxTextField;
    private final JTextField totalTextField;
    private final JButton selectProviderButton;
    private final JButton addFurnituresButton;
    private JDatePickerImpl datePicker;
    private final JButton saveButton;
    private final JButton cancelInvoice;
    private final JButton addItemEmptyButton;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    private final ArrayList furnitureCodesAdded = new ArrayList();
    private final JTable invoiceDetailTable;
    private final DefaultTableModel invoiceDetailDefaultTableModel;
    private JDesktopPane pane;
   
    private final int ID_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int TAX_RATE_COLUMN = 3;
    private final int STOCK_COLUMN = 4;//NO SE USA
    private final int UNIT_PRICE = 5;
    private final int TAX_AMOUNT = 6;//NO SE USA
    private final int QUANTITY_COLUMN = 7;
    private final int SUB_TOTAL_COLUMN = 8;
    private final int EXEMPT_AMOUNT = 9;
    private final int TAXT_RATE_10 = 10;
    private final int TAXT_RATE_5 = 11;
    private final int DELETE_BUTTON_COLUMN = 12;
    
    private static final double TAX5 = 21;
    private static final double TAX10 = 11;
    
    private ArrayList taxList = new ArrayList(); //Tasas de IVA
    private HashMap taxRatioMap = new HashMap(); //Cocientes para IVA
    private HashMap taxableRatioMap = new HashMap();//Cocientes para Gravadas
    
    private final JScrollPane invoiceDetailTableJScrollPane;
   public PurchaseInvoiceCreate(PurchaseInvoiceController purchaseInvoiceController){
       this.purchaseInvoiceController = purchaseInvoiceController;
       searchController = new SearchController();
       
       purchaseInvoicePanel = new JPanel();
       purchaseInvoicePanel.setLayout(null);
       
       invoicingDateLabel = new JLabel("Fecha Factura:");
       invoicingDateLabel.setBounds(30, 30, 130, 25);
       purchaseInvoicePanel.add(invoicingDateLabel);
       
       UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        datePicker.setBounds(160, 30, 170, 25);
        purchaseInvoicePanel.add(datePicker);
        
       invoiceNumberLabel = new JLabel("Numero Factura:");
       invoiceNumberLabel.setBounds(370, 30, 130, 25);
       purchaseInvoicePanel.add(invoiceNumberLabel);
       
       invoiceBranchTextField = new JTextField();
       invoiceBranchTextField.setBounds(490, 30, 50, 25);
       purchaseInvoicePanel.add(invoiceBranchTextField);
       
       invoiceGuionLabel = new JLabel("-");
       invoiceGuionLabel.setBounds(550, 30, 10, 25);
       purchaseInvoicePanel.add(invoiceGuionLabel);
       
       invoicePrinterTextField = new JTextField();
       invoicePrinterTextField.setBounds(560, 30, 50, 25);
       purchaseInvoicePanel.add(invoicePrinterTextField);
       
       invoiceGuion2Label = new JLabel("-");
       invoiceGuion2Label.setBounds(620, 30, 10, 25);
       purchaseInvoicePanel.add(invoiceGuion2Label);
       
       invoiceNumberTextField = new JTextField();
       invoiceNumberTextField.setBounds(630, 30, 70, 25);
       purchaseInvoicePanel.add(invoiceNumberTextField);
       
       invoiceFiscalStampNumberLabel = new JLabel("Timbrado:");
       invoiceFiscalStampNumberLabel.setBounds(730, 30, 70, 25);
       purchaseInvoicePanel.add(invoiceFiscalStampNumberLabel);
       
       invoiceFiscalStampNumberTextField = new JTextField("");
       invoiceFiscalStampNumberTextField.setBounds(800, 30, 70, 25);
       purchaseInvoicePanel.add(invoiceFiscalStampNumberTextField);
       
       providerLabel = new JLabel("<HTML><U>Datos del Proveedor</U></HTML>");
       providerLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
       providerLabel.setBounds(80, 80, 200, 25);
       purchaseInvoicePanel.add(providerLabel);
       
       selectProviderButton = new JButton();
        ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/provider_32x32.png"));
        selectProviderButton.setIcon(subjectIconImage);
        selectProviderButton.setBounds(260, 80, 200, 32);
        selectProviderButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectProviderButtonAction();
            }
        });
        selectProviderButton.setText(" Seleccionar Proveedor");
        purchaseInvoicePanel.add(selectProviderButton);
       
       //FILA 1
       providerCodeLabel = new JLabel("Codigo:");
       providerCodeLabel.setBounds(30, 140, 80, 25);
       purchaseInvoicePanel.add(providerCodeLabel);
       
       providerCodeTextField = new JTextField("");
       providerCodeTextField.setEditable(false);
       providerCodeTextField.setBounds(160, 140, 170, 25);
       purchaseInvoicePanel.add(providerCodeTextField);
       
       providerAddressLabel = new JLabel("Dirección:");
       providerAddressLabel.setBounds(370, 140, 160, 25);
       purchaseInvoicePanel.add(providerAddressLabel);
        
       providerAddressTextField = new JTextField();
       providerAddressTextField.setEditable(false);
       providerAddressTextField.setBounds(490, 140, 170, 25);
       purchaseInvoicePanel.add(providerAddressTextField);
       
       //FILA 2
       providerNameLabel = new JLabel("Razon Social:");
       providerNameLabel.setBounds(30, 170, 80, 25);
       purchaseInvoicePanel.add(providerNameLabel);
       
       providerNameTextField = new JTextField("");
       providerNameTextField.setEditable(false);
       providerNameTextField.setBounds(160, 170, 170, 25);
       purchaseInvoicePanel.add(providerNameTextField);
        
       providerTelephoneLabel = new JLabel("Telefono:");
       providerTelephoneLabel.setBounds(370, 170, 80, 25);
       purchaseInvoicePanel.add(providerTelephoneLabel);
       
       providerTelephoneTextField = new JTextField("");
       providerTelephoneTextField.setEditable(false);
       providerTelephoneTextField.setBounds(490, 170, 170, 25);
       purchaseInvoicePanel.add(providerTelephoneTextField);
       
       //FILA 3
       providerTradenameLabel = new JLabel("Nombre Comercial:");
       providerTradenameLabel.setBounds(30, 200, 120, 25);
       purchaseInvoicePanel.add(providerTradenameLabel);
       
       providerTradenameTextField = new JTextField("");
       providerTradenameTextField.setEditable(false);
       providerTradenameTextField.setBounds(160, 200, 170, 25);
       purchaseInvoicePanel.add(providerTradenameTextField);
       
       providerFiscalNumberLabel = new JLabel("RUC/CI:");
       providerFiscalNumberLabel.setBounds(370, 200, 80, 25);
       purchaseInvoicePanel.add(providerFiscalNumberLabel);
       
       providerFiscalNumberTextField = new JTextField("");
       providerFiscalNumberTextField.setEditable(false);
       providerFiscalNumberTextField.setBounds(490, 200, 170, 25);
       purchaseInvoicePanel.add(providerFiscalNumberTextField);

       //FILA 4
       providerCityLabel = new JLabel("Ciudad:");
       providerCityLabel.setBounds(30, 230, 120, 25);
       purchaseInvoicePanel.add(providerCityLabel);
       
       providerCityTextField = new JTextField("");
       providerCityTextField.setEditable(false);
       providerCityTextField.setBounds(160, 230, 170, 25);
       purchaseInvoicePanel.add(providerCityTextField);
       
        ImageIcon addFurnituresIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addFurnituresButton = new JButton(" Agregar Mobiliario");
        addFurnituresButton.setIcon(addFurnituresIconImage);
        addFurnituresButton.setBounds(30, 290, 200, 32);
        addFurnituresButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addFurnituresButtonAction();
            }
        });
        purchaseInvoicePanel.add(addFurnituresButton);
        
        ImageIcon addItemEmptyIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addItemEmptyButton = new JButton(" Agregar Item");
        addItemEmptyButton.setIcon(addItemEmptyIconImage);
        addItemEmptyButton.setBounds(270, 290, 170, 32);
        addItemEmptyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //addFurnituresButtonAction();
                addInvoiceItem();
            }
        });
        purchaseInvoicePanel.add(addItemEmptyButton);
        
        //TABLA DE DETALLES
        invoiceDetailDefaultTableModel = new invoiceDetailDefaultTableModel();
        invoiceDetailTable = new JTable(invoiceDetailDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda inactiva
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        invoiceDetailDefaultTableModel.addColumn("Id");
        invoiceDetailDefaultTableModel.addColumn("Código");
        invoiceDetailDefaultTableModel.addColumn("Descripción");
        invoiceDetailDefaultTableModel.addColumn("Tasa de Impuesto");
        invoiceDetailDefaultTableModel.addColumn("Disponibilidad");
        invoiceDetailDefaultTableModel.addColumn("Precio Unitario");
        invoiceDetailDefaultTableModel.addColumn("IVA");
        invoiceDetailDefaultTableModel.addColumn("Cantidad");
        invoiceDetailDefaultTableModel.addColumn("Subtotal");
        invoiceDetailDefaultTableModel.addColumn("Exenta");
        invoiceDetailDefaultTableModel.addColumn("10%");
        invoiceDetailDefaultTableModel.addColumn("5%");
        invoiceDetailDefaultTableModel.addColumn("");
       
        invoiceDetailTable.setRowHeight(22);
        
        TableCellRenderer rendererFromHeader = invoiceDetailTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setHeaderRenderer(new PurchaseInvoiceCreate.SimpleHeaderRenderer());
        
        //ID
        invoiceDetailTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        
        //TAX RATE
        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellRenderer(rightRenderer);
        
        ComboBoxItem[] taxRateComboBox = FurnitureController.getFurnitureTaxRatesForComboBox(false); 
        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellEditor(new TaxRateCellEditor(taxRateComboBox));
        invoiceDetailTable.getColumnModel().getColumn(TAX_RATE_COLUMN).setCellRenderer(new TaxRateCellRenderer());
        
        //STOCK
        invoiceDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setCellRenderer(rightRenderer);
        invoiceDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setMaxWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setMinWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(STOCK_COLUMN).setPreferredWidth(0);
        
        //UNIT PRICE
        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE).setCellEditor(new UnitPriceCellEditor());
        invoiceDetailTable.getColumnModel().getColumn(UNIT_PRICE).setCellRenderer(rightRenderer);
        
        //FINE AMOUNT
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT).setCellRenderer(rightRenderer);
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT).setMaxWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT).setMinWidth(0);
        invoiceDetailTable.getColumnModel().getColumn(TAX_AMOUNT).setPreferredWidth(0);
        
        //QUANTITY
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellEditor(new QuantityCellEditor());
        invoiceDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(rightRenderer);
        
        //SUB TOTAL
        invoiceDetailTable.getColumnModel().getColumn(SUB_TOTAL_COLUMN).setCellRenderer(rightRenderer);
        
        //EXENTAS
        invoiceDetailTable.getColumnModel().getColumn(EXEMPT_AMOUNT).setCellRenderer(rightRenderer);
        
        //IVA 10
        invoiceDetailTable.getColumnModel().getColumn(TAXT_RATE_10).setCellRenderer(rightRenderer);
        
        //IVA 5
        invoiceDetailTable.getColumnModel().getColumn(TAXT_RATE_5).setCellRenderer(rightRenderer);
        
        invoiceDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMaxWidth(80);
        invoiceDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setMinWidth(80);
        invoiceDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setResizable(false);
        invoiceDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        invoiceDetailTable.getColumnModel().getColumn(DELETE_BUTTON_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        
        invoiceDetailTableJScrollPane = new JScrollPane();
        invoiceDetailTableJScrollPane.setBounds(30, 340, 1350, 300);
        invoiceDetailTableJScrollPane.setViewportView(invoiceDetailTable);
        
         //Sub Totales
        subTotalLabel = new JLabel("Sub Totales: ");
        subTotalLabel.setBounds(30, 660, 80, 25);
        purchaseInvoicePanel.add(subTotalLabel);
        
        total5Label = new JLabel("5%: ");
        total5Label.setBounds(160, 660, 30, 25);
        purchaseInvoicePanel.add(total5Label);
        
        total5TextField = new JTextField("0");
        total5TextField.setBounds(200, 660, 170, 25);
        total5TextField.setEditable(false);
        total5TextField.setHorizontalAlignment(JLabel.RIGHT);
        purchaseInvoicePanel.add(total5TextField);
        
        total10Label = new JLabel("10%: ");
        total10Label.setBounds(390, 660, 35, 25);
        purchaseInvoicePanel.add(total10Label);
        
        total10TextField = new JTextField("0");
        total10TextField.setEditable(false);
        total10TextField.setHorizontalAlignment(JLabel.RIGHT);
        total10TextField.setBounds(440, 660, 170, 25);
        purchaseInvoicePanel.add(total10TextField);
        
        exemptTotalLabel = new JLabel("Exentas: ");
        exemptTotalLabel.setBounds(640, 660, 100, 25);
        purchaseInvoicePanel.add(exemptTotalLabel);
        
        exemptTotalTextField = new JTextField("0");
        exemptTotalTextField.setEditable(false);
        exemptTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        exemptTotalTextField.setBounds(730, 660, 170, 25);
        purchaseInvoicePanel.add(exemptTotalTextField);
        
        //Liquidacion del IVA
        taxSettlementLabel = new JLabel("Liquidacion del IVA: ");
        taxSettlementLabel.setBounds(30, 690, 130, 25);
        purchaseInvoicePanel.add(taxSettlementLabel);
        
        tax5Label = new JLabel("5%: ");
        tax5Label.setBounds(160, 690, 30, 25);
        purchaseInvoicePanel.add(tax5Label);
        
        tax5TextField = new JTextField("0");
        tax5TextField.setBounds(200, 690, 170, 25);
        tax5TextField.setEditable(false);
        tax5TextField.setHorizontalAlignment(JLabel.RIGHT);
        purchaseInvoicePanel.add(tax5TextField);
        
        tax10Label = new JLabel("10%: ");
        tax10Label.setBounds(390, 690, 35, 25);
        purchaseInvoicePanel.add(tax10Label);
        
        tax10TextField = new JTextField("0");
        tax10TextField.setEditable(false);
        tax10TextField.setHorizontalAlignment(JLabel.RIGHT);
        tax10TextField.setBounds(440, 690, 170, 25);
        purchaseInvoicePanel.add(tax10TextField);
        
        totalTaxLabel = new JLabel("Total IVA: ");
        totalTaxLabel.setBounds(640, 690, 100, 25);
        purchaseInvoicePanel.add(totalTaxLabel);
        
        totalTaxTextField = new JTextField("0");
        totalTaxTextField.setEditable(false);
        totalTaxTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTaxTextField.setBounds(730, 690, 170, 25);
        purchaseInvoicePanel.add(totalTaxTextField);
        
        //Total
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(30, 720, 80, 25);
        purchaseInvoicePanel.add(totalLabel);
    
        totalTextField = new JTextField("0");
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(200, 720, 170, 25);
        purchaseInvoicePanel.add(totalTextField);
        
        ImageIcon saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
        saveButton.setBounds(900, 770, 170, 32); 
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                savePurchaseInvoiceButtonAction();
            }
        });
        purchaseInvoicePanel.add(saveButton);
        
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
        cancelInvoice.setBounds(1100, 770, 170, 32);
        purchaseInvoicePanel.add(cancelInvoice);
        
        add(invoiceDetailTableJScrollPane);
        add(purchaseInvoicePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Registrar Factura de Compra");
        setBounds(150,50,1420,860);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
   }

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        purchaseInvoiceController.setPurchaseInvoiceCreateClosed();
    }
   
   
   public void selectProviderButtonAction(){
        providerSearch = searchController.getProviderSearch();
        providerSearch.setVisible(true);
        showSearchDialog(providerSearch);
        inactivateElements();
        providerSearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) { 
                activateElements();
                if(!searchController.getProviderSelectedCode().equals("")){
                    updateProviderSelected(searchController.getProviderSelectedCode());
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
   
   public void showSearchDialog(Object dialogView){
        pane = getDesktopPane();
        pane.add((JInternalFrame) dialogView, new Integer(2001));
        pane.setVisible(true);
    }
   
   public void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        selectProviderButton.setEnabled(false);
        //addFurnituresButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelInvoice.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
   
   public void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        selectProviderButton.setEnabled(true);
        //addFurnituresButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelInvoice.setEnabled(true);
    }
   
   private void updateProviderSelected(String providerCode){
        HashMap providerMap = ProviderController.getProviderByCode(providerCode);
        providerCodeTextField.setText((String)providerMap.get("code"));
        providerAddressTextField.setText((String)providerMap.get("address"));
        providerNameTextField.setText((String)providerMap.get("name"));
        providerTelephoneTextField.setText((String)providerMap.get("telephone"));
        providerTradenameTextField.setText((String)providerMap.get("tradename"));
        providerCityTextField.setText((String)providerMap.get("city"));
        
        String fiscalNumber; 
        
        if(((String)providerMap.get("fiscalNumber")).contains("-")){
            fiscalNumber = amountFormat.format(Double.valueOf(((String)providerMap.get("fiscalNumber")).split("-")[0]));
            providerFiscalNumberTextField.setText(fiscalNumber+"-"+((String)providerMap.get("fiscalNumber")).split("-")[1]);
        }else{
            fiscalNumber = amountFormat.format(Double.valueOf(((String)providerMap.get("fiscalNumber"))));
            providerFiscalNumberTextField.setText(fiscalNumber);
        }
    }
   
   public void addFurnituresButtonAction(){
        furnitureSearch = searchController.getFurnitureSearch(furnitureCodesAdded, new Date(), false);
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
   
   public void addFuritureSelectedToDetailTable(ArrayList furnitureCodes){
        
        HashMap furnitureMap;
        ArrayList furnitureList = FurnitureController.getFurnitureListByCodeList(furnitureCodes);
        
        Object[] row = new Object[invoiceDetailTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            furnitureCodesAdded.add(furnitureMap.get("code"));
            row[ID_COLUMN] = furnitureMap.get("id");
            row[CODE_COLUMN] = furnitureMap.get("code");
            row[DESCRIPTION_COLUMN] = furnitureMap.get("description");
            
            ComboBoxItem[] taxRateComboBox = FurnitureController.getFurnitureTaxRatesForComboBox(false);
            ComboBoxItem taxRateComboBoxItem = null;
            for (ComboBoxItem taxRateComboBox1 : taxRateComboBox) {
                taxRateComboBoxItem = taxRateComboBox1;
                if(taxRateComboBoxItem.getKey().equals(furnitureMap.get("taxRate").toString())){
                    break;
                }
            }

            row[TAX_RATE_COLUMN] = taxRateComboBoxItem;
            row[STOCK_COLUMN] = 0;
            row[UNIT_PRICE] = amountFormat.format((Double)furnitureMap.get("unitPrice"));
            row[TAX_AMOUNT] = amountFormat.format((Double)furnitureMap.get("fineAmountPerUnit"));
            row[QUANTITY_COLUMN] = 0;
            row[SUB_TOTAL_COLUMN] = 0;
            row[EXEMPT_AMOUNT] = 0;
            row[TAXT_RATE_10] = 0;
            row[TAXT_RATE_5] = 0;
                    

            invoiceDetailDefaultTableModel.addRow(row);
        }
    }
   
   public void addInvoiceItem(){
        Object[] row = new Object[invoiceDetailTable.getColumnCount()];
        
        ComboBoxItem[] taxRateComboBox;
        ComboBoxItem taxRateComboBoxItem = null;
       
        taxRateComboBox = FurnitureController.getFurnitureTaxRatesForComboBox(false);
        taxRateComboBoxItem = null;
        for (ComboBoxItem taxRateComboBox1 : taxRateComboBox) {
            taxRateComboBoxItem = taxRateComboBox1;
            if(taxRateComboBoxItem.getKey().equals("10")){
                break;
            }
        }
        
        row[TAX_RATE_COLUMN] = taxRateComboBoxItem;
        row[STOCK_COLUMN] = 0;
        row[UNIT_PRICE] = 0;
        row[TAX_AMOUNT] = 0;
        row[EXEMPT_AMOUNT] = 0;
        row[QUANTITY_COLUMN] = 0;
        row[SUB_TOTAL_COLUMN] = 0;
        row[TAXT_RATE_10] = 0;
        row[TAXT_RATE_5] = 0;
        
        invoiceDetailDefaultTableModel.addRow(row);
   }
   
   public void updateValues(int row, String taxRateString, double unitPrice, double quantity){
        try {
            double taxRate = 0;
            if(taxRateString.equals("")){
                taxRate = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString()).doubleValue();
            }else{
                taxRate = Double.valueOf(taxRateString);
            }
                
            if(unitPrice == 0){
                unitPrice = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, UNIT_PRICE).toString()).doubleValue();
            }
            
            if(quantity == 0){
                quantity = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, QUANTITY_COLUMN).toString()).doubleValue();
            }
            
            //SubTotal
            double oldSubTotal = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, SUB_TOTAL_COLUMN).toString()).doubleValue();
            
            double subTotal = quantity * unitPrice;
            
            invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, SUB_TOTAL_COLUMN);
            
            //TOTAL
            double total = amountFormat.parse(totalTextField.getText()).doubleValue();
            total = (total - oldSubTotal) + subTotal;
            totalTextField.setText(amountFormat.format(total));
            
            //Item tax
            String furnitureTaxRateString = invoiceDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
            switch (furnitureTaxRateString) {
                case "0":
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, EXEMPT_AMOUNT);
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), row, TAXT_RATE_5);
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), row, TAXT_RATE_10);
                    break;
                case "5":
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), row, EXEMPT_AMOUNT);
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, TAXT_RATE_5);
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), row, TAXT_RATE_10);
                    break;
                case "10":
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), row, EXEMPT_AMOUNT);
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), row, TAXT_RATE_5);
                    invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(subTotal), row, TAXT_RATE_10);
                    break;
            }
            
            //CERAMOS subTotal para reutilizar, solo para dejar constancia y no confundir
            subTotal = 0; 
            
            Vector dataVector;
            double total5 = 0 , total10 = 0, exemptTotal = 0, totalTax5 = 0, totalTax10 = 0, totalTax = 0;
            
            for (int i = 0; i < invoiceDetailTable.getRowCount(); i++){
                dataVector = (Vector) invoiceDetailDefaultTableModel.getDataVector().get(i);
                dataVector.get(TAX_RATE_COLUMN);
                subTotal = amountFormat.parse(dataVector.get(SUB_TOTAL_COLUMN).toString()).doubleValue();
                switch (dataVector.get(TAX_RATE_COLUMN).toString()) {
                    case "0":
                        exemptTotal = exemptTotal + subTotal;
                        invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new Double("0")), i, TAX_AMOUNT);
                        break;
                    case "5":
                        total5 = total5 + subTotal;
                        totalTax5 = totalTax5 + (new BigDecimal(subTotal / TAX5).setScale(2, RoundingMode.CEILING).doubleValue());
                        totalTax = totalTax + (new BigDecimal(subTotal / TAX5).setScale(2, RoundingMode.CEILING).doubleValue());
                        invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new BigDecimal(subTotal / TAX5).setScale(2, RoundingMode.CEILING).doubleValue()), i, TAX_AMOUNT);
                        break;
                    case "10":
                        total10 = total10 + subTotal;
                        totalTax10 = totalTax10 + (new BigDecimal(subTotal / TAX10).setScale(2, RoundingMode.CEILING).doubleValue());
                        totalTax = totalTax + (new BigDecimal(subTotal / TAX10).setScale(2, RoundingMode.CEILING).doubleValue());
                        invoiceDetailDefaultTableModel.setValueAt(amountFormat.format(new BigDecimal(subTotal / TAX10).setScale(2, RoundingMode.CEILING).doubleValue()), i, TAX_AMOUNT);
                        break;
                }
            }
//            //SUBTOTALES
            total5TextField.setText(amountFormat.format(total5));
            total10TextField.setText(amountFormat.format(total10));
            exemptTotalTextField.setText(amountFormat.format(exemptTotal));
//            
//            //LIQUIDACION IVA
            tax5TextField.setText(amountFormat.format(totalTax5));
            tax10TextField.setText(amountFormat.format(totalTax10));
            //System.out.println("totalTax: "+totalTax);
            totalTaxTextField.setText(amountFormat.format(totalTax));
            
        } catch (ParseException ex) {
            Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
   private class invoiceDetailDefaultTableModel extends DefaultTableModel{
        
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
                                case QUANTITY_COLUMN:
                                    return true;
                                case DELETE_BUTTON_COLUMN:
                                    return true;
                                case DESCRIPTION_COLUMN:
                                    return invoiceDetailTable.getModel().getValueAt(row, CODE_COLUMN) == null;
                                case TAX_RATE_COLUMN:
                                    return invoiceDetailTable.getModel().getValueAt(row, CODE_COLUMN) == null;
                                case UNIT_PRICE:
                                    return invoiceDetailTable.getModel().getValueAt(row, CODE_COLUMN) == null;
                                default:
                                    return false;
                            }
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
          String newQuantityString = ((JTextField) component).getText();
            try {
                if(newQuantityString.equals("")){
                    newQuantityString = "0";
                }
                int newQuantity = amountFormat.parse(newQuantityString.trim()).intValue();
                //updateSubTotal(newQuantity, row, column, 0);
                updateValues(row, "", 0, newQuantity);
                ((JTextField) component).setText(amountFormat.format(newQuantity));
            } catch (ParseException ex) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
          return ((JTextField) component).getText();
        }

    }
   
   class UnitPriceCellEditor extends AbstractCellEditor implements TableCellEditor {
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
          String newUnitPriceString = ((JTextField) component).getText();
            try {
                if(newUnitPriceString.equals("")){
                    newUnitPriceString = "0";
                }
                double newUnitPrice = amountFormat.parse(newUnitPriceString.trim()).doubleValue();
                //updateSubTotal(0, row, column, newUnitPrice);
                updateValues(row, "", newUnitPrice, 0);
                ((JTextField) component).setText(amountFormat.format(newUnitPrice));
            } catch (ParseException ex) {
                Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
          return ((JTextField) component).getText();
        }
   }
   
   public void savePurchaseInvoiceButtonAction(){
       JOptionPane optionPane;
       JDialog dialog;
       ArrayList invoiceDetailMapList = new ArrayList();
       HashMap invoiceMap = new HashMap();
       if(datePicker.getModel().getValue() == null){
            optionPane = new JOptionPane("Favor ingresar la fecha de la factura", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(invoiceBranchTextField.getText().equals("") || invoicePrinterTextField.getText().equals("") || invoiceNumberTextField.getText().equals("")){
            optionPane = new JOptionPane("Favor ingrese la numeracion completa de la factura", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(invoiceFiscalStampNumberTextField.getText().equals("")){
            optionPane = new JOptionPane("Favor ingrese el timbrado", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(providerCodeTextField.getText().equals("")){
            optionPane = new JOptionPane("No ha sido seleccionado ningun proveedor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(invoiceDetailTable.getRowCount()==0){
            optionPane = new JOptionPane("No ha sido agregado ningun detalle (item)", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(!allQuantityAdded()){
            optionPane = new JOptionPane("Existen detalles con cantidad 0 (Cero). Favor ingrese correctamente todas las cantidades.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            HashMap invoiceDetailMap = new HashMap();

            Date selectedDate = (Date) datePicker.getModel().getValue();
            String invoiceFiscalStampNumber = invoiceFiscalStampNumberTextField.getText();
            String invoiceBranch = invoiceBranchTextField.getText();
            String invoicePrinter = invoicePrinterTextField.getText();
            String invoiceNumber = invoiceNumberTextField.getText();

            String providerCode = providerCodeTextField.getText();
            String providerName = providerNameTextField.getText();
            String providerTradename = providerTradenameTextField.getText();
            String providerFiscalNumber = providerFiscalNumberTextField.getText().replaceAll("\\.", "");
           
            String providerAddress = providerAddressTextField.getText();

            double netTotal = 0;
            double taxRateTotal = 0;
            try {
                taxRateTotal = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
                netTotal = amountFormat.parse(totalTextField.getText()).doubleValue();
            } catch (ParseException ex) {
                Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            invoiceMap.put("invoicingDate", selectedDate);
            invoiceMap.put("invoiceFiscalStampNumber", invoiceFiscalStampNumber);
            invoiceMap.put("invoiceBranch", invoiceBranch);
            invoiceMap.put("invoicePrinter", invoicePrinter);
            invoiceMap.put("invoiceNumber", invoiceNumber);
            invoiceMap.put("providerCode", providerCode);
            invoiceMap.put("providerName", providerName);
            invoiceMap.put("providerTradename", providerTradename);
            invoiceMap.put("providerFiscalNumber", providerFiscalNumber);
            invoiceMap.put("providerAddress", providerAddress);
            invoiceMap.put("netTotal", netTotal);
            invoiceMap.put("balance", netTotal);
            invoiceMap.put("taxRateTotal", taxRateTotal);
            
            double exemptTotal = 0;
            double totalTax5 = 0;
            double totalTax10 = 0;
            double totalTax = 0;
            double totalTaxable5 = 0;
            double totalTaxable10 = 0;
            double totalTaxable = 0;
           
            for(int i = 0 ; i < invoiceDetailTable.getRowCount(); i++){
  
                invoiceDetailMap = new HashMap();
                int furnitureId = 0;
                if(invoiceDetailDefaultTableModel.getValueAt(i, 0) != null){
                    furnitureId = (int) invoiceDetailDefaultTableModel.getValueAt(i, ID_COLUMN);
                }      
                String furnitureCode = (String) invoiceDetailDefaultTableModel.getValueAt(i, CODE_COLUMN);
                String furnitureDescription = (String) invoiceDetailDefaultTableModel.getValueAt(i, DESCRIPTION_COLUMN);

                ComboBoxItem taxRateComboBox = (ComboBoxItem) invoiceDetailDefaultTableModel.getValueAt(i, TAX_RATE_COLUMN);
                String taxRate = taxRateComboBox.getValue();
                System.out.println("taxRate: "+taxRate);
                double unitPrice = 0;
                try {
                    unitPrice = amountFormat.parse((invoiceDetailDefaultTableModel.getValueAt(i, UNIT_PRICE).toString())).doubleValue();
                } catch (ParseException ex) {
                    Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
                }
                String quantity =  invoiceDetailDefaultTableModel.getValueAt(i, QUANTITY_COLUMN).toString();
                double subTotal = 0;
                try {
                    subTotal = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(i, SUB_TOTAL_COLUMN).toString()).doubleValue();
                } catch (ParseException ex) {
                    Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
                }
                double taxRate5 = 0;
                double taxRate10 = 0;
                double taxableRate5 = 0;
                double taxableRate10 = 0;
                double taxAmount = 0;
                double taxableAmount = 0;
                
                if(taxRate.equals("0")){
                    try {
                        exemptTotal = exemptTotal + amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(i, SUB_TOTAL_COLUMN).toString()).doubleValue();
                    } catch (ParseException ex) {
                        Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(taxRate.equals("5")){
                    try {
                        taxRate5 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(i, TAX_AMOUNT).toString()).doubleValue();
                        totalTax5 = totalTax5 + taxRate5;
                        taxableRate5 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(i, SUB_TOTAL_COLUMN).toString()).doubleValue();
                        totalTaxable5 = totalTaxable5 + taxableRate5;
                        taxAmount = taxRate5;
                        taxableAmount = taxableRate5;
                    } catch (ParseException ex) {
                        Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else if(taxRate.equals("10")){
                    try {
                        taxRate10 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(i, TAX_AMOUNT).toString()).doubleValue();
                        totalTax10 = totalTax10 + taxRate10;
                        taxableRate10 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(i, SUB_TOTAL_COLUMN).toString()).doubleValue();
                        totalTaxable10 = totalTaxable10 + taxableRate10;
                        taxAmount = taxRate10;
                        taxableAmount = taxableRate10;
                    }catch (ParseException ex) {
                        Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                invoiceDetailMap.put("furnitureId", furnitureId);
                invoiceDetailMap.put("furnitureCode", furnitureCode);
                invoiceDetailMap.put("furnitureDescription", furnitureDescription);
                invoiceDetailMap.put("taxRate", taxRate);
                invoiceDetailMap.put("unitPrice", unitPrice);
                invoiceDetailMap.put("quantity", quantity);
                invoiceDetailMap.put("taxRate5", taxRate5);
                invoiceDetailMap.put("taxRate10", taxRate10);
                invoiceDetailMap.put("taxableRate5", taxableRate5);
                invoiceDetailMap.put("taxableRate10", taxableRate10);
                invoiceDetailMap.put("taxAmount", taxAmount);
                invoiceDetailMap.put("subTotal", subTotal);
                invoiceDetailMap.put("taxableAmount", taxableAmount);

                invoiceDetailMapList.add(invoiceDetailMap);
            }
            totalTaxable = totalTaxable5 + totalTaxable10;
            invoiceMap.put("exemptTotal", exemptTotal);
            invoiceMap.put("totalTax5", totalTax5);
            invoiceMap.put("totalTax10", totalTax10);
            invoiceMap.put("totalTaxable10", totalTaxable10);
            invoiceMap.put("totalTaxable5", totalTaxable5);
            invoiceMap.put("totalTaxable", totalTaxable);
        }
        HashMap mapReturn = purchaseInvoiceController.savePurchaseInvoice(invoiceMap, invoiceDetailMapList);
        
        if(((Integer)mapReturn.get("status"))==PurchaseInvoiceController.SUCCESFULLY_SAVED){
                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
        }else if((Integer)mapReturn.get("status") == PurchaseInvoiceController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
        
   }
   
   public class TaxRateCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ComboBoxItem) {
                ComboBoxItem paymentMethod = (ComboBoxItem) value;
                setText(paymentMethod.getValue());
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getSelectionForeground());
            }

            return this;
        }
    }
      
    public class TaxRateCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
        private ComboBoxItem item;
        private final ComboBoxItem[] items;
        private int row;

        public TaxRateCellEditor(ComboBoxItem[] items) {
            this.items = items;
        }

        @Override
        public Object getCellEditorValue() {
            return this.item;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.row = row;
            if (value instanceof ComboBoxItem) {
                this.item = (ComboBoxItem) value;
            }

            JComboBox<ComboBoxItem> taxRateComboBox = new JComboBox<>();

            for (ComboBoxItem taxRate : items) {
                taxRateComboBox.addItem(taxRate);
            }

            taxRateComboBox.setSelectedItem(item);
            taxRateComboBox.addActionListener(this);

            if (isSelected) {
                taxRateComboBox.setBackground(table.getSelectionBackground());
            } else {
                taxRateComboBox.setBackground(table.getSelectionForeground());
            }
            
            return taxRateComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            JComboBox<ComboBoxItem> taxRateItem = (JComboBox<ComboBoxItem>) event.getSource();
            this.item = (ComboBoxItem) taxRateItem.getSelectedItem();
            fireEditingStopped();
            updateValues(row, item.getKey(), 0, 0);
        }
    }
    
    private boolean allQuantityAdded(){
        Vector dataVector;
        try {
            for (int row = 0; row < invoiceDetailTable.getRowCount(); row++){
               dataVector = (Vector) invoiceDetailDefaultTableModel.getDataVector().get(row);
                   if(amountFormat.parse(dataVector.get(QUANTITY_COLUMN).toString()).intValue()==0 || amountFormat.parse(dataVector.get(UNIT_PRICE).toString()).intValue()==0){
                       return false;
                   }
           }
        } catch (ParseException ex) {
            Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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
                Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
      }
    
    /*private void removeRow(int row) throws ParseException{        
        double subTotal = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, SUB_TOTAL_COLUMN).toString()).doubleValue();
        
        //Item tax
        String furnitureTaxRateString = invoiceDetailDefaultTableModel.getValueAt(row, TAX_RATE_COLUMN).toString();
        double itemTaxAmount =0;
       
        if(furnitureTaxRateString.equals("5")){
            itemTaxAmount = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAXT_RATE_5).toString()).doubleValue(); 
        }else if(furnitureTaxRateString.equals("10")){
            itemTaxAmount = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAXT_RATE_10).toString()).doubleValue(); 
        }        

        //TOTAL
        double total = amountFormat.parse(totalTextField.getText()).doubleValue();
        total = total - subTotal;
        totalTextField.setText(amountFormat.format(total));
        
        //TOTAL IVA
        double totalTax = amountFormat.parse(totalTaxTextField.getText()).doubleValue();
        totalTax = totalTax - itemTaxAmount;
        totalTaxTextField.setText(amountFormat.format(totalTax));
        
        if(invoiceDetailDefaultTableModel.getValueAt(row, 1) != null){
            String furnitureCode = invoiceDetailDefaultTableModel.getValueAt(row, CODE_COLUMN).toString();
            furnitureCodesAdded.remove(furnitureCode);
        }
        invoiceDetailDefaultTableModel.removeRow(row);
    }*/
    
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
                    subTotal5 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAXT_RATE_5).toString()).doubleValue();
                    
                    //SUBTOTAL
                    currentSubTotalAmount = amountFormat.parse(total5TextField.getText()).doubleValue();
                    currentSubTotalAmount = currentSubTotalAmount - subTotal5;
                    total5TextField.setText(amountFormat.format(currentSubTotalAmount));
                            
                    //TAX
                    itemTaxAmount = new BigDecimal(subTotal5/ TAX5).setScale(2, RoundingMode.CEILING).doubleValue();
                    taxAmount = amountFormat.parse(tax5TextField.getText()).doubleValue();
                    taxAmount = taxAmount - itemTaxAmount;
                    tax5TextField.setText(amountFormat.format(taxAmount));
                    break;
                case "10":
                    subTotal10 = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, TAXT_RATE_10).toString()).doubleValue();
                    
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
                    exemptTotal = amountFormat.parse(invoiceDetailDefaultTableModel.getValueAt(row, EXEMPT_AMOUNT).toString()).doubleValue();
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

//            String eventDetailIdString = invoiceDetailDefaultTableModel.getValueAt(row, EVENT_DETAIL_ID_COLUMN).toString();
            //eventDetailsAdded.remove(eventDetailIdString);
            invoiceDetailDefaultTableModel.removeRow(row);
            
            //updateDetailTableRowItem();
        }catch (Throwable th) {
            Logger.getLogger(PurchaseInvoiceCreate.class.getName()).log(Level.SEVERE, null, th);
        }
    }
}
