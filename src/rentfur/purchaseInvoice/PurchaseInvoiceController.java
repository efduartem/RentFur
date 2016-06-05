/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.purchaseInvoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private UserRoles userRoles;
    public final int ERROR_IN_SAVED = 1;
    
    public PurchaseInvoiceCreate getPurchaseInvoiceCreate(MainWindowController mainWindowController){
        if(purchaseInvoiceCreate == null){
            purchaseInvoiceCreate = new PurchaseInvoiceCreate(this);
        }
        this.mainWindowController = mainWindowController;
        
        return purchaseInvoiceCreate;
    }
    
    public HashMap savePurchaseInvoice(HashMap purchaseInvoiceMap, ArrayList purchaseInvoiceDetailMapArrayList){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement invoicePs;
        PreparedStatement invoiceDetailPs;
        
        StringBuilder invoiceSb = new StringBuilder();
        invoiceSb.append("INSERT INTO purchase_invoice(id, fiscal_stamp_number, invoice_branch, invoice_printer, invoice_number,");
        invoiceSb.append("invoicing_date, provider_code, provider_name, provider_fiscal_number, provider_address, exempt_total, total_tax_5, total_tax_10, ");
        invoiceSb.append("total_tax, total_taxable_5, total_taxable_10, total_taxable, balance, net_total, observation, creation_date, creator_user_id, ");
        invoiceSb.append("last_modification_user_id, last_modification_date)");
        invoiceSb.append(" VALUES ((select nextval('purchase_invoice_seq')), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
        invoiceSb.append("?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?, current_timestamp);");
        
        StringBuilder invoiceDetailSb = new StringBuilder();
        invoiceDetailSb.append("INSERT INTO purchase_invoice_detail(id, code, description, quantity, unit_price, tax_rate, total_amount, tax_amount_5, ");
        invoiceDetailSb.append("tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_total)");
        invoiceDetailSb.append("VALUES ((select nextval('purchase_invoice_detail_seq')), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        try {
            userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            long userLoggedId = loggedUser.getId();
            
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            connRentFur = DbConnectUtil.getConnection();
            invoicePs = connRentFur.prepareStatement(invoiceSb.toString());
            invoiceDetailPs = connRentFur.prepareStatement(invoiceDetailSb.toString());
            
            invoicePs.setString(1, purchaseInvoiceMap.get("invoiceFiscalStampNumber").toString());
            invoicePs.setString(2, purchaseInvoiceMap.get("invoiceBranch").toString());
            invoicePs.setString(3, purchaseInvoiceMap.get("invoicePrinter").toString());
            invoicePs.setString(4, purchaseInvoiceMap.get("invoiceNumber").toString());
            invoicePs.setTimestamp(5, new java.sql.Timestamp(((Date) purchaseInvoiceMap.get("invoicingDate")).getTime()));
            invoicePs.setString(6, purchaseInvoiceMap.get("providerCode").toString());
            invoicePs.setString(7, purchaseInvoiceMap.get("providerName").toString());
            invoicePs.setString(8, purchaseInvoiceMap.get("providerFiscalNumber").toString());
            invoicePs.setString(9, purchaseInvoiceMap.get("providerAddress").toString());
            invoicePs.setDouble(10, Double.valueOf(purchaseInvoiceMap.get("exemptTotal").toString()));
            invoicePs.setDouble(11, Double.valueOf(purchaseInvoiceMap.get("totalTax5").toString()));
            invoicePs.setDouble(12, Double.valueOf(purchaseInvoiceMap.get("totalTax10").toString()));
            invoicePs.setDouble(13, Double.valueOf(purchaseInvoiceMap.get("taxRateTotal").toString()));
            invoicePs.setDouble(14, Double.valueOf(purchaseInvoiceMap.get("totalTaxable5").toString()));
            invoicePs.setDouble(15, Double.valueOf(purchaseInvoiceMap.get("totalTaxable10").toString()));
            invoicePs.setDouble(16, Double.valueOf(purchaseInvoiceMap.get("totalTaxable").toString()));
            invoicePs.setDouble(17, Double.valueOf(purchaseInvoiceMap.get("balance").toString()));
            invoicePs.setDouble(18, Double.valueOf(purchaseInvoiceMap.get("netTotal").toString()));
            invoicePs.setString(19, "");
            invoicePs.setLong(20, userLoggedId);
            invoicePs.setLong(21, userLoggedId);
            invoicePs.executeUpdate();
            
            for(int i=0; i<purchaseInvoiceDetailMapArrayList.size(); i++){
                HashMap invoiceDetailMap = (HashMap) purchaseInvoiceDetailMapArrayList.get(i);
               
                
                invoiceDetailPs.setString(1, (String) invoiceDetailMap.get("furnitureCode"));
                invoiceDetailPs.setString(2, (String) invoiceDetailMap.get("furnitureDescription"));
                invoiceDetailPs.setInt(3, Integer.valueOf(invoiceDetailMap.get("quantity").toString()));
                invoiceDetailPs.setDouble(4, Double.valueOf(invoiceDetailMap.get("unitPrice").toString()));
                invoiceDetailPs.setDouble(5, Double.valueOf(invoiceDetailMap.get("taxRate").toString()));
                invoiceDetailPs.setDouble(6, Double.valueOf(invoiceDetailMap.get("subTotal").toString()));
                invoiceDetailPs.setDouble(7, Double.valueOf(invoiceDetailMap.get("taxRate5").toString()));
                invoiceDetailPs.setDouble(8, Double.valueOf(invoiceDetailMap.get("taxRate10").toString()));
                invoiceDetailPs.setDouble(9, Double.valueOf(invoiceDetailMap.get("taxAmount").toString()));
                invoiceDetailPs.setDouble(10,Double.valueOf(invoiceDetailMap.get("taxableRate5").toString()));
                invoiceDetailPs.setDouble(11,Double.valueOf(invoiceDetailMap.get("taxableRate10").toString()));
                invoiceDetailPs.setDouble(12,Double.valueOf(invoiceDetailMap.get("taxableRate10").toString()));
                invoiceDetailPs.executeQuery();
            }
            
            
        } catch (Throwable th) {
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
        return mapToReturn;
    }
}
