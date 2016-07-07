/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.purchaseInvoice;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import net.java.balloontip.BalloonTip;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.position.PositionController;
import rentfur.provider.ProviderIndex;
import rentfur.util.ComboBoxItem;
import rentfur.util.DateLabelFormatter;
import rentfur.util.NumericTextField;
import rentfur.util.UserRoles;

/**
 *
 * @author hp
 */
public class PurchaseInvoiceIndex extends JInternalFrame{
    private final PurchaseInvoiceController purchaseInvoiceController;
    private PurchaseInvoiceCreate purchaseInvoiceCreate;
    private PurchaseInvoiceShow purchaseInvoiceShow;
    private final JPanel purchaseInvoiceIndexParamsPanel;
    private boolean onlyQuery = false;
    public final JLabel codeLabel;
    public final JLabel nameLabel;
    public final JLabel invoiceNumberLabel;
    public final JLabel invoicingDateLabel;
    public final JLabel tradenameLabel;
    public final JLabel fiscalNumberLabel;
    
    public final JTextField codeTextField;
    public final JTextField nameTextField;
    public final JTextField invoiceNumberTextField;
    public final JTextField tradenameTextField;
    private final NumericTextField fiscalNumberTextField;
    private JDatePickerImpl datePicker;
    private final DecimalFormat amountFormat;
    
    private JDesktopPane purchaseIndexPane;
    
    private final JButton recordPurchaseInvoiceButton;
    private final ImageIcon searchIconImage;
    private final JButton searchButton;
    
    private final JTable purchaseInvoicesResultTable;
    private final DefaultTableModel purchaseInvoicesResultDefaultTableModel;
    private final JScrollPane purchaseInvoicesResultTableJScrollPane;
    
    private final int ID_COLUMN = 0;
    private final int CODE_COLUMN = 1;
    private final int NAME_COLUMN = 2;
    private final int TRADENAME_COLUMN = 3;
    private final int FISCAL_NUMBER_COLUMN = 4;//NO SE USA
    private final int NUMBER_COLUMN = 5;
    private final int AMOUNT_COLUMN = 6;//NO SE USA
    private final int DATE_COLUMN = 7;
    private final int SHOW_COLUMN = 8;
    
    private final ImageIcon helpIconImage;
    private final JLabel helpLabel;
    private BalloonTip helpBalloonTip;
    public PurchaseInvoiceIndex(PurchaseInvoiceController purchaseInvoiceController, UserRoles userRoles){
        this.purchaseInvoiceController = purchaseInvoiceController;
        
//        if((Boolean)userRoles.getRolesMap().get(PositionController.ROLE_RF_PROVIDER)){
//            onlyQuery = true;
//        }
        
        purchaseInvoiceIndexParamsPanel = new JPanel();
        purchaseInvoiceIndexParamsPanel.setLayout(null);
        
        //FILA 1
        invoiceNumberLabel = new JLabel("Numero factura:");
        invoiceNumberLabel.setBounds(30, 50, 160, 25);
        purchaseInvoiceIndexParamsPanel.add(invoiceNumberLabel);
        
        invoiceNumberTextField = new JTextField();
        invoiceNumberTextField.setBounds(160, 50, 170, 25);
        purchaseInvoiceIndexParamsPanel.add(invoiceNumberTextField);
        
        invoicingDateLabel = new JLabel("Fecha de Factura:");
        invoicingDateLabel.setBounds(370, 50, 160, 25);
        purchaseInvoiceIndexParamsPanel.add(invoicingDateLabel);
        
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        datePicker.setBounds(480, 50, 170, 25);
        purchaseInvoiceIndexParamsPanel.add(datePicker);
        
        //FILA 2
        codeLabel = new JLabel("Codigo Proveedor:");
        codeLabel.setBounds(30, 80, 130, 25);
        purchaseInvoiceIndexParamsPanel.add(codeLabel);
        
        codeTextField = new JTextField();
        codeTextField.setBounds(160, 80, 170, 25);
        purchaseInvoiceIndexParamsPanel.add(codeTextField);
        
        nameLabel = new JLabel("Razon Social:");
        nameLabel.setBounds(370, 80, 100, 25);
        purchaseInvoiceIndexParamsPanel.add(nameLabel);
        
        nameTextField = new JTextField();
        nameTextField.setBounds(480, 80, 170, 25);
        purchaseInvoiceIndexParamsPanel.add(nameTextField);
        
        //FILA 3
        tradenameLabel = new JLabel("Nombre Comercial:");
        tradenameLabel.setBounds(30, 110, 120, 25);
        purchaseInvoiceIndexParamsPanel.add(tradenameLabel);
        
        tradenameTextField = new JTextField();
        tradenameTextField.setBounds(160, 110, 170, 25);
        purchaseInvoiceIndexParamsPanel.add(tradenameTextField);
        
        fiscalNumberLabel = new JLabel("RUC / CI:");
        fiscalNumberLabel.setBounds(370, 110, 120, 25);
        purchaseInvoiceIndexParamsPanel.add(fiscalNumberLabel);
        
        amountFormat = new DecimalFormat("#,###");
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        fiscalNumberTextField = new NumericTextField(20, amountFormat);
        fiscalNumberTextField.setBounds(480, 110, 170, 25);
        fiscalNumberTextField.addKeyListener(new KeyListener() {

                     @Override
                     public void keyTyped(KeyEvent e) {
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
                         }else{
                             texto = texto.replaceAll(",", ".");
                         }
                     }
                 });

