/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.receipt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.event.EventController;
import rentfur.subject.SubjectController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DateLabelFormatter;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import rentfur.book.BookController;
import rentfur.event.EventCreate;
import rentfur.report.ReportController;
import rentfur.util.NumericTextField;

/**
 *
 * @author FDuarte
 */
public class ReceiptCreate extends JInternalFrame{
    private final ReceiptController receiptController;
    private final JPanel receiptCreatePanel;
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
    private final JLabel receiptDateLabel;
    private final JLabel receiptNumberLabel;
    private final JLabel totalLabel;
    private final JLabel balanceTotalLabel;
    private final JLabel newBalanceTotalLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final JTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JDatePickerImpl datePicker;
    private final JTextField totalTextField;
    private final JTextField balanceTotalTextField;
    private final JTextField newBalanceTotalTextField;
    private final JTextField branchTextField;
    private final JTextField printerTextField;
    private final JTextField numberTextField;
    private final JButton addPaymentMethod;
    private final JButton saveReceipt;
    private final JButton cancelReceipt;
    private final JTable receiptDetailTable;
    private final DefaultTableModel receiptDetailDefaultTableModel;
    private final JScrollPane receiptDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final int PAYMENT_METHOD_COLUMN = 0;
    private final int DOC_NUMBER_COLUMN = 1;
    private final int DOC_EMITION_DATE_COLUMN = 2;
    private final int DOC_PAYMENT_DATE_COLUMN = 3;
    private final int DOC_DUE_DATE_COLUMN = 4;
    private final int BANK_COLUMN = 5;
    private final int AMOUNT_COLUMN = 6;
    private final int DELETE_COLUMN = 7;
    
    private final HashMap eventMap;
    private final HashMap subjectMap;
    private final HashMap receiptNumMap;
    
