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
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author hp
 */
public class SubjectCreate extends JInternalFrame{
    private final SubjectController subjectController;
    private final JPanel subjectCreatePanel;
    private final JLabel codeLabel;
    private final JLabel nameLabel;
    private final JLabel fisccalNumberLabel;
    private final JLabel addressLabel;
    private final JLabel telephoneLabel;
    private final JLabel cityLabel;
    
    private final JTextField codeTextField;
    private final JTextField nameTextField;
    private final JTextField fiscalNumberTextField;
    private final JTextField addresssTextField;
    private final JTextField telephoneTextField;
    private final JTextField cityTextField;
    private final JTextField fiscalNumberDigitTextField;
    
    private final JComboBox fiscalNumberComboBox;
    
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    
    private final JButton saveButton;
    private final JButton cancelButton;
    
    private boolean rucIsEnabled = true;
    public SubjectCreate(SubjectController subjectController){
        this.subjectController = subjectController;
        
        subjectCreatePanel = new JPanel();
        subjectCreatePanel.setLayout(null);
        
        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 20, 80, 25);
        subjectCreatePanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(170, 20, 160, 25);
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
        subjectCreatePanel.add(codeTextField);
        
        nameLabel = new JLabel("Nombre:");
        nameLabel.setBounds(30, 50, 80, 25);
        subjectCreatePanel.add(nameLabel);
        
        nameTextField = new JTextField();
        nameTextField.setBounds(170, 50, 160, 25);
        subjectCreatePanel.add(nameTextField);
        
        fisccalNumberLabel = new JLabel("Nro. Documento:");
        fisccalNumberLabel.setBounds(30,80, 100, 25);
        subjectCreatePanel.add(fisccalNumberLabel);
        
        fiscalNumberDigitTextField = new JTextField();
        fiscalNumberDigitTextField.setBounds(350, 110, 30, 25);
        fiscalNumberDigitTextField.setVisible(false);
        subjectCreatePanel.add(fiscalNumberDigitTextField);
        
        fiscalNumberComboBox = new JComboBox();
        fiscalNumberComboBox.addItem("CI");
        fiscalNumberComboBox.addItem("RUC");
        fiscalNumberComboBox.setBounds(170, 80, 160, 25);
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
                    saveButton.setEnabled(true);
                }
                subjectCreatePanel.add(fiscalNumberDigitTextField);
            }
        });
        
        fiscalNumberTextField = new JTextField();
        fiscalNumberTextField.setBounds(170, 110, 160, 25);
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
            }
        });
        subjectCreatePanel.add(fiscalNumberTextField);
        
        addressLabel = new JLabel("Direccion:");
        addressLabel.setBounds(30,140, 80, 25);
        subjectCreatePanel.add(addressLabel);
        
        addresssTextField = new JTextField();
        addresssTextField.setBounds(170, 140, 160, 25);
        subjectCreatePanel.add(addresssTextField);
        
        telephoneLabel = new JLabel("Telefono");
        telephoneLabel.setBounds(30,170, 160, 25);
        subjectCreatePanel.add(telephoneLabel);
        
        telephoneTextField = new JTextField();
        telephoneTextField.setBounds(170,170, 160, 25);
        subjectCreatePanel.add(telephoneTextField);
        
        cityLabel = new JLabel("Ciudad");
        cityLabel.setBounds(30,200, 160, 25);
        subjectCreatePanel.add(cityLabel);
        
        cityTextField = new JTextField();
        cityTextField.setBounds(170,200, 160, 25);
        subjectCreatePanel.add(cityTextField);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(30, 250, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        subjectCreatePanel.add(saveButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(170, 250, 120, 32);
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
        setBounds(200,100,400,350);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private void saveButtonAction(ActionEvent e){
        String code = codeTextField.getText();
        String name = nameTextField.getText();
        String fiscalNumber = fiscalNumberTextField.getText();
        String address = addresssTextField.getText();
        String telephone = telephoneTextField.getText();
        String city = cityTextField.getText();
        
        
        HashMap mapReturn = subjectController.saveSubject(code, name, fiscalNumber, address, telephone, city);
         if((Integer) mapReturn.get("status") == subjectController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
        }else if((Integer)mapReturn.get("status") == subjectController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
     public void setCodeTextFieldColor(Color color){
        codeTextField.setForeground(color);
    }
     
     private void cancelButtonAction(ActionEvent e) {
        this.dispose();
        subjectController.viewClosed();
    }
     
    private void generateVerificationDidgit(){
        fiscalNumberDigitTextField.setText("1");
        fiscalNumberDigitTextField.setEditable(false);
        subjectCreatePanel.add(fiscalNumberDigitTextField);
        
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
        fiscalNumberDigitTextField.setEditable(true);
        if(rucIsEnabled){
            saveButton.setEnabled(false);
        }
    }
}
