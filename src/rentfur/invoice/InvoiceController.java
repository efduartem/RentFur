/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rentfur.book.BookController;
import rentfur.event.EventController;
import rentfur.util.DbConnectUtil;
import rentfur.util.User;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class InvoiceController {
    private InvoiceCreate invoiceCreate;
    private InvoiceShow invoiceShow;
    
    public static final String CASH_BILL_DESCRIPTION = "CONTADO";
    public static final String CREDIT_BILL_DESCRIPTION = "CREDITO";
    
    public static final String CASH_BILL = "0";
    public static final String CREDIT_BILL = "1";
    
    public static final int SUCCESFULLY_SAVED = 0;
    public static final int ERROR_IN_SAVED = 1;
    
    public InvoiceCreate getInvoiceCreate(int eventId){
        if(invoiceCreate == null){
            invoiceCreate = new InvoiceCreate(this, eventId);
        }
        return invoiceCreate;
    }
    
    public InvoiceShow getInvoiceShow(int eventId, int invoiceId){
        if(invoiceShow == null){
            invoiceShow = new InvoiceShow(this, eventId, invoiceId);
        }
        return invoiceShow;
    }
    
    public void invoiceCreateClosed(){
        invoiceCreate = null;
    }
    
    public void invoiceShowClosed(){
        invoiceShow = null;
    }
    
    public static ArrayList getInvoicesByEventId(int eventId){
        Connection connRentFur = null;
        PreparedStatement psInvoice;
        PreparedStatement psInvoiceDetail = null;
        ResultSet rsInvoice;
        ResultSet rsInvoiceDetail = null;
        ResultSetMetaData rsInvoiceMd;
        ResultSetMetaData rsInvoiceDetailMd;
        ArrayList<HashMap<String,Object>> invoiceList = new ArrayList<>();
        int invoiceId = 0;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            //Invoice
            StringBuilder invoiceSelectSb = new StringBuilder();
            invoiceSelectSb.append("SELECT id, balance, cancelled, cancelled_date, cancelled_reason, creation_date, creation_user_id, subject_address, subject_code, subject_tradename, subject_fiscal_number, subject_name, subject_phone, discount_rate, discount_total, exempt_total, fiscal_stamp_number, gross_total, invoice_branch, invoice_number, invoice_printer, invoice_type, invoicing_date, last_modification_date, last_modification_user_id, net_total, observation, status, term, type, tax05total, tax10total, tax_total, taxted05total, taxted10total, printed, event_id, whith_credit_note FROM invoice WHERE event_id = ? ORDER BY id ASC");

            //Invoice Detail
            StringBuilder invoiceDetailSelectSb = new StringBuilder();
            invoiceDetailSelectSb.append("SELECT id, description, discount_amount, discount_rate, exempt_amount, gross_amount, invoice_id, net_amount, observation, furniture_code, furniture_name, tax_rate, quantity, row_number, status, unit_price, tax05amount, tax10amount, tax_amount, taxted05amount, taxted10amount, event_detail_id FROM invoice_detail WHERE  invoice_id = ? ORDER BY row_number ASC");

            
            psInvoice = connRentFur.prepareStatement(invoiceSelectSb.toString());
            psInvoice.setInt(1, eventId);
            HashMap <String,Object> invoiceMap;
            ArrayList invoiceDetailList;
            HashMap <String,Object> invoiceDetailMap;
            rsInvoice = psInvoice.executeQuery();
            rsInvoiceMd = rsInvoice.getMetaData();
            ArrayList<String> columns;
            ArrayList<String> detailColumns;
            columns = new ArrayList<>(rsInvoiceMd.getColumnCount());
            
            for(int i = 1; i <= rsInvoiceMd.getColumnCount(); i++){
                columns.add(rsInvoiceMd.getColumnName(i));
            }
            
            while(rsInvoice.next()){                
                invoiceMap = new HashMap<>(columns.size());
                for(String col : columns) {
                    invoiceMap.put(col, rsInvoice.getObject(col));
                }
                invoiceId = rsInvoice.getInt("id");
                psInvoiceDetail = connRentFur.prepareStatement(invoiceDetailSelectSb.toString());
                psInvoiceDetail.setInt(1, invoiceId);
                invoiceDetailList = new ArrayList();
                rsInvoiceDetail = psInvoiceDetail.executeQuery();
                rsInvoiceDetailMd = rsInvoiceDetail.getMetaData();
                detailColumns = new ArrayList<>(rsInvoiceDetailMd.getColumnCount());
                for(int i = 1; i <= rsInvoiceDetailMd.getColumnCount(); i++){
                    detailColumns.add(rsInvoiceDetailMd.getColumnName(i));
                }
                
                while(rsInvoiceDetail.next()){
                    invoiceDetailMap = new HashMap<>(detailColumns.size());
                    for(String col : detailColumns) {
                        invoiceDetailMap.put(col, rsInvoiceDetail.getObject(col));
                    }
                    invoiceDetailList.add(invoiceDetailMap);
                }
                invoiceMap.put("invoiceDetail", invoiceDetailList);
                invoiceList.add(invoiceMap);
            }
            
            connRentFur.commit();
            
            if(psInvoiceDetail != null){
                psInvoiceDetail.close();
            }
            if(rsInvoiceDetail != null){
                rsInvoiceDetail.close();
            }
            psInvoice.close();
            rsInvoice.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
        }finally{
            try{
                if(connRentFur != null){
                    connRentFur.close();
                }
            }catch(SQLException sqle){
                System.err.println(sqle.getMessage());
                System.err.println(sqle);
            }
        }
        
        return invoiceList;
    }
    
    public static HashMap getInvoiceById(int invoiceId){
        Connection connRentFur = null;
        PreparedStatement psInvoice;
        PreparedStatement psInvoiceDetail = null;
        ResultSet rsInvoice;
        ResultSet rsInvoiceDetail = null;
        ResultSetMetaData rsInvoiceMd;
        ResultSetMetaData rsInvoiceDetailMd;
        HashMap <String,Object> invoiceMap = null;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            //Invoice
            StringBuilder invoiceSelectSb = new StringBuilder();
            invoiceSelectSb.append("SELECT id, balance, cancelled, cancelled_date, cancelled_reason, creation_date, creation_user_id, subject_address, subject_code, subject_tradename, subject_fiscal_number, subject_name, subject_phone, discount_rate, discount_total, exempt_total, fiscal_stamp_number, gross_total, invoice_branch, invoice_number, invoice_printer, invoice_type, invoicing_date, last_modification_date, last_modification_user_id, net_total, observation, status, term, type, tax05total, tax10total, tax_total, taxted05total, taxted10total, printed, event_id FROM invoice WHERE id = ?");

            //Invoice Detail
            StringBuilder invoiceDetailSelectSb = new StringBuilder();
            invoiceDetailSelectSb.append("SELECT id, description, discount_amount, discount_rate, exempt_amount, gross_amount, invoice_id, net_amount, observation, furniture_code, furniture_name, tax_rate, quantity, row_number, status, unit_price, tax05amount, tax10amount, tax_amount, taxted05amount, taxted10amount, event_detail_id FROM invoice_detail WHERE  invoice_id = ? ORDER BY row_number ASC");

            
            psInvoice = connRentFur.prepareStatement(invoiceSelectSb.toString());
            psInvoice.setInt(1, invoiceId);
            ArrayList invoiceDetailList;
            HashMap <String,Object> invoiceDetailMap;
            rsInvoice = psInvoice.executeQuery();
            rsInvoiceMd = rsInvoice.getMetaData();
            ArrayList<String> columns;
            ArrayList<String> detailColumns;
            columns = new ArrayList<>(rsInvoiceMd.getColumnCount());
            
            for(int i = 1; i <= rsInvoiceMd.getColumnCount(); i++){
                columns.add(rsInvoiceMd.getColumnName(i));
            }
            
            while(rsInvoice.next()){                
                invoiceMap = new HashMap<>(columns.size());
                for(String col : columns) {
                    invoiceMap.put(col, rsInvoice.getObject(col));
                }
                
                psInvoiceDetail = connRentFur.prepareStatement(invoiceDetailSelectSb.toString());
                psInvoiceDetail.setInt(1, invoiceId);
                invoiceDetailList = new ArrayList();
                rsInvoiceDetail = psInvoiceDetail.executeQuery();
                rsInvoiceDetailMd = rsInvoiceDetail.getMetaData();
                detailColumns = new ArrayList<>(rsInvoiceDetailMd.getColumnCount());
                for(int i = 1; i <= rsInvoiceDetailMd.getColumnCount(); i++){
                    detailColumns.add(rsInvoiceDetailMd.getColumnName(i));
                }
                
                while(rsInvoiceDetail.next()){
                    invoiceDetailMap = new HashMap<>(detailColumns.size());
                    for(String col : detailColumns) {
                        invoiceDetailMap.put(col, rsInvoiceDetail.getObject(col));
                    }
                    invoiceDetailList.add(invoiceDetailMap);
                }
                invoiceMap.put("invoiceDetail", invoiceDetailList);
            }
            
            connRentFur.commit();
            
            if(psInvoiceDetail != null){
                psInvoiceDetail.close();
            }
            if(rsInvoiceDetail != null){
                rsInvoiceDetail.close();
            }
            psInvoice.close();
            rsInvoice.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
        }finally{
            try{
                if(connRentFur != null){
                    connRentFur.close();
                }
            }catch(SQLException sqle){
                System.err.println(sqle.getMessage());
                System.err.println(sqle);
            }
        }
        
        return invoiceMap;
    }
    
    public HashMap createInvoice(HashMap subjectMap, ArrayList invoiceDetailList, Date invoceDate, int eventId, HashMap invoiceNumMap, double netTotal, double exemptTotal, double tax05total, double tax10total, double taxTotal, double taxted05total, double taxted10total, String observation, boolean detailedInvoice){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        int invoiceId = 0;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            StringBuilder invoiceInsertSb = new StringBuilder();
            invoiceInsertSb.append("INSERT INTO invoice(id, balance, cancelled, creation_date, creation_user_id, subject_address, subject_code, subject_tradename, subject_fiscal_number, subject_name, subject_phone, discount_rate, discount_total, exempt_total, fiscal_stamp_number, gross_total, invoice_branch, invoice_number, invoice_printer, invoice_type, invoicing_date, last_modification_date, last_modification_user_id, net_total, observation, type, tax05total, tax10total, tax_total, taxted05total, taxted10total, printed, event_id) VALUES (nextval('invoice_seq'), ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps = connRentFur.prepareStatement(invoiceInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
            
            ps.setDouble(1, netTotal); //BALANCE
            ps.setBoolean(2, false); //cancelled
            ps.setInt(3, loggedUser.getId());
            ps.setString(4, subjectMap.get("address").toString());
            ps.setString(5, subjectMap.get("code").toString());
            ps.setString(6, subjectMap.get("tradename").toString());
            ps.setString(7, subjectMap.get("fiscalNumber").toString());
            ps.setString(8, subjectMap.get("name").toString());
            ps.setString(9, subjectMap.get("telephone").toString());
            ps.setDouble(10, 0);
            ps.setDouble(11, 0);
            ps.setDouble(12, exemptTotal);
            ps.setString(13, invoiceNumMap.get("fiscalStampNumber").toString());
            ps.setDouble(14, netTotal); //gross_total
            ps.setString(15, invoiceNumMap.get("branch").toString());
            ps.setString(16, String.format ("%07d", (Integer) invoiceNumMap.get("number")));
            ps.setString(17, invoiceNumMap.get("printer").toString());
            ps.setString(18, CASH_BILL_DESCRIPTION);
            ps.setDate(19, new java.sql.Date (invoceDate.getTime()));
            ps.setInt(20, loggedUser.getId());
            ps.setDouble(21, netTotal); //net_total
            ps.setString(22, observation);
            ps.setString(23, CASH_BILL);
            ps.setDouble(24, tax05total);
            ps.setDouble(25, tax10total);
            ps.setDouble(26, taxTotal);
            ps.setDouble(27, taxted05total);
            ps.setDouble(28, taxted10total);
            ps.setBoolean(29, false);
            ps.setInt(30, eventId);
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            while(rs != null && rs.next()){
                invoiceId = rs.getInt(1);
            }
            
            StringBuilder invoiceDetailInsertSb = new StringBuilder();
            HashMap detailMap = new HashMap();
            invoiceDetailInsertSb.append("INSERT INTO invoice_detail(id, description, discount_amount, discount_rate, exempt_amount, gross_amount, invoice_id, net_amount, furniture_code, furniture_name, tax_rate, quantity, row_number, unit_price, tax05amount, tax10amount, tax_amount, taxted05amount, taxted10amount, event_detail_id) VALUES (nextval('invoice_detail_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = connRentFur.prepareStatement(invoiceDetailInsertSb.toString());
            
            for(int i = 0 ; i < invoiceDetailList.size(); i++){
                detailMap = (HashMap) invoiceDetailList.get(i);
                //furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                
                ps.setString(1, detailMap.get("description").toString());
                ps.setDouble(2, 0);
                ps.setDouble(3, 0);
                ps.setDouble(4, Double.valueOf(detailMap.get("exemptAmount").toString()));//exemptAmount
                ps.setDouble(5, Double.valueOf(detailMap.get("totalAmount").toString()));//gross_amount
                ps.setInt(6, invoiceId);
                ps.setDouble(7, Double.valueOf(detailMap.get("totalAmount").toString()));//net_amount
                ps.setString(8, detailMap.get("code").toString());
                ps.setString(9, detailMap.get("description").toString());
                ps.setDouble(10, Double.valueOf(detailMap.get("taxRate").toString()));
                ps.setDouble(11, Double.valueOf(detailMap.get("quantity").toString()));
                ps.setInt(12, Integer.valueOf(detailMap.get("rowNumber").toString()));
                ps.setDouble(13, Double.valueOf(detailMap.get("unitPrice").toString()));
                
//                ps.setDouble(14, Double.valueOf(detailMap.get("tax5Amount").toString()));
//                ps.setDouble(15, Double.valueOf(detailMap.get("tax10Amount").toString()));
                
                
                ps.setDouble(16, Double.valueOf(detailMap.get("taxAmount").toString()));
                
                if(Double.valueOf(detailMap.get("taxRate").toString())==5){
                    ps.setDouble(14, Double.valueOf(detailMap.get("taxAmount").toString()));
                    ps.setDouble(15, Double.valueOf("0"));
                    
                    ps.setDouble(17, Double.valueOf(detailMap.get("tax5Amount").toString()));
                    ps.setDouble(18, Double.valueOf("0"));
                    
                }else if(Double.valueOf(detailMap.get("taxRate").toString())==10){
                    ps.setDouble(14, Double.valueOf("0"));
                    ps.setDouble(15, Double.valueOf(detailMap.get("taxAmount").toString()));
                
                    ps.setDouble(17, Double.valueOf("0"));
                    ps.setDouble(18, Double.valueOf(detailMap.get("tax10Amount").toString()));
                }else{
                    ps.setDouble(14, Double.valueOf("0"));
                    ps.setDouble(15, Double.valueOf("0"));
                    ps.setDouble(17, Double.valueOf("0"));
                    ps.setDouble(18, Double.valueOf("0"));
                }
                
                if(detailedInvoice){
                    ps.setInt(19, Integer.valueOf(detailMap.get("eventDetailId").toString()));
                    EventController.updateEventDetailBilled(Integer.valueOf(detailMap.get("eventDetailId").toString()));
                }else{
                    ps.setInt(19, Integer.valueOf("0"));
                }
                
                ps.addBatch();

            }
            ps.executeBatch();
            ps.clearBatch();
            
            EventController.updateEventBillableBalance(eventId, netTotal);
            
            updateBook((Integer) invoiceNumMap.get("number"));
            
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            mapToReturn.put("id", eventId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Factura registrada correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
            mapToReturn.put("message", th.getMessage());
        }finally{
            try{
                if(connRentFur != null){
                    connRentFur.close();
                }
            }catch(SQLException sqle){
                System.err.println(sqle.getMessage());
                System.err.println(sqle);
            }
        }
        return mapToReturn;
    }
    
    public void updateBook(int number){
        
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            
            StringBuilder eventUpdateSb = new StringBuilder();
            eventUpdateSb.append("UPDATE book set last_value = ? WHERE type = ?");

            ps = connRentFur.prepareStatement(eventUpdateSb.toString());
            ps.setDouble(1, number);
            ps.setInt(2, BookController.INVOICE);
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
        }finally{
            try{
                if(connRentFur != null){
                    connRentFur.close();
                }
            }catch(SQLException sqle){
                System.err.println(sqle.getMessage());
                System.err.println(sqle);
            }
        }
    }
    
}
