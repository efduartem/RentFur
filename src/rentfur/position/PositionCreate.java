/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.position;

import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author FDuarte
 */
public class PositionCreate extends JInternalFrame{
    
    private final PositionController positionController;
    //private final JPanel rolesPanel;
    private final JPanel createPanel;
    private final JLabel descriptionLabel;
    private final JTextField descriptionTextField;
    private final JButton createButton;
    private final JButton cancelButton;
    //private final JCheckBox suplierCheckBox;
    
    public PositionCreate(PositionController positionController){
        this.positionController = positionController;
        
        
        createPanel = new JPanel();
        GroupLayout createPanelGroupLayout = new GroupLayout(createPanel);
        createPanel.setLayout(createPanelGroupLayout);
        createPanelGroupLayout.setAutoCreateContainerGaps(true);
        createPanelGroupLayout.setAutoCreateGaps(true);
        
        descriptionLabel = new JLabel("Descripción:");
        descriptionLabel.setBounds(50,20, 100, 25);
        createPanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField();
        descriptionTextField.setBounds(180, 20, 160, 25);
        createPanel.add(descriptionTextField);
        
        createButton = new JButton("Crear");
        createButton.setBounds(50, 50, 100, 25);
        createPanel.add(createButton);
        
        cancelButton = new JButton("Cancelar");
        cancelButton.setBounds(150, 50, 100, 25);
        createPanel.add(cancelButton);

        //rolesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 1, true), "Permisos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 14), new java.awt.Color(0, 0, 0))); // NOI18N

        //GroupLayout rolesPanelLayout = new GroupLayout(rolesPanel);
        //rolesPanel.setLayout(rolesPanelLayout);

        //GroupLayout createPanelLayout = new GroupLayout(createPanel);

        add(createPanel);
        //add(rolesPanel);
        
        setIconifiable(false);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Crear Cargo");
        setBounds(360,200,600,300);
        setVisible(true);
        
    }
}
