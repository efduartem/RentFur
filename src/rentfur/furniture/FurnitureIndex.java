/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.java.balloontip.BalloonTip;
import rentfur.position.PositionController;
import rentfur.util.ComboBoxItem;
import rentfur.report.HechaukaReport;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class FurnitureIndex extends JInternalFrame{
    private final FurnitureController furnitureController;
    private final JPanel furnitureIndexParamsPanel;
    //private final JPanel furnitureIndexResultPanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JLabel furnitureFamilyLabel;
    private final JLabel statusLabel;
    private final JLabel taxRateLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JComboBox furnitureFamilyComboBox;
    private final JComboBox furnitureStatusComboBox;
    private final JComboBox taxRateComboBox;
    private final ImageIcon searchFurnitureIconImage;
    private final JButton searchFurnitureButton;
    private final ImageIcon createIconImage;
    private final JButton createFurnitureButton;
    private final JButton createFurnitureFamilyButton;
    private final JTable furnituresResultTable;
    private final DefaultTableModel furnituresResultDefaultTableModel;
    private final JScrollPane furnituresResultTableJScrollPane;
    private boolean onlyQuery = false;
    private final ImageIcon helpIconImage;
    private final JLabel helpLabel;
    private BalloonTip myBalloonTip;
    
    public FurnitureIndex(FurnitureController furnitureController, UserRoles userRoles){
        this.furnitureController = furnitureController;
        
        if((Boolean)userRoles.getRolesMap().get(PositionController.ROLE_RF_FURNITURE)){
            onlyQuery = true;
        }
        
        furnitureIndexParamsPanel = new JPanel();
        furnitureIndexParamsPanel.setLayout(null);
        
//        final Rectangle customOffset = new Rectangle(30, 50, 300, 200);
//                        final JComponent customComponent = new JComponent() {
//                                protected void paintComponent(Graphics g) {
//                                        Rectangle clip = g.getClipBounds();
//                                        g.setColor(Color.WHITE);
//                                        g.fillRect(clip.x, clip.y, clip.width, clip.height);
//                                        g.setColor(Color.BLUE);
//                                        g.fillRect(customOffset.x, customOffset.y, customOffset.width, customOffset.height);
//                                }
//                        };
//                        customComponent.setPreferredSize(new Dimension(1280, 640));
//
//                        // Now create the CustomBalloonTip, such that it attaches itself to the blue rectangle
//                        CustomBalloonTip customBalloon = new CustomBalloonTip(customComponent, 
//                                new JLabel("<HTML><U>Datos del Cliente</U><br><B>Datos del Cliente</B><br><ul><li>Uno</li><li>Dos</li></ul></HTML>"),
//                                customOffset,
//                                new EdgedBalloonStyle(Color.WHITE, Color.BLACK), 
//                                BalloonTip.Orientation.LEFT_ABOVE, 
//                                BalloonTip.AttachLocation.ALIGNED, 
//                                20, 20, 
//                                false);
//        customComponent.setBounds(500, 50, 80, 25);
//        customComponent.setVisible(true);
//        furnitureIndexParamsPanel.add(customComponent);
        
        helpIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/help_24x24.png"));
        helpLabel = new JLabel("AYUDA");
        helpLabel.setIcon(helpIconImage);
        helpLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
//                        System.out.println("MOSTRAR GLOBO");
                        showHelp();
                    }
        });
        helpLabel.setBounds(400, 50, 80, 25);
        furnitureIndexParamsPanel.add(helpLabel);
        
        //getHechaukaReport
        //HechaukaReport.getHechaukaReport();
        
        String imgsrc = getClass().getResource("/rentfur/button/image/util/search_24x24.png").toString();
        
        myBalloonTip = new BalloonTip(helpLabel, "<HTML><U>Datos del Cliente</U><br><B>Datos del Cliente</B><br>"
                + "<img src='"+imgsrc+"'><br><li>Uno</li><li>Dos</li></HTML>");
        myBalloonTip.setVisible(false);
        myBalloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false);
