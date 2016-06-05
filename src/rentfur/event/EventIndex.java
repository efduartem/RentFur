package rentfur.event;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
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
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */
public class EventIndex extends JInternalFrame{
    private final EventController eventController;
    private EventShowAndEdit eventShowAndEdit;
    private EventCreate eventCreate;
    private EventEdit eventEdit;
    private final JPanel eventHeaderPanel;
    private final JLabel eventDateLabel;
    private final JLabel eventsDayTitleLabel;
    private final JLabel eventsDayLabel;
    private JTextField eventsDayTextField;
    private final JLabel eventsDayTotalLabel;
    private JTextField eventsDayTotalTextField;
    private final JLabel budgetedDayLabel;
    private JTextField budgetedDayTextField;
    private final JLabel budgetedDayTotalLabel;
    private JTextField budgetedDayTotalTextField;
    private JLabel eventsDayValueTitleLabel;
    private final JDatePickerImpl datePicker;
    private final JLabel furnitureDescriptionFilterLabel;
    private final JTextField furnitureDescriptionFilterTextField;
    
    private final JTable furnituresStockTable;
    private final DefaultTableModel furnituresStockDefaultTableModel;
    private final JScrollPane furnituresStockTableJScrollPane;
    
    private final JTable dayEventsTable;
    private final DefaultTableModel dayEventsDefaultTableModel;
    private final JScrollPane dayEventsTableJScrollPane;
    
    private final DecimalFormat amountFormat = new DecimalFormat("#,###");
    
    private final TableRowSorter trsfiltro;
    
    private JDesktopPane pane;
    
    private double eventsDayQuantity = 0;
    private double eventsDayTotal = 0;
    private double budgetedDayQuantity = 0;
    private double budgetedDayTotal = 0;
    
    private final JButton createEventButton;
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
     
