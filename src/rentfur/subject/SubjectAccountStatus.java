/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */
public class SubjectAccountStatus extends JInternalFrame{
    
    private final SubjectController subjectController;
    private final JPanel subjectAccountStatusPanel;
    private final JLabel titleLabel;
    private final JLabel initDateLabel;
    private final JLabel endDateLabel;
    private final JLabel previousBalanceLabel;
    private final JLabel balanceLabel;
    private final JTextField initDateTextField;
    private final JTextField endDateTextField;
    private final JTextField previousBalanceTextField;
    private final JTextField balanceTextField;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
    private final JButton closeButton;
    private final JDatePickerImpl initDateDatePicker;
    private final JDatePickerImpl endDateDatePicker;
    
    private final JTable purchaseInvoicesResultTable;
    private final DefaultTableModel purchaseInvoicesResultDefaultTableModel;
    private final JScrollPane purchaseInvoicesResultTableJScrollPane;
    
    private final int ID_COLUMN = 0;
    private final int DATE_COLUMN = 1;
    private final int DOCUMENT_COLUMN = 2;
    private final int DOCUMENT_DESCRIPTION_COLUMN = 3;
    private final int DOCUMENT_NUMBER_COLUMN = 4;
    private final int CONCEPT_COLUMN = 5;
    private final int DEBIT_COLUMN = 6;
    private final int CREDIT_COLUMN = 7;
    private final int BALANCE_COLUMN = 8;
    private final int PREVIOUS_BALANCE_COLUMN = 9;
    
    private final HashMap subjectMap;
    
