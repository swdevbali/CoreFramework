package smsgateway.commport;

import smsgateway.SMS;

public class ManajerDataPort {
    
    //SMS sms=new SMS();
    DatabasePort conn=new DatabasePort();
    //MataClass mata=new MataClass();
    /** Creates a new instance of manageData */
    public ManajerDataPort() {
    }
    /************ Control SMS ******/
    public void doInsertInbox(String[] data){
        SMS.getSingleton().insertInbox(data);
    }
    public void doInsertOutbox(String[] data){
        SMS.getSingleton().insertOutbox(data);
    }
    public String[][] doViewInbox(){
        return SMS.getSingleton().getAllInbox();
    }
    public String[][] doViewOutbox(){
        return SMS.getSingleton().getAllOutbox();
    }
    public void doEditInbox(String[] data){
        SMS.getSingleton().editInbox(data);
    }
    public void doEditOutbox(String[] data){
        SMS.getSingleton().editOutBox(data);
    }
    public void doDeleteInbox(String value){
        SMS.getSingleton().deleteInbox(value);
    }
    public void doDeleteInbox(String[] values){
        SMS.getSingleton().deleteInbox(values);
    }
    public void doDeleteOutbox(String value){
        SMS.getSingleton().deleteOutbox(value);
    }
    public void doDeleteOutbox(String[] values){
        SMS.getSingleton().deleteOutbox(values);
    }
    public String[][] doGetUnsentOutbox(){
        return SMS.getSingleton().getUnsentOutbox();
    }
    public void doUpdateStatusOutbox(String[] data){
        SMS.getSingleton().updateStatusOutbox(data);
    }
    
    /************** Control MATA ***********/
    public void doInsertMata(String[] data){
        //mata.insertMata(data);
    }
    
    public void doDeleteMata(String Values){
        //mata.deleteMata(Values);
    }
    public void doDeleteMata(String[] Values){
        //mata.deleteMata(Values);
    }
    
    
    
    
    
    /************** Control Koneksi ***********/
    
    
     /*
      *
      ** Untuk entitas user
      *
      */
    
    /* Method : kirim seluruh data modem
     */
    public String[][] doViewConnData(){
        return conn.getAllConnData();
    }
    
    /* Method : aktifkan modem
     */
    public void setActivedConn(String connName){
        conn.setActived(connName);
    }
    
    /* Method : kirim modem yang aktif
     */
    public String[] getActivedConnData(){
        return conn.getActivedConnData();
    }
    
    /* Method : kirim data modem teseleksi
     */
    public String[] getSelectedConnData(String value){
        return conn.getSelectedConnData(value);
    }
    
    /* Method : apakah nama koneksi sudah ada ?
     */
    public boolean isAlreadyExistConn(String connName){
        return conn.isAlreadyExistConn(connName);
    }
    
    /* Method : hapus data modem
     */
    public void deleteConn(String keyValue){
        conn.deleteData(keyValue);
    }
    
    /* Method : ubah data modem
     */
    public void editConn(String[] data){
        conn.editData(data);
    }
    
    /* Method : tambah data modem
     */
    public void addConn(String[] data){
        conn.insertData(data);
    };
}
