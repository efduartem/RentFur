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
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import rentfur.furniture.FurnitureController;
import rentfur.furniture.FurnitureCreate;
import rentfur.furniture.FurnitureIndex;
import rentfur.furnitureFamily.FurnitureFamilyCreate;
import rentfur.subject.SubjectController;
import rentfur.subject.SubjectCreate;
import rentfur.subject.SubjectIndex;
import rentfur.user.UserController;
import rentfur.user.UserCreate;

/**
 *
 * @author FDuarte
 */
public class MainWindow extends JFrame{
    private final MainWindowController mainWindowController;
    JDesktopPane desktop;
    private Container container;/*declaramos el contenedor*/
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu furnitureMenu = new JMenu("Mobiliarios");
    private final JMenu subjectMenu = new JMenu("Clientes");
    private final JMenu userMenu = new JMenu("Usuarios");
    private final JMenuItem manageFurnitureItem = new JMenuItem("Administrar Mobiliarios");
    private final JMenuItem manageSubjectItem = new JMenuItem("Administrar Clientes");
    private final JMenuItem manageUserItem = new JMenuItem("Administrar Usuarios");
    
    FurnitureController furnitureController = new FurnitureController();
    FurnitureIndex furnitureIndex;
    FurnitureCreate furnitureCreate;
    FurnitureFamilyCreate furnitureFamilyCreate;
    
    SubjectController subjectController = new SubjectController();
    SubjectIndex subjectIndex;
    SubjectCreate subjectCreate;
    
    UserController userController = new UserController();
    UserCreate userCreate;
    
    public MainWindow(MainWindowController mainWindowController){
        
        this.mainWindowController = mainWindowController;
        /*permite iniciar las propiedades de los componentes*/
        initComponents();
        /*Asigna un titulo a la barra de titulo*/
        setTitle("RentFur");
        /*tamaño de la ventana*/
        setSize(1400,950);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        /*pone la ventana en el Centro de la pantalla*/
        setLocationRelativeTo(null);
    }
    
    public void initComponents(){
        ImageIcon furnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/furniture_24x24.png"));
        furnitureMenu.setIcon(furnitureIconImage);
        menuBar.add(furnitureMenu);
        menuBar.add(subjectMenu);
        menuBar.add(userMenu);
        furnitureMenu.add(manageFurnitureItem);
        subjectMenu.add(manageSubjectItem);
        userMenu.add(manageUserItem);
        add(menuBar, BorderLayout.NORTH);
        makeDesktop(); 
        manageFurnitureItem.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                   setVisibleFurnitureIndexInternalFrame();
            }
        });
        
        manageSubjectItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleSubjectIndexInternalFrame();
            }
        });
        
        manageUserItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleUserIndexInternalFrame();
            }
        });
        container=getContentPane();/*instanciamos el contenedor*/
        /*con esto definmos nosotros mismos los tamaños y posicion
          de los componentes*/
        //container.setLayout(null);
        /*Agregamos los componentes al Contenedor*/
        container.add(desktop, BorderLayout.CENTER);
    }
    
    private void makeDesktop() {
       desktop = new JDesktopPane();
    }
    
    //Muestra InternalFrame de Busqueda de Mobiliatios
    public void setVisibleFurnitureIndexInternalFrame(){
        furnitureIndex = furnitureController.getFurnitureIndex(this.mainWindowController);
        desktop.add(furnitureIndex);
        
    }
    
    public void setVisibleSubjectIndexInternalFrame(){
        subjectIndex = subjectController.getSubjectIndex(this.mainWindowController);
        desktop.add(subjectIndex);
    }
    
    public void setVisibleSubjectCreateInternalFrame(){
        subjectCreate = subjectController.getSubjectCreate();
        desktop.add(subjectCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //Muestra InternalFrame de Creacion de Mobiliario
    public void setVisibleFurnitureCreateInternalFrame(){
        furnitureCreate = furnitureController.getFurnitureCreate();
        furnitureController.setDisabledIndexView();
        desktop.add(furnitureCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //Muestra InternalFrame de Creacion de FAMILIA de Mobiliarios
    public void setVisibleFurnitureCreateFamilyInternalFrame(){
        furnitureFamilyCreate = furnitureController.getFurnitureFamilyCreate();
        furnitureController.setDisabledIndexView();
        desktop.add(furnitureFamilyCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    public void setVisibleUserIndexInternalFrame(){
        userCreate = userController.getUserCreate();
        desktop.add(userCreate);//, JLayeredPane.MODAL_LAYER);
        //getContentPane().add(desktop);
    }
    
}
