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
import rentfur.furnitureFamily.FurnitureFamilyController;
import rentfur.util.MainWindow;

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
        
        /*JFrame frame = new JFrame("Demo's");
        JDesktopPane desktop = new JDesktopPane();
        Container container = frame.getContentPane();//declaramos el contenedor
        frame.setSize(1200, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FurnitureFamilyController furnitureFamilyController = new FurnitureFamilyController();
        desktop.add(furnitureFamilyController.getFurnitureFamilyCreate(), JLayeredPane.DEFAULT_LAYER);
        container.add(desktop, BorderLayout.CENTER);
        frame.setVisible(true);
        */
        /*Declaramos el objeto*/
        MainWindow mainWindowUtilTemp;
        /*Instanciamos el objeto*/
        mainWindowUtilTemp = new MainWindow();
        /*Enviamos el objeto como parametro para que sea unico
          en toda la aplicación*/
        mainWindowUtilTemp.setMainWindow(mainWindowUtilTemp);
        /*Hacemos que se cargue la ventana*/
        mainWindowUtilTemp.setVisible(true);
    }
    
}
