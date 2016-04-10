/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

import java.util.HashMap;

/**
 *
 * @author FDuarte
 */
public class UserRoles {
    public static HashMap rolesMap = new HashMap();

    public UserRoles() {
    }
    
    public UserRoles(HashMap rolesList) {
        this.rolesMap = rolesList;
    }

    public HashMap getRolesMap() {
        return rolesMap;
    }

    public void setRolesMap(HashMap rolesList) {
        this.rolesMap = rolesList;
    }
}
