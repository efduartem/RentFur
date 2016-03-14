/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author hp
 */
public class FurnitureCreate extends JInternalFrame{
    private final FurnitureController furnitureController;
    private final JPanel furnitureCreatePanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JLabel familyLabel;
    private final JLabel unitPriceLabel;
    private final JLabel unitCostPriceLabel;
    private final JLabel fineAmountPerUnitLabel;
    private final JLabel totalStockLabel;
    private final JLabel observationLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JComboBox familyComboBox;
    private final JTextField unitPriceTextField;
    private final JTextField unitCostPriceTextField;
    private final JTextField fineAmountPerUnitTextField;
    private final JTextField totalStockTextField;
    private final JTextArea observationTextArea;
    private final ImageIcon createIconImage;
    private final JButton saveButton;
    private final ImageIcon cancelIconImage;
    private final JButton cancelButton;
    
    public FurnitureCreate(FurnitureController furnitureController){
        this.furnitureController = furnitureController;
        
        furnitureCreatePanel = new JPanel();
        
        furnitureCreatePanel.setLayout(null);

        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 20, 80, 25);
        furnitureCreatePanel.add(codeLabel);
        
        codeTextField = new JTextField(20);
        codeTextField.setBounds(150, 20, 160, 25);
        furnitureCreatePanel.add(codeTextField);
        
        descriptionLabel = new JLabel("Descripcion:");
        descriptionLabel.setBounds(30,50, 80, 25);
        furnitureCreatePanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField(20);
        descriptionTextField.setBounds(150, 50, 160, 25);
        furnitureCreatePanel.add(descriptionTextField);
        
        familyLabel = new JLabel("Familia:");
        familyLabel.setBounds(30,80, 80, 25);
        furnitureCreatePanel.add(familyLabel);
        
        ComboBoxItem[] familiesComboBox = furnitureController.getFurnitureFamiliesForComboBox();
        familyComboBox = new JComboBox(familiesComboBox);
        familyComboBox.setBounds(150, 80, 160, 25);
        furnitureCreatePanel.add(familyComboBox);
                
        unitPriceLabel = new JLabel("Precio Unitario:");
        unitPriceLabel.setBounds(30,110, 80, 25);
        furnitureCreatePanel.add(unitPriceLabel);
        
        unitPriceTextField = new JTextField(20); //debe ser solo numeric
        unitPriceTextField.setBounds(150, 110, 160, 25);
        unitPriceTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                e.consume(); // ignorar el evento de teclado
                }
            }
        });
        furnitureCreatePanel.add(unitPriceTextField);
        
        
        unitCostPriceLabel = new JLabel("Costo Unitario:");
        unitCostPriceLabel.setBounds(30,140, 80, 25);
        furnitureCreatePanel.add(unitCostPriceLabel);
        
        unitCostPriceTextField = new JTextField(20);
        unitCostPriceTextField.setBounds(150, 140, 160, 25);
        unitCostPriceLabel.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                e.consume(); // ignorar el evento de teclado
                }
            }
        });
        furnitureCreatePanel.add(unitCostPriceTextField);
        
        fineAmountPerUnitLabel = new JLabel("Monto multa:");
        fineAmountPerUnitLabel.setBounds(30,170, 80, 25);
        furnitureCreatePanel.add(fineAmountPerUnitLabel);
        
        fineAmountPerUnitTextField = new JTextField(20);
        fineAmountPerUnitTextField.setBounds(150, 170, 160, 25);
        fineAmountPerUnitTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                e.consume(); // ignorar el evento de teclado
                }
            }
        });
        furnitureCreatePanel.add(fineAmountPerUnitTextField);
        
        totalStockLabel = new JLabel("Stock:");
        totalStockLabel.setBounds(30,200, 80, 25);
        furnitureCreatePanel.add(totalStockLabel);
        
        totalStockTextField = new JTextField(20);
        totalStockTextField.setBounds(150, 200, 160, 25);
        totalStockTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                e.consume(); // ignorar el evento de teclado
                }
            }
        });
        furnitureCreatePanel.add(totalStockTextField);
        
        observationLabel = new JLabel("Observacion:");
        observationLabel.setBounds(30,230, 80, 25);
        furnitureCreatePanel.add(observationLabel);
        
        observationTextArea = new JTextArea();
        observationTextArea.setBounds(150, 230, 160, 60);
        furnitureCreatePanel.add(observationTextArea);
        
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(30, 320, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        furnitureCreatePanel.add(saveButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(160, 320, 120, 32);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        furnitureCreatePanel.add(cancelButton);
        
        add(furnitureCreatePanel);
        pack();
        
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Crear Mobiliario");
        setBounds(200,100,500,400);

        setVisible(true);
    }
    
    private void saveButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String description = descriptionTextField.getText();
        ComboBoxItem family = (ComboBoxItem) familyComboBox.getSelectedItem();
        String familyId = family.getKey();
        System.out.println("unitPriceTextField.getText(): "+unitPriceTextField.getText());
        String unitPrice = unitPriceTextField.getText();
        System.out.println("unitPrice double: "+unitPrice);
        String unitCostPrice = unitCostPriceTextField.getText();
        String fineAmountPerUnit = fineAmountPerUnitTextField.getText();
        String totalStock = totalStockTextField.getText();
        String observation = observationTextArea.getText();
        
        HashMap mapReturn = furnitureController.saveFurniture(code, description, familyId, unitPrice, unitCostPrice,fineAmountPerUnit, totalStock, observation);
        if((Integer) mapReturn.get("status") == furnitureController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
        }else if((Integer)mapReturn.get("status") == furnitureController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    private void cancelButtonAction(ActionEvent e) {
        this.dispose();
        furnitureController.viewClosed();
    }
}
