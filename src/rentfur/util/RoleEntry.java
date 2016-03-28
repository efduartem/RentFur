/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;
import javax.swing.ImageIcon;

/**
 *
 * @author FDuarte
 */
public class RoleEntry {
    private final String name;
    private ImageIcon image;

    public RoleEntry(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public ImageIcon getImage() {
      if (image == null) {
        image = new ImageIcon(getClass().getResource("/rentfur/button/image/util/bullet_arrow_right.png"));
      }
      return image;
    }

    // Override standard toString method to give a useful result
    public String toString() {
      return name;
    }
}
