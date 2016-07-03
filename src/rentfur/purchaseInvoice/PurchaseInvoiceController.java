/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.purchaseInvoice;

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
import rentfur.furnitureMovement.FurnitureMovementController;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.User;
import rentfur.util.UserRoles;


/**
 *
 * @author hp
 */
public class PurchaseInvoiceController {
    private MainWindowController mainWindowController;
    private PurchaseInvoiceCreate purchaseInvoiceCreate;
    private PurchaseInvoiceShow purchaseInvoiceShow;
    private PurchaseInvoiceIndex purchaseInvoiceIndex;
    private UserRoles userRoles;
    public static final int ERROR_IN_SAVED = 1;
    public static final int SUCCESFULLY_SAVED = 0;
    
    public static final int MOVEMENT_TYPE_OUTPUT = 1;
    //public static final int MOVEMENT_CONCEPT_CODE_COMPRAS = 1;

    
    public PurchaseInvoiceCreate getPurchaseInvoiceCreate(){
        if(purchaseInvoiceCreate == null){
            purchaseInvoiceCreate = new PurchaseInvoiceCreate(this);
        }
        return purchaseInvoiceCreate;
    }
    
    public PurchaseInvoiceShow getPurchaseInvoiceShow(int purchaseInvoiceId){
        if(purchaseInvoiceShow == null){
            purchaseInvoiceShow = new PurchaseInvoiceShow(this, purchaseInvoiceId);
        }
        return purchaseInvoiceShow;
    }
    
    public void setPurchaseInvoiceCreateClosed(){
        purchaseInvoiceCreate = null;
    }
    
    public void setPurchaseInvoiceIndexClosed(){
        purchaseInvoiceIndex = null;
    }
    
    public void purchaseInvoiceShowClosed(){
        purchaseInvoiceShow = null;
    }
    
    public PurchaseInvoiceIndex getPurchaseInvoiceIndex(MainWindowController mainWindowController, UserRoles userRoles){
        if(purchaseInvoiceIndex == null){
            purchaseInvoiceIndex = new PurchaseInvoiceIndex(this, userRoles);
        }
        this.mainWindowController = mainWindowController;
        
        return purchaseInvoiceIndex;
    }
    
