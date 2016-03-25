/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rentfur.util.ComboBoxItem;
import rentfur.util.NumericTextField;

/**
 *
 * @author hp
 */
public class SubjectCreate extends JInternalFrame{
    private final SubjectController subjectController;
    private final JPanel subjectCreatePanel;
    private final JLabel nameLabel;
    private final JLabel tradenameLabel;
    private final JLabel fisccalNumberLabel;
    private final JLabel addressLabel;
    private final JLabel telephoneLabel;
    private final JLabel cityLabel;
    private final JTextField nameTextField;
    private final JTextField tradenameTextField;
    private final NumericTextField fiscalNumberTextField;
    private final JTextField addresssTextField;
    private final JTextField telephoneTextField;
    private final JTextField cityTextField;
    private final JTextField fiscalNumberDigitTextField;
    
    private final JComboBox fiscalNumberComboBox;
    
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    
    private final JButton saveButton;
    private final JButton cancelButton;
    
    public static final String DOCUMENT_CI = "CI";
    public static final String DOCUMENT_RUC = "RUC";
    
    private boolean rucIsEnabled = false;
    public SubjectCreate(SubjectController subjectController){
        this.subjectController = subjectController;
        
        subjectCreatePanel = new JPanel();
        subjectCreatePanel.setLayout(null);

        nameLabel = new JLabel("Razón Social:");
        nameLabel.setBounds(40, 20, 80, 25);
        subjectCreatePanel.add(nameLabel);
        
        nameTextField = new JTextField();
        nameTextField.setBounds(180, 20, 160, 25);
        subjectCreatePanel.add(nameTextField);
        
        tradenameLabel = new JLabel("Nombre Comercial: ");
        tradenameLabel.setBounds(40, 50, 120, 25);
        subjectCreatePanel.add(tradenameLabel);
        
        tradenameTextField =  new JTextField();
        tradenameTextField.setBounds(180, 50, 160, 25);
        subjectCreatePanel.add(tradenameTextField);
        
        fisccalNumberLabel = new JLabel("Nro. Documento:");
        fisccalNumberLabel.setBounds(40, 80, 100, 25);
        subjectCreatePanel.add(fisccalNumberLabel);
        
        fiscalNumberDigitTextField = new JTextField();
        fiscalNumberDigitTextField.setBounds(360, 110, 30, 25);
        fiscalNumberDigitTextField.setVisible(false);
        subjectCreatePanel.add(fiscalNumberDigitTextField);
        
        fiscalNumberComboBox = new JComboBox();
        fiscalNumberComboBox.addItem(DOCUMENT_CI);
        fiscalNumberComboBox.addItem(DOCUMENT_RUC);
        fiscalNumberComboBox.setBounds(180, 80, 160, 25);
        subjectCreatePanel.add(fiscalNumberComboBox);
        fiscalNumberComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(fiscalNumberComboBox.getSelectedItem().equals("RUC")){
                    rucIsEnabled = true;
                    addVerificationDigitTextField();   
                }else{
                    rucIsEnabled = false;
                    fiscalNumberDigitTextField.setVisible(false);
                    saveButton.setToolTipText("");
                    saveButton.setEnabled(true);
                }
                subjectCreatePanel.add(fiscalNumberDigitTextField);
            }
        });
        
        DecimalFormat amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        fiscalNumberTextField = new NumericTextField(20, amountFormat);
        fiscalNumberTextField.setBounds(180, 110, 160, 25);
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
        
        subjectCreatePanel.add(fiscalNumberTextField);
        
        addressLabel = new JLabel("Direccion:");
        addressLabel.setBounds(40,140, 80, 25);
        subjectCreatePanel.add(addressLabel);
        
        addresssTextField = new JTextField();
        addresssTextField.setBounds(180, 140, 160, 25);
        subjectCreatePanel.add(addresssTextField);
        
        telephoneLabel = new JLabel("Telefono");
        telephoneLabel.setBounds(40,170, 160, 25);
        subjectCreatePanel.add(telephoneLabel);
        
        telephoneTextField = new JTextField();
        telephoneTextField.setBounds(180,170, 160, 25);
        subjectCreatePanel.add(telephoneTextField);
        
        cityLabel = new JLabel("Ciudad");
        cityLabel.setBounds(40,200, 160, 25);
        subjectCreatePanel.add(cityLabel);
        
        cityTextField = new JTextField();
        cityTextField.setBounds(180,200, 160, 25);
        subjectCreatePanel.add(cityTextField);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(50, 250, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        subjectCreatePanel.add(saveButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(190, 250, 120, 32);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        subjectCreatePanel.add(cancelButton);
        
        
        add(subjectCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Crear Cliente");
        setBounds(400,220,450,370);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void saveButtonAction(ActionEvent e){
        String name = nameTextField.getText();
        String tradename = tradenameTextField.getText();
        String fiscalNumber = fiscalNumberTextField.getText();
        String address = addresssTextField.getText();
        String telephone = telephoneTextField.getText();
        String city = cityTextField.getText();
        String verifyDigit = fiscalNumberDigitTextField.getText();
        String documentNumberSelectedItem = (String) fiscalNumberComboBox.getSelectedItem();
        
        HashMap mapReturn = subjectController.saveSubject(tradename, name, fiscalNumber, address, telephone, city, documentNumberSelectedItem, verifyDigit);
         if((Integer) mapReturn.get("status") == subjectController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
        }else if((Integer)mapReturn.get("status") == subjectController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public void doDefaultCloseAction() {
        cancelButtonAction(null);
    }
     
     private void cancelButtonAction(ActionEvent e) {
        this.dispose();
        subjectController.viewClosed();
    }
     
    private void generateVerificationDidgit(){
        int dv = subjectController.calculateDv(fiscalNumberTextField.getText().replaceAll("\\.", ""));
        fiscalNumberDigitTextField.setText(String.valueOf(dv));
        fiscalNumberDigitTextField.setEditable(false);
        subjectCreatePanel.add(fiscalNumberDigitTextField);
        saveButton.setToolTipText("");
        saveButton.setEnabled(true);
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
    
    public void resetVerificationDigitTextField(){
        String html = "<html><p><b><font color=\"green\" size=\"4\" face=\"Verdana\">Favor actualizar el Digito Verificador antes de crear el cliente</font></b></p></html>";
        fiscalNumberDigitTextField.setEditable(true);
        if(rucIsEnabled){
            saveButton.setEnabled(false);
            saveButton.setToolTipText(html);
        }
    }
}
