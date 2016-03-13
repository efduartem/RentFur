/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furnitureFamily;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindow;

/**
 *
 * @author FDuarte
 */
public class FurnitureFamilyController {
    private FurnitureFamilyCreate furnitureFamilyCreate;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    
    public FurnitureFamilyCreate getFurnitureFamilyCreate() {
        if(furnitureFamilyCreate==null){
            furnitureFamilyCreate = new FurnitureFamilyCreate(this);
        }
        
        return furnitureFamilyCreate;
    }
    
    public HashMap saveFurnitureFamily(String code, String description){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");

            if(code == null || code.equals("")){                
                mapToReturn.put("message", "El campo codigo es requerido para la creacion de la familia de Mobiliriarios");
            }else if(description == null || description.equals("")){
                mapToReturn.put("message", "Para crear la familia de Mobiliriarios debe ingresar una Descripcion");
            }else{
                connRentFur = DbConnectUtil.getConnection();
                String furnitureFamilyInsert = "INSERT INTO furniture_family (id, code, description) values (nextval('furniture_family_seq'), ?, ?)";
                ps = connRentFur.prepareStatement(furnitureFamilyInsert);
                ps.setString(1, code);
                ps.setString(2, description);
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Familia de Mobiliriarios creada correctamente");
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
        //removeFurnitureFamilyCreate
        furnitureFamilyCreate = null;
    }
}
