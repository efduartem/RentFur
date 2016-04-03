/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.position;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import rentfur.user.UserIndex;
import rentfur.util.ComboBoxItem;
import rentfur.util.RoleEntry;

/**
 *
 * @author FDuarte
 */
public class PositionIndex extends JInternalFrame{
    private final PositionController positionController;
    private final JPanel positionIndexParamsPanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JLabel roleLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JComboBox roleComboBox;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
    private final ImageIcon createIconImage;
    private final JButton createPositionButton;
    private final JTable positionsResultTable;
    private DefaultTableModel positionsResultDefaultTableModel;
    private final JScrollPane positionsResultTableJScrollPane;
    
    public PositionIndex(PositionController positionController){
        this.positionController = positionController;
        
        positionIndexParamsPanel = new JPanel();
        positionIndexParamsPanel.setLayout(null);
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        positionIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(170, 50, 160, 25);
        positionIndexParamsPanel.add(codeTextField);

        descriptionLabel = new JLabel("Descripción:");
        descriptionLabel.setBounds(30, 80, 120, 25);
        positionIndexParamsPanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField();
        descriptionTextField.setBounds(170, 80, 160, 25);
        positionIndexParamsPanel.add(descriptionTextField);
        
        roleLabel = new JLabel("Permiso:");
        roleLabel.setBounds(30, 110, 120, 25);
        positionIndexParamsPanel.add(roleLabel);
        
        ComboBoxItem[] rolesComboBox = positionController.getRoleForComboBox(true);
        roleComboBox = new JComboBox(rolesComboBox);
        roleComboBox.setBounds(170, 110, 160, 25);
        positionIndexParamsPanel.add(roleComboBox);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Buscar", searchIconImage);
        searchButton.setBounds(40, 150, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPositionButtonAction(e);
            }
        });
        positionIndexParamsPanel.add(searchButton);
        
        //BOTON PARA CREAR MOBILIARIO
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        createPositionButton = new JButton("  Crear Cargo", createIconImage);
        createPositionButton.setBounds(180, 150, 180, 32);
        createPositionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPositionCreate();
            }
        });
        positionIndexParamsPanel.add(createPositionButton);
        
        //TABLA DE RESULTADOS
        positionsResultDefaultTableModel = new positionsIndextResultDefaultTableModel();
        positionsResultTable = new JTable(positionsResultDefaultTableModel);
        
        positionController.setPositionIndexResultsTable(positionsResultDefaultTableModel, false, null, null, null);
        updateRowHeights(positionsResultTable, positionsResultDefaultTableModel);
        
        //Formato para celda centrada
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        //ID
        positionsResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        positionsResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        positionsResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Code
        positionsResultTable.getColumnModel().getColumn(1).setMaxWidth(90);
        positionsResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        
        //Description
        positionsResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //ROLES
        positionsResultTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        positionsResultTable.getColumnModel().getColumn(3).setCellRenderer(new TableListRenderer());
        
        positionsResultTable.getColumnModel().getColumn(4).setMaxWidth(90);
        positionsResultTable.getColumnModel().getColumn(4).setMinWidth(90);
        positionsResultTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        positionsResultTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        positionsResultTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JTextField()));
        
        positionsResultTableJScrollPane = new JScrollPane();
        positionsResultTableJScrollPane.setBounds(30, 200, 1000, 310);
        positionsResultTableJScrollPane.setViewportView(positionsResultTable);
        
        add(positionsResultTableJScrollPane); 
        add(positionIndexParamsPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Cargos");
        setBounds(100,50,1100,650);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        closeIndexView(null);
    }
    
    private void closeIndexView(ActionEvent e) {
        this.dispose();
        positionController.indexViewClosed();
    }
    
    public void getPositionCreate(){
        positionController.getPositionCreateView();
    }
    
    public void setDisabledElements(){
        codeTextField.setEditable(false);
        descriptionTextField.setEditable(false);
        roleComboBox.setEnabled(false);
        createPositionButton.setEnabled(false);
        searchButton.setEnabled(false);
        this.setClosable(false);
        positionsResultTable.setEnabled(false);
    }
    
    public void setEnableddElements(){
        codeTextField.setEditable(true);
        descriptionTextField.setEditable(true); 
        roleComboBox.setEnabled(true);
        createPositionButton.setEnabled(true);
        searchButton.setEnabled(true);
        this.setClosable(true);
        positionsResultTable.setEnabled(true);
    }
    
    private class positionsIndextResultDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 4:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
          if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
          } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
          }
          setText((value == null) ? "" : value.toString());
          return this;
        }
      }

      /**
       * @version 1.0 11/09/98
       */

     class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int row;

        private int column;
        
        public ButtonEditor(JTextField jtf) {
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
          if (isPushed) {
              showUserShowAndEditView(row);
          }
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
        }
      }
    
    class TableListRenderer implements TableCellRenderer
    {
        JList list;
        int cellHeight;

        public TableListRenderer()
        {
            list = new JList();
            cellHeight = 22;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column)
        {
            
            list.setListData((RoleEntry[])value);
            list.setCellRenderer(new BookCellRenderer());
            list.setFixedCellHeight(cellHeight);
            list.setVisibleRowCount(((RoleEntry[])value).length);
            return list;
        }
    }

    class BookCellRenderer extends JLabel implements ListCellRenderer {
      private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

      public BookCellRenderer() {
        setOpaque(true);
        setIconTextGap(-1);
      }

      public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
          RoleEntry entry = (RoleEntry) value;
          setText(entry.getName());
          setIcon(entry.getImage());
          if (isSelected) {
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
          } else {
            setBackground(Color.white);
            setForeground(Color.black);
          }
          return this;
        }
      }
    
    
    private void updateRowHeights(JTable positionsResultTable, DefaultTableModel positionsResultDefaultTableModel){
        Vector dataVector;
        int rowHeight;
        RoleEntry[] roles;
        for (int row = 0; row < positionsResultTable.getRowCount(); row++)
        {
            dataVector = (Vector) positionsResultDefaultTableModel.getDataVector().get(row);
            roles = (RoleEntry[]) dataVector.get(3);
            
            if(roles.length==0){
                rowHeight = 22;
            }else{
                rowHeight = (roles.length * 22);
            }
            
            positionsResultTable.setRowHeight(row, rowHeight);
        }
    }
    
    public void searchPositionButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String description = descriptionTextField.getText();
        ComboBoxItem role = (ComboBoxItem) roleComboBox.getSelectedItem();
        String roleId = "";
        if(role!=null){
            roleId = role.getKey();
        }
        positionController.setPositionIndexResultsTable(positionsResultDefaultTableModel, true, code, description, roleId);
        updateRowHeights(positionsResultTable, positionsResultDefaultTableModel);

    }
    
    public void showUserShowAndEditView(int row){
        Vector dataVector = (Vector) positionsResultDefaultTableModel.getDataVector().get(row);
        int positionId = (Integer) dataVector.get(0);
        positionController.getPositionShowAndEditView(positionId);
    }
}
