/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.SQLUtilService;
import rentfur.util.User;
import rentfur.util.UserRoles;

/**
 *
 * @author hp
 */
public class ProviderController {
    private MainWindowController mainWindowController;
    private ProviderCreate providerCreate;
    private ProviderIndex providerIndex;
    private ProviderShowAndEdit providerShowAndEdit;
    private UserRoles userRoles;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final boolean IS_ACTIVE = true;
    public final String ALL_VALUES = "Todos";
    
    public ProviderIndex getProviderIndex(MainWindowController mainWindowController, UserRoles userRoles){
        if(providerIndex == null){
            providerIndex = new ProviderIndex(this, userRoles);
        }
        this.mainWindowController = mainWindowController;
        return providerIndex;
    }
    
    public ProviderCreate getProviderCreate(){
        if(providerCreate == null){
            providerCreate = new ProviderCreate(this);
        }
        return providerCreate;
    }
    
    public void searchProviderButtonAction(){
        providerIndex.searchProviderButtonAction(null);
    }
    
     public void setDisabledIndexView(){
        providerIndex.setDisabledElements();
    }
    
    public void setEnabledIndexView(){
        providerIndex.setEnabledElements();
    }
    
    public void indexViewClosed(){
        providerIndex = null;
    }
    
    public ProviderShowAndEdit getProviderShowAndEdit(int providerId, UserRoles userRoles){
         if(providerShowAndEdit == null){
            providerShowAndEdit = new ProviderShowAndEdit(this, providerId, userRoles);
        }
        return providerShowAndEdit;
    }
    
    public void getProviderShowAndEditView(int providerId){
        mainWindowController.setVisibleProviderShowAndEditInternalFrame(providerId);
    }
    
    public void showAndEditViewClosed(){
        providerShowAndEdit = null;
    }
    
