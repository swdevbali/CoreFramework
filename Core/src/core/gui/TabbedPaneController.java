package core.gui;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author Eko SW
 */
public class TabbedPaneController {

    private Hashtable<JComponent, JComponent> hashControl = new Hashtable();

    public JTabbedPane getTabUtama() {
        return tabUtama;
    }
    private final JTabbedPane tabUtama;

    public TabbedPaneController(JTabbedPane tabUtama) {
        this.tabUtama = tabUtama;
    }

    public void activate(JComponent component) {
        if (!hashControl.contains(component)) {
            hashControl.put(component, component);
            tabUtama.add(component.getToolTipText(), component);

        }
        tabUtama.setSelectedComponent(component);
    }

    public void closeActiveTab() {
        JComponent c = (JComponent) tabUtama.getSelectedComponent();

        if(c!=null){
            tabUtama.remove(c);
            hashControl.remove(c);
        }
    }
}
