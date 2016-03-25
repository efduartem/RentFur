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
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "user";
    
    public UserCreate getUserCreate(){
        if(userCreate == null){
            userCreate = new UserCreate(this);
        }
        return userCreate;
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
            
            positions = new ComboBoxItem[rows+1];
            positions[0] =  new ComboBoxItem();
            
            while(rs.next()){
                positions[rs.getRow()] =  new ComboBoxItem();
                positions[rs.getRow()].setKey(rs.getString("id"));
                positions[rs.getRow()].setValue(rs.getString("description"));
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
    
    public HashMap saveUser(String username, String fullname, char[]password, String position){
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
            }else{
                Locale pyLocale = new Locale("es", "PY");
                NumberFormat nf = NumberFormat.getInstance(pyLocale);
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder userInsertSb = new StringBuilder();
                userInsertSb.append("INSERT INTO user(id, code, username, fullname, password, active, position_id)");
                userInsertSb.append("VALUES (nextval('user_seq'), LPAD(nextval('user_code_seq')::text, 4, '0'), ?, ?, ?, ?, ?);");
                
                ps = connRentFur.prepareStatement(userInsertSb.toString());
                ps.setString(1, username);
                ps.setString(2, fullname);
                ps.setString(2, String.valueOf(password));
                ps.setBoolean(3, true);
                ps.setLong(4, Long.valueOf(position));
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
    
    public void createViewClosed(){
        userCreate = null;
    }
    
}
