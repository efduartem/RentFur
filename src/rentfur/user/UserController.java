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
public class UserController {
    private MainWindowController mainWindowController;
    private UserCreate userCreate;
    private UserShowAndEdit userShowAndEdit;
    private UserIndex userIndex;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String TABLE_NAME = "users";
    public final String ALL_VALUES = "Todos";
    
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
    
    public UserIndex getUserIndex(MainWindowController mainWindowController){
        if(userIndex == null){
            userIndex = new UserIndex(this);
        }
        this.mainWindowController = mainWindowController;
        return userIndex;
    }
    
    public void indexViewClosed(){
        userIndex = null;
    }
    
    public void getUserCreateView(){
        mainWindowController.setVisibleUserCreateInternalFrame();
    }
    
    public void setDisabledIndexView(){
        userIndex.setDisabledElements();
    }
    
    public void setEnabledIndexView(){
        userIndex.setEnableddElements();
    }
    
    public void searchUserButtonAction(){
        userIndex.searchUserButtonAction(null);
    }
    
    public void getUserShowAndEditView(int userId){
        mainWindowController.setVisibleUserShowAndEditInternalFrame(userId);
    }
    
    public void showAndEditViewClosed(){
        userShowAndEdit = null;
    }
    
    public ComboBoxItem[] getPositionForComboBox(boolean addAllOption){
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
            
            if(addAllOption){
                positions = new ComboBoxItem[rows+1];
                positions[0] =  new ComboBoxItem();
                positions[0].setKey(ALL_VALUES);
                positions[0].setValue(ALL_VALUES);
                while(rs.next()){
                    positions[rs.getRow()] =  new ComboBoxItem();
                    positions[rs.getRow()].setKey(rs.getString("id"));
                    positions[rs.getRow()].setValue(rs.getString("description"));
                }
            }else{
                positions = new ComboBoxItem[rows];
                
                while(rs.next()){
                    positions[rs.getRow()-1] =  new ComboBoxItem();
                    positions[rs.getRow()-1].setKey(rs.getString("id"));
                    positions[rs.getRow()-1].setValue(rs.getString("description"));
                }
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
    
    public ComboBoxItem[] getUserStatusForComboBox(){
        ComboBoxItem[] furnitureStatus = null;
        
        try{
            
            furnitureStatus = new ComboBoxItem[3];
            furnitureStatus[0] =  new ComboBoxItem();
            furnitureStatus[0].setKey(ALL_VALUES);
            furnitureStatus[0].setValue(ALL_VALUES);

            furnitureStatus[1] =  new ComboBoxItem();
            furnitureStatus[1].setKey("true");
            furnitureStatus[1].setValue("Activo");

            furnitureStatus[2] =  new ComboBoxItem();
            furnitureStatus[2].setKey("false");
            furnitureStatus[2].setValue("Inactivo");
                
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return furnitureStatus;
    }
    
    public void setUserIndexResultsTable(DefaultTableModel usersResultDefaultTableModel, boolean searchPressed, String code, String fullname, String username, String positionId, String userStatus){
        
        try{
            if(!searchPressed){
                usersResultDefaultTableModel.addColumn("Id");
                usersResultDefaultTableModel.addColumn("Código");
                usersResultDefaultTableModel.addColumn("Nombre Completo");
                usersResultDefaultTableModel.addColumn("Nombre de Usuario");
                usersResultDefaultTableModel.addColumn("Cargo");
                usersResultDefaultTableModel.addColumn("Estado");
                usersResultDefaultTableModel.addColumn("");
                usersResultDefaultTableModel.addColumn("");
                usersResultDefaultTableModel.addColumn("Status");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = usersResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                usersResultDefaultTableModel.removeRow(0);
            }
            ArrayList searchResultList = getSearchResultList(code, fullname, username, positionId, userStatus);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){

                    row = new Object[usersResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("code");
                    row[2] = resultValueMap.get("fullname");
                    row[3] = resultValueMap.get("username");
                    row[4] = resultValueMap.get("position");
                    if((Boolean)resultValueMap.get("active")){
                        row[5] = "Activo";
                    }else{
                        row[5] = "Inactivo";
                    }
                    
                    if((Boolean)resultValueMap.get("active")){
                        row[6] = "Inactivar";
                    }else{
                        row[6] = "Activar";
                    }
                    
                    row[7] = "Ver";
                    row[8] = resultValueMap.get("active");

                    usersResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
    }
    
    public ArrayList getSearchResultList(String code, String fullname, String username , String positionId, String userStatus){
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
            
            if(fullname==null){
                fullname="";
            }
            
            if(username==null){
                username="";
            }
            
            StringBuilder furnituriesQuery = new StringBuilder();
            furnituriesQuery.append("SELECT u.id, u.code, u.fullname, u.username, u.active, (SELECT description FROM position p WHERE p.id = u.position_id) as position FROM users u WHERE u.code ilike ? AND u.fullname ilike ? AND u.username ilike ?");
            if(positionId!= null && !positionId.equals(ALL_VALUES)){
                furnituriesQuery.append(" AND position_id = ").append(positionId);
            }
            
            if(userStatus!= null && !userStatus.equals(ALL_VALUES)){
                furnituriesQuery.append(" AND active = ").append(userStatus);
            }
            
            furnituriesQuery.append(" ORDER BY u.code, u.fullname, u.username, u.active");
            ps = connRentFur.prepareStatement(furnituriesQuery.toString());
            ps.setString(1, "%"+code+"%");
            ps.setString(2, "%"+fullname+"%");
            ps.setString(3, "%"+username+"%");
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("fullname", rs.getString("fullname"));
                resultValuesMap.put("username", rs.getString("username"));
                resultValuesMap.put("active", rs.getBoolean("active"));
                resultValuesMap.put("position", rs.getString("position"));
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
    
    public HashMap updateUserStatus(int userId, boolean active){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            active = !active;
            connRentFur = DbConnectUtil.getConnection();
            String furnitureDelete = "UPDATE users SET active = ? WHERE id = ?";
            ps = connRentFur.prepareStatement(furnitureDelete);
            ps.setBoolean(1, active);
            ps.setInt(2, userId);
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
}
