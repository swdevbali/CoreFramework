package core.dataview;

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
 * @author  PRAM WEE
 */
public class JCombBoxEx extends JComboBox implements CoreConstants {

    Vector<Integer> selectedIndexToAutoId = new Vector();
    HashMap<Integer, Integer> autoIdToSelectedIndex = new HashMap(); //kalau mau umum, Integer jd Object lho... ^^
    private String fieldName;
    private int dataType = INTEGER;
    private boolean focusFirst;
    private boolean pendingLoad = false;
    private boolean manuallyFilled = false;
    private boolean keyFromText = false;

    public boolean isKeyFromText() {
        return keyFromText;
    }

    public void setKeyFromText(boolean keyFromText) {
        this.keyFromText = keyFromText;
    }

    public boolean isManuallyFilled() {
        return manuallyFilled;
    }

    public void setManuallyFilled(boolean manuallyFilled) {
        this.manuallyFilled = manuallyFilled;
    }

    public boolean isPendingLoad() {
        return pendingLoad;
    }

    public void setPendingLoad(boolean pendingLoad) {
        this.pendingLoad = pendingLoad;
    }

    public boolean isFocusFirst() {
        return focusFirst;
    }

    public void setFocusFirst(boolean focusFirst) {
        this.focusFirst = focusFirst;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getSqlSelect() {
        return sqlSelect;
    }

    public void setSelectedIndexToLabel(String label) {
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemAt(i).equals(label)) {
                setSelectedIndex(i);
                break;
            }
        }
    }

    public void setSqlSelect(String sqlSelect) {
        this.sqlSelect = sqlSelect;
        if (!"".equals(sqlSelect)) {
            loadData(sqlSelect);
        }
    }
    String sqlSelect;

    /** Creates new form BeanForm */
    public JCombBoxEx() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public int SizeOfSelectedIndexToAutoId() {
        return selectedIndexToAutoId.size();
    }

    public void addAutoId(Integer autoId) {
        selectedIndexToAutoId.add(autoId);
    }

    public void clear() {
        autoIdToSelectedIndex.clear();
        selectedIndexToAutoId.removeAllElements();
    }

    public String getAutoIdFromSelectedIndex() {
        if (getSelectedIndex() == -1) {
            return "NULL";
        }
        return getAutoIdFromSelectedIndex(getSelectedIndex()) + "";
    }

    public Object getAutoIdFromSelectedIndex(int selectedIndex) {
        return selectedIndexToAutoId.get(selectedIndex);
    }

    public Object getSelectedIndexFromAutoId(int index) {
        return autoIdToSelectedIndex.get(index);//.intValue();
    }

    public int getSelectedIndexFromAutoId(String index) {
        if (index == null || "".equals(index)) {
            return -1;
        }

        System.out.println(index);
      //  System.out.println("FIX " + index + "," + autoIdToSelectedIndex.get(Integer.parseInt(index)));
        return Integer.parseInt("" + autoIdToSelectedIndex.get(Integer.parseInt(index)));//dulu Integer.parseInt(
    }

    public String getText() {
        return "" + getItemAt(getSelectedIndex());
    }

    public boolean isEmpty() {
        //Main.getSingleton().debug("comboBoxController.isEmpty", "selectedIndexToAutoId :" + selectedIndexToAutoId.size() + ",autoIdToSelectedIndex :" + autoIdToSelectedIndex.size());
        return selectedIndexToAutoId.size() <= 0 || autoIdToSelectedIndex.size() <= 0 || getSelectedIndex() < 0;
    }

    public void loadData(String string) {
        this.sqlSelect = string;
        ResultSet rs;
        pendingLoad = true;
        if (!DB.DB.equals("")) {
            rs = DB.getInstance().selectQuery(string);
            try {
                int oldIndex = getSelectedIndex();
                if (oldIndex < 0) {
                    oldIndex = 0;
                }
                removeAllItems();
                clear();

                int i = 0;
                // -- disini ada ide u/ postpone selection. Mungkin salah
                while (rs.next()) {
                    addItem(rs.getString(2));// -- "nama"
                    putAutoIdWithIndex(new Integer(rs.getInt(1)), i); // "auto_id"
                    //putAutoIdWithIndex(rs.getObject(1), i); // "auto_id"
                    //addAutoId(rs.getObject(1));
                    addAutoId(new Integer("" + rs.getObject(1)));
                    i++;
                }

                if (getItemCount() > 0) {
                    if (oldIndex > 0 && oldIndex < getItemCount()) {
                        setSelectedIndex(oldIndex);
                    }
                }
                pendingLoad = false;
            } catch (SQLException ex) {
                //Startup.getInstance().debug("ComboBoxController.loadData(" + string + ")", ex.getMessage());
                Logger.getLogger(JCombBoxEx.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void putAutoIdWithIndex(Integer autoId, int index) {
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
        setSelectedIndex(Integer.parseInt(getAutoIdFromSelectedIndex()));
    }

    public void setSelectedIndexToAutoId(String key) {
        if (getItemCount() > 0) {
            System.out.println(" setSelectedIndexToAutoId " + key);
            setSelectedIndex(getSelectedIndexFromAutoId(key));
        }
    }
}
