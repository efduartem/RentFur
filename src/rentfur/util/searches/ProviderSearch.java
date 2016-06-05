/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util.searches;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
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
 * @author hp
 */
public class ProviderSearch extends JInternalFrame{
    private SearchController searchController;
    private final JPanel providerParamPanel;
    private final JLabel providerCodeLabel;
    private final JLabel providerAddressLabel;
    private final JLabel providerNameLabel;
    private final JLabel providerTelephoneLabel;
    private final JLabel providerTradenameLabel;
    private final JLabel providerFiscalNumberLabel;
    private final JLabel providerCityLabel;
    private final JLabel listOfResultLabel;
    private final JTextField providerCodeTextField;
    private final JTextField providerNameTextField;
    private final JTextField providerAddressTextField;
    private final JTextField providerCityTextField;
    private final JTextField providerTradenameTextField;
    private final JTable providerResultTable;
    private final JTextField providerTelephoneTextField;
   
    private final NumericTextField providerFiscalNumberTextField;
    private final JButton selectButton;
    private final JButton cancelButton;
    private TableRowSorter trsfiltro;
    private String currentSelectedCode = "";
    private final DefaultTableModel providerResultDefaultTableModel;
    private final JScrollPane providerResultTableJScrollPane;
    
    public ProviderSearch (SearchController searchController){
        this.searchController = searchController;
        
        providerParamPanel = new JPanel();
        providerParamPanel.setLayout(null);
        
        //FILA 1
        providerCodeLabel = new JLabel("Codigo:");
        providerCodeLabel.setBounds(30, 20, 80, 25);
        providerParamPanel.add(providerCodeLabel);
        
        providerCodeTextField = new JTextField();
        providerCodeTextField.setBounds(160, 20, 170, 25);
        providerCodeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                providerResultTable.repaint();
                filter();
            }
        });
        providerParamPanel.add(providerCodeTextField);
        
        providerAddressLabel = new JLabel("Dirección:");
        providerAddressLabel.setBounds(370, 20, 160, 25);
        providerParamPanel.add(providerAddressLabel);
        
        providerAddressTextField = new JTextField();
        providerAddressTextField.setBounds(440, 20, 170, 25);
        providerAddressTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                providerResultTable.repaint();
                filter();
            }
        });
        providerParamPanel.add(providerAddressTextField);
        
        //FILA 2
        providerNameLabel = new JLabel("Razón Social:");
        providerNameLabel.setBounds(30, 50, 100, 25);
        providerParamPanel.add(providerNameLabel);
        
        providerNameTextField = new JTextField();
        providerNameTextField.setBounds(160, 50, 170, 25);
        providerNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                providerResultTable.repaint();
                filter();
            }
        });
        providerParamPanel.add(providerNameTextField);
        
        providerTelephoneLabel = new JLabel("Teléfono:");
        providerTelephoneLabel.setBounds(370, 50, 100, 25);
        providerParamPanel.add(providerTelephoneLabel);
        
        providerTelephoneTextField = new JTextField();
        providerTelephoneTextField.setBounds(440, 50, 170, 25);
        providerTelephoneTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                providerResultTable.repaint();
                filter();
            }
        });
        providerParamPanel.add(providerTelephoneTextField);
        
        //FILA 3
        providerTradenameLabel = new JLabel("Nombre Comercial:");
        providerTradenameLabel.setBounds(30, 80, 120, 25);
        providerParamPanel.add(providerTradenameLabel);
        
        providerTradenameTextField = new JTextField();
        providerTradenameTextField.setBounds(160, 80, 170, 25);
        providerTradenameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                providerResultTable.repaint();
                filter();
            }
        });
        providerParamPanel.add(providerTradenameTextField);
        
        providerFiscalNumberLabel = new JLabel("RUC / CI:");
        providerFiscalNumberLabel.setBounds(370, 80, 120, 25);
        providerParamPanel.add(providerFiscalNumberLabel);
        
        DecimalFormat amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        providerFiscalNumberTextField = new NumericTextField(20, amountFormat);
        providerFiscalNumberTextField.setBounds(440, 80, 170, 25);
        providerFiscalNumberTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                         providerResultTable.repaint();
                         filter();
                     }
                     
                     public void update(KeyEvent e){
                         String texto = providerFiscalNumberTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                providerFiscalNumberTextField.setValue(Double.valueOf(texto));
                            }
                         }
                     }
                 });

        providerParamPanel.add(providerFiscalNumberTextField);
        
        //FILA 4
        providerCityLabel = new JLabel("Ciudad:");
        providerCityLabel.setBounds(30, 110, 80, 25);
        providerParamPanel.add(providerCityLabel);
        
        providerCityTextField = new JTextField();
        providerCityTextField.setBounds(160, 110, 170, 25);
        providerCityTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                providerResultTable.repaint();
                filter();
            }
        });
        providerParamPanel.add(providerCityTextField);
        
        listOfResultLabel = new JLabel("<HTML><U>Lista de Proveedores</U></HTML>");
        listOfResultLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        listOfResultLabel.setBounds(400, 140, 200, 25);
        providerParamPanel.add(listOfResultLabel);
        
        selectButton =  new JButton("Seleccionar");
        selectButton.setBounds(680, 420, 100, 25);
        selectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectButtonAction(e);
            }
        });
        providerParamPanel.add(selectButton);
        
        cancelButton =  new JButton("Cancelar");
        cancelButton.setBounds(800, 420, 100, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        providerParamPanel.add(cancelButton);
        
        //TABLA DE RESULTADOS
        providerResultDefaultTableModel = new providerSearchtResultDefaultTableModel();
        providerResultTable = new JTable(providerResultDefaultTableModel){
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
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        searchController.setProviderIndexResultsTable(providerResultDefaultTableModel, false, null, null, null, null, null, null, null);
        providerResultTable.setRowHeight(22);
                
        ButtonGroup radioButtonGroup = new ButtonGroup();
        for(int i = 0 ; i < providerResultTable.getRowCount(); i++){
            radioButtonGroup.add((JRadioButton) providerResultDefaultTableModel.getValueAt(i, 0));
        }
        
        //Code
        providerResultTable.getColumnModel().getColumn(0).setMaxWidth(90);
        providerResultTable.getColumnModel().getColumn(0).setMinWidth(90);
        providerResultTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        providerResultTable.getColumnModel().getColumn(0).setCellRenderer(new ProviderSearch.RadioButtonRenderer());
        providerResultTable.getColumnModel().getColumn(0).setCellEditor(new ProviderSearch.RadioButtonEditor(new JCheckBox()));
        
        //Name
        providerResultTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        
        //Tradename
        providerResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Address
        providerResultTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        //City
        providerResultTable.getColumnModel().getColumn(4).setMaxWidth(100);
        providerResultTable.getColumnModel().getColumn(4).setMinWidth(100);
        providerResultTable.getColumnModel().getColumn(4).setResizable(false);
        
        //Telephone
        providerResultTable.getColumnModel().getColumn(5).setMaxWidth(100);
        providerResultTable.getColumnModel().getColumn(5).setMinWidth(100);
        providerResultTable.getColumnModel().getColumn(5).setResizable(false);
        
        //Fiscal Number
        providerResultTable.getColumnModel().getColumn(6).setMaxWidth(100);
        providerResultTable.getColumnModel().getColumn(6).setMinWidth(100);
        providerResultTable.getColumnModel().getColumn(6).setResizable(false);
        providerResultTable.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);
        
        //ID
        providerResultTable.getColumnModel().getColumn(7).setMaxWidth(0);
        providerResultTable.getColumnModel().getColumn(7).setMinWidth(0);
        providerResultTable.getColumnModel().getColumn(7).setPreferredWidth(0);
        
        //CODIGO PARA FILTRO
        providerResultTable.getColumnModel().getColumn(8).setMaxWidth(0);
        providerResultTable.getColumnModel().getColumn(8).setMinWidth(0);
        providerResultTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        
        trsfiltro = new TableRowSorter(providerResultDefaultTableModel);
        // Añadimos al Jtable el filtro trsfiltro
        
        providerResultTable.setRowSorter(trsfiltro);
        
        providerResultTableJScrollPane = new JScrollPane();
        providerResultTableJScrollPane.setBounds(30, 180, 900, 220);
        providerResultTableJScrollPane.setViewportView(providerResultTable);
        
        add(providerResultTableJScrollPane);
        add(providerParamPanel);
        
        pack();
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Busqueda de Proveedores");
        setBounds(350,150,1000,520);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(false);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        searchController.setClosedSubjectSearch();
        searchController.setSubjetcSelectedCode("");
    }
    
    private void cancelButtonAction(ActionEvent e) {
        searchController.setProviderSelectedCode("");
        doDefaultCloseAction();
    }
    
    public void filter() {
        ArrayList<RowFilter<TableModel, Object>> andFilter = new ArrayList<>(1);
	RowFilter<TableModel, Object> rf;
        
        //Name
        rf = RowFilter.regexFilter("(?i)" + providerNameTextField.getText(), 1);
        andFilter.add(rf);
        
        //TradeName
        rf = RowFilter.regexFilter("(?i)" + providerTradenameTextField.getText(), 2);
        andFilter.add(rf);
        
        //Address
        rf = RowFilter.regexFilter("(?i)" + providerAddressTextField.getText(), 3);
        andFilter.add(rf);
        
        //City
        rf = RowFilter.regexFilter("(?i)" + providerCityTextField.getText(), 4);
        andFilter.add(rf);
        
        //Telephone
        rf = RowFilter.regexFilter("(?i)" + providerTelephoneTextField.getText(), 5);
        andFilter.add(rf);
        
        //Fiscal Number
        rf = RowFilter.regexFilter("(?i)" + providerFiscalNumberTextField.getText(), 6);
        andFilter.add(rf);
        
        //Code
        rf = RowFilter.regexFilter("(?i)" + providerCodeTextField.getText(), 8);
        andFilter.add(rf);
        
        
	
	RowFilter<TableModel, Object> rowf = RowFilter.andFilter(andFilter);
	trsfiltro.setRowFilter(rowf);
        
        //trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+textFilter, column));
    }
    
    private void selectButtonAction(ActionEvent e){
        if(currentSelectedCode.equals("")){
            JOptionPane optionPane = new JOptionPane("No ha seleccionado ningun Proveedor", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            searchController.setProviderSelectedCode(currentSelectedCode);
            doDefaultCloseAction();
        }
    };
    
    private class providerSearchtResultDefaultTableModel extends DefaultTableModel{
        
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
        private String code;
        public RadioButtonEditor(JCheckBox checkBox) {
          super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
          if (value == null)
            return null;
          button = (JRadioButton) value;
          this.code = ((JRadioButton)value).getText();
          button.addItemListener(this);
          return (Component) value;
        }

        @Override
        public Object getCellEditorValue() {
          button.removeItemListener(this);
          currentSelectedCode = code;
          return button;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
          super.fireEditingStopped();
        }
      }
}