        purchaseInvoiceIndexParamsPanel.add(fiscalNumberTextField);
        
        //BOTON PARA CREAR FACTURA DE COMPRA
        ImageIcon createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/shopping_cart_24x24.png"));
        recordPurchaseInvoiceButton = new JButton("  Registrar Compra", createIconImage);
        recordPurchaseInvoiceButton.setBounds(160, 190, 180, 32);
        recordPurchaseInvoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getPurchaseInvoiceRecordView();
            }
        });
        purchaseInvoiceIndexParamsPanel.add(recordPurchaseInvoiceButton);
        
        //BOTON DE BUSQUEDA
        searchIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/search_24x24.png"));
        searchButton = new JButton("  Buscar", searchIconImage);
        searchButton.setBounds(30, 190, 120, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProviderButtonAction(e);
            }
        });
        purchaseInvoiceIndexParamsPanel.add(searchButton);
        
        
        //TABLA DE RESULTADOS
        purchaseInvoicesResultDefaultTableModel = new PurchaseInvoicesIndextResultDefaultTableModel();
        purchaseInvoicesResultTable = new JTable(purchaseInvoicesResultDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda centrada
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                
        purchaseInvoiceController.setPurchaseInvoicesIndexResultsTable(purchaseInvoicesResultDefaultTableModel, false, null, null, null, null, null, null);
        purchaseInvoicesResultTable.setRowHeight(23);
        
        //ID
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setMaxWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setMinWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(0);
        purchaseInvoicesResultTable.getColumnModel().getColumn(ID_COLUMN).setResizable(false);
        
        //Codigo Proveedor
        purchaseInvoicesResultTable.getColumnModel().getColumn(CODE_COLUMN).setMaxWidth(200);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CODE_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CODE_COLUMN).setPreferredWidth(180);
        purchaseInvoicesResultTable.getColumnModel().getColumn(CODE_COLUMN).setCellRenderer(centerRenderer);
         
        //Nombre Proveedor
        purchaseInvoicesResultTable.getColumnModel().getColumn(NAME_COLUMN).setMaxWidth(200);
        purchaseInvoicesResultTable.getColumnModel().getColumn(NAME_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(NAME_COLUMN).setPreferredWidth(180);
        
        //Nombre Comercial Proveedor
        purchaseInvoicesResultTable.getColumnModel().getColumn(TRADENAME_COLUMN).setMaxWidth(210);
        purchaseInvoicesResultTable.getColumnModel().getColumn(TRADENAME_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(TRADENAME_COLUMN).setPreferredWidth(200);
        
        //RUC Proveedor
        purchaseInvoicesResultTable.getColumnModel().getColumn(FISCAL_NUMBER_COLUMN).setMaxWidth(200);
        purchaseInvoicesResultTable.getColumnModel().getColumn(FISCAL_NUMBER_COLUMN).setMinWidth(100);
        purchaseInvoicesResultTable.getColumnModel().getColumn(FISCAL_NUMBER_COLUMN).setPreferredWidth(180);
        
        //NUMERO
        purchaseInvoicesResultTable.getColumnModel().getColumn(NUMBER_COLUMN).setMaxWidth(280);
        purchaseInvoicesResultTable.getColumnModel().getColumn(NUMBER_COLUMN).setMinWidth(150);
        purchaseInvoicesResultTable.getColumnModel().getColumn(NUMBER_COLUMN).setPreferredWidth(270);
        purchaseInvoicesResultTable.getColumnModel().getColumn(NUMBER_COLUMN).setCellRenderer(centerRenderer);
        
        //MONTO
        purchaseInvoicesResultTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMaxWidth(200);
        purchaseInvoicesResultTable.getColumnModel().getColumn(AMOUNT_COLUMN).setMinWidth(130);
        purchaseInvoicesResultTable.getColumnModel().getColumn(AMOUNT_COLUMN).setPreferredWidth(190);
        purchaseInvoicesResultTable.getColumnModel().getColumn(AMOUNT_COLUMN).setCellRenderer(rightRenderer);
        
        //FECHA
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setMaxWidth(200);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setMinWidth(130);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setPreferredWidth(190);
        purchaseInvoicesResultTable.getColumnModel().getColumn(DATE_COLUMN).setCellRenderer(centerRenderer);
        
        //VER
        purchaseInvoicesResultTable.getColumnModel().getColumn(SHOW_COLUMN).setMaxWidth(110);
        purchaseInvoicesResultTable.getColumnModel().getColumn(SHOW_COLUMN).setMinWidth(110);
        purchaseInvoicesResultTable.getColumnModel().getColumn(SHOW_COLUMN).setPreferredWidth(110);
        purchaseInvoicesResultTable.getColumnModel().getColumn(SHOW_COLUMN).setCellRenderer(new ButtonRenderer());
        purchaseInvoicesResultTable.getColumnModel().getColumn(SHOW_COLUMN).setCellEditor(new ButtonEditor(new JTextField()));
        
        purchaseInvoicesResultTableJScrollPane = new JScrollPane();
        purchaseInvoicesResultTableJScrollPane.setBounds(30, 240, 1030, 300);
        purchaseInvoicesResultTableJScrollPane.setViewportView(purchaseInvoicesResultTable);
        
        helpIconImage  = new ImageIcon(getClass().getResource("/rentfur/button/image/util/help_24x24.png"));
        helpLabel = new JLabel("AYUDA");
        helpLabel.setIcon(helpIconImage);
        helpLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showHelp();
                    }
        });
        helpLabel.setBounds(1100, 580, 80, 25);
        purchaseInvoiceIndexParamsPanel.add(helpLabel);

        helpBalloonTip = new BalloonTip(helpLabel, "<html><head></head><body style='background:#F4EFEF;'><div style='margin:24px 34px;'><h2>Busqueda de Compras</h2><p>En esta vista se pueden filtrar las compras por los campos:</p> <ul><li><p>Numero Factura</p></li><li><p>Fecha Factura</p></li><li><p>Codigo Proveedor</p></li><li><p>Razon Social</p></li><li><p>Nombre comercial</p></li><li><p>RUC/CI</p></li></ul><p>Para obtener los resultados de dicho filtro se debe presionar sobre el boton &nbsp<img src='file:/C:/Users/FDuarte/Documents/NetBeansProjects/RentFur/build/classes/rentfur/button/image/util/search_24x24.png'>&nbsp<strong>Buscar</strong></p><p>Debajo se muestra la tabla con la lista de las compras que corresponde a la busqueda realizada.</p><p>&nbsp<img src='file:/C:/Users/FDuarte/Documents/NetBeansProjects/RentFur/build/classes/rentfur/button/image/util/shopping_cart_24x24.png'><strong>Registrar Compras</strong> se puede registrar una nueva factura de compra</p><p>La tabla de resultados muestra los datos principales de cada compra y se cuenta con la posibilidad de:</p> <ul><li>Ver: Ingresar a la vista donde se ven los detalles de la compra.</li></ul></div></body></html>");
        helpBalloonTip.setVisible(false);
        helpBalloonTip.setCloseButton(BalloonTip.getDefaultCloseButton(), false);
       
        
        add(purchaseInvoicesResultTableJScrollPane);
        add(purchaseInvoiceIndexParamsPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Gestionar Compras");
        setBounds(250,50,1200,650);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        purchaseInvoiceController.setPurchaseInvoiceIndexClosed();
    }
    
    private class PurchaseInvoicesIndextResultDefaultTableModel extends DefaultTableModel{
        
        /*@Override
        public int getRowCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getColumnCount() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }*/
        
        @Override
        public boolean isCellEditable(int row, int column) {
                                                    switch(column){
                                                        case SHOW_COLUMN:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    private void getPurchaseInvoiceRecordView(){
        purchaseInvoiceCreate = purchaseInvoiceController.getPurchaseInvoiceCreate();
        purchaseInvoiceCreate.setVisible(true);
        showSearchDialog(purchaseInvoiceCreate);
        inactivateElements();
        purchaseInvoiceCreate.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                activateElements();
//                if(!searchController.getFurnitureSelectedCode().isEmpty()){
//                    addFuritureSelectedToDetailTable(searchController.getFurnitureSelectedCode());
//                }
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
    
    private void showPurchaseInvoiceShowView(int row){
        int purchaseInvoiceId = Integer.valueOf(purchaseInvoicesResultDefaultTableModel.getValueAt(row, ID_COLUMN).toString());
        //FurnitureMovementShow
        purchaseInvoiceShow = purchaseInvoiceController.getPurchaseInvoiceShow(purchaseInvoiceId);
        purchaseInvoiceShow.setVisible(true);
        showSearchDialog(purchaseInvoiceShow);
        inactivateElements();
        purchaseInvoiceShow.addInternalFrameListener(new InternalFrameListener() {

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
//        searchButton.setEnabled(false);
        recordPurchaseInvoiceButton.setEnabled(false);
//        movementTypeComboBox.setEnabled(false);
//        conceptCodeComboBox.setEnabled(false);
        this.setClosable(false);
        this.setIconifiable(false);
//        furnitureMovementResultTable.setEnabled(false);
    }
    
    public void activateElements(){
//        searchButton.setEnabled(true);
//        movementTypeComboBox.setEnabled(true);
//        conceptCodeComboBox.setEnabled(true);
        recordPurchaseInvoiceButton.setEnabled(true);
        this.setClosable(true);
        this.setIconifiable(true);
//        furnitureMovementResultTable.setEnabled(true);
    }
    
    private void showSearchDialog(Object dialogView){
        purchaseIndexPane = this.getDesktopPane();
        purchaseIndexPane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        purchaseIndexPane.setVisible(true);
    }
    
    public void searchProviderButtonAction(ActionEvent e) {
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date invoicingDateDate = (Date) datePicker.getModel().getValue();
        
        String invoicingDate = "";
        if(invoicingDateDate!=null){
            invoicingDate = formatter.format(invoicingDateDate);
        }
        
        String code = codeTextField.getText();
        String name = nameTextField.getText();
        String tradename = tradenameTextField.getText();
        String fiscalNumber = fiscalNumberTextField.getText();
        fiscalNumber = fiscalNumber.replaceAll("\\.", "");
        String invoiceNumberString = invoiceNumberTextField.getText();
       purchaseInvoiceController.setPurchaseInvoicesIndexResultsTable(purchaseInvoicesResultDefaultTableModel, true, invoicingDate, code, name, tradename, invoiceNumberString, fiscalNumber);
    }
    
    private class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
          if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
          } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
          }
//          if(column==9){
//              if(onlyQuery){
//                    String message = "Su usuario solo cuenta con permiso de consultas";
//                    setEnabled(false);
//                    setToolTipText(message);
//                }
//          }
          setText((value == null) ? "" : value.toString());
          return this;
        }
      }

      /**
       * @version 1.0 11/09/98
       */

     private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;

        private boolean isPushed;
        
        private int row;

        private int column;
        
        public ButtonEditor(JTextField jtf) {
          super(jtf);
          button = new JButton();
          button.setOpaque(true);
          button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
          });
          this.clickCountToStart = 1;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
          label = (value == null) ? "" : value.toString();
          button.setText(label);
          isPushed = true;
          this.row = row;
          this.column = column;
          return button;
        }

        
        @Override
        public Object getCellEditorValue() {
          if (isPushed) {
//                if(column==10){
//                    if(!onlyQuery){
//                        label = updateFurnitureStatus(row, label);
//                    }
//                }else 
                if(column==SHOW_COLUMN){
                    showPurchaseInvoiceShowView(row);
                }
          }
          isPushed = false;          
          return label;
        }

        @Override
        public boolean stopCellEditing() {
          isPushed = false;
          return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
      }
     
     public void showHelp(){
        if(!helpBalloonTip.isVisible()){
            helpBalloonTip.setVisible(true);
        }
    }
}
