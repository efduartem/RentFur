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
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
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
    
    public void setFurnitureIndexResultsTable(DefaultTableModel furnituresResultDefaultTableModel, boolean searchPressed, String code, String description){
        
        try{
            if(!searchPressed){
                furnituresResultDefaultTableModel.addColumn("Código");
                furnituresResultDefaultTableModel.addColumn("Descripción");
            }
            ArrayList searchResultList = getSearchResultList(code, description);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){

                    row = new Object[furnituresResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("code");
                    row[1] = resultValueMap.get("description");
                    /*if(permisosIdMap.containsKey(rs.getInt("idpermisos"))){
                        fila[1] = Boolean.TRUE;
                    }else{
                        fila[1] = Boolean.FALSE;
                    } */

                    furnituresResultDefaultTableModel.addRow(row);

                }
            }
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
    }
    
    public ArrayList getSearchResultList(String code, String description){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList listToReturn = null;
        
        try{
            HashMap resultValuesMap;
            connRentFur = DbConnectUtil.getConnection();
            String furnituriesQuery = "SELECT code, description FROM furniture ORDER BY description";
            ps = connRentFur.prepareStatement(furnituriesQuery);
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("descripcion", rs.getString("descripcion"));
                listToReturn.add(resultValuesMap);
                /*if(permisosIdMap.containsKey(rs.getInt("idpermisos"))){
                    fila[1] = Boolean.TRUE;
                }else{
                    fila[1] = Boolean.FALSE;
                } */ 
            }
            rs.close();
            ps.close();

        }catch(SQLException th){
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
        
        return listToReturn;
    }
}
