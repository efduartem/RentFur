/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.event;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.sasl.Sasl;
import rentfur.furniture.FurnitureController;
import rentfur.furnitureMovement.FurnitureMovementController;
import rentfur.subjectMovement.SubjectMovementController;
import rentfur.util.ComboBoxItem;
import rentfur.util.DbConnectUtil;
import rentfur.util.MainWindowController;
import rentfur.util.User;
import rentfur.util.UserRoles;

/**
 *
 * @author FDuarte
 */
public class EventController {
    private MainWindowController mainWindowController;
    public static final int BUDGETED = 0;
    public static final int CONFIRMED = 1;
    public static final int CANCELED = 2;
    public static final int FINISHED = 3;
    
    public static final int SUCCESFULLY_SAVED = 0;
    public static final int ERROR_IN_SAVED = 1;
    
    public static final String ALL_VALUES = "Todos";
    private EventIndex eventIndex;
    private EventCreate eventCreate;
    private EventShowAndEdit eventShowAndEdit;
    private EventEdit eventEdit;
    
    public EventIndex getEventIndex(MainWindowController mainWindowController){
        if(eventIndex == null){
            eventIndex = new EventIndex(this);
        }
        this.mainWindowController = mainWindowController;
        return eventIndex;
    }
    
    public EventCreate getEventCreate(){
        if(eventCreate == null){
            eventCreate = new EventCreate(this);
        }
        return eventCreate;
    }
    
    public EventEdit getEventEdit(int eventId){
        if(eventEdit == null){
            eventEdit = new EventEdit(this, eventId);
        }
        return eventEdit;
    }
    
    public void eventIndexClosed(){
        eventIndex = null;
    }
    
    public void eventEditClosed(){
        eventEdit = null;
    }
    
    public void eventCreateClosed(){
        eventCreate = null;
    }
    
    public void eventShowAndEditClosed(){
        eventShowAndEdit = null;
    }
    
    public EventShowAndEdit getEventShowAndEdit(int eventId){
        if(eventShowAndEdit == null){
            eventShowAndEdit = new EventShowAndEdit(this, eventId);
        }
        return eventShowAndEdit;
    }
    
    public void setEventShowAndEditInMainWindow(int eventId){
        mainWindowController.setVisibleEventShowAndEditInternalFrame(eventId);
    }
    
