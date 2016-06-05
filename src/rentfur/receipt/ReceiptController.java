package rentfur.receipt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import rentfur.book.BookController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.User;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class ReceiptController {
    private MainWindowController mainWindowController;
    private ReceiptCreate receiptCreate;
    private ReceiptShow receiptShow;
    private UserRoles userRoles;
    public static final int SUCCESFULLY_SAVED = 0;
    public static final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "receipt";
    public static final String ALL_VALUES = "Todos";
    
    public static final int PAYMENT_METHOD_ALL_VALUES = 0;
    public static final int CASH = 1;
    public static final int PAY_CHECK = 2;
    public static final int DATED_CHECK = 3;
    
    public ReceiptCreate getReceiptCreate(int eventId){
        if(receiptCreate == null){
            receiptCreate = new ReceiptCreate(this, eventId);
        }
        return receiptCreate;
    }
    
    public void receiptCreateClosed(){
        receiptCreate = null;
    }
    
    public ReceiptShow getReceiptShow(int eventId, int receiptId){
        if(receiptShow == null){
            receiptShow = new ReceiptShow(this, eventId, receiptId);
        }
        return receiptShow;
    }
    
    public void receiptShowClosed(){
        receiptShow = null;
    }
    
    public static ComboBoxItem[] getPaymentMethodAvailablesForCreateReceipt(boolean addAllOption){
        
        ComboBoxItem[] statusAvailables = null;
        try{
            
            if(addAllOption){
                statusAvailables = new ComboBoxItem[4];
                
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(ALL_VALUES);
                statusAvailables[0].setValue(ALL_VALUES);
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(CASH));
                statusAvailables[1].setValue(getPaymentMethod(CASH));
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(PAY_CHECK));
                statusAvailables[2].setValue(getPaymentMethod(PAY_CHECK));
                
                statusAvailables[3] =  new ComboBoxItem();
                statusAvailables[3].setKey(String.valueOf(DATED_CHECK));
                statusAvailables[3].setValue(getPaymentMethod(DATED_CHECK));

            }else{
                statusAvailables = new ComboBoxItem[3];
                         
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(String.valueOf(CASH));
                statusAvailables[0].setValue(getPaymentMethod(CASH));
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(PAY_CHECK));
                statusAvailables[1].setValue(getPaymentMethod(PAY_CHECK));
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(DATED_CHECK));
                statusAvailables[2].setValue(getPaymentMethod(DATED_CHECK));
                
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return statusAvailables;
    }
    
    public static String getPaymentMethod(int paymentMethod){
        String paymentMethodString;
        switch(paymentMethod){
            case PAYMENT_METHOD_ALL_VALUES: 
                paymentMethodString = "TODOS";
                break;
            case CASH: 
                paymentMethodString = "EFECTIVO";
                break;
            case PAY_CHECK: 
                paymentMethodString = "CHEQUE";
                break;
           case DATED_CHECK: 
                paymentMethodString = "CHEQUE DIFERIDO";
                break;
            default: 
                paymentMethodString = "";
                break;
        }
        
        return paymentMethodString;
    }
    
    public static ComboBoxItem[] getBanksForComboBox(boolean addAllOption){
        ComboBoxItem[] banks = null;
        
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        try{
                connRentFur = DbConnectUtil.getConnection();    
                int rows = 0;
                String banksQuery = "SELECT id, name FROM bank";
                ps = connRentFur.prepareStatement(banksQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = ps.executeQuery();
                if (rs.last()) {
                    rows = rs.getRow();
                    rs.beforeFirst();
                }
                
                if(addAllOption){
                    banks = new ComboBoxItem[rows+1];
                    
                    banks[0] =  new ComboBoxItem();
                    banks[0].setKey(ALL_VALUES);
                    banks[0].setValue(ALL_VALUES);
                    
                    while(rs.next()){
                        banks[rs.getRow()] =  new ComboBoxItem();
                        banks[rs.getRow()].setKey(rs.getString("id"));
                        banks[rs.getRow()].setValue(rs.getString("name"));
                    }
                }else{
                    banks = new ComboBoxItem[rows];
                    while(rs.next()){
                        banks[rs.getRow()-1] =  new ComboBoxItem();
                        banks[rs.getRow()-1].setKey(rs.getString("id"));
                        banks[rs.getRow()-1].setValue(rs.getString("name"));
                    }

                }
                
                ps.close();
                rs.close();
        }catch(SQLException e){
            System.err.println(e.getMessage());
            System.err.println(e);
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
        
        return banks;
    }
    
    public HashMap createReceipt(HashMap subjectMap, ArrayList paymentList, double netTotal, double balance, String observation, Date receiptDate, int eventId, HashMap receiptNumMap){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psContractNumber = null;
        ResultSet rs;
        ResultSet rsContractNumber = null;
        int receiptId = 0;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            StringBuilder eventInsertSb = new StringBuilder();
            eventInsertSb.append("INSERT INTO receipt(id, advance_amount, creation_date, creation_user_id, subject_code, subject_name, last_modification_date, last_modification_user_id, observation, receipt_branch, receipt_date, receipt_number, receipt_printer, status, total_payed, total_to_pay, event_id) VALUES (nextval('receipt_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ps = connRentFur.prepareStatement(eventInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, netTotal);
            ps.setTimestamp(2, new Timestamp(new Date().getTime()));
            ps.setInt(3, loggedUser.getId());
            ps.setString(4, subjectMap.get("code").toString());
            ps.setString(5, subjectMap.get("name").toString());
            ps.setTimestamp(6, new Timestamp(new Date().getTime()));
            ps.setInt(7, loggedUser.getId());
            ps.setString(8, observation);
            ps.setString(9, receiptNumMap.get("branch").toString());
            ps.setDate(10, new java.sql.Date (receiptDate.getTime()));
            ps.setString(11, String.format ("%07d", (Integer) receiptNumMap.get("number")));
            ps.setString(12, receiptNumMap.get("branch").toString());
            ps.setInt(13, 0);
            ps.setDouble(14, netTotal);
            ps.setDouble(15, netTotal);
            ps.setInt(16, eventId);
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            while(rs != null && rs.next()){
                receiptId = rs.getInt(1);
            }
            
            StringBuilder eventDetailInsertSb = new StringBuilder();
            HashMap paymentMap = new HashMap();
            eventDetailInsertSb.append("INSERT INTO payment_method(id, balance, bank_id, document_emition_date, document_number, payment_type, receipt_id, total_amount, payment_date, due_date) VALUES (nextval('payment_method_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps = connRentFur.prepareStatement(eventDetailInsertSb.toString());
            int paymentMethod;
            for(int i = 0 ; i < paymentList.size(); i++){
                paymentMap = (HashMap) paymentList.get(i);
                paymentMethod = Integer.valueOf(paymentMap.get("paymentMethod").toString());
                ps.setDouble(1, Double.valueOf(paymentMap.get("amount").toString()));
                if(paymentMethod==PAY_CHECK || paymentMethod==DATED_CHECK){
                    ps.setInt(2, Integer.valueOf(paymentMap.get("bank").toString()));
                    ps.setDate(3, new java.sql.Date(((Date)paymentMap.get("emitionDate")).getTime()));
                    ps.setDate(9, new java.sql.Date(((Date)paymentMap.get("dueDate")).getTime()));
                }else{
                    ps.setNull(2, java.sql.Types.INTEGER);
                    ps.setNull(3, java.sql.Types.DATE);
                    ps.setNull(9, java.sql.Types.DATE);
                }
                ps.setString(4, paymentMap.get("docNumber").toString());
                ps.setInt(5, Integer.valueOf(paymentMap.get("paymentMethod").toString()));
                ps.setInt(6, receiptId);
                ps.setDouble(7, Double.valueOf(paymentMap.get("amount").toString()));
                if(paymentMethod==DATED_CHECK){
                    ps.setDate(8, new java.sql.Date(((Date)paymentMap.get("paymentDate")).getTime()));
                }else{
                    ps.setNull(8, java.sql.Types.DATE);
                }
                
                ps.addBatch();
            }
            ps.executeBatch();
            ps.clearBatch();
            
            updateEventBalance(balance, eventId);
            updateBook((Integer) receiptNumMap.get("number"));
            
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            if(psContractNumber!=null){
                psContractNumber.close();
            }
            
            if(rsContractNumber!=null){
                rsContractNumber.close();
            }
            
            mapToReturn.put("id", eventId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Recibo registrado correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void updateEventBalance(double balance, int eventId){
        
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            
            StringBuilder eventUpdateSb = new StringBuilder();
            eventUpdateSb.append("UPDATE event set balance = ? WHERE id = ?");

            ps = connRentFur.prepareStatement(eventUpdateSb.toString());
            ps.setDouble(1, balance);
            ps.setInt(2, eventId);
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
            ps.setInt(2, BookController.RECEIPT);
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public static ArrayList getReceiptsByEventId(int eventId){
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psPaymentMethod = null;
        ResultSet rs;
        ResultSet rsPaymentMethod = null;
        ArrayList receiptList = new ArrayList();    
        int receiptId = 0;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            //Recibo
            StringBuilder receiptSelectSb = new StringBuilder();
            receiptSelectSb.append("SELECT id, cancelled, cancelled_date, cancelled_reason, creation_date, creation_user_id, subject_code, subject_name, last_modification_date, last_modification_user_id, observation, receipt_branch, receipt_date, receipt_number, receipt_printer, status, total_payed, total_to_pay FROM receipt WHERE event_id = ?");

            //Medios de Pago
            StringBuilder paymentMethodSelectSb = new StringBuilder();
            paymentMethodSelectSb.append("SELECT id, balance, (SELECT code FROM bank WHERE id = pm.bank_id) as bank_code, (SELECT name FROM bank WHERE id = pm.bank_id) as bank_name, pm.bank_id, document_emition_date, document_number, payment_type, total_amount, payment_date, due_date FROM payment_method pm WHERE receipt_id = ?");
            ArrayList paymentMethodList;
            HashMap paymentMehodMap;
            
            ps = connRentFur.prepareStatement(receiptSelectSb.toString());
            ps.setInt(1, eventId);
            HashMap receiptMap;
            rs = ps.executeQuery();
            while(rs.next()){
                receiptMap = new HashMap();
                receiptId = rs.getInt("id");
                receiptMap.put("id", rs.getInt("id"));
                receiptMap.put("cancelled", rs.getBoolean("cancelled"));
                receiptMap.put("cancelledDate", rs.getTimestamp("cancelled_date"));
                receiptMap.put("cancelledReason", rs.getString("cancelled_reason"));
                receiptMap.put("creationDate", rs.getTimestamp("creation_date"));
                receiptMap.put("creationUserId", rs.getInt("creation_user_id"));
                receiptMap.put("subjectCode", rs.getString("subject_code"));
                receiptMap.put("subjectName", rs.getString("subject_name"));
                receiptMap.put("lastModificationDate", rs.getTimestamp("last_modification_date"));
                receiptMap.put("lastModificationUserId", rs.getInt("last_modification_user_id"));
                receiptMap.put("observation", rs.getString("observation"));
                receiptMap.put("receiptBranch", rs.getString("receipt_branch"));
                receiptMap.put("receiptPrinter", rs.getString("receipt_printer"));
                receiptMap.put("receiptNumber", rs.getString("receipt_number"));
                receiptMap.put("receiptDate", rs.getTimestamp("receipt_date"));
                receiptMap.put("totalPayed", rs.getDouble("total_payed"));
                
                
                psPaymentMethod = connRentFur.prepareStatement(paymentMethodSelectSb.toString());
                psPaymentMethod.setInt(1, receiptId);
                paymentMethodList = new ArrayList();
                rsPaymentMethod = psPaymentMethod.executeQuery();
                while(rsPaymentMethod.next()){
                    paymentMehodMap = new HashMap();
                    paymentMehodMap.put("id", rsPaymentMethod.getInt("id"));
                    paymentMehodMap.put("balance", rsPaymentMethod.getDouble("balance"));
                    paymentMehodMap.put("bankId", rsPaymentMethod.getInt("bank_id"));
                    paymentMehodMap.put("bankCode", rsPaymentMethod.getString("bank_code"));
                    paymentMehodMap.put("bankName", rsPaymentMethod.getString("bank_name"));
                    paymentMehodMap.put("documentEmitionDate", rsPaymentMethod.getTimestamp("document_emition_date"));
                    paymentMehodMap.put("documentNumber", rsPaymentMethod.getString("document_number"));
                    paymentMehodMap.put("paymentTypeInt", rsPaymentMethod.getInt("payment_type"));
                    paymentMehodMap.put("paymentType", getPaymentMethod(rsPaymentMethod.getInt("payment_type")));
                    paymentMehodMap.put("totalAmount", rsPaymentMethod.getDouble("total_amount"));
                    paymentMehodMap.put("paymentDate", rsPaymentMethod.getTimestamp("payment_date"));
                    paymentMehodMap.put("dueDate", rsPaymentMethod.getTimestamp("due_date"));
                    paymentMethodList.add(paymentMehodMap);
                }
                receiptMap.put("paymentMethod", paymentMethodList);
                
                receiptList.add(receiptMap);
            }
            
            connRentFur.commit();
            
            if(psPaymentMethod != null){
                psPaymentMethod.close();
            }
            if(rsPaymentMethod != null){
                rsPaymentMethod.close();
            }
            ps.close();
            rs.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        return receiptList;
    }
    
    public static HashMap getReceiptById(int receiptId){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        HashMap receipttMap = new HashMap();    
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder receiptSelectSb = new StringBuilder();
            receiptSelectSb.append("SELECT id, advance_amount, cancelled, cancelled_date, cancelled_reason, creation_date, creation_user_id, subject_code, subject_name, last_modification_date, last_modification_user_id, observation, receipt_branch, receipt_date, receipt_number, receipt_printer, status, total_payed, total_to_pay, event_id FROM receipt WHERE id = ?");

            ps = connRentFur.prepareStatement(receiptSelectSb.toString());
            ps.setInt(1, receiptId);
            
            rs = ps.executeQuery();
            if(rs.next()){
                receipttMap.put("id", rs.getInt("id"));
                receipttMap.put("advanceAmount", rs.getDouble("advance_amount"));
                receipttMap.put("cancelled", rs.getBoolean("cancelled"));
                receipttMap.put("cancelledDate", rs.getTimestamp("cancelled_date"));
                receipttMap.put("creationDate", rs.getTimestamp("creation_date"));
                receipttMap.put("cancelledReason", rs.getString("cancelled_reason"));
                receipttMap.put("creationUserId", rs.getInt("creation_user_id"));
                receipttMap.put("subjectCode", rs.getString("subject_code"));
                receipttMap.put("subjectName", rs.getString("subject_name"));
                receipttMap.put("lastModificationDate", rs.getTimestamp("last_modification_date"));
                receipttMap.put("lastModificationUserId", rs.getInt("last_modification_user_id"));
                receipttMap.put("observation", rs.getString("observation"));
                receipttMap.put("receiptBranch", rs.getString("receipt_branch"));
                receipttMap.put("receiptDate", rs.getTimestamp("receipt_date"));
                receipttMap.put("receiptNumber", rs.getString("receipt_number"));
                receipttMap.put("receiptPrinter", rs.getString("receipt_printer"));
                receipttMap.put("status", rs.getInt("status"));
                receipttMap.put("totalPayed", rs.getDouble("total_payed"));
                receipttMap.put("totalToPay", rs.getDouble("total_to_pay"));
                receipttMap.put("eventId", rs.getInt("event_id"));
            }
            
            StringBuilder paymentMethodSelectSb = new StringBuilder();
            paymentMethodSelectSb.append("SELECT id, balance, (SELECT code FROM bank WHERE id = pm.bank_id) as bank_code, (SELECT name FROM bank WHERE id = pm.bank_id) as bank_name, pm.bank_id, document_deposit_date, document_emition_date, document_number, payment_type, receipt_id, total_amount, payment_date, due_date FROM payment_method pm WHERE receipt_id = ?");
            ArrayList paymentMethodList = new ArrayList();
            HashMap paymentMethodMap;
            ps = connRentFur.prepareStatement(paymentMethodSelectSb.toString());
            ps.setInt(1, receiptId);
            
            rs = ps.executeQuery();
            while(rs.next()){
                paymentMethodMap = new HashMap();
                paymentMethodMap.put("id", rs.getInt("id"));
                paymentMethodMap.put("bankCode", rs.getString("bank_code"));
                paymentMethodMap.put("bankName", rs.getString("bank_name"));
                paymentMethodMap.put("bankId", rs.getDouble("bank_id"));
                paymentMethodMap.put("documentEmitionDate", rs.getTimestamp("document_emition_date"));
                paymentMethodMap.put("documentNumber", rs.getString("document_number"));
                paymentMethodMap.put("totalAmount", rs.getDouble("total_amount"));
                paymentMethodMap.put("paymentTypeInt", rs.getInt("payment_type"));
                paymentMethodMap.put("paymentType", getPaymentMethod(rs.getInt("payment_type")));
                paymentMethodMap.put("paymentDate", rs.getTimestamp("payment_date"));
                paymentMethodMap.put("dueDate", rs.getTimestamp("due_date"));
                paymentMethodList.add(paymentMethodMap);

            }
            
            receipttMap.put("paymentMethod", paymentMethodList);
            
            connRentFur.commit();
            rs.close();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        return receipttMap;
    }
    
    public HashMap cancelReceipt(int receiptId, String cancelledReason){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        double totalPayed = 0;
        int eventId = 0;
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            String receiptSelect = "SELECT total_payed, event_id FROM receipt WHERE id = ?";
            ps = connRentFur.prepareStatement(receiptSelect);
            ps.setInt(1, receiptId);
            rs = ps.executeQuery();
            if(rs.next()){
                totalPayed = rs.getDouble("total_payed");
                eventId = rs.getInt("event_id");
            }
            
            String receiptUpdate = "UPDATE receipt SET cancelled = ?, cancelled_reason = ?, cancelled_date = current_timestamp WHERE id = ?";
            ps = connRentFur.prepareStatement(receiptUpdate);
            ps.setBoolean(1, Boolean.TRUE);
            ps.setString(2, cancelledReason);
            ps.setInt(3, receiptId);
            ps.executeUpdate();
            
            String eventUpdate = "UPDATE event SET balance = (balance + ?) WHERE id = ?";
            ps = connRentFur.prepareStatement(eventUpdate);
            ps.setDouble(1, totalPayed);
            ps.setInt(2, eventId);
            ps.executeUpdate();
            
            ps.close();
            
            connRentFur.commit();
            
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Recibo Anulado correctamente");
        }catch(SQLException th){
            try {
                connRentFur.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(ReceiptController.class.getName()).log(Level.SEVERE, null, ex);
            }
            mapToReturn.put("message", th.getMessage());
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
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
}
