package smsgateway;

import core.system.LoggingWindow;




/**
 *
 * @author  modify waras
 */
public class PDU {
    private static PDU singleton = null;

    static PDU getSingleton() {
        if(singleton == null) singleton = new PDU();
        return singleton;
    }
    
    /** Creates a new instance of PduDictionary */
    
    private PDU() {
    }
    // untuk membalikkan karakter
    public String reversedString(String karakter) {
        int panjangKarakter = 0;
        StringBuffer stringBuffer = null;
        panjangKarakter = karakter.length();
        stringBuffer = new StringBuffer(panjangKarakter);
        for (int i = 0; (i + 1) < panjangKarakter; i = i + 2) {
            stringBuffer.append(karakter.charAt(i + 1));
            stringBuffer.append(karakter.charAt(i));
        }
        return new String(stringBuffer);
    }
    
    /**
     * Methode rubahKeHexa
     * Keterangan : Konversi nilai Desimal menjadi Hexadesimal
     * @param a : nilai desimal yang akan dikonversi
     * @return : karakter
     */
    public String convertToHexa(int a) {
        char[] hexa = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};
        
        char[]  karakter = new char[2];
        // Mengambil hanya 8 bit 255d = 11111111 b
        a = a & 255;
        // hasil pembagian dengan 16
        karakter[0] = hexa[a / 16];
        // sisa hasil pembagian dengan 16
        karakter[1] = hexa[a % 16];
        
        return new String(karakter);
    }
    
    /**
     * Methode delapanKeTujuhBit
     * Keterangan : Merubah 8 bit menjadi 7 bit (GSM ke ASCII)
     * @param pesan  : pesan SMS yang akan dirubah
     * @param msglen : panjang pesan SMS
     * @return : msg
     */
// Deklarasi variabel
    static char[] gsmToAsciiMap; // GSM ==> ASCII
    
    public String toSevenBit(String pesan, int msglen) {
        int i, o, r = 0, rlen = 0, olen = 0, charcnt = 0;
        StringBuffer msg = new StringBuffer(160);
        int pesanlen = pesan.length();
        String ostr;
        char c;
        
        // pengulangan hingga nilai terpenuhi
        // i + 1 < pesanlen dan charcnt < msglen
        for (i = 0; ( (i + 1) < pesanlen) && (charcnt < msglen); i = i + 2) {
            // mengambil dua digit Hexadesimal
            ostr = pesan.substring(i, i + 2);
            o = Integer.parseInt(ostr, 16);
            // berikan nilai olen = 8
            olen = 8;
            
            // geser posisi semua bit ke kiri sebanyak rlen bit
            o <<= rlen;
            o |= r; // berikan sisa bit dari o ke r
            olen += rlen; // olen = olen + rlen
            
            c = (char) (o & 127); // mendapatkan nilai o menjadi 7 bit
            o >>>= 7; // geser posis bit ke kanan sebanyak 7 bit
            olen -= 7;
            
            r = o; // menaruh sisa bit dari o ke r.
            rlen = olen;
            
            c = gsmToAsciiMap[c]; // rubah ke Text (kode ASCII)
            msg.append(c); // tambahkan ke msg
            charcnt++; // nilai charcnt ditambahkan 1
            
            // jika rlen >= 7
            if (rlen >= 7) {
                c = (char) (r & 127);
                r >>>= 7;
                rlen -= 7;
                msg.append(c);
                charcnt++;
            }
        } // Akhir for
        if ( (rlen > 0) && (charcnt < msglen)) {
            msg.append( (char) r);
        }
        return msg.toString();
    } // Akhir methode delapanKeTujuhBit
    
    
    
// Deklarasi variabel
    static char[] asciiToGsmMap; // ASCII ==> GSM
    
// Awal methode tujuhKeDelapanBit
    public String toEightBit(String pesan) {
        
        StringBuffer msg = new StringBuffer(pesan);
        
        StringBuffer encmsg = new StringBuffer(2 * 160);
        int bb = 0, bblen = 0, i;
        char o = 0, c = 0, tc;
        
        for (i = 0; i < msg.length() || bblen >= 8; i++) {
            if (i < msg.length()) {
                c = msg.charAt(i);
                tc = asciiToGsmMap[c];
                c = tc;
                c &= ~ (1 << 7);
                bb |= (c << bblen);
                bblen += 7;
            }
            while (bblen >= 8) {
                o = (char) (bb & 255);
                encmsg.append(convertToHexa(o));
                bb >>>= 8;
                bblen -= 8;
            }
        }
        if ( (bblen > 0)) {
            encmsg.append(convertToHexa(bb));
        }
        return encmsg.toString();
    } // Akhir methode tujuhKeDelapanBit
    
    static {
        final int lastindex = 255;
        gsmToAsciiMap = new char[lastindex + 1];
        asciiToGsmMap = new char[lastindex + 1];
        int i;
        
        for (i = 0; i <= lastindex; i++) {
            gsmToAsciiMap[i] = asciiToGsmMap[i] = (char) i;
        }
    } // Akhir static
    
    // Deklarasi variabel
    
