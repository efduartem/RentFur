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
import rentfur.furniture.FurnitureShowAndEdit;
import rentfur.furniture.FurnitureIndex;
import rentfur.furnitureFamily.FurnitureFamilyCreate;
import rentfur.position.PositionController;
import rentfur.position.PositionCreate;
import rentfur.position.PositionIndex;
import rentfur.position.PositionShowAndEdit;
import rentfur.provider.ProviderController;
import rentfur.provider.ProviderCreate;
import rentfur.subject.SubjectController;
import rentfur.subject.SubjectCreate;
import rentfur.subject.SubjectIndex;
import rentfur.subject.SubjectShowAndEdit;
import rentfur.user.UserController;
import rentfur.user.UserCreate;
import rentfur.user.UserIndex;
import rentfur.user.UserShowAndEdit;

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
    private final JMenu providerMenu = new JMenu("Proveedores");
    private final JMenu organizationMenu = new JMenu("Organización");
    private final JMenuItem manageFurnitureItem = new JMenuItem("Administrar Mobiliarios");
    private final JMenuItem manageSubjectItem = new JMenuItem("Administrar Clientes");
    private final JMenuItem manageProviderItem = new JMenuItem("Administrar Proveedores");
    private final JMenuItem manageUserItem = new JMenuItem("Administrar Usuarios");
    private final JMenuItem managePositionItem = new JMenuItem("Administrar Cargos");
    
    //FURNITURE 
    FurnitureController furnitureController = new FurnitureController();
    FurnitureIndex furnitureIndex;
    FurnitureCreate furnitureCreate;
    FurnitureShowAndEdit furnitureShowAndEdit;
    FurnitureFamilyCreate furnitureFamilyCreate;
    
    //SUBJECT
    SubjectController subjectController = new SubjectController();
    SubjectIndex subjectIndex;
    SubjectCreate subjectCreate;
    SubjectShowAndEdit subjectShowAndEdit;
    
    //USER
    UserController userController = new UserController();
    UserShowAndEdit userShowAndEdit;
    UserIndex userIndex;
    UserCreate userCreate;
    
    //POSITION
    PositionController positionController = new PositionController();
    PositionCreate positionCreate;
    PositionIndex positionIndex;
    PositionShowAndEdit positionShowAndEdit;
    
    //PROVIDER
    ProviderController providerController = new ProviderController();
    ProviderCreate providerCreate;
    
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
        //Furniture
        ImageIcon furnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/furniture_24x24.png"));
        furnitureMenu.setIcon(furnitureIconImage);
        menuBar.add(furnitureMenu);
            furnitureMenu.add(manageFurnitureItem);
        
        //Subject
        ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/subject_24x24.png"));
        subjectMenu.setIcon(subjectIconImage);
        menuBar.add(subjectMenu);
            subjectMenu.add(manageSubjectItem);
            
        //Provider
        //ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/subject_24x24.png"));
        //providerMenu.setIcon(subjectIconImage);
        menuBar.add(providerMenu);
            providerMenu.add(manageProviderItem);
        
        //Organization
        ImageIcon organizationIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/organization_24x24.png"));
        organizationMenu.setIcon(organizationIconImage);
        menuBar.add(organizationMenu);
        
        //Users
        ImageIcon userIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/users_24x24.png"));
        manageUserItem.setIcon(userIconImage);
        organizationMenu.add(manageUserItem);

        //Position
        ImageIcon positionIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/position_24x24.png"));
        managePositionItem.setIcon(positionIconImage);
        organizationMenu.add(managePositionItem);
        
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
        
        manageProviderItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleProviderIndexInternalFrame();
            }
        });
        
        manageUserItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleUserIndexInternalFrame();
            }
        });
        
        managePositionItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisiblePositionIndexInternalFrame();
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
    
    
    //FURNITURE
    //Muestra InternalFrame de Busqueda de Mobiliatios
    public void setVisibleFurnitureIndexInternalFrame(){
        furnitureIndex = furnitureController.getFurnitureIndex(this.mainWindowController);
        desktop.add(furnitureIndex);
        
    }
    
    //Muestra InternalFrame de Detalles de un Mobiliario
    public void setVisibleFurnitureShowAndEditInternalFrame(int furnitureId){
        furnitureShowAndEdit = furnitureController.getFurnitureShowAndEdit(furnitureId);
        furnitureController.setDisabledIndexView();
        desktop.add(furnitureShowAndEdit, JLayeredPane.MODAL_LAYER);
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
    
    //SUBJECT
    public void setVisibleSubjectIndexInternalFrame(){
        subjectIndex = subjectController.getSubjectIndex(this.mainWindowController);
        desktop.add(subjectIndex);
    }
    
    public void setVisibleSubjectCreateInternalFrame(){
        subjectCreate = subjectController.getSubjectCreate();
        subjectController.setDisabledIndexView();
        desktop.add(subjectCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //Muestra InternalFrame de Detalles de un Cliente
    public void setVisibleSubjectShowAndEditInternalFrame(int subjectId){
        subjectShowAndEdit = subjectController.getSubjectShowAndEdit(subjectId);
        subjectController.setDisabledIndexView();
        desktop.add(subjectShowAndEdit, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //PROVIDER
    public void setVisibleProviderIndexInternalFrame(){
        providerCreate = providerController.getProviderCreate();
        desktop.add(providerCreate);
    }

    
    //USER
    public void setVisibleUserIndexInternalFrame(){
        userIndex = userController.getUserIndex(this.mainWindowController);
        desktop.add(userIndex);
    }
    
    //Muestra InternalFrame de Creacion de Mobiliario
    public void setVisibleUserCreateInternalFrame(){
        userCreate = userController.getUserCreate();
        userController.setDisabledIndexView();
        desktop.add(userCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //Muestra InternalFrame de Detalles de un Mobiliario
    public void setVisibleUserShowAndEditInternalFrame(int userId){
        userShowAndEdit = userController.getUserShowAndEdit(userId);
        userController.setDisabledIndexView();
        desktop.add(userShowAndEdit, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //POSITION
    public void setVisiblePositionIndexInternalFrame(){
        positionIndex = positionController.getPositionIndex(this.mainWindowController);
        desktop.add(positionIndex);
    }
    
    //Muestra InternalFrame de Creacion de Cargos
    public void setVisiblePositionCreateInternalFrame(){
        positionCreate = positionController.getPositionCreate();
        positionController.setDisabledIndexView();
        desktop.add(positionCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //Muestra InternalFrame de Detalles de un Mobiliario
    public void setVisiblePositionShowAndEditInternalFrame(int positionId){
        positionShowAndEdit = positionController.getPositionShowAndEdit(positionId);
        positionController.setDisabledIndexView();
        desktop.add(positionShowAndEdit, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
}
