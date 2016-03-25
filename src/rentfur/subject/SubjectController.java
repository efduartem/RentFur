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
    
    public HashMap saveSubject(String tradename, String name, String fiscalNumber, String address, String telephone, String city, String documentNumberSelectedItem, String verifyDigit){
         HashMap mapToReturn = new HashMap();
         Connection connRentFur = null;
         PreparedStatement ps;
         try{
             mapToReturn.put("status", ERROR_IN_SAVED);
             mapToReturn.put("message", "");
             
             if(name==null || name.equals("")){
                 mapToReturn.put("message", "El campo Razón Social es requerido para la creacion de Cliente");
             }else if(fiscalNumber == null || fiscalNumber.equals("")){
                 mapToReturn.put("message", "El campo Nro de documento es requerido para la creacion de Cliente");
             }else if(!SQLUtilService.isUniqueFiscalNumber(fiscalNumber.replaceAll("\\.", ""))){
                 mapToReturn.put("message", "El Nro de documento ya se encuentra registrado, favor verificar este valor");
             }else{
                   connRentFur = DbConnectUtil.getConnection();
                   fiscalNumber = fiscalNumber.replaceAll("\\.", "");
                   if(documentNumberSelectedItem.equals(SubjectCreate.DOCUMENT_RUC)){
                       fiscalNumber += "-"+verifyDigit;
                   }
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
     
     public void setSubjectIndexResultsTable(DefaultTableModel subjectResultDefaultTableModel, boolean searchPressed, String code, String name){
         try{
            if(!searchPressed){
                subjectResultDefaultTableModel.addColumn("Id");
                subjectResultDefaultTableModel.addColumn("Código");
                subjectResultDefaultTableModel.addColumn("Descripción");
                subjectResultDefaultTableModel.addColumn("");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = subjectResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                subjectResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(code, name);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                DecimalFormat amountFormat = new DecimalFormat("#,###");
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){

                    row = new Object[subjectResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("code");
                    row[2] = resultValueMap.get("name");
                    if((Boolean)resultValueMap.get("isActive")){
                        row[3] = "Inactivar";
                    }else{
                        row[3] = "Activar";
                    }
                    
                    /*if(permisosIdMap.containsKey(rs.getInt("idpermisos"))){
                        fila[1] = Boolean.TRUE;
                    }else{
                        fila[1] = Boolean.FALSE;
                    } */

                    subjectResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
     }
     
     public ArrayList getSearchResultList(String code, String name){
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
            
            StringBuilder furnituriesQuery = new StringBuilder();
            furnituriesQuery.append("SELECT s.id, s.code, s.name, s.is_active FROM subject s WHERE s.code ilike ? AND s.name ilike ?");   
            furnituriesQuery.append(" ORDER BY s.code, s.name, s.is_active");
            ps = connRentFur.prepareStatement(furnituriesQuery.toString());
            ps.setString(1, "%"+code+"%");
            ps.setString(2, "%"+name+"%");
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("name", rs.getString("name"));
                resultValuesMap.put("isActive", rs.getBoolean("is_active"));
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