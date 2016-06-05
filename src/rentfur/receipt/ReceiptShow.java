package rentfur.receipt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
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
import javax.swing.BorderFactory;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
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
import rentfur.util.NumericTextField;

/**
 *
 * @author FDuarte
 */

public class ReceiptShow extends JInternalFrame{
    private final ReceiptController receiptController;
    private final JPanel receiptShowPanel;
    private final JLabel subjectCodeLabel;
    private final JLabel titleLabel;
    private final JLabel subjectLabel;
    private final JLabel subjectAddressLabel;
    private final JLabel subjectNameLabel;
    private final JLabel subjectTelephoneLabel;
    private final JLabel subjectTradenameLabel;
    private final JLabel subjectFiscalNumberLabel;
    private final JLabel subjectCityLabel;
    private final JLabel receiptDateLabel;
    private final JLabel receiptNumberLabel;
    private final JLabel cancelledLabel;
    private final JLabel cancelledDateLabel;
    private final JLabel cancelledReasonLabel;
    private final JLabel totalLabel;
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
    private final JTextField branchTextField;
    private final JTextField printerTextField;
    private final JTextField numberTextField;
    private final JTextField totalTextField;
    private JButton addPaymentMethod;
    private JButton cancelReceipt;
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
    
    private HashMap eventMap;
    private HashMap subjectMap;
    private HashMap receiptMap;
    
