/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.receipt;

import java.awt.Component;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import rentfur.event.EventController;
import rentfur.util.ComboBoxItem;

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
    
    private final JTable eventDetailTable;
    private final DefaultTableModel eventDetailDefaultTableModel;
    private final JScrollPane eventDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final int PAYMENT_METHOD_COLUMN = 0;
    
    public ReceiptCreate(ReceiptController receiptController){
        this.receiptController = receiptController;
        
        receiptCreatePanel = new JPanel();
        receiptCreatePanel.setLayout(null);
        
        titleLabel = new JLabel("<HTML><U>Recibo</U></HTML>");
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(600, 20, 200, 25);
        receiptCreatePanel.add(titleLabel);
        
        subjectLabel = new JLabel("<HTML><U>Datos del Cliente</U></HTML>");
        subjectLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        subjectLabel.setBounds(80, 110, 200, 25);
        receiptCreatePanel.add(subjectLabel);
        
        totalSummaryLabel = new JLabel("<HTML><U>Totales</U></HTML>");
        totalSummaryLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        totalSummaryLabel.setBounds(1130, 110, 200, 25);
        receiptCreatePanel.add(totalSummaryLabel);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 150, 80, 25);
        receiptCreatePanel.add(subjectCodeLabel);
        
//        subjectCodeTextField = new JTextField();
//        subjectCodeTextField.setEditable(false);
//        subjectCodeTextField.setBounds(160, 150, 170, 25);
//        receiptCreatePanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 150, 160, 25);
        receiptCreatePanel.add(subjectAddressLabel);
        
//        subjectAddressTextField = new JTextField();
//        subjectAddressTextField.setEditable(false);
//        subjectAddressTextField.setBounds(490, 150, 170, 25);
//        receiptCreatePanel.add(subjectAddressTextField);
        
        //Total Evento
//        totalLabel = new JLabel("Total: ");
//        totalLabel.setBounds(1100, 150, 80, 25);
//        receiptCreatePanel.add(totalLabel);
//        
//        totalTextField = new JTextField("0");
//        totalTextField.setEditable(false);
//        totalTextField.setHorizontalAlignment(JLabel.RIGHT);
//        totalTextField.setBounds(1200, 150, 170, 25);
//        receiptCreatePanel.add(totalTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 180, 100, 25);
        receiptCreatePanel.add(subjectNameLabel);
        
//        subjectNameTextField = new JTextField();
//        subjectNameTextField.setEditable(false);
//        subjectNameTextField.setBounds(160, 180, 170, 25);
//        receiptCreatePanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 180, 100, 25);
        receiptCreatePanel.add(subjectTelephoneLabel);
        
//        subjectTelephoneTextField = new JTextField();
//        subjectTelephoneTextField.setEditable(false);
//        subjectTelephoneTextField.setBounds(490, 180, 170, 25);
//        receiptCreatePanel.add(subjectTelephoneTextField);
        
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
        
//        subjectTradenameTextField = new JTextField();
//        subjectTradenameTextField.setEditable(false);
//        subjectTradenameTextField.setBounds(160, 210, 170, 25);
//        receiptCreatePanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 210, 120, 25);
        receiptCreatePanel.add(subjectFiscalNumberLabel);
        
//        subjectFiscalNumberTextField = new JTextField();
//        subjectFiscalNumberTextField.setEditable(false);
//        subjectFiscalNumberTextField.setBounds(490, 210, 170, 25);
//        receiptCreatePanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 240, 80, 25);
        receiptCreatePanel.add(subjectCityLabel);
        
//        subjectCityTextField = new JTextField();
//        subjectCityTextField.setEditable(false);
//        subjectCityTextField.setBounds(160, 240, 170, 25);
//        receiptCreatePanel.add(subjectCityTextField);
        
        eventDetailDefaultTableModel = new eventDetailDefaultTableModel();
        eventDetailTable = new JTable(eventDetailDefaultTableModel);
        
        eventDetailDefaultTableModel.addColumn("Medio de Pago");
        eventDetailDefaultTableModel.addColumn("Numero de Documento");
        eventDetailDefaultTableModel.addColumn("Fecha");
        eventDetailDefaultTableModel.addColumn("Banco");
        eventDetailDefaultTableModel.addColumn("Monto");
        
        eventDetailTable.setRowHeight(22);
        
        eventDetailTableJScrollPane = new JScrollPane(eventDetailTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        eventDetailTableJScrollPane.setBounds(30, 300, 800, 400);
        add(eventDetailTableJScrollPane);
        
        eventDetailTable.getColumnModel().getColumn(0).setCellEditor(new MyComboBoxEditor(EventController.getEventStatusAvailablesForCreateEvent(false)));
        eventDetailTable.getColumnModel().getColumn(0).setCellRenderer(new MyComboBoxRenderer(EventController.getEventStatusAvailablesForCreateEvent(false)));
        
        Object[] row = new Object[5];        
        
        row[0] = EventController.getEventStatusAvailablesForCreateEvent(false);
        row[1] = "";
        row[2] = "";
        row[3] = "";
        row[4] = "";

        eventDetailDefaultTableModel.addRow(row);
        
        
        add(receiptCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Registrar Pago");
        setBounds(150,50,1250,700);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    private class eventDetailDefaultTableModel extends DefaultTableModel{
        
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
                                                        case PAYMENT_METHOD_COLUMN: return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    class MyComboBoxRenderer extends JComboBox implements TableCellRenderer {
        public MyComboBoxRenderer(ComboBoxItem[] items) {
          super(items);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
          if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
          } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
          }
          setSelectedItem(value);
          return this;
        }
      }

      class MyComboBoxEditor extends DefaultCellEditor {
        public MyComboBoxEditor(ComboBoxItem[] items) {
          super(new JComboBox(items));
        }
      }
    
}
