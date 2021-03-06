/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

/**
 *
 * @author FDuarte
 */
public class MainWindowController {
    private MainWindow mainWindow;
    
    public MainWindow getMainWindowCreate() {
        if(mainWindow==null){
            mainWindow = new MainWindow(this);
        }
        return mainWindow;
    }
    
    //FURNITURE
    public void setVisibleFurnitureCreateInternalFrame(){
        mainWindow.setVisibleFurnitureCreateInternalFrame();
    }
    
    public void setVisibleFurnitureFamilyCreateInternalFrame(){
        mainWindow.setVisibleFurnitureCreateFamilyInternalFrame();
    }
    
    public void setVisibleFurnitureShowAndEditInternalFrame(int furnitureId){
        mainWindow.setVisibleFurnitureShowAndEditInternalFrame(furnitureId);
    }
    
    //SUBJECT
    public void setVisibleSubjectCreateInternalFrame(){
        mainWindow.setVisibleSubjectCreateInternalFrame();
    }
    
    public void setVisibleSubjectShowAndEditInternalFrame(int subjectId){
        mainWindow.setVisibleSubjectShowAndEditInternalFrame(subjectId);
    }
    
    //USER
    public void setVisibleUserCreateInternalFrame(){
        mainWindow.setVisibleUserCreateInternalFrame();
    }
    
    public void setVisibleUserShowAndEditInternalFrame(int userId){
        mainWindow.setVisibleUserShowAndEditInternalFrame(userId);
    }
    
    //POSITION
    public void setVisiblePositionCreateInternalFrame(){
        mainWindow.setVisiblePositionCreateInternalFrame();
    }
    
    public void setVisiblePositionShowAndEditInternalFrame(int positionId){
        mainWindow.setVisiblePositionShowAndEditInternalFrame(positionId);
    }
    
    //PROVIDER
    public void setVisibleProviderCreateInternalFrame(){
        mainWindow.setVisibleProviderCreateInternalFrame();
    }
    
    public void setVisibleProviderShowAndEditInternalFrame(int providerId){
        mainWindow.setVisibleProviderShowAndEditInternalFrame(providerId);
    }
    
    //EVENT
    public void setVisibleEventShowAndEditInternalFrame(int eventId){
        mainWindow.setVisibleEventShowAndEditInternalFrame(eventId);
    }

    //PURCHASE PROVIDER
//    public void setVisiblePurchaseInvoiceCreateInternalFrame(){
//        mainWindow.setVisiblePurchaseInvoiceCreateInternalFrame();
//    }
    
}
