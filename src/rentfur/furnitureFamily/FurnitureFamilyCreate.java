/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furnitureFamily;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rentfur.furniture.FurnitureController;

/**
 *
 * @author FDuarte
 */
public class FurnitureFamilyCreate extends JInternalFrame{
    
    private final FurnitureController furnitureController;
    private final JPanel furnitureFamilyCreatePanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton saveButton;
    private final JButton cancelButton;
    
    public FurnitureFamilyCreate(FurnitureController furnitureController) {
        this.furnitureController = furnitureController;
        
        furnitureFamilyCreatePanel = new JPanel();
        
        furnitureFamilyCreatePanel.setLayout(null);

        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 20, 80, 25);
        furnitureFamilyCreatePanel.add(codeLabel);

        codeTextField = new JTextField(20);
        codeTextField.setBounds(120, 20, 160, 25);
        furnitureFamilyCreatePanel.add(codeTextField);

        descriptionLabel = new JLabel("Descripcion:");
        descriptionLabel.setBounds(30, 50, 80, 25);
        furnitureFamilyCreatePanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField(20);
        descriptionTextField.setBounds(120, 50, 160, 25);
        furnitureFamilyCreatePanel.add(descriptionTextField);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        saveButton = new JButton(" Crear", createIconImage);
        saveButton.setBounds(30, 90, 120, 32);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        furnitureFamilyCreatePanel.add(saveButton);
        
        ///ar/lefunes/recurso/imagen.png
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(160, 90, 120, 32);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
        furnitureFamilyCreatePanel.add(cancelButton);

        /*JButton registerButton = new JButton("Cancelar");
        registerButton.setBounds(180, 80, 80, 25);
        panel.add(registerButton);
        */
        add(furnitureFamilyCreatePanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Crear Familia de Mobiliario");
        setBounds(400,340,400,200);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
    
    private void saveButtonAction(ActionEvent e) {
        String code = codeTextField.getText();
        String description = descriptionTextField.getText();
        HashMap mapReturn = furnitureController.saveFurnitureFamily(code, description);
        if((Integer) mapReturn.get("status") == furnitureController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonAction(e);
        }else if((Integer)mapReturn.get("status") == furnitureController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void cancelButtonAction(ActionEvent e) {
        this.dispose();
        furnitureController.createFamilyClosed();
        furnitureController.setEnabledIndexView();
    }
    
    @Override
    public void doDefaultCloseAction() {
        cancelButtonAction(null);
    }
    
}
