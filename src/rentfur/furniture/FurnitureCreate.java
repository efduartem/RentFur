/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Locale;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.InternalFrameListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import rentfur.util.ComboBoxItem;
import rentfur.util.NumericTextField;

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
    private final NumericTextField unitPriceTextField;
    private final NumericTextField unitCostPriceTextField;
    private final NumericTextField fineAmountPerUnitTextField;
    private final NumericTextField totalStockTextField;
    private final JTextArea observationTextArea;
    private final ImageIcon createIconImage;
    private final JButton saveButton;
    private final ImageIcon cancelIconImage;
    private final JButton cancelButton;
    private DecimalFormat amountFormat;
    
    public FurnitureCreate(FurnitureController furnitureController){
        this.furnitureController = furnitureController;
        
        furnitureCreatePanel = new JPanel();
        
        furnitureCreatePanel.setLayout(null);

        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(50, 20, 100, 25);
        furnitureCreatePanel.add(codeLabel);
        
        codeTextField = new JTextField(20);
        codeTextField.setBounds(180, 20, 160, 25);
        codeTextField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                codeTextField.setForeground(Color.BLACK);
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        furnitureCreatePanel.add(codeTextField);
        
        descriptionLabel = new JLabel("Descripcion:");
        descriptionLabel.setBounds(50,50, 100, 25);
        furnitureCreatePanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField(20);
        descriptionTextField.setBounds(180, 50, 160, 25);
        furnitureCreatePanel.add(descriptionTextField);
        
        familyLabel = new JLabel("Familia:");
        familyLabel.setBounds(50,80, 100, 25);
        furnitureCreatePanel.add(familyLabel);
        
        ComboBoxItem[] familiesComboBox = furnitureController.getFurnitureFamiliesForComboBox();
        familyComboBox = new JComboBox(familiesComboBox);
        familyComboBox.setBounds(180, 80, 160, 25);
        furnitureCreatePanel.add(familyComboBox);
        
        unitPriceLabel = new JLabel("Precio Unitario:");
        unitPriceLabel.setBounds(50,110, 100, 25);
        furnitureCreatePanel.add(unitPriceLabel);
        
        amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        unitPriceTextField = new NumericTextField(20, amountFormat);
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

        furnitureCreatePanel.add(unitPriceTextField);
        
        unitCostPriceLabel = new JLabel("Costo Unitario:");
        unitCostPriceLabel.setBounds(50, 140, 100, 25);
        furnitureCreatePanel.add(unitCostPriceLabel);
        
        unitCostPriceTextField = new NumericTextField(20, amountFormat);
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
        furnitureCreatePanel.add(unitCostPriceTextField);
        
        fineAmountPerUnitLabel = new JLabel("Monto multa:");
        fineAmountPerUnitLabel.setBounds(50,170, 80, 25);
        furnitureCreatePanel.add(fineAmountPerUnitLabel);
        
        fineAmountPerUnitTextField = new NumericTextField(20, amountFormat);
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
        furnitureCreatePanel.add(fineAmountPerUnitTextField);
        
        totalStockLabel = new JLabel("Stock:");
        totalStockLabel.setBounds(50,200, 80, 25);
        furnitureCreatePanel.add(totalStockLabel);
        
        totalStockTextField = new NumericTextField(20, amountFormat);
        totalStockTextField.setBounds(180, 200, 160, 25);
        totalStockTextField.addKeyListener(new KeyListener() {

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
                         String texto = totalStockTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                totalStockTextField.setValue(Double.valueOf(texto));
                            }
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });
        furnitureCreatePanel.add(totalStockTextField);
        
        observationLabel = new JLabel("Observacion:");
        observationLabel.setBounds(50,230, 80, 25);
        furnitureCreatePanel.add(observationLabel);
        
        observationTextArea = new JTextArea();
        observationTextArea.setBounds(180, 230, 160, 60);
        furnitureCreatePanel.add(observationTextArea);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(60, 320, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        furnitureCreatePanel.add(saveButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(200, 320, 120, 32);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        furnitureCreatePanel.add(cancelButton);
        
        add(furnitureCreatePanel);
        
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Crear Mobiliario");
        setBounds(350,200,500,450);
        //pack();
        setVisible(true);
    }
    
    private void saveButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String description = descriptionTextField.getText();
        ComboBoxItem family = (ComboBoxItem) familyComboBox.getSelectedItem();
        String familyId = "";
        if(family!=null){
            familyId = family.getKey();
        }
        String unitPrice = unitPriceTextField.getText();
        String unitCostPrice = unitCostPriceTextField.getText();
        String fineAmountPerUnit = fineAmountPerUnitTextField.getText();
        String totalStock = totalStockTextField.getText();
        String observation = observationTextArea.getText();
        
        HashMap mapReturn = furnitureController.saveFurniture(code, description, familyId, unitPrice, unitCostPrice,fineAmountPerUnit, totalStock, observation);
        if((Integer) mapReturn.get("status") == furnitureController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == furnitureController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    private void cancelButtonAction(ActionEvent e) {
        this.dispose();
        furnitureController.createViewClosed();
        furnitureController.setEnabledIndexView();
    }
    
    public void setCodeTextFieldColor(Color color){
        codeTextField.setForeground(color);
    }
    
    @Override
    public void doDefaultCloseAction() {
        cancelButtonAction(null);
    }
    
}
