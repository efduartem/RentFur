/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.subject;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import rentfur.util.DbConnectUtil;
import rentfur.util.SQLUtilService;

/**
 *
 * @author hp
 */
public class SubjectController {
    private SubjectCreate subjectCreate;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final boolean IS_ACTIVE = true;
    public final boolean IS_INACTIVE = false;
    public final String TABLE_NAME = "subject";
    
    public SubjectCreate getSubjectCreate(){
        if( subjectCreate == null){
            subjectCreate = new SubjectCreate(this);
        }
        return subjectCreate;
    }
    
    HashMap saveSubject(String code, String name, String fiscalNumber, String address, String telephone, String city){
         HashMap mapToReturn = new HashMap();
         Connection connRentFur = null;
         PreparedStatement ps;
         try{
             mapToReturn.put("status", ERROR_IN_SAVED);
             mapToReturn.put("message", "");
             
             SQLUtilService sqlUtilService = new SQLUtilService();
             if(code==null || code.equals("")){
                 mapToReturn.put("message", "El campo Codigo es requerido para la creacion de Cliente");
             }else if(name==null || name.equals("")){
                 mapToReturn.put("message", "El campo Nombre es requerido para la creacion de Cliente");
             }else if(fiscalNumber == null || fiscalNumber.equals("")){
                 mapToReturn.put("message", "El campo Nro de documento es requerido para la creacion de Cliente");
             }else if(!sqlUtilService.isUniqueCode(TABLE_NAME, code)){
                 mapToReturn.put("message", "El codigo "+code+" ya existe, favor ingresar otro valor");
                 subjectCreate.setCodeTextFieldColor(Color.red);
             }else{
                   Locale pyLocale = new Locale("es", "PY");
                   NumberFormat nf = NumberFormat.getInstance(pyLocale);
                   connRentFur = DbConnectUtil.getConnection();
                   
                   StringBuilder subjectCreateSb = new StringBuilder();
                   subjectCreateSb.append("INSERT INTO subject(id, code, name, address, telephone, fiscal_number, city, is_active)");
                   subjectCreateSb.append("VALUES ((select nextval('subject_id_seq')), ?, ?, ?, ?, ?, ?, ?)");
             
                   ps = connRentFur.prepareStatement(subjectCreateSb.toString());
                   ps.setString(1, code);
                   ps.setString(2, name);
                   ps.setString(3, address);
                   ps.setString(4, telephone);
                   ps.setString(5, fiscalNumber);
                   ps.setString(6, city);
                   ps.setBoolean(7, IS_ACTIVE);
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
   
}