    public HashMap saveProvider(String tradename, String name, String fiscalNumber, String address, String telephone, String city, String documentNumberSelectedItem, String verifyDigit){
         HashMap mapToReturn = new HashMap();
         Connection connRentFur = null;
         PreparedStatement ps;
         try{
             userRoles = new UserRoles();
             User loggedUser = userRoles.getUser();
             
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
                   providerCreateSb.append("INSERT INTO provider(id, code, name, address, telephone, fiscal_number, city, is_active, tradename, creator_user_id, creation_date, last_modification_user_id, last_modification_date)");
                   providerCreateSb.append("VALUES ((select nextval('provider_code_seq')), LPAD(nextval('provider_code_seq')::text, 4, '0'), ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, current_timestamp)");
             
                   ps = connRentFur.prepareStatement(providerCreateSb.toString());
                   ps.setString(1, name);
                   ps.setString(2, address);
                   ps.setString(3, telephone);
                   ps.setString(4, fiscalNumber);
                   ps.setString(5, city);
                   ps.setBoolean(6, IS_ACTIVE);
                   ps.setString(7, tradename);
                   ps.setInt(8, loggedUser.getId());
                   ps.setInt(9, loggedUser.getId());
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
    
    public ComboBoxItem[] getProviderIsActiveForComboBox(){
        ComboBoxItem[] providerStatus = null;
        
        try{
            
            providerStatus = new ComboBoxItem[3];
            providerStatus[0] =  new ComboBoxItem();
            providerStatus[0].setKey(ALL_VALUES);
            providerStatus[0].setValue(ALL_VALUES);

            providerStatus[1] =  new ComboBoxItem();
            providerStatus[1].setKey("true");
            providerStatus[1].setValue("Si");

            providerStatus[2] =  new ComboBoxItem();
            providerStatus[2].setKey("false");
            providerStatus[2].setValue("No");
                
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return providerStatus;
    }
    
    public void setProviderIndexResultsTable(DefaultTableModel providerResultDefaultTableModel, boolean searchPressed, String code, String name, String tradename, String address, String city, String telephone, String subjectActive, String fiscalNumber){
         try{
            if(!searchPressed){
                providerResultDefaultTableModel.addColumn("Id");
                providerResultDefaultTableModel.addColumn("Código");
                providerResultDefaultTableModel.addColumn("Razón Social");
                providerResultDefaultTableModel.addColumn("Nombre Comercial");
                providerResultDefaultTableModel.addColumn("Dirección");
                providerResultDefaultTableModel.addColumn("Ciudad");
                providerResultDefaultTableModel.addColumn("Teléfono");
                providerResultDefaultTableModel.addColumn("CI / RUC");
                providerResultDefaultTableModel.addColumn("Activo");
                providerResultDefaultTableModel.addColumn("");
                providerResultDefaultTableModel.addColumn("");
                providerResultDefaultTableModel.addColumn("Active");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = providerResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                providerResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(code, name, tradename, address, city, telephone, subjectActive, fiscalNumber);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                String fiscalNumberResult = "";
                String verificationDigitFiscalNumber = "";
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){
                    verificationDigitFiscalNumber = "";
                    row = new Object[providerResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("code");
                    row[2] = resultValueMap.get("name");
                    row[3] = resultValueMap.get("tradename");
                    row[4] = resultValueMap.get("address");
                    row[5] = resultValueMap.get("city");
                    row[6] = resultValueMap.get("telephone");
                    
                    if(resultValueMap.get("fiscalNumber").toString().contains("-")){
                        fiscalNumberResult = resultValueMap.get("fiscalNumber").toString().split("-")[0];
                        verificationDigitFiscalNumber = "-"+resultValueMap.get("fiscalNumber").toString().split("-")[1];
                    }else{
                        fiscalNumberResult = resultValueMap.get("fiscalNumber").toString();
                    }
                    
                    fiscalNumberResult = amountFormat.format(Double.valueOf(fiscalNumberResult));
                    
                    row[7] = fiscalNumberResult+verificationDigitFiscalNumber;
                    
                    if((Boolean)resultValueMap.get("isActive")){
                        row[8] = "Si";
                    }else{
                        row[8] = "No";
                    }
                    
                    if((Boolean)resultValueMap.get("isActive")){
                        row[9] = "Inactivar";
                    }else{
                        row[9] = "Activar";
                    }
                    
                    row[10] = "Ver";
                    row[11] = resultValueMap.get("isActive");

                    providerResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
     }
    
    public ArrayList getSearchResultList(String code, String name, String tradename, String address, String city, String telephone, String providerActive, String fiscalNumber){
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
            
            StringBuilder providerQuery = new StringBuilder();
            providerQuery.append("SELECT p.id, p.code, p.name, p.tradename, p.address, p.telephone, p.fiscal_number, p.city, p.is_active FROM provider p");   
            providerQuery.append(" WHERE p.code ilike ? AND p.name ilike ? AND p.tradename ilike ? AND p.address ilike ? AND p.telephone ilike ? AND p.city ilike ? AND p.fiscal_number ilike ?");
            if(providerActive!= null && !providerActive.equals(ALL_VALUES)){
                providerQuery.append(" AND is_active = ").append(providerActive);
            }
            providerQuery.append(" ORDER BY p.code, p.name, p.is_active");
            ps = connRentFur.prepareStatement(providerQuery.toString());
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
    
    public HashMap updateProviderStatus(int providerId, boolean active){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
             
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            active = !active;
            connRentFur = DbConnectUtil.getConnection();
            String userUpdate = "UPDATE provider SET is_active = ?, last_modification_user_id = ?, last_modification_date = current_timestamp WHERE id = ?";
            ps = connRentFur.prepareStatement(userUpdate);
            ps.setBoolean(1, active);
            ps.setInt(2, providerId);
            ps.setInt(3, loggedUser.getId());
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
    
    public void getProviderCreateView(){
        mainWindowController.setVisibleProviderCreateInternalFrame();
    }
    
    public HashMap getProviderById(int providerId){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            connRentFur = DbConnectUtil.getConnection();
            StringBuilder providerQuery = new StringBuilder();
            providerQuery.append("SELECT id, code, name, address, fiscal_number, telephone, is_active, status, tradename, city");
            providerQuery.append(" FROM provider p WHERE p.id = ?");
            ps = connRentFur.prepareStatement(providerQuery.toString());
            ps.setInt(1, providerId);
            rs = ps.executeQuery();
            if(rs.next()){
                mapToReturn.put("id",rs.getInt("id"));
                mapToReturn.put("code",rs.getString("code"));
                mapToReturn.put("name", rs.getString("name"));
                mapToReturn.put("address", rs.getString("address"));
                mapToReturn.put("fiscalNumber", rs.getString("fiscal_number"));
                mapToReturn.put("telephone", rs.getString("telephone"));
                mapToReturn.put("tradename", rs.getString("tradename"));
                mapToReturn.put("city", rs.getString("city"));
                mapToReturn.put("isActive", rs.getBoolean("is_active"));
                mapToReturn.put("status", rs.getInt("status"));
            }
            
            rs.close();
            ps.close();
        }catch(Throwable th){
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
    
    public HashMap updateProvider(String name, String tradename, String fiscalNumber, String address, String telephone, String city, boolean active, int providerId, String verifyDigit, String currentFiscalNumber){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            fiscalNumber = fiscalNumber.replaceAll("\\.", "");
            
            if(!verifyDigit.equals("")){
                fiscalNumber += "-"+verifyDigit;
            }
            
            if(name==null || name.equals("")){
                 mapToReturn.put("message", "El campo Razón Social es requerido para la actualizacion de Proveedor");
             }else if(fiscalNumber == null || fiscalNumber.equals("")){
                 mapToReturn.put("message", "El campo Nro de documento es requerido para la actualizacion de Proveedor");
             }else if(!SQLUtilService.isUniqueFiscalNumber(fiscalNumber) && !currentFiscalNumber.equals(fiscalNumber)){
                 mapToReturn.put("message", "El Nro. de documento ya se encuentra registrado, favor verificar este valor");
             }else{
                
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder providerUpdateSb = new StringBuilder();
                providerUpdateSb.append("UPDATE provider SET  name=?, tradename=?, address=?, telephone=?, fiscal_number=?,");
                providerUpdateSb.append(" city=?, is_active=?, last_modification_user_id = ?, last_modification_date = current_timestamp WHERE id = ?");

                ps = connRentFur.prepareStatement(providerUpdateSb.toString());
                ps.setString(1, name);
                ps.setString(2, tradename);
                ps.setString(3, address);
                ps.setString(4, telephone);
                ps.setString(5, fiscalNumber);
                ps.setString(6, city);
                ps.setBoolean(7, active);
                ps.setInt(8, loggedUser.getId());
                ps.setInt(9, providerId);
                
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Proveedor actualizado correctamente");
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
}
