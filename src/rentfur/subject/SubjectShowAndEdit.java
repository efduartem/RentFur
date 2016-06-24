/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

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
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import rentfur.position.PositionController;
import rentfur.util.NumericTextField;
import rentfur.util.UserRoles;

/**
 *
 * @author hp
 */
public class SubjectShowAndEdit extends JInternalFrame{
    private final SubjectController subjectController;
    private SubjectAccountStatus subjectAccountStatus;
    private final JPanel subjectShowAndEditPanel;
    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel tradenameLabel;
    private final JLabel fisccalNumberLabel;
    private final JLabel addressLabel;
    private final JLabel telephoneLabel;
    private final JLabel cityLabel;
    private final JLabel activeLabel;
    private final JTextField codeTextField;
    private final JTextField nameTextField;
    private final JTextField tradenameTextField;
    private final NumericTextField fiscalNumberTextField;
    private final JTextField fiscalNumberDigitTextField;
    private final JTextArea addresssTextField;
    private final JTextField telephoneTextField;
    private final JTextField cityTextField;
    private final JCheckBox activeCheckBox;
    private final JComboBox fiscalNumberComboBox;
    private final ImageIcon saveIconImage;
    private final ImageIcon editIconImage;
    private final ImageIcon cancelIconImage;
    private final ImageIcon acoountStatusIconImage;
    private final JButton saveButton;
    private final JButton editButton;
    private final JButton cancelButton;
    private final JButton acoountStatusButton;
    private final String fiscalNumber;
    private String fiscalNumberVerificationDigit = "";
    private final HashMap subjectMap;
    private boolean rucIsEnabled = false;
    private JDesktopPane subjectShowAndEditPane;
     