    public ReceiptCreate(ReceiptController receiptController, int eventId){
        this.receiptController = receiptController;
        
        receiptCreatePanel = new JPanel();
        receiptCreatePanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        receiptNumMap = BookController.getReceiptNum();
        
        titleLabel = new JLabel("<HTML><U>Recibo</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        receiptCreatePanel.add(titleLabel);
        
        receiptDateLabel = new JLabel("Fecha:");
        receiptDateLabel.setBounds(30, 60, 130, 25);
        receiptCreatePanel.add(receiptDateLabel);
        
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
                if(receiptDetailDefaultTableModel.getRowCount()>0){
                    //showDayChangeWarning();
                }else{
                    //updateCurrentDeliveryDateSelected();
                }
            }
        });
        datePicker.getComponent(1).setEnabled(false);
        datePicker.setBounds(160, 60, 170, 25);
        receiptCreatePanel.add(datePicker);
        
        receiptNumberLabel = new JLabel("Numero:");
        receiptNumberLabel.setBounds(370, 60, 130, 25);
        receiptCreatePanel.add(receiptNumberLabel);
        
        branchTextField = new JTextField(receiptNumMap.get("branch").toString());
        branchTextField.setEditable(false);
        branchTextField.setHorizontalAlignment(JLabel.CENTER);
        branchTextField.setBounds(470, 60, 40, 25);
        receiptCreatePanel.add(branchTextField);
        
        JLabel firstSeparator = new JLabel("-");
        firstSeparator.setBounds(517, 60, 40, 25);
        receiptCreatePanel.add(firstSeparator);
        
        printerTextField = new JTextField(receiptNumMap.get("printer").toString());
        printerTextField.setEditable(false);
        printerTextField.setHorizontalAlignment(JLabel.CENTER);
        printerTextField.setBounds(530, 60, 40, 25);
        receiptCreatePanel.add(printerTextField);
        
        JLabel secondSeparator = new JLabel("-");
        secondSeparator.setBounds(577, 60, 40, 25);
        receiptCreatePanel.add(secondSeparator);
        
        String receiptNum = String.format ("%07d", (Integer) receiptNumMap.get("number"));
        numberTextField = new JTextField(receiptNum);
        numberTextField.setEditable(false);
        numberTextField.setHorizontalAlignment(JLabel.RIGHT);
        numberTextField.setBounds(590, 60, 60, 25);
        receiptCreatePanel.add(numberTextField);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        receiptCreatePanel.add(subjectLabel);
        
        totalSummaryLabel = new JLabel("<HTML><U>Totales</U></HTML>");
        totalSummaryLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        totalSummaryLabel.setBounds(900, 110, 200, 25);
        receiptCreatePanel.add(totalSummaryLabel);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        receiptCreatePanel.add(subjectCodeLabel);

        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        receiptCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        receiptCreatePanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        receiptCreatePanel.add(subjectAddressTextField);
        
        //Total SALDO
        balanceTotalLabel = new JLabel("Saldo: ");
        balanceTotalLabel.setBounds(900, 150, 80, 25);
        receiptCreatePanel.add(balanceTotalLabel);
        
        balanceTotalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("balance").toString())));
        balanceTotalTextField.setEditable(false);
        balanceTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        balanceTotalTextField.setBounds(1000, 150, 170, 25);
        receiptCreatePanel.add(balanceTotalTextField);
        
        //Total Pago
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(900, 180, 80, 25);
        receiptCreatePanel.add(totalLabel);
    
        totalTextField = new JTextField("0");
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(1000, 180, 170, 25);
        receiptCreatePanel.add(totalTextField);
        
        newBalanceTotalLabel = new JLabel("Saldo Final: ");
        newBalanceTotalLabel.setBounds(900, 210, 80, 25);
        receiptCreatePanel.add(newBalanceTotalLabel);
        
        newBalanceTotalTextField = new JTextField(amountFormat.format(Double.valueOf(eventMap.get("balance").toString())));
        newBalanceTotalTextField.setEditable(false);
        newBalanceTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        newBalanceTotalTextField.setBounds(1000, 210, 170, 25);
        receiptCreatePanel.add(newBalanceTotalTextField);
        
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        receiptCreatePanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        receiptCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        receiptCreatePanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        receiptCreatePanel.add(subjectTelephoneTextField);
        
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
        receiptCreatePanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        receiptCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        receiptCreatePanel.add(subjectFiscalNumberLabel);
        
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
        receiptCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        receiptCreatePanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        receiptCreatePanel.add(subjectCityTextField);
        
        ImageIcon addPaymentMethodImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        addPaymentMethod = new JButton();
        addPaymentMethod.setText(" Agregar Pago");
        addPaymentMethod.setIcon(addPaymentMethodImageIcon);
        addPaymentMethod.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addPaymentMethodButtonAction();
            }
        });
        addPaymentMethod.setBounds(30, 290, 170, 32);
        receiptCreatePanel.add(addPaymentMethod);
        
        ImageIcon saveReceiptImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveReceipt = new JButton();
        saveReceipt.setText(" Guardar Recibo");
        saveReceipt.setIcon(saveReceiptImageIcon);
        saveReceipt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveReceiptButtonAction();
            }
        });
        saveReceipt.setBounds(1000, 540, 170, 32);
        receiptCreatePanel.add(saveReceipt);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelReceipt = new JButton();
        cancelReceipt.setText(" Cancelar");
        cancelReceipt.setIcon(cancelImageIcon);
        cancelReceipt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelReceipt.setBounds(1000, 590, 170, 32);
        receiptCreatePanel.add(cancelReceipt);
        
        receiptDetailDefaultTableModel = new receiptDetailDefaultTableModel();
        receiptDetailTable = new JTable(receiptDetailDefaultTableModel);
        
        receiptDetailDefaultTableModel.addColumn("Medio de Pago");
        receiptDetailDefaultTableModel.addColumn("Numero de Documento");
        receiptDetailDefaultTableModel.addColumn("Fecha de Emision");
        receiptDetailDefaultTableModel.addColumn("Fecha de Cobro");
        receiptDetailDefaultTableModel.addColumn("Vencimiento");
        receiptDetailDefaultTableModel.addColumn("Banco");
        receiptDetailDefaultTableModel.addColumn("Monto");
        receiptDetailDefaultTableModel.addColumn("");
        
        receiptDetailTable.setRowHeight(22);
        
        //Alineacion a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        receiptDetailTableJScrollPane = new JScrollPane(receiptDetailTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        receiptDetailTableJScrollPane.setBounds(30, 350, 940, 290);
        add(receiptDetailTableJScrollPane);

        receiptDetailTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setCellEditor(new PaymentMethodCellEditor(ReceiptController.getPaymentMethodAvailablesForCreateReceipt(false)));
        receiptDetailTable.getColumnModel().getColumn(PAYMENT_METHOD_COLUMN).setCellRenderer(new PaymentMethodCellRenderer());
        
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setMinWidth(160);
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setMaxWidth(160);
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setPreferredWidth(160);
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setCellRenderer(rightRenderer);
        
        receiptDetailTable.getColumnModel().getColumn(DOC_EMITION_DATE_COLUMN).setCellEditor(new EmitionDatePickerCell());
        receiptDetailTable.getColumnModel().getColumn(DOC_EMITION_DATE_COLUMN).setCellRenderer(new EmitionDatePickerCellRenderer());
        
        receiptDetailTable.getColumnModel().getColumn(DOC_PAYMENT_DATE_COLUMN).setCellEditor(new PaymentDatePickerCell());
        receiptDetailTable.getColumnModel().getColumn(DOC_PAYMENT_DATE_COLUMN).setCellRenderer(new PaymentDatePickerCellRenderer());
        
        receiptDetailTable.getColumnModel().getColumn(DOC_DUE_DATE_COLUMN).setCellEditor(new DueDatePickerCell());
        receiptDetailTable.getColumnModel().getColumn(DOC_DUE_DATE_COLUMN).setCellRenderer(new DueDatePickerCellRenderer());
        
        receiptDetailTable.getColumnModel().getColumn(BANK_COLUMN).setCellEditor(new BankCellEditor(ReceiptController.getBanksForComboBox(false)));
        receiptDetailTable.getColumnModel().getColumn(BANK_COLUMN).setCellRenderer(new BankCellRenderer());
        
        //AMOUNT
        receiptDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellEditor(new AmountCellEditor());
        receiptDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellRenderer(new AmountCellRenderer());
        
        
        receiptDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setCellEditor(new DeleteButtonEditor(new JTextField()));
        receiptDetailTable.getColumnModel().getColumn(DELETE_COLUMN).setCellRenderer(new DeleteButtonRenderer());
        
        add(receiptCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Registrar Pago");
        setBounds(250,100,1250,700);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        receiptController.receiptCreateClosed();
    }
    
    private void saveReceiptButtonAction(){
        JOptionPane optionPane;
        JDialog dialog;
        if (receiptDetailTable.isEditing()){
            receiptDetailTable.getCellEditor().stopCellEditing();
        }
        
        if(receiptDetailTable.getRowCount()==0){
            optionPane = new JOptionPane("No ha sido agregado ningun detalle (item)", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(!allAmountAdded()){
            optionPane = new JOptionPane("Existen detalles con monto 0 (Cero). Favor ingrese correctamente todos los montos.", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else if(!allFieldAdded()){
            optionPane = new JOptionPane("Existen datos obligatorios de CHEQUES que no han sido ingresados, favor verificar e intentar nuevamente", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            Date receiptDate = (Date) datePicker.getModel().getValue();
            HashMap paymentMap;
            ArrayList paymentList = new ArrayList();
            Vector dataVector;
            double netTotal = 0;
            double balance = 0;
            String observation = "";
            int eventId = Integer.valueOf(eventMap.get("id").toString());
            try {
            
                for (int row = 0; row < receiptDetailTable.getRowCount(); row++){
                    dataVector = (Vector) receiptDetailDefaultTableModel.getDataVector().get(row);
                    paymentMap = new HashMap();
                    paymentMap.put("paymentMethod",((ComboBoxItem)dataVector.get(PAYMENT_METHOD_COLUMN)).getKey());
                    paymentMap.put("docNumber", dataVector.get(DOC_NUMBER_COLUMN));
                    if(dataVector.get(DOC_EMITION_DATE_COLUMN).equals("")){
                        paymentMap.put("emitionDate", "");
                    }else{
                        paymentMap.put("emitionDate", (Date)dataVector.get(DOC_EMITION_DATE_COLUMN));
                    }
                    
                    if(dataVector.get(DOC_DUE_DATE_COLUMN).equals("")){
                        paymentMap.put("dueDate", "");
                    }else{
                        paymentMap.put("dueDate", (Date)dataVector.get(DOC_DUE_DATE_COLUMN));
                    }
                    
                    if(dataVector.get(DOC_PAYMENT_DATE_COLUMN).equals("")){
                        paymentMap.put("paymentDate", "");
                    }else{
                        paymentMap.put("paymentDate", (Date)dataVector.get(DOC_PAYMENT_DATE_COLUMN));
                    }
                    
                    paymentMap.put("bank", ((ComboBoxItem)dataVector.get(PAYMENT_METHOD_COLUMN)).getKey());
                    paymentMap.put("amount", amountFormat.parse(dataVector.get(AMOUNT_COLUMN).toString()).doubleValue());
                    paymentList.add(paymentMap);
                }
                
                netTotal = amountFormat.parse(totalTextField.getText()).doubleValue();
                balance = amountFormat.parse(newBalanceTotalTextField.getText()).doubleValue();
            
            } catch (ParseException ex) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            HashMap returnMap = receiptController.createReceipt(subjectMap, paymentList, netTotal, balance, observation, receiptDate, eventId, receiptNumMap);
            
            if(((Integer)returnMap.get("status"))==ReceiptController.SUCCESFULLY_SAVED){
                ReportController.getReceiptReport((Integer)returnMap.get("receiptId"));
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
                doDefaultCloseAction();
            }else if((Integer)returnMap.get("status") == ReceiptController.ERROR_IN_SAVED){
                JOptionPane.showMessageDialog(null, returnMap.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private boolean allAmountAdded(){
        Vector dataVector;
        try {
            for (int row = 0; row < receiptDetailTable.getRowCount(); row++){
               dataVector = (Vector) receiptDetailDefaultTableModel.getDataVector().get(row);
                   if(amountFormat.parse(dataVector.get(AMOUNT_COLUMN).toString()).doubleValue()==0){
                       return false;
                   }
           }
        } catch (ParseException ex) {
            Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    private boolean allFieldAdded(){
        Vector dataVector;
        ComboBoxItem paymentMethod;
        for (int row = 0; row < receiptDetailTable.getRowCount(); row++){
            dataVector = (Vector) receiptDetailDefaultTableModel.getDataVector().get(row);
            paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
            if(Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK){
                if(dataVector.get(DOC_DUE_DATE_COLUMN).equals("") || dataVector.get(DOC_EMITION_DATE_COLUMN).equals("") || dataVector.get(DOC_NUMBER_COLUMN).equals("") || dataVector.get(BANK_COLUMN).equals("")){
                    return false;
                }
            }else if(Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK){
                if(dataVector.get(DOC_DUE_DATE_COLUMN).equals("") || dataVector.get(DOC_EMITION_DATE_COLUMN).equals("") || dataVector.get(DOC_NUMBER_COLUMN).equals("") || dataVector.get(BANK_COLUMN).equals("") || dataVector.get(DOC_PAYMENT_DATE_COLUMN).equals("")){
                    return false;
                }
            }
        }
        return true;
    }
    
    private void updateFields(ComboBoxItem item, int row){
        ComboBoxItem bankItem = new ComboBoxItem("", "");
        if(Integer.valueOf(item.getKey())==ReceiptController.PAY_CHECK){
            receiptDetailDefaultTableModel.setValueAt("", row, DOC_PAYMENT_DATE_COLUMN);
        }else if(Integer.valueOf(item.getKey())==ReceiptController.DATED_CHECK){
        }else if(Integer.valueOf(item.getKey())==ReceiptController.CASH){
            receiptDetailDefaultTableModel.setValueAt("", row, DOC_NUMBER_COLUMN);
            receiptDetailDefaultTableModel.setValueAt("", row, DOC_EMITION_DATE_COLUMN);
            receiptDetailDefaultTableModel.setValueAt("", row, DOC_DUE_DATE_COLUMN);
            receiptDetailDefaultTableModel.setValueAt("", row, DOC_PAYMENT_DATE_COLUMN);
            receiptDetailDefaultTableModel.setValueAt(bankItem, row, BANK_COLUMN);
        }
    }
    
    private void addPaymentMethodButtonAction(){
        Object[] row = new Object[8];
        ComboBoxItem item = new ComboBoxItem(String.valueOf(ReceiptController.PAY_CHECK), ReceiptController.getPaymentMethod(ReceiptController.PAY_CHECK));
        ComboBoxItem bankItem = new ComboBoxItem("", "");
        
        row[0] = item;
        row[1] = "";
        row[2] = "";
        row[3] = "";
        row[4] = "";
        row[5] = bankItem;
        row[6] = "0";
        row[7] = "";
        
        receiptDetailDefaultTableModel.addRow(row);
    }
    
    private void updateTotal(double newAmount, int row, int column){
        try {
            double oldAmount = amountFormat.parse(receiptDetailDefaultTableModel.getValueAt(row, AMOUNT_COLUMN).toString()).doubleValue();
            
            //TOTAL PAYED
            double totalPayed = amountFormat.parse(totalTextField.getText()).doubleValue();
            totalPayed = (totalPayed - oldAmount) + newAmount;
            totalTextField.setText(amountFormat.format(totalPayed));
            
            //FINAL BALANCE
            double newBalance = amountFormat.parse(newBalanceTotalTextField.getText()).doubleValue();
            newBalance = (newBalance + oldAmount) - newAmount;
            newBalanceTotalTextField.setText(amountFormat.format(newBalance));
        } catch (ParseException ex) {
            Logger.getLogger(ReceiptCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void removeRow(int row){
        try {
            double paymentAmount = amountFormat.parse(receiptDetailDefaultTableModel.getValueAt(row, AMOUNT_COLUMN).toString()).doubleValue();
            
            //TOTAL BALANCE
            double totalPayed = amountFormat.parse(totalTextField.getText()).doubleValue();
            totalPayed = totalPayed - paymentAmount;
            totalTextField.setText(amountFormat.format(totalPayed));
            
            //FINAL BALANCE
            double newBalance = amountFormat.parse(newBalanceTotalTextField.getText()).doubleValue();
            newBalance = newBalance + paymentAmount;
            newBalanceTotalTextField.setText(amountFormat.format(newBalance));
            
            receiptDetailDefaultTableModel.removeRow(row);
        } catch (ParseException ex) {
            Logger.getLogger(ReceiptCreate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private class receiptDetailDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            ComboBoxItem paymentMethod;
            switch(column){
                case PAYMENT_METHOD_COLUMN:
                    return true;
                case DOC_NUMBER_COLUMN:
                    paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
                    return Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK ||
                        Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK;
                case DOC_EMITION_DATE_COLUMN: 
                    paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
                    return Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK || 
                        Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK;
                case DOC_PAYMENT_DATE_COLUMN: 
                    paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
                    return Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK;
                case DOC_DUE_DATE_COLUMN: 
                    paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
                    return Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK ||
                        Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK;
                case BANK_COLUMN: 
                    paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
                    return Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK ||
                        Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK;
                case AMOUNT_COLUMN: 
                    return true;
                case DELETE_COLUMN:
                    return true;
                default:    return false;
            }
        }
           
    }
      
      public class PaymentMethodCellRenderer extends DefaultTableCellRenderer {
     
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
      
    public class PaymentMethodCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
        private ComboBoxItem item;
        private final ComboBoxItem[] items;
        private int row;

        public PaymentMethodCellEditor(ComboBoxItem[] items) {
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

            JComboBox<ComboBoxItem> paymentMethodComboBox = new JComboBox<>();

            for (ComboBoxItem paymentMethod : items) {
                paymentMethodComboBox.addItem(paymentMethod);
            }

            paymentMethodComboBox.setSelectedItem(item);
            paymentMethodComboBox.addActionListener(this);
            if (isSelected) {
                paymentMethodComboBox.setBackground(table.getSelectionBackground());
            } else {
                paymentMethodComboBox.setBackground(table.getSelectionForeground());
            }
            
            return paymentMethodComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            JComboBox<ComboBoxItem> paymentMethodItem = (JComboBox<ComboBoxItem>) event.getSource();
            this.item = (ComboBoxItem) paymentMethodItem.getSelectedItem();
            updateFields(this.item, row);
            fireEditingStopped();
        }
    }
    
    public class BankCellRenderer extends DefaultTableCellRenderer {
     
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
      
    public class BankCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {
        private ComboBoxItem item;
        private final ComboBoxItem[] items;
        private int row;

        public BankCellEditor(ComboBoxItem[] items) {
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

            JComboBox<ComboBoxItem> paymentMethodComboBox = new JComboBox<>();

            for (ComboBoxItem paymentMethod : items) {
                paymentMethodComboBox.addItem(paymentMethod);
            }

            paymentMethodComboBox.setSelectedItem(item);
            paymentMethodComboBox.addActionListener(this);
            if (isSelected) {
                paymentMethodComboBox.setBackground(table.getSelectionBackground());
            } else {
                paymentMethodComboBox.setBackground(table.getSelectionForeground());
            }
            
            return paymentMethodComboBox;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            JComboBox<ComboBoxItem> paymentMethodItem = (JComboBox<ComboBoxItem>) event.getSource();
            this.item = (ComboBoxItem) paymentMethodItem.getSelectedItem();
            fireEditingStopped();
        }
    }
    
    private class EmitionDatePickerCellRenderer extends DefaultTableCellRenderer {
     
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            ComboBoxItem paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
            if(Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK ||
                    Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK){
                if (value instanceof Date) {
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    String dateString = format.format((Date) value);
                    this.setText(dateString);
                }
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                } else {
                    setBackground(table.getSelectionForeground());
                }
                return this;
            }else{
                this.setText("");
                return this;
            }
            
        }

    }
    
    private class EmitionDatePickerCell extends DatePickerCellEditor{

        public EmitionDatePickerCell() {
            this.clickCountToStart = 1;
        }

        @Override
        public Date getCellEditorValue() {
            return super.getCellEditorValue(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return super.getTableCellEditorComponent(table, value, isSelected, row, column); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing(); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
          //removeRow(row);
        }

    }
    
    private class DueDatePickerCellRenderer extends DefaultTableCellRenderer {
     
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            ComboBoxItem paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
            if(Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK ||
                    Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK){
                if (value instanceof Date) {
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    String dateString = format.format((Date) value);
                    this.setText(dateString);
                }
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                } else {
                    setBackground(table.getSelectionForeground());
                }
                return this;
            }else{
                this.setText("");
                return this;
            }
            
        }

    }
    
    private class DueDatePickerCell extends DatePickerCellEditor{

        public DueDatePickerCell() {
            this.clickCountToStart = 1;
        }

        @Override
        public Date getCellEditorValue() {
            return super.getCellEditorValue(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return super.getTableCellEditorComponent(table, value, isSelected, row, column); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing(); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
          //removeRow(row);
        }

    }
    
    private class PaymentDatePickerCellRenderer extends DefaultTableCellRenderer {
     
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            ComboBoxItem paymentMethod = (ComboBoxItem) receiptDetailDefaultTableModel.getValueAt(row, PAYMENT_METHOD_COLUMN);
            if(Integer.valueOf(paymentMethod.getKey())==ReceiptController.PAY_CHECK ||
                    Integer.valueOf(paymentMethod.getKey())==ReceiptController.DATED_CHECK){
                if (value instanceof Date) {
                    String pattern = "yyyy-MM-dd";
                    SimpleDateFormat format = new SimpleDateFormat(pattern);
                    String dateString = format.format((Date) value);
                    this.setText(dateString);
                }
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                } else {
                    setBackground(table.getSelectionForeground());
                }
                return this;
            }else{
                this.setText("");
                return this;
            }
            
        }

    }
    
    private class PaymentDatePickerCell extends DatePickerCellEditor{

        public PaymentDatePickerCell() {
            this.clickCountToStart = 1;
        }

        @Override
        public Date getCellEditorValue() {
            return super.getCellEditorValue(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return super.getTableCellEditorComponent(table, value, isSelected, row, column); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing(); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
          //removeRow(row);
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
          removeRow(row);
        }
      }
    
    class AmountCellRenderer extends JTextField implements TableCellRenderer {

        public AmountCellRenderer() {
            setOpaque(true);
            setBackground(new Color(237, 247, 243));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Monto");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class AmountCellEditor extends AbstractCellEditor implements TableCellEditor {

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
                double oldAmount = amountFormat.parse((receiptDetailDefaultTableModel.getValueAt(row, AMOUNT_COLUMN).toString())).intValue();
                String newAmountString = ((JTextField) component).getText();
                if(newAmountString.equals("")){
                    newAmountString = "0";
                }
                double newAmount = amountFormat.parse(newAmountString).doubleValue();
                double balanceTotal = amountFormat.parse(balanceTotalTextField.getText()).doubleValue();
                double total = amountFormat.parse(totalTextField.getText()).doubleValue();
                if(balanceTotal < ((total-oldAmount)+newAmount)){
                    optionPane = new JOptionPane("Con el valor ingresado se superara el saldo del evento", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    dialog = optionPane.createDialog(null, "Atencion!");
                    dialog.setVisible(true);
                    ((JTextField) component).setText(amountFormat.format(oldAmount));
                }else{
                    ((JTextField) component).setText(amountFormat.format(newAmount));
                    updateTotal(newAmount, row, column);
                }
            } catch (HeadlessException | ParseException th) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, th);
            }
            return ((JTextField) component).getText();
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing(); //To change body of generated methods, choose Tools | Templates.
        } 

    }
}
