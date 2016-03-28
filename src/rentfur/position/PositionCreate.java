/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.position;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author FDuarte
 */
public class PositionCreate extends JInternalFrame{
    
    private final PositionController positionController;
    private final JPanel rolesPanel;
    private JPanel createPanel = null;
    private JLabel descriptionLabel = null;
    private JTextField descriptionTextField = null;
    private final JButton createButton;
    private final JButton cancelButton;
    
    private final JCheckBox providerCheckBox;
    private final JCheckBox subjectCheckBox;
    private final JCheckBox furnitureCheckBox;
    private final JCheckBox usersCheckBox;
    private final JCheckBox providerReadOnlyCheckBox;
    private final JCheckBox subjectReadOnlyCheckBox;
    private final JCheckBox furnitureReadOnlyCheckBox;
    private final JCheckBox usersReadOnlyCheckBox;
    
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    
    public PositionCreate(PositionController positionController){
        this.positionController = positionController;
        
        createPanel = new JPanel();
        descriptionLabel = new JLabel("Descripcion:");
        descriptionTextField = new JTextField();
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/create_24x24.png"));
        createButton = new JButton(" Crear", createIconImage);
        createButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createButtonActionPerformed(e);
            }
        });
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonActionPerformed(e);
            }
        });
               
        GroupLayout createPanelLayout = new GroupLayout(createPanel);
        createPanel.setLayout(createPanelLayout);
        GroupLayout.Group horizontalGroupCreatePanelLayout = createPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGroup(createPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(createPanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(descriptionLabel)
                        .addGap(18, 18, 18)
                        .addComponent(descriptionTextField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, createPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(createButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(210, Short.MAX_VALUE));
        
        createPanelLayout.setHorizontalGroup(horizontalGroupCreatePanelLayout);
        
        GroupLayout.Group verticalGroupCreatePanelLayout = createPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(createPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(createPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(createPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(createButton)
                    .addComponent(cancelButton))
                .addContainerGap(18, Short.MAX_VALUE));
        
        createPanelLayout.setVerticalGroup(verticalGroupCreatePanelLayout);
        
        rolesPanel = new JPanel();
        
        rolesPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(0, 102, 102), 1, true), "Permisos", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", 3, 14), new Color(0, 0, 0))); // NOI18N

        providerCheckBox = new JCheckBox("Proveedores");
        providerCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                suplierCheckBoxActionPerformed(e);
            }
        });
        
        subjectCheckBox = new JCheckBox("Clientes");
        subjectCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                subjectCheckBoxActionPerformed(e);
            }
        });
        
        furnitureCheckBox = new JCheckBox("Mobiliarios");
        furnitureCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                furnitureCheckBoxActionPerformed(e);
            }
        });
        
        usersCheckBox = new JCheckBox("Usuarios");
        usersCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                usersCheckBoxActionPerformed(e);
            }
        });
        
        providerReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        providerReadOnlyCheckBox.setEnabled(false);
        subjectReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        subjectReadOnlyCheckBox.setEnabled(false);
        furnitureReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        furnitureReadOnlyCheckBox.setEnabled(false);
        usersReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        usersReadOnlyCheckBox.setEnabled(false);
        
        GroupLayout rolesPanelLayout = new GroupLayout(rolesPanel);
        rolesPanel.setLayout(rolesPanelLayout);
        
        GroupLayout.Group horizontalGroupRolesPanelLayout = rolesPanelLayout.createSequentialGroup();
        rolesPanelLayout.setHorizontalGroup(horizontalGroupRolesPanelLayout);
        
        horizontalGroupRolesPanelLayout.addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(rolesPanelLayout.createSequentialGroup()
                    .addGap(18,18,18)
                    .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(providerCheckBox)
                        .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                              .addComponent(providerReadOnlyCheckBox))
                        .addComponent(subjectCheckBox)
                        .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                              .addComponent(subjectReadOnlyCheckBox))
                        .addComponent(furnitureCheckBox)
                        .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                              .addComponent(furnitureReadOnlyCheckBox)))
                    .addGap(50,50,50)
                    .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(usersCheckBox)
                        .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                              .addComponent(usersReadOnlyCheckBox)))
                    .addGap(18,18,18)
                ));
        
        /*
        RolesPanelLayout.setHorizontalGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RolesPanelLayout.createSequentialGroup()
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(RolesPanelLayout.createSequentialGroup()
                                .addComponent(furnitureCheckbox)
                                .addGap(101, 101, 101)
                                .addComponent(providerCheckBox))
                            .addComponent(subjectCheckBox)
                            .addGroup(RolesPanelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(subjectReadOnlyCheckBox))))
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(furnitureReadOnlyCheckbox)
                        .addGap(75, 75, 75)
                        .addComponent(providerReadOnlyCheckBox)))
                .addContainerGap(238, Short.MAX_VALUE))
        );
        */
        GroupLayout.Group verticalGroupRolesPanelLayout = rolesPanelLayout.createSequentialGroup();
        rolesPanelLayout.setVerticalGroup(verticalGroupRolesPanelLayout);
        
        verticalGroupRolesPanelLayout.addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
        .addGroup(rolesPanelLayout.createSequentialGroup()
            .addGap(18,18,18)
            .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(providerCheckBox)
                .addComponent(usersCheckBox))
            .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(providerReadOnlyCheckBox)
                .addComponent(usersReadOnlyCheckBox))
                
            .addGap(18,18,18)
            .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(subjectCheckBox))
                
            .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addGap(18,18,18)
                .addComponent(subjectReadOnlyCheckBox))
                
            .addGap(18,18,18)
            .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(furnitureCheckBox))
            .addGroup(rolesPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addGap(18,18,18)
                .addComponent(furnitureReadOnlyCheckBox))
                
            .addGap(18,18,18)
            ));
        
        //verticalGroupRolesPanelLayout

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(createPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rolesPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(rolesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        
        setTitle("Crear Cargo");
        setBounds(360,200,600,420);
        setVisible(true);
        
    }
    
    public void suplierCheckBoxActionPerformed(ActionEvent evt) {
        if(((JCheckBox) evt.getSource()).isSelected()){
            providerReadOnlyCheckBox.setEnabled(true);
        }else{
            providerReadOnlyCheckBox.setEnabled(false);
            providerReadOnlyCheckBox.setSelected(false);
        }
    }
    
    public void subjectCheckBoxActionPerformed(ActionEvent evt){
        if(((JCheckBox) evt.getSource()).isSelected()){
            subjectReadOnlyCheckBox.setEnabled(true);
        }else{
            subjectReadOnlyCheckBox.setEnabled(false);
            subjectReadOnlyCheckBox.setSelected(false);
        }
    }
    
    public void furnitureCheckBoxActionPerformed(ActionEvent evt){
        if(((JCheckBox) evt.getSource()).isSelected()){
            furnitureReadOnlyCheckBox.setEnabled(true);
        }else{
            furnitureReadOnlyCheckBox.setEnabled(false);
            furnitureReadOnlyCheckBox.setSelected(false);
        }
    }
    
    public void usersCheckBoxActionPerformed(ActionEvent evt){
        if(((JCheckBox) evt.getSource()).isSelected()){
             usersReadOnlyCheckBox.setEnabled(true);
        }else{
             usersReadOnlyCheckBox.setEnabled(false);
             usersReadOnlyCheckBox.setSelected(false);
        }
    }
    
    public void createButtonActionPerformed(ActionEvent evt){
        String description = descriptionTextField.getText();
        boolean provider = providerCheckBox.isSelected();
        boolean subject = subjectCheckBox.isSelected();
        boolean furniture = furnitureCheckBox.isSelected();
        boolean users = usersCheckBox.isSelected();
        boolean providerReadOnly = providerReadOnlyCheckBox.isSelected();
        boolean subjectReadOnly = subjectReadOnlyCheckBox.isSelected();
        boolean furnitureReadOnly = furnitureReadOnlyCheckBox.isSelected();
        boolean usersReadOnly = usersReadOnlyCheckBox.isSelected();
        
        HashMap rolesMap = new HashMap();
        rolesMap.put("provider", provider);
        rolesMap.put("subject", subject);
        rolesMap.put("furniture", furniture);
        rolesMap.put("users", users);
        rolesMap.put("providerReadOnly", providerReadOnly);
        rolesMap.put("subjectReadOnly", subjectReadOnly);
        rolesMap.put("furnitureReadOnly", furnitureReadOnly);
        rolesMap.put("usersReadOnly", usersReadOnly);
        
        HashMap mapReturn = positionController.savePosition(description, rolesMap);
        if((Integer) mapReturn.get("status") == positionController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            cancelButtonActionPerformed(null);
        }else if((Integer)mapReturn.get("status") == positionController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void cancelButtonActionPerformed(ActionEvent evt){
        this.dispose();
        positionController.createViewClosed();
        positionController.setEnabledIndexView();
        positionController.searchPositionButtonAction();
    }
    
    @Override
    public void doDefaultCloseAction() {
        cancelButtonActionPerformed(null);
    }
}
