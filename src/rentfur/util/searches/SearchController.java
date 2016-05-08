/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util.searches;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;
import rentfur.furniture.FurnitureController;
import rentfur.util.DbConnectUtil;

/**
 *
 * @author FDuarte
 */
public class SearchController {
    private SubjectSearch subjectSearch;
    private FurnitureSearch furnitureSearch;
    private String subjetcSelectedCode = "";
    private ArrayList furnitureSelectedCodes = new ArrayList();
    
    public SubjectSearch getSubjectSearch(){
        if(subjectSearch == null){
            subjectSearch = new SubjectSearch(this);
        }
        return subjectSearch;
    }
    
    public FurnitureSearch getFurnitureSearch(ArrayList furnitureCodesAdded){
        if(furnitureSearch == null){
            furnitureSearch = new FurnitureSearch(this, furnitureCodesAdded);
        }
        return furnitureSearch;
    }
    
    public void setSubjectIndexResultsTable(DefaultTableModel subjectResultDefaultTableModel, boolean searchPressed, String code, String name, String tradename, String address, String city, String telephone, String fiscalNumber){
         try{
            if(!searchPressed){
                subjectResultDefaultTableModel.addColumn("Código");
                subjectResultDefaultTableModel.addColumn("Razón Social");
                subjectResultDefaultTableModel.addColumn("Nombre Comercial");
                subjectResultDefaultTableModel.addColumn("Dirección");
                subjectResultDefaultTableModel.addColumn("Ciudad");
                subjectResultDefaultTableModel.addColumn("Teléfono");
                subjectResultDefaultTableModel.addColumn("CI / RUC");
                subjectResultDefaultTableModel.addColumn("Id");
                subjectResultDefaultTableModel.addColumn("Código");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = subjectResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                subjectResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSubjectSearchResultList(code, name, tradename, address, city, telephone, fiscalNumber);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                String fiscalNumberResult = "";
                String verificationDigitFiscalNumber = "";
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){
                    verificationDigitFiscalNumber = "";
                    row = new Object[subjectResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = new JRadioButton(resultValueMap.get("code").toString());
                    row[1] = resultValueMap.get("name");
                    row[2] = resultValueMap.get("tradename");
                    row[3] = resultValueMap.get("address");
                    row[4] = resultValueMap.get("city");
                    row[5] = resultValueMap.get("telephone");
                    
                    if(resultValueMap.get("fiscalNumber").toString().contains("-")){
                        fiscalNumberResult = resultValueMap.get("fiscalNumber").toString().split("-")[0];
                        verificationDigitFiscalNumber = "-"+resultValueMap.get("fiscalNumber").toString().split("-")[1];
                    }else{
                        fiscalNumberResult = resultValueMap.get("fiscalNumber").toString();
                    }
                    
                    fiscalNumberResult = amountFormat.format(Double.valueOf(fiscalNumberResult));
                    
                    row[6] = fiscalNumberResult+verificationDigitFiscalNumber;
                    
                    row[7] = resultValueMap.get("id");
                    row[8] = resultValueMap.get("code");

                    subjectResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
     }
    
    public ArrayList getSubjectSearchResultList(String code, String name, String tradename, String address, String city, String telephone, String fiscalNumber){
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
            
            if(name==null){
                name="";
            }
            
            if(tradename==null){
                tradename="";
            }
            
            if(address==null){
                address="";
            }
            
            if(city==null){
                city="";
            }
            
            if(telephone==null){
                telephone="";
            }
            
            if(fiscalNumber==null){
                fiscalNumber="";
            }
            
            StringBuilder subjectsQuery = new StringBuilder();
            subjectsQuery.append("SELECT s.id, s.code, s.name, s.tradename, s.address, s.telephone, s.fiscal_number, s.city, s.is_active FROM subject s");   
            subjectsQuery.append(" WHERE s.code ilike ? AND s.name ilike ? AND s.tradename ilike ? AND s.address ilike ? AND s.telephone ilike ? AND s.city ilike ? AND s.fiscal_number ilike ? AND is_active = true");
            subjectsQuery.append(" ORDER BY s.code, s.name, s.is_active");
            ps = connRentFur.prepareStatement(subjectsQuery.toString());
            ps.setString(1, "%"+code+"%");
            ps.setString(2, "%"+name+"%");
            ps.setString(3, "%"+tradename+"%");
            ps.setString(4, "%"+address+"%");
            ps.setString(5, "%"+telephone+"%");
            ps.setString(6, "%"+city+"%");
            ps.setString(7, "%"+fiscalNumber+"%");
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("name", rs.getString("name"));
                resultValuesMap.put("tradename", rs.getString("tradename"));
                resultValuesMap.put("address", rs.getString("address"));
                resultValuesMap.put("telephone", rs.getString("telephone"));
                resultValuesMap.put("fiscalNumber", rs.getString("fiscal_number"));
                resultValuesMap.put("city", rs.getString("city"));
                resultValuesMap.put("isActive", rs.getBoolean("is_active"));
                listToReturn.add(resultValuesMap);
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
    
    public void setClosedSubjectSearch(){
        subjectSearch = null;
    }
    
    public void setClosedFurnitureSearch(){
        furnitureSearch = null;
    }
    
    public void setSubjetcSelectedCode(String subjetcSelectedCode){
        this.subjetcSelectedCode = subjetcSelectedCode;
    }
    
    public String getSubjetcSelectedCode(){
        return this.subjetcSelectedCode;
    }
    
    
    //FURNITURE
    
    public void setFurnitureIndexResultsTable(DefaultTableModel furnituresResultDefaultTableModel, boolean searchPressed, String code, String description, String familyId, ArrayList furnitureCodesAdded){
        
        try{
            if(!searchPressed){
                furnituresResultDefaultTableModel.addColumn("Código");
                furnituresResultDefaultTableModel.addColumn("Descripción");
                furnituresResultDefaultTableModel.addColumn("Familia");
                furnituresResultDefaultTableModel.addColumn("Tasa de Impuesto");
                furnituresResultDefaultTableModel.addColumn("Precio Unitario");
                furnituresResultDefaultTableModel.addColumn("Costo Unitario");
                furnituresResultDefaultTableModel.addColumn("Multa");
                furnituresResultDefaultTableModel.addColumn("Stock total");
                furnituresResultDefaultTableModel.addColumn("Id");
                furnituresResultDefaultTableModel.addColumn("Código");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = furnituresResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                furnituresResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(code, description, familyId, furnitureCodesAdded);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){

                    row = new Object[furnituresResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    
                    row[0] = new JCheckBox(resultValueMap.get("code").toString());
                    row[1] = resultValueMap.get("description");
                    row[2] = resultValueMap.get("family");
                    row[3] = resultValueMap.get("taxRate");
                    row[4] = amountFormat.format((Double)resultValueMap.get("unitPrice"));
                    row[5] = amountFormat.format((Double)resultValueMap.get("unitCostPrice"));
                    row[6] = amountFormat.format((Double)resultValueMap.get("fineAmountPerUnit"));
                    row[7] = amountFormat.format((Double)resultValueMap.get("totalStock"));
                    
                    row[8] = resultValueMap.get("id");
                    row[9] = resultValueMap.get("code");
                    
                    furnituresResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
    }
    
    public ArrayList getSearchResultList(String code, String description, String familyId, ArrayList furnitureCodesAdded){
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
            furnituriesQuery.append("SELECT f.id, f.code, f.tax_rate, f.description, f.unit_price, f.fine_amount_per_unit, f.unit_cost_price, f.total_stock, (SELECT description FROM furniture_family ff WHERE ff.id = f.furniture_family_id) as family, f.active FROM furniture f WHERE f.code ilike ? AND f.description ilike ?");
            if(familyId!= null && !familyId.equals(FurnitureController.ALL_VALUES)){
                furnituriesQuery.append(" AND furniture_family_id = ").append(familyId);
            }
            
            furnituriesQuery.append(" AND f.active = true ");
            furnituriesQuery.append(" AND f.code != ALL (?) ");
            
            furnituriesQuery.append(" ORDER BY f.code, f.description, f.active");
            
            ps = connRentFur.prepareStatement(furnituriesQuery.toString());
            ps.setString(1, "%"+code+"%");
            ps.setString(2, "%"+description+"%");
            ps.setArray(3, connRentFur.createArrayOf("text", furnitureCodesAdded.toArray()));
            
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
                resultValuesMap.put("taxRate", rs.getInt("tax_rate"));
                listToReturn.add(resultValuesMap);
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
    
    public void setFurnitureSelectedCodes(ArrayList furnitureSelectedCodes){
        this.furnitureSelectedCodes = furnitureSelectedCodes;
    }
    
    public ArrayList getFurnitureSelectedCode(){
        return this.furnitureSelectedCodes;
    }
}