    public SubjectShowAndEdit(SubjectController subjectController, final int subjectId, UserRoles userRoles){
        this.subjectController = subjectController;
        
        subjectShowAndEditPanel = new JPanel();
        subjectShowAndEditPanel.setLayout(null);
        
        subjectMap = SubjectController.getSubjectById(subjectId);
        
        saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
        saveButton.setBounds(110, 380, 200, 35);
        saveButton.setVisible(false);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e, subjectId);
            }
        });
        subjectShowAndEditPanel.add(saveButton);
        
        editIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/edit_24x24.png"));
        editButton = new JButton("     Editar", editIconImage);
        editButton.setBounds(110, 380, 200, 35);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enabledEditActions(e);
            }
        });
        
        if((Boolean)userRoles.getRolesMap().get(PositionController.ROLE_RF_SUBJECT)){
            String message = "Su usuario solo cuenta con permiso de consultas";
            editButton.setEnabled(false);
            editButton.setToolTipText(message);
        }
        
        subjectShowAndEditPanel.add(editButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(350, 380, 200, 35);
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        subjectShowAndEditPanel.add(cancelButton);
        
        
        acoountStatusIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/account_status_24x24.png"));
        acoountStatusButton = new JButton(" Estado de Cuenta", acoountStatusIconImage);
        acoountStatusButton.setBounds(350, 380, 200, 35);
        acoountStatusButton.setVisible(true);
        acoountStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSubjectAccountStatusView();
            }
        });
        subjectShowAndEditPanel.add(acoountStatusButton);

        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(40,20, 100, 25);
        subjectShowAndEditPanel.add(codeLabel);
        
        codeTextField = new JTextField(String.valueOf(subjectMap.get("code")));
        codeTextField.setBounds(180, 20, 210, 25);
        codeTextField.setEditable(false);
        subjectShowAndEditPanel.add(codeTextField);
        
        nameLabel = new JLabel("Razón Social:");
        nameLabel.setBounds(40, 50, 80, 25);
        subjectShowAndEditPanel.add(nameLabel);
        
        nameTextField = new JTextField(String.valueOf(subjectMap.get("name")));
        nameTextField.setBounds(180, 50, 210, 25);
        nameTextField.setEditable(false);
        subjectShowAndEditPanel.add(nameTextField);
        
        tradenameLabel = new JLabel("Nombre Comercial: ");
        tradenameLabel.setBounds(40, 80, 120, 25);
        subjectShowAndEditPanel.add(tradenameLabel);
        
        tradenameTextField =  new JTextField(String.valueOf(subjectMap.get("tradename")));
        tradenameTextField.setBounds(180, 80, 210, 25);
        tradenameTextField.setEditable(false);
        subjectShowAndEditPanel.add(tradenameTextField);
        
        fisccalNumberLabel = new JLabel("Nro. Documento:");
        fisccalNumberLabel.setBounds(40, 110, 100, 25);
        subjectShowAndEditPanel.add(fisccalNumberLabel);
        
        fiscalNumberComboBox = new JComboBox();
        fiscalNumberComboBox.addItem(SubjectCreate.DOCUMENT_CI);
        fiscalNumberComboBox.addItem(SubjectCreate.DOCUMENT_RUC);
        fiscalNumberComboBox.setEnabled(false);
        fiscalNumberComboBox.setBounds(180, 110, 210, 25);
        subjectShowAndEditPanel.add(fiscalNumberComboBox);
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
                subjectShowAndEditPanel.add(fiscalNumberDigitTextField);
            }
        });
        
        fiscalNumberDigitTextField = new JTextField();
        fiscalNumberDigitTextField.setBounds(400, 140, 30, 25);
        fiscalNumberDigitTextField.setEnabled(false);
        subjectShowAndEditPanel.add(fiscalNumberDigitTextField);
        
        DecimalFormat amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        if(subjectMap.get("fiscalNumber").toString().contains("-")){
            fiscalNumberComboBox.setSelectedItem(SubjectCreate.DOCUMENT_RUC);
            fiscalNumber = amountFormat.format(Double.valueOf(subjectMap.get("fiscalNumber").toString().split("-")[0]));
            fiscalNumberVerificationDigit = subjectMap.get("fiscalNumber").toString().split("-")[1];
            fiscalNumberDigitTextField.setText(fiscalNumberVerificationDigit);
            saveButton.setEnabled(true);
        }else{
            fiscalNumber = amountFormat.format(Double.valueOf(subjectMap.get("fiscalNumber").toString()));
            fiscalNumberDigitTextField.setVisible(false);
        }
        
        fiscalNumberTextField = new NumericTextField(fiscalNumber, 20, amountFormat);
        fiscalNumberTextField.setBounds(180, 140, 210, 25);
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
                         }
                     }
                 });
        
        subjectShowAndEditPanel.add(fiscalNumberTextField);
        
        addressLabel = new JLabel("Direccion:");
        addressLabel.setBounds(40,170, 80, 25);
        subjectShowAndEditPanel.add(addressLabel);
        
        addresssTextField = new JTextArea(String.valueOf(subjectMap.get("address")),0,0);
        addresssTextField.setLineWrap(true);
        addresssTextField.setWrapStyleWord(true);
        addresssTextField.setEditable(false);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(180, 170, 210, 90);
        scrollPane.setViewportView(addresssTextField);
        subjectShowAndEditPanel.add(scrollPane);
        
