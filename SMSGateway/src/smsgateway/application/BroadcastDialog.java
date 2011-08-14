
package smsgateway.application;

import core.system.DB;
import core.system.Startup;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/**
 *
 * @author  PRAM WEE
 */
public class BroadcastDialog extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
    private static BroadcastDialog singleton = null;

    public static BroadcastDialog getSingleton() {
        if (singleton == null) {
            singleton = new BroadcastDialog(Startup.getInstance().getMainFrame(), true);
        }
        return singleton;
    }

    /** Creates new form Broadcast */
    public BroadcastDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
    }

    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */
    public int getReturnStatus() {
        return returnStatus;
    }

    private void broadcastMessage() throws HeadlessException {

        String sql;
        String sqlPelanggan="";
        String[][] pelanggan;
        
        if (optPelangganAktif.isSelected()) {
            sqlPelanggan = "select hp from pelanggan where status='Aktif' order by nama";
        } else if (optSemuaPelanggan.isSelected()) {
            sqlPelanggan = "select hp from pelanggan order by nama";
        }else if (optPelangganTidakAktif.isSelected()) {
            sqlPelanggan = "select hp from pelanggan where status='Tidak Aktif' order by nama";
        }

        pelanggan = DB.getInstance().getDataSet(sqlPelanggan, false);

        for (int i = 0; i < pelanggan.length; i++) {
            sql = "insert into sms_outbox(no_telp,pesan) values('" + pelanggan[i][0] + "','" + txtPesanBc.getText() + "')";
            DB.getInstance().executeQuery(sql);
        }
        String pesanKonfirmasi = "";
        if (pelanggan.length == 0) {
            pesanKonfirmasi = "Tidak ada pelanggan yang bisa dikirim pesan broadcast";
        } else {
            pesanKonfirmasi = "Pesan broadcast akan terkirim ke sebanyak " + pelanggan.length + " pelanggan";
        }

        JOptionPane.showMessageDialog(this, pesanKonfirmasi, "Broadcast", JOptionPane.INFORMATION_MESSAGE);
        txtPesanBc.setText("");
        optPelangganAktif.setSelected(true);
        txtPesanBc.requestFocus();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        okButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPesanBc = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        optPelangganAktif = new javax.swing.JRadioButton();
        optSemuaPelanggan = new javax.swing.JRadioButton();
        optPelangganTidakAktif = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setTitle("Broadcast Message");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("Tutup");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Pesan Broadcast");

        txtPesanBc.setColumns(20);
        txtPesanBc.setLineWrap(true);
        txtPesanBc.setRows(5);
        jScrollPane1.setViewportView(txtPesanBc);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Jenis Broadcast"));

        buttonGroup1.add(optPelangganAktif);
        optPelangganAktif.setSelected(true);
        optPelangganAktif.setText("Pelanggan aktif saja");
        optPelangganAktif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optPelangganAktifActionPerformed(evt);
            }
        });

        buttonGroup1.add(optSemuaPelanggan);
        optSemuaPelanggan.setText("Semua pelanggan");
        optSemuaPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optSemuaPelangganActionPerformed(evt);
            }
        });

        buttonGroup1.add(optPelangganTidakAktif);
        optPelangganTidakAktif.setText("Pelanggan tidak aktif");
        optPelangganTidakAktif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optPelangganTidakAktifActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(optPelangganAktif)
                    .addComponent(optPelangganTidakAktif)
                    .addComponent(optSemuaPelanggan))
                .addGap(283, 283, 283))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(optPelangganAktif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optPelangganTidakAktif)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(optSemuaPelanggan)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        jLabel2.setText("Pesan ini akan segera dikirimkan ke semua pelanggan yang dipilih,");

        jLabel3.setText("segera setelah Anda menekan tombol OK");

        jButton1.setText("Kirim");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jButton1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1)))
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-465)/2, (screenSize.height-509)/2, 465, 509);
    }// </editor-fold>//GEN-END:initComponents
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        broadcastMessage();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void optPelangganAktifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optPelangganAktifActionPerformed
        pelangganTertentu(!optPelangganAktif.isSelected());
    }//GEN-LAST:event_optPelangganAktifActionPerformed

    private void optPelangganTidakAktifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optPelangganTidakAktifActionPerformed
        pelangganTertentu(!optPelangganTidakAktif.isSelected());
    }//GEN-LAST:event_optPelangganTidakAktifActionPerformed

    private void optSemuaPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optSemuaPelangganActionPerformed
        pelangganTertentu(!optSemuaPelanggan.isSelected());
    }//GEN-LAST:event_optSemuaPelangganActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                BroadcastDialog dialog = new BroadcastDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton optPelangganAktif;
    private javax.swing.JRadioButton optPelangganTidakAktif;
    private javax.swing.JRadioButton optSemuaPelanggan;
    private javax.swing.JTextArea txtPesanBc;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void pelangganTertentu(boolean b) {
//        tblPelangganTertentu.setEnabled(b);
//        btnTambah.setEnabled(b);
//        btnHapus.setEnabled(b);
    }
}