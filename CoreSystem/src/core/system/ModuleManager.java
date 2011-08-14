/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.system;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 *
 * @author PRAM WEE
 */
public class ModuleManager {

    private HashMap<String,Component> moduleMap = new HashMap<String,Component>();
    public static ModuleManager getInstance() {
        if(instance == null) instance = new ModuleManager();
        return instance;
    }
    private ModuleManager(){}
    private static ModuleManager instance = null;

    public void addHome(Component instance) throws ModuleExistException {
        addModule("HOME",instance);
    }

    public void addModule(String moduleKey, Component module) throws ModuleExistException {
        if(moduleMap.containsKey(moduleKey)) throw new ModuleExistException();
        moduleMap.put(moduleKey.toUpperCase(),module);
    }

    public Component getModule(String moduleKey) {
        Component module = moduleMap.get(moduleKey.toUpperCase());
        if(module==null) {
            JLabel lblUnderConstruction;
            lblUnderConstruction = new JLabel("Module \"" + moduleKey + "\" belum didefinisikan");
            lblUnderConstruction.setIcon(new ImageIcon(getClass().getResource("/core/images/UnderConstruction.png")));
            module = lblUnderConstruction;
        }
        return module;
        
    }
}
