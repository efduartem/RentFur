/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.position;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.RoleEntry;

/**
 *
 * @author FDuarte
 */
public class PositionController {
    private MainWindowController mainWindowController;
    private PositionCreate positionCreate;
    private PositionIndex positionIndex;
    private PositionShowAndEdit positionShowAndEdit;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    public final String ROLE_RF_FURNITURE = "ROLE_RF_FURNITURE";
    public final String ROLE_RF_PROVIDER = "ROLE_RF_PROVIDER";
    public final String ROLE_RF_SUBJECT = "ROLE_RF_SUBJECT";
    public final String ROLE_RF_POSITION = "ROLE_RF_POSITION";
    public final String ROLE_RF_USER = "ROLE_RF_USER";
    public final String ALL_VALUES = "Todos";
    
    
    public PositionCreate getPositionCreate(){
        if(positionCreate == null){
            positionCreate = new PositionCreate(this);
        }
        return positionCreate;
    }
    
    public PositionIndex getPositionIndex(MainWindowController mainWindowController){
        if(positionIndex == null){
            positionIndex = new PositionIndex(this);
        }
        this.mainWindowController = mainWindowController;
        return positionIndex;
    }
    
    public PositionShowAndEdit getPositionShowAndEdit(int positionId){
        if(positionShowAndEdit == null){
            positionShowAndEdit = new PositionShowAndEdit(this,positionId);
        }
        return positionShowAndEdit;
    }
    
    public void showAndEditViewClosed(){
        positionShowAndEdit = null;
    }
    
    public void createViewClosed(){
        positionCreate = null;
    }
    
    public void indexViewClosed(){
        positionIndex = null;
    }
    
    public void getPositionCreateView(){
        mainWindowController.setVisiblePositionCreateInternalFrame();
    }
    
    public void setDisabledIndexView(){
        positionIndex.setDisabledElements();
    }
    
    public void setEnabledIndexView(){
        positionIndex.setEnableddElements();
    }
    
    public void searchPositionButtonAction(){
        positionIndex.searchPositionButtonAction(null);
    }
    
    public void getPositionShowAndEditView(int positionId){
        mainWindowController.setVisiblePositionShowAndEditInternalFrame(positionId);
    }
    
