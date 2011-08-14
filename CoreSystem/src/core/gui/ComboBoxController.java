/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui;

import core.*;

import core.system.DB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author PRAM WEE
 * pokoknya bs ngebuatin referential integrity via ComboBox... dikit2
 */
public class ComboBoxController {

    Vector<Object> selectedIndexToAutoId = new Vector();
    HashMap<Object, Object> autoIdToSelectedIndex = new HashMap();
    private JComboBox comboBox;
    private String sqlSelect;

    public ComboBoxController(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    public int SizeOfSelectedIndexToAutoId() {
        return selectedIndexToAutoId.size();
    }

    public void addAutoId(Object autoId) {
        selectedIndexToAutoId.add(autoId);
    }

    public void clear() {
        autoIdToSelectedIndex.clear();
        selectedIndexToAutoId.removeAllElements();
    }

    public Object getAutoIdFromSelectedIndex() {
        return getAutoIdFromSelectedIndex(comboBox.getSelectedIndex());
    }

    public Object getAutoIdFromSelectedIndex(int selectedIndex) {
        return selectedIndexToAutoId.get(selectedIndex);
    }

    public Object getSelectedIndexFromAutoId(int index) {
        return autoIdToSelectedIndex.get(index);//.intValue();
    }

    public Object getSelectedIndexFromAutoId(String index) {
        if (index == null || "".equals(index)) {
            return -1;
        }
        //return autoIdToSelectedIndex.get(Integer.parseInt(index)).intValue();
        return autoIdToSelectedIndex.get(index);
    }

    public String getText() {
        return "" + comboBox.getItemAt(comboBox.getSelectedIndex());
    }

    public boolean isEmpty() {
        //Main.getSingleton().debug("comboBoxController.isEmpty", "selectedIndexToAutoId :" + selectedIndexToAutoId.size() + ",autoIdToSelectedIndex :" + autoIdToSelectedIndex.size());
        return selectedIndexToAutoId.size() <= 0 || autoIdToSelectedIndex.size() <= 0 || comboBox.getSelectedIndex() < 0;
    }


    public void loadData(String string) {
        this.sqlSelect = string;
        ResultSet rs;
        rs = DB.getInstance().selectQuery(string);
        try {
            int oldIndex = comboBox.getSelectedIndex();
            if (oldIndex < 0) {
                oldIndex = 0;
            }
            comboBox.removeAllItems();
            clear();

            int i = 0;
            // -- disini ada ide u/ postpone selection. Mungkin salah
            while (rs.next()) {
                comboBox.addItem(rs.getString(2));// -- "nama"
                putAutoIdWithIndex(rs.getObject(1), i); // "auto_id"
                addAutoId(rs.getObject(1));
                i++;
            }

            if (comboBox.getItemCount() > 0) {
                if (oldIndex > 0 && oldIndex < comboBox.getItemCount()) {
                    comboBox.setSelectedIndex(oldIndex);
                }
            }
        } catch (SQLException ex) {
            //Startup.getInstance().debug("ComboBoxController.loadData(" + string + ")", ex.getMessage());
            Logger.getLogger(ComboBoxController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void putAutoIdWithIndex(Object autoId, int index) {
        autoIdToSelectedIndex.put(autoId, index);
    }

    public void reload() {
        if (!"".equals(sqlSelect)) {
            loadData(sqlSelect);
        } else {
            //Startup.getInstance().debug("ComboBoxController.reload", "sqlSelect belum diset");
        }
    }

    public void setSelectedIndexToAutoId() {
        comboBox.setSelectedIndex(Integer.parseInt(""+getAutoIdFromSelectedIndex()));
    }

    public void setSelectedIndexToAutoId(String key) {
        if (comboBox.getItemCount() > 0) {
            comboBox.setSelectedIndex(Integer.parseInt(""+getSelectedIndexFromAutoId(key)));
        }
    }
}
