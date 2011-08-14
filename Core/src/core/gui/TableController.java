/*
 * ModelDaftar.java
 *
 * Created on 28 December 2007, 17:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package core.gui;

import core.system.Startup;
import core.system.DB;
import core.*;
import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author PRAM WEE
 */
public class TableController {

    public static final int STATE_BROWSE = 0;
    public static final int STATE_TAMBAH = 1;
    public static final int STATE_SIMPAN = 2;
    public static final int STATE_UBAH = 3;
    public static final int STATE_HAPUS = 4;
    protected JTable tabelDaftar = null;
    private boolean isDirty = true;
    private String sqlSelect = null;
    private String tableName = null;
    private JDialogEntry dialogEntry = null;
    private Component hostComponent = null;
    private HashMap mapIds = new HashMap();
    private PreparedStatement pstmtFillControl = null;

    public boolean isIsIdInteger() {
        return isIdInteger;
    }

    public void setIsIdInteger(boolean isIdInteger) {
        this.isIdInteger = isIdInteger;
    }
    private boolean isIdInteger;

    public PreparedStatement getPstmtFillControl() {
        return pstmtFillControl;
    }

    public void setPstmtFillControl(PreparedStatement pstmtFillControl) {
        this.pstmtFillControl = pstmtFillControl;
    }
    private String rowId;

    public void loadTable(String sql, boolean createColumn) {
        setSqlSelect(sql);
        loadTable(createColumn);
    }

    public void setColumIdName(String columIdName) {
        this.columIdName = columIdName;
    }
    private String columIdName = "id";

    public InputPanel getInputPanel() {
        return inputPanel;
    }

    public void performCancel() {
        inputPanel.performCancel();
    }

    public void performOK() {
        this.inputPanel.performOK();
    }

    public void setInputPanel(InputPanel inputPanel) {
        this.inputPanel = inputPanel;
    }
    private InputPanel inputPanel = null;
    private PreparedStatement prepStmt = null;

    public int getRowId() {
        final Object id = mapIds.get(tabelDaftar.getSelectedRow());
        if (id != null) {
            return Integer.parseInt("" + id); // OR, just return row_id
        } else {
            return -1;
        }
    }

    public int getState() {
        return state;
    }
    private int state;

