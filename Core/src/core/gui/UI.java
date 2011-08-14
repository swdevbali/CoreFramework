/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core.gui;

import core.*;

import core.system.DB;
import javax.swing.JComboBox;

/**
 *
 * @author irul
 */
public class UI {
    
    private static UI instance = null;
    
    private UI(){
        
    }
    public static UI getInstance(){
        if(instance==null) instance = new UI();
        return instance;
    }
    
    public void fillComboBox(JComboBox cbo, String query) {
        String[][] berkas = DB.getInstance().getDataSet(query,false);
        cbo.removeAllItems();
        for (int i = 0; i < berkas.length; i++) {
            cbo.addItem(berkas[i][0]);
        }
        if (cbo.getItemCount() > 0) {
            cbo.setSelectedIndex(0);
        }
    }
}
