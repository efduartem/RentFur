/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.report;

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
import rentfur.util.DbConnectUtil;

/**
 *
 * @author FDuarte
 */
public class HechaukaReport {
    
    public static HashMap getHechaukaReport(String year, String month, String typeBook){
        
        HashMap valurToReturn = new HashMap();
        String reportType = "1";
        //String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
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
        String fileName;
        try{
            valurToReturn.put("saved", false);
            if(typeBook.equals("0")){
                fileName = System.getProperty("user.home")+"\\Desktop\\LibroVenta_"+year+"_"+month+".txt";
            }else{
                fileName = System.getProperty("user.home")+"\\Desktop\\LibroCompra_"+year+"_"+month+".txt";
            }
            

//            String fileurl = sys+ "\\Desktop\\new";
//            System.out.println("fileUrl: "+fileurl);
            
            
            connRentFur = DbConnectUtil.getConnection();    
            
            
            Calendar initCalendar = Calendar.getInstance();
            initCalendar.set(Integer.valueOf(year), (Integer.valueOf(month)-1), 1);
            lastDayOfMonth = initCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Integer.valueOf(year), (Integer.valueOf(month)-1), lastDayOfMonth);
            
            Date initDate = initCalendar.getTime();
            Date endDate = endCalendar.getTime();
            String invoicesQuery;
            if(typeBook.equals("0")){
                invoicesQuery = "SELECT subject_fiscal_number, subject_name, fiscal_stamp_number, invoice_branch || '-' || invoice_printer || '-' || invoice_number as invoiceNumber, net_total, tax05total, tax10total, tax_total, taxted05total, taxted10total, exempt_total, to_char(invoicing_date, 'DD/MM/YYYY') as invoicing_date FROM invoice WHERE invoicing_date >= ? AND invoicing_date <= ? AND cancelled = false";
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

                fichero = new FileWriter(fileName);
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
            }else{
                invoicesQuery = "SELECT provider_fiscal_number, provider_name, fiscal_stamp_number, invoice_branch || '-' || invoice_printer || '-' || invoice_number as invoiceNumber, net_total, total_tax_5, total_tax_10, total_tax, total_taxable_5, total_taxable_10, exempt_total, to_char(invoicing_date, 'DD/MM/YYYY') as invoicing_date FROM purchase_invoice WHERE invoicing_date >= ? AND invoicing_date <= ?";
                ps = connRentFur.prepareStatement(invoicesQuery);
                ps.setTimestamp(1, new java.sql.Timestamp(initDate.getTime()));
                ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));

                rs = ps.executeQuery();
                int gravada10, gravada5, iva5, iva10;
                detailList = new ArrayList();
                while(rs.next()) {
                    detailMap = new HashMap();
                    gravada10 = (new BigDecimal(rs.getDouble("total_taxable_10")/1.1).setScale(0, RoundingMode.HALF_UP).intValue());
                    iva10 = (new BigDecimal(rs.getDouble("total_taxable_10")/11).setScale(0, RoundingMode.HALF_UP).intValue());
                    gravada5 = (new BigDecimal(rs.getDouble("total_taxable_5")/1.05).setScale(0, RoundingMode.HALF_UP).intValue());
                    iva5 = (new BigDecimal(rs.getDouble("total_taxable_5")/21).setScale(0, RoundingMode.HALF_UP).intValue());

                    detailMap.put("subject_fiscal_number",rs.getString("provider_fiscal_number"));
                    detailMap.put("subject_name",rs.getString("provider_name"));
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
                    detailMap.put("type","1");
                    detailList.add(detailMap);
                    netTotal = netTotal + (gravada10 + gravada5);
                    detailQuantity++;
                }
                
                invoicesQuery = "SELECT subject_fiscal_number, subject_name, fiscal_stamp_number, credit_note_branch || '-' || credit_note_printer || '-' || credit_note_number as invoiceNumber, net_total, tax05total, tax10total, tax_total, taxted05total, taxted10total, exempt_total, to_char(credit_note_date, 'DD/MM/YYYY') as invoicing_date FROM credit_note WHERE credit_note_date >= ? AND credit_note_date <= ? AND cancelled = false";
                ps = connRentFur.prepareStatement(invoicesQuery);
                ps.setTimestamp(1, new java.sql.Timestamp(initDate.getTime()));
                ps.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));

                rs = ps.executeQuery();
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
                    detailMap.put("type","2");
                    detailList.add(detailMap);
                    netTotal = netTotal + (gravada10 + gravada5);
                    detailQuantity++;
                }

                headerList = new ArrayList();
                headerList.add("1");
                headerList.add(year.concat(month));
                headerList.add(reportType);
                headerList.add("911");
                headerList.add("211");
                headerList.add("5116215");
                headerList.add("6");
                headerList.add("ERALDO FABIAN DUARTE MARTINEZ");
                headerList.add("0");
                headerList.add("0");
                headerList.add("0");
                headerList.add(detailQuantity);
                headerList.add(netTotal);
                headerList.add("NO");
                headerList.add("2");
                
                fichero = new FileWriter(fileName);
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
                    pw.write(detailMap.get("fiscal_stamp_number").toString()+"\t");
                    pw.write(detailMap.get("type").toString()+"\t");
                    pw.write(detailMap.get("invoiceNumber").toString()+"\t");
                    pw.write(detailMap.get("invoicing_date").toString()+"\t");
                    pw.write(detailMap.get("taxted10total").toString()+"\t");
                    pw.write(detailMap.get("tax10total").toString()+"\t");
                    pw.write(detailMap.get("taxted05total").toString()+"\t");
                    pw.write(detailMap.get("tax05total").toString()+"\t");
                    pw.write(detailMap.get("exempt_total").toString()+"\t");
                    pw.write("0\t");
                    pw.write(detailMap.get("condicionVenta").toString()+"\t");
                    pw.write(detailMap.get("cantidadCuotas").toString()+"\t");
                    pw.println("");
                }
            }
            valurToReturn.put("saved", true);
            valurToReturn.put("message", "Reporte obtenido correctamente, el archivo correspondiente puede encontrarlo en el escritorio");
        } catch (IOException e) {
            valurToReturn.put("message", e.getMessage());
            e.printStackTrace();
        } catch (SQLException ex) {
            valurToReturn.put("message", ex.getMessage());
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
        
        return valurToReturn;
    }
    
}

