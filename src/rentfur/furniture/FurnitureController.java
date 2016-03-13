/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

/**
 *
 * @author hp
 */
public class FurnitureController {
    private FurnitureCreate furnitureCreate;
    public final int SUCCESFULLY_SAVED = 0;
    public final int ERROR_IN_SAVED = 1;
    
    
    public FurnitureCreate getFurnitureCreate(){
        if(furnitureCreate == null){
            furnitureCreate = new FurnitureCreate(this);
        }
        return furnitureCreate;
    }
}