    public ReceiptShow(ReceiptController receiptController, int eventId, int receiptId){
        this.receiptController = receiptController;
        
        receiptShowPanel = new JPanel();
        receiptShowPanel.setLayout(null);
        
        eventMap = EventController.getEventById(eventId);
        subjectMap = SubjectController.getSubjectByCode(eventMap.get("subjectCode").toString());
        receiptMap = receiptController.getReceiptById(receiptId);
        
        titleLabel = new JLabel("<HTML><U>Recibo</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        receiptShowPanel.add(titleLabel);
        
        receiptDateLabel = new JLabel("Fecha:");
        receiptDateLabel.setBounds(30, 60, 130, 25);
        receiptShowPanel.add(receiptDateLabel);
        
        UtilDateModel model = new UtilDateModel(new Date(((Timestamp)receiptMap.get("receiptDate")).getTime()));
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
        receiptShowPanel.add(datePicker);
        
        receiptNumberLabel = new JLabel("Numero:");
        receiptNumberLabel.setBounds(370, 60, 130, 25);
        receiptShowPanel.add(receiptNumberLabel);
        
        branchTextField = new JTextField(receiptMap.get("receiptBranch").toString());
        branchTextField.setEditable(false);
        branchTextField.setHorizontalAlignment(JLabel.CENTER);
        branchTextField.setBounds(470, 60, 40, 25);
        receiptShowPanel.add(branchTextField);
        
        JLabel firstSeparator = new JLabel("-");
        firstSeparator.setBounds(517, 60, 40, 25);
        receiptShowPanel.add(firstSeparator);
        
        printerTextField = new JTextField(receiptMap.get("receiptPrinter").toString());
        printerTextField.setEditable(false);
        printerTextField.setHorizontalAlignment(JLabel.CENTER);
        printerTextField.setBounds(530, 60, 40, 25);
        receiptShowPanel.add(printerTextField);
        
        JLabel secondSeparator = new JLabel("-");
        secondSeparator.setBounds(577, 60, 40, 25);
        receiptShowPanel.add(secondSeparator);
        
        numberTextField = new JTextField(receiptMap.get("receiptNumber").toString());
        numberTextField.setEditable(false);
        numberTextField.setHorizontalAlignment(JLabel.RIGHT);
        numberTextField.setBounds(590, 60, 60, 25);
        receiptShowPanel.add(numberTextField);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        receiptShowPanel.add(subjectLabel);
        
        cancelledLabel = new JLabel("Anulado: ");
        cancelledLabel.setBounds(800, 100, 200, 25);
        receiptShowPanel.add(cancelledLabel);
        
        cancelledTextField = new JTextField();
        if((Boolean)receiptMap.get("cancelled")){
            cancelledTextField.setText("SI");
            cancelledTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.RED, Color.RED));
        }else{
            cancelledTextField.setText("NO");
            cancelledTextField.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.GREEN.darker(), Color.GREEN.brighter()));
        }
        cancelledTextField.setEditable(false);
        cancelledTextField.setEnabled(false);
        cancelledTextField.setBounds(950, 100, 230, 25);
        receiptShowPanel.add(cancelledTextField);
        
        cancelledReasonLabel = new JLabel("Motivo: ");
        cancelledReasonLabel.setBounds(800, 130, 200, 25);
        receiptShowPanel.add(cancelledReasonLabel);
        
        cancelledReasonTextArea = new JTextArea(0,0);
        if(receiptMap.get("cancelledReason")!=null){
            cancelledReasonTextArea.setText(receiptMap.get("cancelledReason").toString());
        }else{
            cancelledReasonTextArea.setText("");
        }
        
        cancelledReasonTextArea.setLineWrap(true);
        cancelledReasonTextArea.setWrapStyleWord(true);
        cancelledReasonTextArea.setEditable(false);
        cancelledReasonTextArea.setEnabled(false);
        JScrollPane cancelledReasonScrollPane = new JScrollPane();
        cancelledReasonScrollPane.setBounds(950, 130, 230, 90);
        cancelledReasonScrollPane.setViewportView(cancelledReasonTextArea);
        receiptShowPanel.add(cancelledReasonScrollPane);
        
        cancelledDateLabel = new JLabel("Fecha de Anulacion: ");
        cancelledDateLabel.setBounds(800, 240, 200, 25);
        receiptShowPanel.add(cancelledDateLabel);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cancelledDateTextField = new JTextField();
        if(receiptMap.get("cancelledDate")!=null){
            formatter.format(new Date(((Timestamp)receiptMap.get("cancelledDate")).getTime()));
            cancelledDateTextField.setText(formatter.format(new Date(((Timestamp)receiptMap.get("cancelledDate")).getTime())));
        }else{
            cancelledDateTextField.setText("");
        }
        cancelledDateTextField.setHorizontalAlignment(JLabel.CENTER);
        cancelledDateTextField.setEditable(false);
        cancelledDateTextField.setEnabled(false);
        cancelledDateTextField.setBounds(950, 240, 230, 25);
        receiptShowPanel.add(cancelledDateTextField);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        receiptShowPanel.add(subjectCodeLabel);

        subjectCodeTextField = new JTextField(subjectMap.get("code").toString());
        subjectCodeTextField.setEditable(false);
        subjectCodeTextField.setBounds(160, 150, 170, 25);
        receiptShowPanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        receiptShowPanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField(subjectMap.get("address").toString());
        subjectAddressTextField.setEditable(false);
        subjectAddressTextField.setBounds(490, 150, 170, 25);
        receiptShowPanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        receiptShowPanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField(subjectMap.get("name").toString());
        subjectNameTextField.setEditable(false);
        subjectNameTextField.setBounds(160, 180, 170, 25);
        receiptShowPanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        receiptShowPanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField(subjectMap.get("telephone").toString());
        subjectTelephoneTextField.setEditable(false);
        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
        receiptShowPanel.add(subjectTelephoneTextField);

        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 210, 120, 25);
        receiptShowPanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField(subjectMap.get("tradename").toString());
        subjectTradenameTextField.setEditable(false);
        subjectTradenameTextField.setBounds(160, 210, 170, 25);
        receiptShowPanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        receiptShowPanel.add(subjectFiscalNumberLabel);
        
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
        receiptShowPanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        receiptShowPanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField(subjectMap.get("city").toString());
        subjectCityTextField.setEditable(false);
        subjectCityTextField.setBounds(160, 240, 170, 25);
        receiptShowPanel.add(subjectCityTextField);
        
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
        receiptShowPanel.add(addPaymentMethod);
        
        //Total Pago
        totalLabel = new JLabel("Total Gs.: ");
        totalLabel.setBounds(1000, 490, 80, 25);
        receiptShowPanel.add(totalLabel);
    
        totalTextField = new JTextField(amountFormat.format((Double)receiptMap.get("totalPayed")));
        totalTextField.setEditable(false);
        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
        totalTextField.setBounds(1000, 520, 170, 25);
        receiptShowPanel.add(totalTextField);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelReceipt = new JButton();
        cancelReceipt.setText(" Cerrar");
        cancelReceipt.setIcon(cancelImageIcon);
        cancelReceipt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelReceipt.setBounds(1000, 590, 170, 32);
        receiptShowPanel.add(cancelReceipt);
        
        receiptDetailDefaultTableModel = new receiptDetailDefaultTableModel();
        receiptDetailTable = new JTable(receiptDetailDefaultTableModel);
        
        receiptDetailDefaultTableModel.addColumn("Medio de Pago");
        receiptDetailDefaultTableModel.addColumn("Numero de Documento");
        receiptDetailDefaultTableModel.addColumn("Fecha de Emision");
        receiptDetailDefaultTableModel.addColumn("Fecha de Cobro");
        receiptDetailDefaultTableModel.addColumn("Vencimiento");
        receiptDetailDefaultTableModel.addColumn("Banco");
        receiptDetailDefaultTableModel.addColumn("Monto");
        
        receiptDetailTable.setRowHeight(22);
        
        //Alineacion a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        receiptDetailTableJScrollPane = new JScrollPane(receiptDetailTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        receiptDetailTableJScrollPane.setBounds(30, 350, 940, 290);
        add(receiptDetailTableJScrollPane);
        
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setMinWidth(160);
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setMaxWidth(160);
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setPreferredWidth(160);
        receiptDetailTable.getColumnModel().getColumn(DOC_NUMBER_COLUMN).setCellRenderer(rightRenderer);
        
        //AMOUNT
        receiptDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellEditor(new AmountCellEditor());
        receiptDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellRenderer(new AmountCellRenderer());
        
        HashMap paymentMethodMap;
        ArrayList paymentList = (ArrayList) receiptMap.get("paymentMethod");
        Object[] row = new Object[receiptDetailTable.getColumnCount()];
        
        for(int i = 0; i < paymentList.size(); i++){
            paymentMethodMap = (HashMap) paymentList.get(i);

            row[PAYMENT_METHOD_COLUMN] = paymentMethodMap.get("paymentType");
            row[DOC_NUMBER_COLUMN] = paymentMethodMap.get("documentNumber");
            if(paymentMethodMap.get("documentEmitionDate")!=null){
                row[DOC_EMITION_DATE_COLUMN] = new java.sql.Date(((Timestamp)paymentMethodMap.get("documentEmitionDate")).getTime());
            }else{
                row[DOC_EMITION_DATE_COLUMN] = "";
            }
            
            if(paymentMethodMap.get("paymentDate")!=null){
                row[DOC_PAYMENT_DATE_COLUMN] = new java.sql.Date(((Timestamp)paymentMethodMap.get("paymentDate")).getTime());
            }else{
                row[DOC_PAYMENT_DATE_COLUMN] = "";
            }
            
            if(paymentMethodMap.get("dueDate")!=null){
                row[DOC_DUE_DATE_COLUMN] =  new java.sql.Date(((Timestamp)paymentMethodMap.get("dueDate")).getTime());
            }else{
                row[DOC_DUE_DATE_COLUMN] = "";
            }
            
            row[BANK_COLUMN] = paymentMethodMap.get("bankName");
            row[AMOUNT_COLUMN] = amountFormat.format((Double)paymentMethodMap.get("totalAmount"));
        
            receiptDetailDefaultTableModel.addRow(row);
        }
        
        add(receiptShowPanel);
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
        receiptController.receiptShowClosed();
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
    
    private class receiptDetailDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            switch(column){
                default:    return false;
            }
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
                double oldAmount = amountFormat.parse((receiptDetailDefaultTableModel.getValueAt(row, AMOUNT_COLUMN).toString())).intValue();
                String newAmountString = ((JTextField) component).getText();
                if(newAmountString.equals("")){
                    newAmountString = "0";
                }
                double newAmount = amountFormat.parse(newAmountString).doubleValue();
                double balanceTotal = amountFormat.parse("9").doubleValue();
                double total = amountFormat.parse("9").doubleValue();
                if(balanceTotal < ((total-oldAmount)+newAmount)){
                    optionPane = new JOptionPane("Con el valor ingresado se superara el saldo del evento", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
                    dialog = optionPane.createDialog(null, "Atencion!");
                    dialog.setVisible(true);
                    ((JTextField) component).setText(amountFormat.format(oldAmount));
                }else{
                    ((JTextField) component).setText(amountFormat.format(newAmount));
                    //updateTotal(newAmount, row, column);
                }
            } catch (HeadlessException | ParseException th) {
                Logger.getLogger(EventCreate.class.getName()).log(Level.SEVERE, null, th);
            }
            return ((JTextField) component).getText();
        }

    }
}