    public EventIndex(EventController eventController){
        this.eventController = eventController;
        
        amountFormat.setGroupingUsed(true);
        amountFormat.setGroupingSize(3);
        amountFormat.setParseIntegerOnly(true);
        
        eventHeaderPanel = new JPanel();
        eventHeaderPanel.setLayout(null);
        
        eventDateLabel = new JLabel("Fecha: ");
        eventDateLabel.setBounds(30, 40, 100, 25);
        eventHeaderPanel.add(eventDateLabel);
        
        UtilDateModel model = new UtilDateModel();
        model.setDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        // Don't know about the formatter, but there it is...
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter("yyyy-MM-dd"));
        datePicker.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Refrescar stockes
                updateDayEvents();
                updateDayStock();
                updateDayLabel();
            }
        });
        datePicker.setBounds(160, 40, 170, 25);
        eventHeaderPanel.add(datePicker);
        
        //Fecha del Evento al abrir la ventana
        Date deliveryDate = (Date) datePicker.getModel().getValue();
        
        furnitureDescriptionFilterLabel = new JLabel("Filtrar por Descripcion: ");
        furnitureDescriptionFilterLabel.setBounds(550, 40, 200, 25);
        eventHeaderPanel.add(furnitureDescriptionFilterLabel);
        
        furnitureDescriptionFilterTextField = new JTextField();
        furnitureDescriptionFilterTextField.setBounds(750, 40, 250, 25);
        furnitureDescriptionFilterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                furnituresStockTable.repaint();
                filter();
            }
        });
        eventHeaderPanel.add(furnitureDescriptionFilterTextField);
        
        //TABLA DE Stocks
        furnituresStockDefaultTableModel = new FurnituresStockDefaultTableModel();
        furnituresStockTable = new JTable(furnituresStockDefaultTableModel);
        
        //Alineacion a la derecha para numeros
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        //Formato para celda inactiva
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer();
        statusRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        furnituresStockDefaultTableModel.addColumn("Código");
        furnituresStockDefaultTableModel.addColumn("Descripción");
        furnituresStockDefaultTableModel.addColumn("Familia");
        furnituresStockDefaultTableModel.addColumn("Total");
        furnituresStockDefaultTableModel.addColumn("Comprometido");
        furnituresStockDefaultTableModel.addColumn("Disponible");
        
        furnituresStockTable.setRowHeight(24);
                
        furnituresStockTable.getTableHeader().setDefaultRenderer(new SimpleStockHeaderRenderer());
        
        //Furniture Code Column
        furnituresStockTable.getColumnModel().getColumn(0).setMaxWidth(80);
        furnituresStockTable.getColumnModel().getColumn(0).setMinWidth(80);
        furnituresStockTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        
        //Furniture Descripion Column
        furnituresStockTable.getColumnModel().getColumn(1).setMaxWidth(210);
        furnituresStockTable.getColumnModel().getColumn(1).setMinWidth(210);
        furnituresStockTable.getColumnModel().getColumn(1).setPreferredWidth(210);
        
        //Furniture Family Column
        furnituresStockTable.getColumnModel().getColumn(2).setMaxWidth(180);
        furnituresStockTable.getColumnModel().getColumn(2).setMinWidth(180);
        furnituresStockTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        
        //Total Stock Column
        furnituresStockTable.getColumnModel().getColumn(3).setMaxWidth(110);
        furnituresStockTable.getColumnModel().getColumn(3).setMinWidth(110);
        furnituresStockTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        furnituresStockTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        
        //Committed Stock Column
        furnituresStockTable.getColumnModel().getColumn(4).setMaxWidth(109);
        furnituresStockTable.getColumnModel().getColumn(4).setMinWidth(109);
        furnituresStockTable.getColumnModel().getColumn(4).setPreferredWidth(109);
        furnituresStockTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        //Available Stock Column
        furnituresStockTable.getColumnModel().getColumn(5).setMaxWidth(109);
        furnituresStockTable.getColumnModel().getColumn(5).setMinWidth(109);
        furnituresStockTable.getColumnModel().getColumn(5).setPreferredWidth(109);
        furnituresStockTable.getColumnModel().getColumn(5).setCellRenderer(new StockAvailableCellRenderer());
        
        ArrayList furnitureList = eventController.getFurnitureStockForIndex(deliveryDate);
        HashMap furnitureMap;
        Object[] row = new Object[furnituresStockTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            row[0] = furnitureMap.get("furnitureCode");
            row[1] = furnitureMap.get("furnitureDescription");
            row[2] = furnitureMap.get("furnitureFamily");
            row[3] = amountFormat.format((Double)furnitureMap.get("stockTotal"));
            row[4] = amountFormat.format((Double)furnitureMap.get("stockCommited"));
            row[5] = amountFormat.format((Double)furnitureMap.get("stockAvailable"));
            furnituresStockDefaultTableModel.addRow(row);
        } 
        
        trsfiltro = new TableRowSorter(furnituresStockDefaultTableModel);
        // Añadimos al Jtable el filtro trsfiltro
        
        furnituresStockTable.setRowSorter(trsfiltro);
        
        furnituresStockTableJScrollPane = new JScrollPane();
        furnituresStockTableJScrollPane.setBounds(500, 90, 800, 300);
        furnituresStockTableJScrollPane.setViewportView(furnituresStockTable);
        
        add(furnituresStockTableJScrollPane);
        
        eventsDayTitleLabel = new JLabel("<HTML>Eventos del Dia: </HTML>");
        eventsDayTitleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 20));
        eventsDayTitleLabel.setBounds(100, 415, 200, 20);
        eventHeaderPanel.add(eventsDayTitleLabel);
        
        SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMM 'de' yyyy", new Locale("es_PY"));
        String eventDay = formateador.format(new Date());
        eventsDayValueTitleLabel = new JLabel();
        eventsDayValueTitleLabel.setText("<HTML>"+eventDay+"</HTML>");
        eventsDayValueTitleLabel.setFont(new Font(Font.SERIF, Font.ITALIC, 18));
        eventsDayValueTitleLabel.setBounds(255, 416, 200, 20);
        eventHeaderPanel.add(eventsDayValueTitleLabel);
        
        createEventButton = new JButton("  Nuevo Evento");
        ImageIcon newEventIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/calendar_24x24.png"));
        createEventButton.setIcon(newEventIconImage);
        createEventButton.setBounds(1350, 680, 190, 35);
        eventHeaderPanel.add(createEventButton);
        
        createEventButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createEventAction();
            }
        });
        
        eventsDayLabel = new JLabel("Eventos del dia: ");
        eventsDayLabel.setBounds(1350, 460, 100, 25);
        eventHeaderPanel.add(eventsDayLabel);
        
        eventsDayTextField = new JTextField();
        eventsDayTextField.setBounds(1450, 460, 150, 25);
        eventsDayTextField.setEditable(false);
        eventsDayTextField.setHorizontalAlignment(JLabel.RIGHT);
        eventHeaderPanel.add(eventsDayTextField);
        
        eventsDayTotalLabel = new JLabel("Total Gs.: ");
        eventsDayTotalLabel.setBounds(1350, 490, 100, 25);
        eventHeaderPanel.add(eventsDayTotalLabel);
        
        eventsDayTotalTextField = new JTextField();
        eventsDayTotalTextField.setBounds(1450, 490, 150, 25);
        eventsDayTotalTextField.setEditable(false);
        eventsDayTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        eventHeaderPanel.add(eventsDayTotalTextField);
        
        budgetedDayLabel = new JLabel("Presupuestos: ");
        budgetedDayLabel.setBounds(1350, 560, 100, 25);
        eventHeaderPanel.add(budgetedDayLabel);
        
        budgetedDayTextField = new JTextField();
        budgetedDayTextField.setBounds(1450, 560, 150, 25);
        budgetedDayTextField.setEditable(false);
        budgetedDayTextField.setHorizontalAlignment(JLabel.RIGHT);
        eventHeaderPanel.add(budgetedDayTextField);
        
        budgetedDayTotalLabel = new JLabel("Total Gs.: ");
        budgetedDayTotalLabel.setBounds(1350, 590, 100, 25);
        eventHeaderPanel.add(budgetedDayTotalLabel);
        
        budgetedDayTotalTextField = new JTextField();
        budgetedDayTotalTextField.setBounds(1450, 590, 150, 25);
        budgetedDayTotalTextField.setEditable(false);
        budgetedDayTotalTextField.setHorizontalAlignment(JLabel.RIGHT);
        eventHeaderPanel.add(budgetedDayTotalTextField);
        
        TableCellRenderer renderer = new EvenOddRenderer();
        
        //TABLA DE Eventos de Dia
        dayEventsDefaultTableModel = new DayEventsDefaultTableModel();
        dayEventsTable = new JTable(dayEventsDefaultTableModel);
        dayEventsTable.setDefaultRenderer(Object.class, renderer);
        dayEventsDefaultTableModel.addColumn("Id");
        dayEventsDefaultTableModel.addColumn("Código");
        dayEventsDefaultTableModel.addColumn("Cliente");
        dayEventsDefaultTableModel.addColumn("Estado");
        dayEventsDefaultTableModel.addColumn("Total");
        dayEventsDefaultTableModel.addColumn("Saldo");
        dayEventsDefaultTableModel.addColumn("Lugar");
        dayEventsDefaultTableModel.addColumn("Observaciones");
        dayEventsDefaultTableModel.addColumn("");
        dayEventsDefaultTableModel.addColumn("statusInt");
        
        dayEventsTable.setRowHeight(24);
                
        dayEventsTable.getTableHeader().setDefaultRenderer(new SimpleHeaderRenderer());
        
        //ID Column
        dayEventsTable.getColumnModel().getColumn(0).setMaxWidth(0);
        dayEventsTable.getColumnModel().getColumn(0).setMinWidth(0);
        dayEventsTable.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        //Subject Code Column
        dayEventsTable.getColumnModel().getColumn(1).setMaxWidth(80);
        dayEventsTable.getColumnModel().getColumn(1).setMinWidth(80);
        dayEventsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        
        //Subject Name Column
        dayEventsTable.getColumnModel().getColumn(2).setMaxWidth(210);
        dayEventsTable.getColumnModel().getColumn(2).setMinWidth(210);
        dayEventsTable.getColumnModel().getColumn(2).setPreferredWidth(210);
        
        //Status Column
        dayEventsTable.getColumnModel().getColumn(3).setMaxWidth(110);
        dayEventsTable.getColumnModel().getColumn(3).setMinWidth(110);
        dayEventsTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        
        //Total Column
        dayEventsTable.getColumnModel().getColumn(4).setMaxWidth(180);
        dayEventsTable.getColumnModel().getColumn(4).setMinWidth(180);
        dayEventsTable.getColumnModel().getColumn(4).setPreferredWidth(180);
        //dayEventsTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        
        //Balance Column
        dayEventsTable.getColumnModel().getColumn(5).setMaxWidth(180);
        dayEventsTable.getColumnModel().getColumn(5).setMinWidth(180);
        dayEventsTable.getColumnModel().getColumn(5).setPreferredWidth(180);
        //dayEventsTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        
        //Place Of Delivery Column
        dayEventsTable.getColumnModel().getColumn(6).setPreferredWidth(300);
        
        //Observation Column
        dayEventsTable.getColumnModel().getColumn(7).setPreferredWidth(300);
        
        //Show Button Column
        dayEventsTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        dayEventsTable.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JTextField()));
        
        //Status Int Column
        dayEventsTable.getColumnModel().getColumn(9).setMaxWidth(0);
        dayEventsTable.getColumnModel().getColumn(9).setMinWidth(0);
        dayEventsTable.getColumnModel().getColumn(9).setPreferredWidth(0);
        
        ArrayList eventList = eventController.getEventsByDayForIndex(deliveryDate);
        HashMap eventMap;
        Object[] rowEvent = new Object[dayEventsTable.getColumnCount()];        
        for(int i = 0; i < eventList.size(); i++){
            eventMap = (HashMap) eventList.get(i);
            rowEvent[0] = eventMap.get("id");
            rowEvent[1] = eventMap.get("subjectCode");
            rowEvent[2] = eventMap.get("subjectName");
            rowEvent[3] = eventMap.get("status");
            rowEvent[4] = amountFormat.format((Double)eventMap.get("netTotal"));
            rowEvent[5] = amountFormat.format((Double)eventMap.get("balance"));
            rowEvent[6] = eventMap.get("placeOfDelivery");
            rowEvent[7] = eventMap.get("observation");
            rowEvent[8] = "Ver";
            rowEvent[9] = (Integer)eventMap.get("statusInt");
            if(((Integer)eventMap.get("statusInt"))==EventController.CONFIRMED){
                eventsDayQuantity++;
                eventsDayTotal = eventsDayTotal + (Double)eventMap.get("netTotal");
            }else if(((Integer)eventMap.get("statusInt"))==EventController.BUDGETED){
                budgetedDayQuantity++;
                budgetedDayTotal = budgetedDayTotal + (Double)eventMap.get("netTotal");
            }
            dayEventsDefaultTableModel.addRow(rowEvent);
        } 
        
        eventsDayTextField.setText(amountFormat.format(eventsDayQuantity));
        eventsDayTotalTextField.setText(amountFormat.format(eventsDayTotal));
        budgetedDayTextField.setText(amountFormat.format(budgetedDayQuantity));
        budgetedDayTotalTextField.setText(amountFormat.format(budgetedDayTotal));
        
        dayEventsTableJScrollPane = new JScrollPane();
        dayEventsTableJScrollPane.setBounds(30, 460, 1268, 350);
        dayEventsTableJScrollPane.setViewportView(dayEventsTable);
        
        add(dayEventsTableJScrollPane);
        
        add(eventHeaderPanel);
        pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setClosable(true);
        setTitle("Eventos");
        setBounds(50,30,1800,900);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
     }
     
     private class FurnituresStockDefaultTableModel extends DefaultTableModel{
        
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
                                                        default:    return false;
                                                    }
                                                }
           
    }
     
    private class DayEventsDefaultTableModel extends DefaultTableModel{
        
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
                                                        case 8:     return true;
                                                        default:    return false;
                                                    }
                                                }
           
    }
    
    private class SimpleHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public SimpleHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Plain", Font.PLAIN, 13));
            setBackground(new Color(179, 225, 242));
            setBorder(BorderFactory.createEtchedBorder());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

    }
    
    private class SimpleStockHeaderRenderer extends JLabel implements TableCellRenderer {
 
        public SimpleStockHeaderRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Plain", Font.PLAIN, 13));
            setBackground(new Color(141, 170, 201));
            setBorder(BorderFactory.createEtchedBorder());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }

    }
    
    class StockAvailableCellRenderer extends JTextField implements TableCellRenderer {

        public StockAvailableCellRenderer() {
            setOpaque(true);
            setBackground(new Color(227, 231, 249));
            //setBackground(new Color(222, 236, 237));
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
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        eventController.eventIndexClosed();
        //eventController.setEventShowAndEditInMainWindow(30);
        //furnitureController.setEnabledIndexView();
        //furnitureController.searchFurnitureButtonAction();
    }
    
    private void updateDayLabel(){
        Date deliveryDate = (Date) datePicker.getModel().getValue();
        SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMM 'de' yyyy", new Locale("es_PY"));
        String eventDay = formateador.format(deliveryDate);
        eventsDayValueTitleLabel.setText("<HTML>"+eventDay+"</HTML>");
    }
    
    private void updateDayStock(){
        int rowCount = furnituresStockDefaultTableModel.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            furnituresStockDefaultTableModel.removeRow(i);
        }
        
        Date deliveryDate = (Date) datePicker.getModel().getValue();
        ArrayList furnitureList = eventController.getFurnitureStockForIndex(deliveryDate);
        HashMap furnitureMap;
        Object[] row = new Object[furnituresStockTable.getColumnCount()];        
        for(int i = 0; i < furnitureList.size(); i++){
            furnitureMap = (HashMap) furnitureList.get(i);
            row[0] = furnitureMap.get("furnitureCode");
            row[1] = furnitureMap.get("furnitureDescription");
            row[2] = furnitureMap.get("furnitureFamily");
            row[3] = amountFormat.format((Double)furnitureMap.get("stockTotal"));
            row[4] = amountFormat.format((Double)furnitureMap.get("stockCommited"));
            row[5] = amountFormat.format((Double)furnitureMap.get("stockAvailable"));
            furnituresStockDefaultTableModel.addRow(row);
        } 
    }
    
    private void filter() {
        ArrayList<RowFilter<TableModel, Object>> andFilter = new ArrayList<>(1);
	RowFilter<TableModel, Object> rf;
        
        rf = RowFilter.regexFilter("(?i)" + furnitureDescriptionFilterTextField.getText(), 1);
        andFilter.add(rf);
        
	RowFilter<TableModel, Object> rowf = RowFilter.andFilter(andFilter);
        
	trsfiltro.setRowFilter(rowf);
    }
    
    private void iconifiedInternalFrame(){
        try {
            this.setIcon(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(EventIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void deIconifiedInternalFrame(){
        try {
            this.setIcon(false);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(EventIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void inactivateElements(){
        this.setIconifiable(false);
        this.setClosable(false);
        dayEventsTable.setEnabled(false);
        furnituresStockTable.setEnabled(false);
        furnitureDescriptionFilterTextField.setEnabled(false);
    }
    
    private void activateElements(){
        this.setIconifiable(true);
        this.setClosable(true);
        dayEventsTable.setEnabled(true);
        furnituresStockTable.setEnabled(true);
        furnitureDescriptionFilterTextField.setEnabled(true);
    }
    
    private void createEventAction(){
        iconifiedInternalFrame();
        eventCreate = eventController.getEventCreate();
        eventCreate.setVisible(true);
        showSearchDialog(eventCreate);
        inactivateElements();
        eventCreate.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) { 
                activateElements();
                deIconifiedInternalFrame();
                updateDayEvents();
                updateDayStock();
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
    
    private void showAndEditEventInternalFrameView(int row){
        Vector dataVector = (Vector) dayEventsDefaultTableModel.getDataVector().get(row);
        int eventId = (Integer) dataVector.get(0);
        int status = (Integer) dataVector.get(9);
        iconifiedInternalFrame();
        if(status==EventController.CONFIRMED || status==EventController.CANCELED){
            eventShowAndEdit = eventController.getEventShowAndEdit(eventId);
            eventShowAndEdit.setVisible(true);
            showSearchDialog(eventShowAndEdit);
            inactivateElements();
            eventShowAndEdit.addInternalFrameListener(new InternalFrameListener() {

                @Override
                public void internalFrameOpened(InternalFrameEvent e) {}

                @Override
                public void internalFrameClosing(InternalFrameEvent e) {}

                @Override
                public void internalFrameClosed(InternalFrameEvent e) { 
                    activateElements();
                    deIconifiedInternalFrame();
                    updateDayEvents();
                    updateDayStock();
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
        }else{
            eventEdit = eventController.getEventEdit(eventId);
            eventEdit.setVisible(true);
            showSearchDialog(eventEdit);
            inactivateElements();
            eventEdit.addInternalFrameListener(new InternalFrameListener() {

                @Override
                public void internalFrameOpened(InternalFrameEvent e) {}

                @Override
                public void internalFrameClosing(InternalFrameEvent e) {}

                @Override
                public void internalFrameClosed(InternalFrameEvent e) { 
                    activateElements();
                    deIconifiedInternalFrame();
                    updateDayEvents();
                    updateDayStock();
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
    }
    
    private void updateDayEvents(){
        
        int rowCount = dayEventsDefaultTableModel.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dayEventsDefaultTableModel.removeRow(i);
        }
        
        eventsDayQuantity = 0;
        eventsDayTotal = 0;
        budgetedDayQuantity = 0;
        budgetedDayTotal = 0;
        
        Date deliveryDate = (Date) datePicker.getModel().getValue();
        ArrayList eventList = eventController.getEventsByDayForIndex(deliveryDate);
        HashMap eventMap;
        Object[] row = new Object[dayEventsTable.getColumnCount()];        
        for(int i = 0; i < eventList.size(); i++){
            eventMap = (HashMap) eventList.get(i);
            row[0] = eventMap.get("id");
            row[1] = eventMap.get("subjectCode");
            row[2] = eventMap.get("subjectName");
            row[3] = eventMap.get("status");
            row[4] = amountFormat.format((Double)eventMap.get("netTotal"));
            row[5] = amountFormat.format((Double)eventMap.get("balance"));
            row[6] = eventMap.get("placeOfDelivery");
            row[7] = eventMap.get("observation");
            row[8] = "Ver";
            row[9] = (Integer)eventMap.get("statusInt");
            if(((Integer)eventMap.get("statusInt"))==EventController.CONFIRMED){
                eventsDayQuantity++;
                eventsDayTotal = eventsDayTotal + (Double)eventMap.get("netTotal");
            }else if(((Integer)eventMap.get("statusInt"))==EventController.BUDGETED){
                budgetedDayQuantity++;
                budgetedDayTotal = budgetedDayTotal + (Double)eventMap.get("netTotal");
            }
            
            dayEventsDefaultTableModel.addRow(row);
        }
        
        eventsDayTextField.setText(amountFormat.format(eventsDayQuantity));
        eventsDayTotalTextField.setText(amountFormat.format(eventsDayTotal));
        budgetedDayTextField.setText(amountFormat.format(budgetedDayQuantity));
        budgetedDayTotalTextField.setText(amountFormat.format(budgetedDayTotal));
    }
    
    private void showSearchDialog(Object dialogView){
        pane = getDesktopPane();
        pane.add((JInternalFrame) dialogView, JLayeredPane.MODAL_LAYER);
        pane.setVisible(true);
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {

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
          setText((value == null) ? "" : value.toString());
          return this;
        }
      }


     class ButtonEditor extends DefaultCellEditor {
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
                if(column==8){
                    showAndEditEventInternalFrameView(row);
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
     
     class EvenOddRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int status = Integer.valueOf(table.getModel().getValueAt(row, 9).toString());
            Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ((JLabel) renderer).setOpaque(true);
            Color foreground;
            Color background;
            
            if (status == EventController.BUDGETED) {
                foreground = Color.black;
                background = new Color(244, 249, 199);
                renderer.setForeground(foreground);
                renderer.setBackground(background);
            }else if (status == EventController.CANCELED) {
                foreground = Color.black;
                background = new Color(255, 204, 204);
                renderer.setForeground(foreground);
                renderer.setBackground(background);
            }else{
                foreground = Color.black;
                background = Color.white;
                renderer.setForeground(foreground);
                renderer.setBackground(background);
            }

            if(column == 4 || column == 5){
                ((JLabel) renderer).setHorizontalAlignment(JLabel.RIGHT);
            }else{
                ((JLabel) renderer).setHorizontalAlignment(JLabel.LEFT);
            }
            
            return renderer;
        }
      }

}
