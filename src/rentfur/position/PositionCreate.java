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
    private final JCheckBox receiptCheckBox;
    private final JCheckBox receiptReadOnlyCheckBox;
    private final JCheckBox invoiceCheckBox;
    private final JCheckBox invoiceReadOnlyCheckBox;
    private final JCheckBox eventsCheckBox;
    private final JCheckBox eventsReadOnlyCheckBox;
    private final JCheckBox creditNoteCheckBox;
    private final JCheckBox creditNoteReadOnlyCheckBox;
    private final JCheckBox reportCheckBox;
    
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
        
        eventsCheckBox = new JCheckBox("Eventos");
        eventsCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               eventsCheckBoxActionPerformed(e);
            }

            private void eventsCheckBoxActionPerformed(ActionEvent e) {
                if(((JCheckBox) e.getSource()).isSelected()){
                    eventsReadOnlyCheckBox.setEnabled(true);
                }else{
                    eventsReadOnlyCheckBox.setEnabled(false);
                    eventsReadOnlyCheckBox.setSelected(false);
                }
            }
        });
        
        
        receiptCheckBox = new JCheckBox("Recibos");
        receiptCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               receiptsCheckBoxActionPerformed(e);
            }

            private void receiptsCheckBoxActionPerformed(ActionEvent e) {
                if(((JCheckBox) e.getSource()).isSelected()){
                    receiptReadOnlyCheckBox.setEnabled(true);
                }else{
                    receiptReadOnlyCheckBox.setEnabled(false);
                    receiptReadOnlyCheckBox.setSelected(false);
                }
            }
        });
        
        invoiceCheckBox = new JCheckBox("Facturas");
        invoiceCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               invoicesCheckBoxActionPerformed(e);
            }

            private void invoicesCheckBoxActionPerformed(ActionEvent e) {
                if(((JCheckBox) e.getSource()).isSelected()){
                    invoiceReadOnlyCheckBox.setEnabled(true);
                }else{
                    invoiceReadOnlyCheckBox.setEnabled(false);
                    invoiceReadOnlyCheckBox.setSelected(false);
                }
            }
        });
        
        
        creditNoteCheckBox = new JCheckBox("Notas de Credito");
        creditNoteCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
               creditNotesCheckBoxActionPerformed(e);
            }

            private void creditNotesCheckBoxActionPerformed(ActionEvent e) {
                if(((JCheckBox) e.getSource()).isSelected()){
                    creditNoteReadOnlyCheckBox.setEnabled(true);
                }else{
                    creditNoteReadOnlyCheckBox.setEnabled(false);
                    creditNoteReadOnlyCheckBox.setSelected(false);
                }
            }
        });
        
        reportCheckBox = new JCheckBox("Informes");

        
        providerReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        providerReadOnlyCheckBox.setEnabled(false);
        subjectReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        subjectReadOnlyCheckBox.setEnabled(false);
        furnitureReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        furnitureReadOnlyCheckBox.setEnabled(false);
        usersReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        usersReadOnlyCheckBox.setEnabled(false);
        eventsReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        eventsReadOnlyCheckBox.setEnabled(false);
        receiptReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        receiptReadOnlyCheckBox.setEnabled(false);
        invoiceReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        invoiceReadOnlyCheckBox.setEnabled(false);
        creditNoteReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        creditNoteReadOnlyCheckBox.setEnabled(false);
        
        javax.swing.GroupLayout RolesPanelLayout = new javax.swing.GroupLayout(rolesPanel);
        rolesPanel.setLayout(RolesPanelLayout);
        RolesPanelLayout.setHorizontalGroup(
            RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RolesPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(providerCheckBox)
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(providerReadOnlyCheckBox))
                    .addComponent(subjectCheckBox)
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(subjectReadOnlyCheckBox))
                    .addComponent(furnitureCheckBox)
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(furnitureReadOnlyCheckBox)))
                .addGap(58, 58, 58)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(usersCheckBox)
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(receiptReadOnlyCheckBox)
                            .addComponent(eventsReadOnlyCheckBox)
                            .addComponent(usersReadOnlyCheckBox)))
                    .addComponent(eventsCheckBox)
                    .addComponent(receiptCheckBox))
                .addGap(69, 69, 69)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reportCheckBox)
                    .addComponent(creditNoteCheckBox)
                    .addComponent(invoiceCheckBox)
                    .addGroup(RolesPanelLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(creditNoteReadOnlyCheckBox)
                            .addComponent(invoiceReadOnlyCheckBox))))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        RolesPanelLayout.setVerticalGroup(
            RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RolesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(providerCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usersCheckBox)
                    .addComponent(invoiceCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(providerReadOnlyCheckBox)
                    .addComponent(usersReadOnlyCheckBox)
                    .addComponent(invoiceReadOnlyCheckBox))
                .addGap(15, 15, 15)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectCheckBox)
                    .addComponent(eventsCheckBox)
                    .addComponent(creditNoteCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectReadOnlyCheckBox)
                    .addComponent(eventsReadOnlyCheckBox)
                    .addComponent(creditNoteReadOnlyCheckBox))
                .addGap(18, 18, 18)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(furnitureCheckBox)
                    .addComponent(receiptCheckBox)
                    .addComponent(reportCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(RolesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(furnitureReadOnlyCheckBox)
                    .addComponent(receiptReadOnlyCheckBox))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        
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
        setBounds(380,200,700,430);
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
        
        boolean event = eventsCheckBox.isSelected();
        boolean receipt = receiptCheckBox.isSelected();
        boolean invoice = invoiceCheckBox.isSelected();
        boolean creditNote = creditNoteCheckBox.isSelected();
        boolean report = reportCheckBox.isSelected();
        
        boolean providerReadOnly = providerReadOnlyCheckBox.isSelected();
        boolean subjectReadOnly = subjectReadOnlyCheckBox.isSelected();
        boolean furnitureReadOnly = furnitureReadOnlyCheckBox.isSelected();
        boolean usersReadOnly = usersReadOnlyCheckBox.isSelected();
        
        boolean eventReadOnly = eventsReadOnlyCheckBox.isSelected();
        boolean receiptReadOnly = receiptReadOnlyCheckBox.isSelected();
        boolean invoiceReadOnly = invoiceReadOnlyCheckBox.isSelected();
        boolean creditNoteReadOnly = creditNoteReadOnlyCheckBox.isSelected();
        
        HashMap rolesMap = new HashMap();
        rolesMap.put("provider", provider);
        rolesMap.put("subject", subject);
        rolesMap.put("furniture", furniture);
        rolesMap.put("users", users);
        rolesMap.put("event", event);
        rolesMap.put("receipt", receipt);
        rolesMap.put("invoice", invoice);
        rolesMap.put("creditNote", creditNote);
        rolesMap.put("report", report);
        rolesMap.put("providerReadOnly", providerReadOnly);
        rolesMap.put("subjectReadOnly", subjectReadOnly);
        rolesMap.put("furnitureReadOnly", furnitureReadOnly);
        rolesMap.put("usersReadOnly", usersReadOnly);
        rolesMap.put("eventReadOnly", eventReadOnly);
        rolesMap.put("receiptReadOnly", receiptReadOnly);
        rolesMap.put("invoiceReadOnly", invoiceReadOnly);
        rolesMap.put("creditNoteReadOnly", creditNoteReadOnly);
        
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
