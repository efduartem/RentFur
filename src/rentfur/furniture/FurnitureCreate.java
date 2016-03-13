/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.furniture;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author hp
 */
public class FurnitureCreate extends JInternalFrame{
    private final FurnitureController furnitureController;
    private final JPanel furnitureCreatePanel;
    private final JLabel codeLabel;
    
    public FurnitureCreate(FurnitureController furnitureController){
        this.furnitureController = furnitureController;
        
        furnitureCreatePanel = new JPanel();
        
        furnitureCreatePanel.setLayout(null);

        codeLabel = new JLabel("Codigo:");
        codeLabel.setBounds(30, 20, 80, 25);
        furnitureCreatePanel.add(codeLabel);
        
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
