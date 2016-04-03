/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.Color;
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
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.SQLUtilService;

/**
 *
 * @author hp
 */
public class SubjectController {
    private MainWindowController mainWindowController;
    private SubjectCreate subjectCreate;
    private SubjectIndex subjectIndex;
    private SubjectShowAndEdit subjectShowAndEdit;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final boolean IS_ACTIVE = true;
    public final boolean IS_INACTIVE = false;
    public final String TABLE_NAME = "subject";
    public final String ALL_VALUES = "Todos";
    
    public SubjectIndex getSubjectIndex(MainWindowController mainWindowController){
        if( subjectIndex == null){
            subjectIndex = new SubjectIndex(this);
        }
        this.mainWindowController = mainWindowController;
        return subjectIndex;
    }
    
    public SubjectCreate getSubjectCreate(){
        if( subjectCreate == null){
            subjectCreate = new SubjectCreate(this);
        }
        return subjectCreate;
    }
    
    public SubjectShowAndEdit getSubjectShowAndEdit(int subjectId){
        if(subjectShowAndEdit == null){
            subjectShowAndEdit = new SubjectShowAndEdit(this, subjectId);
        }
        return subjectShowAndEdit;
    }
    
    public void searchSubjectButtonAction(){
        subjectIndex.searchSubjectButtonAction(null);
    }
    
    public void setDisabledIndexView(){
        subjectIndex.setDisabledElements();
    }
    
    public void showAndEditViewClosed(){
        subjectShowAndEdit = null;
    }
    
    public void setEnabledIndexView(){
        subjectIndex.setEnableddElements();
    }
    
    public void getSubjectShowAndEditView(int subjectId){
        mainWindowController.setVisibleSubjectShowAndEditInternalFrame(subjectId);
    }
    