    public HashMap savePurchaseInvoice(HashMap purchaseInvoiceMap, ArrayList purchaseInvoiceDetailMapArrayList){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement invoiceInsertPs;
        PreparedStatement invoiceDetailInsertPs;
        PreparedStatement furnitureUpdatePs;
        PreparedStatement furnitureStockUpdatePs;
        ResultSet invoiceIdRs;
        
        StringBuilder invoiceSb = new StringBuilder();
        invoiceSb.append("INSERT INTO purchase_invoice(id, fiscal_stamp_number, invoice_branch, invoice_printer, invoice_number,");
        invoiceSb.append("invoicing_date, provider_code, provider_name, provider_fiscal_number, provider_address, exempt_total, total_tax_5, total_tax_10, ");
        invoiceSb.append("total_tax, total_taxable_5, total_taxable_10, total_taxable, balance, net_total, observation, creation_date, creator_user_id, ");
        invoiceSb.append("last_modification_user_id, last_modification_date, provider_tradename)");
        invoiceSb.append(" VALUES ((select nextval('purchase_invoice_seq')), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
        invoiceSb.append("?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?, current_timestamp, ?)");
        
        StringBuilder invoiceDetailSb = new StringBuilder();
        invoiceDetailSb.append("INSERT INTO purchase_invoice_detail(id, code, description, quantity, unit_price, tax_rate, total_amount, tax_amount_5, ");
        invoiceDetailSb.append("tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_total, purchase_invoice_id)");
        invoiceDetailSb.append("VALUES ((select nextval('purchase_invoice_detail_seq')), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);");
        
        StringBuilder furnitureUpdateSb = new StringBuilder();
        furnitureUpdateSb.append("UPDATE furniture SET total_stock = total_stock + ? WHERE id = ?");
        
        StringBuilder furnitureStockUpdateSb = new StringBuilder();
        furnitureStockUpdateSb.append("UPDATE furniture_stock SET stock_total = stock_total + ?, stock_available = stock_available + ? WHERE furniture_id = ? AND day >= ?");
        
        try {
            
            java.sql.Date invoicingDate = new java.sql.Date(((Date) purchaseInvoiceMap.get("invoicingDate")).getTime());
            Timestamp invoicingDateTs = new java.sql.Timestamp(((Date) purchaseInvoiceMap.get("invoicingDate")).getTime());
            
            userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            long userLoggedId = loggedUser.getId();
            long invoiceId = 0;
            
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            invoiceInsertPs = connRentFur.prepareStatement(invoiceSb.toString(), Statement.RETURN_GENERATED_KEYS);
            invoiceDetailInsertPs = connRentFur.prepareStatement(invoiceDetailSb.toString());
            furnitureUpdatePs = connRentFur.prepareStatement(furnitureUpdateSb.toString());
            furnitureStockUpdatePs = connRentFur.prepareStatement(furnitureStockUpdateSb.toString());
                    
            invoiceInsertPs.setString(1, purchaseInvoiceMap.get("invoiceFiscalStampNumber").toString());
            invoiceInsertPs.setString(2, purchaseInvoiceMap.get("invoiceBranch").toString());
            invoiceInsertPs.setString(3, purchaseInvoiceMap.get("invoicePrinter").toString());
            invoiceInsertPs.setString(4, purchaseInvoiceMap.get("invoiceNumber").toString());
            invoiceInsertPs.setTimestamp(5, invoicingDateTs);
            invoiceInsertPs.setString(6, purchaseInvoiceMap.get("providerCode").toString());
            invoiceInsertPs.setString(7, purchaseInvoiceMap.get("providerName").toString());
            invoiceInsertPs.setString(8, purchaseInvoiceMap.get("providerFiscalNumber").toString());
            invoiceInsertPs.setString(9, purchaseInvoiceMap.get("providerAddress").toString());
            invoiceInsertPs.setDouble(10, Double.valueOf(purchaseInvoiceMap.get("exemptTotal").toString()));
            invoiceInsertPs.setDouble(11, Double.valueOf(purchaseInvoiceMap.get("totalTax5").toString()));
            invoiceInsertPs.setDouble(12, Double.valueOf(purchaseInvoiceMap.get("totalTax10").toString()));
            invoiceInsertPs.setDouble(13, Double.valueOf(purchaseInvoiceMap.get("taxRateTotal").toString()));
            invoiceInsertPs.setDouble(14, Double.valueOf(purchaseInvoiceMap.get("totalTaxable5").toString()));
            invoiceInsertPs.setDouble(15, Double.valueOf(purchaseInvoiceMap.get("totalTaxable10").toString()));
            invoiceInsertPs.setDouble(16, Double.valueOf(purchaseInvoiceMap.get("totalTaxable").toString()));
            invoiceInsertPs.setDouble(17, Double.valueOf(purchaseInvoiceMap.get("balance").toString()));
            invoiceInsertPs.setDouble(18, Double.valueOf(purchaseInvoiceMap.get("netTotal").toString()));
            invoiceInsertPs.setString(19, "");
            invoiceInsertPs.setLong(20, userLoggedId);
            invoiceInsertPs.setLong(21, userLoggedId);
            invoiceInsertPs.setString(22, purchaseInvoiceMap.get("providerTradename").toString());
            invoiceInsertPs.executeUpdate();
            
            invoiceIdRs = invoiceInsertPs.getGeneratedKeys();
            while(invoiceIdRs != null && invoiceIdRs.next()){
                invoiceId = invoiceIdRs.getInt(1);
            }
            
            HashMap movementDetailMap;
            ArrayList movementDetailList = new ArrayList();
            double inputMovementImport = 0;
            for(int i=0; i<purchaseInvoiceDetailMapArrayList.size(); i++){
                HashMap invoiceDetailMap = (HashMap) purchaseInvoiceDetailMapArrayList.get(i);
                int quantityFurniture = Integer.valueOf(invoiceDetailMap.get("quantity").toString());
                
                invoiceDetailInsertPs.setString(1, (String) invoiceDetailMap.get("furnitureCode"));
                invoiceDetailInsertPs.setString(2, (String) invoiceDetailMap.get("furnitureDescription"));
                invoiceDetailInsertPs.setInt(3, quantityFurniture);
                invoiceDetailInsertPs.setDouble(4, Double.valueOf(invoiceDetailMap.get("unitPrice").toString()));
                invoiceDetailInsertPs.setDouble(5, Double.valueOf(invoiceDetailMap.get("taxRate").toString()));
                invoiceDetailInsertPs.setDouble(6, Double.valueOf(invoiceDetailMap.get("subTotal").toString()));
                invoiceDetailInsertPs.setDouble(7, Double.valueOf(invoiceDetailMap.get("taxRate5").toString()));
                invoiceDetailInsertPs.setDouble(8, Double.valueOf(invoiceDetailMap.get("taxRate10").toString()));
                invoiceDetailInsertPs.setDouble(9, Double.valueOf(invoiceDetailMap.get("taxAmount").toString()));
                invoiceDetailInsertPs.setDouble(10,Double.valueOf(invoiceDetailMap.get("taxableRate5").toString()));
                invoiceDetailInsertPs.setDouble(11,Double.valueOf(invoiceDetailMap.get("taxableRate10").toString()));
                invoiceDetailInsertPs.setDouble(12,Double.valueOf(invoiceDetailMap.get("taxableAmount").toString()));
                invoiceDetailInsertPs.setLong(13, invoiceId);
                invoiceDetailInsertPs.executeUpdate();
                
                if(invoiceDetailMap.get("furnitureCode") != null){
                    furnitureUpdatePs.setInt(1, quantityFurniture);
                    furnitureUpdatePs.setLong(2, Long.valueOf(invoiceDetailMap.get("furnitureId").toString()));
                    furnitureUpdatePs.executeUpdate();
                    
                    furnitureStockUpdatePs.setInt(1, quantityFurniture);
                    furnitureStockUpdatePs.setInt(2, quantityFurniture);
                    furnitureStockUpdatePs.setLong(3, Long.valueOf(invoiceDetailMap.get("furnitureId").toString()));
                    furnitureStockUpdatePs.setDate(4, invoicingDate);
                    furnitureStockUpdatePs.executeUpdate();
                    
                    movementDetailMap = new HashMap();
                    movementDetailMap.put("furnitureId", Integer.valueOf(invoiceDetailMap.get("furnitureId").toString()));
                    movementDetailMap.put("code", invoiceDetailMap.get("furnitureCode").toString());
                    movementDetailMap.put("description", invoiceDetailMap.get("furnitureDescription").toString());
                    movementDetailMap.put("quantity", Integer.valueOf(invoiceDetailMap.get("quantity").toString()));
                    movementDetailMap.put("cost", Double.valueOf(invoiceDetailMap.get("unitPrice").toString()));
                    inputMovementImport = Double.valueOf(invoiceDetailMap.get("unitPrice").toString()) * Double.valueOf(invoiceDetailMap.get("quantity").toString());
                    movementDetailMap.put("import", inputMovementImport);
                    movementDetailList.add(movementDetailMap);
                    //insertar movimiento de la compra
                    //movementInsert(connRentFur,purchaseInvoiceMap,purchaseInvoiceDetailMapArrayList, loggedUser);
                }                
            }
            
            //Movimiento de alta de mobiliarios
            FurnitureMovementController.inputMovementRecordFromPurchase(connRentFur,purchaseInvoiceMap, movementDetailList, loggedUser);
            
            connRentFur.commit();
            mapToReturn.put("id", invoiceId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Factura registrada correctamente");
            
        } catch (NumberFormatException | SQLException th) {
            System.err.println(th.getMessage());
            System.err.println(th);
            
            System.err.println("Error: "+th.getMessage());
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
    
    public void setPurchaseInvoicesIndexResultsTable(DefaultTableModel purchaseInvoiceResultDefaultTableModel, boolean searchPressed, String invoiceDate, String providerCode, String providerName, String providerTradename, String invoiceNumber, String fiscalNumber){
         try{
            if(!searchPressed){
                purchaseInvoiceResultDefaultTableModel.addColumn("Id");
                purchaseInvoiceResultDefaultTableModel.addColumn("Cod. Proveedor");
                purchaseInvoiceResultDefaultTableModel.addColumn("Nombre");
                purchaseInvoiceResultDefaultTableModel.addColumn("Nombre Comercial");
                purchaseInvoiceResultDefaultTableModel.addColumn("RUC / CI");
                purchaseInvoiceResultDefaultTableModel.addColumn("Numero");
                purchaseInvoiceResultDefaultTableModel.addColumn("Monto");
                purchaseInvoiceResultDefaultTableModel.addColumn("Fecha");
                purchaseInvoiceResultDefaultTableModel.addColumn("");
            }

            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = purchaseInvoiceResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                purchaseInvoiceResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(invoiceDate, providerCode, providerName, providerTradename, invoiceNumber, fiscalNumber);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){
                    row = new Object[purchaseInvoiceResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("provider_code").toString();
                    row[2] = resultValueMap.get("provider_name").toString();
                    row[3] = resultValueMap.get("provider_tradename").toString();
                    row[4] = resultValueMap.get("provider_fiscal_number").toString();
                    row[5] = resultValueMap.get("invoice_num");
                    row[6] = amountFormat.format((Double)resultValueMap.get("net_total"));
                    row[7] = resultValueMap.get("invoiceDate");
                    row[8] = "Ver";

                    purchaseInvoiceResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
     }
    
    private ArrayList getSearchResultList(String invoiceDate, String providerCode, String providerName, String providerTradename, String invoiceNumber, String fiscalNumber){
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        ArrayList listToReturn = new ArrayList();
        
        try{
            HashMap resultValuesMap;
            connRentFur = DbConnectUtil.getConnection();
            
            if(invoiceDate==null){
                invoiceDate="";
            }
            
            if(providerCode==null){
                providerCode="";
            }
            
            if(providerName==null){
                providerName="";
            }
            
            if(providerTradename==null){
                providerTradename="";
            }
            
            if(invoiceNumber==null){
                invoiceNumber="";
            }
            
            if(fiscalNumber==null){
                fiscalNumber="";
            }
            
            StringBuilder purchaseInvoicesQuery = new StringBuilder();
            purchaseInvoicesQuery.append("SELECT id, fiscal_stamp_number, invoice_branch, invoice_printer, invoice_number, provider_code, provider_name, provider_address, provider_fiscal_number, exempt_total, total_tax_5, total_tax_10, total_tax, total_taxable_5, total_taxable_10, total_taxable, balance, net_total, observation, creator_user_id, last_modification_user_id, invoicing_date, creation_date, last_modification_date, (invoice_branch || '-' || invoice_printer || '-' || invoice_number) as invoice_num , provider_tradename, to_char(invoicing_date, 'YYYY-MM-DD') as invoiceDate FROM purchase_invoice WHERE provider_code ilike ? AND provider_name ilike ? AND (invoice_branch || '-' || invoice_printer || '-' || invoice_number) like ? AND provider_fiscal_number like ? AND provider_tradename ilike ?");
            
            if(!invoiceDate.equals("")){
                purchaseInvoicesQuery.append(" AND cast(invoicing_date as date) = '").append(invoiceDate).append("'");
            }
            
            purchaseInvoicesQuery.append(" ORDER BY invoicing_date");
            ps = connRentFur.prepareStatement(purchaseInvoicesQuery.toString());
            ps.setString(1, "%"+providerCode+"%");
            ps.setString(2, "%"+providerName+"%");
            ps.setString(3, "%"+invoiceNumber+"%");
            ps.setString(4, "%"+fiscalNumber+"%");
            ps.setString(5, "%"+providerTradename+"%");
            //System.out.println("ps: "+ps.toString());
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("provider_code", rs.getInt("provider_code"));
                resultValuesMap.put("provider_name", rs.getString("provider_name"));
                resultValuesMap.put("provider_tradename", rs.getString("provider_tradename"));
                resultValuesMap.put("provider_fiscal_number", rs.getString("provider_fiscal_number"));
                resultValuesMap.put("invoice_num", rs.getString("invoice_num"));
                resultValuesMap.put("net_total", rs.getDouble("net_total"));
                resultValuesMap.put("invoicing_date", rs.getTimestamp("invoicing_date"));
                resultValuesMap.put("invoiceDate", rs.getString("invoiceDate"));
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
    
    public static HashMap getPurchaseInvoiceById(int invoiceId){
        Connection connRentFur = null;
        PreparedStatement psInvoice;
        PreparedStatement psInvoiceDetail = null;
        ResultSet rsInvoice;
        ResultSet rsInvoiceDetail = null;
        ResultSetMetaData rsInvoiceMd;
        ResultSetMetaData rsInvoiceDetailMd;
        HashMap <String,Object> invoiceMap = null;
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            //Invoice
            StringBuilder invoiceSelectSb = new StringBuilder();
            invoiceSelectSb.append("SELECT id, fiscal_stamp_number, invoice_branch, invoice_printer, invoice_number, provider_code, provider_name, provider_address, provider_fiscal_number, exempt_total, total_tax_5, total_tax_10, total_tax, total_taxable_5, total_taxable_10, total_taxable, balance, net_total, observation, creator_user_id, last_modification_user_id, invoicing_date, creation_date, last_modification_date, provider_tradename FROM purchase_invoice WHERE id = ?");

            //Invoice Detail
            StringBuilder invoiceDetailSelectSb = new StringBuilder();
            invoiceDetailSelectSb.append("SELECT code, description, quantity, unit_price, total_amount, tax_amount_5, tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_total, id, tax_rate, purchase_invoice_id FROM purchase_invoice_detail WHERE  purchase_invoice_id = ? ORDER BY id ASC");

            
            psInvoice = connRentFur.prepareStatement(invoiceSelectSb.toString());
            psInvoice.setInt(1, invoiceId);
            ArrayList invoiceDetailList;
            HashMap <String,Object> invoiceDetailMap;
            rsInvoice = psInvoice.executeQuery();
            rsInvoiceMd = rsInvoice.getMetaData();
            ArrayList<String> columns;
            ArrayList<String> detailColumns;
            columns = new ArrayList<>(rsInvoiceMd.getColumnCount());
            
            for(int i = 1; i <= rsInvoiceMd.getColumnCount(); i++){
                columns.add(rsInvoiceMd.getColumnName(i));
            }
            
            while(rsInvoice.next()){                
                invoiceMap = new HashMap<>(columns.size());
                for(String col : columns) {
                    invoiceMap.put(col, rsInvoice.getObject(col));
                }
                
                psInvoiceDetail = connRentFur.prepareStatement(invoiceDetailSelectSb.toString());
                psInvoiceDetail.setInt(1, invoiceId);
                
                invoiceDetailList = new ArrayList();
                rsInvoiceDetail = psInvoiceDetail.executeQuery();
                rsInvoiceDetailMd = rsInvoiceDetail.getMetaData();
                detailColumns = new ArrayList<>(rsInvoiceDetailMd.getColumnCount());
                for(int i = 1; i <= rsInvoiceDetailMd.getColumnCount(); i++){
                    detailColumns.add(rsInvoiceDetailMd.getColumnName(i));
                }
                
                while(rsInvoiceDetail.next()){
                    invoiceDetailMap = new HashMap<>(detailColumns.size());
                    for(String col : detailColumns) {
                        invoiceDetailMap.put(col, rsInvoiceDetail.getObject(col));
                    }
                    invoiceDetailList.add(invoiceDetailMap);
                }
                invoiceMap.put("invoiceDetail", invoiceDetailList);
            }
            
            connRentFur.commit();
            
            if(psInvoiceDetail != null){
                psInvoiceDetail.close();
            }
            if(rsInvoiceDetail != null){
                rsInvoiceDetail.close();
            }
            psInvoice.close();
            rsInvoice.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseInvoiceController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        return invoiceMap;
    }
}
