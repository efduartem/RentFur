/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.position;

/**
 *
 * @author FDuarte
 */
public class PositionController {
    private PositionCreate positionCreate;
    
    public PositionCreate getPositionCreate(){
        if(positionCreate == null){
            positionCreate = new PositionCreate(this);
        }
        return positionCreate;
    }
    
}
