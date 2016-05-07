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
 * @author FDuarte
 */
public class SubjectSearch extends JInternalFrame{
    private final SearchController searchController;
    private final JPanel subjectParamPanel;
    private final JLabel subjectCodeLabel;
    private final JLabel subjectAddressLabel;
    private final JLabel subjectNameLabel;
    private final JLabel subjectTelephoneLabel;
    private final JLabel subjectTradenameLabel;
    private final JLabel subjectFiscalNumberLabel;
    private final JLabel subjectCityLabel;
    private final JLabel listOfResultLabel;
    private final JTextField subjectCodeTextField;
    private final JTextField subjectAddressTextField;
    private final JTextField subjectNameTextField;
    private final JTextField subjectTelephoneTextField;
    private final JTextField subjectTradenameTextField;
    private final NumericTextField subjectFiscalNumberTextField;
    private final JTextField subjectCityTextField;
    private final JTable subjectsResultTable;
    private final DefaultTableModel subjectsResultDefaultTableModel;
    private final JScrollPane subjectsResultTableJScrollPane;
    private TableRowSorter trsfiltro;
    private final JButton selectButton;
    private final JButton cancelButton;
    private String currentSelectedCode = "";
    
    public SubjectSearch(SearchController searchController){
        this.searchController = searchController;
        
        subjectParamPanel = new JPanel();
        subjectParamPanel.setLayout(null);
        
        //FILA 1
        subjectCodeLabel = new JLabel("Codigo:");
        subjectCodeLabel.setBounds(30, 20, 80, 25);
        subjectParamPanel.add(subjectCodeLabel);
        
        subjectCodeTextField = new JTextField();
        subjectCodeTextField.setBounds(160, 20, 170, 25);
        subjectCodeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                subjectsResultTable.repaint();
                filter();
            }
        });
        subjectParamPanel.add(subjectCodeTextField);
        
        subjectAddressLabel = new JLabel("Dirección:");
        subjectAddressLabel.setBounds(370, 20, 160, 25);
        subjectParamPanel.add(subjectAddressLabel);
        
        subjectAddressTextField = new JTextField();
        subjectAddressTextField.setBounds(440, 20, 170, 25);
        subjectAddressTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                subjectsResultTable.repaint();
                filter();
            }
        });
        subjectParamPanel.add(subjectAddressTextField);
        
        //FILA 2
        subjectNameLabel = new JLabel("Razón Social:");
        subjectNameLabel.setBounds(30, 50, 100, 25);
        subjectParamPanel.add(subjectNameLabel);
        
        subjectNameTextField = new JTextField();
        subjectNameTextField.setBounds(160, 50, 170, 25);
        subjectNameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                subjectsResultTable.repaint();
                filter();
            }
        });
        subjectParamPanel.add(subjectNameTextField);
        
        subjectTelephoneLabel = new JLabel("Teléfono:");
        subjectTelephoneLabel.setBounds(370, 50, 100, 25);
        subjectParamPanel.add(subjectTelephoneLabel);
        
        subjectTelephoneTextField = new JTextField();
        subjectTelephoneTextField.setBounds(440, 50, 170, 25);
        subjectTelephoneTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                subjectsResultTable.repaint();
                filter();
            }
        });
        subjectParamPanel.add(subjectTelephoneTextField);
        
        //FILA 3
        subjectTradenameLabel = new JLabel("Nombre Comercial:");
        subjectTradenameLabel.setBounds(30, 80, 120, 25);
        subjectParamPanel.add(subjectTradenameLabel);
        
        subjectTradenameTextField = new JTextField();
        subjectTradenameTextField.setBounds(160, 80, 170, 25);
        subjectTradenameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                subjectsResultTable.repaint();
                filter();
            }
        });
        subjectParamPanel.add(subjectTradenameTextField);
        
        subjectFiscalNumberLabel = new JLabel("RUC / CI:");
        subjectFiscalNumberLabel.setBounds(370, 80, 120, 25);
        subjectParamPanel.add(subjectFiscalNumberLabel);
        
        DecimalFormat amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        subjectFiscalNumberTextField = new NumericTextField(20, amountFormat);
        subjectFiscalNumberTextField.setBounds(440, 80, 170, 25);
        subjectFiscalNumberTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                         subjectsResultTable.repaint();
                         filter();
                     }
                     
                     public void update(KeyEvent e){
                         String texto = subjectFiscalNumberTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                subjectFiscalNumberTextField.setValue(Double.valueOf(texto));
                            }
                         }
                     }
                 });

        subjectParamPanel.add(subjectFiscalNumberTextField);
        
        //FILA 4
        subjectCityLabel = new JLabel("Ciudad:");
        subjectCityLabel.setBounds(30, 110, 80, 25);
        subjectParamPanel.add(subjectCityLabel);
        
        subjectCityTextField = new JTextField();
        subjectCityTextField.setBounds(160, 110, 170, 25);
        subjectCityTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                subjectsResultTable.repaint();
                filter();
            }
        });
        subjectParamPanel.add(subjectCityTextField);
        
        listOfResultLabel = new JLabel("<HTML><U>Lista de Clientes</U></HTML>");
        listOfResultLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        listOfResultLabel.setBounds(400, 140, 200, 25);
        subjectParamPanel.add(listOfResultLabel);
        
        selectButton =  new JButton("Seleccionar");
        selectButton.setBounds(680, 420, 100, 25);
        selectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectButtonAction(e);
            }
        });
        subjectParamPanel.add(selectButton);
        
        cancelButton =  new JButton("Cancelar");
        cancelButton.setBounds(800, 420, 100, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        subjectParamPanel.add(cancelButton);
        
        //TABLA DE RESULTADOS
        subjectsResultDefaultTableModel = new subjectsSearchtResultDefaultTableModel();
        subjectsResultTable = new JTable(subjectsResultDefaultTableModel){
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
        
        searchController.setSubjectIndexResultsTable(subjectsResultDefaultTableModel, false, null, null, null, null, null, null, null);
        subjectsResultTable.setRowHeight(22);
                
        ButtonGroup radioButtonGroup = new ButtonGroup();
        for(int i = 0 ; i < subjectsResultTable.getRowCount(); i++){
            radioButtonGroup.add((JRadioButton) subjectsResultDefaultTableModel.getValueAt(i, 0));
        }
        
        //Code
        subjectsResultTable.getColumnModel().getColumn(0).setMaxWidth(90);
        subjectsResultTable.getColumnModel().getColumn(0).setMinWidth(90);
        subjectsResultTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        subjectsResultTable.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        subjectsResultTable.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor(new JCheckBox()));
        
        //Name
        subjectsResultTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        
        //Tradename
        subjectsResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Address
        subjectsResultTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        
        //City
        subjectsResultTable.getColumnModel().getColumn(4).setMaxWidth(100);
        subjectsResultTable.getColumnModel().getColumn(4).setMinWidth(100);
        subjectsResultTable.getColumnModel().getColumn(4).setResizable(false);
        
        //Telephone
        subjectsResultTable.getColumnModel().getColumn(5).setMaxWidth(100);
        subjectsResultTable.getColumnModel().getColumn(5).setMinWidth(100);
        subjectsResultTable.getColumnModel().getColumn(5).setResizable(false);
        
        //Fiscal Number
        subjectsResultTable.getColumnModel().getColumn(6).setMaxWidth(100);
        subjectsResultTable.getColumnModel().getColumn(6).setMinWidth(100);
        subjectsResultTable.getColumnModel().getColumn(6).setResizable(false);
        subjectsResultTable.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);
        
        //ID
        subjectsResultTable.getColumnModel().getColumn(7).setMaxWidth(0);
        subjectsResultTable.getColumnModel().getColumn(7).setMinWidth(0);
        subjectsResultTable.getColumnModel().getColumn(7).setPreferredWidth(0);
        
        //CODIGO PARA FILTRO
        subjectsResultTable.getColumnModel().getColumn(8).setMaxWidth(0);
        subjectsResultTable.getColumnModel().getColumn(8).setMinWidth(0);
        subjectsResultTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        
        trsfiltro = new TableRowSorter(subjectsResultDefaultTableModel);
        // Añadimos al Jtable el filtro trsfiltro
        
        subjectsResultTable.setRowSorter(trsfiltro);
        
        subjectsResultTableJScrollPane = new JScrollPane();
        subjectsResultTableJScrollPane.setBounds(30, 180, 900, 220);
        subjectsResultTableJScrollPane.setViewportView(subjectsResultTable);
        
        add(subjectsResultTableJScrollPane);
        add(subjectParamPanel);
        
        pack();
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Busqueda de Clientes");
        setBounds(110,90,1000,520);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(false);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        searchController.setClosedSubjectSearch();
        searchController.setSubjetcSelectedCode("");
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void cancelButtonAction(ActionEvent e) {
        searchController.setSubjetcSelectedCode("");
        doDefaultCloseAction();
    }
    
    private void selectButtonAction(ActionEvent e){
        if(currentSelectedCode.equals("")){
            JOptionPane optionPane = new JOptionPane("No ha seleccionado ningun Cliente", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            searchController.setSubjetcSelectedCode(currentSelectedCode);
            doDefaultCloseAction();
        }
    };
    
    private class subjectsSearchtResultDefaultTableModel extends DefaultTableModel{
        
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
    
    public void filter() {
        ArrayList<RowFilter<TableModel, Object>> andFilter = new ArrayList<>(1);
	RowFilter<TableModel, Object> rf;
        
        //Name
        rf = RowFilter.regexFilter("(?i)" + subjectNameTextField.getText(), 1);
        andFilter.add(rf);
        
        //TradeName
        rf = RowFilter.regexFilter("(?i)" + subjectTradenameTextField.getText(), 2);
        andFilter.add(rf);
        
        //Address
        rf = RowFilter.regexFilter("(?i)" + subjectAddressTextField.getText(), 3);
        andFilter.add(rf);
        
        //City
        rf = RowFilter.regexFilter("(?i)" + subjectCityTextField.getText(), 4);
        andFilter.add(rf);
        
        //Telephone
        rf = RowFilter.regexFilter("(?i)" + subjectTelephoneTextField.getText(), 5);
        andFilter.add(rf);
        
        //Fiscal Number
        rf = RowFilter.regexFilter("(?i)" + subjectFiscalNumberTextField.getText(), 6);
        andFilter.add(rf);
        
        //Code
        rf = RowFilter.regexFilter("(?i)" + subjectCodeTextField.getText(), 8);
        andFilter.add(rf);
        
        
	
	RowFilter<TableModel, Object> rowf = RowFilter.andFilter(andFilter);
	trsfiltro.setRowFilter(rowf);
        
        //trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+textFilter, column));
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
