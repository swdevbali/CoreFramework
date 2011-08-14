package smsgateway.commport;

import core.system.DB;

public class DatabasePort {
//    Basisdata db=new Basisdata();
    private String sqlViewConn = "SELECT nama,port,bps,data_bits,parity,stop_bits,flow_control,idserihp FROM koneksi_data";
    //private String sqlViewConn = "SELECT nama,port,bps,data_bits,parity,stop_bits,flow_control,format_pesan_outbox,format_pesan_outbox_spesifik,idserihp FROM koneksi_data";
    String[] connLogOneRow;

    public DatabasePort() {
    }

    public String[][] getAllConnData() {
        return DB.getInstance().getDataSet(sqlViewConn, false);
    }

    public String[] getActivedConnData() {
        String sql = "SELECT d.nama,port,bps,data_bits,parity,stop_bits,flow_control,waktu,status FROM koneksi_data d,koneksi_log l WHERE l.idkoneksi_data=d.idkoneksi_data AND l.status='1'";
        return DB.getInstance().getDataSet(sql, false)[0];
    }

    public String[] getSelectedConnData(String value) {
        String sql = "SELECT nama,port,bps,data_bits,parity,stop_bits," +
                "flow_control,idserihp FROM koneksi_data  where  nama='" + value + "'";//format_pesan_outbox,format_pesan_outbox_spesifik
        return DB.getInstance().getDataSet(sql, false)[0];
    }

    public void setActived(String connName) {// melakukan set active status koneksi
        try {
            String connCenterLogSqlUpadete = "UPDATE koneksi_log SET status = '0'";
            DB.getInstance().executeQuery(connCenterLogSqlUpadete);
            /******** stlah pusing, akhirnya ketemu jga. *******/
            Thread.currentThread().sleep(750);// try n' errorku'


            if (connName != null) {
                String idkoneksi_data = DB.getInstance().findId("koneksi_data","idkoneksi_data", "nama='" + connName + "'");

                String connCenterLogInsert = "INSERT INTO " +
                        "koneksi_log(idkoneksi_data,status) " +
                        "VALUES" +
                        "(" +
                        "" + idkoneksi_data + "," +
                        "1)";
                DB.getInstance().executeQuery(connCenterLogInsert);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void insertData(String[] data) {
        String connCenterSqlInsert = "INSERT INTO " +
                "koneksi_data(nama,port,bps,data_bits,parity,stop_bits,flow_control,idserihp) " +//format_pesan_outbox,format_pesan_outbox_spesifik
                "VALUES" +
                "('" + data[0] + "'," +
                " '" + data[1] + "'," +
                " " + data[2] + "," +
                " " + data[3] + "," +
                " '" + data[4] + "'," +
                " " + data[5] + "," +
                " '" + data[6] + "'"           
                 + "," + data[9]  +
                ")";
        DB.getInstance().executeQuery(connCenterSqlInsert);
        String idkoneksi_data = DB.getInstance().findId("koneksi_data", "id","nama='" + data[0] + "'");
        String connLogSqlInsert = "INSERT INTO " +
                "koneksi_log(idkoneksi_data,status) " +
                "VALUES" +
                "(" + idkoneksi_data + "," +
                "'0')";

        DB.getInstance().executeQuery(connLogSqlInsert);
    }

    public void editData(String[] data) {
        String sqlData = "UPDATE koneksi_data SET " +
                "nama='" + data[0] + "'," +
                "port='" + data[1] + "'," +
                "bps=" + data[2] + "," +
                "data_bits=" + data[3] + "," +
                "parity='" + data[4] + "'," +
                "stop_bits=" + data[5] + "," +
                "flow_control='" + data[6] + "', " +
                //"format_pesan_outbox='" + data[8] + "', " +
                //"format_pesan_outbox_spesifik='" + data[9] + "', " +
                "idserihp=" + data[10] + 
                " WHERE nama='" + data[7] + "'";
        DB.getInstance().executeQuery(sqlData);
    }

    public void deleteData(String keyValue) {
        String connCenterSqlDelete = "DELETE FROM koneksi_data WHERE nama='" + keyValue + "'";
        DB.getInstance().executeQuery(connCenterSqlDelete);
    }

    public boolean isAlreadyExistConn(String keyValeu) {
        return DB.getInstance().isAlreadyExistData("koneksi_data", "nama", keyValeu);
    }
}
