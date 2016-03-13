/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

/**
 *
 * @author FDuarte
 */
public class ComboBoxItem {
    private int key;
    private String value;

    public ComboBoxItem() {
    }

    public ComboBoxItem(int key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getKey()
    {
        return this.key;
    }

    public String getValue()
    {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }
    
    
}