    public static ComboBoxItem[] getEventStatusAvailablesForCreateEvent(boolean addAllOption){
        
        ComboBoxItem[] statusAvailables = null;
        try{
            
            if(addAllOption){
                statusAvailables = new ComboBoxItem[3];
                
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(ALL_VALUES);
                statusAvailables[0].setValue(ALL_VALUES);
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(BUDGETED));
                statusAvailables[1].setValue(getEventStatus(BUDGETED));
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(CONFIRMED));
                statusAvailables[2].setValue(getEventStatus(CONFIRMED));
                
            }else{
                statusAvailables = new ComboBoxItem[2];
                         
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(String.valueOf(BUDGETED));
                statusAvailables[0].setValue(getEventStatus(BUDGETED));
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(CONFIRMED));
                statusAvailables[1].setValue(getEventStatus(CONFIRMED));
                
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return statusAvailables;
    }
    
    public static ComboBoxItem[] getEventStatusAvailablesForShowAndEditEvent(boolean addAllOption, HashMap eventMap){
        
        ComboBoxItem[] statusAvailables = null;
        try{
            
            if(addAllOption){
                statusAvailables = new ComboBoxItem[3];
                
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(ALL_VALUES);
                statusAvailables[0].setValue(ALL_VALUES);
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(CANCELED));
                statusAvailables[1].setValue(getEventStatus(CANCELED));
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(CONFIRMED));
                statusAvailables[2].setValue(getEventStatus(CONFIRMED));
                
            }else if(!((ArrayList) eventMap.get("penaltyDetail")).isEmpty()){
                statusAvailables = new ComboBoxItem[2];
                
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(String.valueOf(CONFIRMED));
                statusAvailables[0].setValue(getEventStatus(CONFIRMED));
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(FINISHED));
                statusAvailables[1].setValue(getEventStatus(FINISHED));
            }else{
                statusAvailables = new ComboBoxItem[3];
                         
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(String.valueOf(CANCELED));
                statusAvailables[0].setValue(getEventStatus(CANCELED));
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(CONFIRMED));
                statusAvailables[1].setValue(getEventStatus(CONFIRMED));
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(FINISHED));
                statusAvailables[2].setValue(getEventStatus(FINISHED));
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return statusAvailables;
    }
    
    public HashMap createEvent(HashMap subjectMap, Date deliveryDate, int status, String placeOfDelivery, ArrayList furnitureList, double netTotal, double totalTax, double totalTax5, double totalTax10, double totalTaxable5, double totalTaxable10, double totalTaxable, String observation){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psContractNumber = null;
        ResultSet rs;
        ResultSet rsContractNumber = null;
        int eventId = 0;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            String contractNumberNextVal;
            int contractNumber = 0;
            
            StringBuilder eventInsertSb = new StringBuilder();
            eventInsertSb.append("INSERT INTO event(id, number, creation_date, delivery_date, pickup_date, status, place_of_delivery, observation, contract_number, net_total, total_tax_5, total_tax_10, total_tax, total_taxable_5, total_taxable_10, total_taxable, subject_code, subject_name, subject_address, subject_fiscal_number, subject_telephone, subject_city, subject_tradename, creator_user_id, last_modification_user_id, last_modification_date, balance, billable_balance)");
            eventInsertSb.append(" VALUES (nextval('event_seq'), nextval('event_number_seq'), current_timestamp, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?)");

            ps = connRentFur.prepareStatement(eventInsertSb.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, new java.sql.Date (deliveryDate.getTime())); //Fecha de Entrega
            ps.setDate(2, new java.sql.Date (deliveryDate.getTime()));//Fecha de Recoleccion
            ps.setInt(3, status); //Estado
            ps.setString(4, placeOfDelivery); //Lugar de Entrega
            ps.setString(5, observation);
            if(status == BUDGETED){
                //Si no esta confirmado no es guardado nada como numero de contrato
                ps.setNull(6, java.sql.Types.INTEGER);
            }else{
                //Si ya esta confirmado se obtiene el numero de contrato para guardarlo
                contractNumberNextVal = "SELECt nextval('contract_number_seq')";
                psContractNumber = connRentFur.prepareStatement(contractNumberNextVal);
                rsContractNumber = psContractNumber.executeQuery();
                if(rsContractNumber.next()){
                   contractNumber = rsContractNumber.getInt(1);
                }
                ps.setInt(6, contractNumber);
            }
            ps.setDouble(7, netTotal);
            ps.setDouble(8, totalTax5);
            ps.setDouble(9, totalTax10);
            ps.setDouble(10, totalTax);
            ps.setDouble(11, totalTaxable5);
            ps.setDouble(12, totalTaxable10);
            ps.setDouble(13, totalTaxable);
            ps.setString(14, subjectMap.get("code").toString());
            ps.setString(15, subjectMap.get("name").toString());
            ps.setString(16, subjectMap.get("address").toString());
            ps.setString(17, subjectMap.get("fiscalNumber").toString());
            ps.setString(18, subjectMap.get("telephone").toString());
            ps.setString(19, subjectMap.get("city").toString());
            ps.setString(20, subjectMap.get("tradename").toString());
            ps.setInt(21, loggedUser.getId());
            ps.setInt(22, loggedUser.getId());
            ps.setDouble(23, netTotal);
            ps.setDouble(24, netTotal);
            ps.executeUpdate();
            
            rs = ps.getGeneratedKeys();
            while(rs != null && rs.next()){
                eventId = rs.getInt(1);
            }
            
            StringBuilder eventDetailInsertSb = new StringBuilder();
            HashMap furnitureMap = new HashMap();
            eventDetailInsertSb.append("INSERT INTO event_detail(id, furniture_code, furniture_description, unit_price, fine_amount_per_unit, quantity, total_amount, tax_amount_5, tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_amount, observation, penalty, billable, annexed, event_id) VALUES (nextval('event_detail_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps = connRentFur.prepareStatement(eventDetailInsertSb.toString());
            
            for(int i = 0 ; i < furnitureList.size(); i++){
                furnitureMap = (HashMap) furnitureList.get(i);
                furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                ps.setString(1, furnitureMap.get("code").toString());
                ps.setString(2, furnitureMap.get("description").toString());
                ps.setDouble(3, Double.valueOf(furnitureMap.get("unitPrice").toString()));
                ps.setDouble(4, Double.valueOf(furnitureMap.get("fineAmountPerUnit").toString()));
                ps.setInt(5, Integer.valueOf(furnitureMap.get("quantity").toString()));
                ps.setDouble(6, Double.valueOf(furnitureMap.get("subTotal").toString()));
                ps.setDouble(7, Double.valueOf(furnitureMap.get("tax5").toString()));
                ps.setDouble(8, Double.valueOf(furnitureMap.get("tax10").toString()));
                if(Double.valueOf(furnitureMap.get("taxRate").toString())==5){
                    //tax_amount
                    ps.setDouble(9, Double.valueOf(furnitureMap.get("tax5").toString()));
                    
                    //taxable_amount_5
                    ps.setDouble(10, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                    //taxable_amount_10
                    ps.setDouble(11, Double.valueOf("0"));
                    
                    //taxable_amount
                    ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                }else if(Double.valueOf(furnitureMap.get("taxRate").toString())==10){
                    //tax_amount
                    ps.setDouble(9, Double.valueOf(furnitureMap.get("tax10").toString()));
                    
                    //taxable_amount_5
                    ps.setDouble(10, Double.valueOf("0"));
                    
                    //taxable_amount_10
                    ps.setDouble(11, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                    //taxable_amount
                    ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                }else{
                    ps.setDouble(9, Double.valueOf("0"));
                    ps.setDouble(10, Double.valueOf("0"));
                    ps.setDouble(11, Double.valueOf("0"));
                    ps.setDouble(12, Double.valueOf("0"));
                }
                
                ps.setString(13, "");
                ps.setBoolean(14, Boolean.FALSE);
                ps.setBoolean(15, Boolean.TRUE);
                ps.setBoolean(16, Boolean.FALSE);
                ps.setInt(17, eventId);
                ps.addBatch();
                
                if(status == CONFIRMED){
                    //Actualizar stock para el dia 
                    updateDayStock(furnitureMap.get("id").toString(), deliveryDate, furnitureMap.get("quantity").toString());
                }
            }
            ps.executeBatch();
            ps.clearBatch();
            
            if(status == CONFIRMED){
                //Registrar movimiento de debito
                String movement = "Contrato Nro. "+contractNumber;
                SubjectMovementController.updateEventBalance(SubjectMovementController.DEBIT_MOVEMENT_TYPE, SubjectMovementController.CONTRACT_MOVEMENT_DOCUMENT_TYPE, String.valueOf(contractNumber), netTotal, subjectMap.get("code").toString(), movement);
            }
            
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            if(psContractNumber!=null){
                psContractNumber.close();
            }
            
            if(rsContractNumber!=null){
                rsContractNumber.close();
            }
            
            mapToReturn.put("id", eventId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Evento registrado correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public HashMap updateEventBudgeted(HashMap subjectMap, Date deliveryDate, int status, String placeOfDelivery, ArrayList furnitureList, double netTotal, double totalTax, double totalTax5, double totalTax10, double totalTaxable5, double totalTaxable10, double totalTaxable, String observation, int eventId){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        PreparedStatement psContractNumber = null;
        ResultSet rs = null;
        ResultSet rsContractNumber = null;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            String contractNumberNextVal;
            int contractNumber = 0;
            
            StringBuilder eventInsertSb = new StringBuilder();
            eventInsertSb.append("UPDATE event SET delivery_date=?, pickup_date=?, status=?, place_of_delivery=?, observation=?, net_total=?, total_tax_5=?, total_tax_10=?, total_tax=?, total_taxable_5=?, total_taxable_10=?, total_taxable=?, subject_code=?, subject_name=?, subject_address=?, subject_fiscal_number=?, subject_telephone=?, subject_city=?, subject_tradename=?, last_modification_date=current_timestamp, last_modification_user_id=?, balance=?, contract_number = ?, billable_balance = ? WHERE id = ?");

            ps = connRentFur.prepareStatement(eventInsertSb.toString());
            ps.setDate(1, new java.sql.Date (deliveryDate.getTime())); //Fecha de Entrega
            ps.setDate(2, new java.sql.Date (deliveryDate.getTime()));//Fecha de Recoleccion
            ps.setInt(3, status); //Estado
            ps.setString(4, placeOfDelivery); //Lugar de Entrega
            ps.setString(5, observation);
            ps.setDouble(6, netTotal);
            ps.setDouble(7, totalTax5);
            ps.setDouble(8, totalTax10);
            ps.setDouble(9, totalTax);
            ps.setDouble(10, totalTaxable5);
            ps.setDouble(11, totalTaxable10);
            ps.setDouble(12, totalTaxable);
            ps.setString(13, subjectMap.get("code").toString());
            ps.setString(14, subjectMap.get("name").toString());
            ps.setString(15, subjectMap.get("address").toString());
            ps.setString(16, subjectMap.get("fiscalNumber").toString());
            ps.setString(17, subjectMap.get("telephone").toString());
            ps.setString(18, subjectMap.get("city").toString());
            ps.setString(19, subjectMap.get("tradename").toString());
            ps.setInt(20, loggedUser.getId());
            ps.setDouble(21, netTotal);
            if(status == BUDGETED){
                //Si no esta confirmado no es guardado nada como numero de contrato
                ps.setNull(22, java.sql.Types.INTEGER);
            }else{
                //Si ya esta confirmado se obtiene el numero de contrato para guardarlo
                contractNumberNextVal = "SELECt nextval('contract_number_seq')";
                psContractNumber = connRentFur.prepareStatement(contractNumberNextVal);
                rsContractNumber = psContractNumber.executeQuery();
                if(rsContractNumber.next()){
                   contractNumber = rsContractNumber.getInt(1);
                }
                ps.setInt(22, contractNumber);
            }
            ps.setDouble(23, netTotal);
            ps.setInt(24, eventId);
            ps.executeUpdate();
            
            String deleteDetails = "DELETE FROM event_detail WHERE event_id = ?";
            ps = connRentFur.prepareStatement(deleteDetails);
            ps.setInt(1, eventId);
            ps.executeUpdate();
            connRentFur.commit();
            
            StringBuilder eventDetailInsertSb = new StringBuilder();
            HashMap furnitureMap = new HashMap();
            eventDetailInsertSb.append("INSERT INTO event_detail(id, furniture_code, furniture_description, unit_price, fine_amount_per_unit, quantity, total_amount, tax_amount_5, tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_amount, observation, penalty, billable, annexed, event_id) VALUES (nextval('event_detail_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps = connRentFur.prepareStatement(eventDetailInsertSb.toString());
            
            for(int i = 0 ; i < furnitureList.size(); i++){
                furnitureMap = (HashMap) furnitureList.get(i);
                furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                ps.setString(1, furnitureMap.get("code").toString());
                ps.setString(2, furnitureMap.get("description").toString());
                ps.setDouble(3, Double.valueOf(furnitureMap.get("unitPrice").toString()));
                ps.setDouble(4, Double.valueOf(furnitureMap.get("fineAmountPerUnit").toString()));
                ps.setInt(5, Integer.valueOf(furnitureMap.get("quantity").toString()));
                ps.setDouble(6, Double.valueOf(furnitureMap.get("subTotal").toString()));
                ps.setDouble(7, Double.valueOf(furnitureMap.get("tax5").toString()));
                ps.setDouble(8, Double.valueOf(furnitureMap.get("tax10").toString()));
                if(Double.valueOf(furnitureMap.get("taxRate").toString())==5){
                    //tax_amount
                    ps.setDouble(9, Double.valueOf(furnitureMap.get("tax5").toString()));
                    
                    //taxable_amount_5
                    ps.setDouble(10, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                    //taxable_amount_10
                    ps.setDouble(11, Double.valueOf("0"));
                    
                    //taxable_amount
                    ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                }else if(Double.valueOf(furnitureMap.get("taxRate").toString())==10){
                    //tax_amount
                    ps.setDouble(9, Double.valueOf(furnitureMap.get("tax10").toString()));
                    
                    //taxable_amount_5
                    ps.setDouble(10, Double.valueOf("0"));
                    
                    //taxable_amount_10
                    ps.setDouble(11, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                    //taxable_amount
                    ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                }else{
                    ps.setDouble(9, Double.valueOf("0"));
                    ps.setDouble(10, Double.valueOf("0"));
                    ps.setDouble(11, Double.valueOf("0"));
                    ps.setDouble(12, Double.valueOf("0"));
                }
                
                ps.setString(13, "");
                ps.setBoolean(14, Boolean.FALSE);
                ps.setBoolean(15, Boolean.TRUE);
                ps.setBoolean(16, Boolean.FALSE);
                ps.setInt(17, eventId);
                ps.addBatch();
                
                if(status == CONFIRMED){
                    //Actualizar stock para el dia 
                    updateDayStock(furnitureMap.get("id").toString(), deliveryDate, furnitureMap.get("quantity").toString());
                }
            }
            ps.executeBatch();
            ps.clearBatch();
            
            if(status == CONFIRMED){
                //Registrar movimiento de debito
                String movement = "Contrato Nro. "+contractNumber;
                SubjectMovementController.updateEventBalance(SubjectMovementController.DEBIT_MOVEMENT_TYPE, SubjectMovementController.CONTRACT_MOVEMENT_DOCUMENT_TYPE, String.valueOf(contractNumber), netTotal, subjectMap.get("code").toString(), movement);
            }
            connRentFur.commit();
            ps.close();
            
            if(rs!=null){
                rs.close();
            }
            
            if(psContractNumber!=null){
                psContractNumber.close();
            }
            
            if(rsContractNumber!=null){
                rsContractNumber.close();
            }
            
            mapToReturn.put("id", eventId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            mapToReturn.put("message", "Presupuesto actualizado correctamente");
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public HashMap updateEventConfirmed(HashMap subjectMap, HashMap eventMap, int status, String placeOfDelivery, String observation, ArrayList furnitureList, ArrayList penaltyFurnitureList, double netTotal, double balanceTotal, double billableBalanceTotal){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int eventId = 0;
        int contractNumber = 0;
        ArrayList furnitureDetailList;
        HashMap furnitureMap;
        Date deliveryDate;
        try{
            eventId = (Integer) eventMap.get("id");
            deliveryDate = new Date(((Timestamp)eventMap.get("deliveryDate")).getTime());
            contractNumber = (Integer) eventMap.get("contractNumber");
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");
            
            StringBuilder eventUpdateSb = new StringBuilder();
            
            double tax;
            
            
            if(status == CANCELED){
                eventUpdateSb.append("UPDATE event SET status = ?, balance = 0, last_modification_user_id = ?, last_modification_date = current_timestamp WHERE id = ?");
                ps = connRentFur.prepareStatement(eventUpdateSb.toString());
                ps.setInt(1, status); //Estado
                ps.setInt(2, loggedUser.getId()); //Usuario
                ps.setInt(3, eventId); 
                ps.executeUpdate();
                
                furnitureDetailList = (ArrayList) eventMap.get("detail");
                for(int i = 0 ; i < furnitureDetailList.size(); i++){
                    furnitureMap = (HashMap) furnitureDetailList.get(i);
                    furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                    updateDayStockReturn(furnitureMap.get("id").toString(), deliveryDate, furnitureMap.get("quantity").toString());
                }
                mapToReturn.put("message", "Evento cancelado correctamente");
            }else{
                double totalTax = 0;
                double totalTax5 = 0;
                double totalTax10 = 0;
                double totalTaxable5 = 0;
                double totalTaxable10 = 0;
                double totalTaxable = 0;
                
                double totalAnnexed = 0;
                double totalPenalty = 0;
                
                StringBuilder eventDetailInsertSb = new StringBuilder();
                eventDetailInsertSb.append("INSERT INTO event_detail(id, furniture_code, furniture_description, unit_price, fine_amount_per_unit, quantity, total_amount, tax_amount_5, tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_amount, observation, penalty, billable, annexed, event_id) VALUES (nextval('event_detail_seq'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                ps = connRentFur.prepareStatement(eventDetailInsertSb.toString());
                for(int i = 0 ; i < furnitureList.size(); i++){
                    furnitureMap = (HashMap) furnitureList.get(i);
                    furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                    ps.setString(1, furnitureMap.get("code").toString());
                    ps.setString(2, furnitureMap.get("description").toString());
                    ps.setDouble(3, Double.valueOf(furnitureMap.get("unitPrice").toString()));
                    ps.setDouble(4, Double.valueOf(furnitureMap.get("fineAmountPerUnit").toString()));
                    ps.setInt(5, Integer.valueOf(furnitureMap.get("quantity").toString()));
                    ps.setDouble(6, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                    totalAnnexed = totalAnnexed + Double.valueOf(furnitureMap.get("subTotal").toString());
                            
                    if(Double.valueOf(furnitureMap.get("taxRate").toString())==5){
                        tax = new BigDecimal(Double.valueOf(furnitureMap.get("subTotal").toString()) / 21).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        totalTax += tax;
                        totalTax5 += tax;
                        totalTaxable5 += Double.valueOf(furnitureMap.get("subTotal").toString());
                        totalTaxable += Double.valueOf(furnitureMap.get("subTotal").toString());
                        
                        ps.setDouble(7, tax);
                        ps.setDouble(8, Double.valueOf("0"));
                        //tax_amount
                        ps.setDouble(9, tax);

                        //taxable_amount_5
                        ps.setDouble(10, Double.valueOf(furnitureMap.get("subTotal").toString()));

                        //taxable_amount_10
                        ps.setDouble(11, Double.valueOf("0"));

                        //taxable_amount
                        ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));

                    }else if(Double.valueOf(furnitureMap.get("taxRate").toString())==10){
                        tax = new BigDecimal(Double.valueOf(furnitureMap.get("subTotal").toString()) / 11).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        totalTax += tax;
                        totalTax10 += tax;
                        totalTaxable10 += Double.valueOf(furnitureMap.get("subTotal").toString());
                        totalTaxable += Double.valueOf(furnitureMap.get("subTotal").toString());
                        
                        ps.setDouble(7, Double.valueOf("0"));
                        ps.setDouble(8, tax);
                        
                        //tax_amount
                        ps.setDouble(9, tax);

                        //taxable_amount_5
                        ps.setDouble(10, Double.valueOf("0"));

                        //taxable_amount_10
                        ps.setDouble(11, Double.valueOf(furnitureMap.get("subTotal").toString()));

                        //taxable_amount
                        ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));

                    }else{
                        ps.setDouble(7, Double.valueOf("0"));
                        ps.setDouble(8, Double.valueOf("0"));
                        ps.setDouble(9, Double.valueOf("0"));
                        ps.setDouble(10, Double.valueOf("0"));
                        ps.setDouble(11, Double.valueOf("0"));
                        ps.setDouble(12, Double.valueOf("0"));
                    }

                    ps.setString(13, "");
                    ps.setBoolean(14, Boolean.FALSE);
                    ps.setBoolean(15, Boolean.TRUE);
                    ps.setBoolean(16, Boolean.TRUE);
                    ps.setInt(17, eventId);
                    ps.addBatch();
                    
                    //Actualizar stock para el dia 
                    updateDayStock(furnitureMap.get("id").toString(), deliveryDate, furnitureMap.get("quantity").toString());

                }
                
                HashMap movementDetailMap;
                ArrayList movementDetailList = new ArrayList();
                double outputMovementImport = 0;
                        
                for(int i = 0 ; i < penaltyFurnitureList.size(); i++){
                    furnitureMap = (HashMap) penaltyFurnitureList.get(i);
                    furnitureMap = deepMerge(furnitureMap, FurnitureController.getFurnitureByCode(furnitureMap.get("code").toString()));
                    
                    ps.setString(1, furnitureMap.get("code").toString());
                    ps.setString(2, furnitureMap.get("description").toString());
                    ps.setDouble(3, Double.valueOf(furnitureMap.get("unitPrice").toString()));
                    ps.setDouble(4, Double.valueOf(furnitureMap.get("fineAmountPerUnit").toString()));
                    ps.setInt(5, Integer.valueOf(furnitureMap.get("quantity").toString()));
                    ps.setDouble(6, Double.valueOf(furnitureMap.get("subTotal").toString()));
                    
                    totalPenalty = totalPenalty + Double.valueOf(furnitureMap.get("subTotal").toString());
                    
                    if(Double.valueOf(furnitureMap.get("taxRate").toString())==5){
                        tax = new BigDecimal(Double.valueOf(furnitureMap.get("subTotal").toString()) / 21).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        totalTax += tax;
                        totalTax5 += tax;
                        totalTaxable5 += Double.valueOf(furnitureMap.get("subTotal").toString());
                        totalTaxable += Double.valueOf(furnitureMap.get("subTotal").toString());
                        
                        ps.setDouble(7, tax);
                        ps.setDouble(8, Double.valueOf("0"));
                        //tax_amount
                        ps.setDouble(9, tax);

                        //taxable_amount_5
                        ps.setDouble(10, Double.valueOf(furnitureMap.get("subTotal").toString()));

                        //taxable_amount_10
                        ps.setDouble(11, Double.valueOf("0"));

                        //taxable_amount
                        ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));

                    }else if(Double.valueOf(furnitureMap.get("taxRate").toString())==10){
                        tax = new BigDecimal(Double.valueOf(furnitureMap.get("subTotal").toString()) / 11).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        totalTax += tax;
                        totalTax10 += tax;
                        totalTaxable10 += Double.valueOf(furnitureMap.get("subTotal").toString());
                        totalTaxable += Double.valueOf(furnitureMap.get("subTotal").toString());
                        
                        ps.setDouble(7, Double.valueOf("0"));
                        ps.setDouble(8, tax);
                        
                        //tax_amount
                        ps.setDouble(9, tax);

                        //taxable_amount_5
                        ps.setDouble(10, Double.valueOf("0"));

                        //taxable_amount_10
                        ps.setDouble(11, Double.valueOf(furnitureMap.get("subTotal").toString()));

                        //taxable_amount
                        ps.setDouble(12, Double.valueOf(furnitureMap.get("subTotal").toString()));

                    }else{
                        ps.setDouble(7, Double.valueOf("0"));
                        ps.setDouble(8, Double.valueOf("0"));
                        ps.setDouble(9, Double.valueOf("0"));
                        ps.setDouble(10, Double.valueOf("0"));
                        ps.setDouble(11, Double.valueOf("0"));
                        ps.setDouble(12, Double.valueOf("0"));
                    }

                    ps.setString(13, "");
                    ps.setBoolean(14, Boolean.TRUE);
                    ps.setBoolean(15, Boolean.TRUE);
                    ps.setBoolean(16, Boolean.TRUE);
                    ps.setInt(17, eventId);
                    ps.addBatch();
                    
                    movementDetailMap = new HashMap();
                    movementDetailMap.put("furnitureId", Integer.valueOf(furnitureMap.get("id").toString()));
                    movementDetailMap.put("code", furnitureMap.get("code").toString());
                    movementDetailMap.put("description", furnitureMap.get("description").toString());
                    movementDetailMap.put("quantity", Integer.valueOf(furnitureMap.get("quantity").toString()));
                    movementDetailMap.put("cost", Double.valueOf(furnitureMap.get("unitCostPrice").toString()));
                    outputMovementImport = Double.valueOf(furnitureMap.get("unitCostPrice").toString()) * Double.valueOf(furnitureMap.get("quantity").toString());
                    movementDetailMap.put("import", outputMovementImport);
                    movementDetailList.add(movementDetailMap);
                    
                    //Actualizar stock para el dia 
                    updateStockByPenalty(furnitureMap.get("id").toString(), furnitureMap.get("quantity").toString());
                    
                }
                
                ps.executeBatch();
                ps.clearBatch();
                
                //Movimiento de baja de mobiliarios
                FurnitureMovementController.outputMovementRecord(movementDetailList, FurnitureMovementController.MOVEMENT_CONCEPT_DAMAGED_PENALTY, new Date());
                
                //REGISTRAR MOVIMIENTOS DE DEBITO
                String movement;
                if(totalAnnexed > 0){
                    movement = "Anexos - Cargos - Contrato Nro. "+contractNumber;
                    SubjectMovementController.updateEventBalance(SubjectMovementController.DEBIT_MOVEMENT_TYPE, SubjectMovementController.CONTRACT_ANEXED_MOVEMENT_TYPE, String.valueOf(contractNumber), totalAnnexed, subjectMap.get("code").toString(), movement);
                }
                
                if(totalPenalty > 0){
                    movement = "Multa - Contrato Nro. "+contractNumber;
                    SubjectMovementController.updateEventBalance(SubjectMovementController.DEBIT_MOVEMENT_TYPE, SubjectMovementController.PENALTY_MOVEMENT_TYPE, String.valueOf(contractNumber), totalPenalty, subjectMap.get("code").toString(), movement);
                }
                
                //ACTUALIZAR DATOS GENERALES DEL EVENTO
                eventUpdateSb.append("UPDATE event SET balance = ?, net_total = ?, total_tax_5 = (total_tax_5 + ?), total_tax_10 = (total_tax_10 + ?), total_tax = (total_tax + ?), total_taxable_5 = (total_taxable_5 + ?), total_taxable_10 = (total_taxable_10 + ?), total_taxable = (total_taxable + ?), last_modification_user_id = ?, place_of_delivery = ?, observation = ? , last_modification_date = current_timestamp, billable_balance = ? WHERE id = ?");
                ps = connRentFur.prepareStatement(eventUpdateSb.toString());
                ps.setDouble(1, balanceTotal); 
                ps.setDouble(2, netTotal);
                ps.setDouble(3, totalTax5);
                ps.setDouble(4, totalTax10);
                ps.setDouble(5, totalTax);
                ps.setDouble(6, totalTaxable5);
                ps.setDouble(7, totalTaxable10);
                ps.setDouble(8, totalTaxable);
                ps.setInt(9, loggedUser.getId()); //Usuario
                ps.setString(10, placeOfDelivery);
                ps.setString(11, observation);
                ps.setDouble(12, billableBalanceTotal);
                ps.setInt(13, eventId);
                ps.executeUpdate();
                
                mapToReturn.put("message", "Evento actualizado correctamente");
            }   
            
            connRentFur.commit();
            
            if(ps!=null){
                ps.close();
            }
            
            if(rs!=null){
                rs.close();
            }

            mapToReturn.put("id", eventId);
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    
    public static HashMap deepMerge(HashMap original, HashMap newMap) {
        for (Object key : newMap.keySet()) {
            if (newMap.get(key) instanceof HashMap && original.get(key) instanceof HashMap) {
                HashMap originalChild = (HashMap) original.get(key);
                HashMap newChild = (HashMap) newMap.get(key);
                original.put(key, deepMerge(originalChild, newChild));
            } else {
                original.put(key, newMap.get(key));
            }
        }
        return original;
    }
    
    public void updateDayStock(String id, Date deliveryDate, String quantity){
        
        Connection connRentFur = null;
        PreparedStatement ps;

        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder furnitureStockUpdateSb = new StringBuilder();
            furnitureStockUpdateSb.append("UPDATE furniture_stock SET stock_available = (stock_available - ?), stock_committed = (stock_committed + ?) WHERE furniture_id = ? AND day = ?");

            ps = connRentFur.prepareStatement(furnitureStockUpdateSb.toString());
            ps.setInt(1, Integer.valueOf(quantity));
            ps.setInt(2, Integer.valueOf(quantity));
            ps.setInt(3, Integer.valueOf(id));
            ps.setDate(4, new java.sql.Date (deliveryDate.getTime())); //Fecha de Entrega
            ps.executeUpdate();
            connRentFur.commit();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public void updateStockByPenalty(String id, String quantity){
        
        Connection connRentFur = null;
        PreparedStatement ps;

        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder furnitureStockUpdateSb = new StringBuilder();
            furnitureStockUpdateSb.append("UPDATE furniture_stock SET stock_available = (stock_available - ?), stock_total = (stock_total - ?) WHERE furniture_id = ? AND day >= current_date");

            ps = connRentFur.prepareStatement(furnitureStockUpdateSb.toString());
            ps.setInt(1, Integer.valueOf(quantity));
            ps.setInt(2, Integer.valueOf(quantity));
            ps.setInt(3, Integer.valueOf(id));
            ps.executeUpdate();
            
            furnitureStockUpdateSb = new StringBuilder();
            furnitureStockUpdateSb.append("UPDATE furniture SET total_stock = (total_stock - ?) WHERE id = ?");
            ps = connRentFur.prepareStatement(furnitureStockUpdateSb.toString());
            ps.setInt(1, Integer.valueOf(quantity));
            ps.setInt(2, Integer.valueOf(id));
            ps.executeUpdate();
            
            connRentFur.commit();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public static void updateEventBillableBalance(Integer eventId, double netTotal){
        
        Connection connRentFur = null;
        PreparedStatement ps;

        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder eventBillableBalanceUpdateSb = new StringBuilder();
            eventBillableBalanceUpdateSb.append("UPDATE event SET billable_balance = (billable_balance - ?) WHERE id = ?");

            ps = connRentFur.prepareStatement(eventBillableBalanceUpdateSb.toString());
            ps.setDouble(1, netTotal);
            ps.setInt(2, eventId);
            ps.executeUpdate();
            
            connRentFur.commit();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public void updateDayStockReturn(String id, Date deliveryDate, String quantity){
        
        Connection connRentFur = null;
        PreparedStatement ps;

        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder furnitureStockUpdateSb = new StringBuilder();
            furnitureStockUpdateSb.append("UPDATE furniture_stock SET stock_available = (stock_available + ?), stock_committed = (stock_committed - ?) WHERE furniture_id = ? AND day = ?");

            ps = connRentFur.prepareStatement(furnitureStockUpdateSb.toString());
            ps.setInt(1, Integer.valueOf(quantity));
            ps.setInt(2, Integer.valueOf(quantity));
            ps.setInt(3, Integer.valueOf(id));
            ps.setDate(4, new java.sql.Date (deliveryDate.getTime())); //Fecha de Entrega
            ps.executeUpdate();
            connRentFur.commit();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public static HashMap getEventById(int eventId){
        
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        HashMap eventMap = new HashMap();    
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder eventHeaderSelectSb = new StringBuilder();
            eventHeaderSelectSb.append("SELECT id, \"number\" as eventNumber, creation_date, delivery_date, pickup_date, status, place_of_delivery, observation, contract_number, net_total, total_tax_5, total_tax_10, total_tax, total_taxable_5, total_taxable_10, total_taxable, subject_code, subject_name, subject_address, subject_fiscal_number, subject_telephone, subject_city, subject_tradename, creator_user_id, last_modification_date, last_modification_user_id, balance, detailed_invoice, billable_balance FROM event WHERE id = ?");

            ps = connRentFur.prepareStatement(eventHeaderSelectSb.toString());
            ps.setInt(1, eventId);
            
            rs = ps.executeQuery();
            if(rs.next()){
                eventMap.put("id", rs.getInt("id"));
                eventMap.put("number", rs.getInt("eventNumber"));
                eventMap.put("creationDate", rs.getTimestamp("creation_date"));
                eventMap.put("deliveryDate", rs.getTimestamp("delivery_date"));
                eventMap.put("pickupDate", rs.getTimestamp("pickup_date"));
                eventMap.put("status", rs.getInt("status"));
                eventMap.put("placeOfDelivery", rs.getString("place_of_delivery"));
                eventMap.put("observation", rs.getString("observation"));
                eventMap.put("contractNumber", rs.getInt("contract_number"));
                eventMap.put("netTotal", rs.getDouble("net_total"));
                eventMap.put("totalTax5", rs.getDouble("total_tax_5"));
                eventMap.put("totalTax10", rs.getDouble("total_tax_10"));
                eventMap.put("totalTax", rs.getDouble("total_tax"));
                eventMap.put("totalTaxable5", rs.getDouble("total_taxable_5"));
                eventMap.put("totalTaxable10", rs.getDouble("total_taxable_10"));
                eventMap.put("totalTaxable", rs.getDouble("total_taxable"));
                eventMap.put("subjectCode", rs.getString("subject_code"));
                eventMap.put("creatorUserId", rs.getInt("creator_user_id"));
                eventMap.put("lastModificationUserId", rs.getString("last_modification_user_id"));
                eventMap.put("lastModificationDate", rs.getTimestamp("last_modification_date"));
                eventMap.put("balance", rs.getDouble("balance"));
                eventMap.put("detailedInvoice", rs.getBoolean("detailed_invoice"));
                eventMap.put("detailedInvoiceString", rs.getString("detailed_invoice"));
                eventMap.put("billableBalance", rs.getDouble("billable_balance"));
            }
            
            StringBuilder eventDetailSelectSb = new StringBuilder();
            eventDetailSelectSb.append("SELECT id, furniture_code, furniture_description, unit_price, fine_amount_per_unit, quantity, total_amount, tax_amount_5, tax_amount_10, tax_amount, taxable_amount_5, taxable_amount_10, taxable_amount, observation, penalty, billable, annexed FROM event_detail WHERE event_id = ? ORDER BY id ASC");
            ArrayList eventDetailList = new ArrayList();
            ArrayList penaltyEventDetailList = new ArrayList();
            HashMap eventDetailMap;
            ps = connRentFur.prepareStatement(eventDetailSelectSb.toString());
            ps.setInt(1, eventId);
            
            rs = ps.executeQuery();
            while(rs.next()){
                eventDetailMap = new HashMap();
                eventDetailMap.put("eventDetailId", rs.getInt("id"));
                eventDetailMap.put("furnitureCode", rs.getString("furniture_code"));
                eventDetailMap.put("furnitureDescription", rs.getString("furniture_description"));
                eventDetailMap.put("unitPrice", rs.getDouble("unit_price"));
                eventDetailMap.put("fineAmountPerUnit", rs.getDouble("fine_amount_per_unit"));
                eventDetailMap.put("quantity", rs.getInt("quantity"));
                eventDetailMap.put("totalAmount", rs.getDouble("total_amount"));
                eventDetailMap.put("taxAmount5", rs.getDouble("tax_amount_5"));
                eventDetailMap.put("taxAmount10", rs.getDouble("tax_amount_10"));
                eventDetailMap.put("taxAmount", rs.getDouble("tax_amount"));
                eventDetailMap.put("taxableAmount5", rs.getDouble("taxable_amount_5"));
                eventDetailMap.put("taxableAmount10", rs.getDouble("taxable_amount_10"));
                eventDetailMap.put("taxableAmount", rs.getDouble("taxable_amount"));
                eventDetailMap.put("observation", rs.getString("observation"));
                eventDetailMap.put("penalty", rs.getBoolean("penalty"));
                eventDetailMap.put("billable", rs.getBoolean("billable"));
                eventDetailMap.put("annexed", rs.getBoolean("annexed"));
                eventDetailMap = deepMerge(eventDetailMap, FurnitureController.getFurnitureByCode(rs.getString("furniture_code").toString()));
                if(rs.getBoolean("penalty")){
                    penaltyEventDetailList.add(eventDetailMap);
                }else{
                    eventDetailList.add(eventDetailMap);
                }
            }
            
            eventMap.put("detail", eventDetailList);
            eventMap.put("penaltyDetail", penaltyEventDetailList);
            
            connRentFur.commit();
            rs.close();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
        
        return eventMap;
    }
    
    public ArrayList getEventsByDayForIndex(Date deliveryDate){
        ArrayList eventList = new ArrayList();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        HashMap eventMap = new HashMap();    
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder eventHeaderSelectSb = new StringBuilder();
            eventHeaderSelectSb.append("SELECT id, \"number\" as eventNumber, place_of_delivery, observation, contract_number, net_total, subject_code, subject_name, subject_telephone, balance, status FROM event WHERE CAST(delivery_date AS DATE) = ? ORDER BY status, id ASC");

            ps = connRentFur.prepareStatement(eventHeaderSelectSb.toString());
            ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
            
            rs = ps.executeQuery();
            while(rs.next()){
                eventMap = new HashMap();
                eventMap.put("id", rs.getInt("id"));
                eventMap.put("number", rs.getInt("eventNumber"));
                eventMap.put("placeOfDelivery", rs.getString("place_of_delivery"));
                eventMap.put("observation", rs.getString("observation"));
                eventMap.put("contractNumber", rs.getInt("contract_number"));
                eventMap.put("netTotal", rs.getDouble("net_total"));
                eventMap.put("subjectCode", rs.getString("subject_code"));
                eventMap.put("subjectName", rs.getString("subject_name"));
                eventMap.put("subjectTelephone", rs.getString("subject_telephone"));
                eventMap.put("balance", rs.getDouble("balance"));
                eventMap.put("statusInt", rs.getInt("status"));
                eventMap.put("status", getEventStatus(rs.getInt("status")));
                eventList.add(eventMap);
            }
            
            connRentFur.commit();
            rs.close();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
        return eventList;
    }
    
    public static String getEventStatus(int status){
        String statusString;
        switch(status){
            case BUDGETED: 
                statusString = "PRESUPUESTADO";
                break;
            case CONFIRMED: 
                statusString = "CONFIRMADO";
                break;
            case CANCELED: 
                statusString = "CANCELADO";
                break;
            case FINISHED: 
                statusString =  "FINALIZADO";
                break;
            default: 
                statusString = "";
                break;
        }
        
        return statusString;
    }
    
    public ArrayList getFurnitureStockForIndex(Date deliveryDate){
        ArrayList furnitureList = new ArrayList();
        Connection connRentFur = null;
        PreparedStatement ps;
        ResultSet rs;
        HashMap furnitureMap = new HashMap();    
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);

            StringBuilder eventHeaderSelectSb = new StringBuilder();
            eventHeaderSelectSb.append("SELECT f.id, f.code, f.description, (SELECT description FROM furniture_family WHERE id = f.furniture_family_id) as family, (SELECT stock_total FROM furniture_stock WHERE furniture_id = f.id AND day = ?) as stock_total, (SELECT stock_available FROM furniture_stock WHERE furniture_id = f.id AND day = ?) as stock_available, (SELECT stock_committed FROM furniture_stock WHERE furniture_id = f.id AND day = ?) as stock_committed FROM furniture f WHERE f.active = true ORDER BY f.description");

            ps = connRentFur.prepareStatement(eventHeaderSelectSb.toString());
            ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
            ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
            ps.setDate(3, new java.sql.Date(deliveryDate.getTime()));
            
            rs = ps.executeQuery();
            while(rs.next()){
                furnitureMap = new HashMap();
                furnitureMap.put("id", rs.getInt("id"));
                furnitureMap.put("furnitureCode", rs.getString("code"));
                furnitureMap.put("furnitureDescription", rs.getString("description"));
                furnitureMap.put("furnitureFamily", rs.getString("family"));
                furnitureMap.put("stockTotal", rs.getDouble("stock_total"));
                furnitureMap.put("stockAvailable", rs.getDouble("stock_available"));
                furnitureMap.put("stockCommited", rs.getDouble("stock_committed"));
                furnitureList.add(furnitureMap);
            }
            
            connRentFur.commit();
            rs.close();
            ps.close();
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
        return furnitureList;
    }
    
    public HashMap updateInvoicedType(int eventId, boolean detailedInvoice){
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");

            StringBuilder eventInsertSb = new StringBuilder();
            eventInsertSb.append("UPDATE event SET detailed_invoice = ?, last_modification_user_id = ?, last_modification_date = current_timestamp WHERE id = ?");

            ps = connRentFur.prepareStatement(eventInsertSb.toString());
            ps.setBoolean(1, detailedInvoice);
            ps.setInt(2, loggedUser.getId());
            ps.setInt(3, eventId);
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
           
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
    
    public static void updateEventDetailBilled(int eventDetailId){
        
        HashMap mapToReturn = new HashMap();
        Connection connRentFur = null;
        PreparedStatement ps;
        
        try{
            
            connRentFur = DbConnectUtil.getConnection();
            connRentFur.setAutoCommit(false);
            UserRoles userRoles = new UserRoles();
            User loggedUser = userRoles.getUser();
            mapToReturn.put("status", ERROR_IN_SAVED);
            mapToReturn.put("message", "");

            StringBuilder eventInsertSb = new StringBuilder();
            eventInsertSb.append("UPDATE event_detail SET billable = ? WHERE id = ?");

            ps = connRentFur.prepareStatement(eventInsertSb.toString());
            ps.setBoolean(1, false);
            ps.setInt(2, eventDetailId);
            
            ps.executeUpdate();
            connRentFur.commit();
            ps.close();
           
            mapToReturn.put("status", SUCCESFULLY_SAVED);
            
        }catch(SQLException th){
            try {
                if(connRentFur!=null){
                    connRentFur.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.err.println("Error: "+th.getMessage());
            System.err.println(th.getNextException());
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
        
    }
    
}
