/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import rentfur.util.ComboBoxItem;
import rentfur.util.NumericTextField;

/**
 *
 * @author FDuarte
 */
public class FurnitureShowAndEdit extends JInternalFrame{
    
    private final FurnitureController furnitureController;
    private final JPanel furnitureShowAndEditPanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JLabel familyLabel;
    private final JLabel unitPriceLabel;
    private final JLabel unitCostPriceLabel;
    private final JLabel fineAmountPerUnitLabel;
    private final JLabel activeLabel;
    private final JLabel observationLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JComboBox familyComboBox;
    private final NumericTextField unitPriceTextField;
    private final NumericTextField unitCostPriceTextField;
    private final NumericTextField fineAmountPerUnitTextField;
    private final JCheckBox activeCheckBox;
    private final JTextArea observationTextArea;
    private final ImageIcon saveIconImage;
    private final ImageIcon editIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton saveButton;
    private final JButton editButton;
    private final JButton cancelButton;
    private DecimalFormat amountFormat;
    
    public FurnitureShowAndEdit(FurnitureController furnitureController, final int furnitureId){
        this.furnitureController = furnitureController;
        
        HashMap furnituteMap = furnitureController.getFurnitureById(furnitureId);
        
        furnitureShowAndEditPanel = new JPanel();
        
        furnitureShowAndEditPanel.setLayout(null);
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(50,20, 100, 25);
        furnitureShowAndEditPanel.add(codeLabel);
        
        codeTextField = new JTextField(String.valueOf(furnituteMap.get("code")));
        codeTextField.setBounds(180, 20, 160, 25);
        codeTextField.setEditable(false);
        furnitureShowAndEditPanel.add(codeTextField);
        
        descriptionLabel = new JLabel("Descripcion:");
        descriptionLabel.setBounds(50,50, 100, 25);
        furnitureShowAndEditPanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField(String.valueOf(furnituteMap.get("description")));
        descriptionTextField.setBounds(180, 50, 160, 25);
        descriptionTextField.setEditable(false);
        furnitureShowAndEditPanel.add(descriptionTextField);
        
        familyLabel = new JLabel("Familia:");
        familyLabel.setBounds(50,80, 100, 25);
        furnitureShowAndEditPanel.add(familyLabel);
        
        ComboBoxItem[] familiesComboBox = furnitureController.getFurnitureFamiliesForComboBox(false);
        
        ComboBoxItem familiesComboBoxItem = null;
        for (ComboBoxItem familiesComboBoxFor : familiesComboBox) {
            familiesComboBoxItem = familiesComboBoxFor;
            if(familiesComboBoxItem.getKey().equals(furnituteMap.get("furnitureFamilyId").toString())){
                break;
            }
        }
       
        familyComboBox = new JComboBox(familiesComboBox);
        familyComboBox.setSelectedItem(familiesComboBoxItem);
        familyComboBox.setEditable(false);
        familyComboBox.setEnabled(false);
        familyComboBox.setBounds(180, 80, 160, 25);
        furnitureShowAndEditPanel.add(familyComboBox);
        
        unitPriceLabel = new JLabel("Precio Unitario:");
        unitPriceLabel.setBounds(50,110, 100, 25);
        furnitureShowAndEditPanel.add(unitPriceLabel);
        
        amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        unitPriceTextField = new NumericTextField(amountFormat.format(furnituteMap.get("unitPrice")), 20, amountFormat);
        unitPriceTextField.setEditable(false);
        unitPriceTextField.setBounds(180, 110, 160, 25);
        unitPriceTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                     }
                     
