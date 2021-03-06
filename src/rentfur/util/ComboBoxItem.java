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
    private String key;
    private String value;
    private boolean enable;

    public ComboBoxItem() {
        this.enable = true;
    }
    
    public ComboBoxItem(boolean enable)
    {
        this.enable = enable;
    }

    public ComboBoxItem(String key, String value)
    {
        this.key = key;
        this.value = value;
        this.enable = true;
    }
    
    public ComboBoxItem(String key, String value, boolean enable)
    {
        this.key = key;
        this.value = value;
        this.enable = enable;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey()
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
    
    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    
}
