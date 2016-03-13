/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author FDuarte
 */
public class FurnitureIndex extends JInternalFrame{
    private FurnitureController furnitureController;
    private final JPanel furnitureIndexParamsPanel;
    //private final JPanel furnitureIndexResultPanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JLabel furnitureFamilyLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JComboBox furnitureFamilyComboBox;
    
    
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
        
        //container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        add(furnitureIndexParamsPanel);
        //container.add(furnitureIndexResultPanel);
        
        pack();
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Administrar Mobiliarios");
        setBounds(200,100,800,400);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }
}
