package core.system;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DB {

    public static boolean USE_MYSQL = true; //defaulted to ODBC DSN
    public static boolean USE_MSSQL = false;
    private static Connection connectiondb;
    public static boolean USE_ODB = false;
    public static String ODB_FILE;
    public static String ODB_TEMP;
    private Statement statement;
    private static DB singleton;
    //mysql specific
    public static String HOST = "localhost";
    public static String PORT = "3306";
    public static String DB = "";
    //acess can be too
    public static String USER_NAME = "root";
    public static String PASSWORD = "";
    public static boolean SUPPRESS_ERROR = false;

    public static DB getInstance() {
        if (singleton == null) {
            singleton = new DB();
        }
        return singleton;
    }
    private String syntax;

    private DB() {
    }

    public void dispose() {
        singleton = null;
    }

    public String findId(String table, String kolom, String where) {
        String[][] data;
        if (!"".equals(where)) {
            data = getDataSet("select " + kolom + " from " + table + " where " + where, false);
        } else {
            data = getDataSet("select " + kolom + " from " + table, false);
        }

        if (data.length == 0) {
            return "";
        } else {
            return data[0][0];
        }
    }

    public int findValueAsInt(String table, String value, String where) {
        String val = findValue(table, value, where);
        if (val.equals("")) {
            return 0;
        }
        return Integer.parseInt(val);
    }

    public String findValue(String table, String value, String where) {
        String sql = "select " + value + " from " + table + " where " + where;
        //System.out.println("findValue:" + sql);
        String[][] data = getDataSet(sql, false);
        if (data.length == 0) {
            return "";
        } else {
            return data[0][0];
        }
    }

    public Connection getConnection() {
        if (connectiondb == null) {
            LoggingWindow.getInstance().addToLog("uuu database null");
            LoggingWindow.getInstance().addToLog("***" + "Opening new Database Connection" + "Database");
            try {
                if (USE_MYSQL) {
                    LoggingWindow.getInstance().addToLog("***" + "Using MySQL :" + DB + "Database");
                    Class.forName("com.mysql.jdbc.Driver");
                } else if (USE_MSSQL) {
                    LoggingWindow.getInstance().addToLog("***" + "Using MSSQL: " + DB + "Database");
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                } else if (USE_ODB) {
                    Class.forName("org.hsqldb.jdbcDriver");
                } else {
                    LoggingWindow.getInstance().addToLog("***" + "Using Microsoft JDBC ODBC Bridge : " + DB + "Database");
                    Class.forName("sun.jdbc.odbc.JdbcOdbc");
                }

            } catch (ClassNotFoundException cnfe) {
                JOptionPane.showMessageDialog(null, "Error Loading Driver: " + cnfe);
            }
            String urldb = "";
            try {
                urldb = getUrl();
                if (USE_MYSQL) {
                    connectiondb = DriverManager.getConnection(urldb, USER_NAME, PASSWORD);

                } else if (USE_MSSQL) {
                    connectiondb = DriverManager.getConnection(urldb, USER_NAME, PASSWORD);
                } else if (USE_ODB) {
                    connectiondb = DriverManager.getConnection(urldb, USER_NAME, PASSWORD);
                } else {
                    connectiondb = DriverManager.getConnection(urldb);//
                }
                LoggingWindow.getInstance().addToLog("***" + "Connection Established", "Database");
                //JOptionPane.showMessageDialog(null, "Koneksi terbuka");
            } catch (Exception e) {
                final String errMsg = "Error Database: Tidak dapat tersambung ke Basis data : " + DB + "\n" + e.getMessage() + "\n" + urldb;
                JOptionPane.showMessageDialog(Startup.getInstance().getMainFrame(), errMsg);
                System.out.println(errMsg);

            }
        }
        return connectiondb;
    }

    public void closeConnection() {

        try {
            getConnection().close();
            connectiondb = null;
        } catch (Exception e) {
            LoggingWindow.getInstance().addToLog(e.getMessage());
        }
    }

    public void closeConnection(boolean force) {
        if (force) {
            try {
                connectiondb.close();
                LoggingWindow.getInstance().addToLog("***" + "Connection Closed" + "Database");
            } catch (Exception e) {
                Startup.getInstance().debug("Asal", "Database.ConnectMySQL : " + e.getMessage());
            }
        }
    }

    public boolean emptySelectQuery(String sql) {
        try {
            ResultSet rs = selectQuery(sql);
            return !rs.first();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }

    }

    public HashMap[] getDataSetAsArrayOfMap(PreparedStatement prepStmtFillControl, boolean b) {
        try {
            LoggingWindow.getInstance().addToLog("***" + "[getDataSet]: PreparedStatement = " + prepStmtFillControl + "Database");
            return processDataSetResultSetAsArrayOfHashMap(prepStmtFillControl.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    // belum pakai b apakah callable, in turn, masih banyakan pakai sql aja
    public HashMap[] getDataSetAsArrayOfMap(String sqlSelect, boolean b) {
        try {
            Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return processDataSetResultSetAsArrayOfHashMap(stmt.executeQuery(sqlSelect));
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getDataSetAsHTMLTable(String selectQuery, boolean isCallable) {
        HashMap[] s = getDataSetAsArrayOfMap(selectQuery, isCallable);
        return toHTMLTable(s);
    }

    public String getDataSetAsList(String selectQuery, boolean isCallable) {
        HashMap[] s = getDataSetAsArrayOfMap(selectQuery, isCallable);
        return toList(s);
    }

    public String getDataSetAsStringSeparatedByToken(String sqlSelect, String separator, boolean useNumber, boolean isCallable) {
        try {
            Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //System.out.println(sqlSelect);
            return processDataSetResultSetStringSeparatedByToken(stmt.executeQuery(sqlSelect), separator, useNumber);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public int selectLastInsertedId() {
        String sql = "select last_insert_id()";//bisa beda u/ ms sql server
        String data[][] = getDataSet(sql, false);
        if (data.length > 0) {
            return Integer.parseInt(data[0][0]);
        }
        return -1;
    }

    public void setConnection(Connection connectiondb) {
        this.connectiondb = connectiondb;
    }

    public boolean executeQuery(String sql) {
        try {
            connectiondb = this.getConnection();
            statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.execute(sql);
            //PreparedStatement stmt = connectiondb.
            //stmt.execute();
            //statement.close();
            //connectiondb.close();
            LoggingWindow.getInstance().addToLog("***" + "[executeQuery]" + sql + "Database");
            return true;
        } catch (SQLException e) {
            if (!SUPPRESS_ERROR) {
                JOptionPane.showMessageDialog(null, "Database.executeQuery() : " + sql + "\n" + e.getMessage());
            }
            return false;
        }
    }

    public ResultSet selectQuery(String sql) {
        try {
            //LoggingWindow.getInstance().addToLog(selectStatement);
            connectiondb = this.getConnection();
            sql = sql.toUpperCase();
            Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = stmt.executeQuery(sql);
            LoggingWindow.getInstance().addToLog("***" + "[selectQuery]" + sql + "Database");
            return resultSet;
        } catch (SQLException e) {
            //Startup.getInstance().debug("Main.selectQuery", e.getMessage());
            Logger.getLogger(Startup.class.getName()).log(Level.SEVERE, sql, e);
            return null;
        }
    }

    public void update(String table, String value, String where) {
        if (!"".equals(where)) {
            executeQuery("update " + table + " set " + value + " where " + where);
        } else {
            executeQuery("update " + table + " set " + value);
        }
    }

    private String[][] processDataSetResultSet(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData;
            int indexOfRow = 0; // index dimulai dari nol.
            int nColoumn;
            int nRow;
            String[][] result;


            metaData = resultSet.getMetaData();
            nColoumn = metaData.getColumnCount();

            resultSet.last(); // menuju paling baris terakhir
            nRow = resultSet.getRow();
            result = new String[nRow][];
            resultSet.beforeFirst();
            while (resultSet.next()) {
                // disini skalian langsung ke baris berikutnya.
                result[indexOfRow] = new String[nColoumn];
                for (int i = 0; i < nColoumn; i++) {
                    //LoggingWindow.getInstance().addToLog(nColoumn);
                    result[indexOfRow][i] = resultSet.getString(i + 1);

                }
                indexOfRow++;
            }
            LoggingWindow.getInstance().addToLog("***" + "[processDataSetResultSet]: LENGTH = " + indexOfRow + "Database");
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Akan memproses resultSet menjadi array of hashMap
     * @param resultSet hasil query RreparedStatement atau Statement
     * @return array of hashmap yg dpt diambil nilai berdasarkan nama kolomnya
     */
    private HashMap[] processDataSetResultSetAsArrayOfHashMap(ResultSet resultSet) {
        try {
            ResultSetMetaData metaData;
            int indexOfRow = 0; // index dimulai dari nol.
            int nColoumn;
            int nRow;
            HashMap[] result;


            metaData = resultSet.getMetaData();
            nColoumn = metaData.getColumnCount();
            resultSet.last(); // menuju paling baris terakhir
            nRow = resultSet.getRow();
            result = new HashMap[nRow];

            resultSet.beforeFirst();
            while (resultSet.next()) {
                // disini skalian langsung ke baris berikutnya.
                result[indexOfRow] = new HashMap();
                for (int i = 1; i <= nColoumn; i++) {
                    LoggingWindow.getInstance().addToLog(metaData.getColumnName(i).toUpperCase());
                    result[indexOfRow].put(metaData.getColumnName(i).toUpperCase(), resultSet.getString(i));
                }
                indexOfRow++;
            }
            LoggingWindow.getInstance().addToLog("***" + "[processDataSetResultSet]: LENGTH = " + indexOfRow + "Database");
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public String processDataSetResultSetStringSeparatedByToken(ResultSet resultSet, String separator, boolean useNumber) {
        try {
            String result = "";
            resultSet.last(); // menuju paling baris terakhir
            resultSet.beforeFirst();
            int i = 0;
            while (resultSet.next()) {

                if (i == 0) {
                    for (int j = 1; j <= resultSet.getMetaData().getColumnCount(); j++) {
                        if (j == 1) {
                            if (!useNumber) {
                                result = resultSet.getString(j);
                            } else {
                                result = (i + 1) + ". " + resultSet.getString(j);
                            }
                        } else {
                            if (!useNumber) {
                                result = result + separator + resultSet.getString(j);
                            } else {
                                result = (i + 1) + ". " + result + separator + resultSet.getString(j);
                            }
                        }
                    }
                } else {
                    if (!useNumber) {
                        result = result + separator + resultSet.getString(1);
                    } else {
                        result = (i + 1) + ". " + result + separator + resultSet.getString(1);
                    }

                }
                i++;
            }
            if (!result.equals("")) {
                //result = result + "#";
            }

            return result;
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }

    }

    private String[][] getDataSet(Statement stmt, String sqlSelect) {
        try {
            LoggingWindow.getInstance().addToLog("***" + "[getDataSet]: Statement query = " + sqlSelect + "Database");
            return processDataSetResultSet(stmt.executeQuery(sqlSelect));
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private String[][] getDataSet(CallableStatement cstmt) {
        try {
            LoggingWindow.getInstance().addToLog("***" + "[getDataSet]: CallableStatement" + "Database");
            return processDataSetResultSet(cstmt.executeQuery());
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String[][] getDataSet(String sqlStatement, boolean isCallable) {
        try {
            LoggingWindow.getInstance().addToLog("***" + "[getDataSet]: Query = " + sqlStatement + "Database");
            connectiondb = this.getConnection();
            if (isCallable) {
                CallableStatement cstmt = connectiondb.prepareCall(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                return getDataSet(cstmt);
            } else {
                Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                return getDataSet(stmt, sqlStatement);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    private void processLoadQueryToTable(JTable tabel, ResultSet resultSet, HashMap idMap, boolean createColumn) {
        ResultSetMetaData metaData;
        int indexOfRow = 0; // index dimulai dari nol.
        int nColoumn;
        int nRow;
        String[][] result;
        int lastRow;
        LoggingWindow.getInstance().addToLog("***" + "[loadQueryToTable]: displaying resultSet" + resultSet);
        try {
            lastRow = tabel.getSelectedRow();

            metaData = resultSet.getMetaData();// penting digunakan, tentang kolom
            nColoumn = metaData.getColumnCount();
            DefaultTableModel model;
            if (createColumn) {
                model = new DefaultTableModel(); // --- spesifik JTabel
                for (int i = 2; i <= nColoumn; i++) // --- spesifik JTabel, td mulai dari 1, dipakai u/ ID, yg tdk tampil di Table
                { // --- spesifik JTabel
                    model.addColumn(metaData.getColumnName(i)); // --- spesifik JTabel
                } // --- spesifik JTabel
            } else {
                model = (DefaultTableModel) tabel.getModel();
                model.setRowCount(0);
            }

            resultSet.last();// menuju paling baris terakhir
            nRow = resultSet.getRow();
            result = new String[nRow][];
            resultSet.beforeFirst();// kembali ke sblm brs pertma;
            if (idMap != null) {
                idMap.clear();
            }

            while (resultSet.next()) {// disini skalian langsung ke baris berikutnya.                
                result[indexOfRow] = new String[nColoumn];
                if (idMap != null) {
                    idMap.put(indexOfRow, resultSet.getString(1));
                }
                for (int i = 1; i < nColoumn; i++) { //td i=0
                    result[indexOfRow][i - 1] = resultSet.getString(i + 1);
                }
                model.addRow(result[indexOfRow]); // --- spesifik JTabel
                indexOfRow++;

            }
            tabel.setModel(model); // --- spesifik JTabel
            tabel.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            if (lastRow > indexOfRow) {
                tabel.getSelectionModel().setSelectionInterval(nRow, nRow);
            } else {
                tabel.getSelectionModel().setSelectionInterval(0, 0);
            }

            //connectiondb.close();
        } catch (Exception ex) {
            result = null;
            Startup.getInstance().debug("Main.loadQueryToTable, resultset: " + resultSet, ex.getMessage());
        }
    }

    public void loadQueryToTable(JTable tabel, PreparedStatement prepStmt, HashMap idMap, boolean createColumn) {
        try {
            connectiondb = this.getConnection();
            ResultSet resultSet = prepStmt.executeQuery();
            processLoadQueryToTable(tabel, resultSet, idMap, createColumn);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadQueryToTable(JTable tabel, String selectStatement, HashMap idMap, boolean createColumn) {
        try {
            //        LoggingWindow.getInstance().addToLog("***" + "[loadQueryToTable]: Query = " + selectStatement + "Database");
            connectiondb = this.getConnection();
            Statement stmt = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //prepareCall(selectStatement); //createStatement();                
            ResultSet resultSet = stmt.executeQuery(selectStatement);
            processLoadQueryToTable(tabel, resultSet, idMap, createColumn);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // -- baru dari Taufik
    public boolean isAlreadyExistData(String tableName, String keyColoumn, String value) {// just for primary key
        int nRow;
        boolean check;
        String query = "SELECT * from `" + tableName + "` WHERE `" + keyColoumn + "`='" + value + "'";
        try {
            LoggingWindow.getInstance().addToLog("***" + "[isAlreadyExistData]: tableName = " + tableName + "Database");
            connectiondb = DriverManager.getConnection(getUrl(), USER_NAME, PASSWORD);
            statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            nRow = resultSet.getRow();
            if (nRow == 0) {
                check = false;
            } else {
                check = true;
            }
            //connectiondb.close();
        } catch (SQLException exc) {
            check = false;
            System.err.print(exc);
        }
        return check;
    }

    private String getUrl() {
        String url;
        if (USE_MYSQL) {
            url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB;
        } else if (USE_MSSQL) {//jdbc:sqlserver://localhost:1433;database=smk2depo_smk2depok";
            url = "jdbc:sqlserver://" + HOST + ":" + PORT + ";database=" + DB;
        } else if (USE_ODB) {//jdbc:sqlserver://localhost:1433;database=smk2depo_smk2depok";
            url = "jdbc:hsqldb:file:" + DB;
        } else {
            url = "jdbc:odbc:" + DB;
        }

        LoggingWindow.getInstance().addToLog("***" + "[getUrl]:  = " + url + "Database");
        return url;
    }

    private String toHTMLTable(HashMap[] map) {
        String result = "<html><table border=1>";

        if (map.length == 0) {
            return "";
        }

        for (int i = 0; i < map.length; i++) {
            result += "<tr>";
            for (Iterator it = map[i].keySet().iterator(); it.hasNext();) {
                result += "<tr>";
                Object key = it.next();
                Object value = map[i].get(key);
                result += "<td>" + key + "</td><td>" + value + "</td>";
                result += "</tr>";
            }
            result += "</tr>";
        }

        result += "</table></html>";
        LoggingWindow.getInstance().addToLog(result);
        return result;
    }

    private String toList(HashMap[] map) {
        String result = "";
        if (map.length == 0) {
            return "";
        }

        for (int i = 0; i < map.length; i++) {
            for (Iterator it = map[i].keySet().iterator(); it.hasNext();) {
                Object key = it.next();
                Object value = map[i].get(key);
                result += value + ";";
            }
            result += "#";
        }
        return result;
    }

    private void shutdownOdb() {
        try {
            Statement st = getConnection().createStatement();
            // db writes out to files and performs clean shuts down
            // otherwise there will be an unclean shutdown
            // when program ends
            st.execute("SHUTDOWN");
            getConnection().close(); // if there are no other open connection

            //masukkan kembali ke archive ODB
            Runtime.getRuntime().exec("compres.bat " + ODB_FILE + " " + ODB_TEMP);

        } catch (IOException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void shutdownDatabase() {
        if (USE_ODB) {
            shutdownOdb();
            //archive in *.odb
        }
        //pause for 5 seconds
        Wait.manySec(3);
        connectiondb = null;
    }

    public void prepareDb() {
        if (USE_ODB) {
            try {
                syntax = "extract.bat " + ODB_FILE + " " + ODB_TEMP;
                // TODO : pastikan clean shutdown, kalau belum, ... tambahkan ONLY NEW via RAR functions
                //extract database dari file ODB ke target ODB
                //Runtime.getRuntime().exec("winrar\\winrar e -o+ " + ODB_FILE + "  database\\backup database\\data database\\properties database\\script " + DB + " -y");
                System.out.println(syntax);
                //ubah brooo!! spy cek file lock

                //Wait.manySec(3);
                Runtime.getRuntime().exec(syntax);
            } catch (IOException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        //;
    }

    // === FIX THIS
    public List get(String sqlSelect, String fqnModel) {
        List list = new ArrayList();
        try {
            PreparedStatement pstmt = getConnection().prepareStatement(sqlSelect, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            list = getDataSetAsArrayList(pstmt, fqnModel);

        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public ArrayList getDataSetAsArrayList(PreparedStatement prepStmtFillControl, String fqnModel) {
        try {
            return processDataSetResultSetAsArrayList(prepStmtFillControl.executeQuery(), fqnModel);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private ArrayList processDataSetResultSetAsArrayList(ResultSet resultSet, String fqnModel) {
        ArrayList result = new ArrayList();

        try {
            ResultSetMetaData metaData;
            int nColoumn;
            String columnName;
            String fieldValue;
            Field field;
            Object modelInstance;

            metaData = resultSet.getMetaData();
            nColoumn = metaData.getColumnCount();
            resultSet.beforeFirst();
            Class modelClass = Class.forName(fqnModel);
            while (resultSet.next()) {
                modelInstance = modelClass.newInstance();
                for (int i = 1; i <= nColoumn; i++) {
                    columnName = metaData.getColumnName(i);
                    field = modelInstance.getClass().getDeclaredField(columnName);
                    fieldValue = resultSet.getString(i);
                    field.set(modelInstance, fieldValue);
                }
                result.add(modelInstance);
            }
        } catch (Exception ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public Object getById(String tableName, String pkFieldName, String fqn, String inputId) {
        Object modelInstance = null;
        try {
            Class modelClass = Class.forName(fqn);
            ResultSetMetaData metaData;
            String columnName;
            String fieldValue;
            Field field;
            PreparedStatement pstmt = getConnection().prepareStatement("select * from " + tableName + " where " + pkFieldName + "=" + inputId, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = pstmt.executeQuery();
            metaData = resultSet.getMetaData();
            int nColoumn = metaData.getColumnCount();
            resultSet.beforeFirst();
            modelInstance = modelClass.newInstance();
            resultSet.next();
            for (int i = 1; i <= nColoumn; i++) {
                columnName = metaData.getColumnName(i);
                field = modelInstance.getClass().getDeclaredField(columnName);
                fieldValue = resultSet.getString(i);
                field.set(modelInstance, fieldValue);
            }

        } catch (Exception ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modelInstance;
    }
}
