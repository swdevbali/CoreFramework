/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.gui;

import core.dataview.JTextFieldEx;
import core.dataview.JCombBoxEx;
import core.dataview.JDateChooser;
import core.dataview.JSliderEx;
import core.dataview.JTextPaneEx;
import core.system.DB;
import core.system.Startup;
import java.awt.Component;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author PRAM WEE
 */
public class InputPanel extends JPanel {

    private String rowId = "-1";
    private String rowToBeEdited;
    private TableController tableController;
    private JTable tblData = null;
    private JPanel pnlUtama = null;

    public InputPanel(){
        try {
            UIManager.setLookAndFeel(Startup.getInstance().getLookAndFeel());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public TableController getTableController() {
        return tableController;
    }

    public String getRowId() {
        return rowId;
    }

    public void beginEdit() {
        try {
            enableAllControls();
            clearField();
        } catch (Exception ex) {
            Logger.getLogger(InputPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void changeActiveAndFocusFirst(boolean flag) throws Exception {
        if (getPnlUtama() == null) {
            throw new Exception("Anda harus menspesifikasikan kontainer utama. Bisa panel atau apa saja");
        }
        Component[] cmp = getPnlUtama().getComponents();
        for (int i = 0; i < cmp.length; i++) {
            if (!(cmp[i] instanceof JLabel)) {
                cmp[i].setEnabled(flag);
                //System.out.println(cmp[i].getName());
                if (cmp[i] instanceof JTextFieldEx || cmp[i] instanceof JCombBoxEx) {
                    if (cmp[i] instanceof JTextFieldEx && ((JTextFieldEx) cmp[i]).isFocusFirst()) {
                        ((JTextFieldEx) cmp[i]).requestFocus();
                    }
                    if (cmp[i] instanceof JCombBoxEx && ((JCombBoxEx) cmp[i]).isFocusFirst()) {
                        ((JCombBoxEx) cmp[i]).requestFocus();
                    }

                }
            }
        }
    }

    public JPanel getPnlUtama() {
        if (pnlUtama == null) {
            return (JPanel) getComponents()[0];
        }
        return pnlUtama;
    }

    public void performCancel() {
        tableController.setState(TableController.STATE_BROWSE);
    }

    public void performOK() {
        tableController.setState(TableController.STATE_SIMPAN);
    }

    public void setPnlUtama(JPanel pnlUtama) {
        this.pnlUtama = pnlUtama;
    }

    public JTable getTblData() {
        return tblData;
    }

    public void setTblData(JTable tblData) {
        this.tblData = tblData;
    }

    public void fillControl(String[][] data) {
    }

    public void performTambah() {
        if (tblData != null) {
            tblData.setEnabled(false);
        }
        blankField();
        clearField();
    }

    public void clearField() {
    }

    public void disableAllControls() throws Exception {
        changeActiveAndFocusFirst(false);
    }

    public void enableAllControls() throws Exception {
        changeActiveAndFocusFirst(true);
    }

    protected String delegateGetInsertSql() {
        Component[] cmps = getFieldComponents();
        String fieldList = "";
        String fieldName = "";
        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextFieldEx || cmps[i] instanceof JCombBoxEx) {
                if (cmps[i] instanceof JTextFieldEx) {
                    fieldName = ((JTextFieldEx) cmps[i]).getFieldName();
                }
                if (cmps[i] instanceof JCombBoxEx) {
                    fieldName = ((JCombBoxEx) cmps[i]).getFieldName();
                }
                fieldList = fieldList + fieldName + ",";
            }
        }
        fieldList = fieldList.substring(0, fieldList.length() - 1);
        String query = "insert into " + tableController.getTableName() + "(" + fieldList + ") values (";
        String value = "";
        String[] token;
        int dataType = 0;

        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextFieldEx || cmps[i] instanceof JCombBoxEx) {
                if (cmps[i] instanceof JTextFieldEx) {
                    value = ((JTextFieldEx) cmps[i]).getText();
                    dataType = ((JTextFieldEx) cmps[i]).getDataType();
                } else if (cmps[i] instanceof JCombBoxEx) {
                    value = "" + ((JCombBoxEx) cmps[i]).getAutoIdFromSelectedIndex();
                    dataType = ((JCombBoxEx) cmps[i]).getDataType();
                }

                switch (dataType) {
                    case core.dataview.CoreConstants.STRING:
                        query = query + "'" + value + "',";
                        break;
                    case core.dataview.CoreConstants.INTEGER:
                        query = query + value + ",";
                        break;
                }
            }
        }
        query = query.substring(0, query.length() - 1);
        query = query + ")";
        //System.out.println(query);
        return query;
    }

    protected String delegateGetUpdateSql(String rowId) {
        Component[] cmps = getFieldComponents();
        String query = "UPDATE " + tableController.getTableName() + " SET ";
        String value = "";
        int dataType = 0;
        String fieldName = "";
        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextFieldEx || cmps[i] instanceof JCombBoxEx) {
                if (cmps[i] instanceof JTextFieldEx) {
                    fieldName = ((JTextFieldEx) cmps[i]).getFieldName();
                    value = ((JTextFieldEx) cmps[i]).getText();
                    dataType = ((JTextFieldEx) cmps[i]).getDataType();
                } else if (cmps[i] instanceof JCombBoxEx) {
                    fieldName = ((JCombBoxEx) cmps[i]).getFieldName();
                    value = "" + ((JCombBoxEx) cmps[i]).getAutoIdFromSelectedIndex();
                    dataType = ((JCombBoxEx) cmps[i]).getDataType();
                }

                switch (dataType) {
                    case core.dataview.CoreConstants.STRING:
                        query = query + fieldName + "='" + value + "',";
                        break;
                    case core.dataview.CoreConstants.INTEGER:
                        query = query + fieldName + "=" + value + ",";
                        break;
                }
            }
        }
        query = query.substring(0, query.length() - 1);
        query = query + " WHERE ID =" + rowId;
        return query;
    }

    protected void afterInsert() {
    }

    protected void afterUpdate(String rowToBeEdited) {
    }

    public boolean cobaSimpan() {
        if (rowToBeEdited.equals("-1")) {
            beforeInsert();
            DB.getInstance().executeQuery(delegateGetInsertSql());//delegateGetInsertSql()
            afterInsert();
        } else {
            beforeUpdate(rowToBeEdited);
            DB.getInstance().executeQuery(delegateGetUpdateSql(rowToBeEdited));
            afterUpdate(rowToBeEdited);
        }
        return true; // FIX : ini soalnya diganti tak kira success / ndak, ternyata cuman ada result/ndak
    }

    public void afterControlFilled(HashMap hashMap) {
    }

    void fillControl(HashMap hashMap) {
        Component cmps[] = getPnlUtama().getComponents();


        String key;
        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextPaneEx) { //not working here..
                JTextPaneEx txtField = (JTextPaneEx) cmps[i];
                key = txtField.getFieldName().toUpperCase();
                System.out.println("fillControl JTextPaneEx : " + key);
                if (!txtField.isManuallyFilled()) {
                    txtField.setText("" + hashMap.get(key));
                }
            } else if (cmps[i] instanceof JTextFieldEx) {
                JTextFieldEx txtField = (JTextFieldEx) cmps[i];
                key = txtField.getFieldName().toUpperCase();
                System.out.println("fillControl JTextFieldEx : " + key);
                if (!txtField.isManuallyFilled()) {
                    txtField.setText("" + hashMap.get(key));
                }
            } else if (cmps[i] instanceof JCombBoxEx) {
                JCombBoxEx cbo = (JCombBoxEx) cmps[i];
                key = cbo.getFieldName().toUpperCase();
                System.out.println("fillControl JCombBoxEx : " + key + ", value =" +hashMap.get(key));
                if (!cbo.isManuallyFilled()) {
                    cbo.setSelectedIndexToAutoId("" + hashMap.get(key));
                }
            } else if (cmps[i] instanceof JSliderEx) {
                JSliderEx sld = (JSliderEx) cmps[i];
                key = sld.getFieldName().toUpperCase();
                //System.out.println("fillControl : " + key);
                if (!sld.isManuallyFilled()) {
                    sld.setValue(Integer.parseInt("" + hashMap.get(key)));
                }
            } else if (cmps[i] instanceof JDateChooser) {
                JDateChooser sld = (JDateChooser) cmps[i];
                key = sld.getFieldName().toUpperCase();
                //System.out.println("fillControl : " + key);
                //if (!sld.isManuallyFilled()) {
                    sld.setText(hashMap.get(key)+"");
                //}
            }
        }
    }

    void initAllControls() throws Exception {
        if (getPnlUtama() == null) {
            throw new Exception("Anda harus menspesifikasikan kontainer utama. Bisa panel atau apa saja");
        }
        Component[] cmp = getPnlUtama().getComponents();
        for (int i = 0; i < cmp.length; i++) {
            if ((cmp[i] instanceof JTextFieldEx)) {
                JTextFieldEx txtField = (JTextFieldEx) cmp[i];
                txtField.setText("");


            } else if ((cmp[i] instanceof JCombBoxEx)) {
                JCombBoxEx cbo = (JCombBoxEx) cmp[i];
                cbo.setSelectedIndex(0);
                if (cbo.isFocusFirst()) {
                    cbo.requestFocus();
                }
            }
        }
    }

    public void setTableController(TableController tableController) {
        this.tableController = tableController;
    }

    public boolean validateAllControls() {
        return true;
    }

    public void setRowToBeEdited(String i) {
        this.rowToBeEdited = i;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public void beforeUpdate(String rowToBeEdited) {
    }

    public void beforeInsert() {
    }

    private void blankField() {
        Component[] cmps = getPnlUtama().getComponents();
        JTextField txtField;

        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextField) {
                txtField = (JTextField) cmps[i];
                txtField.setText("");
            }
        }
    }

    private Component[] getFieldComponents() {
        Component[] cmps;
        if (pnlUtama != null) {
            cmps = getPnlUtama().getComponents();
        } else {
            cmps = getComponents();
        }
        return cmps;
    }
}
