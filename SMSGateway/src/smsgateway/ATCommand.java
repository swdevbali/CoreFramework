package smsgateway;

import core.system.DB;
import core.system.LoggingWindow;
import core.system.Startup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.comm.*;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import smsgateway.exception.ExceptionOnSetProperties;
import smsgateway.exception.ExceptionOnStreaming;

public class ATCommand implements SerialPortEventListener {

    private static ATCommand singleton = null;
    public static boolean SEND_ONLY = false;
    public static boolean RECEIVE_ONLY = false;
    public static boolean SEND_RECEIVE = true;
    private int atDelay = 1000;
    /**********  instance  ************/
    ThreadSendMessage threadSend = new ThreadSendMessage();
    //SMS.getSingleton() SMS.getSingleton()=new SMS.getSingleton()();
    //PDU pdu=new PDU();
    // Basisdata db=new Basisdata();
    private int BPS;
    private int DATA_BITS;
    private int PARITY;
    private int STOP_BITS;
    private int FLOW_COTROL;
    private SerialPort serialPort;
    private InputStream inStream;
    private OutputStream outStream;
    private byte[] bacaBuffer = new byte[100000];
    private int akhirBuffer = 0;
    private int id;//id
    private int ID_OUTBOX;// index untuk pesan (outbox) yang belum terkirim.
    private TimerExecution timerExecution;
    public boolean isThreadOn() {
        return threadOn;
    }

    public void setThreadOn(boolean threadOn) {
        this.threadOn = threadOn;
    }
    private boolean threadOn = false;
    public String actConn = null;
    public int INIT_STATUS;// status mdem.
    private String nameConn;

    public static ATCommand getSingleton() {
        if (singleton == null) {
            singleton = new ATCommand();
        }
        return singleton;
    }
    private boolean detectMerk = false;
    private int iterasiDetectMerk;
    private boolean detectTipe;
    private int iterasiDetectTipe;
    public static String tipe;
    private boolean detectImei;
    private int iterasiDetectImei;
    public static String imei;
    private boolean detectOperator;
    private int iterasiDetectOperator;
    public static String operator;
    public static boolean RESPONSE_MESSAGE = true;

    public String getSeri() {
        return seri;
    }
    private String seri;

    public String getMerk() {
        return merk;
    }
    public static String merk;

    private ATCommand() {
    }

    public void setResponseMessage(String response) {
        if (response.equals("noresponse")) {
            RESPONSE_MESSAGE = false;
        } else if (response.equals("response")) {
            RESPONSE_MESSAGE = true;
        }
    }

    public void setSendPolicy(String policy) {
        if (policy.equals("sendonly")) {
            SEND_ONLY = true;
            RECEIVE_ONLY = false;
            SEND_RECEIVE = false;
        } else if (policy.equals("receiveonly")) {
            RECEIVE_ONLY = true;
            SEND_ONLY = false;
            SEND_RECEIVE = false;
        } else if (policy.equals("full")) {
            SEND_ONLY = false;
            RECEIVE_ONLY = false;
            SEND_RECEIVE = true;
        }
    }

    public void setTimerExecution(TimerExecution timerExecution) {
        this.timerExecution = timerExecution;
    }

    public void stopThreadSendMessage() {
        threadSend.stop();
        threadOn = false;
    }

    private void atDetectHandphoneTypeAndSeri() {
        //    sendAtCommand("AT+CGMI\15", atDelay); // ini u/ nokia
//        sendAtCommand("AT+CGMM\15", atDelay); // ini u/ nokia
        detectMerk = true;
        iterasiDetectMerk = 1;

        sendAtCommand("AT+CGMI\15", atDelay); // ini u/ nokia

        detectTipe = true;
        iterasiDetectTipe = 1;
        sendAtCommand("AT+CGMM\15", atDelay); // ini u/ nokia

        detectImei = true;
        iterasiDetectImei = 1;
        sendAtCommand("AT+CGMM\15", atDelay); // ini u/ nokia

        detectOperator = true;
        iterasiDetectOperator = 1;
        sendAtCommand("AT+COPS?\15", atDelay); // ini u/ nokia

    }

