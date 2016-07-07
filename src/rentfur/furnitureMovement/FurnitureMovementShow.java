/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furnitureMovement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.java.balloontip.BalloonTip;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.util.DateLabelFormatter;
import rentfur.furniture.FurnitureController;
import rentfur.user.UserController;

/**
 *
 * @author FDuarte
 */
public class FurnitureMovementShow extends JInternalFrame{
    private final FurnitureMovementController furnitureMovementController;
    private final JPanel movementShowPanel;
    private final JLabel movementTypeLabel;
    private final JLabel titleLabel;
    private final JLabel conceptLabel;
    private final JLabel documentTypeLabel;
    private final JLabel documentNumberLabel;
    private final JLabel documentDateLabel;
    private final JLabel creatorUserLabel;
    private final JLabel movementDateLabel;
    private final JLabel movementNumberLabel;
    private final JTextField movementTypeTextField;
    private final JTextField conceptTextField;
    private final JTextField documentTypeTextField;
    private final JTextField documentNumberTextField;
    private final JTextField documentDateTextField;
    private final JTextField creatorUserTextField;
    private final JDatePickerImpl movementDateDatePicker;
    private final JTextField movementNumberTextField;
    private final JButton cancelButton;
    private final JTable movementDetailTable;
    private final DefaultTableModel movementDetailDefaultTableModel;
    private final JScrollPane movementDetailTableJScrollPane;
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    private final ImageIcon helpIconImage;
    private final JLabel helpLabel;
    private BalloonTip helpBalloonTip;
    
    private final int ID_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int DESCRIPTION_COLUMN = 2;
    private final int FAMILY_COLUMN = 3;
    private final int QUANTITY_COLUMN = 4;
    private final int COST_COLUMN = 5;
    private final int AMOUNT_COLUMN = 6;
    
    private final HashMap movementMap;
    
