/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;

/**
 *
 * @author hp
 */
public class FurnitureController {
    private FurnitureCreate furnitureCreate;
    private FurnitureIndex furnitureIndex;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    
    
    public FurnitureCreate getFurnitureCreate(){
        if(furnitureCreate == null){
            furnitureCreate = new FurnitureCreate(this);
        }
        return furnitureCreate;
    }
    
    public FurnitureIndex getFurnitureIndex(){
        if(furnitureIndex == null){
            furnitureIndex = new FurnitureIndex(this);
        }
        return furnitureIndex;
    }
    
    public ComboBoxItem[] getFurnitureFamiliesForComboBox(){
        ComboBoxItem[] furnitureFamilies = null;
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            int rows = 0;
            connRentFur = DbConnectUtil.getConnection();
            String furnitureFamilyQuery = "SELECT id, description FROM furniture_family ORDER BY description";
            ps = connRentFur.prepareStatement(furnitureFamilyQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            
            if (rs.last()) {
                rows = rs.getRow();
                rs.beforeFirst();
            }
            furnitureFamilies = new ComboBoxItem[rows];
            
            while(rs.next()){
                furnitureFamilies[rs.getRow()-1] =  new ComboBoxItem();
                furnitureFamilies[rs.getRow()-1].setKey(rs.getString("id"));
                furnitureFamilies[rs.getRow()-1].setValue(rs.getString("description"));
            }
            rs.close();
            ps.close();

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
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
        
        return furnitureFamilies;
    } 

    HashMap saveFurniture(String code, String description, String familyId, String unitPrice, String unitCostPrice, String fineAmountPerUnit, String totalStock, String observation) {
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");

            if(code == null || code.equals("")){                
                mapToReturn.put("message", "El campo codigo es requerido para la creacion del Mobiliriario");
            }else if(description == null || description.equals("")){
                mapToReturn.put("message", "El campo Descripcion es requerido para la creacion del Mobiliriario");
            }else if(unitPrice == null || unitPrice.equals("")){
                mapToReturn.put("message", "El campo Precio unitario es requerido para la creacion del Mobiliriario");
            }else if(unitCostPrice == null || unitCostPrice.equals("")){
                 mapToReturn.put("message", "El campo Costo unitario es requerido para la creacion del Mobiliriario");
            }else if(fineAmountPerUnit == null || fineAmountPerUnit.equals("")){
                  mapToReturn.put("message", "El campo Multa es requerido para la creacion del Mobiliriario");
            }else if(totalStock == null || totalStock.equals("")){
                  mapToReturn.put("message", "El campo Stock es requerido para la creacion del Mobiliriario");
            }else{
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder furnitureInsertSb = new StringBuilder();
                furnitureInsertSb.append("INSERT INTO furniture(id, code, description, furniture_family_id, unit_price, total_stock,fine_amount_per_unit, unit_cost_price,  observation)");
                furnitureInsertSb.append(" VALUES (nextval('furniture_seq'), ?, ?, ?, ?, ?, ?, ?, ?)");
                
                ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
                ps.setString(1, code);
                ps.setString(2, description);
                ps.setInt(3, Integer.valueOf(familyId));
                ps.setDouble(4, Double.valueOf(unitPrice));
                ps.setDouble(5, Double.valueOf(totalStock));
                ps.setDouble(6, Double.valueOf(fineAmountPerUnit));
                ps.setDouble(7, Double.valueOf(unitCostPrice));
                ps.setString(8, observation);
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Mobiliriario creada correctamente");
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
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
    
    public void viewClosed(){
        furnitureCreate = null;
    }
}

