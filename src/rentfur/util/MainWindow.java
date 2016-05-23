/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import rentfur.event.EventController;
import rentfur.event.EventCreate;
import rentfur.event.EventIndex;
import rentfur.event.EventShowAndEdit;
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
import rentfur.provider.ProviderIndex;
import rentfur.provider.ProviderShowAndEdit;
import rentfur.subject.SubjectController;
import rentfur.subject.SubjectCreate;
import rentfur.subject.SubjectIndex;
import rentfur.subject.SubjectShowAndEdit;
import rentfur.user.UserController;
import rentfur.user.UserCreate;
import rentfur.user.UserIndex;
import rentfur.user.UserShowAndEdit;
import rentfur.util.searches.SearchController;
import rentfur.util.searches.SubjectSearch;

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
    private final JMenu eventsMenu = new JMenu(" Eventos");

    private final JMenuItem manageFurnitureItem = new JMenuItem("Administrar Mobiliarios");
    private final JMenuItem manageSubjectItem = new JMenuItem("Administrar Clientes");
    private final JMenuItem manageProviderItem = new JMenuItem("Administrar Proveedores");
    private final JMenuItem manageUserItem = new JMenuItem("Administrar Usuarios");
    private final JMenuItem managePositionItem = new JMenuItem("Administrar Cargos");
    
    private final JMenuItem manageEvents = new JMenuItem("Administrar Eventos");
    
    UserRoles userRoles = new UserRoles();
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
    ProviderIndex providerIndex;
    ProviderShowAndEdit providerShowAndEdit;
    
    //EVENTS
    EventController eventController = new EventController();
    EventIndex eventIndex;
    EventCreate eventCreate;
    EventShowAndEdit eventShowAndEdit;
    
    public MainWindow(MainWindowController mainWindowController){
        
        this.mainWindowController = mainWindowController;
        /*permite iniciar las propiedades de los componentes*/
        initComponents();
        /*Asigna un titulo a la barra de titulo*/
        setTitle("RentFur ["+userRoles.getUser().getUserName()+" - "+userRoles.getUser().getFullName()+"] - "+userRoles.getUser().getPosition());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/rentfur/util/image/rentfur_icon.png")));
        /*tamaño de la ventana*/
        setSize(1350,850);
        desktop.setBackground(new Color(204, 204, 204));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        /*pone la ventana en el Centro de la pantalla*/
        setLocationRelativeTo(null);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }
    
    public void initComponents(){
        
        //Furniture
        ImageIcon furnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/furniture_24x24.png"));
        furnitureMenu.setIcon(furnitureIconImage);
        menuBar.add(furnitureMenu);
            furnitureMenu.add(manageFurnitureItem);
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_FURNITURE)){
            manageFurnitureItem.setEnabled(false);
            manageFurnitureItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Mobiliarios");
        }
            
        //Subject
        ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/subject_24x24.png"));
        subjectMenu.setIcon(subjectIconImage);
        menuBar.add(subjectMenu);
            subjectMenu.add(manageSubjectItem);
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_SUBJECT)){
            manageSubjectItem.setEnabled(false);
            manageSubjectItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Clientes");
        }
            
        //Provider
        ImageIcon providerIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/provider_32x32.png"));
        providerMenu.setIcon(providerIconImage);
        menuBar.add(providerMenu);
            providerMenu.add(manageProviderItem);
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_PROVIDER)){
            manageProviderItem.setEnabled(false);
            manageProviderItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Proveedores");
        }
            
        //Organization
        ImageIcon organizationIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/organization_24x24.png"));
        organizationMenu.setIcon(organizationIconImage);
        menuBar.add(organizationMenu);
        
        //Users
        ImageIcon userIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/users_24x24.png"));
        manageUserItem.setIcon(userIconImage);
        organizationMenu.add(manageUserItem);

        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_USER)){
            manageUserItem.setEnabled(false);
            manageUserItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Usuarios");
        }
        
        //Position
        ImageIcon positionIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/position_24x24.png"));
        managePositionItem.setIcon(positionIconImage);
        organizationMenu.add(managePositionItem);
        
        /*if(!userRoles.getRolesMap().containsKey(positionController.ROLE_RF_POSITION)){
            managePositionItem.setEnabled(false);
            managePositionItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Cargos");
        }*/
        
        //Events
        ImageIcon eventsIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/events_calendar_24x24.png"));
        eventsMenu.setIcon(eventsIconImage);
        menuBar.add(eventsMenu);
                eventsMenu.add(manageEvents);
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
        
        manageEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleEventsIndexInternalFrame();
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
        furnitureIndex = furnitureController.getFurnitureIndex(this.mainWindowController, userRoles);
        desktop.add(furnitureIndex);
        
    }
    
    //Muestra InternalFrame de Detalles de un Mobiliario
    public void setVisibleFurnitureShowAndEditInternalFrame(int furnitureId){
        furnitureShowAndEdit = furnitureController.getFurnitureShowAndEdit(furnitureId, userRoles);
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
        subjectIndex = subjectController.getSubjectIndex(this.mainWindowController, userRoles);
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
        subjectShowAndEdit = subjectController.getSubjectShowAndEdit(subjectId, userRoles);
        subjectController.setDisabledIndexView();
        desktop.add(subjectShowAndEdit, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    //PROVIDER
    public void setVisibleProviderIndexInternalFrame(){
        providerIndex = providerController.getProviderIndex(this.mainWindowController, userRoles);
        desktop.add(providerIndex);
    }
    
    public void setVisibleProviderCreateInternalFrame(){
        providerCreate = providerController.getProviderCreate();
        providerController.setDisabledIndexView();
        desktop.add(providerCreate, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }
    
    public void setVisibleProviderShowAndEditInternalFrame(int providerId){
        providerShowAndEdit = providerController.getProviderShowAndEdit(providerId, userRoles);
        providerController.setDisabledIndexView();
        desktop.add(providerShowAndEdit, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
    }

    
    //USER
    public void setVisibleUserIndexInternalFrame(){
        userIndex = userController.getUserIndex(this.mainWindowController, userRoles);
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
        userShowAndEdit = userController.getUserShowAndEdit(userId, userRoles);
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
    
    //EVENTS
    public void setVisibleEventsIndexInternalFrame(){
        eventIndex = eventController.getEventIndex(this.mainWindowController);
        //eventCreate = eventController.getEventCreate();
        desktop.add(eventIndex);
    }
    
    public void setVisibleEventShowAndEditInternalFrame(int eventId){
        eventShowAndEdit = eventController.getEventShowAndEdit(eventId);
        desktop.add(eventShowAndEdit);
    }
    
}