                     public void update(KeyEvent e){
                         String texto = unitPriceTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                unitPriceTextField.setValue(Double.valueOf(texto));
                            }
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });

        furnitureShowAndEditPanel.add(unitPriceTextField);
        
        unitCostPriceLabel = new JLabel("Costo Unitario:");
        unitCostPriceLabel.setBounds(50, 140, 100, 25);
        furnitureShowAndEditPanel.add(unitCostPriceLabel);
        
        unitCostPriceTextField = new NumericTextField(amountFormat.format(furnituteMap.get("unitCostPrice")), 20, amountFormat);
        unitCostPriceTextField.setEditable(false);
        unitCostPriceTextField.setBounds(180, 140, 160, 25);
        unitCostPriceTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                     }
                     
                     public void update(KeyEvent e){
                         String texto = unitCostPriceTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                unitCostPriceTextField.setValue(Double.valueOf(texto));
                            }
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });
        furnitureShowAndEditPanel.add(unitCostPriceTextField);
        
        fineAmountPerUnitLabel = new JLabel("Monto multa:");
        fineAmountPerUnitLabel.setBounds(50,170, 80, 25);
        furnitureShowAndEditPanel.add(fineAmountPerUnitLabel);
        
        fineAmountPerUnitTextField = new NumericTextField(amountFormat.format(furnituteMap.get("fineAmountPerUnit")), 20, amountFormat);
        fineAmountPerUnitTextField.setEditable(false);
        fineAmountPerUnitTextField.setBounds(180, 170, 160, 25);
        fineAmountPerUnitTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                     }
                     
                     public void update(KeyEvent e){
                         String texto = fineAmountPerUnitTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                fineAmountPerUnitTextField.setValue(Double.valueOf(texto));
                            }
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });
        furnitureShowAndEditPanel.add(fineAmountPerUnitTextField);
        
        activeLabel = new JLabel("Activo:");
        activeLabel.setBounds(50,200, 80, 25);
        furnitureShowAndEditPanel.add(activeLabel);
        
        activeCheckBox = new JCheckBox("", Boolean.valueOf(furnituteMap.get("active").toString()));
        activeCheckBox.setEnabled(false);
        activeCheckBox.setBounds(178,200, 80, 25);
        furnitureShowAndEditPanel.add(activeCheckBox);        
        
        observationLabel = new JLabel("Observacion:");
        observationLabel.setBounds(50,230, 80, 25);
        furnitureShowAndEditPanel.add(observationLabel);
        
        observationTextArea = new JTextArea(furnituteMap.get("observation").toString(),0,0);
        observationTextArea.setLineWrap(true);
        observationTextArea.setWrapStyleWord(true);
        observationTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(180, 230, 160, 90);
        scrollPane.setViewportView(observationTextArea);
        furnitureShowAndEditPanel.add(scrollPane);
        
        saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
        saveButton.setBounds(60, 340, 120, 32);
        saveButton.setVisible(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e, furnitureId);
            }
        });
        furnitureShowAndEditPanel.add(saveButton);
        
        editIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/edit_24x24.png"));
        editButton = new JButton("     Editar", editIconImage);
        editButton.setBounds(110, 340, 200, 35);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enabledEditActions(e);
            }
        });
        furnitureShowAndEditPanel.add(editButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(200, 340, 120, 32);
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        furnitureShowAndEditPanel.add(cancelButton);
        
        add(furnitureShowAndEditPanel);
        
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Detalles ["+furnituteMap.get("code").toString()+" - "+furnituteMap.get("description").toString()+"]");
        setBounds(400,150,460,450);
        //pack();
        setVisible(true);
    }
    
    private void cancelButtonAction(ActionEvent e) {
        descriptionTextField.setEditable(false);
        familyComboBox.setEnabled(false);
        unitPriceTextField.setEditable(false);
        unitCostPriceTextField.setEditable(false);
        fineAmountPerUnitTextField.setEditable(false);
        activeCheckBox.setEnabled(false);
        observationTextArea.setEditable(false);
        
         //Botones
        editButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
    }
    
    public void enabledEditActions(ActionEvent e){
        descriptionTextField.setEditable(true);
        familyComboBox.setEnabled(true);
        unitPriceTextField.setEditable(true);
        unitCostPriceTextField.setEditable(true);
        fineAmountPerUnitTextField.setEditable(true);
        activeCheckBox.setEnabled(true);
        observationTextArea.setEditable(true);
        
        //Botones
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
    }
    
    public void saveButtonAction(ActionEvent e, int furnitureId){
        String description = descriptionTextField.getText();
        ComboBoxItem family = (ComboBoxItem) familyComboBox.getSelectedItem();
        String familyId = family.getKey();
        String unitPrice = unitPriceTextField.getText();
        String unitCostPrice = unitCostPriceTextField.getText();
        String fineAmountPerUnit = fineAmountPerUnitTextField.getText();
        String observation = observationTextArea.getText();
        boolean active = activeCheckBox.isSelected();
        
        HashMap mapReturn = furnitureController.updateFurniture(description, familyId, unitPrice, unitCostPrice, fineAmountPerUnit, observation, active, furnitureId);
        if((Integer) mapReturn.get("status") == furnitureController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == furnitureController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        furnitureController.showAndEditViewClosed();
        furnitureController.setEnabledIndexView();
        furnitureController.searchFurnitureButtonAction();
    }
}