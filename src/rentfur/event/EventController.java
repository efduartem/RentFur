/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.event;

import rentfur.util.MainWindowController;

/**
 *
 * @author FDuarte
 */
public class EventController {
    private MainWindowController mainWindowController;
    
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
    
}
