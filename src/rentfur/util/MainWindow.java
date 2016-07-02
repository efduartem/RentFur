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
import rentfur.furnitureMovement.FurnitureMovementController;
import rentfur.furnitureMovement.FurnitureMovementIndex;
import rentfur.furnitureMovement.FurnitureMovementInputCreate;
import rentfur.help.Help;
import rentfur.position.PositionController;
import rentfur.position.PositionCreate;
import rentfur.position.PositionIndex;
import rentfur.position.PositionShowAndEdit;
import rentfur.provider.ProviderController;
import rentfur.provider.ProviderCreate;
import rentfur.provider.ProviderIndex;
import rentfur.provider.ProviderShowAndEdit;
import rentfur.purchaseInvoice.PurchaseInvoiceController;
import rentfur.purchaseInvoice.PurchaseInvoiceCreate;
import rentfur.purchaseInvoice.PurchaseInvoiceIndex;
import rentfur.report.EventsTotalByDate;
import rentfur.report.HechaukaReportView;
import rentfur.report.PendingEventsByDate;
import rentfur.report.ReportController;
import rentfur.report.SubjectAccountStatusReport;
import rentfur.subject.SubjectController;
import rentfur.subject.SubjectCreate;
import rentfur.subject.SubjectIndex;
import rentfur.subject.SubjectShowAndEdit;
import rentfur.user.ChangePasswordInternalFrame;
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
    private final JMenu systemMenu = new JMenu("Sistema");
    private final JMenu furnitureMenu = new JMenu("Mobiliarios");
    private final JMenu subjectMenu = new JMenu("Clientes");
    private final JMenu providerMenu = new JMenu("Proveedores");
    private final JMenu organizationMenu = new JMenu("Organización");
    private final JMenu eventsMenu = new JMenu(" Eventos");
    private final JMenu reportsMenu = new JMenu(" Informes");

    private static final JMenuItem exitItem = new JMenuItem("Salir");
    private static final JMenuItem helpItem = new JMenuItem("Ayuda");
    private static final JMenuItem changePasswordItem = new JMenuItem("Cambiar Contraseña");
    
    private static final JMenuItem manageFurnitureItem = new JMenuItem("Administrar Mobiliarios");
    
    private final JMenu furnitureInventoryItem = new JMenu("Inventario");
    private final JMenuItem manageFurnitureMovementItem = new JMenuItem("Movimientos de Stock");
    private final JMenuItem inputMovementItem = new JMenuItem("Registrar Entradas");
    private final JMenuItem outputMovementItem = new JMenuItem("Registrar Salidas");
    
    private static final JMenuItem manageSubjectItem = new JMenuItem("Administrar Clientes");
    private static final JMenuItem manageProviderItem = new JMenuItem("Administrar Proveedores");
    private static final JMenuItem manageUserItem = new JMenuItem("Administrar Usuarios");
    private final JMenuItem managePositionItem = new JMenuItem("Administrar Cargos");
    private final JMenuItem managePurchaseItem = new JMenuItem("Gestionar Compras");
    
    private static final JMenuItem manageEvents = new JMenuItem("Calendario de Eventos");
    
    private final JMenu eventReportsItem = new JMenu("Eventos");
    private final JMenu subjectReportsItem = new JMenu("Clientes");
    private static final JMenuItem hechaukaReportsItem = new JMenuItem("Hechauka");
    
    private static final JMenuItem eventsTotalByDateItem = new JMenuItem("Resumen de Eventos por Fecha");
    private static final JMenuItem pendingEventsByDateItem = new JMenuItem("Resumen Eventos Pendientes por Fecha");
    private static final JMenuItem subjectAccountStatusReportItem = new JMenuItem("Estado de Cuenta");
    
    
    
    private static final UserRoles userRoles = new UserRoles();
    //FURNITURE 
    FurnitureController furnitureController = new FurnitureController();
    FurnitureIndex furnitureIndex;
    FurnitureCreate furnitureCreate;
    FurnitureShowAndEdit furnitureShowAndEdit;
    FurnitureFamilyCreate furnitureFamilyCreate;
    
    FurnitureMovementController furnitureMovementController = new FurnitureMovementController();
    FurnitureMovementIndex furnitureMovementIndex;
    FurnitureMovementInputCreate furnitureMovementInputCreate;
    
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
    ChangePasswordInternalFrame changePasswordInternalFrame;
    
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
    
    //PURCHASE PROVIDER
    PurchaseInvoiceController purchaseInvoiceController = new PurchaseInvoiceController();
    PurchaseInvoiceCreate purchaseInvoiceCreate;
    PurchaseInvoiceIndex purchaseInvoiceIndex;
    
    //REPORTS
    ReportController reportController = new ReportController();
    EventsTotalByDate eventsTotalByDate;
    PendingEventsByDate pendingEventsByDate;
    SubjectAccountStatusReport subjectAccountStatusReport;
    HechaukaReportView hechaukaReportView;
    
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
        
        ImageIcon systemIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/system_icon.png"));
        systemMenu.setIcon(systemIconImage);
        
        ImageIcon changePasswordIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/change_password_icon.png"));
        changePasswordItem.setIcon(changePasswordIconImage);
        
        ImageIcon helpIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/help_24x24.png"));
        helpItem.setIcon(helpIconImage);
        
        ImageIcon exitIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/exit_icon.png"));
        exitItem.setIcon(exitIconImage);
        
        menuBar.add(systemMenu);
            systemMenu.add(changePasswordItem);
            systemMenu.add(helpItem);
            systemMenu.add(exitItem);
        
        changePasswordItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleUserChangePasswordInternalFrame();
            }
        });
        
        helpItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Help help = new Help();
                help.setVisible(true);
            }
        });
            
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        //Furniture
        ImageIcon furnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/furniture_24x24.png"));
        furnitureMenu.setIcon(furnitureIconImage);
        
        ImageIcon manageFurnitureIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/drinks_24_x_24.png"));
        manageFurnitureItem.setIcon(manageFurnitureIconImage);
        
        ImageIcon furnitureInventoryItemIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/inventory_maintenance_32_x_32.png"));
        furnitureInventoryItem.setIcon(furnitureInventoryItemIconImage);
        
        ImageIcon furnitureMovementIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/movement_24x24.png"));
        manageFurnitureMovementItem.setIcon(furnitureMovementIconImage);
        
        ImageIcon inputMovementIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/input_movement_20x20.png"));
        inputMovementItem.setIcon(inputMovementIconImage);
        
        ImageIcon outputMovementIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/exit_movement_20x20.png"));
        outputMovementItem.setIcon(outputMovementIconImage);
            
        menuBar.add(furnitureMenu);
            furnitureMenu.add(manageFurnitureItem);
            furnitureMenu.add(furnitureInventoryItem);
                furnitureInventoryItem.add(manageFurnitureMovementItem);
                furnitureInventoryItem.add(inputMovementItem);
                //furnitureInventoryItem.add(outputMovementItem);
                
            
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_FURNITURE)){
            manageFurnitureItem.setEnabled(false);
            manageFurnitureItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Mobiliarios");
        }
        
        manageFurnitureItem.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                   setVisibleFurnitureIndexInternalFrame();
            }
        });
        
        manageFurnitureMovementItem.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                   setVisibleFurnitureMovementIndexInternalFrame();
            }
        });
        
        inputMovementItem.addActionListener(new ActionListener() {
            @Override
             public void actionPerformed(ActionEvent e) {
                   setVisibleFurnitureMovementInputCreateInternalFrame();
            }
        });
            
        //Subject
        ImageIcon subjectIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/subject_24x24.png"));
        subjectMenu.setIcon(subjectIconImage);
        menuBar.add(subjectMenu);
            subjectMenu.add(manageSubjectItem);
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_SUBJECT)){
            manageSubjectItem.setEnabled(false);
            manageSubjectItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Clientes");
        }
        
        manageSubjectItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleSubjectIndexInternalFrame();
            }
        });
            
        //Provider
        ImageIcon providerIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/provider_32x32.png"));
        providerMenu.setIcon(providerIconImage);
        menuBar.add(providerMenu);
            providerMenu.add(manageProviderItem);
            providerMenu.add(managePurchaseItem);
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_PROVIDER)){
            manageProviderItem.setEnabled(false);
            manageProviderItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Proveedores");
        }
        
        manageProviderItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleProviderIndexInternalFrame();
            }
        });
        
        managePurchaseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisiblePurchaseInvoiceIndexInternalFrame();
            }
        });
            
        //Events
        ImageIcon eventsIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/events_calendar_24x24.png"));
        eventsMenu.setIcon(eventsIconImage);
        
        ImageIcon manageEventsIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/calendar_icon.png"));
        manageEvents.setIcon(manageEventsIconImage);
        
        menuBar.add(eventsMenu);
                eventsMenu.add(manageEvents);
                
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_EVENTS)){
            manageEvents.setEnabled(false);
            manageEvents.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Clientes");
        }
        
        manageEvents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleEventsIndexInternalFrame();
            }
        });
                
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
        
        manageUserItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleUserIndexInternalFrame();
            }
        });
        
        //Position
        ImageIcon positionIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/position_24x24.png"));
        managePositionItem.setIcon(positionIconImage);
        organizationMenu.add(managePositionItem);
        
        managePositionItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisiblePositionIndexInternalFrame();
            }
        });
        /*if(!userRoles.getRolesMap().containsKey(positionController.ROLE_RF_POSITION)){
            managePositionItem.setEnabled(false);
            managePositionItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Cargos");
        }*/
        
        //Reports
        ImageIcon reportsIconImage = new ImageIcon(getClass().getResource("/rentfur/button/image/util/report_menu_icon.png"));
        reportsMenu.setIcon(reportsIconImage);
        menuBar.add(reportsMenu);
            reportsMenu.add(eventReportsItem);
                eventReportsItem.add(eventsTotalByDateItem);
                eventReportsItem.add(pendingEventsByDateItem);
            reportsMenu.add(subjectReportsItem);
                subjectReportsItem.add(subjectAccountStatusReportItem);
            reportsMenu.add(hechaukaReportsItem);
                
        eventsTotalByDateItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleEventsTotalByDateInternalFrame();
            }
        });
        
        pendingEventsByDateItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisiblePendingEventsByDateInternalFrame();
            }
        });
        
        subjectAccountStatusReportItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleSubjectAccountStatusReportInternalFrame();
            }
        });
        
        hechaukaReportsItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisibleHechaukaReportInternalFrame();
            }
        });
                
        add(menuBar, BorderLayout.NORTH);
        makeDesktop(); 
        
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
    
    //Muestra InternalFrame de Movimientos de Mobiliatios
    public void setVisibleFurnitureMovementIndexInternalFrame(){
        furnitureMovementIndex = furnitureMovementController.getFurnitureMovementIndex(this.mainWindowController, userRoles);
        desktop.add(furnitureMovementIndex);
        
    }
    
    //Muestra InternalFrame de Movimientos de Mobiliatios
    public void setVisibleFurnitureMovementInputCreateInternalFrame(){
        furnitureMovementInputCreate = furnitureMovementController.getFurnitureMovementInputCreate(this.mainWindowController, userRoles);
        desktop.add(furnitureMovementInputCreate);
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
    
    public void setVisibleUserChangePasswordInternalFrame(){
        changePasswordInternalFrame = userController.getUserChangePassword();
        desktop.add(changePasswordInternalFrame);
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

    //PURCHASE INVOICE
    public void setVisiblePurchaseInvoiceIndexInternalFrame(){
        purchaseInvoiceIndex = purchaseInvoiceController.getPurchaseInvoiceIndex(this.mainWindowController, userRoles);
        desktop.add(purchaseInvoiceIndex);
        getContentPane().add(desktop);
    }
    
    //REPORTS
    public void setVisibleEventsTotalByDateInternalFrame(){
        eventsTotalByDate = reportController.getEventsTotalByDateView();
        desktop.add(eventsTotalByDate);
    }
    
    public void setVisiblePendingEventsByDateInternalFrame(){
        pendingEventsByDate = reportController.getPendingEventsByDateView();
        desktop.add(pendingEventsByDate);
    }
    
    public void setVisibleSubjectAccountStatusReportInternalFrame(){
        subjectAccountStatusReport = reportController.getSubjectAccountStatusReportView();
        desktop.add(subjectAccountStatusReport);
    }
    
    public void setVisibleHechaukaReportInternalFrame(){
        hechaukaReportView = reportController.getHechaukaReportView();
        desktop.add(hechaukaReportView);
    }
    
    public static void updateItemAccess(){
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_FURNITURE)){
            manageFurnitureItem.setEnabled(false);
            manageFurnitureItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Mobiliarios");
        }else{
            manageFurnitureItem.setEnabled(true);
        }
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_SUBJECT)){
            manageSubjectItem.setEnabled(false);
            manageSubjectItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Clientes");
        }else{
            manageSubjectItem.setEnabled(true);
        }
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_PROVIDER)){
            manageProviderItem.setEnabled(false);
            manageProviderItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Proveedores");
        }else{
            manageProviderItem.setEnabled(true);
        }
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_USER)){
            manageUserItem.setEnabled(false);
            manageUserItem.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Usuarios");
        }else{
            manageUserItem.setEnabled(true);
        }
        
        if(!userRoles.getRolesMap().containsKey(PositionController.ROLE_RF_EVENTS)){
            manageEvents.setEnabled(false);
            manageEvents.setToolTipText("Su usuario no cuenta con permisos para acceder a la administración de Eventos");
        }else{
            manageEvents.setEnabled(true);
        }
    }
    
}
