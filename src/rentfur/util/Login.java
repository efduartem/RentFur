package rentfur.util;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import rentfur.RentFur;

/**
 *
 * @author FDuarte
 */
public class Login extends JFrame {

    private JDesktopPane loginDesktopPane;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel keysImageLabel;
    private JLabel userImageLabel;
    private JLabel passwordImageLabel;
    private JLabel loginTitleLabel;
    private JLabel loginLogoImageLabel;
    private JPanel loginPanel;
    private JPasswordField passwordPasswordField;
    private JTextField usernameTextField;
    private JButton loginButton;

    UserRoles roles = new UserRoles();
    
    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")                        
    private void initComponents() {

        
        loginDesktopPane = new JDesktopPane();
        loginPanel = new JPanel();
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        usernameTextField = new JTextField();
        passwordPasswordField = new JPasswordField();
        userImageLabel = new JLabel();
        passwordImageLabel = new JLabel();
        keysImageLabel = new JLabel();
        loginTitleLabel = new JLabel();
        loginLogoImageLabel = new JLabel();
        loginButton = new JButton();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("RentFur");
        setForeground(new Color(0, 0, 255));
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/rentfur/util/image/rentfur_icon.png")));

        loginDesktopPane.setBackground(new Color(0, 51, 102));

        loginPanel.setBackground(new Color(0, 153, 204));
        loginPanel.setBorder(BorderFactory.createEtchedBorder());

        usernameLabel.setText("Usuario:");
        usernameTextField.setText("fduarte");
        passwordPasswordField.setText("12345");
        passwordLabel.setText("Contraseña:");

        loginButton.setText("Login");
        loginButton.setIcon(new ImageIcon(getClass().getResource("/rentfur/util/image/loginButton.png")));
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });
        
        userImageLabel.setIcon(new ImageIcon(getClass().getResource("/rentfur/util/image/loginUserLabel.png")));

        passwordImageLabel.setIcon(new ImageIcon(getClass().getResource("/rentfur/util/image/loginPasswordLabel.png"))); // NOI18N

        GroupLayout jPanel1Layout = new GroupLayout(loginPanel);
        loginPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(loginButton)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(userImageLabel)
                            .addComponent(passwordImageLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordLabel)
                            .addComponent(usernameLabel))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(passwordPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(usernameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernameLabel)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(userImageLabel)))
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(passwordPasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordLabel)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(passwordImageLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(loginButton)
                .addContainerGap())
        );

        keysImageLabel.setIcon(new ImageIcon(getClass().getResource("/rentfur/util/image/loginKeysImage.png")));

        loginTitleLabel.setFont(new Font("Segoe Script", 3, 48));
        loginTitleLabel.setForeground(new Color(255, 255, 255));
        loginTitleLabel.setText("RentFur");

        loginLogoImageLabel.setIcon(new ImageIcon(getClass().getResource("/rentfur/util/image/loginImageLogoLabel.png")));

        GroupLayout jDesktopPane1Layout = new GroupLayout(loginDesktopPane);
        loginDesktopPane.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(jDesktopPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(loginLogoImageLabel)
                        .addGap(18, 18, 18)
                        .addComponent(loginTitleLabel, GroupLayout.PREFERRED_SIZE, 257, GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addComponent(keysImageLabel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(181, 181, 181))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addGroup(jDesktopPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(loginLogoImageLabel, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE))
                    .addGroup(GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(loginTitleLabel)
                        .addGap(28, 28, 28)))
                .addGroup(jDesktopPane1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(loginPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(keysImageLabel, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                        .addGap(178, 178, 178))))
        );
        loginDesktopPane.setLayer(loginPanel, JLayeredPane.DEFAULT_LAYER);
        loginDesktopPane.setLayer(keysImageLabel, JLayeredPane.DEFAULT_LAYER);
        loginDesktopPane.setLayer(loginTitleLabel, JLayeredPane.DEFAULT_LAYER);
        loginDesktopPane.setLayer(loginLogoImageLabel, JLayeredPane.DEFAULT_LAYER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(loginDesktopPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(loginDesktopPane)
        );
        setResizable(false);
        setBounds(560, 270, WIDTH, WIDTH);
        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(RentFur.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }*/
        //</editor-fold>

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    
    public void loginAction(){
        if(passwordPasswordField.getPassword().length == 0 || usernameTextField.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null,"Para tener acceso al sistema \n "
                + "debe ingresar su nombre de usuario y contraseña","Acceso al sistema",
                JOptionPane.INFORMATION_MESSAGE);
        }else{
            Connection usuariosConnection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            HashMap rolesMap = new HashMap();
            int userId = 0;
            String userCode = "";
            String username = "";
            String fullname = "";
            String position = "";
            
            boolean active = true;
            int positionId = 0;
            try{
                String password = new String(passwordPasswordField.getPassword());
                usuariosConnection=DbConnectUtil.getConnection();
                String validacionUsuario = "SELECT id, code, username, fullname, active, (SELECT description FROM position p WHERE p.id = position_id) as position, position_id, password FROM users WHERE username = ? AND password = ?";
                ps = usuariosConnection.prepareStatement(validacionUsuario);
                ps.setString(1, usernameTextField.getText());
                ps.setString(2, password);
                rs = ps.executeQuery();
                if(rs.next()){
                    userId = rs.getInt("id");
                    userCode = rs.getString("code");
                    username = rs.getString("username");
                    fullname = rs.getString("fullname");
                    active = rs.getBoolean("active");
                    position = rs.getString("position");
                    positionId = rs.getInt("position_id");
                    password = rs.getString("password");
                }
                
                User user = new User(userId, userCode, username, fullname, active, position, password);
                
                if(!userCode.equals("") && active){
                    String query="SELECT (SELECT code FROM role WHERE id = pr.role_id) as code, pr.only_query FROM position_role pr WHERE pr.position_id = ?";
                    ps = usuariosConnection.prepareStatement(query);
                    ps.setInt(1, positionId);
                    rs = ps.executeQuery();
                    while(rs.next()){
                        rolesMap.put(rs.getString("code"), rs.getBoolean("only_query"));
                    }
                    roles.setRolesMap(rolesMap);
                    roles.setUser(user);
                    showMainWindow();
                    this.dispose();
                }else if(!active){
                    JOptionPane.showMessageDialog(null,"Usuario desactivado, favor comunicarse con el Administrador del Sistema",
                    "Acceso al Sistema", JOptionPane.WARNING_MESSAGE);
                    this.passwordPasswordField.setText("");
                }else{
                    JOptionPane.showMessageDialog(null,"Nombre de usuario o contraseña incorrectos",
                    "Acceso al Sistema", JOptionPane.WARNING_MESSAGE);
                    this.passwordPasswordField.setText("");
                }
                
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }finally{
                if(usuariosConnection!=null){
                    try{ usuariosConnection.close(); }catch(Throwable e){ System.out.println(e.getMessage()); }
                }
                if(ps!=null){
                    try{ ps.close(); }catch(Throwable e){ System.out.println(e.getMessage()); }
                }
                if(rs!=null){
                    try{ rs.close(); }catch(Throwable e){ System.out.println(e.getMessage()); }
                }
            }
        }
    }
    
    public void showMainWindow(){
        MainWindowController mainWindowController;
        mainWindowController = new MainWindowController();
        
        MainWindow mainWindow = mainWindowController.getMainWindowCreate();
        mainWindow.setVisible(true);
    }

}