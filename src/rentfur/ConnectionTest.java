/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rentfur;

import rentfur.util.DbConnectUtil;

import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author FDuarte
 */
public class ConnectionTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection connRentFur;
        connRentFur=DbConnectUtil.getConnection();
     
        if(connRentFur!=null)
        {            
            JOptionPane.showMessageDialog(null, "Conexión Realizada Correctamente", "RentFur", JOptionPane.INFORMATION_MESSAGE);
            //Object[] possibleValues = { "First", "Second", "Third" };
            //JOptionPane.showInputDialog(null, "Choose one", "Input", JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
        }
    }
    
}
