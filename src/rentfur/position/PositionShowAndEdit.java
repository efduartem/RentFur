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
    private final JCheckBox receiptCheckBox;
    private final JCheckBox receiptReadOnlyCheckBox;
    private final JCheckBox invoiceCheckBox;
    private final JCheckBox invoiceReadOnlyCheckBox;
    private final JCheckBox eventsCheckBox;
    private final JCheckBox eventsReadOnlyCheckBox;
    private final JCheckBox creditNoteCheckBox;
    private final JCheckBox creditNoteReadOnlyCheckBox;
    private final JCheckBox reportCheckBox;
    private HashMap positionMap;
    
    @SuppressWarnings("empty-statement")
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

        //PROVIDER
        providerCheckBox = new JCheckBox("Proveedores");
        providerCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suplierCheckBoxActionPerformed(e);
            }
        });
        providerCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_PROVIDER)){
            providerCheckBox.setSelected(true);
        };
        
        providerReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        providerReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_PROVIDER) && (Boolean) positionMap.get(PositionController.ROLE_RF_PROVIDER)){
            providerReadOnlyCheckBox.setSelected(true);
        };
        
        //SUBJECT
        subjectCheckBox = new JCheckBox("Clientes");
        subjectCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                subjectCheckBoxActionPerformed(e);
            }
        });
        subjectCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_SUBJECT)){
            subjectCheckBox.setSelected(true);
        };
        
        subjectReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        subjectReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_SUBJECT) && (Boolean) positionMap.get(PositionController.ROLE_RF_SUBJECT)){
            subjectReadOnlyCheckBox.setSelected(true);
        };
        
        //FURNITURE
        furnitureCheckBox = new JCheckBox("Mobiliarios");
        furnitureCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                furnitureCheckBoxActionPerformed(e);
            }
        });
        furnitureCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_FURNITURE)){
            furnitureCheckBox.setSelected(true);
        };
        
        furnitureReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        furnitureReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(positionController.ROLE_RF_FURNITURE) && (Boolean) positionMap.get(positionController.ROLE_RF_FURNITURE)){
            furnitureReadOnlyCheckBox.setSelected(true);
        };
        
        //USERS
        usersCheckBox = new JCheckBox("Usuarios");
        usersCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                usersCheckBoxActionPerformed(e);
            }
        });
        usersCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_USER)){
            usersCheckBox.setSelected(true);
        };
        
        usersReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        usersReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_USER) && (Boolean) positionMap.get(PositionController.ROLE_RF_USER)){
            usersReadOnlyCheckBox.setSelected(true);
        };
        
        //EVENTS
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
        eventsCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_EVENTS)){
            eventsCheckBox.setSelected(true);
        };
        
        eventsReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        eventsReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_EVENTS) && (Boolean) positionMap.get(PositionController.ROLE_RF_EVENTS)){
            eventsReadOnlyCheckBox.setSelected(true);
        };
        
        //RECEIPTS
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
        receiptCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_RECEIPTS)){
            receiptCheckBox.setSelected(true);
        };
        
        receiptReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        receiptReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_RECEIPTS) && (Boolean) positionMap.get(PositionController.ROLE_RF_RECEIPTS)){
            receiptReadOnlyCheckBox.setSelected(true);
        };

        //INVOICES
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
        invoiceCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_INVOICES)){
            invoiceCheckBox.setSelected(true);
        };
        
        invoiceReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        invoiceReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_INVOICES) && (Boolean) positionMap.get(PositionController.ROLE_RF_INVOICES)){
            invoiceReadOnlyCheckBox.setSelected(true);
        };
        
        //CREDIT NOTES
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
        creditNoteCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_CREDIT_NOTES)){
            creditNoteCheckBox.setSelected(true);
        };
        
        creditNoteReadOnlyCheckBox = new JCheckBox("[Solo Consulta]");
        creditNoteReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_CREDIT_NOTES) && (Boolean) positionMap.get(PositionController.ROLE_RF_CREDIT_NOTES)){
            creditNoteReadOnlyCheckBox.setSelected(true);
        };
        
        //REPORTS
        reportCheckBox = new JCheckBox("Informes");
        reportCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_REPORTS)){
            reportCheckBox.setSelected(true);
        };
        
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
        setBounds(380,200,700,430);
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
        
        eventsCheckBox.setEnabled(true);
        if(eventsCheckBox.isSelected()){
            eventsReadOnlyCheckBox.setEnabled(true);
        }
        
        receiptCheckBox.setEnabled(true);
        if(receiptCheckBox.isSelected()){
            receiptReadOnlyCheckBox.setEnabled(true);
        }
        
        invoiceCheckBox.setEnabled(true);
        if(invoiceCheckBox.isSelected()){
            invoiceReadOnlyCheckBox.setEnabled(true);
        }
        
        creditNoteCheckBox.setEnabled(true);
        if(creditNoteCheckBox.isSelected()){
            creditNoteReadOnlyCheckBox.setEnabled(true);
        }
        
        reportCheckBox.setEnabled(true);
        
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
    }
    
    @SuppressWarnings("empty-statement")
    private void cancelButtonAction(ActionEvent e) {
        descriptionTextField.setEditable(false);
        
        providerCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_PROVIDER)){
            providerCheckBox.setSelected(true);
        }else{
             providerCheckBox.setSelected(false);
        };
        providerReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_PROVIDER) && (Boolean) positionMap.get(PositionController.ROLE_RF_PROVIDER)){
            providerReadOnlyCheckBox.setSelected(true);
        }else{
            providerReadOnlyCheckBox.setSelected(false);
        };
        
        subjectCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_SUBJECT)){
            subjectCheckBox.setSelected(true);
        }else{
             subjectCheckBox.setSelected(false);
        };
        subjectReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_SUBJECT) && (Boolean) positionMap.get(PositionController.ROLE_RF_SUBJECT)){
            subjectReadOnlyCheckBox.setSelected(true);
        }else{
            subjectReadOnlyCheckBox.setSelected(false);
        };
        
        furnitureCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_FURNITURE)){
            furnitureCheckBox.setSelected(true);
        }else{
            furnitureCheckBox.setSelected(false);
        };
        furnitureReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_FURNITURE) && (Boolean) positionMap.get(PositionController.ROLE_RF_FURNITURE)){
            furnitureReadOnlyCheckBox.setSelected(true);
        }else{
            furnitureReadOnlyCheckBox.setSelected(false);
        };
        
        usersCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_USER)){
            usersCheckBox.setSelected(true);
        }else{
            usersCheckBox.setSelected(false);
        };
        usersReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_USER) && (Boolean) positionMap.get(PositionController.ROLE_RF_USER)){
            usersReadOnlyCheckBox.setSelected(true);
        }else{
            usersReadOnlyCheckBox.setSelected(false);
        };
        
        eventsCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_EVENTS)){
            eventsCheckBox.setSelected(true);
        }else{
            eventsCheckBox.setSelected(false);
        };
        eventsReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_EVENTS) && (Boolean) positionMap.get(PositionController.ROLE_RF_EVENTS)){
            eventsReadOnlyCheckBox.setSelected(true);
        }else{
            eventsReadOnlyCheckBox.setSelected(false);
        };
        
        receiptCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_RECEIPTS)){
            receiptCheckBox.setSelected(true);
        }else{
            receiptCheckBox.setSelected(false);
        };
        receiptReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_RECEIPTS) && (Boolean) positionMap.get(PositionController.ROLE_RF_RECEIPTS)){
            receiptReadOnlyCheckBox.setSelected(true);
        }else{
            receiptReadOnlyCheckBox.setSelected(false);
        };
        
        invoiceCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_INVOICES)){
            invoiceCheckBox.setSelected(true);
        }else{
            invoiceCheckBox.setSelected(false);
        };
        invoiceReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_INVOICES) && (Boolean) positionMap.get(PositionController.ROLE_RF_INVOICES)){
            invoiceReadOnlyCheckBox.setSelected(true);
        }else{
            invoiceReadOnlyCheckBox.setSelected(false);
        };
        
        creditNoteCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_CREDIT_NOTES)){
            creditNoteCheckBox.setSelected(true);
        }else{
            creditNoteCheckBox.setSelected(false);
        };
        creditNoteReadOnlyCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_CREDIT_NOTES) && (Boolean) positionMap.get(PositionController.ROLE_RF_CREDIT_NOTES)){
            creditNoteReadOnlyCheckBox.setSelected(true);
        }else{
            creditNoteReadOnlyCheckBox.setSelected(false);
        };
        
        reportCheckBox.setEnabled(false);
        if(positionMap.containsKey(PositionController.ROLE_RF_REPORTS)){
            reportCheckBox.setSelected(true);
        }else{
            reportCheckBox.setSelected(false);
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
        
        boolean event = eventsCheckBox.isSelected();
        boolean eventOnlyQuery = eventsReadOnlyCheckBox.isSelected();
        
        boolean receipt = receiptCheckBox.isSelected();
        boolean receiptOnlyQuery = receiptReadOnlyCheckBox.isSelected();
        
        boolean invoice = invoiceCheckBox.isSelected();
        boolean invoiceOnlyQuery = invoiceReadOnlyCheckBox.isSelected();
        
        boolean creditNote = creditNoteCheckBox.isSelected();
        boolean creditNoteOnlyQuery = creditNoteReadOnlyCheckBox.isSelected();
        
        boolean report = reportCheckBox.isSelected();
        
        HashMap mapReturn = positionController.updatePosition(positionId, positionMap, description, provider, providerOnlyQuery, subject, subjectOnlyQuery, furniture, furnitureOnlyQuery, user, userOnlyQuery, event, eventOnlyQuery, receipt, receiptOnlyQuery, invoice, invoiceOnlyQuery, creditNote, creditNoteOnlyQuery, report);
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
