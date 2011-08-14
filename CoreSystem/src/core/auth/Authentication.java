/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.auth;

import core.system.Startup;
import core.system.LoginDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author PRAM WEE
 */
public class Authentication {

    private AuthenticationListener listener;

    public void doLogin() {
        LoginDialog.getInstance().setVisible(true);
    }

    public void doLogout() {
        if(JOptionPane.showConfirmDialog(Startup.getInstance().getMainFrame(),"Apakah Anda benar-benar akan logout?", "Logout Aplikasi", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            Authentication.getInstance().getUserInfo().setLogin(false);
            Authentication.getInstance().getListener().userNotLogin();
        }
    }

    public AuthenticationListener getListener() {
        return listener;
    }
    private static Authentication instance = null;
    private UserInfo userInfo = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    private Authentication() {

    }

    public static Authentication getInstance() {
        if (instance == null) {
            instance = new Authentication();
            instance.userInfo = new UserInfo();
        }
        return instance;
    }

    public void registerListener(AuthenticationListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (!userInfo.isLogin()) {
            listener.userNotLogin();
        } else {
            listener.userLogin();
        }
    }
}