    public HashMap saveSubject(String tradename, String name, String fiscalNumber, String address, String telephone, String city, String documentNumberSelectedItem, String verifyDigit){
         HashMap mapToReturn = new HashMap();
         Connection connRentFur = null;
         PreparedStatement ps;
         try{
             mapToReturn.put("status", ERROR_IN_SAVED);
             mapToReturn.put("message", "");
             
             fiscalNumber = fiscalNumber.replaceAll("\\.", "");
            if(documentNumberSelectedItem.equals(SubjectCreate.DOCUMENT_RUC)){
                fiscalNumber += "-"+verifyDigit;
            }
             
             if(name==null || name.equals("")){
                 mapToReturn.put("message", "El campo Razón Social es requerido para la creacion de Cliente");
             }else if(fiscalNumber == null || fiscalNumber.equals("")){
                 mapToReturn.put("message", "El campo Nro de documento es requerido para la creacion de Cliente");
             }else if(!SQLUtilService.isUniqueFiscalNumber(fiscalNumber)){
                 mapToReturn.put("message", "El Nro de documento ya se encuentra registrado, favor verificar este valor");
             }else{
                   connRentFur = DbConnectUtil.getConnection();
                   
                   StringBuilder subjectCreateSb = new StringBuilder();
                   subjectCreateSb.append("INSERT INTO subject(id, code, name, address, telephone, fiscal_number, city, is_active, tradename)");
                   subjectCreateSb.append("VALUES ((select nextval('subject_seq')), LPAD(nextval('subject_code_seq')::text, 4, '0'), ?, ?, ?, ?, ?, ?, ?)");
             
                   ps = connRentFur.prepareStatement(subjectCreateSb.toString());
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
                   mapToReturn.put("message", "Cliente creado correctamente");
             
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
        subjectCreate = null;
    }
     
    public void indexViewClosed(){
        subjectIndex = null;
    } 
    
    public void getSubjectCreateView(){
        mainWindowController.setVisibleSubjectCreateInternalFrame();
    }
    
    public ComboBoxItem[] getSubjectIsActiveForComboBox(){
        ComboBoxItem[] subjectStatus = null;
        
        try{
            
            subjectStatus = new ComboBoxItem[3];
            subjectStatus[0] =  new ComboBoxItem();
            subjectStatus[0].setKey(ALL_VALUES);
            subjectStatus[0].setValue(ALL_VALUES);

            subjectStatus[1] =  new ComboBoxItem();
            subjectStatus[1].setKey("true");
            subjectStatus[1].setValue("Si");

            subjectStatus[2] =  new ComboBoxItem();
            subjectStatus[2].setKey("false");
            subjectStatus[2].setValue("No");
                
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return subjectStatus;
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
     
     public void setSubjectIndexResultsTable(DefaultTableModel subjectResultDefaultTableModel, boolean searchPressed, String code, String name, String tradename, String address, String city, String telephone, String subjectActive, String fiscalNumber){
         try{
            if(!searchPressed){
                subjectResultDefaultTableModel.addColumn("Id");
                subjectResultDefaultTableModel.addColumn("Código");
                subjectResultDefaultTableModel.addColumn("Razón Social");
                subjectResultDefaultTableModel.addColumn("Nombre Comercial");
                subjectResultDefaultTableModel.addColumn("Dirección");
                subjectResultDefaultTableModel.addColumn("Ciudad");
                subjectResultDefaultTableModel.addColumn("Teléfono");
                subjectResultDefaultTableModel.addColumn("CI / RUC");
                subjectResultDefaultTableModel.addColumn("Activo");
                subjectResultDefaultTableModel.addColumn("");
                subjectResultDefaultTableModel.addColumn("");
                subjectResultDefaultTableModel.addColumn("Active");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = subjectResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                subjectResultDefaultTableModel.removeRow(0);
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
                    row = new Object[subjectResultDefaultTableModel.getColumnCount()];
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

                    subjectResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
     }
     
     public ArrayList getSearchResultList(String code, String name, String tradename, String address, String city, String telephone, String subjectActive, String fiscalNumber){
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
            subjectsQuery.append(" WHERE s.code ilike ? AND s.name ilike ? AND s.tradename ilike ? AND s.address ilike ? AND s.telephone ilike ? AND s.city ilike ? AND s.fiscal_number ilike ?");
            if(subjectActive!= null && !subjectActive.equals(ALL_VALUES)){
                subjectsQuery.append(" AND is_active = ").append(subjectActive);
            }
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
     
    public HashMap updateSubjectStatus(int subjectId, boolean active){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            active = !active;
            connRentFur = DbConnectUtil.getConnection();
            String userUpdate = "UPDATE subject SET is_active = ? WHERE id = ?";
            ps = connRentFur.prepareStatement(userUpdate);
            ps.setBoolean(1, active);
            ps.setInt(2, subjectId);
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

    public HashMap getSubjectById(int subjectId){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            StringBuilder subjectQuery = new StringBuilder();
            subjectQuery.append("SELECT id, code, name, address, telephone, fiscal_number, city, is_active, status, tradename");
            subjectQuery.append(" FROM subject WHERE id = ?");
            ps = connRentFur.prepareStatement(subjectQuery.toString());
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();
            if(rs.next()){
                mapToReturn.put("id",rs.getInt("id"));
                mapToReturn.put("code",rs.getString("id"));
                mapToReturn.put("name",rs.getString("name"));
                if(rs.getString("address") != null){
                    mapToReturn.put("address",rs.getString("address"));
                }else{
                    mapToReturn.put("address","");
                }
                if(rs.getString("telephone") != null){
                     mapToReturn.put("telephone",rs.getString("telephone"));
                }else{
                     mapToReturn.put("telephone","");
                }
                if(rs.getString("fiscal_number") != null){
                    mapToReturn.put("fiscalNumber", rs.getString("fiscal_number"));
                }else{
                    mapToReturn.put("fiscalNumber", "");
                }
                if(rs.getString("city") != null){
                     mapToReturn.put("city",rs.getString("city"));
                }else{
                    mapToReturn.put("city","");
                }
                
                mapToReturn.put("active",rs.getBoolean("is_active"));
                
                if(rs.getString("status") != null){
                    mapToReturn.put("status",rs.getString("status"));
                }else{
                    mapToReturn.put("status","");
                }
                
                if(rs.getString("tradename") != null){
                     mapToReturn.put("tradename",rs.getString("tradename"));
                }else{
                     mapToReturn.put("tradename","");
                }
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
     
    public HashMap updateSubject(String name, String tradename, String fiscalNumber, String address, String telephone, String city, boolean active, int subjectId, String verifyDigit, String currentFiscalNumber){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            fiscalNumber = fiscalNumber.replaceAll("\\.", "");
            
            if(!verifyDigit.equals("")){
                fiscalNumber += "-"+verifyDigit;
            }
            
            if(name==null || name.equals("")){
                 mapToReturn.put("message", "El campo Razón Social es requerido para la actualizacion de Cliente");
             }else if(fiscalNumber == null || fiscalNumber.equals("")){
                 mapToReturn.put("message", "El campo Nro de documento es requerido para la actualizacion de Cliente");
             }else if(!SQLUtilService.isUniqueFiscalNumber(fiscalNumber) && !currentFiscalNumber.equals(fiscalNumber)){
                 mapToReturn.put("message", "El Nro. de documento ya se encuentra registrado, favor verificar este valor");
             }else{
                
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder subjectUpdateSb = new StringBuilder();
                subjectUpdateSb.append("UPDATE subject SET  name=?, tradename=?, address=?, telephone=?, fiscal_number=?,");
                subjectUpdateSb.append(" city=?, is_active=? WHERE id = ?");
                
                
                
                ps = connRentFur.prepareStatement(subjectUpdateSb.toString());
                ps.setString(1, name);
                ps.setString(2, tradename);
                ps.setString(3, address);
                ps.setString(4, telephone);
                ps.setString(5, fiscalNumber);
                ps.setString(6, city);
                ps.setBoolean(7, active);
                ps.setInt(8, subjectId);
                
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Cliente actualizado correctamente");
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