    public SubjectAccountStatus(SubjectController subjectController, int subjectId){
        
        this.subjectController = subjectController;
        
//        if((Boolean)userRoles.getRolesMap().get(PositionController.ROLE_RF_SUBJECT)){
//            onlyQuery = true;
//        }
        
        subjectMap = SubjectController.getSubjectById(subjectId);
        
        subjectAccountStatusPanel = new JPanel();
        subjectAccountStatusPanel.setLayout(null);
        
        titleLabel = new JLabel("<HTML><U>Registro de Movimientos</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(500, 20, 200, 25);
        subjectAccountStatusPanel.add(titleLabel);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = 1;
        String date = year + "/" + month + "/" + day;
        Date initDate = null;

        try {
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
          initDate = formatter.parse(date);
        } catch (ParseException e) {
          System.out.println(e.toString());
          e.printStackTrace();
        }
        
        UtilDateModel initModel = new UtilDateModel(initDate);
        UtilDateModel endModel = new UtilDateModel(new Date());
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl initDatePanel = new JDatePanelImpl(initModel, p);
        JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel, p);
        // Don't know about the formatter, but there it is...
        
        
        //FILA 1
        initDateLabel = new JLabel("Fecha Inicio:");
        initDateLabel.setBounds(30, 80, 80, 25);
        subjectAccountStatusPanel.add(initDateLabel);
        
        initDateTextField = new JTextField();
        initDateTextField.setBounds(160, 80, 170, 25);
        subjectAccountStatusPanel.add(initDateTextField);
        
        initDateDatePicker = new JDatePickerImpl(initDatePanel, new DateLabelFormatter("yyyy-MM-dd"));
        initDateDatePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setInitDateValue();
            }
        });
        initDateDatePicker.setBounds(370, 80, 170, 25);
        subjectAccountStatusPanel.add(initDateDatePicker);
        setInitDateValue();
        
        previousBalanceLabel = new JLabel("Saldo Anterior:");
        previousBalanceLabel.setBounds(800, 80, 130, 25);
        subjectAccountStatusPanel.add(previousBalanceLabel);
        
        previousBalanceTextField = new JTextField();
        previousBalanceTextField.setBounds(940, 80, 170, 25);
        previousBalanceTextField.setHorizontalAlignment(JLabel.RIGHT);
        previousBalanceTextField.setEditable(false);
        subjectAccountStatusPanel.add(previousBalanceTextField);
        
        
        //FILA 2
        endDateLabel = new JLabel("Fecha Fin:");
        endDateLabel.setBounds(30, 110, 100, 25);
        subjectAccountStatusPanel.add(endDateLabel);
        
        endDateTextField = new JTextField();
        endDateTextField.setBounds(160, 110, 170, 25);
        subjectAccountStatusPanel.add(endDateTextField);
        
        endDateDatePicker = new JDatePickerImpl(endDatePanel, new DateLabelFormatter("yyyy-MM-dd"));
        endDateDatePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setEndDateValue();
            }
            
            
        });
        endDateDatePicker.setBounds(370, 110, 170, 25);
        subjectAccountStatusPanel.add(endDateDatePicker);
        setEndDateValue();
        
        balanceLabel = new JLabel("Saldo:");
        balanceLabel.setBounds(800, 110, 130, 25);
        subjectAccountStatusPanel.add(balanceLabel);
        
        balanceTextField = new JTextField();
        balanceTextField.setEditable(false);
        balanceTextField.setHorizontalAlignment(JLabel.RIGHT);
        balanceTextField.setBounds(940, 110, 170, 25);
        subjectAccountStatusPanel.add(balanceTextField);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Filtrar", searchIconImage);
        searchButton.setBounds(30, 170, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterSubjectAccountButtonAction(e);
            }
        });
        subjectAccountStatusPanel.add(searchButton);
        
        //TABLA DE RESULTADOS
        purchaseInvoicesResultDefaultTableModel = new SubjectAccountStatusDefaultTableModel();
        purchaseInvoicesResultTable = new JTable(purchaseInvoicesResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                
        subjectController.setSubejctAccountStatusResultsTable(purchaseInvoicesResultDefaultTableModel, false, initDateTextField.getText(), endDateTextField.getText(), subjectMap.get("code").toString(), balanceTextField, previousBalanceTextField);
        purchaseInvoicesResultTable.setRowHeight(24);
            
        //ID
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setResizable(false);
        
        //Fecha
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setMaxWidth(210);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setMinWidth(150);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setPreferredWidth(200);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setCellRenderer(centerRenderer);
         
        //Tipo de documento
        
        //Tipo de documento
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_COLUMN).setMaxWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_COLUMN).setMinWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_COLUMN).setPreferredWidth(0);
        
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_DESCRIPTION_COLUMN).setMaxWidth(300);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_DESCRIPTION_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_DESCRIPTION_COLUMN).setPreferredWidth(290);
        
        //Numero de documento
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setMaxWidth(230);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setPreferredWidth(220);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setCellRenderer(centerRenderer);
        
        //Concepto
        purchaseInvoicesResultTable.getColumnModel().getColumn(CONCEPT_COLUMN).setMaxWidth(320);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CONCEPT_COLUMN).setMinWidth(280);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CONCEPT_COLUMN).setPreferredWidth(300);
        
        //DEBE
        purchaseInvoicesResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setMaxWidth(130);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setPreferredWidth(110);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setCellRenderer(rightRenderer);
        
        //HABER
        purchaseInvoicesResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setMaxWidth(130);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setPreferredWidth(110);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setCellRenderer(rightRenderer);
        
        //SALDO
        purchaseInvoicesResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setMaxWidth(130);
        purchaseInvoicesResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setPreferredWidth(110);
        purchaseInvoicesResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setCellRenderer(rightRenderer);
        
        //SALDO ANTERIOR
        purchaseInvoicesResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setMaxWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setMinWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setPreferredWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setCellRenderer(rightRenderer);
        
        purchaseInvoicesResultTableJScrollPane = new JScrollPane();
        purchaseInvoicesResultTableJScrollPane.setBounds(30, 240, 1120, 300);
        purchaseInvoicesResultTableJScrollPane.setViewportView(purchaseInvoicesResultTable);
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        closeButton = new JButton();
        closeButton.setText(" Cerrar");
        closeButton.setIcon(cancelImageIcon);
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        closeButton.setBounds(940, 600, 170, 32);
        subjectAccountStatusPanel.add(closeButton);
        
        add(purchaseInvoicesResultTableJScrollPane);
        
        add(subjectAccountStatusPanel);
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Estado de Cuenta ["+subjectMap.get("code").toString()+" - "+subjectMap.get("name").toString()+"]");
        setBounds(400,100,1200,700);
        //pack();
        setVisible(true);
    }

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        subjectController.setSubjectAccountStatusClosed();
    }
    
    public void filterSubjectAccountButtonAction(ActionEvent e) {
        
        String initDate = initDateTextField.getText();
        String endDate = endDateTextField.getText();
       subjectController.setSubejctAccountStatusResultsTable(purchaseInvoicesResultDefaultTableModel, true, initDate, endDate, subjectMap.get("code").toString(), balanceTextField, previousBalanceTextField);
    }
    
    private void setInitDateValue(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date initDate = (Date) initDateDatePicker.getModel().getValue();
        
        if(initDate!=null){
            initDateTextField.setText(formatter.format(initDate));
        }else{
            initDateTextField.setText("");
        }
        
    }
    
    private void setEndDateValue(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date endDate = (Date) endDateDatePicker.getModel().getValue();
        
        if(endDate!=null){
            endDateTextField.setText(formatter.format(endDate));
        }else{
            endDateTextField.setText("");
        }
        
    }
    
    private class SubjectAccountStatusDefaultTableModel extends DefaultTableModel{
        
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
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    
}
