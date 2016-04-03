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
public class PositionShowAndEdit extends JInternalFrame{
    private final PositionController positionController;
    private final JPanel rolesPanel;
    private JPanel createPanel = null;
    private JLabel descriptionLabel = null;
    private JTextField descriptionTextField = null;
    private final ImageIcon editIconImage;
    private final ImageIcon saveIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton saveButton;
    private final JButton cancelButton;
    private final JButton editButton;
    private final JCheckBox providerCheckBox;
    private final JCheckBox subjectCheckBox;
    private final JCheckBox furnitureCheckBox;
    private final JCheckBox usersCheckBox;
    private final JCheckBox providerReadOnlyCheckBox;
    private final JCheckBox subjectReadOnlyCheckBox;
    private final JCheckBox furnitureReadOnlyCheckBox;
    private final JCheckBox usersReadOnlyCheckBox;
    private HashMap positionMap;
    
    public PositionShowAndEdit(PositionController positionController, final int positionId){
        this.positionController = positionController;
        
        positionMap = positionController.getPositionById(positionId);
        
        createPanel = new JPanel();
        descriptionLabel = new JLabel("Descripcion:");
        descriptionTextField = new JTextField(positionMap.get("description").toString());
        descriptionTextField.setEditable(false);
        
        saveIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/save_24x24.png"));
        saveButton = new JButton(" Guardar", saveIconImage);
        saveButton.setVisible(false);
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e, positionId);
            }
        });
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setVisible(false);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonAction(e);
            }
        });
 
        editIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/edit_24x24.png"));
        editButton = new JButton(" Editar", editIconImage);
        editButton.setVisible(true);
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                editButtonAction(e);
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
                        .addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(saveButton)
                    .addComponent(editButton)    
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
        providerCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_PROVIDER)){
            providerCheckBox.setSelected(true);
        };
        
        subjectCheckBox = new JCheckBox("Clientes");
        subjectCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                subjectCheckBoxActionPerformed(e);
            }
        });
        subjectCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_SUBJECT)){
            subjectCheckBox.setSelected(true);
        };
        
        furnitureCheckBox = new JCheckBox("Mobiliarios");
        furnitureCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                furnitureCheckBoxActionPerformed(e);
            }
        });
        furnitureCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_FURNITURE)){
            furnitureCheckBox.setSelected(true);
        };
        
        usersCheckBox = new JCheckBox("Usuarios");
        usersCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                usersCheckBoxActionPerformed(e);
            }
        });
        usersCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_USER)){
            usersCheckBox.setSelected(true);
        };
        
        providerReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        providerReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_PROVIDER) && (Boolean) positionMap.get(positionController.ROLE_RF_PROVIDER)){
            providerReadOnlyCheckBox.setSelected(true);
        };
        
        subjectReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        subjectReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_SUBJECT) && (Boolean) positionMap.get(positionController.ROLE_RF_SUBJECT)){
            subjectReadOnlyCheckBox.setSelected(true);
        };
        
        furnitureReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        furnitureReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_FURNITURE) && (Boolean) positionMap.get(positionController.ROLE_RF_FURNITURE)){
            furnitureReadOnlyCheckBox.setSelected(true);
        };
        
        usersReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        usersReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_USER) && (Boolean) positionMap.get(positionController.ROLE_RF_USER)){
            usersReadOnlyCheckBox.setSelected(true);
        };
        
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
        
        setTitle("Cargo ["+positionMap.get("description").toString()+"]");
        setBounds(360,200,600,420);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        positionController.showAndEditViewClosed();
        positionController.setEnabledIndexView();
        positionController.searchPositionButtonAction();
    }
    
    public void editButtonAction(ActionEvent e){
        descriptionTextField.setEditable(true);
        
        providerCheckBox.setEnabled(true);
        if(providerCheckBox.isSelected()){
            providerReadOnlyCheckBox.setEnabled(true);
        }
        
        subjectCheckBox.setEnabled(true);
        if(subjectCheckBox.isSelected()){
            subjectReadOnlyCheckBox.setEnabled(true);
        }
        
        furnitureCheckBox.setEnabled(true);
        if(furnitureCheckBox.isSelected()){
            furnitureReadOnlyCheckBox.setEnabled(true);
        }
        
        usersCheckBox.setEnabled(true);
        if(usersCheckBox.isSelected()){
            usersReadOnlyCheckBox.setEnabled(true);
        }
        
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
    }
    
    private void cancelButtonAction(ActionEvent e) {
        descriptionTextField.setEditable(false);
        
        providerCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_PROVIDER)){
            providerCheckBox.setSelected(true);
        }else{
             providerCheckBox.setSelected(false);
        };
        providerReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_PROVIDER) && (Boolean) positionMap.get(positionController.ROLE_RF_PROVIDER)){
            providerReadOnlyCheckBox.setSelected(true);
        }else{
            providerReadOnlyCheckBox.setSelected(false);
        };
        
        subjectCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_SUBJECT)){
            subjectCheckBox.setSelected(true);
        }else{
             subjectCheckBox.setSelected(false);
        };
        subjectReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_SUBJECT) && (Boolean) positionMap.get(positionController.ROLE_RF_SUBJECT)){
            subjectReadOnlyCheckBox.setSelected(true);
        }else{
            subjectReadOnlyCheckBox.setSelected(false);
        };
        
        furnitureCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_FURNITURE)){
            furnitureCheckBox.setSelected(true);
        }else{
            furnitureCheckBox.setSelected(false);
        };
        furnitureReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_FURNITURE) && (Boolean) positionMap.get(positionController.ROLE_RF_FURNITURE)){
            furnitureReadOnlyCheckBox.setSelected(true);
        }else{
            furnitureReadOnlyCheckBox.setSelected(false);
        };
        
        usersCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_USER)){
            usersCheckBox.setSelected(true);
        }else{
            usersCheckBox.setSelected(false);
        };
        usersReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_USER) && (Boolean) positionMap.get(positionController.ROLE_RF_USER)){
            usersReadOnlyCheckBox.setSelected(true);
        }else{
            usersReadOnlyCheckBox.setSelected(false);
        };
        
        editButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
    }
    
    public void saveButtonAction(ActionEvent e, int positionId){
        String description = descriptionTextField.getText();
        
        boolean provider = providerCheckBox.isSelected();
        boolean providerOnlyQuery = providerReadOnlyCheckBox.isSelected();
        
        boolean subject = subjectCheckBox.isSelected();
        boolean subjectOnlyQuery = subjectReadOnlyCheckBox.isSelected();
        
        boolean furniture = furnitureCheckBox.isSelected();
        boolean furnitureOnlyQuery = furnitureReadOnlyCheckBox.isSelected();
        
        boolean user = usersCheckBox.isSelected();
        boolean userOnlyQuery = usersReadOnlyCheckBox.isSelected();
        
        HashMap mapReturn = positionController.updatePosition(positionId, positionMap, description, provider, providerOnlyQuery, subject, subjectOnlyQuery,
                                                              furniture, furnitureOnlyQuery, user, userOnlyQuery);
        if((Integer) mapReturn.get("status") == positionController.SUCCESFULLY_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            positionMap = positionController.getPositionById(positionId);
            cancelButtonAction(null);
        }else if((Integer)mapReturn.get("status") == positionController.ERROR_IN_SAVED){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
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
    
}
