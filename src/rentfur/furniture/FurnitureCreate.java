/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author hp
 */
public class FurnitureCreate extends JInternalFrame{
    private final FurnitureController furnitureController;
    private final JPanel furnitureCreatePanel;
    private final JLabel codeLabel;
    private final JLabel descriptionLabel;
    private final JLabel familyLabel;
    private final JLabel unitPriceLabel;
    private final JTextField codeTextField;
    private final JTextField descriptionTextField;
    private final JTextField familyTextField;
    private final JTextField unitPriceTextField;
    JFormattedTextField numformatted;
    
    public FurnitureCreate(FurnitureController furnitureController){
        this.furnitureController = furnitureController;
        
        furnitureCreatePanel = new JPanel();
        
        furnitureCreatePanel.setLayout(null);

        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 20, 80, 25);
        furnitureCreatePanel.add(codeLabel);
        
        codeTextField = new JTextField(20);
        codeTextField.setBounds(120, 20, 160, 25);
        furnitureCreatePanel.add(codeTextField);
        
        descriptionLabel = new JLabel("Descripcion");
        descriptionLabel.setBounds(30,50, 80, 25);
        furnitureCreatePanel.add(descriptionLabel);
        
        descriptionTextField = new JTextField(20);
        descriptionTextField.setBounds(120, 50, 160, 25);
        furnitureCreatePanel.add(descriptionTextField);
        
        familyLabel = new JLabel("Familia Mobiliario");
        familyLabel.setBounds(30,80, 80, 25);
        furnitureCreatePanel.add(familyLabel);
        
        familyTextField = new JTextField();
        familyTextField.setBounds(120, 80, 160, 25);
        furnitureCreatePanel.add(familyTextField);
                
        unitPriceLabel = new JLabel("Precio Unitario");
        unitPriceLabel.setBounds(30,120, 80, 25);
        furnitureCreatePanel.add(unitPriceLabel);
        
        unitPriceTextField = new JTextField(20); //debe ser solo numeric
        unitPriceTextField.setBounds(120, 120, 160, 25);
        unitPriceTextField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();

                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b' /*corresponde a BACK_SPACE*/)) {
                e.consume(); // ignorar el evento de teclado
                }
            }
        });
        furnitureCreatePanel.add(unitPriceTextField);
        
        
        
        
        
        add(furnitureCreatePanel);
        pack();
        
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setTitle("Crear Familia de Mobiliario");
        setBounds(200,100,400,200);

        setVisible(true);
    }
}
