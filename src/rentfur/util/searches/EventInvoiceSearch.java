package rentfur.util.searches;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import rentfur.util.NumericTextField;

/**
 *
 * @author FDuarte
 */
public class EventInvoiceSearch extends JInternalFrame{
    private final SearchController searchController;
    private final JPanel invoceParamPanel;
    private final JLabel invoiceNumberLabel;
//    private final JLabel subjectAddressLabel;
//    private final JLabel subjectNameLabel;
//    private final JLabel subjectTelephoneLabel;
//    private final JLabel subjectTradenameLabel;
//    private final JLabel subjectFiscalNumberLabel;
//    private final JLabel subjectCityLabel;
    private final JLabel listOfResultLabel;
    private final JTextField invoiceNumberTextField;
//    private final JTextField subjectAddressTextField;
//    private final JTextField subjectNameTextField;
//    private final JTextField subjectTelephoneTextField;
//    private final JTextField subjectTradenameTextField;
//    private final NumericTextField subjectFiscalNumberTextField;
//    private final JTextField subjectCityTextField;
    private final JTable invoicesResultTable;
    private final DefaultTableModel invoicesResultDefaultTableModel;
    private final JScrollPane invoicesResultTableJScrollPane;
    private TableRowSorter trsfiltro;
    private final JButton selectButton;
    private final JButton cancelButton;
    private int currentSelectedId = 0;
    
    public EventInvoiceSearch(SearchController searchController, int eventId){
        this.searchController = searchController;
        
        invoceParamPanel = new JPanel();
        invoceParamPanel.setLayout(null);
        
        //FILA 1
        invoiceNumberLabel = new JLabel("Nro. Factura:");
        invoiceNumberLabel.setBounds(30, 20, 80, 25);
        invoceParamPanel.add(invoiceNumberLabel);
        
        invoiceNumberTextField = new JTextField();
        invoiceNumberTextField.setBounds(160, 20, 170, 25);
        invoiceNumberTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                invoicesResultTable.repaint();
                filter();
            }
        });
        invoceParamPanel.add(invoiceNumberTextField);
        
        listOfResultLabel = new JLabel("<HTML><U>Lista de Facturas</U></HTML>");
        listOfResultLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        listOfResultLabel.setBounds(200, 50, 200, 25);
        invoceParamPanel.add(listOfResultLabel);
        
        selectButton =  new JButton("Seleccionar");
        selectButton.setBounds(400, 240, 100, 25);
        selectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectButtonAction(e);
            }
        });
        invoceParamPanel.add(selectButton);
        
        cancelButton =  new JButton("Cancelar");
        cancelButton.setBounds(510, 240, 100, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        invoceParamPanel.add(cancelButton);
        
        //TABLA DE RESULTADOS
        invoicesResultDefaultTableModel = new InvoicesSearchtResultDefaultTableModel();
        invoicesResultTable = new JTable(invoicesResultDefaultTableModel){
            @Override
            public void tableChanged(TableModelEvent e) {
              super.tableChanged(e);
              repaint();
            }
          };
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        searchController.setEventInvoiceResultsTable(invoicesResultDefaultTableModel, eventId);
        invoicesResultTable.setRowHeight(22);
                
        ButtonGroup radioButtonGroup = new ButtonGroup();
        for(int i = 0 ; i < invoicesResultTable.getRowCount(); i++){
            radioButtonGroup.add((JRadioButton) invoicesResultDefaultTableModel.getValueAt(i, 0));
        }
        
        //CheckBox
        invoicesResultTable.getColumnModel().getColumn(0).setMaxWidth(30);
        invoicesResultTable.getColumnModel().getColumn(0).setMinWidth(30);
        invoicesResultTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        invoicesResultTable.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        invoicesResultTable.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor(new JCheckBox()));
        
        //Number
        invoicesResultTable.getColumnModel().getColumn(1).setMaxWidth(330);
        invoicesResultTable.getColumnModel().getColumn(1).setMinWidth(130);
        invoicesResultTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        invoicesResultTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        //Total
        invoicesResultTable.getColumnModel().getColumn(3).setMaxWidth(130);
        invoicesResultTable.getColumnModel().getColumn(3).setMinWidth(130);
        invoicesResultTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        invoicesResultTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        //Id
        invoicesResultTable.getColumnModel().getColumn(4).setMaxWidth(0);
        invoicesResultTable.getColumnModel().getColumn(4).setMinWidth(0);
        invoicesResultTable.getColumnModel().getColumn(4).setPreferredWidth(0);
        
        //Number
        invoicesResultTable.getColumnModel().getColumn(5).setMaxWidth(0);
        invoicesResultTable.getColumnModel().getColumn(5).setMinWidth(0);
        invoicesResultTable.getColumnModel().getColumn(5).setPreferredWidth(0);
        
        trsfiltro = new TableRowSorter(invoicesResultDefaultTableModel);
        // Añadimos al Jtable el filtro trsfiltro
        
        invoicesResultTable.setRowSorter(trsfiltro);
        
        invoicesResultTableJScrollPane = new JScrollPane();
        invoicesResultTableJScrollPane.setBounds(30, 100, 600, 120);
        invoicesResultTableJScrollPane.setViewportView(invoicesResultTable);
        
        add(invoicesResultTableJScrollPane);
        add(invoceParamPanel);
        
        pack();
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Busqueda de Factura");
        setBounds(490,250,700,320);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(false);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        searchController.setClosedEventInvoiceSearch();
        searchController.setInvoiceSelectedId(0);
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void cancelButtonAction(ActionEvent e) {
        searchController.setInvoiceSelectedId(0);
        doDefaultCloseAction();
    }
    
    private void selectButtonAction(ActionEvent e){
        if(currentSelectedId == 0){
            JOptionPane optionPane = new JOptionPane("No ha seleccionado ninguna Factura", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            searchController.setInvoiceSelectedId(currentSelectedId);
            doDefaultCloseAction();
        }
    };
    
    public int getIdValueFromTable(int row){
        int invoiceId = 0;
        try{
            invoiceId = Integer.valueOf(invoicesResultDefaultTableModel.getValueAt(row, 4).toString());
        }catch(Throwable th){
            th.printStackTrace();
        }
        return invoiceId;
    }
    
    public void filter() {
        ArrayList<RowFilter<TableModel, Object>> andFilter = new ArrayList<>(1);
	RowFilter<TableModel, Object> rf;
        
        //Name
        rf = RowFilter.regexFilter("(?i)" + invoiceNumberTextField.getText(), 1);
        andFilter.add(rf);
        
	RowFilter<TableModel, Object> rowf = RowFilter.andFilter(andFilter);
	trsfiltro.setRowFilter(rowf);
        
        //trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+textFilter, column));
    }
    
    private class InvoicesSearchtResultDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 0:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    class RadioButtonRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
          if (value == null)
            return null;
          return (Component) value;
        }
      }

      class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
        private JRadioButton button;
        private int id;
        public RadioButtonEditor(JCheckBox checkBox) {
          super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
          if (value == null)
            return null;
          button = (JRadioButton) value;
          this.id = getIdValueFromTable(row);
          button.addItemListener(this);
          return (Component) value;
        }

        @Override
        public Object getCellEditorValue() {
          button.removeItemListener(this);
          currentSelectedId = id;
          return button;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
          super.fireEditingStopped();
        }
      }
}
