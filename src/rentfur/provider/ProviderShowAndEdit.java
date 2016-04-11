/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.provider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import javax.swing.JTextField;
import rentfur.util.NumericTextField;

/**
 *
 * @author hp
 */
public class ProviderShowAndEdit extends JInternalFrame{
    private final ProviderController providerController;
    private final JPanel providerShowAndEditPanel;
    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel tradenameLabel;
    private final JLabel fiscalNumberLabel;
    private final JLabel addressLabel;
    private final JLabel telephoneLabel;
    private final JLabel cityLabel;
    private final JLabel activeLabel;
    private final JComboBox fiscalNumberComboBox;
    private final JTextField codeTextField;
    private final JTextField nameTextField;
    private final JTextField tradenameTextField;
    private final NumericTextField  fiscalNumberTextField;
    private final JTextField fiscalNumberDigitTextField;
    private final JTextField addressTextField;
    private final JTextField telephoneTextField;
    private final JTextField cityTextField;
    private final JCheckBox activeCheckBox;
    private boolean rucIsEnabled = false;
    private String fiscalNumber;
    private String fiscalNumberVerificationDigit = "";
    private final JButton saveButton;
    private final JButton editButton;
    private final JButton cancelButton;
    private final ImageIcon saveIconImage;
    private final ImageIcon editIconImage;
    private final ImageIcon cancelIconImage;
    HashMap providerMap;
    public ProviderShowAndEdit(ProviderController providerController, final int providerId){
        this.providerController = providerController;
        
        providerShowAndEditPanel = new JPanel();
        providerShowAndEditPanel.setLayout(null);
        providerMap = providerController.getProviderById(providerId);
         
        saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
        saveButton.setBounds(60, 290, 120, 32);
        saveButton.setVisible(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e, providerId);
            }
        });
        providerShowAndEditPanel.add(saveButton);
         
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(50,20, 100, 25);
        providerShowAndEditPanel.add(codeLabel);
        
        codeTextField = new JTextField(String.valueOf(providerMap.get("code")));
        codeTextField.setBounds(180, 20, 160, 25);
        codeTextField.setEditable(false);
        providerShowAndEditPanel.add(codeTextField);
        
        nameLabel = new JLabel("Razón Social:");
        nameLabel.setBounds(50, 50, 80, 25);
        providerShowAndEditPanel.add(nameLabel);
        
        nameTextField = new JTextField(String.valueOf(providerMap.get("name")));
        nameTextField.setBounds(180, 50, 160, 25);
        nameTextField.setEditable(false);
        providerShowAndEditPanel.add(nameTextField);
        
        tradenameLabel = new JLabel("Nombre Comercial: ");
        tradenameLabel.setBounds(50, 80, 120, 25);
        providerShowAndEditPanel.add(tradenameLabel);
        
        tradenameTextField =  new JTextField(String.valueOf(providerMap.get("tradename")));
        tradenameTextField.setBounds(180, 80, 160, 25);
        tradenameTextField.setEditable(false);
        providerShowAndEditPanel.add(tradenameTextField);
        
        fiscalNumberLabel = new JLabel("Nro. Documento:");
        fiscalNumberLabel.setBounds(50, 110, 120, 25);
        providerShowAndEditPanel.add(fiscalNumberLabel);
               
        fiscalNumberComboBox = new JComboBox();
        fiscalNumberComboBox.addItem(ProviderCreate.DOCUMENT_CI);
        fiscalNumberComboBox.addItem(ProviderCreate.DOCUMENT_RUC);
        fiscalNumberComboBox.setEnabled(false);
        fiscalNumberComboBox.setBounds(180, 110, 160, 25);
        providerShowAndEditPanel.add(fiscalNumberComboBox);
        fiscalNumberComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(fiscalNumberComboBox.getSelectedItem().equals("RUC")){
                    rucIsEnabled = true;
                    addVerificationDigitTextField();
                    resetVerificationDigitTextField();
                }else{
                    rucIsEnabled = false;
                    fiscalNumberDigitTextField.setVisible(false);
                    fiscalNumberDigitTextField.setText("");
                    saveButton.setToolTipText("");
                    saveButton.setEnabled(true);
                }
                providerShowAndEditPanel.add(fiscalNumberDigitTextField);
            }
        });
        
        fiscalNumberDigitTextField = new JTextField();
        fiscalNumberDigitTextField.setBounds(360, 140, 30, 25);
        fiscalNumberDigitTextField.setEnabled(false);
        providerShowAndEditPanel.add(fiscalNumberDigitTextField);
        
        DecimalFormat amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        if(providerMap.get("fiscalNumber").toString().contains("-")){
            fiscalNumberComboBox.setSelectedItem(ProviderCreate.DOCUMENT_RUC);
            fiscalNumber = amountFormat.format(Double.valueOf(providerMap.get("fiscalNumber").toString().split("-")[0]));
            fiscalNumberVerificationDigit = providerMap.get("fiscalNumber").toString().split("-")[1];
            fiscalNumberDigitTextField.setText(fiscalNumberVerificationDigit);
            saveButton.setEnabled(true);
        }else{
            fiscalNumber = amountFormat.format(Double.valueOf(providerMap.get("fiscalNumber").toString()));
            fiscalNumberDigitTextField.setVisible(false);
        }
        
        fiscalNumberTextField = new NumericTextField(fiscalNumber, 20, amountFormat);
        fiscalNumberTextField.setBounds(180, 140, 160, 25);
        fiscalNumberTextField.setEnabled(false);
        fiscalNumberTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
                         resetVerificationDigitTextField();
                     }

                     @Override
                     public void keyPressed(KeyEvent e) {
                     }

                     @Override
                     public void keyReleased(KeyEvent e) {
                         update(e);
                     }
                     
                     public void update(KeyEvent e){
                         String texto = fiscalNumberTextField.getText();
                         texto = texto.replaceAll("\\.", "");
                         if(e.getKeyChar()!=','){
                            texto = texto.replaceAll(",", ".");
                            if(!texto.isEmpty()){
                                fiscalNumberTextField.setValue(Double.valueOf(texto));
                            }
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });
        
        providerShowAndEditPanel.add(fiscalNumberTextField);
        
        addressLabel = new JLabel("Direccion:");
        addressLabel.setBounds(50,170, 80, 25);
        providerShowAndEditPanel.add(addressLabel);
        
        addressTextField = new JTextField(String.valueOf(providerMap.get("address")));
        addressTextField.setBounds(180, 170, 160, 25);
        addressTextField.setEditable(false);
        providerShowAndEditPanel.add(addressTextField);
        
        telephoneLabel = new JLabel("Telefono:");
        telephoneLabel.setBounds(50,200, 160, 25);
        providerShowAndEditPanel.add(telephoneLabel);
        
        telephoneTextField = new JTextField(String.valueOf(providerMap.get("telephone")));
        telephoneTextField.setBounds(180,200, 160, 25);
        telephoneTextField.setEditable(false);
        providerShowAndEditPanel.add(telephoneTextField);
        
        cityLabel = new JLabel("Ciudad");
        cityLabel.setBounds(50,230, 160, 25);
        providerShowAndEditPanel.add(cityLabel);
        
        cityTextField = new JTextField(String.valueOf(providerMap.get("city")));
        cityTextField.setBounds(180,230, 160, 25);
        cityTextField.setEditable(false);
        providerShowAndEditPanel.add(cityTextField);
        
        activeLabel = new JLabel("Activo:");
        activeLabel.setBounds(50,260, 80, 25);
        providerShowAndEditPanel.add(activeLabel);
        
        activeCheckBox = new JCheckBox("", Boolean.valueOf(providerMap.get("isActive").toString()));
        activeCheckBox.setEnabled(false);
        activeCheckBox.setBounds(178,260, 80, 25);
        providerShowAndEditPanel.add(activeCheckBox);
        
        editIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/edit_24x24.png"));
        editButton = new JButton("     Editar", editIconImage);
        editButton.setBounds(110, 290, 200, 35);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enabledEditActions(e);
            }
        });
        providerShowAndEditPanel.add(editButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(200, 290, 120, 32);
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        providerShowAndEditPanel.add(cancelButton);
        
        
        add(providerShowAndEditPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Detalles ["+providerMap.get("code").toString()+" - "+providerMap.get("name").toString()+"]");
        setBounds(400,220,450,380);
        setVisible(true);
    }
    
     public void addVerificationDigitTextField(){
        fiscalNumberDigitTextField.setVisible(true);
        fiscalNumberDigitTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                generateVerificationDidgit();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
        
        saveButton.setEnabled(false);
    }
     private void generateVerificationDidgit(){
        int dv = providerController.calculateDv(fiscalNumberTextField.getText().replaceAll("\\.", ""));
        fiscalNumberDigitTextField.setText(String.valueOf(dv));
        fiscalNumberDigitTextField.setEditable(false);
        providerShowAndEditPanel.add(fiscalNumberDigitTextField);
        saveButton.setToolTipText("");
        saveButton.setEnabled(true);
    }
     
    public void resetVerificationDigitTextField(){
        String html = "<html><p><b><font color=\"green\" size=\"4\" face=\"Verdana\">Favor actualizar el Digito Verificador antes de actualizar el cliente</font></b></p></html>";
        fiscalNumberDigitTextField.setEditable(true);
       if(rucIsEnabled){
            saveButton.setEnabled(false);
            saveButton.setToolTipText(html);
       }
    }
    
    public void enabledEditActions(ActionEvent e){
        nameTextField.setEditable(true);
        tradenameTextField.setEditable(true);
        addressTextField.setEditable(true);
        telephoneTextField.setEditable(true);
        cityTextField.setEditable(true);
        activeCheckBox.setEnabled(true);
        fiscalNumberComboBox.setEnabled(true);
        fiscalNumberDigitTextField.setEnabled(true);
        fiscalNumberTextField.setEnabled(true);
        
        //Botones
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
    }
    
    private void cancelButtonAction(ActionEvent e) {
        nameTextField.setEditable(false);
        tradenameTextField.setEditable(false);
        fiscalNumberTextField.setEditable(false);
        addressTextField.setEditable(false);
        telephoneTextField.setEditable(false);
        cityTextField.setEditable(false);
        activeCheckBox.setEnabled(false);
        fiscalNumberComboBox.setEnabled(false);
        fiscalNumberDigitTextField.setEnabled(false);
        fiscalNumberTextField.setEnabled(false);

        //Botones
        editButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
    }
    
    public void saveButtonAction(ActionEvent e, int providerId){
        String name = nameTextField.getText();
        String tradename = tradenameTextField.getText();
        String documentNumber = fiscalNumberTextField.getText();
        String verifyDigit = fiscalNumberDigitTextField.getText();
        String address = addressTextField.getText();
        String telephone = telephoneTextField.getText();
        String city = cityTextField.getText();
        boolean active = activeCheckBox.isSelected();
        String currentFiscalNumber = providerMap.get("fiscalNumber").toString();
        
        HashMap mapReturn = providerController.updateProvider(name, tradename, documentNumber, address, telephone, city, active, providerId, verifyDigit, currentFiscalNumber);
        if((Integer) mapReturn.get("status") == providerController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == providerController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        providerController.showAndEditViewClosed();
        providerController.setEnabledIndexView();
        providerController.searchProviderButtonAction();
    }
}