    public FurnitureMovementShow(FurnitureMovementController furnitureMovementController, int movementId){
        this.furnitureMovementController = furnitureMovementController;
        
        movementShowPanel = new JPanel();
        movementShowPanel.setLayout(null);
        
        titleLabel = new JLabel();
        
        movementMap = FurnitureMovementController.getMovementById(movementId);
        
        if(Integer.valueOf(movementMap.get("movement_type").toString())==FurnitureMovementController.MOVEMENT_TYPE_INPUT){
            titleLabel.setText("<HTML><U>Movimiento de Entrada</U></HTML>");
        }else{
            titleLabel.setText("<HTML><U>Movimiento de Salida</U></HTML>");
        }
        
        titleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 25));
        titleLabel.setBounds(400, 20, 300, 25);
        movementShowPanel.add(titleLabel);
        
        movementDateLabel = new JLabel("Fecha:");
        movementDateLabel.setBounds(30, 60, 130, 25);
        movementShowPanel.add(movementDateLabel);
        
        UtilDateModel model = new UtilDateModel(new Date(((Timestamp)movementMap.get("movement_date")).getTime()));
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        movementDateDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        movementDateDatePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(movementDetailDefaultTableModel.getRowCount()>0){
                    //showDayChangeWarning();
                }else{
                    //updateCurrentDeliveryDateSelected();
                }
            }
        });
        movementDateDatePicker.getComponent(1).setEnabled(false);
        movementDateDatePicker.setBounds(170, 60, 170, 25);
        movementShowPanel.add(movementDateDatePicker);
        
        movementNumberLabel = new JLabel("Numero:");
        movementNumberLabel.setBounds(30, 90, 130, 25);
        movementShowPanel.add(movementNumberLabel);
        
        movementNumberTextField = new JTextField(movementMap.get("movement_number").toString());
        movementNumberTextField.setEditable(false);
        movementNumberTextField.setBounds(170, 90, 120, 25);
        movementShowPanel.add(movementNumberTextField);
        
        //FILA 1
        movementTypeLabel = new JLabel("Tipo:");
        movementTypeLabel.setBounds(30, 120, 80, 25);
        movementShowPanel.add(movementTypeLabel);
        
        movementTypeTextField = new JTextField(FurnitureMovementController.getFurnitureMovementType(Integer.valueOf(movementMap.get("movement_type").toString())));
        movementTypeTextField.setEditable(false);
        movementTypeTextField.setBounds(170, 120, 170, 25);
        movementShowPanel.add(movementTypeTextField);
        
        conceptLabel = new JLabel("Concepto:");
        conceptLabel.setBounds(30, 150, 160, 25);
        movementShowPanel.add(conceptLabel);
        
        conceptTextField = new JTextField(movementMap.get("concept").toString());
        conceptTextField.setEditable(false);
        conceptTextField.setBounds(170, 150, 300, 25);
        movementShowPanel.add(conceptTextField);
        
        //FILA 2
        documentTypeLabel = new JLabel("Tipo de Documento:");
        documentTypeLabel.setBounds(30, 180, 150, 25);
        movementShowPanel.add(documentTypeLabel);
        
        documentTypeTextField = new JTextField();
        if(movementMap.get("document_type")==null){
            documentTypeTextField.setText("");
        }else{
            documentTypeTextField.setText(movementMap.get("document_type").toString());
        }
        documentTypeTextField.setEditable(false);
        documentTypeTextField.setBounds(170, 180, 170, 25);
        movementShowPanel.add(documentTypeTextField);
        
        documentNumberLabel = new JLabel("Nro. de Documento:");
        documentNumberLabel.setBounds(380, 180, 150, 25);
        movementShowPanel.add(documentNumberLabel);
        
        documentNumberTextField = new JTextField();
        if(movementMap.get("document_number")==null){
            documentNumberTextField.setText("");
        }else{
            documentNumberTextField.setText(movementMap.get("document_number").toString());
        }
        documentNumberTextField.setEditable(false);
        documentNumberTextField.setBounds(510, 180, 170, 25);
        movementShowPanel.add(documentNumberTextField);

        //FILA 3
        documentDateLabel = new JLabel("Fecha del Documento:");
        documentDateLabel.setBounds(30, 210, 130, 25);
        movementShowPanel.add(documentDateLabel);
        
        documentDateTextField = new JTextField();
        if(movementMap.get("document_date")==null){
            documentDateTextField.setText("");
        }else{
            documentDateTextField.setText(movementMap.get("document_date").toString());
        }
        documentDateTextField.setEditable(false);
        documentDateTextField.setBounds(170, 210, 170, 25);
        movementShowPanel.add(documentDateTextField);
        
        //FILA 4
        creatorUserLabel = new JLabel("Usuario:");
        creatorUserLabel.setBounds(30, 240, 80, 25);
        movementShowPanel.add(creatorUserLabel);
        
        HashMap creatorUserMap = UserController.getUserById(Integer.valueOf(movementMap.get("creator_user_id").toString()));
        creatorUserTextField = new JTextField(creatorUserMap.get("fullname").toString());
        creatorUserTextField.setEditable(false);
        creatorUserTextField.setBounds(170, 240, 170, 25);
        movementShowPanel.add(creatorUserTextField);
        
        movementDetailDefaultTableModel = new MovementDetailDefaultTableModel();
        movementDetailTable = new JTable(movementDetailDefaultTableModel);
        
        movementDetailDefaultTableModel.addColumn("Id");
        movementDetailDefaultTableModel.addColumn("Código");
        movementDetailDefaultTableModel.addColumn("Descripción");
        movementDetailDefaultTableModel.addColumn("Familia");
        if(Integer.valueOf(movementMap.get("movement_type").toString())==FurnitureMovementController.MOVEMENT_TYPE_INPUT){
            movementDetailDefaultTableModel.addColumn("Cantidad Entrante");   
        }else{
            movementDetailDefaultTableModel.addColumn("Cantidad Saliente");   
        }
        movementDetailDefaultTableModel.addColumn("Costo");
        movementDetailDefaultTableModel.addColumn("Importe");
        
        movementDetailTable.setRowHeight(23);
        
        //Alineacion a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //ID
        movementDetailTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        movementDetailTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        movementDetailTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        
        //CODE
        movementDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(85);
        movementDetailTable.getColumnModel().getColumn(CODE_COLUMN).setMinWidth(85);
        movementDetailTable.getColumnModel().getColumn(CODE_COLUMN).setPreferredWidth(85);
        
        //DESCRIPTION
        movementDetailTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setMinWidth(250);
        movementDetailTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setMaxWidth(350);
        movementDetailTable.getColumnModel().getColumn(DESCRIPTION_COLUMN).setPreferredWidth(300);
        
        //DESCRIPTION
        movementDetailTable.getColumnModel().getColumn(FAMILY_COLUMN).setMinWidth(150);
        movementDetailTable.getColumnModel().getColumn(FAMILY_COLUMN).setMaxWidth(150);
        movementDetailTable.getColumnModel().getColumn(FAMILY_COLUMN).setPreferredWidth(15);
        
        //QUANTITY
        movementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setMinWidth(100);
        movementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setMaxWidth(130);
        movementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setPreferredWidth(120);
        movementDetailTable.getColumnModel().getColumn(QUANTITY_COLUMN).setCellRenderer(new QuantityCellRenderer());
        
        //COST
        movementDetailTable.getColumnModel().getColumn(COST_COLUMN).setMinWidth(100);
        movementDetailTable.getColumnModel().getColumn(COST_COLUMN).setMaxWidth(130);
        movementDetailTable.getColumnModel().getColumn(COST_COLUMN).setPreferredWidth(120);
        movementDetailTable.getColumnModel().getColumn(COST_COLUMN).setCellRenderer(new CostCellRenderer());
        
        //SUB TOTAL
        movementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMinWidth(100);
        movementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMaxWidth(130);
        movementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setPreferredWidth(120);
        movementDetailTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellRenderer(rightRenderer);
        
        movementDetailTableJScrollPane = new JScrollPane();
        movementDetailTableJScrollPane.setBounds(30, 290, 910, 280);
        movementDetailTableJScrollPane.setViewportView(movementDetailTable);
        
        HashMap paymentMethodMap;
        ArrayList paymentList = (ArrayList) movementMap.get("movementDetail");
        Object[] row = new Object[movementDetailTable.getColumnCount()];
        
        HashMap furnitureFamilyMap;
        
        for(int i = 0; i < paymentList.size(); i++){
            paymentMethodMap = (HashMap) paymentList.get(i);

            row[ID_COLUMN] = paymentMethodMap.get("id");
            row[CODE_COLUMN] = paymentMethodMap.get("furniture_code");
            row[DESCRIPTION_COLUMN] = paymentMethodMap.get("furniture_description");

            furnitureFamilyMap = FurnitureController.getFurnitureFamilyByCode(paymentMethodMap.get("furniture_code").toString());
            row[FAMILY_COLUMN] = furnitureFamilyMap.get("description").toString();
            
            if(Integer.valueOf(movementMap.get("movement_type").toString())==FurnitureMovementController.MOVEMENT_TYPE_INPUT){
                row[QUANTITY_COLUMN] = amountFormat.format(Integer.valueOf(paymentMethodMap.get("quantity_input").toString()));
            }else{
                row[QUANTITY_COLUMN] = amountFormat.format(Integer.valueOf(paymentMethodMap.get("quantity_output").toString()));
            }
            
            row[COST_COLUMN] = amountFormat.format((Double)paymentMethodMap.get("amount"));
            row[AMOUNT_COLUMN] = amountFormat.format((Double)paymentMethodMap.get("sub_total"));
        
            movementDetailDefaultTableModel.addRow(row);
        }
        
        ImageIcon cancelImageIcon = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton();
        cancelButton.setText(" Cerrar");
        cancelButton.setIcon(cancelImageIcon);
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        cancelButton.setBounds(700, 590, 170, 32);
        movementShowPanel.add(cancelButton);
        
        helpIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/help_24x24.png"));
        helpLabel = new JLabel("AYUDA");
        helpLabel.setIcon(helpIconImage);
        helpLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showHelp();
                    }
        });
        helpLabel.setBounds(900, 20, 300, 25);
        movementShowPanel.add(helpLabel);

        helpBalloonTip = new BalloonTip(helpLabel, "<html><head></head><body style='background:#F4EFEF;'><div style='margin:24px 34px;'><h2>Detalles de Movimientos de Mobiliario</h2><p>En esta vista se muestran los datos del movimiento seleccionado. En la cabecera se encuentran los datos:</p><ul><li><p>Fecha</p></li><li><p>Numero</p></li><li><p>Tipo</p></li><li><p>Concepto</p></li><li><p>Tipo de Documento</p></li><li><p>Numero de Documento</p></li><li><p>Fecha del Documento</p></li><li><p>Usuario</p></li></ul><p>En la seccion de abajo se muestra la tabla de detalles del movimiento con los datos:</p><ul><li><p>Codigo</p></li><li><p>Descripcion</p></li><li><p>Familia</p></li><li><p>Cantidad entrante</p></li><li><p>Costo</p></li><li><p>Importe</p></li></ul></div></body></html>");
        helpBalloonTip.setVisible(false);
        helpBalloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false);
        
        add(movementDetailTableJScrollPane);
        add(movementShowPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Detalles de Movimiento");
        setBounds(400,120,1000,700);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
    }

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        furnitureMovementController.setFurnitureMovementShowClosed();
    }
    
    
    
    private class MovementDetailDefaultTableModel extends DefaultTableModel{
        
        @Override
        public boolean isCellEditable(int row, int column) {
            switch(column){
                default:    return false;
            }
        }
           
    }
    
    class QuantityCellRenderer extends JTextField implements TableCellRenderer {

        public QuantityCellRenderer() {
            setOpaque(true);
            setBackground(new Color(237, 247, 243));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Cantidad");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    private class CostCellRenderer extends JTextField implements TableCellRenderer {

        public CostCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
            setHorizontalAlignment(JLabel.RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
            setToolTipText("Costo");
            setHorizontalAlignment(JLabel.RIGHT);
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    public void showHelp(){
        if(!helpBalloonTip.isVisible()){
            helpBalloonTip.setVisible(true);
        }
    }
}
