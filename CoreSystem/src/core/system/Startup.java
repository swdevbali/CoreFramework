package core.system;

import core.auth.Authentication;
import java.awt.Image;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author 03523202 (originally), now PRAM WEE develop it. Jazaakallah
 * Idenya adalah menggunakan singleton.
 * TODO : coba buat nanati pny super frame berupa kelas khusus, atau menggunakan containment aja.
 */
public class Startup {

    private static final SplashFrame frameSplash = new SplashFrame();
    private static final Thread threadSplash = new Thread(frameSplash);
    private static Startup singleton;
    public static boolean BYPASS_PASSWORD = true;
    public static final String NIMBUS_LAF = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
    public static final String ALLOY_LAF = "com.incors.plaf.alloy.AlloyLookAndFeel";
    public static final String SYSTEM_LAF = UIManager.getSystemLookAndFeelClassName();
    private String lookAndFeel = SYSTEM_LAF;

    public String getLookAndFeel() {
        return lookAndFeel;
    }

    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
        if (lookAndFeel.equals(ALLOY_LAF)) {
            com.incors.plaf.alloy.AlloyLookAndFeel.setProperty("alloy.licenseCode", "v#ej_technologies#uwbjzx#e6pck8");
        }
    }

    public Image createImage(URL imageURL, String description) {


        if (imageURL == null) {
            System.err.println("Resource not found: " + imageURL);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
    private boolean isAuthenticatedUser = false;
    private JFrame mainFrame = null;
    private String userName;
    private boolean userAdmin;

    public static Startup getInstance() {
        if (singleton == null) {
            singleton = new Startup();
        }
        return singleton;
    }
    private int berkasId;
    private int userId;
    private String namaBerkas;
    private boolean displaySplash = true;

    public int getBerkasID() {
        return berkasId;
    }

    public boolean getIsAuthenticatedUser() {
        return isIsAuthenticatedUser();
    }

    public boolean authenticate(String userName, char[] sandi) {
        String stringSandi = "";
        for (int i = 0; i < sandi.length; i++) {
            stringSandi += sandi[i];
            sandi[i] = '0';
        }
        final String sql = "select " + LoginDialog.idpemakai + ",nama,peran from " + "pemakai where nama=\'" + userName + "\' and password=\'" + stringSandi + "\'";
        System.out.println(sql);
        String[][] data = DB.getInstance().getDataSet(sql, false);

        if (data.length == 0) {
            setIsAuthenticatedUser(false);
        } else {

            setIsAuthenticatedUser(true);
            Startup.getInstance().setUserName(userName);
            Startup.getInstance().setUserId(Integer.parseInt(data[0][0]));
            Authentication.getInstance().getUserInfo().setRole(data[0][2]);



        }
        return isIsAuthenticatedUser();
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public String getNamaBerkas() {
        return this.namaBerkas;
    }

    public void setDisplaySplash(boolean b) {
        this.displaySplash = b;
    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        mainFrame.setTitle(ApplicationInfo.OFFICIAL_NAME + " version " + ApplicationInfo.VERSION + " by " + ApplicationInfo.AUTHOR);
    }

    public String getUserName() {
        return userName;
    }

    /** Creates a new instance of Main */
    private Startup() {
    }

    public void setSplashIcon(ImageIcon imageIcon) {
        frameSplash.splashIcon = imageIcon;
    }

    public void setUserName(String userId) {
        this.userName = userId;
    }

    public boolean isIsAuthenticatedUser() {
        return isAuthenticatedUser;
    }

    public void setIsAuthenticatedUser(boolean isAuthenticatedUser) {
        this.isAuthenticatedUser = isAuthenticatedUser;
    }

    public void logout() {
        isAuthenticatedUser = false;
        // -- TODO : event broadcast u/ after logout dan jg ... after Login ya ...
    }

    public void start() {
        //   try {
        //       UIManager.setLookAndFeel(Startup.getInstance().getLookAndFeel());
        if (singleton.getMainFrame() == null) {
            JOptionPane.showMessageDialog(null, "Anda belum mengatur MainFrame Aplikasi, akan menggunakan default", "Core", JOptionPane.INFORMATION_MESSAGE);
            Startup.getInstance().setMainFrame(MainFrame.getInstance());
        }
        LoggingWindow.getInstance().addToLog("Loading Splash Screen");
        if (displaySplash) {
            loadSplashScreen();
        }
        frameSplash.dispose();

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                LoggingWindow.getInstance().addToLog("Login");
                LoginDialog.getInstance().setVisible(true);
                if (LoginDialog.getInstance().getReturnStatus() == LoginDialog.RET_OK) {
                    Startup.getInstance().getMainFrame().setVisible(true);
                }
            }
        });

        /*  } catch (ClassNotFoundException ex) {
        Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
        Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
        Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
        Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

    protected void afterUserLogout() {
    }

    public static void main(String[] args) {
        getInstance().start();
    }

    protected static void loadSplashScreen() {
        threadSplash.start();
        while (!frameSplash.isShowing()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(null,
                        "Kesalahan dalam menampilkan splash : "
                        + e.getMessage());
            }
        }
    }

    boolean isUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(String userAdmin) {
        // System.out.println(userAdmin);
        if (userAdmin.equals("0")) {
            this.userAdmin = false;
        } else {
            this.userAdmin = true;
        }
    }

    public void debug(String dariMana, String message) {
        /*if (JOptionPane.showConfirmDialog(Startup.getInstance().getMainFrame(),
        "Berasal dari kode :" + dariMana + "\n\n'" + message + "'\n\nShutdown aplikasi?",
        "Terjadi Kesalahan",
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        Startup.getInstance().initiateShutdow();
        System.exit(0);
        }*/
        Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, null, dariMana + " : " + message);
    }

    public void initiateShutdow() {
        // TODO : buat Database connection jd singleton dan close disini ya
    }

    public int toInt(boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean fromInt(int i) {
        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAllowed() {
        try {
            if (!Startup.getInstance().getIsAuthenticatedUser()) {
                throw new Exception("Anda harus login terlebih dahulu");
            }
            if (!Startup.getInstance().isUserAdmin()) {
                throw new Exception("Anda tidak berhak mengakses fungsi ini");
            }
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Startup.getInstance().getMainFrame(), ex.getMessage());
            return false;
        }

    }

    public void setBerkasID(int berkasId) {
        this.berkasId = berkasId;
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setNamaBerkas(String namaBerkas) {
        this.namaBerkas = namaBerkas;
    }

    public void shutdown() {
        DB.getInstance().shutdownDatabase();
        System.exit(0);
    }

    public void setUIFont(FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
