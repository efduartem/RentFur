/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import rentfur.furniture.FurnitureController;
import rentfur.util.MainWindow;
import rentfur.util.MainWindowController;

/**
 *
 * @author FDuarte
 */
public class RentFur {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(RentFur.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MainWindowController mainWindowController;
        mainWindowController = new MainWindowController();
        
        MainWindow mainWindow = mainWindowController.getMainWindowCreate();
        mainWindow.setVisible(true);
    }
    
}
