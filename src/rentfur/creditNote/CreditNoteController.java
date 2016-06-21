/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.creditNote;

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
import rentfur.util.DbConnectUtil;
import rentfur.util.User;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class CreditNoteController {
    private CreditNoteCreate creditNoteCreate;
    
    public static final int SUCCESFULLY_SAVED = 0;
    public static final int ERROR_IN_SAVED = 1;
    
    public CreditNoteCreate getCreditNoteCreate(int eventId){
        if(creditNoteCreate == null){
            creditNoteCreate = new CreditNoteCreate(this, eventId);
        }
        return creditNoteCreate;
    }
    
    public void creditNoteCreateClosed(){
        creditNoteCreate = null;
    }
    
    public HashMap createCreditNote(HashMap subjectMap, ArrayList invoiceDetailList, Date invoceDate, int eventId, HashMap invoiceNumMap, double netTotal, double exemptTotal, double tax05total, double tax10total, double taxTotal, double taxted05total, double taxted10total, String observation, int invoiceId){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psInvoiceDetailBillable;
        ResultSet rs;
        int creditNoteId = 0;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            StringBuilder creditNoteInsertSb = new StringBuilder();
            creditNoteInsertSb.append("INSERT INTO credit_note(id, cancelled, creation_date, creation_user_id, credit_note_branch, credit_note_date, credit_note_number, credit_note_printer, subject_address, subject_code, subject_company_alias, subject_fiscal_number, subject_name, subject_phone, discount_rate, discount_total, exempt_total, fiscal_stamp_number, gross_total, invoice_id, net_total, observation, tax05total, tax10total, tax_total, taxted05total, taxted10total, event_id) VALUES (nextval('credit_note_seq'), false, current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps = connRentFur.prepareStatement(creditNoteInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, loggedUser.getId());
            ps.setString(2, invoiceNumMap.get("branch").toString());
            ps.setDate(3, new java.sql.Date (invoceDate.getTime()));
            ps.setString(4, String.format ("%07d", (Integer) invoiceNumMap.get("number")));
            ps.setString(5, invoiceNumMap.get("printer").toString());
            
            ps.setString(6, subjectMap.get("address").toString());
            ps.setString(7, subjectMap.get("code").toString());
            ps.setString(8, subjectMap.get("tradename").toString());
            ps.setString(9, subjectMap.get("fiscalNumber").toString());
            ps.setString(10, subjectMap.get("name").toString());
            ps.setString(11, subjectMap.get("telephone").toString());
            ps.setDouble(12, 0);
            ps.setDouble(13, 0);
            ps.setDouble(14, exemptTotal);
            ps.setString(15, invoiceNumMap.get("fiscalStampNumber").toString());
            ps.setDouble(16, netTotal); //gross_total
            ps.setInt(17, invoiceId);
            ps.setDouble(18, netTotal); //net_total
            ps.setString(19, observation);
            ps.setDouble(20, tax05total);
            ps.setDouble(21, tax10total);
            ps.setDouble(22, taxTotal);
            ps.setDouble(23, taxted05total);
            ps.setDouble(24, taxted10total);
            ps.setInt(25, eventId);
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            while(rs != null && rs.next()){
                creditNoteId = rs.getInt(1);
            }
            
            StringBuilder invoiceDetailBillableUpdateSb = new StringBuilder();
            invoiceDetailBillableUpdateSb.append("UPDATE event_detail SET billable = true WHERE id = ?");
            psInvoiceDetailBillable = connRentFur.prepareStatement(invoiceDetailBillableUpdateSb.toString());
            
            StringBuilder creditNoteDetailInsertSb = new StringBuilder();
            HashMap detailMap = new HashMap();
            creditNoteDetailInsertSb.append("INSERT INTO credit_note_detail(id, credit_note_id, discount_amount, discount_rate, exempt_amount, gross_amount, net_amount, furniture_code, furniture_name, tax_rate, quantity, row_number, unit_price, tax05amount, tax10amount, tax_amount, taxted05amount, taxted10amount, description) VALUES (nextval('credit_note_detail_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = connRentFur.prepareStatement(creditNoteDetailInsertSb.toString());
            
            for(int i = 0 ; i < invoiceDetailList.size(); i++){
                detailMap = (HashMap) invoiceDetailList.get(i);
                //furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                ps.setInt(1, creditNoteId);
                //ps.setString(1, detailMap.get("description").toString());
                ps.setDouble(2, 0);
                ps.setDouble(3, 0);
                ps.setDouble(4, Double.valueOf(detailMap.get("exemptAmount").toString()));//exemptAmount
                ps.setDouble(5, Double.valueOf(detailMap.get("totalAmount").toString()));//gross_amount
                ps.setDouble(6, Double.valueOf(detailMap.get("totalAmount").toString()));//net_amount
                ps.setString(7, detailMap.get("code").toString());
                ps.setString(8, detailMap.get("description").toString());
                ps.setDouble(9, Double.valueOf(detailMap.get("taxRate").toString()));
                ps.setDouble(10, Double.valueOf(detailMap.get("quantity").toString()));
                ps.setInt(11, Integer.valueOf(detailMap.get("rowNumber").toString()));
                ps.setDouble(12, Double.valueOf(detailMap.get("unitPrice").toString()));
                
//                ps.setDouble(13, Double.valueOf(detailMap.get("tax5Amount").toString()));
//                ps.setDouble(14, Double.valueOf(detailMap.get("tax10Amount").toString()));
                
                ps.setDouble(15, Double.valueOf(detailMap.get("taxAmount").toString()));
                
                if(Double.valueOf(detailMap.get("taxRate").toString())==5){
                    ps.setDouble(13, Double.valueOf(detailMap.get("taxAmount").toString()));
                    ps.setDouble(14, Double.valueOf("0"));
//                    
                    ps.setDouble(16, Double.valueOf(detailMap.get("tax5Amount").toString()));
                    ps.setDouble(17, Double.valueOf("0"));
                    
                }else if(Double.valueOf(detailMap.get("taxRate").toString())==10){
                    ps.setDouble(13, Double.valueOf("0"));
                    ps.setDouble(14, Double.valueOf(detailMap.get("taxAmount").toString()));
                
                    ps.setDouble(16, Double.valueOf("0"));
                    ps.setDouble(17, Double.valueOf(detailMap.get("tax10Amount").toString()));
                }else{
                    ps.setDouble(13, Double.valueOf("0"));
                    ps.setDouble(14, Double.valueOf("0"));
                    ps.setDouble(16, Double.valueOf("0"));
                    ps.setDouble(17, Double.valueOf("0"));
                }
                ps.setString(18, detailMap.get("description").toString());
                ps.addBatch();
                
                if(Integer.valueOf(detailMap.get("eventDetailId").toString()) != 0){
                    psInvoiceDetailBillable.setInt(1, Integer.valueOf(detailMap.get("eventDetailId").toString()));
                    psInvoiceDetailBillable.executeUpdate();
                }
//                if(detailedInvoice){
//                    EventController.updateEventDetailBilled(Integer.valueOf(detailMap.get("eventDetailId").toString()));
//                }
//                EventController.updateEventBillableBalance(eventId, netTotal);
                

            }
            ps.executeBatch();
            ps.clearBatch();
            
            StringBuilder invoiceUpdateSb = new StringBuilder();
            invoiceUpdateSb.append("UPDATE invoice SET whith_credit_note = true WHERE id = ?");
            ps = connRentFur.prepareStatement(invoiceUpdateSb.toString());
            ps.setInt(1, invoiceId);
            ps.executeUpdate();
            
            StringBuilder eventUpdateSb = new StringBuilder();
            eventUpdateSb.append("UPDATE event SET billable_balance = billable_balance + ? WHERE id = ?");
            ps = connRentFur.prepareStatement(eventUpdateSb.toString());
            ps.setDouble(1, netTotal);
            ps.setInt(2, eventId);
            ps.executeUpdate();
            
            updateBook((Integer) invoiceNumMap.get("number"));
            
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            if(psInvoiceDetailBillable!=null){
                psInvoiceDetailBillable.close();
            }
            
            mapToReturn.put("id", eventId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Evento registrado correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CreditNoteController.class.getName()).log(Level.SEVERE, null, ex);
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
            ps.setInt(2, BookController.CREDIT_NOTE);
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CreditNoteController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public static ArrayList getCreditNotesByEventId(int eventId){
        Connection connRentFur = null;
        PreparedStatement psCreditNote;
        PreparedStatement psCreditNoteDetail = null;
        ResultSet rsCreditNote;
        ResultSet rsCreditNoteDetail = null;
        ResultSetMetaData rsCreditNoteMd;
        ResultSetMetaData rsCreditNoteDetailMd;
        ArrayList<HashMap<String,Object>> creditNoteList = new ArrayList<>();
        int creditNoteId = 0;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            //Credit Note
            StringBuilder creditNoteSelectSb = new StringBuilder();
            creditNoteSelectSb.append("SELECT id, cancelled, cancelled_date, cancelled_reason, creation_date, creation_user_id, credit_note_branch, credit_note_date, credit_note_number, credit_note_printer, subject_address, subject_code, subject_company_alias, subject_fiscal_number, subject_name, subject_phone, discount_rate, discount_total, exempt_total, fiscal_stamp_number, gross_total, invoice_id, last_modification_date, last_modification_user_id, net_total, observation, status, tax05total, tax10total, tax_total, taxted05total, taxted10total, event_id FROM credit_note WHERE event_id = ?");

            //Credit Note Detail
            StringBuilder creditNoteDetailSelectSb = new StringBuilder();
            creditNoteDetailSelectSb.append("SELECT id, credit_note_id, discount_amount, discount_rate, exempt_amount, gross_amount, net_amount, observation, furniture_code, furniture_name, tax_rate, quantity, row_number, unit_price, tax05amount, tax10amount, tax_amount, taxted05amount, taxted10amount, description FROM credit_note_detail WHERE  credit_note_id = ? ORDER BY row_number ASC");

            
            psCreditNote = connRentFur.prepareStatement(creditNoteSelectSb.toString());
            psCreditNote.setInt(1, eventId);
            HashMap <String,Object> creditNoteMap;
            ArrayList creditNoteDetailList;
            HashMap <String,Object> creditNoteDetailMap;
            rsCreditNote = psCreditNote.executeQuery();
            rsCreditNoteMd = rsCreditNote.getMetaData();
            ArrayList<String> columns;
            ArrayList<String> detailColumns;
            columns = new ArrayList<>(rsCreditNoteMd.getColumnCount());
            
            for(int i = 1; i <= rsCreditNoteMd.getColumnCount(); i++){
                columns.add(rsCreditNoteMd.getColumnName(i));
            }
            
            while(rsCreditNote.next()){                
                creditNoteMap = new HashMap<>(columns.size());
                for(String col : columns) {
                    creditNoteMap.put(col, rsCreditNote.getObject(col));
                }
                creditNoteId = rsCreditNote.getInt("id");
                psCreditNoteDetail = connRentFur.prepareStatement(creditNoteDetailSelectSb.toString());
                psCreditNoteDetail.setInt(1, creditNoteId);
                creditNoteDetailList = new ArrayList();
                rsCreditNoteDetail = psCreditNoteDetail.executeQuery();
                rsCreditNoteDetailMd = rsCreditNoteDetail.getMetaData();
                detailColumns = new ArrayList<>(rsCreditNoteDetailMd.getColumnCount());
                for(int i = 1; i <= rsCreditNoteDetailMd.getColumnCount(); i++){
                    detailColumns.add(rsCreditNoteDetailMd.getColumnName(i));
                }
                
                while(rsCreditNoteDetail.next()){
                    creditNoteDetailMap = new HashMap<>(detailColumns.size());
                    for(String col : detailColumns) {
                        creditNoteDetailMap.put(col, rsCreditNoteDetail.getObject(col));
                    }
                    creditNoteDetailList.add(creditNoteDetailMap);
                }
                creditNoteMap.put("creditNoteDetail", creditNoteDetailList);
                creditNoteList.add(creditNoteMap);
            }
            
            connRentFur.commit();
            
            if(psCreditNoteDetail != null){
                psCreditNoteDetail.close();
            }
            if(rsCreditNoteDetail != null){
                rsCreditNoteDetail.close();
            }
            psCreditNote.close();
            rsCreditNote.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CreditNoteController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        return creditNoteList;
    }
}
