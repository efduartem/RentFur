/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.report;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import rentfur.util.DateLabelFormatter;
import rentfur.util.searches.SearchController;
import rentfur.util.searches.SubjectSearch;

/**
 *
 * @author FDuarte
 */
public class SubjectAccountStatusReport extends JInternalFrame{
    private final ReportController reportController;
    private final SearchController searchController;
    private SubjectSearch subjectSearch;
    private final JPanel reportParamPanel;
    private final JLabel subjectLabel;
    private final JLabel initDateLabel;
    private final JLabel endDateLabel;
    private final JTextField subjectTextField;
    private final JDatePickerImpl initDateDatePicker;
    private final JDatePickerImpl endDateDatePicker;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton selectSubjectButton;
    private final JButton executeButton;
    private final JButton cancelButton;
    private String subjectSelectedCode = "";
    
    private JDesktopPane eventPane;
    
    public SubjectAccountStatusReport(ReportController reportController){
        this.reportController = reportController;
        
        searchController = new SearchController();
        
        reportParamPanel = new JPanel();
        reportParamPanel.setLayout(null);
        
        subjectLabel = new JLabel("Cod. Cliente:");
        subjectLabel.setBounds(50, 20, 120, 25);
        reportParamPanel.add(subjectLabel);
        
        subjectTextField = new JTextField();
        subjectTextField.setBounds(200, 20, 130, 25);
        reportParamPanel.add(subjectTextField);
        
        selectSubjectButton = new JButton();
        ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/subject_24x24.png"));
        selectSubjectButton.setIcon(subjectIconImage);
        selectSubjectButton.setBounds(360, 15, 200, 32);
        selectSubjectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectSubjectButtonAction();
            }
        });
        selectSubjectButton.setText(" Seleccionar Cliente");
        reportParamPanel.add(selectSubjectButton);
        
        
        UtilDateModel initModel = new UtilDateModel(new Date());
        UtilDateModel endModel = new UtilDateModel(new Date());
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl initDatePanel = new JDatePanelImpl(initModel, p);
        JDatePanelImpl endDatePanel = new JDatePanelImpl(endModel, p);
        // Don't know about the formatter, but there it is...
        
        initDateLabel = new JLabel("Desde:");
        initDateLabel.setBounds(50, 60, 120, 25);
        reportParamPanel.add(initDateLabel);
        
        initDateDatePicker = new JDatePickerImpl(initDatePanel, new DateLabelFormatter("yyyy-MM-dd"));
//        initDateDatePicker.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                //setInitDateValue();
//            }
//        });
        initDateDatePicker.setBounds(200, 60, 160, 25);
        reportParamPanel.add(initDateDatePicker);
        
        endDateLabel = new JLabel("Hasta:");
        endDateLabel.setBounds(50,90, 120, 25);
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
        endDateDatePicker.setBounds(200, 90, 160, 25);
        reportParamPanel.add(endDateDatePicker);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/execute_report_pdf.png"));
        executeButton = new JButton(" Ejecutar", createIconImage);
        executeButton.setBounds(130, 160, 150, 50);
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        
        reportParamPanel.add(executeButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(330, 160, 150, 50);
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
        setTitle("Informe de Estado de Cuentas");
        setBounds(650,280,650,280);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        reportController.subjectAccountStatusReportViewClosed();
    }
    
    private void saveButtonAction(ActionEvent e) {
        Date initDate = (Date) initDateDatePicker.getModel().getValue();
        Date endDate = (Date) endDateDatePicker.getModel().getValue();
        if(initDate == null || endDate == null || subjectSelectedCode.equals("")){
            JOptionPane.showMessageDialog(null, "No ha ingresado todos los parametros", "Atencion!", JOptionPane.WARNING_MESSAGE);
        }else{
            reportController.executeSubjectAccountStatusReport(initDate, endDate, subjectSelectedCode);
//            HashMap mapReturn = userController.changePassword(userRoles.getUser().getId(), currentPassword, newPassword, confirmPassword);
//            if((Integer) mapReturn.get("status") == userController.SUCCESFULLY_SAVED){
//                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
//                doDefaultCloseAction();
//            }else if((Integer)mapReturn.get("status") == userController.ERROR_IN_SAVED){
//                JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
//            }
        }
    }
    
    private void selectSubjectButtonAction(){
        subjectSearch = searchController.getSubjectSearch();
        subjectSearch.setVisible(true);
        showSearchDialog(subjectSearch);
        inactivateElements();
        subjectSearch.addInternalFrameListener(new InternalFrameListener() {

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {}

            @Override
            public void internalFrameClosed(InternalFrameEvent e) { 
                activateElements();
                if(!searchController.getSubjetcSelectedCode().equals("")){
                    subjectSelectedCode = searchController.getSubjetcSelectedCode();
                    subjectTextField.setText(subjectSelectedCode);
                }
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
    };
    
    private void showSearchDialog(Object dialogView){
        eventPane = this.getDesktopPane();
        eventPane.add((JInternalFrame) dialogView, JLayeredPane.POPUP_LAYER);
        eventPane.setVisible(true);
    }
    
    private void inactivateElements(){
        setClosable(false);
        setIconifiable(false);
        selectSubjectButton.setEnabled(false);
        executeButton.setEnabled(false);
        cancelButton.setEnabled(false);
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
    
    private void activateElements(){
        getContentPane().getComponents()[0].setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setClosable(true);
        setIconifiable(true);
        selectSubjectButton.setEnabled(true);
        executeButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }
}