    /**
     * Versi yg mempergunakan PreparedStatement
     * @param hostComponent
     * @param prepStmt
     * @param jTable1
     * @param inputPanel
     */
    public TableController(Component hostComponent, String tableName, PreparedStatement prepStmt,
            JTable jTable1, core.gui.InputPanel inputPanel, boolean isIdInteger) {
        try {
            initialize(inputPanel, "", tableName, jTable1, hostComponent, isIdInteger);
            this.prepStmt = prepStmt;
        } catch (Exception ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Versi yg mempergunakan Query string biasa
     * @param hostComponent
     * @param tableName
     * @param sqlSelect
     * @param jTable1
     * @param inputPanel
     */
    public TableController(Component hostComponent, String tableName, String sqlSelect,
            JTable jTable1, core.gui.InputPanel inputPanel, boolean isIdInteger) {
        try {
            setColumIdName("id" + tableName);//jika columnid tidak diberi nilai, berikan nilai default dunk
            initialize(inputPanel, sqlSelect, tableName, jTable1, hostComponent, isIdInteger);


        } catch (Exception ex) {
            Startup.getInstance().debug("Asal", ex.getMessage());
        }
    }

    public TableController(Component hostComponent, String tableName, String sqlSelect,
            JTable jTable1, core.gui.InputPanel inputPanel, String colIdName, boolean isIdInteger) {
        try {
            setColumIdName(colIdName);
            initialize(inputPanel, sqlSelect, tableName, jTable1, hostComponent, isIdInteger);


        } catch (Exception ex) {
            Startup.getInstance().debug("Asal", ex.getMessage());
        }
    }

    public void fillInputPanel() {
        if (inputPanel != null) {
            try {
                int row = tabelDaftar.getSelectedRow();
                //System.out.println("xyz" + row);
                rowId = "" + mapIds.get(row);
                inputPanel.setRowId(rowId);
                HashMap[] data;
                int id = -1;
                if (isIdInteger) {

                    if (rowId == null || rowId.equals("null")) {
                        id = -1;
                    } else {
                        id = Integer.parseInt(rowId);
                    }
                    pstmtFillControl.setInt(1, id);
                    data = DB.getInstance().getDataSetAsArrayOfMap("select * from " + tableName + " where " + getColumIdName() + " = " + rowId, false);
                } else {

                    pstmtFillControl.setString(1, rowId);
                    data = DB.getInstance().getDataSetAsArrayOfMap("select * from " + tableName + " where " + getColumIdName() + " = '" + rowId + "'", false);
                }

                if (!rowId.equals("null")) {
                    inputPanel.fillControl(data[0]);
                    inputPanel.afterControlFilled(data[0]);
                }

            } catch (SQLException ex) {
                Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadTable(String sqlSelect) {

        setSqlSelect(sqlSelect);
        loadTable();

    }

    public void performUbahEx() {
        setState(TableController.STATE_UBAH);
    }

    public void performTambahEx() {
        setState(TableController.STATE_TAMBAH);
    }

    public void setPrepStmt(PreparedStatement prepStmt) {
        this.prepStmt = prepStmt;
    }

    public void setState(int state) {
        try {
            this.state = state;
            switch (state) {
                case STATE_BROWSE:
                    if (!"".equals(sqlSelect)) {
                        loadTable();
                    }
                    if (inputPanel != null) {
                        inputPanel.disableAllControls();
                        fillInputPanel();
                    }
                    tabelDaftar.setEnabled(true);
                    break;
                case STATE_TAMBAH:
                    if (inputPanel != null) {
                        inputPanel.setRowToBeEdited("-1");
                        tabelDaftar.setEnabled(false);
                        inputPanel.enableAllControls();
                        inputPanel.initAllControls();
                        inputPanel.clearField();

                    }
                    break;
                case STATE_UBAH:
                    if (inputPanel != null) {
                        inputPanel.setRowToBeEdited("" + mapIds.get(tabelDaftar.getSelectedRow()));
                        tabelDaftar.setEnabled(false);
                        inputPanel.enableAllControls();
                        inputPanel.clearField();
                        fillInputPanel();
                    }
                    break;
                case STATE_SIMPAN:
                    if (inputPanel != null) {
                        if (inputPanel.validateAllControls() && inputPanel.cobaSimpan()) {
                            loadTable();
                            setState(STATE_BROWSE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Cek kembali input Anda. Jangan menginputkan Angka saja atau mengosongkan input");
                        }
                    }
                    break;
                case STATE_HAPUS:
                    performHapus();
                    setState(STATE_BROWSE);
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setTabelDaftar(JTable tabelDaftar) {
        this.tabelDaftar = tabelDaftar;
    }

    public JTable getTabelDaftar() {
        return tabelDaftar;
    }

    public String getSqlSelect() {
        return sqlSelect;
    }

    public void setSqlSelect(String sqlSelect) {
        this.sqlSelect = sqlSelect;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        try {
            this.tableName = tableName;
            //setColumIdName("id" + tableName);//default
            if (!DB.DB.equals("")) {
                pstmtFillControl = DB.getInstance().getConnection().prepareStatement("select * from " + tableName + " where " + getColumIdName() + " = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }

        } catch (SQLException ex) {
            Logger.getLogger(TableController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void isDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }

    public void loadTable(boolean createColumn) {
        if (tabelDaftar != null) {
            if (!"".equals(sqlSelect)) {
                DB.getInstance().loadQueryToTable(tabelDaftar, sqlSelect, mapIds, createColumn);
            } else {
                DB.getInstance().loadQueryToTable(tabelDaftar, prepStmt, mapIds, createColumn);
            }
            isDirty(false);
        } else {
            try {
                throw new Exception("Instan JTabel u/ menampilkan query belum diset");
            } catch (Exception ex) {
                Startup.getInstance().debug("DaftarController.loadTable", ex.getMessage());
            }
        }

        afterLoadTable();
    }

    public void loadTable() {
        int rowLama = tabelDaftar.getSelectedRow();
        loadTable(true);
        if (rowLama > 0) {
            tabelDaftar.getSelectionModel().setSelectionInterval(0, rowLama);

        }
    }

    public void performHapus() {
        if (getTabelDaftar().getSelectedRow() >= 0
                && JOptionPane.showConfirmDialog(Startup.getInstance().getMainFrame(),
                "Akan menghapus data. Anda yakin?", "Menghapus Data",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String rowToBeErased = mapIds.get(getTabelDaftar().getSelectedRow()) + "";
            // System.out.println("XXX" + rowToBeErased);
            if (dialogEntry != null) {
                dialogEntry.beforeHapus(Integer.parseInt(rowToBeErased));
            }
            DB.SUPPRESS_ERROR = true;

            if (dialogEntry != null) {
                dialogEntry.beforeHapus(Integer.parseInt(rowToBeErased));
            }
            if (isIdInteger) {
                DB.getInstance().executeQuery("delete from " + getTableName() + ""
                        + " where " + columIdName + "=" + rowToBeErased);
            } else {
                DB.getInstance().executeQuery("delete from " + getTableName() + ""
                        + " where " + columIdName + "= '" + rowToBeErased + "'");
            }

            DB.SUPPRESS_ERROR = false;
            if (dialogEntry != null) {
                dialogEntry.afterHapus(Integer.parseInt(rowToBeErased));
            }
            int rowLama = tabelDaftar.getSelectedRow();
            loadTable();
            rowLama--;
            if (rowLama < 0) {
                rowLama = 0;
            }
            //loadTable(true);
            if (rowLama >= 0) {
                tabelDaftar.getSelectionModel().setSelectionInterval(0, rowLama);
            }

        }
    }

    public void performTambah() {
        // -- ini setVisible belum di override, karena yg bertngg jwb u/ mengambil/refresh data adalah JDialogDaftar. Boleh kalau mau di jadikan di setVisible

        dialogEntry.postInit();
        dialogEntry.clearFields();
        dialogEntry.setRowToBeEdited(-1);
        //dialogEntry.clearFields ();
        dialogEntry.show(true); // -- modal dialog ya, jd harus menunggu..
        //if (isDirty()) {
        loadTable();
        // TODO : select last item added
        //}
    }

    public void performUbah() {
        if (getTabelDaftar().getSelectedRow() >= 0) {
            dialogEntry.postInit();
            dialogEntry.setRowToBeEdited(Integer.parseInt("" + mapIds.get(getTabelDaftar().getSelectedRow())));
            //dialogEntry.setRowToBeEdited(Integer.parseInt((String) getTabelDaftar().getModel().getValueAt(getTabelDaftar().getSelectedRow(), 0)));
            dialogEntry.show();
            //if (isDirty()) { // -- kalau tidak batal maksudnya -- TOFIX : 
            loadTable();
            // TODO : select last item added
            //}
        }
    }

    public JDialogEntry getDialogEntry() {
        return dialogEntry;
    }

    public void setDialogEntry(JDialogEntry dialogEntry) {
        this.dialogEntry = dialogEntry;
        dialogEntry.setTableController(this);
    }

    // --tidak berguna lagi, karena dia skrg bukan turunan jdialog atau apapun, tp hanya controller... 
    // -- cari cara lain, yg lbh baik, u/ meniru behaviour ini
    public void setVisible(boolean b) {
        if (b) {
            loadTable();
        }
    }

    public void afterLoadTable() {
    }

    protected void eventChangeSqlSelect() {
    }

    public void afterHapus() {
    }

    public String getColumIdName() {
        return columIdName;
    }

    private void initialize(InputPanel inputPanel, String sqlSelect, String tableName, JTable jTable1, Component hostComponent, boolean isIdInteger) throws Exception {
        setTableName(tableName);
        this.sqlSelect = sqlSelect;
        this.hostComponent = hostComponent;
        if (hostComponent == null) {
            throw new Exception("Maaf ya, hostComponent harus diberi nilai pada daftar tabel : " + tableName);
        }
        this.isIdInteger = isIdInteger;
        this.tabelDaftar = jTable1;
        this.inputPanel = inputPanel;
        if (inputPanel != null) {
            inputPanel.setTableController(this);
        }
//            if (!"".equals(sqlSelect)) {
//                loadTable();
//            }
        tabelDaftar.setFillsViewportHeight(true);
        setState(STATE_BROWSE);
    }
}
