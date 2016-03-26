/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.SQLUtilService;

/**
 *
 * @author hp
 */
public class UserController {
    private MainWindowController mainWindowController;
    private UserCreate userCreate;
    private UserShowAndEdit userShowAndEdit;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "user";
    
    public UserCreate getUserCreate(){
        if(userCreate == null){
            userCreate = new UserCreate(this);
        }
        return userCreate;
    }
    
    public UserShowAndEdit getUserShowAndEdit(int userId){
        if(userShowAndEdit == null){
            userShowAndEdit = new UserShowAndEdit(this,userId);
        }
        return userShowAndEdit;
    }
    
    public ComboBoxItem[] getPositionForComboBox(){
        ComboBoxItem[] positions = null;
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        try{
            int rows = 0;
            connRentFur = DbConnectUtil.getConnection();
            String positionString = "SELECT id, description FROM position ORDER BY description";
            
            ps = connRentFur.prepareStatement(positionString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = ps.executeQuery();
            
            if (rs.last()) {
                rows = rs.getRow();
                rs.beforeFirst();
            }
            
            positions = new ComboBoxItem[rows];            
            while(rs.next()){
                positions[rs.getRow()-1] =  new ComboBoxItem();
                positions[rs.getRow()-1].setKey(rs.getString("id"));
                positions[rs.getRow()-1].setValue(rs.getString("description"));
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
        
        return positions;
    }
    
    public HashMap saveUser(String username, String fullname, String password,  String confirmPassword, String position){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;

        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            SQLUtilService sqlUtilService = new SQLUtilService();
            
            if(username == null || username.equals("")){                
                mapToReturn.put("message", "El campo Nombre Usuario es requerido para la creacion del Usuario");
            }else if(fullname==null || fullname.equals("")){
                mapToReturn.put("message", "El campo Nombre completo es requerido para la creacion del Usuario");
            }else if(password==null || password.equals("")){
               mapToReturn.put("message", "El campo Contraseña es requerido para la creacion del Usuario"); 
            }if(!password.equals(confirmPassword)){
                mapToReturn.put("message", "Las contraseñas ingresadas no coinciden");
            }else{
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder userInsertSb = new StringBuilder();
                userInsertSb.append("INSERT INTO users(id, code, username, fullname, password, active, position_id)");
                userInsertSb.append("VALUES (nextval('users_seq'), LPAD(nextval('users_code_seq')::text, 4, '0'), ?, ?, ?, ?, ?);");
                
                ps = connRentFur.prepareStatement(userInsertSb.toString());
                ps.setString(1, username);
                ps.setString(2, fullname);
                ps.setString(3, password);
                ps.setBoolean(4, true);
                ps.setLong(5, Long.valueOf(position));
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Usuario creado correctamente");
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
    
    public HashMap updateUser(int userId, String username, String fullname, String password,  String confirmPassword, String position, boolean status){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
         try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            SQLUtilService sqlUtilService = new SQLUtilService();
            
             System.out.println("USERNAME: "+username);
            if(username == null || username.trim().equals("")){                
                mapToReturn.put("message", "El campo Nombre Usuario no puede quedar vacio.");
            }else if(fullname==null || fullname.equals("")){
                mapToReturn.put("message", "El campo Nombre completo es requerido para la creacion del Usuario");
            }else if(password==null || password.equals("")){
               mapToReturn.put("message", "El campo Contraseña es requerido para la creacion del Usuario"); 
            }else if(!password.equals(confirmPassword)){
                mapToReturn.put("message", "Las contraseñas ingresadas no coinciden");
            }else{
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder userInsertSb = new StringBuilder();
                userInsertSb.append("UPDATE users");
                userInsertSb.append(" SET username=?, fullname=?, password=?, position_id=?, active=?");
                userInsertSb.append(" WHERE id = ?");
                
                ps = connRentFur.prepareStatement(userInsertSb.toString());
                ps.setString(1, username);
                ps.setString(2, fullname);
                ps.setString(3, password);
                ps.setLong(4, Long.valueOf(position));
                ps.setBoolean(5, status);
                ps.setInt(6, userId);
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Usuario modificado correctamente");
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
    public void createViewClosed(){
        userCreate = null;
    }
    
    public HashMap getUserById(int userId){
        HashMap userMap = new HashMap();
        Connection connRentFur = null;
        PreparedStatement userPs;
        ResultSet userRs;
        try{
            SQLUtilService sqlUtilService = new SQLUtilService();
            connRentFur = DbConnectUtil.getConnection();
            
            StringBuilder userSb = new StringBuilder();
            userSb.append("SELECT code, username, fullname, password, active, position_id,");
            userSb.append("(SELECT description FROM position WHERE id = position_id) FROM users WHERE id = ?");
            userPs = connRentFur.prepareStatement(userSb.toString());
            userPs.setInt(1, userId);
            
            userRs = userPs.executeQuery();
            if(userRs.next()){
                userMap.put("code", userRs.getString(1));
                userMap.put("username", userRs.getString(2));
                userMap.put("fullname", userRs.getString(3));
                userMap.put("password", userRs.getString(4));
                userMap.put("active", userRs.getBoolean(5));
                userMap.put("positionId", userRs.getLong(6));
                userMap.put("positionDescription", userRs.getString(7));
            }
            
            userPs.close();
            userRs.close();
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
        return userMap;
    }
    
}