//        furnitureIndexResultPanel = new JPanel(new BorderLayout());
//        furnitureIndexParamsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 1, true), "Busqueda de Mobiliarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        
        
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        furnitureIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(150, 50, 160, 25);
        furnitureIndexParamsPanel.add(codeTextField);

        descriptionLabel = new JLabel("Descripcion:");
        descriptionLabel.setBounds(30, 80, 80, 25);
        furnitureIndexParamsPanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField();
        descriptionTextField.setBounds(150, 80, 160, 25);
        furnitureIndexParamsPanel.add(descriptionTextField);
        
        furnitureFamilyLabel = new JLabel("Familia:");
        furnitureFamilyLabel.setBounds(30, 110, 80, 25);
        furnitureIndexParamsPanel.add(furnitureFamilyLabel);
        
        ComboBoxItem[] furnitureFamilies = furnitureController.getFurnitureFamiliesForComboBox(true);
        furnitureFamilyComboBox = new JComboBox(furnitureFamilies);
        furnitureFamilyComboBox.setBounds(150, 110, 160, 25);
        furnitureIndexParamsPanel.add(furnitureFamilyComboBox);
        
        taxRateLabel = new JLabel("Tasa de impuesto:");
        taxRateLabel.setBounds(30, 140, 130, 25);
        furnitureIndexParamsPanel.add(taxRateLabel);
        
        ComboBoxItem[] taxRate = FurnitureController.getFurnitureTaxRatesForComboBox(true);
        taxRateComboBox = new JComboBox(taxRate);
        taxRateComboBox.setBounds(150, 140, 160, 25);
        furnitureIndexParamsPanel.add(taxRateComboBox);
        
        statusLabel = new JLabel("Estado:");
        statusLabel.setBounds(30, 170, 80, 25);
        furnitureIndexParamsPanel.add(statusLabel);
        
        ComboBoxItem[] furnitureStatus = furnitureController.getFurnitureStatusForComboBox();
        furnitureStatusComboBox = new JComboBox(furnitureStatus);
        furnitureStatusComboBox.setBounds(150, 170, 160, 25);
        furnitureIndexParamsPanel.add(furnitureStatusComboBox);
        
        //BOTON DE BUSQUEDA
        searchFurnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchFurnitureButton = new JButton("  Buscar", searchFurnitureIconImage);
        searchFurnitureButton.setBounds(30, 220, 120, 32);
        searchFurnitureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFurnitureButtonAction(e);
            }
        });
        furnitureIndexParamsPanel.add(searchFurnitureButton);
        
        //BOTON PARA CREAR MOBILIARIO
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        createFurnitureButton = new JButton("  Crear Mobiliario", createIconImage);
        createFurnitureButton.setBounds(160, 220, 180, 32);
        createFurnitureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFurnitureCreate();
            }
        });
        
        furnitureIndexParamsPanel.add(createFurnitureButton);
        
        //BOTON PARA CREAR FAMILIA DE MOBILIARIOS
        createFurnitureFamilyButton = new JButton("  Crear Familia de Mobiliarios", createIconImage);
        createFurnitureFamilyButton.setBounds(350, 220, 230, 32);
        createFurnitureFamilyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFurnitureFamilyCreate();
            }
        });
        furnitureIndexParamsPanel.add(createFurnitureFamilyButton);
        
        if(onlyQuery){
            String message = "Su usuario solo cuenta con permiso de consultas";
            createFurnitureButton.setEnabled(false);
            createFurnitureFamilyButton.setEnabled(false);
            createFurnitureButton.setToolTipText(message);
            createFurnitureFamilyButton.setToolTipText(message);
        }
        
        //TABLA DE RESULTADOS
        furnituresResultDefaultTableModel = new furnituresIndextResultDefaultTableModel();
        furnituresResultTable = new JTable(furnituresResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda inactiva
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        //statusInactiveRenderer.setBackground(new Color(16751001));
        
        //Formato para celda activa
        //DefaultTableCellRenderer statusActiveRenderer = new DefaultTableCellRenderer();
        //statusActiveRenderer.setHorizontalAlignment(JLabel.CENTER);
        //statusActiveRenderer.setBackground(new Color(13434828));
        
        furnitureController.setFurnitureIndexResultsTable(furnituresResultDefaultTableModel, false, null, null, null, null, null);
        furnituresResultTable.setRowHeight(22);
        //ID
        furnituresResultTable.getColumnModel().getColumn(0).setMaxWidth(0);
        furnituresResultTable.getColumnModel().getColumn(0).setMinWidth(0);
        furnituresResultTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Code
        furnituresResultTable.getColumnModel().getColumn(1).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(1).setMinWidth(90);
        
        //Description
        furnituresResultTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Familia
        furnituresResultTable.getColumnModel().getColumn(3).setMaxWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setMinWidth(120);
        furnituresResultTable.getColumnModel().getColumn(3).setResizable(false);
        
        //TaxRate
        furnituresResultTable.getColumnModel().getColumn(4).setMaxWidth(120);
        furnituresResultTable.getColumnModel().getColumn(4).setMinWidth(120);
        furnituresResultTable.getColumnModel().getColumn(4).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(4).setCellRenderer(statusRenderer);
        
        //UnitPrice
        furnituresResultTable.getColumnModel().getColumn(5).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(5).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(5).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        //UnitCostPrice
        furnituresResultTable.getColumnModel().getColumn(6).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(6).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(6).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        
        //FineAmount
        furnituresResultTable.getColumnModel().getColumn(7).setMaxWidth(100);
        furnituresResultTable.getColumnModel().getColumn(7).setMinWidth(100);
        furnituresResultTable.getColumnModel().getColumn(7).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        
        //Stock Total
        furnituresResultTable.getColumnModel().getColumn(8).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(8).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(8).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        
        furnituresResultTable.getColumnModel().getColumn(9).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(9).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(9).setResizable(false);
        furnituresResultTable.getColumnModel().getColumn(9).setCellRenderer(statusRenderer);
        
        furnituresResultTable.getColumnModel().getColumn(10).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(10).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(10).setPreferredWidth(90);
        furnituresResultTable.getColumnModel().getColumn(10).setCellRenderer(new ButtonRenderer());
        furnituresResultTable.getColumnModel().getColumn(10).setCellEditor(new ButtonEditor(new JTextField()));
        
        furnituresResultTable.getColumnModel().getColumn(11).setMaxWidth(90);
        furnituresResultTable.getColumnModel().getColumn(11).setMinWidth(90);
        furnituresResultTable.getColumnModel().getColumn(11).setPreferredWidth(90);
        furnituresResultTable.getColumnModel().getColumn(11).setCellRenderer(new ButtonRenderer());
        furnituresResultTable.getColumnModel().getColumn(11).setCellEditor(new ButtonEditor(new JTextField()));
        
        //Status
        furnituresResultTable.getColumnModel().getColumn(12).setMaxWidth(0);
        furnituresResultTable.getColumnModel().getColumn(12).setMinWidth(0);
        furnituresResultTable.getColumnModel().getColumn(12).setPreferredWidth(0);
        
        furnituresResultTableJScrollPane = new JScrollPane();
        furnituresResultTableJScrollPane.setBounds(30, 280, 1300, 300);
        furnituresResultTableJScrollPane.setViewportView(furnituresResultTable);
        
        add(furnituresResultTableJScrollPane);
        add(furnitureIndexParamsPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Mobiliarios");
        setBounds(100,50,1400,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    public void updateFurnitureFamilyComboBox(){
        ComboBoxItem[] furnitureFamilies = furnitureController.getFurnitureFamiliesForComboBox(true);
        furnitureFamilyComboBox.removeAllItems();
        for (ComboBoxItem furnitureFamilie : furnitureFamilies) {
            furnitureFamilyComboBox.addItem(furnitureFamilie);
        }
    }
    
    public void showHelp(){
        if(!myBalloonTip.isVisible()){
            myBalloonTip.setVisible(true);
        }
    }
    
    public void searchFurnitureButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String description = descriptionTextField.getText();
        ComboBoxItem family = (ComboBoxItem) furnitureFamilyComboBox.getSelectedItem();
        String familyId = "";
        if(family!=null){
            familyId = family.getKey();
        }
        
        ComboBoxItem status = (ComboBoxItem) furnitureStatusComboBox.getSelectedItem();
        String furnitureStatus = "";
        if(status!=null){
            furnitureStatus = status.getKey();
        }
        
        String taxRate = "";
        ComboBoxItem taxRateSelected = (ComboBoxItem) taxRateComboBox.getSelectedItem();
        if(taxRateSelected != null){
            taxRate = taxRateSelected.getKey();
        }
        
        furnitureController.setFurnitureIndexResultsTable(furnituresResultDefaultTableModel, true, code, description, familyId, furnitureStatus, taxRate);
        /*HashMap mapReturn = furnitureController.saveFurnitureFamily(code, description);
        if((Integer) mapReturn.get("status") == furnitureFamilyController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
        }else if((Integer)mapReturn.get("status") == furnitureFamilyController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }*/
    }
    
    private class furnituresIndextResultDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 10:     return true;
                                                        case 11:    return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    public void getFurnitureCreate(){
        furnitureController.getFurnitureCreateView();
    }
    
    public void getFurnitureFamilyCreate(){
        furnitureController.getFurnitureFamilyCreateView();
    }
    
    public void setDisabledElements(){
        codeTextField.setEditable(false);
        descriptionTextField.setEditable(false);
        furnitureFamilyComboBox.setEnabled(false);
        createFurnitureButton.setEnabled(false);
        searchFurnitureButton.setEnabled(false);
        createFurnitureFamilyButton.setEnabled(false);
        this.setClosable(false);
        furnituresResultTable.setEnabled(false);
        furnitureStatusComboBox.setEnabled(false);
    }
    
    public void setEnableddElements(){
        if(!onlyQuery){
            createFurnitureButton.setEnabled(true);
            createFurnitureFamilyButton.setEnabled(true);
        }
        codeTextField.setEditable(true);
        descriptionTextField.setEditable(true);
        furnitureFamilyComboBox.setEnabled(true);
        searchFurnitureButton.setEnabled(true);        
        this.setClosable(true);
        furnituresResultTable.setEnabled(true);
        furnitureStatusComboBox.setEnabled(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        closeIndexView(null);
    }
    
    private void closeIndexView(ActionEvent e) {
        this.dispose();
        furnitureController.indexViewClosed();
    }
    
    public String updateFurnitureStatus(int row, String label){
        Vector dataVector = (Vector) furnituresResultDefaultTableModel.getDataVector().get(row);
        int furnitureId = (Integer) dataVector.get(0);
        String code = (String) dataVector.get(1);
        String description = (String) dataVector.get(2);
        boolean active = (boolean) dataVector.get(12);
        String valueToReturn = label;
        int respuesta;
        HashMap mapReturn;
        if(active){
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea inactivar el mobiliario {0} - {1}?", code, description),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = furnitureController.updateFurnitureStatus(furnitureId, active);
                if((Integer) mapReturn.get("status") == furnitureController.SUCCESFULLY_SAVED){
                    furnitureController.searchFurnitureButtonAction();
                    valueToReturn = "Activar";
                }else if((Integer)mapReturn.get("status") == furnitureController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            respuesta = JOptionPane.showConfirmDialog(this, MessageFormat.format("Confirma que desea activar el mobiliario {0} - {1}?", code, description),"Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(respuesta == JOptionPane.YES_OPTION){
                mapReturn = furnitureController.updateFurnitureStatus(furnitureId, active);
                if((Integer) mapReturn.get("status") == furnitureController.SUCCESFULLY_SAVED){
                    furnitureController.searchFurnitureButtonAction();
                    valueToReturn = "Inactivar";
                }else if((Integer)mapReturn.get("status") == furnitureController.ERROR_IN_SAVED){
                    JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
                } 
            }
        }
        
        return valueToReturn;
    }
    
    public void showFurnitureShowAndEditView(int row){
        Vector dataVector = (Vector) furnituresResultDefaultTableModel.getDataVector().get(row);
        int furnitureId = (Integer) dataVector.get(0);
        furnitureController.getFurnitureShowAndEditView(furnitureId);
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
          if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
          } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
          }
          if(column==10){
              if(onlyQuery){
                    String message = "Su usuario solo cuenta con permiso de consultas";
                    setEnabled(false);
                    setToolTipText(message);
                }
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
                if(column==10){
                    if(!onlyQuery){
                        label = updateFurnitureStatus(row, label);
                    }
                }else if(column==11){
                    showFurnitureShowAndEditView(row);
                }
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
}
