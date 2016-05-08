/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.event;

import rentfur.util.ComboBoxItem;
import rentfur.util.MainWindowController;

/**
 *
 * @author FDuarte
 */
public class EventController {
    private MainWindowController mainWindowController;
    public static final int BUDGETED = 0;
    public static final int CONFIRMED = 1;
    public static final int CANCELED = 2;
    public static final String ALL_VALUES = "Todos";
    private EventCreate eventCreate;
    
    public EventCreate getEventCreate(){
        if(eventCreate == null){
            eventCreate = new EventCreate(this);
        }
        return eventCreate;
    }
    
    public void eventCreateClosed(){
        eventCreate = null;
    }
    
    public static ComboBoxItem[] getEventStatusAvailablesForComboBox(boolean addAllOption){
        
        ComboBoxItem[] statusAvailables = null;
        try{
            
            if(addAllOption){
                statusAvailables = new ComboBoxItem[4];
                
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(ALL_VALUES);
                statusAvailables[0].setValue(ALL_VALUES);
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(BUDGETED));
                statusAvailables[1].setValue("Presupuestado");
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(CONFIRMED));
                statusAvailables[2].setValue("Confirmado");
                
                statusAvailables[3] =  new ComboBoxItem();
                statusAvailables[3].setKey(String.valueOf(CANCELED));
                statusAvailables[3].setValue("Cancelado");
            }else{
                statusAvailables = new ComboBoxItem[3];
                         
                statusAvailables[0] =  new ComboBoxItem();
                statusAvailables[0].setKey(String.valueOf(BUDGETED));
                statusAvailables[0].setValue("Presupuestado");
                
                statusAvailables[1] =  new ComboBoxItem();
                statusAvailables[1].setKey(String.valueOf(CONFIRMED));
                statusAvailables[1].setValue("Confirmado");
                
                statusAvailables[2] =  new ComboBoxItem();
                statusAvailables[2].setKey(String.valueOf(CANCELED));
                statusAvailables[2].setValue("Cancelado");
            }

        }catch(Throwable th){
            System.err.println(th.getMessage());
            System.err.println(th);
        }
        
        return statusAvailables;
    }
    
}
