/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import static rentfur.receipt.ReceiptController.CASH;
import rentfur.util.DbConnectUtil;

/**
 *
 * @author FDuarte
 */
public class BookController {
    public static final int INVOICE = 1;
    public static final int RECEIPT = 2;
    public static final int CREDIT_NOTE = 3;
    
    public static HashMap getReceiptNum(){
        HashMap mapToReturn = new HashMap();
        
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        try{
                connRentFur = DbConnectUtil.getConnection();    
                String bookQuery = "SELECT id, type, branch, printer, (last_value + 1) as value, fiscal_stamp_number, validity_start_date, validity_finish_date, start_number, end_number  FROM book WHERE validity_finish_date > current_timestamp AND type = ? ORDER BY validity_start_date LIMIT 1";
                ps = connRentFur.prepareStatement(bookQuery);
                ps.setInt(1, RECEIPT);
                rs = ps.executeQuery();
               
                if(rs.next()){
                    mapToReturn.put("branch", rs.getString("branch"));
                    mapToReturn.put("printer", rs.getString("printer"));
                    mapToReturn.put("number", rs.getInt("value"));
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
        
        return mapToReturn;
    }
    
}
