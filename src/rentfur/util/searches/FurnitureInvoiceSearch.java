package rentfur.util.searches;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import rentfur.furniture.FurnitureController;
import rentfur.util.ComboBoxItem;
import sun.util.calendar.CalendarUtils;

/**
 *
 * @author FDuarte
 */
public class FurnitureInvoiceSearch extends JInternalFrame{
    private final SearchController searchController;
    
    private final JPanel furnitureParamPanel;
    
    private final JLabel furnitureCodeLabel;
    private final JLabel furnitureDescriptionLabel;
    private final JLabel furnitureFamilyLabel;
    private final JLabel listOfResultLabel;
    
    private final JTextField furnitureCodeTextField;
    private final JTextField furnitureDescriptionTextField;
    private final JComboBox familyComboBox;
    
    private final JTable furnituresResultTable;
    private final DefaultTableModel furnituresResultDefaultTableModel;
    private final JScrollPane furnituresResultTableJScrollPane;
    private final TableRowSorter trsfiltro;
    private final JButton selectButton;
    private final JButton cancelButton;
    private final HashMap currentSelectedIds = new HashMap();
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    public FurnitureInvoiceSearch(SearchController searchController, ArrayList eventDetailsAdded, ArrayList furnitureEventList, int eventId){
        this.searchController = searchController;
        
        furnitureParamPanel = new JPanel();
        furnitureParamPanel.setLayout(null);
        
        //FILA 1
        furnitureCodeLabel = new JLabel("Codigo:");
        furnitureCodeLabel.setBounds(30, 20, 80, 25);
        furnitureParamPanel.add(furnitureCodeLabel);
        
        furnitureCodeTextField = new JTextField();
        furnitureCodeTextField.setBounds(160, 20, 170, 25);
        furnitureCodeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                furnituresResultTable.repaint();
                filter();
            }
        });
        furnitureParamPanel.add(furnitureCodeTextField);
        
        //FILA 2
        furnitureDescriptionLabel = new JLabel("Descripción:");
        furnitureDescriptionLabel.setBounds(30, 50, 100, 25);
        furnitureParamPanel.add(furnitureDescriptionLabel);
        
        furnitureDescriptionTextField = new JTextField();
        furnitureDescriptionTextField.setBounds(160, 50, 170, 25);
        furnitureDescriptionTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                furnituresResultTable.repaint();
                filter();
            }
        });
        furnitureParamPanel.add(furnitureDescriptionTextField);
        
        furnitureFamilyLabel = new JLabel("Familia:");
        furnitureFamilyLabel.setBounds(30, 80, 100, 25);
        furnitureParamPanel.add(furnitureFamilyLabel);
        
        ComboBoxItem[] familiesComboBox = FurnitureController.getFurnitureFamiliesForComboBox(true);
        familyComboBox = new JComboBox(familiesComboBox);
        familyComboBox.setBounds(160, 80, 170, 25);
        familyComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
        furnitureParamPanel.add(familyComboBox);
        
        listOfResultLabel = new JLabel("<HTML><U>Lista de Mobiliarios</U></HTML>");
        listOfResultLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        listOfResultLabel.setBounds(400, 105, 200, 25);
        furnitureParamPanel.add(listOfResultLabel);
        
        selectButton =  new JButton("Seleccionar");
        selectButton.setBounds(680, 380, 100, 25);
        selectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectButtonAction(e);
            }
        });
        furnitureParamPanel.add(selectButton);
        
        cancelButton =  new JButton("Cancelar");
        cancelButton.setBounds(800, 380, 100, 25);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        furnitureParamPanel.add(cancelButton);
        
        //TABLA DE RESULTADOS
        furnituresResultDefaultTableModel = new furnituresSearchtResultDefaultTableModel();
        furnituresResultTable = new JTable(furnituresResultDefaultTableModel){
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
        
        searchController.setFurnitureInvoicedResultsTable(furnituresResultDefaultTableModel, false, eventDetailsAdded, furnitureEventList, eventId);
        furnituresResultTable.setRowHeight(22);
        
        //Code
        furnituresResultTable.getColumnModel().getColumn(0).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(0).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        furnituresResultTable.getColumnModel().getColumn(0).setCellRenderer(new RadioButtonRenderer());
        furnituresResultTable.getColumnModel().getColumn(0).setCellEditor(new RadioButtonEditor(new JCheckBox()));
        
//        //Name
//        furnituresResultTable.getColumnModel().getColumn(1).setMaxWidth(200);
        furnituresResultTable.getColumnModel().getColumn(1).setMinWidth(270);
        furnituresResultTable.getColumnModel().getColumn(1).setPreferredWidth(270);
        
        //Family
        furnituresResultTable.getColumnModel().getColumn(2).setMaxWidth(96);
        furnituresResultTable.getColumnModel().getColumn(2).setMinWidth(96);
        furnituresResultTable.getColumnModel().getColumn(2).setPreferredWidth(96);
        
        //TaxRate
        furnituresResultTable.getColumnModel().getColumn(3).setMaxWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setMinWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setCellRenderer(statusRenderer);
        
        //FineAmountPerUnit
        furnituresResultTable.getColumnModel().getColumn(4).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(4).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        furnituresResultTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        //StockDisponible
        furnituresResultTable.getColumnModel().getColumn(5).setHeaderRenderer(new AvailableSimpleHeaderRenderer());
        furnituresResultTable.getColumnModel().getColumn(5).setMaxWidth(130);
        furnituresResultTable.getColumnModel().getColumn(5).setMinWidth(130);
        furnituresResultTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        furnituresResultTable.getColumnModel().getColumn(5).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(5).setCellRenderer(new AvailableCellRenderer());
        
        //ID
        furnituresResultTable.getColumnModel().getColumn(6).setMaxWidth(0);
        furnituresResultTable.getColumnModel().getColumn(6).setMinWidth(0);
        furnituresResultTable.getColumnModel().getColumn(6).setPreferredWidth(0);
        
        //CODIGO PARA FILTRO
        furnituresResultTable.getColumnModel().getColumn(7).setMaxWidth(0);
        furnituresResultTable.getColumnModel().getColumn(7).setMinWidth(0);
        furnituresResultTable.getColumnModel().getColumn(7).setPreferredWidth(0);
        
        //ID DEL DETALLE DEL EVENTO
        furnituresResultTable.getColumnModel().getColumn(8).setMaxWidth(0);
        furnituresResultTable.getColumnModel().getColumn(8).setMinWidth(0);
        furnituresResultTable.getColumnModel().getColumn(8).setPreferredWidth(0);
        
        //SubTOTAL
        furnituresResultTable.getColumnModel().getColumn(9).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(9).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        furnituresResultTable.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);
        
        furnituresResultTableJScrollPane = new JScrollPane();
        furnituresResultTableJScrollPane.setBounds(30, 140, 900, 220);
        furnituresResultTableJScrollPane.setViewportView(furnituresResultTable);
        
        trsfiltro = new TableRowSorter(furnituresResultDefaultTableModel);
        // Añadimos al Jtable el filtro trsfiltro
        
        furnituresResultTable.setRowSorter(trsfiltro);
        
        add(furnituresResultTableJScrollPane);
        add(furnitureParamPanel);
        
        pack();
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Busqueda de Mobiliarios");
        setBounds(350,150,1000,460);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(false);
        
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        searchController.setClosedFurnitureInvoicedSearch();
        searchController.setFurnitureInvoicedIds(new HashMap());
    }
    
    private void cancelButtonAction(ActionEvent e) {
        searchController.setFurnitureInvoicedIds(new HashMap());
        doDefaultCloseAction();
    }
    
    private void selectButtonAction(ActionEvent e){
        if(currentSelectedIds.isEmpty()){
            JOptionPane optionPane = new JOptionPane("No ha seleccionado ningun Mobiliario", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
            JDialog dialog = optionPane.createDialog(this, "Atencion!");
            dialog.setVisible(true);
        }else{
            searchController.setFurnitureInvoicedIds(currentSelectedIds);
            doDefaultCloseAction();
        }
    };
    
    private void addItemToMap(String code, int row){
        try {
            HashMap itemValues = new HashMap();
            double taxRate = amountFormat.parse(furnituresResultDefaultTableModel.getValueAt(row, 3).toString()).doubleValue();
            double price = amountFormat.parse(furnituresResultDefaultTableModel.getValueAt(row, 4).toString()).doubleValue();
            double quantity = Integer.valueOf(furnituresResultDefaultTableModel.getValueAt(row, 5).toString());
            int eventDetailId = Integer.valueOf(furnituresResultDefaultTableModel.getValueAt(row, 8).toString());
            String description = furnituresResultDefaultTableModel.getValueAt(row, 1).toString();
            
            itemValues.put("code", code);
            itemValues.put("taxRate", taxRate);
            itemValues.put("price", price);
            itemValues.put("quantity", quantity);
            itemValues.put("eventDetailId", eventDetailId);
            itemValues.put("description", description);
            
            currentSelectedIds.put(eventDetailId, itemValues);
        } catch (ParseException ex) {
            Logger.getLogger(FurnitureInvoiceSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void removeItemFromMap(int row){
        try {
            int eventDetailId = Integer.valueOf(furnituresResultDefaultTableModel.getValueAt(row, 8).toString());
            currentSelectedIds.remove(eventDetailId);
        } catch (Throwable th) {
            Logger.getLogger(FurnitureInvoiceSearch.class.getName()).log(Level.SEVERE, null, th);
        }
    }
    
    private class furnituresSearchtResultDefaultTableModel extends DefaultTableModel{
        
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
        
        rf = RowFilter.regexFilter("(?i)" + furnitureDescriptionTextField.getText(), 1);
        andFilter.add(rf);
        
        ComboBoxItem family = (ComboBoxItem) familyComboBox.getSelectedItem();
        if(!family.getValue().equals("Todos")){
            rf = RowFilter.regexFilter("(?i)" + family.getValue(), 2);
            andFilter.add(rf);
        }
        
        rf = RowFilter.regexFilter("(?i)" + furnitureCodeTextField.getText(), 7);
        andFilter.add(rf);
        
	RowFilter<TableModel, Object> rowf = RowFilter.andFilter(andFilter);
        
	trsfiltro.setRowFilter(rowf);
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
        private JCheckBox button;
        private String code;
        private boolean isSelected;
        private int row;
        public RadioButtonEditor(JCheckBox checkBox) {
          super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
          if (value == null)
            return null;
          button = (JCheckBox) value;
          this.code = ((JCheckBox)value).getText();
          this.isSelected = ((JCheckBox)value).isSelected();
          button.addItemListener(this);
          this.row = row;
          return (Component) value;
        }

        @Override
        public Object getCellEditorValue() {
          button.removeItemListener(this);
            if(isSelected){
                removeItemFromMap( row);
            }else{
                addItemToMap(code, row);
            }
          return button;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
          super.fireEditingStopped();
        }
      }
      
      class AvailableCellRenderer extends JTextField implements TableCellRenderer {

        public AvailableCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Cantidad");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
      
    private class AvailableSimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public AvailableSimpleHeaderRenderer() {
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
    
}
