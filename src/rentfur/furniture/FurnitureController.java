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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import rentfur.furnitureFamily.FurnitureFamilyCreate;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;

/**
 *
 * @author hp
 */
public class FurnitureController {
    private MainWindowController mainWindowController;
    private FurnitureCreate furnitureCreate;
    private FurnitureIndex furnitureIndex;
    private FurnitureFamilyCreate furnitureFamilyCreate;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "furniture";
    public final String ALL_VALUES = "Todos";
    
    public FurnitureCreate getFurnitureCreate(){
        if(furnitureCreate == null){
            furnitureCreate = new FurnitureCreate(this);
        }
        return furnitureCreate;
    }
    
    public FurnitureIndex getFurnitureIndex(MainWindowController mainWindowController){
        if(furnitureIndex == null){
            furnitureIndex = new FurnitureIndex(this);
        }
        this.mainWindowController = mainWindowController;
        return furnitureIndex;
    }
    
    public ComboBoxItem[] getFurnitureFamiliesForComboBox(boolean addAllOption){
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
            if(addAllOption){
                furnitureFamilies = new ComboBoxItem[rows+1];
                furnitureFamilies[0] =  new ComboBoxItem();
                furnitureFamilies[0].setKey(ALL_VALUES);
                furnitureFamilies[0].setValue(ALL_VALUES);
                while(rs.next()){
                    furnitureFamilies[rs.getRow()] =  new ComboBoxItem();
                    furnitureFamilies[rs.getRow()].setKey(rs.getString("id"));
                    furnitureFamilies[rs.getRow()].setValue(rs.getString("description"));
                }
            }else{
                furnitureFamilies = new ComboBoxItem[rows];
                
                while(rs.next()){
                    furnitureFamilies[rs.getRow()-1] =  new ComboBoxItem();
                    furnitureFamilies[rs.getRow()-1].setKey(rs.getString("id"));
                    furnitureFamilies[rs.getRow()-1].setValue(rs.getString("description"));
                }
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
    
    public ComboBoxItem[] getFurnitureStatusForComboBox(){
        ComboBoxItem[] furnitureStatus = null;
        
        try{
            
            furnitureStatus = new ComboBoxItem[3];
            furnitureStatus[0] =  new ComboBoxItem();
            furnitureStatus[0].setKey(ALL_VALUES);
            furnitureStatus[0].setValue(ALL_VALUES);

            furnitureStatus[1] =  new ComboBoxItem();
            furnitureStatus[1].setKey("true");
            furnitureStatus[1].setValue("Activo");

            furnitureStatus[2] =  new ComboBoxItem();
            furnitureStatus[2].setKey("false");
            furnitureStatus[2].setValue("Inactivo");
                
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return furnitureStatus;
    }
    
    public void updateIndexFurnitureFamilyComboBox(){
        furnitureIndex.updateFurnitureFamilyComboBox();
    }

    HashMap saveFurniture(String description, String familyId, String unitPrice, String unitCostPrice, String fineAmountPerUnit, String observation, boolean active) {
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            if(description == null || description.equals("")){
                mapToReturn.put("message", "El campo Descripcion es requerido para la creacion del Mobiliriario");
            }else if(unitPrice == null || unitPrice.equals("")){
                mapToReturn.put("message", "El campo Precio unitario es requerido para la creacion del Mobiliriario");
            }else if(unitCostPrice == null || unitCostPrice.equals("")){
                mapToReturn.put("message", "El campo Costo unitario es requerido para la creacion del Mobiliriario");
            }else if(fineAmountPerUnit == null || fineAmountPerUnit.equals("")){
                mapToReturn.put("message", "El campo Multa es requerido para la creacion del Mobiliriario");
            }else if(familyId == null || familyId.equals("")){
                mapToReturn.put("message", "No cuenta con familias de Mobiliario creadas, favor crear al menos una para continuar con la creacion de Mobiliario");
            }else{
                
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder furnitureInsertSb = new StringBuilder();
                furnitureInsertSb.append("INSERT INTO furniture(id, code, description, furniture_family_id, unit_price, fine_amount_per_unit, unit_cost_price,  observation, active)");
                furnitureInsertSb.append(" VALUES (nextval('furniture_seq'), LPAD(nextval('furniture_code_seq')::text, 4, '0'), ?, ?, ?, ?, ?, ?, ?)");
                
                ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
                ps.setString(1, description);
                ps.setInt(2, Integer.valueOf(familyId));
                ps.setDouble(3, Double.valueOf(nf.parse(unitPrice).toString()));
                ps.setDouble(4, Double.valueOf(nf.parse(fineAmountPerUnit).toString()));
                ps.setDouble(5, Double.valueOf(nf.parse(unitCostPrice).toString()));
                ps.setString(6, observation);
                ps.setBoolean(7, active);
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Mobiliriario creado correctamente");
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
    
    public void setFurnitureIndexResultsTable(DefaultTableModel furnituresResultDefaultTableModel, boolean searchPressed, String code, String description, String familyId, String furnitureStatus){
        
        try{
            if(!searchPressed){
                furnituresResultDefaultTableModel.addColumn("Id");
                furnituresResultDefaultTableModel.addColumn("Código");
                furnituresResultDefaultTableModel.addColumn("Descripción");
                furnituresResultDefaultTableModel.addColumn("Familia");
                furnituresResultDefaultTableModel.addColumn("Precio Unitario");
                furnituresResultDefaultTableModel.addColumn("Costo Unitario");
                furnituresResultDefaultTableModel.addColumn("Multa");
                furnituresResultDefaultTableModel.addColumn("Stock total");
                furnituresResultDefaultTableModel.addColumn("");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = furnituresResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                furnituresResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(code, description, familyId, furnitureStatus);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){

                    row = new Object[furnituresResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("code");
                    row[2] = resultValueMap.get("description");
                    row[3] = resultValueMap.get("family");
                    row[4] = amountFormat.format((Double)resultValueMap.get("unitPrice"));
                    row[5] = amountFormat.format((Double)resultValueMap.get("unitCostPrice"));
                    row[6] = amountFormat.format((Double)resultValueMap.get("fineAmountPerUnit"));
                    row[7] = amountFormat.format((Double)resultValueMap.get("totalStock"));
                    if((Boolean)resultValueMap.get("active")){
                        row[8] = "Inactivar";
                    }else{
                        row[8] = "Activar";
                    }
                    
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
    
    public ArrayList getSearchResultList(String code, String description, String familyId, String furnitureStatus){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList listToReturn = new ArrayList();
        
        try{
            HashMap resultValuesMap;
            connRentFur = DbConnectUtil.getConnection();
            
            if(code==null){
                code="";
            }
            
            if(description==null){
                description="";
            }
            
            StringBuilder furnituriesQuery = new StringBuilder();
            furnituriesQuery.append("SELECT f.id, f.code, f.description, f.unit_price, f.fine_amount_per_unit, f.unit_cost_price, f.total_stock, (SELECT description FROM furniture_family ff WHERE ff.id = f.furniture_family_id) as family, f.active FROM furniture f WHERE f.code ilike ? AND f.description ilike ?");
            if(familyId!= null && !familyId.equals(ALL_VALUES)){
                furnituriesQuery.append(" AND furniture_family_id = ").append(familyId);
            }
            
            if(furnitureStatus!= null && !furnitureStatus.equals(ALL_VALUES)){
                furnituriesQuery.append(" AND active = ").append(furnitureStatus);
            }
            
            furnituriesQuery.append(" ORDER BY f.code, f.description, f.active");
            ps = connRentFur.prepareStatement(furnituriesQuery.toString());
            ps.setString(1, "%"+code+"%");
            ps.setString(2, "%"+description+"%");
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("description", rs.getString("description"));
                resultValuesMap.put("unitPrice", rs.getDouble("unit_price"));
                resultValuesMap.put("fineAmountPerUnit", rs.getDouble("fine_amount_per_unit"));
                resultValuesMap.put("unitCostPrice", rs.getDouble("unit_cost_price"));
                resultValuesMap.put("totalStock", rs.getDouble("total_stock"));
                resultValuesMap.put("family", rs.getString("family"));
                resultValuesMap.put("active", rs.getBoolean("active"));
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
    
    public HashMap saveFurnitureFamily(String description){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");

            if(description == null || description.equals("")){
                mapToReturn.put("message", "No ha ingresado una Descripcion, favor registre el nombre de la Familia");
            }else{
                connRentFur = DbConnectUtil.getConnection();
                String furnitureFamilyInsert = "INSERT INTO furniture_family (id, code, description) values (nextval('furniture_family_seq'), LPAD(nextval('furniture_family_code_seq')::text, 4, '0'), ?)";
                ps = connRentFur.prepareStatement(furnitureFamilyInsert);
                ps.setString(1, description);
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
    
    public void getFurnitureCreateView(){
        mainWindowController.setVisibleFurnitureCreateInternalFrame();
    }
    
    public void getFurnitureFamilyCreateView(){
        mainWindowController.setVisibleFurnitureFamilyCreateInternalFrame();
    }
    
    public FurnitureFamilyCreate getFurnitureFamilyCreate(){
        if(furnitureFamilyCreate==null){
            furnitureFamilyCreate = new FurnitureFamilyCreate(this);
        }
        return furnitureFamilyCreate;
    }
    
    public void setDisabledIndexView(){
        furnitureIndex.setDisabledElements();
    }
    
    public void setEnabledIndexView(){
        furnitureIndex.setEnableddElements();
    }
    
    public void indexViewClosed(){
        furnitureIndex = null;
    }
    
    public void createViewClosed(){
        furnitureCreate = null;
    }
    
    public void createFamilyClosed(){
        furnitureFamilyCreate = null;
    }
    
    public HashMap inactivateFurniture(int furnitureId){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            connRentFur = DbConnectUtil.getConnection();
            String furnitureDelete = "UPDATE furniture SET active = false WHERE id = ?";
            ps = connRentFur.prepareStatement(furnitureDelete);
            ps.setInt(1, furnitureId);
            ps.executeUpdate();
            ps.close();
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "");
        }catch(Throwable th){
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
    
    public void searchFurnitureButtonAction(){
        furnitureIndex.searchFurnitureButtonAction(null);
    }
}

