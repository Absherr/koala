/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author ADAM
 */
public class ServerUtils {
    
    public static boolean loginQuery(String pmLogin, String pmPassword,MySql pmConn)
    {
        String lvQuery= "SELECT COUNT(id) FROM auth_user where password = '" + pmPassword +
                "' AND (username = '" + pmLogin + "' OR id = " + pmLogin;
        return true;
    }
    
}
