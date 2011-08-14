/*
 * JDialogEntry.java
 *
 * Created on 26 December 2007, 21:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package core.gui;

import core.system.Startup;
import core.system.DB;
import core.*;
import core.dataview.JCombBoxEx;
import core.dataview.JSliderEx;
import core.dataview.JTextFieldEx;
import core.dataview.JTextPaneEx;
import java.awt.Component;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author PRAM WEE
 * Ini jg mo dijadiin EntryController, tp skrng lg butuh cm u/ DaftarTransaksi... hm.. bener gak nih ya?
 */
public abstract class JDialogEntry extends JDialog {

    private Window parent;
    private String sql;
    private JPanel pnlUtama = null;
    private int rowToBeEdited;
    private String tableName;
    private TableController tableController;

    public void setPnlUtama(JPanel pnlUtama) {
        this.pnlUtama = pnlUtama;
    }

    public int getRowToBeEdited() {
        return rowToBeEdited;
    }

   
    /** Creates a new instance of JDialogEntry */
    public JDialogEntry(java.awt.Window parent, String tableName) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.parent = parent;
        this.tableName = tableName;
        this.rowToBeEdited = -1;
        setTitle("Entry");

    }

    public void setRowToBeEdited(int id) {
        callPrepareEditing();
        this.rowToBeEdited = id;
        System.out.println("ABOUT TO EDIT THIS ROW ID = " + id);
        if (id >= 0) {
            try {
                tableController.getPstmtFillControl().setInt(1, id);
                HashMap[] data = DB.getInstance().getDataSetAsArrayOfMap(tableController.getPstmtFillControl(), false);

                fillControl(data[0]);
            //inputPanel.afterControlFilled(data[0]);
            /*  kalo mau buat spt InputPanel. Lumayan nih Libs.
             *  HashMap[] data = DB.getInstance().getDataSetAsArrayOfMap("select * from " + tableName + " where " + getColumIdName() + " = " + rowId, false);
             *  inputPanel.fillControl(data[0]);
             *  inputPanel.afterControlFilled(data[0]);
             */
            } catch (SQLException ex) {
                Startup.getInstance().debug("Asal", ex.getMessage());
            }
        }
    }
    /* Versi lama yg belum bisa isi field sendiri.
    public void setRowToBeEdited(int id) {
    callPrepareEditing();
    this.rowToBeEdited = id;
    System.out.println("ABOUT TO EDIT THIS ROW ID = " + id);
    if (id >= 0) {
    try {
    Statement stmt = DB.getInstance().getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //prepareCall(selectStatement); //createStatement();
    sql = "select * from " + getTableName() + " where " + tableController.getColumIdName() + "=" + id;
    ResultSet rs = stmt.executeQuery(sql);

    rs.beforeFirst();
    rs.next();
    delegateFillControl(rs);
    } catch (SQLException ex) {
    Startup.getInstance().debug("Asal", ex.getMessage());
    }
    }
    }*/

    public String getTableName() {
        return tableName;
    }

    protected abstract void delegateFillControl(ResultSet rs);

     public void performSimpanOnly() {
        if (cobaSimpan()) {
            tableController.isDirty(true); // -- kalau di batalkan, brarti ini tetap false, jg tak akan direload
            tableController.loadTable();
        }
    }

    public void performSimpan() {
        if (validateAllFields()) {
            performSimpanOnly();
            clearFields();
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Perhatikan kembali form input Anda. Tidak boleh kosong dan nilai data harus sesuai tipenya");
        }
    }

    protected void afterInsert() {
    }

    protected void afterUpdate(int rowToBeEdited) {
    }

    public void callPrepareEditing() {
    }

    public void afterHapus(int rowToBeErased) {
    }

    public void beforeHapus(int rowToBeErased) {
    }

    public void beforeInsert() {
    }

    public void beforeUpdate(int rowToBeEdited) {
    }

    private boolean cobaSimpan() {
        if (rowToBeEdited == -1) {
            beforeInsert();
            DB.getInstance().executeQuery(delegateGetInsertSql());
            afterInsert();
        } else {
            beforeUpdate(rowToBeEdited);
            DB.getInstance().executeQuery(delegateGetUpdateSql(rowToBeEdited));
            afterUpdate(rowToBeEdited);

        }
        // tableController.loadTable();
        return true; // FIX : ini soalnya diganti tak kira success / ndak, ternyata cuman ada result/ndak
    }

    protected abstract String delegateGetInsertSql();

    protected abstract String delegateGetUpdateSql(int rowId);

    protected void clearFields() {
        blankFieldAndFocusFirst();
    }

    public void setTableController(TableController tableController) {
        this.tableController = tableController;
    }

    // -- pilihan lain adalah abstract, shgga di child class harus di redefine
    protected abstract void postInit();

    // -- doubted 
    public void reloadData() {
        postInit();
        setRowToBeEdited(rowToBeEdited);
    }

    private void blankFieldAndFocusFirst() {
        Component[] cmps = getPnlUtama().getComponents();
        JTextFieldEx txtField;
        JTextPaneEx txtPane;
        JScrollPane scroll;
        Component cmp;

        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextFieldEx) {
                txtField = (JTextFieldEx) cmps[i];
                txtField.setText("");
                if (txtField.isFocusFirst()) {
                    txtField.requestFocus();
                }
            } else if (cmps[i] instanceof JScrollPane) {
                scroll = (JScrollPane) cmps[i];
                cmp = scroll.getViewport().getComponent(0);
                if (cmp instanceof JTextPaneEx) {
                    txtPane = (JTextPaneEx) cmp;
                    txtPane.setText("");
                    if (txtPane.isFocusFirst()) {
                        txtPane.requestFocus();
                    }
                }
            }
        }

    }

    public void perfromLanjut() {
        if (cobaSimpan()) {
            setRowToBeEdited(-1);
            tableController.isDirty(true);
            tableController.loadTable();
            clearFields();

        }


    }

    public TableController getTableController() {
        return tableController;
    }

    public boolean validateAllFields() {
        return true;
    }

    public boolean isInsertMode() {
        return rowToBeEdited == -1;
    }

    private void fillControl(HashMap hashMap) {
        Component cmps[] = getPnlUtama().getComponents();

        String key;
        JScrollPane scroll;
        Component cmp;
        JTextPaneEx txtPane;
        for (int i = 0; i < cmps.length; i++) {
            if (cmps[i] instanceof JTextFieldEx) {
                JTextFieldEx txtField = (JTextFieldEx) cmps[i];
                key = txtField.getFieldName().toUpperCase();
                //System.out.println("fillControl : " + key);
                if (!txtField.isManuallyFilled()) {
                    txtField.setText("" + hashMap.get(key));
                }
            } else if (cmps[i] instanceof JCombBoxEx) {
                JCombBoxEx cbo = (JCombBoxEx) cmps[i];
                key = cbo.getFieldName().toUpperCase();
                //System.out.println("fillControl : " + key);
                if (!cbo.isManuallyFilled()) {
                    if (cbo.isKeyFromText()) {
                        cbo.setSelectedIndexToLabel(cbo.getText());
                    } else {
                        cbo.setSelectedIndexToAutoId("" + hashMap.get(key));
                    }
                }
            } else if (cmps[i] instanceof JSliderEx) {
                JSliderEx sld = (JSliderEx) cmps[i];
                key = sld.getFieldName().toUpperCase();
                //System.out.println("fillControl : " + key);
                if (!sld.isManuallyFilled()) {
                    sld.setValue(Integer.parseInt("" + hashMap.get(key)));
                }
            } else if (cmps[i] instanceof JScrollPane) {
                scroll = (JScrollPane) cmps[i];
                cmp = scroll.getViewport().getComponent(0);
                if (cmp instanceof JTextPaneEx) {
                    txtPane = (JTextPaneEx) cmp;
                    key = txtPane.getFieldName().toUpperCase();
                    if (!txtPane.isManuallyFilled()) {
                        txtPane.setText(hashMap.get(key) + "");
                    }
                }
            }
        }

    }

    private JPanel getPnlUtama() {
        return pnlUtama;
    }
}
