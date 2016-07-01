/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FDuarte
 */
public class HechaukaReport {
    
    public static void getHechaukaReport(){
        String month; String reportType;
        month = "06";
        reportType = "1";
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        int lastDayOfMonth = 0;
        int detailQuantity = 0;
        int netTotal = 0;
        ArrayList headerList;
        ArrayList detailList;
        HashMap detailMap;
        FileWriter fichero = null;
        PrintWriter pw = null;
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        try
        {
            
            connRentFur = DbConnectUtil.getConnection();    
            
            
            Calendar initCalendar = Calendar.getInstance();
            initCalendar.set(Integer.valueOf(year), (Integer.valueOf(month)-1), 1);
            lastDayOfMonth = initCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Integer.valueOf(year), (Integer.valueOf(month)-1), lastDayOfMonth);
            
            Date initDate = initCalendar.getTime();
            Date endDate = endCalendar.getTime();
            
            String invoicesQuery = "SELECT subject_fiscal_number, subject_name, fiscal_stamp_number, invoice_branch || '-' || invoice_printer || '-' || invoice_number as invoiceNumber, net_total, tax05total, tax10total, tax_total, taxted05total, taxted10total, exempt_total, to_char(invoicing_date, 'DD/MM/YYYY') as invoicing_date FROM invoice WHERE invoicing_date >= ? AND invoicing_date <= ? AND cancelled = false";
            ps = connRentFur.prepareStatement(invoicesQuery);
            ps.setTimestamp(1, new java.sql.Timestamp(initDate.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            
            rs = ps.executeQuery();
            int gravada10, gravada5, iva5, iva10;
            detailList = new ArrayList();
            while(rs.next()) {
                detailMap = new HashMap();
                gravada10 = (new BigDecimal(rs.getDouble("taxted10total")/1.1).setScale(0, RoundingMode.HALF_UP).intValue());
                iva10 = (new BigDecimal(rs.getDouble("taxted10total")/11).setScale(0, RoundingMode.HALF_UP).intValue());
                gravada5 = (new BigDecimal(rs.getDouble("taxted05total")/1.05).setScale(0, RoundingMode.HALF_UP).intValue());
                iva5 = (new BigDecimal(rs.getDouble("taxted05total")/21).setScale(0, RoundingMode.HALF_UP).intValue());
                
                detailMap.put("subject_fiscal_number",rs.getString("subject_fiscal_number"));
                detailMap.put("subject_name",rs.getString("subject_name"));
                detailMap.put("invoiceNumber", rs.getString("invoiceNumber"));
                detailMap.put("net_total", rs.getInt("net_total"));
                detailMap.put("taxted10total", gravada10);
                detailMap.put("tax10total", iva10);
                detailMap.put("taxted05total", gravada5);
                detailMap.put("tax05total", iva5);
                detailMap.put("exempt_total", rs.getInt("exempt_total"));
                detailMap.put("cantidadCuotas", "0");
                detailMap.put("condicionVenta", "1");
                detailMap.put("fiscal_stamp_number",rs.getString("fiscal_stamp_number"));
                detailMap.put("invoicing_date",rs.getString("invoicing_date"));
                detailList.add(detailMap);
                netTotal = netTotal + rs.getInt("net_total");
                detailQuantity++;
            }
            
            headerList = new ArrayList();
            headerList.add("1");
            headerList.add(year.concat(month));
            headerList.add(reportType);
            headerList.add("921");
            headerList.add("221");
            headerList.add("5116215");
            headerList.add("6");
            headerList.add("ERALDO FABIAN DUARTE MARTINEZ");
            headerList.add("0");
            headerList.add("0");
            headerList.add("0");
            headerList.add(detailQuantity);
            headerList.add(netTotal);
            headerList.add("2");
            
            fichero = new FileWriter("prueba1.txt");
            pw = new PrintWriter(fichero);
            for (int i = 0 ; i < headerList.size(); i++) {
                    if(i != (headerList.size()-1)){
                        pw.write(headerList.get(i).toString()+"\t");
                    }else{
                        pw.write(headerList.get(i).toString());
                    }
            }
            pw.println("");
            
            String ruc = "";
            String dv = "";
            for (int i = 0; i < detailList.size(); i++){
                detailMap = (HashMap) detailList.get(i);
                
                ruc = detailMap.get("subject_fiscal_number").toString().split("-")[0];
                dv = detailMap.get("subject_fiscal_number").toString().split("-")[1];
                
                pw.write("2\t");
                pw.write(ruc+"\t");
                pw.write(dv+"\t");
                pw.write(detailMap.get("subject_name").toString()+"\t");
                pw.write("1\t");
                pw.write(detailMap.get("invoiceNumber").toString()+"\t");
                pw.write(detailMap.get("invoicing_date").toString()+"\t");
                pw.write(detailMap.get("taxted10total").toString()+"\t");
                pw.write(detailMap.get("tax10total").toString()+"\t");
                pw.write(detailMap.get("taxted05total").toString()+"\t");
                pw.write(detailMap.get("tax05total").toString()+"\t");
                pw.write(detailMap.get("exempt_total").toString()+"\t");
                pw.write(detailMap.get("net_total").toString()+"\t");
                pw.write(detailMap.get("condicionVenta").toString()+"\t");
                pw.write(detailMap.get("cantidadCuotas").toString()+"\t");
                pw.write(detailMap.get("fiscal_stamp_number").toString()+"\t");
                pw.println("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(HechaukaReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (IOException e2) {
              e2.printStackTrace();
           }
        }
    }
    
}