    /* Method : cari jumlah port yang terdapat dikomputer.
     */
    private int getNumSerilPort() {
        int i = 0;
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();// harus sebagai var lokal, sb ada iterasi.

        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                i++;
            }
        }
        return i;
    }

    /* Mengaktifkan modem 
     */
    public void installModem(String[] data) {
        try {
            openPort(data[1]);
            openPortStream();
            setValidData(data[2], data[3], data[4], data[5], data[6]);
            setProperties();
            setTerminal(data[7]);
            nameConn = data[0];
            if (SEND_RECEIVE || SEND_ONLY) {
                if (!threadOn) {
                    threadSend.start();

                    threadOn = true;
                    if (SMS.getSingleton().getSmsProcessing() != null) {
                        SMS.getSingleton().getSmsProcessing().afterModemInstalled();
                    }

                    // format pesan SMS outbox
                    //if ("0".equals(data[7])) {
                    SMS.getSingleton().buildUnsentOutboxSql("");
                //  } else {
                //     SMS.getSingleton().buildUnsentOutboxSql(data[8]);
                //  }

                }
            }
        } catch (ExceptionOnStreaming ex) {
            INIT_STATUS = 1;
        } catch (ExceptionOnSetProperties ex) {
            INIT_STATUS = 1;
        }
    }

    /**
     * Set terminal Comm, dengan beberapa urutan aksinya,
     * dan menampilkan progress bar aktivitasnya
     */
    public void setTerminal(String idserihp) {
        INIT_STATUS = -1;
        //TerminalProgressPopup.getSingleton().setVisible(true);
        //TerminalProgressPopup.getSingleton().setMaxValue(4);        
        //TerminalProgressPopup.getSingleton().setProgressText("Mengirim AT Command u/ memastikan keterhubungan Device");
        sendAtCommand("AT\15", atDelay);
        //TerminalProgressPopup.getSingleton().stepUp();
        //TerminalProgressPopup.getSingleton().setProgressText("Mengatur format pesan PDU atau Text");
        setFormatSmsToPDU();
        //TerminalProgressPopup.getSingleton().stepUp();
        //TerminalProgressPopup.getSingleton().setProgressText("Mengatur device u/ menerima pesan secara otomatis"); ^-^ mumet neh
        atAutoDetectMessage(idserihp);
        //TerminalProgressPopup.getSingleton().stepUp();
        atReadUnreadMessage();
        atDetectHandphoneTypeAndSeri();
    //TerminalProgressPopup.getSingleton().close();
    }

    /* Method : menentukan nama-nama port
     */
    public String[] getPortName() {
        int i = 0;
        String[] portName = new String[getNumSerilPort()];
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portName[i] = portId.getName();
                i++;
            }
        }
        return portName;
    }

    /* Method : open serial port
     */
    private void openPort(String portName) {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(portName)) {
                    try {
                        serialPort = (SerialPort) portId.open("atCommand", 5000);
                    } catch (PortInUseException ex) {
                        INIT_STATUS = 2;
                    }
                }
            }
        }
    }

    /* Method : close serial port
     */
    public void closePort() {
        try {
            serialPort.close();
        } catch (NullPointerException e) {
        }
    }

    /* Method : streaming port
     */
    private void openPortStream() throws ExceptionOnStreaming {
        try {
            inStream = serialPort.getInputStream();
            outStream = serialPort.getOutputStream();
        } catch (IOException e) {
            throw new ExceptionOnStreaming("IO gagal ");
        } catch (NullPointerException e) {
            throw new ExceptionOnStreaming("Port belum dibuka ");
        } catch (IllegalStateException exp) {
            throw new ExceptionOnStreaming("Port belum dibuka ");
        }
    }

    /* Method : mengatur data serial port
     */
    private void setProperties() throws ExceptionOnSetProperties {
        try {
            serialPort.setFlowControlMode(FLOW_COTROL);
            serialPort.setSerialPortParams(BPS, DATA_BITS, STOP_BITS, PARITY);
            serialPort.notifyOnDataAvailable(true);
            serialPort.addEventListener(this);
        } catch (UnsupportedCommOperationException ex) {
            System.out.println("ATCommand:setProperties :" + ex.getMessage());
        } catch (TooManyListenersException ex) {
            System.out.println("ATCommand:setProperties : " + ex.getMessage());
        } catch (IllegalStateException exp) {
            throw new ExceptionOnSetProperties("Port belum dibuka");
        }

    }

    /**
     * Menggunakan data dari form setup port u/ mensetup properti koneksi PORT
     */
    private void setValidData(String bps, String data_bits, String parity,
            String stop_bits, String flow_control) {
        BPS = Integer.parseInt(bps);
        //**********************
        if (data_bits.equals("5")) {
            DATA_BITS = SerialPort.DATABITS_5;
        } else if (data_bits.equals("6")) {
            DATA_BITS = SerialPort.DATABITS_6;
        } else if (data_bits.equals("7")) {
            DATA_BITS = SerialPort.DATABITS_7;
        } else {
            DATA_BITS = SerialPort.DATABITS_8;
        }
        ///////////////////////
        if (parity.equals("Even")) {
            PARITY = SerialPort.PARITY_EVEN;
        } else if (parity.equals("Odd")) {
            PARITY = SerialPort.PARITY_ODD;
        } else if (parity.equals("None")) {
            PARITY = SerialPort.PARITY_NONE;
        } else if (parity.equals("Mark")) {
            PARITY = SerialPort.PARITY_MARK;
        } else {
            PARITY = SerialPort.PARITY_SPACE;
        }
        //========================
        if (stop_bits.equals("1")) {
            STOP_BITS = SerialPort.STOPBITS_1;
        } else if (stop_bits.equals("1.5")) {
            STOP_BITS = SerialPort.STOPBITS_1_5;
        } else {
            STOP_BITS = SerialPort.STOPBITS_2;
        }
        //---------------------------
        if (flow_control.equals("None")) {
            FLOW_COTROL = SerialPort.FLOWCONTROL_NONE;
        } else if (flow_control.equals("Hardware")) {
            FLOW_COTROL = SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT;
        } else {
            FLOW_COTROL = SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT;
        }
    }
    /* Method : Listener terhadap aksi pada serial port
     * Hm... dipanggil oleh listenernya lho... 
     */
    private int n;

    public void serialEvent(SerialPortEvent serialEvent) {
        String buffer = "";
        try {

            while ((n = inStream.available()) > 0) {
                // Baca input
                n = inStream.read(bacaBuffer, akhirBuffer, n);
                akhirBuffer += n;
                if ((bacaBuffer[akhirBuffer - 1] == 10) && (bacaBuffer[akhirBuffer - 2] == 13)) {
                    //berikan nilai tersebut ke buffer
                    buffer = new String(bacaBuffer, 0, akhirBuffer - 2);
                    System.out.println("buffer=" + buffer);
                    receiveAtResponse(buffer.trim());
                    akhirBuffer = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Ada kesalahan di ATCommand.serialEvent : " + e.getMessage() + ",bufer=" + buffer);
        }
    }

    /* Method : Baca seluruh SMS.getSingleton() (terbuka atau tidak)
     */
    private void atReadUnreadMessage() {
        sendAtCommand("AT+CMGL=0\15", atDelay);
    }

    /* Method : set format SMS.getSingleton() menjasi PDU
     */
    private void setFormatSmsToPDU() {
        sendAtCommand("AT+CMGF=0\15", atDelay); // formatnya PDU, bukan teks

    }

    /* Method : auto detect SMS.getSingleton() masuk
     */
    private void atAutoDetectMessage(String idserihp) {

        String data[][] = DB.getInstance().getDataSet("select cnmi from serihp where idserihp=" + idserihp, false);
        if (data.length == 0) {
            JOptionPane.showMessageDialog(Startup.getInstance().getMainFrame(), "Maaf, seri HP dengan ID " + idserihp + " tidak bisa ditemukan");
        }

        String cnmi = data[0][0];
        sendAtCommand("AT+CNMI=" + cnmi + "\15", atDelay);
    /* -- tidak mengambil tindakan jika tidak bs autodetect message ... harus di poll
    -- TODO : belum tahu cara mbaca hasil AT Command, langsung ganti u/ nokia aja
    sendAtCommand("AT+CNMI=3,1\15",1250); // ini u/ motorolla V3i
    sendAtCommand("AT+CNMI=1,1,0,0,0\15", atDelay); // ini u/ nokia 6100,3310
    sendAtCommand("AT+CNMI=2,1\15",1250);// ini u/ SE T630-T628
    sendAtCommand("AT+CNMI=3,3\15", atDelay);// ini u/ SE T68
    sendAtCommand("AT+CNMI=1,1,0,0,1\15", atDelay);// ini u/ SIEMENS C55
    sendAtCommand("AT+CNMI=1,1,0,2,1\15", atDelay);// ini u/ SIEMENS M55
    sendAtCommand("AT+CNMI=2,1,0,1\15", atDelay); // ini u/ SE W850i
    ngolek CMNI jodo-jodoan neh eh ndas mumet kagak konek2 neh*/


    }

    /* Method : baca SMS.getSingleton() per index-nya
     * ini perlu di riset u/ membaca CMGR dimana CMT-nya hanya menunjukkan
     * panjang karakter SMS, bukannya index SMS-nya
     * Tapi kok ini berfungsi di motoroll V3-nya Taufik.... ^_^
     */
    private void atReadMessage(int indexMsg) {
        sendAtCommand("AT+CMGR=" + indexMsg + "\15", atDelay);
    }

    /* Method : kirim SMS.getSingleton()
     */
    public void atSendMessage(String pesanPDUKirim) {
        try {
            sendAtCommand("AT+CMGS=" + (pesanPDUKirim.length() / 2) + "\15", atDelay);
            sendAtCommand("00" + pesanPDUKirim, atDelay); // Kirim Pesan Format PDU

            sendAtCommand("\032", 100); // Ctrl + Z

            Thread.currentThread().sleep(10000);
        } catch (InterruptedException ex) {
            System.out.println("ATCommand.atSendMessage : " + ex.getMessage());
        }
    }

    /* Method : reject telp.
     */
    private void atRejectPhone() {
        sendAtCommand("ATH0" + "\15", atDelay);
    }

    /* Method : perintah at command, dengan delay-nya.
     *
     */
    public void sendAtCommand(String atCmd, int delay) {
        Boolean isWait = new Boolean(true);
        boolean isDelay = false;
        LoggingWindow.getInstance().addToLog(atCmd, "AT Command");
        System.out.println("AT Command " + atCmd);
        synchronized (isWait) {
            try {
                outStream.write((atCmd).getBytes());
                outStream.flush();
                isWait.wait(delay);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException ex) {
                isDelay = true;
            }
        }
    }

    /* Method : hapus SMS.getSingleton()
     */
    private void atDeleteMessage(int indexMsg) {
        sendAtCommand("AT+CMGD=" + indexMsg + "\15", atDelay);
    }

    /* Method : mengolah output dari perintah AT
     */
    private void receiveAtResponse(String buffer) {
        String[] hasil;
        String[] dataOutbox = new String[2];
        int PDU = 0;
        String respons;
        StringTokenizer st;
        //System.out.println("ATCommand.receiveAtResponse() : ");
        st = new StringTokenizer(buffer, "\r\n");
        while (st.hasMoreTokens()) {
            respons = st.nextToken();
            System.out.println("Respons " + respons);
            if (respons.equals("AT")) {
                INIT_STATUS = 0;
                actConn = nameConn;
            } else if (detectMerk) {
                if (iterasiDetectMerk == 2) {
                    merk = respons;
                    detectMerk = false;
                } else {
                    ++iterasiDetectMerk;
                }
            } else if (detectTipe) {
                if (iterasiDetectTipe == 2) {
                    tipe = respons;
                    detectTipe = false;
                } else {
                    ++iterasiDetectTipe;
                }
            } else if (detectImei) {
                if (iterasiDetectImei == 2) {
                    imei = respons;
                    detectImei = false;
                } else {
                    ++iterasiDetectImei;
                }
            } else if (detectOperator) {
                if (iterasiDetectOperator == 2) {
                    operator = respons;
                    detectOperator = false;
                } else {
                    ++iterasiDetectOperator;
                }
            } else if (respons.startsWith("+CMGS")) { //ini kalau berhasil sending smsnya...

                System.out.println("Mengupdate outbox " + ID_OUTBOX);
                dataOutbox[0] = "sent";
                dataOutbox[1] = "" + ID_OUTBOX;
                SMS.getSingleton().updateStatusOutbox(dataOutbox);
            } else if (respons.startsWith("AT+CGMI")) {
                merk = respons;
            } else if (respons.startsWith("AT+CGMM")) {
                seri = respons;
            } else if (respons.startsWith("+CMS")) {
                Pattern pattern = Pattern.compile(" ");
                hasil = pattern.split(respons.trim());
                int errno = Integer.parseInt(hasil[2].trim());
                if (errno == 321) {
                    System.out.println("Terjadi kesalahan pembacaan memori");
                }
            } else if (respons.startsWith("RING")) {
                atRejectPhone();
            } else if (respons.startsWith("+CMTI:") && !SEND_ONLY) { // --  all "+CMTI 

                Pattern pattern = Pattern.compile(",");
                hasil = pattern.split(respons.trim());
                id = Integer.parseInt(hasil[1].trim()); // -- ini kan panjang pesan, bukan indexnya??? :-\
                //id -= 22;// apa ini u/ nokia aja ya?, atau 23 = 1 desimal

                atReadMessage(id);// ... terus kalau dah ke read, jadi kesini lg, yaitu CMGR...

            } else if (respons.startsWith("+CMGR:")) {
                PDU = 1;  // .. yaitu baca sms dan masukkan ke...

            } else if (respons.startsWith("+CMGL")) {
                Pattern pattern = Pattern.compile(":");
                hasil = pattern.split(respons.trim());
                pattern = Pattern.compile(",");
                hasil = pattern.split(hasil[1].trim());
                id = Integer.parseInt(hasil[0].trim());
                PDU = 1;
            } else if (PDU == 1) { 
                receiveMessage(id, respons.trim());
                PDU = 0;
            }
        }
    }

    /* Method : transaksi saat ada SMS.getSingleton() masuk. Inbox dibaca dengan CMGL / CMGR
     * Method ini dipanggil di atResponse saat CMGR di deteksi, otomatis, karna CNMI
     */
    private void receiveMessage(int index, String Pdu) {
        String noTelp = PDU.getSingleton().pduToText(Pdu)[0];
        String pesan = PDU.getSingleton().pduToText(Pdu)[1];
        if (noTelp.endsWith("F")) {
            noTelp = noTelp.substring(0, noTelp.length() - 1);
        }
        
        String[] dataInbox = new String[3];
        dataInbox[0] = noTelp;
        dataInbox[1] = pesan;
        SMS.getSingleton().insertInbox(dataInbox);
        atDeleteMessage(index);
        if (RESPONSE_MESSAGE) {
            SMS.getSingleton().setIncomingPhoneNumber(noTelp);
            String[] dataOutbox = new String[3]; // TODO : ini kudu dibaikin
            dataOutbox[0] = noTelp;
            dataOutbox[1] = SMS.getSingleton().messageProcessing(pesan); // ini sudah ada no HP! 
            dataOutbox[2] = "p0";
            SMS.getSingleton().insertOutbox(dataOutbox);
        }
    }

    /**
     * ini adalah method yg akan brjln di latar blkg oleh thread
     * digunakan untuk mengirimkan pesan kembali, u/ status belum terkirim.
     * TOFIX hanya boleh dikirim kalau HP sudah konek dunk ya?
     * harapannya nanti ID_OUTBOX yg disini diambil dari DB, akan di update di receiveAtResponse saat ada respons +CGMS
     *  ID_OUTBOX ini akan dibaca saat atReceiveAtRespone dipanggil
     */
    private void sendMessage() {
        String noTelp = null;
        String pesan = null;
        String pecahanPesan = null;

        for (int i = 0; i < SMS.getSingleton().getUnsentOutbox().length; i++) {
            ID_OUTBOX = Integer.parseInt(SMS.getSingleton().getUnsentOutbox()[i][0]);
            System.out.println("Akan mengirim outbox, ID = " + ID_OUTBOX);
            noTelp = SMS.getSingleton().getUnsentOutbox()[i][1];
            pesan = SMS.getSingleton().getUnsentOutbox()[i][2];
            if (pesan.length() > 160) {
                System.out.println("Pesan < 160 karakter, kirim dlm beberapa pecahan");
                do {
                    pecahanPesan = pesan.substring(0, 160);
                    System.out.println("pecahan : " + pecahanPesan);
                    atSendMessage(PDU.getSingleton().pduKirimSms(noTelp, pecahanPesan));
                    pesan = pesan.substring(160);
                } while (pesan.length() > 160);
                if (pesan.length() > 0) {
                    System.out.println("pecahan terakhir :" + pesan);
                    atSendMessage(PDU.getSingleton().pduKirimSms(noTelp, pesan));
                }

            } else {
                System.out.println("Pesan <= 160 karakter, sekali kirim");
                atSendMessage(PDU.getSingleton().pduKirimSms(noTelp, pesan));
            }
        }
        if (timerExecution != null) {
            timerExecution.executeTimer();
        }
    }

    public static void main(String arg[]) {
        DB.USE_MYSQL = false;
        DB.USE_MSSQL = false;
        DB.DB = "dito";
        SMS.getSingleton().buildUnsentOutboxSql("");
        ATCommand.getSingleton().sendMessage();
    }

    public boolean getThreadSendStatus() {
        return threadSend.isRunning();
    }

    /* Ini adalah aksi kirim pesan yang sesungguhnya...
     */
    class ThreadSendMessage extends Timer implements ActionListener {

        public ThreadSendMessage() {
            super(2000, null);
            addActionListener(this);
            this.stop();
        }

        public void actionPerformed(ActionEvent e) {
            sendMessage();     // disini, awal dari segala sesuatanya...               
        }
    }
}
