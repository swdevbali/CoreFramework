/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.auth;

/**
 *
 * @author PRAM WEE
 */
public class UserInfo {

    private String userName;

    private boolean login = false;

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
    
    public UserInfo(){
        login = false;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    private String password;
    private String role;
}
