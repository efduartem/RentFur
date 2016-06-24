/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furnitureMovement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import rentfur.furniture.FurnitureController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.User;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class FurnitureMovementController {
    private MainWindowController mainWindowController;
    private FurnitureMovementIndex furnitureMovementIndex;
    private FurnitureMovementInputCreate furnitureMovementInputCreate;
    private FurnitureMovementShow furnitureMovementShow;
    
    public static final int SUCCESFULLY_SAVED = 0;
    public static final int ERROR_IN_SAVED = 1;
    
    public static final int MOVEMENT_TYPE_INPUT = 0;
    public static final int MOVEMENT_TYPE_OUTPUT = 1;
    
    public static final int MOVEMENT_CONCEPT_PURCHASE = 0;
    public static final int MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT = 1;
    public static final int MOVEMENT_CONCEPT_INITIAL_INVENTORY = 2;
    public static final int MOVEMENT_CONCEPT_DAMAGED_PENALTY = 3;
    
    public static final int MOVEMENT_DOCUMENT_TYPE_PURCHASE = 0;
    
    public static final String ALL_VALUES = "Todos";
    
    public FurnitureMovementIndex getFurnitureMovementIndex(MainWindowController mainWindowController, UserRoles userRoles){
        if(furnitureMovementIndex == null){
            furnitureMovementIndex = new FurnitureMovementIndex(this, userRoles);
        }
        this.mainWindowController = mainWindowController;
        return furnitureMovementIndex;
    }
    
    public void indexViewClosed(){
        furnitureMovementIndex = null;
    } 
    
    public FurnitureMovementInputCreate getFurnitureMovementInputCreate(MainWindowController mainWindowController, UserRoles userRoles) {
        if(furnitureMovementInputCreate == null){
            furnitureMovementInputCreate = new FurnitureMovementInputCreate(this);
        }
        this.mainWindowController = mainWindowController;
        return furnitureMovementInputCreate;
    }
    
    public FurnitureMovementShow getFurnitureMovementShow(int movementId) {
        if(furnitureMovementShow == null){
            furnitureMovementShow = new FurnitureMovementShow(this, movementId);
        }
        return furnitureMovementShow;
    }

    public void setFurnitureMovementInputCreateClosed() {
        furnitureMovementInputCreate = null;
    }
    
    public void setFurnitureMovementShowClosed() {
        furnitureMovementShow = null;
    }
    
    public static HashMap getMovementById(int movementId){
        Connection connRentFur = null;
        PreparedStatement psMovement;
        PreparedStatement psMovementDetail = null;
        ResultSet rsMovement;
        ResultSet rsMovementDetail = null;
        ResultSetMetaData rsMovementMd;
        ResultSetMetaData rsMovementDetailMd;
        HashMap <String,Object> movementMap = null;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            //Invoice
            StringBuilder movementSelectSb = new StringBuilder();
            movementSelectSb.append("SELECT id, movement_type, concept, document_number, document_date, movement_date, movement_number, document_type, concept_code, creator_user_id, creation_date FROM movement WHERE id = ?");

            //Invoice Detail
            StringBuilder movementDetailSelectSb = new StringBuilder();
            movementDetailSelectSb.append("SELECT id, furniture_code, furniture_description, quantity_input, quantity_output, stock, amount, movement_id, sub_total FROM movement_detail WHERE  movement_id = ? ORDER BY id ASC");

            
            psMovement = connRentFur.prepareStatement(movementSelectSb.toString());
            psMovement.setInt(1, movementId);
            
            ArrayList movementDetailList;
            HashMap <String,Object> movementDetailMap;
            rsMovement = psMovement.executeQuery();
            rsMovementMd = rsMovement.getMetaData();
            ArrayList<String> columns;
            ArrayList<String> detailColumns;
            columns = new ArrayList<>(rsMovementMd.getColumnCount());
            
            for(int i = 1; i <= rsMovementMd.getColumnCount(); i++){
                columns.add(rsMovementMd.getColumnName(i));
            }
            
            while(rsMovement.next()){                
                movementMap = new HashMap<>(columns.size());
                for(String col : columns) {
                    movementMap.put(col, rsMovement.getObject(col));
                }
                
                psMovementDetail = connRentFur.prepareStatement(movementDetailSelectSb.toString());
                psMovementDetail.setInt(1, movementId);
                movementDetailList = new ArrayList();
                rsMovementDetail = psMovementDetail.executeQuery();
                rsMovementDetailMd = rsMovementDetail.getMetaData();
                detailColumns = new ArrayList<>(rsMovementDetailMd.getColumnCount());
                for(int i = 1; i <= rsMovementDetailMd.getColumnCount(); i++){
                    detailColumns.add(rsMovementDetailMd.getColumnName(i));
                }
                
                while(rsMovementDetail.next()){
                    movementDetailMap = new HashMap<>(detailColumns.size());
                    for(String col : detailColumns) {
                        movementDetailMap.put(col, rsMovementDetail.getObject(col));
                    }
                    movementDetailList.add(movementDetailMap);
                }
                movementMap.put("movementDetail", movementDetailList);
            }
            
            connRentFur.commit();
            
            if(psMovementDetail != null){
                psMovementDetail.close();
            }
            if(rsMovementDetail != null){
                rsMovementDetail.close();
            }
            psMovement.close();
            rsMovement.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FurnitureMovementController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        return movementMap;
    }
    
    public void setFurnitureMovementIndexResultsTable(DefaultTableModel furnitureMovementResultDefaultTableModel, boolean searchPressed, String movementNumber, String movementType, String conceptCode, String movementDate){
         try{
            if(!searchPressed){
                furnitureMovementResultDefaultTableModel.addColumn("Id");
                furnitureMovementResultDefaultTableModel.addColumn("Numero");
                furnitureMovementResultDefaultTableModel.addColumn("Tipo");
                furnitureMovementResultDefaultTableModel.addColumn("Concepto");
                furnitureMovementResultDefaultTableModel.addColumn("Tipo Documento");
                furnitureMovementResultDefaultTableModel.addColumn("Nro. Documento");
                furnitureMovementResultDefaultTableModel.addColumn("");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = furnitureMovementResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                furnitureMovementResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(movementNumber, movementType, conceptCode, movementDate);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){
                    row = new Object[furnitureMovementResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("movementNumber");
                    row[2] = getFurnitureMovementType(Integer.valueOf(resultValueMap.get("movementType").toString()));
                    row[3] = resultValueMap.get("concept").toString();
                    row[4] = resultValueMap.get("documentType");
                    row[5] = resultValueMap.get("documentNumber");
                    
//                    if(resultValueMap.get("fiscalNumber").toString().contains("-")){
//                        fiscalNumberResult = resultValueMap.get("fiscalNumber").toString().split("-")[0];
//                        verificationDigitFiscalNumber = "-"+resultValueMap.get("fiscalNumber").toString().split("-")[1];
//                    }else{
//                        fiscalNumberResult = resultValueMap.get("fiscalNumber").toString();
//                    }
//                    
//                    fiscalNumberResult = amountFormat.format(Double.valueOf(fiscalNumberResult));
//                    
//                    row[7] = fiscalNumberResult+verificationDigitFiscalNumber;
//                    
//                    if((Boolean)resultValueMap.get("isActive")){
//                        row[8] = "Si";
//                    }else{
//                        row[8] = "No";
//                    }
//                    
//                    if((Boolean)resultValueMap.get("isActive")){
//                        row[9] = "Inactivar";
//                    }else{
//                        row[9] = "Activar";
//                    }
                    
                    row[6] = "Ver";
//                    row[11] = resultValueMap.get("isActive");

                    furnitureMovementResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
     }
    
    private ArrayList getSearchResultList(String movementNumber, String movementType, String conceptCode, String movementDate){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList listToReturn = new ArrayList();
        
        try{
            HashMap resultValuesMap;
            connRentFur = DbConnectUtil.getConnection();
            
            if(movementNumber==null){
                movementNumber="";
            }
            
            StringBuilder furnitureMovementsQuery = new StringBuilder();
            furnitureMovementsQuery.append("SELECT id, movement_type, concept, document_number, document_date, movement_date, movement_number, document_type, concept_code FROM movement");   
            furnitureMovementsQuery.append(" WHERE movement_number ilike ?");
            
            if(movementType!= null && !movementType.equals(ALL_VALUES)){
                furnitureMovementsQuery.append(" AND movement_type = ").append(movementType);
            }
            
            if(conceptCode!= null && !conceptCode.equals(ALL_VALUES)){
                furnitureMovementsQuery.append(" AND concept_code = ").append(conceptCode);
            }
            
            if(movementDate!= null && !movementDate.equals("")){
                furnitureMovementsQuery.append(" AND movement_date = '").append(movementDate).append("'");
            }
            
            furnitureMovementsQuery.append(" ORDER BY movement_date");
            ps = connRentFur.prepareStatement(furnitureMovementsQuery.toString());
            ps.setString(1, "%"+movementNumber+"%");
            
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("movementType", rs.getInt("movement_type"));
                resultValuesMap.put("concept", rs.getString("concept"));
                resultValuesMap.put("documentNumber", rs.getString("document_number"));
                resultValuesMap.put("documentDate", rs.getTimestamp("document_date"));
                resultValuesMap.put("movementDate", rs.getTimestamp("movement_date"));
                resultValuesMap.put("movementNumber", rs.getString("movement_number"));
                resultValuesMap.put("documentType", rs.getInt("document_type"));
                resultValuesMap.put("conceptCode", rs.getInt("concept_code"));
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
    
    public HashMap inputMovementRecord(ArrayList movementDetailList, int conceptCode, Date movementDate){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psTotalStock;
        ResultSet rs;
        int movementId = 0;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            StringBuilder furnitureTotalStockUpdateSb = new StringBuilder();
            furnitureTotalStockUpdateSb.append("UPDATE furniture SET total_stock = ? WHERE code = ?");

            psTotalStock = connRentFur.prepareStatement(furnitureTotalStockUpdateSb.toString());
            
            StringBuilder movementInsertSb = new StringBuilder();
            movementInsertSb.append("INSERT INTO movement(id, movement_type, concept, movement_date, movement_number, concept_code, creation_date, creator_user_id) VALUES (nextval('movement_seq'), ?, ?, ?, LPAD(nextval('movement_number_seq')::text, 6, '0'), ?, current_timestamp, ?)");

            ps = connRentFur.prepareStatement(movementInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, MOVEMENT_TYPE_INPUT); 
            ps.setString(2, getFurnitureMovementConcept(conceptCode));
            ps.setDate(3, new java.sql.Date(movementDate.getTime()));
            ps.setInt(4, conceptCode);
            ps.setInt(5, loggedUser.getId());
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            while(rs != null && rs.next()){
                movementId = rs.getInt(1);
            }
            
            StringBuilder movementDetailInsertSb = new StringBuilder();
            HashMap movementDetailMap;
            HashMap furnitureMap;
            int totalStock;
            movementDetailInsertSb.append("INSERT INTO movement_detail(id, furniture_code, furniture_description, quantity_input, quantity_output, stock, amount, movement_id, sub_total) VALUES (nextval('movement_detail_seq'), ?, ?, ?, 0, ?, ?, ?, ?)");
            ps = connRentFur.prepareStatement(movementDetailInsertSb.toString());
            
            for(int i = 0 ; i < movementDetailList.size(); i++){
                movementDetailMap = (HashMap) movementDetailList.get(i);
                furnitureMap = FurnitureController.getFurnitureByCode(movementDetailMap.get("code").toString());
                totalStock = (Integer) furnitureMap.get("totalStock") + Integer.valueOf(movementDetailMap.get("quantity").toString());
                ps.setString(1, movementDetailMap.get("code").toString());
                ps.setString(2, movementDetailMap.get("description").toString());
                ps.setInt(3, Integer.valueOf(movementDetailMap.get("quantity").toString()));
                ps.setInt(4, totalStock);
                ps.setDouble(5, Double.valueOf(movementDetailMap.get("cost").toString()));
                ps.setInt(6, movementId);
                ps.setDouble(7, Double.valueOf(movementDetailMap.get("import").toString()));
                ps.addBatch();
                
                psTotalStock.setInt(1, totalStock); 
                psTotalStock.setString(2, movementDetailMap.get("code").toString()); 
                psTotalStock.addBatch();
                
                FurnitureController.updateFurnitureStockDaysInput(Integer.valueOf(movementDetailMap.get("furnitureId").toString()), movementDetailMap.get("code").toString(), Integer.valueOf(movementDetailMap.get("quantity").toString()), totalStock);
                
            }
            ps.executeBatch();
            ps.clearBatch();
            
            psTotalStock.executeBatch();
            psTotalStock.clearBatch();
            
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            if(psTotalStock!=null){
                psTotalStock.close();
            }
            
            
            mapToReturn.put("id", movementId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Movimiento registrado correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FurnitureMovementController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public static HashMap outputMovementRecord(ArrayList movementDetailList, int conceptCode, Date movementDate){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        int movementId = 0;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");

            StringBuilder movementInsertSb = new StringBuilder();
            movementInsertSb.append("INSERT INTO movement(id, movement_type, concept, movement_date, movement_number, concept_code, creation_date, creator_user_id) VALUES (nextval('movement_seq'), ?, ?, ?, LPAD(nextval('movement_number_seq')::text, 6, '0'), ?, current_timestamp, ?)");

            ps = connRentFur.prepareStatement(movementInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, MOVEMENT_TYPE_OUTPUT); 
            ps.setString(2, getFurnitureMovementConcept(conceptCode));
            ps.setDate(3, new java.sql.Date(movementDate.getTime()));
            ps.setInt(4, conceptCode);
            ps.setInt(5, loggedUser.getId());
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            while(rs != null && rs.next()){
                movementId = rs.getInt(1);
            }
            
            StringBuilder movementDetailInsertSb = new StringBuilder();
            HashMap movementDetailMap;
            HashMap furnitureMap;
            movementDetailInsertSb.append("INSERT INTO movement_detail(id, furniture_code, furniture_description, quantity_input, quantity_output, stock, amount, movement_id, sub_total) VALUES (nextval('movement_detail_seq'), ?, ?, 0, ?, ?, ?, ?, ?)");
            ps = connRentFur.prepareStatement(movementDetailInsertSb.toString());
            
            for(int i = 0 ; i < movementDetailList.size(); i++){
                movementDetailMap = (HashMap) movementDetailList.get(i);
                furnitureMap = FurnitureController.getFurnitureByCode(movementDetailMap.get("code").toString());
                ps.setString(1, movementDetailMap.get("code").toString());
                ps.setString(2, movementDetailMap.get("description").toString());
                ps.setInt(3, Integer.valueOf(movementDetailMap.get("quantity").toString()));
                ps.setInt(4, (Integer) furnitureMap.get("totalStock"));
                ps.setDouble(5, Double.valueOf(movementDetailMap.get("cost").toString()));
                ps.setInt(6, movementId);
                ps.setDouble(7, Double.valueOf(movementDetailMap.get("import").toString()));
                ps.addBatch();
            }
            ps.executeBatch();
            ps.clearBatch();

            
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            
            mapToReturn.put("id", movementId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Movimiento registrado correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FurnitureMovementController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public static void inputMovementRecordFromPurchase(Connection connRentFur, HashMap purchaseInvoiceMap, ArrayList movementDetailList, User loggedUser){
        
       String documentNumber = purchaseInvoiceMap.get("invoiceBranch").toString()+"-"+purchaseInvoiceMap.get("invoicePrinter").toString()+"-"+purchaseInvoiceMap.get("invoiceNumber").toString();
       Timestamp invoicePurchaseDate = new java.sql.Timestamp(((Date) purchaseInvoiceMap.get("invoicingDate")).getTime());       
       PreparedStatement movementInsertPs;
       PreparedStatement movementDetailInsertPs;
       PreparedStatement movementPs;
       ResultSet movementStockRs;
       ResultSet movementRs;
       long movementId = 0;
       
       StringBuilder movementInsertSb = new StringBuilder();
       movementInsertSb.append("INSERT INTO movement(id, movement_number, movement_type, concept_code, concept, document_number, document_type, document_date, movement_date, creator_user_id, creation_date)");
       movementInsertSb.append(" VALUES ((select nextval('movement_seq')), LPAD(nextval('movement_number_seq')::text, 6, '0'), ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp);");
    
       StringBuilder movementDetailInsertSb = new StringBuilder();
       movementDetailInsertSb.append("INSERT INTO movement_detail(id, furniture_code, furniture_description, quantity_input, quantity_output, stock, amount, movement_id, sub_total)");
       movementDetailInsertSb.append("VALUES ((select nextval('movement_detail_seq')), ?, ?, ?, ?, ?, ?, ?, ?)");
    
       StringBuilder movementSb = new StringBuilder();
       movementSb.append("SELECT stock FROM movement_detail WHERE furniture_code = ? ORDER BY id DESC LIMIT 1");
       try{
           movementInsertPs = connRentFur.prepareStatement(movementInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
           movementInsertPs.setInt(1, FurnitureMovementController.MOVEMENT_TYPE_INPUT);
           movementInsertPs.setInt(2, FurnitureMovementController.MOVEMENT_CONCEPT_PURCHASE);
           movementInsertPs.setString(3, getFurnitureMovementConcept(FurnitureMovementController.MOVEMENT_CONCEPT_PURCHASE));
           movementInsertPs.setString(4, documentNumber);
           movementInsertPs.setInt(5, FurnitureMovementController.MOVEMENT_DOCUMENT_TYPE_PURCHASE);
           movementInsertPs.setTimestamp(6, invoicePurchaseDate);
           movementInsertPs.setInt(7, loggedUser.getId());
           
           movementInsertPs.executeUpdate();
           
           movementDetailInsertPs = connRentFur.prepareStatement(movementDetailInsertSb.toString());
           
           movementRs = movementInsertPs.getGeneratedKeys();
            while(movementRs != null && movementRs.next()){
                movementId = movementRs.getInt(1);
            }

            movementPs = connRentFur.prepareStatement(movementSb.toString());
           for(int i=0; i<movementDetailList.size(); i++){
                HashMap invoiceDetailMap = (HashMap) movementDetailList.get(i);
                int quantityInput = Integer.valueOf(invoiceDetailMap.get("quantity").toString());
                movementPs.setString(1, (String) invoiceDetailMap.get("code"));
                movementStockRs = movementPs.executeQuery();
            
                movementDetailInsertPs.setString(1, (String) invoiceDetailMap.get("code"));
                movementDetailInsertPs.setString(2, (String) invoiceDetailMap.get("description"));
                movementDetailInsertPs.setInt(3, quantityInput);
                movementDetailInsertPs.setInt(4, 0);
                if(movementStockRs.next()){
                    movementDetailInsertPs.setInt(5, movementStockRs.getInt("stock") + quantityInput);
                }else{
                    movementDetailInsertPs.setInt(5, quantityInput);
                }
                
                movementDetailInsertPs.setDouble(6, Double.valueOf(invoiceDetailMap.get("cost").toString()));
                movementDetailInsertPs.setLong(7, movementId);
                movementDetailInsertPs.setDouble(8, Double.valueOf(invoiceDetailMap.get("import").toString()));
                movementDetailInsertPs.executeUpdate();
           }
           
       }catch(NumberFormatException | SQLException th){
           System.err.println(th.getMessage());
           System.err.println(th);
       }finally{}
       
    }
    
    public static ComboBoxItem[] getMovementTypesForComboBox(boolean addAllOption){
        ComboBoxItem[] movementTypes = null;
        
        try{
            
            if(addAllOption){
                movementTypes = new ComboBoxItem[3];
                movementTypes[0] =  new ComboBoxItem();
                movementTypes[0].setKey(ALL_VALUES);
                movementTypes[0].setValue(ALL_VALUES);
                
                movementTypes[1] =  new ComboBoxItem();
                movementTypes[1].setKey(String.valueOf(MOVEMENT_TYPE_INPUT));
                movementTypes[1].setValue(getFurnitureMovementType(MOVEMENT_TYPE_INPUT));
                
                movementTypes[2] =  new ComboBoxItem();
                movementTypes[2].setKey(String.valueOf(MOVEMENT_TYPE_OUTPUT));
                movementTypes[2].setValue(getFurnitureMovementType(MOVEMENT_TYPE_OUTPUT));
                
            }else{
                movementTypes = new ComboBoxItem[2];
                
                movementTypes[0] =  new ComboBoxItem();
                movementTypes[0].setKey(String.valueOf(MOVEMENT_TYPE_INPUT));
                movementTypes[0].setValue(getFurnitureMovementType(MOVEMENT_TYPE_INPUT));
                
                movementTypes[1] =  new ComboBoxItem();
                movementTypes[1].setKey(String.valueOf(MOVEMENT_TYPE_OUTPUT));
                movementTypes[1].setValue(getFurnitureMovementType(MOVEMENT_TYPE_OUTPUT));
                
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return movementTypes;
    }
    
    public static ComboBoxItem[] getConceptCodesForComboBox(boolean addAllOption){
        ComboBoxItem[] movementTypes = null;
        
        
        try{
            
            if(addAllOption){
                movementTypes = new ComboBoxItem[5];
                movementTypes[0] =  new ComboBoxItem();
                movementTypes[0].setKey(ALL_VALUES);
                movementTypes[0].setValue(ALL_VALUES);
                
                movementTypes[1] =  new ComboBoxItem();
                movementTypes[1].setKey(String.valueOf(MOVEMENT_CONCEPT_PURCHASE));
                movementTypes[1].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_PURCHASE));
                
                movementTypes[2] =  new ComboBoxItem();
                movementTypes[2].setKey(String.valueOf(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                movementTypes[2].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                
                movementTypes[3] =  new ComboBoxItem();
                movementTypes[3].setKey(String.valueOf(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                movementTypes[3].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                
                movementTypes[4] =  new ComboBoxItem();
                movementTypes[4].setKey(String.valueOf(MOVEMENT_CONCEPT_DAMAGED_PENALTY));
                movementTypes[4].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_DAMAGED_PENALTY));
                
            }else{
                movementTypes = new ComboBoxItem[4];
                
                movementTypes[0] =  new ComboBoxItem();
                movementTypes[0].setKey(String.valueOf(MOVEMENT_CONCEPT_PURCHASE));
                movementTypes[0].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_PURCHASE));
                
                movementTypes[1] =  new ComboBoxItem();
                movementTypes[1].setKey(String.valueOf(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                movementTypes[1].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                
                movementTypes[2] =  new ComboBoxItem();
                movementTypes[2].setKey(String.valueOf(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                movementTypes[2].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                
                movementTypes[3] =  new ComboBoxItem();
                movementTypes[3].setKey(String.valueOf(MOVEMENT_CONCEPT_DAMAGED_PENALTY));
                movementTypes[3].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_DAMAGED_PENALTY));
                
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return movementTypes;
    }
    
    public static ComboBoxItem[] getInputConceptCodesForComboBox(boolean addAllOption){
        ComboBoxItem[] inputConceptCodes = null;
        
        try{
            
            if(addAllOption){
                inputConceptCodes = new ComboBoxItem[3];
                inputConceptCodes[0] =  new ComboBoxItem();
                inputConceptCodes[0].setKey(ALL_VALUES);
                inputConceptCodes[0].setValue(ALL_VALUES);
                
                inputConceptCodes[1] =  new ComboBoxItem();
                inputConceptCodes[1].setKey(String.valueOf(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                inputConceptCodes[1].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                
                inputConceptCodes[2] =  new ComboBoxItem();
                inputConceptCodes[2].setKey(String.valueOf(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                inputConceptCodes[2].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                
            }else{
                inputConceptCodes = new ComboBoxItem[2];
                
                inputConceptCodes[0] =  new ComboBoxItem();
                inputConceptCodes[0].setKey(String.valueOf(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                inputConceptCodes[0].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT));
                
                inputConceptCodes[1] =  new ComboBoxItem();
                inputConceptCodes[1].setKey(String.valueOf(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                inputConceptCodes[1].setValue(getFurnitureMovementConcept(MOVEMENT_CONCEPT_INITIAL_INVENTORY));
                
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return inputConceptCodes;
    }
    
    public static String getFurnitureMovementType(int movementType){
        String movementTypeString = "";
        switch(movementType){
            case MOVEMENT_TYPE_INPUT: 
                movementTypeString = "ENTRADA";
                break;
            case MOVEMENT_TYPE_OUTPUT: 
                movementTypeString = "SALIDA";
                break;
        }
        
        return movementTypeString;
    }
    
    public static String getFurnitureMovementConcept(int conceptCode){
        String conceptCodeString = "";
        switch(conceptCode){
            case MOVEMENT_CONCEPT_PURCHASE: 
                conceptCodeString = "COMPRA";
                break;
            case MOVEMENT_CONCEPT_INVENTORY_ADJUSTMENT_INPUT: 
                conceptCodeString = "ENTRADA POR AJUSTE A INVENTARIO";
                break;
            case MOVEMENT_CONCEPT_INITIAL_INVENTORY: 
                conceptCodeString = "INVENTARIO INICIAL";
                break;
            case MOVEMENT_CONCEPT_DAMAGED_PENALTY: 
                conceptCodeString = "AVERÍA - MULTA";
                break;
        }
        
        return conceptCodeString;
    }
    
    public static String getFurnitureMovementDocumentType(int documentType){
        String documentTypeString = "";
        
        switch(documentType){
            case MOVEMENT_DOCUMENT_TYPE_PURCHASE: 
                documentTypeString = "FACTURA DE COMPRA";
                break;
        }
        
        return documentTypeString;
    }
}
