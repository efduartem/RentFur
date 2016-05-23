package rentfur.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author FDuarte
 */
public class SQLUtilService {
    
    public boolean isUniqueCode(String table, String code){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        boolean valueToReturn = false;
        try{
                connRentFur = DbConnectUtil.getConnection();    
                StringBuilder countQuery = new StringBuilder();
                countQuery.append("SELECT count(*) from ");
                countQuery.append(table);
                countQuery.append(" WHERE code = ? ");
                ps = connRentFur.prepareStatement(countQuery.toString());
                ps.setString(1, code);
                rs = ps.executeQuery();
                if(rs.next()){
                    if(rs.getInt(1)==0){
                        valueToReturn = true;
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
        
        return valueToReturn;
    }
    
    public static boolean isUniqueFiscalNumber(String fiscalNumber){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        boolean valueToReturn = false;
        try{
                connRentFur = DbConnectUtil.getConnection();    
                StringBuilder countQuery = new StringBuilder();
                countQuery.append("SELECT count(*) FROM subject WHERE split_part(fiscal_number, '-', 1) = ? ");
                ps = connRentFur.prepareStatement(countQuery.toString());
                
                if(fiscalNumber.contains("-")){
                    fiscalNumber = fiscalNumber.split("-")[0];
                }
                
                ps.setString(1, fiscalNumber);
                System.out.println("ps: "+ps.toString());
                rs = ps.executeQuery();
                if(rs.next()){
                    if(rs.getInt(1)==0){
                        valueToReturn = true;
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
        System.out.println("valueToReturn: "+valueToReturn);
        return valueToReturn;
    }
    
    public static String getSystemConfigurationValue(String key){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        String valueToReturn = "";
        try{
                connRentFur = DbConnectUtil.getConnection();    
                StringBuilder countQuery = new StringBuilder();
                countQuery.append("SELECT value FROM system_configuration WHERE key = ? ");
                ps = connRentFur.prepareStatement(countQuery.toString());
                ps.setString(1, key);
                
                rs = ps.executeQuery();
                if(rs.next()){
                    valueToReturn = rs.getString("value");
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
        
        return valueToReturn;
    }
}