    public HashMap savePosition(String description, HashMap rolesMap) {
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            if(description == null || description.equals("")){
                mapToReturn.put("message", "El campo Descripcion es requerido para la creacion del Cargo");
            }else{
                
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder furnitureInsertSb = new StringBuilder();
                furnitureInsertSb.append("INSERT INTO position(id, code, description)");
                furnitureInsertSb.append(" VALUES (nextval('position_seq'), LPAD(nextval('position_code_seq')::text, 4, '0'), ?)");
                ps = connRentFur.prepareStatement(furnitureInsertSb.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, description);
                ps.executeUpdate();
                
                int positionId = 0;
                rs = ps.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    positionId = (int) rs.getLong(1);
                }
                
                insertNewsPositionRole(positionId, rolesMap);
                
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Cargo creado correctamente");
            }
            
        }catch(SQLException th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
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
    
    public void insertNewsPositionRole(int positionId, HashMap rolesMap) throws SQLException{
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();

            StringBuilder furnitureInsertSb = new StringBuilder();
            furnitureInsertSb.append("INSERT INTO position_role(position_id, role_id, only_query)");
            furnitureInsertSb.append(" VALUES (?, ?, ?)");
            ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
            
            int roleId = 0;
                    
            //FURNITURE
            if((Boolean) rolesMap.get("furniture")){
                boolean furnitureReadOnly = (Boolean) rolesMap.get("furnitureReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_FURNITURE);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, furnitureReadOnly);
                ps.addBatch();
            }

            //PROVIDER
            if((Boolean) rolesMap.get("provider")){
                boolean provider = (Boolean) rolesMap.get("provider");
                boolean providerReadOnly = (Boolean) rolesMap.get("providerReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_PROVIDER);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, providerReadOnly);
                ps.addBatch();
            }

            //SUBJECT
            if((Boolean) rolesMap.get("subject")){ 
                boolean subjectReadOnly = (Boolean) rolesMap.get("subjectReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_SUBJECT);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, subjectReadOnly);
                ps.addBatch();
            }

            //USERS
            if((Boolean) rolesMap.get("users")){
                boolean users = (Boolean) rolesMap.get("users");
                boolean usersReadOnly = (Boolean) rolesMap.get("usersReadOnly");
                roleId = getRoleIdByCode(ROLE_RF_USER);
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, usersReadOnly);
                ps.addBatch();
            }
           
            ps.executeBatch();
            ps.close();
            
        }catch(SQLException th){
            throw th;
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
    }
    
    public int getRoleIdByCode(String roleCode) throws SQLException{
        int roleId = 0;
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();

            StringBuilder furnitureInsertSb = new StringBuilder();
            furnitureInsertSb.append("SELECT id FROM role WHERE code = ? ");
            ps = connRentFur.prepareStatement(furnitureInsertSb.toString());
            ps.setString(1, roleCode);
            rs = ps.executeQuery();
            if(rs.next()){
                roleId = rs.getInt("id");
            }
            rs.close();
            ps.close();
            
        }catch(SQLException th){
            throw th;
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
        
        return roleId;
    }
    
    public void setPositionIndexResultsTable(DefaultTableModel positionsResultDefaultTableModel, boolean searchPressed, String code, String description, String roleId){
        
        try{
            if(!searchPressed){
                positionsResultDefaultTableModel.addColumn("Id");
                positionsResultDefaultTableModel.addColumn("Código");
                positionsResultDefaultTableModel.addColumn("Descripción");
                positionsResultDefaultTableModel.addColumn("Permisos");
                positionsResultDefaultTableModel.addColumn("");
            }
            
            int numeroRegistrosTablaPermisos=0;
            numeroRegistrosTablaPermisos = positionsResultDefaultTableModel.getRowCount();
            for(int i=0;i<numeroRegistrosTablaPermisos;i++){
                positionsResultDefaultTableModel.removeRow(0);
            }
            
            ArrayList searchResultList = getSearchResultList(code, description, roleId);
            if(searchResultList!=null && !searchResultList.isEmpty()){
                HashMap resultValueMap;
                Object[] row;
                for(int rowNumber = 0; rowNumber < searchResultList.size(); rowNumber++){

                    row = new Object[positionsResultDefaultTableModel.getColumnCount()];
                    resultValueMap = (HashMap) searchResultList.get(rowNumber);

                    row[0] = resultValueMap.get("id");
                    row[1] = resultValueMap.get("code");
                    row[2] = resultValueMap.get("description");
                    row[3] = resultValueMap.get("roles");//roles;
                    row[4] = "Ver";

                    positionsResultDefaultTableModel.addRow(row);

                }
            }
            
        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
            th.printStackTrace();
        }
    }
    
    public ArrayList getSearchResultList(String code, String description, String roleId){
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psRole;
        ResultSet rs;
        ResultSet rsRole;
        ArrayList listToReturn = new ArrayList();
        
        try{
            HashMap resultValuesMap;
            connRentFur = DbConnectUtil.getConnection();
            
            if(code==null){
                code="";
            }
            
            if(description==null){
                description="";
            }
            
            int roleRows = 0;
            RoleEntry[] roles = null;
            String roleQuery = "SELECT pr.only_query, (SELECT r.description FROM role r WHERE r.id = pr.role_id) as roleName FROM position_role pr WHERE pr.position_id = ?";
            psRole = connRentFur.prepareStatement(roleQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            StringBuilder positionsQuery = new StringBuilder();
            positionsQuery.append("SELECT p.id, p.code, p.description FROM position p WHERE p.code ilike ? AND p.description ilike ?");            
            if(roleId!= null && !roleId.equals(ALL_VALUES)){
                positionsQuery.append(" AND p.id in (SELECT position_id FROM position_role WHERE role_id = ").append(roleId).append(")");
            }
            positionsQuery.append(" ORDER BY p.code, p.description");
            ps = connRentFur.prepareStatement(positionsQuery.toString());
            
            ps.setString(1, "%"+code+"%");
            ps.setString(2, "%"+description+"%");
            rs = ps.executeQuery();
            while(rs.next()){
                resultValuesMap = new HashMap();
                resultValuesMap.put("id", rs.getInt("id"));
                resultValuesMap.put("code", rs.getString("code"));
                resultValuesMap.put("description", rs.getString("description"));
                
                psRole.setInt(1, rs.getInt("id"));
                rsRole = psRole.executeQuery();
                if (rsRole.last()) {
                    roleRows = rsRole.getRow();
                    rsRole.beforeFirst();
                }
                roles = new RoleEntry[roleRows];
                while(rsRole.next()){
                    if(rsRole.getBoolean("only_query")){
                        roles[rsRole.getRow()-1] =  new RoleEntry(rsRole.getString("roleName")+" [Solo Consultas]");
                    }else{
                        roles[rsRole.getRow()-1] =  new RoleEntry(rsRole.getString("roleName"));
                    }
                }
                resultValuesMap.put("roles", roles);
                
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
    
    public ComboBoxItem[] getRoleForComboBox(boolean addAllOption){
        ComboBoxItem[] positions = null;
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        try{
            int rows = 0;
            connRentFur = DbConnectUtil.getConnection();
            String positionString = "SELECT id, description FROM role ORDER BY description";
            
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
    
    public HashMap getPositionById(int positionId){
        HashMap positionMap = new HashMap();
        Connection connRentFur = null;
        PreparedStatement positionPs;
        ResultSet positionRs;
        PreparedStatement rolePs;
        ResultSet roleRs;
        try{
            connRentFur = DbConnectUtil.getConnection();
            
            StringBuilder positionSb = new StringBuilder();
            positionSb.append("SELECT id, code, description FROM position WHERE id = ?");
            positionPs = connRentFur.prepareStatement(positionSb.toString());
            positionPs.setInt(1, positionId);
            
            positionRs = positionPs.executeQuery();
            if(positionRs.next()){
                positionMap.put("id", positionRs.getInt(1));
                positionMap.put("code", positionRs.getString(2));
                positionMap.put("description", positionRs.getString(3));
            }
            
            positionPs.close();
            positionRs.close();
            
            StringBuilder roleSb = new StringBuilder();
            roleSb.append("SELECT (SELECT r.code FROM role r WHERE r.id = pr.role_id) as role, pr.only_query FROM position_role pr WHERE pr.position_id = ?");
            rolePs = connRentFur.prepareStatement(roleSb.toString());
            rolePs.setInt(1, positionId);
            
            roleRs = rolePs.executeQuery();
            while(roleRs.next()){
                positionMap.put(roleRs.getString("role"), roleRs.getBoolean("only_query"));
            }
            
            rolePs.close();
            roleRs.close();

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
        return positionMap;
    }
    
    public HashMap updatePosition(int positionId, HashMap positionMap, String description, boolean provider, boolean providerOnlyQuery,
                                    boolean subject, boolean subjectOnlyQuery, boolean furniture, boolean furnitureOnlyQuery,
                                    boolean users, boolean usersOnlyQuery){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
         try{
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            if(description == null || description.trim().equals("")){
                
                mapToReturn.put("message", "El campo Descripcion no puede quedar vacio.");
                
            }else{
                
                boolean currentProviderValue = positionMap.containsKey(ROLE_RF_PROVIDER);
                if(provider){
                    if(currentProviderValue){
                        if(providerOnlyQuery!=Boolean.valueOf(positionMap.get(ROLE_RF_PROVIDER).toString())){
                            updatePositionOnlyQuery(positionId, ROLE_RF_PROVIDER, providerOnlyQuery);
                        }
                    }else{
                        insertNewPositionRole(positionId, ROLE_RF_PROVIDER, providerOnlyQuery);
                    }
                }else{
                    if(currentProviderValue){
                        deletePositionRoleValue(positionId, ROLE_RF_PROVIDER);
                    }
                }
                
                
                boolean currentSubjectValue = positionMap.containsKey(ROLE_RF_SUBJECT);
                if(subject){
                    if(currentSubjectValue){
                        if(subjectOnlyQuery!=Boolean.valueOf(positionMap.get(ROLE_RF_SUBJECT).toString())){
                            updatePositionOnlyQuery(positionId, ROLE_RF_SUBJECT, subjectOnlyQuery);
                        }
                    }else{
                        insertNewPositionRole(positionId, ROLE_RF_SUBJECT, subjectOnlyQuery);
                    }
                }else{
                    if(currentSubjectValue){
                        deletePositionRoleValue(positionId, ROLE_RF_SUBJECT);
                    }
                }
                
                boolean currentFurnitureValue = positionMap.containsKey(ROLE_RF_FURNITURE);
                if(furniture){
                    if(currentFurnitureValue){
                        if(furnitureOnlyQuery!=Boolean.valueOf(positionMap.get(ROLE_RF_FURNITURE).toString())){
                            updatePositionOnlyQuery(positionId, ROLE_RF_FURNITURE, furnitureOnlyQuery);
                        }
                    }else{
                        insertNewPositionRole(positionId, ROLE_RF_FURNITURE, furnitureOnlyQuery);
                    }
                }else{
                    if(currentFurnitureValue){
                        deletePositionRoleValue(positionId, ROLE_RF_FURNITURE);
                    }
                }
                
                boolean currentUsersValue = positionMap.containsKey(ROLE_RF_USER);
                if(users){
                    if(currentUsersValue){
                        if(usersOnlyQuery!=Boolean.valueOf(positionMap.get(ROLE_RF_USER).toString())){
                            updatePositionOnlyQuery(positionId, ROLE_RF_USER, usersOnlyQuery);
                        }
                    }else{
                        insertNewPositionRole(positionId, ROLE_RF_USER, usersOnlyQuery);
                    }
                }else{
                    if(currentUsersValue){
                        deletePositionRoleValue(positionId, ROLE_RF_USER);
                    }
                }
                
                connRentFur = DbConnectUtil.getConnection();
                
                StringBuilder positionUpdateSb = new StringBuilder();
                positionUpdateSb.append("UPDATE position SET description = ? WHERE id = ?");
                ps = connRentFur.prepareStatement(positionUpdateSb.toString());
                ps.setString(1, description);
                ps.setInt(2, positionId);
                ps.executeUpdate();
                ps.close();
                mapToReturn.put("status", SUCCESFULLY_SAVED);
                mapToReturn.put("message", "Cargo actualizado correctamente");
                
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
    
    public void deletePositionRoleValue(int positionId, String roleCode){        
        Connection connRentFur = null;
        PreparedStatement ps;
         try{   
                connRentFur = DbConnectUtil.getConnection();
                int roleId = getRoleIdByCode(roleCode);
                StringBuilder positionDeleteSb = new StringBuilder();
                positionDeleteSb.append("DELETE FROM position_role WHERE position_id = ? AND role_id = ?");
                
                ps = connRentFur.prepareStatement(positionDeleteSb.toString());
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.executeUpdate();
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
    }
    
    public void updatePositionOnlyQuery(int positionId, String roleCode, boolean onlyQuery){
        Connection connRentFur = null;
        PreparedStatement ps;
         try{   
                connRentFur = DbConnectUtil.getConnection();
                int roleId = getRoleIdByCode(roleCode);
                StringBuilder positionDeleteSb = new StringBuilder();
                positionDeleteSb.append("UPDATE position_role set only_query = ? WHERE position_id = ? AND role_id = ?");
                
                ps = connRentFur.prepareStatement(positionDeleteSb.toString());
                ps.setBoolean(1, onlyQuery);
                ps.setInt(2, positionId);
                ps.setInt(3, roleId);
                ps.executeUpdate();
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
    }
    
    public void insertNewPositionRole(int positionId, String roleCode, boolean onlyQuery){
        Connection connRentFur = null;
        PreparedStatement ps;
         try{   
                connRentFur = DbConnectUtil.getConnection();
                int roleId = getRoleIdByCode(roleCode);
                StringBuilder positionDeleteSb = new StringBuilder();
                positionDeleteSb.append("INSERT INTO position_role (position_id, role_id, only_query) VALUES (?, ?, ?)");
                ps = connRentFur.prepareStatement(positionDeleteSb.toString());
                ps.setInt(1, positionId);
                ps.setInt(2, roleId);
                ps.setBoolean(3, onlyQuery);
                ps.executeUpdate();
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
    }
}
