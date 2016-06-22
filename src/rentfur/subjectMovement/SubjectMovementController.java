/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subjectMovement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rentfur.util.DbConnectUtil;

/**
 *
 * @author FDuarte
 */
public class SubjectMovementController {
    
    public static final int DEBIT_MOVEMENT_TYPE = 0;
    public static final int CREDIT_MOVEMENT_TYPE = 1;
    
    public static final int CONTRACT_MOVEMENT_DOCUMENT_TYPE = 0;
    public static final int CONTRACT_ANEXED_MOVEMENT_TYPE = 1;
    public static final int PENALTY_MOVEMENT_TYPE = 2;
    public static final int RECEIPT_MOVEMENT_TYPE = 3;
    public static final int RECEIPT_CANCELLED_MOVEMENT_TYPE = 4;
    
    public static void updateEventBalance(int movementType, int documentType, String documentNumber, double movementAmount, String subjectCode, String movement){
        
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        double previousBalance = 0;
        double finalBalance = 0;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            
            StringBuilder subjectMovementSelectSb = new StringBuilder();
            subjectMovementSelectSb.append("SELECT balance FROM subject_movement WHERE subject_code = ? ORDER BY id DESC LIMIT 1");
            
            ps = connRentFur.prepareStatement(subjectMovementSelectSb.toString());
            ps.setString(1, subjectCode);
            rs = ps.executeQuery();
            if(rs.next()){
                previousBalance = rs.getDouble("balance");
            }
            
            StringBuilder subjectMovementInsertSb = new StringBuilder();
            subjectMovementInsertSb.append("INSERT INTO subject_movement(id, subject_code, type, movement, movement_date, document_type, document_number, debit, credit, balance, previous_balance) VALUES (nextval('subject_movement_seq'), ?, ?, ?, current_timestamp, ?, ?, ?, ?, ?, ?)");

            ps = connRentFur.prepareStatement(subjectMovementInsertSb.toString());
            ps.setString(1, subjectCode);
            ps.setInt(2, movementType);
            ps.setString(3, movement);
            ps.setInt(4, documentType);
            ps.setString(5, documentNumber);
            if(movementType == DEBIT_MOVEMENT_TYPE){
                ps.setDouble(6, movementAmount);
                ps.setDouble(7, new Double("0"));
                finalBalance = previousBalance + movementAmount;
            }else if(movementType == CREDIT_MOVEMENT_TYPE){
                ps.setDouble(6, new Double("0"));
                ps.setDouble(7, movementAmount);
                finalBalance = previousBalance - movementAmount;
            }
            
            ps.setDouble(8, finalBalance);
            ps.setDouble(9, previousBalance);
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SubjectMovementController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public static String getMovementType(int movementType){
        String movementTypeString = "";
        switch(movementType){
            case DEBIT_MOVEMENT_TYPE: 
                movementTypeString = "DEBITO";
                break;
            case CREDIT_MOVEMENT_TYPE: 
                movementTypeString = "CREDITO";
                break;
        }
        
        return movementTypeString;
    }
}
