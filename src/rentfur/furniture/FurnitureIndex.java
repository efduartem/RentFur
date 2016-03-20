/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import rentfur.util.ComboBoxItem;

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
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JComboBox furnitureFamilyComboBox;
    private final ImageIcon searchFurnitureIconImage;
    private final JButton searchFurnitureButton;
    private final ImageIcon createIconImage;
    private final JButton createFurnitureButton;
    private final JButton createFurnitureFamilyButton;
    private final JTable furnituresResultTable;
    private final DefaultTableModel furnituresResultDefaultTableModel;
    private final JScrollPane furnituresResultTableJScrollPane;
    
    
    public FurnitureIndex(FurnitureController furnitureController){
        this.furnitureController = furnitureController;
        
        furnitureIndexParamsPanel = new JPanel();
        furnitureIndexParamsPanel.setLayout(null);
        //furnitureIndexResultPanel = new JPanel(new BorderLayout());
        
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 50, 80, 25);
        furnitureIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(110, 50, 160, 25);
        furnitureIndexParamsPanel.add(codeTextField);

        descriptionLabel = new JLabel("Descripcion:");
        descriptionLabel.setBounds(30, 80, 80, 25);
        furnitureIndexParamsPanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField();
        descriptionTextField.setBounds(110, 80, 160, 25);
        furnitureIndexParamsPanel.add(descriptionTextField);
        
        furnitureFamilyLabel = new JLabel("Familia:");
        furnitureFamilyLabel.setBounds(30, 110, 80, 25);
        furnitureIndexParamsPanel.add(furnitureFamilyLabel);
        
        ComboBoxItem[] furnitureFamilies = furnitureController.getFurnitureFamiliesForComboBox();
        furnitureFamilyComboBox = new JComboBox(furnitureFamilies);
        furnitureFamilyComboBox.setBounds(110, 110, 160, 25);
        furnitureIndexParamsPanel.add(furnitureFamilyComboBox);
        
        //BOTON DE BUSQUEDA
        searchFurnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchFurnitureButton = new JButton("  Buscar", searchFurnitureIconImage);
        searchFurnitureButton.setBounds(30, 160, 120, 32);
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
        createFurnitureButton.setBounds(160, 160, 180, 32);
        createFurnitureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFurnitureCreate();
            }
        });
        furnitureIndexParamsPanel.add(createFurnitureButton);
        
        //
        createFurnitureFamilyButton = new JButton("  Crear Familia de Mobiliarios", createIconImage);
        createFurnitureFamilyButton.setBounds(350, 160, 230, 32);
        createFurnitureFamilyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFurnitureFamilyCreate();
            }
        });
        furnitureIndexParamsPanel.add(createFurnitureFamilyButton);
        
        //TABLA DE RESULTADOS
        furnituresResultDefaultTableModel = new furnituresIndextResultDefaultTableModel();
        furnituresResultTable = new JTable(furnituresResultDefaultTableModel);
        
        furnitureController.setFurnitureIndexResultsTable(furnituresResultDefaultTableModel, false, null, null, null);
        furnituresResultTableJScrollPane = new JScrollPane();
        furnituresResultTableJScrollPane.setBounds(30, 210, 500, 250);
        furnituresResultTableJScrollPane.setViewportView(furnituresResultTable);
        
        add(furnituresResultTableJScrollPane);
        //container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        add(furnitureIndexParamsPanel);
        //container.add(furnitureIndexResultPanel);
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Mobiliarios");
        setBounds(200,100,900,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    private void searchFurnitureButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String description = descriptionTextField.getText();
        ComboBoxItem family = (ComboBoxItem) furnitureFamilyComboBox.getSelectedItem();
        String familyId = "";
        if(family!=null){
            familyId = family.getKey();
        }
        furnitureController.setFurnitureIndexResultsTable(furnituresResultDefaultTableModel, true, code, description, familyId);
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
                                                        //case 1:     return true;
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
    }
    
    public void setEnableddElements(){
        codeTextField.setEditable(true);
        descriptionTextField.setEditable(true);
        furnitureFamilyComboBox.setEnabled(true);
        createFurnitureButton.setEnabled(true);
        searchFurnitureButton.setEnabled(true);
        createFurnitureFamilyButton.setEnabled(true);
        this.setClosable(true);
        furnituresResultTable.setEnabled(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        closeIndexView(null);
    }
    
    private void closeIndexView(ActionEvent e) {
        this.dispose();
        furnitureController.indexViewClosed();
    }
}