//        addresssTextField = new JTextField(String.valueOf(subjectMap.get("address")));
//        addresssTextField.setBounds(180, 170, 210, 25);
//        addresssTextField.setEditable(false);
//        subjectShowAndEditPanel.add(addresssTextField);
        
        telephoneLabel = new JLabel("Telefono");
        telephoneLabel.setBounds(40,270, 160, 25);
        subjectShowAndEditPanel.add(telephoneLabel);
        
        telephoneTextField = new JTextField(String.valueOf(subjectMap.get("telephone")));
        telephoneTextField.setBounds(180,270, 210, 25);
        telephoneTextField.setEditable(false);
        subjectShowAndEditPanel.add(telephoneTextField);
        
        cityLabel = new JLabel("Ciudad");
        cityLabel.setBounds(40,300, 160, 25);
        subjectShowAndEditPanel.add(cityLabel);
        
        cityTextField = new JTextField(String.valueOf(subjectMap.get("city")));
        cityTextField.setBounds(180,300, 210, 25);
        cityTextField.setEditable(false);
        subjectShowAndEditPanel.add(cityTextField);
        
        activeLabel = new JLabel("Activo:");
        activeLabel.setBounds(40,330, 80, 25);
        subjectShowAndEditPanel.add(activeLabel);
        
        activeCheckBox = new JCheckBox("", Boolean.valueOf(subjectMap.get("active").toString()));
        activeCheckBox.setEnabled(false);
        activeCheckBox.setBounds(178,330, 80, 25);
        subjectShowAndEditPanel.add(activeCheckBox);
        
        
        add(subjectShowAndEditPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Detalles ["+subjectMap.get("code").toString()+" - "+subjectMap.get("name").toString()+"]");
        setBounds(400,200,650,500);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    public void resetVerificationDigitTextField(){
        String html = "<html><p><b><font color=\"green\" size=\"4\" face=\"Verdana\">Favor actualizar el Digito Verificador antes de actualizar el cliente</font></b></p></html>";
        fiscalNumberDigitTextField.setEditable(true);
       if(rucIsEnabled){
            saveButton.setEnabled(false);
            saveButton.setToolTipText(html);
       }
    }
    
    public void saveButtonAction(ActionEvent e, int subjectId){
        String name = nameTextField.getText();
        String tradename = tradenameTextField.getText();
        String documentNumber = fiscalNumberTextField.getText();
        String verifyDigit = fiscalNumberDigitTextField.getText();
        String addresss = addresssTextField.getText();
        String telephone = telephoneTextField.getText();
        String city = cityTextField.getText();
        boolean active = activeCheckBox.isSelected();
        String currentFiscalNumber = subjectMap.get("fiscalNumber").toString();
        
        HashMap mapReturn = subjectController.updateSubject(name, tradename, documentNumber, addresss, telephone, city, active, subjectId, verifyDigit, currentFiscalNumber);
        if((Integer) mapReturn.get("status") == subjectController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == subjectController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void cancelButtonAction(ActionEvent e) {
        nameTextField.setEditable(false);
        tradenameTextField.setEditable(false);
        fiscalNumberTextField.setEditable(false);
        addresssTextField.setEditable(false);
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
        acoountStatusButton.setVisible(true);
    }
    
    public void enabledEditActions(ActionEvent e){
        nameTextField.setEditable(true);
        tradenameTextField.setEditable(true);
        fiscalNumberTextField.setEditable(true);
        addresssTextField.setEditable(true);
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
        acoountStatusButton.setVisible(false);
    }
    
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        subjectController.showAndEditViewClosed();
        subjectController.setEnabledIndexView();
        subjectController.searchSubjectButtonAction();
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
        int dv = subjectController.calculateDv(fiscalNumberTextField.getText().replaceAll("\\.", ""));
        fiscalNumberDigitTextField.setText(String.valueOf(dv));
        fiscalNumberDigitTextField.setEditable(false);
        subjectShowAndEditPanel.add(fiscalNumberDigitTextField);
        saveButton.setToolTipText("");
        saveButton.setEnabled(true);
    }
    
    private void showSubjectAccountStatusView(){
        
        int subjectId = (Integer) subjectMap.get("id");
        subjectAccountStatus = subjectController.getSubjectAccountStatusView(subjectId);
        subjectAccountStatus.setVisible(true);
        showSearchDialog(subjectAccountStatus);
        inactivateElements();
        subjectAccountStatus.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {}

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {}

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {}

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {}
        });
        
    }
    
    public void inactivateElements(){
        editButton.setEnabled(false);
        saveButton.setEnabled(false);
        cancelButton.setEnabled(false);
        acoountStatusButton.setEnabled(false);
        this.setClosable(false);
        this.setIconifiable(false);
        //furnitureMovementResultTable.setEnabled(false);
    }
    
    public void activateElements(){
        editButton.setEnabled(true);
        saveButton.setEnabled(true);
        cancelButton.setEnabled(true);
        acoountStatusButton.setEnabled(true);
        this.setClosable(true);
        this.setIconifiable(true);
        //furnitureMovementResultTable.setEnabled(true);
    }
    
    private void showSearchDialog(Object dialogView){
        subjectShowAndEditPane = this.getDesktopPane();
        subjectShowAndEditPane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        subjectShowAndEditPane.setVisible(true);
    }
}