// Awal methode PduTerimaSms
    public String[] pduToText(String smspdu) {
        String dapatNotlp = null;
        String pesan = null;
        String infoSmsc = null;
        int nilaiSmsc = 0;
        int nomorSmsc = 0;
        String panjangNotlp = null;
        int nilaiPanjangNotlp = 0;
        int nilaiNotlp = 0;
        String Notlp = null;
        String panjangPesan = null;
        int nilaiPanjangPesan = 0;
        String pesanPDU = null;
        int i = 0;
        try {
            // Mengambil nilai panjang informasi SMSC
            infoSmsc = smspdu.substring(i, 2);
            nilaiSmsc = Integer.parseInt(infoSmsc, 16);
            // format nomor dan nomor MSC dibuang
            i = i + 4;
            nomorSmsc = i + (nilaiSmsc * 2) - 2;
            // Nilai PDU Type dibuang
            i = nomorSmsc + 2;
            // Mengambil Panjang Nomor Telepon Pengirim
            panjangNotlp = smspdu.substring(i, i + 2);
            nilaiPanjangNotlp = Integer.parseInt(panjangNotlp, 16);
            // format nomor pengirim dibuang
            i = i + 4;
            nilaiNotlp = i + nilaiPanjangNotlp + nilaiPanjangNotlp % 2;
            // Nomor telepon pengirim
            Notlp = smspdu.substring(i, nilaiNotlp);
            dapatNotlp = reversedString(Notlp);
            i = nilaiNotlp;
            // Nilai PID, DCS, dan SCTS dibuang
            i = i + 18;
            // Mengambil Panjang Pesan SMS
            panjangPesan = smspdu.substring(i, i + 2);
            nilaiPanjangPesan = Integer.parseInt(panjangPesan, 16);
            i = i + 2;
            pesanPDU = smspdu.substring(i, smspdu.length());
            pesan = toSevenBit(pesanPDU, nilaiPanjangPesan);
        } catch (Exception e) {
            
        }
        return new String[]{dapatNotlp,pesan};
    }
    
    
    /**
     * Methode PduKirimSms
     * */
    public String pduKirimSms(String notlp, String pesan) {
        LoggingWindow.getInstance().addToLog("mengirim pdu");
        StringBuffer pesanPDUKirim = null;
        int panjangNotlpTujuan = 0;
        int panjangPesanKirim = 0;
        String PduPesan = null;
        pesanPDUKirim = new StringBuffer(320); // 320 = 160 * 2 (panjang max)
        // Tambahkan nilai PDU Type ---> Default = 11
        pesanPDUKirim.append("11");
        // Tambahkan nilai MR ---> Default = 00
        pesanPDUKirim.append("00");
        // Tambahkan nilai panjang nomor pengirim
        panjangNotlpTujuan = notlp.length();
        
        pesanPDUKirim.append(convertToHexa(panjangNotlpTujuan));
        // Tambah nilai format no. telepon --> format internasional = 91
        pesanPDUKirim.append("91");
        // Tambahkan nilai nomor telepon pengirim
        // Jika panjang notlp adalah ganjil
        if ( (notlp.length() % 2) == 1) {
            notlp = reversedString(notlp + "F");
        }
        // Jika panjang notlp adalah genap
        else {
            notlp = reversedString(notlp);
        }
        pesanPDUKirim.append(notlp);
        // tambahkan nilai PID ---> Default = 00
        pesanPDUKirim.append("00");
        // tambahkan nilai DCS ---> Default = 00
        pesanPDUKirim.append("00");
        // tambahkan nilai VP = 4 hari ---> AA h
        pesanPDUKirim.append("AA");
        panjangPesanKirim = pesan.length();
        PduPesan = toEightBit(pesan);
        pesanPDUKirim.append(convertToHexa(panjangPesanKirim));
        pesanPDUKirim.append(PduPesan);
        
        return new String(pesanPDUKirim);
    }
    
}
