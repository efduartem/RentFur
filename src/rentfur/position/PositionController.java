/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import rentfur.util.DbConnectUtil;

/**
 *
 * @author FDuarte
 */
public class PositionController {
    private PositionCreate positionCreate;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String ROLE_RF_FURNITURE = "ROLE_RF_FURNITURE";
    public final String ROLE_RF_PROVIDER = "ROLE_RF_PROVIDER";
    public final String ROLE_RF_SUBJECT = "ROLE_RF_SUBJECT";
    public final String ROLE_RF_POSITION = "ROLE_RF_POSITION";
    public final String ROLE_RF_USER = "ROLE_RF_USER";
    
    
    public PositionCreate getPositionCreate(){
        if(positionCreate == null){
            positionCreate = new PositionCreate(this);
        }
        return positionCreate;
    }
    
    public void createViewClosed(){
        positionCreate = null;
    }
    
    public HashMap savePosition(String description, HashMap rolesMap) {
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            if(description == null || description.equals("")){
                mapToReturn.put("message", "El campo Descripcion es requerido para la creacion del Cargo");
            }else{
                
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder furnitureInsertSb = new StringBuilder();
                furnitureInsertSb.append("INSERT INTO position(id, code, description)");
                furnitureInsertSb.append(" VALUES (nextval('position_seq'), LPAD(nextval('position_code_seq')::text, 4, '0'), ?)");
                ps = connRentFur.prepareStatement(furnitureInsertSb.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, description);
                ps.executeUpdate();
                
                int positionId = 0;
                rs = ps.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    positionId = (int) rs.getLong(1);
                }
                
                insertNewsPositionRole(positionId, rolesMap);
                
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Cargo creado correctamente");
            }
            
        }catch(SQLException th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
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
    
    public void insertNewsPositionRole(int positionId, HashMap rolesMap) throws SQLException{
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();

            StringBuilder furnitureInsertSb = new StringBuilder();
            furnitureInsertSb.append("INSERT INTO position_role(position_id, role_id, only_query)");
            furnitureInsertSb.append(" VALUES (?, ?, ?)");
            ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
            
            int roleId = 0;
                    
            //FURNITURE
            if((Boolean) rolesMap.get("furniture")){
                boolean furnitureReadOnly = (Boolean) rolesMap.get("furnitureReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_FURNITURE);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, furnitureReadOnly);
                ps.addBatch();
            }

            //PROVIDER
            if((Boolean) rolesMap.get("provider")){
                boolean provider = (Boolean) rolesMap.get("provider");
                boolean providerReadOnly = (Boolean) rolesMap.get("providerReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_PROVIDER);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, providerReadOnly);
                ps.addBatch();
            }

            //SUBJECT
            if((Boolean) rolesMap.get("subject")){ 
                boolean subjectReadOnly = (Boolean) rolesMap.get("subjectReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_SUBJECT);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, subjectReadOnly);
                ps.addBatch();
            }

            //USERS
            if((Boolean) rolesMap.get("users")){
                boolean users = (Boolean) rolesMap.get("users");
                boolean usersReadOnly = (Boolean) rolesMap.get("usersReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_USER);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, usersReadOnly);
                ps.addBatch();
            }
           
            ps.executeBatch();
            ps.close();
            
        }catch(SQLException th){
            throw th;
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
    
    public int getRoleIdByCode(String roleCode) throws SQLException{
        int roleId = 0;
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();

            StringBuilder furnitureInsertSb = new StringBuilder();
            furnitureInsertSb.append("SELECT id FROM role WHERE code = ? ");
            ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
            ps.setString(1, roleCode);
            rs = ps.executeQuery();
            if(rs.next()){
                roleId = rs.getInt("id");
            }
            rs.close();
            ps.close();
            
        }catch(SQLException th){
            throw th;
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
        
        return roleId;
    }
}
