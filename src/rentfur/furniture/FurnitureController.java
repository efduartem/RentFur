/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.SQLUtilService;

/**
 *
 * @author hp
 */
public class FurnitureController {
    private FurnitureCreate furnitureCreate;
    private FurnitureIndex furnitureIndex;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "furniture";
    
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
            SQLUtilService sqlUtilService = new SQLUtilService();
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
            }else if(!sqlUtilService.isUniqueCode(TABLE_NAME, code)){
                mapToReturn.put("message", "El codigo $code ya existe favor ingresar otro valor");
                furnitureCreate.setCodeTextFieldColor(Color.red);
            }else{
                
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder furnitureInsertSb = new StringBuilder();
                furnitureInsertSb.append("INSERT INTO furniture(id, code, description, furniture_family_id, unit_price, total_stock,fine_amount_per_unit, unit_cost_price,  observation)");
                furnitureInsertSb.append(" VALUES (nextval('furniture_seq'), ?, ?, ?, ?, ?, ?, ?, ?)");
                
                ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
                ps.setString(1, code);
                ps.setString(2, description);
                ps.setInt(3, Integer.valueOf(familyId));
                ps.setDouble(4, Double.valueOf(nf.parse(unitPrice).toString()));
                ps.setDouble(5, Double.valueOf(nf.parse(totalStock).toString()));
                ps.setDouble(6, Double.valueOf(nf.parse(fineAmountPerUnit).toString()));
                ps.setDouble(7, Double.valueOf(nf.parse(unitCostPrice).toString()));
                ps.setString(8, observation);
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Mobiliriario creada correctamente");
            }
            
        }catch(SQLException | NumberFormatException | ParseException th){
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
            th.printStackTrace();
        }
    }
    
    public ArrayList getSearchResultList(String code, String description){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList listToReturn = new ArrayList();
        
        try{
            HashMap resultValuesMap;
            connRentFur = DbConnectUtil.getConnection();
            String furnituriesQuery = "SELECT code, description FROM furniture ORDER BY description";
            ps = connRentFur.prepareStatement(furnituriesQuery);
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("description", rs.getString("description"));
                System.out.println(resultValuesMap);
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
        
        return listToReturn;
    }
    
}

