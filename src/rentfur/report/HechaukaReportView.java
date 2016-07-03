/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rentfur.util.ComboBoxItem;

/**
 *
 * @author FDuarte
 */
public class HechaukaReportView extends JInternalFrame{
    private final ReportController reportController;
    private final JPanel reportParamPanel;
    private final JLabel yearLabel;
    private final JLabel monthLabel;
    private final JLabel typeLabel;
    private final JComboBox yearComboBox;
    private final JComboBox monthComboBox;
    private final JComboBox typeBookComboBox;
    private final ImageIcon createIconImage;
    private final ImageIcon cancelIconImage;
    private final JButton executeButton;
    private final JButton cancelButton;
    
    public HechaukaReportView(ReportController reportController){
        this.reportController = reportController;
        
        reportParamPanel = new JPanel();
        reportParamPanel.setLayout(null);
        
        yearLabel = new JLabel("Año:");
        yearLabel.setBounds(50, 20, 120, 25);
        reportParamPanel.add(yearLabel);
        
        ComboBoxItem[] yearCbxItem = null;
        yearCbxItem = new ComboBoxItem[5];
        
        yearCbxItem[0] =  new ComboBoxItem();
        yearCbxItem[0].setKey("2015");
        yearCbxItem[0].setValue("2015");

        yearCbxItem[1] =  new ComboBoxItem();
        yearCbxItem[1].setKey("2016");
        yearCbxItem[1].setValue("2016");

        yearCbxItem[2] =  new ComboBoxItem();
        yearCbxItem[2].setKey("2017");
        yearCbxItem[2].setValue("2017");
        
        yearCbxItem[3] =  new ComboBoxItem();
        yearCbxItem[3].setKey("2018");
        yearCbxItem[3].setValue("2018");
        
        yearCbxItem[4] =  new ComboBoxItem();
        yearCbxItem[4].setKey("2019");
        yearCbxItem[4].setValue("2019");
        
        yearComboBox = new JComboBox(yearCbxItem);
        yearComboBox.setSelectedItem(yearCbxItem[1]);
        yearComboBox.setBounds(200, 20, 160, 25);
        reportParamPanel.add(yearComboBox);
        
        monthLabel = new JLabel("Periodo:");
        monthLabel.setBounds(50,50, 120, 25);
        reportParamPanel.add(monthLabel);
        
        ComboBoxItem[] monthCbxItem = null;
        monthCbxItem = new ComboBoxItem[12];
        
        monthCbxItem[0] =  new ComboBoxItem();
        monthCbxItem[0].setKey("01");
        monthCbxItem[0].setValue("Enero");

        monthCbxItem[1] =  new ComboBoxItem();
        monthCbxItem[1].setKey("02");
        monthCbxItem[1].setValue("Febrero");

        monthCbxItem[2] =  new ComboBoxItem();
        monthCbxItem[2].setKey("03");
        monthCbxItem[2].setValue("Marzo");
        
        monthCbxItem[3] =  new ComboBoxItem();
        monthCbxItem[3].setKey("04");
        yearCbxItem[3].setValue("Abril");
        
        monthCbxItem[4] =  new ComboBoxItem();
        monthCbxItem[4].setKey("05");
        monthCbxItem[4].setValue("Mayo");
        
        monthCbxItem[5] =  new ComboBoxItem();
        monthCbxItem[5].setKey("06");
        monthCbxItem[5].setValue("Junio");
        
        monthCbxItem[6] =  new ComboBoxItem();
        monthCbxItem[6].setKey("07");
        monthCbxItem[6].setValue("Julio");
        
        monthCbxItem[7] =  new ComboBoxItem();
        monthCbxItem[7].setKey("08");
        monthCbxItem[7].setValue("Agosto");
        
        monthCbxItem[8] =  new ComboBoxItem();
        monthCbxItem[8].setKey("09");
        monthCbxItem[8].setValue("Setiembre");
        
        monthCbxItem[9] =  new ComboBoxItem();
        monthCbxItem[9].setKey("10");
        monthCbxItem[9].setValue("Octubre");
        
        monthCbxItem[10] =  new ComboBoxItem();
        monthCbxItem[10].setKey("11");
        monthCbxItem[10].setValue("Novimebre");
        
        monthCbxItem[11] =  new ComboBoxItem();
        monthCbxItem[11].setKey("12");
        monthCbxItem[11].setValue("Diciembre");
        
        monthComboBox = new JComboBox(monthCbxItem);
        monthComboBox.setSelectedItem(monthCbxItem[6]);
//        endDateDatePicker.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                setEndDateValue();
//            }
//            
//            
//        });
        monthComboBox.setBounds(200, 50, 160, 25);
        reportParamPanel.add(monthComboBox);
        
        
        typeLabel = new JLabel("Tipo Libro:");
        typeLabel.setBounds(50,80, 120, 25);
        reportParamPanel.add(typeLabel);
        
        ComboBoxItem[] typeBookCbxItem = null;
        typeBookCbxItem = new ComboBoxItem[2];
        
        typeBookCbxItem[0] =  new ComboBoxItem();
        typeBookCbxItem[0].setKey("0");
        typeBookCbxItem[0].setValue("Libro de Venta");

        typeBookCbxItem[1] =  new ComboBoxItem();
        typeBookCbxItem[1].setKey("1");
        typeBookCbxItem[1].setValue("Libro de Compra");
        
        typeBookComboBox = new JComboBox(typeBookCbxItem);
        typeBookComboBox.setBounds(200,80, 160, 25);
        reportParamPanel.add(typeBookComboBox);
        
        createIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/execute_report_txt.png"));
        executeButton = new JButton(" Ejecutar", createIconImage);
        executeButton.setBounds(50, 130, 150, 50);
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveButtonAction(e);
            }
        });
        
        reportParamPanel.add(executeButton);
        
        cancelIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/cancel_24x24.png"));
        cancelButton = new JButton(" Cancelar", cancelIconImage);
        cancelButton.setBounds(220, 130, 150, 50);
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
        setTitle("Hechauka");
        setBounds(650,280,450,230);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }
    
    @Override
    public void doDefaultCloseAction() {
        this.dispose();
        reportController.hechaukaReportViewClosed();
    }
    
    private void saveButtonAction(ActionEvent e) {
        String year = ((ComboBoxItem) yearComboBox.getSelectedItem()).getKey();
        String month = ((ComboBoxItem) monthComboBox.getSelectedItem()).getKey();
        String typeBook = ((ComboBoxItem) typeBookComboBox.getSelectedItem()).getKey();
        HashMap mapReturn = HechaukaReport.getHechaukaReport(year, month, typeBook);
        if((Boolean) mapReturn.get("saved")){
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "", JOptionPane.INFORMATION_MESSAGE);
            doDefaultCloseAction();
        }else{
            JOptionPane.showMessageDialog(null, mapReturn.get("message"), "Atencion", JOptionPane.WARNING_MESSAGE);
        }
    }
}
