/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.util.DateLabelFormatter;

/**
 *
 * @author FDuarte
 */
public class PendingEventsByDate extends JInternalFrame{
    private final ReportController reportController;
    private final JPanel reportParamPanel;
//    private final JLabel initDateLabel;
    private final JLabel endDateLabel;
//    private final JDatePickerImpl initDateDatePicker;
    private final JDatePickerImpl endDateDatePicker;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton executeButton;
    private final JButton cancelButton;
    
    public PendingEventsByDate(ReportController reportController){
        this.reportController = reportController;
        
        reportParamPanel = new JPanel();
        reportParamPanel.setLayout(null);
        
//        UtilDateModel initModel = new UtilDateModel(new Date());
        UtilDateModel endModel = new UtilDateModel(new Date());
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
//        JDatePanelImpl initDatePanel = new JDatePanelImpl(initModel, p);
        JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel, p);
        // Don't know about the formatter, but there it is...
        
//        initDateLabel = new JLabel("Desde:");
//        initDateLabel.setBounds(50, 20, 120, 25);
//        reportParamPanel.add(initDateLabel);
//        
//        initDateDatePicker = new JDatePickerImpl(initDatePanel, new DateLabelFormatter("yyyy-MM-dd"));
////        initDateDatePicker.addActionListener(new ActionListener() {
////
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                //setInitDateValue();
////            }
////        });
//        initDateDatePicker.setBounds(200, 20, 160, 25);
//        reportParamPanel.add(initDateDatePicker);
        
        endDateLabel = new JLabel("Hasta:");
        endDateLabel.setBounds(50,40, 120, 25);
        reportParamPanel.add(endDateLabel);
        
        endDateDatePicker = new JDatePickerImpl(endDatePanel, new DateLabelFormatter("yyyy-MM-dd"));
//        endDateDatePicker.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                setEndDateValue();
//            }
//            
//            
//        });
        endDateDatePicker.setBounds(200, 40, 160, 25);
        reportParamPanel.add(endDateDatePicker);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/execute_report_pdf.png"));
        executeButton = new JButton(" Ejecutar", createIconImage);
        executeButton.setBounds(50, 100, 150, 50);
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        
        reportParamPanel.add(executeButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(220, 100, 150, 50);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doDefaultCloseAction();
            }
        });
        reportParamPanel.add(cancelButton);

        add(reportParamPanel);
        
        //pack();
        setIconifiable(true);
        setMaximizable(false);
        setResizable(true);
        setClosable(true);
        setTitle("Resumen Eventos Pendientes por Fecha");
        setBounds(650,280,450,200);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        reportController.pendingEventsByDateViewClosed();
    }
    
    private void saveButtonAction(ActionEvent e) {
//        Date initDate = (Date) initDateDatePicker.getModel().getValue();
        Date endDate = (Date) endDateDatePicker.getModel().getValue();
        if(endDate == null){
            JOptionPane.showMessageDialog(null, "No ha ingresado todos los parametros", "Atencion!", JOptionPane.WARNING_MESSAGE);
        }else{
            reportController.executePendingEventsByDateReport(endDate);
//            HashMap mapReturn = userController.changePassword(userRoles.getUser().getId(), currentPassword, newPassword, confirmPassword);
//            if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
//                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
//                doDefaultCloseAction();
//            }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
//                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
//            }
        }
    }
    
}
