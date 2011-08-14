package core.system;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author  03523202
 * email    klaten202@yahoo.com
 */
public class SplashFrame extends JWindow implements Runnable{
    public static ImageIcon splashIcon = null;
    JLabel SplashLabel;
    public void run(){
        this.setBackground(new Color(255,255,255));
        if(splashIcon == null){
             SplashLabel = new JLabel(new ImageIcon(getClass().getResource("/core/images/coreLogo.png")));
        }else{
            SplashLabel = new JLabel(splashIcon);
        }
        
        SplashLabel.setBackground(new Color(255,255,255));
        SplashLabel.setOpaque(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        
        getContentPane().add(SplashLabel,BorderLayout.CENTER);
        
        setSize(splashIcon.getIconWidth(),splashIcon.getIconHeight());//(490,300);
        setLocation((screen.width - splashIcon.getIconWidth())/2,((screen.height-splashIcon.getIconHeight())/2));
        setVisible(true);
    }
}   // akhir class FrameSplash