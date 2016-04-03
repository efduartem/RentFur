/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.SQLUtilService;

/**
 *
 * @author hp
 */
public class ProviderController {
    private MainWindowController mainWindowController;
    private ProviderCreate providerCreate;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final boolean IS_ACTIVE = true;
    
    public ProviderCreate getProviderCreate(){
        if(providerCreate == null){
            providerCreate = new ProviderCreate(this);
        }
        return providerCreate;
    }
    
    public HashMap saveProvider(String tradename, String name, String fiscalNumber, String address, String telephone, String city, String documentNumberSelectedItem, String verifyDigit){
         HashMap mapToReturn = new HashMap();
         Connection connRentFur = null;
         PreparedStatement ps;
         try{
             mapToReturn.put("status", ERROR_IN_SAVED);
             mapToReturn.put("message", "");
             
             if(name==null || name.equals("")){
                 mapToReturn.put("message", "El campo Razón Social es requerido para la creacion de Proveedor");
             }else if(fiscalNumber == null || fiscalNumber.equals("")){
                 mapToReturn.put("message", "El campo Nro de documento es requerido para la creacion de Proveedor");
             }else if(!SQLUtilService.isUniqueFiscalNumber(fiscalNumber.replaceAll("\\.", ""))){
                 mapToReturn.put("message", "El Nro de documento ya se encuentra registrado, favor verificar este valor");
             }else{
                   connRentFur = DbConnectUtil.getConnection();
                   fiscalNumber = fiscalNumber.replaceAll("\\.", "");
                   if(documentNumberSelectedItem.equals(ProviderCreate.DOCUMENT_RUC)){
                       fiscalNumber += "-"+verifyDigit;
                   }
                   StringBuilder providerCreateSb = new StringBuilder();
                   providerCreateSb.append("INSERT INTO provider(id, code, name, address, telephone, fiscal_number, city, is_active, tradename)");
                   providerCreateSb.append("VALUES ((select nextval('provider_seq')), LPAD(nextval('provider_code_seq')::text, 4, '0'), ?, ?, ?, ?, ?, ?, ?)");
             
                   ps = connRentFur.prepareStatement(providerCreateSb.toString());
                   ps.setString(1, name);
                   ps.setString(2, address);
                   ps.setString(3, telephone);
                   ps.setString(4, fiscalNumber);
                   ps.setString(5, city);
                   ps.setBoolean(6, IS_ACTIVE);
                   ps.setString(7, tradename);
                   ps.executeUpdate();
                   ps.close();
                   
                   mapToReturn.put("status", SUCCESFULLY_SAVED);
                   mapToReturn.put("message", "Proveedor creado correctamente");
             
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
        providerCreate = null;
    }
    
    public int calculateDv(String p_numero) { 
         int p_basemax = 11;
	  int v_total, v_resto, k, v_numero_aux, v_digit; 
	  String v_numero_al = ""; 
	 
	  for (int i = 0; i < p_numero.length(); i++) { 
	    char c = p_numero.charAt(i); 
	    if(Character.isDigit(c)) { 
	      v_numero_al += c; 
	    } else { 
	      v_numero_al += (int) c; 
	    } 
	  } 
	 
	  k = 2; 
	  v_total = 0; 
	 
	  for(int i = v_numero_al.length() - 1; i >= 0; i--) { 
	    k = k > p_basemax ? 2 : k; 
	    v_numero_aux = v_numero_al.charAt(i) - 48; 
	    v_total += v_numero_aux * k++; 
	  } 
	 
	  v_resto = v_total % 11; 
	  v_digit = v_resto > 1 ? 11 - v_resto : 0; 
	 
	  return v_digit; 
	}
}
