package smsgateway;

import core.system.DB;
import core.system.LoggingWindow;
import core.system.Startup;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMS {

    private static SMS singleton = null;
    Date today = new Date();
    private String delimiter = " ";
    private String outboxBeginWith = "";
    private String unsentOutboxSql;

    public String getOutboxBeginWith() {
        return outboxBeginWith;
    }

    public void setOutboxBeginWith(String outboxBeginWith) {
        this.outboxBeginWith = outboxBeginWith;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    private String sqlViewInbox = "select * from sms_inbox order by id asc";
    private String sqlViewOutbox = "select * from sms_outbox order by id asc";
    private int n,  p;
    private Object data[][];
    private SMSProcessing smsProcessing;

    public SMSProcessing getSmsProcessing() {
        return smsProcessing;
    }

    public void setSmsProcessing(SMSProcessing smsProcessing) {
        this.smsProcessing = smsProcessing;
    }

    public static SMS getSingleton() {
        if (singleton == null) {
            singleton = new SMS();
        }
        return singleton;
    }
    private String incomingPhoneNumber = null;
    private boolean unknownAgent = false;
    private int autoIdAgent;

    private SMS() {
    }

    public int getRowCount() {
        return n;
    }

    public String[][] getAllInbox() {
        return DB.getInstance().getDataSet(sqlViewInbox, false);

    }

    public Object getDataFromSMSInbox() {
        try {
            String SQL = "SELECT id,no_telp,pesan,waktu FROM sms_inbox order by no_telp";
            ResultSet resultSet = DB.getInstance().selectQuery(SQL);

            resultSet.last();
            p = 0;
            n = resultSet.getRow();
            data = new Object[n][4];
            resultSet.beforeFirst();
            while (resultSet.next()) {
                data[p][0] = resultSet.getString(1); // isi ke kolom no

                data[p][1] = resultSet.getString(2); // isi ke kolom nomor telepon

                data[p][2] = resultSet.getString(3); // isi ke kolom pesan

                data[p][3] = resultSet.getString(4); // isi ke kolom waktu

                p++;
            }
            resultSet.close();
        } catch (SQLException sqle) {
            Startup.getInstance().debug("SMS.getDataFromSMSInbox", sqle.getMessage());

        }
        return data;
    }

    public String[][] getAllOutbox() {
        return DB.getInstance().getDataSet(sqlViewOutbox, false);

    }

    public Object getDataFromSMSOutbox() {
        try {
            LoggingWindow.getInstance().addToLog("bs diselect?", "SMS Gateway");
            String SQL = "SELECT id,no_telp,pesan,waktu,status FROM sms_outbox order by no_telp";
            ResultSet resultSet = DB.getInstance().selectQuery(SQL);

            resultSet.last();
            p = 0;
            n = resultSet.getRow();
            LoggingWindow.getInstance().addToLog("jumlah data " + n, "SMS Gateway");
            data = new Object[n][5];
            resultSet.beforeFirst();
            while (resultSet.next()) {
                LoggingWindow.getInstance().addToLog("Ini tak pernah dipanggil ya?", "SMS Gateway");
                data[p][0] = resultSet.getString(1); // isi ke kolom no

                data[p][1] = resultSet.getString(2); // isi ke kolom nomor telepon

                data[p][2] = resultSet.getString(3); // isi ke kolom pesan

                data[p][3] = resultSet.getString(4); // isi ke kolom waktu

                data[p][4] = resultSet.getString(5); // isi ke kolom status
                //LoggingWindow.getInstance().addToLog(data[p]);

                LoggingWindow.getInstance().addToLog("datanya = " + resultSet.getString(2), "SMS Gateway");
                p++;
            }

            resultSet.close();

        } catch (SQLException sqle) {
            LoggingWindow.getInstance().addToLog(sqle.getMessage(), "SMS Gateway"); // SALAHNYA DISINI YA... karena Exceptionnya tidak ditampilkan apa kesalahannya

        }
        return data;
    } // akhir dari method getDataFromSMSOutbox

    public void insertInbox(String[] data) {
        String sqlInsert = "INSERT INTO sms_inbox (no_telp , pesan )" +
                "VALUES(" +
                "'" + data[0] + "'," +
                "'" + data[1] + "')";
        DB.getInstance().executeQuery(sqlInsert);
    }

    public void insertOutbox(String[] data) {
        String sqlInsert = "INSERT INTO sms_outbox (  no_telp , pesan , status )" +
                "VALUES(" +
                " '" + data[0] + "'," +
                " '" + data[1] + "'," +
                "'" + data[2] + "')";
        DB.getInstance().executeQuery(sqlInsert);
    }

    public void editInbox(String[] data) {
        String sqlUpdate = "UPDATE sms_inbox SET " +
                "pesan = '" + data[0] + "' " +
                "WHERE idsms_inbox =" + data[1];
        DB.getInstance().executeQuery(sqlUpdate);
    }

    public void editOutBox(String[] data) {
        String sqlUpdate = "UPDATE sms_outbox SET " +
                "pesan = '" + data[0] + "' " +
                "WHERE idsms_outbox =" + data[1];
        DB.getInstance().executeQuery(sqlUpdate);
    }

    public void deleteInbox(String value) {
        String sqlDelete = "DELETE FROM sms_inbox WHERE idsms_inbox='" + value + "'";
        DB.getInstance().executeQuery(sqlDelete);
    }

    public void deleteOutbox(String value) {
        String sqlDelete = "DELETE FROM sms_outbox WHERE idsms_outbox='" + value + "'";
        DB.getInstance().executeQuery(sqlDelete);
    }

    public void deleteInbox(String[] values) {
        for (int i = 0; i < values.length; i++) {
            String sqlDelete = "DELETE FROM sms_inbox WHERE idsms_inbox='" + values[i] + "'";
            DB.getInstance().executeQuery(sqlDelete);
        }
    }

    public void deleteOutbox(String[] values) {
        for (int i = 0; i < values.length; i++) {
            String sqlDelete = "DELETE FROM sms_outbox WHERE idsms_outbox='" + values[i] + "'";
            DB.getInstance().executeQuery(sqlDelete);
        }
    }

    public String[][] getUnsentOutbox() {
        LoggingWindow.getInstance().addToLog("SMS.getUnsentOutbox() - with status p0..p6", "SMS Gateway");
        return DB.getInstance().getDataSet(unsentOutboxSql, false);

    }

    public String getNextStatus(String id) {
        String sql = "SELECT status FROM sms_outbox WHERE idsms_outbox='" + id + "'";
        LoggingWindow.getInstance().addToLog("getNextStatus");
        int N_PENDING = Integer.parseInt(DB.getInstance().getDataSet(sql, false)[0][0].substring(1, 2));
        String nextStatus = "p0";
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                nextStatus = "f"; // failed, sudah dicoba mengirim selama 0..5, 6x

            } else {
                if (N_PENDING == i) { // pending 

                    nextStatus = "p" + (i + 1);
                    break;
                }
            }
        }
        return nextStatus;
    }

    public void updateStatusOutbox(String[] data) {
        String sqlUpadate = "UPDATE sms_outbox SET " +
                "status = '" + data[0] + "' " +
                "WHERE idsms_outbox =" + data[1];
        DB.getInstance().executeQuery(sqlUpadate);
//        
    }

    /*********************************/
    //public String getConfirmMessage(){
    //  return confMsg;
    //}
    /* Method : untuk validasi format perintah
     */
    public static void main(String[] args) {
        SMS test = new SMS();
        test.setIncomingAgent("6285292135559");        //62818466481

        LoggingWindow.getInstance().addToLog(test.messageProcessing("harga as"));
        LoggingWindow.getInstance().addToLog(test.messageProcessing("help"));
        LoggingWindow.getInstance().addToLog(test.messageProcessing("Status"));
        LoggingWindow.getInstance().addToLog(test.messageProcessing("Tagihan"));
        LoggingWindow.getInstance().addToLog(test.messageProcessing("iSi s5 085292135559"));
    }
    /*************************************/
    private boolean savedInbox = false;

    public boolean isSavedInbox() {
        return savedInbox;
    }

    // ini setelah pengecekan apakah ia adalah agen
    public String messageProcessing(String pesan) {
        String[] token = pesan.toUpperCase().split(delimiter);
        if (smsProcessing != null) {
            return smsProcessing.delegateMessageProcessing(token, getIncomingPhoneNumber());
        }
        return "Kode pemrosesan belum diimplementasikan";
    }

    public void buildUnsentOutboxSql(String format) {
        if ("".equals(format)) {
            unsentOutboxSql = "SELECT * FROM sms_outbox WHERE status LIKE 'p%' " + " ORDER BY waktu ASC";
        }else if("fitra-kuliner".equalsIgnoreCase(format)){
           unsentOutboxSql = "SELECT * FROM inbox";
        } else {
            unsentOutboxSql = "SELECT * FROM sms_outbox WHERE status LIKE 'p%' ";
            String token[] = format.split(",");
            String pesanFormat = "(";
            for (int i = 0; i < token.length; i++) {
                pesanFormat = pesanFormat + "pesan like '" + token[i] + "%'";
                if (i < token.length - 1) {
                    pesanFormat = pesanFormat + " or ";

                }
            }
            pesanFormat = pesanFormat + ")";
            unsentOutboxSql = unsentOutboxSql + " and " + pesanFormat + " ORDER BY waktu ASC";

        }
        LoggingWindow.getInstance().addToLog(unsentOutboxSql, "SMS Gateway");
        System.out.println("buildUnsetOutboxSql : " + unsentOutboxSql);
    }

    void setAutoIdAgent(int aInt) {
        autoIdAgent = aInt;
        LoggingWindow.getInstance().addToLog("autoIdAgent = " + autoIdAgent, "SMS Gateway");
    }

    void setUnknownAgent(boolean b) {
        unknownAgent = b;
    }

    public String delegateMessageProcessing() {
        return "Not Yet Implemented";
    }

    private String getIncomingPhoneNumber() {
        return incomingPhoneNumber;
    }

    public void setIncomingPhoneNumber(String incomingPhoneNumber) {
        this.incomingPhoneNumber = incomingPhoneNumber;
    }

    public boolean isUnknownAgent() {
        return unknownAgent;
    }

    public int getAutoIdAgent() {
        return autoIdAgent;
    }

    public void setIncomingAgent(String noTelp) {
        setIncomingPhoneNumber(noTelp);
        try {
            ResultSet rs = null;
            rs = DB.getInstance().selectQuery("select auto_id from agen where hp='" + noTelp + "'");
            rs.beforeFirst();
            if (!rs.next()) {
                setUnknownAgent(true);
                SMS.getSingleton().setAutoIdAgent(-1);
            } else {
                SMS.getSingleton().setAutoIdAgent(rs.getInt("auto_id"));
                SMS.getSingleton().setUnknownAgent(false);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        LoggingWindow.getInstance().addToLog("incomingAgent : " + SMS.getSingleton().getAutoIdAgent() + "," + SMS.getSingleton().isUnknownAgent(), "SMS Gateway");

    }
}
