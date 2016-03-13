/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import rentfur.furnitureFamily.FurnitureFamilyController;
import rentfur.furnitureFamily.FurnitureFamilyCreate;

/**
 *
 * @author FDuarte
 */
public class MainWindow extends JFrame{
    JDesktopPane desktop;
    private Container container;/*declaramos el contenedor*/
    private MainWindow mainWindowUtilTemp;
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menu = new JMenu("Abrir");
    private final JMenuItem item = new JMenuItem("Ventana Interna");
    FurnitureFamilyController furnitureFamilyController = new FurnitureFamilyController();
    FurnitureFamilyCreate furnitureFamilyCreate;
    
    public MainWindow(){
        super("MainWindowUtilTem");
        /*permite iniciar las propiedades de los componentes*/
        initComponents();
        /*Asigna un titulo a la barra de titulo*/
        setTitle("RentFur");
        /*tamaño de la ventana*/
        setSize(1200,750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        /*pone la ventana en el Centro de la pantalla*/
        setLocationRelativeTo(null);
    }
    
    public void initComponents(){
        menuBar.add(menu);
        menu.add(item);
        add(menuBar, BorderLayout.NORTH);
        makeDesktop(); 
        item.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                   furnitureFamilyCreate = furnitureFamilyController.getFurnitureFamilyCreate();                  
                   desktop.add(furnitureFamilyCreate, JLayeredPane.DEFAULT_LAYER);
            }
        });
        container=getContentPane();/*instanciamos el contenedor*/
        /*con esto definmos nosotros mismos los tamaños y posicion
          de los componentes*/
        //container.setLayout(null);
        /*Agregamos los componentes al Contenedor*/
        container.add(desktop, BorderLayout.CENTER);
    }
    
    public void setMainWindow(MainWindow mainWindowUtilTemp) {
        this.mainWindowUtilTemp = mainWindowUtilTemp;
    }
    
    private void makeDesktop() {
       desktop = new JDesktopPane();
    }
}
