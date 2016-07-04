/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.TableCellRenderer;
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
    
    private final JTable subjectAccountMovementsResultTable;
    private final DefaultTableModel subjectAccountMovementsResultDefaultTableModel;
    private final JScrollPane subjectAccountMovementsResultTableJScrollPane;
    
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
        titleLabel.setBounds(500, 20, 300, 25);
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
        initDateTextField.setEditable(false);
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
        
        previousBalanceTextField = new JTextField("0");
        previousBalanceTextField.setBounds(940, 80, 170, 25);
        previousBalanceTextField.setHorizontalAlignment(JLabel.RIGHT);
        previousBalanceTextField.setEditable(false);
        subjectAccountStatusPanel.add(previousBalanceTextField);
        
        
        //FILA 2
        endDateLabel = new JLabel("Fecha Fin:");
        endDateLabel.setBounds(30, 110, 100, 25);
        subjectAccountStatusPanel.add(endDateLabel);
        
        endDateTextField = new JTextField();
        endDateTextField.setEditable(false);
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
        
        balanceTextField = new JTextField("0");
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
        subjectAccountMovementsResultDefaultTableModel = new SubjectAccountStatusDefaultTableModel();
        subjectAccountMovementsResultTable = new JTable(subjectAccountMovementsResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                
        subjectController.setSubejctAccountStatusResultsTable(subjectAccountMovementsResultDefaultTableModel, false, initDateTextField.getText(), endDateTextField.getText(), subjectMap.get("code").toString(), balanceTextField, previousBalanceTextField);
        subjectAccountMovementsResultTable.setRowHeight(24);
        
        TableCellRenderer rendererFromHeader = subjectAccountMovementsResultTable.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setHeaderRenderer(new CreditHeaderRenderer());
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setHeaderRenderer(new DebitHeaderRenderer());
        subjectAccountMovementsResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setHeaderRenderer(new BalanceHeaderRenderer());
            
        //ID
        subjectAccountMovementsResultTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(ID_COLUMN).setResizable(false);
        
        //Fecha
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DATE_COLUMN).setMaxWidth(210);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DATE_COLUMN).setMinWidth(150);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DATE_COLUMN).setPreferredWidth(200);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DATE_COLUMN).setCellRenderer(centerRenderer);
         
        //Tipo de documento
        
        //Tipo de documento
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_COLUMN).setMaxWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_COLUMN).setMinWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_COLUMN).setPreferredWidth(0);
        
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_DESCRIPTION_COLUMN).setMaxWidth(300);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_DESCRIPTION_COLUMN).setMinWidth(100);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_DESCRIPTION_COLUMN).setPreferredWidth(290);
        
        //Numero de documento
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setMaxWidth(230);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setMinWidth(100);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setPreferredWidth(220);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DOCUMENT_NUMBER_COLUMN).setCellRenderer(centerRenderer);
        
        //Concepto
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CONCEPT_COLUMN).setMaxWidth(320);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CONCEPT_COLUMN).setMinWidth(280);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CONCEPT_COLUMN).setPreferredWidth(300);
        
        //DEBE
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setMaxWidth(130);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setMinWidth(100);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setPreferredWidth(110);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(DEBIT_COLUMN).setCellRenderer(new DebitCellRenderer());
        
        //HABER
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setMaxWidth(130);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setMinWidth(100);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setPreferredWidth(110);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(CREDIT_COLUMN).setCellRenderer(new CreditCellRenderer());
        
        //SALDO
        subjectAccountMovementsResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setMaxWidth(130);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setMinWidth(100);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setPreferredWidth(110);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(BALANCE_COLUMN).setCellRenderer(new BalanceCellRenderer());
        
        //SALDO ANTERIOR
        subjectAccountMovementsResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setMaxWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setMinWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setPreferredWidth(0);
        subjectAccountMovementsResultTable.getColumnModel().getColumn(PREVIOUS_BALANCE_COLUMN).setCellRenderer(rightRenderer);
        
        subjectAccountMovementsResultTableJScrollPane = new JScrollPane();
        subjectAccountMovementsResultTableJScrollPane.setBounds(30, 240, 1120, 300);
        subjectAccountMovementsResultTableJScrollPane.setViewportView(subjectAccountMovementsResultTable);
        
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
        
        add(subjectAccountMovementsResultTableJScrollPane);
        
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
       subjectController.setSubejctAccountStatusResultsTable(subjectAccountMovementsResultDefaultTableModel, true, initDate, endDate, subjectMap.get("code").toString(), balanceTextField, previousBalanceTextField);
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
    
    private class CreditHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public CreditHeaderRenderer() {
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
    
    private class DebitHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public DebitHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(new Color(141, 170, 201));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

    }
    
    private class BalanceHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public BalanceHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(new Color(239, 221, 119));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

    }
    
    private class CreditCellRenderer extends JTextField implements TableCellRenderer {

        public CreditCellRenderer() {
            setOpaque(true);
            setBackground(new Color(237, 247, 243));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Haber");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    private class DebitCellRenderer extends JTextField implements TableCellRenderer {

        public DebitCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Debe");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    private class BalanceCellRenderer extends JTextField implements TableCellRenderer {

        public BalanceCellRenderer() {
            setOpaque(true);
            setBackground(new Color(244, 249, 199));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Saldo");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
}
